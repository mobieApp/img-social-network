package com.example.instagramclone.Utils;

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

import java.util.ArrayList;

public class FollowAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> userId;
    private FirebaseFirestore firestore;
    private DocumentReference documentReference;
    public FollowAdapter(Context context, ArrayList<String> data){
        this.context = context;
        this.userId = data;
    }
    @Override
    public int getCount() {
        return userId.size();
    }

    @Override
    public String getItem(int i) {
        return userId.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.layout_row_follow, viewGroup, false);
        }

        String currentUserId = getItem(i);
        TextView usernameFollow = (TextView) view.findViewById(R.id.usernameFollow);
        TextView nameFollow = (TextView) view.findViewById(R.id.nameFollow);
        firestore = FirebaseFirestore.getInstance();
        documentReference = firestore.collection("User").document(currentUserId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    User currentUser = documentSnapshot.toObject(User.class);
                    usernameFollow.setText(currentUser.getUsername());
                    nameFollow.setText(currentUser.getName());
                }
            }
        });
        return view;
    }
}
