/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dashihui.afford.ui.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dashihui.afford.R;
import com.dashihui.afford.business.entity.EtyServerTime;
import com.dashihui.afford.ui.activity.server.AtyFragmentServerTime;
import com.dashihui.afford.util.screen.UtilScreen;
import com.lidroid.xutils.util.LogUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class WdtPagerTabServerTime extends HorizontalScrollView {

	private final static String TAG = "WdtPagerSlidingTabStrip";

	private static int yesBoolean = 0;

	public interface IconTabProvider {
		public int getPageIconResId(int position);
	}

	// @formatter:off
	private static final int[] ATTRS = new int[] {
		android.R.attr.textSize,
		android.R.attr.textColor
    };
	// @formatter:on

	private LinearLayout.LayoutParams defaultTabLayoutParams;
	private LinearLayout.LayoutParams expandedTabLayoutParams;

	private final PageListener pageListener = new PageListener();
	public OnPageChangeListener delegatePageListener;

	private LinearLayout tabsContainer;
	private ViewPager mPager;
	private int tabCount;

	private int currentPosition = 0;
	private float currentPositionOffset = 0f;

	private Paint rectPaint;
	private Paint dividerPaint;

	private int indicatorColor = 0xFF666666;
	private int underlineColor = 0x1A000000;
	private int dividerColor = 0x1A000000;

	private boolean shouldExpand = false;
	private boolean textAllCaps = true;

	private int scrollOffset = 52;
	private int indicatorHeight = 4;
	private int underlineHeight = 2;
	private int dividerPadding = 12;
	private int tabPadding = 14;
	private int dividerWidth = 1;
	private int mDisplayNum = 3;
	private int tabTextSize = 18;
	private int tabTextColor = 0xFF666666;
	private Typeface tabTypeface = null;
	private int tabTypefaceStyle = Typeface.ITALIC;

	private int lastScrollX = 0;

	//tab背景
	private int tabBackgroundResId = R.drawable.background_tabs;
//	private int tabBackgroundResId = R.drawable.servicetime_btn_label_bottom;

	private Activity mContext;

	private Locale locale;
	//存放tab栏textview的map
	private Map<Integer, TextView> mTextViewMap = new HashMap<Integer, TextView>();

	public WdtPagerTabServerTime(Context context) {
		this(context, null);
		mContext = (Activity)context;
	}

	public WdtPagerTabServerTime(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		mContext = (Activity)context;
	}

	public WdtPagerTabServerTime(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = (Activity)context;
		setFillViewport(true);
		setWillNotDraw(false);
		tabsContainer = new LinearLayout(context);
		tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
		tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(tabsContainer);

		DisplayMetrics dm = getResources().getDisplayMetrics();

		scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
		indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
		underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
		dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
		tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
		dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
		tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);
		mDisplayNum = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mDisplayNum, dm);
		

		// get system attrs (android:textSize and android:textColor)

		TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

		tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
		tabTextColor = a.getColor(1, tabTextColor);

		a.recycle();

		// get custom attrs

		a = context.obtainStyledAttributes(attrs, R.styleable.WdtPagerSlidingTabStrip);

		indicatorColor = a.getColor(R.styleable.WdtPagerSlidingTabStrip_pstsIndicatorColor, indicatorColor);
		underlineColor = a.getColor(R.styleable.WdtPagerSlidingTabStrip_pstsUnderlineColor, underlineColor);
		dividerColor = a.getColor(R.styleable.WdtPagerSlidingTabStrip_pstsDividerColor, dividerColor);
		indicatorHeight = a.getDimensionPixelSize(R.styleable.WdtPagerSlidingTabStrip_pstsIndicatorHeight, indicatorHeight);
		underlineHeight = a.getDimensionPixelSize(R.styleable.WdtPagerSlidingTabStrip_pstsUnderlineHeight, underlineHeight);
		dividerPadding = a.getDimensionPixelSize(R.styleable.WdtPagerSlidingTabStrip_pstsDividerPadding, dividerPadding);
		tabPadding = a.getDimensionPixelSize(R.styleable.WdtPagerSlidingTabStrip_pstsTabPaddingLeftRight, tabPadding);
		tabBackgroundResId = a.getResourceId(R.styleable.WdtPagerSlidingTabStrip_pstsTabBackground, tabBackgroundResId);
		shouldExpand = a.getBoolean(R.styleable.WdtPagerSlidingTabStrip_pstsShouldExpand, shouldExpand);
		scrollOffset = a.getDimensionPixelSize(R.styleable.WdtPagerSlidingTabStrip_pstsScrollOffset, scrollOffset);
		textAllCaps = a.getBoolean(R.styleable.WdtPagerSlidingTabStrip_pstsTextAllCaps, textAllCaps);
