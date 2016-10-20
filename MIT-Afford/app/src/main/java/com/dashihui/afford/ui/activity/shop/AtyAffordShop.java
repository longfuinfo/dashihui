package com.dashihui.afford.ui.activity.shop;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessCommon;
import com.dashihui.afford.business.BusinessShop;
import com.dashihui.afford.business.entity.EtyList;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseMenuActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.thirdapi.choosemenu.ExpandTabView;
import com.dashihui.afford.thirdapi.choosemenu.ViewLeft;
import com.dashihui.afford.thirdapi.choosemenu.ViewMiddle;
import com.dashihui.afford.thirdapi.choosemenu.ViewMiddleRight;
import com.dashihui.afford.thirdapi.choosemenu.ViewRight;
import com.dashihui.afford.ui.activity.AtySearch;
import com.dashihui.afford.ui.activity.AtyHome;
import com.dashihui.afford.ui.adapter.AdapterAffordShop;
import com.dashihui.afford.ui.adapter.AdapterImage;
import com.dashihui.afford.ui.widget.WgtAlertDialog;
import com.dashihui.afford.ui.widget.WgtGridViewWithHeaderAndFooter;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.preferences.UtilPreferences;
import com.dashihui.afford.util.string.UtilString;
import com.dashihui.afford.util.toast.UtilToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtyAffordShop extends BaseMenuActivity implements View.OnClickListener {

    @ViewInject(R.id.gridview)
    private WgtGridViewWithHeaderAndFooter mGridview;
    @ViewInject(R.id.img_shopcart)
    private ImageView mImgShopCart;
    @ViewInject(R.id.backbutton)
    private ImageButton mBackButton;
    @ViewInject(R.id.ll_noGoods)
    private LinearLayout mLlNoGoods;

    //顶部轮播图控件
    private View mViewGroup;
    private ViewPager mViewPager;
    //列表适配器
    private AdapterAffordShop mAdapter;

    private BusinessShop mShopBll;
    private int currentItem = 0; // 当前图片的索引号

    //轮播图
    private AdapterImage mImageAdapter;
    private List<Map<String, Object>> picLists = new ArrayList<Map<String, Object>>();
    private Handler mHandler;
    private List<Map<String, Object>> mMapList;
    //分页码
    private int pageNum = 1;


    public final static String INTENT_SHOP_ID = "shopid";
    public final static String INTENT_SHOP_IMG = "shopImg";

    private boolean isPage = false;//是否分页
    private static String type = "010000000";//列表分类
    private static String smallType = "";//二级分类
    private static int totalPage = 0;//总页数
    private ExpandTabView expandTabView;

    private String typeName = "蔬菜水果";//默认蔬菜水果
    private ArrayList<View> mViewArray = new ArrayList<View>();
    private ViewMiddle viewMiddle;
    private ViewLeft viewLeft;
    private ViewRight viewRight;
    private ViewMiddleRight viewMiddleRight;

    private BusinessCommon mBLLCommon;
    private List<Map<String, Object>> mListMapCategory;
    //销量点击排序
    private static boolean isorderby = false;
    private static String salesOrderby = "1";


    private static int isSelf = 0;//0:不是自营  1:是自营
    private static int isRebate = 0;// 是否返利 0:不返  1:返

    private static int isOrder = 0;//0:默认未排序 1:价格高到低 2:价格低到高 3:销量高到低 4;销量低到高


    private String mTopTitle, mInitCode;
    private Map<String, Object> mMapTitle;

    private ViewGroup anim_mask_layout;//动画层
    //是否刷新数据标识
    private final static String AFFORDSHOP_REFRESH = "affordshopRefresh";

    public final static String INITNAME = "INITNAME";
    private boolean isFirst = true;
    private int selected = 0;
    private int i;
    //区别是首页还是便利店传给轮播图的值
    public final static int SHOPTYPE = 1;

    @Override
    public int getContentViewLayoutResId() {
        return R.layout.aty_affordshop;
    }

    @Override
    protected void onCreatOverride(Bundle savedInstanceState) {
        ViewUtils.inject(this);//依赖注入
        //初始化
        initView();
        mBLLCommon = new BusinessCommon(this);
        mShopBll = new BusinessShop(this);
        mMapList = new ArrayList<>();
        mListMapCategory = new ArrayList<>();
        mAdapter = new AdapterAffordShop(this, mMapList);
        mGridview.setAdapter(mAdapter);
        //请求分类列表
        mBLLCommon.commonCategory(isSelf + "");
        LogUtils.e("isSelf=========>" + isSelf);
        //顶部轮播图
        mShopBll.getStoreAdList();
        //回显已选中的是否自营
        expandTabView.toggleDrawable(isSelf, 2);
        expandTabView.setBackgroundColor(Color.WHITE);
        if (isSelf == 1) {
            expandTabView.setTitleColor(getResources().getColor(R.color.shop_font_red), 2);
        }
        initData();//
        //************************切换小区时更新页面******************************
        String refresh = UtilPreferences.getString(this, AFFORDSHOP_REFRESH) + "";
        //第一次进入为空时填写
        if (UtilString.isEmpty(refresh)) {
            String storeTitle1 = AffordApp.getInstance().getEntityLocation().getSTORE().getTITLE();
            UtilPreferences.putString(this, AFFORDSHOP_REFRESH, storeTitle1);
        }
    }


    @Override
    protected void onResume() {
        if (AffordApp.getInstance().getEntityLocation() != null) {
            String refresh = UtilPreferences.getString(this, AFFORDSHOP_REFRESH) + "";
            String storeTitle = AffordApp.getInstance().getEntityLocation().getSTORE().getTITLE();
            if (!storeTitle.equals(refresh)) {
                //请求分类列表
                mBLLCommon.commonCategory(isSelf + "");
                //顶部轮播图
                mShopBll.getStoreAdList();
                initData();

                //更改本地存储的社区名称
                UtilPreferences.putString(this, AFFORDSHOP_REFRESH, storeTitle);
            } else {
                LogUtils.e("AtyAffordShop======00====同一个便利店====>" + refresh);
            }
        } else {
            LogUtils.e("onResume========AffordApp.getInstance()======>");
        }
        super.onResume();
    }

    /**
     *
     */
    public void initData() {
        Intent intent = getIntent();
        smallType = "";
        pageNum = 1;
        mShopBll.getGoodsList(type, smallType, "1", isOrder + "", pageNum + "", isSelf +
                "", isRebate + "");
        //显示加载框
        showProDialog(this);
        if (intent != null && intent.getStringExtra(AtyHome.INTENT_TAG_TYPE) != null) {
            type = intent.getStringExtra(AtyHome.INTENT_TAG_TYPE);
            typeName = intent.getStringExtra(AtyHome.INTENT_TAG_TYPENAME);
            isSelf =0;
            expandTabView.toggleDrawable(isSelf, 2);
//            mShopBll.getGoodsList(type + "", smallType, "1", salesOrderby, pageNum + "");
            mShopBll.getGoodsList(type, smallType, "1", isOrder + "", pageNum + "", isSelf +
                    "", isRebate + "");
        } else {
            //默认情况下，选择蔬菜水果
            expandTabView.toggleDrawable(isSelf, 2);
//            mShopBll.getGoodsList(type + "", smallType, "1", salesOrderby, pageNum + "");
            mShopBll.getGoodsList(type, smallType, "1", isOrder + "", pageNum + "", isSelf +
                    "", isRebate + "");
        }
    }

    private int getLastVisiblePosition = 0, lastVisiblePositionY = 0;

    private void initView() {

        View home_Layout = LayoutInflater.from(this).inflate(R.layout.aty_affordshop_header, null);
        //初始化二级菜单
        expandTabView = (ExpandTabView) findViewById(R.id.expandtab_view);
        // 轮播图
        mViewPager = (ViewPager) home_Layout.findViewById(R.id.viewpager);
        mViewGroup = (View) home_Layout.findViewById(R.id.viewGroup);
        mGridview.addHeaderView(home_Layout);
        mGridview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int y = 0;
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        Log.v("onScrollStateChanged", "已经停止：SCROLL_STATE_IDLE");
                        // 判断滚动到底部
                        if (!isPage && view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            View v = view.getChildAt(view.getChildCount() - 1);
                            int[] location = new int[2];
                            v.getLocationOnScreen(location);
                            y = location[1];
                            if (view.getLastVisiblePosition() != getLastVisiblePosition && lastVisiblePositionY != y && totalPage > pageNum)//第一次拖至底部
                            {
                                getLastVisiblePosition = view.getLastVisiblePosition();
                                lastVisiblePositionY = y;
                                //显示加载框
                                showProDialog(AtyAffordShop.this);
                                //获取商品列表，第二个参数：优惠类型	1：普通，2：推荐，3：限量，4：一元购
                                mShopBll.getGoodsList(type, smallType, "1", isOrder + "", ++pageNum + "", isSelf +
                                        "", isRebate + "");                                isPage = true;//关闭获取分页事件
                                return;
                            } else  if(totalPage == pageNum){
                                if(isFirst) {
                                    isFirst = false;
                                    UtilToast.show(AtyAffordShop.this, "商品已全部加载完！");
                                }
                            }

                        }
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        if (!isPage && view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            if (view.getLastVisiblePosition() != getLastVisiblePosition && lastVisiblePositionY != y && totalPage > pageNum)//第一次拖至底部
                            {
                                //显示加载框
                                showProDialog(AtyAffordShop.this);
                                //获取商品列表，第二个参数：优惠类型	1：普通，2：推荐，3：限量，4：一元购
                                mShopBll.getGoodsList(type, smallType, "1", isOrder + "", ++pageNum + "", isSelf +
                                        "", isRebate + "");                                isPage = true;//关闭获取分页事件
                                return;
                            }
                            }

                        }
                /*    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        if (view.getLastVisiblePosition() != getLastVisiblePosition && lastVisiblePositionY != y && totalPage > pageNum)//第一次拖至底部
                    {
                        //显示加载框
                        showProDialog(AtyAffordShop.this);
                        //获取商品列表，第二个参数：优惠类型	1：普通，2：推荐，3：限量，4：一元购
                        mShopBll.getGoodsList(type + "", smallType, "1", isOrder + "", ++pageNum + "");
                        isPage = true;//关闭获取分页事件
                        break;
                    }*/

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
            }
        });

    }

    private void initVaule(List<Map<String, Object>> listmap) {

        viewLeft = new ViewLeft(this, listmap);
        viewMiddle = new ViewMiddle(this);
        viewRight = new ViewRight(this);
        viewMiddleRight = new ViewMiddleRight(this);

        mViewArray.clear();
        mViewArray.add(viewLeft);
        mViewArray.add(viewMiddle);
        mViewArray.add(viewMiddleRight);
        mViewArray.add(viewRight);

        ArrayList<String> mTextArray = new ArrayList<String>();
        mTextArray.add("全部");
        mTextArray.add("价格");
        mTextArray.add("大实惠直营");
        mTextArray.add("销量");
        expandTabView.removeAllViews();
        expandTabView.setValue(mTextArray, mViewArray);
    }

    private void initCateData() {
        //初始化顶部菜单栏显示的标题
        for (int i = 0; i < mListMapCategory.size(); i++) {
            mTopTitle = mListMapCategory.get(i).get("NAME") + "";
            mInitCode = mListMapCategory.get(i).get("CODE") + "";
            if (type.equals(mInitCode) && "".equals(smallType)) {
                if (!UtilString.isEmpty(mTopTitle)) {
                    mMapTitle = new HashMap<>();
                    mMapTitle.put(INITNAME, mTopTitle);
                }
            }
        }
        if (mMapTitle != null && mMapTitle.size() > 0) {
            expandTabView.setTitle(mMapTitle.get(INITNAME) + "", 0);
            expandTabView.setTitleColor(Color.parseColor("#c52720"), 0);
        } else {
            expandTabView.setTitle(viewLeft.getShowText(), 0);
            expandTabView.setTitleColor(Color.parseColor("#c52720"), 0);
        }

//        expandTabView.setTitle(viewLeft.getShowText(), 0);
        expandTabView.setTitle(viewMiddle.getShowText(), 1);
        expandTabView.setTitle(viewRight.getShowText(), 3);
        expandTabView.setTitle(viewMiddleRight.getShowText(), 2);
        initListener();

    }

    private void initListener() {

        viewLeft.setOnSelectListener(new ViewLeft.OnSelectListener() {

            @Override
            public void getValue(String showText, String dType, String xType) {
                onRefresh(showText, dType, xType);
            }
        });
        expandTabView.setOnButtonClickListener(new ExpandTabView.OnButtonClickListener() {
            @Override
            public void onClick(int selectPosition) {
                switch (selectPosition) {
                    case 0:
                        isOrder = 1;
                        //1：默认排序，2：销量从高到低，3：价格从低到高，4：价格从高到低
                        //设置选中时的颜色
                        expandTabView.setTitleColor(getResources().getColor(R.color
                                .shop_font_red), 0);
                        expandTabView.setTitleColor(getResources().getColor(R.color
                                .shop_font_black), 1);
                        expandTabView.setTitleColor(getResources().getColor(R.color
                                .shop_font_black), 3);
                        expandTabView.togglePriceDrawable(isOrder, 3);
                        break;
                    case 1://销量
                        mMapList.clear();
                        if (isOrder != 2) {
                            isOrder = 2;
                        }
                        expandTabView.togglePriceDrawable(isOrder, 3);
                        pageNum = 1;
                        //设置选中时的颜色
                        expandTabView.setTitleColor(getResources().getColor(R.color
                                .shop_font_black), 0);
                        expandTabView.setTitleColor(getResources().getColor(R.color
                                .shop_font_red), 1);
                        expandTabView.setTitleColor(getResources().getColor(R.color
                                .shop_font_black), 3);
                        //显示加载框
                        showProDialog(AtyAffordShop.this);
                        expandTabView.dismisPopup();
                        break;
                    case 2://自营
                        mMapList.clear();
                        pageNum = 1;
                        if (isSelf == 0) {
                            isSelf = 1;
                            LogUtils.e("onRefresh===showText===========>" + "hong");
                            expandTabView.setTitleColor(getResources().getColor(R.color
                                    .shop_font_red), 2);
                            expandTabView.toggleDrawable(isSelf, 2);
                        } else {
                            isSelf = 0;
                            LogUtils.e("onRefresh===showText===========>" + "hei");
                            expandTabView.setTitleColor(getResources().getColor(R.color
                                    .shop_font_black), 2);
                            expandTabView.toggleDrawable(isSelf, 2);
                        }
//                        LogUtils.e("onRefresh=====价格排序=========>" + selectPosition);
                        //显示加载框
                        showProDialog(AtyAffordShop.this);
                        mBLLCommon.commonCategory(isSelf + "");
                        expandTabView.dismisPopup();
                        break;
                    case 3://价格
                        //                1：默认排序，2：销量从高到低，3：价格从低到高，4：价格从高到低
                        if (isOrder != 3 && isOrder != 4) {
                            isOrder = 3;
                            expandTabView.togglePriceDrawable(isOrder, 3);
                        } else if (isOrder == 3) {
                            isOrder = 4;
                            expandTabView.togglePriceDrawable(isOrder, 3);
                        } else if (isOrder == 4) {
                            isOrder = 3;
                            expandTabView.togglePriceDrawable(isOrder, 3);
                        }
                        showProDialog(AtyAffordShop.this);
                        mMapList.clear();
                        pageNum = 1;
                        expandTabView.setTitleColor(getResources().getColor(R.color
                                .shop_font_black), 0);
                        expandTabView.setTitleColor(getResources().getColor(R.color
                                .shop_font_black), 1);
                        expandTabView.setTitleColor(getResources().getColor(R.color
                                .shop_font_red), 3);
                        expandTabView.dismisPopup();
                        break;
                    default:
                        break;
                }
                mShopBll.getGoodsList(type, smallType, "1", isOrder + "", pageNum + "", isSelf +
                        "", isRebate + "");

            }
        });
    }


    /**
     * @param showText
     * @param dType    大分类
     * @param xType    小分类
     */
    private void onRefresh(String showText, String dType, String xType) {

        expandTabView.onPressBack();
        LogUtils.e("onRefresh===showText===========>" + showText);
        expandTabView.setTitle(showText, 0);
        mMapList.clear();
        isPage = true;//关闭获取分页事件
        pageNum = 1;
        type = dType;
        smallType = xType;
        LogUtils.e("onRefresh=======smallType======>" + smallType);
        //重新选择选默认排序
        salesOrderby = "1";
        //显示加载框
        showProDialog(this);
//        mShopBll.getGoodsList(type, smallType, "1", salesOrderby, pageNum + "");
        //分类列表请求数据
        mShopBll.getGoodsList(type, smallType, "1", isOrder + "", pageNum + "", isSelf + "",
                isRebate + "");
    }


    @Override
    public void onSuccess(EtySendToUI info) {
        if (info != null) {
            switch (info.getTag()) {
                case AffConstans.BUSINESS.TAG_AD_STOREADLIST://轮播图
//                    LogUtils.e("onSuccess===shop===轮播图========>" + info.getInfo());
                    picLists.clear();
                    List<Map<String, Object>> listObject = (List<Map<String, Object>>) info
                            .getInfo();
//                    LogUtils.e("onSuccess===shop===listObject========>" + listObject);

                    if (!UtilList.isEmpty(listObject)) {
                        picLists.addAll(listObject);
                        mImageAdapter = new AdapterImage(this, picLists, mViewPager, mViewGroup,
                                SHOPTYPE);
                        mViewPager.setAdapter(mImageAdapter);// 轮播图 显示数据
                        mViewPager.setCurrentItem(picLists.size() * 1000);
                        startAutoFlowTimer();
                    } else {
                        LogUtils.e("onSuccess===轮播图===========>" + info);
                    }
                    break;


                case AffConstans.BUSINESS.TAG_COMMON_CATEGORY://商品分类查询
                    LogUtils.e("onSuccess============商品分类查询============>" + info.getInfo());
                    if (mListMapCategory.size() == 0) {
                        mListMapCategory.clear();
                        mListMapCategory.addAll((List<Map<String, Object>>) info.getInfo());
                        Log.i("mListMapCategory", mListMapCategory.toString());
                        initVaule(mListMapCategory);
                        initCateData();
                    } else {
                        mListMapCategory.clear();
                        mListMapCategory.addAll((List<Map<String, Object>>) info.getInfo());
                        LogUtils.e("mListMapCategory============!null" + mListMapCategory.toString());
                        viewLeft.setmModelCatogray(mListMapCategory);
                    }
                    break;

                case AffConstans.BUSINESS.TAG_GOODS_LIST://列表
                    //隐藏加载框
                    dissProDialog();
                    EtyList etyList = (EtyList) info.getInfo();
                    isPage = false;//恢复获取分页事件
                    LogUtils.e("onSuccess===shop===etyList.getLIST()==1=====>" + etyList.getLIST());
                    if (etyList != null && !UtilList.isEmpty(etyList.getLIST())) {
                        mLlNoGoods.setVisibility(View.GONE);
                        //如果是第一页，清空原来数据
                        if (pageNum == 1) {
                            mMapList.clear();
                        }
                        totalPage = etyList.getTOTALPAGE();
                        mMapList.addAll(etyList.getLIST());
                        Log.i("getLIST", etyList.getLIST().toString());
                        Log.i("mMapList", etyList.getLIST().toString());
                        mAdapter.setList(mMapList);
                        mAdapter.notifyDataSetChanged();
                        if (pageNum == 1 && !mGridview.isStackFromBottom()) {
                            mGridview.setSelection(0);
                            mGridview.setAdapter(mAdapter);
                        }

                        LogUtils.e("onSuccess===shop===etyList.getLIST()==2=====>" + etyList
                                .getLIST());
                        LogUtils.e("onSuccess===shop===列表=======>" + mMapList);
                    } else {
                        if (etyList != null && UtilList.isEmpty(etyList.getLIST())) {
                            mMapList.clear();
                            mAdapter.setList(mMapList);
                            mAdapter.notifyDataSetChanged();
                            mLlNoGoods.setVisibility(View.VISIBLE);
                        }
                        LogUtils.e("onSuccess===列表=======2====>" + etyList.getLIST().size());
                    }
                    //加载框消失
                    break;
                default:
                    break;
            }
        } else {
            //加载框消失
            LogUtils.e("onSuccess======Aty=========>" + info);
        }

    }


    @Override
    public void onFailure(EtySendToUI error) {
        if (error != null) {
            switch (error.getTag()) {
                case AffConstans.BUSINESS.TAG_AD_STOREADLIST://轮播图
//                    LogUtils.e("onFailure===shop===轮播图========>" + error.getInfo());
                    break;
                case AffConstans.BUSINESS.TAG_GOODS_LIST://列表
                    LogUtils.e("onSuccess===shop===etyList.getLIST()==1=====>");
                    //隐藏加载框
                    dissProDialog();
                    isPage = false;//恢复获取分页事件
                    if (pageNum > 1) {
                        pageNum--;
                    }
                    LogUtils.e("onFailure===shop===列表=======>" + error.getInfo());
                    break;

                default:
//                    LogUtils.e("onFailure===default===========>" + error);
                    break;
            }
        } else {
//            LogUtils.e("onFailure======AtyHome=========>" + error);
        }
        //加载框消失
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    //顶部搜索按钮
    @OnClick(R.id.top_btn_rightSearch)
    public void onBtnSearchClick(View v) {
        mBaseUtilAty.startActivity(AtySearch.class);
    }

    @Override
    public void onClick(View v) {
//        LogUtils.e("onClick====AtyAffordShop===========>");
    }

    @OnClick(R.id.backbutton)//快速返回顶部
    public void onBackButtonclick(View v) {
//        LogUtils.e("onBackButtonclick=======pageNum======>" + pageNum);
        if (!mGridview.isStackFromBottom()) {
            mGridview.setSelection(0);
            mGridview.setAdapter(mAdapter);
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
     * @param
     * @return void
     * @throws
     * @Description: 创建动画层
     */
    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
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
//        LogUtils.e("onStop============mListMapCategory============>" + mListMapCategory);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mMapList != null) {
            mMapList.clear();
        }
        if (mListMapCategory.size() != 0) {
            mListMapCategory.clear();
        }
        isSelf = 0;

        super.onDestroy();
    }


    @Override
    public int getButtonType() {
        return 1;
    }
}
