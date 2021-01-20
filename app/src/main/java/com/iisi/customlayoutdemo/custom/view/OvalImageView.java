package com.iisi.customlayoutdemo.custom.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class OvalImageView extends View {

    //屏幕大小
    private int screenHeight, screenWidth;
    private int radius;
    private Rect center;
    private RectF centerF;


    public OvalImageView(Context context) {
        super(context);
    }

    public OvalImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public OvalImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public OvalImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    /*@Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        getMeasuredHeight()
        int childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
        int childHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
    }*/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawOval(canvas);
    }

    public Rect getCenterRect() {
        return center;
    }

    public Bitmap loadBitmapFromView() {
        Log.e("iisi","loadBitmapFromView width : "+this.getWidth());
        Log.e("iisi","loadBitmapFromView height : "+this.getHeight());
        Bitmap b = Bitmap.createBitmap( this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        this.layout(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
        this.draw(c);
        return b;
    }

    private void initView(Context context){
        screenHeight = MaskView.DimenUtil.getScreenSize(context).heightPixels;
        screenWidth = MaskView.DimenUtil.getScreenSize(context).widthPixels;
        radius = MaskView.DimenUtil.dip2px(context, 10);
        int centerWidth = (int) Math.round(screenWidth * 0.9);
        int centerHeight = (int) Math.round(screenHeight * 0.3);
        center = getCenterRect(centerHeight, centerWidth);
        centerF = new RectF(center);
    }

    private void drawOval(Canvas canvas){


        Paint p = new Paint();
        //p.setColor(Color.RED);// 设置红色
        p.setARGB(119, 0, 0, 0);

        Path mPath = new Path();
        mPath.reset();

        RectF oval2 = new RectF(60, 100, 200, 240);// 设置个新的长方形，扫描测量
        //canvas.drawArc(oval2, 200, 130, true, p);
        // 画弧，第一个参数是RectF：该类是第二个参数是角度的开始，第三个参数是多少度，第四个参数是真的时候画扇形，是假的时候画弧线
        //画椭圆，把oval改一下
        oval2.set(0,0,250,800);
        //canvas.drawOval(oval2, p);



        //mPath.addOval(centerF, Path.Direction.CW);
        //mPath.addCircle(Math.round(screenWidth * 0.5), Math.round(screenHeight * 0.5), Math.round(screenWidth * 0.5), Path.Direction.CW);
        mPath.addRoundRect(centerF, radius, radius, Path.Direction.CW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas.clipOutPath(mPath);
        } else {
            canvas.clipPath(mPath, Region.Op.XOR);
        }
        canvas.drawRect(0, 0, 1080, 1920, p);
    }

    private Rect getCenterRect(int height, int width) {
        Rect rect = new Rect();
        int left = (this.screenWidth - width) / 2;
        int top = (int) Math.round(this.screenHeight * 0.2);
        rect.set(left, top, left + width, top + height);

        return rect;
    }

    public static class DimenUtil {

        //dp转px
        public static int dip2px(Context context, int dp) {
            float density = context.getResources().getDisplayMetrics().density;
            return (int) (dp * density + 0.5f);
        }

        //px转dp
        public static int px2dip(Context context, int px) {
            float density = context.getResources().getDisplayMetrics().density;
            return (int) (px / density + 0.5f);
        }

        //获取屏幕大小（px）
        public static DisplayMetrics getScreenSize(Context context) {
            return context.getResources().getDisplayMetrics();
        }
    }
}
