package com.hua.emojikeyboard_core.custom_edittext;

import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.view.inputmethod.InputMethodManager;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/23 14:03
 */

public class FlexKeyboardManager {

    public static final int keyboard_type_custom = 0;
    public static final int keyboard_type_system = 1;

    public static void showSoftInput(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

    }

}
