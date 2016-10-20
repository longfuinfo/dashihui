package com.dashihui.afford.ui.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessCommon;
import com.dashihui.afford.business.BusinessOrder;
import com.dashihui.afford.business.BusinessUser;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.sqlite.SqliteShoppingCart;
import com.dashihui.afford.thirdapi.greedsqlite.ShoppingCart;
import com.dashihui.afford.ui.activity.my.AtyMyAddress;
import com.dashihui.afford.ui.adapter.AdapterSettlement;
import com.dashihui.afford.ui.model.ModelDoAdd;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.number.UtilNumber;
import com.dashihui.afford.util.preferences.UtilPreferences;
import com.dashihui.afford.util.string.UtilString;
import com.dashihui.afford.util.toast.UtilToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AtySettlement extends BaseActivity implements View.OnClickListener {


    @ViewInject(R.id.ibtn_userinfo)
    private ImageButton mIbtEditAddr;
    private BusinessUser mBllUser;
    private BusinessOrder mBllOrder;
    @ViewInject(R.id.noAddress)
    private TextView mNoAddress;
    @ViewInject(R.id.top_address)
    private LinearLayout mLytTopaddress;
    @ViewInject(R.id.addAddress)
    private LinearLayout mLytAddAddress;
    @ViewInject(R.id.tv_shihuibi)
    private TextView mMoney;


    @ViewInject(R.id.username)
    private TextView mUserName;//用户名字
    @ViewInject(R.id.usersex)
    private TextView mUserSex;//性别
    @ViewInject(R.id.userphone)
    private TextView mUserPhone;//用户电话
    @ViewInject(R.id.useraddr)
    private TextView mUserAddr;//用户地址

    /*************************/
    @ViewInject(R.id.delivery_remark)
    private TextView mRemarkTv;

    @ViewInject(R.id.goods_price)
    private TextView mGoodsPrice;//商品金额
    @ViewInject(R.id.stilpay_money)
    private TextView mPayMoney;//需付款

    @ViewInject(R.id.txtViewPrice)
    private TextView mTxtViewPrice;//实付款
    @ViewInject(R.id.txtViewSettlement)
    private TextView mTxtViewSettlement;//提交订单
    @ViewInject(R.id.txt_store_total)
    private TextView mTxtStoreTotal;//门店价格总计

    @ViewInject(R.id.txt_self_total)
    private TextView mTxtSelfTotal;//直营价格总计

    @ViewInject(R.id.ls_shop)
    private ListView mLsShop;//门店商品列表


    @ViewInject(R.id.ls_mine)
    private ListView mLsMine;//直营商品列表


    @ViewInject(R.id.txtPayType_Shop)
    private TextView mTxtPayType_Shop;
    @ViewInject(R.id.delivery_remark)
    private TextView mTxtRemark;//备注

    @ViewInject(R.id.txtSendType_shop)
    private TextView mTxtSendType_Shop;
    @ViewInject(R.id.txtPrice_receive)
    private TextView mTxtReceive;//货到付款
    @ViewInject(R.id.txtTime)
    private TextView mTxtTime;//门店送货时间

    @ViewInject(R.id.rlty_sendType_shop)
    private RelativeLayout mSendTypeShop;//配送方式

    @ViewInject(R.id.rlyt_payType_shop)
    private RelativeLayout mPayTypeShop;//支付方式
    @ViewInject(R.id.ll_store)
    private LinearLayout mLl_store;
    @ViewInject(R.id.ll_self)
    private LinearLayout mLl_self;
    @ViewInject(R.id.ll_remark)
    private LinearLayout mLlRemark;
    @ViewInject(R.id.ib_shihuibi)
    private ImageButton mShihuibi;

    @ViewInject(R.id.lyt_shihuibi)
    private LinearLayout mLytShihuibi;


    private AdapterSettlement mAdapterSettlement;
    private AdapterSettlement mAdapterShopSettlement;
    private AdapterSettlement mAdapterSelfSettlement;
    private List<ShoppingCart> mShopCartList;
    private List<ShoppingCart> mShopList = new ArrayList<>();//门店商品
    private List<ShoppingCart> mMineList = new ArrayList<>();// 直营商品
    private int payType = 1;
    private int takeType = 1;
    private ModelDoAdd mModelDoAdd;
    private String mOrderInfo;//从FragmentOrderState获取再来一单的订单信息
    public final static String ORDERTAG = "orderTag";
    private BusinessCommon mBllCommon;
    private Map<String, Object> mMapObject;
    private List<Map<String, Object>> mListMaps;
    private int mNoAddressType;
    private int position = 1;//确定点击的是哪个位置  1.门店 支付方式 2.门店 配送方式
    private int type = 1;//确定点击的是哪个按钮
    private Dialog mDialog;
    private Button bt1_dialog;
    private Button bt2_dialog;
    private Button bt3_dialog;
    private View  view;
    private double storeAllPrice = 0;//门店总价格
    private double selfAllPrice = 0;//直营总价格
    private double shihuiPrice = 0;//抵用实惠币金额
    private String myMoney;
    private String isredeem = "0";//是否抵用实惠币

    private int i;
    private Map<String, Object> mMoneyMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_settlement);
        ViewUtils.inject(this);
