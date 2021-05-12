package com.iisi.customlayoutdemo.banner;

import android.view.ViewGroup;

public interface IViewHolder<T, VH> {

    VH onCreateHolder(ViewGroup parent, int viewType);

    void onBindHolder(VH holder, T data, int position);

}
