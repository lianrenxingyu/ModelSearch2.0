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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
    private RecyclerView recycleView_result;
    private ListView listView_record;
    private RelativeLayout relative_record;
    private TextView tv_deleteRecord;
    private LinearLayout linear_turn_page;

    //翻页控件
    private ImageView iv_next_page;
    private ImageView iv_last_page;
    private TextView tv_pagesNum;

    private List<String> recordList; //历史记录
    private List<Result> resultList = new ArrayList<>();//搜索出来的结果
    private ResultAdapter resultAdapter;
    private ArrayAdapter<String> recordAdapter;
    private RecordSQLHandle sqlHandle;

    private String tempHashcode;
    private String tempPagesNum;
    private String tempType; //暂时记录本次搜索的类型
    private String tempSearchName;//用于暂时存储搜索物体的名称,表示当前搜索的物体的名字
    private boolean hasInit;//标志,mRecyclerView是否已经初始化,该标志用于在返回按钮时的判断

    private String path;//图片路径
    private String type;//搜索类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        btn_textSearch = findViewById(R.id.btn_textSearch);
        et_textSearch = findViewById(R.id.et_textSearch);
        recycleView_result = findViewById(R.id.result_recyclerView);
        listView_record = findViewById(R.id.listview_record);
        relative_record = findViewById(R.id.relative_record);
        tv_deleteRecord = findViewById(R.id.tv_deleteRecord);
        linear_turn_page = findViewById(R.id.ll_turn_page);
        iv_last_page = findViewById(R.id.iv_last_page);
        iv_next_page = findViewById(R.id.iv_next_page);
        tv_pagesNum = findViewById(R.id.tv_pagesNum);

        btn_textSearch.setOnClickListener(this);
        tv_deleteRecord.setOnClickListener(this);
        listView_record.setOnTouchListener(this);
        iv_next_page.setOnClickListener(this);
        iv_last_page.setOnClickListener(this);

        //初始化数据
