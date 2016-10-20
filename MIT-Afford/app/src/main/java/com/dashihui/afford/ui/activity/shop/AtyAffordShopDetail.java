package com.dashihui.afford.ui.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessShop;
import com.dashihui.afford.business.BusinessUser;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.business.entity.EtyShopDetail;
import com.dashihui.afford.common.base.BaseAtyFragment;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.sqlite.SqliteBrowseHistory;
import com.dashihui.afford.sqlite.SqliteShoppingCart;
import com.dashihui.afford.thirdapi.greedsqlite.BrowseHistory;
import com.dashihui.afford.thirdapi.greedsqlite.ShoppingCart;
import com.dashihui.afford.ui.activity.AtyLogin;
import com.dashihui.afford.ui.activity.AtyShoppingCart;
import com.dashihui.afford.ui.activity.fragment.FragmentShopDetailNextPager;
import com.dashihui.afford.ui.activity.fragment.FragmentShopDetailPager;
import com.dashihui.afford.ui.widget.WgtDragLayout;
import com.dashihui.afford.ui.widget.WgtDragLayout.ShowNextPageNotifier;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.number.UtilNumber;
import com.dashihui.afford.util.string.UtilString;
import com.dashihui.afford.util.toast.UtilToast;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

public class AtyAffordShopDetail extends BaseAtyFragment {

    @ViewInject(R.id.tv_unread_count)
    private TextView mTxtViewShopCartNum;
    @ViewInject(R.id.detail_fav_text)
    private TextView mTxtCollect;
    @ViewInject(R.id.draglayout)
    private WgtDragLayout mWgtDragLayout;
    @ViewInject(R.id.detail_fav)
    private LinearLayout mLytCollect;

    private BusinessShop mShopBll;
    private EtyShopDetail mapObject;
    private BusinessUser mBllUSer;
    private ViewGroup anim_mask_layout;//动画层

    public final static String RETURN_ACTIVITY = "returnActivity";
    private boolean mIsCollected = false;
    public static boolean mIsClickCollected = false;

