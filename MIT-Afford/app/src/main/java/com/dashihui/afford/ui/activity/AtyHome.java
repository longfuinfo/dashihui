package com.dashihui.afford.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.NearbySearchInfo;
import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessCommon;
import com.dashihui.afford.business.BusinessShop;
import com.dashihui.afford.business.entity.EntityLocation;
import com.dashihui.afford.business.entity.EtyList;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseMenuActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.ui.widget.WgtGridViewWithHeaderAndFooter;
import com.dashihui.afford.ui.widget.WgtHeaderGridView;
import com.dashihui.afford.ui.activity.server.AtyServer;
import com.dashihui.afford.ui.activity.server.AtyServerList;
import com.dashihui.afford.ui.adapter.AdapterHomeGridView;
import com.dashihui.afford.ui.adapter.AdapterHomeRecomGridView;
import com.dashihui.afford.ui.adapter.AdapterImage;
import com.dashihui.afford.ui.adapter.AdapterSpecial;
import com.dashihui.afford.ui.widget.WgtGridViewServer;
import com.dashihui.afford.util.UtilCommon;
import com.dashihui.afford.util.UtilUpdateApp;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.map.UtilMap;
import com.dashihui.afford.util.number.UtilNumber;
import com.dashihui.afford.util.preferences.UtilPreferences;
import com.dashihui.afford.util.string.UtilString;
import com.dashihui.afford.util.toast.UtilToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtyHome extends BaseMenuActivity implements CloudListener {

    private ViewPager mViewPager;
    private View mViewGroup;
    private View home_Layout;
    private int getLastVisiblePosition = 0, lastVisiblePositionY = 0;

    @ViewInject(R.id.loc_title)
    private TextView mTxtTitle;
    @ViewInject(R.id.rlyt_nonetwork)
    private RelativeLayout mRlytNoNetWork;//没有网络页面
    @ViewInject(R.id.layoutTags)
    private LinearLayout mLayoutTags;//标签列表

    @ViewInject(R.id.wgtHAF_gridview)
    private WgtGridViewWithHeaderAndFooter mWgtGridViewHeaderAndFooter;
//    @ViewInject(R.id.progressBar)
//    private LinearLayout mProgressBar;

    private AdapterImage mImageAdapter;
    private List<Map<String, Object>> picLists = new ArrayList<Map<String, Object>>();
    private Handler mHandler;

    /***************
     * 获取商铺详情：温馨提示
     **************/
    private TextView mTvExplain;//温馨提示

    /***********************
     * 4大分类
     ***********************/
    private WgtGridViewServer mWgtHorGridView;//4个分类的gridview
    private AdapterHomeGridView mAdapterHomeGridView;//4个分类的adapter

    private Map<String, Object> mapObject;
    private List<Map<String, Object>> mListMap;//保存对应的分类名和图片
    private List<Map<String, Object>> mListmapTags;//获取商品分类


    public static final String TAGNAME = "TAGNAME";//保存对应的分类名的键
    public static final String IMAGE = "IMAGE";//保存对应的分类图片的键
    public static final String CATEGORYONCODE = "CODE";//保存对应的分类图片的键
    public static final String TAGCODE = "TAGCODE";//保存对应的分类图片的键

    /************************
     * 专题分类
     ***********************/
    private ListView mListView;
    private AdapterSpecial mSpecialAdapter;

    /***********************
     * 精品推荐
     ***********************/


    private AdapterHomeRecomGridView mAdapterHomeRecom;
    private List<Map<String, Object>> mMapListByRecom;

    public final static String LEISURECODE = "07";
    public final static String WASHCODE = "04";

    //分页码
    private int pageNum = 1;
    private int mTotalPage = 0;//总页数
    private boolean isPage = false;//是否分页

    /************************  ***********************/
    private ViewGroup anim_mask_layout;//动画层
    @ViewInject(R.id.img_shopcart)
    private ImageView mImgShopCart;
    /************************  ***********************/
    private ImageButton mLeftImage, mRightTopImage, mRightBottomImage;

    private int currentItem = 0; // 当前图片的索引号
    private int i;
    //分类及对应图片
    public final static String mName[] = {"生鲜蔬果", "生活百货", "粮油调料", "酒水饮料"};
    public final static String mCode[] = {"010000000", "030000000", "040000000", "020000000"};
    public final static int mImg[] = new int[]{R.drawable.home_fruit, R.drawable.home_life, R.drawable.home_oil, R.drawable.home_drink};
    public final static String INTENT_TAG_TYPE = "tagType";
    public final static String INTENT_TAG_TYPENAME = "typeName";
    public final static String INTENT_TAG_TITLENAME = "titleName";
    public final static String INTENT_TAG_ADDRESS = "address";
    //是否刷新数据标识
    public final static String HOME_REFRESH = "homeRefresh";

    private BusinessCommon mBllCommon;
    private BusinessShop mBllShop;
    private UtilUpdateApp mUpdateApp;
    private AbsListView view1;
    // 定位相关
    private LocationClient mLocClient;
    private int mUid = 0;
    //区别是首页还是便利店传给轮播图的值
    public final static int HOMETYPE = 2;
    //让商品已加载完成只弹出一次
    private boolean isFirst = true;


    @Override
    public int getContentViewLayoutResId() {
        return R.layout.aty_home;
    }

    @Override
    protected void onCreatOverride(Bundle savedInstanceState) {

        ViewUtils.inject(this);//依赖注入
        //初始化更新
        initView();
        //顶部轮播图
        mBllCommon.getpublicAdList();
        //升级检测
        mBllCommon.checkVersion();
    /*    if (AffordApp.getInstance().getEntityLocation() == null) {
            //定位,没有自动登录后不再
            setLocationOption();
        } else {
            //商品标签分类
            mBllShop.goodsTags();
        }*/
        setLocationOption();

    }

    @Override
    protected void onResume() {

        mRlytNoNetWork.setVisibility(View.GONE);

        //没有定位时 去定位，否则直接请求数据
        if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance().getEntityLocation().getSTORE() != null) {
            String homeRefresh = UtilPreferences.getString(this, HOME_REFRESH) + "";
            String storeTitle = AffordApp.getInstance().getEntityLocation().getSTORE().getTITLE();
            //头部标题
            mTxtTitle.setText(storeTitle);
            //商铺消息通知
            mBllCommon.storeTip();
            //************************切换小区时更新页面******************************
            if (!storeTitle.equals(homeRefresh)) {
                //商品标签分类

                mBllShop.goodsTags();
                UtilPreferences.putString(this, HOME_REFRESH, storeTitle);
            } else {
                //第一次进入为空时填写
                if (UtilString.isEmpty(homeRefresh)) {
                    UtilPreferences.putString(this, HOME_REFRESH, homeRefresh);
                }
            }

        } else {

            LogUtils.e("null为空异常========================>");
        }
        super.onResume();
    }

    @Override
    public void onSuccess(EtySendToUI info) {
        if (info != null) {
            switch (info.getTag()) {
                case AffConstans.BUSINESS.TAG_AD_PUBLICADLIST://轮播图
                    picLists.clear();
                    picLists.addAll((List<Map<String, Object>>) info.getInfo());
                    LogUtils.e("==============>轮播图==============>" + info.getInfo());
                    mImageAdapter = new AdapterImage(this, picLists, mViewPager, mViewGroup, HOMETYPE);
                    mViewPager.setAdapter(mImageAdapter);// 轮播图 显示数据
                    mViewPager.setCurrentItem(picLists.size() * 1000);
                    startAutoFlowTimer();
                    break;
                case AffConstans.BUSINESS.TAG_COMMOON_CHECKVERSION://版本更新检查
                    Map<String, Object> beanRegister = (Map<String, Object>) info.getInfo();
                    mUpdateApp = new UtilUpdateApp(this);
                    mUpdateApp.startVersion(beanRegister, false);
                    break;

                case AffConstans.BUSINESS.TAG_COMMON_LOCATION://根据百度定位UID获取本地商铺信息
                    EntityLocation entityLocat = (EntityLocation) info.getInfo();
                    AffordApp.getInstance().setEntityLocation(entityLocat);
                    //商铺消息通知
                    mBllCommon.storeTip();
                    //发送广播更新底部购物车显示
                    sendShopChartBroadcast();
                    //标题更新
                    if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance().getEntityLocation().getSTORE() != null) {
                        mTxtTitle.setText(AffordApp.getInstance().getEntityLocation().getSTORE().getTITLE());
                    } else {
                        LogUtils.e("null为空异常========================>");
                        mBaseUtilAty.startActivity(AtyLocation.class);
                    }

                    mBllShop.goodsTags();

                    break;
                case AffConstans.BUSINESS.TAG_STORE_TIP://店铺通知查询
                    Map<String, Object> mapStoreTip = (Map<String, Object>) info.getInfo();
                    if (!UtilMap.isEmpty(mapStoreTip)) {
                        mTvExplain.setText(mapStoreTip.get("CONTENT") + "");
                    } else {
                        mTvExplain.setText("");
                    }
                    break;

                case AffConstans.BUSINESS.TAG_GOODS_TAGS://商品标签查询
                    List<Map<String, Object>> listmapTags = (List<Map<String, Object>>) info.getInfo();
                    mListmapTags.clear();
                    if (!UtilList.isEmpty(listmapTags)) {
                        mLayoutTags.setVisibility(View.VISIBLE);
                        mListmapTags.addAll(listmapTags);
                    } else {
                        mLayoutTags.setVisibility(View.GONE);
                    }
                    //刷新界面
                    mSpecialAdapter.setList(mListmapTags);
                    mSpecialAdapter.notifyDataSetChanged();
                    setlistViewHeigh(mListView);
                    //精品推荐列表//显示加载框
                    showProDialog(this);
                    pageNum = 1;
                    mBllShop.goodsListByRecom(pageNum + "");
                    break;

                case AffConstans.BUSINESS.TAG_GOODS_LISTBYRECOM://精品推荐商品列表

                    onBackButtonclick(getCurrentFocus());
                    EtyList etyListByRecom = (EtyList) info.getInfo();

                    if (pageNum == 1) {
                        mMapListByRecom.clear();
                    }
                    if (etyListByRecom != null && !UtilList.isEmpty(etyListByRecom.getLIST())) {
                        isPage = false;//恢复获取分页事件
                        mTotalPage = UtilNumber.IntegerValueOf(etyListByRecom.getTOTALPAGE() + "");
                        if (pageNum > 1) {
                            mWgtGridViewHeaderAndFooter.setSelection(mMapListByRecom.size());
                        }
                        mMapListByRecom.addAll(etyListByRecom.getLIST());
                    } else {
                        mMapListByRecom.clear();
                        LogUtils.e("onSuccess=======精品推荐商品列表========>" + mMapListByRecom);
//                        UtilToast.show(this, "服务器开小差了！", Toast.LENGTH_SHORT);
                    }
                    mAdapterHomeRecom.setList(mMapListByRecom);
                    mAdapterHomeRecom.notifyDataSetChanged();
                    //隐藏加载框
                    dissProDialog();
                    break;

                default:
                    LogUtils.e("onSuccess===default===========>" + info);
                    break;
            }
        } else {
            LogUtils.e("onSuccess======AtyHome====null=====>" + info);
        }

    }

    @Override
    public void onFailure(EtySendToUI info) {
        if (info != null) {
            switch (info.getTag()) {
                case AffConstans.BUSINESS.TAG_AD_PUBLICADLIST://轮播图

                    String msgStr = info.getInfo() + "";
                    LogUtils.e("onFailure======AtyHome=========>" + msgStr);
                    break;
                case AffConstans.BUSINESS.TAG_COMMOON_CHECKVERSION://版本更新检查
                    String msgStrVerSion = info.getInfo() + "";
                    LogUtils.e("onFailure======AtyHome=========>" + msgStrVerSion);
                    break;

                case AffConstans.BUSINESS.TAG_COMMON_LOCATION://根据百度定位UID获取本地商铺信息
                    String msgStrLoction = info.getInfo() + "";
                    LogUtils.e("onFailure======AtyHome=========>" + msgStrLoction);
                    break;
                case AffConstans.BUSINESS.TAG_STORE_TIP://店铺通知查询
                    String msgStrTip = info.getInfo() + "";
                    LogUtils.e("onFailureK======AtyHome=====店铺通知查询====>" + msgStrTip);
                    break;

                case AffConstans.BUSINESS.TAG_GOODS_TAGS://商品标签查询
                    String msgStrTags = info.getInfo() + "";
                    mLayoutTags.setVisibility(View.GONE);

                    LogUtils.e("onFailure======AtyHome=========>" + msgStrTags);
                    break;

                case AffConstans.BUSINESS.TAG_GOODS_LISTBYRECOM://精品推荐商品列表
                    String msgStrCom = info.getInfo() + "";
                    LogUtils.e("onFailure======AtyHome=========>" + msgStrCom);
                    //隐藏加载框
                    dissProDialog();
                    break;

                default:
                    LogUtils.e("onFailure===default===========>" + info);
                    break;
            }
        } else {
            LogUtils.e("onFailure======AtyHome=========>" + info);
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {

        mBllCommon = new BusinessCommon(this);
        //商品标签分类
        if (mBllShop == null) {
            mBllShop = new BusinessShop(this);
        }
        mMapListByRecom = new ArrayList<>();
        mListmapTags = new ArrayList<>();
        /********* 4个分类 ********/
        /********* 精品推荐 ********/
        home_Layout = LayoutInflater.from(this).inflate(R.layout.aty_home_header, null);
        mWgtHorGridView = (WgtGridViewServer) home_Layout.findViewById(R.id.wgt_gridview);
        mViewPager = (ViewPager) home_Layout.findViewById(R.id.viewpager);

        mLayoutTags = (LinearLayout) home_Layout.findViewById(R.id.layoutTags);
        mViewGroup = (View) home_Layout.findViewById(R.id.viewGroup);
        mListView = (ListView) home_Layout.findViewById(R.id.listview);
        mTvExplain = (TextView) home_Layout.findViewById(R.id.explain);//温馨提示
        mLeftImage = (ImageButton) home_Layout.findViewById(R.id.left_imgbtn);
        mRightTopImage = (ImageButton) home_Layout.findViewById(R.id.right_imgbtnTop);
        mRightBottomImage = (ImageButton) home_Layout.findViewById(R.id.right_imgbtnBottom);
        mLeftImage.setOnClickListener(onImageClick);
        mRightTopImage.setOnClickListener(onImageClick);
        mRightBottomImage.setOnClickListener(onImageClick);

        mWgtGridViewHeaderAndFooter.addHeaderView(home_Layout);
        mAdapterHomeRecom = new AdapterHomeRecomGridView(this, mMapListByRecom);
        //自定义推荐标签品种
        mSpecialAdapter = new AdapterSpecial(this, mListmapTags);
        mListView.setAdapter(mSpecialAdapter);
        //造数据
        saveNameWithImg();
        mAdapterHomeGridView = new AdapterHomeGridView(this, mListMap);
        mWgtHorGridView.setAdapter(mAdapterHomeGridView);
        mWgtGridViewHeaderAndFooter.setAdapter(mAdapterHomeRecom);
        mWgtGridViewHeaderAndFooter.setOnScrollListener(new AbsListView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                view1 = view;
                int y = 0;
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        Log.v("onScrollStateChanged", "已经停止：SCROLL_STATE_IDLE");
                        // 判断滚动到底部
                        if (!isPage && view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            int[] location = new int[2];
                            View v = view.getChildAt(view.getChildCount() - 1);
                            v.getLocationOnScreen(location);
                            y = location[1];
                            if (view.getLastVisiblePosition() != getLastVisiblePosition && lastVisiblePositionY != y && mTotalPage > pageNum)//第一次拖至底部
                            {
                                getLastVisiblePosition = view.getLastVisiblePosition();
                                lastVisiblePositionY = y;
                                //显示加载框
                                showProDialog(AtyHome.this);
                                //获取商品列表，第二个参数：优惠类型	1：普通，2：推荐，3：限量，4：一元购
                                mBllShop.goodsListByRecom(++pageNum + "");
                                isPage = true;//关闭获取分页事件
                                return;
                            } else if(mTotalPage == pageNum) {
                                if (isFirst) {
                                    isFirst = false;
                                    UtilToast.show(AtyHome.this, "商品已全部加载完！");
                                }
                            }

                        }
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        if (!isPage && view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            if (view.getLastVisiblePosition() != getLastVisiblePosition && lastVisiblePositionY != y && mTotalPage > pageNum)//第一次拖至底部
                            {
                                //显示加载框
                                showProDialog(AtyHome.this);
                                //获取商品列表，第二个参数：优惠类型	1：普通，2：推荐，3：限量，4：一元购
                                mBllShop.goodsListByRecom(++pageNum + "");
                                isPage = true;//关闭获取分页事件
                                return;
                            }
                        }
                   /* case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        if (view.getLastVisiblePosition() != getLastVisiblePosition && lastVisiblePositionY != y && mTotalPage > pageNum)//第一次拖至底部
                        {
                            //显示加载框
                            showProDialog(AtyHome.this);
                            //获取商品列表，第二个参数：优惠类型	1：普通，2：推荐，3：限量，4：一元购
                            mBllShop.goodsListByRecom(++pageNum + "");
                            isPage = true;//关闭获取分页事件
                            break;
                        }*/
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }
    /**
     * 将图片和名字对应保存
     */
    private void saveNameWithImg() {
        //服务分类的名字存入对应的图片
        mListMap = new ArrayList<>();
        for (int i = 0; i < mName.length; i++) {
            mapObject = new HashMap<>();
            mapObject.put(IMAGE, mImg[i]);
            mapObject.put(CATEGORYONCODE, mCode[i]);
            mapObject.put(TAGNAME, mName[i]);
            mListMap.add(mapObject);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                break;
        }
        return super.onTouchEvent(event);
    }

    /****************************
     * 以下是精品推荐
     *************************/


    //顶部搜索按钮
    @OnClick(R.id.search_edt)
    public void onBtnSearchClick(View v) {
        mBaseUtilAty.startActivity(AtySearch.class);
    }

    private View.OnClickListener onImageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.left_imgbtn:
                    if (UtilCommon.isNetworkAvailable(AtyHome.this)) {
                        mBaseUtilAty.startActivity(AtyServer.class);
                    } else {
                        mBaseUtilAty.startActivity(AtyNetWork.class);
                    }
                    break;
                case R.id.right_imgbtnTop:
//                    //根据获取的服务分类名称跳转到服务列表
                    if (UtilCommon.isNetworkAvailable(AtyHome.this)) {
                        Intent intent = new Intent(AtyHome.this, AtyServerList.class);
                        intent.putExtra(CommConstans.SERVER.SERVER_NAME, "休闲娱乐");
                        intent.putExtra(CommConstans.SERVER.INTENT_TAG_TYPE, LEISURECODE);
                        intent.putExtra(CommConstans.SERVER.SERVER_CODE, LEISURECODE);
                        startActivity(intent);
                    } else {
                        mBaseUtilAty.startActivity(AtyNetWork.class);
                    }
                    break;
                case R.id.right_imgbtnBottom:
                    if (UtilCommon.isNetworkAvailable(AtyHome.this)) {
                        //根据获取的服务分类名称跳转到服务列表
                        Intent intentWash = new Intent(AtyHome.this, AtyServerList.class);
                        intentWash.putExtra(CommConstans.SERVER.SERVER_NAME, "洗衣");
                        intentWash.putExtra(CommConstans.SERVER.INTENT_TAG_TYPE, WASHCODE);
                        intentWash.putExtra(CommConstans.SERVER.SERVER_CODE, WASHCODE);
                        startActivity(intentWash);
                    } else {
                        mBaseUtilAty.startActivity(AtyNetWork.class);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @OnClick(R.id.loc_title)
    public void onTvTitleClick(View v) {
        if (UtilCommon.isNetworkAvailable(AtyHome.this)) {
            if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance().getEntityLocation().getCOMMUNITY() != null) {
                LogUtils.e("AtyLocation===AtyLocation==1111====>");
                Intent mIntent = new Intent(AtyHome.this, AtyLocation.class);
                mIntent.putExtra(INTENT_TAG_TITLENAME, AffordApp.getInstance().getEntityLocation().getCOMMUNITY().getTITLE());
                mIntent.putExtra(INTENT_TAG_ADDRESS, AffordApp.getInstance().getEntityLocation().getCOMMUNITY().getADDRESS());
                startActivity(mIntent);
            } else {
                mBaseUtilAty.startActivity(AtyLocation.class);
            }
        } else {
            mBaseUtilAty.startActivity(AtyNetWork.class);
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


    /*************************
     * 以下是v1.3.8前版本
     *************************/

    //点击立即重试，再次获取网络
    @OnClick(R.id.net_again)
    public void onGetNetWorkClick(View v) {
        if (UtilCommon.isNetworkAvailable(this)) {
            mBaseUtilAty.startActivity(AtyHome.class);
        } else {
            UtilToast.show(getApplicationContext(), R.string.home_examine, Toast.LENGTH_SHORT);
            mBaseUtilAty.startActivity(AtyNetWork.class);
        }

    }


    //点击首页顶部跳转至定位小区
    @OnClick(R.id.title)
    public void onTitleClick(View v) {
        if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance().getEntityLocation().getCOMMUNITY() != null) {
            Intent mIntent = new Intent(AtyHome.this, AtyLocation.class);
            mIntent.putExtra(INTENT_TAG_TITLENAME, AffordApp.getInstance().getEntityLocation().getCOMMUNITY().getTITLE());
            mIntent.putExtra(INTENT_TAG_ADDRESS, AffordApp.getInstance().getEntityLocation().getCOMMUNITY().getADDRESS());
            startActivity(mIntent);
        } else {
            mBaseUtilAty.startActivity(AtyLocation.class);
        }
        finish();
    }

    @OnClick(R.id.backbutton)//快速返回顶部
    public void onBackButtonclick(View v) {
        LogUtils.e("onBackButtonclick=======pageNum======>" + pageNum);
        if (!mWgtGridViewHeaderAndFooter.isStackFromBottom()) {
            mWgtGridViewHeaderAndFooter.setSelection(0);
            mWgtGridViewHeaderAndFooter.setAdapter(mAdapterHomeRecom);
        }
    }


    /**
     * 自动轮播
     */
    public void startAutoFlowTimer() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (mHandler != null) {
                    synchronized (mHandler) {
                        if (picLists.size() == 1) {
                            return;
                        }
                        currentItem = mViewPager.getCurrentItem() + 1;
                        mViewPager.setCurrentItem(currentItem);
                        Message message = mHandler.obtainMessage(0);
                        sendMessageDelayed(message, 3000);
                    }
                } else {
//                    LogUtils.e("自动轮播已关闭=============>" + mHandler);
                }
            }
        };
        Message message = mHandler.obtainMessage(0);
        mHandler.sendMessageDelayed(message, 3000);
    }

    /**
     * 停止运行
     */
    public void stopAutoFlowTimer() {
        if (mHandler != null)
            mHandler.removeMessages(0);
        mHandler = null;
    }

    @Override
    protected void onStop() {
        stopAutoFlowTimer();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //退出时销毁定位
        if (mLocClient != null) {
            mLocClient.stop();
            mLocClient = null;
        }
        CloudManager.getInstance().destroy();
        super.onDestroy();
    }

    @Override
    public int getButtonType() {
        return 0;
    }


    /**
     * 定位
     */
    private void  setLocationOption() {
        mLocClient = new LocationClient(this);
        MyLocationListener mMyLocationListener = new MyLocationListener();
        mLocClient.registerLocationListener(mMyLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认是gcj02
        option.setScanSpan(6000);//设置发起定位请求的间隔时间是6秒
        option.setIsNeedAddress(true);//返回定位结果包含地址信息
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mLocClient.setLocOption(option);
        mLocClient.start();
//       mLocClient.requestLocation();
//        LogUtils.e("======定位开始========>");
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                mBaseUtilAty.startActivity(AtyLocation.class);
                finish();
                return;
            }
            String currentPosition = location.getLongitude() + "," + location.getLatitude();
            nearbySearch(currentPosition);
        }

    }


    /**
     * 云检索一定范围内的社区信息
     *
     * @param currentPosition
     */
    private void nearbySearch(String currentPosition) {
        CloudManager.getInstance().init(this);
        NearbySearchInfo info = new NearbySearchInfo();
        info.ak = AffConstans.PUBLIC.INFO_AK;
        info.geoTableId = AffConstans.PUBLIC.INFO_geoTableId;
        info.radius = AffConstans.PUBLIC.INFO_RADIUS;
        info.location = currentPosition;//定位后传过来的坐标
        //云检索一定范围内的社区信息
        CloudManager.getInstance().nearbySearch(info);
    }

    @Override
    public void onGetSearchResult(CloudSearchResult cloudSearchResult, int error) {
//        LogUtils.e("onGetSearchResult=============>" + error);
        if (cloudSearchResult != null && cloudSearchResult.poiList != null
                && cloudSearchResult.poiList.size() > 0) {
            int minDistance = Integer.MAX_VALUE;
//            String cityStr = "";
            for (CloudPoiInfo info : cloudSearchResult.poiList) {
                if (info.distance < minDistance) {
                    minDistance = info.distance;
//                    mCommunityName = info.title;
//                    cityStr = info.city;
                    mUid = info.uid;
                }
            }
            if (mUid != 0) {
                //根据最近的社区调用 百度定位获取社区信息
                mBllCommon.getlocation(mUid + "");
            } else {
                LogUtils.e("======定位监听=====111===>" + minDistance);
                //到定位页
                mBaseUtilAty.startActivity(AtyLocation.class);
                finish();
            }
        } else {
            Intent mIntent = new Intent(AtyHome.this, AtyLocation.class);
            mIntent.putExtra(INTENT_TAG_TITLENAME, "");
            mIntent.putExtra(INTENT_TAG_ADDRESS, "");
            startActivity(mIntent);
            finish();
        }
    }

    @Override
    public void onGetDetailSearchResult(DetailSearchResult detailSearchResult, int i) {
        LogUtils.e("========onGetDetailSearchResult========>");
    }


    /**
     * @param
     * @return void
     * @throws
     * @Description: 创建动画层
     */
    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(Integer.MAX_VALUE - 1);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    /**
     * @param parent
     * @param view
     * @param location
     * @return
     */
    private View addViewToAnimLayout(final ViewGroup parent, final View view, int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }


    /**
     * 设置动画
     *
     * @param view1
     * @param startLocation
     */
    public void setAnim(final View view1, int[] startLocation) {
        anim_mask_layout = null;
        anim_mask_layout = createAnimLayout();
        anim_mask_layout.addView(view1);//把动画小球添加到动画层
        final View viewAnim = addViewToAnimLayout(anim_mask_layout, view1, startLocation);
        int[] endLocation = new int[2];// 存储动画结束位置的X、Y坐标

        mImgShopCart.getLocationInWindow(endLocation);// mImgShopCart是购物车图标/数字小图标

        // 计算位移
        int endX = endLocation[0] - startLocation[0] + 50;// 动画位移的X坐标
        int endY = endLocation[1] - startLocation[1];// 动画位移的y坐标

//        TranslateAnimation translateAnimationY = new TranslateAnimation(100, endX, -130, endY);
        TranslateAnimation translateAnimationY = new TranslateAnimation(0, endX, 0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationY.setFillAfter(true);

        AnimationSet set1 = new AnimationSet(false);
        set1.setFillAfter(false);
        set1.addAnimation(translateAnimationY);
        set1.setDuration(800);// 动画的执行时间
        viewAnim.startAnimation(set1);
        // 动画监听事件
        set1.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                view1.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                view1.setVisibility(View.INVISIBLE);
                //发送广播更新底部购物车显示
                sendShopChartBroadcast();
            }
        });
    }


}
