package com.example.instagramclone.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.R;
import com.example.instagramclone.models.Post;
import com.example.instagramclone.view.PostProfileActivity;
import com.example.instagramclone.view.ProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImagePostAdapter extends RecyclerView.Adapter<ImagePostAdapter.ViewHolder> {
    ArrayList<Post> PostList;
    Context mContext;
    String username;

    public ImagePostAdapter(ArrayList<Post> postList, Context mContext, String username) {
        PostList = postList;
        this.mContext = mContext;
        this.username = username;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.img_post_grid_item, parent, false);
        return new ImagePostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Post iPost = PostList.get(position);
        Picasso.get().load(iPost.getMedia_url()).into(holder.img1);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PostProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username",username);
                bundle.putInt("pos",position);
                bundle.putSerializable("PostList",PostList);
//                intent.putExtra("PostList",PostList);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return PostList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img1 = itemView.findViewById(R.id.img1);
        }
    }
}
