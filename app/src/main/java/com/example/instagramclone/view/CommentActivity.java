package com.example.instagramclone.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
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
import com.example.instagramclone.Utils.TimestampDuration;
import com.example.instagramclone.Utils.UserAuthentication;
import com.example.instagramclone.models.Comment;
import com.example.instagramclone.models.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CommentActivity extends AppCompatActivity {
    private ImageView backArrow, postCmt;
    private EditText cmtEditTxt;
    private ListView listViewComment;
    private FirebaseUser user;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private String postID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_comments);
        backArrow = findViewById(R.id.backArrow);
        postCmt = findViewById(R.id.ivPostComment);
        cmtEditTxt = findViewById(R.id.commentEdt);
        listViewComment = (ListView) findViewById(R.id.listViewComment);

        String isFocus = getIntent().getExtras().getString("FOCUS");
        ArrayList<String> tmpArr = new ArrayList<>();
        postID = getIntent().getExtras().getString("postID");
        tmpArr.add(postID);

        if (isFocus.equals("TRUE"))
            cmtEditTxt.requestFocus();

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        user = firebaseAuth.getCurrentUser();
        loadComment();

        listViewComment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Delete at position", i + "");
                AlertDialog alertDialog = new AlertDialog.Builder(CommentActivity.this).create();
                alertDialog.setTitle("Do you want to delete this comment?");
                alertDialog.setCancelable(true);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Comment comment = (Comment) adapterView.getItemAtPosition(i);
                        if (comment.getListReply().size() == 0)
                            firestore.collection("Comment").document(comment.getId()).delete();
                        else {
                            for (String reply : comment.getListReply())
                                firestore.collection("Comment").document(reply).delete();
                            firestore.collection("Comment").document(comment.getId()).delete();
                        }
                        loadComment();
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                return true;
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
                if (!cmtEditTxt.getText().toString().equals("")) {
                    Comment comment = new Comment();

                    DocumentReference documentReference = firestore.collection("Comment").document();
                    comment.setId(documentReference.getId());
                    comment.setContent(cmtEditTxt.getText().toString());
                    comment.setListReply(new ArrayList<>());
                    comment.setTimestamp(Calendar.getInstance().getTime());
                    comment.setUserId(firebaseAuth.getCurrentUser().getUid());
                    comment.setPostID(postID);
                    comment.setReactList(new ArrayList<>());
                    comment.setReply(false);

                    documentReference.set(comment);
                    cmtEditTxt.setText("");
                    loadComment();
                }
            }
        });
    }

    public void loadComment() {
        ArrayList<String> tmpArr = new ArrayList<>();
        tmpArr.add(postID);
        firestore.collection("Comment").whereIn("postID", tmpArr)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<String> userCmtID = new ArrayList<>();
                    ArrayList<Comment> commentArr = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("DOCUMENT: ", document.getId() + " => " + document.getData());

                        Comment comment = new Comment();
                        Timestamp ts = (Timestamp) document.getData().get("timestamp");

                        comment.setId(document.getData().get("id").toString());
                        comment.setContent(document.getData().get("content").toString());
                        comment.setListReply((ArrayList<String>) document.getData().get("listReply"));
                        comment.setPostID(document.getData().get("postID").toString());
                        comment.setReactList((ArrayList<String>) document.getData().get("reactList"));
                        comment.setTimestamp(ts.toDate());
                        comment.setUserId(document.getData().get("userId").toString());
                        comment.setReply(Boolean.parseBoolean(document.getData().get("reply").toString()));

                        userCmtID.add(document.getData().get("userId").toString());
                        commentArr.add(comment);
                    }
                    Log.d("commentsArr DATA", commentArr.toString());
                    getCommentUsers(userCmtID, commentArr);
//                            Log.d("DATA OF commentsArr", commentsArr.toString());
//                            Log.d("DATA OF commentsUserArr", commentsUserArr.toString());
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void getCommentUsers(ArrayList<String> userCmtID, ArrayList<Comment> commentsArr) {
        if (!commentsArr.isEmpty()) {
            firestore.collection("User").whereIn(FieldPath.documentId(), userCmtID)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        ArrayList<String> commentsUserArr = new ArrayList<>();
                        ArrayList<String> commentUserAvatar = new ArrayList<>();
                        ArrayList<String> idArr = new ArrayList<>();
                        ArrayList<String> usernameArr = new ArrayList<>();
                        ArrayList<String> avatarArr = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("DOCUMENT in getCommentUsers()", document.getId() + " => " + document.getData());
                            HashMap<String, Object> map = (HashMap) document.getData();
                            usernameArr.add(map.get("username").toString());
                            avatarArr.add(map.get("avatar").toString());
                            idArr.add(document.getId());
                        }

                        for (String id : userCmtID) {
                            commentsUserArr.add(usernameArr.get(idArr.indexOf(id)));
                            commentUserAvatar.add(avatarArr.get(idArr.indexOf(id)));
                        }

                        Log.d("DATA OF commentsUserArr", commentsUserArr.toString());
                        listViewComment.setAdapter(new CommentAdapter(CommentActivity.this, R.layout.layout_comment, commentsArr, commentsUserArr, commentUserAvatar));
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                }
            });
        } else
            listViewComment.setAdapter(new CommentAdapter(CommentActivity.this, R.layout.layout_comment, commentsArr, userCmtID, userCmtID));
    }
}