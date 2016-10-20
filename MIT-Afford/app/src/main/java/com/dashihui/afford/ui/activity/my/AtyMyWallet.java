package com.dashihui.afford.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessUser;
import com.dashihui.afford.business.entity.EtyMoney;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseAtyFragment;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.ui.activity.fragment.FragmentWallet;
import com.dashihui.afford.ui.widget.WdtPagerTab;
import com.dashihui.afford.util.preferences.UtilPreferences;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.Map;

/**
 * Created by Administrator on 2016/5/4.
 */
public class AtyMyWallet extends BaseAtyFragment {

    private WdtPagerTab tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    @ViewInject(R.id.wallet_num)
    private TextView mWallerNum;
    @ViewInject(R.id.friend_num)
    private TextView mFriendNum;


    private Map<String, Object> mTotalMap;
    private BusinessUser mBllUser;

    private EtyMoney userMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_my_wallet);
        ViewUtils.inject(this);

        tabs = (WdtPagerTab) findViewById(R.id.apps_fragment_tabs);
        pager = (ViewPager) findViewById(R.id.apps_fragment_viewpager);

        adapter = new MyPagerAdapter(getSupportFragmentManager());

        // 设置缓存页面，当前页面的相邻N各页面都会被缓存
        pager.setOffscreenPageLimit(adapter.getCount());
        pager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        tabs.setViewPager(pager);
        //tabs.setPosition(intent.getIntExtra(TABCODE, 0));
        tabs.setPosition(0);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                FragmentWallet fragmentOrder = FragmentWallet.newInstance(AtyMyWallet.this, i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mBllUser = new BusinessUser(this);
        mBllUser.getMoney();
        //mBllUser.getExpenseRecord("1");
        userMoney = new EtyMoney();


    }

    @Override
    public void onSuccess(EtySendToUI successEty) {
        if (successEty != null) {
            switch (successEty.getTag()) {
                case AffConstans.BUSINESS.TAG_USER_MONEY://实惠币
                   // LogUtils.e("onSuccess=====钱包页面=========>" + mTotalMap.get("MONEY")+"  "+mTotalMap.get("INVITECOUNT"));
                    mTotalMap = (Map<String, Object>) successEty.getInfo();
                    if (Double.valueOf(mTotalMap.get("MONEY") + "") == 0.0){
                     mWallerNum.setText("0.00");
                    }else {
                        mWallerNum.setText(mTotalMap.get("MONEY") + "");
                    }
                    mFriendNum.setText(mTotalMap.get("INVITECOUNT") + "个");
                    AffordApp.getInstance().setUserMoney(mTotalMap.get("MONEY") + "");
                    AffordApp.getInstance().setUserFriend(Integer.valueOf(mTotalMap.get("INVITECOUNT") + ""));

                    UtilPreferences.putString(this, CommConstans.Login.FRIEND, userMoney.getINVITECOUNT() + "");
                    UtilPreferences.putString(this, CommConstans.Login.MONEY, userMoney.getMONEY() + "");
                    LogUtils.e("onSuccess=====钱包页面=========>" + mTotalMap.get("MONEY") + "  " + mTotalMap.get("INVITECOUNT"));
                    LogUtils.e("onSuccess=====钱包页面=========>" + AffordApp.getInstance().getUserFriend());
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

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CommConstans.COMMONARRAY.WALLET_ARRAY[position];
        }

        @Override
        public int getCount() {
            return CommConstans.COMMONARRAY.WALLET_ARRAY.length;
        }

        @Override
        public Fragment getItem(int position) {
            FragmentWallet fragmentOrder = FragmentWallet.newInstance(AtyMyWallet.this, position);
            return fragmentOrder;
        }

    }


    @OnClick(R.id.what_shihuibi)
    public void onWhatShihuibi(View v) {
        Intent intent = new Intent(this,AtyWhatShihuibi.class);
        startActivity(intent);

    }
    @OnClick(R.id.left_back_collect)
    public void onBack(View v){
        onBackPressed();
    }
    @OnClick(R.id.ly_my_shihuibi)
    public void onShihuibi(View v){
        Intent intent = new Intent(this,AtyMyShihuibi.class);
        startActivity(intent);

    }
    @OnClick(R.id.ly_my_friend)
    public void onFriend(View v){
        Intent intent = new Intent(this,AtyRecommendFriend.class);
        startActivity(intent);

    }
}
