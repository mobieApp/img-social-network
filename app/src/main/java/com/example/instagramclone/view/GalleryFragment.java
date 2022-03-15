package com.example.instagramclone.view;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bm.library.PhotoView;
import com.example.instagramclone.R;
import com.example.instagramclone.Utils.GalleryAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class GalleryFragment extends Fragment {
    NewPostActivity newPostActivity;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private ArrayList<String> imagePaths;
    private RecyclerView imagesRV;
    private GalleryAdapter imageRVAdapter;
    private Uri pathImageSelected = null;
    private PhotoView mImageParallaxHeader;

    public GalleryFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            newPostActivity = (NewPostActivity) getActivity();
        }catch (IllegalStateException e){
            throw new IllegalStateException("NewPostActivity must implement callbacks");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_gallery,null);

        mImageParallaxHeader = (PhotoView) view.findViewById(R.id.parallax_header_imageview);
        imagePaths = new ArrayList<>();

        imagesRV = view.findViewById(R.id.idRVImages);

        prepareRecyclerView();

        requestPermissions();

        return view;
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(newPostActivity.getApplicationContext(), READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermissions(){
        if(checkPermission()){
            Toast.makeText(newPostActivity.getApplicationContext(),"Permissions granted",Toast.LENGTH_SHORT).show();
            getImagePath();
        }else{
            requestPermission();
        }
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(newPostActivity,new String[]{READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
    }

    private void prepareRecyclerView(){
        imageRVAdapter = new GalleryAdapter(this,imagePaths);

        GridLayoutManager manager = new GridLayoutManager(newPostActivity,4);

        imagesRV.setLayoutManager(manager);
        imagesRV.setAdapter(imageRVAdapter);
    }

    private void getImagePath(){
        boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        if(isSDPresent){
            final String[] columns = {MediaStore.Images.ImageColumns._ID,
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.ImageColumns.MIME_TYPE};

            final String orderBy = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";
            Cursor cursor = newPostActivity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,columns,null,null,orderBy);

            int count = cursor.getCount();

            for(int i = 0; i < count; i++){
                cursor.moveToPosition(i);
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                imagePaths.add(cursor.getString(dataColumnIndex));
            }
            imageRVAdapter.notifyDataSetChanged();
            cursor.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        Toast.makeText(newPostActivity.getApplicationContext(), "Permissions Granted..", Toast.LENGTH_SHORT).show();
                        getImagePath();
                    }else{
                        Toast.makeText(newPostActivity.getApplicationContext(), "Permissions denined, Permissions are required to use the app..", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    public void setImageSelected(File file) {
        Picasso.get().load(file).fit().centerCrop().into(mImageParallaxHeader);
    }
}
