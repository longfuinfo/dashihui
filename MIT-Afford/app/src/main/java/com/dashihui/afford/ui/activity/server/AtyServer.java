package com.dashihui.afford.ui.activity.server;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessServer;
import com.dashihui.afford.business.entity.EtyList;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseMenuActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.ui.adapter.AdapterServer;
import com.dashihui.afford.ui.adapter.AdapterServerListView;
import com.dashihui.afford.ui.widget.WgtGridViewServer;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.preferences.UtilPreferences;
import com.dashihui.afford.util.string.UtilString;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AtyServer extends BaseMenuActivity {
//    @ViewInject(R.id.noserver_lay)
//    private LinearLayout noserver;

    @ViewInject(R.id.server_listview)
    private PullToRefreshListView mListView;
    @ViewInject(R.id.txtEempty)
    private TextView mTxtEempty;
    private AdapterServerListView mAdapterServerListView;

    private WgtGridViewServer mGridView;
    private View mServer_header;
   

    private int mPageNum = 1; //分页码
    private int mTotalPage = 0;//总页数
    private List<Map<String, Object>> mShopsListMap;

    private AdapterServer mAdapterServer;
    private List<Map<String, Object>> mListMap;
    private BusinessServer mBllServer;
    //是否刷新数据标识
    private final static String SERVER_REFRESH = "ServerRefresh";

    @Override
    public int getContentViewLayoutResId() {
        return R.layout.aty_server;
    }

    @Override
    protected void onCreatOverride(Bundle savedInstanceState) {
        ViewUtils.inject(this);//依赖注入

        initListViewHeader();
        mBllServer = new BusinessServer(this);
        mBllServer.getCategory();//获取服务分类
        showProDialog(this);
        mBllServer.getServersShops("", mPageNum + "");
        //************************切换小区时更新页面******************************
        String refresh = UtilPreferences.getString(this, SERVER_REFRESH)+"";
        //第一次进入为空时填写
        if (UtilString.isEmpty(refresh) && AffordApp.getInstance().getEntityLocation() != null){
            String storeTitle1 = AffordApp.getInstance().getEntityLocation().getSTORE().getTITLE();
            UtilPreferences.putString(this, SERVER_REFRESH, storeTitle1);
        }

    }
    @Override
    protected void onResume() {
        if (AffordApp.getInstance().getEntityLocation()!=null){
            String refresh = UtilPreferences.getString(this, SERVER_REFRESH)+"";
            String storeTitle = AffordApp.getInstance().getEntityLocation().getSTORE().getTITLE();
            if (!storeTitle.equals(refresh)){
                mBllServer.getCategory();//获取服务分类
                showProDialog(this);
                mBllServer.getServersShops("", mPageNum + "");
                //更改本地存储的社区名称
                UtilPreferences.putString(this, SERVER_REFRESH, storeTitle);
            }else {
                LogUtils.e("AtyServer======00=3333===同一个便利店====>" + refresh);
            }
        }else {
            LogUtils.e("onResume========AffordApp.getInstance()======>");
        }
        super.onResume();


    }

    @Override
    public void onSuccess(EtySendToUI info) {

        if (info != null && info.getInfo() != null) {
            switch (info.getTag()) {
                case AffConstans.BUSINESS.TAG_SERVICE_CATEGORY://服务分类
                    mListMap = (List<Map<String, Object>>) info.getInfo();

                    //服务分类的Adapter
                    mAdapterServer = new AdapterServer(this, mListMap);


                    mGridView.setAdapter(mAdapterServer);
                    mGridView.setOnItemClickListener(onViewGroupItemClick);
                   // mViewGroup.addView(mGridView);
                    LogUtils.e("onSuccess======mListMap===服务分类====>" + mListMap);
                    break;
                case AffConstans.BUSINESS.TAG_SERVICE_SHOPS://服务商家列表获取
                    EtyList etyList = (EtyList) info.getInfo();
                    dissProDialog();
                    if(mPageNum ==1){
                       // LogUtils.e("onSuccess========********==========>" + "1111111");
                        mShopsListMap.clear();
                    }
                    if (etyList != null && !UtilList.isEmpty(etyList.getLIST())) {
//                        mIsPage = false;//恢复获取分页事件
                       // LogUtils.e("onSuccess=========*********==========>" + "2222222");
                        mTxtEempty.setVisibility(View.GONE);
                        mTotalPage = etyList.getTOTALPAGE();
                        mShopsListMap.addAll(etyList.getLIST());
                       // mListView.setVisibility(View.VISIBLE);
                    }else{
                        LogUtils.e("onSuccess=========*********===getChildCount=======>" + mListView.getChildCount());
                        mTxtEempty.setVisibility(View.VISIBLE);
                       //mListView.setVisibility(View.GONE);

                    }
                    mAdapterServerListView.setList(mShopsListMap);
                    mAdapterServerListView.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        } else {
            LogUtils.e("onSuccess====AtyServer====else=======>" + info.getInfo());
        }
    }

    @Override
    public void onFailure(EtySendToUI error) {
        if (error != null ) {
            switch (error.getTag()) {
                case AffConstans.BUSINESS.TAG_SERVICE_CATEGORY://服务分类
                    break;
                case AffConstans.BUSINESS.TAG_SERVICE_SHOPS://服务商家列表获取
                    dissProDialog();
                    break;
                default:
                    break;
            }
        } else {
            LogUtils.e("onSuccess====AtyServer====else=======>" + error.getInfo());
        }
        LogUtils.e("onFailure====AtyServer===========>" + error);
    }

    /**
     * PullToRefreshListView添加头部
     */
    public void initListViewHeader() {
        //初始化头部文件
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        mServer_header = LayoutInflater.from(this).inflate(R.layout.aty_server_header, null);
        mGridView = (WgtGridViewServer) mServer_header.findViewById(R.id.gridview);

        //将头部文件添加至PullToRefreshListView
        mServer_header.setLayoutParams(layoutParams);
        mShopsListMap = new ArrayList<>();
        mListView.getRefreshableView().addHeaderView(mServer_header);
        mAdapterServerListView = new AdapterServerListView(this, mShopsListMap);
        mListView.setAdapter(mAdapterServerListView);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mListView.setOnRefreshListener(onRefreshLoadMorePage);
    }

    private PullToRefreshBase.OnRefreshListener2 onRefreshLoadMorePage = new PullToRefreshBase.OnRefreshListener2() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            // 隐藏加载更多圈 底部
            mListView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mListView.onRefreshComplete();
                }
            }, 1000);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            //加载更多
            if (mTotalPage > 0 &&mTotalPage > mPageNum) {
                //获取商家列表
                mPageNum++;
                showProDialog(AtyServer.this);
                mBllServer.getServersShops("", mPageNum + "");//请求服务商家列表
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
    };

    /**
     * GridView 的item点击事件
     *
     * @param v
     */
    private AdapterView.OnItemClickListener onViewGroupItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            LogUtils.e("onGridViewItemClick======服务代码=======>" + mListMap.get(position).get(CommConstans.SERVER.SERVER_CODE));
            if (!UtilList.isEmpty(mListMap) && "01".equals(mListMap.get(position).get(CommConstans.SERVER.SERVER_CODE)+"")) {//家政代码
                startActivity(new Intent(AtyServer.this, AtyServerHouse.class));
            }else {
                Intent intent = new Intent(AtyServer.this, AtyServerList.class);
                intent.putExtra(CommConstans.SERVER.SERVER_NAME, mListMap.get(position).get(CommConstans.SERVER.SERVER_NAME) + "");
                intent.putExtra(CommConstans.SERVER.INTENT_TAG_TYPE, mListMap.get(position).get(CommConstans.SERVER.SERVER_CODE) + "");
                intent.putExtra(CommConstans.SERVER.SERVER_CODE, mListMap.get(position).get(CommConstans.SERVER.SERVER_CODE) + "");
                startActivity(intent);
            }
        }
    };

    /**
     * 返回
     *
     * @param v
     */
    @OnClick(R.id.left_ibtnBack)
    public void onBackLeftClick(View v) {
        onBackPressed();
    }


    @Override
    public int getButtonType() {
        return 2;
    }
}
