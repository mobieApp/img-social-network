package com.example.instagramclone.controller;

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
import com.example.instagramclone.Utils.RandomString;
import com.example.instagramclone.models.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    private TextView linkLogin;
    private EditText fullname,email,password;
    private Button regBtn;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        linkLogin = (TextView) findViewById(R.id.linkLogin);
        fullname = (EditText) findViewById(R.id.Fullname);
        email = (EditText) findViewById(R.id.Email);
        password = (EditText) findViewById(R.id.Password);
        regBtn = (Button) findViewById(R.id.registerBtn);
        mAuth = FirebaseAuth.getInstance();
        Account account= new Account();
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
                String Name = fullname.getText().toString();
                String Email = email.getText().toString().trim();
                String Pass = password.getText().toString().trim();
                if(Name.isEmpty()){
                    fullname.setError("Fullname is empty");
                    fullname.requestFocus();
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
                account.setName(Name);
                account.setEmail(Email);
                account.setPassword(Pass);

                mAuth.createUserWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this,"You are successfully Registered", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
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
