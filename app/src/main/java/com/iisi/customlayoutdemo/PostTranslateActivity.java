package com.iisi.customlayoutdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iisi.customlayoutdemo.custom.view.PostTranslateImageView;

public class PostTranslateActivity extends AppCompatActivity {
    private PostTranslateImageView img;
    private float i = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_translate);
        img = findViewById(R.id.img);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.btn_function_round_fav_b);
        img.setBitmap(icon);
        Button scaleBt = findViewById(R.id.scaleBt);
        scaleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = i + 0.1f;
                img.postScale(i);
            }
        });

        /*Button moveUp = findViewById(R.id.moveUp);
        moveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.postTranslate(10, 0);
            }
        });

        Button moveDown = findViewById(R.id.moveDown);
        moveDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.postTranslate(0, 10);
            }
        });*/
    }
}
