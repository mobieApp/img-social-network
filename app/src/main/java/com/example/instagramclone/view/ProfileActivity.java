package com.example.instagramclone.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.instagramclone.R;
import com.example.instagramclone.Utils.BottomNavigationViewHolder;
import com.example.instagramclone.Utils.UserAdapter;
import com.example.instagramclone.Utils.UserAuthentication;
import com.example.instagramclone.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 4;
    private static final String PATH = "User";
    private FirebaseFirestore firestore;
    private TextView usernameToolbar, display_name, description, website, btnEditProfile, numberPost1, numberFollower1, numberFollowing1;
    private TextView numberPost,numberFollower, numberFollowing, btnFollow;
    private LinearLayout layoutFollower1, layoutFollowing1, layoutFollower, layoutFollowing;
    private CircleImageView imageView;
    private String userId;
    private ConstraintLayout profileView, profileInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        usernameToolbar = (TextView) findViewById(R.id.toolbarUserName);
        display_name = (TextView) findViewById(R.id.display_name);
        description = (TextView) findViewById(R.id.description);
        website = (TextView) findViewById(R.id.website);
        imageView = (CircleImageView) findViewById(R.id.userAvatar);
        profileView = findViewById(R.id.profileView);
        profileInfo = findViewById(R.id.profileInfo);

        //------Set up toolbar---------
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);
        //------Get user from firebase------
        Intent intent = getIntent();
        if (intent.hasExtra("userId")){
            userId = intent.getStringExtra("userId");
        }
        else userId = UserAuthentication.userId;
        Log.d(TAG, "onCreate: " + userId);
        setUserInfo();
    }

    private void setUserInfo(){
        firestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firestore.collection("User").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Log.e(TAG, "onEvent: Listen failed - " + error);
                    return;
                }
                if (value != null && value.exists()){
                    User userAccountSetting = value.toObject(User.class);

                    //Searched User Profile
                    if(userId != UserAuthentication.userId){
                        profileView.setVisibility(View.VISIBLE);
                        profileInfo.setVisibility(View.GONE);

                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_backarrow);
                        BindViewSearchedProfile();

                        numberPost.setText(userAccountSetting.getPosts().toString());
                        numberFollower.setText(userAccountSetting.NumberFollower().toString());
                        numberFollowing.setText(userAccountSetting.NumberFollowing().toString());
                        setNavView(1);
                    }
                    //User Profile Info
                    else{
                        profileView.setVisibility(View.GONE);
                        profileInfo.setVisibility(View.VISIBLE);

                        BindViewProfileInfo();

                        numberPost1.setText(userAccountSetting.getPosts().toString());
                        numberFollower1.setText(userAccountSetting.NumberFollower().toString());
                        numberFollowing1.setText(userAccountSetting.NumberFollowing().toString());
                        setNavView(ACTIVITY_NUM);
                    }
                    usernameToolbar.setText(userAccountSetting.getUsername());
                    display_name.setText(userAccountSetting.getName());
                    description.setText(userAccountSetting.getStory());
                    website.setText(userAccountSetting.getWebsite());
                    Picasso.get().load(userAccountSetting.getAvatar()).into(imageView);
                }else{
                    Log.d(TAG, "onEvent: Data null");
                }
            }
        });
    }

    private void BindViewProfileInfo(){
        numberPost1 = (TextView) findViewById(R.id.numberPost1);
        numberFollower1 = (TextView) findViewById(R.id.numberFollower1);
        numberFollowing1 = (TextView) findViewById(R.id.numberFollowing1);
        layoutFollower1 = (LinearLayout) findViewById(R.id.btnFollowers1);
        layoutFollowing1 = (LinearLayout) findViewById(R.id.btnFollowing1);
        btnEditProfile = (TextView) findViewById(R.id.btnEditProfile);

        layoutFollower1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(numberFollower1.getText().toString()) != 0) {
                    Intent intent = new Intent(ProfileActivity.this, FollowActivity.class);
                    intent.putExtra("nameActivity", "Follower");
                    startActivity(intent);
                }
            }
        });

        layoutFollowing1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(numberFollowing1.getText().toString()) != 0) {
                    Intent intent = new Intent(ProfileActivity.this, FollowActivity.class);
                    intent.putExtra("nameActivity", "Following");
                    startActivity(intent);
                }
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable img = imageView.getDrawable();
                Bitmap bitmap = ((BitmapDrawable) img).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] imgData = stream.toByteArray();

                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("username",usernameToolbar.getText().toString());
                intent.putExtra("name",display_name.getText().toString());
                intent.putExtra("description",description.getText().toString());
                intent.putExtra("website",website.getText().toString());
                intent.putExtra("avatar",imgData);
                startActivity(intent);
            }
        });
    }

    private void BindViewSearchedProfile(){
        numberPost = (TextView) findViewById(R.id.numberPost);
        numberFollower = (TextView) findViewById(R.id.numberFollower);
        numberFollowing = (TextView) findViewById(R.id.numberFollowing);
        layoutFollower = (LinearLayout) findViewById(R.id.btnFollowers);
        layoutFollowing = (LinearLayout) findViewById(R.id.btnFollowing);
        btnFollow = (TextView) findViewById(R.id.btnFollow);

        layoutFollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(numberFollower.getText().toString()) != 0) {
                    Intent intent = new Intent(ProfileActivity.this, FollowActivity.class);
                    intent.putExtra("nameActivity", "Follower");
                    startActivity(intent);
                }
            }
        });

        layoutFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(numberFollowing.getText().toString()) != 0) {
                    Intent intent = new Intent(ProfileActivity.this, FollowActivity.class);
                    intent.putExtra("nameActivity", "Following");
                    startActivity(intent);
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

    private void setNavView(int item){
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHolder.activateNavigationView(this,bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(item);
        menuItem.setChecked(true);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setNavView(ACTIVITY_NUM);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
