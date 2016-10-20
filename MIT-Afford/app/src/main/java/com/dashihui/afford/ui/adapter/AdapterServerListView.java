package com.dashihui.afford.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dashihui.afford.R;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.ui.activity.server.AtyServerDetail;
import com.dashihui.afford.ui.activity.server.AtyServerList;
import com.dashihui.afford.util.list.UtilList;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;
import java.util.Map;

/**
 * @author NiuFC
 * @version 1.0
 * @date 2015-10-30
 */
public class AdapterServerListView extends AdapterBase<Map<String, Object>> {

    private List<Map<String, Object>> mListMap;

    public AdapterServerListView(Activity context, List<Map<String, Object>> _List) {
        super(context, _List);
        this.mListMap = _List;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.aty_server_list_item, null);
            viewHolder = new ViewHolder(mContext, mListMap);
            //依赖注入初始化
            ViewUtils.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
            viewHolder.refresh(position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.update(position, mListMap);
        }
        return convertView;
    }

    /**
     * @author p
     */
    static class ViewHolder {
        @ViewInject(R.id.left_img)
        ImageView mImage;
        @ViewInject(R.id.tv_name)
        TextView mTvName;
        @ViewInject(R.id.tv_nums)
        TextView mTvNums;
        @ViewInject(R.id.tv_present)
        TextView mTvPresent;

        private int mPosition;
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
        public void refresh(final int position) {
            mPosition = position;
            if (!UtilList.isEmpty(mListMap)){
                Glide.with(mContent)
                        .load(AffConstans.PUBLIC.ADDRESS_IMAGE + mListMap.get(mPosition).get("THUMB") + "")
                        .placeholder(R.drawable.default_list)
                        .error(R.drawable.default_list)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mImage);
                mTvName.setText(mListMap.get(mPosition).get("NAME") + "");
                mTvNums.setText(mListMap.get(mPosition).get("TOTAL") + "次");
                mTvPresent.setText(mListMap.get(mPosition).get("COMMENT") + "");
            }else {
                LogUtils.e("refresh=======null====>");
            }
        }

        /**
         * 更新
         */
        public void update(final int position, List<Map<String, Object>> _listMap) {
            refreshList(_listMap);
            refresh(position);
        }

        /**
         * 列表item监听，跳至服务详情页
         *
         * @param v
         */
        @OnClick(R.id.itemLyt)
        public void OnCheckClick(View v) {
            if (!UtilList.isEmpty(mListMap)) {
                Intent intent = new Intent(mContent, AtyServerDetail.class);
                intent.putExtra(AtyServerList.INTENT_SERVER_ID, mListMap.get(mPosition).get("ID") + "");
                intent.putExtra(AtyServerList.INTENT_SERVER_THUMB, mListMap.get(mPosition).get("THUMB") + "");
                mContent.startActivity(intent);
            } else {
                Toast.makeText(mContent, "请稍等...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
