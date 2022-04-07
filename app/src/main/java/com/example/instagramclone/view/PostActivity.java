package com.example.instagramclone.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.example.instagramclone.R;
import com.example.instagramclone.Utils.PostAdapter;
import com.example.instagramclone.Utils.SpaceTokenizer;
import com.example.instagramclone.Utils.UserAdapter;
import com.example.instagramclone.Utils.UserAuthentication;
import com.example.instagramclone.models.Post;
import com.example.instagramclone.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PostActivity extends AppCompatActivity {

    private StorageReference storageReference;
    private CollectionReference postCollection;
    private HashMap<String,User> UserList = new HashMap<>();
    private ArrayList<String> usernameList = new ArrayList<String>();
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_newpost_confirm);
        ImageView imgPost = findViewById(R.id.ivPhoto);
        ImageView backBtn = findViewById(R.id.backBtn);
        ImageView postBtn = findViewById(R.id.postDone);
        MultiAutoCompleteTextView caption = findViewById(R.id.caption);
        ConstraintLayout loadingLayout = findViewById(R.id.LoadingLayout);

        byte[] arr = getIntent().getExtras().getByteArray("IMG");
        Bitmap bmp = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        imgPost.setImageBitmap(bmp);

        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, usernameList);
        caption.setAdapter(adapter);
        caption.setThreshold(2);
        EventChangeListener();

        caption.setTokenizer(new SpaceTokenizer());

        caption.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Layout layout = caption.getLayout();
                int pos = caption.getSelectionStart();
                int line = layout.getLineForOffset(pos);
                int baseline = layout.getLineBaseline(line);

                int bottom = caption.getHeight();

                caption.setDropDownVerticalOffset(baseline - bottom);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        storageReference = FirebaseStorage.getInstance().getReference();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        postCollection = FirebaseFirestore.getInstance().collection("Post");
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingLayout.setVisibility(View.VISIBLE);
                //Hide keyboard
                InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(PostActivity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


                StorageReference ref = storageReference.child("Post/" + UUID.randomUUID().toString() + ".jpg");
                ref.putBytes(arr).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Post post = new Post();
                                post.setMedia_url(uri.toString());
                                post.setCaption(caption.getText().toString());
                                post.setUserId(UserAuthentication.userId);
                                post.setTimestamp(new Date());
                                post.setId(postCollection.document().getId());
                                postCollection.document(post.getId()).set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        String[] words = caption.getText().toString().split(" ");
                                        for (String word : words){
                                            if(word.startsWith("@")){
                                                User tagUser = UserList.get(word.substring(1));
                                                if(tagUser != null) PostAdapter.addNotification(tagUser.getUserid(),post.getId(),"tag you in a post");;
                                            }
                                        }
                                        loadingLayout.setVisibility(View.GONE);
                                        Intent intent = new Intent(PostActivity.this, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    private void EventChangeListener() {
        FirebaseFirestore.getInstance().collection("User")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                User user = dc.getDocument().toObject(User.class);
                                user.setUserid(dc.getDocument().getId());
                                usernameList.add(user.getUsername());
                                UserList.put(user.getUsername(),user);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}