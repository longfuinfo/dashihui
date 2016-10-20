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
import com.dashihui.afford.business.BusinessServer;
import com.dashihui.afford.business.BusinessUser;
import com.dashihui.afford.business.entity.EtyList;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseFragment;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.ui.activity.server.AtyServer;
import com.dashihui.afford.ui.adapter.AdapterFrgServerOrderWaitSer;
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

public class FragmentServerWait extends BaseFragment {
    private static final String TAG = "WdtAppsFragment";
    private static final String ARG_POSITION = "position";
    private static Context mContext;

    public final static String LISTMAP_TYPE = "LISTMAPTYPE";

    /**
     * 列表相关
     */
    private List<Map<String, Object>> mListMap;

    private int position;
    private int pageNum = 1;//当前页码
    private int totalPage = 0;//服务总页数

    @ViewInject(R.id.fragment_listview)
    private PullToRefreshListView mListView;
    @ViewInject(R.id.noOrder)
    private LinearLayout mLytOrder;
    private AdapterFrgServerOrderWaitSer mAdapterWaitdeliver;//待服务

    private boolean mLoadMore = false;//是否为加载更多，如果是上拉操作，则为true
    private BusinessUser mBllUser;
    private BusinessServer mBllServer;

    public static FragmentServerWait newInstance(Context context, int position) {
        FragmentServerWait f = new FragmentServerWait();
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
        mBllServer = new BusinessServer(this);
        mListMap = new ArrayList<Map<String, Object>>();
        mBllUser.userServerOrderList((position + 1) + "", pageNum + "");

        //根据订单列表分类，加载不同的adapter

        mAdapterWaitdeliver = new AdapterFrgServerOrderWaitSer(getActivity(), this, mListMap);
        mListView.setAdapter(mAdapterWaitdeliver);

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
                if (totalPage > 0 && totalPage > pageNum) {
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
        LogUtils.e("onSuccess=======FragmentServerOrderList====beanSendUI====>" + beanSendUI);
        LogUtils.e("onSuccess=======FragmentServerOrderList====beanSendUI.getInfo()====>" + beanSendUI.getInfo());
        switch (beanSendUI.getTag()) {
            case AffConstans.BUSINESS.TAG_USER_SERVICEORDERLIST://用户服务订单列表
                EtyList listObject = (EtyList) beanSendUI.getInfo();
                totalPage = listObject.getTOTALPAGE();
                if (pageNum ==1) {
                    mListMap.clear();
                }
                if (!UtilList.isEmpty(listObject.getLIST())) {
                    mListMap.addAll(listObject.getLIST());
                    mAdapterWaitdeliver.notifyDataSetChanged();
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
            case AffConstans.BUSINESS.TAG_SER_ORDER_URGE://家政催单
                UtilToast.show(mContext, "催单成功！", Toast.LENGTH_SHORT);

                break;
            case AffConstans.BUSINESS.TAG_SERVICE_ORDER_URGE://其他服务催单
                UtilToast.show(mContext, "催单成功！", Toast.LENGTH_SHORT);

                break;
            case AffConstans.BUSINESS.TAG_SERVICE_ORDER_SIGN://服务签收订单
                UtilToast.show(mContext, "订单已确认！", Toast.LENGTH_SHORT);
                mBllUser.userServerOrderList(pageNum + "", (position + 1) + "");
                break;
            case AffConstans.BUSINESS.TAG_SER_ORDER_SIGN://家政签收订单
                UtilToast.show(mContext, "订单已确认！", Toast.LENGTH_SHORT);
                mBllUser.userServerOrderList((position + 1) + "", pageNum + "");
                break;
            case AffConstans.BUSINESS.TAG_SER_ORDER_DELETE://家政删除订单
                UtilToast.show(mContext, "订单删除成功！", Toast.LENGTH_SHORT);
                mBllUser.userServerOrderList((position + 1) + "", pageNum + "");

                break;
            case AffConstans.BUSINESS.TAG_SERVICE_ORDER_DELETE://其他服务删除订单
                UtilToast.show(mContext, "订单删除成功！", Toast.LENGTH_SHORT);
                mBllUser.userServerOrderList((position + 1) + "", pageNum + "");

                break;
            default:
                break;
        }

        if (mListMap != null) {
            mAdapterWaitdeliver.setList(mListMap);
            mAdapterWaitdeliver.notifyDataSetChanged();
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
        LogUtils.e("onFailure=======FragmentOrderList====beanSendUI.getInfo()====>" + beanSendUI.getInfo());
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_SER_ORDER_CANCEL://家政取消订单
                case AffConstans.BUSINESS.TAG_SERVICE_ORDER_CANCEL://其他服务取消订单
                case AffConstans.BUSINESS.TAG_SER_ORDER_URGE://家政催单
                case AffConstans.BUSINESS.TAG_SERVICE_ORDER_URGE://其他服务催单
                case AffConstans.BUSINESS.TAG_SERVICE_ORDER_SIGN://服务签收订单
                case AffConstans.BUSINESS.TAG_SER_ORDER_SIGN://家政签收订单
                    UtilToast.show(mContext, beanSendUI.getInfo()+"", Toast.LENGTH_SHORT);
                    break;
                case AffConstans.BUSINESS.TAG_SER_ORDER_DELETE://家政删除订单
                    UtilToast.show(mContext, beanSendUI.getInfo()+"", Toast.LENGTH_SHORT);
                    mBllUser.userServerOrderList((position + 1) + "", pageNum + "");

                    break;
                case AffConstans.BUSINESS.TAG_SERVICE_ORDER_DELETE://其他服务删除订单
                    UtilToast.show(mContext, beanSendUI.getInfo()+"", Toast.LENGTH_SHORT);
                    mBllUser.userServerOrderList((position + 1) + "", pageNum + "");

                    break;
                default:
                    break;
            }
        }
    }


    @OnClick(R.id.go_shopping)
    public void onGoShoppingClick(View v) {
        mContext.startActivity(new Intent(mContext, AtyServer.class));
    }
}