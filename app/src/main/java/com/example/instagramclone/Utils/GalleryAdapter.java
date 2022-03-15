package com.example.instagramclone.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.R;
import com.example.instagramclone.view.GalleryFragment;
import com.example.instagramclone.view.NewPostActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.RecyclerViewHolder> {
    private final Context context;
    private final ArrayList<String> imagePathArrayList;

    private GalleryFragment galleryFragment;

    private int imageSelected = -1;

    public GalleryAdapter(GalleryFragment galleryFragment, ArrayList<String> imagePathArrayList) {
        this.galleryFragment = galleryFragment;
        this.imagePathArrayList = imagePathArrayList;
        this.context = (NewPostActivity)galleryFragment.getActivity();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_grid_item, parent,false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        File imgFile = new File(imagePathArrayList.get(position));
        if (imgFile.exists()){
            Picasso.get().setLoggingEnabled(true);
            Picasso.get().load(imgFile).placeholder(R.drawable.ic_launcher_background).resize(400,400).centerCrop().into(holder.imageIV);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    galleryFragment.setImageSelected(imgFile);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return imagePathArrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageIV;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageIV = itemView.findViewById(R.id.idIVImages);
        }
    }
}
