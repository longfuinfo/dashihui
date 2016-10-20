package com.dashihui.afford.ui.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dashihui.afford.R;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.util.list.UtilList;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.util.LogUtils;

import java.util.List;

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
public class AdapterAffordShopDetail extends PagerAdapter implements OnPageChangeListener {
    private final static String TAG = "AdapterImage";
    private Activity mContext;//Context类
    private List<String> mListMaps;//图片的id
    private ViewPager mViewPager;//Viewpaper
    private ImageView[] mTips;//指示器数组


    private TextView mCurNum;
    private TextView mTotalNum;
//    private View mShopLayout;

    public AdapterAffordShopDetail(Activity context, List<String> listMaps, ViewPager viewPager, TextView _CurNum,TextView _TotalNum) {
        mContext = context;
        mListMaps = listMaps;
        mViewPager = viewPager;
        mCurNum = _CurNum;
        mTotalNum = _TotalNum;
        mViewPager.setOnPageChangeListener(this);
        initTips();
    }


    private void initTips() {
        int num = 1;
        LogUtils.e("initTips====mListMaps.size()=====>" + mListMaps.size());
        if (mListMaps != null && mListMaps.size() > 0) {
            num = mListMaps.size();
            LogUtils.e("initTips====mListMaps.size()===mTotalNum==>" + mListMaps.size());
            mCurNum.setVisibility(View.VISIBLE);
            mTotalNum.setVisibility(View.VISIBLE);
            mTotalNum.setText("/"+num);
        }else {
            mCurNum.setVisibility(View.GONE);
            mTotalNum.setVisibility(View.GONE);
        }
        mTips = new ImageView[num];

    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        initTips();
    }

    @Override
    public int getCount() {
        if (UtilList.isEmpty(mListMaps)){
            return 0;
        }
        return mListMaps.size() == 0 ? 1 : mListMaps.size();
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
        if (!UtilList.isEmpty(mListMaps)) {
            String uri = AffConstans.PUBLIC.ADDRESS_IMAGE + mListMaps.get(position % mListMaps.size()) + "";
            Glide.with(mContext)
                    .load(uri)
                    .placeholder(R.drawable.default_list)
                    .error(R.drawable.default_list)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        } else {
            LogUtils.e("uri====AdapterAffordShopDetail====null==error==========>");
        }
        imageView.setScaleType(ScaleType.FIT_XY);
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
                mCurNum.setText((position + 1) + "");
            } else {
                LogUtils.e("====onPageSelected================>");
            }
        }
    }


}