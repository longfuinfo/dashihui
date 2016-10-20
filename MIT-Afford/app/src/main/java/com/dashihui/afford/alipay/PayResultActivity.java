package com.dashihui.afford.alipay;

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
import com.dashihui.afford.ui.activity.AtyFragmentService;
import com.dashihui.afford.ui.activity.AtyOrdertDetail;
import com.dashihui.afford.ui.activity.AtySettlementOrder;
import com.dashihui.afford.util.map.UtilMap;
import com.dashihui.afford.util.number.UtilNumber;
import com.dashihui.afford.util.preferences.UtilPreferences;
import com.dashihui.afford.util.string.UtilString;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.Map;


public class PayResultActivity extends BaseActivity {

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
		setContentView(R.layout.pay_result_zfb);
		ViewUtils.inject(this);

		mBllOrder = new BusinessOrder(this);
		mBllServer = new BusinessServer(this);

		mOrderCode = UtilPreferences.getString(this, AtySettlementOrder.PREFERENCES_ORDER_CODE);
		mOrderType = UtilNumber.IntegerValueOf(UtilPreferences.getString(this, AtySettlementOrder.PREFERENCES_ORDER_TYPE));
		if (!UtilString.isEmpty(mOrderCode) && !UtilString.isEmpty(mOrderType + "")) {
			if (mOrderType == 1) {//标志为1时查询商品支付结果
				LogUtils.e("onResp======支付宝======orderType====1======>" + mOrderType);
				if (mBllOrder == null) {
					mBllOrder = new BusinessOrder(this);
				}
				mBllOrder.queryPay(mOrderCode);
			} else if (mOrderType == 2) {//标志为2时查询服务支付结果
				LogUtils.e("onResp======支付宝======orderType=====2=====>" + mOrderType);
				if (mBllServer == null) {
					mBllServer = new BusinessServer(this);
				}
				mBllServer.getOrderQuery(mOrderCode);
			} else {//标志为3时查询家政支付结果
				LogUtils.e("onResp======支付宝======orderType=====3=====>" + mOrderType);
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
			LogUtils.e("支付失败====本地订单号或订单号标志存储异常==========>");
		}
	}

	@OnClick(R.id.ibtnBack)
	public void onBackClick(View view) {
		onBackPressed();
	}

	@Override
	public void onSuccess(EtySendToUI beanSendUI) {
		if (beanSendUI != null) {
			Map<String,Object> mapObject = (Map<String,Object>)beanSendUI.getInfo();
			if (!UtilMap.isEmpty(mapObject) && "0".equals(mapObject.get("PAYSTATE")+"")){
				mPadTxtView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.shopcar_icon_payfailed), null, null);
				mPadTxtView.setText("很抱歉！支付失败。");
				mTitle.setText("支付失败");
			}else {
				mPadTxtView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.shopcar_icon_paysuccess), null, null);
				mPadTxtView.setText("恭喜您！支付成功。");
				mTitle.setText("支付成功");

			}
		}else {
			mPadTxtView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.shopcar_icon_payfailed), null, null);
			mPadTxtView.setText("很抱歉！支付失败。");
			mTitle.setText("支付失败");
		}

	}

	@Override
	public void onFailure(EtySendToUI beanSendUI) {
		mPadTxtView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.shopcar_icon_payfailed), null, null);
		mPadTxtView.setText("很抱歉！支付失败。");
		mTitle.setText("支付失败");
	}

	/**
	 * 点击查看详情跳转至订单列表页
	 * @param v
	 */
	@OnClick(R.id.todetail)
	public void onToDetailClick(View v) {
		if (mOrderType == 1) {
			Intent intent = new Intent(this, AtyFragmentOrdert.class);
			intent.putExtra(AtySettlementOrder.ORDER_CODE, mOrderCode);
			startActivity(intent);
			finish();
		}else {
			Intent mapIntent = new Intent(this, AtyFragmentService.class);
			if (3 == mOrderType) {//家政
				mapIntent.putExtra(AtySettlementOrder.ORDER_PAY_TYPE, CommConstans.ORDER.HOUSE_ORDER_PAY);
			} else if (2 == mOrderType) {//其他服务
				mapIntent.putExtra(AtySettlementOrder.ORDER_PAY_TYPE, CommConstans.ORDER.SERVER_ORDER_PAY);
			}
			mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, mOrderCode);
			startActivity(mapIntent);
			finish();
		}

	}
}
