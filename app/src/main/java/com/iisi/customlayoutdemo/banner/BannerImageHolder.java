package com.iisi.customlayoutdemo.banner;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BannerImageHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;

    public BannerImageHolder(@NonNull View itemView) {
        super(itemView);
        this.imageView = (ImageView) itemView;
    }
}
