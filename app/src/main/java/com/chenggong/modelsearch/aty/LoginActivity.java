package com.chenggong.modelsearch.aty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chenggong.modelsearch.R;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    TextView tv_register;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tv_register = findViewById(R.id.tv_register);
        btn_login = findViewById(R.id.btn_login);
        tv_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                RegisterActivity.start(LoginActivity.this);
                break;
            case R.id.btn_login:
                // TODO  登录操作
        }
    }
}
