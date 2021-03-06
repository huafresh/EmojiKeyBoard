package com.hua.emojikeyboard_core;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.io.File;
import java.io.FileFilter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hua.emojikeyboard_core.core.EditTextWrapper;
import com.hua.emojikeyboard_core.core.EmojiEntity;
import com.hua.emojikeyboard_core.core.EmojiKeyBoardConfig;
import com.hua.emojikeyboard_core.core.IEmojiBottomTab;
import com.hua.emojikeyboard_core.core.IEmojiPage;
import com.hua.emojikeyboard_core.core.SimpleViewPagerAdapter;
import com.hua.emojikeyboard_core.defaults.BaseEmojiKeyBoardConfig;

/**
 * Created by hua on 2017/10/8.
 * 表情键盘。
 * 使用前需要提前调用初始化方法{@link #initialization}，提升键盘弹出响应速度
 * 调用{@link #show}弹出表情键盘
 * 调用{@link #dismiss}隐藏表情键盘
 */

public class EmojiKeyBoard {
    /**
     * 表情图片根目录名称
     */
    public static final String EMOJI_ROOT_DIR_NAME = "emoji";
    /**
     * 无表情图片界面提示
     */
    private static final String NO_EMOJI = "暂无表情图片";
    /**
     * 以文件夹名称为key存储每个页面每个表情图片的名称
     */
    private HashMap<String, List<String>> sEmojiNameMap = new HashMap<>();
    /**
     * 顶部分割线颜色
     */
    private static final int TOP_LINE_COLOR = 0xffdddddd;
    /**
     * 表情键盘布局视图，这里保存起来避免多次创建
     */
    private WeakReference<LinearLayout> mKeyBoardLayout;
    /**
     * 表情键盘输入目标
     */
    private WeakReference<EditText> mTarget;
    /**
     * 表情键盘的配置
     */
    private EmojiKeyBoardConfig mKeyBoardConfig;
    /**
     * 可识别的表情图片后缀
     */
    private static String[] emojiSuffixs = new String[]{".png", ".jpg", ".jpeg"};
    /**
     * 读取到内存的表情图片的数量
     */
    private static final int EMOJI_COUNT = 50;
    /**
     * 表情键盘是否弹出
     */
    private boolean isShowing = false;

    public static EmojiKeyBoard getInstance() {
        return HOLDER.sInstance;
    }

    private static final class HOLDER {
        private static final EmojiKeyBoard sInstance = new EmojiKeyBoard();
    }

    public void initialization(Context context) {
        initEmojiNameMap(context);
    }


    /**
     * 隐藏表情键盘
     */
    public void dismiss() {
        View keyBoardView = mKeyBoardLayout.get();
        if (keyBoardView != null) {
            ViewParent parent = keyBoardView.getParent();
            if (parent != null) {
                ViewGroup viewGroup = (ViewGroup) parent;
                viewGroup.removeView(keyBoardView);
                isShowing = false;
            }
        }
    }

    public void show(EditText target, ViewGroup container) {
        show(target, null, container);
    }

    /**
     * 显示表情键盘
     *
     * @param target    输入目标
     * @param config    表情键盘配置。为空使用默认的{@link BaseEmojiKeyBoardConfig}
     * @param container 表情键盘布局的容器
     */
    public void show(EditText target, EmojiKeyBoardConfig config, ViewGroup container) {
        if (target == null || container == null || isShowing) {
            return;
        }

        isShowing = true;

        mTarget = new WeakReference<>(target);

        if (config != null) {
            mKeyBoardConfig = config;
        } else {
            mKeyBoardConfig = new BaseEmojiKeyBoardConfig();
        }

        if (mKeyBoardLayout == null || mKeyBoardLayout.get() == null) {
            LinearLayout keyBoardLayout = new LinearLayout(target.getContext());
            mKeyBoardLayout = new WeakReference<>(keyBoardLayout);
            if (sEmojiNameMap.size() == 0) {
                createEmptyEmojiLayout(keyBoardLayout);
            } else {
                createKeyBoardLayout(keyBoardLayout);
            }
        }

        container.removeAllViews();
        container.addView(mKeyBoardLayout.get());
    }

