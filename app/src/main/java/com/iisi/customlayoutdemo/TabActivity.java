package com.iisi.customlayoutdemo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.iisi.customlayoutdemo.databinding.ActivityTabBinding;

public class TabActivity extends AppCompatActivity {

    ActivityTabBinding binding;
    TabLayout tabLayout;
    String [] titles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        titles = new String[2];
        titles[0] = "測試1";
        titles[1] = "測試2";

        tabLayout = findViewById(R.id.tab_layout);

        if (tabLayout != null) {
            //TabLayout.Tab tab1 = tabLayout.getTabAt(0);
            //if (tab1 != null) {
            TabLayout.Tab tab1 = tabLayout.newTab();
            tab1.setCustomView(getTabView0());
            tabLayout.addTab(tab1);
            //}
            //TabLayout.Tab tab2 = tabLayout.getTabAt(1);
            //if (tab2 != null) {
            TabLayout.Tab tab2 = tabLayout.newTab();
            tab2.setCustomView(getTabView1());
            //}
            tabLayout.addTab(tab2);

        }
    }

    public View getTabView0() {
        LayoutInflater mInflater = LayoutInflater.from(this);
        View view = mInflater.inflate(R.layout.custom_tab_left, null);
        TextView tv = (TextView) view.findViewById(R.id.tab_text_left);
        tv.setText(titles[0]);
        return view;
    }

    public View getTabView1() {
        LayoutInflater mInflater = LayoutInflater.from(this);
        View view = mInflater.inflate(R.layout.custom_tab_right, null);
        TextView tv = (TextView) view.findViewById(R.id.tab_text_right);
        tv.setText(titles[1]);
        return view;
    }
}
