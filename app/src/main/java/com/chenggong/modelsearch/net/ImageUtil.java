package com.chenggong.modelsearch.net;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by chenggong on 18-3-30.
 * 图片的操作类
 */

public class ImageUtil {
    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).into(imageView);
    }

}
