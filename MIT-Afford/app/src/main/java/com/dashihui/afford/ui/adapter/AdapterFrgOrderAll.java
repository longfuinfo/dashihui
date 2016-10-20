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
 * 全部订单
 * @author NiuFC
 * @version 1.0
 * @date 2015-10-30
 */
public class AdapterFrgOrderAll extends AdapterBase<Map<String, Object>> {
    private List<Map<String, Object>> mMapList;
    private FragmentOrder mFragmentOrder;

    public AdapterFrgOrderAll(Activity context, BaseFragment fragment, List<Map<String, Object>> _List) {
        super(context, _List);
        this.mMapList = _List;
        mFragmentOrder = (FragmentOrder) fragment;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.frag_order_all_item, null);
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
                mContext.finish();
            }
        });
        viewHolder.lytPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(mContext, AtyOrdertDetail.class);
                mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, mMapList.get(position).get("AMOUNT") + "");
                mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, mMapList.get(position).get("ORDERNUM") + "");
                mContext.startActivity(mapIntent);
                mContext.finish();
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
        private Button mBtnCancel;

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
                int payType = UtilNumber.IntegerValueOf(mListMap.get(position).get("PAYTYPE") + "");
                int takeType = UtilNumber.IntegerValueOf(mListMap.get(position).get("TAKETYPE") + "");
                if (payType == CommConstans.SHOPORDERPAYTYPE.ONLINE && takeType == CommConstans.SHOPORDERTAKETYPE.DELIVER){
                    mBtnTopay.setVisibility(View.VISIBLE);
                    mBtnCancel.setVisibility(View.VISIBLE);
                    mBtnUrged.setVisibility(View.GONE);
                }else if (payType == CommConstans.SHOPORDERPAYTYPE.ONLINE && takeType == CommConstans.SHOPORDERTAKETYPE.TAKESELF){
                    mBtnTopay.setVisibility(View.VISIBLE);
                    mBtnCancel.setVisibility(View.VISIBLE);
                    mBtnUrged.setVisibility(View.GONE);
                }else {
                    mBtnTopay.setVisibility(View.GONE);
                    mBtnCancel.setVisibility(View.GONE);
                    mBtnUrged.setVisibility(View.GONE);
                }


                List<Map<String, Object>> listMaps = (List<Map<String, Object>>) mListMap.get(position).get("GOODSLIST");
                if (!UtilList.isEmpty(listMaps)) {
                    txtViewName.setText(listMaps.get(0).get("NAME") + "");
                    Glide.with(mContent)
                            .load(AffConstans.PUBLIC.ADDRESS_IMAGE + listMaps.get(0).get("THUMB") + "")
                            .placeholder(R.drawable.default_list)
                            .error(R.drawable.default_list)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imgView);
                } else {
                    LogUtils.e("refresh===Error=====null=========>" + listMaps + "");
                }
                mStoreName.setText(mListMap.get(position).get("STORENAME")+"");
                txtViewPrice.setText("￥" + mListMap.get(position).get("AMOUNT") + "元");
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
