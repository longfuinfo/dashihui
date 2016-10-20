package com.dashihui.afford.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessCommon;
import com.dashihui.afford.business.BusinessUser;
import com.dashihui.afford.business.entity.EntityLocation;
import com.dashihui.afford.business.entity.EtyLogin;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.ui.adapter.AutoCompleteAdapter;
import com.dashihui.afford.ui.widget.WgtAlertDialog;
import com.dashihui.afford.util.UtilCommon;
import com.dashihui.afford.util.UtilEncryptionDecryption;
import com.dashihui.afford.util.preferences.UtilPreferences;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnCompoundButtonCheckedChange;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class AtyLogin extends BaseActivity {
    private final static String TAG = "AtyLogin";


    private BusinessUser mBllUser;
    /**
     * 登录账号
     */
    @ViewInject(R.id.editUserName)
    private AutoCompleteTextView mEditUserName;
    /**
     * 登录密码
     */
    @ViewInject(R.id.editUserPwd)
    private EditText mEditUserPwd;

    @ViewInject(R.id.btnLogin)
    private Button mBtnLogin;//登录
    @ViewInject(R.id.regeditBtn)
    private Button mRegeditBtn;// 注册

    @ViewInject(R.id.forgetTxt)
    private TextView mForgetTxt;
    @ViewInject(R.id.chkBoxMemory)
    private CheckBox mChkSaveUser;//记住账户

    @ViewInject(R.id.chkBoxAutoLogin)
    private CheckBox mChkBoxAutoLogin;//自动登录
    @ViewInject(R.id.ibtnBack)
    private ImageButton mIbtnBack;

    //private LoginTask mLoginTask;
    /**
     * 账号存储文件
     */
    private UtilPreferences utilShared;


    private static String phoneStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_login);
        ViewUtils.inject(this);//依赖注入

        mChkSaveUser.setChecked(true);
        // 获取本地历史账号列表
        if (UtilPreferences.getString(this, CommConstans.Login.SHPREFER_USER_LIST) != null) {
            //如果有历史账号列表
            ArrayList<String> newArrayList = new ArrayList<String>();
            String[] books = UtilPreferences.getString(this, CommConstans.Login.SHPREFER_USER_LIST).split(",");
            for (int i = 0; i < books.length; i++) {
                newArrayList.add(books[i]);
            }
            AutoCompleteAdapter mAutoAdapter = new AutoCompleteAdapter(this,
                    newArrayList, "userinfo", 10);
            mEditUserName.setAdapter(mAutoAdapter);
            mEditUserName.setText(books[books.length - 1]);
            mEditUserPwd.requestFocus();
        } else {
            mEditUserName.requestFocus();
        }


        Intent _intent = this.getIntent();
        if (_intent != null) {
            phoneStr = _intent.getStringExtra(CommConstans.REGISTER.INTENT_KEY_PHONE);
            String goneRigedit = _intent.getStringExtra(CommConstans.REGISTER.INTENT_NO_RIGEDIT);
            if ("0".equals(goneRigedit)){
                mRegeditBtn.setVisibility(View.GONE);
            }
        }
        if (phoneStr != null && !"".equals(phoneStr)) {
            mEditUserName.setText(phoneStr);
        }
        mBllUser = new BusinessUser(this);

    }


    @OnCompoundButtonCheckedChange(R.id.chkBoxAutoLogin)
    public void onAutoLoginCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub
        if (mChkBoxAutoLogin.isChecked()
                && !mChkSaveUser.isChecked()) {
            mChkSaveUser.setChecked(true);
        }
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        LogUtils.e("onSuccess=========AtyLogin========>" + beanSendUI);

        mBtnLogin.setEnabled(true);
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_USER_LOGIN://手机登录成功
                    EtyLogin entityLogin = (EtyLogin) beanSendUI.getInfo();
                    // 本地保存登录用户名&密码
                    AffordApp.LOG_PHONE = mEditUserName.getText().toString().trim();
                    AffordApp.LOG_PSWD = mEditUserPwd.getText().toString().trim();
                    AffordApp.getInstance().setUserLogin(entityLogin);
                    //是否记住账户
                    isCookiePwd();
                    // 是否自动登录
                    isAutoLogin();

                    // 保存登录TOKEN到本地
                    UtilPreferences.putString(this, CommConstans.Login.TOKEN, entityLogin.getTOKEN() + "");
                    LogUtils.e("onSuccess===登录===========>" + entityLogin.getTOKEN() + "");
                    // 显示登录结果，如果无描述返回时，显示默认提示语
                    mBaseUtilAty.ShowMsg("登录成功");
                    AtyLogin.this.finish();
                    //根据社区ID锁定所在小区店铺信息
