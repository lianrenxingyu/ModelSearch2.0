package com.chenggong.modelsearch.Aty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chenggong.modelsearch.Adapter.ResultAdapter;
import com.chenggong.modelsearch.Bean.Result;
import com.chenggong.modelsearch.R;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends Activity {

    private RecyclerView mRecyclerView;
    private List<Result> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //初始化数据
        initData();

        mRecyclerView = findViewById(R.id.result_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        ResultAdapter resultAdapter = new ResultAdapter(resultList);
        mRecyclerView.setAdapter(resultAdapter);

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
        resultList = new ArrayList<>();
        for(int i = 0;i<10;i++){
            resultList.add(result);
        }
    }
    public static void start(Context context){
        Intent intent = new Intent(context, ResultActivity.class);
        context.startActivity(intent);
    }
}
