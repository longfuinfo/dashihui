package com.dashihui.afford.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dashihui.afford.R;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.util.list.UtilList;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/17.
 */
public class AdapterServer extends AdapterBase<Map<String, Object>> {
    private List<Map<String, Object>> mListMap;

    public AdapterServer(Activity context, List<Map<String, Object>> _List) {
        super(context, _List);
        this.mListMap = _List;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.aty_server_item, null);
            viewHolder = new ViewHolder(mContext, mListMap);
            //依赖注入初始化
            ViewUtils.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
            viewHolder.refresh(position);
//            LogUtils.e("getView======第一次==============>" + position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
//            LogUtils.e("getView======其他==============>" + position);
            //每次更新最新数据
            viewHolder.update(position, mListMap);
        }
        return convertView;
    }

    static class ViewHolder {

        @ViewInject(R.id.tvImg)
        private TextView mTvName;
        @ViewInject(R.id.Image)
        private ImageView mImage;
        private Activity mContent;
        private List<Map<String, Object>> mListMap;

        public ViewHolder(Activity context, List<Map<String, Object>> _listMap) {
            this.mContent = context;
            this.mListMap = _listMap;
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
                mTvName.setText(mListMap.get(position).get("NAME")+"");
                Glide.with(mContent)
                        .load(AffConstans.PUBLIC.ADDRESS_IMAGE + mListMap.get(position).get("ICON") + "")
                        .placeholder(R.drawable.default_list)
                        .error(R.drawable.default_list)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mImage);
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
