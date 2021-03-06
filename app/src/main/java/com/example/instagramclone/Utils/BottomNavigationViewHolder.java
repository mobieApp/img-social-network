package com.example.instagramclone.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.instagramclone.R;
import com.example.instagramclone.controller.HomeActivity;
import com.example.instagramclone.controller.NewPostActivity;
import com.example.instagramclone.controller.NotificationActivity;
import com.example.instagramclone.controller.ProfileActivity;
import com.example.instagramclone.controller.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class BottomNavigationViewHolder {
    public static void activateNavigationView(Context context, BottomNavigationView bottomNavigationView){
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_user:
                        Intent intent = new Intent(context, ProfileActivity.class);
                        context.startActivity(intent);
                        break;
                    case R.id.ic_home:
                        Intent intent1 = new Intent(context, HomeActivity.class);
                        context.startActivity(intent1);
                        break;
                    case R.id.ic_newpost:
                        Intent intent2 = new Intent(context, NewPostActivity.class);
                        context.startActivity(intent2);
                        break;
                    case R.id.ic_search:
                        Intent intent3 = new Intent(context, SearchActivity.class);
                        context.startActivity(intent3);
                        break;
                    case R.id.ic_alert:
                        Intent intent4 = new Intent(context, NotificationActivity.class);
                        context.startActivity(intent4);
                        break;
                }
                return true;
            }
        });
    }
}
