package com.hua.emojikeyboard_core.custom_edittext;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.hua.emojikeyboard_core.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hua
 * @version 1.0
 * @date 2018/11/25
 */
public class SimpleKeyboardTheme implements IKeyboardTheme {
    @Override
    public int themeId() {
        return R.id.keyboard_theme_simple;
    }

    @Override
    public View onCreateKeyboardView(Context context, LayoutInflater inflater, ViewGroup container) {
        View inputView = inflater.inflate(R.layout.keyboard_simple_digital, container, false);
        GridView grid = inputView.findViewById(R.id.digital_grid);
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            data.add(i + "");
        }
        grid.setAdapter(new ArrayAdapter<String>(context,
                R.layout.item_keyboard_simple_digital, R.id.text, data));
        return inputView;
    }

    @Override
    public void onBindEditText(@NonNull FlexKeyboardEditText editText) {

    }
}
