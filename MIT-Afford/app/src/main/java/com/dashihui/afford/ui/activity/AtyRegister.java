package com.dashihui.afford.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.dashihui.afford.util.toast.UtilToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnCompoundButtonCheckedChange;

public class AtyRegister extends BaseActivity {

    private final static String TAG = "AtyRegister";

    /** 重新获取验证码按钮 */
    @ViewInject(R.id.btnVerify)
    private Button mBtnVerify;
    /** 注册按钮 */
    @ViewInject(R.id.btnRegister)
    private Button mBtnRegister;
    /** 新手机号码 */
    @ViewInject(R.id.editNewPhone)
    private EditText mEditNewPhone;
    /** 输入验证码 */
    @ViewInject(R.id.editVerify)
    private EditText mEditVerify;
    /** 当前密码 */
    @ViewInject(R.id.editPwd)
    private EditText mEditPwd;
    /** 新密码，确认新密码 */
    @ViewInject(R.id.editPwd1)
    private EditText mEditPwd1;
    @ViewInject(R.id.editPwd2)
    private EditText  mEditPwd2;
    @ViewInject(R.id.ibtnBack)
    private ImageButton mIbtnBack;
    @ViewInject(R.id.chkBoxAgree)
    private CheckBox mChkBoxAgree;
    @ViewInject(R.id.txtTitle)
    private TextView mTxtTitle;
    @ViewInject(R.id.editInvitationCode)
    private EditText mInvitation;


    private Boolean mCheched;
    private Intent mIntent;
    private static String mPhoneNumStr = "";
    //    private static String mCode = "";
    private static String mType = "0";
    private Boolean isloop = true;
    private Handler mHandler;
    /** 获取验证码 */
    private static final int GET_CHECK_CODE = 11111;


