package com.example.ccswwf.dailycoupon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

public class PageHome extends Fragment {

    private SliderLayout sliderLayout;

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
        View view = inflater.inflate(R.layout.fragment_page_home, container, false);

        sliderLayout = view.findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.FILL);
        sliderLayout.setScrollTimeInSec(1);

        setSliderViews();

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
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {

                }
            });

            sliderLayout.addSliderView(sliderView);
        }
    }

}
