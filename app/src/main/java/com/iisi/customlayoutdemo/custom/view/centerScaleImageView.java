package com.iisi.customlayoutdemo.custom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

public class centerScaleImageView extends AppCompatImageView implements View.OnTouchListener {




    public centerScaleImageView(Context context)
    {
        super(context);
        setOnTouchListener(this);
        init();
    }

    public centerScaleImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setOnTouchListener(this);
        init();
    }

    public centerScaleImageView(Context context, AttributeSet attrs,
                             int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
        init();
    }

    private void init(){

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
