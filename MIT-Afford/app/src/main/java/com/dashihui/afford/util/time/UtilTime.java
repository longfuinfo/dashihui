package com.dashihui.afford.util.time;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class UtilTime {

	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * long time to string
	 * 
	 * @param timeInMillis
	 * @param dateFormat
	 * @return
	 */
	public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
		return dateFormat.format(new Date(timeInMillis));
	}

	/**
	 * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @param timeInMillis
	 * @return
	 */
	public static String getTime(long timeInMillis) {
		return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
	}

	/**
	 * 格式化明细时间格式
	 * TODO(这里用一句话描述这个方法的作用) 
	 * @Title: getFormatTime 
	 * @param timeStr
	 * @return   设定文件 
	 * @return String    返回类型 
	 * @author 牛丰产 
	 * @date 2015年2月27日 上午9:35:55 
	 * @评审人: 
	 * @维护人: 
	 * @version V1.0
	 */
	public static String getFormatTime(String timeStr) {
		String timeformat = "--:--:--";
		if (timeStr.length() == 5) {
			// 加入“:”
			timeformat = timeStr.substring(0, 1) + ":"
					+ timeStr.substring(1, 3) + ":" + timeStr.substring(3, 5);
		} else {
			if (timeStr.length() == 6) {
				timeformat = timeStr.substring(0, 2) + ":"
						+ timeStr.substring(2, 4) + ":"
						+ timeStr.substring(4, 6);
			} else {
				timeformat = timeStr;
			}
		}
		return timeformat;
	}
	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static long getCurrentTimeInLong() {
		return System.currentTimeMillis();
	}

	/**
	 * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString() {
		return getTime(getCurrentTimeInLong());
	}

	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
		return getTime(getCurrentTimeInLong(), dateFormat);
	}
}
