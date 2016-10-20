package com.dashihui.afford.ui.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessUser;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.ui.activity.AtyRegisterVerify;
import com.dashihui.afford.ui.widget.WgtAlertDialog;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.Map;


public class AtyMyAccount extends BaseActivity {
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    public static final String BasePath = "/mnt/sdcard/DaShiHui/";

    @ViewInject(R.id.img_headpic)//用户头像
    private ImageView mImgUserHead;
    @ViewInject(R.id.tv_username)//用户昵称
    private TextView mTvUserName;
    @ViewInject(R.id.tv_phone)//绑定手机
    private TextView mTvPone;
    @ViewInject(R.id.tvBtn_exit)
    private TextView mTvBtnExit;

    private Map<String, Object> mInfoMap;
    private ImageView mUserHeadPic;
//    private BitmapUtils mBitmapUtils;

    private BusinessUser mBllUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_my_account);
        ViewUtils.inject(this);
        mBllUser = new BusinessUser(this);
        mBllUser.getInfo();
        if (!AffordApp.isLogin()) {
            mTvBtnExit.setVisibility(View.GONE);
        }
    }

    //头像
    @OnClick(R.id.lyt_headpic)
    public void onHeadpicClick(View v) {
//        mHeadPicPopu = new WdtMyHeadPopuWindow(AtyMyAccount.this, onPopItemClick);
//        mHeadPicPopu.showAtLocation(findViewById(R.id.img_headpic), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    //PopuWindow子项监听
//    private View.OnClickListener onPopItemClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//        }
//    };


    //用户名
    @OnClick(R.id.lyt_username)
    public void onUserNameClick(View v) {
//        mBaseUtilAty.startActivity(AtyShoppingCart.class);
    }

    //账户密码
    @OnClick(R.id.lyt_password)
    public void onAccUsePwdClick(View v) {

        Bundle bundle1 = new Bundle();
        bundle1.putString(CommConstans.REGISTER.INTENT_KEY_TYPE, CommConstans.REGISTER.INTENT_VALUE_PWDRESET);
        mBaseUtilAty.startActivity(AtyRegisterVerify.class, bundle1, true);
    }

    //绑定手机号
    @OnClick(R.id.lyt_phone)
    public void onPhoneClick(View v) {

    }

    //管理收货地址
    @OnClick(R.id.tv_addr)
    public void onMangerAddrClick(View v) {
        mBaseUtilAty.startActivity(AtyMyAddress.class);
    }

    //退出当前账户
    @OnClick(R.id.tvBtn_exit)
    public void onTvBtnExitClick(View v) {
        //退出登录
        final WgtAlertDialog mAtDialog = new WgtAlertDialog();
        mAtDialog.show(this,
                "取消", "确定",
                "确认退出当前账户？",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAtDialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AffordApp.getInstance().exitLogin(AtyMyAccount.this);
                        mAtDialog.dismiss();
                        finish();
                    }
                }, false, false, 0, null);
    }

    //返回
    @OnClick(R.id.ibtn_back)
    public void onIBtnBackClick(View v) {
        onBackPressed();
    }


    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        LogUtils.e("AtyMyAccount=====onSuccess========beanSendUI====>" + beanSendUI);
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_USER_INFO://获取登录用户个人信息
                    LogUtils.e("AtyMyAccount=====onSuccess========beanSendUI.getInfo()====>" + beanSendUI.getInfo());
                    mInfoMap = (Map<String, Object>) beanSendUI.getInfo();
//                    String uri = AffConstans.PUBLIC.ADDRESS_IMAGE + mInfoMap.get("AVATOR");
//                    LogUtils.e("uri====用户头像=====>" + uri);
                    LogUtils.e("mTvUserName=========>" + mInfoMap.get("NICKNAME") + "");
                    mTvUserName.setText(mInfoMap.get("NICKNAME") + "");
                    mTvPone.setText(mInfoMap.get("NICKNAME") + "");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        LogUtils.e("AtyMyAccount=====onSuccess========beanSendUI====>" + beanSendUI);
    }
}
