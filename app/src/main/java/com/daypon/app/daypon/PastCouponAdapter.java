package com.daypon.app.daypon;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class PastCouponAdapter extends RecyclerView.Adapter<PastCouponViewHolder> {

    private Context context;

    List<Coupon> coupons = new ArrayList<>();

    public void add(Coupon coupon) {
        coupons.add(coupon);
        notifyDataSetChanged();
    }

    @Override
    public PastCouponViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.past_coupon_view, parent, false);
        return new PastCouponViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PastCouponViewHolder holder, int position) {
        final Coupon coupon = coupons.get(position);
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(context).load(coupon.getImgUrl()).apply(options).into(holder.mImageCoupon);

        holder.mNameText.setText(coupon.getName());
        holder.mShopNameText.setText(coupon.getShopName());
        holder.mUseDateText.setText(coupon.getUseDate());

    }

    @Override
    public int getItemCount() {
        return coupons.size();
    }

}
