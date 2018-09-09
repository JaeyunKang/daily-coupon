package com.example.ccswwf.dailycoupon;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PageSettings extends Fragment {

    TextView versionText;

    public static PageSettings newInstance() {
        Bundle args = new Bundle();
        PageSettings fragment = new PageSettings();
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
        View view = inflater.inflate(R.layout.fragment_page_settings, container, false);

        LinearLayout privacyPolicyView = (LinearLayout) view.findViewById(R.id.layout_privacy_policy);
        privacyPolicyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent privacyPolicyActiviy = new Intent(getActivity(), PrivatePolicyActivity.class);
                startActivity(privacyPolicyActiviy);
            }
        });

        versionText = (TextView) view.findViewById(R.id.text_version);
        versionText.setText(String.valueOf(BuildConfig.VERSION_NAME));

        return view;
    }

}
