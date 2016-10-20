package com.dashihui.afford.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessOrder;
import com.dashihui.afford.business.BusinessServer;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.ui.activity.AtyFragmentOrdert;
import com.dashihui.afford.ui.activity.AtyFragmentServerDetail;
import com.dashihui.afford.ui.activity.AtyOrdertDetail;
import com.dashihui.afford.ui.activity.AtySettlementOrder;
import com.dashihui.afford.util.number.UtilNumber;
import com.dashihui.afford.util.preferences.UtilPreferences;
import com.dashihui.afford.util.string.UtilString;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    final IWXAPI api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

    private BusinessOrder mBllOrder;
    private BusinessServer mBllServer;
    @ViewInject(R.id.padTxtView)
    private TextView mPadTxtView;
    @ViewInject(R.id.confirm_weipay_title)
    private TextView mTitle;
    private String mOrderCode;
    private int mOrderType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_entry_wx);
        ViewUtils.inject(this);
        api.handleIntent(getIntent(), this);
        mBllOrder = new BusinessOrder(this);
        mBllServer = new BusinessServer(this);
    }

    @Override
    protected void onResume() {
        mOrderCode = UtilPreferences.getString(this, AtySettlementOrder.PREFERENCES_ORDER_CODE);
        mOrderType = UtilNumber.IntegerValueOf(UtilPreferences.getString(this, AtySettlementOrder.PREFERENCES_ORDER_TYPE));
        super.onResume();
    }

    @OnClick(R.id.ibtnBack)
    public void onBackClick(View view) {
        onBackPressed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        //打印日志
        LogUtils.i("onPayFinish, errCode = " + baseResp.errCode);
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

            if (baseResp.errCode == 0) {//支付成功,跳转至支付成功页面
                LogUtils.e("onResp======微信支付======orderCode==========>" + mOrderCode);
                LogUtils.e("onResp======微信支付======orderType==========>" + mOrderType);
                mOrderCode = UtilPreferences.getString(this, AtySettlementOrder.PREFERENCES_ORDER_CODE);
                mOrderType = UtilNumber.IntegerValueOf(UtilPreferences.getString(this, AtySettlementOrder.PREFERENCES_ORDER_TYPE));
                if (!UtilString.isEmpty(mOrderCode) && !UtilString.isEmpty(mOrderType + "")) {
                    if (mOrderType == 1) {//标志为1时查询商品支付结果
                        LogUtils.e("onResp======微信支付======orderType====1======>" + mOrderType);
                        if (mBllOrder == null) {
                            mBllOrder = new BusinessOrder(this);
                        }
                        mBllOrder.queryPay(mOrderCode);
                    } else if (mOrderType == 2) {//标志为2时查询服务支付结果
                        LogUtils.e("onResp======微信支付======orderType=====2=====>" + mOrderType);
                        if (mBllServer == null) {
                            mBllServer = new BusinessServer(this);
                        }
                        mBllServer.getOrderQuery(mOrderCode);
                    } else {//标志为3时查询家政支付结果
                        LogUtils.e("onResp======微信支付======orderType=====3=====>" + mOrderType);
                        if (mBllServer == null) {
                            mBllServer = new BusinessServer(this);
                        }
                        mBllServer.getSerOrderQuery(mOrderCode);
                    }
                } else {
                    mPadTxtView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.shopcar_icon_payfailed), null, null);
//                    mPadTxtView.setText("很抱歉!！支付失败。"+mOrderType+"_"+mOrderCode);
                    mPadTxtView.setText("很抱歉！支付失败。");
                    mTitle.setText("支付失败");
                    LogUtils.e("支付成功====本地订单号或订单号标志存储异常==========>");
                }

            } else {
                LogUtils.e("onResp======订单号======orderType=====3=====>");
                mPadTxtView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.shopcar_icon_payfailed), null, null);
                mPadTxtView.setText("很抱歉！支付失败。");
                mTitle.setText("支付失败");
