package com.dashihui.afford.ui.activity.server;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessServer;
import com.dashihui.afford.business.BusinessUser;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.business.entity.EtyServerDetail;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.ui.activity.AtyFragmentServerDetail;
import com.dashihui.afford.ui.activity.AtyLogin;
import com.dashihui.afford.ui.activity.AtySettlementOrder;
import com.dashihui.afford.ui.activity.AtySettlementRemark;
import com.dashihui.afford.ui.activity.my.AtyMyAddress;
import com.dashihui.afford.ui.adapter.AdapterServerSettlement;
import com.dashihui.afford.ui.model.ModelDoAdd;
import com.dashihui.afford.ui.model.ModelServerOrder;
import com.dashihui.afford.ui.widget.WgtAlertDialog;
import com.dashihui.afford.util.number.UtilNumber;
import com.dashihui.afford.util.string.UtilString;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtyServerSettlement extends BaseActivity {

    @ViewInject(R.id.left_back)
    private ImageButton mLeftBack;
    @ViewInject(R.id.ibtn_userinfo)
    private ImageButton mIbtEditAddr;
    private BusinessUser mBllUser;
    private BusinessServer mBllServer;
    @ViewInject(R.id.noAddress)
    private TextView mNoAddress;
    @ViewInject(R.id.top_address)
    private LinearLayout mLytTopaddress;
    @ViewInject(R.id.addAddress)
    private LinearLayout mLytAddAddress;


    @ViewInject(R.id.username)
    private TextView mUserName;//用户名字
    @ViewInject(R.id.usersex)
    private TextView mUserSex;//性别
    @ViewInject(R.id.userphone)
    private TextView mUserPhone;//用户电话
    @ViewInject(R.id.useraddr)
    private TextView mUserAddr;//用户地址


    @ViewInject(R.id.payType)
    private RadioGroup mPayType;//支付方式
    @ViewInject(R.id.takeType)
    private RadioGroup takeType;//配送方式
    @ViewInject(R.id.delivery_remark)
    private TextView mRemarkTv;

    @ViewInject(R.id.goods_price)
    private TextView mGoodsPrice;//商品金额
    @ViewInject(R.id.stilpay_money)
    private TextView mPayMoney;//需付款
    @ViewInject(R.id.txtViewPrice)
    private TextView mPrice;//实付款

    @ViewInject(R.id.txtViewSettlement)
    private TextView mTxtViewSettlement;//提交订单

    @ViewInject(R.id.listView)
    private ListView mListView;

    @ViewInject(R.id.delivery_remark)
    private TextView mBtnRemakTv;
    @ViewInject(R.id.serverTimeLyt)
    private LinearLayout mLytServerTime;//服务时间
    @ViewInject(R.id.server_time)
    private TextView mServerTime;


    private AdapterServerSettlement mAdapterSettlement;
    private Map<String, Object> mObjectMap;
    private EtyServerDetail mDetailObject;
    private double mAallNum = 0, mAllPrice = 0d, mSellPrice;
    private String mGoodsID = "", mCount = "", mTitle, mSex, mStoreID;
    public final static String INTENT_TITLE = "title";
    public final static String INTENT_SELLPRICE = "sellPrice";
    public final static String INTENT_GOODSID = "goodsID";
    public final static String INTENT_STOREID = "storeID";


    private ModelDoAdd mModelDoAdd;
    private ModelServerOrder mModelServerOrder;
    private String mOrderInfo;//从FragmentOrderState获取再来一单的订单信息
//    public final static int SERVIECE = 102;//服务标志
//    private Map<String, String> mDefaultAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_server_settlement);
        ViewUtils.inject(this);
        mBllUser = new BusinessUser(this);
        mModelDoAdd = new ModelDoAdd();
        mModelServerOrder = new ModelServerOrder();
        mBllServer = new BusinessServer(this);
        //支付方式初始化
        mModelDoAdd.setPaytype("1");
        //送货方式初始化
        mModelDoAdd.setTaketype("1");
        //初始化服务时间
        mServerTime.setHint("请选择服务时间");
        mServerTime.setText("");
        //初始化数据
        initData();
        //支付方式
        initPayTypeView();
    }


    @Override
    protected void onResume() {
        mTxtViewSettlement.setEnabled(true);
        LogUtils.e("是否登录================>" + AffordApp.isLogin());
        //是否登录
        if (!AffordApp.isLogin()) {
            //没有登录
            LogUtils.e("没有登录======isLogin==========>");
            Intent intent = new Intent(AtyServerSettlement.this, AtyLogin.class);
            intent.putExtra(CommConstans.Login.INTENT_KEY, CommConstans.Login.INTENT_VALUE_SETTLEMENT);
            startActivity(intent);
            finish();
        }

        //获取默认地址
        mBllUser.defaultAddress();
        LogUtils.e("登录======isLogin==========>");
        super.onResume();
    }


    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_USER_DEFAULTADDRESS://默认地址
                    LogUtils.e("onSuccess======默认地址========>" + beanSendUI.getInfo());
                    Map<String, String> mDefaultAddr = (Map<String, String>) beanSendUI.getInfo();
                    mNoAddress.setVisibility(View.GONE);
                    mLytTopaddress.setVisibility(View.VISIBLE);
                    mUserName.setText(mDefaultAddr.get("LINKNAME") + "");//用户名字
                    mSex = mDefaultAddr.get("SEX") + "";
                    /****&&  设置性别   &&******/
                    if ("1".equals(mSex)) {
                        mUserSex.setText("先生 ");//性别
                        mModelDoAdd.setSex("先生 ");
                    } else {
                        mUserSex.setText("女士");//性别
                        mModelDoAdd.setSex("女士");
                    }
                    mUserPhone.setText(mDefaultAddr.get("TEL") + "");//用户电话
                    mUserAddr.setText(mDefaultAddr.get("ADDRESS") + "");//用户地址
                    break;
                case AffConstans.BUSINESS.TAG_SERVICE_ORDER_SAVE://服务保存订单(立即预约)
                    mObjectMap = (Map<String, Object>) beanSendUI.getInfo();
                    Map<String, Object> order = (Map<String, Object>) beanSendUI.getInfo();
                    LogUtils.e("onSuccess======提交订单========>" + order);
                    if (order != null) {
                        //如果在线支付直接跳转到支付页面，如果是服务后付款，则带标志跳至详情页
                        if ("1".equals(mModelDoAdd.getPaytype())) {
                            Intent mapIntent = new Intent(this, AtySettlementOrder.class);
                            mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, order.get("AMOUNT") + "");
                            mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, order.get("ORDERNUM") + "");
                            //传输一个支付标志，区分商品、服务、家政订单支付
                            mapIntent.putExtra(AtySettlementOrder.ORDER_PAY_TYPE, CommConstans.ORDER.SERVER_ORDER_PAY);
                            startActivity(mapIntent);
                            /**************************************************/
                        } else {
                            Intent mapIntent = new Intent(this, AtyFragmentServerDetail.class);
                            mapIntent.putExtra(AtySettlementOrder.ORDER_PAY_TYPE, CommConstans.ORDER.SERVER_ORDER_PAY);
                            mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, order.get("AMOUNT") + "");
                            mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, order.get("ORDERNUM") + "");
                            startActivity(mapIntent);
                        }
                        finish();
                    } else {
                        LogUtils.e("onSuccess======提交订单===error=====>" + order);
                    }
                    break;

                default:
                    LogUtils.e("onSuccess===default===========>" + beanSendUI);
                    break;
            }
        } else {
            LogUtils.e("onSuccess======AtyHome=========>" + beanSendUI);
        }
        //去提交订单按钮恢复
        mTxtViewSettlement.setEnabled(true);

    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_USER_DEFAULTADDRESS://默认地址
                    /**********  设置地址  **********/
                    mUserAddr.setText("");
                    /**********  收货人姓名 **********/
                    mUserName.setText("");
                    /**********  收货人电话 **********/
                    mUserPhone.setText("");
                    mNoAddress.setVisibility(View.VISIBLE);
                    mLytTopaddress.setVisibility(View.GONE);
                    break;
                case AffConstans.BUSINESS.TAG_SERVICE_ORDER_SAVE://提交订单
                    LogUtils.e("onFailure======提交订单========>" + beanSendUI.getInfo());
                    if (beanSendUI!=null && beanSendUI.getInfo()!=null){
                        if (mDialog == null) {
                            mDialog = new WgtAlertDialog();
                        }
                        mDialog.show(this,
                                "确定",
                                beanSendUI.getInfo().toString(),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mDialog.dismiss();
                                        AtyServerSettlement.this.finish();
                                    }
                                }, false, false);
                    }else {
                        LogUtils.e("onFailure===default===========>" + beanSendUI);
                    }

                    break;
                default:

                    break;
            }
        } else {
            LogUtils.e("onFailure======AtySettlement=========>" + beanSendUI);
        }
        //去提交订单按钮恢复
        mTxtViewSettlement.setEnabled(true);
    }
    //支付方式
    public void initPayTypeView() {
        mPayType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                /****&&  设置支付方式   &&******/
                switch (checkedId) {
                    case R.id.confirm_weixin_cBoxAgree://线上支付
                        mModelDoAdd.setPaytype("1");
                        LogUtils.e("onPayTypeCheckedChanged======线上支付========>" + checkedId);
                        break;
                    case R.id.confirm_delivery_cBoxAgree://服务后付款
                        mModelDoAdd.setPaytype("2");
                        LogUtils.e("onPayTypeCheckedChanged======服务后付款=======>" + checkedId);
                        break;
                    default:
                        break;
                }
            }
        });
    }



