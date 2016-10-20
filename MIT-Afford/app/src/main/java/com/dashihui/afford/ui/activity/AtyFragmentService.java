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

package com.dashihui.afford.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dashihui.afford.R;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.base.BaseAtyFragment;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.ui.activity.fragment.FragmentServer;
import com.dashihui.afford.ui.activity.fragment.FragmentServerComplete;
import com.dashihui.afford.ui.activity.fragment.FragmentServerWait;
import com.dashihui.afford.ui.widget.WdtPagerSlidingTabStrip;


public class AtyFragmentService extends BaseAtyFragment implements OnClickListener {

	private WdtPagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;

	private int currentColor = 0xFF666666;
	private TextView mTopTitle_txt;
	private ImageButton mIbtnBack;

	private int mSearchClassifyType;// 此变量标志着是否是从搜索页面过来的，按地区或行业搜索的

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_orderserver_frag);

		mTopTitle_txt = (TextView) findViewById(R.id.title);
		mTopTitle_txt.setText("服务订单");
		mIbtnBack = (ImageButton) findViewById(R.id.ibtnBack);
		mIbtnBack.setOnClickListener(this);

		tabs = (WdtPagerSlidingTabStrip) findViewById(R.id.apps_fragment_tabs);
		pager = (ViewPager) findViewById(R.id.apps_fragment_viewpager);
		Intent intent = getIntent();
		//此页面会从两个页面进入，1 应用首页分类按钮 2 搜索页面分类按钮 如果从搜索页面分类按钮点击进入，须取出searchClassifyType
		mSearchClassifyType = intent.getIntExtra("searchClassifyType", -1);
		adapter = new MyPagerAdapter(getSupportFragmentManager());

		// 设置缓存页面，当前页面的相邻N各页面都会被缓存
		pager.setOffscreenPageLimit(adapter.getCount());
		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
		pager.setPageMargin(pageMargin);
		pager.setCurrentItem(intent.getIntExtra("tabCode", 0));
		tabs.setViewPager(pager);

		changeColor(currentColor);
	}

	private void changeColor(int newColor) {
		tabs.setIndicatorColor(newColor);
	}

	public void onColorClicked(View v) {
		int color = Color.parseColor(v.getTag().toString());
		changeColor(color);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("currentColor", currentColor);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		currentColor = savedInstanceState.getInt("currentColor");
		changeColor(currentColor);
	}

	@Override
	public void onSuccess(EtySendToUI beanSendUI) {

	}

	@Override
	public void onFailure(EtySendToUI beanSendUI) {

	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager supportFragmentManager) {
			super(supportFragmentManager);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CommConstans.COMMONARRAY.TABS_SERVICE[position];
		}

		@Override
		public int getCount() {
			return CommConstans.COMMONARRAY.TABS_SERVICE.length;
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = FragmentServer.newInstance(AtyFragmentService.this, position);
				break;
			case 1:
				fragment = FragmentServerWait.newInstance(AtyFragmentService.this, position);
				break;
			case 2:
				fragment = FragmentServerComplete.newInstance(AtyFragmentService.this, position);
				break;
			default:
				break;
			}
			return fragment;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ibtnBack:
			onBackPressed();
			break;
		default:
			break;
		}
	}

}