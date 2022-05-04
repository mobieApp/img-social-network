package com.example.instagramclone.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instagramclone.R;
import com.example.instagramclone.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class RegisterActivity extends AppCompatActivity {
    private User user;
    private TextView linkLogin;
    private EditText username,email,password;
    private Button regBtn;
    private CheckBox showPassBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        linkLogin = (TextView) findViewById(R.id.linkLogin);
        username = (EditText) findViewById(R.id.Username);
        email = (EditText) findViewById(R.id.Email);
        password = (EditText) findViewById(R.id.Password);
        regBtn = (Button) findViewById(R.id.registerBtn);
        showPassBtn = (CheckBox) findViewById(R.id.showPassword);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        showPassBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (showPassBtn.isChecked()){
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = username.getText().toString();
                String Email = email.getText().toString().trim();
                String Pass = password.getText().toString().trim();
                String HashPassword = new String();
                if(Username.isEmpty()){
                    username.setError("Fullname is empty");
                    username.requestFocus();
                    return;
                }
                if(Email.isEmpty())
                {
                    email.setError("Email is empty");
                    email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
                {
                    email.setError("Enter the valid email address");
                    email.requestFocus();
                    return;
                }
                if(Pass.isEmpty())
                {
                    password.setError("Enter the password");
                    password.requestFocus();
                    return;
                }
                if(Pass.length() < 6)
                {
                    password.setError("Your password has less than 6 characters");
                    password.requestFocus();
                    return;
                }

                user = new User();
                user.setUsername(Username);
                user.setEmail(Email);
                HashPassword = BCrypt.withDefaults().hashToString(12,Pass.toCharArray());
                user.setPassword(HashPassword);
                StorageReference ref = FirebaseStorage.getInstance().getReference().child("Avatar/defaultAvatar.jpg");
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        user.setAvatar(uri.toString());
                    }
                });
                mAuth.createUserWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this,"You are successfully Registered", Toast.LENGTH_SHORT).show();
                            String id = mAuth.getCurrentUser().getUid();
                            DocumentReference docRefUser = firestore.collection("User").document(id);
                            docRefUser.set(user);

                            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                        }
                        else{
                            Toast.makeText(RegisterActivity.this,"You are not successfully Registered", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
