package com.dashihui.afford.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.dashihui.afford.R;

/**
 * 模仿ProgressDialog等待框，使用自定义layout，可以显示文字
 * @author zhangbo
 */
public class WdtProDialog extends Dialog {

	private static WdtProDialog mDialog;
	private WdtProDialog(Context context) {
		super(context);
	}
	public static WdtProDialog getInstance(Context context){
		if (mDialog==null){
			mDialog = new WdtProDialog(context);
		}
		return mDialog;
	}

	public static WdtProDialog createDialog(Context context){
		mDialog = new WdtProDialog(context,R.style.dialog);
		mDialog.setContentView(R.layout.diaog_com_progress);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setCancelable(true);
		mDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		return mDialog;
	}

	private WdtProDialog(Context context, int theme) {
		super(context, theme);
		View view = LayoutInflater.from(context).inflate(R.layout.diaog_com_progress, null);
		ImageView img = (ImageView) view.findViewById(R.id.dialog_loading_img);
		TextView tv = (TextView) view.findViewById(R.id.dialog_txt);
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.common_progress_loading);
		img.startAnimation(animation);
		setContentView(view);
	}

	private WdtProDialog(Context context, String txt, int theme) {
		super(context, theme);
		View view = LayoutInflater.from(context).inflate(R.layout.diaog_com_progress, null);
		ImageView img = (ImageView) view.findViewById(R.id.dialog_loading_img);
		TextView tv = (TextView) view.findViewById(R.id.dialog_txt);
		tv.setVisibility(View.VISIBLE);
		tv.setText(txt);
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.common_progress_loading);
		img.startAnimation(animation);
		setContentView(view);
	}

}