//    //配送方式
//    @OnRadioGroupCheckedChange(R.id.takeType)
//    public void onTakeTypeCheckedChanged(RadioGroup group, int checkedId) {
//        LogUtils.e("onTakeTypeCheckedChanged======AtySettlement=========>" + checkedId);
//        switch (checkedId) {
//            case R.id.confirm_store_cBoxAgree://门店配送
//                mModelDoAdd.setTaketype("1");
//                LogUtils.e("onTakeTypeCheckedChanged======门店配送=======>" + checkedId);
//                break;
//            case R.id.confirm_myself_cBoxAgree://上门自取
//                mModelDoAdd.setTaketype("2");
//                LogUtils.e("onTakeTypeCheckedChanged======上门自取=======>" + checkedId);
//                break;
//            default:
//                break;
//        }
//    }

    /**
     * 提交订单
     *
     * @param v
     */
    @OnClick(R.id.txtViewSettlement)//立即预约
    public void onSettlementClick(View v) {
        //禁止多次提交
        mTxtViewSettlement.setEnabled(false);
        /**********  设置地址  **********/
        String address = mUserAddr.getText().toString().trim();
        /**********  收货人姓名 **********/
        String userName = mUserName.getText().toString().trim();
        /**********  收货人电话 **********/
        String phone = mUserPhone.getText().toString().trim();
        /**********  订单备注 **********/
        String describe = mRemarkTv.getText().toString().trim();
        /**********  服务时间 **********/
        String time = mServerTime.getText().toString().trim();
        /**********  地址信息 **********/
        String addressInfo = userName + mSex + phone + address;
//
//        LogUtils.e("收货人电话============>" + phone);
//        LogUtils.e("地址===============>" + address);
//        LogUtils.e("收货人姓名===============>" + userName);
//        LogUtils.e("订单备注============>" + describe);

        if (!"".equals(address) && !"".equals(userName) && !"".equals(phone) && !UtilString.isEmpty(time)) {
            mModelServerOrder.setAddress(address);
            mModelServerOrder.setLinkname(userName);
            mModelServerOrder.setTel(phone);
            mModelServerOrder.setDescribe(describe);
            mModelServerOrder.setStoreid(mStoreID);
            mModelServerOrder.setPaytype(mModelDoAdd.getPaytype());
            mModelServerOrder.setAmount(mAllPrice + "");
            mModelServerOrder.setSex(mSex);
            mModelServerOrder.setServicesid(mGoodsID);
            mModelServerOrder.setSertime(mServerTime.getText().toString());

            mBllServer.getOrderSave(mModelServerOrder);

        } else {
            if (mDialog == null) {
                mDialog = new WgtAlertDialog();
            }
            if ("".equals(address) && "".equals(userName) && "".equals(phone)) {

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
            mTxtViewSettlement.setEnabled(true);//解除禁止多次提交
        }
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

    /**
     * 动态设置listview高度
     *
     * @param listView
     */
    private void setlistViewHeigh(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        listView.setLayoutParams(params);

    }

    @OnClick(R.id.serverTimeLyt)
    public void onTimeLytClick(View v) {
        Intent intent = new Intent(AtyServerSettlement.this, AtyFragmentServerTime.class);
        startActivityForResult(intent, CommConstans.ADDRESS.SERVERDATAS);
    }

    @OnClick(R.id.lyt_remark)//备注
    public void onLytRemarkClick(View v) {
        Intent intent = new Intent(AtyServerSettlement.this, AtySettlementRemark.class);
        startActivityForResult(intent, CommConstans.ADDRESS.MARS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CommConstans.ADDRESS.MARS:
                if (data != null && data.getExtras().size() > 0) {
                    Bundle _remBunlde = data.getExtras();
                    String remText = _remBunlde.getString(AtySettlementRemark.REMARK);

                    mRemarkTv.setText(remText);
                } else {
                    mRemarkTv.setText("");
                    mRemarkTv.setHint("配送备注（可选）");
                }
                break;
            case CommConstans.ADDRESS.SERVERDATAS:
                LogUtils.e("onActivityResult=========data=========>" + data);
                if (data != null) {
                    Bundle _time = data.getExtras();
                    String dataStr = _time.getString(AtyFragmentServerTime.DATEFLAG);
                    LogUtils.e("onActivityResult=========datas=========>" + dataStr);
                    mServerTime.setText(dataStr);
                } else {
                    mServerTime.setHint("请选择服务时间");
                    mServerTime.setText("");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance().getEntityLocation().getSTORE() != null) {
            mTitle = getIntent().getStringExtra(AtyServerDetail.TITLE);
            mGoodsID = getIntent().getStringExtra(AtyServerDetail.GOODSID);
            mStoreID = getIntent().getStringExtra(AtyServerDetail.STOREID);
            mSellPrice = UtilNumber.DoubleValueOf(getIntent().getStringExtra(AtyServerDetail.SELLPRICE));
            LogUtils.e("商品名=====title========>" + mTitle);
            LogUtils.e("服务价钱====sellprice=========>" + mSellPrice);
            mAllPrice += mSellPrice;
            //把传递过来的订单信息封装到listMap
            List<Map<String, Object>> listmap = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put(INTENT_TITLE, mTitle);
            map.put(INTENT_SELLPRICE, mSellPrice);
            map.put(INTENT_GOODSID, mGoodsID);
            map.put(INTENT_STOREID, mStoreID);
            listmap.add(map);
            mAdapterSettlement = new AdapterServerSettlement(this, listmap);
            LogUtils.e("initData====listmap=========>" + listmap);
            mListView.setAdapter(mAdapterSettlement);
            setlistViewHeigh(mListView);

            mGoodsPrice.setText("" + UtilNumber.DoubleValueOf(mAllPrice + "") + "");//商品金额
            mPayMoney.setText("" + UtilNumber.DoubleValueOf(mAllPrice + "") + "");//需付款
            mPrice.setText("实付款：￥" + UtilNumber.DoubleValueOf(mAllPrice + "") + "");//实付款

        }
    }
}
