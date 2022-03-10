package com.example.instagramclone.Utils;

import android.content.Context;
import android.view.ContentInfo;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    String data1[], data2[];
    int images[];
    Context context;
    public PostAdapter(Context context, String s1[], String s2[], int img[]){
        this.context = context;
        data1 = s1;
        data2 = s2;
        images = img;
    }
    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {

    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