    private Handler mHandler;
    private FragmentShopDetailPager mFrgShopDetailPager;
    private FragmentShopDetailNextPager mFrgShopDetailNextPager;
    private String mGoodsId;
    private BitmapUtils mBitmapUtils;
    private int mIsself;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_affordshop_detail);
        ViewUtils.inject(this);//依赖注入
        mShopBll = new BusinessShop(this);
        mBitmapUtils = new BitmapUtils(this);
        mBllUSer = new BusinessUser(this);

        if (getIntent().getStringExtra(AtyAffordShop.INTENT_SHOP_ID) != null) {
            showProDialog(this);
            mShopBll.getGoodsDetail(getIntent().getStringExtra(AtyAffordShop.INTENT_SHOP_ID));
        }
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {

        mFrgShopDetailPager = new FragmentShopDetailPager();
        mFrgShopDetailNextPager = new FragmentShopDetailNextPager();
        getSupportFragmentManager().beginTransaction().add(R.id.first_framelayout, mFrgShopDetailPager).add(R.id.second_framelayout, mFrgShopDetailNextPager).commit();
        ShowNextPageNotifier nextPageNotifier = new ShowNextPageNotifier() {
            @Override
            public void onDragNext() {
                mFrgShopDetailNextPager.initView();
            }
        };
        mWgtDragLayout.setNextPageListener(nextPageNotifier);
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
//        LogUtils.i("onSuccess===AtyAffordShopDetail===详情页========>" + beanSendUI);
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_GOODS_DETAIL://商品详情
                    dissProDialog();
                    mapObject = (EtyShopDetail) beanSendUI.getInfo();
                    //添加浏览历史
                    addBrowseHistory(mapObject);
                    if (mapObject != null) {
                        mGoodsId = mapObject.getID() + "";
                        mIsself = mapObject.getISSELF();
                        if (mapObject.getISCOLLECTED() == 0) {
                            mTxtCollect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.btn_normal_concern), null, null);
                            mIsCollected = false;
                        } else {
                            mTxtCollect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.btn_press_concern), null, null);
                            mIsCollected = true;
                        }
                        //初始化购物车数量
                        changeShopCartNum();
                    } else {
//                        LogUtils.i("onSuccess======null====mapObject====>" + mapObject);
                        finish();
                    }
                    break;
                case AffConstans.BUSINESS.TAG_USER_DOCOLLECT://用户收藏
                    mTxtCollect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.btn_press_concern), null, null);
                    mIsCollected = !mIsCollected;
                    UtilToast.show(AtyAffordShopDetail.this, "关注成功！", Toast.LENGTH_SHORT);
                    mIsClickCollected = true;
                    mFrgShopDetailPager.isCollectClick();
                    mLytCollect.setEnabled(true);
                    break;
                case AffConstans.BUSINESS.TAG_USER_CANCELCOLLECT://用户取消收藏
                    mTxtCollect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.btn_normal_concern), null, null);
                    mIsCollected = !mIsCollected;
                    UtilToast.show(AtyAffordShopDetail.this, "关注取消！", Toast.LENGTH_SHORT);
                    mIsClickCollected = false;
                    mFrgShopDetailPager.isCollectClick();
                    mLytCollect.setEnabled(true);
                    break;

                default:
                    LogUtils.i("onSuccess===default===========>" + beanSendUI);
                    break;
            }
        } else {
            LogUtils.i("onSuccess======AtyAffordShopDetail======不可能的错误===>" + beanSendUI);
        }
    }


    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        switch (beanSendUI.getTag()) {
            case AffConstans.BUSINESS.TAG_GOODS_DETAIL://商品详情
                dissProDialog();
                break;
            case AffConstans.BUSINESS.TAG_USER_DOCOLLECT://用户收藏
                mLytCollect.setEnabled(true);
                break;
            case AffConstans.BUSINESS.TAG_USER_CANCELCOLLECT://用户取消收藏
                mLytCollect.setEnabled(true);
                break;
            default:
                LogUtils.i("onSuccess===default===========>" + beanSendUI);
                break;
        }
        LogUtils.i("onFailure======AtyAffordShopDetail=========>" + beanSendUI);
    }


    //图文详情,底部栏关注
    @OnClick({R.id.detail_pictext, R.id.detail_fav})
    public void onGoodsDetailClick(View v) {
        switch (v.getId()) {
            case R.id.detail_fav://点击关注
                if (AffordApp.getInstance().isLogin()) {
                    LogUtils.i("onGoodsDetailClick======mGoodsId=========>" + mGoodsId);
                    if (mIsCollected != false) {
                        mBllUSer.cancelcollect(mGoodsId);
                    } else {
                        mBllUSer.docollect(mGoodsId,mIsself+"");
                    }
                    mLytCollect.setEnabled(false);//禁止连续点击
                } else {
                    startActivity(new Intent(this, AtyLogin.class));
                }
                break;
            default:
                break;
        }
    }

    /**
     * 购物车
     *
     * @param v
     */
    @OnClick(R.id.detail_shopcart)
    public void onShoppingCartClick(View v) {//购物车
        Intent intent = new Intent(this, AtyShoppingCart.class);
        startActivity(intent);
        finish();
    }

    /**
     * 加入购物车
     *
     * @param v
     */
    @OnClick(R.id.detail_joinshopcart)
    public void onJoninClick(View v) {
        if (mapObject == null) {
            LogUtils.i("null===============>" + mapObject);
            return;
        }

        int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
        v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
        ImageView ball = new ImageView(this);// buyImg是动画的图片
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(40, 40);
        ball.setLayoutParams(layoutParams);
        if (!UtilList.isEmpty(mapObject.getIMAGES())) {
            String uri = AffConstans.PUBLIC.ADDRESS_IMAGE + mapObject.getIMAGES().get(0) + "";
            mBitmapUtils.display(ball,uri);
            //初始图片过大
//            Glide.with(this)
//                    .load(uri)
//                    .placeholder(R.drawable.cvs_btn_addshopcar)
//                    .error(R.drawable.cvs_btn_addshopcar)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(ball);
        } else {
            LogUtils.i("uri===图片地址====error=======>" + mapObject.getIMAGES());
        }
        setAnim(ball, startLocation);// 开始执行动画
    }

    /**
     * 返回
     * @param v
     */
    @OnClick(R.id.ibtnBack)
    public void onBtnBackClick(View v) {
        onBackPressed();
    }

    /**
     * 向数据库添加数据,如果数据库已经存在此商品数据，则修改商品数量
     *
     * @param objectMap
     * @param num
     */
    private void addShopCart(EtyShopDetail objectMap, int num) {
        if (objectMap == null) {
            LogUtils.i("null===============>" + objectMap);
            return;
        }
        ShoppingCart _shoppingCart = new ShoppingCart();

        String isSelf = objectMap.getISSELF() + "";
        //点击加入购物车将数据保存至本地数据库
        _shoppingCart.setID(objectMap.getID() + "");
        //如果是自营
        if ("1".equals(isSelf)){
            _shoppingCart.setShopID(CommConstans.SELF_TYPE);
        }else {
            _shoppingCart.setShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
        }

        _shoppingCart.setName(objectMap.getNAME() + "");
        _shoppingCart.setSpec(objectMap.getSPEC() + "");
        _shoppingCart.setShortinfo(objectMap.getSHORTINFO() + "");
        _shoppingCart.setMarketprice(objectMap.getMARKETPRICE() + "");
        String img = getIntent().getStringExtra(AtyAffordShop.INTENT_SHOP_IMG);
        if (!UtilString.isEmpty(img)) {
            String str = getIntent().getStringExtra(AtyAffordShopDetail.RETURN_ACTIVITY);
            if (CommConstans.SHOPDETAIL.RETURN_TAG2.equals(str)) {
                _shoppingCart.setImgsrc(objectMap.getTHUMB() + "");
            } else {
                _shoppingCart.setImgsrc(img + "");
            }

        } else {
            _shoppingCart.setImgsrc("");
        }
        //是否是自营商品：0：不是，1：是。
        _shoppingCart.setShoptype(objectMap.getISSELF() + "");
        _shoppingCart.setUrv(objectMap.getURV() + "");
        _shoppingCart.setType(objectMap.getTYPE() + "");
        _shoppingCart.setBuynum(num + "");
        _shoppingCart.setSellprice(objectMap.getSELLPRICE() + "");
        _shoppingCart.setIschoose(true);
        //保存到本地数据库
        SqliteShoppingCart.getInstance(this).update(objectMap.getID() + "", _shoppingCart);
        //改变购物车数量
        changeShopCartNum();

    }

    /**
     * 添加浏览历史
     */
    private void addBrowseHistory(EtyShopDetail objectMap) {
        BrowseHistory _browseHistory = SqliteBrowseHistory.getInstance(AtyAffordShopDetail.this).getBrowseHistoryByUid(objectMap.getID() + "");
        if (_browseHistory != null) {
            int _HistoryNums = UtilNumber.IntegerValueOf(_browseHistory.getHistorynum());
            LogUtils.i("浏览历史已有数据===============>" + _HistoryNums);
            addBrowseHistory(objectMap, ++_HistoryNums);
        } else {
            LogUtils.i("浏览历史库第一次添加数据===============>" + _browseHistory);
            addBrowseHistory(objectMap, 1);
        }
    }

    /**
     * 向数据库添加数据,如果数据库已经存在此商品数据，则修改浏览次数数量
     *
     * @param objectMap
     * @param num
     */
    private void addBrowseHistory(EtyShopDetail objectMap, int num) {
        LogUtils.i("addBrowseHistory==========添加浏览历史===objectMap=======>" + objectMap);
        if (objectMap == null) {
            LogUtils.i("null===============>" + objectMap);
            return;
        }
        BrowseHistory _browseHistory = new BrowseHistory();
        //点击加入购物车将数据保存至本地数据库
        _browseHistory.setID(objectMap.getID() + "");
        _browseHistory.setShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
        _browseHistory.setName(objectMap.getNAME() + "");
        _browseHistory.setSpec(objectMap.getSPEC() + "");
        _browseHistory.setShortinfo(objectMap.getSHORTINFO() + "");
        _browseHistory.setMarketprice(objectMap.getMARKETPRICE() + "");
//        _browseHistory.setImgsrc(objectMap.getIMAGES().get(0) + "");
//        if (!UtilList.isEmpty(objectMap.getIMAGES())) {
//            _browseHistory.setImgsrc(objectMap.getIMAGES().get(0) + "");
        if (objectMap.getTHUMB() != null) {
            LogUtils.i("addBrowseHistory====22======浏览历史图片=========>" + objectMap.getTHUMB());
            _browseHistory.setImgsrc(objectMap.getTHUMB());
        } else {
            _browseHistory.setImgsrc("");
        }
        //是否是自营商品：0：不是，1：是。
        _browseHistory.setShoptype(objectMap.getISSELF() + "");
        _browseHistory.setUrv(objectMap.getURV() + "");
        _browseHistory.setType(objectMap.getTYPE() + "");
        _browseHistory.setHistorynum(num + "");
        _browseHistory.setSellprice(objectMap.getSELLPRICE() + "");
        //保存到本地数据库
        SqliteBrowseHistory.getInstance(this).update(objectMap.getID() + "", _browseHistory);

    }

    /**
     * 改变购物车数量
     */
    private void changeShopCartNum() {

        int shopCartNum = 0;
        if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance().getEntityLocation().getSTORE() != null) {
            List<ShoppingCart> shopCartList = SqliteShoppingCart.getInstance(this).getListByShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
            if (!UtilList.isEmpty(shopCartList)) {
                for (int k = 0; k < shopCartList.size(); k++) {
                    shopCartNum += UtilNumber.IntegerValueOf(shopCartList.get(k).getBuynum());
                }
                mTxtViewShopCartNum.setVisibility(View.VISIBLE);
                mTxtViewShopCartNum.setText(UtilNumber.IntegerValueOf(shopCartNum + "") + "");
            } else {
                LogUtils.i("购物车添加err===============>" + shopCartNum);
                mTxtViewShopCartNum.setVisibility(View.GONE);
            }
        }
