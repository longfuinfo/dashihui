package com.dashihui.afford.ui.activity.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessUser;
import com.dashihui.afford.business.entity.EtyMoney;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseMenuActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.sqlite.SqliteBrowseHistory;
import com.dashihui.afford.thirdapi.greedsqlite.BrowseHistory;
import com.dashihui.afford.ui.activity.AtyFragmentOrdert;
import com.dashihui.afford.ui.activity.AtyFragmentService;
import com.dashihui.afford.ui.activity.AtyLogin;
import com.dashihui.afford.util.number.UtilNumber;
import com.dashihui.afford.util.preferences.UtilPreferences;
import com.dashihui.afford.wxapi.WXEntryActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;
import java.util.Map;

public class AtyMy extends BaseMenuActivity {

    @ViewInject(R.id.head_login)
    private RelativeLayout mRytLogin;
    @ViewInject(R.id.head_NoLogin)
    private RelativeLayout mRlytNoLogin;//没有登录
    @ViewInject(R.id.my_login_username)
    private TextView mTxtUserName;//用户名
    @ViewInject(R.id.my_att_num)
    private TextView mUnLoginCollectNums;//未登录时关注数量
    @ViewInject(R.id.mylogin_att_num)
    private TextView mCollectNums;//关注数量
    @ViewInject(R.id.my_head_login)
    private ImageView mHead;//用户头像

    @ViewInject(R.id.iv_baijin)
    private ImageView ivBaijin;//用户头像

    /**
     * 6个数字提醒
     */
    @ViewInject(R.id.order_left_hintnum)
    private TextView mTvOrderLeftNum;//订单待付款
    @ViewInject(R.id.order_middle_hintnum)
    private TextView mTvOrderMidNum;//订单待收货
    @ViewInject(R.id.order_right_hintnum)
    private TextView mTvOrderRightNum;//订单待评价
    @ViewInject(R.id.server_left_hintnum)
    private TextView mTvSerLeftNum;//服务订单待付款
    @ViewInject(R.id.server_middle_hintnum)
    private TextView mTvSerMidNum;//服务订单待收货
    @ViewInject(R.id.server_right_hintnum)
    private TextView mTvSerRigNum;//服务订单待评价

    @ViewInject(R.id.mylogin_browse_num)
    private TextView mBrowseNum1;
    @ViewInject(R.id.my_head_login)
    private ImageView mImgHeadLogin;
    @ViewInject(R.id.my_browse_num)
    private TextView mBrowseNums2;//浏览历史
    @ViewInject(R.id.my_member_lv)
    private TextView mMemberLv;//会员等级

    /**************
     * 我的订单
     ***********/
    private Map<String, Object> mTotalMap;
    private BusinessUser mBllUser;
    //是否刷新数据标识
    private final static String MY_REFRESH = "MyRefresh";



    @Override
    public int getContentViewLayoutResId() {
        return R.layout.aty_my;
    }

    @Override
    protected void onCreatOverride(Bundle savedInstanceState) {
        ViewUtils.inject(this);//依赖注入
        mBllUser = new BusinessUser(this);
    }

    @Override
    protected void onResume() {
        //浏览历史个数
        if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance()
                .getEntityLocation().getSTORE() != null) {
            List<BrowseHistory> browseHistoryList = SqliteBrowseHistory.getInstance(this)
                    .getListBrowseHistoryByShopID(AffordApp.getInstance().getEntityLocation()
                            .getSTORE().getID() + "");
            mBrowseNum1.setText(browseHistoryList.size() + "");
            mBrowseNums2.setText(browseHistoryList.size() + "");
        }
        showProDialog(this);
        //统计
        mBllUser.getOrderState();//商品订单、服务订单统计

