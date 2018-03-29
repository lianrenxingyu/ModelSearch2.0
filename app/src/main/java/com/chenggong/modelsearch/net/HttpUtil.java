package com.chenggong.modelsearch.net;

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
     * @param url url地址
     * @param jsonStr 发送的json字符串
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
}
