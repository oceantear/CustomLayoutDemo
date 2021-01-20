package com.iisi.customlayoutdemo.custom.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import com.iisi.customlayoutdemo.vo.FlowLayoutContent;
import com.iisi.customlayoutdemo.R;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup implements View.OnClickListener, View.OnTouchListener {

    private OnWrapLayoutClick mListener;
    private ArrayList<FlowLayoutContent> mContent;
    private ArrayList<String> mSelectedContent;
    private int mDefaultLayout = R.layout.default_layout_item;
    private boolean mClickable;
    private boolean mEnableMutiChoose;
    private float mScaleX;
    private ColorStateList mTextColor;
    private int mTextBackground;
    private LayoutInflater inflater;
    private Resources r;

    public FlowLayout(Context context) {
        super(context);
        Log.e("iisi", "FlowLayout");
        mContent = new ArrayList<>();
        mSelectedContent = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    @SuppressLint("ResourceType")
    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e("iisi", "FlowLayout");
        mContent = new ArrayList<>();
        mSelectedContent = new ArrayList<>();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WrapLayout);
        mClickable = ta.getBoolean(R.styleable.WrapLayout_clickable, false);
        mEnableMutiChoose = ta.getBoolean(R.styleable.WrapLayout_enableMultiChoose, false);
        mTextColor = ta.getColorStateList(R.styleable.WrapLayout_textColor);
        if(mTextColor == null)
            mTextColor = ContextCompat.getColorStateList(context, R.color.wraplayout_default_text_color);

        mTextBackground = ta.getResourceId(R.styleable.WrapLayout_textBackground, R.drawable.wraplayout_default_bg);
        mScaleX = ta.getFloat(R.styleable.WrapLayout_scaleX, 1f);
        inflater = LayoutInflater.from(context);
        ta.recycle();
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e("iisi", "FlowLayout");
        mContent = new ArrayList<>();
        mSelectedContent = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    private void initView(){
        Log.e("iisi", "initView");
        removeAllViews();
        for (int i = 0; i < mContent.size(); i++) {
            inflater.inflate(mDefaultLayout, this, true);
            View v = getChildAt(i);
            TextView tv = v.findViewById(R.id.name);
            tv.setText(mContent.get(i).name);
        }

        /*for(int i = 0; i < getChildCount() ; i++){
            View v = getChildAt(i);
            TextView tv = v.findViewById(R.id.name);
            tv.setText(mContent.get(i));
        }*/
    }

    public void setClickListener(OnWrapLayoutClick ls){
        this.mListener = ls;
    }

    public interface OnWrapLayoutClick{
        void onItemClick(ArrayList<String> content);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("iisi", "onMeasure");
        int maxHeight = 0;
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        if(mContent.size() > 0)
            maxHeight = getWrapHeight(widthMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), maxHeight);
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
        Log.e("iisi", "onLayout");
        FlowLayoutParams params;
        int row = 0;
        int occupancyWidthPostion = left;
        int occupancyHeightPostion = top;
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            child.setOnClickListener(this);
            child.setOnTouchListener(this);
            child.setSelected(mContent.get(i).isSelected);
            LinearLayout layout = (LinearLayout) child;
            TextView tv = (TextView) layout.getChildAt(0);
            //child.setBackgroundResource(mTextBackground);
            tv.setBackgroundResource(mTextBackground);
            tv.setTextColor(mTextColor);
            if(child.isSelected()){
                String str = tv.getText().toString();
                mSelectedContent.add(str);
            }
            params = (FlowLayoutParams)child.getLayoutParams();
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

    private int getWrapHeight(int widthMeasureSpec) {
        FlowLayoutParams params;
        int row = 1;
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec); //layout擁有最大的width
        int occupancyWidth = 0; //佔用的width
        int occupancyHeight = 0; //佔用的height

        Log.e("iisi","getWrapHeight : getChildCount() = "+getChildCount());

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            params = (FlowLayoutParams) child.getLayoutParams();
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

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new FlowLayoutParams(getContext(), attrs);
    }

    @Override
    public void onClick(View v) {
        Log.e("iisi","onClick");

        if(!mEnableMutiChoose){
            resetSelectedStatus();
        }

        LinearLayout layout = (LinearLayout) v;
        TextView tv = (TextView) layout.getChildAt(0);
        String str = tv.getText().toString();
        //LogUtil.e("iisi","onTouch : "+str);
        if (v.isSelected()) {//now is selected
            v.setSelected(false);
            tv.setSelected(false);
            mSelectedContent.remove(str);
        } else {
            v.setSelected(true);
            tv.setSelected(true);
            mSelectedContent.add(str);
        }

        //refreshDrawableState();

        /*LogUtil.e("iisi","item size : "+getChildCount());
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            LinearLayout l = (LinearLayout) child;
            LogUtil.e("iisi",i +" isSelected : "+l.isSelected());
        }*/

        if (mListener != null)
            mListener.onItemClick(mSelectedContent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.e("iisi","ACTION_DOWN");
                v.setScaleX(mScaleX);
                break;
            case MotionEvent.ACTION_UP:
                Log.e("iisi","ACTION_UP");
                v.setScaleX(1f);
                v.performClick();
                break;
        }
        return true;
    }


    public class FlowLayoutParams extends MarginLayoutParams {

        public FlowLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private void resetSelectedStatus(){
        Log.e("iisi", "resetSelectedStatus");
        mSelectedContent.clear();
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            LinearLayout layout = (LinearLayout) child;
            if(layout.isSelected())
                layout.setSelected(false);
        }
    }

    public void setLayout(int layout){
        mDefaultLayout = layout;
    }

    public void setContent(List<FlowLayoutContent> content){
        Log.e("iisi","setContent");
        if(mContent.size() > 0) {
            mContent.clear();
            mContent.addAll(content);
            mSelectedContent.clear();
        }else
            mContent.addAll(content);
        initView();
        requestLayout();
        invalidate();
    }
}

