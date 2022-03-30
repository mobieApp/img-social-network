package com.example.instagramclone.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.instagramclone.R;
import com.example.instagramclone.Utils.NPImageViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class NPImageProcessingActivity extends AppCompatActivity {
    ImageButton btnBack, btnContinue;
    ViewPager2 viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_and_filter);

        btnBack = (ImageButton) findViewById(R.id.toolbar_back);
        btnContinue = (ImageButton) findViewById(R.id.toolbar_continue);
        viewPager = (ViewPager2) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        Intent intent = getIntent();
        byte[] img = intent.getExtras().getByteArray("IMG");

        NPImageViewPagerAdapter adapter = new NPImageViewPagerAdapter(this, img);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position){
                    case 0:
                        tab.setText("EDIT");
                        break;
                    case 1:
                        tab.setText("FILTER");
                        break;
                }
            }
        }).attach();

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}