package com.chenggong.modelsearch.aty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chenggong.modelsearch.R;
import com.chenggong.modelsearch.adapter.ResultAdapter;
import com.chenggong.modelsearch.bean.Result;
import com.chenggong.modelsearch.bean.SearchReqBean;
import com.chenggong.modelsearch.net.HttpUtil;
import com.chenggong.modelsearch.utils.Configure;
import com.chenggong.modelsearch.utils.Encode;
import com.chenggong.modelsearch.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ResultActivity extends Activity implements View.OnTouchListener, View.OnClickListener {

    private static final String TAG = "ResultActivity";
    private Button btn_textSearch;
    private EditText et_textSearch;
    private RecyclerView mRecyclerView;
    private List<Result> resultList;
    private ResultAdapter resultAdapter;

    private String path;//图片路径
    private String type;//搜索类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        btn_textSearch = findViewById(R.id.btn_textSearch);
        et_textSearch = findViewById(R.id.et_textSearch);
        mRecyclerView = findViewById(R.id.result_recyclerView);

        btn_textSearch.setOnClickListener(this);
        mRecyclerView.setOnTouchListener(this);

        //初始化数据
        initData();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        resultAdapter = new ResultAdapter(this, resultList);
        mRecyclerView.setAdapter(resultAdapter);

        path = getIntent().getStringExtra("path");
        type = getIntent().getStringExtra("type");
        if (type.equals(Configure.IMAGE_TYPE) && path != null) {
            //TODO name字段的截取
            imageSearch("飞机", path);
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        //通过监听焦点的活动情况，设置输入法的弹出关闭状态
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

    /**
     * 文本搜索通过这个方法启动
     */
    public static void start(Context context) {
        start(context, null, Configure.NAME_TYPE);
    }

    /**
     * @param context
     * @param path    本地选取图片的路径
     */
    public static void start(Context context, String path, String type) {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra("path", path);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //文字搜索功能
            case R.id.btn_textSearch:
                textSearch(et_textSearch.getText().toString());

                //关闭输入法
                InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                methodManager.hideSoftInputFromWindow(et_textSearch.getWindowToken(), 0);
                // TODO 完成搜索功能
                break;
        }
    }

    /**
     * 通过模型名称搜索
     *
     * @param name 模型的名字
     */
    private void textSearch(String name) {
        SearchReqBean reqBean = new SearchReqBean(Configure.NAME_TYPE, name);
        String jsonStr = JSON.toJSONString(reqBean);
        Logger.d(TAG, jsonStr);
        HttpUtil.sendOkHttpRequest(Configure.NAME_UPLOAD_URL, jsonStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ResultActivity.this, "模型搜索出现错误", Toast.LENGTH_SHORT).show();
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
                        mRecyclerView.smoothScrollToPosition(0);
                    }
                });
            }
        });
    }

    /**
     * 图片搜索
     *
     * @param name 图片的名称
     * @param path 图片路径
     */
    private void imageSearch(String name, String path) {

        //图片编码
        String imageEncode = Encode.encodeFile(path);
        Logger.d(TAG, imageEncode);

        SearchReqBean reqBean = new SearchReqBean(Configure.IMAGE_TYPE, name, imageEncode);
        String jsonStr = JSON.toJSONString(reqBean);
        Logger.d(TAG, jsonStr);
        HttpUtil.sendOkHttpRequest(Configure.IAMGE_UPLOAD_URL, jsonStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ResultActivity.this, "图片搜索出现错误", Toast.LENGTH_SHORT).show();
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
                        mRecyclerView.smoothScrollToPosition(0);
                    }
                });

            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.result_recyclerView:
                float startY = 0;
                float endY;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        endY = event.getY();
                        if ((endY - startY) > 15) {
                            //关闭输入法
                            InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            methodManager.hideSoftInputFromWindow(et_textSearch.getWindowToken(), 0);
                        }
                        break;
                }
                break;
        }
        return false;
    }
}
