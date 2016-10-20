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
import android.widget.Toast;

import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessOrder;
import com.dashihui.afford.business.entity.EtyList;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseFragment;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.ui.activity.AtyHome;
import com.dashihui.afford.ui.activity.AtyOrderComplete;
import com.dashihui.afford.ui.activity.AtySettlementOrder;
import com.dashihui.afford.ui.adapter.AdapterAllOrder;
import com.dashihui.afford.ui.adapter.AdapterFrgOrderEvaluate;
import com.dashihui.afford.ui.adapter.AdapterFrgOrderNopay;
import com.dashihui.afford.ui.adapter.AdapterFrgOrderNotake;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.toast.UtilToast;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentOrder extends BaseFragment {
    private static final String TAG = "WdtAppsFragment";
    private static final String ARG_POSITION = "position";

    /**
     * 列表相关
     */
//	private PullToRefreshListView mListView;// 列表
//	private AdapterApps mListViewAdapter;// 列表Adapter
    private List<Map<String, Object>> mMapList;

    private static Context mContext;
    private int position;
    private int mPosiRece;//待收货的页面下标

    private boolean mLoadMore = false;//是否为加载更多，如果是上拉操作，则为true

    private BusinessOrder mBllOrder;
    private int pageNum = 1;//当前页码
    private int totalPage = 0;//总页数
    @ViewInject(R.id.fragment_listview)
    private PullToRefreshListView mListView;
    @ViewInject(R.id.noOrder)
    private LinearLayout mLytOrder;

    //列表适配器
    private AdapterAllOrder mAdapterAll;//全部
    private AdapterFrgOrderNopay mAdapter;//待付款
    private AdapterFrgOrderNotake mAdapterNotake;//待收货
    private AdapterFrgOrderEvaluate mAdapterEva;//待评价

    public static String orderPrice ="";
    public static String orderCode ="";

    public static FragmentOrder newInstance(Context context, int position) {
        FragmentOrder f = new FragmentOrder();
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
    public void onResume() {
        super.onResume();
        LogUtils.e("FragmentOrder=======onResume==============>"+position);
//        if (position == 3)  {
        mBllOrder.ordeList((position + 1) + "", pageNum + "");
//        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){
            LogUtils.e("FragmentOrder======**************========>" + getUserVisibleHint() + (position + 1)+pageNum);
            mBllOrder = new BusinessOrder(this);
            mBllOrder.ordeList((position + 1) + "",pageNum + "");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_listview, container, false);
        ViewUtils.inject(this, rootView); //注入view和事件
        mMapList = new ArrayList<>();

        //请求列表数据
        mBllOrder = new BusinessOrder(this);
        mBllOrder.ordeList((position + 1) + "", pageNum + "");


        //根据订单列表分类，加载不同的adapter
        if (position == 0) {
            mAdapterAll = new AdapterAllOrder(getActivity(), this, mMapList);
            mListView.setAdapter(mAdapterAll);
        } else if (position == 1) {
            mAdapter = new AdapterFrgOrderNopay(getActivity(), this, mMapList);
            mListView.setAdapter(mAdapter);
        } else if (position == 2) {
            mAdapterNotake = new AdapterFrgOrderNotake(getActivity(), this, mMapList);
            mListView.setAdapter(mAdapterNotake);
        }else {
            mAdapterEva = new AdapterFrgOrderEvaluate(getActivity(), this, mMapList);
            mListView.setAdapter(mAdapterEva);
        }
        return rootView;
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_ORDER_LIST://订单列表
                    EtyList listObjects = (EtyList) beanSendUI.getInfo();
                    if (pageNum==1) {
                        mMapList.clear();
                    }
                    if (listObjects != null && !UtilList.isEmpty(listObjects.getLIST())) {
                        mMapList.addAll(listObjects.getLIST());
                        LogUtils.e("onSuccess=========listObjects===确认订单成功=0====>" + mMapList);
                        //根据订单列表分类，加载不同的adapter
                        totalPage = listObjects.getTOTALPAGE();

                        if (position == 0) {
                            LogUtils.e("onSuccess=========mMapList=000===>" + mMapList);
                            mAdapterAll.setList(mMapList);
                            mAdapterAll.notifyDataSetChanged();
                        } else if (position == 1) {
                            mAdapter.setList(mMapList);
                            mAdapter.notifyDataSetChanged();
                        }  else if (position == 2) {
                            LogUtils.e("onSuccess=========mMapList==222==>" + mMapList);
                            mAdapterNotake.setList(mMapList);
                            mAdapterNotake.notifyDataSetChanged();
                        }else {
                            LogUtils.e("onSuccess=========mMapList==333==>" + mMapList);
                            mAdapterEva.setList(mMapList);
                            mAdapterEva.notifyDataSetChanged();
                        }
                    }

                    //没有订单时显示的页面
                    if (mMapList != null) {
                        if (mMapList.size() > 0) {
                            mLytOrder.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                        } else {
                            mLytOrder.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.GONE);
                        }
                    }
                    break;

                case AffConstans.BUSINESS.TAG_ORDER_URGE://催单
                    UtilToast.show(mContext, "催单成功，送货员正飞奔在路上！！", Toast.LENGTH_SHORT);
                    break;
                case AffConstans.BUSINESS.TAG_ORDER_DOCANCELORDER://取消订单
                    mBllOrder.ordeList((position + 1) + "", pageNum + "");
                    UtilToast.show(mContext, "订单取消成功！", Toast.LENGTH_SHORT);
                    break;
                case AffConstans.BUSINESS.TAG_ORDER_DORECEIVEORDER://确认收货
//                    Map<String, Object> map = (Map<String, Object>) beanSendUI.getInfo();
//                    mBllOrder.ordeList((position + 1) + "", pageNum + "");
                    UtilToast.show(mContext, "非常感谢，欢迎再次订购！", Toast.LENGTH_SHORT);
                    Intent intent = new Intent(mContext,AtyOrderComplete.class);
                    if (!"".equals(orderCode) && !"".equals(orderPrice)){
                        intent.putExtra(AtySettlementOrder.ORDER_CODE,orderCode + "");
                        intent.putExtra(AtySettlementOrder.ORDER_PRICE, orderPrice + "");
                        startActivity(intent);
                        getActivity().finish();
                    }
                    break;
                case AffConstans.BUSINESS.TAG_ORDER_DELETE://删除订单
//                    Map<String, Object> map = (Map<String, Object>) beanSendUI.getInfo();
                    mBllOrder.ordeList((position + 1) + "", pageNum + "");
                    UtilToast.show(mContext, "订单已删除！", Toast.LENGTH_SHORT);

                    break;
                default:
                    break;
            }

        } else {
            LogUtils.e("onSuccess===列表==beanSendUI.getInfo()=========>" + beanSendUI.getInfo());
        }

        // 隐藏加载更多圈 底部
        mListView.onRefreshComplete();
    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        switch (beanSendUI.getTag()) {
            case AffConstans.BUSINESS.TAG_ORDER_URGE:
            case AffConstans.BUSINESS.TAG_ORDER_DORECEIVEORDER:
                UtilToast.show(mContext, beanSendUI.getInfo()+"", Toast.LENGTH_SHORT);
//                mMapList.clear();
//                mListView.setAdapter();
//                .notifyDataSetChanged();
                break;
            default:
                break;
        }
        // 隐藏加载更多圈 底部
        mListView.onRefreshComplete();
    }

    @OnClick(R.id.go_shopping)//立即逛逛
    public void onGoShoppingClick(View v) {
        mContext.startActivity(new Intent(mContext, AtyHome.class));
    }
}