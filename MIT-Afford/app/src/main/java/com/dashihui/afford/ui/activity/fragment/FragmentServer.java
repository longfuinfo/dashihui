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
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessUser;
import com.dashihui.afford.business.entity.EtyList;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseFragment;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.ui.activity.server.AtyServer;
import com.dashihui.afford.ui.adapter.AdapterFrgServerOrderNopay;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.toast.UtilToast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentServer extends BaseFragment {
    private static final String TAG = "WdtAppsFragment";
    private static final String ARG_POSITION = "position";
//    public final static String LISTMAP_TYPE = "LISTMAPTYPE";
    private static Context mContext;

    /**
     * 列表相关
     */

    private List<Map<String, Object>> mListMap;

    private int position;
    private int pageNum = 1;//当前页码
    private int mTotalPage = 0;//服务总页数

    @ViewInject(R.id.fragment_listview)
    private PullToRefreshListView mListView;
    @ViewInject(R.id.noOrder)
    private LinearLayout mLytOrder;
    private AdapterFrgServerOrderNopay mAdapterSeverNopay;//待支付

    private BusinessUser mBllUser;

    public static FragmentServer newInstance(Context context, int position) {
        FragmentServer f = new FragmentServer();
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
        requestListViewData();//请求列表数据
        return rootView;
    }

    private void requestListViewData() {
        mBllUser = new BusinessUser(this);
        mListMap = new ArrayList<Map<String, Object>>();
        mBllUser.userServerOrderList((position + 1) + "", pageNum + "");

        //根据订单列表分类，加载不同的adapter

        mAdapterSeverNopay = new AdapterFrgServerOrderNopay(getActivity(), this, mListMap);
        mListView.setAdapter(mAdapterSeverNopay);

        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //刷新
                LogUtils.e("onPullDownToRefresh=====================>");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //加载更多
                LogUtils.e("onPullUpToRefresh=====================>");

                if (mTotalPage > 0 && mTotalPage > pageNum) {
                    //待付款，待发货，待收货
                    pageNum++;
                    mBllUser.userServerOrderList((position + 1) + "", pageNum + "");
                } else {
                    // 隐藏加载更多圈 底部
                    mListView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mListView.onRefreshComplete();
                        }
                    }, 1000);
                }
            }
        });
    }


    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
//        LogUtils.e("onSuccess=======FragmentServerOrderList====beanSendUI====>" + beanSendUI);
        LogUtils.e("onSuccess=======FragmentServerOrderList====beanSendUI.getInfo()====>" + beanSendUI.getInfo());
        if (beanSendUI != null)
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_USER_SERVICEORDERLIST://服务订单列表
                    EtyList listObject = (EtyList) beanSendUI.getInfo();
                    mTotalPage = listObject.getTOTALPAGE();
                    LogUtils.e("onSuccess=======mTotalPage========>" + mTotalPage);
                    if (pageNum ==1) {
                        mListMap.clear();
                    }
                    if (!UtilList.isEmpty(listObject.getLIST())) {
                        mListMap.addAll(listObject.getLIST());
                        mAdapterSeverNopay.notifyDataSetChanged();
                    }
                    break;
                case AffConstans.BUSINESS.TAG_SER_ORDER_CANCEL://家政取消订单
                    UtilToast.show(mContext, "订单取消成功！", Toast.LENGTH_SHORT);
                    mBllUser.userServerOrderList((position + 1) + "", pageNum + "");

                    break;
                case AffConstans.BUSINESS.TAG_SERVICE_ORDER_CANCEL://其他服务取消订单
                    UtilToast.show(mContext, "订单取消成功！", Toast.LENGTH_SHORT);
                    mBllUser.userServerOrderList((position + 1) + "", pageNum + "");

                    break;

                default:
                    break;
            }
        if (mListMap != null) {
            mAdapterSeverNopay.setList(mListMap);
            mAdapterSeverNopay.notifyDataSetChanged();
        }

        //没有订单时显示的页面
        if (mListMap.size() > 0) {
            mLytOrder.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        } else {
            mLytOrder.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }
        // 隐藏加载更多圈 底部
        mListView.onRefreshComplete();
    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
//        LogUtils.e("onFailure=======FragmentOrderList====beanSendUI.getInfo()====>" + beanSendUI.getInfo());
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_SER_ORDER_CANCEL://家政取消订单
                case AffConstans.BUSINESS.TAG_SERVICE_ORDER_CANCEL://其他服务取消订单
                    UtilToast.show(mContext, beanSendUI.getInfo()+"", Toast.LENGTH_SHORT);
                    break;

                default:
                    break;
            }
        }
    }

    @OnClick(R.id.go_shopping)
    public void onGoShoppingClick(View v){
        mContext.startActivity(new Intent(mContext, AtyServer.class));
    }
}