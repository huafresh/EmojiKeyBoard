package com.hua.emojikeyboard_core.custom_edittext;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.hua.emojikeyboard_core.R;

import java.lang.reflect.Method;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/23 13:40
 */

public class FlexKeyboardEditText extends AppCompatEditText implements View.OnFocusChangeListener {
    public static final int KEYBOARD_TYPE_SYSTEM = 0;
    public static final int KEYBOARD_TYPE_CUSTOM = 1;
    private int keyboardType;
    private int keyboardThemeId;
    private int visibleView;
    private boolean systemSoftEnable = true;

    public FlexKeyboardEditText(Context context) {
        this(context, null);
    }

    public FlexKeyboardEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlexKeyboardEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlexKeyboardEditText);
        this.keyboardType = ta.getInt(R.styleable.FlexKeyboardEditText_keyboard_type, KEYBOARD_TYPE_CUSTOM);
        this.keyboardThemeId = ta.getInt(R.styleable.FlexKeyboardEditText_keyboard_theme_id, R.id.keyboard_theme_simple);
        this.visibleView = ta.getInt(R.styleable.FlexKeyboardEditText_keyboard_visible_view, -1);
        ta.recycle();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (keyboardType == KEYBOARD_TYPE_CUSTOM) {
                if (!KeyboardManager.get().isCustomShowing()) {
                    Context context = getContext();
                    if (context instanceof Activity) {
                        View visibleView = ((Activity) context).getWindow().findViewById(this.visibleView);
                        if (visibleView == null) {
                            visibleView = this;
                        }
                        KeyboardManager.get().showCustomSoftInput((Activity) context, keyboardThemeId, visibleView);
                    }
                }
                if (systemSoftEnable) {
                    setIsShowSystemSoftInputOnFocus(false);
                }
            } else {
                if (!systemSoftEnable) {
                    setIsShowSystemSoftInputOnFocus(true);
                }
            }
        }
    }

    private void setIsShowSystemSoftInputOnFocus(boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setShowSoftInputOnFocus(show);
            systemSoftEnable = true;
        } else {
            try {
                Method setShowSoftInputOnFocus = EditText.class.getMethod(
                        "setShowSoftInputOnFocus", Boolean.TYPE);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(this, show);
                systemSoftEnable = true;
            } catch (Exception var3) {
                var3.printStackTrace();
                systemSoftEnable = false;
            }
        }
    }


}
