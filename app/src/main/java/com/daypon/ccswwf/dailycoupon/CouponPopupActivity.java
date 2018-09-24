package com.daypon.ccswwf.dailycoupon;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CouponPopupActivity extends AppCompatActivity {

    ImageView closeView;

    ImageView mCouponImg;
    TextView mCouponNameText;
    TextView mShopNameText;
    TextView mUseDateText;

    Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_popup);

        mCouponImg = (ImageView) findViewById(R.id.coupon_img);
        mCouponNameText = (TextView) findViewById(R.id.coupon_name);
        mShopNameText = (TextView) findViewById(R.id.shop_name);
        mUseDateText = (TextView) findViewById(R.id.use_date);

        confirmButton = (Button) findViewById(R.id.confirm_btn);

        closeView = (ImageView) findViewById(R.id.ic_close);
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Coupon coupon = (Coupon) getIntent().getSerializableExtra("coupon");

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(this).load(coupon.getImgUrl()).apply(options).into(mCouponImg);

        mCouponNameText.setText(coupon.getName());
        mShopNameText.setText(coupon.getShopName());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String currentDate = sdf.format(new Date());
        mUseDateText.setText(currentDate);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final SharedPreferences userInformation = getSharedPreferences("user", 0);
                    final String userId = userInformation.getString("user_id", "");
                    ContentValues values = new ContentValues();
                    values.put("user_id", userId);
                    values.put("coupon_id", coupon.getId());
                    CouponUseTask task = new CouponUseTask(values, coupon);
                    task.execute();
                    findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private class CouponUseTask extends AsyncTask<Void, Void, String> {

        String url = "http://prography.org/coupon-use";
        ContentValues values;
        Coupon coupon;

        CouponUseTask(ContentValues values, Coupon coupon) {
            this.values = values;
            this.coupon = coupon;
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

            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            Intent couponCompleteActivity = new Intent(getApplicationContext(), CouponCompleteActivity.class);
            couponCompleteActivity.putExtra("coupon", this.coupon);
            startActivity(couponCompleteActivity);
            finish();
        }
    }
}
