package com.iisi.customlayoutdemo.custom.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.iisi.customlayoutdemo.ImageEditor.listener.ScreenAnimatorListener;


public class ScreenAnimateView extends CardView {

    private int mFinalW;
    private int mFinalH;

    private Handler mHandler;

    private ObjectAnimator mScaleXAnim = null;
    private ObjectAnimator mScaleYAnim = null;

    private ObjectAnimator mTranslationXAnim = null;
    private ObjectAnimator mTranslationYAnim = null;

    private ScreenAnimatorListener mListener;
    private long mDelayAfterAnimator = 1*1000;//秒

    public ScreenAnimateView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ScreenAnimateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScreenAnimateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == GONE) {
            resetView();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        anim(null, false);
        startTick(false);
    }

    private void init(Context context) {
        mHandler = new Handler(Looper.getMainLooper());
        setCardElevation(20);
        setRadius(dp2pixel(20));
    }

    public void setPath(final String path, int w, int h, final boolean anim, final Uri uri, ScreenAnimatorListener ls) {
        this.mListener = ls;
        setClickable(false);
        anim(null, false);
        final ImageView thumb = new ImageView(getContext());
        FrameLayout.LayoutParams thumbParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        int padding = (int) dp2pixel(2);
        int margins = (int) dp2pixel(8);

        //截圖之後的寬度，高度按照比例設置
        mFinalW = (int) dp2pixel(90);
        mFinalH = (int) ((float) mFinalW * h) / w;
        if (!anim) {
            //邊框
            params.setMargins(margins, margins, margins, margins);
            margins = (int) dp2pixel(2);
            params.width = mFinalW + margins * 2;
            params.height = mFinalH + margins * 2;
            params.gravity = Gravity.START | Gravity.BOTTOM;

            thumbParams.width = mFinalW;
            thumbParams.height = mFinalH;
            thumbParams.gravity = Gravity.CENTER;
            setLayoutParams(params);
            requestLayout();
        } else {
            //邊框
            thumbParams.setMargins(margins, margins, margins, margins);
            params.setMargins(0, 0, 0, 0);
            params.width = FrameLayout.LayoutParams.MATCH_PARENT;
            params.height = FrameLayout.LayoutParams.MATCH_PARENT;
            setLayoutParams(params);
            requestLayout();
        }
        thumb.setScaleType(ImageView.ScaleType.FIT_XY);
        thumb.setLayoutParams(thumbParams);
        addView(thumb);
        post(new Runnable() {
            @Override
            public void run() {
                float scale = (float) mFinalW / getMeasuredWidth();
                int radius = 20;
                if (anim) {
                    radius = (int) (5f / scale);
                }
                setRadius((int) dp2pixel(radius));

                Glide.with(getContext())
                     .load(uri)
                     .centerCrop()
                     .listener(new RequestListener<Drawable>() {
                         @Override
                         public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                             if (anim) {
                                 anim(thumb, true);
                             }
                             return false;
                         }

                         @Override
                         public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                             if (thumb.getDrawable() == null) {
                                 // 避免截圖成功時出現短暫的白色背景
                                 thumb.setImageDrawable(resource);
                             }
                             if (anim) {
                                 anim(thumb, true);
                             }
                             return false;
                         }
                     }).into(thumb);

                //延遲關閉截圖
                startTick(true);
            }
        });
    }

    /**
     * Animation
     * @param view
     * @param start
     */
    private void anim(final ImageView view, boolean start) {
        if (!start) {
            if (getChildCount() > 0) {
                // 快速點擊截圖，上一次的view 尚未移除，reset view
                resetView();
            }
            setScaleX(1f);
            setScaleY(1f);
            setTranslationX(0f);
            setTranslationY(0f);
            clearAnimation();
            if (mScaleXAnim != null) {
                mScaleXAnim.cancel();
                mScaleXAnim = null;
            }
            if (mScaleYAnim != null) {
                mScaleYAnim.cancel();
                mScaleYAnim = null;
            }
            if (mTranslationXAnim != null) {
                mTranslationXAnim.cancel();
                mTranslationXAnim = null;
            }
            if (mTranslationYAnim != null) {
                mTranslationYAnim.cancel();
                mTranslationYAnim = null;
            }
            return;
        }

        view.post(new Runnable() {
            @Override
            public void run() {
                if (!view.isAttachedToWindow()) {
                    // View 被移除
                    return;
                }
                setCardBackgroundColor(Color.WHITE);

                //等待cross fade動畫
                float margins = dp2pixel(10);
                float scaleToX = (float) mFinalW / getMeasuredWidth();
                float scaleToY = (float) mFinalH / getMeasuredHeight();
                float translateToX = -(getMeasuredWidth() / 2f - (mFinalW / 2 + margins));
                float translateToY = getMeasuredHeight() / 2f - (mFinalH / 2f + margins);

                //以當前view為中心，x軸右為正，左為負，y軸下為正，上為負

                mScaleXAnim = ObjectAnimator.ofFloat(ScreenAnimateView.this, "scaleX", 1.0f, scaleToX);
                mScaleYAnim = ObjectAnimator.ofFloat(ScreenAnimateView.this, "scaleY", 1.0f, scaleToY);

                mTranslationXAnim = ObjectAnimator.ofFloat(ScreenAnimateView.this, "translationX", 1.0f, translateToX);
                mTranslationYAnim = ObjectAnimator.ofFloat(ScreenAnimateView.this, "translationY", 1.0f, translateToY);

                //速度
                mScaleXAnim.setDuration(500);
                mScaleYAnim.setDuration(500);
                mTranslationXAnim.setDuration(500);
                mTranslationYAnim.setDuration(500);

                //縮放
                mScaleXAnim.start();
                mScaleYAnim.start();
                //平移
                mTranslationXAnim.start();
                mTranslationYAnim.start();
                setEnabled(false);
                mScaleXAnim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        super.onAnimationCancel(animation);
                        setEnabled(true);
                        if(mListener != null) mListener.onAnimationCancel();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        setEnabled(true);
                        setClickable(true);
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        if(mListener != null) mListener.onAnimationStart();
                    }
                });

            }
        });
    }

    private void resetView() {
        setCardBackgroundColor(Color.TRANSPARENT);
        removeAllViews();
        startTick(false);
    }

    private void startTick(boolean start) {
        if (!start) {
            mHandler.removeCallbacksAndMessages(null);
            return;
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mListener != null) mListener.onAnimationEnd();
                setVisibility(GONE);
            }
        }, mDelayAfterAnimator);
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
