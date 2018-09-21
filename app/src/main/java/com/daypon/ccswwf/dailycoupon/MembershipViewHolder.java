package com.daypon.ccswwf.dailycoupon;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class MembershipViewHolder extends RecyclerView.ViewHolder {

    public TextView mNumText;
    public TextView mMembershipRangeText;
    public TextView mMembershipPayText;
    public TextView mPriceText;

    public MembershipViewHolder(View itemView) {
        super(itemView);
        mNumText = (TextView) itemView.findViewById(R.id.text_num);
        mMembershipRangeText = (TextView) itemView.findViewById(R.id.text_membership_range);
        mMembershipPayText = (TextView) itemView.findViewById(R.id.text_membership_pay);
        mPriceText = (TextView) itemView.findViewById(R.id.text_price);
    }
}
