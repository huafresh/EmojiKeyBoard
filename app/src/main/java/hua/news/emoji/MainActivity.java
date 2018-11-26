package hua.news.emoji;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.hua.emojikeyboard_core.EmojiKeyBoard;
import com.hua.emojikeyboard_core.core.SimpleViewPagerAdapter;
import com.hua.emojikeyboard_core.custom_edittext.FlexKeyboardEditText;
import com.hua.emojikeyboard_core.custom_edittext.KeyboardManager;

public class MainActivity extends AppCompatActivity {

    private MyEditText edittext;
    private LinearLayout container;
    private ViewPager viewPager;
    private LinearLayout container1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getLayoutInflater().setFactory2(new LayoutInflater.Factory2() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                return null;
            }

            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                return null;
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emoji();
        //testviewpager();
    }

    private void testviewpager() {
        container1 = (LinearLayout) findViewById(R.id.container);
        findViewById(R.id.out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager();
            }
        });
    }

    private void viewPager() {
        //viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager = new ViewPager(this);
        List<View> views = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TextView textView = new TextView(this);
            textView.setText("页面" + i);
            views.add(textView);
        }
        SimpleViewPagerAdapter adapter = new SimpleViewPagerAdapter(views);
        viewPager.setAdapter(adapter);
        container1.addView(viewPager);
    }

    private void emoji() {
        edittext = findViewById(R.id.editText);
        container = (LinearLayout) findViewById(R.id.keyboard);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EmojiKeyBoard.getInstance().show(edittext, container);
                KeyboardManager.get().showCustomSoftInput(MainActivity.this,
                        R.id.keyboard_theme_simple);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EmojiKeyBoard.getInstance().dismiss();
                KeyboardManager.get().dismissCustomSoftInput();
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edittext.append("abc");
            }
        });
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edittext.delete();
            }
        });

        copyAssetsEmoji();

        EmojiKeyBoard.getInstance().initialization(this);

    }

    private void copyAssetsEmoji() {
        try {
            String[] emojis = getAssets().list("emoji/normal");
            for (String emojiName : emojis) {
                InputStream in = getAssets().open("emoji/normal/" + emojiName);
                String destPath = getExternalFilesDir(null) + "/emoji/normal/" + emojiName;
                File destFile = FileUtil.getFile(destPath);
                FileOutputStream fos = new FileOutputStream(destFile);

                FileUtil.readFromSteamToStream(in, fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
