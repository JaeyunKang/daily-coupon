package com.daypon.ccswwf.dailycoupon;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONObject;

public class PageHome extends Fragment {

    public static int membershipActive;

    private View view;

    private SliderLayout sliderLayout;
    private LinearLayout applyMembershipLayout;
    private Button applyMembershipButton;

    public static PageHome newInstance() {
        Bundle args = new Bundle();
        PageHome fragment = new PageHome();
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
        view = inflater.inflate(R.layout.fragment_page_home, container, false);

        sliderLayout = view.findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.FILL);
        sliderLayout.setScrollTimeInSec(1);

        setSliderViews();

        applyMembershipLayout = view.findViewById(R.id.apply_membership);
        applyMembershipButton = view.findViewById(R.id.apply_membership_btn);

        final SharedPreferences userInformation = getActivity().getSharedPreferences("user", 0);
        final String userId = userInformation.getString("user_id", "");
        if (userId.equals("")) {
            applyMembershipLayout.setVisibility(View.VISIBLE);
        } else {
            try {
                ContentValues values = new ContentValues();
                values.put("user_id", userId);
                ActiveMembershipCheckTask task = new ActiveMembershipCheckTask(values);
                task.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        applyMembershipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userId.equals("")) {
                    Intent loginActivity = new Intent(getActivity(), LoginActivity.class);
                    startActivity(loginActivity);
                } else {
                    Intent membershipApplyActivity = new Intent(getActivity(), MembershipApplyActivity.class);
                    startActivity(membershipApplyActivity);
                }
            }
        });

        return view;
    }

    private void setSliderViews() {
        for (int i = 0; i < 3; i++) {

            SliderView sliderView = new SliderView(getActivity());

            switch (i) {
                case 0:
                    sliderView.setImageUrl("https://s3.ap-northeast-2.amazonaws.com/daily-coupon/slide1.jpg");
                    break;
                case 1:
                    sliderView.setImageUrl("https://s3.ap-northeast-2.amazonaws.com/daily-coupon/slide2.jpg");
                    break;
                case 2:
                    sliderView.setImageUrl("https://s3.ap-northeast-2.amazonaws.com/daily-coupon/slide3.jpg");
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);

            sliderLayout.addSliderView(sliderView);
        }
    }

    private class ActiveMembershipCheckTask extends AsyncTask<Void, Void, String> {

        String url = "http://prography.org/active-membership-check";
        ContentValues values;

        ActiveMembershipCheckTask(ContentValues values) {
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
                if (resultObject.getInt("active") == 1) {
                    membershipActive = 1;
                } else {
                    membershipActive = 0;
                }

                if (membershipActive == 1) {
                    applyMembershipLayout.setVisibility(View.GONE);
                } else {
                    applyMembershipLayout.setVisibility(View.VISIBLE);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

}