    private BusinessUser mBllUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_register);
        ViewUtils.inject(this);//依赖注入初始化
        mCheched = mChkBoxAgree.isChecked();
        mBllUser = new BusinessUser(this);

        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                handleMsg(msg);
            }
        };
    }

    /**
     * 处理异步Handler
     * @param msg
     */
    private void handleMsg(Message msg) {
        switch (msg.what) {
            case GET_CHECK_CODE:
                if (msg.arg1 < 0) {
                    break;
                }
                String numStr = String.valueOf(msg.arg1);
                if (!"0".equals(numStr)) {
                    mBtnVerify.setText(numStr + "秒后重新获取");
                    mBtnVerify.setEnabled(false);
                    mBtnVerify.setBackgroundResource(R.drawable.btngraybg);
                } else {
                    mBtnVerify.setText("获取验证码");
                    mBtnVerify.setEnabled(true);
                    mBtnVerify.setBackgroundResource(R.drawable.btn_style_one);
                }

                break;

            default:
                break;
        }
    }
    @Override
    protected void onResume() {
        initBLL();//业务初始化
        super.onResume();

    }
    @OnClick(R.id.btnVerify)
    public void onVerifyClick(View view){

        // 重新获取“手机短信验证码”
        if (!"".equals(mPhoneNumStr)) {
            mBtnVerify.setEnabled(false);
            if(CommConstans.REGISTER.INTENT_VALUE_REGISTER.equals(mType)){
                //新用户注册获取验证码
                loadVerifyData(mPhoneNumStr, CommConstans.REGISTER.INTENT_VALUE_REGISTER);
            }else{
                LogUtils.e("找回密码获取验证码==========111111====>"+ mType);
                //找回密码获取验证码
                loadVerifyData(mPhoneNumStr, CommConstans.REGISTER.INTENT_VALUE_PWDRESET);
            }
        } else {
            mBaseUtilAty.ShowMsg("手机号码获取异常");
            finish();
        }
    }
    @OnClick(R.id.btnRegister)
    public void onRegistClick(View view){
        mBtnRegister.setEnabled(false);
        LogUtils.e("类型==============>"+ mType);
        String verifyStr = mEditVerify.getText().toString().trim();// 验证码
        String pwd = mEditPwd.getText().toString().trim();
        String pwd1 = mEditPwd1.getText().toString().trim();
        String pwd2 = mEditPwd2.getText().toString().trim();
        String invition = mInvitation.getText().toString().trim();//邀请码
        // 验证码格式判断
        if (verifyStr.length() != 4) {
            mBaseUtilAty.ShowMsg("验证码错误！");
            mBtnRegister.setEnabled(true);
            return;
        }
        if (pwd.length() < 6) {
            mBaseUtilAty.ShowMsg("密码不能少于6位！");
            mBtnRegister.setEnabled(true);
            return;
        }
        if (CommConstans.REGISTER.INTENT_VALUE_PWDRESET.equals(mType)) {
            if (pwd1.length() < 6) {
                mBaseUtilAty.ShowMsg("新密码不能少于6位！");
                mBtnRegister.setEnabled(true);
                return;
            }
            // 密码重置
            if (pwd1.equals(pwd2)) {
//                loadRegisterData(mPhoneNumStr, pwd, pwd1, verifyStr);
                loadRegisterData(mPhoneNumStr, pwd1, pwd2, verifyStr,invition);
            } else {
                mBaseUtilAty.ShowMsg("两次新密码输入不一致");
                mBtnRegister.setEnabled(true);
            }
        } else {
            // 注册、找回密码、变更手机号
            if (pwd.equals(pwd1)) {
                LogUtils.e("注册、找回密码、变更手机号=========密码相等====>"+ mType);
                if (CommConstans.REGISTER.INTENT_VALUE_REGISTER.equals(mType)) {// 注册，需要同意协议
                    if (mCheched) {
                        loadRegisterData(mPhoneNumStr, pwd,pwd1, verifyStr,invition);
                    } else {
                        mBtnRegister.setEnabled(true);
                        mBaseUtilAty.ShowMsg(R.string.agree);
                    }
                } else {
                    if (CommConstans.REGISTER.INTENT_VALUE_PHONERESET.equals(mType)) {
                        // 新输入手机号码是否正确
                        mPhoneNumStr = mEditNewPhone.getText().toString().trim();
                        if (mPhoneNumStr != null
                                && mPhoneNumStr.length() == 11
                                && UtilCommon.isMobileNO(mPhoneNumStr)) {
                            loadRegisterData(mPhoneNumStr,pwd,pwd1,verifyStr,invition);
                        } else {
                            mBtnRegister.setEnabled(true);
                            mBaseUtilAty.ShowMsg("新输入手机号码格式不正确！");
                        }
                    } else {
                        loadRegisterData(mPhoneNumStr,pwd,pwd1,verifyStr,invition);
                    }
                }
            } else {
                mBtnRegister.setEnabled(true);
                mBaseUtilAty.ShowMsg("两次密码输入不一致");
            }
        }
    }

    /**
     * load相关协议
     * @param phone
     * @param password
     * @param code
     */
    private void loadRegisterData(String phone,String password,String pwd1,String code, String invite){
        LogUtils.e("类型=======loadRegisterData=======>" + mType);
        if (CommConstans.REGISTER.INTENT_VALUE_REGISTER.equals(mType)) {
            // 注册账号
            // 手机号、密码、验证码
            LogUtils.e("类型=======注册账号=======>"+mPhoneNumStr);
            mBllUser.regist(mPhoneNumStr, password, code,invite);
        } else if (CommConstans.REGISTER.INTENT_VALUE_PWD.equals(mType)) {
            // 找回密码
            // 手机号码、密码（新密码）、验证码
            mBllUser.resetPwd(mPhoneNumStr,password, pwd1, code);

        } else if (CommConstans.REGISTER.INTENT_VALUE_PWDRESET.equals(mType)) {
            // 密码重置 手机号码（用户名）、原密码、新密码、验证码
            // 参数列表
//            mBllUser.resetPwd(mPhoneNumStr,password, pwd1, code);
            String _pwd2 = mEditPwd2.getText().toString().trim();
            mBllUser.resetPwd(mPhoneNumStr,pwd1, _pwd2, code);

        } else {
            mBtnRegister.setEnabled(true);
            LogUtils.e("类型=======注册账号=======>"+mPhoneNumStr);
        }
    }

    /**
     * 根据不同情况获取验证码
     * @param phoneStr
     * @param type
     */
    private void loadVerifyData(String phoneStr, String type) {
        if (mBaseUtilAty.isNetworkAvailable(this)) {
            if (CommConstans.REGISTER.INTENT_VALUE_REGISTER.equals(type)) {
                // 注册账号
                // 手机号、密码、验证码
                LogUtils.e("类型=======注册账号=======>"+phoneStr);
                mBllUser.sendregcode(phoneStr);
            } else if (CommConstans.REGISTER.INTENT_VALUE_PWD.equals(type)) {
                // 找回密码
                // 手机号码、密码（新密码）、验证码
                mBllUser.sendResetPwdCode(phoneStr);
            } else if (CommConstans.REGISTER.INTENT_VALUE_PWDRESET.equals(type)) {
                // 密码重置 手机号码（用户名）、原密码、新密码、验证码
                // 参数列表
                mBllUser.sendResetPwdCode(phoneStr);
            } else {
                // 变更手机号码 原手机号码（登录账号）、新手机号码、密码、验证码
                // 参数列表

            }
        } else {
            mBtnVerify.setEnabled(true);
            mBaseUtilAty.ShowMsg(R.string.error_network);
        }
    }


    @OnCompoundButtonCheckedChange(R.id.chkBoxAgree)
    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
        mCheched = isChecked;
    }
    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        mBtnRegister.setEnabled(true);
        LogUtils.e("onSuccess=========AtyRegisterVerify========>" + beanSendUI);
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_USER_RIGIST://立即注册
                    LogUtils.e("onSuccess======立即注册========>" + beanSendUI);
                    LogUtils.e("onSuccess======立即注册========>" + mPhoneNumStr);
                    UtilToast.show(this,"注册成功！");
                    Intent intent = new Intent(AtyRegister.this, AtyLogin.class);
                    intent.putExtra(CommConstans.REGISTER.INTENT_KEY_PHONE,mPhoneNumStr);
                    startActivity(intent);
                    finish();
                    break;
                case AffConstans.BUSINESS.TAG_USER_SENDRESETPWDCODE://找回密码发送验证码
                    LogUtils.e("onSuccess======找回密码========>" + beanSendUI);
                    // 开启循环
                    isloop = true;
                    loadVerify();
                    break;
                case AffConstans.BUSINESS.TAG_USER_SENDREGCODE://用户注册时获取短信验证码
                    LogUtils.e("onSuccess======用户注册时获取短信验证码========>" + beanSendUI);
                    // 开启循环
                    isloop = true;
                    loadVerify();
                    break;
                case AffConstans.BUSINESS.TAG_USER_RESETPWD://找回密码操作
                    LogUtils.e("onSuccess======找回密码操作========>" + beanSendUI);
                    UtilToast.show(this,"密码修改成功！");
                    Intent intent1 = new Intent(AtyRegister.this, AtyLogin.class);
                    intent1.putExtra(CommConstans.REGISTER.INTENT_KEY_PHONE, mPhoneNumStr);
                    startActivity(intent1);
                    finish();
                    break;

                default:
                    LogUtils.e("onSuccess===default===========>" + beanSendUI);
                    break;
            }
        } else {
            LogUtils.e("onSuccess======AtyRegister=========>" + beanSendUI);
        }
    }

    @Override
    public void onFailure(EtySendToUI error) {
        mBtnRegister.setEnabled(true);
        LogUtils.e("onFailure======AtyRegister=========>" + error);
        if (error != null) {
            switch (error.getTag()) {
                case AffConstans.BUSINESS.TAG_USER_RIGIST://立即注册

                    break;
                case AffConstans.BUSINESS.TAG_USER_SENDRESETPWDCODE://找回密码
                    mBtnVerify.setEnabled(true);
                    break;
                case AffConstans.BUSINESS.TAG_USER_SENDREGCODE://用户注册时获取短信验证码
                    LogUtils.e("onSuccess======用户注册时获取短信验证码========>" + error);
                    mBtnVerify.setEnabled(true);
                    break;
                default:
                    LogUtils.e("onSuccess===default===========>" + error);
                    break;
            }
        } else {
            LogUtils.e("onSuccess======AtyRegister=========>" + error);
        }

        if (error != null) {
            if (mDialog == null) {
                mDialog = new WgtAlertDialog();
            }
            mDialog.show(this,
                    "确定",
                    error.getInfo().toString(),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                        }
                    }, false, false);
        }
    }

    /**
     * 业务初始化
     */
    private void initBLL(){
        mIntent = this.getIntent();
        if (mIntent != null) {
            // 当为“变更手机号码”时无此参数
            if (mIntent.getStringExtra(CommConstans.REGISTER.INTENT_KEY_PHONE) != null)
                mPhoneNumStr = mIntent.getStringExtra(CommConstans.REGISTER.INTENT_KEY_PHONE);
            // 当为“变更手机号码”时无此参数
//            if (mIntent.getStringExtra(AffConstans.REGISTER.INTENT_KEY_CODE) != null)
//                mCode = mIntent.getStringExtra(AffConstans.REGISTER.INTENT_KEY_CODE);
            mType = mIntent.getStringExtra(CommConstans.REGISTER.INTENT_KEY_TYPE);
        }
        // 此处转到此页面type =1为新用户注册，2为找回密码，3为密码修改
        if (CommConstans.REGISTER.INTENT_VALUE_REGISTER.equals(mType)) {
            mTxtTitle.setText("新用户注册");
            mBtnRegister.setText("立即注册");
            mEditNewPhone.setVisibility(View.GONE);
            mBtnVerify.setText("50秒后重新获取");
            mChkBoxAgree.setVisibility(View.VISIBLE);
        } else if (CommConstans.REGISTER.INTENT_VALUE_PWD.equals(mType)) {
            mTxtTitle.setText("找回密码");
            mBtnRegister.setText("确认");
            mEditNewPhone.setVisibility(View.GONE);
            mBtnVerify.setText("50秒后重新获取");
            mChkBoxAgree.setVisibility(View.GONE);
        } else if (CommConstans.REGISTER.INTENT_VALUE_PWDRESET.equals(mType)) {
            mTxtTitle.setText("密码重置");
            mBtnRegister.setText("确认");
            mChkBoxAgree.setVisibility(View.GONE);
            mEditNewPhone.setVisibility(View.GONE);
            mBtnVerify.setText("50秒后重新获取");
            mEditPwd.setHint("当前密码");
            mEditPwd.setText(AffordApp.LOG_PSWD);//登录密码
            mEditPwd.setVisibility(View.GONE);
            mEditPwd1.setHint("新密码");
            mEditPwd2.setVisibility(View.VISIBLE);
            mEditPwd2.setHint("确认新密码");
        } else if (CommConstans.REGISTER.INTENT_VALUE_PHONERESET.equals(mType)) {
            mTxtTitle.setText("变更手机号码");
            mEditNewPhone.setVisibility(View.VISIBLE);
            mBtnVerify.setText("获取验证码");
            mBtnVerify.setEnabled(true);
            mBtnVerify.setBackgroundResource(R.drawable.btn_blue);
            mEditPwd.setHint("新密码");
            mBtnRegister.setText("确认");
            mChkBoxAgree.setVisibility(View.GONE);
        }
        // 验证码获取倒计时
        if (!CommConstans.REGISTER.INTENT_VALUE_PHONERESET.equals(mType)) {
            loadVerify();
        }
    }

    /**
     * 验证码轮询
     */
    private void loadVerify() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                int num = 50;
                while (isloop) {
                    Message msg = mHandler.obtainMessage(GET_CHECK_CODE);

                    try {
                        Thread.sleep(1000);
                        num--;
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (num == 0) {
                        isloop = false;
                    }
                    msg.arg1 = num;
                    msg.sendToTarget();
                }
            }
        }).start();
    }

    @OnClick(R.id.ibtnBack)
    public void onBackClick(View view){
        onBackPressed();
    }



    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

    }
}
