package com.daypon.ccswwf.dailycoupon;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Calendar;

public class ShopActivity extends AppCompatActivity {

    public ImageView mImageShop;
    public TextView mNameText;
    public TextView mLocationText;
    public TextView mDescriptionText;
    public TextView mTodayOpeningText;
    public TextView mVistNumText;
    public TextView mNoticeText;
    public LinearLayout mNoticeTextWrap;
    public TextView mNaverUrlText;
    public TextView mAddressText;

    public ArrayList<TextView> mOpeningHoursText;
    public TextView mOpeningNoticeText;

    private CouponAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mImageShop = (ImageView) findViewById(R.id.img_shop);
        mNameText = (TextView) findViewById(R.id.text_name);
        mLocationText = (TextView) findViewById(R.id.text_location);
        mDescriptionText = (TextView) findViewById(R.id.text_description);
        mTodayOpeningText = (TextView) findViewById(R.id.text_today_opening);
        mVistNumText = (TextView) findViewById(R.id.text_visit_num);
        mNoticeTextWrap = (LinearLayout) findViewById(R.id.text_notice_wrap);
        mNoticeText = (TextView) findViewById(R.id.text_notice);
        mNaverUrlText = (TextView) findViewById(R.id.text_naver_url);
        mAddressText = (TextView) findViewById(R.id.text_address);

        mOpeningHoursText = new ArrayList<>();
        mOpeningHoursText.add((TextView) findViewById(R.id.text_opening_hours1));
        mOpeningHoursText.add((TextView) findViewById(R.id.text_opening_hours2));
        mOpeningHoursText.add((TextView) findViewById(R.id.text_opening_hours3));
        mOpeningHoursText.add((TextView) findViewById(R.id.text_opening_hours4));
        mOpeningHoursText.add((TextView) findViewById(R.id.text_opening_hours5));
        mOpeningHoursText.add((TextView) findViewById(R.id.text_opening_hours6));
        mOpeningHoursText.add((TextView) findViewById(R.id.text_opening_hours7));
        mOpeningNoticeText = findViewById(R.id.text_opening_notice);

        mAdapter = new CouponAdapter();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_coupon);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        final Shop shop = (Shop) getIntent().getSerializableExtra("Shop");

        TextView toolBarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolBarTitle.setText(shop.getName());

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(this).load(shop.getImgUrl()).apply(options).into(mImageShop);
        mNameText.setText(shop.getName());
        mLocationText.setText(shop.getLocation());
        mDescriptionText.setText(shop.getDescription());
        Calendar cal = Calendar.getInstance();
        mTodayOpeningText.setText(shop.getOpenings().get(cal.get(Calendar.DAY_OF_WEEK) - 1));

        int visitNum = shop.getVisitNum();
        String visitInfo;
        if (visitNum == 0) {
            visitInfo = "이번 달 신규 제휴점";
        } else {
            visitInfo = "최근 한달 " + String.valueOf(visitNum) + "명 방문";
        }
        mVistNumText.setText(visitInfo);
        if (!shop.getNotice().equals("")) {
            mNoticeTextWrap.setVisibility(View.VISIBLE);
            mNoticeText.setText(shop.getNotice());
        }

        mNaverUrlText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(shop.getNaverUrl())));
            }
        });
        mAddressText.setText(shop.getAddress());

        ArrayList<Coupon> coupons = shop.getCoupons();
        for (int c = 0; c < coupons.size(); c++) {
            Coupon coupon = coupons.get(c);
            coupon.setShopName(shop.getName());
            mAdapter.add(coupon);
        }

        final SharedPreferences userInformation = getSharedPreferences("user", 0);
        final String userId = userInformation.getString("user_id", "");

        try {
            ContentValues values = new ContentValues();
            values.put("user_id", userId);
            values.put("shop_id", shop.getId());
            CouponCheckTask task = new CouponCheckTask(values);
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mOpeningHoursText.get(0).setText("[월요일] " + shop.getOpenings().get(0));
        mOpeningHoursText.get(1).setText("[화요일] " + shop.getOpenings().get(1));
        mOpeningHoursText.get(2).setText("[수요일] " + shop.getOpenings().get(2));
        mOpeningHoursText.get(3).setText("[목요일] " + shop.getOpenings().get(3));
        mOpeningHoursText.get(4).setText("[금요일] " + shop.getOpenings().get(4));
        mOpeningHoursText.get(5).setText("[토요일] " + shop.getOpenings().get(5));
        mOpeningHoursText.get(6).setText("[일요일] " + shop.getOpenings().get(6));
        if (!shop.getOpeningNotice().equals("")) {
            mOpeningNoticeText.setVisibility(View.VISIBLE);
            mOpeningNoticeText.setText(shop.getOpeningNotice());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class CouponCheckTask extends AsyncTask<Void, Void, String> {

        String url = "http://prography.org/coupon-check";
        ContentValues values;

        CouponCheckTask(ContentValues values) {
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
                if (Integer.parseInt(result) == 1) {
                    mAdapter.setReceived(true);
                }
                else {
                    mAdapter.setReceived(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
