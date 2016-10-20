package com.dashihui.afford.thirdapi.choosemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.thirdapi.choosemenu.adapter.LeftAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ViewMiddle extends RelativeLayout implements ViewBaseAction{

	private ListView mListView;
	private  List<Map<String, Object>> items  = new ArrayList<Map<String, Object>>();//显示字段
	private  String[] itemsVaule = new String[] { "1", "2", "3", "4", "5", "6" };//隐藏id
	//	private OnSelectListener mOnSelectListener;
	private LeftAdapter adapter;
	private String mDistance;
	private String showText = "销量";
	private Context mContext;

	public String getShowText() {
		return showText;
	}

	public ViewMiddle(BaseActivity context) {
		super(context);
		init(context);
	}

	public ViewMiddle(BaseActivity context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ViewMiddle(BaseActivity context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(BaseActivity context) {
		mContext = context;
//		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		inflater.inflate(R.layout.third_choose_view_distance, this, true);
//		setBackgroundDrawable(getResources().getDrawable(R.drawable.choosearea_bg_mid));
//		mListView = (ListView) findViewById(R.id.listView);
//		adapter = new TextAdapter(context, items, R.drawable.choose_item_right, R.drawable.choose_eara_item_selector);
//		adapter.setTextSize(17);
//		if (mDistance != null) {
//			for (int i = 0; i < itemsVaule.length; i++) {
//				if (itemsVaule[i].equals(mDistance)) {
//					adapter.setSelectedPositionNoNotify(i);
//					showText = items.get(i).get("NAME")+"";
//					break;
//				}
//			}
//		}
//		mListView.setAdapter(adapter);
//		adapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {
//
//			@Override
//			public void onItemClick(View view, int position) {
//
//				if (mOnSelectListener != null) {
//					showText = items.get(position).get("NAME")+"";
//					mOnSelectListener.getValue(itemsVaule[position], items.get(position).get("NAME")+"");
//				}
//			}
//		});
	}



//	public void setOnSelectListener(OnSelectListener onSelectListener) {
//		mOnSelectListener = onSelectListener;
//	}
//
//	public interface OnSelectListener {
//		public void getValue(String distance, String showText);
//	}



	@Override
	public void hide() {
		
	}

	@Override
	public void show() {
		
	}

}
