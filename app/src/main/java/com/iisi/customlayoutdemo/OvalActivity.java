package com.iisi.customlayoutdemo;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iisi.customlayoutdemo.custom.view.OvalImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class OvalActivity extends AppCompatActivity {

    private OvalImageView mOvalImageView;
    private ImageView mCropImageView;
    private HandlerThread mCameraThread;
    protected Handler mCameraHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oval);

        mCameraThread = new HandlerThread("CameraBackground");
        mCameraThread.start();
        mCameraHandler = new Handler(mCameraThread.getLooper());

        mOvalImageView = findViewById(R.id.ovalView);
        mCropImageView = findViewById(R.id.cropImage);
        Button showImage = findViewById(R.id.showImage);
        showImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOvalImageView.setVisibility(View.GONE);
                mCropImageView.setVisibility(View.VISIBLE);
                mCropImageView.setImageBitmap(cropPhoto(mOvalImageView.loadBitmapFromView()));
                saveImage(cropPhoto(mOvalImageView.loadBitmapFromView()));
            }
        });

        //mOvalImageView.loadBitmapFromView();
    }


    protected Bitmap cropPhoto(Bitmap bitmap) {
        int w = mOvalImageView.getCenterRect().width();
        int h = mOvalImageView.getCenterRect().height();
        if (w < bitmap.getWidth()) {
            //一般情形
            int centerWidth = (int) Math.round(bitmap.getWidth() * 0.9);
            int centerHeight = (int) Math.round(bitmap.getHeight() * 0.3); //與MaskView
            int left = (bitmap.getWidth() - centerWidth) / 2;
            int top = (int) Math.round(bitmap.getHeight() * 0.2);

            return Bitmap.createBitmap(bitmap, left, top, centerWidth, centerHeight);
        } else {
            //照片比擷取區域小
            return Bitmap.createBitmap(bitmap, 0, (int) Math.round(bitmap.getHeight() * 0.2), w, (int) Math.round(bitmap.getHeight() * 0.54));
        }
    }

    protected String getFilePath() {
        String root = Environment.getExternalStorageDirectory().toString();
        //File myDir = new File(root + "/DCIM/Camera/");
        //return getExternalFilesDir(null) + "/iRent";
        return root + "/DCIM/Camera/";
    }

    private void saveImage(Bitmap bitmap) {
        long dtMili = System.currentTimeMillis();
        mCameraHandler.post(new ImageSaver(bitmap, getFilePath(), Long.toString(dtMili)));
    }


    public static class ImageSaver implements Runnable {

        private Image mImage;
        private String path;
        private Bitmap mBitmap;
        //private OnTakeCameraCompleteListener listener;
        private String fileName;

        //public ImageSaver(Image image, String path, String fileName, OnTakeCameraCompleteListener listener) {
        public ImageSaver(Image image, String path, String fileName) {
            mImage = image;
            this.path = path;
            //this.listener = listener;
            this.fileName = fileName;
        }


        //public ImageSaver(Bitmap mBitmap, String path, String fileName, OnTakeCameraCompleteListener listener) {
        public ImageSaver(Bitmap mBitmap, String path, String fileName) {
            this.mBitmap = mBitmap;
            this.path = path;
            //this.listener = listener;
            this.fileName = fileName;
        }

        @Override
        public void run() {
            if (mImage != null) {
                ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);
                File mImageDir = new File(path);

                boolean success = true;
                if (!mImageDir.exists()) {
                    mImageDir.mkdir();
                }

                File file = new File(path, fileName + ".jpg");

                FileOutputStream fos = null;
                try {
                    file.createNewFile();
                    fos = new FileOutputStream(file);
                    fos.write(data, 0, data.length);
                } catch (IOException e) {
                    e.printStackTrace();
                    success = false;
                } finally {
                    mImage.close();
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //if (success) listener.onTakeCameraComplete();
            } else if (mBitmap != null) {

                File mImageDir = new File(path);

                boolean success = true;
                if (!mImageDir.exists()) {
                    success = mImageDir.mkdir();
                }
                File file = new File(path, fileName + "Crop.png");
                OutputStream os = null;
                try {
                    os = new BufferedOutputStream(new FileOutputStream(file));
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 80, os);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //if (success) listener.onCropPhotoComplete(mBitmap);
            }
        }
    }
}
