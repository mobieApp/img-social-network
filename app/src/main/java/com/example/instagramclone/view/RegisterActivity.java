package com.example.instagramclone.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instagramclone.R;
import com.example.instagramclone.models.User;
import com.example.instagramclone.models.UserAccountSetting;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private User user;
    private UserAccountSetting setting;
    private TextView linkLogin;
    private EditText username,email,password;
    private Button regBtn;
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

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = username.getText().toString();
                String Email = email.getText().toString().trim();
                String Pass = password.getText().toString().trim();
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

                user = new User(Email,Pass);
                setting = new UserAccountSetting();
                setting.setUsername(Username);

                mAuth.createUserWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this,"You are successfully Registered", Toast.LENGTH_SHORT).show();
                            user.setId(mAuth.getCurrentUser().getUid());
                            DocumentReference docRefUser = firestore.collection("User").document(user.getId());
                            docRefUser.set(user);

                            DocumentReference docRefSetting = firestore.collection("UserAccountSetting").document(user.getId());
                            docRefSetting.set(setting);
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