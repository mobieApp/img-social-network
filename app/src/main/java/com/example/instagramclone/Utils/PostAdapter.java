package com.example.instagramclone.Utils;

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
import com.example.instagramclone.view.CommentActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
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
        DocumentReference updateRef = FirebaseFirestore.getInstance().collection("Post").document(modal.getId());
        holder.icLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> list = modal.getListLike();
                String statusHeart = (String) holder.icLike.getTag();
                if (statusHeart != null) {
                    switch (statusHeart) {
                        case "black":
                            list.add(UserAuthentication.userId);
                            holder.icLike.setImageDrawable(context.getDrawable(R.drawable.ic_heart_red));
                            holder.icLike.setTag("red");
                            addNotification(UserAuthentication.userId, modal.getId());
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
                Log.i("Pop", "Menu class: " + popMenu.getClass().getName());
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        return menuItemClicked(menuItem);
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

    private void addNotification(String userId, String postId) {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("postId", postId);
        data.put("text", "like your post");
        data.put("isPost", true);
        data.put("timestamp", new Date());
        Task<DocumentReference> collectionReference = FirebaseFirestore.getInstance().collection("Notification").add(data);
    }

    private boolean menuItemClicked(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.hide_post:
                Toast.makeText(context, "Hide", Toast.LENGTH_SHORT).show();
                break;
            case R.id.report_post:
                CreateDialogPost();
                Toast.makeText(context, "Report", Toast.LENGTH_SHORT).show();
            default:
                Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void CreateDialogPost() {
        final View view = View.inflate(context,R.layout.dialog_report, null);
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("The Report");
//        alertDialog.setIcon("Icon id here");
        alertDialog.setCancelable(false);
//        alertDialog.setMessage("Enter the content of the report ");


        final EditText reportContents = (EditText) view.findViewById(R.id.etComments);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String EditValue = reportContents.getText().toString();
//                if(EditValue.equals("")) Toast.makeText(alertDialog.getContext(), "Please fill to editText", Toast.LENGTH_SHORT).show();
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
}
