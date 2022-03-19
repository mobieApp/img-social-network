package com.example.instagramclone.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.instagramclone.R;
import com.example.instagramclone.Utils.EditPhotoAdapter;

public class EditPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_photo);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewEdit);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setAdapter(new EditPhotoAdapter());

        recyclerView.setLayoutManager(linearLayoutManager);
    }
}