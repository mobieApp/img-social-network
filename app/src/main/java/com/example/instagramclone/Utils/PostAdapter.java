package com.example.instagramclone.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.R;
import com.example.instagramclone.models.Post;
import com.example.instagramclone.view.CommentActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private ArrayList<Post> instaModalArrayList;
    private Context context;
    private FirebaseFirestore firestore;
    private DocumentReference documentReference;
    private TimestampDuration duration;

    public PostAdapter(ArrayList<Post> instaModalArrayList, Context context) {
        firestore = FirebaseFirestore.getInstance();
        this.instaModalArrayList = instaModalArrayList;
        Log.d("AAA", "PostAdapter: " + this.instaModalArrayList.toString());
        this.context = context;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_view_post, parent, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        Post modal = instaModalArrayList.get(position);

        documentReference = firestore.collection("User").document(modal.getUserId());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                holder.authorTV.setText(documentSnapshot.getString("username"));
                String avatar = documentSnapshot.getString("avatar");
                if (!avatar.equals("") && avatar != null)
                    Picasso.get().load(avatar).into(holder.authorIV);
                else {
                    holder.authorIV.setImageDrawable(context.getDrawable(R.drawable.avatar_default));
                }
            }
        });

        setTimestamp(holder.timetv, modal.getTimestamp());
        Picasso.get().load(modal.getMedia_url()).into(holder.postIV);
        holder.likeTV.setText("" + modal.getListLike().size() + " likes");
        holder.desctv.setText(modal.getCaption());

        if (modal.getListLike().contains(UserAuthentication.userId)) {
            holder.icLike.setImageDrawable(context.getDrawable(R.drawable.ic_heart_red));
            holder.icLike.setTag("red");
        }
        DocumentReference updateRef = FirebaseFirestore.getInstance().collection("Post").document(modal.getId());
        holder.icLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> list = modal.getListLike();
                String statusHeart = (String) holder.icLike.getTag();
                if (statusHeart != null) {
                    switch (statusHeart) {
                        case "black":
                            list.add(UserAuthentication.userId);
                            holder.icLike.setImageDrawable(context.getDrawable(R.drawable.ic_heart_red));
                            holder.icLike.setTag("red");
                            addNotification(UserAuthentication.userId, modal.getId());
                            break;
                        case "red":
                            list.remove(UserAuthentication.userId);
                            holder.icLike.setImageDrawable(context.getDrawable(R.drawable.ic_heart));
                            holder.icLike.setTag("black");
                            break;
                    }
                    updateRef.update("listLike", list);
                    holder.likeTV.setText("" + modal.getListLike().size() + " likes");
                }
            }
        });

        holder.cmtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("FOCUS", "TRUE");
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.commentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("FOCUS", "FALSE");
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return instaModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView authorIV;
        private TextView authorTV;
        private ImageView postIV, icLike, cmtBtn;
        private TextView likeTV, desctv, timetv, commentTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            authorIV = itemView.findViewById(R.id.UserAvatar);
            authorTV = itemView.findViewById(R.id.UserName);
            postIV = itemView.findViewById(R.id.IVPost);
            likeTV = itemView.findViewById(R.id.TVLikes);
            desctv = itemView.findViewById(R.id.TVPostDescription);
            timetv = itemView.findViewById(R.id.timestampPost);
            icLike = itemView.findViewById(R.id.icLike);
            cmtBtn = itemView.findViewById(R.id.commentBtn);
            commentTV = itemView.findViewById(R.id.textViewComment);
        }
    }

    private void setTimestamp(TextView textView, Date createdAt) {
        duration = new TimestampDuration(createdAt);
        if (duration.DiffDay() != 0) {
            if (duration.DiffDay() <= 7)
                textView.setText(duration.DiffDay() + " ngày trước");
            else textView.setText(LocalDate.parse(createdAt.toString()).toString());
        } else if (duration.DiffHour() != 0)
            textView.setText(duration.DiffHour() + " giờ trước");
        else if (duration.DiffMinute() != 0)
            textView.setText(duration.DiffMinute() + " phút trước");
        else if (duration.DiffSecond() != 0)
            textView.setText(duration.DiffSecond() + " giây trước");
    }

    private void addNotification(String userId, String postId) {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("postId", postId);
        data.put("text", "like your post");
        data.put("isPost", true);
        data.put("timestamp", new Date());
        Task<DocumentReference> collectionReference = FirebaseFirestore.getInstance().collection("Notification").add(data);
    }
}