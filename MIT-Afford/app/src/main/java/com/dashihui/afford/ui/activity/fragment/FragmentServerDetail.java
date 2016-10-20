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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.TypeReference;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessServer;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseFragment;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.thirdapi.FastJSONHelper;
import com.dashihui.afford.ui.activity.AtyFragmentServerDetail;
import com.dashihui.afford.ui.activity.AtyFragmentService;
import com.dashihui.afford.ui.adapter.AdapterFrgServerDetail;
import com.dashihui.afford.ui.adapter.AdapterFrgServerDetailGoods;
import com.dashihui.afford.util.number.UtilNumber;
import com.dashihui.afford.util.toast.UtilToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentServerDetail extends BaseFragment {
    private static final String TAG = "WdtAppsFragment";
    private static final String ARG_POSITION = "position";

    /**
     * 列表相关
     */
//	private PullToRefreshListView mListView;// 列表
//	private AdapterApps mListViewAdapter;// 列表Adapter

    private static Context mContext;
    private int position;

    private boolean mLoadMore = false;//是否为加载更多，如果是上拉操作，则为true

    private BusinessServer mBllServer;
    private int pageNum = 1;

    @ViewInject(R.id.listViewGoods)
    private ListView mListViewGoods;
    @ViewInject(R.id.listView)
    private ListView mListView;
    @ViewInject(R.id.goods_price)
    private TextView mTotalPrice;
    @ViewInject(R.id.tv_cancelorder)
    private TextView mCancelOrder;//取消订单时动态的文本框

    @ViewInject(R.id.sername)
    private TextView mSerClassName;//服务分类名
    @ViewInject(R.id.sertime)
    private TextView mSerTime;//服务时间
    @ViewInject(R.id.serPrice)
    private TextView mSerPrice;//服务价格
    @ViewInject(R.id.lyt_seritem)
    private LinearLayout mLytSerItem;//购买服务布局

    private String mOrderPrice;
    private String mOrderNum;//订单号
    private int mType;//订单类型，1：家政服务，2：其他服务
    private String mServiceTitle;//服务项，如：日常保洁2小时
    private String mHouseOrderNum;//订单号
    private int mPaytype, mPayState, mDeliverState, mOrderState;//
    private String mStartTime;//下单时间


    //列表适配器
    private AdapterFrgServerDetail mAdapterServerDetail;//订单详情
    private AdapterFrgServerDetailGoods mAdapterServerDetailGoods;
    private List<Map<String, String>> listMap = new ArrayList<>();
    private List<Map<String, Object>> listMapGoods;
    private FragmentServerState mFragServerState;
    private AtyFragmentServerDetail mAtyFrgServerDetail;
    public static int selectPos;
    private Map<String, Object> mapObject;
    private Map<String, Object> mHouseMapObject;
    private Map<String, Object> mAllMapObject;

//    private int mListMapTpye;//数据分类标记



    public static FragmentServerDetail newInstance(Context context, int position) {
        selectPos = position;
        FragmentServerDetail f = new FragmentServerDetail();
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
        mFragServerState = new FragmentServerState();
        mAtyFrgServerDetail = new AtyFragmentServerDetail();
        mAtyFrgServerDetail.ORDSTATETAG = false;

//        //记录从服务家政或服务传过来的标志
        LogUtils.e("onCreate===========从服务家政或服务传过来的标志mHouseTpye====fragment====>" + AtyFragmentServerDetail.mAtyHouseTpye);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_server_detail, container, false);
        ViewUtils.inject(this, rootView); //注入view和事件
        mCancelOrder.setVisibility(View.GONE);
        //请求列表数据

        mBllServer = new BusinessServer(this);
        mAllMapObject = new HashMap<>();
        /*******************************************/


        //服务后付款
        //判断订单号是否为空
        LogUtils.e("FragmentServerDetail=========mOrderCode=====mOrderCode===>" + AtyFragmentServerDetail.mOrderCode);
        LogUtils.e("FragmentServerDetail=========mOrderCode====mAtyHouseTpye====>" + AtyFragmentServerDetail.mAtyHouseTpye);
        if (!"".equals(AtyFragmentServerDetail.mOrderCode)) {
            //判断是从AtyServerHouseSettlement还是AtyServerSettlement传过来的订单号，AtyServerHouseSettlement的订单标志位2，AtyServerSettlement订单标志为1
            if (CommConstans.ORDER.SERVER_ORDER_PAY.equals(AtyFragmentServerDetail.mAtyHouseTpye)) {//服务
                //请求服务分类的订单详情
                LogUtils.e("FragmentServerDetail=========mOrderCode====请求服务分类的订单详情====>" + AtyFragmentServerDetail.mAtyHouseTpye);
                mBllServer.getOrderDetail(AtyFragmentServerDetail.mOrderCode);
            } else if (CommConstans.ORDER.HOUSE_ORDER_PAY.equals(AtyFragmentServerDetail.mAtyHouseTpye)) {//家政
                //请求服务家政的订单详情
                mBllServer.getSerOrderDetail(AtyFragmentServerDetail.mOrderCode);
            } else {
                UtilToast.show(mContext, "服务/家政分类标志错误===>还需修改", Toast.LENGTH_SHORT);
            }
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        LogUtils.e("onSuccess=======FragmentOrderDetail====beanSendUI====>" + beanSendUI);
        LogUtils.e("onSuccess=======FragmentOrderDetail====beanSendUI.getInfo()====>" + beanSendUI.getInfo());
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_SERVICE_ORDER_DETAIL://服务订单详情
                    mapObject = (Map<String, Object>) beanSendUI.getInfo();
                    //存放一个标志，记录是哪个服务分类的数据，用这个标志来区分订单的状态的接口
                    LogUtils.e("onSuccess====111111111111111===mapObject====mapObject====>" + mapObject);
                    onServerOrderDetailOnSuccess(mapObject);//请求成功后加载数据
//                    orderStateListSuccess();
                    break;
                case AffConstans.BUSINESS.TAG_SER_ORDER_DETAIL://家政订单详情
                    mHouseMapObject = (Map<String, Object>) beanSendUI.getInfo();
                    //存放一个标志，记录是哪个服务分类的数据，用这个标志来区分订单的状态的接口
                    onServerOrderDetailOnSuccess(mHouseMapObject);//请求成功后加载数据
//                    orderStateListSuccess();
                    break;
                case AffConstans.BUSINESS.TAG_SERVICE_ORDER_CANCEL://取消服务订单
                    UtilToast.show(mContext, "订单取消成功", Toast.LENGTH_SHORT);
                    Intent intent = new Intent(getActivity(), AtyFragmentService.class);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                    LogUtils.e("OnSuccess=====1111111111=====mOrderNum==cancelOrder==========>" + mOrderNum);
                    break;
                case AffConstans.BUSINESS.TAG_SER_ORDER_CANCEL://取消服务订单
                    UtilToast.show(mContext, "订单取消成功", Toast.LENGTH_SHORT);
                    Intent houseIntent = new Intent(getActivity(), AtyFragmentService.class);
                    getActivity().startActivity(houseIntent);
                    getActivity().finish();
                    LogUtils.e("OnSuccess==========mOrderNum==cancelOrder==========>" + mOrderNum);
                    break;
                case AffConstans.BUSINESS.TAG_SERVICE_ORDER_SIGN://服务订单确认收货
                    UtilToast.show(mContext, "订单已完成，欢迎再次订购！", Toast.LENGTH_SHORT);
                    Intent intent1 = new Intent(getActivity(), AtyFragmentService.class);
                    getActivity().startActivity(intent1);
                    getActivity().finish();
                    LogUtils.e("OnSuccess==========mOrderNum==cancelOrder==========>" + mOrderNum);
                    break;
                case AffConstans.BUSINESS.TAG_SERVICE_ORDER_URGE://服务订单催单
                    UtilToast.show(mContext, "催单成功！", Toast.LENGTH_SHORT);
                    LogUtils.e("OnSuccess==========mOrderNum==cancelOrder==========>" + mOrderNum);

                    //****************************此处注释需要处理

//                    mBllServer.getOrderDetail(AtyFragmentOrdertDetail_bak.mOrderCode);
                    listMap.clear();
                    mListView.setAdapter(mAdapterServerDetail);
                    mAdapterServerDetail.notifyDataSetChanged();
                    break;
                default:
                    break;
            }

        } else {
            LogUtils.e("onSuccess=======error====>" + beanSendUI);
        }
