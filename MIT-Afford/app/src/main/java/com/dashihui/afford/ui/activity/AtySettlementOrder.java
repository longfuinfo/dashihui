package com.dashihui.afford.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.dashihui.afford.R;
import com.dashihui.afford.alipay.PayConstants;
import com.dashihui.afford.alipay.PayResult;
import com.dashihui.afford.alipay.PayResultActivity;
import com.dashihui.afford.business.BusinessOrder;
import com.dashihui.afford.business.BusinessServer;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.ui.widget.WgtAlertDialog;
import com.dashihui.afford.util.map.UtilMap;
import com.dashihui.afford.util.preferences.UtilPreferences;
import com.dashihui.afford.util.string.UtilString;
import com.dashihui.afford.wxapi.Constants;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.Map;

public class AtySettlementOrder extends BaseActivity {

    @ViewInject(R.id.ordernums)
    private TextView mOrdernums;
    @ViewInject(R.id.aty_confirm_paymoney)
    private TextView mConfirmPaymoney;

    @ViewInject(R.id.submit)
    private Button mSubmit;
    @ViewInject(R.id.raidoGroup_payType)
    private RadioGroup mRadioBtnPayType;

    private IWXAPI api;
    private PayReq mRequest;//调用微信支付

    private String orderPrice;//订单总钱数
    private String orderCode;//订单号
    private String mOrderPayType;//订单支付分类
    private BusinessOrder mBllOrder;
    private BusinessServer mBllServer;
    private String orderPriceStore;//订单门店总钱数
    private String orderPriceSelf;//订单直营总钱数

    /********
     * 订单传输键名
     *******/
    public final static String ORDER_PRICE = "orderPrice";
    public final static String ORDER_CODE = "orderCode";
    public final static String ORDER_PAY_TYPE = "order_pay_type";
    public final static String ORDER_PRICE_STORE = "orderPriceStore";
    public final static String ORDER_SELF = "orderPriceSelf";
    /********
     * 支付方式
     *******/
    public final static String ORDER_WEIXIN_PAY = "1";//微信支付
    public final static String ORDER_ZHIFUBAO_PAY = "2";//支付宝支付
    private String mPayType = ORDER_WEIXIN_PAY;//区分支付宝和微信支付,默认微信支付
    /*********
     * 存储订单信息键名
     *********/
    public final static String PREFERENCES_ORDER_CODE = "preferencesordercode";//存储订单号 键
    public final static String PREFERENCES_ORDER_TYPE = "preferencesordertype";//存储订单标志 键


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_settlement_order);
        ViewUtils.inject(this);
        api = WXAPIFactory.createWXAPI(this, null);

        mRequest = new PayReq();
        mBllOrder = new BusinessOrder(this);
        mBllServer = new BusinessServer(this);
