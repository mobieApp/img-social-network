package com.example.instagramclone.Utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import java.util.Collection;
import java.util.Collections;
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

        ArrayList<Comment> notReplyCmt = new ArrayList<>();
        for(Comment comment : commentArr)
            if(!comment.isReply())
                notReplyCmt.add(comment);

        for (int i = 0; i < commentArr.size(); i++) {
            if (commentArr.get(i).isReply()) {
                Comment singleCmt = commentArr.get(i);
                this.commentArr.remove(i);

                Comment mainCmt = new Comment();
                for(Comment notReply : notReplyCmt)
                    if(notReply.getListReply().contains(singleCmt.getId())) {
                        mainCmt = notReply;
                        break;
                    }

                int idx = this.commentArr.indexOf(mainCmt);
                if (idx >= commentArr.size())
                    commentArr.add(singleCmt);
                else
                    commentArr.add(idx + 1, singleCmt);

                String singleUsername = this.username.get(i);
                this.username.remove(i);
                if (idx >= commentArr.size())
                    this.username.add(singleUsername);
                else
                    this.username.add(idx + 1, singleUsername);


                String singleAvatar = this.avatarArr.get(i);
                this.avatarArr.remove(i);
                if (idx >= commentArr.size())
                    this.avatarArr.add(singleAvatar);
                else
                    this.avatarArr.add(idx + 1, singleAvatar);
            }
        }
    }

    public void refreshComment() {

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
        RelativeLayout layout = (RelativeLayout) row.findViewById(R.id.commentRelativeLayout);

        if (commentArr.get(position).isReply()) {
            RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) layout.getLayoutParams();
            relativeParams.setMargins(160, 0, 0, 0);  // left, top, right, bottom
            layout.setLayoutParams(relativeParams);
        }

        if (!commentArr.isEmpty()) {
            comment.setText(commentArr.get(position).getContent());
            comment_username.setText(username.get(position));
            cmtLikeCount.setText("" + commentArr.get(position).getReactList().size() + " likes");

            if (!avatarArr.get(position).equals("") && avatarArr.get(position) != null)
                Picasso.get().load(avatarArr.get(position)).into(circleImageView);
            commentDate.setText(setTimestamp(commentArr.get(position).getTimestamp()));

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

            cmtRep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

//        Log.d("DATA IN COMMENT ADAPTER", commentArr.get(position) + " <-> " + username.get(position));
        return (row);
    }

    private String setTimestamp(Date createdAt) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        cal.setTime(createdAt);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        duration = new TimestampDuration(createdAt);
        if (duration.DiffDay() != 0) {
            if (duration.DiffDay() <= 7)
                return (duration.DiffDay() + " ngày trước");
            else if (year != LocalDate.now().getYear())
                return (day + " tháng " + month + ", " + year);
            else return (day + " tháng " + month);
        } else if (duration.DiffHour() != 0)
            return (duration.DiffHour() + " giờ trước");
        else if (duration.DiffMinute() != 0)
            return (duration.DiffMinute() + " phút trước");
        else
            return (duration.DiffSecond() + " giây trước");
    }
}