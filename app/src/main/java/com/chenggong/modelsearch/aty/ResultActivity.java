package com.chenggong.modelsearch.aty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chenggong.modelsearch.R;
import com.chenggong.modelsearch.adapter.ResultAdapter;
import com.chenggong.modelsearch.bean.Result;
import com.chenggong.modelsearch.bean.SearchReqBean;
import com.chenggong.modelsearch.db.RecordSQLHandle;
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
    private ListView lv_record;

    private List<String> recordList; //历史记录
    private List<Result> resultList = new ArrayList<>();//搜索出来的结果
    private ResultAdapter resultAdapter;
    ArrayAdapter<String> recordAdapter;
    private RecordSQLHandle sqlHandle;

    private boolean hasInit;//标志,mRecyclerView是否已经初始化,该标志用于在返回按钮时的判断

    private String path;//图片路径
    private String type;//搜索类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        btn_textSearch = findViewById(R.id.btn_textSearch);
        et_textSearch = findViewById(R.id.et_textSearch);
        mRecyclerView = findViewById(R.id.result_recyclerView);
        lv_record = findViewById(R.id.lv_record);

        btn_textSearch.setOnClickListener(this);
        mRecyclerView.setOnTouchListener(this);

        //初始化数据
//        initData();

        //对Record中的listview进行操作
//        {"成功", "成功", "成功", "成功", "成功", "成功", "成功", "成功", "成功", "成功", "成功", "成功", "成功", "成功", "成功", "成功", "哈哈哈"};
        sqlHandle = new RecordSQLHandle(this);
        recordList = new ArrayList<>();
        recordList.addAll(sqlHandle.getAllRecord());
        recordAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, recordList);
        lv_record.setAdapter(recordAdapter);

        lv_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                et_textSearch.setText(recordList.get(position));
                textSearch(recordList.get(position));
            }
        });

        //点击弹出历史记录界面
        et_textSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> tempList= sqlHandle.getAllRecord();
                recordList.clear();
                recordList.addAll(tempList);
                recordAdapter.notifyDataSetChanged();
                mRecyclerView.setVisibility(View.GONE);
                lv_record.setVisibility(View.VISIBLE);

            }
        });

        //对键盘enter键的改写,监听搜索
        et_textSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    textSearch(et_textSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });

        et_textSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String name = et_textSearch.getText().toString().trim();
                recordList.clear();
                List<String> tempList = sqlHandle.querySimlar(name);
                recordList.addAll(tempList);
                recordAdapter.notifyDataSetChanged();
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        resultAdapter = new ResultAdapter(this, resultList);
        mRecyclerView.setAdapter(resultAdapter);

        path = getIntent().getStringExtra("path");
        type = getIntent().getStringExtra("type");

        //判断类型
        if (type.equals(Configure.IMAGE_TYPE) && path != null) {
            //TODO name字段的截取
            imageSearch("飞机", path);
        }

    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        //通过监听焦点的活动情况，设置输入法的弹出关闭状态
//        InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (hasFocus && type.equals(Configure.NAME_TYPE)) {
//            methodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//            boolean isOpen = methodManager.isActive();
//            if (!isOpen) {  //通过判断输入法弹出状态，如果关闭，则弹出输入法
//                methodManager.showSoftInput(et_textSearch, InputMethodManager.SHOW_FORCED);
//            }
//            Logger.d(TAG, "执行");
//        } else if (!hasFocus) {
//            methodManager.hideSoftInputFromWindow(et_textSearch.getWindowToken(), 0);
//        }
//    }


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
        if (name.trim().equals("")) {
            Toast.makeText(ResultActivity.this, "搜索内容不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        sqlHandle.insert(name);// 插入历史记录数据库
        SearchReqBean reqBean = new SearchReqBean(Configure.NAME_TYPE, name.trim());
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
                resultList.addAll(tempList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultAdapter.notifyDataSetChanged();
                        mRecyclerView.smoothScrollToPosition(0);

                        hasInit = true;
                        //关闭输入法
                        InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        methodManager.hideSoftInputFromWindow(et_textSearch.getWindowToken(), 0);

                        lv_record.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
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
                resultList.addAll(tempList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultAdapter.notifyDataSetChanged();
                        mRecyclerView.smoothScrollToPosition(0);

                        hasInit = true;
                        //关闭输入法
                        InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        methodManager.hideSoftInputFromWindow(et_textSearch.getWindowToken(), 0);

                        lv_record.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                    }
                });

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && lv_record.getVisibility() == View.VISIBLE) {
            //如果已经有搜索结果,则返回到搜索结果界面
            if (hasInit) {
                lv_record.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
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
