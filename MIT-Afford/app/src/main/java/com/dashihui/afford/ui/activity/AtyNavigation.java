package com.dashihui.afford.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dashihui.afford.R;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.ui.adapter.AdapterNavigation;
import com.dashihui.afford.util.UtilCommon;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

public class AtyNavigation extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    @ViewInject(R.id.vp_view)
    private ViewPager mViewPager;
    @ViewInject(R.id.lyt_bottom)
    private LinearLayout mLinearLayout;
    @ViewInject(R.id.btn_welcome)
    private Button mBtnBottom;

    private List<View> mListView;
    private int[] mImages = {R.drawable.img_welcome_one, R.drawable.img_welcome_two, R.drawable.img_welcome_three};
    private AdapterNavigation mAdapterNavi;
    private ImageView[] mPoint;
    private int mCurrIndex;//当前页下标

    //滑动
    private int mLastValut = -1;
    private boolean mIsScrolling = false;
    //判断只走一次
    private int mCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_navigation);
        ViewUtils.inject(this);

        initView();
        initData();
        initPoint();
    }

    //初始化控件
    private void initView() {
        mBtnBottom.setVisibility(View.GONE);
        mListView = new ArrayList<View>();
        mViewPager.addOnPageChangeListener(this);
    }

    private void initPoint() {
        mPoint = new ImageView[mImages.length];
        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
            mPoint[i] = (ImageView) mLinearLayout.getChildAt(i);
            mPoint[i].setImageResource(R.drawable.circle_gray);
            mPoint[i].setOnClickListener(this);
            mPoint[i].setTag(i);
        }
        mCurrIndex = 0;
        mPoint[mCurrIndex].setImageResource(R.drawable.circle_red);
    }

    //初始化数据
    private void initData() {
        for (int i : mImages) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(i);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mListView.add(imageView);
        }
        mAdapterNavi = new AdapterNavigation(mListView);
        mViewPager.setAdapter(mAdapterNavi);
    }

    @Override
    public void onClick(View v) {
        int number = (Integer) v.getTag();
        mViewPager.setCurrentItem(number);
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
        if (mIsScrolling) {
            if (mLastValut == i2 && i == mListView.size() - 1 && mCount == 0) {
                mCount = 1;
            }
        }
        mLastValut = i2;
    }

    @Override
    public void onPageSelected(int i) {
        sdsd(i);
        mCount = 0;

        if (i == 2) {
            mBtnBottom.setVisibility(View.VISIBLE);
        } else {
            mBtnBottom.setVisibility(View.GONE);
        }
    }

    //底部小点
    private void sdsd(int ss) {
        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
            mPoint[i] = (ImageView) mLinearLayout.getChildAt(i);
            mPoint[i].setImageResource(R.drawable.circle_gray);
        }
        mPoint[ss].setImageResource(R.drawable.circle_red);
    }

    @Override
    public void onPageScrollStateChanged(int i) {
        if (i == 1) {
            mIsScrolling = true;
        } else {
            mIsScrolling = false;
        }
    }

    @OnClick(R.id.btn_welcome)
    public void onBtnBottomClick(View v){
        LogUtils.e("==============>"+UtilCommon.gPSIsOPen(this));
        mBaseUtilAty.startActivity(AtyHome.class);
        AtyNavigation.this.finish();
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {

    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {

    }
}
