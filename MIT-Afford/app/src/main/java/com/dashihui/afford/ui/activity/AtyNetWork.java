package com.dashihui.afford.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dashihui.afford.R;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.util.UtilCommon;
import com.dashihui.afford.util.toast.UtilToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class AtyNetWork extends BaseActivity {
    @ViewInject(R.id.net_again)
    private TextView net_again;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_network);
        ViewUtils.inject(this);
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {

    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {

    }

    //点击立即重试，再次获取网络，当前有可用网络时隐藏没有网络提示的界面，进入首页
    @OnClick(R.id.net_again)
    public void onGetNetWorkClick(View v) {
        if (UtilCommon.isNetworkAvailable(this)) {
            mBaseUtilAty.startActivity(AtyHome.class);
        } else {
            UtilToast.show(getApplicationContext(), R.string.home_examine, Toast.LENGTH_SHORT);
        }
    }
}
