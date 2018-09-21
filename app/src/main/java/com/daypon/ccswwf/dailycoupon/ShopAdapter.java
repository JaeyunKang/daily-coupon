package com.daypon.ccswwf.dailycoupon;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopViewHolder> {

    private Context context;

    List<Shop> shops = new ArrayList<>();
    public void add(Shop shop) {
        shops.add(shop);
        notifyDataSetChanged();
    }

    @Override
    public ShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.shop_view, parent, false);
        return new ShopViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ShopViewHolder holder, int position) {
        final Shop shop = shops.get(position);
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(context).load(shop.getImgUrl()).apply(options).into(holder.mImageShop);

        holder.mNameText.setText(shop.getName());
        holder.mCouponText.setText("Coupon: " + shop.getCoupons().get(0).getName() + " 등 " + String.valueOf(shop.getCoupons().size()) + "가지 중 선택");

        String visitInfo;
        if (shop.getVisitNum() == 0) {
            visitInfo = "이번 달 신규 제휴점";
        } else {
            visitInfo = "최근 한달 " + String.valueOf(shop.getVisitNum()) + "명 방문";
        }
        holder.mVistNumText.setText(visitInfo);
        holder.mLocationText.setText(shop.getLocation());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shopActivity = new Intent(context, ShopActivity.class);

                shopActivity.putExtra("Shop", shop);
                context.startActivity(shopActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

}
