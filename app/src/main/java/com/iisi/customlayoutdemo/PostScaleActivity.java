package com.iisi.customlayoutdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iisi.customlayoutdemo.custom.view.PostScaleImageView;
import com.iisi.customlayoutdemo.custom.view.PostTranslateImageView;

public class PostScaleActivity extends AppCompatActivity {

    private PostScaleImageView img;
    private float i = 1;
    private EditText editorTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_scale);
        img = findViewById(R.id.img);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.test);

        img.setBitmap(icon);
        Button scaleBt = findViewById(R.id.scaleBt);
        scaleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //i = i + 0.1f;
                img.postScale(i);
            }
        });

        Button moveUp = findViewById(R.id.moveUp);
        moveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.postTranslate(30, 0);
            }
        });

        Button moveDown = findViewById(R.id.moveDown);
        moveDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.postTranslate(0, 100);
            }
        });

        editorTxt = findViewById(R.id.editorTxt);
        editorTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(editorTxt.getText().toString()))
                    i = Float.parseFloat(editorTxt.getText().toString());
            }
        });
    }
}