//                mPadTxtView.setText("很抱歉!!！支付失败。"+mOrderType+"_"+mOrderCode);
            }
        }else {
            mPadTxtView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.shopcar_icon_payfailed), null, null);
            mPadTxtView.setText("很抱歉！支付失败。");
            mTitle.setText("支付失败");
//                mPadTxtView.setText("很抱歉!!！支付失败。"+mOrderType+"_"+mOrderCode);
        }
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        LogUtils.e("onSuccess======订单号======onSuccess=========>");
        mPadTxtView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.shopcar_icon_paysuccess), null, null);
        mPadTxtView.setText("恭喜您！支付成功。");
        mTitle.setText("支付成功");
//        switch (beanSendUI.getTag()) {
//            case AffConstans.BUSINESS.TAG_ORDER_PAY://商品订单支付
//                mPadTxtView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.shopcar_icon_paysuccess), null, null);
//                mPadTxtView.setText("恭喜您！支付成功。");
//                break;
//            case AffConstans.BUSINESS.TAG_SERVICE_ORDER_PAY:
//                mPadTxtView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.shopcar_icon_paysuccess), null, null);
//                mPadTxtView.setText("恭喜您！支付成功。");
//                break;
//            case AffConstans.BUSINESS.TAG_SER_ORDER_PAY:
//                mPadTxtView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.shopcar_icon_paysuccess), null, null);
//                mPadTxtView.setText("恭喜您！支付成功。");
//                break;
//            default:
//                break;
//        }

//        if (mDialog == null) {
//            mDialog = new WgtAlertDialog();
//        }
//        mDialog.show(this,
//                "确定",
//                "支付成功！",
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mDialog.dismiss();
//                        WXPayEntryActivity.this.finish();
//                    }
//                });
    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        LogUtils.e("onFailure======订单号======onFailure=========>");
        mPadTxtView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.shopcar_icon_payfailed), null, null);
        mPadTxtView.setText("很抱歉！支付失败。");
//        mPadTxtView.setText(beanSendUI.getInfo()+"");
//        switch (beanSendUI.getTag()) {
//            case AffConstans.BUSINESS.TAG_ORDER_PAY://商品订单支付
//                mPadTxtView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.shopcar_icon_payfailed), null, null);
//                mPadTxtView.setText("很抱歉！支付失败。");
//                break;
//            case AffConstans.BUSINESS.TAG_SERVICE_ORDER_PAY://服务订单支付
//                mPadTxtView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.shopcar_icon_payfailed), null, null);
//                mPadTxtView.setText("很抱歉！支付失败。");
//                break;
//            case AffConstans.BUSINESS.TAG_SER_ORDER_PAY://家政订单支付
//                mPadTxtView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.shopcar_icon_payfailed), null, null);
//                mPadTxtView.setText("很抱歉！支付失败。");
//                break;
//            default:
//                break;
//        }


//        if (mDialog == null) {
//            mDialog = new WgtAlertDialog();
//        }
//        mDialog.show(this,
//                "确定",
//                "支付失败！",
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mDialog.dismiss();
//                        WXPayEntryActivity.this.finish();
//                    }
//                }, false, false);
    }

    /**
     * 点击查看详情跳转至订单详情页
     * @param v
     */
    @OnClick(R.id.todetail)
    public void onToDetailClick(View v) {
        if (mOrderType == 1) {
            Intent intent = new Intent(this, AtyFragmentOrdert.class);
            intent.putExtra(AtySettlementOrder.ORDER_CODE, mOrderCode);
            startActivity(intent);
        }else {
            Intent mapIntent = new Intent(this, AtyFragmentServerDetail.class);
            if (3 == mOrderType) {//家政
                mapIntent.putExtra(AtySettlementOrder.ORDER_PAY_TYPE, CommConstans.ORDER.HOUSE_ORDER_PAY);
            } else if (2 == mOrderType) {//其他服务
                mapIntent.putExtra(AtySettlementOrder.ORDER_PAY_TYPE, CommConstans.ORDER.SERVER_ORDER_PAY);
            }
            mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, mOrderCode);
            startActivity(mapIntent);
        }
        finish();
    }

}
