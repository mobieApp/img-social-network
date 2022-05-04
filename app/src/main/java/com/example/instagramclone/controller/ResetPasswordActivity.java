package com.example.instagramclone.controller;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instagramclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private TextView resetPasswordInput;
    private Button sendEmailResetPassBtn;
    private FirebaseAuth mAuth;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        resetPasswordInput = (TextView) findViewById(R.id.resetEmail);
        sendEmailResetPassBtn = (Button) findViewById(R.id.sendBtn);

        mAuth = FirebaseAuth.getInstance();
        sendEmailResetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = resetPasswordInput.getText().toString();
                if (TextUtils.isEmpty(userEmail)){
                    Toast.makeText(ResetPasswordActivity.this,"Please write your valid email first...",Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()){
                                Toast.makeText(ResetPasswordActivity.this,"Plaese Check Your Email...", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                Log.e("ResetPasswordActivity", "onComplete: Error Occured " + task.getException().getMessage());
                            }
                        }
                    });
                }
            }
        });
    }
}
