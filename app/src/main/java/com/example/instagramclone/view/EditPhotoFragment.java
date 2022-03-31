package com.example.instagramclone.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.instagramclone.R;
import com.example.instagramclone.RecyclerViewClickListener;
import com.example.instagramclone.Utils.EditPhotoAdapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.ViewType;

public class EditPhotoFragment extends Fragment implements RecyclerViewClickListener {
    PhotoEditor mPhotoEditor;
    RecyclerView recyclerView;
    PhotoEditorView mPhotoEditorView;
    EditText addText;
    Button doneBtn;
    NPImageProcessingActivity mActivity;
    ImageButton btnBack, btnContinue;
    byte[] imgByteArr;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_edit_photo, null);
        btnContinue = (ImageButton) mActivity.findViewById(R.id.toolbar_continue);
        btnBack = (ImageButton) mActivity.findViewById(R.id.toolbar_back);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewEdit);
        mPhotoEditorView = (PhotoEditorView) view.findViewById(R.id.photoEditorView);
        addText = view.findViewById(R.id.addText);
        doneBtn = view.findViewById(R.id.doneBtn);

        Bitmap bmp = BitmapFactory.decodeByteArray(imgByteArr, 0, imgByteArr.length);
        mPhotoEditorView.getSource().setImageBitmap(bmp);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setAdapter(new EditPhotoAdapter(this));
        recyclerView.setLayoutManager(linearLayoutManager);

//        mPhotoEditorView.getSource().setImageResource(R.drawable.user_image);

        //Use custom font using latest support library
        Typeface mTextRobotoTf = ResourcesCompat.getFont(mActivity, R.font.roboto_medium);

        //loading font from asset
//        Typeface mEmojiTypeFace = Typeface.createFromAsset(getAssets(), "emojione-android.ttf");

        mPhotoEditor = new PhotoEditor.Builder(mActivity, mPhotoEditorView)
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
            public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {

            }

            @Override
            public void onStartViewChangeListener(ViewType viewType) {

            }

            @Override
            public void onStopViewChangeListener(ViewType viewType) {

            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhotoEditor.saveAsBitmap(new OnSaveBitmap() {
                    @Override
                    public void onBitmapReady(Bitmap saveBitmap) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        saveBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] imgData = stream.toByteArray();
                        Intent intent = new Intent(getContext(), PostActivity.class);
                        intent.putExtra("IMG", imgData);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }
        });

        return view;
    }

    public EditPhotoFragment(byte[] img) {
        imgByteArr = img;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (NPImageProcessingActivity) getActivity();
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
                addText.setEnabled(true);
                addText.setVisibility(View.VISIBLE);
                addText.requestFocus();

                doneBtn.setVisibility(View.VISIBLE);
                doneBtn.setEnabled(true);
                doneBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPhotoEditor.addText(addText.getText().toString(), Color.parseColor("white"));
                        addText.setEnabled(false);
                        addText.setVisibility(View.GONE);
                        doneBtn.setVisibility(View.GONE);
                        doneBtn.setEnabled(false);
                    }
                });
                break;
            case 2:
                mPhotoEditor.brushEraser();
                break;
            case 3:
                ArrayList<String> result = PhotoEditor.getEmojis(mActivity);
//                Toast.makeText(mActivity, result.size() + "", Toast.LENGTH_SHORT).show();
                mPhotoEditor.addEmoji(result.get(0));
                break;
        }
    }
}