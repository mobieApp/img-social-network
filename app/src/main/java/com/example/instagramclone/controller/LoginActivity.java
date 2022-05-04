package com.example.instagramclone.controller;

import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;

import com.example.instagramclone.R;
import com.example.instagramclone.Utils.UserAuthentication;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    private TextView linkCreate, forgetPassword;
    private Button loginBtn;
    private EditText email,password;
    private FirebaseAuth mAuth;
    private CheckBox showPassBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        linkCreate = (TextView) findViewById(R.id.createAccount);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        email = (EditText) findViewById(R.id.Email);
        password = (EditText) findViewById(R.id.Password);
        forgetPassword = (TextView) findViewById(R.id.forgetPass);
        showPassBtn = (CheckBox) findViewById(R.id.showPassword);

        mAuth = FirebaseAuth.getInstance();
        linkCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
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
        if (UserAuthentication.UserExists()){
            Intent intent1 = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent1);
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
//                check decode password

                mAuth.signInWithEmailAndPassword(Email,Pass).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"Please Check Your Login Credentials",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class));
            }
        });
    }
}