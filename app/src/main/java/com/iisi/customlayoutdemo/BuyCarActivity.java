package com.iisi.customlayoutdemo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class BuyCarActivity extends AppCompatActivity {

    private RecyclerView mLeftList;
    private RecyclerView mRightList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_car);

        mLeftList = findViewById(R.id.left_list);
        mRightList = findViewById(R.id.right_list);


        mLeftList.addOnScrollListener(new RecyclerView.OnScrollListener() {
        });
    }
}
