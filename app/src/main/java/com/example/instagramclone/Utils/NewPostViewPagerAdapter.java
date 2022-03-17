package com.example.instagramclone.Utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.instagramclone.view.GalleryFragment;
import com.example.instagramclone.view.PhotoFragment;

public class NewPostViewPagerAdapter extends FragmentStateAdapter {
    public NewPostViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new GalleryFragment();
            case 1:
                return new PhotoFragment();
        }
        return new GalleryFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
