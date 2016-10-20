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
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;
import java.util.Map;

/**
 * @author NiuFC
 * @version 1.0
 * @date 2015-10-30
 */
public class AdapterFrgLimit extends AdapterBase<Map<String, Object>> {

    public AdapterFrgLimit(Activity context, List<Map<String, Object>> _List) {
        super(context, _List);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.frag_limit_item, null);
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

            //立即抢购
            viewHolder.mBtnLimitShopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AtyAffordShopDetail.class);
                    intent.putExtra(AtyAffordShop.INTENT_SHOP_ID, mList.get(position).get("ID") + "");
                    mContext.startActivity(intent);
                }
            });


        return convertView;
    }

    /**
     * @author p
     */
    static class ViewHolder {
        @ViewInject(R.id.my_limit_goodsimg)
        private ImageView mLimitImg;
        @ViewInject(R.id.my_limit_goodsname)
        private TextView mTvLimitName;
        @ViewInject(R.id.my_concern_price)
        private TextView mTvLimitFavPrice;
        @ViewInject(R.id.my_limit_oldprice)
        private TextView mTvLimitOldPrice;
        @ViewInject(R.id.limit_shopping)
        private Button mBtnLimitShopping;

        private Activity mContent;
        private List<Map<String, Object>> mListMap;
        private int mPositon = -1;
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
            mPositon = position;
            /***********  赋值 ***********/
            if (!UtilList.isEmpty(mListMap)){
                mTvLimitName.setText(mListMap.get(position).get("NAME") + "");
                Glide.with(mContent)
                        .load(AffConstans.PUBLIC.ADDRESS_IMAGE + mListMap.get(position).get("THUMB") + "")
                        .placeholder(R.drawable.default_list)
                        .error(R.drawable.default_list)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mLimitImg);
                mTvLimitFavPrice.setText(mListMap.get(position).get("SELLPRICE") + "");
                mTvLimitOldPrice.setText(mListMap.get(position).get("MARKETPRICE") + "");
                mTvLimitOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
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
        @OnClick(R.id.limit_shopping)
        public void onClick(View v) {
            Intent intent = new Intent(mContent, AtyAffordShopDetail.class);
            if (mPositon >= 0){
                intent.putExtra(AtyAffordShop.INTENT_SHOP_ID, mListMap.get(mPositon).get("ID") + "");
                mContent.startActivity(intent);
            }
        }
    }

}
