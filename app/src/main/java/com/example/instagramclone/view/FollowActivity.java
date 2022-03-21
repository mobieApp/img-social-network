package com.example.instagramclone.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.instagramclone.R;
import com.example.instagramclone.Utils.FollowAdapter;
import com.example.instagramclone.Utils.UserAuthentication;
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

        String nameActivity = getIntent().getStringExtra("nameActivity");
        getSupportActionBar().setTitle(nameActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firestore = FirebaseFirestore.getInstance();
        documentReference = firestore.collection("User").document(UserAuthentication.userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                ArrayList<String> listFollow = null;
                if (nameActivity.equals("Following"))
                    listFollow = user.getFollowing();
                else
                    listFollow = user.getFollower();
                FollowAdapter adapter = new FollowAdapter(FollowActivity.this, listFollow);
                listView.setAdapter(adapter);
            }
        });
    }
}
