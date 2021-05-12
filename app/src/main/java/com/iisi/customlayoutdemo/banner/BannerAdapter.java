package com.iisi.customlayoutdemo.banner;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BannerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements IViewHolder<T, VH> {

    private List<T> mData;
    private VH mViewHolder;
    private int increaseCount = 2; //for loop play

    public BannerAdapter(List<T> datas){
        if(datas == null)
            datas = new ArrayList<>();
        this.mData = datas;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return onCreateHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder,int position) {
        int realPos = getRealPos(position);
        Log.e("iisi","realPos = "+realPos);
        onBindHolder(holder, mData.get(realPos), realPos);
    }

    @Override
    public int getItemCount() {
        //return mData != null ? mData.size() : 0;
        return getRealItemCount() == 0 ? getRealItemCount() : getRealItemCount() + increaseCount;
    }

    public int getRealItemCount(){
        return mData != null ? mData.size() : 0;
    }

    private int getRealPos(int position){

        if(increaseCount == 2){
            return position;
        }else{
            if(position == 0){
                return getRealItemCount() - 1;
            }else if(position == getRealItemCount() + 1){
                return 0;
            }else{
                return getRealItemCount() - 1;
            }
        }
    }
}
