package com.dashihui.afford.ui.activity.fragment;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessShop;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.business.entity.EtyShopDetail;
import com.dashihui.afford.common.base.BaseFragment;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.ui.activity.AtyLogin;
import com.dashihui.afford.ui.activity.my.AtyMyMember;
import com.dashihui.afford.ui.activity.my.AtyMyMemberShare;
import com.dashihui.afford.ui.activity.shop.AtyAffordShop;
import com.dashihui.afford.ui.activity.shop.AtyAffordShopDetail;
import com.dashihui.afford.ui.adapter.AdapterAffordShopDetail;
import com.dashihui.afford.ui.widget.WgtScrollView;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.number.UtilNumber;
import com.dashihui.afford.util.string.UtilString;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

public class FragmentShopDetailPager extends BaseFragment {
    private static final String ARG_POSITION = "position";
    private static Context mContext;

    @ViewInject(R.id.detail_goodsname)
    private TextView mTextViewName;//详情页商品名
    @ViewInject(R.id.text_sale)
    private TextView mTvSale;//销售量
    @ViewInject(R.id.tv_collect)
    public TextView mTvCollect;//关注量
    @ViewInject(R.id.detail_new_goodsprice)
    private TextView mTextViewPrice;//商品优惠后价格
    @ViewInject(R.id.detail_old_goodsprice)
    private TextView mTextViewOldPrice;//商品原价
    @ViewInject(R.id.text_size)
    private TextView mTvSize;//商品规格
    @ViewInject(R.id.text_brand)
    private TextView mTvBrandName;//商品品牌
    @ViewInject(R.id.text_present)
    private TextView mTvPresent;//商品介绍
    @ViewInject(R.id.viewpager)
    private ViewPager mViewPager;
    @ViewInject(R.id.viewGroup)
    private LinearLayout mViewGroup;
    @ViewInject(R.id.bot_tvnum)
    private TextView mCurNum;
    @ViewInject(R.id.bot_num)
    private TextView mTotalNum;
    @ViewInject(R.id.custScrollView)
    private WgtScrollView mScrollView;

    @ViewInject(R.id.lyt_topdetail)
    private static LinearLayout mLytTopDetail;
    @ViewInject(R.id.lyt_nextdetail)
    private static LinearLayout mLytNextDetail;
    @ViewInject(R.id.txtview)
    private TextView textView;
    @ViewInject(R.id.lyt_detatil)
    private static RelativeLayout lyt_detatil;
    @ViewInject(R.id.rlyt_view)
    private static RelativeLayout rlyt_view;

    @ViewInject(R.id.text_member_rebate)//会员返现
    private TextView mMemberRebate;
    @ViewInject(R.id.text_recommend_rebate)//推荐返现
    private TextView mRecommendRebate;

    private int isSelf;
    private BusinessShop mShopBll;
    private EtyShopDetail mapObject;
    private String mGoodsId;
    private List<String> mListImgStr;//图片地址集合
    //轮播图
    private AdapterAffordShopDetail mDetailImageAdapter;
    public int mCollectCounts;//关注数量
    private static int mPosition;
    private int isrebate;

