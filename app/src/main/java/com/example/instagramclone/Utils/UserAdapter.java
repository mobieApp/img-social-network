package com.example.instagramclone.Utils;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.R;
import com.example.instagramclone.models.SearchRecent;
import com.example.instagramclone.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentSnapshot;

import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.Collections;


import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private ArrayList<User> mUser;
    private Context mContext;
    private boolean isRecent;


    public UserAdapter(ArrayList<User> list, Context context, boolean isRecent) {
        mUser = list;
        mContext = context;
        this.isRecent = isRecent;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.username.setText(mUser.get(position).getUsername());
        holder.name.setText(mUser.get(position).getName());
        if (!mUser.get(position).getAvatar().equals("")) {
            Glide.with(mContext).load(mUser.get(position).getAvatar()).into(holder.img_profile);
        }


        FirebaseFirestore ref = FirebaseFirestore.getInstance();
        if (isRecent != true) {
            holder.delBtn.setVisibility(View.GONE);
            holder.timeRecent.setVisibility(View.GONE);
        }

        FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (isRecent != true) return;
                User getUser = documentSnapshot.toObject(User.class);
                Collections.sort(getUser.getRecent());
                for (SearchRecent i : getUser.getRecent()) {
                    if (i.getUserid().equals(mUser.get(position).getUserid())) {
                        holder.timeRecent.setText(i.GetStringTimestamp());
                        return;
                    }
                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchRecent user = new SearchRecent(mUser.get(position).getUserid());
                ref.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        User user = task.getResult().toObject(User.class);

                        SearchRecent newRecent = new SearchRecent(mUser.get(position).getUserid());

                        ArrayList<SearchRecent> recents = user.getRecent();
                        if (newRecent.getUserid().equals("")) return;
                        for (SearchRecent sr : recents) {
                            if (sr.getUserid().equals(newRecent.getUserid())) {
                                recents.remove(sr);
                                break;
                            }
                        }
                        recents.add(newRecent);
                        FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update("recent", recents);
                        view.refreshDrawableState();
                    }
                });
            }
        });

        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        User user = task.getResult().toObject(User.class);
                        Collections.sort(user.getRecent());
                        user.getRecent().remove(position);

                        FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update("recent", user.getRecent());
                        view.refreshDrawableState();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public TextView name;
        public CircleImageView img_profile;
        public TextView delBtn;
        public TextView timeRecent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_search);
            name = itemView.findViewById(R.id.fullName_search);
            img_profile = itemView.findViewById(R.id.img_profile);
            delBtn = itemView.findViewById(R.id.delete_user);
            timeRecent = itemView.findViewById(R.id.timeRecent);
        }
    }
}