//        mLytSendExplain.setVisibility(View.VISIBLE);
//        mLytOpenExplain.setVisibility(View.GONE);
        mBllUser = new BusinessUser(this);
        mModelDoAdd = new ModelDoAdd();
        mBllOrder = new BusinessOrder(this);
        mBllCommon = new BusinessCommon(this);
        //获取默认地址
        mBllUser.addresslist();
        //支付方式初始化
        mModelDoAdd.setPaytypeshop(payType + "");
        mModelDoAdd.setPaytypeself("1");
        //送货方式初始化
        mModelDoAdd.setTaketypeshop(takeType + "");
        mModelDoAdd.setTaketypeself("1");
        initData();

        initView();
        //初始化数据
        //支付方式
//        initPayTypeView();
        //配送方式
//        initTakeTypeView();

    }

    /**
     * 初始化数据
     */
    private void initData() {
        myMoney = AffordApp.getInstance().getUserMoney();
        mMoney.setText("可用"+myMoney +"实惠币抵用"+myMoney+"元");
        if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance()
                .getEntityLocation().getSTORE() != null) {
            mShopCartList = SqliteShoppingCart.getInstance(this).getListByShopID
                    (AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "", true);
            for (int i = 0; i < mShopCartList.size(); i++) {
                ShoppingCart cart = mShopCartList.get(i);
                if (UtilString.isEquals(cart.getShoptype(), "1")) {
                    mMineList.add(cart);
                } else {
                    mShopList.add(cart);
                }

            }
            int allNum = 0;//总数量
            double allPric = 0;//总价格
            int selfAllNum = 0; //直营商品数量
            int storeAllNum = 0; //门店商品数量
            String selfGoodsID = "";
            String selfCount = "";
            String storeGoodsID = "";
            String storeCount = "";
            if (mShopList != null && mShopList.size() > 0) {
                mAdapterShopSettlement = new AdapterSettlement(this, mShopList);
                mLsShop.setAdapter(mAdapterShopSettlement);
                setlistViewHeigh(mLsShop);
                for (int i = 0; i < mShopList.size(); i++) {
                    double num = UtilNumber.DoubleValueOf(mShopList.get(i).getBuynum());//购买数量
                    double price = UtilNumber.DoubleValueOf(mShopList.get(i).getSellprice() + "")
                            ;//原价
                    double numPrice = UtilNumber.DoubleValueOf(num * price + "");
                    storeAllPrice += numPrice;
                    storeAllNum += num;
                    if (i == (mShopList.size() - 1)) {
                        storeGoodsID += mShopList.get(i).getID() + "";
                        storeCount += UtilNumber.IntegerValueOf(mShopList.get(i).getBuynum()) + "";
                    } else {
                        storeGoodsID += mShopList.get(i).getID() + ",";
                        storeCount += UtilNumber.IntegerValueOf(mShopList.get(i).getBuynum()) + ",";
                    }
                }
                mModelDoAdd.setStoregoodsids(storeGoodsID);
                mModelDoAdd.setStorecounts(storeCount);
                mModelDoAdd.setStoreamount(UtilNumber.DoubleValueOf(storeAllPrice + "") + "");
                mTxtStoreTotal.setText("￥" + UtilNumber.DoubleValueOf(storeAllPrice + "") + "");
                //商品金额
            } else {
                mLl_store.setVisibility(View.GONE);
            }
            if (mMineList != null && mMineList.size() > 0) {
                mAdapterSelfSettlement = new AdapterSettlement(this, mMineList);
                mLsMine.setAdapter(mAdapterSelfSettlement);
                setlistViewHeigh(mLsMine);
                for (int i = 0; i < mMineList.size(); i++) {
                    double num = UtilNumber.DoubleValueOf(mMineList.get(i).getBuynum());
                    double price = UtilNumber.DoubleValueOf(mMineList.get(i).getSellprice() + "");
                    double numPrice = UtilNumber.DoubleValueOf(num * price + "");
                    selfAllPrice += numPrice;
                    selfAllNum += num;
                    if (i == (mMineList.size() - 1)) {
                        selfGoodsID += mMineList.get(i).getID() + "";
                        selfCount += UtilNumber.IntegerValueOf(mMineList.get(i).getBuynum()) + "";
                    } else {
                        selfGoodsID += mMineList.get(i).getID() + ",";
                        selfCount += UtilNumber.IntegerValueOf(mMineList.get(i).getBuynum()) + ",";
                    }
                }
                Log.i("GoodsID", selfGoodsID);

                mModelDoAdd.setSelfgoodsids(selfGoodsID);
                mModelDoAdd.setSelfcounts(selfCount);
                mModelDoAdd.setSelfamount(UtilNumber.DoubleValueOf(selfAllPrice + "") + "");
                mTxtSelfTotal.setText("￥" + UtilNumber.DoubleValueOf(selfAllPrice + "") + "");//商品金额
            } else {
                mLl_self.setVisibility(View.GONE);
            }

        } else {
            LogUtils.e("null异常============================>");
            finish();
        }
    }

    private void initView() {
        mTxtViewPrice.setText("在线支付：￥" + UtilNumber.DoubleValueOf
                (selfAllPrice + storeAllPrice + "") + "");//在线支付
        mSendTypeShop.setOnClickListener(this);
        mPayTypeShop.setOnClickListener(this);
        view = View.inflate(this, R.layout.dialog_bottom, null);
        bt1_dialog = (Button) view.findViewById(R.id.bt1_dialog);
        bt2_dialog = (Button) view.findViewById(R.id.bt2_dialog);
        bt3_dialog = (Button) view.findViewById(R.id.bt3_dialog);
        mLlRemark.setOnClickListener(this);
        bt1_dialog.setOnClickListener(this);
        bt2_dialog.setOnClickListener(this);
        bt3_dialog.setOnClickListener(this);
    }

    /**
     * 点击事件 显示底部dialog
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (mDialog == null) {
            showDialog();
        }
        switch (v.getId()) {
            case R.id.rlyt_payType_shop:
                //支付类型
                position = 1;
                bt1_dialog.setText(R.string.confirmorder_pay_online);
                bt2_dialog.setText(R.string.confirmorder_deliverypay1);
                bt3_dialog.setText(R.string.confirmorder_dialog_cancle);
                mDialog.setContentView(view);
                changePayColor(payType);
                mDialog.show();
                break;
            case R.id.rlty_sendType_shop:
                //送货类型
                position = 2;
                bt1_dialog.setText(R.string.confirmorder_delivery_store);
                bt2_dialog.setText(R.string.confirmorder_delivery_myself);
                bt3_dialog.setText(R.string.confirmorder_dialog_cancle);
                changePayColor(takeType);
                mDialog.setContentView(view);
                mDialog.show();
                break;
            case R.id.bt1_dialog:
                type = 1;
                positionType();
                break;
            case R.id.bt2_dialog:
                type = 2;
                positionType();
                break;
            case R.id.bt3_dialog:
                type = 3;
                positionType();
                break;
            case R.id.ll_remark:
                Intent intent = new Intent(AtySettlement.this, AtySettlementRemark.class);
                intent.putExtra("remark", mRemarkTv.getText() + "");
                startActivityForResult(intent, CommConstans.ADDRESS.MARS);
                break;

        }
    }


    /**
     * 选择支付方式/配送方式对话框
     */
    private void showDialog() {
        mDialog = new Dialog(this, R.style.DialogSty);
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setWindowAnimations(R.style.DialogAnimation);
    }

    /**
     * 确定所点击的对话框
     */
    private void positionType() {
        switch (position) {
            case 1:
                switch (type) {
                    case 1:
                        payType = 1;
                        //在线支付
                        changePayColor(payType);

                        mTxtViewPrice.setText("在线支付：￥" + UtilNumber.DoubleValueOf
                                (selfAllPrice + storeAllPrice + "") + "");//在线支付
                        mTxtReceive.setVisibility(View.GONE);//货到付款
                        mTxtViewPrice.setVisibility(View.VISIBLE);
                        mLytShihuibi.setVisibility(View.VISIBLE);
                        mTxtPayType_Shop.setText(R.string.confirmorder_pay_online);
                        mModelDoAdd.setPaytypeshop(payType + "");
                        //
                        mDialog.dismiss();
                        break;
                    case 2:
                        //货到付款
                        payType = 2;
                        changePayColor(payType);
                        if (selfAllPrice == 0 && storeAllPrice > 0) {
                            mTxtViewPrice.setVisibility(View.GONE);
                            mTxtReceive.setText("货到付款：￥" + UtilNumber.DoubleValueOf(storeAllPrice
                                    + "") + "");
                            mTxtReceive.setVisibility(View.VISIBLE);
                        } else if (selfAllPrice > 0 && storeAllPrice > 0) {
                            mTxtViewPrice.setVisibility(View.VISIBLE);
                            mTxtReceive.setVisibility(View.VISIBLE);

                            mTxtViewPrice.setText("在线支付：￥" + UtilNumber.DoubleValueOf
                                    (selfAllPrice + "") + "");
                            mTxtReceive.setText("货到付款：￥" + UtilNumber.DoubleValueOf(storeAllPrice
                                    + "") + "");
                        }
                        mTxtPayType_Shop.setText(R.string.confirmorder_deliverypay);
                        mModelDoAdd.setPaytypeshop(payType + "");
                        mDialog.dismiss();
                        if (UtilNumber.DoubleValueOf(selfAllPrice + "") == 0){
                            mLytShihuibi.setVisibility(View.GONE);
                        }
                        break;
                    case 3:
                        //取消
                        mDialog.dismiss();
                        break;
                }
                break;
            case 2:
                switch (type) {
                    case 1:
                        //门店配送
                        takeType = 1;
                        mTxtTime.setText(mMapObject.get("DELIVERYDES") + "");
                        mTxtSendType_Shop.setText(R.string.confirmorder_delivery_store);
                        mModelDoAdd.setTaketypeshop(takeType + "");
                        changePayColor(takeType);
                        mDialog.dismiss();
                        break;
                    case 2:
                        //上门自取
                        takeType = 2;
                        changePayColor(takeType);
                        mTxtTime.setText("营业时间:" + mMapObject.get("BEGINTIME") + "—" + mMapObject
                                .get("ENDTIME"));
                        mTxtSendType_Shop.setText(R.string.confirmorder_delivery_self);
                        mModelDoAdd.setTaketypeshop(2 + "");
                        mDialog.dismiss();
                        break;
                    case 3:
                        //取消
                        mDialog.dismiss();
                        break;
                }
                break;
        }
    }

    private void changePayColor(int i) {
        switch (i){
            case 1:
                bt1_dialog.setTextColor(getResources().getColor(R.color.btn_receive));
                bt2_dialog.setTextColor(getResources().getColor(R.color.btn_dialog));
                break;
            case 2:
                bt2_dialog.setTextColor(getResources().getColor(R.color.btn_receive));
                bt1_dialog.setTextColor(getResources().getColor(R.color.btn_dialog));
        }
    }
   /* //支付方式
    public void initPayTypeView() {
        mPayType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                */

    /****
     * &&  设置支付方式   &&
     ******//*
                switch (checkedId) {
                    case R.id.confirm_weixin_cBoxAgree://线上支付
                        mModelDoAdd.setPaytype("1");
                        LogUtils.e("onPayTypeCheckedChanged======线上支付========>" + checkedId);
                        break;
                    case R.id.confirm_delivery_cBoxAgree://货到付款
                        mModelDoAdd.setPaytype("2");
                        LogUtils.e("onPayTypeCheckedChanged======货到付款=======>" + checkedId);
                        break;
                    default:
                        break;
                }
            }
        });
    }*/

   /* //配送方式
    public void initTakeTypeView() {
        takeType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LogUtils.e("onTakeTypeCheckedChanged======AtySettlement=========>" + checkedId);
                switch (checkedId) {
                    case R.id.confirm_store_cBoxAgree://门店配送
                        mModelDoAdd.setTaketype("1");

                        mLytSendExplain.setVisibility(View.VISIBLE);
                        mLytOpenExplain.setVisibility(View.GONE);
                        if (!"null".equals(mMapObject.get("DELIVERYDES") + "")) {
                            mSendTime.setText(mMapObject.get("DELIVERYDES") + "");
                        } else {
                            mSendTime.setText("无");
                        }

                        LogUtils.e("onTakeTypeCheckedChanged======门店配送=======>" + checkedId);
                        break;
                    case R.id.confirm_myself_cBoxAgree://上门自取
                        mModelDoAdd.setTaketype("2");
                        mLytSendExplain.setVisibility(View.GONE);
                        mLytOpenExplain.setVisibility(View.VISIBLE);
                        mOpenTime.setText(mMapObject.get("BEGINTIME") + "—" + mMapObject.get
                        ("ENDTIME"));

                        LogUtils.e("onTakeTypeCheckedChanged======上门自取=======>" + checkedId);
                        break;
                    default:
                        break;
                }
            }
        });
    }*/
    @Override
    protected void onResume() {
        LogUtils.e("是否登录================>" + AffordApp.isLogin());
        //是否登录
        if (!AffordApp.isLogin()) {
            //没有登录
            LogUtils.e("没有登录======isLogin==========>");
            Intent intent = new Intent(AtySettlement.this, AtyLogin.class);
            intent.putExtra(CommConstans.Login.INTENT_KEY, CommConstans.Login
                    .INTENT_VALUE_SETTLEMENT);
            startActivity(intent);
            finish();
        }
        /****&&  设置店铺ID   &&******/
        if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance()
                .getEntityLocation().getSTORE() != null) {
            mModelDoAdd.setStoreid(AffordApp.getInstance().getEntityLocation().getSTORE().getID()
                    + "");
        } else {
            LogUtils.e("null===Error====AtySettlement===>" + AffordApp.LOG_PHONE);
            finish();
        }

        //获取默认地址
        mBllUser.defaultAddress();
        LogUtils.e("onResume=======storeID==========>" + AffordApp.getInstance()
                .getEntityLocation().getSTORE().getID() + "");
        mBllCommon.storeDetail(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
        LogUtils.e("登录======isLogin==========>");
        //获取用户地址列表
//        mNoAddressType = UtilNumber.IntegerValueOf(getIntent().getStringExtra
// (AtySettlementAddress.NOADDRESS));
//        LogUtils.e("onResume==========mNoAddressType=========>" + mNoAddressType);
//        if (mNoAddressType != 100) {
//            mBllUser.addresslist();
//        }
        //解除禁止多次提交
        mTxtViewSettlement.setEnabled(true);
        super.onResume();
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_USER_DEFAULTADDRESS://默认地址
                    LogUtils.e("onSuccess======默认地址========>" + beanSendUI.getInfo());
                    Map<String, String> defaultAddr = (Map<String, String>) beanSendUI.getInfo();
                    mNoAddress.setVisibility(View.GONE);
                    mLytTopaddress.setVisibility(View.VISIBLE);
                    mUserName.setText(defaultAddr.get("LINKNAME") + "");//用户名字
                    String sex = defaultAddr.get("SEX") + "";
                    /****&&  设置性别   &&******/
                    if ("1".equals(sex)) {
                        mUserSex.setText("先生 ");//性别
                        mModelDoAdd.setSex("先生 ");
                    } else {
                        mUserSex.setText("女士");//性别
                        mModelDoAdd.setSex("女士");
                    }
                    mUserPhone.setText(defaultAddr.get("TEL") + "");//用户电话
                    if (defaultAddr.get("ADDRESS") == null) {
                        mBaseUtilAty.startActivity(AtySettlementAddress.class);
                    } else {
                        mUserAddr.setText(defaultAddr.get("ADDRESS") + "");//用户地址
                    }
                    break;
                case AffConstans.BUSINESS.TAG_ORDER_SAVA://提交订单
                   final Map<String, Object> order = (Map<String, Object>) beanSendUI.getInfo();
                    Log.i("order", order.toString());
                    LogUtils.e("onSuccess======提交订单========>" + order);
                    if (order != null) {
                        LogUtils.e("onSuccess===== mModelDoAdd.getPaytypeshop()=支付方式========>" +
                                mModelDoAdd.getPaytypeshop());
                        if ("1".equals(mModelDoAdd.getPaytypeshop())) {

                            if (order.get("FORWARDFLAG").equals("1")) {
                                LogUtils.e("if===== mModelDoAdd.getPaytypeshop()=支付方式========>" +order.get("FORWARDFLAG"));
                                Intent mapIntent = new Intent(this, AtySettlementOrder.class);
                                mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, (selfAllPrice +
                                        storeAllPrice - shihuiPrice) + "");
                                mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, order.get
                                        ("ORDERNUM") + "");
                                //传输一个支付标志，区分商品、服务、家政订单支付
                                mapIntent.putExtra(AtySettlementOrder.ORDER_PAY_TYPE, CommConstans
                                        .ORDER.ORDER_PAY);
                                startActivity(mapIntent);

//                            Intent mapIntent = new Intent(this, AtySettlementOrder.class);
//                            mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, (selfAllPrice +
//                                    storeAllPrice) + "");
//                            mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, order.get
//                                    ("ORDERNUM") + "");
//                            //传输一个支付标志，区分商品、服务、家政订单支付
//                            mapIntent.putExtra(AtySettlementOrder.ORDER_PAY_TYPE, CommConstans
//                                    .ORDER.ORDER_PAY);
//                            startActivity(mapIntent);

                                //删除购物车里已经购买的商品
                                List<ShoppingCart> shopCartList = SqliteShoppingCart.getInstance
                                        (this).getListByShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "", true);
                                for (int i = 0; i < shopCartList.size(); i++) {
                                    SqliteShoppingCart.getInstance(this).deleteShoppingCart
                                            (shopCartList.get(i));
                                }
                                //存储本地
                                UtilPreferences.putInt(AtySettlement.this, AtyShoppingCart
                                        .SHOPCART_NUM, 0);
                                UtilPreferences.putString(AtySettlement.this, AtyShoppingCart
                                        .SHOPCART_PRICE, "0");

                                sendShopChartBroadcast();
                                finish();
                            }else if(order.get("FORWARDFLAG").equals("2")){
                                new AlertDialog.Builder(this)
                                        .setCancelable(false)
                                        .setTitle("提示")
                                        .setMessage("恭喜你购买成功～")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent mapIntent = new Intent(AtySettlement.this, AtyFragmentOrdert.class);
                                                mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, storeAllPrice + "");
                                                mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, order.get
                                                        ("ORDERNUM") + "");
                                                startActivity(mapIntent);
                                                finish();
                                            }
                                        })
                                        .show();

