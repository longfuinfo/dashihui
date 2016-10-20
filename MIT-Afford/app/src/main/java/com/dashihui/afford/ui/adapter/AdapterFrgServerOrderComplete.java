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
import com.dashihui.afford.ui.activity.fragment.FragmentServerComplete;
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
public class AdapterFrgServerOrderComplete extends AdapterBase<Map<String, Object>> {
    private FragmentServerComplete mFragmentServer;
    private String mType;//标志
    private List<Map<String, Object>> mAllListMap;

    public AdapterFrgServerOrderComplete(Activity context, BaseFragment frgment, List<Map<String, Object>> _List) {
        super(context, _List);
        mFragmentServer = (FragmentServerComplete) frgment;
        mAllListMap = _List;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.frag_server_assess_item, null);
            viewHolder = new ViewHolder(mContext, mFragmentServer, mList);
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

        @ViewInject(R.id.btn_sure)
        private Button mBtnSerDone;//服务完成
        @ViewInject(R.id.btn_surged)
        private Button mBtnSerSurged;//催单
        @ViewInject(R.id.btn_delete)
        private Button mBtnSerDelete;//删除订单
        @ViewInject(R.id.btn_cancel)
        private Button mBtnSerCancel;//取消订单

        private BusinessServer mBllServer;
        private String mOrderNum;
        private Activity mContent;
        private List<Map<String, Object>> mAllListMap;
        private int mListMapType, mPayType, mDeliverState;
        private int mPosition;
        private String mType;//标志

        public ViewHolder(Activity context, BaseFragment fragment, List<Map<String, Object>> _listMap) {
            mContent = context;
            mAllListMap = _listMap;
            mBllServer = new BusinessServer(fragment);
        }


        /**
         * 更新最新数据
         *
         * @param _listMap
         */
        public void refreshList(List<Map<String, Object>> _listMap) {
            mAllListMap = _listMap;
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

                //待付款页面显示的按钮
                if (mListMapType == CommConstans.FRAGSERSTATEORDERTYPE.HOUSESER){//家政
                    if (mPayType == CommConstans.FRAGSERSTATEORDERPAYTYPE.ON_LINE){//在线支付
                        showDeleteBtn();//显示删除按钮
                    }else if (mPayType == CommConstans.FRAGSERSTATEORDERPAYTYPE.AFTER_SERVICE){//服务后付款
                        showDeleteBtn();//显示删除按钮
                    }

                }else if (mListMapType == CommConstans.FRAGSERSTATEORDERTYPE.SERVER){//其他服务
                    if (mPayType == CommConstans.FRAGSERSTATEORDERPAYTYPE.ON_LINE){//在线支付
                        showDeleteBtn();//显示删除按钮
                    }else if (mPayType == CommConstans.FRAGSERSTATEORDERPAYTYPE.AFTER_SERVICE){//服务后付款
                        showDeleteBtn();//显示删除按钮
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
                    if (CommConstans.FRAGSERSTATEORDERDISPATCHSTATE.NO_DISPATCH == mDeliverState) {//未接单
                        mTvStoreName.setVisibility(View.GONE);
                    } else if (CommConstans.FRAGSERSTATEORDERDISPATCHSTATE.HAD_DISPATCH == mDeliverState) {//已派单
                        mTvStoreName.setText(mAllListMap.get(position).get("PROSERNAME") + "");
                    }
                    mTvSerItem.setText(mAllListMap.get(position).get("SERVICETITLE") + "");
                    mTvTime.setText(mAllListMap.get(position).get("SERTIME") + "");
                    mTvAddress.setText(mAllListMap.get(position).get("ADDRESS") + "");
                }
                mPayMoney.setText("￥" + mAllListMap.get(position).get("AMOUNT") + "元");
                mOrderNum = mAllListMap.get(position).get("ORDERNUM") + "";
            }else{
                LogUtils.e("refresh===Error=====null=========>" + mAllListMap + "");
            }

        }

        /**
         * 更新
         */
        public void update(final int position, List<Map<String, Object>> _listMap) {
            refreshList(_listMap);
            refresh(position);
        }

        //显示删除按钮
        public void showDeleteBtn(){
            mBtnSerDone.setVisibility(View.GONE);
            mBtnSerSurged.setVisibility(View.GONE);
            mBtnSerDelete.setVisibility(View.VISIBLE);
            mBtnSerCancel.setVisibility(View.GONE);
        }


        @OnClick(R.id.btn_delete)//删除
        public void onDeleteClick(View v) {
            final WgtAlertDialog mAtDialog = new WgtAlertDialog();
            mAtDialog.show(mContent,
                    "取消", "确定", "确认删除当前订单？",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mAtDialog.dismiss();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (mOrderNum != null) {
                                if (CommConstans.FRAGSERSTATEORDERTYPE.HOUSESER == mListMapType) {
                                    mBllServer.getSerOrderDelete(mOrderNum);
                                } else if (CommConstans.FRAGSERSTATEORDERTYPE.SERVER == mListMapType) {
                                    mBllServer.getOrderDelete(mOrderNum);
                                }
                            }
                            mAtDialog.dismiss();
                        }
                    }, false, false, 0, null);

        }

//        @OnClick(R.id.btn_sure)
//        public void onSignOrder(View v) {//签收
//            final WgtAlertDialog mAtDialog = new WgtAlertDialog();
//            mAtDialog.show(mContent,
//                    "取消", "确定", "确认收货？",
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            mAtDialog.dismiss();
//                        }
//                    }, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            UtilDialog.showProgressDialog(mContent);
//                            if (AffConstans.FRAGSERSTATEORDERTYPE.HOUSESER == mListMapType) {
//                                if (mHouseOrderNum != null) {
//                                    mBllServer.getSerOrderSign(mHouseOrderNum);
//                                }
//                            } else if (AffConstans.FRAGSERSTATEORDERTYPE.SERVER == mListMapType) {
//                                if (mOrderNum != null) {
//                                    mBllServer.getSerOrderSign(mOrderNum);
//                                }
//                            }
//                            mAtDialog.dismiss();
//                        }
//                    }, false, false, 0, null);
//        }

        @OnClick(R.id.goods_lyt)
        public void onLytItemClick(View v) {
            Intent mapIntent = new Intent(mContent, AtyFragmentServerDetail.class);
            mType = mAllListMap.get(mPosition).get("TYPE") + "";

            LogUtils.e("onLytItemClick========完成订单，点击item==mListMapType==========>" + mListMapType);
            if (CommConstans.FRAGSERSTATEORDERTYPE.HOUSESER == mListMapType) {//家政
                mapIntent.putExtra(AtySettlementOrder.ORDER_PAY_TYPE, CommConstans.ORDER.HOUSE_ORDER_PAY);
            } else if (CommConstans.FRAGSERSTATEORDERTYPE.SERVER == mListMapType) {//其他服务
                LogUtils.e("onLytItemClick========完成订单，点击item==mListMapType==========>" + mListMapType);
                mapIntent.putExtra(AtySettlementOrder.ORDER_PAY_TYPE, CommConstans.ORDER.SERVER_ORDER_PAY);
            }
            mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, mAllListMap.get(mPosition).get("AMOUNT") + "");
            mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, mAllListMap.get(mPosition).get("ORDERNUM") + "");
            LogUtils.e("getView=========已完成订单号==========>" + mAllListMap.get(mPosition).get("ORDERNUM") + "");
            mContent.startActivity(mapIntent);
        }
    }
}
