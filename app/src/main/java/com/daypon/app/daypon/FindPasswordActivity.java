package com.daypon.app.daypon;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class FindPasswordActivity extends AppCompatActivity {

    EditText userIdTxt;
    EditText phoneNumber;
    EditText confirmNumber;

    Button phoneNumberCheckButton;
    Button confirmNumberCheckButton;

    Button findPasswordButton;

    private int confirmChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        findPasswordButton = (Button) findViewById(R.id.find_id_btn);

        confirmChecked = 0;

        userIdTxt = (EditText) findViewById(R.id.user_id);
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        confirmNumber = (EditText) findViewById(R.id.confirm_number);

        phoneNumberCheckButton = (Button) findViewById(R.id.phone_number_check);
        confirmNumberCheckButton = (Button) findViewById(R.id.confirm_number_check);

        phoneNumberCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phoneNumber.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "번호를 입력해주세요!", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    ContentValues values = new ContentValues();
                    values.put("phone_number", phoneNumber.getText().toString());
                    PhoneNumberCheckTask task = new PhoneNumberCheckTask(values);
                    task.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        confirmNumberCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(confirmNumber.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "인증번호를 입력해주세요!", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        ContentValues values = new ContentValues();
                        values.put("phone_number", phoneNumber.getText().toString());
                        values.put("confirm_number", confirmNumber.getText().toString());
                        ConfirmNumberCheckTask task = new ConfirmNumberCheckTask(values);
                        task.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        findPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (confirmChecked == 0){
                    Toast.makeText(getApplicationContext(), "번호 인증을 해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        ContentValues values = new ContentValues();
                        values.put("phone_number", phoneNumber.getText().toString());
                        FindPasswordTask task = new FindPasswordTask(values);
                        task.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private class PhoneNumberCheckTask extends AsyncTask<Void, Void, String> {

        String url = "http://daypon.com/phone-number-check-findid";
        ContentValues values;

        PhoneNumberCheckTask(ContentValues values) {
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
                String msg = jsonObject.getString("msg");
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ConfirmNumberCheckTask extends AsyncTask<Void, Void, String> {

        String url = "http://daypon.com/confirm-number-check";
        ContentValues values;

        ConfirmNumberCheckTask(ContentValues values) {
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
                int success = jsonObject.getInt("success");
                String msg = jsonObject.getString("msg");
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                confirmChecked = success;
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class FindPasswordTask extends AsyncTask<Void, Void, String> {

        String url = "http://daypon.com/find-id";
        ContentValues values;

        FindPasswordTask(ContentValues values) {
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

                int exists = jsonObject.getInt("exists");
                if (exists == 0) {
                    Toast.makeText(getApplicationContext(), "해당 번호로 가입된 아이디가 없습니다!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    String userId = jsonObject.getString("user_id");

                    if (!userId.equals(userIdTxt.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "인증된 번호로 가입된 아이디가 아닙니다!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Bundle b = new Bundle();
                    b.putString("user_id", userId);

                    Intent setPasswordActivity = new Intent(FindPasswordActivity.this, SetPasswordActivity.class);
                    setPasswordActivity.putExtras(b);
                    startActivity(setPasswordActivity);
                    finish();
                }


            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
