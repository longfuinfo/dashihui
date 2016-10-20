package com.dashihui.afford.common.menu;

import android.view.View;

/**
 * 底部菜单实体
 * 
 * @author NiuFC
 * @date 2012-12-02
 * @version 1.0
 *
 */
public class WidgetMenuBtn {

	/** 按钮菜单文字 */
	private String text;

	/** 按钮菜单图片 */
	private int ImageResource;
	/** 背景资源 */
	private int backgroundResource;

	/**
	 * 点击事件。
	 */
	private View.OnClickListener clickListener;

	/**
	 * 是否当前已经选中的按钮，如果是则高亮，并且忽略点击事件。
	 */
	private boolean isCurrent = false;
	
	/**
	 * 字体点击时的颜色
	 */
	private int fontColor;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getBackgroundResource() {
		return backgroundResource;
	}

	public void setBackgroundResource(int backgroundResource) {
		this.backgroundResource = backgroundResource;
	}

	public View.OnClickListener getClickListener() {
		return clickListener;
	}

	public int getImageResource() {
		return ImageResource;
	}

	public void setImageResource(int imageResource) {
		ImageResource = imageResource;
	}

	public void setClickListener(View.OnClickListener clickListener) {
		this.clickListener = clickListener;
	}

	public boolean isCurrent() {
		return isCurrent;
	}

	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

	public int getFontColor() {
		return fontColor;
	}

	public void setFontColor(int fontColor) {
		this.fontColor = fontColor;
	}


}
