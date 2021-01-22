package com.iisi.customlayoutdemo.custom.view;

import android.annotation.SuppressLint;
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
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;
import java.util.List;

public class DrawableImageView extends AppCompatImageView implements View.OnTouchListener
{
    private  static final int MODE_DRAW = 1;
    private static final int MODE_ZOOM_SCALE = 2;
    private int mode = 0;//normal mode

    private Canvas mCanvas;
    private Paint paint;
    private Paint mDrawableImagePaint;
    private Matrix matrix;
    private Path mPath;
    private List<Path> mHistoryPath;
    private List<Pair<Path, Paint>> mHistoryPathInfo;
    private List<Pair<Path, Paint>> mUnDoHistoryPathInfo;
    private Bitmap bp;
    private boolean mIsEditable = false;
    private int mPaintColor;
    private float mPaintStrokeWidth;
    private int mPaintAlpha = 255;
    private boolean IsDrawNowPath;

    private float startDis;

    private Matrix matrixNow = new Matrix();
    private Matrix matrixBefore = new Matrix();
    private PointF startPoint = new PointF();
    private PointF nowPoint = new PointF();
    private PointF midPoint = new PointF(0,0);
    private Matrix mInitializationMatrix = new Matrix();//初始缩放值
    private PointF mInitializationScalePoint = new PointF();//初始缩放点
    private PointF mCurrentScalePoint = new PointF(0, 0);//当前缩放点

    private static final float MAX_SCALE = 4f, MIN_SCALE = 1f;//最大放大倍数，最小放大倍数
    float total_scale = MIN_SCALE , current_scale;//total_scale缩放范围2-1，当小于1回弹到1；当大于2回弹到2

    public DrawableImageView(Context context)
    {
        super(context);
        setOnTouchListener(this);
        init();
    }

