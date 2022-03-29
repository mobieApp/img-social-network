package com.example.instagramclone.view;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.instagramclone.R;

public class CommentActivity extends FragmentActivity {
    Bundle saveBundle;
    CommentFragment commentFragment;
    FragmentTransaction ft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        saveBundle = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ImageView imageView = findViewById(R.id.backArrow);

        ft = getSupportFragmentManager().beginTransaction();
        commentFragment = new CommentFragment();
        ft.replace(R.id.frameLayoutHome, commentFragment);
        ft.commit();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public CommentActivity() {
    }
}