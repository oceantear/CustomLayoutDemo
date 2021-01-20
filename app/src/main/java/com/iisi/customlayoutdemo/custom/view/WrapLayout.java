package com.iisi.customlayoutdemo.custom.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class WrapLayout extends ViewGroup implements View.OnClickListener,View.OnTouchListener {

    private OnWrapLayoutClick mListener;
    private ArrayList<String> mContent;

    public WrapLayout(Context context) {
        super(context);
        Log.e("iisi","1");
        mContent = new ArrayList<>();
    }

    public WrapLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e("iisi","2");
        mContent = new ArrayList<>();
    }

    public WrapLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e("iisi","3");
        mContent = new ArrayList<>();
    }

    public void setClickListener(OnWrapLayoutClick ls){
        this.mListener = ls;
    }

    public interface OnWrapLayoutClick{
        void onItemClick(ArrayList<String> content);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("iisi","WrapLayout onMeasure");
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int maxHeight = getWrapHeight(widthMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), maxHeight);

    }

    private int getWrapHeight(int widthMeasureSpec) {
        WrapLayoutParams params;
        int row = 1;
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec); //layout擁有最大的width
        int occupancyWidth = 0; //佔用的width
        int occupancyHeight = 0; //佔用的height

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            params = (WrapLayoutParams) child.getLayoutParams();
//            除了本身的width,還需加上左右兩邊的margin
            int childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
//             除了本身的height,還需加上上下兩邊的margin
            int childHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            occupancyWidth += childWidth;
//            if 超過最大的寬度,將child分配到下一行
            if (occupancyWidth > maxWidth) {
                row++;
                occupancyWidth = childWidth;
            }
            occupancyHeight = childHeight * row;
        }

        return occupancyHeight;
    }

    /**
     * @param changed This is a new size or position for this view
     * @param left    Left position, relative to parent
     * @param top     Top position, relative to parent
     * @param right   Right position, relative to parent
     * @param bottom  Bottom position, relative to parent
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.e("iisi","WrapLayout onLayout");
        WrapLayoutParams params;
        int row = 0;
        int occupancyWidthPostion = left;
        int occupancyHeightPostion = top;
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            child.setOnClickListener(this);
            child.setOnTouchListener(this);
            params = (WrapLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int childHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            if (occupancyWidthPostion + childWidth > right) {
                row++;
                occupancyWidthPostion = left;
            }
            occupancyHeightPostion = row * childHeight;
            /**
             * layout(left,top,right,bottom)
             * left = 左邊座標＝目前佔用的左座標+leftmargin
             * top(距離parent top的座標) = 目前佔用的top座標(隨著row增加會增加)
             * right ＝左邊座標＋child's width
             * bottom = 目前佔用的top座標＋child's height
             */
            child.layout(occupancyWidthPostion + params.leftMargin, occupancyHeightPostion,
                    occupancyWidthPostion + childWidth, occupancyHeightPostion + childHeight);
            occupancyWidthPostion += childWidth;
        }
    }

    /**
     * if you want to get item margin, need to override this method(需要計算margin時)
     *
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new WrapLayoutParams(getContext(), attrs);
    }

    @Override
    public void onClick(View v) {
        LinearLayout layout = (LinearLayout)v;
        TextView tv = (TextView)layout.getChildAt(0);
        String str = tv.getText().toString();
        if(v.isSelected()) {//unslelected
            v.setSelected(false);
            tv.setTextColor(Color.parseColor("#9fa4ac"));
            mContent.remove(str);
        }else {
            v.setSelected(true);
            tv.setTextColor(Color.parseColor("#1da6d0"));
            mContent.add(str);
        }
        if(mListener != null)
            mListener.onItemClick(mContent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.e("iisi","ACTION_DOWN");
                v.setScaleX(0.7f);
                break;
            case MotionEvent.ACTION_UP:
                Log.e("iisi","ACTION_UP");
                v.setScaleX(1f);
                break;
        }
        return false;
    }

    public class WrapLayoutParams extends MarginLayoutParams {

        public WrapLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}

