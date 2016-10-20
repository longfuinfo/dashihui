package com.dashihui.afford.ui.activity.my;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessUser;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.Map;

/**
 * Created by Administrator on 2016/4/6.
 */
public class AtyWhatShihuibi extends BaseActivity{
//    @ViewInject(R.id.wallet_num)
//    private TextView tWalletNum;//实惠币
    @ViewInject(R.id.tv_share)
    private TextView tTvShare;//分享



    private BusinessUser mBllUser;
    private Map<String, Object> mMoneyMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_what_shihuibi);
        ViewUtils.inject(this);
        tTvShare.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        if (AffordApp.isLogin()){
            if (AffordApp.getInstance().getUserLogin().getUSER().getLEVEL() == 2){
                tTvShare.setText("点击分享给好友");

            }else{
               tTvShare.setText("如何成为白金会员");
            }
        }
//        mBllUser = new BusinessUser(this);
//        mBllUser.getMoney();

    }

    @Override
    public void onSuccess(EtySendToUI successEty) {
        if (successEty != null) {
            switch (successEty.getTag()) {
                case AffConstans.BUSINESS.TAG_USER_MONEY://实惠币
                    LogUtils.e("onSuccess==============>" + successEty.getInfo());
                    mMoneyMap = (Map<String, Object>) successEty.getInfo();
                   // tWalletNum.setText(mMoneyMap.get("MONEY") + "");

                    LogUtils.e("onSuccess===大实惠===========>" + mMoneyMap.get("MONEY")+"");
                    break;
                default:
                    LogUtils.e("onSuccess===default===========>" + "");
                    break;

            }
        }

    }

    @Override
    public void onFailure(EtySendToUI failureEty) {
        LogUtils.e("onSuccess===default===========>" + failureEty.getInfo());
    }



    @OnClick(R.id.left_back_collect)
    public void onBack(View v){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onShare(View v){
        if (AffordApp.isLogin()){
            if (AffordApp.getInstance().getUserLogin().getUSER().getLEVEL() == 2){
                mBaseUtilAty.startActivity(AtyMyMemberShare.class);
            }else{
                mBaseUtilAty.startActivity(AtyMyMember.class);
            }
        }

    }
}
