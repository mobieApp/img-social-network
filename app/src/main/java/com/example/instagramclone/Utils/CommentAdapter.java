package com.example.instagramclone.Utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.instagramclone.R;
import com.example.instagramclone.models.Comment;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends ArrayAdapter<Comment> {
    private Context context;
//    private ArrayList<String> commentArr;
//    private ArrayList<Date> timestamp;

    private ArrayList<Comment> commentArr;
    private ArrayList<String> username;
    private ArrayList<String> avatarArr;
    private TimestampDuration duration;

    public CommentAdapter(Context context, int resource,
                          ArrayList<Comment> commentArr, ArrayList<String> username,
                          ArrayList<String> avatarArr) {
        super(context, resource, commentArr);
        this.context = context;
        this.commentArr = commentArr;
        this.username = username;
        this.avatarArr = avatarArr;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.layout_comment, null);
        TextView comment = (TextView) row.findViewById(R.id.comment);
        TextView comment_username = (TextView) row.findViewById(R.id.comment_username);
        CircleImageView circleImageView = (CircleImageView) row.findViewById(R.id.comment_profile_image);
        TextView commentDate = (TextView) row.findViewById(R.id.comment_time_posted);
        ImageView cmtReact = (ImageView) row.findViewById(R.id.comment_like);
        TextView cmtLikeCount = (TextView) row.findViewById(R.id.comment_likeCount);
        TextView cmtRep = (TextView) row.findViewById(R.id.comment_reply);

        if (!commentArr.isEmpty()) {
            comment.setText(commentArr.get(position).getContent());
            comment_username.setText(username.get(position));
            cmtLikeCount.setText("" + commentArr.get(position).getReactList().size() + " likes");

            if (!avatarArr.get(position).equals("") && avatarArr.get(position) != null)
                Picasso.get().load(avatarArr.get(position)).into(circleImageView);
            setTimestamp(commentDate, commentArr.get(position).getTimestamp());

            if (commentArr.get(position).getReactList().contains(UserAuthentication.userId)) {
                cmtReact.setImageDrawable(context.getDrawable(R.drawable.ic_heart_red));
                cmtReact.setTag("red");
            }

            Comment singleCmt = commentArr.get(position);
            cmtReact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Comment ID react", commentArr.get(position).getId());
                    DocumentReference updateRef = FirebaseFirestore.getInstance().collection("Comment").document(singleCmt.getId());
                    ArrayList<String> list = singleCmt.getReactList();
                    String statusHeart = (String) cmtReact.getTag();
                    Log.d("statusHeart", statusHeart);
                    if (statusHeart != null) {
                        switch (statusHeart) {
                            case "black":
                                list.add(UserAuthentication.userId);
                                cmtReact.setImageDrawable(context.getDrawable(R.drawable.ic_heart_red));
                                cmtReact.setTag("red");
//                                addNotification(UserAuthentication.userId, singleCmt.getId());
                                break;
                            case "red":
                                list.remove(UserAuthentication.userId);
                                cmtReact.setImageDrawable(context.getDrawable(R.drawable.ic_heart));
                                cmtReact.setTag("black");
                                break;
                        }
                        updateRef.update("reactList", list);
                        cmtLikeCount.setText("" + commentArr.get(position).getReactList().size() + " likes");
                    }
                }
            });
        }

//        Log.d("DATA IN COMMENT ADAPTER", commentArr.get(position) + " <-> " + username.get(position));
        return (row);
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
