package com.example.ccswwf.dailycoupon;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    public static int version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        version = BuildConfig.VERSION_CODE;

        try {
            ContentValues values = new ContentValues();
            VersionCheckTask task = new VersionCheckTask(values);
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class VersionCheckTask extends AsyncTask<Void, Void, String> {

        String url = "http://prography.org/version-check";
        ContentValues values;

        VersionCheckTask(ContentValues values) {
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

                if (resultObject.getInt("version") == version) {
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                            SplashActivity.this.startActivity(mainIntent);
                            SplashActivity.this.finish();
                        }
                    }, SPLASH_DISPLAY_LENGTH);

                } else {
                    AlertDialog.Builder update = new AlertDialog.Builder(SplashActivity.this);
                    update.setMessage("업데이트가 있습니다");
                    update.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            SplashActivity.this.finish();
                        }
                    });
                    update.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            SplashActivity.this.finish();
                        }
                    });
                    AlertDialog alertUpdate = update.create();
                    alertUpdate.show();
                }

            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
