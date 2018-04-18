package com.chenggong.modelsearch.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    private static final String TAG = "Encode";

    public static String encodeFile(String path) {
        byte[] data = compressPicture(path);
//        //读取文件到byte数组
//        try {
//
//            inputStream = new FileInputStream(path);
//            data = new byte[inputStream.available()];
//            inputStream.read(data); //把文件内容写入data
//            inputStream.close();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (inputStream != null) {
//                    inputStream.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    /**
     * 图片压缩,并写入数据流.采用了两种压缩方式,采样压缩和compress压缩,通过compress压缩获得了byte[] 数组
     * @param path 图片在系统中的路径,还可以考虑通过uri获取图片
     * @return 返回图片的字节数组
     */
    private static byte[] compressPicture(String path) {


        /**
         * 对于available方法的说明,最好不用来开数组
         {@link InputStream inputStream = new FileInputStream(path);
         Logger.d(TAG, "test " + String.valueOf(inputStream.available()));}

         * Note that while some implementations of {@code InputStream} will return
         * the total number of bytes in the stream, many will not.  It is
         * never correct to use the return value of this method to allocate
         * a buffer intended to hold all data in this stream.
         */

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeFile(path, options);//当inJustDecodeBounds为true时,此方法返回为null
        if(bitmap == null) {
            Logger.d(TAG, "bitmap is null");
        }

        //图片占用内存压缩
        int oldWidth = options.outWidth;
        int oldHeight = options.outHeight;
        //目标大小
        int width = 512;
        int height = 512;

        int widthRadio = 1;
        int heightRadio = 1;
        if (oldWidth > width) {
            widthRadio = oldWidth / width;
        }
        if (oldHeight > height) {
            heightRadio = oldHeight / height;
        }

        options.inSampleSize = widthRadio > heightRadio ? widthRadio : heightRadio;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(path, options);
        Logger.d(TAG, "采样比 :"+ String.valueOf(options.inSampleSize)+ " 压缩后图片大小 :" +String.valueOf(bitmap.getByteCount()/1024/8)+"kb");

        // 把bitmap写入一个byte数组
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] data = outputStream.toByteArray();
        Logger.d(TAG, "compress 再次压缩 data length  "+String.valueOf(data.length/1024/8)+"kb");

        return data;

    }
}
