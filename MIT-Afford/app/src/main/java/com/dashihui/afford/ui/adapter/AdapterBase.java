package com.dashihui.afford.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * BaseAdapter完美的封装，抽象的ArrayList
 * @author NiuFC
 * @date  2012-12-5
 * @version 1.0
 * @param <T>
 */
public abstract class AdapterBase<T> extends BaseAdapter {
	protected List<T> mList;
	protected Activity mContext;
	protected ListView mListView;
	

	public AdapterBase(Activity context,List<T> _List){
		this.mContext = context;
		setList(_List);
	}


	@Override
	public int getCount() {
		if(mList != null)
			return mList.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		if (position < mList.size()) {
			return mList == null ? null : mList.get(position);
		}else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	abstract public View getView(int position, View convertView, ViewGroup parent);
	
	public void setList(List<T> list){
		this.mList = list;
		notifyDataSetChanged();
	}


	public List<T> getList(){
		return mList;
	}
	
	public void setList(T[] list){
		ArrayList<T> arrayList = new ArrayList<T>(list.length);  
		for (T t : list) {  
			arrayList.add(t);  
		}  
		setList(arrayList);
	}
	
	public ListView getListView(){
		return mListView;
	}
	
	public void setListView(ListView listView){
		mListView = listView;
	}
	/**
	 * 添加列表项
	 * @param item
	 */
	public void addItem(List<T> item) {
		ArrayList<T> arrayList = new ArrayList<T>(item.size()); 
		mList.addAll(arrayList);
	}
	
	public void setItem(int index, T item) {
		mList.set(index, item);
	}


}
