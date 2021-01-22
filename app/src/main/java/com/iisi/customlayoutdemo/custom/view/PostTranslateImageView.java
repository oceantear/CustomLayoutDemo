package com.iisi.customlayoutdemo.custom.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import static android.view.MotionEvent.ACTION_POINTER_INDEX_MASK;

public class PostTranslateImageView extends AppCompatImageView implements View.OnTouchListener {


    private Matrix matrixNow = new Matrix();
    private PointF midPoint = new PointF(0,0);
    private Bitmap bp;
    private Paint mDrawableImagePaint;
    private PointF startPoint = new PointF();
    private PointF lastPoint = new PointF();
    //private PointF lastCenterPoint = new PointF();

    public PostTranslateImageView(@NonNull Context context) {
        super(context);
        init();
    }

    public PostTranslateImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PostTranslateImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(bp != null ) {
            if(matrixNow != null)
                canvas.drawBitmap(bp, matrixNow, mDrawableImagePaint);
            else
                canvas.drawBitmap(bp, midPoint.x, midPoint.y, mDrawableImagePaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        midPoint.set( w / 2, h / 2);//初始化缩放位置
    }

    private void init(){
        mDrawableImagePaint = new Paint();
        setOnTouchListener(this);
    }

    public void setBitmap(Bitmap bmp){
        bp = bmp;
        //setBitmap(bp);
    }

    public void postScale(float scale_size){
        if(matrixNow == null) {
            matrixNow = new Matrix();
            matrixNow.postTranslate(midPoint.x, midPoint.y);
        }
        matrixNow.postScale(scale_size, scale_size, midPoint.x, midPoint.y);
        invalidate();
    }

    public void postTranslate(float x, float y){
        if(matrixNow == null)
            matrixNow = new Matrix();
        matrixNow.postTranslate(x, y);
        invalidate();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        float x = event.getX();
        float y = event.getY();


            int action = event.getAction();

            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    Log.e("iisi","ACTION_DOWN mode : ");
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    Log.e("iisi","ACTION_POINTER_DOWN : "+(event.getAction() >> MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                    startPoint.set(event.getX(), event.getY());
                    if(lastPoint.x == 0.0f && lastPoint.y == 0.0f) {
                        lastPoint.x = event.getX();
                        lastPoint.y = event.getY();
                    }
                    if(event.getAction() >> 8 == 0){
                        lastPoint.x = event.getX();
                        lastPoint.y = event.getY();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.e("iisi","ACTION_MOVE : ");
                    if(event.getPointerCount() >= 2){

                        PointF nowCenterPoint = new PointF(event.getX(), event.getY());

                        float cX = 0.0f, cY = 0.0f;

                        cX = nowCenterPoint.x - lastPoint.x;
                        cY = nowCenterPoint.y - lastPoint.y;
                        Log.e("jimmy", "start x : " + lastPoint.x + " start y : " + lastPoint.y);
                        Log.e("jimmy", "now x : " + nowCenterPoint.x + " now y : " + nowCenterPoint.y);
                        Log.e("jimmy", "cX : " + cX + " cY : " + cY);

                        lastPoint.set(event.getX(), event.getY());
                        matrixNow.postTranslate(cX, cY);
                        invalidate();
                    }else if(event.getPointerCount() > 1){

                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.e("iisi","ACTION_UP : ");
                    lastPoint = new PointF();

                case MotionEvent.ACTION_POINTER_UP:
                    Log.e("iisi","ACTION_POINTER_UP : " +(event.getAction() >> MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                    if(event.getPointerCount() > 2 ) {
                        if(event.getAction() >> 8 == 0){
                            lastPoint.x = event.getX(1);
                            lastPoint.y = event.getY(1);
                        }
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    break;
                default:
                    break;
            }
        return true;
    }

    private PointF mid(MotionEvent event) {
        float midX = 0, midY = 0;
        try {
            midX = (event.getX(1) + event.getX(0)) / 2;
            midY = (event.getY(1) + event.getY(0)) / 2;
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        return new PointF(midX, midY);
    }

    public static float[] getCenterPoint(float x1, float y1, float x2, float y2) {
        return new float[]{(x1 + x2) / 2f, (y1 + y2) / 2f};
    }
}
