package com.chenggong.modelsearch.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chenggong.modelsearch.bean.Result;

import java.util.List;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by chenggong on 18-3-29.
 * 网络请求工具
 */

public class HttpUtil {
    /**
     * 发送网络post请求 ，传送的数据格式是json
     *
     * @param url      url地址
     * @param jsonStr  发送的json字符串
     * @param callback 回调
     */
    public static void sendOkHttpRequest(String url, String jsonStr, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        //mediaType 就是content-type 传递的数据类型
        MediaType jsonType = MediaType.parse("application/json;charset=utf-8");
        RequestBody body = RequestBody.create(jsonType, jsonStr);
        Request request = new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 处理响应的字符串数据,转化成为resultBean的格式
     *
     * @param responseStr
     * @return
     */
    public static List<Result> handleResponse(String responseStr) {
        List<Result> resultList;
        Result result;
        JSONObject dataObject = JSON.parseObject(responseStr);
        JSONArray dataArray = dataObject.getJSONArray("data");
        //TODO data中的很多数据并没有用，可以考虑过滤之类的操作
        resultList = JSON.parseArray(dataArray.toJSONString(), Result.class);
        return resultList;
    }

}
