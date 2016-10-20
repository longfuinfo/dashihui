package com.dashihui.afford.ui.activity.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessUser;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.thirdapi.FastJSONHelper;
import com.dashihui.afford.util.preferences.UtilPreferences;
import com.dashihui.afford.wxapi.Constants;
import com.dashihui.afford.wxapi.WXEntryActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.mm.sdk.openapi.IWXAPI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/6.
 */
public class AtyMyMemberShare extends BaseActivity {
    @ViewInject(R.id.content_web)
    private WebView wContent;
    private BusinessUser mBllUser;
    private String url = AffConstans.PUBLIC.ADDRESS + AffConstans.BUSINESS.USER_SHARE;
    private String singature;
    private String token;
    private Map<String, Object> mapObject;
    private IWXAPI wxApi;


    //    private UseJsInterface useJs = new UseJsInterface();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_my_member_share);
        ViewUtils.inject(this);
        mBllUser = new BusinessUser(this);
        mapObject = new HashMap<>();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void init() {
        singature = AffordApp.getInstance().getmAffordBean().getSIGNATURE();
        Log.i("singature",singature);
        token = UtilPreferences.getString(this, CommConstans.Login.TOKEN);
        String sUrl = url + "?SIGNATURE=" + singature + "&TOKEN=" + token;
        LogUtils.e("member===分享===========>" + sUrl);
        wContent.loadUrl(sUrl);
        //启用支持javascript
        wContent.getSettings().setJavaScriptEnabled(true);
        wContent.getSettings().setSupportZoom(true);
        wContent.getSettings().setBuiltInZoomControls(true);
        wContent.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        wContent.addJavascriptInterface(useJs,"android");
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
            }
        });
    }
    private void getDateEvaluateJavascript(WebView webView) {
        webView.evaluateJavascript("getShareData()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                LogUtils.e("onReceiveValue=======JSON.parse(mValue)=========>" + JSON.parse(value));
                mapObject = FastJSONHelper.deserializeAny(JSON.parse(value) + "", new
                        TypeReference<HashMap<String, Object>>() {
                        });
                LogUtils.e("onReceiveValue=======mapObject=========>" + mapObject);
                //如果是日常保洁有预订服务时长
               if (mapObject != null) {
                   MYTask myTask = new MYTask();
                   myTask.execute(mapObject.get("thumb") + "");
               }
            }
        });
    }
    public class MYTask extends AsyncTask<String, Void, Bitmap> {
        /**
         * 表示任务执行之前的操作
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * 主要是完成耗时的操作
         */
        @Override
        protected Bitmap doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            //加载网络分享图片
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(arg0[0]);
            Bitmap bitmap = null;
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    byte[] data = EntityUtils.toByteArray(httpEntity);
                    bitmap = BitmapFactory
                            .decodeByteArray(data, 0, data.length);
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            return bitmap;
        }

        /**
         * 主要是更新UI的操作
         */
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            Intent intent = new Intent(AtyMyMemberShare.this, WXEntryActivity.class);
            intent.putExtra("thumb",result);
            intent.putExtra("title", mapObject.get("title") + "");
            intent.putExtra("link", mapObject.get("link") + "");
            intent.putExtra("desc", mapObject.get("desc") + "");
            startActivity(intent);
        }

    }
    @Override
    public void onSuccess(EtySendToUI successEty) {
    }

    @Override
    public void onFailure(EtySendToUI failureEty) {
    }

    @OnClick(R.id.left_back_collect)
    public void onBack(View v) {
        onBackPressed();
    }

    @OnClick(R.id.share)//分享
    public void onShare(View v) {
        getDateEvaluateJavascript(wContent);
    }
}
