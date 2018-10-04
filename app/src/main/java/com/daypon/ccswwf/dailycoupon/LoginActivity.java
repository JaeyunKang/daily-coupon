package com.daypon.ccswwf.dailycoupon;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText idText;
    EditText passwordText;

    TextView loginButton;

    TextView registerButton;

    TextView findIdButton;
    TextView findPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        idText = (EditText) findViewById(R.id.user_id);
        passwordText = (EditText) findViewById(R.id.user_password);
        loginButton = (TextView) findViewById(R.id.login_btn);
        registerButton = (TextView) findViewById(R.id.btn_register);
        findIdButton = (TextView) findViewById(R.id.btn_find_id);
        findPasswordButton = (TextView) findViewById(R.id.btn_find_password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userId = idText.getText().toString();
                String userPassword = passwordText.getText().toString();
                if(userId.equals("") || userPassword.equals("")) {
                    Toast.makeText(getApplicationContext(), "아이디와 패스워드를 모두 입력해주세요!", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        ContentValues values = new ContentValues();
                        values.put("user_id", idText.getText().toString());
                        values.put("user_password", passwordText.getText().toString());
                        LoginTask task = new LoginTask(values);
                        task.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerActivity = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerActivity);
            }
        });

        findIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent findIdActivity = new Intent(LoginActivity.this, FindIdActivity.class);
                startActivity(findIdActivity);
            }
        });

        findPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent findPasswordActivity = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(findPasswordActivity);
            }
        });

    }

    private class LoginTask extends AsyncTask<Void, Void, String> {

        String url = "http://daypon.com/login";
        ContentValues values;

        LoginTask(ContentValues values) {
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
                if (resultObject.getInt("error") == 1) {
                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 다시 확인해주세요!", Toast.LENGTH_LONG).show();
                } else {
                    SharedPreferences userInformation = getSharedPreferences("user", 0);
                    SharedPreferences.Editor editor = userInformation.edit();

                    editor.putString("user_id", idText.getText().toString());
                    editor.commit();

                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainActivity);
                    finish();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

}
