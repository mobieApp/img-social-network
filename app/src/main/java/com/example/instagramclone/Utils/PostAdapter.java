package com.example.instagramclone.Utils;

import static android.content.Intent.getIntent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.R;
import com.example.instagramclone.models.Post;
import com.example.instagramclone.models.React;
import com.example.instagramclone.models.User;
import com.example.instagramclone.controller.CommentActivity;
import com.example.instagramclone.controller.PostProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private ArrayList<Post> instaModalArrayList;
    private Context context;
    private FirebaseFirestore firestore;
    private DocumentReference documentReference;
    private TimestampDuration duration;

    public PostAdapter(ArrayList<Post> instaModalArrayList, Context context) {
        firestore = FirebaseFirestore.getInstance();
        this.instaModalArrayList = instaModalArrayList;
        Log.d("AAA", "PostAdapter: " + this.instaModalArrayList.toString());
        this.context = context;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_view_post, parent, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Log.d("AAA", "onBindViewHolder: ");
        Post modal = instaModalArrayList.get(position);

        documentReference = firestore.collection("User").document(modal.getUserId());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                holder.authorTV.setText(documentSnapshot.getString("username"));
                String avatar = documentSnapshot.getString("avatar");
                if (!avatar.equals("") && avatar != null)
                    Picasso.get().load(avatar).into(holder.authorIV);
                else {
                    holder.authorIV.setImageDrawable(context.getDrawable(R.drawable.avatar_default));
                }
            }
        });

        setTimestamp(holder.timetv, modal.getTimestamp());
        Picasso.get().load(modal.getMedia_url()).into(holder.postIV);
        holder.likeTV.setText("" + modal.getListLike().size() + " likes");
        holder.desctv.setText(modal.getCaption());

        if (modal.getListLike().contains(UserAuthentication.userId)) {
            holder.icLike.setImageDrawable(context.getDrawable(R.drawable.ic_heart_red));
            holder.icLike.setTag("red");
        }
        else {
            holder.icLike.setImageDrawable(context.getDrawable(R.drawable.ic_heart));
            holder.icLike.setTag("black");
        }
        DocumentReference updateRef = FirebaseFirestore.getInstance().collection("Post").document(modal.getId());
        holder.icLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AAA", "onClick: " + modal.getUserId());
                if (!UserAuthentication.userId.equals(modal.getUserId())) {
                    DocumentReference docRef = FirebaseFirestore.getInstance().collection("User").document(UserAuthentication.userId);
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ArrayList<String> list = modal.getListLike();
                            String statusHeart = (String) holder.icLike.getTag();
                            if (statusHeart != null) {
                                User user = documentSnapshot.toObject(User.class);
                                int index = user.getFollowing().indexOf(modal.getUserId());
                                React react = user.getReact().get(index);
                                switch (statusHeart) {
                                    case "black":
                                        list.add(UserAuthentication.userId);
                                        Log.d("AAA", "onSuccess: liked");
                                        holder.icLike.setImageDrawable(context.getDrawable(R.drawable.ic_heart_red));
                                        holder.icLike.setTag("red");
                                        addNotification(UserAuthentication.userId,modal.getUserId(),modal.getId(), "like your post", modal.getMedia_url());
                                        int point = react.getPoint() + 1;
                                        react.setPoint(point);
                                        break;
                                    case "red":
                                        list.remove(UserAuthentication.userId);
                                        holder.icLike.setImageDrawable(context.getDrawable(R.drawable.ic_heart));
                                        holder.icLike.setTag("black");
                                        int point1 = react.getPoint() - 1;
//                                        TODO deleteNotification for like
                                        react.setPoint(point1);
                                        break;
                                }
                                user.getReact().set(index, react);
                                docRef.update("react", user.getReact());
                                updateRef.update("listLike", list);
                                holder.likeTV.setText("" + modal.getListLike().size() + " likes");
                            }
                        }
                    });
                }else{
                    ArrayList<String> list = modal.getListLike();
                    String statusHeart = (String) holder.icLike.getTag();
                    if (statusHeart != null) {
                        switch (statusHeart) {
                            case "black":
                                list.add(UserAuthentication.userId);
                                holder.icLike.setImageDrawable(context.getDrawable(R.drawable.ic_heart_red));
                                holder.icLike.setTag("red");
                                // for testing
                                addNotification(UserAuthentication.userId,UserAuthentication.userId ,modal.getId(), "like your post", modal.getMedia_url());
                                break;
                            case "red":
                                list.remove(UserAuthentication.userId);
                                holder.icLike.setImageDrawable(context.getDrawable(R.drawable.ic_heart));
                                holder.icLike.setTag("black");
                                break;
                        }
                        updateRef.update("listLike", list);
                        holder.likeTV.setText("" + modal.getListLike().size() + " likes");
                    }
                }
            }
        });

        holder.cmtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("FOCUS", "TRUE");
                bundle.putString("postID", modal.getId());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        ArrayList<String> tmpArr = new ArrayList<>();
        tmpArr.add(instaModalArrayList.get(position).getId());
        firestore.collection("Comment").whereIn("postID", tmpArr).
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().size() > 0)
                    holder.commentTV.setText("Xem tất cả " + task.getResult().size() + " bình luận");
            }
        });

        holder.commentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("FOCUS", "FALSE");
                bundle.putString("postID", modal.getId());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.popupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu pop = new PopupMenu(context, holder.popupMenu);
                pop.inflate(R.menu.post_menu);
                Menu popMenu = pop.getMenu();
                MenuItem hide = (MenuItem) popMenu.getItem(0);
                // check owner
                popMenu.getItem(0).setVisible(!modal.getUserId().equals(UserAuthentication.userId));
                popMenu.getItem(1).setVisible((!context.getClass().toString().equals(PostProfileActivity.class.toString()))
                        && !modal.getUserId().equals(UserAuthentication.userId));

                popMenu.getItem(2).setVisible(modal.getUserId().equals(UserAuthentication.userId));
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        return menuItemClicked(menuItem, position);
                    }
                });
                pop.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return instaModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView authorIV;
        private TextView authorTV;
        private ImageView postIV, icLike, cmtBtn, popupMenu;
        private TextView likeTV, desctv, timetv, commentTV;
        private MenuItem delete,hide,report;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            authorIV = itemView.findViewById(R.id.UserAvatar);
            authorTV = itemView.findViewById(R.id.UserName);
            postIV = itemView.findViewById(R.id.IVPost);
            likeTV = itemView.findViewById(R.id.TVLikes);
            desctv = itemView.findViewById(R.id.TVPostDescription);
            timetv = itemView.findViewById(R.id.timestampPost);
            icLike = itemView.findViewById(R.id.icLike);
            cmtBtn = itemView.findViewById(R.id.commentBtn);
            commentTV = itemView.findViewById(R.id.textViewComment);
            popupMenu = itemView.findViewById(R.id.popup_post);

        }
    }

    private void setTimestamp(TextView textView, Date createdAt) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        cal.setTime(createdAt);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        duration = new TimestampDuration(createdAt);
        if (duration.DiffDay() != 0) {
            if (duration.DiffDay() <= 7)
                textView.setText(duration.DiffDay() + " ngày trước");
            else if (year != LocalDate.now().getYear())
                textView.setText(day + " tháng " + month + ", " + year);
            else textView.setText(day + " tháng " + month);
        } else if (duration.DiffHour() != 0)
            textView.setText(duration.DiffHour() + " giờ trước");
        else if (duration.DiffMinute() != 0)
            textView.setText(duration.DiffMinute() + " phút trước");
        else if (duration.DiffSecond() != 0)
            textView.setText(duration.DiffSecond() + " giây trước");
    }

    public static void addNotification(String FromUserID,String ToUserId ,String postId, String message, String img) {
        Map<String, Object> data = new HashMap<>();
        data.put("FromUserId", FromUserID);
        data.put("ToUserId",ToUserId);
        data.put("postId", postId);
        data.put("message", message);
        data.put("isPost", true);
        data.put("timestamp", new Date());
        data.put("imgPost", img);
        Task<DocumentReference> collectionReference = FirebaseFirestore.getInstance().collection("Notification").add(data);
    }

    private boolean menuItemClicked(MenuItem item, int pos) {
        Log.d("TAG", "menuItemClicked: " + item.toString());
        switch (item.getItemId()) {
            case R.id.hide_post:
                CreateDialogHide(pos);
                Toast.makeText(context, "Hide", Toast.LENGTH_SHORT).show();
                break;
            case R.id.report_post:
                CreateDialogReport(pos);
                Toast.makeText(context, "Report", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete_post:
                CreateDialogDelete(pos);
                Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void CreateDialogReport(int pos) {
        final View view = View.inflate(context, R.layout.dialog_report, null);
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("The Report");
//        alertDialog.setIcon("Icon id here");
        alertDialog.setCancelable(false);
//        alertDialog.setMessage("Enter the content of the report ");

        EditText reportContents = (EditText) view.findViewById(R.id.etComments);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String EditValue = reportContents.getText().toString();
                if (EditValue.equals("")) {
                    AlertDialog alertDialog1 = new AlertDialog.Builder(context).create();
                    alertDialog1.setTitle("Warnings");
                    alertDialog1.setMessage("Content report can not empty !");
                    alertDialog1.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog1.show();
                    return;
                }
                Log.d("TAG", "report userid: " + instaModalArrayList.get(pos).getId());
                DocumentReference db = FirebaseFirestore.getInstance().collection("Post").document(instaModalArrayList.get(pos).getId());
                db.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ArrayList<String> theReports = documentSnapshot.toObject(Post.class).getReports();
                        theReports.add(EditValue);
                        db.update("reports", theReports);
                    }
                });
            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }

    private void CreateDialogHide(int pos) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Are your sure hide this post?");
