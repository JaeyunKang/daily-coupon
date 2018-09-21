package com.daypon.ccswwf.dailycoupon;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        PagerAdapter mPagerAdapter = new PagerAdapter(
                getSupportFragmentManager()
        );

        ViewPager mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mPagerAdapter);

        TabLayout mTab = (TabLayout) findViewById(R.id.tabs);
        mTab.setupWithViewPager(mViewPager);
        mTab.getTabAt(0).setIcon(R.drawable.ic_home);
        mTab.getTabAt(1).setIcon(R.drawable.ic_list);
        mTab.getTabAt(2).setIcon(R.drawable.ic_mypage);
        mTab.getTabAt(3).setIcon(R.drawable.ic_more);

        SharedPreferences userInformation = getSharedPreferences("user", 0);
        SharedPreferences.Editor editor = userInformation.edit();

        editor.putString("user_id", "");

    }


}
