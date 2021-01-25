package com.iisi.customlayoutdemo.custom.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
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

import static android.view.MotionEvent.ACTION_POINTER_INDEX_MASK;

public class PostTranslateImageView extends AppCompatImageView implements View.OnTouchListener {

    private final static int MODE_DRAW = 0;
    private final static int MODE_DRAG = 1;
    private int mMode = MODE_DRAW;

    private Matrix matrixNow = new Matrix();
    private PointF midPoint = new PointF(0,0);
    private Bitmap bp;
    private Paint mDrawableImagePaint;
    private PointF startPoint = new PointF();
    private PointF lastPoint = new PointF();
    private float totalTranslateX = 0;
    private float totalTranslateY = 0;
    private float totalRatio = 1f;
    private Paint paint;
    private Path mPath;
    private List<Path> mHistoryPath;
    private List<Pair<Path, Paint>> mHistoryPathInfo;
    private float startDis;
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
        canvas.translate(totalTranslateX, totalTranslateY);
        canvas.scale(totalRatio, totalRatio);
        //canvas.drawColor(Color.GREEN);
        if(bp != null ) {
            /*if(matrixNow != null)
                canvas.drawBitmap(bp, matrixNow, mDrawableImagePaint);
            else
                canvas.drawBitmap(bp, midPoint.x, midPoint.y, mDrawableImagePaint);*/
            canvas.drawBitmap(bp, 0, 0, null);
        }

        canvas.drawPath(mPath, paint);

        for(Pair<Path, Paint> p : mHistoryPathInfo) {
            canvas.drawPath(p.first, p.second);
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

        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new CornerPathEffect(100));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        //mPaintColor = Color.BLACK;
        //mPaintStrokeWidth = paint.getStrokeWidth();
        mPath = new Path();
        mHistoryPathInfo = new ArrayList<>();
        mHistoryPath = new ArrayList<>();
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


    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        x = (x - totalTranslateX) / totalRatio;
        y = (y - totalTranslateY) / totalRatio;

        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        Log.e("jimmy","touch_move : totalRatio : "+totalRatio + " totalTranslateX :"+totalTranslateX +" totalTranslateY: "+totalTranslateY);
        x = (x - totalTranslateX) / totalRatio;
        y = (y - totalTranslateY) / totalRatio;
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
        //IsDrawNowPath = true;
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        mHistoryPath.add(mPath);
        mHistoryPathInfo.add(new Pair<Path, Paint>(mPath, paint));
        mPath = new Path();
        //resetPaint();
    }

    private void calculationRealPoint(Point point, Matrix matrix) {
        float[] values = new float[9];
        matrix.getValues(values);
        int sX = point.x;
        int sY = point.y;
        point.x = (int) ((sX - values[Matrix.MTRANS_X]) / values[Matrix.MSCALE_X]);
        point.y = (int) ((sY - values[Matrix.MTRANS_Y]) / values[Matrix.MSCALE_Y]);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        float x = event.getX();
        float y = event.getY();


            int action = event.getAction();

            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mMode = MODE_DRAW;
                    touch_start(x, y);
                    Log.e("iisi","ACTION_DOWN mode : ");
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    Log.e("iisi","ACTION_POINTER_DOWN : "+(event.getAction() >> MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                    mMode = MODE_DRAG;
                    startPoint.set(event.getX(), event.getY());
                    startDis = distance(event);
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
                    if(mMode == MODE_DRAW){
                        touch_move(x, y);
                        invalidate();
                    }else if(mMode == MODE_DRAG) {
                        if (event.getPointerCount() >= 2) {

                            PointF nowCenterPoint = new PointF(event.getX(), event.getY());

                            float cX = 0.0f, cY = 0.0f;

                            cX = nowCenterPoint.x - lastPoint.x;
                            cY = nowCenterPoint.y - lastPoint.y;
                            totalTranslateX += cX;
                            totalTranslateY += cY;

                            float endDis = distance(event);
                            float current_scale = endDis / startDis;//缩放倍数
                            totalRatio *= current_scale;

                            startDis = endDis;

                            Log.e("jimmy", "start x : " + lastPoint.x + " start y : " + lastPoint.y);
                            Log.e("jimmy", "now x : " + nowCenterPoint.x + " now y : " + nowCenterPoint.y);
                            Log.e("jimmy", "cX : " + cX + " cY : " + cY);

                            lastPoint.set(event.getX(), event.getY());
                            //matrixNow.postTranslate(cX, cY);
                            invalidate();
                        } else if (event.getPointerCount() > 1) {

                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.e("iisi","ACTION_UP : ");
                    if(mMode == MODE_DRAW)
                        touch_up();
                    else if(mMode == MODE_DRAG) {
                        mMode = MODE_DRAW;
                        lastPoint = new PointF();
                    }
                    break;



                case MotionEvent.ACTION_POINTER_UP:
                    Log.e("iisi","ACTION_POINTER_UP : " +(event.getAction() >> MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                    if(mMode == MODE_DRAG) {
                        if (event.getPointerCount() > 2) {
                            if (event.getAction() >> 8 == 0) {
                                lastPoint.x = event.getX(1);
                                lastPoint.y = event.getY(1);
                            }
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

    private float distance(float x0, float y0, float x1, float y1){
        float dx = 0, dy = 0;
        dx = x1 - x0;
        dy = y1 - y0;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
