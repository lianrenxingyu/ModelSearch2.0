package com.chenggong.modelsearch.aty;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chenggong.modelsearch.R;
import com.chenggong.modelsearch.bean.SearchReqBean;
import com.chenggong.modelsearch.net.HttpUtil;
import com.chenggong.modelsearch.utils.Configure;
import com.chenggong.modelsearch.utils.Encode;
import com.chenggong.modelsearch.utils.Logger;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private EditText et_textSearch;
    private ImageView iv_gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_textSearch = findViewById(R.id.et_textSearch);
        et_textSearch.setFocusable(false);//通过设置输入框无法获得焦点，可以防止输入法弹出
        iv_gallery = findViewById(R.id.iv_gallery);
        et_textSearch.setOnClickListener(this);
        iv_gallery.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_login:
                LoginActivity.start(MainActivity.this);
                break;
        }
        return true;
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_textSearch:
                ResultActivity.start(MainActivity.this);
                break;
            case R.id.iv_gallery:

                Toast.makeText(MainActivity.this, "从图库选择图片", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "选择图片"), 1);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if (resultCode != RESULT_OK){
                Toast.makeText(MainActivity.this, "选择图片出现错误", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri uri = data.getData();


            /**
             * 注意: 在系统版本5.0以上和5.0以下，从uri获取图片路径的方法不同，在5.0以上可以直接  {@link uri.getpath()}
             */
            String[] proj = {MediaStore.Images.Media.DATA};
            //好像是android多媒体数据库的封装接口，具体的看Android文档
            Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
            //获得用户选择的图片的索引值
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            //将光标移至开头 ，这个很重要，不小心很容易引起越界
            cursor.moveToFirst();
            //最后根据索引值获取图片路径   结果类似：/mnt/sdcard/DCIM/Camera/IMG_20151124_013332.jpg
            String path = cursor.getString(column_index);


            Logger.d(TAG,path);
            Logger.d(TAG,uri.getPath());


            ResultActivity.start(this,path,Configure.IMAGE_TYPE);


        }
    }

}






















