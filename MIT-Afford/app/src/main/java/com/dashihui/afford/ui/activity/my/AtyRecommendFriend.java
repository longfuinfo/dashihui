package com.dashihui.afford.ui.activity.my;

import android.app.Fragment;
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
import com.dashihui.afford.ui.adapter.AdapterFriend;
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
public class AtyRecommendFriend extends BaseActivity{

    @ViewInject(R.id.lv_friend)
    private ListView mFriend;
    @ViewInject(R.id.friend_num)
    private TextView mFriendNum;
    @ViewInject(R.id.tv_how_recommend)
    private TextView mHowRecommend;

    @ViewInject(R.id.noOrder)
    private LinearLayout mLytOrder;

    private AdapterFriend mAdapter;
    private List<Map<String, Object>> mMapList;
    private BusinessUser mBllUser;
    private int pageNum = 1;//当前页码




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_recommend_friend);
        ViewUtils.inject(this);
        mMapList = new ArrayList<>();
        mBllUser = new BusinessUser(this);
        mBllUser.getExpenseRecord("4",pageNum + "");

         inDate();
    }
    public void inDate(){
        mHowRecommend.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        mAdapter = new AdapterFriend(getParent(),mMapList);
        mFriendNum.setText(AffordApp.getInstance().getUserFriend()+"个");
    }
    @Override
    public void onSuccess(EtySendToUI successEty) {
        if (successEty != null) {
            switch (successEty.getTag()) {
                case AffConstans.BUSINESS.TAG_USER_EXPENSDRECORD://实惠币变动列表
                    EtyList list = (EtyList) successEty.getInfo();
//                    if (pageNum == 1) {
//                        mMapList.clear();
//                    }
//                    if (listObjects != null && !UtilList.isEmpty(listObjects.getLIST())) {
                    LogUtils.e("onSuccess=========mMapList=list ===>" + list.getLIST());
                    mMapList.addAll(list.getLIST());
                    //加载adapter
                    LogUtils.e("onSuccess=========shihuibi=实惠币数据==>" + mMapList);
                    mAdapter.setList(mMapList);
                    mFriend.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    // }
                    //没有记录时显示的页面
                    if (mMapList.size() > 0) {
                        mLytOrder.setVisibility(View.GONE);
                        mFriend.setVisibility(View.VISIBLE);
                    } else {
                        mLytOrder.setVisibility(View.VISIBLE);
                        mFriend.setVisibility(View.GONE);
                    }
                    break;
            }
            mFriend.setAdapter(mAdapter);
        }
    }
    @Override
    public void onFailure(EtySendToUI failureEty) {

    }
    @OnClick(R.id.friend_share)
    public void onRemember(View v){
        if (AffordApp.isLogin()){
            if (AffordApp.getInstance().getUserLogin().getUSER().getLEVEL() == 2){
                mBaseUtilAty.startActivity(AtyMyMemberShare.class);
            }else{
                mBaseUtilAty.startActivity(AtyMyMember.class);
            }
        }

    }

    @OnClick(R.id.left_back_collect)
    public void onBack(View v){
        onBackPressed();
    }

    @OnClick(R.id.tv_how_recommend)//如何推荐好友
    public void onRecommend(View v){

    }
}
