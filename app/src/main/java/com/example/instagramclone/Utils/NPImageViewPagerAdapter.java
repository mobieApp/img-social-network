package com.example.instagramclone.Utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.instagramclone.view.EditPhotoFragment;
import com.example.instagramclone.view.PhotoEffectFragment;

public class NPImageViewPagerAdapter extends FragmentStateAdapter {
    byte[] img;
    public NPImageViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, byte[] arr) {
        super(fragmentActivity);
        img = arr;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                Log.d("HERE", "IN CASE 0");
                return new EditPhotoFragment(img);
            case 1:
                Log.d("HERE", "IN CASE 1");
                return new PhotoEffectFragment(img);
        }
        Log.d("HERE", "IN OUT OF SWITCH");
        return new EditPhotoFragment(img);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