//        LogUtils.e("===价格==onCreate=======>" + orderPrice);
        initPayType();
    }

    public void initPayType() {
        mRadioBtnPayType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                /****&&  选择付款途径   &&******/
                switch (checkedId) {
                    case R.id.wx_radioBtn://微信支付
                        mPayType = ORDER_WEIXIN_PAY;
//                        LogUtils.e("mPayType======微信支付========>" + mPayType);
                        break;
                    case R.id.alipay_radioBtn://支付宝支付
                        mPayType = ORDER_ZHIFUBAO_PAY;
//                        LogUtils.e("mPayType======支付宝支付=======>" + mPayType);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    @OnClick(R.id.submit)
    public void onSubmitClick(View v) {
        mSubmit.setEnabled(false);
        LogUtils.e("mPayType======mPayType====1111===>" + mPayType);
        if (ORDER_WEIXIN_PAY.equals(mPayType)) {
            LogUtils.e("mPayType======mPayType====2222===>" + mPayType);
            //mOrderPayType订单支付类型，根据不同类型调用不同的支付接口：1为商品订单，2为服务订单，3为服务家政订单
            if (api.isWXAppInstalled()){
                LogUtils.e("微信已经安装===============>");
                if (CommConstans.ORDER.ORDER_PAY.equals(mOrderPayType)) {
                    mBllOrder.pay(orderCode, mPayType);//商品订单支付
                } else if (CommConstans.ORDER.SERVER_ORDER_PAY.equals(mOrderPayType)) {
                    mBllServer.getOrderPay(orderCode,mPayType);//服务订单支付
                } else {
                    mBllServer.getSerOrderPay(orderCode, mPayType);//家政订单支付
                }
            }else {
                LogUtils.e("微信没有安装===============>");
            }
        } else {
            LogUtils.e("mPayType======mPayType====3333===>" + mPayType);
            if (CommConstans.ORDER.ORDER_PAY.equals(mOrderPayType)) {
                LogUtils.e("mPayType======mPayType====4444===>" + mPayType);
                mBllOrder.pay(orderCode, mPayType);//商品订单支付
            } else if (CommConstans.ORDER.SERVER_ORDER_PAY.equals(mOrderPayType)) {
                LogUtils.e("mPayType======mPayType====5555===>" + mPayType);
                mBllServer.getOrderPay(orderCode,mPayType);//服务订单支付
            } else {
                LogUtils.e("mPayType======mPayType====6666===>" + mPayType);
                mBllServer.getSerOrderPay(orderCode, mPayType);//家政订单支付
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        mSubmit.setEnabled(true);
        orderPrice = getIntent().getStringExtra(ORDER_PRICE);
        orderCode = getIntent().getStringExtra(ORDER_CODE);
        mOrderPayType = getIntent().getStringExtra(ORDER_PAY_TYPE);
        LogUtils.e("onResume========mOrderPayType=======>" + mOrderPayType);
        if (!UtilString.isEmpty(orderCode) && !UtilString.isEmpty(orderPrice)) {
            mOrdernums.setText(orderCode + "");
            mConfirmPaymoney.setText("￥" + orderPrice + "");
            //存储订单号
            UtilPreferences.putString(this, PREFERENCES_ORDER_CODE, orderCode);
            //存储订单标志
            UtilPreferences.putString(this, PREFERENCES_ORDER_TYPE, mOrderPayType + "");
            LogUtils.e("===订单号==getString=======>" + UtilPreferences.getString(this, PREFERENCES_ORDER_TYPE));
            LogUtils.e("===订单号==mOrderPayType=======>" + mOrderPayType);
        }else if(!UtilString.isEmpty(orderCode)&&!UtilString.isEmpty(orderPriceSelf)&&!UtilString.isEmpty(orderPriceStore)){

            //直营+门店 在线支付
            mOrdernums.setText(orderCode + "");
            mConfirmPaymoney.setText("￥" + (Double.valueOf(orderPriceStore) + Double.valueOf(orderPriceSelf))+ "");
            UtilPreferences.putString(this, PREFERENCES_ORDER_CODE, orderCode);
            UtilPreferences.putString(this, PREFERENCES_ORDER_TYPE, mOrderPayType + "");
        } else {
            Toast.makeText(AtySettlementOrder.this, "订单提交错误！", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @OnClick(R.id.ibtnBack)
    public void onBackClick(View view) {
        onBackPressed();
    }



    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_ORDER_PAY://订单支付
                    LogUtils.e("onSuccess====订单支付========>" + beanSendUI.getInfo());
                case AffConstans.BUSINESS.TAG_SERVICE_ORDER_PAY://服务订单支付
                    LogUtils.e("onSuccess=====服务订单支付========>" + beanSendUI.getInfo());
                case AffConstans.BUSINESS.TAG_SER_ORDER_PAY://服务家政订单支付
                    LogUtils.e("onSuccess==服务家政订单支付===========>" + beanSendUI.getInfo());
                    Map<String, Object> beanorder = (Map<String, Object>) beanSendUI.getInfo();
                    //微信支付
                    if (ORDER_WEIXIN_PAY.equals(mPayType)) {
                        if (!UtilMap.isEmpty(beanorder)) {
                            //在这里调起微信  打开下面的注释,输入自己的appId
//                            api.registerApp(Constants.APP_ID);
                            mRequest.appId = beanorder.get("APPID") + "";
                            mRequest.partnerId = beanorder.get("PARTNERID") + "";
                            mRequest.prepayId = beanorder.get("PREPAYID") + "";
                            mRequest.packageValue = beanorder.get("PACKAGE") + "";
                            mRequest.nonceStr = beanorder.get("NONCESTR") + "";
                            mRequest.timeStamp = beanorder.get("TIMESTAMP") + "";
                            mRequest.sign = beanorder.get("SIGN") + "";
                            api.sendReq(mRequest);
                            AtySettlementOrder.this.finish();
                        } else {
                            if (mDialog == null) {
                                mDialog = new WgtAlertDialog();
                            }
                            mDialog.show(this,
                                    "确定",
                                    "调用微信支付失败",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mDialog.dismiss();
                                            AtySettlementOrder.this.finish();
                                        }
                                    });
                        }
                    } else if (ORDER_ZHIFUBAO_PAY.equals(mPayType)) {

                        if (!UtilMap.isEmpty(beanorder)) {
                            //调用支付宝支付
                            pay(beanorder.get("PARAMS") + "");
                        } else {
                            if (mDialog == null) {
                                mDialog = new WgtAlertDialog();
                            }
                            mDialog.show(this,
                                    "确定",
                                    "调用支付宝失败",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mDialog.dismiss();
                                            AtySettlementOrder.this.finish();
                                        }
                                    });
                        }
                    }
                    //支付宝支付
                    break;
                default:
                    LogUtils.e("onSuccess===default===========>" + beanSendUI);
                    break;
            }
        } else {
            LogUtils.e("onSuccess===============>" + beanSendUI);
        }
        mSubmit.setEnabled(true);
    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        LogUtils.e("onFailure===============>" + beanSendUI.getInfo());
        mSubmit.setEnabled(true);
    }


    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(final String payInfo) {
        if (TextUtils.isEmpty(PayConstants.PARTNER) || TextUtils.isEmpty(PayConstants.RSA_PRIVATE)
                || TextUtils.isEmpty(PayConstants.SELLER)) {
            new AlertDialog.Builder(this)
                    .setTitle("警告")
                    .setMessage("缺少PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    AtySettlementOrder.this.finish();
                                }
                            }).show();
            return;
        }

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(AtySettlementOrder.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = PayConstants.SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     *
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PayConstants.SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(AtySettlementOrder.this, "支付成功", Toast.LENGTH_SHORT).show();

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(AtySettlementOrder.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(AtySettlementOrder.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    Intent intent = new Intent(AtySettlementOrder.this, PayResultActivity.class);
                    startActivity(intent);
                    AtySettlementOrder.this.finish();
                    break;

                default:
                    break;
            }
        }};
}
