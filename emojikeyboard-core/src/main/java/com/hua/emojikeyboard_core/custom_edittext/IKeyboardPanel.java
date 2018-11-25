package com.hua.emojikeyboard_core.custom_edittext;

import android.app.Activity;
import android.content.ComponentName;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author hua
 * @version 1.0
 * @date 2018/11/24
 */
public interface IKeyboardPanel {

    void show(Activity activity, @IdRes int themeId);

    void dismiss();
}
