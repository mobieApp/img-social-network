package com.example.instagramclone.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.instagramclone.R;
import com.example.instagramclone.Utils.UserAuthentication;
import com.example.instagramclone.models.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.UUID;

public class PostActivity extends AppCompatActivity {

    private StorageReference storageReference;
    private CollectionReference postCollection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_newpost_confirm);
        ImageView imgPost = findViewById(R.id.ivPhoto);
        ImageView backBtn = findViewById(R.id.backBtn);
        ImageView postBtn = findViewById(R.id.postDone);
        EditText caption = findViewById(R.id.caption);
        ConstraintLayout loadingLayout = findViewById(R.id.LoadingLayout);

        byte[] arr = getIntent().getExtras().getByteArray("IMG");
        Bitmap bmp = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        imgPost.setImageBitmap(bmp);

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
                imm.hideSoftInputFromWindow(view.getWindowToken(),0);

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
                                        loadingLayout.setVisibility(View.GONE);
                                        Intent intent = new Intent(PostActivity.this,HomeActivity.class);
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
}