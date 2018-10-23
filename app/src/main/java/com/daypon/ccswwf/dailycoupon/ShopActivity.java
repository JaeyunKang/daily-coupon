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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Calendar;

public class ShopActivity extends AppCompatActivity implements OnMapReadyCallback {

    public SliderLayout mImageShop;
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

    private GoogleMap mMap;

    private Shop shop;

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

        mImageShop = findViewById(R.id.img_shop);
        mNameText = (TextView) findViewById(R.id.text_name);
        mLocationText = (TextView) findViewById(R.id.text_location);
        mDescriptionText = (TextView) findViewById(R.id.text_description);
        mTodayOpeningText = (TextView) findViewById(R.id.text_today_opening);
        mVistNumText = (TextView) findViewById(R.id.text_visit_num);
        mNoticeTextWrap = (LinearLayout) findViewById(R.id.text_notice_wrap);
        mNoticeText = (TextView) findViewById(R.id.text_notice);
        mNaverUrlText = (TextView) findViewById(R.id.text_naver_url);
        mAddressText = (TextView) findViewById(R.id.text_address);

        mAdapter = new CouponAdapter();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_coupon);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        shop = (Shop) getIntent().getSerializableExtra("Shop");

        TextView toolBarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolBarTitle.setText(shop.getName());

        mImageShop.setIndicatorAnimation(SliderLayout.Animations.FILL);
        mImageShop.setScrollTimeInSec(1);

        setShopSliderViews(shop.getImgUrl());

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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latlng = new LatLng(shop.getLat(), shop.getLng());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latlng).title(shop.getName());
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));
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

    private void setShopSliderViews(String url) {
        for (int i = 0; i < 6; i++) {
            SliderView sliderView = new SliderView(this);

            switch (i) {
                case 0:
                    sliderView.setImageUrl(url.substring(0, url.length() - 4) + String.valueOf(i + 1) + ".jpg");
                    break;
                case 1:
                    sliderView.setImageUrl(url.substring(0, url.length() - 4) + String.valueOf(i + 1) + ".jpg");
                    break;
                case 2:
                    sliderView.setImageUrl(url.substring(0, url.length() - 4) + String.valueOf(i + 1) + ".jpg");
                    break;
                case 3:
                    sliderView.setImageUrl(url.substring(0, url.length() - 4) + String.valueOf(i + 1) + ".jpg");
                    break;
                case 4:
                    sliderView.setImageUrl(url.substring(0, url.length() - 4) + String.valueOf(i + 1) + ".jpg");
                    break;
                case 5:
                    sliderView.setImageUrl(url.substring(0, url.length() - 4) + String.valueOf(i + 1) + ".jpg");
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);

            mImageShop.addSliderView(sliderView);
        }
    }

    private class CouponCheckTask extends AsyncTask<Void, Void, String> {

        String url = "http://daypon.com/coupon-check";
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
