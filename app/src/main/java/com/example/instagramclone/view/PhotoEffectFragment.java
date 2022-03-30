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
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.instagramclone.R;
import com.example.instagramclone.RecyclerViewClickListener;
import com.example.instagramclone.Utils.PhotoFilterAdapter;

import java.io.ByteArrayOutputStream;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.ViewType;

public class PhotoEffectFragment extends Fragment implements RecyclerViewClickListener {
    PhotoEditor mPhotoEditor;
    NPImageProcessingActivity mActivity;
    ImageButton btnBack, btnContinue;
    PhotoEditorView mPhotoEditorView;
    byte[] imgByteArr;


    public PhotoEffectFragment(byte[] img) {
        imgByteArr = img;
    }

    @Override
    public void onResume() {
        super.onResume();
        Bitmap bmp = BitmapFactory.decodeByteArray(imgByteArr, 0, imgByteArr.length);
        mPhotoEditorView.getSource().setImageBitmap(bmp);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (NPImageProcessingActivity) getActivity();

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_effects, null);
        btnContinue = (ImageButton) mActivity.findViewById(R.id.toolbar_continue);
        btnBack = (ImageButton) mActivity.findViewById(R.id.toolbar_back);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewEffect);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setAdapter(new PhotoFilterAdapter(this));
        recyclerView.setLayoutManager(linearLayoutManager);


        mPhotoEditorView = view.findViewById(R.id.photoEditorView);
//        mPhotoEditorView.getSource().setImageResource(R.drawable.instagram);

        Bitmap bmp = BitmapFactory.decodeByteArray(imgByteArr, 0, imgByteArr.length);
        mPhotoEditorView.getSource().setImageBitmap(bmp);

        //Use custom font using latest support library
        Typeface mTextRobotoTf = ResourcesCompat.getFont(mActivity, R.font.roboto_medium);

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
                    public void onFailure(Throwable e) {

                    }
                });
            }
        });

        return view;
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