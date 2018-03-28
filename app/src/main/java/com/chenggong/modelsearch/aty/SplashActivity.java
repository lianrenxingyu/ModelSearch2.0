package com.chenggong.modelsearch.aty;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.chenggong.modelsearch.R;

public class SplashActivity extends Activity {

    private static final int ANIMATION_DURATION = 1300;

    private ImageView splashImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashImage = (ImageView) findViewById(R.id.splash_image);
        animateImage();
    }

    /**
     * 闪屏界面动画
     */
    private void animateImage() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(splashImage, View.SCALE_X,1f,1.13f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(splashImage, View.SCALE_Y,1f,1.13f);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIMATION_DURATION).play(animatorX).with(animatorY);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                MainActivity.start(SplashActivity.this);
                SplashActivity.this.finish();
            }

        });
    }
}
