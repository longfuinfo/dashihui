package com.dashihui.afford.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class AdapterNavigation extends PagerAdapter {

    private Context mContext;
    private List<View> mListView;

    public AdapterNavigation(List<View> list) {
        this.mListView = list;
    }

    @Override
    public int getCount() {
        return mListView.size();
    }

    /**
     * 初始化posotion位置
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mListView.get(position), 0);
        return mListView.get(position);
    }

    /**
     * 判断是不是对象生成
     *
     * @param view
     * @param o
     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mListView.get(position));
    }
}