        if (!AffordApp.isLogin()) {
            mRlytNoLogin.setVisibility(View.VISIBLE);
            mRytLogin.setVisibility(View.GONE);
        } else {
            mRlytNoLogin.setVisibility(View.GONE);
            mRytLogin.setVisibility(View.VISIBLE);
        }
        if (AffordApp.getInstance().getUserLogin() != null && AffordApp.getInstance()
                .getUserLogin().getUSER() != null) {
            //获取用户名
            mTxtUserName.setText(AffordApp.getInstance().getUserLogin().getUSER().getNICKNAME());
            //获取用户会员等级
            LogUtils.e("onSuccess===会员等级===========>" + AffordApp.getInstance().getUserLogin()
                    .getUSER().getLEVEL());
            if (AffordApp.getInstance().getUserLogin().getUSER().getLEVEL() == 2) {
                mMemberLv.setText("白金会员");
                ivBaijin.setVisibility(View.VISIBLE);
            } else {
                mMemberLv.setText("普通用户");
                ivBaijin.setVisibility(View.GONE);

            }
            mMemberLv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        }
        super.onResume();
    }

    @Override
    public void onSuccess(EtySendToUI info) {
        if (info != null) {
            switch (info.getTag()) {
                case AffConstans.BUSINESS.TAG_USER_STATE://用户订单状态查询
                    mTotalMap = (Map<String, Object>) info.getInfo();
                    dissProDialog();
                    LogUtils.e("onSuccess===订单统计状态===========>" + info.getInfo());
                    //商品订单
                    int _nopay = UtilNumber.IntegerValueOf(mTotalMap.get("NOPAY") + "");
                    int _waitdeliver = UtilNumber.IntegerValueOf(mTotalMap.get("NOTAKE") + "");
                    int _notake = UtilNumber.IntegerValueOf(mTotalMap.get("NOEVAL") + "");
                    //服务订单
                    int serNoPay = UtilNumber.IntegerValueOf(mTotalMap.get("SERNOPAY") + "");
                    int serSerno = UtilNumber.IntegerValueOf(mTotalMap.get("SERNOSERVICE") + "");
                    int serNoTake = UtilNumber.IntegerValueOf(mTotalMap.get("") + "");
                    int collected = UtilNumber.IntegerValueOf(mTotalMap.get("COLLECTED") + "");
                    if (_nopay > 0) {//待付款
                        mTvOrderLeftNum.setVisibility(View.VISIBLE);
                        mTvOrderLeftNum.setText(_nopay + "");
                    } else {
                        mTvOrderLeftNum.setVisibility(View.GONE);
                    }

                    if (_waitdeliver > 0) {//待收货
                        mTvOrderMidNum.setVisibility(View.VISIBLE);
                        mTvOrderMidNum.setText(_waitdeliver + "");
                    } else {
                        mTvOrderMidNum.setVisibility(View.GONE);
                    }

                    if (_notake > 0) {//待评价
                        mTvOrderRightNum.setVisibility(View.VISIBLE);
                        mTvOrderRightNum.setText(_notake + "");
                    } else {
                        mTvOrderRightNum.setVisibility(View.GONE);
                    }

                    if (serNoPay > 0) {//服务待付款
                        mTvSerLeftNum.setVisibility(View.VISIBLE);
                        mTvSerLeftNum.setText(serNoPay + "");
                    } else {
                        mTvSerLeftNum.setVisibility(View.GONE);
                    }

                    if (serSerno > 0) {//服务待服务
                        mTvSerMidNum.setVisibility(View.VISIBLE);
                        mTvSerMidNum.setText(serSerno + "");
                    } else {
                        mTvSerMidNum.setVisibility(View.GONE);
                    }

                    if (serNoTake > 0) {//服务待评价
                        mTvSerRigNum.setVisibility(View.VISIBLE);
                        mTvSerRigNum.setText(serNoTake + "");
                    } else {
                        mTvSerRigNum.setVisibility(View.GONE);
                    }
                    if (collected > 0) {//关注
                        mCollectNums.setText(collected + "");
                    } else {
                        mCollectNums.setText(0 + "");
                    }

                    LogUtils.e("onSuccess===========收藏==========>" + mTotalMap.get("COLLECTED") +
                            "");
                    break;

                case AffConstans.BUSINESS.TAG_USER_INFO:
                    //获取用户关注商品数
                    break;
                default:
                    LogUtils.e("onSuccess===default===========>" + info);
                    //定位
                    break;
            }
        } else {
            LogUtils.e("onSuccess======AtyAffordShop=========>" + info);
        }
        //退出登录后清空所有订单状态数字并隐藏
        if (!AffordApp.isLogin()) {
            noVisibilityNum();
        }
    }

    @Override
    public void onFailure(EtySendToUI info) {
        if (info != null) {
            switch (info.getTag()) {
                case AffConstans.BUSINESS.TAG_USER_STATE://用户订单状态查询
                    dissProDialog();
                    break;
                default:
                    LogUtils.e("onFailure===default===========>" + info);
                    //定位
                    break;
            }
        } else {
            LogUtils.e("onFailure======AtyAffordShop=========>" + info);
        }
    }

    //关注商品myBrowseLyt
    @OnClick({(R.id.lyt_collect), (R.id.lyt_collect_unlogin), (R.id.browseHistoryNumLyt), (R.id
            .myBrowseLyt), (R.id.lyt_share)})
    public void onLytCollectClick(View v) {
        switch (v.getId()) {
            case R.id.lyt_collect://关注商品
                mBaseUtilAty.startActivity(AtyMyCollect.class);
                break;
            case R.id.lyt_collect_unlogin://未登录状态下的关注商品
                mUnLoginCollectNums.setText("0");
                mBaseUtilAty.startActivity(AtyLogin.class);
                break;
            case R.id.myBrowseLyt://未登录浏览历史
                mBaseUtilAty.startActivity(AtyLogin.class);
                break;
            case R.id.browseHistoryNumLyt://浏览历史
                mBaseUtilAty.startActivity(AtyMyBrowseHistory.class);
                break;
            case R.id.lyt_share://分享

                Intent intent = new Intent(this, WXEntryActivity.class);
                intent.putExtra("desc", getResources().getString(R.string.my_sharedesc));
                intent.putExtra("title", getResources().getString(R.string.my_sharetitle));
                intent.putExtra("link", getResources().getString(R.string.my_shareurl));
                Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable
                        .about_icon_logo);
                intent.putExtra("thumb", thumb);
                startActivity(intent);

                break;
            default:
                break;
        }

    }

    @OnClick(R.id.my_member_lv)//会员等级
    public void onMemberLv(View v) {
        mBaseUtilAty.startActivity(AtyMyMember.class);
    }

    @OnClick(R.id.lyt_wallet)//我的钱包
    public void onLtyUser(View v) {
        if (AffordApp.isLogin()) {
            mBaseUtilAty.startActivity(AtyMyWallet.class);
        } else {
            Toast.makeText(this, "请登录", Toast.LENGTH_SHORT).show();
            mBaseUtilAty.startActivity(AtyLogin.class);
        }
    }

    @OnClick(R.id.lyoutRefund)//退款
    public void onLyoutRefundClick(View v) {
        if (AffordApp.isLogin()) {
            Intent inforIntent = new Intent(AtyMy.this, AtyRefund.class);
            startActivity(inforIntent);
        } else {
            mBaseUtilAty.startActivity(AtyLogin.class);
        }

    }

    @OnClick(R.id.order_wait_pay)//代付款
    public void onWaitOrderClick(View v) {
        if (AffordApp.isLogin()) {
            Intent inforIntent = new Intent(AtyMy.this, AtyFragmentOrdert.class);
            // 放入当前点击的位置
            inforIntent.putExtra("tabCode", 1);
            startActivity(inforIntent);
        } else {
            mBaseUtilAty.startActivity(AtyLogin.class);
        }

    }

    @OnClick(R.id.wait_send_goods)
    public void onSendOrderClick(View v) {
        if (AffordApp.isLogin()) {
            Intent inforIntent = new Intent(AtyMy.this, AtyFragmentOrdert.class);
            // 放入当前点击的位置
            inforIntent.putExtra("tabCode", 2);
            startActivity(inforIntent);
        } else {
            mBaseUtilAty.startActivity(AtyLogin.class);
        }
    }

    @OnClick(R.id.wait_receiv_goods)
    public void onReceivOrderClick(View v) {
        if (AffordApp.isLogin()) {
            Intent inforIntent = new Intent(AtyMy.this, AtyFragmentOrdert.class);
            // 放入当前点击的位置
            inforIntent.putExtra("tabCode", 3);
            startActivity(inforIntent);
        } else {
            mBaseUtilAty.startActivity(AtyLogin.class);
        }
    }

    //全部订单
    @OnClick(R.id.orderLyt)
    public void onOrdeClick(View v) {
        if (AffordApp.isLogin()) {
            Intent inforIntent = new Intent(AtyMy.this, AtyFragmentOrdert.class);
            // 放入当前点击的位置
            inforIntent.putExtra("tabCode", 0);
            startActivity(inforIntent);
        } else {
            mBaseUtilAty.startActivity(AtyLogin.class);
        }
    }

    //服务订单
    @OnClick(R.id.my_orderLyt)
    public void onServerOrdeClick(View v) {
        if (AffordApp.isLogin()) {
            Intent intent = new Intent(AtyMy.this, AtyFragmentService.class);
            startActivity(intent);
        } else {
            mBaseUtilAty.startActivity(AtyLogin.class);
        }
    }


    //设置按钮
    @OnClick({R.id.top_right_btnset, R.id.top_login_btnset})
    public void onSetClick(View v) {
        Intent intent = new Intent(AtyMy.this, AtySet.class);
        startActivity(intent);
    }

    //头像
    @OnClick(R.id.my_head_login)
    public void onHeaderImageClick(View v) {
        mBaseUtilAty.startActivity(AtyMyAccount.class);
    }

    //用户名
    @OnClick(R.id.my_login_username)
    public void onLoginUserNameClick(View v) {
        mBaseUtilAty.startActivity(AtyMyAccount.class);
    }

    //用户地址
    @OnClick(R.id.my_address)
    public void onMyAddressClick(View v) {
        mBaseUtilAty.startActivity(AtyMyAddress.class);
    }

    //点击个人中心按钮事件
    @OnClick(R.id.my_head_layout)
    public void onOrderlick(View v) {
        Intent intent = new Intent(AtyMy.this, AtyLogin.class);
        intent.putExtra(CommConstans.Login.INTENT_KEY, CommConstans.Login.INTENT_VALUE_LOGIN);
        startActivity(intent);
    }

