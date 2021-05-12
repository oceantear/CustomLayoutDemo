package com.iisi.customlayoutdemo.banner;

import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public abstract class ImageBannerAdapter<T> extends BannerAdapter<T,BannerImageHolder> {


    public ImageBannerAdapter(List<T> mData) {
        super(mData);
    }

    @Override
    public BannerImageHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new BannerImageHolder(imageView);
    }

}
