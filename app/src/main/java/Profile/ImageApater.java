package Profile;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.instagramclone.R;

public class ImageApater extends BaseAdapter {
    private Context context;
    Integer[] images;
    public ImageApater(Context mainActivityContext, Integer[] thumnails){
        context = mainActivityContext;
        images = thumnails;
    }
    public int getCount(){
        return images.length;
    }
    public Object getItem(int position){
        return images[position];
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
        imageView.setImageResource(images[position]);
        imageView.setId(position);
        return imageView;
    }
}