//        LogUtils.e("==========2=========>");
//        if (mListMapTpye == 1) {
//            mAdapterServerDetailGoods = new AdapterFrgServerDetailGoods(getActivity(), listMapGoods);
//            mListViewGoods.setAdapter(mAdapterServerDetailGoods);
//            setlistViewHeigh(mListViewGoods);
//            LogUtils.e("onCreateView===========mListMapTpye========>" + mListMapTpye);
//        } else if (mListMapTpye == 2) {
//            mAdapterServerDetail = new AdapterFrgServerDetail(getActivity(), listMap);
//            mListView.setAdapter(mAdapterServerDetail);
//            setlistViewHeigh(mListView);
//        }
    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        LogUtils.e("onFailure=======FragmentOrderList====beanSendUI.getInfo()====>" + beanSendUI.getInfo());
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_SERVICE_ORDER_CANCEL://取消服务订单
                case AffConstans.BUSINESS.TAG_ORDER_DORECEIVEORDER:
                case AffConstans.BUSINESS.TAG_ORDER_URGE:
                    UtilToast.show(mContext, beanSendUI.getInfo()+"", Toast.LENGTH_SHORT);
                    LogUtils.e("OnSuccess==========mOrderNum==cancelOrder==========>" + mOrderNum);
                    break;
                default:
                    break;
            }
        }
    }

  /***************************************************/


