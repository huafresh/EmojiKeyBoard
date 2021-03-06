package com.hua.emojikeyboard_core.custom_edittext;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.hua.emojikeyboard_core.R;

import java.lang.reflect.Method;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/23 13:40
 */

public class KeyboardEditText extends AppCompatEditText
        implements View.OnFocusChangeListener, View.OnClickListener {
    public static final int KEYBOARD_TYPE_SYSTEM = 0;
    public static final int KEYBOARD_TYPE_CUSTOM = 1;
    private int keyboardType;
    private int keyboardThemeId;
    private int visibleViewId;
    private boolean systemSoftEnable = true;

    public KeyboardEditText(Context context) {
        this(context, null);
    }

    public KeyboardEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
    }

    public KeyboardEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlexKeyboardEditText);
        this.keyboardType = ta.getInt(R.styleable.FlexKeyboardEditText_keyboard_type, KEYBOARD_TYPE_CUSTOM);
        this.keyboardThemeId = ta.getInt(R.styleable.FlexKeyboardEditText_keyboard_theme_id, R.id.keyboard_theme_simple);
        this.visibleViewId = ta.getInt(R.styleable.FlexKeyboardEditText_keyboard_visible_view, -1);
        ta.recycle();

        setFocusableInTouchMode(true);
        setOnFocusChangeListener(this);
        setOnClickListener(this);
        setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK &&
                        KeyboardManager.get().isCustomShowing()) {
                    KeyboardManager.get().dismissCustomSoftInput();
                    return true;
                }
                return false;
            }
        });

        if (keyboardType == KEYBOARD_TYPE_CUSTOM && isActivityContext()) {
            setIsShowSystemSoftInputOnFocus(false);
        } else {
            setIsShowSystemSoftInputOnFocus(true);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //焦点变化快于系统键盘的弹出
        if (!systemSoftEnable &&
                hasFocus() &&
                !KeyboardManager.get().isCustomShowing()) {
            Activity activity = (Activity) getContext();
            View visibleView = activity.getWindow().getDecorView().findViewById(visibleViewId);
            if (visibleView == null) {
                visibleView = this;
            }
            KeyboardManager.get().showCustomSoftInput(activity, keyboardThemeId, visibleView);
        }
    }

    private boolean isActivityContext() {
        return getContext() instanceof Activity;
    }

    private void setIsShowSystemSoftInputOnFocus(boolean show) {
        if (systemSoftEnable != show) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setShowSoftInputOnFocus(show);
            } else {
                try {
                    Method setShowSoftInputOnFocus = EditText.class.getMethod(
                            "setShowSoftInputOnFocus", Boolean.TYPE);
                    setShowSoftInputOnFocus.setAccessible(true);
                    setShowSoftInputOnFocus.invoke(this, show);
                } catch (Exception var3) {
                    var3.printStackTrace();
                }
            }
            systemSoftEnable = show;
        }
    }


    @Override
    public void onClick(View v) {
        //点击事件慢于系统键盘的弹出
        //如果点击使View获取了焦点，则此回调不会走。
        if (!systemSoftEnable &&
                hasFocus() &&
                !KeyboardManager.get().isCustomShowing()) {
            Activity activity = (Activity) getContext();
            View visibleView = activity.getWindow().getDecorView().findViewById(visibleViewId);
            if (visibleView == null) {
                visibleView = this;
            }
            KeyboardManager.get().showCustomSoftInput(activity, keyboardThemeId, visibleView);
        }
    }
}
