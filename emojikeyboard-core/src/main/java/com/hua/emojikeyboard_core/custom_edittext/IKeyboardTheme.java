package com.hua.emojikeyboard_core.custom_edittext;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/23 13:50
 */

public interface IKeyboardTheme {

    @IdRes
    int themeId();

    View onCreateKeyboardView(Context context, LayoutInflater inflater, ViewGroup container);

    void onBindInputTarget(@NonNull View target);

}
