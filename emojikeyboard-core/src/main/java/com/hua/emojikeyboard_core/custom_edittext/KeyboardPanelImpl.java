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
    private SparseArray<IKeyboardTheme> keyboardThemes = new SparseArray<>();
    private SparseArray<View> keyboardViews = new SparseArray<>();
    private PopupWindow keyboardPopup;
    private ComponentName attachWindow;
    private ScrollAdjustHelper scrollHelper;

    KeyboardPanelImpl() {
        keyboardThemes.put(R.id.keyboard_theme_simple, new SimpleKeyboardTheme());
    }

    @Override
    public void show(Activity activity, int themeId, final View visibleView) {
        ensureActivityCallback(activity.getApplication());
        dismiss();
        keyboardPopup = buildPopupWindow(activity, themeId);
        attachWindow = activity.getComponentName();
        keyboardPopup.showAtLocation(new View(activity), Gravity.BOTTOM, 0, 0);
        final View popupView = keyboardPopup.getContentView();
        popupView.post(new Runnable() {
            @Override
            public void run() {
                scrollEnsureViewVisible(visibleView, popupView);
            }
        });
    }

    private void scrollEnsureViewVisible(View visibleView, View popupView) {
        if (scrollHelper == null) {
            scrollHelper = new ScrollAdjustHelper(visibleView, popupView);
        } else {
            scrollHelper.updateView(visibleView, popupView);
        }
        scrollHelper.adjust();
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
            scrollHelper.reset();
            scrollHelper.recycle();
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
