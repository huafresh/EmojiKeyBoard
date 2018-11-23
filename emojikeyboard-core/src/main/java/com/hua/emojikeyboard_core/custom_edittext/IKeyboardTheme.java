package com.hua.emojikeyboard_core.custom_edittext;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/23 13:50
 */

public interface IKeyboardTheme {

    @IdRes
    int themeId();

    View inputView(LayoutInflater inflater, ViewGroup container);

    void onBindEditText(@NonNull FlexEditText editText);

}
