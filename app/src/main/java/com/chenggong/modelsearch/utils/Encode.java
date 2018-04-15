package com.chenggong.modelsearch.utils;

import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chenggong on 18-4-2.
 * <p>
 * 把图片转码成为base64文件，编码过程
 */

public class Encode {
    public static String encodeFile(String path) {
        InputStream inputStream = null;
        byte[] data = null; //byte数组

        //读取文件到byte数组
        try {

            inputStream = new FileInputStream(path);
            data = new byte[inputStream.available()];
            inputStream.read(data); //把文件内容写入data
            inputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Base64.encodeToString(data,Base64.DEFAULT);
    }
}
