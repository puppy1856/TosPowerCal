package com.moonpa.tospowercal;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.TextView;

import com.github.florent37.bubbletab.BubbleTab;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{
    final String SETTING = "setting";
    String language;
    SharedPreferences setting;
    BubbleTab bubbleTab;
    ViewPager viewPager;
    Resources resources;
    DisplayMetrics dm;
    Configuration config;
    TextView tv_energy, tv_alarm, tv_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bubbleTab = (BubbleTab)findViewById(R.id.bubbleTab);
        viewPager = (ViewPager)findViewById(R.id.viewPage) ;
        tv_energy = (TextView) findViewById(R.id.tab_tv_energy);
        tv_alarm = (TextView)findViewById(R.id.tab_tv_alarm);
        tv_setting = (TextView)findViewById(R.id.tab_tv_setting);


        setting = this.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        language = setting.getString("language","auto");

        resources = this.getResources();
        dm = resources.getDisplayMetrics();
        config = resources.getConfiguration();

        switch (language)
        {
            case "auto":
                config.locale = Locale.getDefault();
                break;
            case "english":
                config.locale = Locale.ENGLISH;
                tv_energy.setText("EnergyCal");
                tv_alarm.setText("Alarm");
                tv_setting.setText("Setting");
                break;
            case "traditionalChinese":
                config.locale = Locale.TRADITIONAL_CHINESE;
                tv_energy.setText("體力計算");
                tv_alarm.setText("鬧鐘");
                tv_setting.setText("設定");
                break;
            case "simplifiedChinese":
                config.locale = Locale.SIMPLIFIED_CHINESE;
                tv_energy.setText("体力计算");
                tv_alarm.setText("闹钟");
                tv_setting.setText("设定");
                break;
        }
        resources.updateConfiguration(config, dm);

        List<Fragment> fragments=new ArrayList<Fragment>();
        fragments.add(new CalFragment());
        fragments.add(new AlarmFragment());
        fragments.add(new AccountFragment());
        BubbleTabAdapter adapter = new BubbleTabAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

        bubbleTab.setupWithViewPager(viewPager);
    }
}
