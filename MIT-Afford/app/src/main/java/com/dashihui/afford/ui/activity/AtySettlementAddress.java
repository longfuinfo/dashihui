package com.dashihui.afford.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessUser;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.util.UtilCommon;
import com.dashihui.afford.util.string.UtilString;
import com.dashihui.afford.util.toast.UtilToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnRadioGroupCheckedChange;

/**
 * Created by Administrator on 2016/1/28.
 */
public class AtySettlementAddress extends BaseActivity {

    @ViewInject(R.id.edit_name)//新增姓名
    private EditText mEtName;

    @ViewInject(R.id.edit_phone)//新增电话
    private EditText mEtPhone;

    @ViewInject(R.id.et_address)
    private EditText mEtUserAddress;//用户详细地址编辑

    @ViewInject(R.id.default_checkbox)
    private CheckBox mCheckBox;//默认地址
    private String mIsDefault = "1";//是否为默认地址

    //用来提交给服务器的用户姓名、性别、收货电话、地址
    private String mNewName, mSex = "1", mNewPhone, mUserAddress;

    private BusinessUser mBllUser;
    public final static String NOADDRESS = "noAddress";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_settlement_address);
        ViewUtils.inject(this);
        mBllUser = new BusinessUser(this);
        if (AffordApp.getInstance().getUserLogin() != null) {
            //获取手机号
            LogUtils.e("onCreate===========phone=========>" + AffordApp.getInstance().getUserLogin().getUSER().getMSISDN() + "");
            mEtPhone.setText(AffordApp.getInstance().getUserLogin().getUSER().getMSISDN() + "");
        } else {
            finish();
        }
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        LogUtils.e("onSuccess==========beanSendUI============>" + beanSendUI.getInfo());
        switch (beanSendUI.getTag()) {
            case AffConstans.BUSINESS.TAG_USER_ADDADDRESS://添加收货地址
                LogUtils.e("onSuccess==========beanSendUI============>" + beanSendUI.getInfo());
                UtilToast.show(this, "地址添加成功！", Toast.LENGTH_SHORT);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        LogUtils.e("onFailure==========beanSendUI============>" + beanSendUI.getInfo());
    }

    @OnClick(R.id.left_back)
    public void leftBack(View v){
        onBackPressed();
    }

    //获取性别的选项值
    @OnRadioGroupCheckedChange(R.id.radiogroup)
    public void onGropCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.man:
                mSex = "1";
                break;
            case R.id.women:
                mSex = "2";
                break;
            default:
                break;
        }
    }

    //是否选中默认
    @OnClick(R.id.default_checkbox)
    public void onIsDefaultCBoxClick(View v) {
        if (mCheckBox.isChecked()) {
            mIsDefault = "1";
        } else {
            mIsDefault = "0";
        }
    }


    @OnClick(R.id.bottom_btn_save)
    public void onBtnSaveAddress(View v) {
        /**********未登录**********/
        mNewName = mEtName.getText().toString().trim();
        mNewPhone = mEtPhone.getText().toString().trim();
        mUserAddress = mEtUserAddress.getText().toString().trim();
        LogUtils.e("onTvBtnSave=========mUserAddress==========>" + mUserAddress);
        if (mNewName.length() <= 0 && "".equals(mNewName)) {
            UtilToast.show(this, "请输入联系人姓名", Toast.LENGTH_SHORT);
        } else if (mNewPhone.length() <= 0 && "".equals(mNewPhone)) {
            UtilToast.show(this, "请输入收货人电话号码", Toast.LENGTH_SHORT);
        } else if (mNewPhone != null && mNewPhone.length() != 11 && !UtilCommon.isMobileNO(mNewPhone)) {
            UtilToast.show(this, "请输入正确的手机号！", Toast.LENGTH_SHORT);
        } else if (!UtilString.isEmpty(mUserAddress)) {
            mBllUser.addaddress(mNewName, mSex, mNewPhone, mUserAddress, mIsDefault);
        } else {
            UtilToast.show(this, "请输入收货地址", Toast.LENGTH_SHORT);
        }
    }
}
