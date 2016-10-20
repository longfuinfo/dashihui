package com.dashihui.afford.ui.activity.server;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessServer;
import com.dashihui.afford.business.entity.EtyList;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.ui.adapter.AdapterServerList;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.number.UtilNumber;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AtyServerList extends BaseActivity {
    @ViewInject(R.id.listView_home)
    private PullToRefreshListView mListView;

    @ViewInject(R.id.top_title)
    private TextView mTitle;
    @ViewInject(R.id.lyt_empty)
    private LinearLayout mLytServerEmpty;//没有数据时的布局

    private AdapterServerList mAdapterServerList;
    private List<Map<String, Object>> mListMap;
    private EtyList mEtyListObject;
    //进入详情页标示
    public final static String INTENT_SERVER_ID = "serverID";
    public final static String INTENT_SERVER_THUMB = "serverthumb";

    private boolean mIsPage = false;//是否分页
    private static int index = -1;//列表分类
    private static int mTotalPage = 1;//总页数
    private int mPageNums = 1;//商品请求的页面
    private BusinessServer mBllServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_server_list);
        ViewUtils.inject(this);//依赖注入
        mBllServer = new BusinessServer(this);

        mListMap = new ArrayList<Map<String, Object>>();
        mAdapterServerList = new AdapterServerList(this, mListMap);
        mListView.setAdapter(mAdapterServerList);

        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //加载更多
                Intent intent = getIntent();
                if (mTotalPage > 0 && mTotalPage > mPageNums) {
                    //获取商品列表，第二个参数：优惠类型	1：普通，2：推荐，3：限量，4：一元购
                    mPageNums++;
                    showProDialog(AtyServerList.this);
                    mBllServer.getServersShops(intent.getStringExtra(CommConstans.SERVER.SERVER_CODE), mTotalPage + "");//请求服务商家列表
                } else {
                    // 隐藏加载更多圈 底部
                    mListView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mListView.onRefreshComplete();
                        }
                    }, 1000);
                }
            }
        });
    }

    //顶部返回按钮
    @OnClick(R.id.left_back)
    public void onIBtnBackClick(View v) {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        mListMap.clear();
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra(CommConstans.SERVER.INTENT_TAG_TYPE) != null) {
            index = UtilNumber.IntegerValueOf(intent.getStringExtra(CommConstans.SERVER.INTENT_TAG_TYPE));
            mTitle.setText(intent.getStringExtra(CommConstans.SERVER.SERVER_NAME));
            showProDialog(AtyServerList.this);
            mBllServer.getServersShops(intent.getStringExtra(CommConstans.SERVER.SERVER_CODE) + "", mPageNums + "");//请求服务商家列表
            LogUtils.e("onResume======SERVER_CODE==2======>" + intent.getStringExtra(CommConstans.SERVER.SERVER_CODE));
        } else {
            //默认情况下，选择家政
            if (index > 0) {
                mTitle.setText(intent.getStringExtra(CommConstans.SERVER.SERVER_NAME));
            } else {
                mTitle.setText("家政");
            }
        }
        super.onResume();
    }

    @Override
    public void onSuccess(EtySendToUI info) {
        LogUtils.e("onSuccess===============>" + info);
        if (info!=null){
            switch (info.getTag()) {
                case AffConstans.BUSINESS.TAG_SERVICE_SHOPS://服务商家列表获取
                    dissProDialog();
                    mEtyListObject = (EtyList) info.getInfo();
                    if (mEtyListObject != null && !UtilList.isEmpty(mEtyListObject.getLIST())) {
                        mLytServerEmpty.setVisibility(View.GONE);
                        mIsPage = false;//恢复获取分页事件
                        mTotalPage = mEtyListObject.getTOTALPAGE();
                        mListMap.addAll(mEtyListObject.getLIST());
                        mAdapterServerList.setList(mListMap);
                        mAdapterServerList.notifyDataSetChanged();
                        LogUtils.e("onSuccess=========mListMap==========>" + mListMap);
                    } else {
                        mLytServerEmpty.setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    break;
            }
        }else {
            LogUtils.e("onSuccess=========info===null=======>" + info);
        }

    }

    @Override
    public void onFailure(EtySendToUI error) {
        if (error!=null){
            switch (error.getTag()) {
                case AffConstans.BUSINESS.TAG_SERVICE_SHOPS://服务商家列表获取
                    dissProDialog();
                    break;
                default:
                    break;
            }
        }else {
            LogUtils.e("onFailure====AtyAffordShop===========>" + error);
        }


    }

    @OnClick(R.id.btn_toOther)
    public void onBtnToOtherClick(View v){
        mBaseUtilAty.startActivity(AtyServer.class);
    }
}
