package com.dashihui.afford.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessOrder;
import com.dashihui.afford.common.base.BaseFragment;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.ui.activity.AtyEvaluate;
import com.dashihui.afford.ui.activity.AtyOrderState;
import com.dashihui.afford.ui.activity.AtyOrdertDetail;
import com.dashihui.afford.ui.activity.AtySettlementOrder;
import com.dashihui.afford.ui.activity.fragment.FragmentOrder;
import com.dashihui.afford.ui.widget.WgtAlertDialog;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.number.UtilNumber;
import com.dashihui.afford.util.toast.UtilToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;
import java.util.Map;

/**
 * 待评价
 * @author NiuFC
 * @version 1.0
 * @date 2015-10-30
 */
public class AdapterFrgOrderEvaluate extends AdapterBase<Map<String, Object>> {
    private FragmentOrder mFragmentOrder;
    private List<Map<String, Object>> mListMap;

    public AdapterFrgOrderEvaluate(Activity context, BaseFragment fragment, List<Map<String, Object>> _List) {
        super(context, _List);
        this.mListMap = _List;
        mFragmentOrder = (FragmentOrder) fragment;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.frag_order_evaluate_item, null);
            viewHolder = new ViewHolder(mContext, mFragmentOrder, mListMap);
            //依赖注入初始化
            ViewUtils.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
            viewHolder.refresh(position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.update(position, mListMap);
        }

