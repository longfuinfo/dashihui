package com.dashihui.afford.ui.adapter;

import android.app.Activity;
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

import java.util.List;
import java.util.Map;

/**
 * @author NiuFC
 * @version 1.0
 * @date 2015-10-30
 */
public class AdapterFrgServerTime extends AdapterBase<Map<String, Object>> {

private int mChoosePos = -1;
    public AdapterFrgServerTime(Activity context, List<Map<String, Object>> _List,int pos) {
        super(context, _List);
        this.mChoosePos = pos;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.frag_server_time_item, null);
            viewHolder = new ViewHolder(mContext,mList,mChoosePos);
            //依赖注入初始化
            ViewUtils.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
            viewHolder.refresh(position);
//            LogUtils.e("getView======第一次==============>"+position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
//            LogUtils.e("getView======其他==============>"+position);
            //每次更新最新数据
            viewHolder.update(position,mList);
        }
        //选中的item设置背景
        if (mChoosePos == position) {
            viewHolder.mLytDateItem.setBackgroundResource(R.drawable.btn_time_itembg_nomal);
        } else {
            viewHolder.mLytDateItem.setBackgroundResource(R.drawable.btn_time_itembg);
        }

        return convertView;
    }

    public void mChooseItem(int pos){
        mChoosePos = pos;
    }

    /**
     * @author p
     */
    static class ViewHolder {

        @ViewInject(R.id.lyt_date)
        LinearLayout mLytDateItem;
        @ViewInject(R.id.lyt_toptab)
        LinearLayout mLytTopTab;
        @ViewInject(R.id.dateDay)
        TextView txtViewTitle;
        @ViewInject(R.id.dateTime)
        TextView txtViewtime;

        private Activity mContent;
        private List<Map<String, Object>> mListMap;
        private int mPosition;
        public ViewHolder(Activity context,List<Map<String, Object>> _listMap,int pos) {
            mContent = context;
            mListMap = _listMap;
            this.mPosition = pos;
        }

        /**
         *  更新最新数据
         * @param _listMap
         */
        public void refreshList(List<Map<String, Object>> _listMap){
            mListMap = _listMap;
        }
        /**
         * 第一次刷新
         */
        public void refresh(final int position) {
            /***********  赋值 ***********/
            if (!UtilList.isEmpty(mListMap)){
                txtViewTitle.setText(mListMap.get(position).get("TITLE") + "");
                txtViewtime.setText(mListMap.get(position).get("DATETIME") + "");
            }else{
                LogUtils.e("refresh===Error=====null=========>" + mListMap + "");
            }

        }

        /**
         * 更新
         */
        public void update(final int position,List<Map<String, Object>> _listMap) {
            refreshList(_listMap);
            refresh(position);
        }
    }

}
