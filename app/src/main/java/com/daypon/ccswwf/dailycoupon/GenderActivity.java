package com.daypon.ccswwf.dailycoupon;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import org.json.JSONObject;

public class GenderActivity extends AppCompatActivity {

    int gender;
    LinearLayout manLayout;
    LinearLayout womanLayout;
    ImageButton manButton;
    ImageButton womanButton;
    Button button;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);

        gender = 2;

        manLayout = (LinearLayout) findViewById(R.id.manLayout);
        womanLayout = (LinearLayout) findViewById(R.id.womanLayout);
        manButton = (ImageButton) findViewById(R.id.manButton);
        womanButton = (ImageButton) findViewById(R.id.womanButton);
        button = (Button) findViewById(R.id.button);

        SharedPreferences userInformation = getSharedPreferences("user", 0);
        userId = userInformation.getString("user_id", "");

        manButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manLayout.setAlpha(1.0f);
                womanLayout.setAlpha(0.5f);
                button.setAlpha(1.0f);
                gender = 0;
            }
        });

        womanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                womanLayout.setAlpha(1.0f);
                manLayout.setAlpha(0.5f);
                button.setAlpha(1.0f);
                gender = 1;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ContentValues values = new ContentValues();
                    values.put("gender", gender);
                    values.put("user_id", userId);
                    GenderUpdateTask task = new GenderUpdateTask(values);
                    task.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private class GenderUpdateTask extends AsyncTask<Void, Void, String> {

        String url = "http://prography.org/gender-update";
        ContentValues values;

        GenderUpdateTask(ContentValues values) {
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
                JSONObject jsonObject = new JSONObject(result);
                Intent birthActivity = new Intent(GenderActivity.this, BirthActivity.class);
                startActivity(birthActivity);
                finish();

            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mainActivity = new Intent(GenderActivity.this, MainActivity.class);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainActivity);
    }
}