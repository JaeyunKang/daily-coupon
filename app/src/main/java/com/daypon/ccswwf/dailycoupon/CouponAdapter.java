package com.daypon.ccswwf.dailycoupon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class CouponAdapter extends RecyclerView.Adapter<CouponViewHolder> {

    private Context context;
    private boolean received;

    List<Coupon> coupons = new ArrayList<>();

    public void add(Coupon coupon) {
        coupons.add(coupon);
        notifyDataSetChanged();
    }

    public void setReceived(boolean received) {
        this.received = received;
        notifyDataSetChanged();
    }

    public boolean getReceived() {
        return this.received;
    }

    @Override
    public CouponViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.coupon_view, parent, false);
        return new CouponViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CouponViewHolder holder, int position) {
        final Coupon coupon = coupons.get(position);
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(context).load(coupon.getImgUrl()).apply(options).into(holder.mImageCoupon);

        holder.mNameText.setText(coupon.getName());
        if (received) {
            holder.mCouponReceiveText.setText("오늘의 쿠폰을 이미 제공 받았습니다.");
            holder.mCouponReceiveText.setBackgroundResource(R.drawable.round_gray_bg);
            holder.mCouponReceiveText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        } else {
            holder.mCouponReceiveText.setText("쿠폰 바로 받기");
            holder.mCouponReceiveText.setBackgroundResource(R.drawable.round_orange_bg);

            final SharedPreferences userInformation = context.getSharedPreferences("user", 0);
            final String userId = userInformation.getString("user_id", "");

            if (userId.equals("")) {
                holder.mCouponReceiveText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent loginActivity = new Intent(context, LoginActivity.class);
                        context.startActivity(loginActivity);
                    }
                });
            } else if (PageHome.membershipActive == 1) {
                holder.mCouponReceiveText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent couponPopupActivity = new Intent(context, CouponPopupActivity.class);
                        couponPopupActivity.putExtra("coupon", coupon);

                        context.startActivity(couponPopupActivity);
                    }
                });
            } else {
                holder.mCouponReceiveText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent membershipApplyActivity = new Intent(context, MembershipApplyActivity.class);
                        context.startActivity(membershipApplyActivity);
                    }
                });
            }

        }
    }

    @Override
    public int getItemCount() {
        return coupons.size();
    }

}