//    //登录状态账户管理、地址管理
//    @OnClick(R.id.rlyt_top_account)
//    public void onTopAccAddrClick(View v) {
//        mBaseUtilAty.startActivity(AtyMyAccount.class);
//    }

    @OnClick(R.id.lyt_storedetail)
    public void onLytMyStore(View v) {
        mBaseUtilAty.startActivity(AtyMyStore.class);
    }

    //反馈与建议
    @OnClick(R.id.lyt_feedback)
    public void onLytFeedbackClick(View v) {
        mBaseUtilAty.startActivity(AtyMyFeedSug.class);
    }

    @OnClick(R.id.server_wait_pay)//服务订单待付款
    public void onServerNoPayClick(View v) {
        if (AffordApp.isLogin()) {
            Intent inforIntent = new Intent(AtyMy.this, AtyFragmentService.class);
            // 放入当前点击的位置
            inforIntent.putExtra("tabCode", 0);
            startActivity(inforIntent);
        } else {
            mBaseUtilAty.startActivity(AtyLogin.class);
        }
    }

    @OnClick(R.id.server_wait_recevie)//服务订单待服务
    public void onServerSernoClick(View v) {
        if (AffordApp.isLogin()) {
            Intent inforIntent = new Intent(AtyMy.this, AtyFragmentService.class);
            // 放入当前点击的位置
            inforIntent.putExtra("tabCode", 1);
            startActivity(inforIntent);
        } else {
            mBaseUtilAty.startActivity(AtyLogin.class);
        }
    }

    @OnClick(R.id.server_wait_evaluate)//服务订单待评价
    public void onServerAssessClick(View v) {
        if (AffordApp.isLogin()) {
            Intent inforIntent = new Intent(AtyMy.this, AtyFragmentService.class);
            // 放入当前点击的位置
            inforIntent.putExtra("tabCode", 2);
            startActivity(inforIntent);
        } else {
            mBaseUtilAty.startActivity(AtyLogin.class);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewUtils.inject(this);
    }

    /**
     * 退出登录后清空所有订单状态数字并隐藏
     */
    public void noVisibilityNum() {
        mTvOrderLeftNum.setText("0");
        mTvOrderMidNum.setText("0");
        mTvOrderRightNum.setText("0");
        mTvSerLeftNum.setText("0");
        mTvSerMidNum.setText("0");
        mTvSerRigNum.setText("0");
        mTvOrderLeftNum.setVisibility(View.GONE);
        mTvOrderMidNum.setVisibility(View.GONE);
        mTvOrderRightNum.setVisibility(View.GONE);
        mTvSerLeftNum.setVisibility(View.GONE);
        mTvSerMidNum.setVisibility(View.GONE);
        mTvSerRigNum.setVisibility(View.GONE);
    }

    @Override
    public int getButtonType() {
        return 4;
    }
}
