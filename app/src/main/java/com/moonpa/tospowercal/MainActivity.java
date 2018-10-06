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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bubbleTab = (BubbleTab)findViewById(R.id.bubbleTab);
        viewPager = (ViewPager)findViewById(R.id.viewPage) ;

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
                break;
            case "traditionalChinese":
                config.locale = Locale.TRADITIONAL_CHINESE;
                break;
            case "simplifiedChinese":
                config.locale = Locale.SIMPLIFIED_CHINESE;
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
