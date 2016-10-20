package com.dashihui.afford.ui.activity.my;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessCommon;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.util.number.UtilNumber;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.Map;

/**
 * Created by Administrator on 2016/1/26.
 */
public class AtyMyStore extends BaseActivity {

    @ViewInject(R.id.image)
    private ImageView mImage;
    @ViewInject(R.id.time)
    private TextView mOpeningTime;//营业时间
    @ViewInject(R.id.address)
    private TextView mStoreAddress;//店铺地址
    @ViewInject(R.id.username)
    private TextView mUserName;//联系人
    @ViewInject(R.id.phone)
    private TextView mPhone;//联系电话
    @ViewInject(R.id.email)
    private TextView mEmail;//电子邮件
    @ViewInject(R.id.sendtime)
    private TextView mDeliveryTime;//配送时间
    @ViewInject(R.id.descripe)
    private TextView mDeliveryExplain;//配送说明

    @ViewInject(R.id.ratBarShop)
    private RatingBar mRBarShop;//商品满意度
    @ViewInject(R.id.ratBarSpeed)
    private RatingBar mRBarSpeed;//配送速度满意度
    @ViewInject(R.id.ratBarServer)
    private RatingBar mRBarServer;//服务质量满意度
    @ViewInject(R.id.et_feedback)

    private BusinessCommon mBllCommon;
    private Map<String, Object> mMapObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_my_store);
        ViewUtils.inject(this);
        mBllCommon = new BusinessCommon(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AffordApp.getInstance().getEntityLocation().getSTORE() != null) {
            //获取商铺详情
            mBllCommon.storeDetail(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
        }
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            LogUtils.e("onSuccess=========beanSendUI.getInfo()==========>" + beanSendUI.getInfo());
            mMapObject = (Map<String, Object>) beanSendUI.getInfo();
            Glide.with(this)
                    .load(AffConstans.PUBLIC.ADDRESS_IMAGE + mMapObject.get("THUMB") + "")
                    .placeholder(R.drawable.default_list)
                    .error(R.drawable.default_list)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mImage);

            mOpeningTime.setText("营业时间：" + mMapObject.get("BEGINTIME") + "—" + mMapObject.get("ENDTIME"));
            mStoreAddress.setText("店铺地址：" + mMapObject.get("ADDRESS") + "");
            mUserName.setText("联系人：" + mMapObject.get("MANAGER") + "");
            mPhone.setText(mMapObject.get("TEL") + "");
            mPhone.setTextColor(Color.parseColor("#005197"));
            mEmail.setText("邮件：" + mMapObject.get("MAIL") + "");
            mDeliveryTime.setText("配送时间：" + mMapObject.get("DELIVERYDES") + "");
            mDeliveryExplain.setText("配送说明：" + mMapObject.get("TITLE") + "");

           mRBarShop.setRating(UtilNumber.FloatValueOf(mMapObject.get("EVAL1")+""));//商品满意度
            mRBarSpeed.setRating(UtilNumber.FloatValueOf(mMapObject.get("EVAL2")+""));//配送速度满意度
            mRBarServer.setRating(UtilNumber.FloatValueOf(mMapObject.get("EVAL3")+""));//服务质量满意度
        }
    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
    }

    @OnClick(R.id.call_tel)
    public void onPhoneClick(View v) {
        LogUtils.e("onPhoneClick========phone========>" + mMapObject.get("TEL") + "");
        Intent intent = new Intent(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mMapObject.get("TEL") + "")));
        startActivity(intent);
    }
}
