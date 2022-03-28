package com.example.instagramclone.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.R;
import com.example.instagramclone.RecyclerViewClickListener;

public class PhotoFilterAdapter extends RecyclerView.Adapter<PhotoFilterAdapter.ViewHolder> {
    private static RecyclerViewClickListener itemListener;

    String[] items = {"None", "Auto Fix", "Brightness", "Contrast", "Cross Process", "Fill Light", "Gray Scale",
            "Lomish", "Posterize", "Sepia", "Temperature", "Tint", "Vignette"};

    Integer[] picture = {R.drawable.none, R.drawable.auto_fix, R.drawable.brightness, R.drawable.contrast, R.drawable.cross_process, R.drawable.fill_light,
            R.drawable.gray_scale, R.drawable.lomish, R.drawable.posterize, R.drawable.sepia, R.drawable.temprature, R.drawable.tint, R.drawable.vignette};

    public PhotoFilterAdapter(RecyclerViewClickListener itemListenerRef) {
        itemListener = itemListenerRef;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Nạp layout cho View
        View editView = inflater.inflate(R.layout.single_effect_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(editView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(items[position]);
        holder.imageView.setImageResource(picture[position]);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View itemview;
        TextView textView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemview = itemView;
            textView = (TextView) itemView.findViewById(R.id.filter_name);
            imageView = (ImageView) itemView.findViewById(R.id.effectImageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemListener.recyclerViewListClicked(view, this.getPosition());
        }
    }
}
