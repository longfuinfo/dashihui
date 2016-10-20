package com.dashihui.afford.util.view;

import android.view.View;
import android.widget.EditText;

import com.dashihui.afford.util.number.UtilNumber;

import java.util.List;

/**
 * 
* @ClassName: UtilView 
* @Description: View工具类，用于对控件的处理 
* @author 吕军伟
* @date 2015年1月16日 上午10:41:52 
* @评审人:
* @维护人: 
* @version V1.0   
*
 */
public class UtilView {
	/**
	 * 
	* @Title: disableInteraction 
	* @Description: 禁用控件的交互操作 （如单击、编辑等)
	* @param @param mListViews    设定文件 
	* @return void    返回类型 
	* @author 吕军伟
	* @date 2015年1月16日 上午9:56:37 
	* @评审人:
	* @维护人: 
	* @version V1.0
	 */
	public static void disableInteraction(List<View> mListViews) {
		// TODO Auto-generated method stub
		for(View view :mListViews){
			view.setEnabled(false);
		}
	}

	/**
	 * 
	* @Title: activativeInteraction 
	* @Description: 激活控件的交互操作 （如单击、编辑等) 
	* @param @param mListViews    设定文件 
	* @return void    返回类型 
	* @author 吕军伟
	* @date 2015年1月16日 上午10:04:36 
	* @评审人:
	* @维护人: 
	* @version V1.0
	 */
	public static void activativeInteraction(List<View> mListViews) {
		// TODO Auto-generated method stub
		for(View view :mListViews){
			view.setEnabled(true);
		}
	}
	
	/**
	 * 
	* @Title: isNumberNotZero 
	* @Description: 输入框是否是有效数字 
	* @param @param editText
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @author 吕军伟
	* @date 2015年1月21日 上午11:22:39 
	* @评审人:
	* @维护人: 
	* @version V1.0
	 */
	public static boolean isNumberNotZero(EditText editText){
		boolean result = false;
		String strVal = editText.getText().toString().trim();
		if(!"".equals(strVal) &&!"0".equals(strVal)&& UtilNumber.isNumeric(strVal)){
			result = true;
		}
		return result;
	}
}
