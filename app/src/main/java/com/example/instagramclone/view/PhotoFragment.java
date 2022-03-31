package com.example.instagramclone.view;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ExifInterface;
import android.media.Image;
import android.media.ImageReader;
import android.net.MacAddress;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.instagramclone.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class PhotoFragment extends Fragment {

    private NewPostActivity newPostActivity;
    private static final String TAG = "PhotoFragment";
    private ImageButton btn_take_photo, btn_continue, btn_switch_camera, btn_toggle_flash;
    private TextureView textureView;
    private Uri pathImage = null;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private String mImageFileLocation;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    private Integer mFlashMode = 0;
    public static final String CAMERA_FRONT = "1";
    public static final String CAMERA_BACK = "0";
    private String cameraId = CAMERA_BACK;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest captureRequest;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;

    // LƯU RA FILE
    private File file;
    private static final int REQUEST_CAMERA_PERMISSION = 101;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    public PhotoFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            newPostActivity = (NewPostActivity) getActivity();
        } catch (IllegalStateException e) {
            throw new IllegalStateException("NewPostActivity must implement callbacks");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_photo, null);
        textureView = (TextureView) view.findViewById(R.id.camera);
        btn_switch_camera = (ImageButton) view.findViewById(R.id.btn_switch_camera);
        btn_toggle_flash = (ImageButton) view.findViewById(R.id.btn_toggle_flash);
        btn_take_photo = (ImageButton) view.findViewById(R.id.btn_take_photo);
        btn_continue = (ImageButton) newPostActivity.findViewById(R.id.toolbar_continue);
        textureView.setSurfaceTextureListener(textureListener);

        btn_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePic();
            }
        });

        btn_switch_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchCamera();
            }
        });
        btn_toggle_flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Switch flash");
                switchFlashMode();
            }
        });
        return view;
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.d(TAG, "onSurfaceTextureAvailable");
            // Open camera khi ready
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // Transform you image captured size according to the surface width and height, và thay đổi kích thước ảnh
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {

            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            
        }
    };

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            Log.e(TAG, "onOpened");
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int i) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    //Thực hiện việc capture ảnh thông qua CAMERACAPTURESESSION
    final CameraCaptureSession.CaptureCallback captureCallbackListener = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            Toast.makeText(newPostActivity.getApplicationContext(), "Saved:" + file, Toast.LENGTH_SHORT).show();
            createCameraPreview();
        }
    };

    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void takePic() {
        if (null == cameraDevice) {
            Log.e(TAG, "cameraDevice is null");
            return;
        }
        CameraManager manager = (CameraManager) newPostActivity.getSystemService(newPostActivity.getApplicationContext().CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }

            // CAPTURE IMAGE với tuỳ chỉnh kích thước
            int width = 640;
            int height = 480;
            if (jpegSizes != null && 0 < jpegSizes.length) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            ArrayList<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            setFlash(captureBuilder);

            // kiểm tra orientation tuỳ thuộc vào mỗi device khác nhau như có nói bên trên
            int deviceRotation = newPostActivity.getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(deviceRotation));

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.TAIWAN);
            Date now = new Date();
            String fileName = "IMG_" + formatter.format(now) + ".jpg";
            final File file = new File(newPostActivity.getGalleryPath(), fileName);
            mImageFileLocation = file.getAbsolutePath();
            Log.d("AAA", "takePic: " + mImageFileLocation);

            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);

                        Bitmap bitmap = rotateImage(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                        bytes = stream.toByteArray();
                        bitmap.recycle();

                        save(bytes);
                        pathImage = Uri.fromFile(file);

                        //ContentValues to show image in gallery
                        /*ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "ImageName");
                        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                        values.put(MediaStore.Images.Media.ORIENTATION, ORIENTATIONS.get(deviceRotation));
                        values.put(MediaStore.Images.Media.CONTENT_TYPE,"image/jpeg");
                        values.put("_data", file.getAbsolutePath());
                        ContentResolver cr = getActivity().getContentResolver();
                        cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);*/

                        newPostActivity.getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (image != null) {
                            image.close();
                        }
                    }
                }

                // Lưu ảnh
                private void save(byte[] bytes) throws IOException {
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(file);
                        output.write(bytes);
                    } finally {
                        if (null != output) {
                            output.close();
                        }
                    }
                }
            };
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    //Toast.makeText(newPostActivity, "Saved: " + file, Toast.LENGTH_SHORT).show();
                    createCameraPreview();
                }
            };
            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        Log.d(TAG, "cameraDevice.createCaptureSession");
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private Bitmap rotateImage(Bitmap bitmap){
        Log.d("AAA", "rotateImage");
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(mImageFileLocation);
            Log.d("AAA", "rotateImage: " + exifInterface);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation){
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            default:
        }
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }

    // Khởi tạo camera để preview trong textureview
    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCapture) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCapture;
                    captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                            CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

                    captureRequest = captureRequestBuilder.build();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                cameraCaptureSessions.setRepeatingRequest(captureRequest, null, mBackgroundHandler);
                            } catch (CameraAccessException e) {
                                Log.e(TAG, "Failed to start camera preview because it couldn't access camera", e);
                            } catch (IllegalStateException e) {
                                Log.e(TAG, "Failed to start camera preview.", e);
                            }
                        }
                    }, 500);
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(newPostActivity, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
        CameraManager manager = (CameraManager) newPostActivity.getSystemService(newPostActivity.getApplicationContext().CAMERA_SERVICE);
        Log.e(TAG, "is camera open");
        try {
            if (cameraId.equals(CAMERA_FRONT)) {
                btn_switch_camera.setImageDrawable(newPostActivity.getResources().getDrawable(R.drawable.ic_camera_front_white,null));
            } else if (cameraId.equals(CAMERA_BACK)) {
                btn_switch_camera.setImageDrawable(newPostActivity.getResources().getDrawable(R.drawable.ic_camera_rear_white,null));
            }
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

            // Add permission for camera and let user grant the permission
            if (ActivityCompat.checkSelfPermission(newPostActivity.getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(newPostActivity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "openCamera: Permission denided");
                ActivityCompat.requestPermissions(newPostActivity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            Log.d(TAG, "openCamera: Permission granted");
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "openCamera X");
    }

    private void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(newPostActivity, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        btn_continue.setVisibility(View.INVISIBLE);
        startBackgroundThread();
        if (textureView.isAvailable()) {
            Log.d(TAG, "onResume: is Available");
            openCamera();
        } else {
            Log.d(TAG, "onResume: is not Available");
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    @Override
    public void onPause() {
        Log.e(TAG, "onPause");
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    public void switchCamera() {
        if (cameraId.equals(CAMERA_FRONT)) {
            cameraId = CAMERA_BACK;
            closeCamera();
            reopenCamera();
        } else if (cameraId.equals(CAMERA_BACK)) {
            cameraId = CAMERA_FRONT;
            closeCamera();
            reopenCamera();
        }
    }
    public void reopenCamera() {
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    private void setFlash(CaptureRequest.Builder mPreviewRequestBuilder) {
        switch (mFlashMode) {
            case 0:
                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
                mPreviewRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                break;
            case 1:
                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                break;
            case 2:
                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH);
                break;
        }
    }
    private void switchFlashMode() {
        switch (mFlashMode) {
            case 0:
                mFlashMode = 1;
                btn_toggle_flash.setImageDrawable(newPostActivity.getResources().getDrawable(R.drawable.ic_flash_auto_white,null));
                captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                try {
                    cameraCaptureSessions.setRepeatingRequest(
                            captureRequestBuilder.build(),
                            null, mBackgroundHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                    return;
                }
                break;
            case 1:
                mFlashMode = 2;
                btn_toggle_flash.setImageDrawable(newPostActivity.getResources().getDrawable(R.drawable.ic_flash_on_white,null));
                captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AE_MODE_ON_ALWAYS_FLASH);
                try {
                    cameraCaptureSessions.setRepeatingRequest(
                            captureRequestBuilder.build(),
                            null, mBackgroundHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                    return;
                }
                break;
            case 2:
                mFlashMode = 0;
                btn_toggle_flash.setImageDrawable(newPostActivity.getResources().getDrawable(R.drawable.ic_flash_off_white,null));
                captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
                captureRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                try {
                    cameraCaptureSessions.setRepeatingRequest(
                            captureRequestBuilder.build(),
                            null, mBackgroundHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                    return;
                }
                break;
        }
        Log.d(TAG, "Mode: " + mFlashMode);
    }
    public Uri getPathImage() {
        return pathImage;
    }
}