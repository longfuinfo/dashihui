package com.dashihui.afford.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.TypeReference;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessOrder;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.thirdapi.FastJSONHelper;
import com.dashihui.afford.ui.adapter.AdapterOrderDetail;
import com.dashihui.afford.ui.adapter.AdapterOrderDetailGoods;
import com.dashihui.afford.ui.widget.WgtAlertDialog;
import com.dashihui.afford.util.map.UtilMap;
import com.dashihui.afford.util.number.UtilNumber;
import com.dashihui.afford.util.string.UtilString;
import com.dashihui.afford.util.toast.UtilToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtyOrdertDetail extends BaseActivity {
    @ViewInject(R.id.goods_price)
    private TextView mTotalPrice;//总计金额
    @ViewInject(R.id.goods_shihubi_price)
    private TextView mShihuibiPrice;//抵用实惠币金额
    @ViewInject(R.id.goods_tatal_price)
    private TextView mGoodsPrice;//实付款
    @ViewInject(R.id.orderNums)
    private TextView mOrderNums;//订单号


    @ViewInject(R.id.username)
    private TextView mUsername;//用户姓名
    @ViewInject(R.id.usersex)
    private TextView mUsersex;//性别
    @ViewInject(R.id.userphone)
    private TextView mUserphone;//电话号码
    @ViewInject(R.id.useraddr)
    private TextView mUseraddr;//收货地址


    @ViewInject(R.id.title)
    private TextView mTitle;//名称
    @ViewInject(R.id.ibtnBack)
    private ImageButton mImgbtnBack;//返回

    @ViewInject(R.id.listViewGoods)
    private ListView mListViewGoods;
    @ViewInject(R.id.listView)
    private ListView mListView;

    /*******************
     * 不同状态下按钮
     *****************/
    @ViewInject(R.id.orderState)
    private Button mOrderStateTxt;//状态
    @ViewInject(R.id.all_order_delete)
    private Button mDeleteBtn;//删除订单
    @ViewInject(R.id.btn_cancel)
    private Button mCancelBtn;//取消订单

    @ViewInject(R.id.all_order_topay)
    private Button mPayBtn;//去支付
    @ViewInject(R.id.tv_btn_urged)
    private Button mUrged;//催单
    @ViewInject(R.id.tv_btn_notake)
    private Button mBtnSubmit;//确认收货
    @ViewInject(R.id.tv_btn_getgood)
    private Button mBtnSubmitGoodBtn;//确认取货
    @ViewInject(R.id.all_order_track)
    private Button mOrderTrackBtn;//订单跟踪
    @ViewInject(R.id.btnOrderEvaluate)
    private Button mBtnEvaluate;//去评价
    @ViewInject(R.id.txt_countdown)
    private TextView mTxtCountDown;//倒计时
    @ViewInject(R.id.ord_txt_time)
    private TextView mTxtTime;

    @ViewInject(R.id.layoutTime)
    private LinearLayout mlayoutTime;

    @ViewInject(R.id.supermarket_name)
    private TextView mTxtStoreName;//店铺名称

    private BusinessOrder mBllOrder;
    //状态
    private List<Map<String, String>> listMap = new ArrayList<>();
    //列表适配器
    private AdapterOrderDetail mAdapterDetail;//订单详情
    private AdapterOrderDetailGoods mAdapterGoods;
    /*******************
     * 订单状态代码
     *****************/
    private int mOrderState, mOrderPayType, mOrderPayMethod, mOrderPayState, mOrderTakeType, mOrderDeliverState, mPackState;
    private String mOrderNum;//订单号
    private List<Map<String, Object>> listMapGoods;
    private  String mOrderCode="";
    private  String mOrderPrice="";
    public final static String TABCODE = "tabCode";

    Long countDowntime;
    CountDownTimer timer;

    public final static int ORDEREVALUATE = 1001;//去评价 = "tabCode";
    private Map<String, Object>  mMapDetail;
//    private String isSelf;

    //    private CountDownTimer timer = new CountDownTimer(countDowntime,1000) {
//        @Override
//        public void onTick(long millisUntilFinished) {
//            SimpleDateFormat format = new SimpleDateFormat("mm分钟ss秒");
//            String dateString = format.format(millisUntilFinished);
//
//        }
//
//        @Override
//        public void onFinish() {
//
//        }
//    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_orderdetail);
        ViewUtils.inject(this);
        //标题命名
        mTitle.setText(R.string.order_detail);
        //请求列表数据
        mBllOrder = new BusinessOrder(this);
        listMapGoods = new ArrayList<>();
        mAdapterGoods = new AdapterOrderDetailGoods(this, listMapGoods);
        mListViewGoods.setAdapter(mAdapterGoods);
        mAdapterDetail = new AdapterOrderDetail(this, listMap);
        mListView.setAdapter(mAdapterDetail);

        mOrderCode = getIntent().getStringExtra(AtySettlementOrder.ORDER_CODE);
        mOrderPrice = getIntent().getStringExtra(AtySettlementOrder.ORDER_PRICE);
//        isSelf = getIntent().getStringExtra(AtySettlementOrder.ORDER_SELF);

        if (!"".equals(mOrderCode)) {
            mBllOrder.getOrderDetail(mOrderCode);
        }
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_ORDER_DETAIL://订单详情
                    Map<String,Object>  mapObject = (Map<String, Object>) beanSendUI.getInfo();
                    mMapDetail = mapObject;
                    listMapGoods.clear();
                    listMap.clear();
                    onSuccessGetData(mapObject);
                    mAdapterGoods.setList(listMapGoods);
                    mListViewGoods.setAdapter(mAdapterGoods);
                    setlistViewHeigh(mListViewGoods);
                    //根据订单列表分类，加载不同的adapter
                    mAdapterDetail.setList(listMap);

                    setlistViewHeigh(mListView);
                    mAdapterGoods.notifyDataSetChanged();
                    mAdapterDetail.notifyDataSetChanged();
                    break;
                case AffConstans.BUSINESS.TAG_ORDER_DOCANCELORDER:
                    UtilToast.show(this, "订单取消成功", Toast.LENGTH_SHORT);
                    mlayoutTime.setVisibility(View.GONE);
                    mTxtTime.setVisibility(View.GONE);
                    mTxtCountDown.setVisibility(View.GONE);
                    if (!"".equals(mOrderCode)) {
                        mBllOrder.getOrderDetail(mOrderCode);
                    }
                    break;
                case AffConstans.BUSINESS.TAG_ORDER_DELETE://删除订单
                    UtilToast.show(this, "订单已删除！", Toast.LENGTH_SHORT);
                    finish();
                    break;
                case AffConstans.BUSINESS.TAG_ORDER_DORECEIVEORDER://确认收货成功后
                    UtilToast.show(this, "非常感谢，欢迎再次订购！", Toast.LENGTH_SHORT);
                    Intent intent = new Intent(this,AtyOrderComplete.class);
                    intent.putExtra(AtySettlementOrder.ORDER_CODE,mMapDetail.get("ORDERNUM") + "");
                    intent.putExtra(AtySettlementOrder.ORDER_PRICE, mMapDetail.get("AMOUNT") + "");
                    startActivity(intent);
                    finish();
                    break;
                case AffConstans.BUSINESS.TAG_ORDER_URGE:
                    UtilToast.show(this, "催单成功！", Toast.LENGTH_SHORT);
//                    mSimDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    mThisUrgeTime = mSimDateTime.format(new Date());//催单成功获取一次时间
//                    mBllOrder.getOrderDetail(AtyFragmentOrdertDetail_bak.mOrderCode);
//                    listMap.clear();
//                    mListView.setAdapter(mAdapterDetail);
//                    mAdapterDetail.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        } else {
            LogUtils.e("onSuccess=======error====>" + beanSendUI);
        }

    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_ORDER_DOCANCELORDER:
                case AffConstans.BUSINESS.TAG_ORDER_DORECEIVEORDER:
                case AffConstans.BUSINESS.TAG_ORDER_URGE:
                    UtilToast.show(this, beanSendUI.getInfo() + "", Toast.LENGTH_SHORT);
                    LogUtils.e("onFailure==========mOrderNum==cancelOrder==========>" );
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ORDEREVALUATE://去评价
                if (data != null ) {
                    Bundle _remBunlde = data.getExtras();
                    String remText = _remBunlde.getString(AtySettlementOrder.ORDER_CODE);
                    if (!"".equals(remText)) {
                        mBllOrder.getOrderDetail(remText);
                    }
                } else {
                    LogUtils.e("onActivityResult======================>"+data);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.ibtnBack)
    public void baceOnclick(View view){
        onBackPressed();
    }

    /**
     * 催单
     */
    @OnClick(R.id.tv_btn_urged)
    public void onUrgedClick(View view) {
        final WgtAlertDialog mAtDialog = new WgtAlertDialog();
        mAtDialog.show(this,
                "取消", "确定",
                "确认催单？",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAtDialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!UtilString.isEmpty(mOrderCode)) {
                            mBllOrder.urge(mOrderCode + "");//提交服务器请求
                        } else {
                            UtilToast.show(AtyOrdertDetail.this, "催单失败！", Toast.LENGTH_SHORT);
                        }
                        mAtDialog.dismiss();
                    }
                });
    }
    /**
     * 删除订单
     */
    @OnClick(R.id.all_order_delete)
    public void onDeleteClick(View view) {
        final WgtAlertDialog mAtDialog = new WgtAlertDialog();
        mAtDialog.show(this,
                "取消", "确定",
                "确认删除订单？",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAtDialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!UtilString.isEmpty(mOrderCode)) {
                            mBllOrder.doDeleteOrder(mOrderCode + "");//提交服务器请求
                        } else {
                            UtilToast.show(AtyOrdertDetail.this, "删除订单失败！", Toast.LENGTH_SHORT);
                        }
                        mAtDialog.dismiss();
                    }
                });
    }

    /**
     * 取消订单提示
     */
    @OnClick(R.id.btn_cancel)
    public void onBtnCancelClick(View v) {
        final WgtAlertDialog mAtDialog = new WgtAlertDialog();
        String showStr = "";
        LogUtils.e("onBtnCancelClick===========mOrderPayType=============>"+mOrderPayType);
        if(mOrderPayState == CommConstans.SHOPORDERPAYSTATE.HADPAY){
            showStr ="取消订单后，您的退款进度请进入"+"\n"+"    “我的-我的订单-退款”查看";
        }else {
            showStr ="取消订单后，您的优惠也将一并取消，是否仍继续？";

        }
        mAtDialog.show(this, "取消", "确定", showStr, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAtDialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UtilString.isEmpty(mOrderCode)) {
                    mBllOrder.doCancelOrder(mOrderCode + "");//点击确定，提交服务器请求取消订单
                } else {
                    UtilToast.show(AtyOrdertDetail.this, "取消订单失败", Toast.LENGTH_SHORT);
                }
                mAtDialog.dismiss();
            }
        });
    }
    @OnClick(R.id.all_order_topay)//去支付
    public void onPriceClick(View v){
        Intent mapIntent = new Intent(this, AtySettlementOrder.class);
        mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, mOrderPrice + "");
        mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, mOrderCode + "");
        mapIntent.putExtra(AtySettlementOrder.ORDER_PAY_TYPE, CommConstans.ORDER.ORDER_PAY);
        startActivity(mapIntent);
        finish();
    }
    @OnClick(R.id.tv_btn_notake)//确认收货
    public void onNotakeClick(View v) {
        cofirmOrder();
    }

    @OnClick(R.id.tv_btn_getgood)//确认取货
    public void onGetGoodClick(View v) {
        cofirmOrder();
    }

    @OnClick(R.id.btnOrderEvaluate)//去评价
    public void onEvaluateClick(View v) {
        if (!UtilString.isEmpty(mOrderCode)){
            Intent mapIntent = new Intent(this, AtyEvaluate.class);
            mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, mOrderPrice + "");
            mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, mOrderCode + "");
            startActivity(mapIntent);
            finish();
        }else {
            LogUtils.e("OrderTrack=======else====订单跟踪=============>");
        }

    }


    @OnClick(R.id.all_order_track)//订单跟踪
    public void onOrderTrackClick(View v) {
        //订单跟踪
        if (!UtilString.isEmpty(mOrderCode)){
            Intent mapIntent = new Intent(this, AtyOrderState.class);
            //支付方式 1：在线支付，2：货到付款
            mapIntent.putExtra(AtyOrderState.PAYTYPE, mMapDetail.get("PAYTYPE") + "");
            //收货方式  1：送货，2：自取
            mapIntent.putExtra(AtyOrderState.TAKETYPE, mMapDetail.get("TAKETYPE") + "");
            mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, mOrderPrice + "");
            mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, mOrderCode + "");
            startActivity(mapIntent);
        }else {
            LogUtils.e("OrderTrack=======else====订单跟踪=============>");
        }


    }
    @OnClick(R.id.orderState)//订单状态
    public void onOrderStateClick(View v){
        Intent intent = new Intent(this,AtyOrderState.class);
        //支付方式 1：在线支付 ，2：货到付款
        intent.putExtra(AtyOrderState.PAYTYPE,mMapDetail.get("PAYTYPE") + "");
        //收货方式 1：送货，2：自取
        intent.putExtra(AtyOrderState.TAKETYPE,mMapDetail.get("TAKETYPE") + "");
        intent.putExtra(AtySettlementOrder.ORDER_PRICE,mOrderPrice + "");
        intent.putExtra(AtySettlementOrder.ORDER_CODE,mOrderCode + "");
        startActivity(intent);
    }
    /**
     * 确认收货
     */
    public void cofirmOrder() {
        final WgtAlertDialog mAtDialog = new WgtAlertDialog();
        mAtDialog.show(this,
                "取消", "确定",
                "确认收货？",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAtDialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!UtilString.isEmpty(mOrderCode)) {
                            mBllOrder.doReceiveOrder(mOrderCode + "");//提交服务器请求
                        } else {
                            UtilToast.show(AtyOrdertDetail.this, "确认收货失败，请从新尝试！", Toast.LENGTH_SHORT);
                        }
                        mAtDialog.dismiss();
                    }
                });
    }



    /**
     * 第一次刷新
     */
    public void refreshBtn(Map<String, Object>  mapObject) {
        if (!UtilMap.isEmpty(mapObject)){
            //订单状态
            LogUtils.e("refresh=============订单状态==============>" + mOrderState);
            //订单状态，1:正常，2：已完成，3：取消，4：删除，5：过期
            if (mOrderState == CommConstans.SHOPORDERSTATE.NORMAL){
                LogUtils.e("refresh=============订单状态========111======>" + mOrderState);
                /*************************正常订单状态  开始***************************************************************
                 * * ****************************************************************************************************/
                //根据支付方式、支付状态判断要显示的按钮
                /**************************************
                 * *********在线付款+送货上门************
                 * ***********************************/
                if (mOrderPayType == CommConstans.SHOPORDERPAYTYPE.ONLINE && mOrderTakeType == CommConstans.SHOPORDERTAKETYPE.DELIVER){
//                    //支付状态
                    if (mOrderPayState == CommConstans.SHOPORDERPAYSTATE.NOPAY) {//支付状态为 未支付
                        mDeleteBtn.setVisibility(View.GONE);//删除订单
                        mCancelBtn.setVisibility(View.VISIBLE);//取消订单
                        mPayBtn.setVisibility(View.VISIBLE);//去支付
                        mUrged.setVisibility(View.GONE);//催单
                        mBtnSubmit.setVisibility(View.GONE);//确认收货
                        mBtnSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                        mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                        mBtnEvaluate.setVisibility(View.GONE);//去评价
                        mTxtCountDown.setVisibility(View.VISIBLE);//倒计时
                        mOrderStateTxt.setText("待付款");

                    }else if(mOrderPayState == CommConstans.SHOPORDERPAYSTATE.HADPAY){//订单正常  支付状态为 已支付

                        //打包状态1：未接单，2：已接单，3：打包中，4：打包完成
                        if (mPackState == CommConstans.ORDERPACKSTATE.NO_ACCEPT){
                            mDeleteBtn.setVisibility(View.GONE);//删除订单
                            mCancelBtn.setVisibility(View.VISIBLE);//取消订单
                            mPayBtn.setVisibility(View.GONE);//去支付
                            mUrged.setVisibility(View.VISIBLE);//催单
                            mBtnSubmit.setVisibility(View.GONE);//确认收货
                            mBtnSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                            mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                            mBtnEvaluate.setVisibility(View.GONE);//去评价
                            mTxtCountDown.setVisibility(View.GONE);//倒计时
                            mOrderStateTxt.setText("付款成功");
                        }else if(mPackState == CommConstans.ORDERPACKSTATE.HAD_ACCEPT){
                            mDeleteBtn.setVisibility(View.GONE);//删除订单
                            mCancelBtn.setVisibility(View.GONE);//取消订单
                            mPayBtn.setVisibility(View.GONE);//去支付
                            mUrged.setVisibility(View.VISIBLE);//催单
                            mBtnSubmit.setVisibility(View.GONE);//确认收货
                            mBtnSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                            mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                            mBtnEvaluate.setVisibility(View.GONE);//去评价
                            mTxtCountDown.setVisibility(View.GONE);//倒计时
                            mOrderStateTxt.setText("打包中");
                        }else if(mPackState == CommConstans.ORDERPACKSTATE.PACKING){
                            mDeleteBtn.setVisibility(View.GONE);//删除订单
                            mCancelBtn.setVisibility(View.GONE);//取消订单
                            mPayBtn.setVisibility(View.GONE);//去支付
                            mUrged.setVisibility(View.VISIBLE);//催单
                            mBtnSubmit.setVisibility(View.GONE);//确认收货
                            mBtnSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                            mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                            mBtnEvaluate.setVisibility(View.GONE);//去评价
                            mTxtCountDown.setVisibility(View.GONE);//倒计时
                            mOrderStateTxt.setText("打包中");
                        }else {
                            //收货状态 1：待发货，2：已发货
                            if (mOrderDeliverState == CommConstans.SHOPORDERTAKETYPE.DELIVER){//待收货
                                mDeleteBtn.setVisibility(View.GONE);//删除订单
                                mCancelBtn.setVisibility(View.GONE);//取消订单
                                mPayBtn.setVisibility(View.GONE);//去支付
                                mUrged.setVisibility(View.VISIBLE);//催单
                                mBtnSubmit.setVisibility(View.GONE);//确认收货
                                mBtnSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                                mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                                mBtnEvaluate.setVisibility(View.GONE);//去评价
                                mTxtCountDown.setVisibility(View.GONE);//倒计时
                                mOrderStateTxt.setText("待配送");
                            }else if(mOrderDeliverState == CommConstans.SHOPORDERTAKETYPE.TAKESELF){
                                mDeleteBtn.setVisibility(View.GONE);//删除订单
                                mCancelBtn.setVisibility(View.GONE);//取消订单
                                mPayBtn.setVisibility(View.GONE);//去支付
                                mUrged.setVisibility(View.GONE);//催单
                                mBtnSubmit.setVisibility(View.VISIBLE);//确认收货
                                mBtnSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                                mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                                mBtnEvaluate.setVisibility(View.GONE);//去评价
                                mTxtCountDown.setVisibility(View.GONE);//倒计时
                                mOrderStateTxt.setText("配送中");
                            }
                        }


                    }
                    /**************************************
                     * *********在线付款+门店自取************
                     * ***********************************/
                }else if (mOrderPayType == CommConstans.SHOPORDERPAYTYPE.ONLINE && mOrderTakeType == CommConstans.SHOPORDERTAKETYPE.TAKESELF){
                    //支付状态
//                    int payState = UtilNumber.IntegerValueOf(mapObject.get("PAYSTATE") + "");
                    if (mOrderPayState == CommConstans.SHOPORDERPAYSTATE.NOPAY) {//支付状态为 未支付
                        mDeleteBtn.setVisibility(View.GONE);//删除订单
                        mCancelBtn.setVisibility(View.VISIBLE);//取消订单
                        mPayBtn.setVisibility(View.VISIBLE);//去支付
                        mUrged.setVisibility(View.GONE);//催单
                        mBtnSubmit.setVisibility(View.GONE);//确认收货
                        mBtnSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                        mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                        mBtnEvaluate.setVisibility(View.GONE);//去评价
                        mTxtCountDown.setVisibility(View.VISIBLE);//倒计时
                        mOrderStateTxt.setText("待付款");

                    }else if(mOrderPayState == CommConstans.SHOPORDERPAYSTATE.HADPAY){//订单正常  支付状态为 已支付
                        //打包状态1：未接单，2：已接单，3：打包中，4：打包完成
                        if (mPackState == CommConstans.ORDERPACKSTATE.NO_ACCEPT){
                            mDeleteBtn.setVisibility(View.GONE);//删除订单
                            mCancelBtn.setVisibility(View.VISIBLE);//取消订单
                            mPayBtn.setVisibility(View.GONE);//去支付
                            mUrged.setVisibility(View.GONE);//催单
                            mBtnSubmit.setVisibility(View.GONE);//确认收货
                            mBtnSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                            mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                            mBtnEvaluate.setVisibility(View.GONE);//去评价
                            mTxtCountDown.setVisibility(View.GONE);//倒计时
                            mOrderStateTxt.setText("付款成功");
                        }else if(mPackState == CommConstans.ORDERPACKSTATE.HAD_ACCEPT){
                            mDeleteBtn.setVisibility(View.GONE);//删除订单
                            mCancelBtn.setVisibility(View.GONE);//取消订单
                            mPayBtn.setVisibility(View.GONE);//去支付
                            mUrged.setVisibility(View.GONE);//催单
                            mBtnSubmit.setVisibility(View.GONE);//确认收货
                            mBtnSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                            mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                            mBtnEvaluate.setVisibility(View.GONE);//去评价
                            mTxtCountDown.setVisibility(View.GONE);//倒计时
                            mOrderStateTxt.setText("打包中");
                        }else if(mPackState == CommConstans.ORDERPACKSTATE.PACKING){
                            mDeleteBtn.setVisibility(View.GONE);//删除订单
                            mCancelBtn.setVisibility(View.GONE);//取消订单
                            mPayBtn.setVisibility(View.GONE);//去支付
                            mUrged.setVisibility(View.GONE);//催单
                            mBtnSubmit.setVisibility(View.GONE);//确认收货
                            mBtnSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                            mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                            mBtnEvaluate.setVisibility(View.GONE);//去评价
                            mTxtCountDown.setVisibility(View.GONE);//倒计时
                            mOrderStateTxt.setText("打包中");
                        }else {
                            mDeleteBtn.setVisibility(View.GONE);//删除订单
                            mCancelBtn.setVisibility(View.GONE);//取消订单
                            mPayBtn.setVisibility(View.GONE);//去支付
                            mUrged.setVisibility(View.GONE);//催单
                            mBtnSubmit.setVisibility(View.GONE);//确认收货
                            mBtnSubmitGoodBtn.setVisibility(View.VISIBLE);//确认取货
                            mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                            mBtnEvaluate.setVisibility(View.GONE);//去评价
                            mTxtCountDown.setVisibility(View.GONE);//倒计时
                            mOrderStateTxt.setText("待取货");
                        }
                    }
                    /**************************************
                     * *********货到付款+送货上门************
                     * ***********************************/
                }else if(mOrderPayType == CommConstans.SHOPORDERPAYTYPE.ONDELIVERY && mOrderTakeType == CommConstans.SHOPORDERTAKETYPE.DELIVER){
                    //货到付款+送货上门
                    //打包状态1：未接单，2：已接单，3：打包中，4：打包完成
                    if (mPackState == CommConstans.ORDERPACKSTATE.NO_ACCEPT){
                        mDeleteBtn.setVisibility(View.GONE);//删除订单
                        mCancelBtn.setVisibility(View.VISIBLE);//取消订单
                        mPayBtn.setVisibility(View.GONE);//去支付
                        mUrged.setVisibility(View.VISIBLE);//催单
                        mBtnSubmit.setVisibility(View.GONE);//确认收货
                        mBtnSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                        mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                        mBtnEvaluate.setVisibility(View.GONE);//去评价
                        mTxtCountDown.setVisibility(View.GONE);//倒计时
                        mOrderStateTxt.setText("处理中");
                    }else if(mPackState == CommConstans.ORDERPACKSTATE.HAD_ACCEPT){
                        mDeleteBtn.setVisibility(View.GONE);//删除订单
                        mCancelBtn.setVisibility(View.VISIBLE);//取消订单
                        mPayBtn.setVisibility(View.GONE);//去支付
                        mUrged.setVisibility(View.VISIBLE);//催单
                        mBtnSubmit.setVisibility(View.GONE);//确认收货
                        mBtnSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                        mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                        mBtnEvaluate.setVisibility(View.GONE);//去评价
                        mTxtCountDown.setVisibility(View.GONE);//倒计时
                        mOrderStateTxt.setText("打包中");
                    }else if(mPackState == CommConstans.ORDERPACKSTATE.PACKING){
                        mDeleteBtn.setVisibility(View.GONE);//删除订单
                        mCancelBtn.setVisibility(View.VISIBLE);//取消订单
                        mPayBtn.setVisibility(View.GONE);//去支付
                        mUrged.setVisibility(View.VISIBLE);//催单
                        mBtnSubmit.setVisibility(View.GONE);//确认收货
                        mBtnSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                        mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                        mBtnEvaluate.setVisibility(View.GONE);//去评价
                        mTxtCountDown.setVisibility(View.GONE);//倒计时
                        mOrderStateTxt.setText("打包中");
                    }else {
                        int deliverState = UtilNumber.IntegerValueOf(mapObject.get("DELIVERSTATE") + "");

                        if (deliverState == CommConstans.SHOPORDERTAKETYPE.DELIVER) {//待收货
                            mDeleteBtn.setVisibility(View.GONE);//删除订单
                            mCancelBtn.setVisibility(View.VISIBLE);//取消订单
                            mPayBtn.setVisibility(View.GONE);//去支付
                            mUrged.setVisibility(View.VISIBLE);//催单
                            mBtnSubmit.setVisibility(View.GONE);//确认收货
                            mBtnSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                            mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                            mBtnEvaluate.setVisibility(View.GONE);//去评价
                            mTxtCountDown.setVisibility(View.GONE);//倒计时
                            mOrderStateTxt.setText("待配送");
                        } else if (deliverState == CommConstans.SHOPORDERTAKETYPE.TAKESELF) {
                            mDeleteBtn.setVisibility(View.GONE);//删除订单
                            mCancelBtn.setVisibility(View.GONE);//取消订单
                            mPayBtn.setVisibility(View.GONE);//去支付
                            mUrged.setVisibility(View.GONE);//催单
                            mBtnSubmit.setVisibility(View.VISIBLE);//确认收货
                            mBtnSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                            mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                            mBtnEvaluate.setVisibility(View.GONE);//去评价
                            mTxtCountDown.setVisibility(View.GONE);//倒计时
                            mOrderStateTxt.setText("配送中");
                        }
                    }
                    /**************************************
                     * *********货到付款+门店自取 ***********
                     * ***********************************/
                }else if(mOrderPayType == CommConstans.SHOPORDERPAYTYPE.ONDELIVERY && mOrderTakeType == CommConstans.SHOPORDERTAKETYPE.TAKESELF){
                    //货到付款+门店自取
                    //打包状态1：未接单，2：已接单，3：打包中，4：打包完成
                    if (mPackState == CommConstans.ORDERPACKSTATE.NO_ACCEPT){
                        mDeleteBtn.setVisibility(View.GONE);//删除订单
                        mCancelBtn.setVisibility(View.VISIBLE);//取消订单
                        mPayBtn.setVisibility(View.GONE);//去支付
                        mUrged.setVisibility(View.GONE);//催单
                        mBtnSubmit.setVisibility(View.GONE);//确认收货
                        mBtnSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                        mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                        mBtnEvaluate.setVisibility(View.GONE);//去评价
                        mTxtCountDown.setVisibility(View.GONE);//倒计时
                        mOrderStateTxt.setText("处理中");
                    }else if(mPackState == CommConstans.ORDERPACKSTATE.HAD_ACCEPT){
                        mDeleteBtn.setVisibility(View.GONE);//删除订单
                        mCancelBtn.setVisibility(View.VISIBLE);//取消订单
                        mPayBtn.setVisibility(View.GONE);//去支付
                        mUrged.setVisibility(View.GONE);//催单
                        mBtnSubmit.setVisibility(View.GONE);//确认收货
                        mBtnSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                        mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                        mBtnEvaluate.setVisibility(View.GONE);//去评价
                        mTxtCountDown.setVisibility(View.GONE);//倒计时
                        mOrderStateTxt.setText("打包中");
                    }else if(mPackState == CommConstans.ORDERPACKSTATE.PACKING){
                        mDeleteBtn.setVisibility(View.GONE);//删除订单
                        mCancelBtn.setVisibility(View.VISIBLE);//取消订单
                        mPayBtn.setVisibility(View.GONE);//去支付
                        mUrged.setVisibility(View.GONE);//催单
                        mBtnSubmit.setVisibility(View.GONE);//确认收货
                        mBtnSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                        mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                        mBtnEvaluate.setVisibility(View.GONE);//去评价
                        mTxtCountDown.setVisibility(View.GONE);//倒计时
                        mOrderStateTxt.setText("打包中");
                    }else {
                        mDeleteBtn.setVisibility(View.GONE);//删除订单
                        mCancelBtn.setVisibility(View.VISIBLE);//取消订单
                        mPayBtn.setVisibility(View.GONE);//去支付
                        mUrged.setVisibility(View.GONE);//催单
                        mBtnSubmit.setVisibility(View.GONE);//确认收货
                        mBtnSubmitGoodBtn.setVisibility(View.VISIBLE);//确认取货
                        mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                        mBtnEvaluate.setVisibility(View.GONE);//去评价
                        mTxtCountDown.setVisibility(View.GONE);//倒计时
                        mOrderStateTxt.setText("待取货");
                    }
                }
                /*****************************订单状态 正常 结束********************************************************
                 * ****************************************************************************************************/
            }else if(mOrderState == CommConstans.SHOPORDERSTATE.FINISH){//已完成
                //订单是否评价1：是，0：否
                int evalstate = UtilNumber.IntegerValueOf(mapObject.get("EVALSTATE") + "");
                //已经评价
                if (evalstate == CommConstans.SHOPORDERDELEVALSTATE.NOEVALSTATE){
                    mDeleteBtn.setVisibility(View.GONE);//删除订单
                    mCancelBtn.setVisibility(View.GONE);//取消订单
                    mPayBtn.setVisibility(View.GONE);//去支付
                    mUrged.setVisibility(View.GONE);//催单
                    mBtnSubmit.setVisibility(View.GONE);//确认收货
                    mBtnSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                    mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                    mBtnEvaluate.setVisibility(View.VISIBLE);//去评价
                    mTxtCountDown.setVisibility(View.GONE);//倒计时
                    mOrderStateTxt.setText("交易完成");
                }else {
                    mDeleteBtn.setVisibility(View.VISIBLE);//删除订单
                    mCancelBtn.setVisibility(View.GONE);//取消订单
                    mPayBtn.setVisibility(View.GONE);//去支付
                    mUrged.setVisibility(View.GONE);//催单
                    mBtnSubmit.setVisibility(View.GONE);//确认收货
                    mBtnSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                    mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                    mBtnEvaluate.setVisibility(View.GONE);//去评价
                    mTxtCountDown.setVisibility(View.GONE);//倒计时
                    mOrderStateTxt.setText("交易完成");
                }
            }else if(mOrderState == CommConstans.SHOPORDERSTATE.CANCEL){//已取消
                mDeleteBtn.setVisibility(View.VISIBLE);//删除订单
                mCancelBtn.setVisibility(View.GONE);//取消订单
                mPayBtn.setVisibility(View.GONE);//去支付
                mUrged.setVisibility(View.GONE);//催单
                mBtnSubmit.setVisibility(View.GONE);//确认收货
                mBtnSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                mBtnEvaluate.setVisibility(View.GONE);//去评价
                mTxtCountDown.setVisibility(View.GONE);//倒计时
                mOrderStateTxt.setText("已取消");
            }else if(mOrderState == CommConstans.SHOPORDERSTATE.EXPIRE){

                mDeleteBtn.setVisibility(View.VISIBLE);//删除订单
                mCancelBtn.setVisibility(View.GONE);//取消订单
                mPayBtn.setVisibility(View.GONE);//去支付
                mUrged.setVisibility(View.GONE);//催单
                mBtnSubmit.setVisibility(View.GONE);//确认收货
                mBtnSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                mBtnEvaluate.setVisibility(View.GONE);//去评价
                mTxtCountDown.setVisibility(View.GONE);//倒计时
                mOrderStateTxt.setText("已过期");
            }

        }else{
            LogUtils.e("refresh===Error=====null=========>" + mapObject + "");
        }
    }


    /**
     * 请求服务成功后获取数据
     */
    public void onSuccessGetData(Map<String, Object>  mapObject) {
        /************联系人、手机号、收货地址************/
        LogUtils.e("refresh===Error=====null=========>" + mapObject.toString());
        mOrderNum = "订单号："+mapObject.get("ORDERNUM") + "";//订单号
        mOrderNums.setText(mOrderNum);
        mUsername.setText(mapObject.get("LINKNAME") + "");//用户姓名
        //mUsersex.setText(mapObject.get("SEX") + "");//性别
        mUserphone.setText(mapObject.get("TEL") + "");//电话号码
        mUseraddr.setText(mapObject.get("ADDRESS") + "");//收货地址
        mTotalPrice.setText("￥" +  mapObject.get("AMOUNT") + "");//总计金额

        //抵用实惠币金额
       mShihuibiPrice.setText("￥" + mapObject.get("REDEEMMONEY") + "");
        //实付款
       mGoodsPrice.setText("￥" + mapObject.get("REALPAY") + "");
        if((mapObject.get("ISSELF") + "").equals("1")){
            mTxtStoreName.setText("大实惠直营");
        }else{
            mTxtStoreName.setText(mapObject.get("STORENAME")+"");
        }
        if (mapObject.get("REMAIN") == null){

        }else {
            countDowntime = Long.parseLong(mapObject.get("REMAIN") + "");
            timer = new CountDownTimer(countDowntime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    SimpleDateFormat format = new SimpleDateFormat("mm分钟ss秒");
                    String dateString = format.format(millisUntilFinished);
                    mTxtCountDown.setVisibility(View.VISIBLE);
                    mTxtTime.setVisibility(View.VISIBLE);
                    mTxtCountDown.setText(dateString);

                }

                @Override
                public void onFinish() {
                    mTxtTime.setVisibility(View.GONE);
                    mTxtCountDown.setVisibility(View.GONE);
                    mTxtCountDown.setText("订单已取消");

                }
            };
            timer.start();
        }


        /************ 订单状态代码 ***********/
        //支付方式：1、在线支付，2、货到付款
        mOrderPayType = UtilNumber.IntegerValueOf(mapObject.get(CommConstans.SHOPORDER.PAYTYPE) + "");
        //收货方式1：送货，2：自取
        mOrderTakeType = UtilNumber.IntegerValueOf(mapObject.get(CommConstans.SHOPORDER.TAKETYPE) + "");
        // 支付渠道1：微信,2:支付宝
        mOrderPayMethod = UtilNumber.IntegerValueOf(mapObject.get(CommConstans.SHOPORDER.PAYMETHOD) + "");
        //支付状态1：待支付，2：已支付
        mOrderPayState = UtilNumber.IntegerValueOf(mapObject.get(CommConstans.SHOPORDER.PAYSTATE) + "");
        //收货状态1：待发货，2：已发货
        mOrderDeliverState = UtilNumber.IntegerValueOf(mapObject.get(CommConstans.SHOPORDER.DELIVERSTATE) + "");
        //订单状态1：正常，2：完成，3：取消，4：删除，5 ：过期，6：关闭
        mOrderState = UtilNumber.IntegerValueOf(mapObject.get(CommConstans.SHOPORDER.ORDERSTATE) + "");
        //打包状态1：未接单，2：已接单，3：打包中，4：打包完成
        mPackState = UtilNumber.IntegerValueOf(mapObject.get("PACKSTATE") + "");
        TypeReference typeListMap = new TypeReference<List<Map<String, Object>>>() {};
        listMapGoods.addAll((List<Map<String, Object>>) FastJSONHelper.deserializeAny(mapObject
                .get("GOODSLIST") + "", typeListMap));
        //数据锁定
        addMaps(mapObject);
        //订单按钮状态
        refreshBtn(mapObject);
    }


    /*************************************************************/

    public void addMaps(Map<String, Object>  mapObject) {
        /*****************	//支付方式1：在线支付，2：货到付款**********************/
        Map<String, String> mapStr1 = new HashMap<>();
        mapStr1.put("name", "支付方式：");
        if ("1".equals(mapObject.get("PAYTYPE") + "")) {//在线支付
            mapStr1.put("content", "在线支付");

        } else if ("2".equals(mapObject.get("PAYTYPE") + "")) {//货到付款
            mapStr1.put("content", "货到付款");
        }
        // mapStr1.put("content", mapObject.get("ORDERNUM") + "");
        listMap.add(mapStr1);
        Map<String, String> mapStr2 = new HashMap<>();
        mapStr2.put("name", "下单时间：");
        mapStr2.put("content", mapObject.get("STARTDATE") + "");
        listMap.add(mapStr2);
        Map<String, String> mapStr3 = new HashMap<>();
        /*****************收货方式1：送货，2：自取**********************/
        mapStr3.put("name", "配送方式：");
        if("1".equals(mapObject.get("ISSELF")+"")) {
            mapStr3.put("content", "第三方配送");
        }else if ("1".equals(mapObject.get("TAKETYPE") + "")) {//----送货
            mapStr3.put("content", "门店配送");
        } else if ("2".equals(mapObject.get("TAKETYPE") + "")) {//---自取
            mapStr3.put("content", "上门自取");
        }
        listMap.add(mapStr3);
        if("0".equals(mapObject.get("ISSELF")+"")) {
            Map<String, String> mapStr4 = new HashMap<>();
            mapStr4.put("name", "配送说明：");
            if (!UtilString.isEmpty(mapObject.get("DESCRIBE") + "")) {
                mapStr4.put("content", mapObject.get("DESCRIBE") + "");
            } else {
                mapStr4.put("content", "美景天城1.5公里以内9:00-19:00免费配送");
            }
            listMap.add(mapStr4);
        }
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
