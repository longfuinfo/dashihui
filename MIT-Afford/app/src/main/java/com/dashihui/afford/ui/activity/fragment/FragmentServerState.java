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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessServer;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseFragment;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.ui.activity.AtyFragmentServerDetail;
import com.dashihui.afford.ui.activity.AtyFragmentService;
import com.dashihui.afford.ui.activity.AtySettlementOrder;
import com.dashihui.afford.ui.adapter.AdapterFrgServerState;
import com.dashihui.afford.ui.widget.WgtAlertDialog;
import com.dashihui.afford.util.number.UtilNumber;
import com.dashihui.afford.util.toast.UtilToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentServerState extends BaseFragment {
    public static final String TAG = "WdtAppsFragment";
    private static final String ARG_POSITION = "position";
    private static final String ORDERNUM = "orderNum";
    private static final String ORDERPRICE = "orderPrice";
    public static final String ORDERINFO = "orderInfo";
    public static final String ORDERSTATES = "orderstates";

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

    @ViewInject(R.id.frag_listview)
    private ListView mListView;
    @ViewInject(R.id.tv_cancel_order)
    private TextView mTvCancelOrder;//取消订单时显示的文本框

    @ViewInject(R.id.tvBtn_cancel_order)
    private Button mTvCancel;//取消订单
    @ViewInject(R.id.tvBtn_topay)
    private Button mTvToPay;//去支付
    @ViewInject(R.id.tvBtn_take)
    private Button mTvConfirm;//确认收货
    @ViewInject(R.id.tvBtn_urged)
    private Button mTvUrged;//催单
    @ViewInject(R.id.tvBtn_delete)
    private Button mTvDelet;//催单
    @ViewInject(R.id.lyt_bottom_order)
    private LinearLayout mLytBtnBottom;//四个状态按钮的布局
    @ViewInject(R.id.btn_again)
    private Button mBtnAgain;//再来一单
    @ViewInject(R.id.rlyt_bottom_order)
    private RelativeLayout mRlytBottom;

    private String mOrderNum;//订单号
    private int mType;//订单类型，1：家政服务，2：其他服务
    private String mServiceTitle;//服务项，如：日常保洁2小时
    private String mHouseOrderNum;//订单号
    private int mPaytype, mPayState, mDeliverState, mOrderState;//

    private String mOrderPrice;//订单价
    private String mPayTime;//支付时间
    private String mStartTime;//下单时间
    private String mLastUrgeTime;//前一次催单时间
    private String mSysThisTime;//系统当前时间
    private int mUrgeNums;//催单次数
    private String mThisUrgeTime;//本次催单时间
    private int mThisUrgeNums;//本次催单次数
    private SimpleDateFormat mSimDateTime;
    private Date sysThisDate;//当前时间
    private Date lastDate;//最后一次催单时间
    private Date payDate;//支付时间
    private Date mDateStartDate;//下单时间
    private long UrgeDifferTime;//上次催单到当前的时间差
    private long urgedMinutes;//上次催单到当前的时间差（分钟）
    private long startDifferTime;//下单时间到当前时间差
    private long startMinutes;//支付时间到当前的时间差（分钟）


    private List<Map<String, Object>> listMapGoods;
    private String mOrderInfo;//再来一单时的订单信息
    private String orderState;//订单状态


    private Map<String, Object> mapObject;
    private Map<String, Object> mHouseMapObject;
//    private Map<String, Object> mAllMapObject;

//    private int mListMapTpye;//数据分类标记
//    private int mHouseTpye;//记录从服务家政或服务传过来的标志

    //列表适配器
    private AdapterFrgServerState mAdapterServerState;//订单状态
    private List<Map<String, String>> listMap = new ArrayList<>();
    public static int mSelectPos;

//    public static final String SERVERLISTTYPE = "1";
//    public static final String HOUSELISTTYPE = "2";

    public static FragmentServerState newInstance(Context context, int position) {
        mSelectPos = position;
        FragmentServerState f = new FragmentServerState();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        LogUtils.e("newInstance====================position=========================?" + position);
        mContext = context;
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        mapObject = new HashMap<>();

        //记录从服务家政或服务传过来的标志
        LogUtils.e("onCreate===========从服务家政或服务传过来的标志mHouseTpye====fragment=1===>" + AtyFragmentServerDetail.mAtyHouseTpye);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_server_state, container, false);
        ViewUtils.inject(this, rootView); //注入view和事件
        hideAllBottomMenu();//隐藏所有状态按钮
        mTvCancelOrder.setVisibility(View.GONE);
        //请求列表数据
        mBllServer = new BusinessServer(this);
//        mAllMapObject = new HashMap<>();
        //服务后付款
        //判断订单号是否为空
        //判断是从AtyServerHouseSettlement还是AtyServerSettlement传过来的订单号，AtyServerHouseSettlement的订单标志位2，AtyServerSettlement订单标志为1
        LogUtils.e("onCreateView=====判断订单号是否为空====mOrderCode========>" + AtyFragmentServerDetail.mOrderCode);
        if (!"".equals(AtyFragmentServerDetail.mOrderCode)) {
            //AtyServerSettlement.SERVIECE=102，AtyServerHouseSettlement.SER_HOUSE=101
            if (CommConstans.ORDER.SERVER_ORDER_PAY.equals(AtyFragmentServerDetail.mAtyHouseTpye)) {//服务
                //请求服务分类的订单详情
                mBllServer.getOrderDetail(AtyFragmentServerDetail.mOrderCode);
            } else if (CommConstans.ORDER.HOUSE_ORDER_PAY.equals(AtyFragmentServerDetail.mAtyHouseTpye)) {//家政
                //请求服务家政的订单详情
                mBllServer.getSerOrderDetail(AtyFragmentServerDetail.mOrderCode);
            } else {
                UtilToast.show(mContext, "服务/家政分类标志错误===>还需修改", Toast.LENGTH_SHORT);
            }
        }
        //加载adapter
        mAdapterServerState = new AdapterFrgServerState(getActivity(), listMap);
        mListView.setAdapter(mAdapterServerState);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_SERVICE_ORDER_DETAIL://服务订单详情
                    mapObject = (Map<String, Object>) beanSendUI.getInfo();
                    LogUtils.e("onSuccess====服务订单详情====>" + mapObject);
                    onServerOrderDetailOnSuccess(mapObject);
                    break;
                case AffConstans.BUSINESS.TAG_SER_ORDER_DETAIL://家政订单详情
                    mHouseMapObject = (Map<String, Object>) beanSendUI.getInfo();
                    LogUtils.e("onSuccess===家政订单详情====>" + mHouseMapObject);
                    onServerOrderDetailOnSuccess(mHouseMapObject);

                    break;
                case AffConstans.BUSINESS.TAG_SERVICE_ORDER_CANCEL://取消服务订单
                    UtilToast.show(mContext, "订单取消成功", Toast.LENGTH_SHORT);
                    Intent intent = new Intent(getActivity(), AtyFragmentService.class);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                    break;
                case AffConstans.BUSINESS.TAG_SER_ORDER_CANCEL://取消服务家政订单
                    UtilToast.show(mContext, "订单取消成功", Toast.LENGTH_SHORT);
                    Intent houseIntent = new Intent(getActivity(), AtyFragmentService.class);
                    getActivity().startActivity(houseIntent);
                    getActivity().finish();
                    break;
                case AffConstans.BUSINESS.TAG_SERVICE_ORDER_SIGN://服务订单确认收货
                    UtilToast.show(mContext, "订单已完成，欢迎再次订购！", Toast.LENGTH_SHORT);
                    LogUtils.e("OnSuccess==========mOrderNum==cancelOrder==========>" + mOrderNum);
                    Intent intent1 = new Intent(getActivity(), AtyFragmentService.class);
                    getActivity().startActivity(intent1);
                    getActivity().finish();
                    break;
                case AffConstans.BUSINESS.TAG_SERVICE_ORDER_URGE://服务订单催单
                    Map<String,Object> mapUrged = (Map<String, Object>) beanSendUI.getInfo();
                    mLastUrgeTime = mapUrged.get("DATETIME")+"";
                    urgeSuccess();
                    break;
                case AffConstans.BUSINESS.TAG_SER_ORDER_URGE://服务订单催单
                    Map<String,Object> mapSerUrged = (Map<String, Object>) beanSendUI.getInfo();
                    mLastUrgeTime = mapSerUrged.get("DATETIME")+"";
                    LogUtils.e("onSuccess==========mLastUrgeTime=========>" + mLastUrgeTime);
                    urgeSuccess();
                    break;
                default:
                    break;
            }
        } else {

        }