//        ShoppingCart newShopCart = SqliteShoppingCart.getInstance(this).getShoppingCartByUid(CommodityID + "");
//        if (newShopCart != null) {
//            LogUtils.i("购物车添加后最新数据===============>" + newShopCart.getBuynum());
//            mTxtViewShopCartNum.setVisibility(View.VISIBLE);
//            mTxtViewShopCartNum.setText(UtilNumber.IntegerValueOf(newShopCart.getBuynum() + "") + "");
//        } else {
//            LogUtils.i("购物车添加err===============>" + newShopCart);
//            mTxtViewShopCartNum.setVisibility(View.GONE);
//        }
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
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
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
        final View viewAnim = addViewToAnimLayout(anim_mask_layout, view1,
                startLocation);
        int[] endLocation = new int[2];// 存储动画结束位置的X、Y坐标

        mTxtViewShopCartNum.getLocationInWindow(endLocation);// mTxtViewShopCartNum是那个购物车数字小图标

        // 计算位移
        int endX = endLocation[0] - startLocation[0];// 动画位移的X坐标
        int endY = endLocation[1] - startLocation[1];// 动画位移的y坐标

        TranslateAnimation translateAnimationY = new TranslateAnimation(100, endX, -130, endY);
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
                ShoppingCart _ShopCart = SqliteShoppingCart.getInstance(AtyAffordShopDetail.this).getShoppingCartByUid(mapObject.getID() + "");
                if (_ShopCart != null) {
                    int _ShopNums = UtilNumber.IntegerValueOf(_ShopCart.getBuynum());
                    LogUtils.i("购物车已有数据===============>" + _ShopNums);
                    addShopCart(mapObject, ++_ShopNums);
                } else {
                    LogUtils.i("购物车第一次添加数据===============>" + _ShopCart);
                    addShopCart(mapObject, 1);
                }
                sendShopChartBroadcast();
            }
        });
    }

    /**
     * 底部购物车显示
     */
    protected void sendShopChartBroadcast() {
        int shopCartNum = 0;
        if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance().getEntityLocation().getSTORE() != null) {
            List<ShoppingCart> shopCartList = SqliteShoppingCart.getInstance(this).getListByShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
            if (!UtilList.isEmpty(shopCartList)) {
                for (int k = 0; k < shopCartList.size(); k++) {
                    shopCartNum += UtilNumber.IntegerValueOf(shopCartList.get(k).getBuynum());
                }

            }
        }
        LogUtils.i("BroadcastReceiver===============>" + shopCartNum);
        Intent intent = new Intent();  //Itent就是我们要发送的内容
        intent.putExtra("data", shopCartNum);
        intent.setAction(CommConstans.REGISTER.BROADCAST_INTENT_ACTION);   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
        sendBroadcast(intent);   //发送广播
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
}
