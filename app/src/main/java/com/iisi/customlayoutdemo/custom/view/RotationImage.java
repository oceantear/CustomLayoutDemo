package com.iisi.customlayoutdemo.custom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.iisi.customlayoutdemo.R;

public class RotationImage extends ImageView implements View.OnClickListener {

    private View.OnClickListener clickListener;
    private boolean mAnimationEnd = true;

    public RotationImage(Context context) {
        super(context);
        setOnClickListener(this);
    }

    public RotationImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    public RotationImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
    }

    public RotationImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOnClickListener(this);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        if (l == this) {
            super.setOnClickListener(l);
        } else {
            clickListener = l;
        }
    }


    @Override
    public void onClick(View v) {
        Log.e("iisi", "onClick");
        if(mAnimationEnd) {
            mAnimationEnd = false;
            rotationImage(v);
            setSelected(!isSelected());
        }

    }

    private void rotationImage(final View v){
        Log.e("iisi", "rotationImage");
        Animation am = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        am.setDuration( 2000 );
        am.setRepeatCount(0);
        am.setFillAfter(false);
        am.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.e("iisi","onAnimationEnd");
                if(isSelected())
                    setImageResource(R.drawable.btn_function_add_s);
                else
                    setImageResource(R.drawable.btn_function_round_fav_b);
                mAnimationEnd = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(am);
    }
}
