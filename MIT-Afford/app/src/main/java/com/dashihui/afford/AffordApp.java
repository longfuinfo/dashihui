package com.dashihui.afford;

import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.dashihui.afford.business.entity.EntityLocation;
import com.dashihui.afford.business.entity.EtyBuilding;
import com.dashihui.afford.business.entity.EtyLogin;
import com.dashihui.afford.business.entity.EtyMoney;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.thirdapi.greedsqlite.DaoMaster;
import com.dashihui.afford.thirdapi.greedsqlite.DaoSession;
import com.dashihui.afford.ui.AffordBean;
import com.dashihui.afford.util.preferences.UtilPreferences;
import com.lidroid.xutils.util.LogUtils;
import com.ta.TAApplication;

import java.util.List;


public class AffordApp extends TAApplication {

	private static AffordBean mAffordBean;
	/*
	 * DEBUG状态，决定日志输出的级别
	 */
	public static Boolean DEBUG;

	/**商铺实体*/
	private EntityLocation entityLocation;
	/**用户登录实体****/
	private EtyLogin userLogin;
	/**实惠币**/
	private String userMoney;
	/**用户邀请好友**/
	private int userFriend;


	private List<EtyBuilding> mListEtyBuilding;


	/**
	 * 数据库操作对象
	 */
	public static final String DB_NAME = "daoShoppingCart_db";
	private static DaoMaster daoMaster;
	private static DaoSession daoSession;


	public static String LOG_PHONE ="";

	/** 登录密码 */
	public static String LOG_PSWD = "";


	@Override
	public void onCreate() {
		super.onCreate();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
		/** 初始化程序的配置数据字符串 */
		initSerURLAndVersion(this);
	}

	/**
	 * return DaoMaster
	 *
	 * @param context
	 */
	public static DaoMaster getDaoMaster(Context context) {
		if (daoMaster == null) {
			DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME,
					null);
			daoMaster = new DaoMaster(helper.getWritableDatabase());
		}
		return daoMaster;
	}

	/**
	 * return DaoSession
	 *
	 * @param context
	 */
	public static DaoSession getDaoSession(Context context) {
		if (daoSession == null) {
			if (daoMaster == null) {
				daoMaster = getDaoMaster(context);
			}
			daoSession = daoMaster.newSession();
		}
		return daoSession;
	}


	/**
	 * 得到全局常量中版本号和服务地址
	 * 
	 * @param context
	 */
	public static void initSerURLAndVersion(final Context context) {
		DEBUG = getDebug(context.getString(R.string.debug));
	}

	/**
	 * 
	 * @Title: getDebug
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param strDebug
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @author 牛丰产
	 * @date 2015-1-9 下午3:17:36
	 * @维护人:
	 * @version V1.0
	 */
	private static boolean getDebug(String strDebug) {
		if ("true".equals(strDebug)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @Description: 单例
	 * @param @return
	 * @author NiuFC
	 * @date 2014年3月29日 上午11:03:51
	 * @维护人:
	 * @version V1.0
	 */
	public static AffordApp getInstance() {
		return (AffordApp) getApplication();
	}

	/**
	 * 单例获取Bean
	 * @return
	 */
	public AffordBean getmAffordBean() {
		if (mAffordBean==null){
			mAffordBean = AffordBean.getAppBean();
		}
		return mAffordBean;
	}

	/**
	 * TODO 判断是否登录过
	 *
	 * @Title: isLogin
	 * @return 设定文件
	 * @return boolean 返回类型
	 * @author 牛丰产
	 * @date 2014年10月9日 下午4:47:43
	 * @维护人:
	 * @version V1.0
	 */
	public static boolean isLogin() {
		if (LOG_PHONE != null && LOG_PHONE !="") {
			LogUtils.e("已经登录=====>");
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否自动登录
	 * @param context
	 * @return
	 */
	public static boolean isAutoLogin(Context context) {
		if (UtilPreferences.getAll(context) != null&& UtilPreferences.getBoolean(context, CommConstans.Login.SHPREFER_AUTO_LOGIN)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 退出登录
	 *
	 * @Title: isLoginOut
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @author LvJW
	 * @date 2014年10月20日 下午3:46:55
	 * @维护人:
	 * @version V1.0
	 */
	public static void exitLogin(Context context) {
		// 清空全局登录用户名
		AffordApp.LOG_PHONE = null;
//		AffordApp.NICK_NAME = null;
		// 清空全局登录密码
		AffordApp.LOG_PSWD = null;
		if (UtilPreferences.getAll(context) != null) {
			UtilPreferences.clearData(context);
		}
	}

	@Override
	public void onTerminate() {
		// 应用结束 在此保存数据
		// ...在此写代码
		super.onTerminate();
	}

	@Override
	public void onLowMemory() {
		// 系统内存过低，在此部署一些内存回收策略
		super.onLowMemory();
	}

	@Override
	protected void onAfterCreateApplication() {
		super.onAfterCreateApplication();
		//注册业务
	}



	public EntityLocation getEntityLocation() {
		return entityLocation;
	}

	public void setEntityLocation(EntityLocation entityLocation) {
		this.entityLocation = entityLocation;
	}

	public EtyLogin getUserLogin() {
		return userLogin;
	}
	public String getUserMoney(){
		return userMoney;
	}
	public void setUserMoney(String userMoney) {
		this.userMoney = userMoney;
	}

	public int getUserFriend(){
		return userFriend;
	}
	public void setUserFriend(int userFriend){
		this.userFriend = userFriend;
	}


	public void setUserLogin(EtyLogin userLogin) {
		this.userLogin = userLogin;
	}


	public List<EtyBuilding> getmListEtyBuilding() {
		return mListEtyBuilding;
	}

	public void setmListEtyBuilding(List<EtyBuilding> mListEtyBuilding) {
		this.mListEtyBuilding = mListEtyBuilding;
	}
}
