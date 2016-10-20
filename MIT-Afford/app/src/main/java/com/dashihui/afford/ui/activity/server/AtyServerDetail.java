package com.dashihui.afford.ui.activity.server;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.business.entity.EtyServerDetail;
import com.dashihui.afford.common.base.AffRequestCallBack;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.sqlite.SqliteBrowseHistory;
import com.dashihui.afford.thirdapi.greedsqlite.BrowseHistory;
import com.dashihui.afford.ui.activity.AtyLogin;
import com.dashihui.afford.ui.activity.fragment.FragmentServerDetailNextPager;
import com.dashihui.afford.ui.activity.fragment.FragmentServerDetailPager;
import com.dashihui.afford.ui.activity.shop.AtyAffordShop;
import com.dashihui.afford.ui.widget.WgtAlertDialog;
import com.dashihui.afford.ui.widget.WgtDragLayout;
import com.dashihui.afford.util.map.UtilMap;
import com.dashihui.afford.util.number.UtilNumber;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.HashMap;
import java.util.Map;

public class AtyServerDetail extends FragmentActivity implements AffRequestCallBack {

    public final static String RETURN_ACTIVITY = "returnActivity";
    public final static String TITLE = "title";
    public final static String SELLPRICE = "sellprice";
    public final static String GOODSID = "goodsID";
    public final static String STOREID = "storeID";
    public static final String NAME = "name";
    public static Map<String, Object> mItemMap = new HashMap<>();
    @ViewInject(R.id.draglayout)
    public WgtDragLayout mWgtDragLayout;

