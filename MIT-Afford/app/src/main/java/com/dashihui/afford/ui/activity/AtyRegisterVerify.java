package com.dashihui.afford.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessUser;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.ui.widget.WgtAlertDialog;
import com.dashihui.afford.util.UtilCommon;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 输入手机号码&获取手机短信验证码
 *
 * @author LvJW
 * @version V1.0
 * @ClassName: AtyRegisterVerify
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2014年10月25日 下午3:42:52
 * @维护人:
 */
public class AtyRegisterVerify extends BaseActivity {

    private final static String TAG = "AtyRegisterVerify";
    //	private VerifyTask mVerifyTask;
    @ViewInject(R.id.verifyBtn)
    private Button mVerifyBtn;
    @ViewInject(R.id.editPhone)
    private EditText mEditPhone;
    @ViewInject(R.id.txtTitle)
    private TextView mTxtTitle;

    @ViewInject(R.id.verifyTxt)
    private TextView mVerifyTxt;// 已有账号

    @ViewInject(R.id.ibtnBack)
    private ImageButton mIBtnBack;
    /**
     * 标题
     */
    private static String type;
    /**
     * 0：默认值 1：新用户注册 ，变更手机号码;2：找回密码，修改密码
     */
    private Intent _intent;
    private BusinessUser mBllUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_registerverify);
        ViewUtils.inject(this);//依赖注入
        mBllUser = new BusinessUser(this);
    }


    /**
     * 获取短信验证码
     *
     * @param @param phoneStr 手机号码
     * @param @param _type 获取短信验证码类型（1新号码，2系统号码）
     * @return void 返回类型
     * @author LvJW
     * @date 2014年11月6日 上午10:10:28
     * @维护人:
     * @version V1.0
     */
    @OnClick(R.id.verifyBtn)
    public void onverifyBtnClick(View view) {
        mVerifyBtn.setEnabled(false);
        String phoneString = mEditPhone.getText().toString().trim();
        if (phoneString != null && mEditPhone.length() == 11
                && UtilCommon.isMobileNO(phoneString)) {
            if (CommConstans.REGISTER.INTENT_VALUE_REGISTER.equals(type)) {
                // 注册账号
                // 手机号、密码、验证码
                LogUtils.e("类型=======注册账号=======>"+phoneString);
                mBllUser.sendregcode(phoneString);
                //显示键盘
                showSoftInput(mEditPhone);
            } else if (CommConstans.REGISTER.INTENT_VALUE_PWD.equals(type)) {
                // 找回密码
                // 手机号码、密码（新密码）、验证码
                mBllUser.sendResetPwdCode(phoneString);
                LogUtils.e("类型=====找回密码=======>" + phoneString);
            } else if (CommConstans.REGISTER.INTENT_VALUE_PWDRESET.equals(type)) {
                // 密码重置 手机号码（用户名）、原密码、新密码、验证码
                // 参数列表
                mBllUser.sendResetPwdCode(phoneString);
            } else if (CommConstans.REGISTER.INTENT_VALUE_PHONERESET.equals(type)) {
                // 变更手机号码 原手机号码（登录账号）、新手机号码、密码、验证码
                // 参数列表
                mVerifyBtn.setEnabled(true);
            }

        } else {
            mBaseUtilAty.ShowMsg("电话号码格式不正确！");
            mVerifyBtn.setEnabled(true);
            LogUtils.e("onverifyBtnClick=========AtyRegisterVerify========>");
        }
        mBaseUtilAty.hideWindowSoftInput();
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        mVerifyBtn.setEnabled(true);
        LogUtils.e("onSuccess=========AtyRegisterVerify========>" + beanSendUI);
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_USER_SENDREGCODE://新用户注册
                    LogUtils.e("onSuccess======获取验证码========>" + beanSendUI);
                        String phoneString = mEditPhone.getText().toString().trim();
                        Intent intent = new Intent(AtyRegisterVerify.this, AtyRegister.class);
                        intent.putExtra(CommConstans.REGISTER.INTENT_KEY_TYPE, type);
                        intent.putExtra(CommConstans.REGISTER.INTENT_KEY_PHONE, phoneString);
//                        intent.putExtra(AffConstans.REGISTER.INTENT_KEY_CODE, mapObject.get("CODE")+"");
                        startActivity(intent);
                        finish();
                    break;
                case AffConstans.BUSINESS.TAG_USER_SENDRESETPWDCODE://重置密码
                    LogUtils.e("onSuccess======重置密码========>" + beanSendUI);
                        String phoneStr = mEditPhone.getText().toString().trim();
                        Intent intentd = new Intent(AtyRegisterVerify.this, AtyRegister.class);
                        intentd.putExtra(CommConstans.REGISTER.INTENT_KEY_TYPE, type);
                        intentd.putExtra(CommConstans.REGISTER.INTENT_KEY_PHONE, phoneStr);
//                        intent.putExtra(AffConstans.REGISTER.INTENT_KEY_CODE, mapObject.get("CODE")+"");
                        startActivity(intentd);
                        finish();
                    break;
                default:
                    break;
            }
        } else {
            LogUtils.e("onSuccess======AtyHome=========>" + beanSendUI);
        }

    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        mVerifyBtn.setEnabled(true);
        LogUtils.e("onFailure=========AtyRegisterVerify========>" + beanSendUI);
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_USER_SENDREGCODE://根据定位UID获取本地商铺信息
                    LogUtils.e("onSuccess======获取验证码========>" + beanSendUI);
                    String errorCode = beanSendUI.getInfo()+"";
                    if (AffConstans.PUBLIC.RESULT_STATE_RIGEDIT_ERROR.equals(errorCode)){

                        if (mDialog==null){
                            mDialog = new WgtAlertDialog();
                        }
                        mDialog.show(this,
                                "确定",
                                "您的账号已经注册！",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    mDialog.dismiss();
                                    String phoneString = mEditPhone.getText().toString().trim();
                                        Intent intent = new Intent(AtyRegisterVerify.this, AtyLogin.class);
                                        intent.putExtra(CommConstans.REGISTER.INTENT_KEY_PHONE, phoneString);
                                        startActivity(intent);
                                        AtyRegisterVerify.this.finish();
                                    }
                                }, false, false);

                    }else {
                        if (mDialog==null){
                            mDialog = new WgtAlertDialog();
                        }
                        mDialog.show(this,
                                "确定",
                                beanSendUI.getInfo().toString(),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mDialog.dismiss();
                                        AtyRegisterVerify.this.finish();
                                    }
                                }, false, false);
                    }
                    break;

                default:
                    if (mDialog==null){
                        mDialog = new WgtAlertDialog();
                    }
                    mDialog.show(this,
                            "确定",
                            beanSendUI.getInfo().toString(),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialog.dismiss();
                                    AtyRegisterVerify.this.finish();
                                }
                            }, false, false);
                    break;
            }
        } else {
            LogUtils.e("onSuccess======AtyHome==13783571041=======>" + beanSendUI);
            AtyRegisterVerify.this.finish();
        }


    }




    /**
     * 弹出软键盘
     *
     * @param view
     */
    private void showSoftInput(final View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }, 100);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        _intent = this.getIntent();
        if (_intent != null) {
            // 此处转到此页面type =1为新用户注册，2为找回密码
            type = _intent.getStringExtra(CommConstans.REGISTER.INTENT_KEY_TYPE);
            if (CommConstans.REGISTER.INTENT_VALUE_REGISTER.equals(type)) {
                mTxtTitle.setText("新用户注册");
                showSoftInput(mEditPhone);
            } else if (CommConstans.REGISTER.INTENT_VALUE_PWD.equals(type)) {
                mTxtTitle.setText("找回密码");
            } else if (CommConstans.REGISTER.INTENT_VALUE_PWDRESET.equals(type)) {
                mTxtTitle.setText("密码重置");
                // 自动赋值当前登录手机号码，并且禁用文本框
                mEditPhone.setText(AffordApp.LOG_PHONE);
                mEditPhone.setEnabled(false);
            } else if (CommConstans.REGISTER.INTENT_VALUE_PHONERESET.equals(type)) {
                mTxtTitle.setText("变更手机号码");
                mEditPhone.setHint("请输入新手机号码");
            }
        }
        if (!CommConstans.REGISTER.INTENT_VALUE_REGISTER.equals(type)) {
            // 如果是找回密码，密码重置，修改手机号码，则隐藏“已有账号？”
            mVerifyTxt.setVisibility(View.GONE);
        } else {
            mVerifyTxt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStop() {
        mBaseUtilAty.hideWindowSoftInput();
        super.onStop();
    }

    @OnClick(R.id.ibtnBack)
    public void onBackClick(View view){
        onBackPressed();
    }

    @OnClick(R.id.verifyTxt)
    public void onVerifyTxtClick(View view){
        // 已有账户？，跳转到登录界面
        Intent intent = new Intent();
        intent.putExtra("phone", mEditPhone.getText().toString().trim());
        intent.setClass(AtyRegisterVerify.this, AtyLogin.class);
        startActivity(intent);
        AtyRegisterVerify.this.finish();
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
    }

}
