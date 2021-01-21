package com.iisi.customlayoutdemo.ImageEditor;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.iisi.customlayoutdemo.ImageEditor.listener.onStorePicListener;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StorePicRunnable implements Runnable {

    private WeakReference<Bitmap> bp;
    private boolean isStoringPic;
    private onStorePicListener listener;
    private Context ctx;

    public StorePicRunnable(Context context, Bitmap bp, onStorePicListener listener) {
        this.ctx = context;
        this.bp = new WeakReference<>(bp);
        this.listener = listener;
    }

    @Override
    public void run() {

        isStoringPic = true;

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", ctx.getResources().getConfiguration().locale);
        String name = format.format(new Date());
        String fname = "ScreenShot_" + name + ".png";
        FileOutputStream fos;
        Uri uri = null;
        File file = null;

        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){

                ContentResolver resolver = ctx.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fname);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                fos = (FileOutputStream) resolver.openOutputStream(uri);

            }else {
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/DCIM/Camera/");

                if(!myDir.exists())
                    myDir.mkdirs();

                file = new File(myDir, fname);
                Log.i("iisi", "store file:" + file);
                //if (file.exists())
                //    file.delete();
                fos = new FileOutputStream(file);
                //uri = Uri.fromFile(file);
                Log.e("iisi","ctx.getPackageName() : "+ctx.getPackageName());
                uri = FileProvider.getUriForFile(ctx, ctx.getPackageName() +".fileprovider", file);
            }

            bp.get().compress(Bitmap.CompressFormat.PNG, 20, fos);
            fos.flush();
            fos.close();

        } catch (Exception e) {
            Log.e("iisi", "Store image from view error : " + e.toString());
        }



        isStoringPic = false;
        if(listener != null)
            listener.onStoredFinish(uri);
    }
}
