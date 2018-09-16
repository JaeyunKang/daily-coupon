package com.example.ccswwf.dailycoupon;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    EditText userId;
    EditText userPassword;
    EditText userPasswordCheck;

    CheckBox allCheck;
    CheckBox privatePolicyCheck;
    CheckBox termsCheck;
    CheckBox alertCheck;

    EditText phoneNumber;
    EditText confirmNumber;

    Button phoneNumberCheckButton;
    Button confirmNumberCheckButton;

    TextView showTerms;
    TextView showPrivatePolicy;
    TextView showAlert;

    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userId = (EditText) findViewById(R.id.user_id);
        userPassword = (EditText) findViewById(R.id.user_password);
        userPasswordCheck = (EditText) findViewById(R.id.user_password_check);

        allCheck = (CheckBox) findViewById(R.id.check_all);
        privatePolicyCheck = (CheckBox) findViewById(R.id.check_private_policy);
        termsCheck = (CheckBox) findViewById(R.id.check_terms);
        alertCheck = (CheckBox) findViewById(R.id.check_alert);

        phoneNumber = (EditText) findViewById(R.id.phone_number);
        confirmNumber = (EditText) findViewById(R.id.confirm_number);

        phoneNumberCheckButton = (Button) findViewById(R.id.phone_number_check);
        confirmNumberCheckButton = (Button) findViewById(R.id.confirm_number_check);

        showTerms = (TextView) findViewById(R.id.show_terms);
        showPrivatePolicy = (TextView) findViewById(R.id.show_private_policy);
        showAlert = (TextView) findViewById(R.id.show_alert);

        registerButton = (Button) findViewById(R.id.register_btn);

        allCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean checked = allCheck.isChecked();
                privatePolicyCheck.setChecked(checked);
                termsCheck.setChecked(checked);
                alertCheck.setChecked(checked);
            }
        });

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

        showTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent termsActivity = new Intent(RegisterActivity.this, TermsActivity.class);
                startActivity(termsActivity);
            }
        });

        showPrivatePolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent privatePolicyActivity = new Intent(RegisterActivity.this, PrivatePolicyActivity.class);
                startActivity(privatePolicyActivity);
            }
        });

        showAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent alertActivity = new Intent(RegisterActivity.this, AlertActivity.class);
                startActivity(alertActivity);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userIdString = userId.getText().toString();
                String userPasswordString = userPassword.getText().toString();
                String userPasswordCheckString = userPasswordCheck.getText().toString();

                if (userIdString.equals("")) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_LONG).show();
                } else if (userPasswordString.equals("") || userPasswordCheckString.equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호와 비밀번호확인칸을 모두 입력해주세요.", Toast.LENGTH_LONG).show();
                } else if (!userPasswordString.equals(userPasswordCheckString)){
                    Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        ContentValues values = new ContentValues();
                        values.put("user_id", userIdString);
                        values.put("user_password", userPasswordString);
                        values.put("phone_number", phoneNumber.getText().toString());
                        RegisterTask task = new RegisterTask(values);
                        task.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private class PhoneNumberCheckTask extends AsyncTask<Void, Void, String> {

        String url = "http://prography.org/phone-number-check";
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

        String url = "http://prography.org/confirm-number-check";
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
                String msg = jsonObject.getString("msg");
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class RegisterTask extends AsyncTask<Void, Void, String> {

        String url = "http://prography.org/register";
        ContentValues values;

        RegisterTask(ContentValues values) {
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
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
