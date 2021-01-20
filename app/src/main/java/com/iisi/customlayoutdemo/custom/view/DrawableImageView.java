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
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import java.util.ArrayList;
import java.util.List;

public class DrawableImageView extends AppCompatImageView implements View.OnTouchListener
{
    float downx = 0;
    float downy = 0;
    float upx = 0;
    float upy = 0;

    private Canvas canvas;
    private Paint paint;
    private Matrix matrix;
    private List<Path> mDrawPath;
    private boolean mIsEditable = false;

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
        //this.canvas = canvas;
    }

    public void init(){
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new CornerPathEffect(100));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPath = new ArrayList<>();
    }

    public void setNewImage(Bitmap alteredBitmap, Bitmap bmp)
    {
        canvas = new Canvas(alteredBitmap );//新的bitmap
        /*paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(5);*/
        matrix = new Matrix();
        canvas.drawBitmap(bmp, matrix, paint);

        setImageBitmap(alteredBitmap);
    }

    public void setImage(Bitmap bmp){
        Bitmap bp = bmp.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap alteredBitmap = Bitmap.createBitmap(bp.getWidth(),
                bp.getHeight(), bp.getConfig());

        canvas = new Canvas(alteredBitmap);
        matrix = new Matrix();
        canvas.drawBitmap(bp, matrix, paint);

        setImageBitmap(alteredBitmap);
    }

    public void setImage(int resId){
        setImageResource(resId);
        Bitmap bp = ((BitmapDrawable)getDrawable()).getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        Bitmap alteredBitmap = Bitmap.createBitmap(bp.getWidth(),
                bp.getHeight(), bp.getConfig());
        canvas = new Canvas(alteredBitmap);

        matrix = new Matrix();

        canvas.drawBitmap(bp, matrix, paint);

        setImageBitmap(alteredBitmap);

    }

    public Bitmap getBitmap(){
        return ((BitmapDrawable)getDrawable()).getBitmap();
    }

    @SuppressLint("ResourceAsColor")
    public void setPaintColor(int color){
        if(paint != null)
            paint.setColor(getResources().getColor(color, null));
    }

    public void setPaintStroke(float strokeWidth){
        if(paint != null)
            paint.setStrokeWidth(strokeWidth);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if(mIsEditable) {
            int action = event.getAction();

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    downx = getPointerCoords(event)[0];//event.getX();
                    downy = getPointerCoords(event)[1];//event.getY();
                    //downx = event.getX();
                    //downy = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    upx = getPointerCoords(event)[0];//event.getX();
                    upy = getPointerCoords(event)[1];//event.getY();
                    //upx = event.getX();
                    //upy = event.getY();
                    //Path p = new Path();
                    //p.lineTo(downx, downy);
                    //p.lineTo(upx, upy);
                    //canvas.drawPath(p, paint);
                    canvas.drawLine(downx, downy, upx, upy, paint);
                    invalidate();
                    downx = upx;
                    downy = upy;
                    break;
                case MotionEvent.ACTION_UP:
                    upx = getPointerCoords(event)[0];//event.getX();
                    upy = getPointerCoords(event)[1];//event.getY();
                    //upx = event.getX();
                    //upy = event.getY();
                    //Path p1 = new Path();
                    //p1.lineTo(downx, downy);
                    //p1.lineTo(upx, upy);
                    //canvas.drawPath(p1, paint);
                    canvas.drawLine(downx, downy, upx, upy, paint);
                    invalidate();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    break;
                default:
                    break;
            }

        }
        return true;
    }

    final float[] getPointerCoords(MotionEvent e)
    {
        final int index = e.getActionIndex();
        final float[] coords = new float[] { e.getX(index), e.getY(index) };
        Matrix matrix = new Matrix();
        //Log.e("iisi","matrix :　"+matrix.toString());
        //Log.e("iisi","ImageMatrix :　"+getImageMatrix());
        getImageMatrix().invert(matrix);
        //Log.e("iisi","invert ImageMatrix :　"+matrix.toString());
        //Log.e("iisi","getScrollX :　"+getScrollX());
        //Log.e("iisi","getScrollY :　"+getScrollY());
        matrix.postTranslate(getScrollX(), getScrollY());
        matrix.mapPoints(coords);
        return coords;
    }

    public void setIsEditable(boolean isEditable){mIsEditable = isEditable ;}

}
