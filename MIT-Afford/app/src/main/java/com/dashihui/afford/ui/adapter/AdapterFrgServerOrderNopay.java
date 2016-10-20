package com.dashihui.afford.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessServer;
import com.dashihui.afford.common.base.BaseFragment;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.ui.activity.AtyFragmentServerDetail;
import com.dashihui.afford.ui.activity.AtySettlementOrder;
import com.dashihui.afford.ui.activity.fragment.FragmentServer;
import com.dashihui.afford.ui.widget.WgtAlertDialog;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.number.UtilNumber;
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
public class AdapterFrgServerOrderNopay extends AdapterBase<Map<String, Object>> {

    private FragmentServer mFragmentServer;
    private List<Map<String, Object>> mAllListMap;
//    private String mType;//标志

    public AdapterFrgServerOrderNopay(Activity context, BaseFragment fragment, List<Map<String, Object>> _AllList) {
        super(context, _AllList);
        mFragmentServer = (FragmentServer) fragment;
        this.mAllListMap = _AllList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.frag_server_nopay_item, null);
            viewHolder = new ViewHolder(mContext, mFragmentServer, mList);
            //依赖注入初始化
            ViewUtils.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
            viewHolder.refresh(position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.update(position, mList);
        }
        viewHolder.mBtnTopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(mContext, AtySettlementOrder.class);
                LogUtils.e("AdapterFrgServerOrderNopay==============mAllListMap======>" + mAllListMap);
                int mType = UtilNumber.IntegerValueOf(mAllListMap.get(position).get("TYPE") + "");
                LogUtils.e("AdapterFrgServerOrderNopay==========mType==========>" + mType);
                LogUtils.e("AdapterFrgServerOrderNopay==========mList==========>" + mAllListMap);
                //根据mType是否为空跳转至付款页面并传一个支付标志区分调用支付接口，
                //支付列表中 “去支付”按钮
                if (CommConstans.FRAGSERSTATEORDERTYPE.HOUSESER == mType) {//家政
                    mapIntent.putExtra(AtySettlementOrder.ORDER_PAY_TYPE, CommConstans.ORDER.HOUSE_ORDER_PAY);
                    mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, mAllListMap.get(position).get("AMOUNT") + "");
                    mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, mAllListMap.get(position).get("ORDERNUM") + "");
                } else if (CommConstans.FRAGSERSTATEORDERTYPE.SERVER == mType) {//其他服务
                    mapIntent.putExtra(AtySettlementOrder.ORDER_PAY_TYPE, CommConstans.ORDER.SERVER_ORDER_PAY);
                    mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, mAllListMap.get(position).get("AMOUNT") + "");
                    mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, mAllListMap.get(position).get("ORDERNUM") + "");
                }
                mContext.startActivity(mapIntent);
                mContext.finish();
            }
        });
        viewHolder.mLytNoPayItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(mContext, AtyFragmentServerDetail.class);
                int type = UtilNumber.IntegerValueOf(mAllListMap.get(position).get("TYPE") + "");
                LogUtils.e("AdapterFrgServerOrderWaitdeliver==========mType==========>" + type);

                if (CommConstans.FRAGSERSTATEORDERTYPE.HOUSESER == type) {//家政
                    mapIntent.putExtra(AtySettlementOrder.ORDER_PAY_TYPE, CommConstans.ORDER.HOUSE_ORDER_PAY);
                } else if (CommConstans.FRAGSERSTATEORDERTYPE.SERVER == type) {//其他服务
                    mapIntent.putExtra(AtySettlementOrder.ORDER_PAY_TYPE, CommConstans.ORDER.SERVER_ORDER_PAY);
                }
                mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, mAllListMap.get(position).get("AMOUNT") + "");
                mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, mAllListMap.get(position).get("ORDERNUM") + "");
                mContext.startActivity(mapIntent);
            }
        });
        return convertView;
    }

    /**
     * @author p
     */
    static class ViewHolder {
        @ViewInject(R.id.proser_name)
        private TextView mTvProserName;//小区名
        @ViewInject(R.id.store_name)
        private TextView mTvStoreName;//店铺名
        @ViewInject(R.id.ser_item)
        private TextView mTvSerItem;
        @ViewInject(R.id.ser_time)
        private TextView mTvTime;
        @ViewInject(R.id.ser_address)
        private TextView mTvAddress;
        @ViewInject(R.id.ser_amout)
        private TextView mPayMoney;
        @ViewInject(R.id.goods_lyt)
        private LinearLayout mLytNoPayItem;

        @ViewInject(R.id.all_order_waitpay)
        private TextView mTvOrderState;//右上角订单状态提示

        @ViewInject(R.id.btn_cancel)
        private Button mBtnCancel;//取消
        @ViewInject(R.id.btn_topay)
        private Button mBtnTopay;//去支付
        @ViewInject(R.id.btn_sure)
        private Button mBtnSure;//确认服务

        private BusinessServer mBllServer;
        private String mOrderNum;
        private Activity mContent;
        private List<Map<String, Object>> mAllListMap;
        private int mListMapType, mPayType, mDeliverState;
        private int mPosition;
        private String mType;//标志

        public ViewHolder(Activity context, BaseFragment fragment, List<Map<String, Object>> _AllListMap) {
            mContent = context;
            mAllListMap = _AllListMap;
            mBllServer = new BusinessServer(fragment);
        }

        /**
         * 更新最新数据
         *
         * @param _AllListMap
         */
        public void refreshList(List<Map<String, Object>> _AllListMap) {
            mAllListMap = _AllListMap;
        }

        /*
         * 第一次刷新
         */
        public void refresh(final int position) {
            mPosition = position;
            if (!UtilList.isEmpty(mAllListMap)){
                mListMapType = UtilNumber.IntegerValueOf(mAllListMap.get(position).get("TYPE") + "");
                mDeliverState = UtilNumber.IntegerValueOf(mAllListMap.get(position).get("DELIVERSTATE") + "");
                mPayType = UtilNumber.IntegerValueOf(mAllListMap.get(position).get("PAYTYPE") + "");
                mOrderNum = mAllListMap.get(position).get("ORDERNUM") + "";

                //待付款页面显示的按钮
                if (mListMapType == CommConstans.FRAGSERSTATEORDERTYPE.HOUSESER) {//家政
                    if (mPayType == CommConstans.FRAGSERSTATEORDERPAYTYPE.ON_LINE) {//在线支付
                        mBtnCancel.setVisibility(View.VISIBLE);
                        mBtnTopay.setVisibility(View.VISIBLE);
                        mBtnSure.setVisibility(View.GONE);
                    }

                } else if (mListMapType == CommConstans.FRAGSERSTATEORDERTYPE.SERVER) {//其他服务
                    if (mPayType == CommConstans.FRAGSERSTATEORDERPAYTYPE.ON_LINE) {//在线支付
                        mBtnCancel.setVisibility(View.VISIBLE);
                        mBtnTopay.setVisibility(View.VISIBLE);
                        mBtnSure.setVisibility(View.GONE);
                    }
                }

                if (CommConstans.FRAGSERSTATEORDERTYPE.HOUSESER == mListMapType) {//家政
                    mTvProserName.setText(mAllListMap.get(position).get("STORENAME") + "");
                    mTvStoreName.setText(mAllListMap.get(position).get("PROSERNAME") + "");
                    mTvSerItem.setText(mAllListMap.get(position).get("SERVICETITLE") + "");
                    mTvTime.setText(mAllListMap.get(position).get("SERTIME") + "");
                    mTvAddress.setText(mAllListMap.get(position).get("ADDRESS") + "");
                } else if (CommConstans.FRAGSERSTATEORDERTYPE.SERVER == mListMapType) {//其他服务
                    mTvProserName.setText(mAllListMap.get(position).get("STORENAME") + "");

                    mTvStoreName.setText(mAllListMap.get(position).get("PROSERNAME") + "");
                    mTvSerItem.setText(mAllListMap.get(position).get("SERVICETITLE") + "");
                    mTvTime.setText(mAllListMap.get(position).get("SERTIME") + "");
                    mTvAddress.setText(mAllListMap.get(position).get("ADDRESS") + "");
                }
                mPayMoney.setText("￥" + mAllListMap.get(position).get("AMOUNT") + "元");
            }else{
                LogUtils.e("refresh===Error=====null=========>" + mAllListMap + "");
            }

        }

        /**
         * 更新
         */
        public void update(final int position, List<Map<String, Object>> _AllListMap) {
            refreshList(_AllListMap);
            refresh(position);
        }


        @OnClick(R.id.btn_cancel)//取消
        public void onCancelClick(View v) {
            final WgtAlertDialog mAtDialog = new WgtAlertDialog();
            mAtDialog.show(mContent,
                    "取消", "确定", "确认取消当前订单？",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mAtDialog.dismiss();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LogUtils.e("onCancelClick====取消===mListMapType=======>" + mListMapType);
                            if (mOrderNum != null) {
                                if (CommConstans.FRAGSERSTATEORDERTYPE.HOUSESER == mListMapType) {
                                    mBllServer.getSerOrderCancel(mOrderNum, "无");
                                } else if (CommConstans.FRAGSERSTATEORDERTYPE.SERVER == mListMapType) {
                                    mBllServer.getOrderCancel(mOrderNum);
                                }
                                mAtDialog.dismiss();
                            }
                        }
                    }, false, false, 0, null);

        }

//        @OnClick(R.id.btn_sure)
//        public void onSignOrder(View v) {//签收
//            if (AffConstans.FRAGSERSTATEORDERTYPE.HOUSESER == mListMapType) {//家政
//                mBllServer.getSerOrderSign(mHouseOrderNum);
//            } else if (AffConstans.FRAGSERSTATEORDERTYPE.SERVER == mListMapType) {//其他服务
//                mBllServer.getSerOrderSign(mOrderNum);
//            }
//        }





    }
}
