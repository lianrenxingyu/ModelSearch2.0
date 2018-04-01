package com.chenggong.modelsearch.aty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chenggong.modelsearch.R;

public class WebPageActivity extends Activity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);

        openWebPage();
    }

    /**
     * 打开网址
     */
    private void  openWebPage(){
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        webView=findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);    //javaScript支持
        settings.setSupportZoom(true);          //支持缩放

        settings.setBuiltInZoomControls(true);// 设置出现缩放工具

        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    //设置返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     *
     * @param context
     * @param url 传入一个网页的URL地址
     */
    public static void start(Context context,String url) {
        Intent intent = new Intent(context, WebPageActivity.class);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }
}
