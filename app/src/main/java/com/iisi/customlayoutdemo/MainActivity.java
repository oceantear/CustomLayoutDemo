package com.iisi.customlayoutdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iisi.customlayoutdemo.ImageEditor.ScreenShotActivity;
import com.iisi.customlayoutdemo.custom.view.FlowLayout;
import com.iisi.customlayoutdemo.custom.view.PostScaleImageView;
import com.iisi.customlayoutdemo.custom.view.WrapLayout;
import com.iisi.customlayoutdemo.databinding.ActivityMainBinding;
import com.iisi.customlayoutdemo.lifecycle.LifeCycleActivity;
import com.iisi.customlayoutdemo.vo.FlowLayoutContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private WrapLayout wrapLayout;
    private FlowLayout flowlayout;
    private LayoutInflater inflater;
    private TextView textView;
    private Button mBotton;
    private final String[] strs = {"測試很長很長很長資料","測試很長很長很長很長很長很長資料","交通","建築學","土木工程","電機工程",
            "計算機科學","機械工程","能源科學","測繪學", "航空航天","礦業","冶金學","印刷","化學工程","水利工程","通信技術","生物工程",
            "材料科學","環境科學","小花","小明","板橋火車站","台北火車站","今天天氣很好","明天會下雨"};
    private List<String> content;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setContentView(R.layout.activity_main);

        //Button autoBt = (Button) findViewById(R.id.autoBt);
        binding.autoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TestActivity.class));
            }
        });

        //Button flowBt= (Button) findViewById(R.id.flowBt);
        binding.flowBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FlowLayoutActivity.class));
            }
        });

        //Button chipBt = (Button) findViewById(R.id.chipBt);
        binding.chipBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChipActivity.class));
            }
        });

        //Button rotationBt = (Button) findViewById(R.id.rotationBt);
        binding.rotationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RotationActivity.class));
            }
        });

        //Button buyCarBt = (Button) findViewById(R.id.buyCarBt);
        binding.buyCarBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BuyCarActivity.class));
            }
        });

        //Button drawImageBt = (Button) findViewById(R.id.drawImageBt);
        binding.drawImageBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DrawImageActivity.class));
            }
        });


        //Button ovalImageBt = (Button) findViewById(R.id.ovalImageBt);
        binding.ovalImageBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OvalActivity.class));
            }
        });

        //Button maskImageBt = (Button) findViewById(R.id.maskImageBt);
        binding.maskImageBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MaskActivity.class));
            }
        });

        //Button screenShotBt = (Button) findViewById(R.id.screenShotBt);
        binding.screenShotBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ScreenShotActivity.class));
            }
        });

        //Button postTranslateBt = (Button) findViewById(R.id.postTranslateBt);
        binding.postTranslateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PostTranslateActivity.class));
            }
        });

        //Button postScaleBt = (Button) findViewById(R.id.postScaleBt);
        binding.postScaleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PostScaleActivity.class));
            }
        });

        //Button centerScaleBt = (Button) findViewById(R.id.centerScaleBt);
        binding.centerScaleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CenterScaleActivity.class));
            }
        });

        //Button TabBt = (Button) findViewById(R.id.TabBt);
        binding.TabBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TabActivity.class));
            }
        });

        //Button lifeCycleBt = findViewById(R.id.lifeCycleBt);
        binding.lifeCycleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LifeCycleActivity.class));
            }
        });

        binding.BannerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BannerActivity.class));
            }
        });



        /*textView = (TextView) findViewById(R.id.item);
        mBotton = findViewById(R.id.bt);
        wrapLayout = (WrapLayout) findViewById(R.id.hot_seatch_layout);
        flowlayout = (FlowLayout) findViewById(R.id.flow_layout);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //final String[] strs = {"測試很長很長很長資料","測試很長很長很長很長很長很長資料","交通","建築學","土木工程","電機工程","計算機科學","機械工程","能源科學","測繪學","航空航天","礦業","冶金學","印刷","化學工程","水利工程","通信技術","生物工程","材料科學","環境科學"};
        *//*for (int i = 0; i < strs.length; i++) {
            LinearLayout itemLayout = (LinearLayout) inflater.inflate(R.layout.layout_item, wrapLayout, false);
            TextView name = (TextView) itemLayout.findViewById(R.id.name);
            name.setPadding(dp2pixel(this, 20), dp2pixel(this, 5),  dp2pixel(this, 20), dp2pixel(this, 5));
            name.setText(strs[i]);
            wrapLayout.addView(itemLayout);
        }*//*
        content = Arrays.asList(strs);

        wrapLayout.setClickListener(new WrapLayout.OnWrapLayoutClick() {

            @Override
            public void onItemClick(ArrayList<String> content) {
                //for(String item : content)
                    //textView.setText(content.toString());
            }
        });
        *//** set custom item layout**//*
        flowlayout.setLayout(R.layout.custom_layout_item);
        flowlayout.setContent(genRandomContent(10));
        mBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, TestActivity.class));
                textView.setText("");
                flowlayout.setContent(genRandomContent(10));
                initContents();
            }
        });

        flowlayout.setClickListener(new FlowLayout.OnWrapLayoutClick() {
            @Override
            public void onItemClick(ArrayList<String> content) {
                textView.setText(content.toString());
            }
        });*/
    }

    private void initContents(){

    }

    private List<FlowLayoutContent> genRandomContent(int size){

        List<String> rcontent = new ArrayList<>();
        int removeItemCount = content.size() - size;
        if(removeItemCount <= 0 ) {
            Log.e("iisi","remove item count >= real size");
            return null;
        }else {
            Collections.shuffle(content);
            rcontent = content.subList(0, size);
            List<FlowLayoutContent> fContents = new ArrayList<>();
            Random rand = new Random();
            for(int i = 0 ; i < rcontent.size() ; i++) {
                boolean selected = rand.nextBoolean();
                FlowLayoutContent fContent = new FlowLayoutContent(rcontent.get(i), selected);
                fContents.add(fContent);
            }
            return fContents;
        }
    }


    private int dp2pixel(Context context, int dp) {
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        return px;
    }
}