        viewHolder.lytPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(mContext, AtyOrdertDetail.class);
                mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, mListMap.get(position).get("AMOUNT") + "");
                mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, mListMap.get(position).get("ORDERNUM") + "");
                mContext.startActivity(mapIntent);
            }
        });

        return convertView;
    }


    /**
     * @author p
     */
    static class ViewHolder {

        @ViewInject(R.id.store_name)
        private TextView mStoreName;

        @ViewInject(R.id.left_img_waitdeliver)
        ImageView mImgView;
        @ViewInject(R.id.tv_name_waitdeliver)
        TextView mTxtViewName;
        @ViewInject(R.id.tv_price_waitdeliver)
        TextView mTxtViewPrice;
        @ViewInject(R.id.lytPrice)
        LinearLayout lytPrice;
        @ViewInject(R.id.all_order_track)
        private Button mOrderTrackBtn;//订单跟踪
        @ViewInject(R.id.btnOrderEvaluate)
        private Button mBtnEvaluate;//去评价

        @ViewInject(R.id.luoutImgArray)
        private LinearLayout luoutImgArray;
        @ViewInject(R.id.imgGoods01)//商品图片
        private ImageView mImgGoods01;
        @ViewInject(R.id.imgGoods02)//商品图片
        private ImageView mImgGoods02;
        @ViewInject(R.id.imgGoods03)//商品图片
        private ImageView mImgGoods03;
        @ViewInject(R.id.imgGoods04)//商品图片
        private ImageView mImgGoods04;

        private BusinessOrder mBllOrder;
        private Activity mContent;
        private List<Map<String, Object>> mListMap;
        private int mPosition = -1;
        private String mOrderNums;

        public ViewHolder(Activity context, BaseFragment fragment, List<Map<String, Object>> _listMap) {
            this.mContent = context;
            this.mListMap = _listMap;
            mBllOrder = new BusinessOrder(fragment);
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
                //根据支付方式、支付状态判断要显示的按钮
                mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                mBtnEvaluate.setVisibility(View.VISIBLE);//去评价

                List<Map<String, Object>> listMaps = (List<Map<String, Object>>) mListMap.get(position).get("GOODSLIST");
                if (!UtilList.isEmpty(listMaps)) {
                    if (listMaps.size()==1){
                        luoutImgArray.setVisibility(View.GONE);
                        mTxtViewName.setVisibility(View.VISIBLE);
                        mImgView.setVisibility(View.VISIBLE);
                        mTxtViewName.setText(listMaps.get(0).get("NAME") + "");
                        Glide.with(mContent)
                                .load(AffConstans.PUBLIC.ADDRESS_IMAGE + listMaps.get(0).get("THUMB") + "")
                                .placeholder(R.drawable.default_list)
                                .error(R.drawable.default_list)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(mImgView);
                    }else {
                        //清空
                        ImageView[] arrayImgView = {mImgGoods01,mImgGoods02,mImgGoods03,mImgGoods04};
                        mTxtViewName.setVisibility(View.GONE);
                        mImgView.setVisibility(View.GONE);
                        luoutImgArray.setVisibility(View.VISIBLE);
                        //先全部隐藏图标
                        if (listMaps.size()<3){
                            for (int k=0;k<=3;k++){
                                arrayImgView[k].setVisibility(View.GONE);
                            }
                        }
                        for (int i=0;i<listMaps.size();i++){
                            if (i<=3){
                                arrayImgView[i].setVisibility(View.VISIBLE);
                                Glide.with(mContent)
                                        .load(AffConstans.PUBLIC.ADDRESS_IMAGE + listMaps.get(i).get("THUMB") + "")
                                        .placeholder(R.drawable.default_list)
                                        .error(R.drawable.default_list)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(arrayImgView[i]);
                            }else {
                                LogUtils.e("refresh============>" + listMaps + "");
                            }
                        }
                    }
                } else {
                    LogUtils.e("refresh===Error=====null=========>" + listMaps + "");
                }
                if((mListMap.get(position).get("ISSELF")+"").equals("0")){
                    mStoreName.setText(mListMap.get(position).get("STORENAME")+"");
                }else{
                    mStoreName.setText("大实惠直营");
                }
                mTxtViewPrice.setText("￥" + mListMap.get(position).get("REALPAY"));
                mOrderNums = mListMap.get(position).get("ORDERNUM") + "";
            }else {
                LogUtils.e("refresh========null=========>" + mListMap );
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
         * 催单
         */
        @OnClick(R.id.tv_btn_urged)
        public void onUrgedClick(View view) {
            final WgtAlertDialog mAtDialog = new WgtAlertDialog();
            mAtDialog.show(mContent,
                    "取消", "确定",
                    "确认催单？",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mAtDialog.dismiss();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mListMap.get(mPosition).get("ORDERNUM") != null) {
                                mBllOrder.urge(mListMap.get(mPosition).get("ORDERNUM") + "");//提交服务器请求
                            } else {
                                UtilToast.show(mContent, "催单失败！", Toast.LENGTH_SHORT);
                            }
                            mAtDialog.dismiss();
                        }
                    });
        }


        /**
         * 取消订单提示
         */
        @OnClick(R.id.btn_cancel)
        public void onBtnCancelClick(View v) {
            final WgtAlertDialog mAtDialog = new WgtAlertDialog();
            mAtDialog.show(mContent, "取消", "确定", "确认取消当前订单？", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAtDialog.dismiss();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListMap.get(mPosition).get("ORDERNUM") + "" != null) {
                        mBllOrder.doCancelOrder(mListMap.get(mPosition).get("ORDERNUM") + "");//点击确定，提交服务器请求取消订单
                    } else {
                        UtilToast.show(mContent, "取消订单失败", Toast.LENGTH_SHORT);
                    }
                    mAtDialog.dismiss();
                }
            });
        }

        @OnClick(R.id.btnOrderEvaluate)//去评价
        public void onEvaluateClick(View v) {
            //订单跟踪
            if (!UtilList.isEmpty(mListMap)){
                Intent mapIntent = new Intent(mContent, AtyEvaluate.class);
                mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, mListMap.get(mPosition).get("AMOUNT") + "");
                mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, mListMap.get(mPosition).get("ORDERNUM") + "");
                mContent.startActivity(mapIntent);
            }else {
                LogUtils.e("OrderTrack=======else====订单跟踪=============>");
            }

        }
        @OnClick(R.id.all_order_track)//订单跟踪
        public void onOrderTrackClick(View v) {
            //订单跟踪
            if (!UtilList.isEmpty(mListMap)){
                Intent mapIntent = new Intent(mContent, AtyOrderState.class);
                //支付方式 1：在线支付，2：货到付款
                mapIntent.putExtra(AtyOrderState.PAYTYPE, mListMap.get(mPosition).get("PAYTYPE") + "");
                //收货方式  1：送货，2：自取
                mapIntent.putExtra(AtyOrderState.TAKETYPE, mListMap.get(mPosition).get("TAKETYPE") + "");

                mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, mListMap.get(mPosition).get("AMOUNT") + "");
                mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, mListMap.get(mPosition).get("ORDERNUM") + "");
                mContent.startActivity(mapIntent);
            }else {
                LogUtils.e("OrderTrack=======else====订单跟踪=============>");
            }


        }
    }
}


