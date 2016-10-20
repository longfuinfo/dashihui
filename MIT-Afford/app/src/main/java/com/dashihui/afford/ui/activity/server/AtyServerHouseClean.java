package com.dashihui.afford.ui.activity.server;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.thirdapi.FastJSONHelper;
import com.dashihui.afford.ui.widget.WgtAlertDialog;
import com.dashihui.afford.util.number.UtilNumber;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2016/1/5.
 */
public class AtyServerHouseClean extends BaseActivity {
    @ViewInject(R.id.title)
    private TextView mTitle;
    @ViewInject(R.id.webView)
    private WebView mWebView;

    private static int index = -1;
    private String mType;
    private Map<String, Object> mapObject;

    private static String[] name = {"日常保洁", "深度保洁"};
    private List<Map<String, Objects>> mListMap;
    public final static String SERHOME_ORDERCOUNT = "serhome_ordercount";
    public final static String SERHOME_ORDERPRICE = "serhome_orderprice";
    public final static String SERHOME_SERTIME = "serhome_sertime";
    public final static String SERHOME_SERTITLE = "serhome_sertitle";
    public final static String SERHOME_SERTYPE = "serhome_sertype";
    public final static String SERHOME_SERNAME = "serhome_sername";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_server_house_clean);
        ViewUtils.inject(this);
        initData();
    }

    private void initData() {
        LogUtils.e("init===========init===========>");
        //优先使用缓存
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //根据传进来的intent的不同值加载不同页面
        Intent intent = getIntent();
        mType = getIntent().getStringExtra(AtyServerHouse.INTENT_TAG);
        if (mType != null) {
            index = UtilNumber.IntegerValueOf(getIntent().getStringExtra(AtyServerHouse.INTENT_TAG));
            if (index == 1) {
                LogUtils.e("initData=========index====1====>" + index);
                mWebView.loadUrl(AffConstans.BUSINESS.DAILY_URL);
            } else {
                LogUtils.e("initData=========index====2====>" + index);
                mWebView.loadUrl(AffConstans.BUSINESS.DEPTH_URL);
            }
        }

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        //自适应屏幕
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.requestFocus();//触摸焦点起作用
    }

    @Override
    protected void onResume() {
        Intent intent = getIntent();
        if (mType != null) {
            index = UtilNumber.IntegerValueOf(mType);
            mTitle.setText(name[index - 1]);
        } else {
            LogUtils.e("onResume=======================>");
        }
        super.onResume();
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        LogUtils.e("onSuccess============beanSendUI.getInfo()===========>" + beanSendUI.getInfo());
    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        LogUtils.e("onFailure============beanSendUI.getInfo()===========>" + beanSendUI.getInfo());
    }

    @OnClick(R.id.left_ibtnBack)//返回
    public void onTopBackClick(View v) {
        onBackPressed();
    }

    //调用JavaScript获取返回值
    private void getDateEvaluateJavascript(WebView webView) {
        mWebView.evaluateJavascript("getItem()", new ValueCallback<String>() {

            @Override
            public void onReceiveValue(String value) {
                LogUtils.e("onReceiveValue=======JSON.parse(mValue)=========>" + JSON.parse(value));
                mapObject = new HashMap<String, Object>();
                mapObject = FastJSONHelper.deserializeAny((String) JSON.parse(value), new TypeReference<HashMap<String, Object>>() {
                });
                LogUtils.e("onReceiveValue=======mapObject=========>" + mapObject);
                Intent intent = new Intent(AtyServerHouseClean.this, AtyServerHouseSettlement.class);
                intent.putExtra(SERHOME_SERTYPE, mType);//服务类型
                LogUtils.e("onReceiveValue=======mType=========>" + mType);

                //如果是日常保洁有预订服务时长
                if (mapObject != null) {
                    if ("1".equals(mType)) {
                        LogUtils.e("立即预约=======mType====1=====>" + mType);
                        LogUtils.e("立即预约=======数量=========>" + mapObject.get("COUNT") + "");
                        intent.putExtra(SERHOME_ORDERCOUNT, mapObject.get("COUNT") + "");//数量
                        intent.putExtra(SERHOME_ORDERPRICE, mapObject.get("SELLPRICE") + "");//订单价格
                        intent.putExtra(SERHOME_SERTIME, mapObject.get("HOUR") + "");//服务预订时长
                        intent.putExtra(SERHOME_SERTITLE, mapObject.get("TITLE") + "");//标题
                        intent.putExtra(SERHOME_SERNAME, name[0]);//服务标题
                    } else {
                        LogUtils.e("立即预约=======mType====2=====>" + mType);
                        LogUtils.e("立即预约=======数量=========>" + mapObject.get("COUNT") + "");
                        intent.putExtra(SERHOME_ORDERCOUNT, mapObject.get("COUNT") + "");//数量
                        intent.putExtra(SERHOME_ORDERPRICE, mapObject.get("SELLPRICE") + "");//订单价格
                        intent.putExtra(SERHOME_SERTITLE, mapObject.get("TITLE") + "");//标题
                        intent.putExtra(SERHOME_SERNAME, name[1]);//服务标题
                    }
                }
//                intent.putExtra(SERHOME_SERNAME, name[index]);//服务标题
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.appointment)//立即预约
    public void onAppointmentClick(View v) {
        if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance().getEntityLocation().getSTORE() != null) {
            int communtyId = UtilNumber.IntegerValueOf(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
            LogUtils.e("====去结算====定位=======>" + communtyId);
            if (communtyId == CommConstans.LOCCOMMUNITY.COMMUNITYID) {
                if (mDialog == null) {
                    mDialog = new WgtAlertDialog();
                }
                mDialog.show(this,
                        "确定",
                        "您所在的小区暂未开通服务，敬请期待！",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.dismiss();
                            }
                        }, false, false);
            } else {
                getDateEvaluateJavascript(mWebView);
            }
        }
    }
}
