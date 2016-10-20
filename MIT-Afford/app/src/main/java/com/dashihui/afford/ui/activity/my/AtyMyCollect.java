package com.dashihui.afford.ui.activity.my;

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
import android.widget.Toast;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessUser;
import com.dashihui.afford.business.entity.EtyList;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.sqlite.SqliteShoppingCart;
import com.dashihui.afford.thirdapi.greedsqlite.ShoppingCart;
import com.dashihui.afford.ui.activity.AtyHome;
import com.dashihui.afford.ui.activity.AtyShoppingCart;
import com.dashihui.afford.ui.adapter.AdapterCollect;
import com.dashihui.afford.ui.widget.WgtAlertDialog;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.number.UtilNumber;
import com.dashihui.afford.util.toast.UtilToast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AtyMyCollect extends BaseActivity {
    @ViewInject(R.id.right_edit)
    private TextView mTvEdit;//编辑
    @ViewInject(R.id.listView_collect)
    private PullToRefreshListView mListView;//
    @ViewInject(R.id.tvBtn_add_shopcart)
    private TextView mTvBtnAddShopCart;//加入购物车
    @ViewInject(R.id.bottom_collect)
    private LinearLayout mLytBottom;//编辑时底部隐藏/显示

    @ViewInject(R.id.aty_shopcart_noshopping_layout)
    private LinearLayout mNoShoppingLyout;//没有物品时显示

    private AdapterCollect mAdapterCollect;
    private List<Map<String, Object>> mListMap;
    private BusinessUser mBllUser;

    private Map<String, Object> mapObject;
    private boolean flag = false;//点击编辑的flag

    private int mPageNums = 1;//商品请求的页面
    private int totalPage = 0;//总页数

    private EtyList listObjects;
    private List<Map<String, Object>> listMaps;
    private List<Map<String, Object>> mDellistMaps = new ArrayList<>();
    private int mPos;//选中收藏项对应的下标
    private List<Map<String, Object>> mRemoveListMaps;//把选择取消收藏的商品放入一个集合
//    private String goodsID="";//取消收藏的ID
//    private String[] mGoodsId;//取消收藏的集合ID

    @ViewInject(R.id.iv_add_cart)
    private ImageView mShopCart;//底部购物车
    @ViewInject(R.id.nums)
    private TextView mTvNums;//购物车小数字
    private int mNum = 0;

    private ViewGroup anim_mask_layout;//动画层

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_my_collected);
        ViewUtils.inject(this);//依赖注入
        mLytBottom.setVisibility(View.GONE);
        mShopCart.setVisibility(View.GONE);