//        addMapDataSerOrderDetail();
    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        LogUtils.e("onFailure===========beanSendUI.getInfo()====>" + beanSendUI.getInfo());
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_SERVICE_ORDER_CANCEL://取消订单
                case AffConstans.BUSINESS.TAG_SERVICE_ORDER_SIGN://确认订单
                case AffConstans.BUSINESS.TAG_SERVICE_ORDER_URGE://催单
                case AffConstans.BUSINESS.TAG_SER_ORDER_URGE://家政催单
                    UtilToast.show(mContext, beanSendUI.getInfo()+"", Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }
        }
    }


    /*******************************************/
//
//    public void addMapDataSerOrderDetail() {
//        LogUtils.e("addMapDataSerOrderDetail======mAllMapObject===11111===>" + mAllMapObject);
//        LogUtils.e("addMapDataSerOrderDetail======mapObject======>" + mapObject);
//        LogUtils.e("addMapDataSerOrderDetail======mHouseMapObject======>" + mHouseMapObject);
//        if (!UtilMap.isEmpty(mapObject)){
//            mAllMapObject = mapObject;
//        }else if (!UtilMap.isEmpty(mHouseMapObject)){
//            mAllMapObject = mHouseMapObject;
//        }
//        LogUtils.e("addMapDataSerOrderDetail======mAllMapObject===22222===>" + mAllMapObject);
//
//    }

    /**
     * 请求服务器成功后 获取订单数据
     */
    public void onServerOrderDetailOnSuccess(Map<String, Object> allMapObject) {
        mType = UtilNumber.IntegerValueOf(mapObject.get(CommConstans.FRAGSERSTATEORDERCODE.TYPE) + "");//订单类型，1：家政服务，2：其他服务
        mOrderNum = allMapObject.get(CommConstans.FRAGSERSTATEORDERCODE.ORDERNUM) + "";//订单号
        mServiceTitle = allMapObject.get(CommConstans.FRAGSERSTATEORDERCODE.SERVICETITLE) + "";//服务项，如：日常保洁2小时
        mPaytype = UtilNumber.IntegerValueOf(allMapObject.get(CommConstans.FRAGSERSTATEORDERCODE.PAYTYPE) + "");
        mPayState = UtilNumber.IntegerValueOf(allMapObject.get(CommConstans.FRAGSERSTATEORDERCODE.PAYSTATE) + "");
        mDeliverState = UtilNumber.IntegerValueOf(allMapObject.get(CommConstans.FRAGSERSTATEORDERCODE.DELIVERSTATE) + "");
        mOrderState = UtilNumber.IntegerValueOf(allMapObject.get(CommConstans.FRAGSERSTATEORDERCODE.ORDERSTATE) + "");
        mOrderPrice = allMapObject.get(CommConstans.FRAGSERSTATEORDERCODE.AMOUNT) + "";//订单总价
        mStartTime = allMapObject.get(CommConstans.FRAGSERSTATEORDERCODE.STARTDATE) + "";//下单时间

        showSerOrderDetail(allMapObject);//展示家政服务订单详情
    }

    /**
     * 展示家政服务订单详情
     */
    public void showSerOrderDetail(Map<String, Object> allMapObject) {
        LogUtils.e("fragmentServerStateNIU=========mType===11111=====>" + allMapObject.get(CommConstans.FRAGSERSTATEORDERCODE.TYPE) + "");
        /**************订单类型，1：家政服务，2：其他服务*************/
        if (CommConstans.ORDER.HOUSE_ORDER_PAY.equals(AtyFragmentServerDetail.mAtyHouseTpye)) {//1：家政服务
            /************支付方式1：在线支付，2：服务后付款***********/
            if (CommConstans.FRAGSERSTATEORDERPAYTYPE.ON_LINE == mPaytype) {//1：在线支付
                showHouseOnLinePay(allMapObject);
            } else if (CommConstans.FRAGSERSTATEORDERPAYTYPE.AFTER_SERVICE == mPaytype) {//2：服务后付款
                showHouseAfterServerPay();
            }
            /**************订单类型，1：家政服务，2：其他服务*************/
        } else if (CommConstans.ORDER.SERVER_ORDER_PAY.equals(AtyFragmentServerDetail.mAtyHouseTpye)) {//2：其他服务
            LogUtils.e("fragmentServerStateNIU=========mType===22222=====>" + mPaytype + "");
            if (CommConstans.FRAGSERSTATEORDERPAYTYPE.ON_LINE == mPaytype) {//1：在线支付
                LogUtils.e("fragmentServerStateNIU=========mType====33333====>" + mPaytype + "");
                showOtherSerOnLinePay(allMapObject);
            } else if (CommConstans.FRAGSERSTATEORDERPAYTYPE.AFTER_SERVICE == mPaytype) {//2：服务后付款
                LogUtils.e("fragmentServerStateNIU=========mType====kkkk====>" + mPaytype + "");
                showOtherSerAfterServerPay(allMapObject);
            }
        }
        mAdapterServerState.setList(listMap);
        mAdapterServerState.notifyDataSetChanged();
    }

    /**
     * 家政： 在线支付
     */
    public void showHouseOnLinePay(Map<String, Object> allMapObject) {
        /************订单类型为家政服务时：1：正常，2：完成，3：取消,4：拒单，5：删除，6：过期,7：无效 ************/
        if (CommConstans.FRAGSERSTATEORDERSTATE.NORMAL == mOrderState) {//1、正常
            /************支付状态：1：未支付，2：已支付***********/
            if (CommConstans.FRAGSERSTATEORDERPAYSTATE.NO_PAY == mPayState) {//1：未支付
                fillMap(allMapObject);
                showToPayAndCancelMenu();
            } else if (CommConstans.FRAGSERSTATEORDERPAYSTATE.HAD_PAY == mPayState) {//2：已支付
                fillMap(allMapObject);
                /*********派单状态1：店铺未接单，2：店铺已接单，3：店铺已派单，4：商家已确认*********/
                if (CommConstans.FRAGSERSTATEORDERDELIVERSTATE.STORE_NO_ACCEPT == mDeliverState) {//1：店铺未接单
                    Map<String, String> mapStr = new HashMap<>();
                    mapStr.put("name", "待接单");
                    mapStr.put("content", "");
                    mapStr.put("date", "");
                    listMap.add(mapStr);
                    showUrgedMenu();
                } else if (CommConstans.FRAGSERSTATEORDERDELIVERSTATE.STORE_HAD_ACCEPT == mDeliverState) {//2：店铺已接单
                    Map<String, String> mapStr = new HashMap<>();
                    mapStr.put("name", "配单中");
                    mapStr.put("content", "");
                    mapStr.put("date", "");
                    listMap.add(mapStr);
                    showUrgedMenu();
                } else if (CommConstans.FRAGSERSTATEORDERDELIVERSTATE.STORE_HAD_DISPATCH == mDeliverState) {//3：店铺已派单
                    showUrgedMenu();
                } else if (CommConstans.FRAGSERSTATEORDERDELIVERSTATE.PROSER_HAD_ACCEPT == mDeliverState) {//4：商家已确认
                    Map<String, String> mapStr = new HashMap<>();
                    mapStr.put("name", "已接单");
                    mapStr.put("content", "");
                    mapStr.put("date", "");
                    listMap.add(mapStr);
                    showServerDoneMenu();
                }
            }

        } else if (CommConstans.FRAGSERSTATEORDERSTATE.FINISH == mOrderState) {//2：完成
            Map<String, String> mapStr = new HashMap<>();
            mapStr.put("name", "交易完成");
            mapStr.put("content", "");
            mapStr.put("date", "");
            listMap.add(mapStr);
//            showDeleteMenu();
        } else if (CommConstans.FRAGSERSTATEORDERSTATE.CANCEL == mOrderState) {//3：取消
            Map<String, String> mapStr = new HashMap<>();
            mapStr.put("name", "已取消");
            mapStr.put("content", "");
            mapStr.put("date", "");
            listMap.add(mapStr);
//            showDeleteMenu();
        } else if (CommConstans.FRAGSERSTATEORDERSTATE.REJECT == mOrderState) {//4：拒单
            Map<String, String> mapStr = new HashMap<>();
            mapStr.put("name", "配单中");
            mapStr.put("content", "");
            mapStr.put("date", "");
            listMap.add(mapStr);
            showUrgedMenu();
        } else if (CommConstans.FRAGSERSTATEORDERSTATE.DELETE == mOrderState) {//5：删除

        } else if (CommConstans.FRAGSERSTATEORDERSTATE.EXPIRE == mOrderState) {//6：过期

        } else if (CommConstans.FRAGSERSTATEORDERSTATE.INVALID == mOrderState) {//7：无效

        }
    }

    /**
     * 家政： 服务后付款
     */
    public void showHouseAfterServerPay() {
        /************订单类型为家政服务时：1：正常，2：完成，3：取消,4：拒单，5：删除，6：过期,7：无效 ************/
        if (CommConstans.FRAGSERSTATEORDERSTATE.NORMAL == mOrderState) {//1、正常
            /*********派单状态1：店铺未接单，2：店铺已接单，3：店铺已派单，4：商家已确认*********/
            if (CommConstans.FRAGSERSTATEORDERDELIVERSTATE.STORE_NO_ACCEPT == mDeliverState) {//1：店铺未接单
                Map<String, String> mapStr = new HashMap<>();
                mapStr.put("name", "待接单");
                mapStr.put("content", "");
                mapStr.put("date", "");
                listMap.add(mapStr);
                showCancelAndUrgedMenu();
            } else if (CommConstans.FRAGSERSTATEORDERDELIVERSTATE.STORE_HAD_ACCEPT == mDeliverState) {//2：店铺已接单
                Map<String, String> mapStr = new HashMap<>();
                mapStr.put("name", "配单中");
                mapStr.put("content", "");
                mapStr.put("date", "");
                listMap.add(mapStr);
                showCancelAndUrgedMenu();
            } else if (CommConstans.FRAGSERSTATEORDERDELIVERSTATE.STORE_HAD_DISPATCH == mDeliverState) {//3：店铺已派单
                Map<String, String> mapStr = new HashMap<>();
                mapStr.put("name", "配单中");
                mapStr.put("content", "");
                mapStr.put("date", "");
                listMap.add(mapStr);
                showCancelAndUrgedMenu();
            } else if (CommConstans.FRAGSERSTATEORDERDELIVERSTATE.PROSER_HAD_ACCEPT == mDeliverState) {//4：商家已确认
                Map<String, String> mapStr = new HashMap<>();
                mapStr.put("name", "已接单");
                mapStr.put("content", "");
                mapStr.put("date", "");
                listMap.add(mapStr);
                showServerDoneMenu();
            }
        } else if (CommConstans.FRAGSERSTATEORDERSTATE.FINISH == mOrderState) {//2：完成
            Map<String, String> mapStr = new HashMap<>();
            mapStr.put("name", "交易完成");
            mapStr.put("content", "");
            mapStr.put("date", "");
            listMap.add(mapStr);
//            showDeleteMenu();
        } else if (CommConstans.FRAGSERSTATEORDERSTATE.CANCEL == mOrderState) {//3：取消
            Map<String, String> mapStr = new HashMap<>();
            mapStr.put("name", "已取消");
            mapStr.put("content", "");
            mapStr.put("date", "");
            listMap.add(mapStr);
//            showDeleteMenu();
        } else if (CommConstans.FRAGSERSTATEORDERSTATE.REJECT == mOrderState) {//4：拒单
            Map<String, String> mapStr = new HashMap<>();
            mapStr.put("name", "配单中");
            mapStr.put("content", "");
            mapStr.put("date", "");
            listMap.add(mapStr);
            showCancelAndUrgedMenu();
        } else if (CommConstans.FRAGSERSTATEORDERSTATE.DELETE == mOrderState) {//5：删除

        } else if (CommConstans.FRAGSERSTATEORDERSTATE.EXPIRE == mOrderState) {//6：过期

        } else if (CommConstans.FRAGSERSTATEORDERSTATE.INVALID == mOrderState) {//7：无效

        }
    }

    /**
     * 其他服务： 在线支付
     */
    public void showOtherSerOnLinePay(Map<String, Object> allMapObject) {
        /************订单类型为其他服务时：1：正常，2：完成，3：取消,4：删除，5：过期 ************/
        LogUtils.e("fragmentServerStateNIU=========mType=====44444=mOrderState==>" + mOrderState);
        if (CommConstans.FRAGSERSTATESERVERORDERSTATE.NORMAL == mOrderState) {//1、正常
            LogUtils.e("fragmentServerStateNIU=========mType=====44444555555===>" + mOrderState);
            /************支付状态：1：未支付，2：已支付***********/
            if (CommConstans.FRAGSERSTATEORDERPAYSTATE.NO_PAY == mPayState) {//1：未支付
                LogUtils.e("fragmentServerStateNIU=========mType===666666==mPayState===>" + mPayState);
                fillMap(allMapObject);
                Map<String, String> mapStr = new HashMap<>();
                mapStr.put("name", "待付款");
                mapStr.put("content", "");
                mapStr.put("date", "");
                listMap.add(mapStr);
                showToPayAndCancelMenu();
            } else if (CommConstans.FRAGSERSTATEORDERPAYSTATE.HAD_PAY == mPayState) {//2：已支付
                LogUtils.e("fragmentServerStateNIU=========mType===77777=====>" + mPayState);
                fillMap(allMapObject);
                Map<String, String> mapStr1 = new HashMap<>();
                mapStr1.put("name", "支付时间");
                mapStr1.put("content", "");
                mapStr1.put("date", allMapObject.get("PAYDATE")+"");
                listMap.add(mapStr1);
                /*********派单状态1：待派单，2：已派单*********/
                if (CommConstans.FRAGSERSTATEORDERDISPATCHSTATE.NO_DISPATCH == mDeliverState) {//1：待派单
                    Map<String, String> mapStr = new HashMap<>();
                    mapStr.put("name", "待接单");
                    mapStr.put("content", "");
                    mapStr.put("date", "");
                    listMap.add(mapStr);
                    showUrgedMenu();
                } else if (CommConstans.FRAGSERSTATEORDERDISPATCHSTATE.HAD_DISPATCH == mDeliverState) {//2：已派单
                    Map<String, String> mapStr2 = new HashMap<>();
                    mapStr2.put("name", "派单时间");
                    mapStr2.put("content", "");
                    mapStr2.put("date", allMapObject.get("DELIVERDATE")+"");
                    listMap.add(mapStr2);

                    Map<String, String> mapStr = new HashMap<>();
                    mapStr.put("name", "已接单");
                    mapStr.put("content", "");
                    mapStr.put("date", "");
                    listMap.add(mapStr);
                    showServerDoneMenu();
                }
            }
            LogUtils.e("fragmentServerStateNIU=========mType===99999=====>" + mPayState);
        } else if (CommConstans.FRAGSERSTATESERVERORDERSTATE.FINISH == mOrderState) {//2：完成
            Map<String, String> mapStr1 = new HashMap<>();
            mapStr1.put("name", "下单时间");
            mapStr1.put("content", "");
            mapStr1.put("date", allMapObject.get("STARTDATE")+"");
            listMap.add(mapStr1);
            Map<String, String> mapStr = new HashMap<>();
            mapStr.put("name", "交易完成");
            mapStr.put("content", "");
            mapStr.put("date", "");
            listMap.add(mapStr);
//            showDeleteMenu();
        } else if (CommConstans.FRAGSERSTATESERVERORDERSTATE.CANCEL == mOrderState) {//3：取消
            Map<String, String> mapStr1 = new HashMap<>();
            mapStr1.put("name", "下单时间");
            mapStr1.put("content", "");
            mapStr1.put("date", allMapObject.get("STARTDATE")+"");
            listMap.add(mapStr1);
            Map<String, String> mapStr = new HashMap<>();
            mapStr.put("name", "已取消");
            mapStr.put("content", "");
            mapStr.put("date", "");
            listMap.add(mapStr);
//            showDeleteMenu();
        } else if (CommConstans.FRAGSERSTATESERVERORDERSTATE.DELETE == mOrderState) {//4：删除

        } else if (CommConstans.FRAGSERSTATESERVERORDERSTATE.EXPIRE == mOrderState) {//5：过期

        }
    }

    /**
     * 其他服务： 服务后付款
     */
    public void showOtherSerAfterServerPay(Map<String, Object> allMapObject) {
        /************订单类型为家政服务时：1：正常，2：完成，3：取消,4：删除，5：过期 ************/
        LogUtils.e("fragmentServerStateNIU=========mType====kkkk==11111==>" + mOrderState + "");
        if (CommConstans.FRAGSERSTATESERVERORDERSTATE.NORMAL == mOrderState) {//1、正常
            LogUtils.e("fragmentServerStateNIU=========mType====kkkk==正常正常===4444==>" + mDeliverState + "");
            /*********派单状态1：待派单，2：已派单*********/
            if (CommConstans.FRAGSERSTATEORDERDISPATCHSTATE.NO_DISPATCH == mDeliverState) {//1：待派单
                LogUtils.e("fragmentServerStateNIU=========mType====kkkk==4444==>" + mDeliverState + "");
                Map<String, String> mapStr1 = new HashMap<>();
                mapStr1.put("name", "下单时间");
                mapStr1.put("content", "");
                mapStr1.put("date", allMapObject.get("STARTDATE")+"");
                listMap.add(mapStr1);
                Map<String, String> mapStr = new HashMap<>();
                mapStr.put("name", "待接单");
                mapStr.put("content", "");
                mapStr.put("date", "");
                listMap.add(mapStr);
                showCancelAndUrgedMenu();
            } else if (CommConstans.FRAGSERSTATEORDERDISPATCHSTATE.HAD_DISPATCH == mDeliverState) {//2：已派单
                LogUtils.e("fragmentServerStateNIU=========mType====kkkk==55555==>" + mDeliverState + "");
                Map<String, String> mapStr1 = new HashMap<>();
                mapStr1.put("name", "下单时间");
                mapStr1.put("content", "");
                mapStr1.put("date", allMapObject.get("STARTDATE")+"");
                listMap.add(mapStr1);
                Map<String, String> mapStr = new HashMap<>();
                mapStr.put("name", "已接单");
                mapStr.put("content", "");
                mapStr.put("date", "");
                listMap.add(mapStr);
                showServerDoneMenu();
            }
        } else if (CommConstans.FRAGSERSTATESERVERORDERSTATE.FINISH == mOrderState) {//2：完成
            LogUtils.e("fragmentServerStateNIU=========mType====kkkk==2222==>" + mOrderState + "");
            Map<String, String> mapStr1 = new HashMap<>();
            mapStr1.put("name", "下单时间");
            mapStr1.put("content", "");
            mapStr1.put("date", allMapObject.get("STARTDATE")+"");
            listMap.add(mapStr1);
            Map<String, String> mapStr = new HashMap<>();
            mapStr.put("name", "交易完成");
            mapStr.put("content", "");
            mapStr.put("date", "");
            listMap.add(mapStr);
//            showDeleteMenu();
        } else if (CommConstans.FRAGSERSTATESERVERORDERSTATE.CANCEL == mOrderState) {//3：取消
            LogUtils.e("fragmentServerStateNIU=========mType====kkkk==33333==>" + mOrderState + "");
            Map<String, String> mapStr1 = new HashMap<>();
            mapStr1.put("name", "下单时间");
            mapStr1.put("content", "");
            mapStr1.put("date", allMapObject.get("STARTDATE")+"");
            listMap.add(mapStr1);
            Map<String, String> mapStr = new HashMap<>();
            mapStr.put("name", "已取消");
            mapStr.put("content", "");
            mapStr.put("date", "");
            listMap.add(mapStr);
//            showDeleteMenu();
        } else if (CommConstans.FRAGSERSTATESERVERORDERSTATE.DELETE == mOrderState) {//4：删除

        } else {//
            LogUtils.e("fragmentServerStateNIU=========mType====kkkk==33333==>" + mOrderState + "");
            Map<String, String> mapStr1 = new HashMap<>();
            mapStr1.put("name", "下单时间");
            mapStr1.put("content", "");
            mapStr1.put("date", allMapObject.get("STARTDATE")+"");
            listMap.add(mapStr1);
            Map<String, String> mapStr = new HashMap<>();
            mapStr.put("name", "已完成");
            mapStr.put("content", "");
            mapStr.put("date", "");
            listMap.add(mapStr);
        }
    }


    /******************************************************/


    /**
     * 请求服务器后催单成功
     */
    public void urgeSuccess() {
        UtilToast.show(mContext, "催单成功！", Toast.LENGTH_SHORT);
        LogUtils.e("OnSuccess==========mOrderNum==cancelOrder==========>" + mOrderNum);
        mSimDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mSysThisTime = mSimDateTime.format(new Date());//催单成功获取一次时间
                /******此处注释需要处理******/
//        mBllServer.getOrderDetail(AtyFragmentOrdertDetail_bak.mOrderCode);
        listMap.clear();
        mListView.setAdapter(mAdapterServerState);
        mAdapterServerState.notifyDataSetChanged();
    }

    /**
     * 显示去支付、取消订单及监听事件
     */
    public void showToPayAndCancelMenu() {
        mRlytBottom.setVisibility(View.VISIBLE);
        mTvToPay.setVisibility(View.VISIBLE);
        mTvCancel.setVisibility(View.VISIBLE);
        mTvConfirm.setVisibility(View.GONE);
        mTvUrged.setVisibility(View.GONE);
        mTvDelet.setVisibility(View.GONE);
        mBtnAgain.setVisibility(View.GONE);
        mTvToPay.setOnClickListener(onOrderStateClick);
        mTvCancel.setOnClickListener(onOrderStateClick);
    }

    /**
     * 显示催单按钮及监听事件
     */
    public void showUrgedMenu() {
        mRlytBottom.setVisibility(View.VISIBLE);
        mTvCancel.setVisibility(View.GONE);
        mTvToPay.setVisibility(View.GONE);
        mTvConfirm.setVisibility(View.GONE);
        mTvUrged.setVisibility(View.VISIBLE);
        mTvDelet.setVisibility(View.GONE);
        mBtnAgain.setVisibility(View.GONE);
        mTvUrged.setOnClickListener(onOrderStateClick);
    }

    /**
     * 显示服务完成及监听事件
     */
    public void showServerDoneMenu() {
        mRlytBottom.setVisibility(View.VISIBLE);
        mTvCancel.setVisibility(View.GONE);
        mTvToPay.setVisibility(View.GONE);
        mTvConfirm.setVisibility(View.VISIBLE);
        mTvUrged.setVisibility(View.GONE);
        mTvDelet.setVisibility(View.GONE);
        mBtnAgain.setVisibility(View.GONE);
        mTvConfirm.setOnClickListener(onOrderStateClick);
    }

    /**
     * 显示服务完成及监听事件
     */
    public void showDeleteMenu() {
        mRlytBottom.setVisibility(View.VISIBLE);
        mTvCancel.setVisibility(View.GONE);
        mTvToPay.setVisibility(View.GONE);
        mTvConfirm.setVisibility(View.GONE);
        mTvUrged.setVisibility(View.GONE);
        mTvDelet.setVisibility(View.VISIBLE);
        mBtnAgain.setVisibility(View.GONE);
        mTvDelet.setOnClickListener(onOrderStateClick);
    }

    /**
     * 显示服务完成及监听事件
     */
    public void showCancelAndUrgedMenu() {
        mRlytBottom.setVisibility(View.VISIBLE);
        mTvCancel.setVisibility(View.VISIBLE);
        mTvToPay.setVisibility(View.GONE);
        mTvConfirm.setVisibility(View.GONE);
        mTvUrged.setVisibility(View.VISIBLE);
        mTvDelet.setVisibility(View.GONE);
        mBtnAgain.setVisibility(View.GONE);
        mTvCancel.setOnClickListener(onOrderStateClick);
        mTvUrged.setOnClickListener(onOrderStateClick);
    }

    /**
     * 订单完成时
     * 显示再来一单按钮及监听事件
     */
    public void showAgainMenu() {
        mRlytBottom.setVisibility(View.VISIBLE);
        mLytBtnBottom.setVisibility(View.GONE);
        mBtnAgain.setVisibility(View.VISIBLE);
        mBtnAgain.setOnClickListener(onOrderStateClick);
    }

    /**
     * 初始化时隐藏所有按钮
     */
    public void hideAllBottomMenu() {
        mRlytBottom.setVisibility(View.GONE);
        mTvCancel.setVisibility(View.GONE);
        mTvToPay.setVisibility(View.GONE);
        mTvConfirm.setVisibility(View.GONE);
        mTvUrged.setVisibility(View.GONE);
        mTvDelet.setVisibility(View.GONE);
        mBtnAgain.setVisibility(View.GONE);
    }

    //订单四个状态的操作监听事件
    private View.OnClickListener onOrderStateClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvBtn_cancel_order://取消订单
                    cancelOrderDialog();
                    break;
                case R.id.tvBtn_topay://去支付
                    toPayOrder();
                    break;
                case R.id.tvBtn_take://交易完成
                    confirmOrder();
                    break;
                case R.id.tvBtn_urged://催单
                    isCanUrged();
                    break;
                case R.id.tvBtn_delete://删除订单
                    deleteOrder();
                    break;
                case R.id.btn_again://再来一单
