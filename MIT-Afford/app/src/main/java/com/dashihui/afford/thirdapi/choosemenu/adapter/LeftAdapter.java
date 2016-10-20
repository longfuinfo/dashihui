package com.dashihui.afford.thirdapi.choosemenu.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dashihui.afford.R;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.ui.adapter.AdapterBase;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.string.UtilString;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LeftAdapter extends AdapterBase<Map<String, Object>> {

    private BaseActivity mContext;
    //	private List<Map<String, Object>> mListData;
    private LinkedList<Map<String, Object>> mArrayData;
    private int selectedPos = -1;
    private String selectedText = "";
    //	private int normalDrawbleId;
//	private Drawable selectedDrawble;
    private float textSize;
    private OnClickListener onClickListener;
    private OnItemClickListener mOnItemClickListener;


    public LeftAdapter(BaseActivity context, List<Map<String, Object>> listData) {
        super(context, listData);
        mContext = context;
        init();

    }

    private void init() {
        onClickListener = new OnClickListener() {

            @Override
            public void onClick(View view) {
                selectedPos = (Integer) view.getTag();
                setSelectedPosition(selectedPos);
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, selectedPos);
                }
            }
        };
    }

    /**
     * @param context
     * @param arrayData
     */
    public LeftAdapter(BaseActivity context, LinkedList<Map<String, Object>> arrayData) {
        super(context, arrayData);
        mContext = context;
        mArrayData = arrayData;
        init();
    }

    /**
     * 设置选中的position,并通知列表刷新
     */
    public void setSelectedPosition(int pos) {
        if (!UtilList.isEmpty(mList) && pos < mList.size()) {
            selectedPos = pos;
            selectedText = mList.get(pos).get("NAME") + "";
            notifyDataSetChanged();
        } else if (!UtilList.isEmpty(mList) && pos < mArrayData.size()) {
            selectedPos = pos;
            selectedText = mArrayData.get(pos).get("NAME") + "";
            notifyDataSetChanged();
        }

    }



    /**
     * 设置选中的position,但不通知刷新
     */
    public void setSelectedPositionNoNotify(int pos) {
        selectedPos = pos;
        if (!UtilList.isEmpty(mList) && pos < mList.size()) {
            selectedText = mList.get(pos).get("NAME") + "";
        } else if (mArrayData != null && pos < mArrayData.size()) {
            selectedText = mArrayData.get(pos).get("NAME") + "";
        }
    }

    /**
     * 获取选中的position
     */
    public int getSelectedPosition() {
        if (mArrayData != null && selectedPos < mArrayData.size()) {
            return selectedPos;
        }
        if (!UtilList.isEmpty(mList) && selectedPos < mList.size()) {
            return selectedPos;
        }

        return -1;
    }

    /**
     * 设置列表字体大小
     */
    public void setTextSize(float tSize) {
        textSize = tSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.from(mContext).inflate(R.layout.third_choose_left_item, parent, false);
            viewHolder = new ViewHolder(mContext, mList);
            //依赖注入初始化
            ViewUtils.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
            viewHolder.refresh(position, mArrayData, selectedText, textSize, onClickListener);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.update(position, mList, mArrayData, selectedText, textSize, onClickListener);
        }


        return convertView;
    }

    /**
     * @author p
     */
    static class ViewHolder {
        @ViewInject(R.id.relyt)
        RelativeLayout raLyout;
        @ViewInject(R.id.tv_title)
        TextView txtView;
        @ViewInject(R.id.count)
        TextView txtViewCount;
        private int[] backgroundImg = {
                R.drawable.store_img_classify_fruit,
                R.drawable.store_img_classify_drink,
                R.drawable.store_img_classify_oils,
                R.drawable.store_img_classify_life
        };


        private Activity mContent;
        private List<Map<String, Object>> mListMap;

        public ViewHolder(Activity context, List<Map<String, Object>> _listMap) {
            mContent = context;
            mListMap = _listMap;
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
        public void refresh(final int position, LinkedList<Map<String, Object>> arrayData,
                            String selectedText, float tSize, OnClickListener onClickListener) {
            if (!UtilList.isEmpty(mListMap)) {
//			txtView.setText(mListMap.get(position).get("SELLPRICE") + "");
                txtViewCount.setText(mListMap.get(position).get("GOODSCOUNT") + "");
                raLyout.setTag(position);
                String mString = "";
                if (!UtilList.isEmpty(mListMap)) {
                    if (position < mListMap.size()) {
                        mString = mListMap.get(position).get("NAME") + "";
                    }
                } else if (arrayData != null) {
                    if (position < arrayData.size()) {
                        mString = arrayData.get(position).get("NAME") + "";
                    }
                }
                if (!UtilString.isEmpty(mString)) {
                    if (mString.contains("全部"))
                        txtView.setText("全部");
                    else
                        txtView.setText(mString);
                    //左边添加图片
                    Drawable drawable = mContent.getResources().getDrawable(backgroundImg[position]);
//                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
                    txtView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    txtView.setCompoundDrawablePadding(20);//设置图片和文字之间的距离
                    txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, tSize);
                }
                if (selectedText != null && selectedText.equals(mString)) {
                    LogUtils.e("onRefresh======selectedText====设置选中的背景图=====>" + selectedText);
                    LogUtils.e("onRefresh======position====设置选中的背景图=====>" + position);
                    raLyout.setBackgroundResource(R.color.white);

//                raLyout.setBackgroundDrawable(mContent.getResources().getDrawable(R.drawable.choose_item_selected));//设置选中的背景图片
                } else {
                    raLyout.setBackgroundResource(R.color.defaultBg);
//                raLyout.setBackgroundDrawable(mContent.getResources().getDrawable(R.drawable.choose_eara_item_selector));//设置选中的背景图片

                }
                txtView.setPadding(20, 0, 0, 0);
                raLyout.setOnClickListener(onClickListener);
            } else {
                LogUtils.e("refresh======null=========>" + mListMap);
            }

        }

        /**
         * 更新
         */
        public void update(final int position, List<Map<String, Object>> _listMap, LinkedList<Map<String, Object>> arrayData,
                           String selectedText, float tSize, OnClickListener onClickListener) {
            refreshList(_listMap);
            refresh(position, arrayData, selectedText, tSize, onClickListener);
        }
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    /**
     * 重新定义菜单选项单击接口
     */
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

}
