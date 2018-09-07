package com.example.ccswwf.dailycoupon;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static int membershipActive;

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

        try {
            ContentValues values = new ContentValues();
            values.put("user_id", "kjy1341@naver.com");
            ActiveMembershipCheckTask task = new ActiveMembershipCheckTask(values);
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class ActiveMembershipCheckTask extends AsyncTask<Void, Void, String> {

        String url = "http://prography.org/active-membership-check";
        ContentValues values;

        ActiveMembershipCheckTask(ContentValues values) {
            this.values = values;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result;
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject resultObject = new JSONObject(result);
                if (resultObject.getInt("active") == 1) {
                    membershipActive = 1;
                } else {
                    membershipActive = 0;
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