//        alertDialog.setIcon("Icon id here");
        alertDialog.setCancelable(false);
//        alertDialog.setMessage("Enter the content of the report ");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DocumentReference db = FirebaseFirestore.getInstance().collection("Post").document(instaModalArrayList.get(pos).getId());
                db.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ArrayList<String> hideLists = documentSnapshot.toObject(Post.class).getIsHide();
                        if (hideLists.contains(UserAuthentication.userId)) return;
                        hideLists.add(UserAuthentication.userId);
                        db.update("isHide", hideLists);
                        Activity activity = (Activity) context;
                        activity.recreate();
                    }
                });
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void CreateDialogDelete(int pos) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        Log.d("AAA", "CreateDialogDelete: " + context);
        alertDialog.setTitle("Are you sure delete this post?");
//        alertDialog.setIcon("Icon id here");
        alertDialog.setCancelable(false);
//        alertDialog.setMessage("Enter the content of the report ");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("Post id", "onClick: "+instaModalArrayList.get(pos).getId());
                FirebaseFirestore.getInstance().collection("Post").document(instaModalArrayList.get(pos).getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("AAA", "CreateDialogDelete: " + context);
                        AlertDialog successDialog = new AlertDialog.Builder(context).create();
                        successDialog.setTitle("Delete post successfully!");
                        successDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                successDialog.dismiss();
                                int actualPosition = pos;
                                instaModalArrayList.remove(pos);
                                notifyItemRemoved(actualPosition);
                                notifyItemRangeChanged(actualPosition, instaModalArrayList.size());
                            }
                        });
                        successDialog.show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertDialog failureDialog = new AlertDialog.Builder(context).create();
                        failureDialog.setTitle("Delete post not available!");
                        failureDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                failureDialog.dismiss();
                            }
                        });
                        failureDialog.show();
                    }
                });
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
