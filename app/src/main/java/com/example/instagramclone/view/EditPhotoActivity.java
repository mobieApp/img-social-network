package com.example.instagramclone.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.instagramclone.R;
import com.example.instagramclone.RecyclerViewClickListener;
import com.example.instagramclone.Utils.EditPhotoAdapter;

import java.util.ArrayList;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.ViewType;

public class EditPhotoActivity extends AppCompatActivity implements RecyclerViewClickListener {
    PhotoEditor mPhotoEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_photo);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewEdit);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setAdapter(new EditPhotoAdapter(this));

        recyclerView.setLayoutManager(linearLayoutManager);

        PhotoEditorView mPhotoEditorView = findViewById(R.id.photoEditorView);
        mPhotoEditorView.getSource().setImageResource(R.drawable.user_image);

        //Use custom font using latest support library
        Typeface mTextRobotoTf = ResourcesCompat.getFont(this, R.font.roboto_medium);

        //loading font from asset
//        Typeface mEmojiTypeFace = Typeface.createFromAsset(getAssets(), "emojione-android.ttf");

        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true)
                .setDefaultTextTypeface(mTextRobotoTf)
                .build();

//        mPhotoEditor.setBrushDrawingMode(true);
//        mPhotoEditor.setBrushSize(30);
//        mPhotoEditor.setOpacity(100);
//        mPhotoEditor.setBrushColor(Color.parseColor("blue"));
//        mPhotoEditor.brushEraser();
        mPhotoEditor.setOnPhotoEditorListener(new OnPhotoEditorListener() {
            @Override
            public void onEditTextChangeListener(View rootView, String text, int colorCode) {
                mPhotoEditor.editText(rootView, text, colorCode);
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
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        switch (position) {
            case 0:
                mPhotoEditor.setBrushDrawingMode(true);
                mPhotoEditor.setBrushColor(Color.parseColor("blue"));
                mPhotoEditor.setOpacity(100);
                break;
            case 1:
//                mPhotoEditor.setFilterEffect(PhotoFilter.FLIP_HORIZONTAL);
                mPhotoEditor.addText("Hello, World", Color.parseColor("blue"));
                break;
            case 2:
                mPhotoEditor.brushEraser();
                break;
            case 3:
                ArrayList<String> result = PhotoEditor.getEmojis(getApplicationContext());
//                Toast.makeText(EditPhotoActivity.this, , Toast.LENGTH_SHORT).show();
                mPhotoEditor.addEmoji(result.get(0));
                break;
        }
    }
}