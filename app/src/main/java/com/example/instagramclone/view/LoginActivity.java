package com.example.instagramclone.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.instagramclone.R;
import com.example.instagramclone.Utils.UserAuthentication;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    TextView linkCreate;
    Button loginBtn;
    EditText email,password;
    FirebaseAuth mAuth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        linkCreate = (TextView) findViewById(R.id.createAccount);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        email = (EditText) findViewById(R.id.Email);
        password = (EditText) findViewById(R.id.Password);
        mAuth = FirebaseAuth.getInstance();
        linkCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        if (UserAuthentication.UserExists()){
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString().trim();
                String Pass = password.getText().toString().trim();
                if (Email.isEmpty()){
                    email.setError("Email is empty");
                    email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    email.setError("Enter the valid email");
                    email.requestFocus();
                    return;
                }
                if (Pass.isEmpty()){
                    password.setError("Password is empty");
                    password.requestFocus();
                    return;
                }

                mAuth.signInWithEmailAndPassword(Email,Pass).addOnCompleteListener(task -> {
                   if(task.isSuccessful()){
                       startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                   }
                   else{
                       Toast.makeText(LoginActivity.this,"Please Check Your Login Credentials",Toast.LENGTH_SHORT).show();
                   }
                });
            }
        });
    }
}
