package com.daypon.app.daypon;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    public static int page_number = 4;

    public  PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return PageHome.newInstance();
            case 1:
                return PageShops.newInstance();
            case 2:
                return PageMypage.newInstance();
            default:
                return PageSettings.newInstance();
        }
    }

    @Override
    public int getCount() {
        return page_number;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "홈";
            case 1:
                return "제휴점찾기";
            case 2:
                return "마이페이지";
            default:
                return "더보기";
        }
    }
}
