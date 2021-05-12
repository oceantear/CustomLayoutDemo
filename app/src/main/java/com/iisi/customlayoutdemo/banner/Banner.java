package com.iisi.customlayoutdemo.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

public class Banner<T,BA extends BannerAdapter> extends FrameLayout {

    private ViewPager2 mViewPager2;
    private ViewPager2.OnPageChangeCallback mOnPageChangeListener;
    private BannerAdapter mAdapter;

    public Banner(@NonNull Context context) {
        super(context);
        Log.e("iisi","Banner construct");
        init(context);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.e("iisi","Banner construct");
        init(context);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e("iisi","Banner construct");
        init(context);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Log.e("iisi","Banner construct");
        init(context);
    }

    private void init(Context context){
        Log.e("iisi","Banner init");
        mViewPager2 = new ViewPager2(context);
        mViewPager2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mViewPager2.setOffscreenPageLimit(1);
        mOnPageChangeListener = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        };
        mViewPager2.registerOnPageChangeCallback(mOnPageChangeListener);
        addView(mViewPager2);
    }

    public void setAdapter(BA adapter){
        Log.e("iisi","Banner setAdapter");
        this.mAdapter = adapter;
        mViewPager2.setAdapter(adapter);
    }
}
