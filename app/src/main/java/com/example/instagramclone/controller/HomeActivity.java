package com.example.instagramclone.controller;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity{
    private static final int ACTIVITY_NUM = 0;
    private ArrayList<Post> postArrayList;
    private ProgressBar progressBar;
    //private StorageReference mStorageReference;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference postCollection = firestore.collection("Post");
    private CollectionReference userCollection = firestore.collection("User");
    private DocumentReference documentReference;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firestore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.LoadingFeed);
        postArrayList = new ArrayList<>();

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
    public Map<Post, Integer> sort(Map<Post, Integer> map) {
        List<Map.Entry<Post, Integer>> list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Post, Integer>>() {
            @Override
            public int compare(Map.Entry<Post, Integer> o1, Map.Entry<Post, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        Map<Post, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<Post, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    private void getPostData(){
        userCollection.document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
                // userId.add(0,UserAuthentication.userId);
                // Log.d("AAA", "onSuccess: Reacts => " + userIdSort.toString());

                postCollection.whereIn("userId",userId).orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isComplete()){
                            Map<Post,Integer> unsortPost = new HashMap<>();
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                Post post = document.toObject(Post.class);
                                unsortPost.put(post,userId.indexOf(post.getUserId()));
                                //postArrayList.add(post);
                            }

                            //Sort Map by priority
                            Map<Post,Integer> sortedPost = sort(unsortPost);
                            for(Post post: sortedPost.keySet()){
//                                Log.d("TAG", "Current user "+UserAuthentication.userId);
                                if(post.getIsHide().contains(UserAuthentication.userId) || post.getReports().size() >5) continue;
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