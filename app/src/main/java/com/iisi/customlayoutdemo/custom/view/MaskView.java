package com.iisi.customlayoutdemo.custom.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.Nullable;

public class MaskView extends View {
    //相机遮罩框外面的线，阴影区域
    private Paint border, area;
    //相机遮罩框中间透明区域
    private Rect center;
    private RectF centerF;
    //屏幕大小
    private int screenHeight, screenWidth;
    private int radius;

    private Bitmap mBitmap;
    private Paint drawBitmap;

    public MaskView(Context context) {
        super(context);
    }

    public MaskView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //setAlpha一定要在setStyle后面，否则不起作用
        border = new Paint(Paint.ANTI_ALIAS_FLAG);
        border.setColor(Color.RED);
        border.setStyle(Paint.Style.STROKE);
        border.setStrokeWidth(DimenUtil.dip2px(context, 3));
        border.setAlpha(255);

        drawBitmap = new Paint(Paint.ANTI_ALIAS_FLAG);
        drawBitmap.setFilterBitmap(true);
        drawBitmap.setDither(true);

        area = new Paint(Paint.ANTI_ALIAS_FLAG);
        area.setStyle(Paint.Style.FILL);
        area.setColor(Color.GRAY);
        area.setAlpha(180);

        screenHeight = DimenUtil.getScreenSize(context).heightPixels;
        screenWidth = DimenUtil.getScreenSize(context).widthPixels;
        int centerWidth = (int) Math.round(screenWidth * 0.9);
        int centerHeight = (int) Math.round(screenHeight * 0.3);
        radius = DimenUtil.dip2px(context, 10);
        center = getCenterRect(centerHeight, centerWidth);
        centerF = new RectF(center);
    }

    public MaskView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MaskView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private Rect getCenterRect(int height, int width) {
        Rect rect = new Rect();
        int left = (this.screenWidth - width) / 2;
        int top = (int) Math.round(this.screenHeight * 0.2);
        rect.set(left, top, left + width, top + height);

        return rect;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //陰影區域
        /*canvas.drawRect(0, 0, screenWidth, center.top, area);
        canvas.drawRect(0, center.bottom, screenWidth, screenHeight, area);
        canvas.drawRect(0, center.top, center.left, center.bottom, area);
        canvas.drawRect(center.right, center.top, screenWidth, center.bottom, area);
        //遮住圓角外面部分
        canvas.drawRect(center.left, center.top, center.left + radius / 2, center.top + radius / 2, area);
        canvas.drawRect(center.left, center.bottom - radius / 2, center.left + radius / 2, center.bottom, area);
        canvas.drawRect(center.right - radius / 2, center.top, center.right, center.top + radius / 2, area);
        canvas.drawRect(center.right - radius / 2, center.bottom - radius / 2, center.right, center.bottom, area);*/

        canvas.drawRoundRect(centerF, radius, radius, border);

        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap,center.left,center.top,drawBitmap);
        }
        postInvalidate();
    }

    public Bitmap getPreviewBitmap() {
        return mBitmap;
    }
    public Rect getCenterRect() {
        return center;
    }
    public void setPreviewBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
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
