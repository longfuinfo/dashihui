package com.dashihui.afford.util.screen;

import android.content.Context;
import android.view.WindowManager;

import com.dashihui.afford.AffordApp;


/**
 * DP和SP之间的转换 UtilScreen
 * <ul>
 * <strong>Convert between dp and sp</strong>
 * <li>{@link UtilScreen#pxToDp(float)}</li>
 * <li>{@link UtilScreen#pxToDp(float)}</li>
 * </ul>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-14
 */
public class UtilScreen {
	
	private static Context mContext = AffordApp.getApplication().getApplicationContext();

	public static float dpToPx(float dp) {
		if (mContext == null) {
			return -1;
		}
		return dp * mContext.getResources().getDisplayMetrics().density;
	}

	public static float pxToDp(float px) {
		if (mContext == null) {
			return -1;
		}
		return px / mContext.getResources().getDisplayMetrics().density;
	}

	public static int dpToPxInt(float dp) {
		return (int) (dpToPx(dp) + 0.5f);
	}

	public static float pxToDpCeilInt(float px) {
		return (int) (pxToDp(px) + 0.5f);
	}

	public static float getScreenHeight() {
		WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		return windowManager.getDefaultDisplay().getHeight();
	}

	public static float getScreenWidth() {
		WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		return windowManager.getDefaultDisplay().getWidth();
	}
}
