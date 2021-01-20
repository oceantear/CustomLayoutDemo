package com.iisi.customlayoutdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.iisi.customlayoutdemo.custom.view.DrawableImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class DrawImageActivity extends AppCompatActivity implements View.OnClickListener {

    DrawableImageView choosenImageView;
    Button choosePicture;
    Button savePicture;

    Bitmap bmp;
    Bitmap alteredBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_image);

        choosenImageView = (DrawableImageView) this.findViewById(R.id.ChoosenImageView);
        choosePicture = (Button) this.findViewById(R.id.ChoosePictureButton);
        savePicture = (Button) this.findViewById(R.id.SavePictureButton);

        savePicture.setOnClickListener(this);
        choosePicture.setOnClickListener(this);

    }

    public void onClick(View v)
    {
        if (v == choosePicture)
        {
            Intent choosePictureIntent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(choosePictureIntent, 0);
        }
        else if (v == savePicture)
        {
            if (alteredBitmap != null)
            {
                /*ContentValues contentValues = new ContentValues(3);
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "Draw On Me");

                Uri imageFileUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                try {
                    OutputStream imageFileOS = getContentResolver()
                            .openOutputStream(imageFileUri);
                    alteredBitmap
                            .compress(Bitmap.CompressFormat.PNG, 90, imageFileOS);
                    Toast t = Toast
                            .makeText(this, "Saved!", Toast.LENGTH_SHORT);
                    t.show();

                } catch (Exception e) {
                    Log.v("EXCEPTION", e.getMessage());
                }*/
                storeImage();
            }
        }
    }

    /*protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {
            Uri imageFileUri = intent.getData();
            try {
                BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                bmpFactoryOptions.inJustDecodeBounds = true;
                bmp = BitmapFactory
                        .decodeStream(
                                getContentResolver().openInputStream(
                                        imageFileUri), null, bmpFactoryOptions);

                bmpFactoryOptions.inJustDecodeBounds = false;
                bmp = BitmapFactory
                        .decodeStream(
                                getContentResolver().openInputStream(
                                        imageFileUri), null, bmpFactoryOptions);

                alteredBitmap = Bitmap.createBitmap(bmp.getWidth(),
                        bmp.getHeight(), bmp.getConfig());

                choosenImageView.setNewImage(alteredBitmap, bmp);
                choosenImageView.setIsEditable(true);
            }
            catch (Exception e) {
                Log.v("ERROR", e.toString());
            }
        }
    }*/

    private void storeImage(){
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/DCIM/Camera/");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".png";
        File file = new File(myDir, fname);
        Log.i("iisi", "" + file);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            alteredBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.iisi.linearlayoutwrapdemo.fileprovider", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
