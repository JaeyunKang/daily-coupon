package com.daypon.app.daypon;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        PagerAdapter mPagerAdapter = new PagerAdapter(
                getSupportFragmentManager()
        );

        final TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);

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

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        toolbarTitle.setText("홈");
                        break;
                    case 1:
                        toolbarTitle.setText("제휴점찾기");
                        break;
                    case 2:
                        toolbarTitle.setText("마이페이지");
                        break;
                    case 3:
                        toolbarTitle.setText("더보기");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


}
