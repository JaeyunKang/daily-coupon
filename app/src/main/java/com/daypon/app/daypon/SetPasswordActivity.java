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

public class SetPasswordActivity extends AppCompatActivity {

    EditText userPassword;
    EditText userPasswordCheck;

    Button setPasswordButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);


        userPassword = (EditText) findViewById(R.id.user_password);
        userPasswordCheck = (EditText) findViewById(R.id.user_password_check);

        setPasswordButton = (Button) findViewById(R.id.set_password_btn);

        setPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userIdString = getIntent().getExtras().getString("user_id");
                String userPasswordString = userPassword.getText().toString();
                String userPasswordCheckString = userPasswordCheck.getText().toString();

                if (userPasswordString.equals("") || userPasswordCheckString.equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호와 비밀번호확인칸을 모두 입력해주세요.", Toast.LENGTH_LONG).show();
                } else if (!userPasswordString.equals(userPasswordCheckString)){
                    Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        ContentValues values = new ContentValues();
                        values.put("user_id", userIdString);
                        values.put("user_password", userPasswordString);
                        SetPasswordTask task = new SetPasswordTask(values);
                        task.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private class SetPasswordTask extends AsyncTask<Void, Void, String> {

        String url = "http://daypon.com/set-password";
        ContentValues values;

        SetPasswordTask(ContentValues values) {
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

                Toast.makeText(getApplicationContext(), "비밀번호가 재설정되었습니다. 로그인해주세요!", Toast.LENGTH_LONG).show();
                Intent loginActivity = new Intent(SetPasswordActivity.this, LoginActivity.class);
                loginActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginActivity);
                finish();


            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
