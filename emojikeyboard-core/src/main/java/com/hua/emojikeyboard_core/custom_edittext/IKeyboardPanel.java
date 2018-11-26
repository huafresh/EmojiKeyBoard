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

    /**
     * 弹出自绘键盘
     *
     * @param activity    Activity
     * @param themeId     键盘UI的id，需要自定义实现{@link IKeyboardTheme}
     * @param visibleView 避免被自绘键盘遮住的视图
     */
    void show(Activity activity, @IdRes int themeId, View visibleView);

    void dismiss();

    boolean isShowing();
}
