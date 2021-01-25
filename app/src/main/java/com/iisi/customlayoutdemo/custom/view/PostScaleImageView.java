package com.iisi.customlayoutdemo.custom.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import java.util.ArrayList;
import java.util.List;

public class PostScaleImageView extends AppCompatImageView implements View.OnTouchListener {

    private static final int DRAW_MODE = 0;
    private static final int SCALE_MODE = 1;
    private int mMode = DRAW_MODE;


    private PointF midPoint = new PointF(0,0);
    private Bitmap bp;
    private Paint mDrawableImagePaint;
    private PointF startPoint = new PointF();
    private PointF lastPoint = new PointF();

    private Matrix matrixNow;
    private Matrix matrixBefore = new Matrix();
    private PointF nowPoint = new PointF();
    private PointF scaleCenter = new PointF();
    private PointF startScalePoint = new PointF();
    private float startDis;
    private float scaleBase = 1f;
    private float current_scale;
    private float total_scale = 1f;

    private Path mPath;
    private List<Path> mHistoryPath;
    private List<Pair<Path, Paint>> mHistoryPathInfo;
    private Paint paint;


    public PostScaleImageView(@NonNull Context context) {
        super(context);
        init();
    }

    public PostScaleImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PostScaleImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

        for(Pair<Path, Paint> p : mHistoryPathInfo) {
            canvas.drawPath(p.first, p.second);
        }

        canvas.drawPath(mPath, paint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        midPoint.set( w / 2, h / 2);//初始化缩放位置
    }

    private void init(){
        mDrawableImagePaint = new Paint();
        setOnTouchListener(this);

        mPath = new Path();
        mHistoryPath = new ArrayList<>();
        mHistoryPathInfo = new ArrayList<>();
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new CornerPathEffect(100));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    public void setBitmap(Bitmap bmp){
        if(matrixNow == null)
            matrixNow = new Matrix();
        bp = bmp;
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
                mMode = DRAW_MODE;
                touch_start(x, y);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.e("iisi","ACTION_POINTER_DOWN : ");
                mMode = SCALE_MODE;

                if(event.getPointerCount() >= 2){
                    scaleCenter.set(mid(event));
                    startScalePoint.set(event.getX(), event.getY());
                    startDis = distance(event);

                    if (startDis > 10f) {
                        //紀錄縮放倍數
                        //matrixBefore.set(getImageMatrix());
                        //matrixNow.set(getImageMatrix());
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("iisi","ACTION_MOVE : ");

                if(mMode == DRAW_MODE){
                    touch_move(x, y);
                    invalidate();
                }else if(mMode == SCALE_MODE) {
                    if (event.getPointerCount() >= 2) {
                        midPoint = mid(event);
                        float endDis = distance(event);
                        float moveDis = Math.abs(distance(event) - startDis);
                        if (moveDis > 10f) {
                            current_scale = (endDis / startDis) * scaleBase;
                            total_scale *= current_scale;
                            Log.e("jimmy", "move dis : " + moveDis);
                            Log.e("jimmy", "current_scale : " + current_scale);
                            Log.e("jimmy", "scaleCenter x : " + midPoint.x + " scaleCenter y : " + midPoint.y);
                            matrixNow.postScale(current_scale, current_scale, midPoint.x, midPoint.y);
                            startDis = endDis;
                            invalidate();
                        }
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                Log.e("iisi","ACTION_UP : ");
                if(mMode == DRAW_MODE)
                    touch_up();
                else if(mMode == SCALE_MODE){
                    mMode = DRAW_MODE;
                    lastPoint = new PointF();
                }
            case MotionEvent.ACTION_POINTER_UP:
                Log.e("iisi","ACTION_POINTER_UP : ");
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
        //IsDrawNowPath = true;
    }

    private void resetPaint(){
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        paint.setAlpha(255);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new CornerPathEffect(100));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        mHistoryPath.add(mPath);
        mHistoryPathInfo.add(new Pair<Path, Paint>(mPath, paint));
        mPath = new Path();
        resetPaint();
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

    private float distance(MotionEvent event) {
        float dx = 0, dy = 0;
        try {
            dx = event.getX(1) - event.getX(0);
            dy = event.getY(1) - event.getY(0);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
