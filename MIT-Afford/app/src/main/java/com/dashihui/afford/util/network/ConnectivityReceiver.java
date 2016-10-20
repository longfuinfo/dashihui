package com.dashihui.afford.util.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;


/**
 * 
* @ClassName: ConnectivityReceiver 
* @Description: 网络监听工具类
* @author niufc
* @date 2015年1月16日 下午5:03:52 
* @评审人:
* @维护人: 
* @version V1.0   
*
 */
public class ConnectivityReceiver extends BroadcastReceiver {
	

	/**
	 * 
	* @ClassName: OnNetworkAvailableListener 
	* @Description: 回调函数
	* @author 吕军伟
	* @date 2015年1月16日 下午5:04:23 
	* @评审人:
	* @维护人: 
	* @version V1.0   
	*
	 */
	public static interface OnNetworkAvailableListener {
		/**
		 * 
		* @Title: onNetworkAvailable 
		* @Description: 网络恢复正常
		* @param     设定文件 
		* @return void    返回类型 
		* @author 吕军伟
		* @date 2015年1月16日 下午5:08:04 
		* @评审人:
		* @维护人: 
		* @version V1.0
		 */
		public void onNetworkAvailable();

		/**
		 * 
		* @Title: onNetworkUnavailable 
		* @Description: 网络不可用 
		* @param     设定文件 
		* @return void    返回类型 
		* @author 吕军伟
		* @date 2015年1月16日 下午5:07:53 
		* @评审人:
		* @维护人: 
		* @version V1.0
		 */
		public void onNetworkUnavailable();
	}

	private final ConnectivityManager connectivityManager;
	private OnNetworkAvailableListener onNetworkAvailableListener;
	// 网络连接是否正常
	private boolean connection = false;

	public ConnectivityReceiver(Context context) {
		connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		checkConnectionOnDemand();
	}

	public void bind(Context context) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(this, filter);
		checkConnectionOnDemand();
	}

	public void unbind(Context context) {
		context.unregisterReceiver(this);
	}

	/**
	 * 
	* @Title: checkConnectionOnDemand 
	* @Description: 检查网络连接状态
	* @param     设定文件 
	* @return void    返回类型 
	* @author 吕军伟
	* @date 2015年1月16日 下午5:05:02 
	* @评审人:
	* @维护人: 
	* @version V1.0
	 */
	private void checkConnectionOnDemand() {
		final NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if (info == null || info.getState() != State.CONNECTED) {
			if (connection == true) {
				connection = false;
				if (onNetworkAvailableListener != null)
					onNetworkAvailableListener.onNetworkUnavailable();
			}
		} else {
			if (connection == false) {
				connection = true;
				if (onNetworkAvailableListener != null)
					onNetworkAvailableListener.onNetworkAvailable();
			}
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (connection == true && intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
			connection = false;
			if (onNetworkAvailableListener != null) {
				onNetworkAvailableListener.onNetworkUnavailable();
			}
		} else if (connection == false && !intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
			connection = true;
			if (onNetworkAvailableListener != null) {
				onNetworkAvailableListener.onNetworkAvailable();
			}
		}
	}

	public boolean hasConnection() {
		return connection;
	}

	public void setOnNetworkAvailableListener(OnNetworkAvailableListener listener) {
		this.onNetworkAvailableListener = listener;
	}

}
