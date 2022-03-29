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

import java.util.ArrayList;

public class CommentAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> items;
    private ArrayList<String> username;

    public CommentAdapter(@NonNull Context context, int resource, ArrayList<String> items, ArrayList<String> username) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
        this.username = username;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.layout_comment, null);
        TextView comment = (TextView) row.findViewById(R.id.comment);
        TextView comment_username = (TextView) row.findViewById(R.id.comment_username);
        comment.setText(items.get(position));
//        comment_username.setText(username.get(position));
        comment_username.setText("username");
        Log.d("DATA IN ADAPTER", items.get(position).toString());
        return (row);
    }
}
