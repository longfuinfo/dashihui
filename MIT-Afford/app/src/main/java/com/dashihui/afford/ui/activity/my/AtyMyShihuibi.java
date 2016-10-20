package com.dashihui.afford.ui.activity.my;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessUser;
import com.dashihui.afford.business.entity.EtyList;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.ui.activity.fragment.FragmentWallet;
import com.dashihui.afford.ui.adapter.AdapterShihuibi;
import com.dashihui.afford.ui.adapter.AdapterWallet;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/4.
 */
public class AtyMyShihuibi extends BaseActivity{

    @ViewInject(R.id.lv_expense)
    private ListView mExpense;

    @ViewInject(R.id.tv_expense_rule)
    private TextView mExpenseRule;

    @ViewInject(R.id.wallet_num)
    private TextView mWalletNum;

    @ViewInject(R.id.noOrder)
    private LinearLayout mLytOrder;


    private Map<String, Object> mTotalMap;
    private AdapterShihuibi aShihuibi;
    private BusinessUser mBllUser;
    private List<Map<String, Object>> mMapList;
    private int pageNum = 1;//当前页码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_my_shihuibi);
        ViewUtils.inject(this);
        mMapList = new ArrayList<>();
        mBllUser = new BusinessUser(this);
        //mBllUser.getMoney();
        mBllUser.getExpenseRecord("3", pageNum + "");
        mExpenseRule.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

        //mWalletNum.setText(MONEY);
        if (Double.valueOf(AffordApp.getInstance().getUserMoney()+ "") == 0.0){
            mWalletNum.setText("0.00");
        }else {
            mWalletNum.setText(AffordApp.getInstance().getUserMoney()+ "");
        }

        LogUtils.e("onSuccess===实惠币=====AtyMyShihuibi======>" + AffordApp.getInstance().getUserMoney() + "");


    }

//    public void inDate(){
//        if (mTotalMap != null) {
//            mExpense.setAdapter(aShihuibi);
//        } else {
//            mBelow.setVisibility(View.VISIBLE);
//            mAbove.setVisibility(View.GONE);
//            mHowRemember.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
//        }

//}

    @Override
    public void onSuccess(EtySendToUI successEty) {
        if (successEty != null) {
            switch (successEty.getTag()) {
                case AffConstans.BUSINESS.TAG_USER_EXPENSDRECORD://实惠币变动列表

                    EtyList list = (EtyList) successEty.getInfo();
                    if (pageNum==1) {
                        mMapList.clear();
                    }
                    mMapList = list.getLIST();
                    LogUtils.e("onSuccess===myshihuibi===========>" + mMapList.toString());
//                    if (pageNum==1) {
//                        mMapList.clear();
//                    }
                    aShihuibi = new AdapterShihuibi(this,mMapList);
                    aShihuibi.setList(mMapList);
                    mExpense.setAdapter(aShihuibi);
                    aShihuibi.notifyDataSetChanged();

                    //没有记录时显示的页面
                    if (mMapList.size() > 0) {

                        mLytOrder.setVisibility(View.GONE);
                        mExpense.setVisibility(View.VISIBLE);
                    } else {
                        mLytOrder.setVisibility(View.VISIBLE);
                        mExpense.setVisibility(View.GONE);
                    }
                    break;
                default:
                    LogUtils.e("onSuccess===default===========>" + "");
                    break;

            }
        }

    }

    @Override
    public void onFailure(EtySendToUI failureEty) {

    }

    @OnClick(R.id.left_back_collect)
    public void onBack(View v){
        onBackPressed();
    }
    @OnClick(R.id.tv_expense_rule)
    public void onExpense(View v){

    }
}
