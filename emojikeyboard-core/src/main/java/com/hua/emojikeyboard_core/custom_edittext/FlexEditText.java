package com.hua.emojikeyboard_core.custom_edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.hua.emojikeyboard_core.R;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/23 13:40
 */

public class FlexEditText extends AppCompatEditText implements View.OnFocusChangeListener{
    private int keyboardType;
    private int keyboardTheme;
    private SparseArray<IKeyboardTheme> themes = new SparseArray<>();

    public FlexEditText(Context context) {
        this(context, null);
    }

    public FlexEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlexEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlexEditText);
//        this.keyboardType = ta.getInt(R.styleable.FlexEditText_keyboard_type, keyboard_type_custom);
//        this.keyboardTheme = ta.getInt(R.styleable.FlexEditText_keyboard_theme, -1);
        ta.recycle();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

}
