package com.daypon.ccswwf.dailycoupon;

import android.content.Intent;
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

public class CouponCompleteActivity extends AppCompatActivity {

    ImageView mCouponImg;
    TextView mCouponNameText;
    TextView mUseDateText;

    Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_complete);

        final Coupon coupon = (Coupon) getIntent().getSerializableExtra("coupon");

        mCouponImg = (ImageView) findViewById(R.id.coupon_img);
        mCouponNameText = (TextView) findViewById(R.id.coupon_name);
        mUseDateText = (TextView) findViewById(R.id.use_date);

        homeButton = (Button) findViewById(R.id.home_btn);

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(this).load(coupon.getImgUrl()).apply(options).into(mCouponImg);

        mCouponNameText.setText(coupon.getName());        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String currentDate = sdf.format(new Date());
        mUseDateText.setText(currentDate);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainActivity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainActivity);
    }
}
