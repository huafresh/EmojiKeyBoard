package com.hua.emojikeyboard_core.custom_edittext;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/23 14:03
 */

public class KeyboardManager {

    public static final int keyboard_type_custom = 0;
    public static final int keyboard_type_system = 1;
    private IKeyboardPanel keyboardPanel;

    private KeyboardManager() {
        keyboardPanel = new KeyboardPanelImpl();
    }

    public static KeyboardManager get() {
        return HOLDER.S_INSTANCE;
    }

    private static final class HOLDER {
        private static final KeyboardManager S_INSTANCE = new KeyboardManager();
    }

    public void showSystemSoftInput(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            View focusView = activity.getWindow().getCurrentFocus();
            if (focusView != null) {
                imm.showSoftInput(focusView, 0);
            }
        }
    }

    public void dismissSystemSoftInput(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            View focusView = activity.getCurrentFocus();
            if (focusView != null) {
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }
    }

    public void showCustomSoftInput(Activity activity, @IdRes int themeId) {
        this.showCustomSoftInput(activity, themeId, activity.getWindow().getCurrentFocus());
    }

    public void showCustomSoftInput(Activity activity, @IdRes int themeId, View visibleView) {
        keyboardPanel.show(activity, themeId, visibleView);
    }

    public void dismissCustomSoftInput() {
        keyboardPanel.dismiss();
    }

}
