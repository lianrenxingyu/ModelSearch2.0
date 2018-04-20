package com.chenggong.modelsearch.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.chenggong.modelsearch.bean.Result;

import java.util.ArrayList;
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
     * 预处理,提高效率,减少重复解析
     * @param responseStr
     * @return
     */
    public static JSONObject preHandle(String responseStr){
        return JSON.parseObject(responseStr);
    }
    /**
     * 处理响应的字符串数据,转化成为resultBean的格式
     * @param dataObject
     * @return
     */
    public static List<Result> handleResponse(JSONObject dataObject) {
        List<Result> resultList;
        List<String> imgWebURLList;
        JSONArray dataArray = dataObject.getJSONArray("data");
        //TODO data中的很多数据并没有用，可以考虑过滤之类的操作
        resultList = JSON.parseArray(dataArray.toJSONString(), Result.class);

        //获取imgWebURL列表的第一个URL
        for (int i = 0;i<resultList.size();i++) {
            Result result = resultList.get(i);
            imgWebURLList = result.getImgWebURL();
            JSONObject jsonObject = JSON.parseObject(imgWebURLList.get(0));
            String imgURL = jsonObject.getString("imgURL");
            result.setImgURL(imgURL);
        }
        return resultList;
    }

    /**
     * 返回页码
     */
    public static String getPagesNum(JSONObject dataObject){
        return dataObject.getString("pagesNum");
    }
    /**
     * 返回hashcode
     */
    public static  String getHashcode(JSONObject dataObject){
        return dataObject.getString("hashcode");
    }

}
