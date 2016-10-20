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
public class AdapterFrgOneBuy extends AdapterBase<Map<String, Object>> {

    public AdapterFrgOneBuy(Activity context, List<Map<String, Object>> _List) {
        super(context, _List);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.frag_onebuy_item, null);
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
        LogUtils.e("赋值==========444444444======viewHolder=== 赋值=======>");

        return convertView;
    }

    /**
     * @author p
     */
    static class ViewHolder {
        @ViewInject(R.id.my_onbuy_goodsimg)
        private ImageView mOneImg;
        @ViewInject(R.id.my_onbuy_goodsname)
        private TextView mTvOneName;
        @ViewInject(R.id.my_onbuy_price)
        private TextView mTvOneFavPrice;
        @ViewInject(R.id.my_onbuy_oldprice)
        private TextView mTvOneOldPrice;
        @ViewInject(R.id.onbuy_shopping)
        private Button mBtnOneShopping;

        private Activity mContent;
        private List<Map<String, Object>> mListMap;
        private int mPosition;
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
            mPosition = position;
            if (!UtilList.isEmpty(mListMap)){
                mTvOneName.setText(mListMap.get(position).get("NAME") + "");
                Glide.with(mContent)
                        .load(AffConstans.PUBLIC.ADDRESS_IMAGE + mListMap.get(position).get("THUMB") + "")
                        .placeholder(R.drawable.default_list)
                        .error(R.drawable.default_list)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mOneImg);
                mTvOneFavPrice.setText(mListMap.get(position).get("SELLPRICE") + "");
                mTvOneOldPrice.setText(mListMap.get(position).get("MARKETPRICE") + "");
                mTvOneOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
            }else {
                LogUtils.e("error===========>");
            }
        }

        /**
         * 更新
         */
        public void update(final int position,List<Map<String, Object>> _listMap) {
            refreshList(_listMap);
            refresh(position);
        }

        //立即抢购
        @OnClick(R.id.onbuy_shopping)
        public void onOneShoppingClick(View v) {
            Intent intent = new Intent(mContent, AtyAffordShopDetail.class);
            intent.putExtra(AtyAffordShop.INTENT_SHOP_ID, mListMap.get(mPosition).get("ID") + "");
            mContent.startActivity(intent);
        }
    }

}
