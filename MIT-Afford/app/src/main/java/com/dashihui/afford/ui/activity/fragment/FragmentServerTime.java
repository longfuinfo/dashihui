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

package com.dashihui.afford.ui.activity.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.dashihui.afford.R;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.business.entity.EtyServerTime;
import com.dashihui.afford.common.base.BaseFragment;
import com.dashihui.afford.ui.activity.server.AtyFragmentServerTime;
import com.dashihui.afford.ui.adapter.AdapterFrgServerTime;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.Map;

public class FragmentServerTime extends BaseFragment {
    private static final String TAG = "WdtAppsFragment";
    private static final String ARG_POSITION = "position";


    /**
     * 列表相关
     */
//	private PullToRefreshListView mListView;// 列表
//	private AdapterApps mListViewAdapter;// 列表Adapter
//    private List<Map<String, Object>> mMapList;

    private static Context mContext;
    private int position;


    @ViewInject(R.id.gridview)
    private GridView mGridview;
    //列表适配器
    private AdapterFrgServerTime mAdapter;//

    private EtyServerTime mServerTime;
    private String mItemDate;
    private String mItemTime;
    private int mItemPos;
    private Map<String, Object> mMapTime;

    public FragmentServerTime() {

    }
    @SuppressLint("ValidFragment")
    public FragmentServerTime(Context context, int position, EtyServerTime serverTime) {
//        FragmentServerTime f = new FragmentServerTime();
        Bundle b = new Bundle();
        mServerTime = serverTime;
//        LogUtils.e("=======FragmentServerTime========>" + position);
//        LogUtils.e("=======mServerTime========>" + mServerTime);
        b.putInt(ARG_POSITION, position);
        mContext = context;
        this.setArguments(b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_gridview, container, false);
        ViewUtils.inject(this, rootView); //注入view和事件
        //根据时间列表分类加载不同的adapter
        mAdapter = new AdapterFrgServerTime(getActivity(), mServerTime.getHOURS(), mItemPos);
        mGridview.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mGridview.setOnItemClickListener(onTimeItemClick);

        return rootView;
    }

    private AdapterView.OnItemClickListener onTimeItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mItemPos = position;
            mAdapter.mChooseItem(position);
            mAdapter.notifyDataSetChanged();
            AtyFragmentServerTime.mItemDateStr = mServerTime.getHOURS().get(position).get("DATETIME").toString();
            LogUtils.e("onItemClick======item=======>" + AtyFragmentServerTime.mItemDateStr);
        }
    };

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {

    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        LogUtils.e("onFailure=======FragmentOrderList====beanSendUI.getInfo()====>" + beanSendUI.getInfo());
        // 隐藏加载更多圈 底部
    }

}