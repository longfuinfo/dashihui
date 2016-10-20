package com.dashihui.afford.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessShop;
import com.dashihui.afford.business.entity.EtyList;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.sqlite.SqliteShoppingCart;
import com.dashihui.afford.thirdapi.greedsqlite.ShoppingCart;
import com.dashihui.afford.ui.adapter.AdapterSpecialList;
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

public class AtyHomeSpecialList extends BaseActivity {

    /**
     * 返回按钮，编辑框，搜索按钮，取消按钮
     */

    @ViewInject(R.id.pulltorefresh_listview)
    private PullToRefreshListView mPullListView;
    @ViewInject(R.id.iv_add_cart)
    private ImageView mShopCart;//底部购物车
    @ViewInject(R.id.nums)
    private TextView mTvNums;//购物车小数字

    @ViewInject(R.id.top_title)
    private TextView mTvTopTitle;
//    private int mNum = 0;
    private int mTotalPage = 1;
    private boolean isPage = false;
    private int mPageNums = 1;//商品请求的页面

    private AdapterSpecialList mAdapterSpecialList;
    private List<Map<String, Object>> mListMap;

    private ViewGroup anim_mask_layout;//动画层
    private Activity mContext;
    private BusinessShop mBllShop;
//    private List<ShoppingCart> mShopCartList;
    private String mTagCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_special);
        ViewUtils.inject(this);
        mBllShop = new BusinessShop(this);
        mListMap = new ArrayList<>();

        Intent intent = getIntent();
        mTagCode = intent.getStringExtra(AtyHome.TAGCODE);
        String tagName = intent.getStringExtra(AtyHome.TAGNAME);
        mTvTopTitle.setText(tagName + "");
        showProDialog(AtyHomeSpecialList.this);
        mBllShop.goodsListByTag(mTagCode, mPageNums + "");
        initView();
    }

    @Override
    protected void onResume() {
        showShopCartNum();
        super.onResume();
    }

    /**
     * 初始化视图
     */
    protected void initView() {
        mShopCart.setVisibility(View.VISIBLE);

        mContext = AtyHomeSpecialList.this;

        mAdapterSpecialList = new AdapterSpecialList(this, mListMap);
        mPullListView.setAdapter(mAdapterSpecialList);
        mPullListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mPullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                // 隐藏加载更多圈 顶部
                mPullListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullListView.onRefreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //加载更多
                if (mTotalPage > 0 && mTotalPage > mPageNums) {
                    //获取商家列表
                    mPageNums++;
                    showProDialog(AtyHomeSpecialList.this);
                    mBllShop.goodsListByTag(mTagCode, mPageNums + "");
                    //请求服务商家列表
                } else {
                    // 隐藏加载更多圈 底部
                    mPullListView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPullListView.onRefreshComplete();
                        }
                    }, 1000);
                }
            }
        });
    }


    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_GOODS_LISTBYTAG://
                    dissProDialog();
                    EtyList etyList = (EtyList) beanSendUI.getInfo();
                    if (mPageNums == 1){
                        mListMap.clear();
                    }
                    if (etyList != null && !UtilList.isEmpty(etyList.getLIST())) {
                        mTotalPage = etyList.getTOTALPAGE();
                        mListMap.addAll(etyList.getLIST());
                        mAdapterSpecialList.setList(mListMap);
                        mAdapterSpecialList.notifyDataSetChanged();
                    } else {
                        LogUtils.e("onSuccess===列表===========>" + beanSendUI.getInfo());
                    }
                    mPullListView.onRefreshComplete();
                    break;
                default:
                    LogUtils.e("onSuccess===default===========>" + beanSendUI);

                    break;
            }
        } else {
            LogUtils.e("onSuccess===============>" + beanSendUI);
        }
    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_GOODS_LISTBYTAG://
                    dissProDialog();
                    break;
                default:
                    LogUtils.e("onFailure===default===========>" + beanSendUI);
                    break;
            }
        } else {
            LogUtils.e("onFailure===============>" + beanSendUI);
        }
    }

    /**
     * 查询购物车商品数量
     */
    public void showShopCartNum() {
        int shopCartNum = 0;
        if(AffordApp.getInstance().getEntityLocation()!=null && AffordApp.getInstance().getEntityLocation().getSTORE()!=null){
            List<ShoppingCart> shopCartList = SqliteShoppingCart.getInstance(this).getListByShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
            if (!UtilList.isEmpty(shopCartList)){
                for (int k =0;k<shopCartList.size();k++){
                    shopCartNum += UtilNumber.IntegerValueOf(shopCartList.get(k).getBuynum());
                }
                mTvNums.setVisibility(View.VISIBLE);
                mTvNums.setText(UtilNumber.IntegerValueOf(shopCartNum + "") + "");
            } else {
                LogUtils.i("购物车添加err===============>" + shopCartNum);
                mTvNums.setVisibility(View.GONE);
            }
        }

    }

    @OnClick(R.id.left_back)
    public void onLeftBack(View v){
        onBackPressed();
    }

    /**
     * 底部购物车监听事件
     *
     * @param v
     */
    @OnClick(R.id.rlyt_shopcart)
    public void onRlytShopCartClick(View v) {
        mBaseUtilAty.startActivity(AtyShoppingCart.class);
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

        mTvNums.getLocationInWindow(endLocation);// mImgShopCart是购物车图标/数字小图标

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
                mTvNums.setVisibility(View.VISIBLE);
//                mNum++;
//                mTvNums.setText(mShopCartList.size() + mNum + "");
                showShopCartNum();
                view1.setVisibility(View.INVISIBLE);
                //发送广播更新底部购物车显示
                sendShopChartBroadcast();
            }
        });
    }
}
