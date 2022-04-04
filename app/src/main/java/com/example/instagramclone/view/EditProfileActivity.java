package com.example.instagramclone.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.instagramclone.R;
import com.example.instagramclone.Utils.UserAuthentication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends Activity {
    private static final String TAG = "Edit_Profile_ACTIVITY";
    private EditText fullnameField, usernameField, websiteField, descriptionField;
    private ImageView btnSaveEdit, btnCancelEdit;
    private TextView textView, btnChangeAvatar;
    private CircleImageView circleImageView;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_edit_settings);

        fullnameField = (EditText) findViewById(R.id.fullname);
        usernameField = (EditText) findViewById(R.id.username);
        websiteField = (EditText) findViewById(R.id.website);
        descriptionField = (EditText) findViewById(R.id.description);
        textView = (TextView) findViewById(R.id.personalInfo);

        btnSaveEdit = (ImageView) findViewById(R.id.btnSaveEdit);
        btnCancelEdit = (ImageView) findViewById(R.id.btnCancelEdit);
        circleImageView = (CircleImageView) findViewById(R.id.circleImageView);
        btnChangeAvatar = (TextView) findViewById(R.id.btnChangeAvatar);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String fullname = intent.getStringExtra("name");
        String website = intent.getStringExtra("website");
        String description = intent.getStringExtra("description");
        byte[] imgData = intent.getByteArrayExtra("avatar");
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgData,0, imgData.length);

        fullnameField.setText(fullname);
        usernameField.setText(username);
        websiteField.setText(website);
        descriptionField.setText(description);
        circleImageView.setImageBitmap(bitmap);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = firestore.collection("User").document(UserAuthentication.userId);

        btnSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usernameField.getText().toString().isEmpty()) {
                    usernameField.setError("Username is empty");
                    return;
                }
                Map<String, Object> edited = new HashMap<>();
                edited.put("username", usernameField.getText().toString());
                edited.put("name", fullnameField.getText().toString());
                edited.put("website", websiteField.getText().toString());
                edited.put("story", descriptionField.getText().toString());
                documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Profile update success");
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        finish();
                    }
                });
            }
        });
        btnCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditPersonalProfile.class));
            }
        });

        btnChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),NewPostActivity.class);
                intent.putExtra("Action","Change Avatar");
                startActivity(intent);
            }
        });
    }
}
