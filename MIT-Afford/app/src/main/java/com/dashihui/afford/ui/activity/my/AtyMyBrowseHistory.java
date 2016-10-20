package com.dashihui.afford.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.sqlite.SqliteBrowseHistory;
import com.dashihui.afford.thirdapi.greedsqlite.BrowseHistory;
import com.dashihui.afford.ui.activity.AtyHome;
import com.dashihui.afford.ui.activity.AtyShoppingCart;
import com.dashihui.afford.ui.adapter.AdapterBrowseHistory;
import com.dashihui.afford.ui.widget.WgtAlertDialog;
import com.dashihui.afford.util.toast.UtilToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

public class AtyMyBrowseHistory extends BaseActivity {
    @ViewInject(R.id.listView)
    private ListView mListView;
    @ViewInject(R.id.aty_browseHistory_layout)
    private LinearLayout mBrowseHistoryLyout;//购物车没有物品时显示
    @ViewInject(R.id.title)
    private TextView mTitle;
    @ViewInject(R.id.top_btn_right)
    private ImageButton mClearHistory;
    private SqliteBrowseHistory mSqlitBrowseHistory;

    private AdapterBrowseHistory mAdapterBrowseHistory;
    private List<BrowseHistory> mBrowseHistoryList;
    private Intent mIntent;

    @ViewInject(R.id.iv_add_cart)
    private ImageView mShopCart;//底部购物车
    @ViewInject(R.id.nums)
    private TextView mTvNums;//购物车小数字
    private int mNum = 0;

    private ViewGroup anim_mask_layout;//动画层


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_my_browsehistory);
        ViewUtils.inject(this);
        mShopCart.setVisibility(View.GONE);
        mTvNums.setVisibility(View.GONE);
        mClearHistory.setVisibility(View.VISIBLE);
        mClearHistory.setImageResource(R.drawable.browse_history__clear);
        mSqlitBrowseHistory = new SqliteBrowseHistory();
        mTitle.setText("浏览历史");

    }

    @Override
    protected void onResume() {
        if (AffordApp.getInstance().getEntityLocation() != null) {
            mBrowseHistoryList = SqliteBrowseHistory.getInstance(this).getListBrowseHistoryByShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
            mAdapterBrowseHistory = new AdapterBrowseHistory(this, mBrowseHistoryList);
            mListView.setAdapter(mAdapterBrowseHistory);
            //判断查询出来的ShopCartList的size是否为0，为0时显示购物车为空的页面,隐藏底部栏和listview
            if (mBrowseHistoryList.size() <= 0) {
                mShopCart.setVisibility(View.GONE);
                mBrowseHistoryLyout.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
                mClearHistory.setVisibility(View.GONE);
            } else {
                mShopCart.setVisibility(View.VISIBLE);
                mBrowseHistoryLyout.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                mClearHistory.setVisibility(View.VISIBLE);
            }
        } else {
            finish();
        }
        super.onResume();
    }

    @OnClick(R.id.go_browseHistory)//购物车没有商品时“立即逛逛”按钮
    public void onTvBtnGoShoppingClick(View v) {
        mBaseUtilAty.startActivity(AtyHome.class);
    }

    @OnClick(R.id.ibtnBack)//顶部返回按钮
    public void onIBtnBackClick(View v) {
        onBackPressed();
    }

    @OnClick(R.id.top_btn_right) //清空浏览历史
    public void onClearHistoryClick(View v) {
        final WgtAlertDialog mAtDialog = new WgtAlertDialog();
        mAtDialog.show(this, "取消", "确定", "清空浏览历史？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAtDialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空浏览历史
                SqliteBrowseHistory.getInstance(AtyMyBrowseHistory.this).deleteBrowseHistory();
                mBrowseHistoryList.clear();
                mAdapterBrowseHistory.notifyDataSetChanged();
                UtilToast.show(AtyMyBrowseHistory.this, "浏览历史已清空！", Toast.LENGTH_SHORT);
                mAtDialog.dismiss();

                mBrowseHistoryList = SqliteBrowseHistory.getInstance(AtyMyBrowseHistory.this).getListBrowseHistoryByShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
                //判断查询出来的ShopCartList的size是否为0，为0时显示购物车为空的页面,隐藏底部栏和listview
                if (mBrowseHistoryList.size() <= 0) {
//                    mShopCart.setVisibility(View.GONE);
                    mBrowseHistoryLyout.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                    mClearHistory.setVisibility(View.GONE);
                } else {
//                    mShopCart.setVisibility(View.VISIBLE);
                    mBrowseHistoryLyout.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mClearHistory.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    public void onSuccess(EtySendToUI beanSendUI) {

    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {

    }

    /**
     * 底部购物车监听事件
     * @param v
     */
    @OnClick(R.id.rlyt_shopcart)
    public void onRlytShopCartClick(View v){
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
                mNum++;
                mTvNums.setVisibility(View.VISIBLE);
                mTvNums.setText(mNum + "");
                view1.setVisibility(View.INVISIBLE);
                //发送广播更新底部购物车显示
                sendShopChartBroadcast();
            }
        });
    }
}
