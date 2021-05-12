package com.iisi.customlayoutdemo;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.iisi.customlayoutdemo.banner.Banner;
import com.iisi.customlayoutdemo.banner.BannerImageHolder;
import com.iisi.customlayoutdemo.banner.ImageBannerAdapter;
import com.iisi.customlayoutdemo.databinding.ActivityBannerBinding;

import java.util.ArrayList;
import java.util.List;

public class BannerActivity extends AppCompatActivity {

    private ActivityBannerBinding binding;
    private Banner mBanner;

    private List<String> mPicLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_buy_car);
        binding = ActivityBannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mPicLink = new ArrayList<>();
        mPicLink.add("https://www.ali-nsa.net/zh-tw/content/images/static/ali-img-1-md.jpg");
        mPicLink.add("https://www.ali-nsa.net/zh-tw/content/images/static/ali-img-2-md.jpg");
        mPicLink.add("https://www.ali-nsa.net/zh-tw/content/images/static/ali-img-3-md.jpg");
        mPicLink.add("https://www.ali-nsa.net/zh-tw/content/images/static/ali-img-4-md.jpg");
        mPicLink.add("https://www.ali-nsa.net/tsou-celebration/content/images/img-01.jpg");


        mBanner = binding.banner;

        mBanner.setAdapter(new ImageBannerAdapter<String>(mPicLink) {
            /*@Override
            public BannerImageHolder onCreateHolder(ViewGroup parent, int viewType) {
                return super.onCreateHolder(parent, viewType);
            }*/

            @Override
            public void onBindHolder(BannerImageHolder holder, String data , int position) {
                Glide.with(holder.itemView)
                        .load(data)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                        .into(holder.imageView);
            }
        });

    }
}
