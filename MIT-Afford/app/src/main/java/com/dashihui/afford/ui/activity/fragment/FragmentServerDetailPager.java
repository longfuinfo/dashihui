package com.dashihui.afford.ui.activity.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessServer;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.business.entity.EtyServerDetail;
import com.dashihui.afford.common.base.BaseFragment;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.thirdapi.autochangeLine.CustomAdapter;
import com.dashihui.afford.thirdapi.autochangeLine.CustomListView;
import com.dashihui.afford.thirdapi.autochangeLine.MainSexangleAdapter;
import com.dashihui.afford.thirdapi.autochangeLine.OnItemClickListener;
import com.dashihui.afford.thirdapi.autochangeLine.OnItemLongClickListener;
import com.dashihui.afford.ui.activity.server.AtyServerDetail;
import com.dashihui.afford.ui.activity.server.AtyServerList;
import com.dashihui.afford.ui.adapter.AdapterAffordShopDetail;
import com.dashihui.afford.ui.widget.WgtAlertDialog;
import com.dashihui.afford.ui.widget.WgtGridViewServer;
import com.dashihui.afford.ui.widget.WgtScrollView;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.map.UtilMap;
import com.dashihui.afford.util.screen.UtilScreen;
import com.dashihui.afford.util.string.UtilString;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.Map;

public class FragmentServerDetailPager extends BaseFragment {
    private static final String ARG_POSITION = "position";
    private static AtyServerDetail mContext;

    @ViewInject(R.id.detail_name)
    private TextView mTvName;//详情页商品名
    @ViewInject(R.id.text_sale)
    private TextView mTvSale;//月服务次数
    @ViewInject(R.id.text_price)
    private TextView mTvPrice;//商品优惠后价格
    @ViewInject(R.id.text_oldprice)
    private TextView mTvOldPrice;//商品原价
    @ViewInject(R.id.text_present)
    private TextView mTvPricePresent;//价格说明

    @ViewInject(R.id.lyt_collect)
    private LinearLayout mLytCollected;//关注
    @ViewInject(R.id.tv_collect)
    private TextView mTxtCollect;//关注文字
    @ViewInject(R.id.tv_unread_count)
    private TextView mTvserverNum;//购物车提示数字
    @ViewInject(R.id.join_server)
    private TextView mTvJionserver;//立即预约
    @ViewInject(R.id.bot_tvnum)
    private TextView mCurNum;
    @ViewInject(R.id.bot_num)
    private TextView mTotalNum;
    @ViewInject(R.id.custScrollView)
    private WgtScrollView mScrollView;



    @ViewInject(R.id.viewpager)
    private ViewPager mViewPager;//顶部轮播图
    @ViewInject(R.id.viewGroup)
    private View mViewGroup;
    @ViewInject(R.id.serverCustomListView)
    private CustomListView mCustomListView;
   // private WgtViewGroupServer viewGroup;
//   private List<String> list = null;
    private MainSexangleAdapter mAdapter;


    @ViewInject(R.id.lyt_topdetail)
    private LinearLayout mLytTopDetail;
    @ViewInject(R.id.lyt_nextdetail)
    private LinearLayout mLytNextDetail;

    private BusinessServer mBllServer;
    private EtyServerDetail mapObject;
    private List<String> mListImgStr;//图片地址集合


//    private AdapterServerDetailItem mAdapterSerDetail;
    private int mItemId = 0;
    private String mStoreID;

    private List<Map<String, Object>> mListMaps;
    private WgtAlertDialog mDialog;

    //轮播图
    private AdapterAffordShopDetail mDetailImageAdapter;

    public static FragmentServerDetailPager newInstance(AtyServerDetail context, int position) {
        FragmentServerDetailPager f = new FragmentServerDetailPager();
        LogUtils.e("FragmentServerDetailPager========newInstance=============>" );
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        mContext = context;
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBllServer = new BusinessServer(this);
        LogUtils.e("FragmentServerDetailPager========newInstance=====onCreate========>");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_serverdetail_pager, container, false);
        ViewUtils.inject(this, rootView); //注入view和事件

