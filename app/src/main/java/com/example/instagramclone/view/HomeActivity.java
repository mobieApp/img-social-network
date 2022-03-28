package com.example.instagramclone.view;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.R;
import com.example.instagramclone.Utils.BottomNavigationViewHolder;
import com.example.instagramclone.Utils.PostAdapter;
import com.example.instagramclone.Utils.UserAuthentication;
import com.example.instagramclone.models.Post;
import com.example.instagramclone.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class HomeActivity extends AppCompatActivity{
    private static final int ACTIVITY_NUM = 0;
    private ArrayList<Post> postArrayList;
    private ProgressBar progressBar;
    //private StorageReference mStorageReference;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference postCollection = firestore.collection("Post");
    private CollectionReference userCollection = firestore.collection("User");
    private DocumentReference documentReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firestore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.LoadingFeed);
        postArrayList = new ArrayList<>();

        /*mStorageReference = FirebaseStorage.getInstance().getReference().child("Post");
        Log.d("AAA", mStorageReference.toString());
        mStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("AAA", "onSuccess: " + uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("AAA", "onFailure");
            }
        });*/

        getPostData();
        setNavView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        UserAuthentication.UserExists();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setNavView();
    }

    private void setNavView(){
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHolder.activateNavigationView(this,bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private void getPostData(){
        userCollection.document(UserAuthentication.userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                Collections.sort(user.getReact());

                ArrayList<String> userId = new ArrayList<>();
                if (user.getReact().size() > 5){
                    for(int i = 0; i < 5; i++){
                        userId.add(user.getReact().get(i).getUserId());
                    }
                }
                else {
                    for(int i = 0; i < user.getReact().size(); i++){
                        userId.add(user.getReact().get(i).getUserId());
                    }
                }
                userId.add(0,UserAuthentication.userId);
                //Log.d("AAA", "onSuccess: Reacts => " + userIdSort.toString());

                postCollection.whereIn("userId",userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isComplete()){
                            Map<Integer,Post> unsortPost = new HashMap<>();
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                Post post = document.toObject(Post.class);
                                post.setId(document.getId());
                                unsortPost.put(userId.indexOf(post.getUserId()),post);
                                //postArrayList.add(post);
                            }

                            //Sort Map by priority
                            Map<Integer,Post> treeMap = new TreeMap<>(new Comparator<Integer>() {
                                @Override
                                public int compare(Integer t1, Integer t2) {
                                    return t1.compareTo(t2);
                                }
                            });

                            treeMap.putAll(unsortPost);
                            for(Post post: treeMap.values()){
                                postArrayList.add(post);
                            }

                            PostAdapter adapter = new PostAdapter(postArrayList,HomeActivity.this);
                            RecyclerView view = findViewById(R.id.RVInstaFeed);

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this, RecyclerView.VERTICAL, false);

                            view.setLayoutManager(linearLayoutManager);

                            view.setAdapter(adapter);
                        } else {
                            Log.d("AAA", "Error getting documents", task.getException());
                        }
                    }
                });
            }
        });
    }
}
