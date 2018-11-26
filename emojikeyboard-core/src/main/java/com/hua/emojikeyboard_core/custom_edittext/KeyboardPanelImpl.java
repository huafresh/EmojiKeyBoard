package com.hua.emojikeyboard_core.custom_edittext;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.hua.emojikeyboard_core.R;

/**
 * @author hua
 * @version 1.0
 * @date 2018/11/24
 */
class KeyboardPanelImpl implements IKeyboardPanel {
    private static boolean registered = false;
    private KeyboardPopup keyboardPopup;

    KeyboardPanelImpl() {

    }

    @Override
    public void show(Activity activity, int themeId, final View visibleView) {
        ensureActivityCallback(activity.getApplication());

        if (keyboardPopup == null || !keyboardPopup.isSameWindow(activity)) {
            keyboardPopup = new KeyboardPopup(activity, themeId);
        } else {
            keyboardPopup.setThemeId(themeId);
        }

        keyboardPopup.show(visibleView);
    }

    @Override
    public void dismiss() {
        if (keyboardPopup != null) {
            keyboardPopup.dismiss();
        }
    }

    @Override
    public boolean isShowing() {
        return keyboardPopup != null && keyboardPopup.isShowing();
    }

    private void ensureActivityCallback(Application application) {
        if (!registered) {
            application.registerActivityLifecycleCallbacks(new ActivityCallback());
            registered = true;
        }
    }

    private class ActivityCallback implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if (keyboardPopup != null && keyboardPopup.isSameWindow(activity)) {
                if (keyboardPopup.isShowing()) {
                    keyboardPopup.dismiss();
                }
                keyboardPopup = null;
            }
        }
    }


}
