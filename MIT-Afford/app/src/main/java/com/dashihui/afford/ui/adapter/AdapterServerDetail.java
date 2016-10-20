package com.dashihui.afford.ui.adapter;

import android.app.Activity;
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
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.string.UtilString;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.util.LogUtils;

import java.util.List;

/** 
 * Viewpaper使用的adapter，轮播图控件
 * Android viewPage notifyDataSetChanged无刷新，http://www.cnblogs.com/maoyu417/p/3740209.html
* @ClassName: AdapterImage 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @date 2014-12-6 上午10:34:48 
* @维护人: 
* @version V1.0   
*/ 
public class AdapterServerDetail extends PagerAdapter implements OnPageChangeListener{
	private final static String TAG = "AdapterImage";
	private Activity mContext;//Context类
	private List<String> mListMaps;//图片的id
	private String mImg;
	private ViewPager mViewPager;//Viewpaper
	private ImageView[] mTips;//指示器数组

	private View mParentView;
	private ViewGroup mTipsViewGroup;

	public AdapterServerDetail(Activity context, List<String> listMaps, ViewPager viewPager, View parentView) {
		mContext = context;
		mListMaps = listMaps;
		mViewPager = viewPager;
		mParentView = parentView;
		mViewPager.setOnPageChangeListener(this);
		initView();
	}

	private void initView() {
		mTipsViewGroup = (ViewGroup) mParentView;
		initTips();
	}

	private void initTips(){
		int num = 1;
		if (mListMaps!=null && mListMaps.size()>0) {
			num =mListMaps.size();
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
				mTips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				mTips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
			mTipsViewGroup.addView(imageView);
		}
	}
	
	
	
	public void setmListMaps(List<String> mListMaps) {
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
		if (!UtilList.isEmpty(mListMaps)){
			return mListMaps.size() == 0 ? 1 : mListMaps.size();
		}else {
			return 0;
		}

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
		if(mListMaps == null || mListMaps.size() <= 0){
			return container;
		}
		ImageView imageView = new ImageView(mContext);
//		List<String> listStr = mListMaps.get(position % mListMaps.size()).getTHUMB();
		mImg = mListMaps.get(position % mListMaps.size())+"";
		if (!UtilString.isEmpty(mImg)){
			String uri = AffConstans.PUBLIC.ADDRESS_IMAGE + mImg+"";
			LogUtils.e("uri===图片地址===========>" + uri);
			Glide.with(mContext)
					.load(uri)
					.placeholder(R.drawable.default_list)
					.error(R.drawable.default_list)
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.into(imageView);
		}else {
			LogUtils.e("uri===null==error==========>" +mImg);
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
				mTips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				mTips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
		}
	}

}