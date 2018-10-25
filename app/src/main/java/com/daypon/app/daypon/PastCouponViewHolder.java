package com.daypon.app.daypon;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PastCouponViewHolder extends RecyclerView.ViewHolder {

    public ImageView mImageCoupon;
    public TextView mNameText;
    public TextView mShopNameText;
    public TextView mUseDateText;

    public PastCouponViewHolder(View itemView) {
        super(itemView);
        mImageCoupon = (ImageView) itemView.findViewById(R.id.past_img_coupon);
        mNameText = (TextView) itemView.findViewById(R.id.name_text);
        mShopNameText = (TextView) itemView.findViewById(R.id.shopname_text);
        mUseDateText = (TextView) itemView.findViewById(R.id.usedate_text);
    }
}
