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

public class EditPhotoAdapter extends RecyclerView.Adapter<EditPhotoAdapter.ViewHolder> {

    String[] items = {"Adjust", "Brightness", "Contrast", "Structure", "Warmth", "Saturation",
            "Color", "Fade", "Highlights", "Shadow", "Vignette", "Tilt Shift", "Sharpen"};

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
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View itemview;
        TextView textView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemview = itemView;
            textView = (TextView) itemView.findViewById(R.id.edit_name);
        }
    }
}