    public static FragmentShopDetailPager newInstance(Context context, int position) {
        FragmentShopDetailPager f = new FragmentShopDetailPager();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        mPosition = position;
        mContext = context;
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShopBll = new BusinessShop(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_shopdetail_pager, container, false);
        ViewUtils.inject(this, rootView); //注入view和事件
        if (getActivity().getIntent().getStringExtra(AtyAffordShop.INTENT_SHOP_ID) != null) {
            mShopBll.getGoodsDetail(getActivity().getIntent().getStringExtra(AtyAffordShop.INTENT_SHOP_ID));
        }
        return rootView;
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_GOODS_DETAIL://商品详情
                    mapObject = (EtyShopDetail) beanSendUI.getInfo();

                    mCollectCounts = UtilNumber.IntegerValueOf(mapObject.getCOLLECTEDCOUNT() + "");
                    if (mapObject != null) {
                        mGoodsId = mapObject.getID() + "";

                        mTvSale.setText("月销售" + mapObject.getSALECOUNT() + "份");
                        mTvCollect.setText("关注" + mCollectCounts);
                        mTvBrandName.setText(mapObject.getBRANDNAME());
                        mTextViewPrice.setText("￥" + mapObject.getSELLPRICE() + "");
                        mTextViewOldPrice.setText("￥" + mapObject.getMARKETPRICE() + "");
                        isSelf = mapObject.getISSELF();//是否是自营商品
                        isrebate = mapObject.getISREBATE();
                        mTextViewOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
                        mTvSize.setText(mapObject.getSPEC() + "");
                        if (isrebate == 1){
                            mMemberRebate.setText("  白金会员购买，返现" + mapObject.getPERCENT4() + "%，购物即存钱");
                            mRecommendRebate.setText("  推荐好友购买，返现" + mapObject.getPERCENT5() + "%，购物即理财");
                            LogUtils.e("onSuccess======null====返利====>" + mapObject.getPERCENT4());
                            LogUtils.e("onSuccess======null====返利====>" + mapObject.getPERCENT5() );
                        }else{
                            mMemberRebate.setVisibility(View.GONE);
                            mRecommendRebate.setText("  推荐好友购买大实惠直营商品，最高返现15%，去推荐");
                        }
                        if (!UtilString.isEmpty(mapObject.getSHORTINFO() + "")) {
                            mTvPresent.setText(mapObject.getSHORTINFO() + "");
                        }
                        if (mapObject.getHASDESCRIBE() == 0) {
                            mScrollView.setScroll(false);
                            lyt_detatil.setVisibility(View.GONE);
                        } else {
                            mScrollView.setScroll(true);
                            lyt_detatil.setVisibility(View.VISIBLE);
                        }

                        if (isSelf == 1 && isrebate == 1) {
                            //直营+返利
                            Bitmap b = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.detail_myself_back);
                            ImageSpan imageSpan = new ImageSpan(getActivity(), b);
                            SpannableString spanString = new SpannableString("icon");
                            spanString.setSpan(imageSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            mTextViewName.setText(spanString);
                            mTextViewName.append(mapObject.getNAME() + "");
                        } else if (isSelf == 1 && isrebate == 0){
                            //直营 非返利
                            Bitmap b = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.myself);
                            ImageSpan imageSpan = new ImageSpan(getActivity(), b);
                            SpannableString spanString = new SpannableString("icon");
                            spanString.setSpan(imageSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            mTextViewName.setText(spanString);
                            mTextViewName.append(mapObject.getNAME() + "");
                        }else if (isSelf == 0 && isrebate == 1){
                            //返利非直营
                            Bitmap b = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.detail_back);
                            ImageSpan imageSpan = new ImageSpan(getActivity(), b);
                            SpannableString spanString = new SpannableString("icon");
                            spanString.setSpan(imageSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            mTextViewName.setText(spanString);
                            mTextViewName.append(mapObject.getNAME() + "");
                        }else {
                            mTextViewName.setText(mapObject.getNAME() + "");
                        }
                        autoImageFlowTimer();//轮播图
                    } else {
                        LogUtils.e("onSuccess======null====mapObject====>" + mapObject);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {

    }

    /**
     * “继续拖动，查看图文详情”布局切换
     */
//    public void isNext() {
//        if (mPosition == 2) {
//            mLytTopDetail.setVisibility(View.VISIBLE);
//            mLytNextDetail.setVisibility(View.GONE);
//        } else if (mPosition == 1) {
//            mLytTopDetail.setVisibility(View.GONE);
//            mLytNextDetail.setVisibility(View.VISIBLE);
//        }
//    }

    /**
     * 点击关注图标时改变关注数量
     */
    public void isCollectClick() {
        if (AtyAffordShopDetail.mIsClickCollected == true) {
            mTvCollect.setText("关注" + ++mCollectCounts);
        } else {
            if (mCollectCounts > 0) {
                mTvCollect.setText("关注" + --mCollectCounts);
            } else if (mCollectCounts <= 0) {
                mTvCollect.setText("关注" + "0");
            }
        }
    }

    /**
     * 顶部轮播图
     */
    public void autoImageFlowTimer() {
        mListImgStr = mapObject.getIMAGES();//图片地址
        if (!UtilList.isEmpty(mListImgStr)) {
            mViewGroup.setVisibility(View.VISIBLE);
        }
        if (mListImgStr != null) {
            mDetailImageAdapter = new AdapterAffordShopDetail(getActivity(), mListImgStr, mViewPager, mCurNum, mTotalNum);
            //动态设置轮播图的高度
            WindowManager wm = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlyt_view.getLayoutParams();
            params.width = width;
            params.height = width;
            rlyt_view.setLayoutParams(params);
            mViewPager.setAdapter(mDetailImageAdapter);// 轮播图 显示数据
            mViewPager.setCurrentItem(0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.lay_rebate)
    public void onRebate(View v) {
        if (AffordApp.isLogin()) {
            Intent intent = new Intent(getContext(), AtyMyMember.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getContext(), AtyLogin.class);
            startActivity(intent);
        }

    }
}