    private WgtAlertDialog mDialog;
    private FragmentServerDetailPager mFragServerDetailPager;
    private FragmentServerDetailNextPager mFragServerDetailNextPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_server_detail);
        ViewUtils.inject(this);//依赖注入
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 初始化View
     */
    private void initView() {
        mFragServerDetailPager = FragmentServerDetailPager.newInstance(AtyServerDetail.this,0);
        mFragServerDetailNextPager = FragmentServerDetailNextPager.newInstance(AtyServerDetail.this,0);
        getSupportFragmentManager().beginTransaction().add(R.id.first_framelayout, mFragServerDetailPager).add(R.id.second_framelayout, mFragServerDetailNextPager).commit();
        WgtDragLayout.ShowNextPageNotifier nextPageNotifier = new WgtDragLayout.ShowNextPageNotifier() {
            @Override
            public void onDragNext() {
                mFragServerDetailNextPager.initView();
                LogUtils.e("========initView=======>");
            }
        };
        mWgtDragLayout.setNextPageListener(nextPageNotifier);
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {

    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {

    }

//    /**
//     * 刷新高度
//     */
//    public void initWgtHeighet(){
//        WgtDragLayout.LayoutParams sp_params = new WgtDragLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        LogUtils.e("mContext========initView=======>"+mWgtDragLayout);
//        mWgtDragLayout.setLayoutParams(sp_params);
//    }

    /**
     * 立即预约
     *
     * @param v
     */
    @OnClick(R.id.join_server)
    public void onJoninClick(View v) {
        if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance().getEntityLocation().getSTORE() != null) {
            int communtyId = UtilNumber.IntegerValueOf(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
            LogUtils.e("====去结算====定位=======>" + communtyId);
            if (communtyId == CommConstans.LOCCOMMUNITY.COMMUNITYID) {
                if (mDialog == null) {
                    mDialog = new WgtAlertDialog();
                }
                mDialog.show(this,
                        "确定",
                        "您所在的小区暂未开通服务，敬请期待！",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.dismiss();
                            }
                        }, false, false);
            } else {
                if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance().getEntityLocation().getSTORE() != null) {
                    if (mItemMap == null) {
                        LogUtils.e("null=======mItemMap========>" + mItemMap);
                        //没有选择时默认选择第一项
                        if (!UtilMap.isEmpty(mItemMap)) {
                            Intent intent = new Intent(AtyServerDetail.this, AtyServerSettlement.class);
                            intent.putExtra(TITLE, mItemMap.get("TITLE") + "");
                            intent.putExtra(SELLPRICE, mItemMap.get("SELLPRICE") + "");
                            intent.putExtra(GOODSID, mItemMap.get("ID") + "");
                            intent.putExtra(STOREID, AffordApp.getInstance().getEntityLocation().getSTORE().getID());
                            startActivity(intent);
                        }
                    } else {
                        String title = mItemMap.get("TITLE") + "";
                        String sellprice = mItemMap.get("SELLPRICE") + "";
                        String goodsID = mItemMap.get("ID") + "";
                        Intent intent = new Intent(AtyServerDetail.this, AtyServerSettlement.class);
                        intent.putExtra(TITLE, title);
                        intent.putExtra(SELLPRICE, sellprice);
                        intent.putExtra(GOODSID, goodsID);
                        intent.putExtra(STOREID, AffordApp.getInstance().getEntityLocation().getSTORE().getID());
                        startActivity(intent);
                    }
                } else {
                    if (mDialog == null) {
                        mDialog = new WgtAlertDialog();
                    }
                    mDialog.show(this,
                            "确定",
                            "请重新登录！",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialog.dismiss();
                                    startActivity(new Intent(AtyServerDetail.this,AtyLogin.class));
                                    AtyServerDetail.this.finish();
                                }
                            });
                }
            }
        }
    }

    /**
     * 返回
     *
     * @param v
     */
    @OnClick(R.id.left_ibtnBack)
    public void onBtnBackClick(View v) {
        onBackPressed();
    }


    /**
     * 添加浏览历史
     */
    private void addBrowseHistory(EtyServerDetail objectMap) {
        BrowseHistory _browseHistory = SqliteBrowseHistory.getInstance(AtyServerDetail.this).getBrowseHistoryByUid(objectMap.getID() + "");
        if (_browseHistory != null) {
            int _HistoryNums = UtilNumber.IntegerValueOf(_browseHistory.getHistorynum());
            LogUtils.e("浏览历史已有数据===============>" + _HistoryNums);
            addBrowseHistory(objectMap, ++_HistoryNums);
        } else {
            LogUtils.e("浏览历史库第一次添加数据===============>" + _browseHistory);
            addBrowseHistory(objectMap, 1);
        }
    }

    /**
     * 向数据库添加数据,如果数据库已经存在此商品数据，则修改浏览次数数量
     *
     * @param objectMap
     * @param num
     */
    private void addBrowseHistory(EtyServerDetail objectMap, int num) {
        if (objectMap == null) {
            LogUtils.e("null===============>" + objectMap);
            return;
        }
//        BrowseHistory _browseHistory = new BrowseHistory();
//        //点击加入购物车将数据保存至本地数据库
//        _browseHistory.setID(objectMap.getID() + "");
//        _browseHistory.setShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
//        _browseHistory.setName(objectMap.getNAME() + "");
//        _browseHistory.setSpec(objectMap.getSPEC() + "");
//        _browseHistory.setShortinfo(objectMap.getSHORTINFO() + "");
//        _browseHistory.setMarketprice(objectMap.getMARKETPRICE() + "");
//        if (!UtilList.isEmpty(objectMap.getIMAGES())) {
//            _browseHistory.setImgsrc(objectMap.getIMAGES().get(0) + "");
//        } else {
//            _browseHistory.setImgsrc("");
//        }
//        _browseHistory.setUrv(objectMap.getURV() + "");
//        _browseHistory.setType(objectMap.getTYPE() + "");
//        _browseHistory.setHistorynum(num + "");
//        _browseHistory.setSellprice(objectMap.getSELLPRICE() + "");
//        //保存到本地数据库
//        SqliteBrowseHistory.getInstance(this).update(objectMap.getID() + "", _browseHistory);

    }


    @Override
    public void onBackPressed() {
        LogUtils.e("onFailure======AtyAffordShopDetail=========>" + getIntent().getStringExtra(RETURN_ACTIVITY));
        //TODO 此处因为带底部菜单的Activity需要重新加载才能刷新购物车上的数字。
        String reActivity = getIntent().getStringExtra(RETURN_ACTIVITY);
        Intent intent = null;
        if (CommConstans.SHOPDETAIL.RETURN_TAG0.equals(reActivity)) {
            intent = new Intent(this, AtyAffordShop.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
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


}