//                    buyAgain();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * //获取系统当前时间、判断催单时间
     */
    public void isCanUrged() {
        mSimDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//格式
        mSysThisTime = mSimDateTime.format(new Date());//当前系统时间
        LogUtils.e("isCanUrged=============mSysThisTime===========>" + mSysThisTime);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            sysThisDate = dateFormat.parse(mSysThisTime);//系统当前时间
            mDateStartDate = dateFormat.parse(mStartTime);//下单时间
            startDifferTime = sysThisDate.getTime() - mDateStartDate.getTime();//下单时间到当前时间差
            startMinutes = startDifferTime / (1000 * 60);


            LogUtils.e("isCanUrged=============sysThisDate===========>" + sysThisDate);
            LogUtils.e("isCanUrged=============payDifferTime===========>" + startDifferTime);
            LogUtils.e("isCanUrged=============payMinutes===========>" + startMinutes);
            LogUtils.e("isCanUrged=============mLastUrgeTime===========>" + mLastUrgeTime);
            LogUtils.e("isCanUrged=============mUrgeNums===========>" + mUrgeNums);

            if (startMinutes < 15) {
                UtilToast.show(mContext, "请订单提交15分钟后再催单！", Toast.LENGTH_SHORT);
            } else if (mUrgeNums == 0 && startMinutes > 15) {
                urgedOrderDialog();//催单请求一次
            } else if (mUrgeNums > 0 && startMinutes > 15 && mLastUrgeTime != null) {
                lastDate = dateFormat.parse(mLastUrgeTime);//最后一次催单时间
                UrgeDifferTime = sysThisDate.getTime() - lastDate.getTime();//最后一次催单到当前时间差
                urgedMinutes = UrgeDifferTime / (1000 * 60);
                LogUtils.e("isCanUrged=============lastDate=====lastDate======>" + lastDate);
                LogUtils.e("isCanUrged=============UrgeDifferTime=====UrgeDifferTime======>" + UrgeDifferTime);
                LogUtils.e("isCanUrged=============urgedMinutes=====urgedMinutes======>" + urgedMinutes);
                if (startMinutes > 15 && urgedMinutes > 5) {
                    urgedOrderDialog();
                } else {
                    UtilToast.show(mContext, "催单成功，送货员正飞奔在路上！", Toast.LENGTH_SHORT);
                }
            } else {
                UtilToast.show(mContext, "催单太频繁，休息一下吧！", Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            LogUtils.e("时间解析==========================>" + e);
        }
    }

    /**
     * 再来一单
     */
//    public void buyAgain() {
//        Intent intent = new Intent(FragmentServerState.mContext, AtySettlementAgain.class);
//        intent.putExtra(TAG, "FragmentOrder");
//        intent.putExtra(ORDERINFO, mOrderInfo);
//        mContext.startActivity(intent);
//    }

    /**
     * 删除订单
     */
    public void deleteOrder() {
        final WgtAlertDialog mAtDialog = new WgtAlertDialog();
        mAtDialog.show(mContext, "取消", "确定", "确认删除当前订单？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAtDialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOrderNum != null) {
                    if (CommConstans.FRAGSERSTATEORDERTYPE.HOUSESER == mType) {//1：家政服务
                        mBllServer.getSerOrderDelete(mOrderNum);//家政订单删除
                    } else if (CommConstans.FRAGSERSTATEORDERTYPE.SERVER == mType) {
                        mBllServer.getOrderDelete(mOrderNum);//其他服务订单删除
                    }
                } else {
                    UtilToast.show(mContext, "订单删除失败，请从新尝试！", Toast.LENGTH_SHORT);
                }
                mAtDialog.dismiss();
            }
        });
    }

    /**
     * 确认收货
     */
    public void confirmOrder() {
        final WgtAlertDialog mAtDialog = new WgtAlertDialog();
        mAtDialog.show(mContext, "取消", "确定", "确认收货？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAtDialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOrderNum != null) {
                    mBllServer.getOrderSign(mOrderNum);//提交服务器请求
                } else {
                    UtilToast.show(mContext, "确认收货失败，请从新尝试！", Toast.LENGTH_SHORT);
                }
                mAtDialog.dismiss();
            }
        });
    }



    /**
     * 去支付
     */
    public void toPayOrder() {
        Intent intent = new Intent(FragmentServerState.mContext, AtySettlementOrder.class);
        intent.putExtra(AtySettlementOrder.ORDER_PAY_TYPE, AtyFragmentServerDetail.mAtyHouseTpye);
        intent.putExtra(AtySettlementOrder.ORDER_CODE, mOrderNum);
        intent.putExtra(AtySettlementOrder.ORDER_PRICE, mOrderPrice);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    /**
     * 取消订单提示
     */
    public void cancelOrderDialog() {
        final WgtAlertDialog mAtDialog = new WgtAlertDialog();
        mAtDialog.show(mContext, "取消", "确定", "确认取消当前订单？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAtDialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommConstans.ORDER.SERVER_ORDER_PAY.equals(AtyFragmentServerDetail.mAtyHouseTpye)) {
                    mBllServer.getOrderCancel(mOrderNum);//点击确定，提交服务器请求取消订单
                } else {
                    if (mHouseMapObject != null) {
                        mHouseOrderNum = mHouseMapObject.get("ORDERNUM") + "";
                        mBllServer.getSerOrderCancel(mHouseOrderNum, "无");//点击确定，提交服务器请求取消服务家政订单
                    }else {
                    }
                }
                mAtDialog.dismiss();
            }
        });
    }

    /**
     * 催单
     */
    public void urgedOrderDialog() {
        final WgtAlertDialog mAtDialog = new WgtAlertDialog();
        mAtDialog.show(mContext, "取消", "确定", "确认催单？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAtDialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommConstans.ORDER.SERVER_ORDER_PAY.equals(AtyFragmentServerDetail.mAtyHouseTpye)) {//服务
                    mBllServer.getOrderUrge(mOrderNum);//点击确定，提交服务器请求取消订单
                } else {
                    if (mHouseMapObject != null) {
                        mHouseOrderNum = mHouseMapObject.get("ORDERNUM") + "";
                        mBllServer.getSerOrderUrge(mHouseOrderNum);//点击确定，提交服务器请求取消服务家政订单
                    }else {
                    }
                }
                mAtDialog.dismiss();
            }
        });
    }


    /**
     * 填充待支付之前数据
     *
     * @param mapObject
     */
    private void fillMap(Map<String, Object> mapObject) {
        Map<String, String> mapStr1 = new HashMap<>();
        mapStr1.put("name", "下单时间");
        mapStr1.put("content", "");
        mapStr1.put("date", mapObject.get("STARTDATE")+"");
        listMap.add(mapStr1);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}