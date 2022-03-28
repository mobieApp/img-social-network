package com.example.instagramclone.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.R;
import com.example.instagramclone.Utils.BottomNavigationViewHolder;
import com.example.instagramclone.Utils.NotificationAdapter;
import com.example.instagramclone.Utils.UserAuthentication;
import com.example.instagramclone.models.Notification;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;
    private static final int ACTIVITY_NUM = 3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_notification);
        recyclerView = findViewById(R.id.RVNotification);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NotificationActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        notificationList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(NotificationActivity.this, notificationList);
        recyclerView.setAdapter(notificationAdapter);

        readNotification();
        setNavView();
    }
    private void setNavView(){
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHolder.activateNavigationView(this,bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private void readNotification() {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Notification");
        reference.whereEqualTo("userId",UserAuthentication.userId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                notificationList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    Notification notification = doc.toObject(Notification.class);
                    notificationList.add(notification);
                }

                Collections.reverse(notificationList);
                notificationAdapter.notifyDataSetChanged();
            }
        });
    }
}
