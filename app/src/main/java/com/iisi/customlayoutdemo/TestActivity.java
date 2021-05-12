package com.iisi.customlayoutdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iisi.customlayoutdemo.custom.view.AutoScaleTextView;

public class TestActivity extends AppCompatActivity {

    AutoScaleTextView mCustomTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mCustomTxt = findViewById(R.id.mytext);
        /*TextView tv = findViewById(R.id.text);
        tv.setText("資策會產業情報研究所預估明年全球5G智慧型手機滲透率可達39.8%，5G應用也帶動Wi-Fi 6技術規格成主流；也預期明年新榮耀品牌全球市占率可到4%，短期美國對華為禁令不會大幅改變。 \" +\n" +
                "                        \"資策會產業情報研究所（MIC）今天上午舉行2021高科技產業前瞻趨勢記者會，觀察明年全球網通產業，MIC產業顧問兼副主任林柏齊預估的9大趨勢重點包括：5G手機加速滲透與差異化、\" +\n" +
                "                \"Wi-Fi 6技術成主流、NR-RedCap演進補中階物聯網缺口、固定式無線接取（FWA）成寬頻最後一哩新選擇、新榮耀手機品牌加速價格競爭、OTT（Over-the-top）持續衝擊傳統機上盒產業、電信商組隊布局邊緣運算、Open RAN生態加速擴展、以及5G SA專網協助數位轉型。 \" +\n" +
                "                \"林柏齊預估，明年全球智慧型手機出貨量約13.55億支，其中5G機種約5.39億支，估滲透率達39.8%。" +
                "資策會產業情報研究所預估明年全球5G智慧型手機滲透率可達39.8%，5G應用也帶動Wi-Fi 6技術規格成主流；也預期明年新榮耀品牌全球市占率可到4%，短期美國對華為禁令不會大幅改變。 \" +\n" +
                "                        \"資策會產業情報研究所（MIC）今天上午舉行2021高科技產業前瞻趨勢記者會，觀察明年全球網通產業，MIC產業顧問兼副主任林柏齊預估的9大趨勢重點包括：5G手機加速滲透與差異化、\" +\n" +
                "                \"Wi-Fi 6技術成主流、NR-RedCap演進補中階物聯網缺口、固定式無線接取（FWA）成寬頻最後一哩新選擇、新榮耀手機品牌加速價格競爭、OTT（Over-the-top）持續衝擊傳統機上盒產業、電信商組隊布局邊緣運算、Open RAN生態加速擴展、以及5G SA專網協助數位轉型。 \" +\n" +
                "                \"林柏齊預估，明年全球智慧型手機出貨量約13.55億支，其中5G機種約5.39億支，估滲透率達39.8%。" +
                "");
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());*/

        mCustomTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("jimmy", "click!!!");
            }
        });
    }
}
