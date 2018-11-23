package com.hua.emojikeyboard_core.core;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by hua on 2017/10/7.
 * 指示器接口
 */

public interface Indicator {

    /**
     * 绑定ViewPager对象
     *
     * @param viewPager ViewPager对象
     */
    void bindViewPager(ViewPager viewPager);

    /**
     * 获取指示器实体视图
     *
     * @return 实体视图
     */
    View getContentView(Context context, int count);

    /**
     * 设置指示器的位置
     *
     * @param position 指示器的位置
     */
     void setPosition(int position);


}
