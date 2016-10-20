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
 * @author NiuFC
 * @version 1.0
 * @date 2015-10-30
 */
public class AdapterFrgOrderNopay extends AdapterBase<Map<String, Object>> {
    private List<Map<String, Object>> mMapList;
    private FragmentOrder mFragmentOrder;
    private int i;

    public AdapterFrgOrderNopay(Activity context, BaseFragment fragment, List<Map<String, Object>> _List) {
        super(context, _List);
        this.mMapList = _List;
        mFragmentOrder = (FragmentOrder) fragment;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.frag_order_nopay_item, null);
            viewHolder = new ViewHolder(mContext, mFragmentOrder, mMapList);
            //依赖注入初始化
            ViewUtils.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
            viewHolder.refresh(position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.update(position, mMapList);
        }


        viewHolder.mBtnTopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(mContext, AtySettlementOrder.class);
                mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, mMapList.get(position).get("AMOUNT") + "");
                mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, mMapList.get(position).get("ORDERNUM") + "");
                mapIntent.putExtra(AtySettlementOrder.ORDER_PAY_TYPE, CommConstans.ORDER.ORDER_PAY);
                mContext.startActivity(mapIntent);
            }
        });
        viewHolder.lytPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(mContext, AtyOrdertDetail.class);
                mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, mMapList.get(position).get("AMOUNT") + "");
                mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, mMapList.get(position).get("ORDERNUM") + "");
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
        @ViewInject(R.id.goods_img)
        private ImageView imgView;
        @ViewInject(R.id.goods_name)
        private TextView txtViewName;
        @ViewInject(R.id.order_price)
        private TextView txtViewPrice;
        @ViewInject(R.id.goods_lyt)
        private LinearLayout lytPrice;

        @ViewInject(R.id.btn_topay)
        private Button mBtnTopay;
        @ViewInject(R.id.tv_btn_urged)
        private Button mBtnUrged;
        @ViewInject(R.id.btn_cancel)
        private Button mBtnCancel;//取消订单
        @ViewInject(R.id.all_order_track)
        private Button mOrderTrackBtn;//订单跟踪
        @ViewInject(R.id.all_order_waitpay)
        private TextView mOrderState;//订单状态

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

        private String mOrderNum;
        private BusinessOrder mBllOrder;

        private Activity mContent;
        private List<Map<String, Object>> mListMap;
        private int mPosition = -1;

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

        /*
         * 第一次刷新
         */
        public void refresh(final int position) {
            mPosition = position;

            if (!UtilList.isEmpty(mListMap)){
                //根据支付方式、支付状态判断要显示的按钮
                //支付状态1：待支付，2：已支付
                int payType = UtilNumber.IntegerValueOf(mListMap.get(position).get("PAYTYPE") + "");
                //收货方式1：送货，2：自取
                int takeType = UtilNumber.IntegerValueOf(mListMap.get(position).get("TAKETYPE") + "");
                /******在线付款+送货上门*/
                if (payType == CommConstans.SHOPORDERPAYTYPE.ONLINE && takeType == CommConstans.SHOPORDERTAKETYPE.DELIVER){
                    mBtnTopay.setVisibility(View.VISIBLE);
                    mBtnCancel.setVisibility(View.GONE);//取消订单
                    mBtnUrged.setVisibility(View.GONE);
                    mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                    /********在线付款+门店自取***********/
                }else if (payType == CommConstans.SHOPORDERPAYTYPE.ONLINE && takeType == CommConstans.SHOPORDERTAKETYPE.TAKESELF){
                    mBtnTopay.setVisibility(View.VISIBLE);
                    mBtnCancel.setVisibility(View.GONE);
                    mBtnUrged.setVisibility(View.GONE);
                    mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                }else {
                    mBtnTopay.setVisibility(View.GONE);
                    mBtnCancel.setVisibility(View.GONE);
                    mBtnUrged.setVisibility(View.GONE);
                    mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                }


                List<Map<String, Object>> listMaps = (List<Map<String, Object>>) mListMap.get(position).get("GOODSLIST");
                if (!UtilList.isEmpty(listMaps)) {
                    if (listMaps.size()==1){
                        luoutImgArray.setVisibility(View.GONE);
                        txtViewName.setVisibility(View.VISIBLE);
                        imgView.setVisibility(View.VISIBLE);
                        txtViewName.setText(listMaps.get(0).get("NAME") + "");
                        Glide.with(mContent)
                                .load(AffConstans.PUBLIC.ADDRESS_IMAGE + listMaps.get(0).get("THUMB") + "")
                                .placeholder(R.drawable.default_list)
                                .error(R.drawable.default_list)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imgView);
                    }else {
                        //清空
                        ImageView[] arrayImgView = {mImgGoods01,mImgGoods02,mImgGoods03,mImgGoods04};
                        txtViewName.setVisibility(View.GONE);
                        imgView.setVisibility(View.GONE);
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
                LogUtils.e("==========ISSELF=======>"+(mListMap.get(position).get("ISSELF")+""));
                if((mListMap.get(position).get("ISSELF")+"").equals("0")) {
                    mStoreName.setText(mListMap.get(position).get("STORENAME") + "");
                }else{
                    mStoreName.setText("大实惠直营");
                }

                txtViewPrice.setText("￥" + mListMap.get(position).get("REALPAY"));
                mOrderNum = mListMap.get(position).get("ORDERNUM") + "";
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
            int i = UtilNumber.IntegerValueOf(_listMap.get(position).get(CommConstans.SHOPORDER.ORDERSTATE) + "");
            if (i == 5){
                mOrderState.setText("已过期");
            }
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
                    if (mOrderNum != null) {
                        mBllOrder.doCancelOrder(mOrderNum);//点击确定，提交服务器请求取消订单
                    } else {
                        UtilToast.show(mContent, "取消订单失败", Toast.LENGTH_SHORT);
                    }
                    mAtDialog.dismiss();
                }
            });
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

//        /**
//         * 催单
//         */
//        public void urgedOrder() {
//            final WgtAlertDialog mAtDialog = new WgtAlertDialog();
//            mAtDialog.show(mContent,
//                    "取消", "确定",
//                    "确认催单？",
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            mAtDialog.dismiss();
//                        }
//                    }, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (mListMap.get(mPosition).get("ORDERNUM") != null) {
//                                UtilDialog.showProgressDialog(mContent);
//                                mBllOrder.urge(mListMap.get(mPosition).get("ORDERNUM") + "");//提交服务器请求
//                            } else {
//                                UtilToast.show(mContent, "催单失败！", Toast.LENGTH_SHORT);
//                            }
//                            mAtDialog.dismiss();
//                        }
//                    });
//        }
    }
}
