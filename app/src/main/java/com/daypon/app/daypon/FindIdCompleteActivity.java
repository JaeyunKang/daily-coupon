package com.daypon.app.daypon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FindIdCompleteActivity extends AppCompatActivity {

    TextView userIdText;

    LinearLayout loginButton;
    LinearLayout findPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id_complete);

        Bundle extras = getIntent().getExtras();
        String userId = extras.getString("user_id");

        userIdText = (TextView) findViewById(R.id.user_id);
        userIdText.setText(userId);

        loginButton = (LinearLayout) findViewById(R.id.login_btn);
        findPasswordButton = (LinearLayout) findViewById(R.id.find_password_btn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginActivity = new Intent(FindIdCompleteActivity.this, LoginActivity.class);
                loginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginActivity);
                finish();
            }
        });

        findPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent findPasswordActivity = new Intent(FindIdCompleteActivity.this, FindPasswordActivity.class);
                findPasswordActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(findPasswordActivity);
                finish();
            }
        });

    }
}