//                    BusinessCommon mBllCommon = new BusinessCommon(this);
//                    LogUtils.e("onSuccess======定位本地商铺，社区信息========>" + entityLogin.getUSER());
//                    if (entityLogin!=null && entityLogin.getUSER()!=null){
//                        mBllCommon.getcommunity(entityLogin.getUSER().getCOMMUNITYID() + "");
//                    }else {
//                        LogUtils.e("onSuccess======定位本地商铺，社区信息========>" + entityLogin.getUSER());
//                    }
                    break;
//                case AffConstans.BUSINESS.TAG_COMMON_GETCOMMUNITY://根据社区ID获取本地商铺信息
//                    LogUtils.e("onSuccess======定位本地商铺，社区信息========>" + beanSendUI);
//                    EntityLocation entityLocat = (EntityLocation) beanSendUI.getInfo();
//                    AffordApp.getInstance().setEntityLocation(entityLocat);
//                    // 返回登录前的页面
//                        AtyLogin.this.finish();
//                    break;
                default:
                    LogUtils.e("onSuccess===default===========>" + beanSendUI);
                    break;
            }
        } else {
            mEditUserPwd.setText("");
            LogUtils.e("登录失败=======>");
        }

    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_USER_LOGIN://手机登录
                    if (mDialog == null) {
                        mDialog = new WgtAlertDialog();
                    }
                    mDialog.show(this,
                            "确定",
                            beanSendUI.getInfo()+"",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialog.dismiss();
                                }
                            }, false, false);

                    break;
                default:
                    LogUtils.e("onSuccess===default===========>" + beanSendUI);
                    break;
            }
        }
        mBtnLogin.setEnabled(true);
        LogUtils.e("登录失败=======>" + beanSendUI);
    }


    @OnClick(R.id.ibtnBack)
    public void onBackClick(View v) {
        // 返回登录前的页面
      this.finish();
    }

    @Override
    public void onBackPressed() {
        // 返回登录前的页面
        AtyLogin.this.finish();

    }

    @OnClick(R.id.btnLogin)
    public void onLoginClick(View v) {
        //设置按钮不可点击
        mBtnLogin.setEnabled(false);
        mBaseUtilAty.hideWindowSoftInput();
        String userName = mEditUserName.getText().toString().trim();
        String userPwd = mEditUserPwd.getText().toString().trim();
        if (userName != null && userName.length() == 11
                && UtilCommon.isMobileNO(userName)) {
            if (!"".equals(userPwd)) {
                mBllUser.login(userName, userPwd);
            } else {
                mBtnLogin.setEnabled(true);
                mBaseUtilAty.ShowMsg("密码不能为空！");
            }
        } else {
            mBtnLogin.setEnabled(true);
            mBaseUtilAty.ShowMsg("用户名不正确！");
        }
    }

    @OnClick(R.id.regeditBtn)
    public void onRegistClick(View v) {
        Bundle bundle1 = new Bundle();
        bundle1.putString(CommConstans.REGISTER.INTENT_KEY_TYPE, CommConstans.REGISTER.INTENT_VALUE_REGISTER);
        mBaseUtilAty.startActivity(AtyRegisterVerify.class, bundle1, true);
    }

    @OnClick(R.id.forgetTxt)
    public void onforgetClick(View v) {
        // 忘记密码(找回密码)
        Bundle bundle = new Bundle();
        bundle.putString(CommConstans.REGISTER.INTENT_KEY_TYPE, CommConstans.REGISTER.INTENT_VALUE_PWD);
        mBaseUtilAty.startActivity(AtyRegisterVerify.class, bundle, true);
    }

    /**
     * 是否自动登录
     */

    private void isAutoLogin() {
        if (mChkBoxAutoLogin.isChecked()) {
            // 勾选了自动登录
            // 保存”自动登录“标志到本地
            UtilPreferences.putBoolean(this, CommConstans.Login.SHPREFER_AUTO_LOGIN, true);
            UtilPreferences.putString(this, CommConstans.Login.AUTO_LOGIN_SET_TIME, Calendar.getInstance().getTimeInMillis() + "");
            // 保存登录账号到本地
            UtilPreferences.putString(this, CommConstans.Login.SHPREFER_USER_NAME, mEditUserName.getText().toString().trim());
            try {
                UtilEncryptionDecryption encry = new UtilEncryptionDecryption();
                // 保存加密后的登录密码到本地
                UtilPreferences.putString(this, CommConstans.Login.SHPREFER_USER_PSWD, encry.encrypt(mEditUserPwd.getText().toString().trim()));
                LogUtils.e("加密后的密码=====>" + encry.encrypt(mEditUserPwd.getText().toString().trim()));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            // 不设置自动登录，则清空本地关于之前自动登录的设置
            // 移除自动登录标志
            UtilPreferences.removeKey(this, CommConstans.Login.SHPREFER_AUTO_LOGIN);
            // 移除自动登录存储的账号
            UtilPreferences.removeKey(this, CommConstans.Login.SHPREFER_USER_NAME);
            // 移除自动登录存储的密码
            UtilPreferences.removeKey(this, CommConstans.Login.SHPREFER_USER_PSWD);

        }
    }

    /**
     * 是否记住账号
     */
    private void isCookiePwd() {
        // 是否记住账号
        if (mChkSaveUser.isChecked()) {
            // *********************记住账号**************************//
            // 本地是否已经存在“存储账号文件”
            if (UtilPreferences.getString(this, CommConstans.Login.SHPREFER_USER_LIST) != null) {
                // 如果本地“存储账号文件”中不含有当前登录账号
                if (!UtilPreferences.getString(this, CommConstans.Login.SHPREFER_USER_LIST).contains(
                        mEditUserName.getText().toString().trim())) {
                    // 存储当前“登录账号”到本地“存储账号文件”中，键名：USER_LIST，键值：以","号分隔的账号
                    UtilPreferences.putString(this, CommConstans.Login.SHPREFER_USER_LIST, UtilPreferences.getString(this, CommConstans.Login.SHPREFER_USER_LIST) + ","
                            + mEditUserName.getText().toString().trim());
                } else {
                    // 如果本地“存储账号文件”已经存在当前登录账号，则将最新登录账号存放到末尾，
                    // 这样下次登录时会把最近一次登录的账号赋值到登录账号文本框中
                    // 读取本地“存储账号文件”中的账号列表，本地“存储账号文件”——是以","号分隔的账号
                    String[] books = UtilPreferences.getString(this, CommConstans.Login.SHPREFER_USER_LIST)
                            .split(",");
                    // 将当前登录账号在本地”存储账号文件“中的末尾，读取时会将末尾的账号标识为最近登录账号
                    UtilPreferences.putString(this, CommConstans.Login.SHPREFER_USER_LIST, setEndStringInStrArray(books, mEditUserName.getText().toString().trim()));
                }
            } else {
                UtilPreferences.putString(this, CommConstans.Login.SHPREFER_USER_LIST, "," + mEditUserName.getText().toString().trim());
            }
        } else {
            // 不记住账号，如果本地已存账号，则删除本地账号
            if (UtilPreferences.getString(this, CommConstans.Login.SHPREFER_USER_LIST) != null) {
                // 读取本地“存储账号文件”中的账号列表，本地“存储账号文件”——是以","号分隔的账号
                String[] books = UtilPreferences.getString(this, CommConstans.Login.SHPREFER_USER_LIST).split(",");
                // 将当前登录账号从本地”存储账号文件“中移除
                UtilPreferences.putString(this, CommConstans.Login.SHPREFER_USER_LIST, deleteSpeStringInStrArray(books, mEditUserName.getText().toString().trim()));
            }
        }
    }

    /**
     * 将指定的字符串重新排序到字符串数组中的末尾
     *
     * @param @param  books 含有要置顶字符串的字符串数组
     * @param @param  str 要放置末尾的字符串
     * @param @return 设定文件
     * @return String 字符串
     * @Title: setTopStringArray
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @author LvJW
     * @date 2014年10月21日 上午9:49:17
     * @维护人:
     * @version V1.0
     */
    public String setEndStringInStrArray(String[] books, String str) {
        String result = null;
        if (books != null && str != null) {
            // 创建临时字符串数组
            ArrayList<String> newArrayList = new ArrayList<String>();
            // 将非”指定字符串“的字符串存放到临时字符串数组中
            for (int i = 0; i < books.length; i++) {
                if (!books[i].equals(str)) {
                    newArrayList.add(books[i]);
                }
            }
            // 添加”指定字符串“到临时字符串数组中，至此指定字符串被排序到了字符串数组中末尾
            newArrayList.add(mEditUserName.getText().toString().trim());
            // 将字符串数组转换为字符串，用","号作为分隔符
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < newArrayList.size(); i++) {
                sb.append(newArrayList.get(i) + ",");
            }
            String account = sb.toString();
            // 移除末尾多余的分隔符号，并返回
            result = account.substring(0, account.length() - 1);
        }
        return result;
    }

    /**
     * 删除字符串数组中的指定字符串
     *
     * @param @param  books
     * @param @param  str
     * @param @return 设定文件
     * @return String 返回类型
     * @Title: deleteStringInStrArray
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @author LvJW
     * @date 2014年10月21日 上午9:56:30
     * @维护人:
     * @version V1.0
     */
    public String deleteSpeStringInStrArray(String[] books, String str) {
        String result = null;
        if (books != null && str != null) {
            ArrayList<String> newArrayList = new ArrayList<String>();
            for (int i = 0; i < books.length; i++) {
                if (!books[i].equals(str)) {
                    newArrayList.add(books[i]);
                }
            }
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < newArrayList.size(); i++) {
                sb.append(newArrayList.get(i) + ",");
            }
            String account = sb.toString();
            result = account.substring(0, account.length() - 1);
        }
        return result;
    }
}