//        showShopCartNum();

        mBllUser = new BusinessUser(this);
        mListMap = new ArrayList<>();
        onItemClickable();
        mAdapterCollect = new AdapterCollect(this, mListMap);
        mListView.setAdapter(mAdapterCollect);

        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //加载更多
                if (totalPage > 0 && totalPage > mPageNums) {
                    //获取商品列表，第二个参数：优惠类型	1：普通，2：推荐，3：限量，4：一元购
                    mPageNums++;
                    mBllUser.collectionlist(mPageNums + "");
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


    @Override
    protected void onResume() {
        mListMap.clear();
        mBllUser.collectionlist(mPageNums + "");
        super.onResume();
    }

    @Override
    public void onSuccess(EtySendToUI info) {
        LogUtils.e("onSuccess===============>" + info);
        if (info != null) {
            switch (info.getTag()) {
                case AffConstans.BUSINESS.TAG_USER_COLLECTIONLIST://关注商品列表
                    listObjects = (EtyList) info.getInfo();
                    listMaps = (List<Map<String, Object>>) listObjects.getLIST();
                    LogUtils.e("onSuccess=======listObjects========>" + listObjects);
                    LogUtils.e("onSuccess=======listObjects.getLIST()========>" + listObjects.getLIST());
                    if (listMaps.size() <= 0) {
                        mNoShoppingLyout.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                        mTvEdit.setVisibility(View.GONE);
                        mShopCart.setVisibility(View.GONE);
//                      showShopCartNum();
                    } else {
                        mNoShoppingLyout.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                        mTvEdit.setVisibility(View.VISIBLE);
//                        mShopCart.setVisibility(View.VISIBLE);
//                        showShopCartNum();
                    }
                    collectList();
                    break;
                case AffConstans.BUSINESS.TAG_USER_CANCELCOLLECT://取消关注
                    cancelCollectSuccess();//取消收藏成功、刷新数据
//                    mBllUser.collectionlist(mPageNums + "");
                    UtilToast.show(AtyMyCollect.this, "取消成功", Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onFailure(EtySendToUI error) {
        LogUtils.e("onFailure====AtyAffordShop===========>" + error);
        if (error != null) {
            switch (error.getTag()) {
                case AffConstans.BUSINESS.TAG_USER_COLLECTIONLIST://收藏商品列表失败
                    mNoShoppingLyout.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                    break;
                case AffConstans.BUSINESS.TAG_USER_CANCELCOLLECT://取消关注失败
                    LogUtils.e("onFailure=========取消关注失败==>");
                    if (mDialog == null) {
                        mDialog = new WgtAlertDialog();
                    }
                    mDialog.show(this,
                            "确定",
                            error.getInfo().toString(),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialog.dismiss();
                                    AtyMyCollect.this.finish();
                                }
                            });
                    break;
                default:
                    break;
            }
        }
    }


    //显示没有收藏物品时界面
    public void visiableNoCollect() {
        if (mListMap.size() <= 0) {
            mNoShoppingLyout.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            mTvEdit.setVisibility(View.GONE);
        } else {
            mNoShoppingLyout.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mTvEdit.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化可编辑状态时ListView的Item可点击
     */
    public void onItemClickable() {
        mTvEdit.setText("编辑");
        flag = true;
    }

    //顶部返回按钮
    @OnClick(R.id.left_back_collect)
    public void onIBtnBackClick(View v) {
        onBackPressed();
    }


    @OnClick(R.id.go_shopping)//购物车没有商品时“立即逛逛”按钮
    public void onTvBtnGoShoppingClick(View v) {
        mBaseUtilAty.startActivity(AtyHome.class);
    }

    /**
     * 编辑按钮事件
     *
     * @param v
     */
    @OnClick(R.id.right_edit)
    public void onTvRightEditClick(View v) {

        if (flag == true) {
            mTvEdit.setText("完成");
            mShopCart.setVisibility(View.GONE);
            mTvNums.setVisibility(View.GONE);
            mLytBottom.setVisibility(View.VISIBLE);
            flag = false;
            mAdapterCollect.setItemVisible(true);
        } else {
            mTvEdit.setText("编辑");
//            mShopCart.setVisibility(View.VISIBLE);
//            showShopCartNum();
            mLytBottom.setVisibility(View.GONE);
            flag = true;
            mAdapterCollect.setItemVisible(false);
        }
    }

    /**
     * 当购物车图标数字大于0时显示红色数字提示
     */
    public void showShopCartNum() {
        if (mNum > 0) {
            mTvNums.setVisibility(View.VISIBLE);
        } else {
            mTvNums.setVisibility(View.GONE);
        }
    }


    //加入购物车
    public void onTvBtnAddClick(View v) {
        mTvBtnAddShopCart.setEnabled(false);
        if (mAdapterCollect != null && mAdapterCollect.getPositon() >= 0) {
            addShoppingCart();//加入购物车的数据操作
        } else {
            UtilToast.show(AtyMyCollect.this, "您还没有选择商品！", Toast.LENGTH_SHORT);
            //取消控件
            mTvBtnAddShopCart.setEnabled(true);
        }
    }

    public boolean setIsTrue(){
        boolean isTrue = false;
        boolean[] isCheckeds = mAdapterCollect.getmIsChecked();
        for (int i = 0; i < isCheckeds.length; i++){
            if (isCheckeds[i]){
                isTrue = true;
            }else{

            }
        }
        return isTrue;
    }

    //加入购物车的数据操作
    public void addShoppingCart() {
        boolean[] isCheckeds = mAdapterCollect.getmIsChecked();
        boolean isCheck;
        isCheck = setIsTrue();
        if (isCheckeds != null && isCheckeds.length > 0 && isCheck) {
            LogUtils.e("addShoppingCart========isCheckeds.length==========>" + isCheck);
            for (int i = 0; i < isCheckeds.length; i++) {
                if (isCheckeds[i] && !UtilList.isEmpty(mListMap)) {
                    ShoppingCart _ShopCart = SqliteShoppingCart.getInstance(AtyMyCollect.this).getShoppingCartByUid(mListMap.get(i).get("ID") + "");
                    if (_ShopCart != null) {
                        int _ShopNums = UtilNumber.IntegerValueOf(_ShopCart.getBuynum());
                        LogUtils.e("购物车已有数据===============>" + _ShopNums);
                        addShopCart(mListMap.get(i), ++_ShopNums);
                    } else {
                        LogUtils.e("购物车第一次添加数据===============>" + _ShopCart);
                        addShopCart(mListMap.get(i), 1);
                    }
                } else {
                    LogUtils.e("mListMap====isEmpty===========>" + mListMap);
                }
            }
            UtilToast.show(AtyMyCollect.this, "加入购物车成功！", Toast.LENGTH_SHORT);
            mAdapterCollect.notifyDataSetChanged();
            sendShopChartBroadcast();
        } else {
            UtilToast.show(AtyMyCollect.this, "您还没有选择商品！", Toast.LENGTH_SHORT);
        }
        //取消控件
        mTvBtnAddShopCart.setEnabled(true);
    }

    //取消关注
    public void onTvBtnDeleteClick(View v) {
        if (mAdapterCollect != null && mAdapterCollect.getPositon() >= 0) {
            cancelCollect();//选择取消多个关注项
        } else {
            UtilToast.show(AtyMyCollect.this, "您还没有选择商品！", Toast.LENGTH_SHORT);
        }


    }

    //选择取消多个关注项
    public void cancelCollect() {
        boolean[] isCheckeds = mAdapterCollect.getmIsChecked();
        String goodsID = "";
        boolean isCheck;
        isCheck = setIsTrue();
        if (isCheckeds != null && isCheckeds.length > 0 && isCheck) {
            LogUtils.e("length===================>" + isCheckeds.length);

            for (int i = 0; i < isCheckeds.length; i++) {
                if (isCheckeds[i] && !UtilList.isEmpty(mListMap)) {
                    mDellistMaps.add(mListMap.get(i));
                    if (i == (isCheckeds.length - 1)) {
                        goodsID += mListMap.get(i).get("ID") + "";
                    } else {
                        goodsID += mListMap.get(i).get("ID") + ",";
                    }
                } else {
                    LogUtils.e("mListMap====isEmpty===========>" + mListMap);
                }
            }
            mBllUser.cancelcollect(goodsID.trim());//点击确定，提交服务器取消请求
        } else {
            UtilToast.show(AtyMyCollect.this, "您还没有选择商品！", Toast.LENGTH_SHORT);
        }
    }

    /**
     * //收藏商品列表
     */
    private void collectList() {
        if (!UtilList.isEmpty(listMaps)) {
            totalPage = listObjects.getTOTALPAGE();
            LogUtils.e("onSuccess========totalPage=======>" + totalPage);
            mListMap.addAll(listMaps);
            LogUtils.e("onSuccess========size=======>" + mListMap.size());
            mAdapterCollect.setList(mListMap);
            //初始化每个item checked是否显示数组
            mAdapterCollect.initCheckData();
            mAdapterCollect.notifyDataSetChanged();
        } else {
            LogUtils.e("onSuccess======null====mapObject====>" + mapObject);
        }
    }

    /**
     * 取消收藏成功、刷新数据
     */
    public void cancelCollectSuccess() {

        //取消收藏成功后刷新listview
//        boolean[] isCheckeds = mAdapterCollect.getmIsChecked();
//        LogUtils.e("cancelCollect=========mAdapterCollect.getPositon()=======>"+mAdapterCollect.getPositon());
//        LogUtils.e("cancelCollectSuccess=========isCheckeds========>" + isCheckeds);
        mListMap.removeAll(mDellistMaps);

        mAdapterCollect.setList(mListMap);
        //初始化每个item checked是否显示数组
        mAdapterCollect.initCheckData();
        //隐藏item选择框
        mAdapterCollect.setItemVisible(false);
        mTvEdit.setText("编辑");
        mLytBottom.setVisibility(View.GONE);
        flag = false;
        mAdapterCollect.notifyDataSetChanged();

//        visiableNoCollect();
        if (mListMap.size() <= 0) {
            mNoShoppingLyout.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            mTvEdit.setVisibility(View.GONE);
        } else {
            mNoShoppingLyout.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mTvEdit.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 向数据库添加数据,如果数据库已经存在此商品数据，则修改商品数量
     *
     * @param stringMap
     * @param num
     */
    private void addShopCart(Map<String, Object> stringMap, int num) {
        if (stringMap == null) {
            LogUtils.e("null===============>" + stringMap);
            return;
        }
        ShoppingCart _shoppingCart = new ShoppingCart();

        String isSelf = stringMap.get("ISSELF") + "";
        //点击加入购物车将数据保存至本地数据库
        _shoppingCart.setID(stringMap.get("ID")  + "");
        //如果是自营
        if ("1".equals(isSelf)){
            _shoppingCart.setShopID(CommConstans.SELF_TYPE);
        }else {
            _shoppingCart.setShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
        }

        _shoppingCart.setName(stringMap.get("NAME") + "");
        _shoppingCart.setSpec(stringMap.get("SPEC") + "");
        _shoppingCart.setShortinfo(stringMap.get("SHORTINFO") + "");
        _shoppingCart.setMarketprice(stringMap.get("MARKETPRICE") + "");
        _shoppingCart.setImgsrc(stringMap.get("THUMB") + "");

        //是否是自营商品：0：不是，1：是。
        _shoppingCart.setShoptype(stringMap.get("ISSELF") + "");
        _shoppingCart.setUrv(stringMap.get("URV") + "");
        _shoppingCart.setType(stringMap.get("TYPE") + "");
        _shoppingCart.setBuynum(num + "");
        _shoppingCart.setSellprice(stringMap.get("SELLPRICE") + "");
        _shoppingCart.setIschoose(true);
        //保存到本地数据库
        SqliteShoppingCart.getInstance(this).update(stringMap.get("ID") + "", _shoppingCart);
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
