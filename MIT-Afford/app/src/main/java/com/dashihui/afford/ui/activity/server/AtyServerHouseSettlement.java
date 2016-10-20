package com.dashihui.afford.ui.activity.server;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessServer;
import com.dashihui.afford.business.BusinessUser;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.ui.activity.AtyFragmentServerDetail;
import com.dashihui.afford.ui.activity.AtyLogin;
import com.dashihui.afford.ui.activity.AtySettlementOrder;
import com.dashihui.afford.ui.activity.AtySettlementRemark;
import com.dashihui.afford.ui.activity.my.AtyMyAddress;
import com.dashihui.afford.ui.model.ModelDoAdd;
import com.dashihui.afford.ui.model.ModelServerOrder;
import com.dashihui.afford.ui.widget.WgtAlertDialog;
import com.dashihui.afford.util.string.UtilString;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnRadioGroupCheckedChange;

import java.util.Map;

public class AtyServerHouseSettlement extends BaseActivity {
    @ViewInject(R.id.username)
    private TextView mTvUserName;//名字
    @ViewInject(R.id.usersex)
    private TextView mTvUserSex;//性别
    @ViewInject(R.id.userphone)
    private TextView mTvUserPhone;//用户电话
    @ViewInject(R.id.useraddr)
    private TextView mTvUserAddress;//用户地址
    @ViewInject(R.id.noAddress)
    private TextView mTvNoAddress;//没有地址时的提示文字
    @ViewInject(R.id.delivery_remark)
    private TextView mTvRemark;//备注
    @ViewInject(R.id.server_time)
    private TextView mTvSeverTime;//服务时间
    @ViewInject(R.id.clean_class)
    private TextView mTvCleanType;//服务类别
    @ViewInject(R.id.cBoxAgree)
    private CheckBox mCleanCheckBox;//清洁剂选择框
    @ViewInject(R.id.goods_price)
    private TextView mTvTotalPrice;//服务总价格
    @ViewInject(R.id.time)
    private TextView mTvSerTime;//服务预计时间
    @ViewInject(R.id.lyt_cleanser)
    private LinearLayout mLytCleanser;//清洁剂布局

    @ViewInject(R.id.txtViewPrice)
    private TextView mTotalMoney;//总计金额

    @ViewInject(R.id.top_address)
    private LinearLayout mLytTopAddress;//地址
    @ViewInject(R.id.txtViewSettlement)
    private TextView mTvBtnSettlement;//提交订单

    private ModelServerOrder mModelServerOrder;

    //性别、服务数量、服务订单价格、服务类型、服务预订时间、支付类型
    private String mSex, mSerCount, mSerOrderPrice, mType, mSerTime, mSerTitle, mPayType;
    private String mIsChooseCleanser;//是否选择清洁剂

