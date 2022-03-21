package com.example.instagramclone.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.instagramclone.R;
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

public class EditPersonalProfile extends AppCompatActivity {
    FirebaseUser user;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    private EditText email, phone;
    private RadioGroup radioGroup;
    RadioButton male, female, notSay;
    private DatePicker datePicker;
    private ImageView btnCancel, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_edit_personal_settings);

        email = (EditText) findViewById(R.id.email);
        email.setEnabled(false);
        phone = (EditText) findViewById(R.id.phone);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        notSay = (RadioButton) findViewById(R.id.notToSay);
        btnCancel = (ImageView) findViewById(R.id.btnCancelEdit);
        btnSave = (ImageView) findViewById(R.id.btnSaveEdit);
        datePicker = (DatePicker) findViewById(R.id.birthday);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = firebaseAuth.getCurrentUser();
        DocumentReference documentReference = firestore.collection("User").document(user.getUid());

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    email.setText(document.getString("email"));
                    phone.setText(document.getString("phone"));
                    String gender = document.getString("gender");
                    String date = document.getString("dob");
                    if(date.contains("/")) {
                        String[] tmp = date.split("/");
                        datePicker.updateDate(Integer.parseInt(tmp[2]), Integer.parseInt(tmp[1]) - 1, Integer.parseInt(tmp[0]));
                    }

                    if (gender.equals("male"))
                        male.setChecked(true);
                    else if (gender.equals("female"))
                        female.setChecked(true);


                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty()) {
                    email.setError("Email field is empty!");
                    return;
                }
                Map<String, Object> edited = new HashMap<>();
                edited.put("email", email.getText().toString());
                edited.put("phone", phone.getText().toString());
                int id = radioGroup.getCheckedRadioButtonId();
                String gender = "Prefer not to say";
                if (male.getId() == id)
                    gender = "male";
                else if (female.getId() == id)
                    gender = "female";
                edited.put("gender", gender);
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();
                edited.put("dob", day + "/" + month + "/" + year);
                documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG", "Profile update success");
                        finish();
                    }
                });
            }
        });

    }
}