package com.dashihui.afford.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dashihui.afford.R;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.ui.activity.shop.AtyAffordShop;
import com.dashihui.afford.ui.activity.shop.AtyAffordShopDetail;
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
public class AdapterFrgRecomend extends AdapterBase<Map<String, Object>> {

    public AdapterFrgRecomend(Activity context, List<Map<String, Object>> _List) {
        super(context, _List);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.frag_recommend_item, null);
            viewHolder = new ViewHolder(mContext,mList);
            //依赖注入初始化
            ViewUtils.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
            viewHolder.refresh(position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.update(position,mList);
        }
        /***********  赋值 ***********/
        if (!UtilList.isEmpty(mList)){
            viewHolder.mTvRecName.setText(mList.get(position).get("NAME") + "");
            LogUtils.e("getView=====mList.size()=========>" + mList.size());
            LogUtils.e("getView=======name=======>" + mList.get(position).get("NAME") + "");
            Glide.with(mContext)
                    .load(AffConstans.PUBLIC.ADDRESS_IMAGE + mList.get(position).get("THUMB") + "")
                    .placeholder(R.drawable.default_list)
                    .error(R.drawable.default_list)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.mRecImg);
            viewHolder.mTvRecFavPrice.setText(mList.get(position).get("SELLPRICE") + "");
            viewHolder.mTvRecOldPrice.setText(mList.get(position).get("MARKETPRICE") + "");
            viewHolder.mTvRecOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
            //立即抢购
            viewHolder.mBtnRecShopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AtyAffordShopDetail.class);
                    intent.putExtra(AtyAffordShop.INTENT_SHOP_ID, mList.get(position).get("ID") + "");
                    mContext.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    /**
     * @author p
     */
    static class ViewHolder {
        @ViewInject(R.id.my_concern_goodsimg)
        private ImageView mRecImg;
        @ViewInject(R.id.my_concern_goodsname)
        private TextView mTvRecName;
        @ViewInject(R.id.my_concern_price)
        private TextView mTvRecFavPrice;
        @ViewInject(R.id.my_concern_oldprice)
        private TextView mTvRecOldPrice;
        @ViewInject(R.id.recommend_shopping)
        private Button mBtnRecShopping;

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
