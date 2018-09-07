package com.example.ccswwf.dailycoupon;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
    public TextView mNoHistoryText;
    public LinearLayout mApplyMembershipBigLayout;
    public TextView mNumMembershipsText;

    private MembershipAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

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

        mValidateRangeText = (TextView) view.findViewById(R.id.validate_range);
        mCancleMembershopLayout = (LinearLayout) view.findViewById(R.id.cancel_membership);
        mBillAccountChangeLayout = (LinearLayout) view.findViewById(R.id.change_bill_accont);
        mApplyMembershipLayout = (LinearLayout) view.findViewById(R.id.apply_membership);
        mNoHistoryText = (TextView) view.findViewById(R.id.no_history);
        mApplyMembershipBigLayout = (LinearLayout) view.findViewById(R.id.apply_membership_big);
        mNumMembershipsText = (TextView) view.findViewById(R.id.num_memberships);

        mAdapter = new MembershipAdapter();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_membership);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        try {
            ContentValues values = new ContentValues();
            values.put("user_id", "kjy1341@naver.com");
            MembershipRenderTask task = new MembershipRenderTask(values);
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private class MembershipRenderTask extends AsyncTask<Void, Void, String> {

        String url = "http://prography.org/membership-check";
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
                if (resultObject.getInt("exist") == 1) {
                    mApplyMembershipBigLayout.setVisibility(View.GONE);
                    mNoHistoryText.setVisibility(View.GONE);

                    JSONArray membershipArray = resultObject.getJSONArray("results");

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat df_out = new SimpleDateFormat("yyyy년 MM월 dd일");

                    mNumMembershipsText.setText(String.valueOf(membershipArray.length()) + "건");

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

                        mAdapter.add(membership);

                        if(i == 0) {
                            Date currentDate = new Date();
                            membershipEndDate.setHours(23);
                            membershipEndDate.setMinutes(59);
                            membershipEndDate.setSeconds(59);
                            int compare = currentDate.compareTo(membershipEndDate);
                            if (compare <= 0) {
                                mValidateRangeText.setText(membership.getEndDate() + "까지 유효합니다");
                                mCancleMembershopLayout.setVisibility(View.VISIBLE);
                                mBillAccountChangeLayout.setVisibility(View.VISIBLE);
                                mApplyMembershipLayout.setVisibility(View.GONE);
                            } else {
                                mValidateRangeText.setText("이용중인 멤버십이 없습니다");
                                mCancleMembershopLayout.setVisibility(View.GONE);
                                mBillAccountChangeLayout.setVisibility(View.GONE);
                                mApplyMembershipLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else {
                    mValidateRangeText.setText("이용중인 멤버십이 없습니다");
                    mCancleMembershopLayout.setVisibility(View.GONE);
                    mBillAccountChangeLayout.setVisibility(View.GONE);
                    mApplyMembershipLayout.setVisibility(View.GONE);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
