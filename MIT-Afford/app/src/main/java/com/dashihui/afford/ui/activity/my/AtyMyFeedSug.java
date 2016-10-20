package com.dashihui.afford.ui.activity.my;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessCommon;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.util.toast.UtilToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;
import java.util.Map;

public class AtyMyFeedSug extends BaseActivity implements TextWatcher {

    @ViewInject(R.id.et_feedback)
    private EditText mEtFeedBack;//输入反馈内容文本框
    private String mFeedbankSug;//反馈内容
    private BusinessCommon mBllCommon;
    private List<Map<String, Object>> mListMap;
    @ViewInject(R.id.tvbtn_submit)
    private TextView mTvBtnSubmit;
    @ViewInject(R.id.edit_num)
    private TextView mTotalNum;
    private int  mNums;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_my_feed_sug);
        ViewUtils.inject(this);
        mBllCommon = new BusinessCommon(this);
        mEtFeedBack.addTextChangedListener(this);
        getTextContext();//获取文本框是否有内容设置不同背景颜色
    }

    //提交反馈与建议内容
    @OnClick(R.id.tvbtn_submit)
    public void onTvBtnSubmitClick(View v) {
        mFeedbankSug = mEtFeedBack.getText().toString().trim();
        LogUtils.e("======获取文本框内容======>" + mFeedbankSug);
        if (!"".equals(mFeedbankSug) && mFeedbankSug.length() > 0) {
            mBllCommon.feedback(mFeedbankSug);
        } else {
            UtilToast.show(this, "请输入反馈建议，欢迎加入大实惠！", Toast.LENGTH_SHORT);
        }
    }

    //返回
    @OnClick(R.id.left_iback)
    public void onLeftImgBackClick(View v) {
        onBackPressed();
    }


    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        LogUtils.e("AtyMyFeedSug======onSuccess====beanSendUI===>" + beanSendUI);
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_COMMOON_FEEDBACK://意见反馈
                    LogUtils.e("====提交成功后结束Activity=====>");
                    finish();
                    UtilToast.show(this, "感谢您的意见！我们会尽快回复！", Toast.LENGTH_SHORT);
                    break;
            }
        }
    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        LogUtils.e("AtyMyFeedSug======onSuccess====beanSendUI===>" + beanSendUI);
        UtilToast.show(this, "网络无法链接，请检查您的网络！", Toast.LENGTH_SHORT);
    }

    /**
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        getTextContext();//获取文本框是否有内容设置不同背景颜色
    }

    /**
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        getTextContext();//获取文本框是否有内容设置不同背景颜色
        //获取文本框输入文字的长度
        mNums = mEtFeedBack.getText().toString().trim().length();
        LogUtils.e("onTextChanged========mNums=========>" + mNums);
    }

    /**
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {
        getTextContext();//获取文本框是否有内容设置不同背景颜色

        //文字长度写入计数框
        if (mNums >= 0 && mNums <500){
            mTotalNum.setText(mNums + "/500");
        } else {
            UtilToast.show(this, "您输入的字数已超过限制", Toast.LENGTH_SHORT);
        }
    }

    //获取文本框是否有内容设置不同背景颜色
    public void getTextContext() {
        mFeedbankSug = mEtFeedBack.getText().toString().trim();
        if ("".equals(mFeedbankSug)) {
            mTvBtnSubmit.setBackgroundResource(R.drawable.btn_feedsug_notext);
        } else {
            mTvBtnSubmit.setBackgroundResource(R.drawable.btn_feedsug);
        }
    }
}
