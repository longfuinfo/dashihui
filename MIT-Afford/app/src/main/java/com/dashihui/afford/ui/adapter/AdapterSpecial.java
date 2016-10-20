package com.dashihui.afford.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dashihui.afford.R;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.ui.activity.AtyNetWork;
import com.dashihui.afford.ui.activity.AtyHome;
import com.dashihui.afford.ui.activity.AtyHomeSpecialList;
import com.dashihui.afford.ui.activity.shop.AtyAffordShop;
import com.dashihui.afford.ui.activity.shop.AtyAffordShopDetail;
import com.dashihui.afford.util.UtilCommon;
import com.dashihui.afford.util.list.UtilList;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshHorizontalScrollView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.Map;

public class AdapterSpecial extends AdapterBase<Map<String, Object>> {
    private List<Map<String, Object>> mSpecialListMap;
    private Activity mContext;
//    private int[] mItem;
    private Intent mSpecialIntent;

    public AdapterSpecial(Activity context, List<Map<String, Object>> dataObjects) {
        super(context, dataObjects);
        this.mContext = context;
        this.mSpecialListMap = dataObjects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.aty_home_listview_item, null);
            viewHolder = new ViewHolder(mContext, mSpecialListMap);
            //依赖注入初始化
            ViewUtils.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
            viewHolder.refresh(position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            //每次更新最新数据
            viewHolder.update(position, mSpecialListMap);
        }
        viewHolder.mLytSpecialName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UtilCommon.isNetworkAvailable(mContext)){
                    if (!UtilList.isEmpty(mSpecialListMap)) {
                        if (mSpecialIntent == null) {
                            mSpecialIntent = new Intent(mContext, AtyHomeSpecialList.class);
                        }
                        mSpecialIntent.putExtra(AtyHome.TAGCODE, mSpecialListMap.get(position).get(AtyHome.TAGCODE) + "");
                        mSpecialIntent.putExtra(AtyHome.TAGNAME, mSpecialListMap.get(position).get(AtyHome.TAGNAME) + "");
                        mContext.startActivity(mSpecialIntent);
                    } else {
                        Toast.makeText(mContext, "服务器繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    mContext.startActivity(new Intent(mContext,AtyNetWork.class));
                }
            }
        });
        return convertView;
    }


    /**
     * @author p
     */
    static class ViewHolder {
        @ViewInject(R.id.specialName)
        private TextView mSpecialName;
        @ViewInject(R.id.item_gallery)
        private PullToRefreshHorizontalScrollView mGalleryItem;

        @ViewInject(R.id.lyt_special_Name)
        private LinearLayout mLytSpecialName;
        @ViewInject(R.id.itemlyout)
        private LinearLayout itemlyout;
        private Activity mContent;
        private LinearLayout mLytItem;
        @ViewInject(R.id.txtLine)
        private TextView mTxtLine;


        private Intent mIntent;
        private int mPosition;
//        private int mPos;
//        private List<Integer> mItem = new ArrayList<>();
        private List<Map<String, Object>> mSpecialListMap;
        private Activity mContext;
        private List<Map<String, Object>> mItemlistMap;


        public ViewHolder(Activity context, List<Map<String, Object>> dataObjects) {
            mContent = context;
            this.mContext = context;
            this.mSpecialListMap = dataObjects;

        }

        /**
         * 更新最新数据
         *
         * @param _listMap
         */
        public void refreshList(List<Map<String, Object>> _listMap) {
            mSpecialListMap = _listMap;
        }

        /**
         * 第一次刷新
         */
        public void refresh(final int position) {
            mPosition = position;
            if (!UtilList.isEmpty(mSpecialListMap)){
                if (position ==(mSpecialListMap.size()-1)){
                    mTxtLine.setVisibility(View.GONE);
                }else {
                    mTxtLine.setVisibility(View.VISIBLE);
                }
                mGalleryItem.setMode(PullToRefreshBase.Mode.MANUAL_REFRESH_ONLY);
                //创建一个 SpannableString对象
                SpannableString msp = new SpannableString(mSpecialListMap.get(mPosition).get("TAGNAME") + "");
                //设置字体前景色为洋红色
                msp.setSpan(new ForegroundColorSpan(Color.parseColor("#e9423a")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //2.0f表示默认字体大小的两倍
                msp.setSpan(new RelativeSizeSpan(1.5f), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //设置字体样式粗体
                msp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            //设置字体样式粗斜体
//            msp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mSpecialName.setText(msp);
                mItemlistMap = (List<Map<String, Object>>) mSpecialListMap.get(mPosition).get("LIST");
                itemlyout.removeAllViews();
                for (int j = 0; j < mItemlistMap.size(); j++) {
                    final int mPos=j;
                    LayoutInflater inflater = mContext.getLayoutInflater();
                    View convertVieitem = inflater.inflate(R.layout.aty_home_gallery_item, null);
                    ImageView mImage = (ImageView) convertVieitem.findViewById(R.id.image);//商品图
                    TextView mTvName = (TextView) convertVieitem.findViewById(R.id.tv_name);//商品名
                    TextView mTvNewPrice = (TextView) convertVieitem.findViewById(R.id.tv_price);//优惠后价
                    TextView mTvOldPrice = (TextView) convertVieitem.findViewById(R.id.tv_oldprice);//售价
                    mLytItem = (LinearLayout) convertVieitem.findViewById(R.id.lyt_item);//售价


                    mTvName.setText(mItemlistMap.get(j).get("NAME") + "");
                    Glide.with(mContent)
                            .load(AffConstans.PUBLIC.ADDRESS_IMAGE + mItemlistMap.get(j).get("THUMB") + "")
                            .placeholder(R.drawable.default_list)
                            .error(R.drawable.default_list)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(mImage);
                    mTvNewPrice.setText("￥" + mItemlistMap.get(j).get("SELLPRICE") + "");
                    mTvOldPrice.setText("￥" + mItemlistMap.get(j).get("MARKETPRICE") + "");
                    mTvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线

                    itemlyout.addView(convertVieitem);
                    mLytItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (UtilCommon.isNetworkAvailable(mContext)){
                                if (!UtilList.isEmpty(mItemlistMap)) {
                                    if (mIntent == null) {
                                        mIntent = new Intent(mContent, AtyAffordShopDetail.class);
                                    }
                                    //便利店列表
                                    mIntent.putExtra(AtyAffordShopDetail.RETURN_ACTIVITY, CommConstans.SHOPDETAIL.RETURN_TAG0 + "");
                                    mIntent.putExtra(AtyAffordShop.INTENT_SHOP_ID, mItemlistMap.get(mPos).get("ID") + "");
                                    mIntent.putExtra(AtyAffordShop.INTENT_SHOP_IMG, mItemlistMap.get(mPos).get("THUMB") + "");
                                    mContent.startActivity(mIntent);

                                } else {
                                    Toast.makeText(mContent, "服务器繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                mContext.startActivity(new Intent(mContext,AtyNetWork.class));
                            }
                        }
                    });
                }
            }else{
                LogUtils.e("refresh===Error=====null=========>" + mSpecialListMap + "");
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
