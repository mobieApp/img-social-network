package com.example.instagramclone.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.instagramclone.R;
import com.example.instagramclone.Utils.CommentAdapter;
import com.example.instagramclone.models.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {
    private ImageView backArrow, postCmt;
    private EditText cmtEditTxt;
    private ListView listViewComment;
    private FirebaseUser user;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_comments);
        backArrow = findViewById(R.id.backArrow);
        postCmt = findViewById(R.id.ivPostComment);
        cmtEditTxt = findViewById(R.id.comment);
        listViewComment = (ListView) findViewById(R.id.listViewComment);

        String tmp = getIntent().getExtras().getString("FOCUS");
        if (tmp.equals("TRUE"))
            cmtEditTxt.requestFocus();

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        user = firebaseAuth.getCurrentUser();
        DocumentReference documentReference = firestore.collection("Comment").document();
        Task<QuerySnapshot> retrieveComment = firestore.collection("Comment")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> commentsArr = new ArrayList<>();
                            ArrayList<String> commentsUserArr = new ArrayList<>();
                            ArrayList<String> userCmtID = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("DOCUMENT: ", document.getId() + " => " + document.getData());
                                HashMap<String, Object> map = (HashMap) document.getData();
                                String commentContent = map.get("content").toString();
                                userCmtID.add(map.get("userId").toString());
                                Log.d("DATA ", commentContent);
                                commentsArr.add(commentContent);
                            }

                            for (String id : userCmtID) {
                                DocumentReference docRef = firestore.collection("User").document(id);
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            commentsUserArr.add(document.getString("username"));
                                            listViewComment.setAdapter(new CommentAdapter(CommentActivity.this, R.layout.layout_comment, commentsArr, userCmtID));
                                            if (document.exists()) {
                                                Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                                            } else {
                                                Log.d("TAG", "No such document");
                                            }
                                        } else {
                                            Log.d("TAG", "get failed with ", task.getException());
                                        }
                                    }
                                });
                            }

                            Log.d("DATA OF commentsArr", commentsArr.toString());
                            Log.d("DATA OF commentsUserArr", commentsUserArr.toString());
//                            listViewComment.setAdapter(new CommentAdapter(CommentActivity.this, R.layout.layout_comment, commentsArr, userCmtID));
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        listViewComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        postCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Map<String, Object> edited = new HashMap<>();
                Comment comment = new Comment();
//                edited.put("content", cmtEditTxt.getText().toString());
                comment.setContent(cmtEditTxt.getText().toString());
                comment.setListReply("");
                comment.setTimestamp(Calendar.getInstance().getTime());
                comment.setUserId(firebaseAuth.getCurrentUser().getUid());
                documentReference.set(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG", "Comment post success");
                    }
                });
                cmtEditTxt.setText("");
                Task<QuerySnapshot> retrieveComment = firestore.collection("Comment")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    ArrayList<String> commentsArr = new ArrayList<>();
                                    ArrayList<String> commentsUserArr = new ArrayList<>();
                                    ArrayList<String> userCmtID = new ArrayList<>();

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("DOCUMENT: ", document.getId() + " => " + document.getData());
                                        HashMap<String, Object> map = (HashMap) document.getData();
                                        String commentContent = map.get("content").toString();
                                        userCmtID.add(map.get("userId").toString());
                                        Log.d("DATA ", commentContent);
                                        commentsArr.add(commentContent);
                                    }

                                    for (String id : userCmtID) {
                                        DocumentReference docRef = firestore.collection("User").document(id);
                                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    commentsUserArr.add(document.getString("username"));
                                                    listViewComment.setAdapter(new CommentAdapter(CommentActivity.this, R.layout.layout_comment, commentsArr, commentsUserArr));
                                                    if (document.exists()) {
                                                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                                                    } else {
                                                        Log.d("TAG", "No such document");
                                                    }
                                                } else {
                                                    Log.d("TAG", "get failed with ", task.getException());
                                                }
                                            }
                                        });
                                    }

                                    Log.d("DATA OF commentsArr", commentsArr.toString());
                                    Log.d("DATA OF commentsUserArr", commentsUserArr.toString());
//                                    listViewComment.setAdapter(new CommentAdapter(CommentActivity.this, R.layout.layout_comment, commentsArr, userCmtID));
                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });
    }
}