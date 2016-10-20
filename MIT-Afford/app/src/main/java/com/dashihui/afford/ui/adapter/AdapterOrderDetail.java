package com.dashihui.afford.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dashihui.afford.R;
import com.dashihui.afford.util.list.UtilList;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.Map;

/**
 * @author NiuFC
 * @version 1.0
 * @date 2015-10-30
 */
public class AdapterOrderDetail extends AdapterBase<Map<String, String>> {

    public AdapterOrderDetail(Activity context, List<Map<String, String>> _List) {
        super(context, _List);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.aty_orderdetail_item, null);
            viewHolder = new ViewHolder(mContext,mList);
            //依赖注入初始化
            ViewUtils.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
            viewHolder.refresh(position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.update(position,mList);
        }


        return convertView;
    }

    /**
     * @author p
     */
    static class ViewHolder {
        @ViewInject(R.id.name)
        TextView mTxtViewName;
        @ViewInject(R.id.date)
        TextView mTxtViewDate;


        private Activity mContent;
        private List<Map<String, String>> mListMap;
        public ViewHolder(Activity context,List<Map<String, String>> _listMap) {
            mContent = context;
            mListMap = _listMap;
        }

        /**
         *  更新最新数据
         * @param _listMap
         */
        public void refreshList(List<Map<String, String>> _listMap){
            mListMap = _listMap;
        }
        /**
         * 第一次刷新
         */
        public void refresh(final int position) {
            if (!UtilList.isEmpty(mListMap)){
                mTxtViewName.setText(mListMap.get(position).get("name") + "");
                mTxtViewDate.setText(mListMap.get(position).get("content") + "");
            }else{
                LogUtils.e("refresh===Error=====null=========>" + mListMap + "");
            }

        }

        /**
         * 更新
         */
        public void update(final int position,List<Map<String, String>> _listMap) {
            refreshList(_listMap);
            refresh(position);
        }
    }

}
