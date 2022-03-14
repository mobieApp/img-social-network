package com.example.instagramclone.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.instagramclone.R;
import com.example.instagramclone.Utils.BottomNavigationViewHolder;
import com.example.instagramclone.Utils.UserAuthentication;
import com.example.instagramclone.models.UserAccountSetting;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
    private static final int ACTIVITY_NUM = 4;
    private static final String PATH = "UserAccountSetting";
    private FirebaseFirestore firestore;
    private TextView usernameToolbar, display_name, description, website, btnEditProfile, numberPost, numberFollower, numberFollowing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        usernameToolbar = (TextView) findViewById(R.id.toolbarUserName);
        display_name = (TextView) findViewById(R.id.display_name);
        description = (TextView) findViewById(R.id.description);
        website = (TextView) findViewById(R.id.website);
        btnEditProfile = (TextView) findViewById(R.id.btnEditProfile);
        numberPost = (TextView) findViewById(R.id.numberPost);
        numberFollower = (TextView) findViewById(R.id.numberFollower);
        numberFollowing = (TextView) findViewById(R.id.numberFollowing);
        setNavView();

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("username",usernameToolbar.getText().toString());
                intent.putExtra("name",display_name.getText().toString());
                intent.putExtra("description",description.getText().toString());
                intent.putExtra("website",website.getText().toString());
                startActivity(intent);
            }
        });

        //------Set up toolbar---------
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);
        //------Get user from firebase------
        setUserInfo();
    }

    private void setUserInfo(){
        firestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firestore.collection(PATH).document(UserAuthentication.userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    UserAccountSetting userAccountSetting = documentSnapshot.toObject(UserAccountSetting.class);
                    usernameToolbar.setText(userAccountSetting.getUsername());
                    display_name.setText(userAccountSetting.getName());
                    description.setText(userAccountSetting.getStory());
                    website.setText(userAccountSetting.getWebsite());
                    numberPost.setText(userAccountSetting.getNumberPosts().toString());
                    numberFollower.setText(userAccountSetting.getNumberFollower().toString());
                    numberFollowing.setText(userAccountSetting.getNumberFollowing().toString());
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.signOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setNavView(){
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHolder.activateNavigationView(this,bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

}
