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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> commentContent;
    private ArrayList<String> username;
    private ArrayList<String> avatarArr;

    public CommentAdapter(Context context, int resource, ArrayList<String> commentContent, ArrayList<String> username, ArrayList<String> avatarArr) {
        super(context, resource, commentContent);
        this.context = context;
        this.commentContent = commentContent;
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
        if (!commentContent.isEmpty()) {
            comment.setText(commentContent.get(position));
            comment_username.setText(username.get(position));
            if (!avatarArr.get(position).equals("") && avatarArr.get(position) != null)
                Picasso.get().load(avatarArr.get(position)).into(circleImageView);
        }
        Log.d("DATA IN ADAPTER", commentContent.get(position) + " <-> " + username.get(position));
        return (row);
    }
}
