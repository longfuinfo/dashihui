package com.dashihui.afford.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.ui.activity.AtyLocation;
import com.dashihui.afford.ui.activity.AtyNetWork;
import com.dashihui.afford.ui.activity.AtyHome;
import com.dashihui.afford.ui.activity.shop.AtyAffordShop;
import com.dashihui.afford.util.UtilCommon;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.number.UtilNumber;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;
import java.util.Map;

public class AdapterHomeGridView extends AdapterBase<Map<String, Object>> {

    private Activity mContext;
    private List<Map<String, Object>> mListMap;

    public AdapterHomeGridView(Activity context, List<Map<String, Object>> list) {
        super(context,list);
        this.mContext = context;
        this.mListMap = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.aty_home_gridview_item, null);
            viewHolder = new ViewHolder(mContext, mListMap);
            //依赖注入初始化
            ViewUtils.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
            viewHolder.refresh(position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            //每次更新最新数据
            viewHolder.update(position, mListMap);
        }

        return convertView;
    }

    static class ViewHolder {
        @ViewInject(R.id.tvName)
        private TextView mTvName;
        @ViewInject(R.id.Image)
        private ImageView mImage;

        private Intent mIntent;
        private int mPosition;
        private Activity mContext;
        private List<Map<String, Object>> mListMap;

        public ViewHolder(Activity context, List<Map<String, Object>> _listMap) {
            this.mContext = context;
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
            mPosition = position;
            if (!UtilList.isEmpty(mListMap)){
                mTvName.setText(mListMap.get(position).get(AtyHome.TAGNAME) + "");
                String uri = mListMap.get(position).get(AtyHome.IMAGE) + "";
                mImage.setImageResource(UtilNumber.IntegerValueOf(uri));
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

        @OnClick(R.id.lyt_item)
        public void onGridLytItemClick(View view) {
            if (UtilCommon.isNetworkAvailable(mContext)){
                Intent intent = new Intent(mContext, AtyAffordShop.class);
                if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance().getEntityLocation().getCOMMUNITY() != null) {
//                LogUtils.e("onGridLytItemClick=======TAGNAME=======>" + mListMap.get(mPosition).get(AtyHome.TAGNAME) + "");
//                LogUtils.e("onGridLytItemClick=======CATEGORYONCODE=======>" + mListMap.get(mPosition).get(AtyHome.CATEGORYONCODE)+"");
                    intent.putExtra(AtyHome.INTENT_TAG_TYPENAME, mListMap.get(mPosition).get(AtyHome.TAGNAME)+"");
                    intent.putExtra(AtyHome.INTENT_TAG_TYPE, mListMap.get(mPosition).get(AtyHome.CATEGORYONCODE) + "");
                    mContext.startActivity(intent);
                } else {
                    mContext.startActivity(new Intent(mContext,AtyLocation.class));
                }
            }else {
                mContext.startActivity(new Intent(mContext,AtyNetWork.class));
            }

        }
    }
}

