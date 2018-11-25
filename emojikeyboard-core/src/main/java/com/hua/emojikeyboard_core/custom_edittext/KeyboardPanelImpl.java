package com.hua.emojikeyboard_core.custom_edittext;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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
import android.view.Window;
import android.view.WindowManager;
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
    private SparseArray<IKeyboardTheme> keyboardThemes = new SparseArray<>();
    private SparseArray<View> keyboardViews = new SparseArray<>();
    private PopupWindow keyboardPopup;
    private ComponentName attachWindow;
    private Rect tempRect = new Rect();
    private int[] tempLocation = new int[2];

    KeyboardPanelImpl() {
        keyboardThemes.put(R.id.keyboard_theme_simple, new SimpleKeyboardTheme());
    }

    @Override
    public void show(final Activity activity, int themeId) {
        registerActivityCallback(activity.getApplication());
        dismiss();
        keyboardPopup = buildPopupWindow(activity, themeId);
        keyboardPopup.showAtLocation(new View(activity), Gravity.BOTTOM, 0, 0);
        attachWindow = activity.getComponentName();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                int scrollOffset = getScrollOffset(activity.getWindow().getCurrentFocus(),
                        keyboardPopup.getContentView());
                activity.getWindow().getDecorView().scrollBy(0, scrollOffset);
            }
        },1000);
    }

    private int getScrollOffset(View focusView, View popupContent) {
        focusView.getLocationOnScreen(tempLocation);
        int focusBottom = tempLocation[1];
        popupContent.getLocationOnScreen(tempLocation);
        int popupTop = tempLocation[1];
        int offset = focusBottom - popupTop;
        return offset > 0 ? offset : 0;
    }

    private void adjustParentWindowToFocusViewVisible(Activity activity) {
        View focusView = activity.getWindow().getCurrentFocus();
        if (focusView != null && focusView.getGlobalVisibleRect(tempRect)) {
            focusView.requestRectangleOnScreen(tempRect);
        }
    }

    private PopupWindow buildPopupWindow(Activity activity, int themeId) {
        PopupWindow popupWindow = new PopupWindow(activity);
        View contentView = getKeyboardViewByThemeId(activity, themeId);
        popupWindow.setContentView(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
        return popupWindow;
    }

    @Override
    public void dismiss() {
        if (keyboardPopup != null) {
            keyboardPopup.dismiss();
            keyboardPopup = null;
            attachWindow = null;
        }
    }

    private @Nullable
    View getKeyboardViewByThemeId(Activity activity, @IdRes int themeId) {
        View keyboardView = keyboardViews.get(themeId);
        if (keyboardView == null) {
            FrameLayout container = new FrameLayout(activity.getApplicationContext());
            IKeyboardTheme iKeyboardTheme = keyboardThemes.get(themeId);
            if (iKeyboardTheme != null) {
                View view = iKeyboardTheme.onCreateKeyboardView(activity.getApplicationContext(),
                        LayoutInflater.from(activity.getApplicationContext()), container);
                container.addView(view);
                keyboardView = container;
            }
        }
        return keyboardView;
    }

    private void registerActivityCallback(Application application) {
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
            KeyboardPanelImpl.this.onActivityDestroyed(activity);
        }
    }

    private void onActivityDestroyed(Activity activity) {
        if (attachWindow != null &&
                attachWindow.equals(activity.getComponentName())) {
            dismiss();
        }
    }

}
