package com.hua.emojikeyboard_core.custom_edittext;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.hua.emojikeyboard_core.R;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/23 14:03
 */

public class KeyboardManager {

    private IKeyboardPanel keyboardPanel;
    static SparseArray<IKeyboardTheme> keyboardThemes = new SparseArray<>();

    static {
        keyboardThemes.put(R.id.keyboard_theme_simple, new SimpleKeyboardTheme());
    }

    private KeyboardManager() {
        keyboardPanel = new KeyboardPanelImpl();
    }

    public static KeyboardManager get() {
        return HOLDER.S_INSTANCE;
    }

    private static final class HOLDER {
        private static final KeyboardManager S_INSTANCE = new KeyboardManager();
    }

    public static void showSystemSoftInput(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            View focusView = activity.getWindow().getCurrentFocus();
            if (focusView != null) {
                imm.showSoftInput(focusView, 0);
            }
        }
    }

    public static void dismissSystemSoftInput(Activity activity) {
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

    public static boolean isSysKeyboardShowing(Activity activity) {
        int screenHeight = activity.getWindow().getDecorView().getHeight();
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom != 0;
    }

    public boolean isCustomShowing() {
        return keyboardPanel.isShowing();
    }

    public static void registerKeyboardTheme(IKeyboardTheme theme) {
        keyboardThemes.put(theme.themeId(), theme);
    }
}
