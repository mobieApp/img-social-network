package com.example.instagramclone.controller;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.instagramclone.R;
import com.example.instagramclone.Utils.FollowAdapter;
import com.example.instagramclone.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FollowActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listView;
    private FirebaseFirestore firestore;
    private DocumentReference documentReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        listView = (ListView) findViewById(R.id.listFollowing);
        toolbar = (Toolbar) findViewById(R.id.toolbarFollowing);
        setSupportActionBar(toolbar);

        String[] values = getIntent().getStringExtra("nameActivity").split(" ");

        getSupportActionBar().setTitle(values[0]);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        Log.d("userid", "onCreate: "+values[1]);

        firestore = FirebaseFirestore.getInstance();
        documentReference = firestore.collection("User").document(values[1]);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                ArrayList<String> listFollow = null;
                if (values[0].equals("Following"))
                    listFollow = user.getFollowing();
                else
                    listFollow = user.getFollower();
                ArrayList<User> userFollowing = new ArrayList<>();
                for (String id : listFollow) {
                    documentReference = firestore.collection("User").document(id);
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            userFollowing.add(documentSnapshot.toObject(User.class));
                            FollowAdapter adapter = new FollowAdapter(FollowActivity.this, userFollowing);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
