/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dashihui.afford.ui.activity.server;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessServer;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.business.entity.EtyServerTime;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.base.BaseAtyFragment;
import com.dashihui.afford.ui.activity.fragment.FragmentServerTime;
import com.dashihui.afford.ui.widget.WdtPagerTabServerTime;
import com.dashihui.afford.util.list.UtilList;
import com.lidroid.xutils.util.LogUtils;

import java.util.List;


public class AtyFragmentServerTime extends BaseAtyFragment implements OnClickListener {

    private WdtPagerTabServerTime mTabs;
    private ViewPager mPager;
    private MyPagerAdapter mPageAdapter;

    private int currentColor = 0xFF666666;
    private TextView mTopTitle_txt;
    private ImageButton mIbtnBack;
    private Button mBtnSure;
    public static String mItemDateStr = "";

    public static final String DATEFLAG = "dateTime";


    public final static String TABCODE = "tabCode";
    public static boolean ORDSTATETAG = false;
    private BusinessServer mBLLServer;

    private List<EtyServerTime> serverTimeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_servertime_frag);
        mBLLServer = new BusinessServer(this);
        mTopTitle_txt = (TextView) findViewById(R.id.title);
        mTopTitle_txt.setText("选择服务时间");
        mIbtnBack = (ImageButton) findViewById(R.id.ibtnBack);
        mBtnSure = (Button) findViewById(R.id.btnsure);
        mBtnSure.setOnClickListener(this);
        mIbtnBack.setOnClickListener(this);

        mTabs = (WdtPagerTabServerTime) findViewById(R.id.apps_fragment_tabs);
        mPager = (ViewPager) findViewById(R.id.apps_fragment_viewpager);
        //请求服务时间
        mBLLServer.getOrderTimes();
    }

    private void changeColor(int newColor) {
        mTabs.setIndicatorColor(newColor);
    }

    public void onColorClicked(View v) {
        int color = Color.parseColor(v.getTag().toString());
        changeColor(color);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentColor", currentColor);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentColor = savedInstanceState.getInt("currentColor");
        changeColor(currentColor);
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        LogUtils.e("onSuccess==============>" + beanSendUI);
        if (beanSendUI != null) {
            serverTimeList = (List<EtyServerTime>) beanSendUI.getInfo();
            if (!UtilList.isEmpty(serverTimeList)) {
                LogUtils.e("onSuccess==============>" + serverTimeList.get(0).getDATE());
                LogUtils.e("onSuccess==============>" + serverTimeList.get(0).getTITLE());
                Intent intent = getIntent();
                mPageAdapter = new MyPagerAdapter(getSupportFragmentManager(), serverTimeList);

                // 设置缓存页面，当前页面的相邻N各页面都会被缓存
                mPager.setOffscreenPageLimit(mPageAdapter.getCount());
                mPager.setAdapter(mPageAdapter);
                final int pageMargin = (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
                mPager.setPageMargin(pageMargin);
                mPager.setCurrentItem(intent.getIntExtra(TABCODE, 0));
                mTabs.setViewPager(mPager);

                changeColor(currentColor);
            } else {
                LogUtils.e("onSuccess=====空null=========>" + serverTimeList);
            }
        } else {
            LogUtils.e("onSuccess===null===========>" + beanSendUI);
        }
    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        LogUtils.e("onFailure========beanSendUI===========>" + beanSendUI);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private List<EtyServerTime> mEtyTabsList;

        public MyPagerAdapter(FragmentManager supportFragmentManager, List<EtyServerTime> _tabsList) {
            super(supportFragmentManager);
            mEtyTabsList = _tabsList;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mEtyTabsList.get(position).getTITLE();
        }

        @Override
        public int getCount() {
            return mEtyTabsList.size();
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new FragmentServerTime(AtyFragmentServerTime.this, position, mEtyTabsList.get(position));
//            switch (position) {
//                case 0:
//                case 1:
//                case 2:
//                    fragment = FragmentServerTime.newInstance(AtyFragmentServerTime.this, position, mEtyTabsList.get(position));
//                    break;
//                default:
//                    fragment = FragmentServerTime.newInstance(AtyFragmentServerTime.this, position, mEtyTabsList.get(position));
//                    break;
//            }
//            LogUtils.e("MyPagerAdapter====fragment顶部栏==========>" + mEtyTabsList.get(position).getDATE());
            return fragment;
        }

        public List<EtyServerTime> getmEtyTabsList() {
            return mEtyTabsList;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtnBack:
                onBackPressed();
                break;
            case R.id.btnsure:
                LogUtils.e("确定==================>" + mItemDateStr);
                if (mItemDateStr != null && !"".equals(mItemDateStr)) {
                    Intent intent = new Intent(this, AtyServerSettlement.class);
                    intent.putExtra(DATEFLAG, mItemDateStr);
                    LogUtils.e("确定=========mItemTime=========>" + mItemDateStr);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    LogUtils.e("确定==================>" + mItemDateStr);
                }

                break;
            default:
                break;
        }
    }

}