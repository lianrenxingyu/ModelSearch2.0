package com.chenggong.modelsearch2_0.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by chenggong on 18-5-30.
 *
 * @author chenggong
 */

public class BottomDialog extends Dialog {
    public BottomDialog(@NonNull Context context) {
        super(context);
    }

    protected BottomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