    private void createEmptyEmojiLayout(LinearLayout container) {
        Context context = container.getContext();
        TextView textView = new TextView(context);
        textView.setText(NO_EMOJI);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundColor(TOP_LINE_COLOR);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                dip2px(context, 200));
        container.addView(textView, params);
    }

    private void createKeyBoardLayout(LinearLayout container) {
        Context mContext = container.getContext();

        //顶部分割线
        View topLine = new View(mContext);
        topLine.setBackgroundColor(TOP_LINE_COLOR);

        //表情页面ViewPager
        ViewPager viewPager = createViewPager(mContext);

        //底部可滑动视图
        View bottomView = null;
        if (mKeyBoardConfig.enableBottomTab()) {
            IEmojiBottomTab mEmojiBottomTab = mKeyBoardConfig.createBottomTab();
            mEmojiBottomTab.bindViewPager(viewPager);
            bottomView = mEmojiBottomTab.createContentView();
        }

        //整体布局容器
        container.setOrientation(LinearLayout.VERTICAL);
        container.addView(topLine, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                dip2px(mContext, 0.5f)));
        container.addView(viewPager);
        if (bottomView != null) {
            container.addView(bottomView);
        }
    }

    private static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private ViewPager createViewPager(Context mContext) {
        DisHorizontalViewPager viewPager = new DisHorizontalViewPager(mContext);

        List<View> viewList = createPageViewList(mContext);
        SimpleViewPagerAdapter adapter = new SimpleViewPagerAdapter(viewList);
        viewPager.setAdapter(adapter);

        return viewPager;
    }


    private List<View> createPageViewList(Context context) {

        List<View> views = new ArrayList<>();

        mKeyBoardConfig.adjustPageCount(sEmojiNameMap);

        for (String emojiDir : sEmojiNameMap.keySet()) {
            EmojiEntity emojiEntity = new EmojiEntity();
            emojiEntity.setEmojiDirName(emojiDir);
            emojiEntity.setEmojiNames(sEmojiNameMap.get(emojiDir));
            IEmojiPage emojiPage = mKeyBoardConfig.createEmojiPage(context, emojiEntity);
            emojiPage.bindTarget(new EditTextWrapper(mTarget.get()));
            View emojiView = emojiPage.createContentView(context, mKeyBoardLayout.get());
            if (emojiView != null) {
                views.add(emojiView);
            }
        }

        return views;
    }

    @SuppressWarnings("unchecked")
    private void initEmojiNameMap(Context context) {

        File emojiRootDirFile = new File(context.getExternalFilesDir(null) + "/" + EMOJI_ROOT_DIR_NAME);
        if (!emojiRootDirFile.exists()) {
            return;
        }

        File[] subDirs = emojiRootDirFile.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        for (File subDir : subDirs) {
            File[] emojiFiles = subDir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isFile() && isContainSuffix(pathname.getName());
                }
            });
            List<String> emojiName = new ArrayList<>();
            for (File emojiFile : emojiFiles) {
                emojiName.add(emojiFile.getName());
            }
            sEmojiNameMap.put(subDir.getName(), emojiName);
        }

        //预读取指定个数的表情图片到内存
        // TODO: 2017/10/12 响应速度慢了再添加预读逻辑
//        int i = 0;
//        for (String emojiDir : sEmojiNameMap.keySet()) {
//            List<String> emojiNames = sEmojiNameMap.get(emojiDir);
//            for (String emojiName : emojiNames) {
//                if (i++ < EMOJI_COUNT) {
//                    String cacheKey = emojiDir + "/" + emojiName;
//                    Bitmap bitmap = BitmapFactory.decodeFile(emojiRootDirFile.getAbsolutePath() + "/" + cacheKey);
//                    // TODO: 2017/10/12 此时未配置config，后续想起它办法
//                    //mKeyBoardConfig.getMemoryCache().putEmoji(cacheKey, bitmap);
//                }
//            }
//        }

    }

    private boolean isContainSuffix(String name) {
        for (String emojiSuffix : emojiSuffixs) {
            if (name.endsWith(emojiSuffix)) {
                return true;
            }
        }
        return false;
    }

    private static class DisHorizontalViewPager extends ViewPager {
        public DisHorizontalViewPager(Context context) {
            this(context, null);
        }

        public DisHorizontalViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return false;
        }
    }


    @SuppressWarnings("unchecked")
    public <T> T getCacheStrategy() {
        return (T) mKeyBoardConfig.getMemoryCache();
    }

    public HashMap<String, List<String>> getEmojiMap() {
        return sEmojiNameMap;
    }

    /**
     * 生成表情图片全路径
     */
    public static String buildEmojiAbsolutePath(Context context, String dirName, String emojiName) {
        return context.getExternalFilesDir(null) + "/" + EMOJI_ROOT_DIR_NAME + "/" + dirName + "/" + emojiName;
    }

}
