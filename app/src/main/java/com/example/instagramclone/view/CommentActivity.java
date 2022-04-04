package com.example.instagramclone.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.instagramclone.R;
import com.example.instagramclone.Utils.CommentAdapter;
import com.example.instagramclone.models.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
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
        cmtEditTxt = findViewById(R.id.comment);
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
        DocumentReference documentReference = firestore.collection("Comment").document();
        loadComment();

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
                comment.setPostID(postID);
                documentReference.set(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG", "Comment post success");
                    }
                });
                cmtEditTxt.setText("");
                tmpArr.add(postID);
                loadComment();
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
                    ArrayList<String> commentsArr = new ArrayList<>();
                    ArrayList<String> userCmtID = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("DOCUMENT: ", document.getId() + " => " + document.getData());
                        HashMap<String, Object> map = (HashMap) document.getData();
                        String username = map.get("content").toString();
                        userCmtID.add(map.get("userId").toString());
                        Log.d("DATA ", username);
                        commentsArr.add(username);
                    }
                    Log.d("commentsArr DATA", commentsArr.toString());
                    getCommentUsers(userCmtID, commentsArr);
//                            Log.d("DATA OF commentsArr", commentsArr.toString());
//                            Log.d("DATA OF commentsUserArr", commentsUserArr.toString());
//                            listViewComment.setAdapter(new CommentAdapter(CommentActivity.this, R.layout.layout_comment, commentsArr, commentsUserArr));
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void getCommentUsers(ArrayList<String> userCmtID, ArrayList<String> commentsArr) {
        if (!commentsArr.isEmpty()) {
            firestore.collection("User").whereIn(FieldPath.documentId(), userCmtID)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        ArrayList<String> commentsUserArr = new ArrayList<>();
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
                        }

                        Log.d("DATA OF commentsUserArr", commentsUserArr.toString());
                        listViewComment.setAdapter(new CommentAdapter(CommentActivity.this, R.layout.layout_comment, commentsArr, commentsUserArr, avatarArr));
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                }
            });
        } else
            listViewComment.setAdapter(new CommentAdapter(CommentActivity.this, R.layout.layout_comment, commentsArr, userCmtID, userCmtID));
    }
}