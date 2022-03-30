package com.example.instagramclone.view;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.instagramclone.R;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_newpost_confirm);
        ImageView imgPost = findViewById(R.id.ivPhoto);
        ImageView backBtn = findViewById(R.id.backBtn);
        ImageView postBtn = findViewById(R.id.postDone);
        EditText caption = findViewById(R.id.caption);

        byte[] arr = getIntent().getExtras().getByteArray("IMG");
        Bitmap bmp = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        imgPost.setImageBitmap(bmp);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PostActivity.this, caption.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}