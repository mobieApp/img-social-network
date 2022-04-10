package com.example.instagramclone.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.instagramclone.R;
import com.example.instagramclone.Utils.PostAdapter;
import com.example.instagramclone.models.Post;

import java.util.ArrayList;

public class PostProfileActivity extends AppCompatActivity {
    private ArrayList<Post> postArrayList;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_profile);
        setSupportActionBar(findViewById(R.id.profileToolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_backarrow);


        progressBar = (ProgressBar) findViewById(R.id.LoadingFeed2);
        recyclerView = (RecyclerView) findViewById(R.id.rcv_post_profile);

        TextView usernameToolbar = (TextView) findViewById(R.id.toolbarUserName);

        Bundle bundle = getIntent().getExtras();
        int pos = bundle.getInt("pos");
        String username = bundle.getString("username");
        ArrayList<Post> list = (ArrayList<Post>) bundle.getSerializable("PostList");
        PostAdapter adapter = new PostAdapter(list,PostProfileActivity.this);

        usernameToolbar.setText(username);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(null, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(pos);

    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
