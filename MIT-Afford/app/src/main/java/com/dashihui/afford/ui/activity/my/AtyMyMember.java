package com.dashihui.afford.ui.activity.my;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessUser;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.ui.activity.AtyLogin;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by Administrator on 2016/4/6.
 */
public class AtyMyMember extends BaseActivity{
    @ViewInject(R.id.content_web)
    private WebView wContent;

    private BusinessUser mBllUser;
    private String url ;
    private String deviceNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_my_member);
        ViewUtils.inject(this);
        deviceNumber = AffordApp.getInstance().getmAffordBean().getSIGNATURE();
        url = AffConstans.PUBLIC.ADDRESS + AffConstans.BUSINESS.USER_RULE + "?SIGNATURE="+deviceNumber;
        LogUtils.e("member===奖励===========>" + url);
        init();
    }
    public void init(){
        LogUtils.e("member===奖励===========>" + url);
       // wContent.loadUrl(url);
        //启用支持javascript
        WebSettings settings = wContent.getSettings();
        settings.setJavaScriptEnabled(true);
        //覆盖webview默认使用第三方或系统默认浏览器打开网页的行为，使用webview打开
        wContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //判断页面加载过程
        wContent.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                setTitle("Loading...");
                setProgress(newProgress * 100);
//                if (newProgress == 100) {
//                    // 网页加载完成
//
//                } else {
//                    // 加载中
//
//                }

            }


        });
        wContent.loadUrl(url);


    }
    //改写物理按键——返回的逻辑
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // TODO Auto-generated method stub
//        if(keyCode==KeyEvent.KEYCODE_BACK)
//        {
//            if(wContent.canGoBack())
//            {
//                wContent.goBack();//返回上一页面
//                return true;
//            }
//            else
//            {
//                System.exit(0);//退出程序
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void onSuccess(EtySendToUI successEty) {

    }

    @Override
    public void onFailure(EtySendToUI failureEty) {

    }
    @OnClick(R.id.left_back_collect)
    public void onBack(View v){
        onBackPressed();
    }
    @OnClick(R.id.member_share)
    public void onShare(View v){
        if (AffordApp.isLogin()) {
            if (AffordApp.getInstance().getUserLogin().getUSER().getLEVEL() == 2) {
                mBaseUtilAty.startActivity(AtyMyMemberShare.class);
            }else{
                new AlertDialog.Builder(this)
                        .setMessage("抱歉，您暂不满足条件!")
                        .setPositiveButton("确定", null)
                        .show();
            }
        }else {
            mBaseUtilAty.startActivity(AtyLogin.class);
        }
    }
}