    public DrawableImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setOnTouchListener(this);
        init();
    }

    public DrawableImageView(Context context, AttributeSet attrs,
                             int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(matrixNow != null){
            canvas.drawBitmap(bp, matrixNow, mDrawableImagePaint);
        }else
            canvas.drawBitmap(bp, 0, 0, mDrawableImagePaint);

        if(IsDrawNowPath){
            canvas.drawPath(mPath, paint);
            IsDrawNowPath = false;
        }

        for(Pair<Path, Paint> p : mHistoryPathInfo) {
            canvas.drawPath(p.first, p.second);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("iisi", "onSizeChanged width : "+w +" height : "+h);
        mInitializationScalePoint.set( w / 2, h / 2);//初始化缩放位置

    }

    public void init(){

        mDrawableImagePaint = new Paint();

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new CornerPathEffect(100));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        mPaintColor = Color.BLACK;
        mPaintStrokeWidth = paint.getStrokeWidth();
        mPath = new Path();
        mHistoryPath = new ArrayList<>();
        mHistoryPathInfo = new ArrayList<>();
        mUnDoHistoryPathInfo = new ArrayList<>();
    }

    public void setImage(Bitmap bmp){
        bp = bmp;
    }

    public Bitmap getBitmap(){
        Bitmap bmp = Bitmap.createBitmap(bp.getWidth(),
                bp.getHeight(), bp.getConfig());
        Canvas cv = new Canvas(bmp);
        cv.drawBitmap(bp, 0, 0, paint);
        for(Pair<Path, Paint> p : mHistoryPathInfo) {
            cv.drawPath(p.first, p.second);
        }
        return bmp;
    }

    public void addText(){

    }

    @SuppressLint("ResourceAsColor")
    public void setPaintColor(int color){
        if(paint != null) {
            mPaintColor = getResources().getColor(color, null);
            paint.setColor(mPaintColor);
        }
    }

    public void setPaintStroke(float strokeWidth){
        if(paint != null) {
            paint.setStrokeWidth(strokeWidth);
            mPaintStrokeWidth = strokeWidth;
        }
    }

    public void setPaintAlpha(int alpha){
        if(paint != null) {
            mPaintAlpha = alpha;
            paint.setAlpha(mPaintAlpha);
        }
    }

    public void unDo(){
        if(mHistoryPathInfo.size() > 0) {
            mUnDoHistoryPathInfo.add(mHistoryPathInfo.remove(mHistoryPathInfo.size() - 1));
            invalidate();
        }
    }

    public void reDo(){
        if(mUnDoHistoryPathInfo.size() > 0){
            mHistoryPathInfo.add(mUnDoHistoryPathInfo.remove(mUnDoHistoryPathInfo.size() - 1));
            invalidate();
        }
    }

    public void cleanCanvas(){
        mHistoryPathInfo.clear();
        invalidate();
    }

    public void scaleSize(float scale_size){
        matrixNow.postScale(scale_size, scale_size, midPoint.x, midPoint.y);
        invalidate();
    }

    public void setIsEditable(boolean isEditable){mIsEditable = isEditable ;}

    private void resetPaint(){
        paint = new Paint();
        paint.setColor(mPaintColor);
        paint.setStrokeWidth(mPaintStrokeWidth);
        paint.setAlpha(mPaintAlpha);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new CornerPathEffect(100));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
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
        IsDrawNowPath = true;
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        mHistoryPath.add(mPath);
        mHistoryPathInfo.add(new Pair<Path, Paint>(mPath, paint));
        mPath = new Path();
        resetPaint();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();

        if(mIsEditable) {
            int action = event.getAction();

            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    Log.e("iisi","ACTION_DOWN mode : "+mode);
                    mode = MODE_DRAW;
                    touch_start(x, y);
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    Log.e("iisi","ACTION_POINTER_DOWN : "+mode);
                    mode = MODE_ZOOM_SCALE;
                    startPoint.set(event.getX(), event.getY());
                    startDis = distance(event);
                    if (startDis > 10f) {
                        //紀錄縮放倍數
                        matrixBefore.set(getImageMatrix());
                        matrixNow.set(getImageMatrix());
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.e("iisi","ACTION_MOVE : "+mode);
                    if(mode == MODE_DRAW) {
                        touch_move(x, y);
                        invalidate();
                    }else if(mode == MODE_ZOOM_SCALE && event.getPointerCount() >= 2 ){
                        nowPoint.set(event.getX(), event.getY());
                        float endDis = distance(event);
                        float moveDis = Math.abs(endDis - startDis);
                        Log.e("iisi","start dis : "+ startDis + " end dis : " + endDis +" move distance : "+moveDis);

                        if (moveDis > 10f) {
                            midPoint = mid(event);
                            current_scale = endDis / startDis;//缩放倍数
                            total_scale *= current_scale;
                            Log.e("iisi","total scale : "+total_scale + " current scale : "+current_scale);
                            Log.e("iisi","mid x : "+midPoint.x + " mid y : "+midPoint.y);
                            //matrixNow.postTranslate((midPoint.x + event.getX()) /2, (midPoint.y + event.getY()) /2);
                            matrixNow.postScale(current_scale, current_scale, midPoint.x, midPoint.y);
                            startDis = endDis;
                            invalidate();
                        }
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    Log.e("iisi","ACTION_UP : "+mode);
                    if(mode == MODE_DRAW)
                        touch_up();
                    else if(mode == MODE_ZOOM_SCALE)
                        mode = MODE_DRAW;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    Log.e("iisi","ACTION_POINTER_UP : "+mode);
                    if(mode == MODE_ZOOM_SCALE) {
                        //checkZoomValid();
                        /*postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mode = MODE_DRAG;
                            }
                        },500);*/

                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    break;
                default:
                    break;
            }

        }
        return true;
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

    private boolean checkZoomValid() {
        if(mode == MODE_ZOOM_SCALE){
            if(total_scale > MAX_SCALE){
                Log.e("iisi", "大於 最大scale");
                resetToMaxStatus();
                matrixNow.set(mInitializationMatrix);
                matrixNow.postScale(MAX_SCALE, MAX_SCALE, mInitializationScalePoint.x, mInitializationScalePoint.y);
                matrixNow.postTranslate(mCurrentScalePoint.x, mCurrentScalePoint.y);
                invalidate();
                return false;
            }else if(total_scale < MIN_SCALE){
                Log.e("iisi", "小於 最小scale");
                resetToMinStatus();
                matrixNow.set(mInitializationMatrix);
                invalidate();
                return false;
            }
            invalidate();
        }
        return true;
    }


    //最小重置数值
    private void resetToMinStatus(){
        mCurrentScalePoint.set(0, 0);
        total_scale = MIN_SCALE;
    }
    //最大重置数值
    private void resetToMaxStatus(){
        total_scale = MAX_SCALE;
    }
}
