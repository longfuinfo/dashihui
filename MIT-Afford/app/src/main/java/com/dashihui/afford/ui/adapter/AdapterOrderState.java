package com.dashihui.afford.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
public class AdapterOrderState extends AdapterBase<Map<String, Object>> {

    public AdapterOrderState(Activity context, List<Map<String, Object>> _List) {
        super(context, _List);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.frag_order_state_item, null);
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
        @ViewInject(R.id.ord_txt_deta)
        TextView mTxtViewDate;
        @ViewInject(R.id.ord_txt_time)
        TextView mTxtViewTime;
        @ViewInject(R.id.image)
        ImageView imageView;
        @ViewInject(R.id.txt_line_1)
        View line1;
        @ViewInject(R.id.txt_line_2)
        View line2;


        private Activity mContent;
        private List<Map<String, Object>> mListMap;
        public ViewHolder(Activity context,List<Map<String, Object>> _listMap) {
            mContent = context;
            mListMap = _listMap;
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
            if (!UtilList.isEmpty(mListMap)){

                if(position == 0){
                    imageView.setImageResource(R.drawable.tail_history_cur);
                    mTxtViewDate.setText(mListMap.get(position).get("CONTENT") + "");
                    mTxtViewTime.setText(mListMap.get(position).get("CREATEDATE") + "");
                    line1.setVisibility(View.INVISIBLE);
                }
                else if ((mListMap.size()-1)==position){
                   mTxtViewDate.setText(mListMap.get(position).get("CONTENT") + "");
                   mTxtViewTime.setText(mListMap.get(position).get("CREATEDATE") + "");
                   line2.setVisibility(View.INVISIBLE);

                }
                else {

                    mTxtViewDate.setText(mListMap.get(position).get("CONTENT") + "");
                    mTxtViewTime.setText(mListMap.get(position).get("CREATEDATE") + "");
                    LogUtils.e("refresh=================>" + mListMap.get(position).get("CONTENT") + "");
                }

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
