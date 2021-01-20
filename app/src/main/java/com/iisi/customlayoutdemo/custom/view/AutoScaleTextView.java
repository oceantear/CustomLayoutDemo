package com.iisi.customlayoutdemo.custom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.iisi.customlayoutdemo.R;

public class AutoScaleTextView extends AppCompatTextView {

    private float mDefaultScaleSize = 1f;
    private float mAutoScale = 1f;
    private float mDefaultAlpha = 1f;
    private float mAutoAlpha = 0.6f;

    public AutoScaleTextView(Context context) {
        super(context);
    }

    public AutoScaleTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AutoScaleText);
        mAutoScale = ta.getFloat(R.styleable.AutoScaleText_autoScale, 0.0f);
        mAutoAlpha = ta.getFloat(R.styleable.AutoScaleText_autoAlpha, 0.0f);
        Log.e("iisi","attrs : "+mAutoScale);
    }

    public AutoScaleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //Log.e("iisi","ACTION_DOWN");
                setScaleX(mAutoScale);
                setScaleY(mAutoScale);
                setAlpha(mAutoAlpha);
                setTextColor(getResources().getColor(R.color.white_60));
                setPressed(true);
                break;
            case MotionEvent.ACTION_UP:
                //Log.e("iisi","ACTION_UP");
                setScaleX(mDefaultScaleSize);
                setScaleY(mDefaultScaleSize);
                setAlpha(mDefaultAlpha);
                setTextColor(getResources().getColor(R.color.white));
                setPressed(false);
                if(!isSelected()) {
                    setSelected(true);
                }else {
                    setSelected(false);
                }
                break;
        }
        return true;
    }


}
