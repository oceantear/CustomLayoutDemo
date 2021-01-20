package com.iisi.customlayoutdemo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.view.View;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp2.getWidth(), bmp2.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, 0, (bmp2.getHeight() - bmp1.getHeight()), null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return bmOverlay;
    }

    public static Uri cacheViewFromView(Context context, View view, Bitmap innerBM) {
        try {

            Bitmap b;
            view.setDrawingCacheEnabled(true);
            b = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
            if (innerBM!=null) {
                b = overlay(innerBM, b);
            }
            String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/" + fileName + ".png"); // overwrites this image every time
            b.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            File imagePath = new File(context.getCacheDir(), "images");
            File newFile = new File(imagePath, fileName + ".png");
            /**
             * 需至Mainfest新增provider
             *
             */
            Uri contentUri = FileProvider.getUriForFile(context, "gov.tfrin.trfinapp.fileprovider", newFile);
            return contentUri;
        } catch (Exception e) {

        }

        return null;
    }
}
