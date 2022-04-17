package com.example.instagramclone.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.R;
import com.example.instagramclone.models.SearchRecent;
import com.example.instagramclone.models.User;
import com.example.instagramclone.controller.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentSnapshot;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;


import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private ArrayList<User> mUser;
    private Context mContext;
    private boolean isRecent;
    private TimestampDuration duration;


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
            Picasso.get().load(mUser.get(position).getAvatar()).into(holder.img_profile);
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
                        setTimestamp(holder.timeRecent,i.getTimestamp());
                        return;
                    }
                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = mUser.get(position).getUserid();
                SearchRecent user = new SearchRecent(userId);
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
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra("userId",userId);
                mContext.startActivity(intent);
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

    private void setTimestamp(TextView textView, Date createdAt) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        cal.setTime(createdAt);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        duration = new TimestampDuration(createdAt);
        if (duration.DiffDay() != 0) {
            if (duration.DiffDay() <= 7)
                textView.setText(duration.DiffDay() + " ngày trước");
            else if (year != LocalDate.now().getYear())
                textView.setText(day + " tháng " + month + ", " + year);
            else textView.setText(day + " tháng " + month);
        } else if (duration.DiffHour() != 0)
            textView.setText(duration.DiffHour() + " giờ trước");
        else if (duration.DiffMinute() != 0)
            textView.setText(duration.DiffMinute() + " phút trước");
        else if (duration.DiffSecond() != 0)
            textView.setText(duration.DiffSecond() + " giây trước");
    }
}