//    public void addMapDataSerOrderDetail(Map<String, Object> mapObject) {
//        LogUtils.e("addMapDataSerOrderDetail======mAllMapObject===11111===>" + mAllMapObject);
//        LogUtils.e("addMapDataSerOrderDetail======mapObject======>" + mapObject);
//        LogUtils.e("addMapDataSerOrderDetail======mHouseMapObject======>" + mHouseMapObject);
//
//        onServerOrderDetailOnSuccess(mapObject);//请求成功后加载数据
//    }

    /**
     * 请求服务器成功后 获取订单数据
     */
    public void onServerOrderDetailOnSuccess(Map<String, Object> mapObject) {
        LogUtils.e("onServerOrderDetailOnSuccess=========mType========>" + mapObject.get(CommConstans.FRAGSERSTATEORDERCODE.TYPE) + "");
        mType = UtilNumber.IntegerValueOf(mapObject.get(CommConstans.FRAGSERSTATEORDERCODE.TYPE) + "");//订单类型，1：家政服务，2：其他服务
        mOrderNum = mapObject.get(CommConstans.FRAGSERSTATEORDERCODE.ORDERNUM) + "";//订单号
        mServiceTitle = mapObject.get(CommConstans.FRAGSERSTATEORDERCODE.SERVICETITLE) + "";//服务项，如：日常保洁2小时
        mPaytype = UtilNumber.IntegerValueOf(mapObject.get(CommConstans.FRAGSERSTATEORDERCODE.PAYTYPE) + "");
        mPayState = UtilNumber.IntegerValueOf(mapObject.get(CommConstans.FRAGSERSTATEORDERCODE.PAYSTATE) + "");
        mDeliverState = UtilNumber.IntegerValueOf(mapObject.get(CommConstans.FRAGSERSTATEORDERCODE.DELIVERSTATE) + "");
        mOrderState = UtilNumber.IntegerValueOf(mapObject.get(CommConstans.FRAGSERSTATEORDERCODE.ORDERSTATE) + "");
        mOrderPrice = mapObject.get(CommConstans.FRAGSERSTATEORDERCODE.AMOUNT) + "";//订单总价
        mStartTime = mapObject.get(CommConstans.FRAGSERSTATEORDERCODE.STARTDATE) + "";//下单时间

        showSerOrderDetail(mapObject);//展示家政服务订单详情
    }

    /**
     * 展示家政服务订单详情
     */
    public void showSerOrderDetail(Map<String, Object> mapObject) {
        addMaps(mapObject);
        LogUtils.e("showSerOrderDetail=========mType========>" + mType);
        LogUtils.e("showSerOrderDetail=========HOUSESER========>"+CommConstans.FRAGSERSTATEORDERTYPE.HOUSESER);
        LogUtils.e("showSerOrderDetail=========SERVER========>"+CommConstans.FRAGSERSTATEORDERTYPE.SERVER);
        if (CommConstans.ORDER.HOUSE_ORDER_PAY.equals(AtyFragmentServerDetail.mAtyHouseTpye)) {//家政数据
            mLytSerItem.setVisibility(View.VISIBLE);

            //根据获取的type不同写入不同控件
            if (mType == CommConstans.FRAGSERSTATECLEANTYPE.DAILY) {//日常保洁
                mSerClassName.setText("日常保洁");
                mSerTime.setText(mapObject.get("TITLE") + "");
                mSerPrice.setText(mapObject.get("AMOUNT") + "");
            } else if (mType == CommConstans.FRAGSERSTATECLEANTYPE.DEPTH){//深度保洁
                mSerClassName.setText("深度保洁");
                mSerTime.setText(mapObject.get("TITLE") + "");
                mSerPrice.setText(mapObject.get("AMOUNT") + "");
            }
            mAdapterServerDetail = new AdapterFrgServerDetail(getActivity(), listMap);
            mListView.setAdapter(mAdapterServerDetail);
            setlistViewHeigh(mListView);
        }else if (CommConstans.ORDER.SERVER_ORDER_PAY.equals(AtyFragmentServerDetail.mAtyHouseTpye)) {//服务数据
            LogUtils.e("showSerOrderDetail=========mType=111111=======>" + mType);
            mLytSerItem.setVisibility(View.GONE);
            TypeReference typeListMap = new TypeReference<List<Map<String, Object>>>() {
            };
            listMapGoods = (List<Map<String, Object>>) FastJSONHelper.deserializeAny(mapObject.get("SERVICELIST") + "", typeListMap);
            mAdapterServerDetailGoods = new AdapterFrgServerDetailGoods(getActivity(), listMapGoods);
            mListViewGoods.setAdapter(mAdapterServerDetailGoods);
            setlistViewHeigh(mListViewGoods);

            mAdapterServerDetail = new AdapterFrgServerDetail(getActivity(), listMap);
            mListView.setAdapter(mAdapterServerDetail);
            setlistViewHeigh(mListView);
        }else  {
            UtilToast.show(mContext, "服务/家政数据区分标志又错了===>", Toast.LENGTH_SHORT);
            LogUtils.e("showSerOrderDetail====else=====mType========>" + mType);
            LogUtils.e("showSerOrderDetail====else=====HOUSESER========>"+CommConstans.FRAGSERSTATEORDERTYPE.HOUSESER);
            LogUtils.e("showSerOrderDetail====else=====SERVER========>" + CommConstans.FRAGSERSTATEORDERTYPE.SERVER);
        }

        mOrderNum = mAllMapObject.get("ORDERNUM") + "";
        mOrderPrice = mAllMapObject.get("AMOUNT") + "";

        LogUtils.e("addMapDataSerOrderDetail======mAllMapObject===22222===>" + mAllMapObject);

    }

    public void addMaps(Map<String, Object> mapObject) {
        /*****************	//支付方式1：在线支付，2：货到付款**********************/
        Map<String, String> mapStr1 = new HashMap<>();
        mapStr1.put("name", "订单号：");
        mapStr1.put("content", mapObject.get("ORDERNUM") + "");
        listMap.add(mapStr1);
        Map<String, String> mapStr2 = new HashMap<>();
        mapStr2.put("name", "下单时间：");
        mapStr2.put("content", mapObject.get("STARTDATE") + "");
        listMap.add(mapStr2);
        Map<String, String> mapStr3 = new HashMap<>();
        mapStr3.put("name", "支付方式：");
        if ("1".equals(mapObject.get("PAYTYPE") + "")) {//在线支付
            mapStr3.put("content", "在线支付");

        } else if ("2".equals(mapObject.get("PAYTYPE") + "")) {//货到付款
            mapStr3.put("content", "货到付款");
        }
        listMap.add(mapStr3);
//        /*****************收货方式1：送货，2：自取**********************/
//        Map<String, String> mapStr4 = new HashMap<>();
//        mapStr4.put("name", "配送方式：");
//        if ("1".equals(mapObject.get("TAKETYPE") + "")) {//----送货
//            mapStr4.put("content", "门店配送");
//        } else if ("2".equals(mapObject.get("TAKETYPE") + "")) {//---自取
//            mapStr4.put("content", "上门自取");
//        }
//        listMap.add(mapStr4);

        Map<String, String> mapStr5 = new HashMap<>();
        mapStr5.put("name", "配送备注：");
        LogUtils.e("==========配送备注==========>" + mapObject.get("DESCRIBE") + "");
        if (!"null".equals(mapObject.get("DESCRIBE") + "")) {
            mapStr5.put("content", mapObject.get("DESCRIBE") + "");
        } else {
            mapStr5.put("content", "无备注");
        }
        listMap.add(mapStr5);

        /************联系人、手机号、收货地址************/
        Map<String, String> mapStr6 = new HashMap<>();
        mapStr6.put("name", "联系人：");
        mapStr6.put("content", mapObject.get("LINKNAME") + "");
        listMap.add(mapStr6);

        Map<String, String> mapStr7 = new HashMap<>();
        mapStr7.put("name", "手机号：");
        mapStr7.put("content", mapObject.get("TEL") + "");
        listMap.add(mapStr7);

        Map<String, String> mapStr8 = new HashMap<>();
        mapStr8.put("name", "收货地址：");
        mapStr8.put("content", mapObject.get("ADDRESS") + "");
        listMap.add(mapStr8);

        //总计金额
        mTotalPrice.setText(mapObject.get("AMOUNT") + "");
    }

    /**
     * 动态设置listview高度
     *
     * @param listView
     */
    private void setlistViewHeigh(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        listView.setLayoutParams(params);

    }
}