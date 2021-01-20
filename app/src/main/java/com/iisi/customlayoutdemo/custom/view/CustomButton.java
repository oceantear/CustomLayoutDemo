package com.iisi.customlayoutdemo.custom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatButton;

import com.iisi.customlayoutdemo.R;

public class CustomButton extends AppCompatButton {
    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("iisi","ACTION_DOWN");
                setTextColor(getResources().getColor(R.color.white_60));
                break;
            case MotionEvent.ACTION_UP:
                Log.e("iisi","ACTION_UP");
                setTextColor(getResources().getColor(R.color.white));
                break;

        }
        return super.onTouchEvent(event);
    }
}
