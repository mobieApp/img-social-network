package com.example.instagramclone.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.instagramclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditPersonalProfile extends AppCompatActivity {
    FirebaseUser user;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    private EditText email, phone;
    private RadioGroup radioGroup;
    RadioButton male, female, notSay;
    private DatePickerDialog datePickerDialog;
    private ImageView btnCancel, btnSave;
    private Button dateButton;

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
        dateButton = (Button) findViewById(R.id.birthdayBtn);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });

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
                        initDatePicker(Integer.parseInt(tmp[2]), Integer.parseInt(tmp[1]) - 1, Integer.parseInt(tmp[0]));
                    }
                    else initDatePicker(0,0,0);
                    dateButton.setText(date);

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
                edited.put("dob", dateButton.getText());
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
    private void initDatePicker(int FirstYear, int FirstMonth, int FirstDay){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day,month,year);
                dateButton.setText(date);
            }
        };
        if (FirstDay == 0 || FirstMonth == 0 || FirstYear == 0){
            Calendar cal = Calendar.getInstance();
            FirstDay = cal.get(Calendar.YEAR);
            FirstMonth = cal.get(Calendar.MONTH);
            FirstYear = cal.get(Calendar.DAY_OF_MONTH);
        }
        datePickerDialog = new DatePickerDialog(this,dateSetListener,FirstYear,FirstMonth,FirstDay);
    }

    private String makeDateString(int day, int month, int year){
        return day + "/" + month + "/" + year;
    }
    public void openDatePicker(){
        datePickerDialog.show();
    }
}