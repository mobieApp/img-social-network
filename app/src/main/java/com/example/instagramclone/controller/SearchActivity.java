package com.example.instagramclone.controller;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.instagramclone.R;
import com.example.instagramclone.Utils.BottomNavigationViewHolder;
import com.example.instagramclone.Utils.UserAdapter;
import com.example.instagramclone.models.SearchRecent;
import com.example.instagramclone.models.User;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {
    private static final int ACTIVITY_NUM = 1;
    FirebaseFirestore ref ;
    ArrayList<User> UserList;
    RecyclerView recyclerView;
    SearchView searchView;
    UserAdapter RecentAdapter;
    UserAdapter SearchAdapter;
    ArrayList<User> RecentUserList;
    ArrayList<User> SearchList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ref = FirebaseFirestore.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.rcv_search);
        searchView = (SearchView) findViewById(R.id.search_bar);
//        searchView.setIconifiedByDefault(false);
        UserList = new ArrayList<User>();
        RecentUserList = new ArrayList<User>();
        SearchList = new ArrayList<User>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecentAdapter = new UserAdapter(RecentUserList,this,true);
        SearchAdapter = new UserAdapter(SearchList,this,false);
        EventChangeListener();
        ref.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Log.e(TAG, "onEvent: Listen failed - " + error);
                    return;
                }
                if (value != null && value.exists()){
                    User user = value.toObject(User.class);
                    if(user.getRecent().toString().equals(RecentUserList.toString())) return;
                    RecentUserList.clear();
                    Collections.sort(user.getRecent());
                    for(SearchRecent RecentUser : user.getRecent()){
                        for (User mUser : UserList){
                            if(mUser.getUserid().equals(RecentUser.getUserid())){
                                RecentUserList.add(mUser);
                                break;
                            }
                        }
                    }
                }
            }
        });
        recyclerView.setAdapter(RecentAdapter);
        setNavView();

    }
    @Override
    protected void onStart(){
        super.onStart();

        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText,SearchList);
                    return true;
                }
            });
        }
    }

    private void EventChangeListener() {
        ref.collection("User")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                User user = dc.getDocument().toObject(User.class);
                                user.setUserid(dc.getDocument().getId());
                                UserList.add(user);
                            }
                            RecentAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }


    private void search(String str,ArrayList<User> Searchlist) {

        if(UserList.isEmpty()){
            Log.d("ERROR", "search: empty");
            return;
        }
        Searchlist.clear();
        for(User obj : UserList){
            if(str.isEmpty()){
                recyclerView.setAdapter(RecentAdapter);
                return;
            }
            if(obj.getUsername().toLowerCase(Locale.ROOT).contains(str.toLowerCase(Locale.ROOT)) || obj.getName().toLowerCase(Locale.ROOT).contains(str.toLowerCase(Locale.ROOT))){
                Searchlist.add(obj);
            }
        }
        recyclerView.setAdapter(SearchAdapter);
    }

    private void setNavView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHolder.activateNavigationView(this,bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setNavView();
    }
}