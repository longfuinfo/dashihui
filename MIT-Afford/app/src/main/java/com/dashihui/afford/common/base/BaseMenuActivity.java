package com.dashihui.afford.common.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.common.menu.WidgetMenuBtn;
import com.dashihui.afford.common.menu.WidgetMenuLayout;
import com.dashihui.afford.common.menu.entry.EntyMenuSource;
import com.dashihui.afford.common.menu.entry.EntyMenuSourceBase;
import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: ActiyBaseMenu
 * @Description: 菜单基类
 * @author NiuFC
 * @date 2014年3月28日 下午4:23:34
 * @维护人:
 * @version V1.0
 * 
 */
public abstract class BaseMenuActivity extends BaseActivity {
	
	private final static String TAG = "BaseMenuActivity";

	/************************** 控件 ****************************************/
	private LayoutInflater mInflater; // 实例化layout使用的类
	protected WidgetMenuLayout bottomMenuLayout; // 底部菜单UI部分
	protected View contentView; // 页面中间UI部分



	/**
	 * 为避免子类错误调用，覆盖该方法，并定义为空方法。
	 */
	public void setContentView(int layoutResID) {}

	/**
	 * setContentView(R.layout.xxxxLayoutId);
	 * 原本在Activity的onCreate方法中调用的setContentView(R.layout.xxxxLayoutId);
	 * 现在从该方法返回。
	 * 
	 * @return
	 */
	public abstract int getContentViewLayoutResId();

	/**
	 * 子类实现后，在原来的onCreate方法中内容移到这里来操作，先于initView()执行。
	 * 
	 * @param savedInstanceState
	 */
	protected abstract void onCreatOverride(Bundle savedInstanceState);


	protected void onCreate(Bundle savedInstanceState) {
		LogUtils.e("onCreate========BaseMenuActivity==========>");
		bottomMenuLayout = new WidgetMenuLayout(this);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		bottomMenuLayout.setOrientation(WidgetMenuLayout.VERTICAL);
		bottomMenuLayout.setLayoutParams(layoutParams);
		setContentView(bottomMenuLayout);
		// 将业务自定义的layout实例化出来，设置给完整页面Layout的内容部分。其中，获取业务自定义layoutID的时候回调了子类的方法。
		this.mInflater = LayoutInflater.from(this);
		contentView = mInflater.inflate(getContentViewLayoutResId(), null);
		bottomMenuLayout.addView(contentView);
		// 回调子类,正常处理onCreate方法。
		onCreatOverride(savedInstanceState);
		// 回调子类，获得所有的底部菜单按钮的集合，并进行处理，将按钮绑定到菜单里。
		bottomMenuLayout.setButtonList(getButtonList());
		bottomMenuLayout.processInitButton();


		super.onCreate(savedInstanceState);

	}

	/**
	 * 是否刷新界面
	 * @return
	 */
	protected boolean isRefresh(String storetitle){
		String storeString = AffordApp.getInstance().getEntityLocation().getSTORE().getTITLE();
		if (!storetitle.equals(storeString)){
			return true;
		}else {
			return false;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		LogUtils.e("onResume====BaseMenuActivity=======>");
		super.onResume();
	}

	/**
	 * @Description: 根据返回值确定所在页底部菜单样式
	 * @param @return
	 * @return List<WigBotButton>
	 * @author NiuFC
	 * @date 2014年3月28日 下午4:27:11
	 * @维护人:
	 * @version V1.0
	 */
	public List<WidgetMenuBtn> getButtonList() {
		LogUtils.e("getButtonList====BaseMenuActivity=========初始化按钮的初始数据对象=====>" );
		List<WidgetMenuBtn> buttons = new ArrayList<>();
		int menu = getButtonType();
		EntyMenuSourceBase source = new EntyMenuSource();
		String[] menuName = source.getMenuName();
		int[] menuid = source.getMenuid();
		int[] bgRes = source.getBgRes();
		int[] fontColor = source.getFontColor();
		// 根据子Activity 改变高亮图标
		if (menu < menuName.length) {
			bgRes[menu] = source.getSelectedBgdrawable();
			menuid[menu] = source.getSelectedBgdrawable(menu);
			fontColor[menu] = source.getSelectedFontdrawable();
		}

		for (int i = 0; i < menuName.length; i++) {
			WidgetMenuBtn oneBottomButton = new WidgetMenuBtn();
			oneBottomButton.setText(menuName[i]);
			oneBottomButton.setFontColor(fontColor[i]);
			oneBottomButton.setImageResource(menuid[i]);
			oneBottomButton.setBackgroundResource(bgRes[i]);
			buttons.add(oneBottomButton);
		}
		return buttons;
	}

	/**
	 * @Description: 底部菜单状态
	 * @param @return
	 * @return int
	 * @author NiuFC
	 * @date 2014年3月28日 下午4:28:12
	 * @维护人:
	 * @version V1.0
	 */
	public abstract int getButtonType();

	@Override
	protected void onDestroy() {
		if (mInflater != null) {
			mInflater = null;
		}
		if (bottomMenuLayout != null) {
			//注销监听
			bottomMenuLayout.unregisterReceiver();
			bottomMenuLayout = null;
		}
		if (contentView != null) {
			contentView = null;
		}
		super.onDestroy();
	}
	
	
}
