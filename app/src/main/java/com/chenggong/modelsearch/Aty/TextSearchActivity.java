package com.chenggong.modelsearch.Aty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.chenggong.modelsearch.R;

public class TextSearchActivity extends Activity {

    private Button btn_textSearch;
    private EditText et_textSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_search);
        btn_textSearch = findViewById(R.id.btn_textSearch);
        et_textSearch = findViewById(R.id.et_textSearch);
        btn_textSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 实现搜索功能
            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (hasFocus) {
            methodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            boolean isOpen = methodManager.isActive();
            if (!isOpen) {  //通过判断输入法弹出状态，如果关闭，则弹出输入法
                methodManager.showSoftInput(et_textSearch,InputMethodManager.SHOW_FORCED);
            }
        } else if (!hasFocus) {
            methodManager.hideSoftInputFromWindow(et_textSearch.getWindowToken(), 0);
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, TextSearchActivity.class);
        context.startActivity(intent);
    }
}
