package com.example.instagramclone.Utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.R;
import com.example.instagramclone.models.Notification;
import com.example.instagramclone.models.Post;
import com.example.instagramclone.models.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class NotificationAdapter extends  RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private List<Notification> notificationList;
    private TimestampDuration duration;

    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        holder.text.setText(notification.getMessage());

        getUserInfo(holder.image_profile,holder.username,notification.getFromUserId());

        Log.d("AAA", "onBindViewHolder: " + notification.isIspost());
        if (notification.isIspost()){
            holder.post_image.setVisibility(View.VISIBLE);
            getPostImage(holder.post_image,notification.getPostId());
        }
        else{
            holder.post_image.setVisibility(View.INVISIBLE);
        }
        setTimestamp(holder.timestamp,notification.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView image_profile, post_image;
        public TextView username, text, timestamp;
        public ViewHolder(View itemView){
            super(itemView);
            image_profile = itemView.findViewById(R.id.avatarUser);
            post_image = itemView.findViewById(R.id.post_image);
            username = itemView.findViewById(R.id.username);
            text = itemView.findViewById(R.id.actionNotify);
            timestamp = itemView.findViewById(R.id.timestampNotify);
        }
    }

    private void getUserInfo(ImageView imageView, TextView username, String userId){
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("User").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                User user = value.toObject(User.class);
                Picasso.get().load(user.getAvatar()).into(imageView);
                username.setText(user.getUsername());
            }
        });
    }

    private void getPostImage(ImageView imageView, String postId){
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Post").document(postId);
        Log.d("AAA", "getPostImage: " + postId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Post post = value.toObject(Post.class);
                Picasso.get().load(post.getMedia_url()).into(imageView);
            }
        });
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
                textView.setText(duration.DiffDay() + " ngày");
            else if (year != LocalDate.now().getYear())
                textView.setText(day + " tháng " + month + ", " + year);
            else textView.setText(day + " tháng " + month);
        } else if (duration.DiffHour() != 0)
            textView.setText(duration.DiffHour() + " giờ");
        else if (duration.DiffMinute() != 0)
            textView.setText(duration.DiffMinute() + " phút");
        else if (duration.DiffSecond() != 0)
            textView.setText(duration.DiffSecond() + " giây");
    }
}
