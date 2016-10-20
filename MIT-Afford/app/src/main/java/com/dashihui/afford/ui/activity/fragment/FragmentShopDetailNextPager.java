package com.dashihui.afford.ui.activity.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessShop;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseFragment;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.ui.activity.shop.AtyAffordShop;
import com.dashihui.afford.ui.widget.WgtWebView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.Map;

public class FragmentShopDetailNextPager extends BaseFragment {

    @ViewInject(R.id.nextpager_webview)
    private WgtWebView mWebView;
    @ViewInject(R.id.progressbar)
    private ProgressBar mProgressBar;
    @ViewInject(R.id.rlyt_nexpager)
    private RelativeLayout mRlytNextPager;
//    @ViewInject(R.id.no_detail_img)
//    private ImageView mNoDetailImage;
    private boolean mHasInited = false;
    private BusinessShop mBllShop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBllShop = new BusinessShop(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_shopdetail_nextpager, null);
        ViewUtils.inject(this, rootView); //注入view和事件
        return rootView;
    }

    public void initView() {
        if (getActivity().getIntent().getStringExtra(AtyAffordShop.INTENT_SHOP_ID) != null) {
            //加载框
            showProDialog(getActivity());
            mBllShop.detailDescribe(getActivity().getIntent().getStringExtra(AtyAffordShop.INTENT_SHOP_ID));
        } else {
            mRlytNextPager.setVisibility(View.GONE);
        }

        if (null != mWebView && !mHasInited) {
            mHasInited = true;
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_GOODS_DETAILDESCRIBE://商品详情描述
                    dissProDialog();
                    Map<String, Object> beanRegister = (Map<String, Object>) beanSendUI.getInfo();
                    if (beanRegister.get("CONTEXT") != null && !"".equals(beanRegister.get("CONTEXT"))) {
                        String message = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" +
                                "<html xmlns='http://www.w3.org/1999/xhtml'> <head> <title></title> <style type='text/css'> img{max-height:225px;max-width:300px;text-align:center;margin: 0 auto}</style>" +
                                "<script></script></head><body> <div id='pic'style='font-size:17px; width: auto; " +
                                "vertical-align:top;word-spacing:4pt;line-height:18pt;word-break:break-all;'>" +
                                beanRegister.get("CONTEXT").toString() + "</div> </body> </html>";
                        LogUtils.e("onSuccess======message========>" + message);
                        mRlytNextPager.setVisibility(View.VISIBLE);
                        setWebViewConfig(mWebView, message);
                    } else {
                        LogUtils.e("onSuccess==========null======异常====>" + beanSendUI.getInfo());
                        mRlytNextPager.setVisibility(View.GONE);
                    }
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
    public void onFailure(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_GOODS_DETAILDESCRIBE://商品详情描述
                    dissProDialog();
                    break;
                default:
                    LogUtils.e("onFailure===default===========>" + beanSendUI);
                    break;
            }
        } else {
            LogUtils.e("onFailure===============>" + beanSendUI);
        }
    }

    /**
     * webView显示
     *
     * @param webView
     * @param htmlString
     */
    public void setWebViewConfig(WgtWebView webView, String htmlString) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.requestFocus();
        settings.setSupportZoom(false);
        settings.setDefaultTextEncodingName("utf-8");
        webView.loadDataWithBaseURL(null, htmlString, "text/html", null, null);
        webView.setWebViewClient(new WebViewClient() {
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
