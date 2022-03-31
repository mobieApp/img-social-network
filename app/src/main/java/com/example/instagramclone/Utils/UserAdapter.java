package com.example.instagramclone.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.R;
import com.example.instagramclone.models.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
   private ArrayList<User> mUser;
   public UserAdapter(ArrayList<User> list){
       mUser = list;
   }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
       holder.username.setText(mUser.get(position).getUsername());
       holder.name.setText(mUser.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public TextView name;
        public CircleImageView img_profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_search);
            name = itemView.findViewById(R.id.fullName_search);
            img_profile = itemView.findViewById(R.id.img_profile);
        }
    }
}
