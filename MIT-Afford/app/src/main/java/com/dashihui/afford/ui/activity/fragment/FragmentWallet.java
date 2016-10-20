package com.dashihui.afford.ui.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dashihui.afford.R;

import com.dashihui.afford.business.BusinessUser;

import com.dashihui.afford.business.entity.EtyList;
import com.dashihui.afford.business.entity.EtyMoney;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseFragment;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.ui.adapter.AdapterWallet;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/5.
 */
public class FragmentWallet extends BaseFragment{

    private List<Map<String, Object>> mMapList;
    private Map<String,Object> mMap;
    private static Context mContext;
    private int position;
    private static final String WALLET_POSITION = "position";

    private BusinessUser mBllUser;
    private int pageNum = 1;//当前页码
    private int totalPage = 0;//总页数

    //列表适配器
    private AdapterWallet mAdapter;

    @ViewInject(R.id.fragment_listview)
    private PullToRefreshListView mListView;
    @ViewInject(R.id.noOrder)
    private LinearLayout mLytOrder;

    public static FragmentWallet newInstance(Context context, int position) {
        FragmentWallet f = new FragmentWallet();
        Bundle b = new Bundle();
        b.putInt(WALLET_POSITION, position);
        mContext = context;
        f.setArguments(b);
        LogUtils.e("=========mMapList=newInstance===>" +position);
        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(WALLET_POSITION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_wallet_listview, container, false);
        ViewUtils.inject(this, rootView); //注入view和事件
        mMapList = new ArrayList<>();
        mAdapter = new AdapterWallet(getActivity(), mMapList,this);
        mListView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBllUser = new BusinessUser(this);
        mBllUser.getExpenseRecord((position + 1)  + "", pageNum + "");
        LogUtils.e("=========mMapList=onResume===>" + (position + 1));
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){
            LogUtils.e("FragmentOrder======**************========>" + getUserVisibleHint() + (position + 1)+pageNum);
            mBllUser = new BusinessUser(this);
            mBllUser.getExpenseRecord((position + 1) + "", pageNum + "");
        }
    }

    @Override
    public void onSuccess(EtySendToUI successEty) {
        LogUtils.e("onSuccess=========mMapList=successEty===>" + successEty);
        LogUtils.e("onSuccess=========mMapList=getTag ===>" + successEty.getTag());
        if (successEty != null) {
            switch (successEty.getTag()) {
                case AffConstans.BUSINESS.TAG_USER_EXPENSDRECORD://实惠币变动列表
                    EtyList list = (EtyList) successEty.getInfo();
                    if (pageNum==1) {
                        mMapList.clear();
                    }
                        LogUtils.e("onSuccess=========mMapList=list ===>" + list.getLIST());
                        //mMap = list.getDATA();
                        mMapList.addAll(list.getLIST());
                        //加载adapter
                        LogUtils.e("onSuccess=========shihuibi=实惠币数据==>" + mMapList.toString());
                        mAdapter.setList(mMapList);
                        mAdapter.notifyDataSetChanged();
                   // }
                    //没有记录时显示的页面
                        if (mMapList.size() > 0) {
                            mLytOrder.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                        } else {
                            mLytOrder.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.GONE);
                        }
                    break;
            }
        }

    }

    @Override
    public void onFailure(EtySendToUI failureEty) {
        LogUtils.e("onFailure=========mMapList=失败===>" + mMapList);

    }
}