//                                Intent mapIntent = new Intent(this, AtyFragmentOrdert.class);
//                                mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, storeAllPrice + "");
//                                mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, order.get
//                                        ("ORDERNUM") + "");
//                                startActivity(mapIntent);


                                //UtilToast.show(this, "订单提交成功！", Toast.LENGTH_SHORT);
                                //删除购物车里已经购买的商品
                                List<ShoppingCart> shopCartList = SqliteShoppingCart.getInstance
                                        (this).getListByShopID(AffordApp.getInstance()
                                        .getEntityLocation().getSTORE().getID() + "", true);

                                for (int i = 0; i < shopCartList.size(); i++) {
                                    SqliteShoppingCart.getInstance(this).deleteShoppingCart
                                            (shopCartList.get(i));
                                }
                                //存储本地
                                UtilPreferences.putInt(AtySettlement.this, AtyShoppingCart
                                        .SHOPCART_NUM, 0);
                                UtilPreferences.putString(AtySettlement.this, AtyShoppingCart
                                        .SHOPCART_PRICE, 0 + "");
                                sendShopChartBroadcast();
                            }
                        } else if ("2".equals(mModelDoAdd.getPaytypeshop()) && selfAllPrice == 0) {
                            //货到付款,并且在线支付金额为0
                            new AlertDialog.Builder(this)
                                    .setCancelable(false)
                                    .setTitle("提示")
                                    .setMessage("恭喜你下单成功～")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent mapIntent = new Intent(AtySettlement.this, AtyFragmentOrdert.class);
                                            mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, storeAllPrice + "");
                                            mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, order.get
                                                    ("ORDERNUM") + "");
                                            startActivity(mapIntent);
                                            finish();

                                        }
                                    })
                                    .show();

