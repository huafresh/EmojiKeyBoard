package hua.news.emoji;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import hua.news.emoji.emoji.EmojiKeyBoard;
import hua.news.emoji.emoji.core.SimpleViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private EditText edittext;
    private LinearLayout container;
    private ViewPager viewPager;
    private LinearLayout container1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            textView.setText("页面"+i);
            views.add(textView);
        }
        SimpleViewPagerAdapter adapter = new SimpleViewPagerAdapter(views);
        viewPager.setAdapter(adapter);
        container1.addView(viewPager);
    }

    private void emoji() {
        edittext = (EditText) findViewById(R.id.editText);
        container = (LinearLayout) findViewById(R.id.keyboard);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmojiKeyBoard.getInstance().show(edittext, container);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmojiKeyBoard.getInstance().dismiss();
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
