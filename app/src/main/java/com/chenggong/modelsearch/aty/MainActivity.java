package com.chenggong.modelsearch.aty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.chenggong.modelsearch.R;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_textSearch;
    private ImageView iv_gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_textSearch = findViewById(R.id.et_textSearch);
        et_textSearch.setFocusable(false);//通过设置输入框无法获得焦点，可以防止输入法弹出
        iv_gallery = findViewById(R.id.iv_gallery);
        et_textSearch.setOnClickListener(this);
        iv_gallery.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_login:
                LoginActivity.start(MainActivity.this);
                break;
        }
        return true;
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_textSearch:
                ResultActivity.start(MainActivity.this);
                break;
            case R.id.iv_gallery:
                //TODO 打开图库的操作
                break;
        }
    }
}
