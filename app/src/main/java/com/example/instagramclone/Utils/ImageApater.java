package com.example.instagramclone.Utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.instagramclone.R;
import com.squareup.picasso.Picasso;

public class ImageApater extends BaseAdapter {
    private Context context;
    String[] imagesURL;
    public ImageApater(Context mainActivityContext, String[] thumnail_URL){
        context = mainActivityContext;
        imagesURL = thumnail_URL;
    }
    public int getCount(){
        return imagesURL.length;
    }
    public Object getItem(int position){
        return imagesURL[position];
    }
    public long getItemId(int position){
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        ImageView imageView;
        if(convertView == null){
            imageView = new ImageView(context);
            int gridSize = context.getResources().getDimensionPixelSize(R.dimen.gridview_size);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(gridSize,gridSize));
            imageView.setScaleType((ImageView.ScaleType.FIT_XY));
            imageView.setPadding(5,5,5,5);
        }
        else{
            imageView = (ImageView) convertView;
        }
        Picasso.get().load(imagesURL[position]).centerCrop().into(imageView);
        imageView.setId(position);
        return imageView;
    }
}
