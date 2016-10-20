package com.dashihui.afford.wxapi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dashihui.afford.R;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

/**
 * 微信分享必须要正式签名，否则会被微信拒绝
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler{

    @ViewInject(R.id.cancel)
    private TextView mTxtCancel;
    private IWXAPI api;
    private String url;
    private String title;
    private Bitmap thumb;
    private String desc;
    private String thumb_1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_share);
        ViewUtils.inject(this);
        Intent intent = getIntent();
        url = intent.getStringExtra("link");
        title = intent.getStringExtra("title");
        thumb = intent.getParcelableExtra("thumb");
        desc = intent.getStringExtra("desc");
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(WXEntryActivity.this, Constants.APP_ID, true);
        api.handleIntent(getIntent(), this);
        api.registerApp(Constants.APP_ID);
    }
    @Override
    public void onSuccess(EtySendToUI beanSendUI) {

    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
    }

    @OnClick({(R.id.cancel), (R.id.wxFriend), (R.id.wxCircleFriends)})
    public void onAllClick(View view) {
        switch (view.getId()) {
            case R.id.cancel://取消
                onBackPressed();
                break;
            case R.id.wxFriend://微信好友
                updateWXStatus(false);
                break;
            case R.id.wxCircleFriends://微信朋友圈
                updateWXStatus(true);
                break;
            default:
                break;
        }
    }

    /**
     * 更新为微信朋友圈状态
     */
    public void updateWXStatus(boolean isFriend) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = desc;
        //Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.about_icon_logo);
        msg.thumbData = bmpToByteArray(thumb, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("大实惠云商平台");
        req.message = msg;
        req.scene = isFriend ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req
                .WXSceneSession;
        api.sendReq(req);
        LogUtils.e("onReq==========1111=webpage=====>");
        WXEntryActivity.this.finish();
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //分享成功
                WXEntryActivity.this.finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //分享取消
                WXEntryActivity.this.finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //分享拒绝
                Toast.makeText(getApplicationContext(),"分享被拒绝",Toast.LENGTH_SHORT).show();
                WXEntryActivity.this.finish();
                break;
        }
    }
}
