package com.example.instagramclone.Utils;

import android.content.Context;
import android.content.Intent;
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
import com.example.instagramclone.RecyclerViewClickListener;
import com.example.instagramclone.models.Post;
import com.example.instagramclone.controller.CommentActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class InstagramFeedRVAdapter extends RecyclerView.Adapter<InstagramFeedRVAdapter.ViewHolder> {
    private static RecyclerViewClickListener itemListener;

    private ArrayList<Post> instaModalArrayList;
    private Context context;
    private FirebaseFirestore firestore;
    private DocumentReference documentReference;
    private TimestampDuration duration;

    public InstagramFeedRVAdapter(ArrayList<Post> instaModalArrayList, Context context, RecyclerViewClickListener itemListenerRef) {
        firestore = FirebaseFirestore.getInstance();
        this.instaModalArrayList = instaModalArrayList;
        Log.d("AAA", "InstagramFeedRVAdapter: " + this.instaModalArrayList.toString());
        this.context = context;
        itemListener = itemListenerRef;
    }

    @NonNull
    @Override
    public InstagramFeedRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_view_post, parent, false);
        return new InstagramFeedRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstagramFeedRVAdapter.ViewHolder holder, int position) {
        Post modal = instaModalArrayList.get(position);
        Log.d("AAA", "onBindViewHolder: " + modal.getUserId());
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
        duration = new TimestampDuration(modal.getTimestamp());
        //Log.d("AAA", "onBindViewHolder: " + duration.toString());
        if (duration.DiffDay() != 0) {
            if (duration.DiffDay() <= 7)
                holder.timetv.setText(duration.DiffDay() + " ngày trước");
            else holder.timetv.setText(LocalDate.parse(modal.getTimestamp().toString()).toString());
        } else if (duration.DiffHour() != 0)
            holder.timetv.setText(duration.DiffHour() + " giờ trước");
        else if (duration.DiffMinute() != 0)
            holder.timetv.setText(duration.DiffMinute() + " phút trước");
        else if (duration.DiffSecond() != 0)
            holder.timetv.setText(duration.DiffSecond() + " giây trước");
        Picasso.get().load(modal.getMedia_url()).into(holder.postIV);
        holder.desctv.setText(modal.getCaption());
//        holder.likeTV.setText("" + modal.getLikesCount() + " likes");
    }

    @Override
    public int getItemCount() {
        return instaModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View itemview;

        CircleImageView authorIV;
        private TextView authorTV;
        private ImageView postIV, cmtBtn;
        private TextView likeTV, desctv, timetv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemview = itemView;
            authorIV = itemView.findViewById(R.id.UserAvatar);
            authorTV = itemView.findViewById(R.id.UserName);
            postIV = itemView.findViewById(R.id.IVPost);
            likeTV = itemView.findViewById(R.id.TVLikes);
            desctv = itemView.findViewById(R.id.TVPostDescription);
            timetv = itemView.findViewById(R.id.timestampPost);
            cmtBtn = itemView.findViewById(R.id.commentBtn);
            cmtBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context.getApplicationContext(), "Comment button hit at position: ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context.getApplicationContext(), CommentActivity.class);
                }
            });
            itemview.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemListener.recyclerViewListClicked(view, this.getPosition());
        }
    }
}