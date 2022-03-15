package com.example.instagramclone.view;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.instagramclone.R;
import com.example.instagramclone.Utils.NewPostViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class NewPostActivity extends AppCompatActivity {
    ImageButton btnBack, btnContinue;
    ViewPager2 viewPager;
    TabLayout tabLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_newpost);

        btnBack = (ImageButton) findViewById(R.id.toolbar_back);
        btnContinue = (ImageButton) findViewById(R.id.toolbar_continue);
        viewPager = (ViewPager2) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        NewPostViewPagerAdapter adapter = new NewPostViewPagerAdapter(this);

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
}



