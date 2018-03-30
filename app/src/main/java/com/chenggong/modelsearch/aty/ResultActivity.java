package com.chenggong.modelsearch.aty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chenggong.modelsearch.adapter.ResultAdapter;
import com.chenggong.modelsearch.bean.Result;
import com.chenggong.modelsearch.R;
import com.chenggong.modelsearch.bean.SearchReqBean;
import com.chenggong.modelsearch.net.HttpUtil;
import com.chenggong.modelsearch.utils.Configure;
import com.chenggong.modelsearch.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ResultActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "ResultActivity";
    private Button btn_textSearch;
    private EditText et_textSearch;
    private RecyclerView mRecyclerView;
    private List<Result> resultList;
    private ResultAdapter resultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        btn_textSearch = findViewById(R.id.btn_textSearch);
        et_textSearch = findViewById(R.id.et_textSearch);
        btn_textSearch.setOnClickListener(this);

        //初始化数据
        initData();

        mRecyclerView = findViewById(R.id.result_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        resultAdapter = new ResultAdapter(this, resultList);
        mRecyclerView.setAdapter(resultAdapter);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (hasFocus) {
            methodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            boolean isOpen = methodManager.isActive();
            if (!isOpen) {  //通过判断输入法弹出状态，如果关闭，则弹出输入法
                methodManager.showSoftInput(et_textSearch, InputMethodManager.SHOW_FORCED);
            }
            Logger.d(TAG, "执行");
        } else if (!hasFocus) {
            methodManager.hideSoftInputFromWindow(et_textSearch.getWindowToken(), 0);
        }
    }


    //初始化数据
    public void initData() {
        Result result = new Result();
        result.setAuthor("作者");
        result.setDescription("这是一段简单的描述这是一段简单的描述这是一段简单的描述这是一段简单的描述这是一段简单的描述这是一段简单的描述");
        result.setSource("www.baidu.com");
        result.setTimePost("2017-01-01");
        result.setName("水壶");
        result.setWebpageURL("baidu.com/shuihu/101aaaaaaaaaaa");
        result.setImgURL("http://www.dayin.la/attachment/thumb/2017-09/27/product_74818_800x600.jpg");
        resultList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            resultList.add(result);
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ResultActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_textSearch:
                SearchReqBean reqBean = new SearchReqBean(Configure.NAME_TYPE, et_textSearch.getText().toString());
                String jsonStr = JSON.toJSONString(reqBean);
                Logger.d(TAG, jsonStr);
                HttpUtil.sendOkHttpRequest(Configure.NAME_UPLOAD_URL, jsonStr, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ResultActivity.this, "出现错误", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Logger.d(TAG, "响应出现错误");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseStr = response.body().string();
                        Logger.d(TAG, responseStr);
                        List<Result> tempList = HttpUtil.handleResponse(responseStr);
                        resultList.clear();
                        for (Result result : tempList) {
                            resultList.add(result);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                resultAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
                // TODO 完成搜索功能
                break;
        }
    }

}
