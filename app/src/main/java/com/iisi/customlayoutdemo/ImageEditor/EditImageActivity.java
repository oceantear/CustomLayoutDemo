package com.iisi.customlayoutdemo.ImageEditor;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.iisi.customlayoutdemo.ImageEditor.listener.onStorePicListener;
import com.iisi.customlayoutdemo.R;
import com.iisi.customlayoutdemo.databinding.ActivityEditImageBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditImageActivity extends AppCompatActivity {

    private ActivityEditImageBinding binding;
    private boolean isEnableEdit, isThicknessEnable, isColorMenuEnable;
    private Uri imageUri;
    private Handler mStorePicHandler;
    private HandlerThread mStorePicThread;
    private long mLastClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditImageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        if(getIntent().getExtras() != null){
            Bundle b = getIntent().getExtras();
            String uriStr = b.getString("path", null);

            /*if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {

                if (!TextUtils.isEmpty(uriStr)) {
                    String rootP = Environment.getExternalStorageDirectory().toString();
                    photoPath = rootP + "/DCIM/Camera/" + uriStr;
                    Bitmap bmp = BitmapFactory.decodeFile(photoPath);
                    binding.drawableImageView.setImage(bmp);
                    binding.drawableImageView.setIsEditable(true);
                }

            }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                String imageStr = b.getString("uri", "");
                if (!TextUtils.isEmpty(imageStr)) {
                    imageUri = Uri.parse(imageStr);
                    try {
                        InputStream is = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        is.close();
                        binding.drawableImageView.setImage(bitmap);
                        binding.drawableImageView.setIsEditable(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }*/

            String imageStr = b.getString("uri", "");
            if (!TextUtils.isEmpty(imageStr)) {
                imageUri = Uri.parse(imageStr);
                try {
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    binding.drawableImageView.setImage(bitmap);
                    binding.drawableImageView.setIsEditable(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        binding.menuColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.thicknessMenu.getVisibility() == View.VISIBLE) {
                    binding.thicknessMenu.setVisibility(View.GONE);
                    isThicknessEnable = !isThicknessEnable;
                }
                isColorMenuEnable = !isColorMenuEnable;
                binding.colorMenu.setVisibility(isColorMenuEnable ? View.VISIBLE : View.GONE);
            }
        });

        binding.menuThickness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.colorMenu.getVisibility() == View.VISIBLE) {
                    isColorMenuEnable = !isColorMenuEnable;
                    binding.colorMenu.setVisibility(View.GONE);
                }
                isThicknessEnable = !isThicknessEnable;
                binding.thicknessMenu.setVisibility(isThicknessEnable ? View.VISIBLE : View.GONE);
            }
        });

        binding.shareBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SystemClock.elapsedRealtime() - mLastClickTime < 2000){
                    return;
                }
                mLastClickTime  = SystemClock.elapsedRealtime();
                shareScreenShot();
            }
        });

        binding.colorBlackBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawableImageView.setPaintColor(R.color.black);
            }
        });

        binding.colorRedBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawableImageView.setPaintColor(R.color.brand_red);
            }
        });

        binding.strokeWidthBt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawableImageView.setPaintStroke(dp2pixel(5));
            }
        });

        binding.strokeWidthBt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawableImageView.setPaintStroke(dp2pixel(10));
            }
        });

        binding.strokeWidthBt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawableImageView.setPaintStroke(dp2pixel(15));
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mStorePicThread.quitSafely();
        try {
            mStorePicThread.join();
            mStorePicThread = null;
            mStorePicHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void shareScreenShot(){

        if(mStorePicThread == null) {
            mStorePicThread = new HandlerThread("StorePicBackground");
            mStorePicThread.start();
            mStorePicHandler = new Handler(mStorePicThread.getLooper());
        }
        mStorePicHandler.post(new StorePicRunnable(getApplicationContext(), binding.drawableImageView.getBitmap(), new onStorePicListener() {

            @Override
            public void onStoredFinish(Uri uri) {
                if(uri != null){
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    shareIntent.setType("image/png");
                    startActivity(Intent.createChooser(shareIntent, "選擇分享方式"));
                }else{
                    Toast.makeText(EditImageActivity.this, "無法擷取資料，請稍後再試！", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }));

    }

    private float dp2pixel(int dp) {
        Resources r = getResources();
        float px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        return px;
    }
}
