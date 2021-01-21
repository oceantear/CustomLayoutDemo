package com.iisi.customlayoutdemo.ImageEditor;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.iisi.customlayoutdemo.ImageEditor.listener.ScreenAnimatorListener;
import com.iisi.customlayoutdemo.ImageEditor.listener.onStorePicListener;
import com.iisi.customlayoutdemo.R;
import com.iisi.customlayoutdemo.databinding.ActivityScreenShotBinding;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenShotActivity extends AppCompatActivity {

    private Uri imageUri;
    private Handler mHandler;
    private Handler mStorePicHandler;
    private HandlerThread mStorePicThread;
    private Runnable mStorePicRunnable;

    private ActivityScreenShotBinding binding;

    private static final int STORING_PIC_FINISH = 1;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScreenShotBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if(mHandler == null){
            mHandler = new Handler(){
                @SuppressLint("HandlerLeak")
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);

                    if(msg.what == 0){
                        final Uri uri = (Uri)msg.obj;
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        int height = displayMetrics.heightPixels;
                        int width = displayMetrics.widthPixels;

                        binding.screenShotAnimateView.setVisibility(View.GONE);
                        binding.screenShotAnimateView.setPath(null, width, height, true, uri, new ScreenAnimatorListener() {
                            @Override
                            public void onAnimationCancel() {

                            }

                            @Override
                            public void onAnimationEnd() {
                                Intent i = new Intent();
                                i.setClass(ScreenShotActivity.this, EditImageActivity.class);
                                Bundle b = new Bundle();
                                b.putString("uri", uri.toString());
                                i.putExtras(b);
                                startActivity(i);
                                overridePendingTransition(0, 0);
                            }

                            @Override
                            public void onAnimationStart() {

                            }
                        });
                        binding.screenShotAnimateView.setVisibility(View.VISIBLE);
                    }

                }
            };
        }

        binding.screenShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SystemClock.elapsedRealtime() - mLastClickTime < 2000){
                    return;
                }
                mLastClickTime  = SystemClock.elapsedRealtime();
                getScreenShotPath(binding.wholeVw);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            mStorePicThread.quitSafely();
            mStorePicThread.join();
            mStorePicThread = null;
            mStorePicHandler = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getScreenShotPath(View v){

        Bitmap b = null;
        v.setDrawingCacheEnabled(true);
        b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);

        if(mStorePicThread == null) {
            mStorePicThread = new HandlerThread("StorePicBackground");
            mStorePicThread.start();
            mStorePicHandler = new Handler(mStorePicThread.getLooper());
        }
        mStorePicHandler.post(new StorePicRunnable(getApplicationContext(), b, new onStorePicListener() {

            @Override
            public void onStoredFinish(final Uri uri) {
                if(uri != null){
                    /*Intent i = new Intent();
                    i.setClass(ScreenShotActivity.this, EditImageActivity.class);
                    Bundle b = new Bundle();
                    //b.putString("path", path);
                    //if(imageUri != null)
                        b.putString("uri", uri.toString());
                    i.putExtras(b);
                    startActivity(i);*/
                    Message msg = mHandler.obtainMessage();
                    msg.what = 0;
                    msg.obj = uri;
                    mHandler.sendMessage(msg);
                }
            }
        }));

    }

    private String storeImage(Bitmap b){

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", getResources().getConfiguration().locale);
        String name = format.format(new Date());
        String fname = "ScreenShot_" + name + ".png";
        FileOutputStream fos;

        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){

                ContentResolver resolver = getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fname);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                fos = (FileOutputStream) resolver.openOutputStream(imageUri);

            }else {
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/DCIM/Camera/");
                //if(!myDir.exists())
                myDir.mkdirs();


                File file = new File(myDir, fname);
                Log.i("iisi", "store file:" + file);
                //if (file.exists())
                //    file.delete();
                fos = new FileOutputStream(file);
            }

            b.compress(Bitmap.CompressFormat.PNG, 20, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Log.e("iisi", "Store image from view error : " + e.toString());
        }

        return fname;
    }

}
