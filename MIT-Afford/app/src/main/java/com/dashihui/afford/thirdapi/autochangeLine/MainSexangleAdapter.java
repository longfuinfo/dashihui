package com.dashihui.afford.thirdapi.autochangeLine;
import java.util.List;
import java.util.Map;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dashihui.afford.R;
import com.dashihui.afford.util.list.UtilList;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


public class MainSexangleAdapter extends CustomAdapter {

	private List<Map<String, Object>> mListMap;
	private int mChoosePos = -1;
//	@ViewInject(R.id.lyt_item)
//	private LinearLayout mLytItem;
//	private boolean flag = false;
//	@ViewInject(R.id.item_name)
//	private TextView mTvName;
	private Activity mContext;
	private LayoutInflater inflater;


	public MainSexangleAdapter(Activity context, List<Map<String,Object>> list) {
		this.mContext = context;
		this.mListMap = list;
		inflater = LayoutInflater.from(mContext);

	}

	@Override
	public int getCount() {
		if (UtilList.isEmpty(mListMap)){
			return 0;
		}else {
			return mListMap.size();
		}
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		LayoutInflater inflater = mContext.getLayoutInflater();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.aty_server_detail_item, null);
			viewHolder = new ViewHolder(mContext, mListMap);
			//依赖注入初始化
			ViewUtils.inject(viewHolder, convertView);
			convertView.setTag(viewHolder);
			viewHolder.refresh(position);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			//每次更新最新数据
			viewHolder.update(position, mListMap);
			// mTvName.setText("");
		}

		//选中的item设置背景
		if (mChoosePos == position) {
			viewHolder.mLytItem.setBackgroundResource(R.drawable.btn_server_textbg);
			viewHolder.mTvName.setTextColor(Color.parseColor("#ffffff"));
		} else {
			viewHolder.mLytItem.setBackgroundResource(R.drawable.btn_server_textbg_nomal);
			viewHolder.mTvName.setTextColor(Color.parseColor("#555555"));
		}
		return convertView;
	}


	/**
	 * 标示选择的item
	 *
	 * @param position
	 */
	public void isChooseItem(int position) {
		mChoosePos = position;
	}

	static class ViewHolder {

		@ViewInject(R.id.item_name)
		private TextView mTvName;
		@ViewInject(R.id.lyt_item)
		private LinearLayout mLytItem;
		private Activity mContent;
		private List<Map<String, Object>> mListMap;
		private int mPostion;

		public ViewHolder(Activity context, List<Map<String, Object>> _listMap) {
			mContent = context;
			mListMap = _listMap;
//			this.mPostion = pos;
		}

		/**
		 * 更新最新数据
		 *
		 * @param _listMap
		 */
		public void refreshList(List<Map<String, Object>> _listMap) {
			mListMap = _listMap;
		}

		/**
		 * 第一次刷新
		 */
		public void refresh(final int position) {
			if (!UtilList.isEmpty(mListMap)){
				mTvName.setText(mListMap.get(position).get("TITLE") + "");
			}else{
				LogUtils.e("refresh===Error=====null=========>" + mListMap + "");
			}

		}

		/**
		 * 更新
		 */
		public void update(final int position, List<Map<String, Object>> _listMap) {
			refreshList(_listMap);
			refresh(position);
		}
	}

}
