package com.daypon.ccswwf.dailycoupon;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PageShops extends Fragment {

    private ShopAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    public static PageShops newInstance() {
        Bundle args = new Bundle();
        PageShops fragment = new PageShops();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_page_shops, container, false);
        mAdapter = new ShopAdapter();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_shop);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        try {
            ContentValues values = new ContentValues();
            values.put("location", "서울대");
            ShopRenderTask task = new ShopRenderTask(values);
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mRecyclerView.setAdapter(mAdapter);

        return view;

    }

    private class ShopRenderTask extends AsyncTask<Void, Void, String> {

        String url = "http://prography.org/shop";
        ContentValues values;

        ShopRenderTask(ContentValues values) {
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
            ArrayList<Shop> shops = new ArrayList<>();

            try {
                JSONObject shopAndCouponObject = new JSONObject(result);
                JSONArray shopArray = shopAndCouponObject.getJSONArray("results");
                JSONArray couponArray = shopAndCouponObject.getJSONArray("c_results");
                for (int i = 0; i < shopArray.length(); i++) {
                    JSONObject shopObject = shopArray.getJSONObject(i);
                    JSONArray shopCouponArray = couponArray.getJSONArray(i);

                    Shop shop = new Shop();
                    String imgUrl = "https://s3.ap-northeast-2.amazonaws.com/daily-coupon/" + String.valueOf(shopObject.getInt("id")) + ".jpg" ;
                    shop.setImgUrl(imgUrl);
                    shop.setId(shopObject.getInt("id"));
                    shop.setName(shopObject.getString("name"));
                    shop.setVisitNum(shopObject.getInt("visit_num"));
                    shop.setLocation(shopObject.getString("location"));
                    shop.setDescription(shopObject.getString("description"));
                    shop.setNotice(shopObject.getString("notice"));
                    shop.setNaverUrl(shopObject.getString("naver_url"));
                    shop.setAddress(shopObject.getString("address"));

                    ArrayList<Coupon> coupons = new ArrayList<>();
                    for (int c = 0; c < shopCouponArray.length(); c++) {
                        Coupon coupon = new Coupon();
                        JSONObject couponObject = shopCouponArray.getJSONObject(c);
                        coupon.setId(couponObject.getInt("id"));
                        coupon.setName(couponObject.getString("name"));
                        coupon.setImgUrl("https://s3.ap-northeast-2.amazonaws.com/daily-coupon/" + couponObject.getString("img_url"));
                        coupons.add(coupon);
                    }
                    shop.setCoupons(coupons);

                    ArrayList<String> openings = new ArrayList<>();
                    for (int c = 1; c <= 7; c++) {
                        String opening = shopObject.getString("opening" + String.valueOf(c));
                        openings.add(opening);
                    }

                    shop.setOpenings(openings);
                    shop.setOpeningNotice(shopObject.getString("opening_notice"));
                    shop.setSubImgNum(shopObject.getInt("sub_img_num"));
                    mAdapter.add(shop);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
