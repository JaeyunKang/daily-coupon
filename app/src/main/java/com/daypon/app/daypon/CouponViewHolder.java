package com.daypon.app.daypon;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CouponViewHolder extends RecyclerView.ViewHolder {

    public ImageView mImageCoupon;
    public TextView mNameText;
    public TextView mCouponReceiveText;

    public CouponViewHolder(View itemView) {
        super(itemView);
        mImageCoupon = (ImageView) itemView.findViewById(R.id.img_coupon);
        mNameText = (TextView) itemView.findViewById(R.id.name_text);
        mCouponReceiveText = (TextView) itemView.findViewById(R.id.coupon_receive);
    }
}
