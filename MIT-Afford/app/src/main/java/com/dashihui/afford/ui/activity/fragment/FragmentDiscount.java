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

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessShop;
import com.dashihui.afford.business.entity.EtyList;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseFragment;
import com.dashihui.afford.ui.adapter.AdapterFrgLimit;
import com.dashihui.afford.ui.adapter.AdapterFrgOneBuy;
import com.dashihui.afford.ui.adapter.AdapterFrgRecomend;
import com.dashihui.afford.util.list.UtilList;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentDiscount extends BaseFragment {
    private static final String TAG = "WdtAppsFragment";
    private static final String ARG_POSITION = "position";
    /**
     * 列表相关
     */
//	private PullToRefreshListView mListView;// 列表
//	private AdapterApps mListViewAdapter;// 列表Adapter
    private List<Map<String, Object>> mMapList;
    private static Context mContext;
    private boolean mLoadMore = false;//是否为加载更多，如果是上拉操作，则为true

    @ViewInject(R.id.fragment_listview)
    private PullToRefreshListView mListView;
    //列表适配器
    private AdapterFrgRecomend mAdapterFrgRecomend;//推荐
    private AdapterFrgLimit mAdapterFrgLimit;//限量
    private AdapterFrgOneBuy mAdapterFrgOneBuy;//一元购

    private String mCategoryCode;//类别代码
    private int mType = 1;//优惠类型
    private int pageNum = 1;//页码
    private int position ;
    private BusinessShop mBllShop;

    public static FragmentDiscount newInstance(Context context, int position) {
        FragmentDiscount f = new FragmentDiscount();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        mContext = context;
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_listview, container, false);
        ViewUtils.inject(this, rootView); //注入view和事件
        mMapList = new ArrayList<>();
        mType = position + 2;
        //请求列表数据
        mBllShop = new BusinessShop(this);
//        mBllShop.getGoodsList("", mType + "", pageNum + "");
        mBllShop.getGoodsList("","",mType + "", "1", ++pageNum + "");
//		根据优惠列表分类，加载不同的adapter
        LogUtils.e("onCreateView=======mType=======>" + mType);
        if (mType == 2) {
            mAdapterFrgRecomend = new AdapterFrgRecomend(getActivity(), mMapList);
            mListView.setAdapter(mAdapterFrgRecomend);
        } else if (mType == 3) {
            mAdapterFrgLimit = new AdapterFrgLimit(getActivity(), mMapList);
            mListView.setAdapter(mAdapterFrgLimit);
        } else if (mType == 4) {
            LogUtils.e("赋值==========4444444444======viewHolder==========>"+mType);
            mAdapterFrgOneBuy = new AdapterFrgOneBuy(getActivity(), mMapList);
            mListView.setAdapter(mAdapterFrgOneBuy);
        } else {
            LogUtils.e("onCreateView=======mType=======>" + mType);
        }
        return rootView;
    }
    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        LogUtils.e("onSuccess=======FragmentDiscount====beanSendUI.getInfo()====>" + beanSendUI);
        EtyList listObjects = (EtyList) beanSendUI.getInfo();

        if (listObjects != null && !UtilList.isEmpty(listObjects.getLIST())) {
            mMapList.addAll(listObjects.getLIST());
            //根据订单列表分类，加载不同的adapter
            if (mType == 2) {
                mAdapterFrgRecomend.setList(mMapList);
                mAdapterFrgRecomend.notifyDataSetChanged();
            } else if (mType == 3) {
                mAdapterFrgLimit.setList(mMapList);
                mAdapterFrgLimit.notifyDataSetChanged();
            } else if (mType == 4) {
                LogUtils.e("赋值=====4444444444=====onSuccess======viewHolder==========>"+mType);
                mAdapterFrgOneBuy.setList(mMapList);
                mAdapterFrgOneBuy.notifyDataSetChanged();
            } else {
                LogUtils.e("onSuccess===mType===========>" + mType);
            }
        } else {
            LogUtils.e("onSuccess===列表===========>" + beanSendUI);
        }
    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        LogUtils.e("onFailure=======FragmentDiscount====beanSendUI.getInfo()====>" + beanSendUI.getInfo());
    }
}