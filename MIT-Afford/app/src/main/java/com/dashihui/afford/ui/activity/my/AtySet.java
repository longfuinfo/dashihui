package com.dashihui.afford.ui.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessCommon;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.ui.widget.WgtAlertDialog;
import com.dashihui.afford.util.UtilUpdateApp;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.Map;

public class AtySet extends BaseActivity {

    private BusinessCommon mBllCommon;
    private UtilUpdateApp mUpdateApp;

    @ViewInject(R.id.setting_update)
    private TextView mTxtUpdate;
    @ViewInject(R.id.setting_exit)
    private TextView mTxtExit;

    @ViewInject(R.id.server_phone)
    private TextView mServerPhone;
    @ViewInject(R.id.server_time)
    private TextView mServerTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_set);
        ViewUtils.inject(this);
        if (!AffordApp.isLogin()) {
            mTxtExit.setVisibility(View.GONE);
        }
    }

    //返回按钮，关于我们,检测升级,退出登录的点击事件
    @OnClick({R.id.set_back, R.id.setting_about, R.id.setting_update, R.id.setting_exit})
    public void onTvBtnSetClick(View v) {
        switch (v.getId()) {
            case R.id.set_back:
                onBackPressed();
                break;
            case R.id.setting_update://检测升级
                mTxtUpdate.setEnabled(false);
                mBllCommon = new BusinessCommon(this);
                mBllCommon.checkVersion();
                break;
            case R.id.setting_exit:
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
                                AffordApp.getInstance().exitLogin(AtySet.this);
                                mAtDialog.dismiss();
                                finish();
                            }
                        }, false, false, 0, null);
                break;
            case R.id.setting_about:
                //关于我们
                mBaseUtilAty.startActivity(AtySetAbout.class);
                break;
        }
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        mTxtUpdate.setEnabled(true);
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_COMMOON_CHECKVERSION://版本更新检查
                    LogUtils.e("onSuccess======版本更新检查========>" + beanSendUI.getInfo());
                    Map<String, Object> beanRegister = (Map<String, Object>) beanSendUI.getInfo();
                    mUpdateApp = new UtilUpdateApp(this);
                    mUpdateApp.startCheckVersion(beanRegister, true);
                    break;

                default:
                    LogUtils.e("onSuccess===default===========>" + beanSendUI);
                    break;
            }
        } else {
            LogUtils.e("onSuccess===============>" + beanSendUI);
        }
    }

    @Override
    public void onFailure(EtySendToUI error) {
        LogUtils.e("onFailure======AtyHome=========>" + error);
        mTxtUpdate.setEnabled(true);
        if (mDialog == null) {
            mDialog = new WgtAlertDialog();
        }
        mDialog.show(this,
                "确定",
                error.getInfo().toString(),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
    }

}