    private ModelDoAdd mModelDoAdd;
    private BusinessUser mBllUser;
    private BusinessServer mBllServer;
    private Map<String, Object> mObjectMap;//请求地址的集合
//    public final static int SER_HOUSE = 101;//服务家政标志

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_server_house_settlement);
        ViewUtils.inject(this);
        mModelDoAdd = new ModelDoAdd();
        mModelServerOrder = new ModelServerOrder();
        mBllUser = new BusinessUser(this);
        mBllServer = new BusinessServer(this);
        mModelDoAdd.setPaytype("1");//默认支付方式
        initData();
    }

    //获取上一页传递过来的数据
    public void initData() {
        Intent intent = getIntent();
        mType = intent.getStringExtra(AtyServerHouseClean.SERHOME_SERTYPE);
        LogUtils.e("initData=========mType==========>" + mType);
        mSerCount = intent.getStringExtra(AtyServerHouseClean.SERHOME_ORDERCOUNT);
        mSerOrderPrice = intent.getStringExtra(AtyServerHouseClean.SERHOME_ORDERPRICE);
        mSerTitle = intent.getStringExtra(AtyServerHouseClean.SERHOME_SERTITLE);
//        LogUtils.e("initData=========mSerCount==========>" + mSerCount);
//        LogUtils.e("initData=========mSerOrderPrice==========>" + mSerOrderPrice);
        if ("1".equals(mType)) {
            mTvCleanType.setText("日常保洁");
            mSerTime = intent.getStringExtra(AtyServerHouseClean.SERHOME_SERTIME);
            LogUtils.e("initData=========mSerTime==========>" + mSerTime);
            mTvSerTime.setText(mSerTime + "小时");
            mLytCleanser.setVisibility(View.GONE);
        } else {
            mTvCleanType.setText("深度保洁");
            mTvSerTime.setText(mSerTitle);
            mLytCleanser.setVisibility(View.GONE);
        }
        //把价钱写入控件
        mTvTotalPrice.setText("￥" + mSerOrderPrice + "元");
        mTotalMoney.setText("总计:￥" + mSerOrderPrice + "元");
    }


    @Override
    protected void onResume() {
        mTvBtnSettlement.setEnabled(true);//解除禁止多次提交
        if (!AffordApp.isLogin()) {
            //没有登录
            LogUtils.e("没有登录======isLogin==========>");
            Intent intent = new Intent(AtyServerHouseSettlement.this, AtyLogin.class);
            intent.putExtra(CommConstans.Login.INTENT_KEY, CommConstans.Login.INTENT_VALUE_SETTLEMENT);
            startActivity(intent);
            finish();
        }
        /****&&  设置店铺ID   &&******/
        if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance().getEntityLocation().getSTORE() != null) {
            mModelDoAdd.setStoreid(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
        } else {
            LogUtils.e("null===Error====AtySettlement===>" + AffordApp.LOG_PHONE);
            finish();
        }

        //获取默认地址
        mBllUser.defaultAddress();
        LogUtils.e("登录======isLogin==========>");
        super.onResume();
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        LogUtils.e("onSuccess=========beanSendUI===========>" + beanSendUI.getInfo());
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_USER_DEFAULTADDRESS://默认地址
                    mObjectMap = (Map<String, Object>) beanSendUI.getInfo();
                    mTvNoAddress.setVisibility(View.GONE);
                    mLytTopAddress.setVisibility(View.VISIBLE);
                    mTvUserName.setText(mObjectMap.get("LINKNAME") + "");//用户名字
                    mSex = mObjectMap.get("SEX") + "";
                    /****&&  设置性别   &&******/
                    if ("1".equals(mSex)) {
                        mTvUserSex.setText("先生 ");//性别
                        mModelDoAdd.setSex("先生 ");
                    } else {
                        mTvUserSex.setText("女士");//性别
                        mModelDoAdd.setSex("女士");
                    }
                    mTvUserPhone.setText(mObjectMap.get("TEL") + "");//用户电话
                    mTvUserAddress.setText(mObjectMap.get("ADDRESS") + "");//用户地址

                    break;
                case AffConstans.BUSINESS.TAG_SER_ORDER_SAVE://服务家政保存订单（订单提交）
                    LogUtils.e("onSuccess=========家政=========>" + beanSendUI.getInfo());
                    Map<String, Object> mObjectMap = (Map<String, Object>) beanSendUI.getInfo();
                    LogUtils.e("onSuccess=========mObjectMap=========>" + mObjectMap);
                    LogUtils.e("onSuccess=========mModelServerOrder.getType()=========>" + mModelServerOrder.getType());
                    if ("1".equals(mModelServerOrder.getPaytype())) {
                        Intent intent = new Intent(AtyServerHouseSettlement.this, AtySettlementOrder.class);
                        intent.putExtra(AtySettlementOrder.ORDER_CODE, mObjectMap.get("ORDERNUM") + "");
                        intent.putExtra(AtySettlementOrder.ORDER_PRICE, mObjectMap.get("AMOUNT") + "");
                        //传输一个支付标志，区分商品、服务、家政订单支付
                        intent.putExtra(AtySettlementOrder.ORDER_PAY_TYPE, CommConstans.ORDER.HOUSE_ORDER_PAY);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(AtyServerHouseSettlement.this, AtyFragmentServerDetail.class);
                        LogUtils.e("onSuccess========请求成功，服务后付款=mType==========>" + mType);

                        //家政日常保洁、深度保洁
                        intent.putExtra(AtySettlementOrder.ORDER_PAY_TYPE, CommConstans.ORDER.HOUSE_ORDER_PAY);
                        intent.putExtra(AtySettlementOrder.ORDER_CODE, mObjectMap.get("ORDERNUM") + "");
                        intent.putExtra(AtySettlementOrder.ORDER_PRICE, mObjectMap.get("AMOUNT") + "");
                        startActivity(intent);
                        finish();
                    }

                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        switch (beanSendUI.getTag()) {
            case AffConstans.BUSINESS.TAG_USER_DEFAULTADDRESS://默认地址
                /********用户名*********/
                mTvUserName.setText("");
                ;
                /********用户电话*********/
                mTvUserPhone.setText("");
                /********用户地址*********/
                mTvUserAddress.setText("");
                mTvNoAddress.setVisibility(View.VISIBLE);
                mLytTopAddress.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        LogUtils.e("onFailure===========请求失败=============>" + beanSendUI.getInfo());
    }

    @OnClick(R.id.left_back)//返回
    public void onBackClick(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick({(R.id.rlyt_address), (R.id.serverTimeLyt), (R.id.ibtn_userinfo)})
    public void onAddAdressClick(View v) {
        switch (v.getId()) {
            case R.id.rlyt_address:
            case R.id.ibtn_userinfo:
                mBaseUtilAty.startActivity(AtyMyAddress.class);
                break;
            case R.id.serverTimeLyt:
                mBaseUtilAty.startActivity(AtyFragmentServerTime.class);
                break;
            default:
                break;
        }
    }

    //服务时间
    @OnClick(R.id.serverTimeLyt)
    public void onTimeLytClick(View v) {
        Intent intent = new Intent(AtyServerHouseSettlement.this, AtyFragmentServerTime.class);
        startActivityForResult(intent, CommConstans.ADDRESS.SERVERDATAS);
    }

    //清洁剂
    @OnClick(R.id.cBoxAgree)
    public void onCheckBoxclick(View v) {
        //根据是否选择设置不同颜色
        LogUtils.e("onCheckBoxclick=======mCleanCheckBox.isChecked()======>" + mCleanCheckBox.isChecked());
        if (mCleanCheckBox.isChecked()) {
            mCleanCheckBox.setTextColor(Color.parseColor("#555555"));
            mIsChooseCleanser = "1";
            LogUtils.e("=========mIsChooseCleanser=====1====>" + mIsChooseCleanser);
        } else {
            mCleanCheckBox.setTextColor(Color.parseColor("#999999"));
            mIsChooseCleanser = "2";
            LogUtils.e("=========mIsChooseCleanser====2=====>" + mIsChooseCleanser);
        }
    }

    @OnClick(R.id.lyt_remark)//备注
    public void onLytRemarkClick(View v) {
        Intent intent = new Intent(AtyServerHouseSettlement.this, AtySettlementRemark.class);
        startActivityForResult(intent, CommConstans.ADDRESS.MARS);
    }

    //支付方式
    @OnRadioGroupCheckedChange(R.id.payType)
    public void onPayTypeCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.confirm_weixin_cBoxAgree:
                mModelDoAdd.setPaytype("1");
                LogUtils.e("onPayTypeCheckedChanged=======setPaytype==1======>" + checkedId);
                break;
            case R.id.confirm_delivery_cBoxAgree:
                mModelDoAdd.setPaytype("2");
                LogUtils.e("onPayTypeCheckedChanged=======setPaytype==2======>" + checkedId);
                break;
            default:
                break;
        }
    }

    //提交订单
    @OnClick(R.id.txtViewSettlement)
    public void onTvBrnSettlementClick(View v) {
        mTvBtnSettlement.setEnabled(false);//禁止多次提交
        /********用户名*********/
        String userName = mTvUserName.getText().toString();
        /********用户电话*********/
        String userPhone = mTvUserPhone.getText().toString();
        /********用户地址*********/
        String address = mTvUserAddress.getText().toString();
        /********服务预约时间*********/
        String serTime = mTvSeverTime.getText().toString();
        /********订单备注*********/
        String remark = mTvRemark.getText().toString();

        if (!"".equals(address) && !"".equals(userName) && !"".equals(userPhone) && !UtilString.isEmpty(serTime)) {
            mModelServerOrder.setStoreid(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");//店铺Id(社区ID)
            LogUtils.e("OnClick=========setStoreid==========>" + AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");

            mModelServerOrder.setStorename(AffordApp.getInstance().getEntityLocation().getSTORE().getTITLE());//店铺名称（社区名称）
            LogUtils.e("OnClick=========mType==========>" + mType);
            mModelServerOrder.setType(mType);//服务类型
            mModelServerOrder.setLinkname(userName);//用户名称
            mModelServerOrder.setSex(mSex);//性别
            mModelServerOrder.setTel(userPhone);//电话
            mModelServerOrder.setAddress(address);//地址
            mModelServerOrder.setSertime(serTime);//服务预约时间
            mModelServerOrder.setTotaltime(mSerTime);//服务时长
            mModelServerOrder.setTitle(mSerTitle);//服务标题
            mModelServerOrder.setUnitprice(mSerOrderPrice);//服务单价
            mModelServerOrder.setCount(mSerCount);//服务数量
            mModelServerOrder.setAmount(mSerOrderPrice);//总金额
            mModelServerOrder.setPaytype(mModelDoAdd.getPaytype());//支付类型
            if (UtilString.isEmpty(remark)) {
                remark = "无备注";
            }
            mModelServerOrder.setDescribe(remark);//订单备注
            /**********************************/
//            LogUtils.e("onTvBrnSettlementClick===========setStoreid=============>" + AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
//            LogUtils.e("onTvBrnSettlementClick===========setStorename=============>" + AffordApp.getInstance().getEntityLocation().getSTORE().getTITLE());
//            LogUtils.e("onTvBrnSettlementClick===========Paytype=============>" + mModelDoAdd.getPaytype());
//            LogUtils.e("onTvBrnSettlementClick===========remark=============>" + remark);
//            LogUtils.e("onTvBrnSettlementClick===========setSertime=============>" + serTime);
//            LogUtils.e("onTvBrnSettlementClick===========mSerOrderPrice=============>" + mSerOrderPrice);
//            LogUtils.e("onTvBrnSettlementClick===========setAmount=============>" + mSerOrderPrice);
//            LogUtils.e("onTvBrnSettlementClick===========setTotaltime=============>" + mSerTime);
            /**********************************/
            mBllServer.getSerOrderSave(mModelServerOrder);
            LogUtils.e("提交=========mModelServerOrder==========>" + mModelServerOrder.getType());
            LogUtils.e("提交=========mModelServerOrder.getStoreid==========>" + mModelServerOrder.getStoreid());
        } else {
            if (mDialog == null) {
                mDialog = new WgtAlertDialog();
            }
            if ("".equals(address) && "".equals(userName) && "".equals(userPhone)) {

                mDialog.show(this,
                        "确定",
                        "请选择服务地址！",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.dismiss();
                                mBaseUtilAty.startActivity(AtyMyAddress.class);
                            }
                        });
            } else {
                mDialog.show(this,
                        "确定",
                        "请选择服务时间！",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.dismiss();
                            }
                        });
            }
            mTvBtnSettlement.setEnabled(true);//解除禁止多次提交
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CommConstans.ADDRESS.MARS:
                if (data != null && data.getExtras().size() > 0) {
                    Bundle _remBunlde = data.getExtras();
                    String remText = _remBunlde.getString(AtySettlementRemark.REMARK);
                    mTvRemark.setText(remText);
                } else {
                    mTvRemark.setText("");
                    mTvRemark.setHint("配送备注（可选）");
                }
                break;
            case CommConstans.ADDRESS.SERVERDATAS:
                LogUtils.e("onActivityResult=========data=========>" + data);
                if (data != null) {
                    Bundle _time = data.getExtras();
                    String dataStr = _time.getString(AtyFragmentServerTime.DATEFLAG);
                    LogUtils.e("onActivityResult=========datas=========>" + dataStr);
                    mTvSeverTime.setText(dataStr);
                } else {
                    mTvSeverTime.setHint("请选择服务时间");
                    mTvSeverTime.setText("");
                }
                break;
            default:
                break;
        }
    }
}
