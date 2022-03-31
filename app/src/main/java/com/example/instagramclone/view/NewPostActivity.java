package com.example.instagramclone.view;

import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.instagramclone.R;
import com.example.instagramclone.Utils.BottomNavigationViewHolder;
import com.example.instagramclone.Utils.NewPostViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class NewPostActivity extends AppCompatActivity {
    private ImageButton btnBack, btnContinue;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    private final String galleryPath = Environment.getExternalStorageDirectory() + "/" + android.os.Environment.DIRECTORY_DCIM + "/";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpost);

        btnBack = (ImageButton) findViewById(R.id.toolbar_back);
        btnContinue = (ImageButton) findViewById(R.id.toolbar_continue);
        viewPager = (ViewPager2) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        NewPostViewPagerAdapter adapter = new NewPostViewPagerAdapter(this);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position){
                    case 0:
                        tab.setText("GALLERY");
                        break;
                    case 1:
                        tab.setText("PHOTO");
                        break;
                }
            }
        }).attach();
    }

    public String getGalleryPath(){ return galleryPath;}
}