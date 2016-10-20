package com.dashihui.afford.thirdapi.choosemenu;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dashihui.afford.R;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.thirdapi.choosemenu.adapter.LeftAdapter;
import com.dashihui.afford.thirdapi.choosemenu.adapter.RightAdapter;
import com.dashihui.afford.util.list.UtilList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ViewLeft extends LinearLayout implements ViewBaseAction {

    private ListView regionListView;
    private ListView plateListView;
    private List<Map<String, Object>> groups = new ArrayList<Map<String, Object>>();
    private LinkedList<Map<String, Object>> childrenItem = new LinkedList<Map<String, Object>>();
    private SparseArray<LinkedList<Map<String, Object>>> children = new
            SparseArray<LinkedList<Map<String, Object>>>();
    private RightAdapter plateListViewAdapter;
    private LeftAdapter earaListViewAdapter;
    private OnSelectListener mOnSelectListener;
    private int tEaraPosition = 0;
    private int tBlockPosition = 0;
    private String showString = "不限";
    private int mPos;
    private List<Map<String, Object>> mModelCatogray;
    private BaseActivity mContext;
    private List<Map<String, Object>> mListCatogray;
    //默认分类
    private static String categoryoncode = "010000000";
    private static String mCategoryonname;//四个分类的名称

    public ViewLeft(BaseActivity context, List<Map<String, Object>> modelCatogray) {
        super(context);
        mModelCatogray = modelCatogray;
        mContext = context;
        initView();
        initData();
        initAdapter();
    }


    private void initView() {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.third_choose_view_region, this, true);
        regionListView = (ListView) findViewById(R.id.listView);
        plateListView = (ListView) findViewById(R.id.listView2);
        setBackgroundDrawable(getResources().getDrawable(R.drawable.choosearea_bg_left));
        mListCatogray = getmModelCatogray();
    }

    private void initData() {

        for (int i = 0; i < mListCatogray.size(); i++) {
            groups.add(mListCatogray.get(i));//四大分类的名称

            LinkedList<Map<String, Object>> tItem = new LinkedList<Map<String, Object>>();
            List<Map<String, Object>> listMaps = (List<Map<String, Object>>) mListCatogray.get(i)
                    .get("CHILDREN");
            Map<String, Object> mapObject = new HashMap<>();
            mapObject.put("CODE", "");
            mapObject.put("NAME", "全部");
            mapObject.put("GOODSCOUNT", mListCatogray.get(i).get("GOODSCOUNT"));
            tItem.add(mapObject);
            for (int j = 0; j < listMaps.size(); j++) {
                tItem.add(listMaps.get(j));
            }
            children.put(i, tItem);
        }
        //默认值

    }

    private void initAdapter() {
        if (!UtilList.isEmpty(groups)) {
            categoryoncode = groups.get(0).get("CODE") + "";
            mCategoryonname = groups.get(0).get("NAME") + "";
        }
        earaListViewAdapter = new LeftAdapter(mContext, groups);
        earaListViewAdapter.setTextSize(16);
        earaListViewAdapter.setSelectedPositionNoNotify(tEaraPosition);
        regionListView.setAdapter(earaListViewAdapter);
        earaListViewAdapter.setOnItemClickListener(new LeftAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                categoryoncode = groups.get(position).get("CODE") + "";
                mCategoryonname = groups.get(position).get("NAME") + "";
                if (position < children.size()) {
                    childrenItem.clear();
                    mPos = position;
                    childrenItem.addAll(children.get(position));
                    plateListViewAdapter.notifyDataSetChanged();
                }
            }
        });
        if (tEaraPosition < children.size())
            childrenItem.addAll(children.get(tEaraPosition));
        plateListViewAdapter = new RightAdapter(mContext, childrenItem);
        plateListViewAdapter.setTextSize(14);
        plateListViewAdapter.setSelectedPositionNoNotify(tBlockPosition);
        plateListView.setAdapter(plateListViewAdapter);
        plateListViewAdapter.setOnItemClickListener(new RightAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, final int position) {

                if (position == 0) {
                    showString = mCategoryonname;
                } else {
                    showString = childrenItem.get(position).get("NAME") + "";
                }
                String mCategoryCode = childrenItem.get(position).get("CODE") + "";
                //选中“对号”
                plateListViewAdapter.setSelectedPositionNoNotify(position);

                if (mOnSelectListener != null) {
                    mOnSelectListener.getValue(showString, categoryoncode, mCategoryCode);
                }
            }
        });
        if (tBlockPosition < childrenItem.size())
            showString = childrenItem.get(tBlockPosition).get("NAME") + "";
        if (showString.contains("不限")) {
            showString = showString.replace("不限", "");
        }
        setDefaultSelect();

    }

    public void setDefaultSelect() {
        regionListView.setSelection(tEaraPosition);
        plateListView.setSelection(tBlockPosition);

    }

    public String getShowText() {
        return showString;
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }


    public List<Map<String, Object>> getmModelCatogray() {
        return mModelCatogray;
    }

    public void setmModelCatogray(List<Map<String, Object>> m) {
        groups.clear();
        children.clear();
        for(int i = 0;i<m.size();i++) {
            groups.add(m.get(i));
            LinkedList<Map<String, Object>> tItem = new LinkedList<Map<String, Object>>();
            List<Map<String, Object>> listMaps = (List<Map<String, Object>>) m.get(i)
                    .get("CHILDREN");
            Map<String, Object> mapObject = new HashMap<>();
            mapObject.put("CODE", "");
            mapObject.put("NAME", "全部");
            mapObject.put("GOODSCOUNT", mListCatogray.get(i).get("GOODSCOUNT"));
            tItem.add(mapObject);
            for (int j = 0; j < listMaps.size(); j++) {
                tItem.add(listMaps.get(j));
            }
            children.put(i, tItem);
        }
        childrenItem.clear();
        childrenItem.addAll(children.get(mPos));
        earaListViewAdapter.setList(groups);
        plateListViewAdapter.setList(childrenItem);
    }

    public interface OnSelectListener {
        public void getValue(String showText, String dType, String xType);
    }


    @Override
    public void hide() {
    }

    @Override
    public void show() {
    }

}
