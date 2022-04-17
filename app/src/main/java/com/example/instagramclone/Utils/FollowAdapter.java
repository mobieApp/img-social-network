package com.example.instagramclone.Utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.instagramclone.R;
import com.example.instagramclone.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<User> listUser;
    private FirebaseFirestore firestore;
    private DocumentReference documentReference;
    public FollowAdapter(Context context, ArrayList<User> data){
        this.context = context;
        this.listUser = data;
    }
    @Override
    public int getCount() {
        return listUser.size();
    }

    @Override
    public User getItem(int i) {
        return listUser.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflator = ((Activity) context).getLayoutInflater();
            view = layoutInflator.inflate(R.layout.layout_row_follow, viewGroup, false);
        }

        User currentUser = getItem(i);
        TextView usernameFollow = (TextView) view.findViewById(R.id.usernameFollow);
        TextView nameFollow = (TextView) view.findViewById(R.id.nameFollow);
        CircleImageView imageView = (CircleImageView) view.findViewById(R.id.imageView2);

        usernameFollow.setText(currentUser.getUsername());
        nameFollow.setText(currentUser.getName());
        Picasso.get().load(currentUser.getAvatar()).into(imageView);

        return view;
    }
}