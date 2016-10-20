package com.dashihui.afford.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import com.dashihui.afford.R;
import com.dashihui.afford.util.preferences.UtilPreferences;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteAdapter extends BaseAdapter implements Filterable {
	private Context context;
	/** 存储账号的文件名 */
	private String fileName;
	/** 存储账号的键名 */
	private final static String ACCOUNT_KEY_NAME = "UserList";
	private ArrayFilter mFilter;
	private ArrayList<String> mOriginalValues;// 所有的Item
	private List<String> mObjects;// 过滤后的item
	private final Object mLock = new Object();
	private int maxMatch = 10;// 最多显示多少个选项,负数表示全部
	
	public AutoCompleteAdapter(Context context,
			ArrayList<String> mOriginalValues, String fname,int maxMatch) {
		this.context = context;
		this.mOriginalValues = mOriginalValues;
		this.fileName = fname;
		this.maxMatch = maxMatch;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

	private class ArrayFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			// TODO Auto-generated method stub
			FilterResults results = new FilterResults();
			if (prefix == null || prefix.length() == 0) {
				synchronized (mLock) {
					Log.i("tag",
							"mOriginalValues.size=" + mOriginalValues.size());
					ArrayList<String> list = new ArrayList<String>(
							mOriginalValues);
					results.values = list;
					results.count = list.size();
					return results;
				}
			} else {
				String prefixString = prefix.toString().toLowerCase();
				final int count = mOriginalValues.size();
				final ArrayList<String> newValues = new ArrayList<String>(count);
				for (int i = 0; i < count; i++) {
					final String value = mOriginalValues.get(i);
					final String valueText = value.toLowerCase();
					if (valueText.startsWith(prefixString)) { // 源码 ,匹配开头
						newValues.add(value);
					}
					if (maxMatch > 0) {// 有数量限制
						if (newValues.size() > maxMatch - 1) {// 不要太多
							break;
						}
					}
				}
				results.values = newValues;
				results.count = newValues.size();
			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			// TODO Auto-generated method stub
			mObjects = (List<String>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mObjects.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		// 此方法有误，尽量不要使用
		return mObjects.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.autocomplete_item, null);
			holder.tv = (TextView) convertView.findViewById(R.id.simple_item_0);
			holder.iv = (ImageView) convertView
					.findViewById(R.id.simple_item_1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv.setText(mObjects.get(position));
		holder.iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String obj = mObjects.remove(position);
				mOriginalValues.remove(obj);
				ArrayList<String> newArrayList = new ArrayList<String>();
				String[] books = UtilPreferences.getString(context, ACCOUNT_KEY_NAME).split(",");
				for (int i = 0; i < books.length; i++) {
					if (!books[i].equals(obj)) {
						newArrayList.add(books[i]);
					}
				}
				StringBuilder sb = new StringBuilder("");
				for (int i = 0; i < newArrayList.size(); i++) {
					sb.append(newArrayList.get(i) + ",");
				}
				String result = sb.toString();
				UtilPreferences.getString(context, ACCOUNT_KEY_NAME,
						result.substring(0, sb.length() - 1));
				notifyDataSetChanged();
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView tv;
		ImageView iv;
	}

	public ArrayList<String> getAllItems() {
		return mOriginalValues;
	}
}
