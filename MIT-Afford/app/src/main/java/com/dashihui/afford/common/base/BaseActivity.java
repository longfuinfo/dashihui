package com.dashihui.afford.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.sqlite.SqliteShoppingCart;
import com.dashihui.afford.thirdapi.greedsqlite.ShoppingCart;
import com.dashihui.afford.ui.widget.WdtProDialog;
import com.dashihui.afford.ui.widget.WgtAlertDialog;
import com.dashihui.afford.util.array.UtilActivity;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.network.ConnectivityReceiver;
import com.dashihui.afford.util.number.UtilNumber;
import com.lidroid.xutils.util.LogUtils;
import com.tencent.stat.StatService;

import java.util.List;


public abstract class BaseActivity extends Activity implements AffRequestCallBack {

	protected WgtAlertDialog mDialog;
	protected WdtProDialog mProDialog =null;
	private final static String TAG = "BaseActivity";
	public static ConnectivityReceiver mConnReceiver = null;
	protected UtilActivity mBaseUtilAty = new UtilActivity(this);
	@Override
	protected  void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AffordApp.getInstance().getAppManager().addActivity(this);
	}

	/**
	 * 显示过渡加载框
	 * @param context
	 */
	protected void showProDialog(Context context){
		if (mProDialog != null && mProDialog.isShowing()) {
			return;
		}
		if (mProDialog == null){
			mProDialog = WdtProDialog.createDialog(context);
		}
		mProDialog.show();
	}

	protected void dissProDialog(){
		if (mProDialog!=null && mProDialog.isShowing()){
			mProDialog.dismiss();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// TODO 腾讯统计
		StatService.onResume(this);

		if(mConnReceiver==null){
			mConnReceiver = new ConnectivityReceiver(this);
			mConnReceiver.setOnNetworkAvailableListener(new ConnectivityReceiver.OnNetworkAvailableListener() {
				
				@Override
				public void onNetworkUnavailable() {
					// TODO Auto-generated method stub
					Log.i(TAG, "======>已断开与互联网的连接");
//					Toast.makeText(getApplicationContext(), R.string.home_no_net, Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onNetworkAvailable() {
					// TODO Auto-generated method stub
					//Toast.makeText(getApplicationContext(), "0网络恢复正常啦", Toast.LENGTH_LONG).show();
					Log.i(TAG, "======>网络恢复正常啦");
//					onResume();
				}
			});
		}
		mConnReceiver.bind(this);
	}


	/**
	 * 底部购物车显示
	 */
	protected void sendShopChartBroadcast(){
		int shopCartNum = 0;
		if(AffordApp.getInstance().getEntityLocation()!=null && AffordApp.getInstance().getEntityLocation().getSTORE()!=null){
			List<ShoppingCart> shopCartList = SqliteShoppingCart.getInstance(BaseActivity.this).getListByShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
			if (!UtilList.isEmpty(shopCartList)){
				for (int k =0;k<shopCartList.size();k++){
					shopCartNum += UtilNumber.IntegerValueOf(shopCartList.get(k).getBuynum());
				}

			}
		}
		LogUtils.e("BroadcastReceiver===============>" + shopCartNum);
		Intent intent = new Intent();  //Itent就是我们要发送的内容
		intent.putExtra("data", shopCartNum);
		intent.setAction(CommConstans.REGISTER.BROADCAST_INTENT_ACTION);   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
		sendBroadcast(intent);   //发送广播
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (mConnReceiver!=null){
			mConnReceiver.unbind(this);
			mConnReceiver =null;
		}
		super.onPause();
		// TODO 腾讯统计
		StatService.onPause(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
