package com.dashihui.afford.ui.activity.shop;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessShop;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.Map;

/**
 * 商品详情图文
 */
public class AtyAffordShopDetailImageText extends BaseActivity {
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.show_news_webView)
    private WebView mWebView;

    private BusinessShop mBllShop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_affordshop_detail_imagetext);
        ViewUtils.inject(this);
        title.setText("图文详情");
        mBllShop = new BusinessShop(this);
        String goodsID = getIntent().getStringExtra(AtyAffordShop.INTENT_SHOP_ID);
        mBllShop.detailDescribe(goodsID);
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        if (beanSendUI!=null){
            switch (beanSendUI.getTag()){
                case AffConstans.BUSINESS.TAG_GOODS_DETAILDESCRIBE://商品详情描述
                    LogUtils.e("onSuccess======商品详情描述========>" + beanSendUI.getInfo());
                    Map<String, Object> beanRegister = (Map<String, Object>) beanSendUI.getInfo();

                    if (beanRegister.get("CONTEXT")!=null && !"".equals(beanRegister.get("CONTEXT"))){
                        String message = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" +
                                "<html xmlns='http://www.w3.org/1999/xhtml'> <head> <title></title> <style type='text/css'> img{max-height:225px;max-width:300px;text-align:center;margin: 0 auto}</style>" +
                                "<script></script></head><body> <div id='pic'style='font-size:17px; width: auto; " +
                                "vertical-align:top;word-spacing:4pt;line-height:18pt;word-break:break-all;'>" +
                                beanRegister.get("CONTEXT").toString() + "</div> </body> </html>";
                        LogUtils.e("onSuccess======message========>" + message);
                        setWebViewConfig(mWebView,message);
                    }else {
                        LogUtils.e("onSuccess==========null======异常====>" + beanSendUI.getInfo());
                    }

                    break;

                default:
                    LogUtils.e("onSuccess===default===========>" + beanSendUI);
                    break;
            }
        }else {
            LogUtils.e("onSuccess===============>" + beanSendUI);
        }
    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        LogUtils.e("onFailure===============>" + beanSendUI);
    }

    /**
     * 返回
     * @param v
     */
    @OnClick(R.id.ibtnBack)
    public void onBtnBackClick(View v){
        onBackPressed();
    }

    /**
     * webView显示
     * @param webView
     * @param htmlString
     */
    public  void setWebViewConfig(WebView webView,String htmlString){
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.requestFocus();
        settings.setSupportZoom(false);
        settings.setDefaultTextEncodingName("utf-8");
        webView.loadDataWithBaseURL(null, htmlString, "text/html", null, null);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }
}
