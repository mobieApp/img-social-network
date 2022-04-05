package com.example.instagramclone.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.R;
import com.example.instagramclone.models.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImagePostAdapter extends RecyclerView.Adapter<ImagePostAdapter.ViewHolder> {
    ArrayList<Post> PostList;
    Context mContext;

    public ImagePostAdapter(ArrayList<Post> postList, Context mContext) {
        PostList = postList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.img_post_grid_item, parent, false);
        return new ImagePostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post iPost = PostList.get(position);
        Picasso.get().load(iPost.getMedia_url()).into(holder.img1);
    }

    @Override
    public int getItemCount() {
        return PostList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img1;
        View space;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img1 = itemView.findViewById(R.id.img1);
        }
    }
}
