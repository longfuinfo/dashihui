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
 * Created by NiuFC on 2015/11/15.
 */
public class AdapterServerList extends AdapterBase<Map<String, Object>> {
    public AdapterServerList(Activity context, List<Map<String, Object>> _mList) {
        super(context, _mList);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.aty_server_list_item, null);
            viewHolder = new ViewHolder(mContext, mList);
            //依赖注入初始化
            ViewUtils.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
            viewHolder.refresh(position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.update(position, mList);

        }
        return convertView;
    }

    /**
     * @author p
     */
    static class ViewHolder {
        @ViewInject(R.id.left_img)
        private ImageView mShopImg;//店铺图片

        @ViewInject(R.id.tv_name)
        private TextView mTextName;//店铺名称

        @ViewInject(R.id.tv_present)
        private TextView mComment;//服务介绍

        @ViewInject(R.id.tv_price)
        private TextView mTextPrice;//服务价格

        @ViewInject(R.id.tv_oldprice)
        private TextView mTextOldPrice;//服务原价

        @ViewInject(R.id.tv_nums)
        private TextView mTextServerNums;//服务次数

        private Activity mContent;
        private List<Map<String, Object>> mListMap;
        private int mPosition;

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
                LogUtils.e("position================>" + position);
                Glide.with(mContent)
                        .load(AffConstans.PUBLIC.ADDRESS_IMAGE + mListMap.get(position).get("THUMB") + "")
                        .placeholder(R.drawable.default_list)
                        .error(R.drawable.default_list)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mShopImg);
                mTextName.setText(mListMap.get(position).get("NAME") + "");
                mComment.setText(mListMap.get(position).get("COMMENT") + "");
//            mTextPrice.setText("￥：" + mListMap.get(position).get("SELLPRICE"));
//            mTextOldPrice.setText("原价：￥" + mListMap.get(position).get("MARKETPRICE"));
//            mTextOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//删除线并加清
                mTextServerNums.setText(mListMap.get(position).get("TOTAL") + "次");
                LogUtils.e("refresh==========mListMap======>" + mListMap);
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

        /**
         * 列表item监听，跳至服务详情页
         *
         * @param v
         */
        @OnClick(R.id.itemLyt)
        public void OnCheckClick(View v) {
            if (!UtilList.isEmpty(mListMap)){
                Intent intent = new Intent(mContent, AtyServerDetail.class);
                intent.putExtra(AtyServerList.INTENT_SERVER_ID, mListMap.get(mPosition).get("ID") + "");
                intent.putExtra(AtyServerList.INTENT_SERVER_THUMB, mListMap.get(mPosition).get("THUMB") + "");
                mContent.startActivity(intent);
            }else {
                Toast.makeText(mContent,"请稍等...",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
