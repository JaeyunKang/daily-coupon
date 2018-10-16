package com.daypon.ccswwf.dailycoupon;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PageMypage extends Fragment {

    public TextView mValidateRangeText;
    public LinearLayout mCancleMembershopLayout;
    public LinearLayout mBillAccountChangeLayout;
    public LinearLayout mApplyMembershipLayout;
    public LinearLayout mApplyMembershipBigLayout;
    public LinearLayout mLoginLayout;

    private TextView mNoCouponHistoryText;
    private TextView mNumPastCouponText;
    private PastCouponAdapter mCouponAdapter;
    private RecyclerView mCouponRecyclerView;
    private GridLayoutManager mGridLayoutManager;

    public static PageMypage newInstance() {
        Bundle args = new Bundle();
        PageMypage fragment = new PageMypage();
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
        View view = inflater.inflate(R.layout.fragment_page_mypage, container, false);

        SharedPreferences userInformation = getActivity().getSharedPreferences("user", 0);
        String userId = userInformation.getString("user_id", "");

        mValidateRangeText = (TextView) view.findViewById(R.id.validate_range);
        mCancleMembershopLayout = (LinearLayout) view.findViewById(R.id.cancel_membership);
        mBillAccountChangeLayout = (LinearLayout) view.findViewById(R.id.change_bill_accont);
        mApplyMembershipLayout = (LinearLayout) view.findViewById(R.id.apply_membership);
        mApplyMembershipBigLayout = (LinearLayout) view.findViewById(R.id.apply_membership_big);
        mLoginLayout = (LinearLayout) view.findViewById(R.id.login);

        mNumPastCouponText = (TextView) view.findViewById(R.id.num_past_coupons);
        mNoCouponHistoryText = (TextView) view.findViewById(R.id.no_coupon_history);
        mCouponAdapter = new PastCouponAdapter();
        mCouponRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_coupon_use);
        mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mCouponRecyclerView.setLayoutManager(mGridLayoutManager);

        try {
            ContentValues values = new ContentValues();
            values.put("user_id", userId);
            MembershipRenderTask task = new MembershipRenderTask(values);
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ContentValues values = new ContentValues();
            values.put("user_id", userId);
            PastCouponRenderTask task = new PastCouponRenderTask(values);
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mCouponRecyclerView.setAdapter(mCouponAdapter);

        mApplyMembershipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent membershipApplyActivity = new Intent(getActivity(), MembershipApplyActivity.class);
                startActivity(membershipApplyActivity);
            }
        });

        mApplyMembershipBigLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent membershipApplyActivity = new Intent(getActivity(), MembershipApplyActivity.class);
                startActivity(membershipApplyActivity);
            }
        });

        mLoginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginActivity = new Intent(getActivity(), LoginActivity.class);
                startActivity(loginActivity);
            }
        });

        return view;
    }

    private class MembershipRenderTask extends AsyncTask<Void, Void, String> {

        String url = "http://daypon.com/membership-check";
        ContentValues values;

        MembershipRenderTask(ContentValues values) {
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

            try {
                JSONObject resultObject = new JSONObject(result);
                SharedPreferences userInformation = getActivity().getSharedPreferences("user", 0);
                String userId = userInformation.getString("user_id", "");
                if (resultObject.getInt("exist") == 1) {
                    mApplyMembershipBigLayout.setVisibility(View.GONE);
                    mLoginLayout.setVisibility(View.GONE);

                    JSONArray membershipArray = resultObject.getJSONArray("results");

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat df_out = new SimpleDateFormat("yyyy.MM.dd");

                    for (int i = 0; i < membershipArray.length(); i++) {
                        JSONObject membershipObject = membershipArray.getJSONObject(i);

                        Membership membership = new Membership();
                        membership.setNum(i + 1);
                        String membershipDateString = membershipObject.getString("created_at");
                        Date membershipStartDate = df.parse(membershipDateString);
                        Date membershipEndDate = df.parse(membershipDateString);
                        membershipEndDate.setMonth(membershipEndDate.getMonth() + 1);
                        membership.setStartDate(df_out.format(membershipStartDate));
                        membership.setEndDate(df_out.format(membershipEndDate));
                        membership.setPrice(membershipObject.getInt("price"));
                        membership.setPayMethod(membershipObject.getString("pay_method"));

                        if(i == 0) {
                            Date currentDate = new Date();
                            membershipEndDate.setHours(23);
                            membershipEndDate.setMinutes(59);
                            membershipEndDate.setSeconds(59);
                            int compare = currentDate.compareTo(membershipEndDate);
                            if (compare <= 0) {
                                if (membership.getPayMethod().equals("google")) {
                                    mValidateRangeText.setText("~" + membership.getEndDate());
                                    mCancleMembershopLayout.setVisibility(View.GONE);
                                    /*mCancleMembershopLayout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent googlePaymentCancelActivity = new Intent(getActivity(), GooglePaymentCancelActivity.class);
                                            startActivity(googlePaymentCancelActivity);
                                        }
                                    });*/
                                    mBillAccountChangeLayout.setVisibility(View.GONE);
                                    mApplyMembershipLayout.setVisibility(View.GONE);
                                } else if (membership.getPayMethod().equals("kakaopay")) {
                                    mValidateRangeText.setText("~" + membership.getEndDate());
                                    mCancleMembershopLayout.setVisibility(View.GONE);
                                    mBillAccountChangeLayout.setVisibility(View.GONE);
                                    mApplyMembershipLayout.setVisibility(View.GONE);
                                }
                            } else {
                                mValidateRangeText.setText("이용중인 멤버십이 없습니다");
                                mCancleMembershopLayout.setVisibility(View.GONE);
                                mBillAccountChangeLayout.setVisibility(View.GONE);
                                mApplyMembershipLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else if (!userId.equals("")){
                    mValidateRangeText.setText("이용중인 멤버십이 없습니다");
                    mCancleMembershopLayout.setVisibility(View.GONE);
                    mBillAccountChangeLayout.setVisibility(View.GONE);
                    mApplyMembershipLayout.setVisibility(View.GONE);
                    mLoginLayout.setVisibility(View.GONE);
                } else {
                    mValidateRangeText.setText("로그인 해주세요");
                    mCancleMembershopLayout.setVisibility(View.GONE);
                    mBillAccountChangeLayout.setVisibility(View.GONE);
                    mApplyMembershipLayout.setVisibility(View.GONE);
                    mApplyMembershipBigLayout.setVisibility(View.GONE);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class PastCouponRenderTask extends AsyncTask<Void, Void, String> {

        String url = "http://daypon.com/past-coupon-check";
        ContentValues values;

        PastCouponRenderTask(ContentValues values) {
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

            try {
                JSONObject resultObject = new JSONObject(result);
                SharedPreferences userInformation = getActivity().getSharedPreferences("user", 0);

                if (resultObject.getInt("exist") == 1) {
                    mNoCouponHistoryText.setVisibility(View.GONE);

                    JSONArray couponArray = resultObject.getJSONArray("results");

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat df_out = new SimpleDateFormat("yyyy년 MM월 dd일");

                    mNumPastCouponText.setText(String.valueOf(couponArray.length()) + "건");

                    for (int i = 0; i < couponArray.length(); i++) {
                        JSONObject couponObject = couponArray.getJSONObject(i);

                        Coupon coupon = new Coupon();
                        coupon.setName(couponObject.getString("cname"));
                        coupon.setShopName(couponObject.getString("sname"));
                        coupon.setImgUrl("https://s3.ap-northeast-2.amazonaws.com/daily-coupon/" + couponObject.getString("img_url"));
                        String couponUseDateString = couponObject.getString("used_at");
                        Date couponUseDate = df.parse(couponUseDateString);
                        coupon.setUseDate(df_out.format(couponUseDate));

                        mCouponAdapter.add(coupon);
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

}