//		mDisplayNum = a.getBoolean(R.styleable.WdtPagerSlidingTabStrip_pstsDisplayNum, mDisplayNum);
		mDisplayNum = a.getInt(R.styleable.WdtPagerSlidingTabStrip_pstsDisplayNum, mDisplayNum);

		a.recycle();

		rectPaint = new Paint();
		rectPaint.setAntiAlias(true);
		rectPaint.setStyle(Style.FILL);

		dividerPaint = new Paint();
		dividerPaint.setAntiAlias(true);
		dividerPaint.setStrokeWidth(dividerWidth);
//		defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		defaultTabLayoutParams = new LinearLayout.LayoutParams((int)UtilScreen.getScreenWidth()/mDisplayNum, LayoutParams.MATCH_PARENT);
		expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

		if (locale == null) {
			locale = getResources().getConfiguration().locale;
		}
	}

	public void setViewPager(ViewPager pager) {
		this.mPager = pager;
		yesBoolean = 0;
		if (pager.getAdapter() == null) {
			throw new IllegalStateException("ViewPager does not have mAdapterGalleryItem instance.");
		}

		pager.setOnPageChangeListener(pageListener);

		notifyDataSetChanged();
	}

	public void setOnPageChangeListener(OnPageChangeListener listener) {
		this.delegatePageListener = listener;
	}

	public void notifyDataSetChanged() {

		tabsContainer.removeAllViews();

		tabCount = mPager.getAdapter().getCount();

		for (int i = 0; i < tabCount; i++) {

			if (mPager.getAdapter() instanceof IconTabProvider) {
				addIconTab(i, ((IconTabProvider) mPager.getAdapter()).getPageIconResId(i));
//				addIconTab(i, R.drawable.servicetime_btn_label_bottom);
			} else {
				LogUtils.e("TAB标题和样式===========getPageTitle===========>" + mPager.getAdapter().getPageTitle(i));
				LogUtils.e("TAB标题和样式===========时间=============>" + ((AtyFragmentServerTime.MyPagerAdapter) mPager.getAdapter()).getmEtyTabsList().get(i));
				addViewTab(i, ((AtyFragmentServerTime.MyPagerAdapter) mPager.getAdapter()).getmEtyTabsList().get(i));
			}

		}

		updateTabStyles();

		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@SuppressWarnings("deprecation")
			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
					getViewTreeObserver().removeGlobalOnLayoutListener(this);
				} else {
					getViewTreeObserver().removeOnGlobalLayoutListener(this);
				}

				currentPosition = mPager.getCurrentItem();
				scrollToChild(currentPosition, 0);
			}
		});
		setTabTextViewColor(mPager.getCurrentItem());
	}


	private void addViewTab(final int position, EtyServerTime etyServerTime) {

//		TextView tab = new TextView(getContext());
//		tab.setText(title);
//		tab.setGravity(Gravity.CENTER);
//		tab.setSingleLine();
		LayoutInflater inflater = mContext.getLayoutInflater();
		View view = inflater.inflate(R.layout.widget_server_times, null);
		TextView txtView = (TextView)view.findViewById(R.id.dateDay);
		TextView txtView1 = (TextView)view.findViewById(R.id.dateTime);
		LinearLayout lytTab = (LinearLayout) view.findViewById(R.id.lyt_toptab);
//		lytTab.setBackgroundResource(R.drawable.servicetime_btn_label_bottom);////////
		txtView.setText(etyServerTime.getTITLE());
		txtView1.setText(etyServerTime.getDATE());
		addTab(position, view);
		mTextViewMap.put(position, txtView);
	}


	private void addTextTab(final int position, String title) {

		TextView tab = new TextView(getContext());
		tab.setText(title);
		tab.setGravity(Gravity.CENTER);
		tab.setSingleLine();
//		tab.setBackgroundResource(R.drawable.servicetime_btn_label_bottom);/////////

		addTab(position, tab);
		mTextViewMap.put(position, tab);
	}

	private void addIconTab(final int position, int resId) {

		ImageButton tab = new ImageButton(getContext());
		tab.setImageResource(resId);

		addTab(position, tab);

	}

	/**
	 * 添加TAB
	 * @param position
	 * @param tab
	 */
	private void addTab(final int position, View tab) {
		tab.setFocusable(true);
		tab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPager.setCurrentItem(position);
			}
		});

		tab.setPadding(tabPadding, 0, tabPadding, 0);
		/*****************/
		tab.setBackgroundResource(R.drawable.background_tabs);

		tabsContainer.addView(tab, position, shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);
	}

	private void updateTabStyles() {

		for (int i = 0; i < tabCount; i++) {

			View v = tabsContainer.getChildAt(i);

			v.setBackgroundResource(tabBackgroundResId);

			if (v instanceof TextView) {

				TextView tab = (TextView) v;
				tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
				tab.setTypeface(tabTypeface, tabTypefaceStyle);
				tab.setTextColor(tabTextColor);
				/*****************/
//				tab.setBackgroundResource(R.drawable.servicetime_btn_label_bottom);

				// setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
				// pre-ICS-build
				if (textAllCaps) {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
						tab.setAllCaps(true);
					} else {
						tab.setText(tab.getText().toString().toUpperCase(locale));
					}
				}
			}
		}

	}

	private void scrollToChild(int position, int offset) {

		if (tabCount == 0) {
			return;
		}

		int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

		if (position > 0 || offset > 0) {
			newScrollX -= scrollOffset;
		}

		if (newScrollX != lastScrollX) {
			lastScrollX = newScrollX;
			scrollTo(newScrollX, 0);
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (isInEditMode() || tabCount == 0) {
			return;
		}

		final int height = getHeight();

		// draw indicator line
		//tab 底部选中线的颜色
		rectPaint.setColor(getResources().getColor(R.color.btfontBlue));

		// default: line below current tab
		View currentTab = tabsContainer.getChildAt(currentPosition);
		float lineLeft = currentTab.getLeft();
		float lineRight = currentTab.getRight();
		// if there is an offset, start interpolating left and right coordinates between current and next tab
		if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {
			//去除第一个和最后一个tab选项卡选中
			View nextTab = tabsContainer.getChildAt(currentPosition+1);
			final float nextTabLeft = nextTab.getLeft();
			final float nextTabRight = nextTab.getRight();
			if (lineLeft != nextTabLeft && yesBoolean<2) {
				//只过滤第一次跳转过来如果满足时中间的tab的情况
				//会进来两次
//				Log.i(TAG, "最左边坐标===TTT==》nextTabLeft=====>"+nextTabLeft);
//				Log.i(TAG, "最左边坐标===TTT==》lineLeft=====>"+lineLeft);
				yesBoolean++;
			}else {
				lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
				lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
				yesBoolean++;
			}
			
		}
		canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height, rectPaint);

		// draw underline
		//绘制完全透明颜色
		rectPaint.setColor(underlineColor);
		canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);

		// draw divider

		dividerPaint.setColor(dividerColor);
		for (int i = 0; i < tabCount - 1; i++) {
			View tab = tabsContainer.getChildAt(i);
			canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
		}
	}
	

	private class PageListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			currentPosition = position;
			currentPositionOffset = positionOffset;

			scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));

			invalidate();

			if (delegatePageListener != null) {
				delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE) {
				scrollToChild(mPager.getCurrentItem(), 0);
			}

			if (delegatePageListener != null) {
				delegatePageListener.onPageScrollStateChanged(state);
			}
		}

		@Override
		public void onPageSelected(int position) {
			if (delegatePageListener != null) {
				delegatePageListener.onPageSelected(position);
			}
			setTabTextViewColor(mPager.getCurrentItem());
		}

	}

	public void setIndicatorColor(int indicatorColor) {
		this.indicatorColor = indicatorColor;
		invalidate();
	}

	public void setIndicatorColorResource(int resId) {
		this.indicatorColor = getResources().getColor(resId);
		invalidate();
	}

	public int getIndicatorColor() {
		return this.indicatorColor;
	}

	public void setIndicatorHeight(int indicatorLineHeightPx) {
		this.indicatorHeight = indicatorLineHeightPx;
		invalidate();
	}

	public int getIndicatorHeight() {
		return indicatorHeight;
	}

	public void setUnderlineColor(int underlineColor) {
		this.underlineColor = underlineColor;
		invalidate();
	}

	public void setUnderlineColorResource(int resId) {
		this.underlineColor = getResources().getColor(resId);
		invalidate();
	}

	public int getUnderlineColor() {
		return underlineColor;
	}

	public void setDividerColor(int dividerColor) {
		this.dividerColor = dividerColor;
		invalidate();
	}

	public void setDividerColorResource(int resId) {
		this.dividerColor = getResources().getColor(resId);
		invalidate();
	}

	public int getDividerColor() {
		return dividerColor;
	}

	public void setUnderlineHeight(int underlineHeightPx) {
		this.underlineHeight = underlineHeightPx;
		invalidate();
	}

	public int getUnderlineHeight() {
		return underlineHeight;
	}

	public void setDividerPadding(int dividerPaddingPx) {
		this.dividerPadding = dividerPaddingPx;
		invalidate();
	}

	public int getDividerPadding() {
		return dividerPadding;
	}

	public void setScrollOffset(int scrollOffsetPx) {
		this.scrollOffset = scrollOffsetPx;
		invalidate();
	}

	public int getScrollOffset() {
		return scrollOffset;
	}

	public void setShouldExpand(boolean shouldExpand) {
		this.shouldExpand = shouldExpand;
		requestLayout();
	}

	public boolean getShouldExpand() {
		return shouldExpand;
	}

	public boolean isTextAllCaps() {
		return textAllCaps;
	}

	public void setAllCaps(boolean textAllCaps) {
		this.textAllCaps = textAllCaps;
	}

	public void setTextSize(int textSizePx) {
		this.tabTextSize = textSizePx;
		updateTabStyles();
	}

	public int getTextSize() {
		return tabTextSize;
	}

	public void setTextColor(int textColor) {
		this.tabTextColor = textColor;
		updateTabStyles();
	}

	public void setTextColorResource(int resId) {
		this.tabTextColor = getResources().getColor(resId);
		updateTabStyles();
	}

	public int getTextColor() {
		return tabTextColor;
	}

	public void setTypeface(Typeface typeface, int style) {
		this.tabTypeface = typeface;
		this.tabTypefaceStyle = style;
		updateTabStyles();
	}

	public void setTabBackground(int resId) {
		this.tabBackgroundResId = resId;
	}

	public int getTabBackground() {
		return tabBackgroundResId;
	}

	public void setTabPaddingLeftRight(int paddingPx) {
		this.tabPadding = paddingPx;
		updateTabStyles();
	}

	public int getTabPaddingLeftRight() {
		return tabPadding;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		currentPosition = savedState.currentPosition;
		requestLayout();
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState savedState = new SavedState(superState);
		savedState.currentPosition = currentPosition;
		return savedState;
	}

	static class SavedState extends BaseSavedState {
		int currentPosition;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			currentPosition = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(currentPosition);
		}

		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
	

	/**
	 * 设置tab栏textview的字体颜色
	 * @param pos
	 */
	private void setTabTextViewColor(int pos){
		if(mTextViewMap.size() <=0 ){
			return;
		}else{
//			for(int i=0;i<mTextViewMap.size();i++){
//
//			}
			for(Integer key : mTextViewMap.keySet()){
				if(key == pos){
					mTextViewMap.get(key).setTextColor(getResources().getColor(R.color.topfontblue));
				}else{
					mTextViewMap.get(key).setTextColor(getResources().getColor(R.color.topTitle));
				}
			}
		}
	}

}
