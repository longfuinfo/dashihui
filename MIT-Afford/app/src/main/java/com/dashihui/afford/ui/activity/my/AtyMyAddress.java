package com.dashihui.afford.ui.activity.my;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessUser;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.thirdapi.swipemenulistview.SwipeMenu;
import com.dashihui.afford.thirdapi.swipemenulistview.SwipeMenuCreator;
import com.dashihui.afford.thirdapi.swipemenulistview.SwipeMenuItem;
import com.dashihui.afford.thirdapi.swipemenulistview.SwipeMenuListView;
import com.dashihui.afford.ui.adapter.AdapterMyAddress;
import com.dashihui.afford.util.toast.UtilToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;
import java.util.Map;

public class AtyMyAddress extends BaseActivity {
    private AdapterMyAddress mAdapterMyAddress;
    private BusinessUser mBllUser;
    private List<Map<String, Object>> mListMaps;

    @ViewInject(R.id.listView_addr)
    private SwipeMenuListView mListViewAddr;
    @ViewInject(R.id.lyt_noAddress)
    private LinearLayout mLytNoAddress;

    private TextView mTvIsDefault;
    private boolean isChoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_my_address);
        ViewUtils.inject(this);
        mLytNoAddress.setVisibility(View.GONE);
        itemDelete();//滑动删除
        mBllUser = new BusinessUser(this);
        mListViewAddr.setOnItemClickListener(onItemClickListener);
    }

    //listview的item监听，获取用户地址信息传递给地址修改页面
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String addressID = mListMaps.get(position).get("ID") + "";
            LogUtils.e("onItemClick========ID=======>" + addressID);
            String userName = mListMaps.get(position).get("LINKNAME") + "";
            String sex = mListMaps.get(position).get("SEX") + "";
            String phone = mListMaps.get(position).get("TEL") + "";
            String userAddress = mListMaps.get(position).get("ADDRESS") + "";
            //点击item后设置为默认地址
            mTvIsDefault = (TextView) view.findViewById(R.id.text_default);
            String isDefault = "1";//赋值为默认地址
            mBllUser.updateaddress(addressID, userName, sex, phone, userAddress, isDefault);

        }
    };

    @Override
    protected void onResume() {
        LogUtils.e("AtyMyAddress=======onResume========>");
        mBllUser.addresslist();
        super.onResume();
    }

    @OnClick(R.id.topleft_back)//返回
    public void onLeftbackClick(View v) {
        onBackPressed();
    }


    @OnClick(R.id.bottom_btn_addr)//新增收货地址
    public void onBtnAddAddressClick(View v) {
        mBaseUtilAty.startActivity(AtyMyAddressNew.class);
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {

        LogUtils.e("AtyMyAddress========onSuccess=======beanSendUI=======>" + beanSendUI);
        LogUtils.e("AtyMyAddress========onSuccess=======beanSendUI.getInfo()=======>" + beanSendUI.getInfo());
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_USER_ADDRESSLIST://收货地址列表
                    mListMaps = (List<Map<String, Object>>) beanSendUI.getInfo();
                    LogUtils.e("onSuccess=========mListMap==========>" + mListMaps);
                    mAdapterMyAddress = new AdapterMyAddress(this, mListMaps);
                    mListViewAddr.setAdapter(mAdapterMyAddress);
                    //地址列表为空时显示的界面
                    LogUtils.e("onSuccess===mListMaps.size()===========>" + mListMaps.size());
                    if (mListMaps.size() <= 0) {
                        mLytNoAddress.setVisibility(View.VISIBLE);
                    }else {
                        mLytNoAddress.setVisibility(View.GONE);
                    }
                    break;
                case AffConstans.BUSINESS.TAG_USER_UPDATEADDRESS://收货地址修改,设置当前item地址为默认地址
                    LogUtils.e("====修改地址成功后结束Activity=====>");
                    mTvIsDefault.setVisibility(View.VISIBLE);//设为默认地址
                    UtilToast.show(this, "默认地址设置成功", Toast.LENGTH_SHORT);
                    finish();
                    break;
                case AffConstans.BUSINESS.TAG_USER_DELADDRESS://收货地址删除
                    LogUtils.e("====当前地址删除成功后结束Activity=====>");
                    //地址列表为空时显示的界面
                    LogUtils.e("onSuccess===mListMaps.size()===========>" + mListMaps.size());
                    if (mListMaps.size() <= 0) {
                        mLytNoAddress.setVisibility(View.VISIBLE);
                    }
                    UtilToast.show(this, "地址删除成功", Toast.LENGTH_SHORT);
                    break;
                default:
                    LogUtils.e("onSuccess===default===========>" + beanSendUI);
                    break;
            }
        }
    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        LogUtils.e("onFailure=========beanSendUI===========>" + beanSendUI);
    }

    //滑动删除
    public void itemDelete() {
        // 创建一个 MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //创建删除按钮
                SwipeMenuItem deleteItem = new SwipeMenuItem(AtyMyAddress.this);
                //设置背景颜色
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                //设置宽度
                deleteItem.setWidth(dp2px(120));
                //设置背景图片
                deleteItem.setIcon(R.drawable.ic_delete);
                //所有设置添加到按钮
                menu.addMenuItem(deleteItem);
            }
        };

        mListViewAddr.setMenuCreator(creator);
        mListViewAddr.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                LogUtils.e("onMenuItemClick=========position==========>" + position + "");
                //移除当前点击的item

                //提交服务器删除地址
                LogUtils.e("onMenuItemClick=========AddressID========>" + mListMaps.get(position).get("ID") + "");
                mBllUser.deladdress(mListMaps.get(position).get("ID") + "");

                //从列表删除当前点击的item
                mListMaps.remove(position);

                //更新购物车列表，刷新数据
                mAdapterMyAddress.setList(mListMaps);
                mAdapterMyAddress.notifyDataSetChanged();
                LogUtils.e("onMenuItemClick===========mListMaps======222======>" + mListMaps.size());
            }
        });
        // set SwipeListener
        mListViewAddr.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });
    }

    //设置删除按钮的宽度
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
