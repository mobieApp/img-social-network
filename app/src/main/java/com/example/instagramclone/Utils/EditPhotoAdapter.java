package com.example.instagramclone.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.R;
import com.example.instagramclone.RecyclerViewClickListener;

public class EditPhotoAdapter extends RecyclerView.Adapter<EditPhotoAdapter.ViewHolder> {
    private static RecyclerViewClickListener itemListener;

    String[] items = {"Brush", "Text", "Eraser", "Emoji"};
    Integer[] image = {R.drawable.ic_brush, R.drawable.ic_text, R.drawable.ic_eraser, R.drawable.ic_insert_emoticon};

    public EditPhotoAdapter(RecyclerViewClickListener itemListenerRef){
        itemListener = itemListenerRef;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Nạp layout cho View
        View editView = inflater.inflate(R.layout.single_edit_photo_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(editView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(items[position]);
        holder.imageView.setImageResource(image[position]);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private View itemview;
        TextView textView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemview = itemView;
            textView = (TextView) itemView.findViewById(R.id.edit_name);
            imageView = (ImageView) itemView.findViewById(R.id.editImageView);
            itemview.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemListener.recyclerViewListClicked(view, this.getPosition());
        }
    }
}
