package com.dashihui.afford.ui.activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessCommon;
import com.dashihui.afford.business.BusinessUser;
import com.dashihui.afford.business.entity.EntityLocation;
import com.dashihui.afford.business.entity.EtyLogin;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.util.UtilCommon;
import com.dashihui.afford.util.UtilEncryptionDecryption;
import com.dashihui.afford.util.preferences.UtilPreferences;
import com.dashihui.afford.util.toast.UtilToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatReportStrategy;
import com.tencent.stat.StatService;

import java.util.Calendar;
import java.util.Map;

public class AtyWelCome extends BaseActivity {


    private BusinessCommon mBllCommon;
    @ViewInject(R.id.net_again)
    private TextView mTvBtnNetAgain;//再次获取网络按钮
    @ViewInject(R.id.rlyt_nonetwork)
    private RelativeLayout mRlytNoNetWork;//没有网络页面
    @ViewInject(R.id.lyt_welcome)
    private LinearLayout mLytWelcome;//欢迎页面布局

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_welcome);
        ViewUtils.inject(this);
        mBllCommon = new BusinessCommon(this);
        mBllCommon.mobileRegister();//注册手机
        // androidManifest.xml指定本activity最先启动
        // 因此，MTA的初始化工作需要在本onCreate中进行
        // 在startStatService之前调用StatConfig配置类接口，使得MTA配置及时生效
        initMTACofig();
    }

    /**
     * 腾讯云分析
     */
    private void initMTACofig() {
        // 读取系统设置
        String IsDebug = this.getResources().getString(R.string.debug);
        if (IsDebug.equals("true")) {
            // 在startStatService之前用StatConfig配置类接口，使得MTA配置及时生效
            initMTAConfig(true);
        } else {
            initMTAConfig(false);
        }
        // 和manifest.xml里配置的的一致
        String appKey = getResources().getString(R.string.MTA_KEY);
        // 开启
        try {
            StatService.startStatService(this, appKey,
                    com.tencent.stat.common.StatConstants.VERSION);
        } catch (MtaSDkException e) {
            LogUtils.e("error=============>腾讯云分析");
            e.printStackTrace();
        }
    }

    /**
     * 根据不同的模式，建议设置的开关状态，可根据实际情况调整，仅供参考。
     *
     * @param isDebugMode 根据调试或发布条件，配置对应的MTA配置
     */
    private void initMTAConfig(boolean isDebugMode) {
        if (isDebugMode) { // 调试时建议设置的开关状态
            // 查看MTA日志及上报数据内容
            StatConfig.setDebugEnable(true);
            // 禁用MTA对app未处理异常的捕获，方便开发者调试时，及时获知详细错误信息。
            StatConfig.setAutoExceptionCaught(false);
            // 关闭wifi下及时上报数据
            StatConfig.setEnableSmartReporting(false);
            // 调试时，使用实时发送
            StatConfig.setStatSendStrategy(StatReportStrategy.INSTANT);
        } else { // 发布时，建议设置的开关状态，请确保以下开关是否设置合理
            // 禁止MTA打印日志
            StatConfig.setDebugEnable(false);
            // 根据情况，决定是否开启MTA对app未处理异常的捕获
            StatConfig.setAutoExceptionCaught(true);
            // 选择默认的上报策略
            StatConfig.setStatSendStrategy(StatReportStrategy.APP_LAUNCH);
            // 关闭wifi下及时上报数据
            StatConfig.setEnableSmartReporting(true);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    /**
     * 暂停1.5秒进入引导页面
     *
     * @data 2013-5-28 上午9:44:54
     * @author NiuFC
     * @version 1.0
     */
    private void startNavi(final boolean isStart) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isStart) {
                    mBaseUtilAty.startActivity(AtyNavigation.class);
                }
                AtyWelCome.this.finish();
            }
        }.start();
    }


    /**
     * 暂停1.5秒进入登陆页面
     *
     * @data 2013-5-28 上午9:44:54
     * @author NiuFC
     * @version 1.0
     */
    private void startHome(final boolean isStart) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isStart) {
                    mBaseUtilAty.startActivity(AtyHome.class);
                } else {
                    mBaseUtilAty.startActivity(AtyLocation.class);
                }
                AtyWelCome.this.finish();
            }
        }.start();
    }


    @Override
    public void onSuccess(EtySendToUI info) {
        if (info != null) {
            switch (info.getTag()) {
                case AffConstans.BUSINESS.TAG_COMMOON_REGISTER://手机注册成功
                    LogUtils.e("onSuccess======手机注册成功========>" + info);
                    Map<String, Object> beanRegister = (Map<String, Object>) info.getInfo();
                    AffordApp.getInstance().getmAffordBean().setSIGNATURE(beanRegister.get("SIGNATURE") + "");
                    // 检查是否设置了自动登录
                    if (AffordApp.isAutoLogin(this)) {
                        // 查看自动登录设置是否超期（七天后注销自动登录）
                        if (UtilPreferences.getString(this, CommConstans.Login.AUTO_LOGIN_SET_TIME) != null) {
                            LogUtils.e("onSuccess======自动登录开启========>" + info);
                            long lastSetTime = Long.valueOf(UtilPreferences.getString(this, CommConstans.Login.AUTO_LOGIN_SET_TIME));
                            if ((Calendar.getInstance().getTimeInMillis() - lastSetTime) > 7 * 24 * 60 * 60 * 1000) {
                                // 自动登录设置已经超过七天
                                AffordApp.exitLogin(this);
                            } else {
                                // 读取本地存储的用户名及密码
                                if (UtilPreferences.getString(this, CommConstans.Login.SHPREFER_USER_NAME) != null
                                        && UtilPreferences.getString(this, CommConstans.Login.SHPREFER_USER_PSWD) != null) {
                                    // 启动登录
                                    BusinessUser bllUser = new BusinessUser(AtyWelCome.this);
                                    try {
                                        UtilEncryptionDecryption encry = new UtilEncryptionDecryption();
                                        LogUtils.e("onSuccess======自动登录开启========>" + info);
                                        LogUtils.e("解密后的密码============>" + encry.decrypt(UtilPreferences.getString(this, CommConstans.Login.SHPREFER_USER_PSWD)));
                                        String userName = UtilPreferences.getString(this, CommConstans.Login.SHPREFER_USER_NAME);
                                        String pwd = encry.decrypt(UtilPreferences.getString(this, CommConstans.Login.SHPREFER_USER_PSWD));
                                        bllUser.login(userName, pwd);
                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        LogUtils.e("解密后的密码====>" + e.getMessage());
                                        e.printStackTrace();
                                    }
                                } else {
                                    AffordApp.exitLogin(this);
                                }
                            }
                        } else {
                            AffordApp.exitLogin(this);

                        }
                    } else {
                        //转到首页
                    }
                    //转到首页
                    isFirstRun();
//                    startHome(true);
                    break;
                case AffConstans.BUSINESS.TAG_USER_LOGIN:
                    EtyLogin entityLogin = (EtyLogin) info.getInfo();
                    if (entityLogin != null) {
                        LogUtils.e("登录成功TOKEN=======>" + entityLogin.getTOKEN());
                        //存储到本地
                        AffordApp.getInstance().setUserLogin(entityLogin);
                        UtilPreferences.putString(this, CommConstans.Login.TOKEN, entityLogin.getTOKEN());
                        UtilEncryptionDecryption encry;
                        try {
                            encry = new UtilEncryptionDecryption();
                            // 读取本地存储的用户名及密码
                            if (UtilPreferences.getString(this, CommConstans.Login.SHPREFER_USER_NAME) != null
                                    && UtilPreferences.getString(this, CommConstans.Login.SHPREFER_USER_PSWD) != null) {
                                AffordApp.LOG_PHONE = UtilPreferences.getString(this, CommConstans.Login.SHPREFER_USER_NAME);
                                AffordApp.LOG_PSWD = encry.decrypt(UtilPreferences.getString(this, CommConstans.Login.SHPREFER_USER_PSWD));
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            //转到首页
                            isFirstRun();
//                            startHome(true);
                        }
                        LogUtils.e("entityLogin==========2222===>" + entityLogin.getUSER());
                        //根据社区ID锁定所在小区店铺信息
                        if(entityLogin.getUSER()!=null){
                            mBllCommon.getcommunity(entityLogin.getUSER().getCOMMUNITYID() + "");
                        }else {
                            LogUtils.e("entityLogin===null====>" + entityLogin.getUSER());
                        }

                    } else {
                        // 退出登录，跳转到主页
                        AffordApp.exitLogin(this);
                        //转到首页
                        isFirstRun();
//                        startHome(true);
                    }
                    break;
                case AffConstans.BUSINESS.TAG_COMMON_GETCOMMUNITY://根据社区ID获取本地商铺信息
                    LogUtils.e("onSuccess======定位本地商铺，社区信息========>" + info);
                    EntityLocation entityLocation = (EntityLocation) info.getInfo();
                    AffordApp.getInstance().setEntityLocation(entityLocation);
                    //转到首页
                    isFirstRun();
//                    startHome(true);
                    break;

                default:
                    LogUtils.e("onSuccess===default===========>" + info);
                    //转到首页
                    isFirstRun();
//                    startHome(true);
                    break;
            }
        } else {
            LogUtils.e("onSuccess======AtyAffordShop=========>" + info);
        }
    }

    @Override
    public void onFailure(EtySendToUI error) {
        LogUtils.e("onFailure====AtyAffordShop===========>" + error);

        mLytWelcome.setVisibility(View.GONE);
        mRlytNoNetWork.setVisibility(View.VISIBLE);
        UtilToast.show(this, "当前网络不可用，请检查网络连接！", Toast.LENGTH_SHORT);

//        if (mDialog == null) {
//            mDialog = new WgtAlertDialog();
//        }
//        mDialog.show(this,
//                "确定",
//                error.getInfo().toString(),
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mDialog.dismiss();
//                        AtyWelCome.this.finish();
//                    }
//                }, false, false);
    }


    //点击立即重试，再次获取网络，当前有可用网络时隐藏没有网络提示的界面，进入首页
    @OnClick(R.id.net_again)
    public void onGetNetWorkClick(View v) {
        if (UtilCommon.isNetworkAvailable(this)) {
//            UtilToast.show(getApplicationContext(), "当前有可用网络！", Toast.LENGTH_SHORT);
            mLytWelcome.setVisibility(View.VISIBLE);
            mRlytNoNetWork.setVisibility(View.GONE);
            mBaseUtilAty.startActivity(AtyHome.class);
        } else {
            UtilToast.show(getApplicationContext(), R.string.home_examine, Toast.LENGTH_SHORT);
        }
    }

    public void isFirstRun() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        //判断是否是第一次运行，如果不是第一次则用true标记，并写入isFirstRun标记，下次启动程序时做判断
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isFirstRun) {
            startNavi(true);
            LogUtils.e("===================>第一次运行");
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        } else {
            startHome(true);
            LogUtils.e("===================>不是第一次运行");
        }
    }
}
