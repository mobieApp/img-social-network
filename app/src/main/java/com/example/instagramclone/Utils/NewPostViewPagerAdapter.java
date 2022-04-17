package com.example.instagramclone.Utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.instagramclone.controller.GalleryFragment;
import com.example.instagramclone.controller.PhotoFragment;

public class NewPostViewPagerAdapter extends FragmentStateAdapter {
    public GalleryFragment mGalleryFragment;
    public PhotoFragment mPhotoFragment;
    public NewPostViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        mGalleryFragment = new GalleryFragment();
        mPhotoFragment = new PhotoFragment();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return mGalleryFragment;
            case 1:
                return mPhotoFragment;
        }
        return mGalleryFragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