//        initData();

        //对Record中的listview进行操作
        sqlHandle = new RecordSQLHandle(this);
        recordList = new ArrayList<>();
        recordList.addAll(sqlHandle.getAllRecord());
        recordAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recordList);
        listView_record.setAdapter(recordAdapter);

        listView_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //注意setText方法会调用 textChangeListener,在afterTextListener方法中导致RecordList变化
                //所以提前取出点击获得的变量名字
                String name = recordList.get(position);
                et_textSearch.setText(name);
                et_textSearch.setCursorVisible(false);
                relative_record.setVisibility(View.GONE);
                textSearch(name);
            }
        });

        //点击弹出历史记录界面
        et_textSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_textSearch.setCursorVisible(true);
                List<String> tempList = sqlHandle.getAllRecord();
                recordList.clear();
                recordList.addAll(tempList);
                recordAdapter.notifyDataSetChanged();
                showRecordView();

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

        //文本框的输入进行监听
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
                List<String> tempList = sqlHandle.querySimilar(name);
                recordList.addAll(tempList);
                recordAdapter.notifyDataSetChanged();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView_result.setLayoutManager(layoutManager);
        resultAdapter = new ResultAdapter(this, resultList);
        recycleView_result.setAdapter(resultAdapter);

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
            case R.id.tv_deleteRecord:
                sqlHandle.clearData();
                recordList.clear();
                recordList.addAll(sqlHandle.getAllRecord());
                recordAdapter.notifyDataSetChanged();
                break;
            case R.id.iv_next_page:
                //todo 下一页功能
                tempType = getReloadType(tempType);
                tempPagesNum = String.valueOf(Integer.valueOf(tempPagesNum) + 1);//加1操作
                turnPage(tempType, tempPagesNum, tempHashcode);
                break;
            case R.id.iv_last_page:
                //todo 上一页
                tempType = getReloadType(tempType);
                tempPagesNum = String.valueOf(Integer.valueOf(tempPagesNum) - 1);//加1操作
                turnPage(tempType, tempPagesNum, tempHashcode);
                break;
        }
    }

    /**
     * 默认搜索第一页
     */
    private void textSearch(String name) {
        textSearch(name, String.valueOf(1));
    }

    /**
     * 通过模型名称搜索
     *
     * @param name 模型的名字
     */
    private void textSearch(String name, String pagesNum) {
        if (name.trim().equals("")) {
            Toast.makeText(ResultActivity.this, "搜索内容不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        tempSearchName = name;
        sqlHandle.insert(name);// 插入历史记录数据库
        search(Configure.NAME_TYPE, name.trim(), pagesNum, "", "", Configure.NAME_UPLOAD_URL);
    }

    /**
     * 默认搜索第一页
     */
    private void imageSearch(String name, String path) {
        imageSearch(name, path, String.valueOf(1));
    }

    /**
     * 图片搜索
     *
     * @param name 图片的名称
     * @param path 图片路径
     */
    private void imageSearch(String name, String path, String pagesNum) {

        //图片编码
        String imageEncode = Encode.encodeFile(path);
        Logger.d(TAG, imageEncode);

        search(Configure.IMAGE_TYPE, name, pagesNum, imageEncode, "", Configure.IAMGE_UPLOAD_URL);
    }

    /**
     * 图片reload
     *
     * @param pagesNum
     * @param hashcode
     */
    private void imageReload(String pagesNum, String hashcode) {
        search(Configure.IMAGE_Reload_TYPE, "", pagesNum, "", hashcode, Configure.IAMGE_RELOAD_URL);
    }

    private void objReload() {
        //TODO 三维物体reload
    }


    /**
     * 翻页操作,reload操作
     *
     * @param type     搜索类型
     * @param pagesNum 要要获得的页码
     * @param hashcode
     */
    private void turnPage(String type, String pagesNum, String hashcode) {
        //文字类型
        if (type.equals(Configure.NAME_TYPE)) {
            textSearch(et_textSearch.getText().toString(), pagesNum);
        } else if (type.equals(Configure.IMAGE_Reload_TYPE)) {
            imageReload(pagesNum, hashcode);
        } else if (type.equals(Configure.OBJECT_Reload_TYPE)) {
            //TODO 模型reload
        }

        tv_pagesNum.setText("第" + tempPagesNum + "页");
        if (pagesNum.equals("1")) {
            iv_last_page.setVisibility(View.INVISIBLE);
        } else {
            iv_last_page.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 网络搜索操作的核心方法
     *
     * @param type     搜索四种类型
     * @param fileName 文件名字
     * @param pagesNum 请求的页码
     * @param encoded  编码,指图片和三维模型
     * @param hashcode hashcode,用于reload
     * @param url      请求的网络地址
     */
    private void search(String type, String fileName, String pagesNum, String encoded, String hashcode, String url) {
        tempType = type;
        SearchReqBean reqBean = new SearchReqBean(type, fileName, pagesNum, encoded, hashcode);
        String jsonStr = JSON.toJSONString(reqBean);
        Logger.d(TAG, jsonStr);
        HttpUtil.sendOkHttpRequest(url, jsonStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ResultActivity.this, "搜索出现错误", Toast.LENGTH_SHORT).show();
                    }
                });
                Logger.d(TAG, "响应出现错误");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseStr = response.body().string();
                Logger.d(TAG, responseStr);
                JSONObject dataObject = HttpUtil.preHandle(responseStr);
                List<Result> tempList = HttpUtil.handleResponse(dataObject);
                resultList.clear();
                resultList.addAll(tempList);
                tempHashcode = HttpUtil.getHashcode(dataObject);
                tempPagesNum = HttpUtil.getPagesNum(dataObject);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultList.size() == 0) {
                            Toast.makeText(ResultActivity.this, "什么都没有搜索到", Toast.LENGTH_LONG).show();
                        }
                        resultAdapter.notifyDataSetChanged();
                        recycleView_result.scrollToPosition(0);
                        et_textSearch.setCursorVisible(false);

                        hasInit = true;
                        //关闭输入法
                        InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        methodManager.hideSoftInputFromWindow(et_textSearch.getWindowToken(), 0);

                        hideRecordView();
                    }
                });

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && relative_record.getVisibility() == View.VISIBLE) {
            //如果已经有搜索结果,则返回到搜索结果界面
            if (hasInit) {
                et_textSearch.setText(tempSearchName);
                hideRecordView();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.listview_record:
                float startY = 0;
                float endY;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
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

    /**
     * 通过判读当前搜索type获取reload的type类型
     *
     * @param type
     */
    private String getReloadType(String type) {

        if (type.equals(Configure.IMAGE_TYPE)) {
            return Configure.IMAGE_Reload_TYPE;
        } else if (type.equals(Configure.OBJECT_TYPE)) {
            return Configure.OBJECT_Reload_TYPE;
        } else {
            return type;
        }
    }

    /**
     * 隐藏历史记录
     */
    private void hideRecordView() {
        relative_record.setVisibility(View.GONE);
        recycleView_result.setVisibility(View.VISIBLE);
        linear_turn_page.setVisibility(View.VISIBLE);
    }

    /**
     * 展示历史记录
     */
    private void showRecordView() {
        recycleView_result.setVisibility(View.GONE);
        linear_turn_page.setVisibility(View.GONE);
        relative_record.setVisibility(View.VISIBLE);
    }
}
