package hua.news.emoji.emoji.core;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Author: hua
 * Created: 2017/10/11
 * Description:
 */

public class SimpleViewPagerAdapter extends PagerAdapter{

    private List<View> mDataList;

    public SimpleViewPagerAdapter(List<View> mDataList) {
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList != null ? mDataList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mDataList.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = mDataList.get(position);
        container.removeView(view);
    }

}
