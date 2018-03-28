package com.chenggong.modelsearch.aty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by chenggong on 18-3-23.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    /**
//     * 是否移除actionBar
//     * @param flag  true 代表没有actionBar
//     */
//    protected void removeActionBar(boolean flag){
//        if(flag){
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
//        }
//    }
}
