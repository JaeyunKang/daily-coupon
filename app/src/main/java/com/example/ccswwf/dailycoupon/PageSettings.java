package com.example.ccswwf.dailycoupon;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PageSettings extends Fragment {

    private TextView versionText;
    private LinearLayout privacyPolicyView;
    private LinearLayout termsView;
    private LinearLayout logoutView;

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

        privacyPolicyView = (LinearLayout) view.findViewById(R.id.layout_privacy_policy);
        privacyPolicyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent privacyPolicyActiviy = new Intent(getActivity(), PrivatePolicyActivity.class);
                startActivity(privacyPolicyActiviy);
            }
        });

        termsView = (LinearLayout) view.findViewById(R.id.layout_terms);
        termsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent termsActivity = new Intent(getActivity(), TermsActivity.class);
                startActivity(termsActivity);
            }
        });

        logoutView = (LinearLayout) view.findViewById(R.id.layout_logout);

        SharedPreferences userInformation = getActivity().getSharedPreferences("user", 0);
        String userId = userInformation.getString("user_id", "");

        if (userId.equals("")) {
            logoutView.setVisibility(View.GONE);
        } else {
            logoutView.setVisibility(View.VISIBLE);
        }
        logoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dlg = new Dialog(getActivity());
                dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dlg.setContentView(R.layout.dialog_logout);
                Window window = dlg.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(wlp);
                dlg.show();

                final ImageView closeBtn = (ImageView) dlg.findViewById(R.id.close_btn);
                final LinearLayout logoutBtn = (LinearLayout) dlg.findViewById(R.id.logout_btn);

                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dlg.dismiss();
                    }
                });

                logoutBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences userInformation = getActivity().getSharedPreferences("user", 0);
                        SharedPreferences.Editor editor = userInformation.edit();
                        editor.putString("user_id", "");
                        editor.commit();

                        Intent loginActivity = new Intent(getActivity(), LoginActivity.class);
                        startActivity(loginActivity);
                        getActivity().finish();
                    }
                });
            }
        });

        versionText = (TextView) view.findViewById(R.id.text_version);
        versionText.setText(BuildConfig.VERSION_NAME);

        return view;
    }

}
