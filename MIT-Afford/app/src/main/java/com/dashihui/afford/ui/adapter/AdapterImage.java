package com.dashihui.afford.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dashihui.afford.R;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.ui.activity.shop.AtyAffordShop;
import com.dashihui.afford.ui.activity.shop.AtyAffordShopDetail;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.number.UtilNumber;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;
import java.util.Map;

/**
 * Viewpaper使用的adapter，轮播图控件
 * Android viewPage notifyDataSetChanged无刷新，http://www.cnblogs.com/maoyu417/p/3740209.html
 *
 * @version V1.0
 * @ClassName: AdapterImage
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2014-12-6 上午10:34:48
 * @维护人:
 */
public class AdapterImage extends PagerAdapter implements OnPageChangeListener {
    private final static String TAG = "AdapterImage";
    private Activity mContext;//Context类
    private List<Map<String, Object>> mListMaps;//图片的id
    private ViewPager mViewPager;//Viewpaper
    private ImageView[] mTips;//指示器数组

    private View mParentView;
    private ViewGroup mTipsViewGroup;
//    private int mImgId, mType;
//    private String mGoodsId;//图片关联的商品Id
    private int mHomeOrShopType;

    public AdapterImage(Activity context, List<Map<String, Object>> listMaps, ViewPager viewPager, View parentView, int homeOrShopType) {
        mContext = context;
        mListMaps = listMaps;
        mViewPager = viewPager;
        mParentView = parentView;
        mHomeOrShopType = homeOrShopType;
        mViewPager.setOnPageChangeListener(this);
        initView();
    }

    private void initView() {
        mTipsViewGroup = (ViewGroup) mParentView;
        initTips();
    }

    private void initTips() {
        int num = 1;
        if (mListMaps != null && mListMaps.size() > 0) {
            num = mListMaps.size();

        }
        mTips = new ImageView[num];
        mTipsViewGroup.removeAllViews();
        //根据图片的数量生成指示器
        for (int i = 0; i < num; i++) {
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.width = 18;
            lp.height = 18;
            lp.setMargins(6, 12, 6, 12);
            imageView.setLayoutParams(lp);
            mTips[i] = imageView;
            if (i == 0) {
                mTips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            } else {
                mTips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }
            mTipsViewGroup.addView(imageView);
        }
    }

    public void setmListMaps(List<Map<String, Object>> mListMaps) {
        this.mListMaps = mListMaps;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        initTips();
    }

    @Override
    public int getCount() {
//		return mListMaps.size();
        return mListMaps.size() == 1 ? 1 : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        if (mListMaps == null || mListMaps.size() <= 0) {
            return container;
        }
        ImageView imageView = new ImageView(mContext);
        String uri = AffConstans.PUBLIC.ADDRESS_IMAGE + mListMaps.get(position % mListMaps.size()).get("THUMB") + "";
//        LogUtils.e("uri====AdapterImage===图片地址======uri=====>" + uri);
        Glide.with(mContext)
                .load(uri)
                .placeholder(R.drawable.default_list)
                .error(R.drawable.default_list)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        imageView.setScaleType(ScaleType.FIT_XY);

        //mType区别广告图片或者商品图片
//        mType = UtilNumber.IntegerValueOf(mListMaps.get(position % mListMaps.size()).get("ID") + "");
        //商品ID
//        mGoodsId =

        //点击轮播图
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHomeOrShopType == 1 && !UtilList.isEmpty(mListMaps)) {//区别从首页还是便利店传的值
//                    if (mType == 2) {
                    Intent intent = new Intent(mContext, AtyAffordShopDetail.class);
                    intent.putExtra(AtyAffordShopDetail.RETURN_ACTIVITY, CommConstans.SHOPDETAIL.RETURN_TAG2 + "");
                    intent.putExtra(AtyAffordShop.INTENT_SHOP_ID, mListMaps.get(position % mListMaps.size()).get("GOODSID") + "");
                    intent.putExtra(AtyAffordShop.INTENT_SHOP_IMG, mListMaps.get(position % mListMaps.size()).get("THUMB") + "");
                    mContext.startActivity(intent);
//                    }
                }
            }
        });
        container.addView(imageView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        return imageView;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < mTips.length; i++) {
            if (i == position % mTips.length) {
                mTips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            } else {
                mTips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }
        }
    }
}