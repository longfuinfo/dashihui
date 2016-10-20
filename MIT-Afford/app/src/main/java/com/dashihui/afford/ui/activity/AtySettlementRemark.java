package com.dashihui.afford.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dashihui.afford.R;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.util.toast.UtilToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by Administrator on 2015/11/23.
 */
public class AtySettlementRemark extends BaseActivity {

    @ViewInject(R.id.remark_tv)
    private EditText mTvRemark;//备注文本框
    private String mRemark;//输入的备注内容
    public final static String REMARK = "remark";
    @ViewInject(R.id.remark_tel)
    private TextView mRemarkTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_settlement_remark);
        ViewUtils.inject(this);
        String remark = getIntent().getStringExtra("remark");
        mTvRemark.setText(remark);
    }

    //返回
    @OnClick(R.id.left_img_back)
    public void onLeftBack(View v) {
        Intent intent = new Intent(this, AtySettlement.class);
        mRemark = mTvRemark.getText().toString().trim();
        intent.putExtra(REMARK, mRemark);
        setResult(RESULT_OK, intent);
        finish();
    }

    //提交
    @OnClick(R.id.remark_submit)
    public void onSubmitClick(View v) {
        mRemark = mTvRemark.getText().toString().trim();
        if (mRemark != null && mRemark.length() > 0) {
            if (mRemark.length()<200){
                Intent intent = new Intent(this, AtySettlement.class);
                intent.putExtra(REMARK, mRemark);
                setResult(RESULT_OK, intent);
                UtilToast.show(this, "备注提交成功", Toast.LENGTH_SHORT);
                finish();
            }else {
                Toast.makeText(this,"备注不能大于200字",Toast.LENGTH_SHORT).show();
            }

        }else {
            Intent intent = new Intent(this, AtySettlement.class);
            intent.putExtra(REMARK, "");
            setResult(RESULT_OK, intent);
            finish();
        }
    }
    @Override
    public void onSuccess(EtySendToUI beanSendUI) {

    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {

    }
}