//                                Intent mapIntent = new Intent(this, AtyFragmentOrdert.class);
//                                mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, storeAllPrice + "");
//                                mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, order.get
//                                        ("ORDERNUM") + "");
//                                startActivity(mapIntent);


                                //UtilToast.show(this, "订单提交成功！", Toast.LENGTH_SHORT);
                                //删除购物车里已经购买的商品
                                List<ShoppingCart> shopCartList = SqliteShoppingCart.getInstance
                                        (this).getListByShopID(AffordApp.getInstance()
                                        .getEntityLocation().getSTORE().getID() + "", true);

                                for (int i = 0; i < shopCartList.size(); i++) {
                                    SqliteShoppingCart.getInstance(this).deleteShoppingCart
                                            (shopCartList.get(i));
                                }
                                //存储本地
                            UtilPreferences.putInt(AtySettlement.this, AtyShoppingCart
                                    .SHOPCART_NUM, 0);
                            UtilPreferences.putString(AtySettlement.this, AtyShoppingCart
                                    .SHOPCART_PRICE, 0 + "");
                            sendShopChartBroadcast();

                               // finish();

                        } else if ("2".equals(mModelDoAdd.getPaytypeshop()) && selfAllPrice != 0) {
                            Intent mapIntent = new Intent(this, AtySettlementOrder.class);
                            mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, selfAllPrice + "");
                            mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, order.get
                                    ("ORDERNUM") + "");
                            //传输一个支付标志，区分商品、服务、家政订单支付
                            mapIntent.putExtra(AtySettlementOrder.ORDER_PAY_TYPE, CommConstans
                                    .ORDER.ORDER_PAY);
                            startActivity(mapIntent);

                            //删除购物车里已经购买的商品
                            List<ShoppingCart> shopCartList = SqliteShoppingCart.getInstance
                                    (this).getListByShopID(AffordApp.getInstance()
                                    .getEntityLocation().getSTORE().getID() + "", true);
                            for (int i = 0; i < shopCartList.size(); i++) {
                                SqliteShoppingCart.getInstance(this).deleteShoppingCart
                                        (shopCartList.get(i));
                            }
                            //存储本地
                            UtilPreferences.putInt(AtySettlement.this, AtyShoppingCart
                                    .SHOPCART_NUM, 0);
                            UtilPreferences.putString(AtySettlement.this, AtyShoppingCart
                                    .SHOPCART_PRICE, "0");
                            sendShopChartBroadcast();
                            finish();
                        } else {
                            LogUtils.e("onSuccess======请选择支付方式========>" + mModelDoAdd.getPaytype
                                    ());
                        }

                    } else {
                        LogUtils.e("onSuccess======提交订单===error=====>" + order);
                    }
                    break;
                case AffConstans.BUSINESS.TAG_STORE_DETAIL://获取商铺详情
                    LogUtils.e("onSuccess==============beanSendUI.getInfo()============>" +
                            beanSendUI.getInfo());
                    mMapObject = (Map<String, Object>) beanSendUI.getInfo();
                    LogUtils.e("onSuccess=========mMapObject=========>" + mMapObject.get
                            ("DELIVERYDES") + "");
                    if (!"null".equals(mMapObject.get("DELIVERYDES") + "")) {
                        mTxtTime.setText(mMapObject.get("DELIVERYDES") + "");
                    } else {
                        mTxtTime.setText("无");
                    }
                    break;
                case AffConstans.BUSINESS.TAG_USER_ADDRESSLIST://收货地址列表
                    mListMaps = (List<Map<String, Object>>) beanSendUI.getInfo();
                    if (UtilList.isEmpty(mListMaps)) {
                        mBaseUtilAty.startActivity(AtySettlementAddress.class);
                    }
                    break;
                case AffConstans.BUSINESS.TAG_USER_MONEY://获取用户实惠币
                    mMoneyMap = (Map<String, Object>) beanSendUI.getInfo();
                   // shihuiPrice = (double) mMoneyMap.get("MONEY");
                default:
                    LogUtils.e("onSuccess===default===========>" + beanSendUI);
                    break;
            }
        } else {
            LogUtils.e("onSuccess======AtyHome=========>" + beanSendUI);
        }
        //去提交订单按钮恢复
        mTxtViewSettlement.setEnabled(true);
    }


    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_USER_DEFAULTADDRESS://默认地址
                    LogUtils.e("onFailure======默认地址========>" + beanSendUI.getInfo());
                    /**********  设置地址  **********/
                    mUserAddr.setText("");
                    /**********  收货人姓名 **********/
                    mUserName.setText("");
                    /**********  收货人电话 **********/
                    mUserPhone.setText("");
                    mNoAddress.setVisibility(View.VISIBLE);
                    mLytTopaddress.setVisibility(View.GONE);
                    break;
                case AffConstans.BUSINESS.TAG_ORDER_SAVA://提交订单
                    LogUtils.e("onFailure======提交订单========>" + beanSendUI.getInfo());
                  /*  if (mDialog == null) {
                        mDialog = new WgtAlertDialog();
                        mDialog.show(this,
                                "确定",
                                beanSendUI.getInfo().toString(),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mDialog.dismiss();
                                        AtySettlement.this.finish();
                                    }
                                }, false, false);
                    }*/
                    break;
                default:
                    LogUtils.e("onFailure===default===========>" + beanSendUI);
                    break;
            }
        } else {
            LogUtils.e("onFailure======AtySettlement=========>" + beanSendUI);
        }
        //去提交订单按钮恢复
        mTxtViewSettlement.setEnabled(true);
    }


    /**
     * 提交订单
     *
     * @param v
     */
    public void onSettlementClick(View v) {
        mBllUser.addresslist();//再次获取地址列表
        LogUtils.e("=============提交订单=======1========>");
        //禁止多次提交
        mTxtViewSettlement.setEnabled(false);
        //**********  设置地址  **********//*
        String address = mUserAddr.getText().toString().trim();
        //**********  收货人姓名 **********//*
        String userName = mUserName.getText().toString().trim();
        //**********  收货人电话 **********//*
        String phone = mUserPhone.getText().toString().trim();
        //**********  订单备注 **********//*
        String describe = mRemarkTv.getText().toString().trim();


        if (!"".equals(address) && !"".equals(userName) && !"".equals(phone)) {
            mModelDoAdd.setAddress(address);
            mModelDoAdd.setLinkname(userName);
            mModelDoAdd.setTel(phone);
            mModelDoAdd.setDescribe(describe);
            mModelDoAdd.setIsredeem(isredeem);

            mBllOrder.sava(mModelDoAdd);
            LogUtils.e("=============提交订单=======2========>");
        } else if (UtilList.isEmpty(mListMaps)) {
            mBaseUtilAty.startActivity(AtySettlementAddress.class);
        } else {
           /* if (mDialog == null) {
                mDialog = new WgtAlertDialog();
            }
            mDialog.show(this,"确定","请选择收货地址！",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDialog.dismiss();
                    mBaseUtilAty.startActivity(AtyMyAddress.class);
                },false, false)

                {
                }*/
            Toast.makeText(this, "请选择收货地址", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.ib_shihuibi)
    public void onShihuibi(View v){
        if (isredeem.equals("0")){
            isredeem = "1";
            shihuiPrice = Double.valueOf(AffordApp.getInstance().getUserMoney());
            if (selfAllPrice > 0 && storeAllPrice > 0){
                mTxtViewPrice.setText("在线支付：￥" + UtilNumber.DoubleValueOf
                        (selfAllPrice - shihuiPrice + "") + "");
            }else {
                mTxtViewPrice.setText("在线支付：￥" + UtilNumber.DoubleValueOf
                        (selfAllPrice - shihuiPrice + storeAllPrice+ "") + "");
            }

           // shihuiPrice = (double) mMoneyMap.get("MONEY");
           // Drawable drawable = getResources().getDrawable(R.drawable.switch_on);
            mShihuibi.setBackgroundResource(R.drawable.switch_on);
        }else {
            isredeem = "0";
            shihuiPrice = 0;
            if (selfAllPrice > 0 && storeAllPrice > 0){
                mTxtViewPrice.setText("在线支付：￥" + UtilNumber.DoubleValueOf
                        (selfAllPrice - shihuiPrice + "") + "");
            }else {
                mTxtViewPrice.setText("在线支付：￥" + UtilNumber.DoubleValueOf
                        (selfAllPrice - shihuiPrice + storeAllPrice+ "") + "");
            }
            mShihuibi.setBackgroundResource(R.drawable.switch_off);
        }
        LogUtils.e("=============提交订单====抵消实惠币=======>" + shihuiPrice);
    }

    @OnClick(R.id.left_back)//返回
    public void onBackClick(View v) {
       onBackPressed();
    }

    @OnClick({(R.id.rlyt_address), (R.id.ibtn_userinfo)})
    public void onAddAdressClick(View v) {
        mBaseUtilAty.startActivity(AtyMyAddress.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CommConstans.ADDRESS.MARS:
                if (data != null && data.getExtras().size() > 0) {
                    Bundle _remBunlde = data.getExtras();
                    String remText = _remBunlde.getString(AtySettlementRemark.REMARK);
                    mRemarkTv.setText(remText);
                } else {
                    mRemarkTv.setText("");
                    mRemarkTv.setHint("备注（可选）");
                }
                break;
            default:
                break;
        }
    }

}
