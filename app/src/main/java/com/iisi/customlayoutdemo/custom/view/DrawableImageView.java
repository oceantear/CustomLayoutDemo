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
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.widget.AppCompatImageView;
import java.util.ArrayList;
import java.util.List;

public class DrawableImageView extends AppCompatImageView implements View.OnTouchListener
{
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

        canvas.drawBitmap(bp, 0, 0, mDrawableImagePaint);

        if(IsDrawNowPath){
            canvas.drawPath(mPath, paint);
            IsDrawNowPath = false;
        }

        for(Pair<Path, Paint> p : mHistoryPathInfo) {
            canvas.drawPath(p.first, p.second);
        }

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

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    break;
                default:
                    break;
            }

        }
        return true;
    }
}
