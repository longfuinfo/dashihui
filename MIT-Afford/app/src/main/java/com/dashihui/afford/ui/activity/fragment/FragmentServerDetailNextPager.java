package com.dashihui.afford.ui.activity.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessServer;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseFragment;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.ui.activity.server.AtyServerList;
import com.dashihui.afford.ui.widget.WgtWebView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.Map;

public class FragmentServerDetailNextPager extends BaseFragment {
    private static final String ARG_POSITION = "position";
    private static Activity mContext;

    @ViewInject(R.id.nextpager_webview)
    private WgtWebView mWebView;
    @ViewInject(R.id.progressbar)
    private ProgressBar mProgressBar;
    @ViewInject(R.id.rlyt_nexpager)
    private RelativeLayout mRlytNextPager;
//    @ViewInject(R.id.no_detail_img)
//    private ImageView mNoDetailImage;


    private boolean hasInited = false;

    private BusinessServer mBllServer;
    private LinearLayout topLyt,nextLyt;

    public static FragmentServerDetailNextPager newInstance(Activity context, int position) {
        FragmentServerDetailNextPager f = new FragmentServerDetailNextPager();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        mContext = context;
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBllServer = new BusinessServer(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_serverdetail_nextpager, null);
        ViewUtils.inject(this, rootView); //注入view和事件
        return rootView;
    }

    public void initView() {
        //如果为空，会请求不到服务器，则显示内置的广告图片代替请求到的web页面
        if (getActivity().getIntent().getStringExtra(AtyServerList.INTENT_SERVER_ID) != null) {
            showProDialog(getActivity());
            mBllServer.getDescribe(getActivity().getIntent().getStringExtra(AtyServerList.INTENT_SERVER_ID));
        }else {
            mRlytNextPager.setVisibility(View.GONE);
        }
        if (null != mWebView && !hasInited) {
            hasInited = true;
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_SERVICE_DESCRIBE://服务商家详情描述
                    //隐藏加载框
                    dissProDialog();
                    Map<String, Object> beanRegister = (Map<String, Object>) beanSendUI.getInfo();
                    LogUtils.e("onSuccess==========beanRegister==========>" + beanRegister);
                    if (beanRegister.get("DESCRIBE") != null && !"".equals(beanRegister.get("DESCRIBE"))) {
                        String message = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" +
                                "<html xmlns='http://www.w3.org/1999/xhtml'> <head> <title></title> <style type='text/css'> img{max-height:225px;max-width:300px;text-align:center;margin: 0 auto}</style>" +
                                "<script></script></head><body> <div id='pic'style='font-size:17px; width: auto; " +
                                "vertical-align:top;word-spacing:4pt;line-height:18pt;word-break:break-all;'>" +
                                beanRegister.get("DESCRIBE") + "</div> </body> </html>";
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
                case AffConstans.BUSINESS.TAG_SERVICE_DESCRIBE://服务商家详情描述
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
