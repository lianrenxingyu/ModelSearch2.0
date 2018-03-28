package com.chenggong.modelsearch.aty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chenggong.modelsearch.R;

public class RegisterActivity extends BaseActivity {

    private Button btn_register;
    private EditText et_userName, et_first_pwd, et_second_pwd, et_email, et_company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn_register = findViewById(R.id.btn_register);
        et_userName = findViewById(R.id.userName);
        et_first_pwd = findViewById(R.id.first_password);
        et_second_pwd = findViewById(R.id.second_password);
        et_email = findViewById(R.id.email);
        et_company = findViewById(R.id.company);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  TODO 检查输入是否符合规范，提交数据，返回登录界面
            }
        });

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }
}
