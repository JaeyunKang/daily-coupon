package com.daypon.app.daypon;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopViewHolder extends RecyclerView.ViewHolder {

    public ImageView mImageShop;
    public TextView mNameText;
    public TextView mCouponText;
    public TextView mVistNumText;
    public TextView mLocationText;

    public ShopViewHolder(View itemView) {
        super(itemView);
        mImageShop = (ImageView) itemView.findViewById(R.id.img_shop);
        mNameText = (TextView) itemView.findViewById(R.id.text_name);
        mCouponText = (TextView) itemView.findViewById(R.id.text_coupon);
        mVistNumText = (TextView) itemView.findViewById(R.id.text_visit_num);
        mLocationText = (TextView) itemView.findViewById(R.id.text_location);
    }
}
