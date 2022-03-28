package com.example.instagramclone.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.instagramclone.R;
import com.example.instagramclone.RecyclerViewClickListener;
import com.example.instagramclone.Utils.EditPhotoAdapter;
import com.example.instagramclone.Utils.PhotoFilterAdapter;
import com.example.instagramclone.RecyclerViewClickListener;

import java.io.File;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.ViewType;

public class PhotoEffectActivity extends AppCompatActivity implements RecyclerViewClickListener {
    PhotoEditor mPhotoEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_effects);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewEffect);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setAdapter(new PhotoFilterAdapter(this));
        recyclerView.setLayoutManager(linearLayoutManager);


        PhotoEditorView mPhotoEditorView = findViewById(R.id.photoEditorView);
        mPhotoEditorView.getSource().setImageResource(R.drawable.instagram);

        //Use custom font using latest support library
        Typeface mTextRobotoTf = ResourcesCompat.getFont(this, R.font.roboto_medium);

        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true)
                .setDefaultTextTypeface(mTextRobotoTf)
                .build();

        mPhotoEditor.setOnPhotoEditorListener(new OnPhotoEditorListener() {
            @Override
            public void onEditTextChangeListener(View rootView, String text, int colorCode) {

            }

            @Override
            public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {

            }

            @Override
            public void onRemoveViewListener(int numberOfAddedViews) {

            }

            @Override
            public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {

            }

            @Override
            public void onStartViewChangeListener(ViewType viewType) {

            }

            @Override
            public void onStopViewChangeListener(ViewType viewType) {

            }
        });

//        Button button = (Button) findViewById(R.id.btn);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("MissingPermission")
//            @Override
//            public void onClick(View view) {
//                File sdCard = Environment.getExternalStorageDirectory();
//                File dir = new File(sdCard.getAbsolutePath() + "/PDFfilter");
//                dir.mkdirs();
//                String fileName = "MyFilterImg";
//                File outFile = new File(dir, fileName);
//                String imagePath = outFile.getAbsolutePath();
//                mPhotoEditor.saveAsFile(imagePath, new PhotoEditor.OnSaveListener() {
//                    @Override
//                    public void onSuccess(@NonNull String imagePath) {
//                        Log.e("PhotoEditor", "Image Saved Successfully");
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Throwable throwable) {
//                        Log.e("PhotoEditor", "Failed to save Image");
//                    }
//                });
//            }
//        });
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        switch (position) {
            case 0:
                mPhotoEditor.setFilterEffect(PhotoFilter.NONE);
                break;
            case 1:
                mPhotoEditor.setFilterEffect(PhotoFilter.AUTO_FIX);
                break;
            case 2:
                mPhotoEditor.setFilterEffect(PhotoFilter.BRIGHTNESS);
                break;
            case 3:
                mPhotoEditor.setFilterEffect(PhotoFilter.CONTRAST);
                break;
            case 4:
                mPhotoEditor.setFilterEffect(PhotoFilter.CROSS_PROCESS);
                break;
            case 5:
                mPhotoEditor.setFilterEffect(PhotoFilter.FILL_LIGHT);
                break;
            case 6:
                mPhotoEditor.setFilterEffect(PhotoFilter.GRAY_SCALE);
                break;
            case 7:
                mPhotoEditor.setFilterEffect(PhotoFilter.LOMISH);
                break;
            case 8:
                mPhotoEditor.setFilterEffect(PhotoFilter.POSTERIZE);
                break;
            case 9:
                mPhotoEditor.setFilterEffect(PhotoFilter.SEPIA);
                break;
            case 10:
                mPhotoEditor.setFilterEffect(PhotoFilter.TEMPERATURE);
                break;
            case 11:
                mPhotoEditor.setFilterEffect(PhotoFilter.TINT);
                break;
            case 12:
                mPhotoEditor.setFilterEffect(PhotoFilter.VIGNETTE);
                break;
        }
    }
}