package com.example.instagramclone.controller;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.instagramclone.R;
import com.example.instagramclone.view.FragmentNewFeed;
import com.example.instagramclone.view.FragmentProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.ic_home);
    }

    FragmentNewFeed fragmentNewFeed = new FragmentNewFeed();
    FragmentProfile fragmentProfile = new FragmentProfile();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.ic_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentNewFeed).commit();
                return true;
            case R.id.ic_user:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragmentProfile).commit();
                return true;
        }
        return false;
    }
}
