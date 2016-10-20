package com.dashihui.afford.ui.activity.server;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dashihui.afford.R;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseMenuActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class AtyServerHouse extends BaseMenuActivity {
    @ViewInject(R.id.title)
    private TextView mTitle;
    public final static String INTENT_TAG = "intentTag";

    @Override
    public int getContentViewLayoutResId() {
        return R.layout.aty_server_house;

    }

    @Override
    protected void onCreatOverride(Bundle savedInstanceState) {
        ViewUtils.inject(this);//依赖注入
        mTitle.setText("家政");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @OnClick({(R.id.dayClean),(R.id.deepClean)})
    public void onDayCleanClick(View v) {
        Intent intent = new Intent(this,AtyServerHouseClean.class);
        intent.putExtra(INTENT_TAG,v.getTag()+"");
        startActivity(intent);
    }

    /**
     * 返回
     *
     * @param v
     */
    @OnClick(R.id.left_ibtnBack)
    public void onBackLeftClick(View v) {
        onBackPressed();
    }

    @Override
    public void onSuccess(EtySendToUI info) {

    }

    @Override
    public void onFailure(EtySendToUI error) {
    }

    @Override
    public int getButtonType() {
        return 3;
    }
}
