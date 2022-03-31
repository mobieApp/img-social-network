package com.example.instagramclone.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.instagramclone.R;
import com.example.instagramclone.Utils.BottomNavigationViewHolder;
import com.example.instagramclone.Utils.UserAdapter;
import com.example.instagramclone.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {
    private static final int ACTIVITY_NUM = 1;
    FirebaseFirestore ref;
    ArrayList<User> UserList;
    RecyclerView recyclerView;
    SearchView searchView;
    UserAdapter adapter;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Searching ....");
        progressDialog.show();

        recyclerView = (RecyclerView) findViewById(R.id.rcv_search);
        searchView = (SearchView) findViewById(R.id.search_bar);

        searchView.setIconifiedByDefault(false);

        ref  = FirebaseFirestore.getInstance();
        UserList = new ArrayList<User>();
        adapter = new UserAdapter(UserList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        EventChangeListener();

        setNavView();

    }
    @Override
    protected void onStart(){
        super.onStart();
//        recyclerView.setAdapter(adapter);
//        if(ref != null){
//           ref.addOnSuccessListener {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if(snapshot.exists()){
//                        for(DataSnapshot ds : snapshot.getChildren()){
//                            UserList.add(ds.getValue(User.class));
//                        }
//                        UserAdapter adapter = new UserAdapter(UserList);
//                        recyclerView.setAdapter(adapter);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(SearchActivity.this, error.getMessage(),Toast.LENGTH_SHORT).show();
//                }
//            });
            if(searchView != null){
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        search(newText);
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
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("fireStore error", "onEvent: "+ error.getMessage() );
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                UserList.add(dc.getDocument().toObject(User.class));
                            }
                            adapter.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                        Log.d("FIRESTORE", ref.collection("User").get().toString());
                    }
                });
    }


    private void search(String str) {
        ArrayList<User> list = new ArrayList<>();
        if(UserList.isEmpty()){
            Log.d("ERROR", "search: empty");
            return;
        }
        for(User obj : UserList){
            if(obj.getUsername().toLowerCase(Locale.ROOT).contains(str.toLowerCase(Locale.ROOT)) || obj.getName().toLowerCase(Locale.ROOT).contains(str.toLowerCase(Locale.ROOT))){
                list.add(obj);
            }
        }
        UserAdapter adapter = new UserAdapter(list);
        recyclerView.setAdapter(adapter);
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