        mLytNextDetail.setVisibility(View.GONE);
        LogUtils.e("FragmentServerDetailPager===shop===详情页========>" + getActivity().getIntent().getStringExtra(AtyServerList.INTENT_SERVER_ID));
        if (getActivity().getIntent().getStringExtra(AtyServerList.INTENT_SERVER_ID) != null) {
            //请求服务详情
            mBllServer.getDetail(getActivity().getIntent().getStringExtra(AtyServerList.INTENT_SERVER_ID));
        }
        return rootView;
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {

        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_SERVICE_DETAIL://服务详情
                    mapObject = (EtyServerDetail) beanSendUI.getInfo();
                    setDatasOnSuccess();//请求服务成功，数据写入对应的控件
                    break;
                default:
                    LogUtils.e("onSuccess===default===========>" + beanSendUI);
                    break;
            }
        } else {
            LogUtils.e("onSuccess======AtyAffordShopDetail======不可能的错误===>" + beanSendUI);
        }

    }


    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_SERVICE_DETAIL://服务详情


                    break;
                default:
                    LogUtils.e("onFailure===default===========>" + beanSendUI);
                    break;
            }
        } else {
            LogUtils.e("onFailure======AtyAffordShopDetail======不可能的错误===>" + beanSendUI);
        }
    }


    private void ShowSexangleListView()
    {
        LogUtils.e("FragmentServerDetailPager=====ShowSexangleListView===mListMaps=============>" + mListMaps);
        mAdapter = new MainSexangleAdapter(mContext, mListMaps);
        mCustomListView.setDividerHeight(10);
        mCustomListView.setDividerWidth(10);
        mCustomListView.setAdapter(mAdapter);
        //改变选中Item的背景色
        mAdapter.isChooseItem(0);
        mAdapter.notifyDataSetChanged();
        mCustomListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                mItemId = position;
                //改变选中Item的背景色
                mAdapter.isChooseItem(position);
                mAdapter.notifyDataSetChanged();
                if (!UtilList.isEmpty(mListMaps)) {
                    // 获取点击的item的数据
                    Map<String, Object> itemMap = mListMaps.get(position);
                    //清空
                    mContext.mItemMap.clear();
                    //给静态全局变量赋值
                    if (!UtilMap.isEmpty(itemMap)) {
                        mContext.mItemMap.putAll(itemMap);
                    }
                    //详情内容对应的item
                    mTvPrice.setText("￥" + itemMap.get("SELLPRICE"));
                    mTvOldPrice.setText("￥" + itemMap.get("MARKETPRICE"));
                } else {
                    LogUtils.e("onItemClick=========mListMaps========>" + mListMaps);
                }
            }
        });

        setlistViewHeigh(mCustomListView);
    }
    /**
     * 请求服务成功，数据写入对应的控件
     */
    public void setDatasOnSuccess() {
        mListMaps = (List<Map<String, Object>>) mapObject.getITEMS();
        //赋初始值
        if (!UtilList.isEmpty(mListMaps)){
            mContext.mItemMap.putAll(mListMaps.get(0));
        }
        mListImgStr = (List<String>) mapObject.getIMAGES();
        mStoreID = mapObject.getID() + "";//商店ID
        if (mapObject != null) {
            //店铺名称及服务次数
            mTvName.setText(mapObject.getNAME());
            mTvSale.setText("月服务次数" + mapObject.getTOTAL());
            if (!UtilList.isEmpty(mListMaps)) {
                mTvPrice.setText("￥" + mListMaps.get(0).get("SELLPRICE") + "");
                mTvOldPrice.setText("￥" + mListMaps.get(0).get("MARKETPRICE") + "");
            }
            mTvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
            if (!UtilString.isEmpty(mapObject.getCOMMENT() + "")) {
                mTvPricePresent.setText(mapObject.getCOMMENT());
            }
            if(mapObject.getHASDESCRIBE() == 0) {
                mScrollView.setScroll(false);
                mLytTopDetail.setVisibility(View.INVISIBLE);
            }else {
                mScrollView.setScroll(true);
                mLytTopDetail.setVisibility(View.VISIBLE);
            }
            autoImageFlowTimer();//轮播图

            //初始化
            ShowSexangleListView();

        } else {
            getActivity().finish();
        }
    }
    /**
     * 动态设置listview高度
     *
     * @param gridView
     */
    private void setlistViewHeigh(CustomListView gridView) {
        CustomAdapter listAdapter = gridView.getMyCustomAdapter();
        if (listAdapter == null ) {
            return;
        }
        int totalHeight = 0;
        int childWidthAll = 0;
        int row = 0;
        int parentWidth = gridView.getMeasuredWidth();
        int num = listAdapter.getCount();
        //当CustomListView的getRow>0时，不需要高度重置
        for (int i = 0; i < num; i++) {
                View chilView = listAdapter.getView(i, null, gridView);
                chilView.measure(0, 0);
                int width = chilView.getMeasuredWidth();
                //当app第一次启动进入详情页是得不到
                if(parentWidth == 0) {
                    parentWidth = 600;
                } else {
                    childWidthAll += width;
                }

                if(i == 0 && childWidthAll <= parentWidth) {
                    totalHeight += chilView.getMeasuredHeight();
                }

                if(childWidthAll > parentWidth) {
                    childWidthAll = width;
                    totalHeight += chilView.getMeasuredHeight();
                    ++row;
                }else {
                    //TODO  此处代码为临时适配，能适应95% 的情况，有待优化 @NiuFC
                    if (num>20){
                        totalHeight += 20;
                    }else if (num <10){
                        totalHeight += 80;
                    }else if (num ==10){
                        totalHeight += 110;
                    }else {
                        totalHeight += chilView.getMeasuredHeight();
                    }
                }

            }

            ViewGroup.LayoutParams params = gridView.getLayoutParams();
            params.height = totalHeight+100;
            gridView.setLayoutParams(params);

    }





    /**
     * 顶部轮播图
     */
    public void autoImageFlowTimer() {
        mListImgStr = mapObject.getIMAGES();//图片地址
        if (!UtilList.isEmpty(mListImgStr)) {
            mViewGroup.setVisibility(View.VISIBLE);
        }
        mDetailImageAdapter = new AdapterAffordShopDetail(getActivity(), mListImgStr, mViewPager, mCurNum, mTotalNum);
        mViewPager.setAdapter(mDetailImageAdapter);// 轮播图 显示数据
        mViewPager.setCurrentItem(0);
//        startAutoFlowTimer();//执行自动轮播
    }


}
