package com.dashihui.afford.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.dashihui.afford.R;
import com.dashihui.afford.thirdapi.FastJSONHelper;
import com.dashihui.afford.util.list.UtilList;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.Map;

public class AdapterRefund extends AdapterBase<Map<String, Object>> {
    private List<Map<String, Object>> mRefundListMap;
    private Activity mContext;
//    private int[] mItem;
    private Intent mSpecialIntent;

    public AdapterRefund(Activity context, List<Map<String, Object>> dataObjects) {
        super(context, dataObjects);
        this.mContext = context;
        this.mRefundListMap = dataObjects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.aty_refund_item, null);
            viewHolder = new ViewHolder(mContext, mRefundListMap);
            //依赖注入初始化
            ViewUtils.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
            viewHolder.refresh(position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            //每次更新最新数据
            viewHolder.update(position, mRefundListMap);
        }

        return convertView;
    }


    /**
     * @author p
     */
    static class ViewHolder {

        @ViewInject(R.id.txtCode)
        private TextView mTxtCode;//退款单号

        @ViewInject(R.id.txtTime)
        private TextView mTxtTime;//申请时间

        @ViewInject(R.id.txtState)
        private TextView mTxtState;//退单状态

        @ViewInject(R.id.txtPrice)
        private TextView mTxtPrice;//订单
        @ViewInject(R.id.txtRefundTrade)
        private TextView mTxtRefundTrade;//交易金额
        @ViewInject(R.id.txtRefundPrice)
        private TextView mTxtRefundPrice;//退款
        @ViewInject(R.id.txtRefundNum)
        private TextView mTxtRefundNum;//退款金额
        @ViewInject(R.id.txtRefundState)
        private TextView mTxtRefundState;//最终退款原因

        @ViewInject(R.id.listview)
        private ListView mListView;//商品列表

        private Activity mContent;
        private LinearLayout mLytItem;

        private Intent mIntent;
        private int mPosition;
        private List<Map<String, Object>> mListMap;
        private Activity mContext;
        private List<Map<String, Object>> mItemlistMap;

        private AdapterRefundGoods mAdapterRGoods;


        public ViewHolder(Activity context, List<Map<String, Object>> dataObjects) {
            mContent = context;
            this.mContext = context;
            this.mListMap = dataObjects;

        }

        /**
         * 更新最新数据
         *
         * @param _listMap
         */
        public void refreshList(List<Map<String, Object>> _listMap) {
            this.mListMap = _listMap;
        }

        /**
         * 第一次刷新
         */
        public void refresh(final int position) {
            mPosition = position;
            if (!UtilList.isEmpty(mListMap)){
                LogUtils.e("refresh===Error=====null=========>" + mTxtCode);
                LogUtils.e("refresh===Error=====null=========>" + mListMap.get(position));
                  mTxtCode.setText("退款单号："+mListMap.get(position).get("REFUNDNUM"));//退款单号
                  mTxtTime.setText("申请时间："+mListMap.get(position).get("CREATEDATE"));//申请时间
                //退款状态：1：审核中，2：审核通过，3：审核不通过，4：已退款
                    String state = mListMap.get(position).get("STATE")+"";
                if ("1".equals(state)){
                    mTxtState.setText("审核中");
                    mTxtRefundState.setVisibility(View.GONE);
                }else if ("2".equals(state)){
                    mTxtState.setText("审核通过");
                    mTxtRefundState.setVisibility(View.VISIBLE);
                    String refundState = ""+mListMap.get(position).get("STATEMENT");
                    mTxtRefundState.setText(refundState);
                    mTxtRefundState.setTextColor(mContent.getResources().getColor(R.color.hintcolor));
                }else if ("3".equals(state)){
                    mTxtState.setText("审核不通过");
                    //退款不通过原因
                    String refundState = ""+mListMap.get(position).get("REFUSEREASON");
                    mTxtRefundState.setVisibility(View.VISIBLE);
                    mTxtRefundState.setText("拒绝理由："+refundState);//退款不通过原因
                }else if ("4".equals(state)){
                    mTxtState.setText("已退款");
                    mTxtRefundState.setVisibility(View.VISIBLE);
                    String refundState = ""+mListMap.get(position).get("STATEMENT");
                    mTxtRefundState.setText(refundState);
                    mTxtRefundState.setTextColor(mContent.getResources().getColor(R.color.hintcolor));
                }else {
                    mTxtState.setText("异常情况");
                    mTxtRefundState.setVisibility(View.GONE);
                }
                  mTxtPrice.setText("交易金额：");
                  mTxtRefundTrade.setText("￥" + mListMap.get(position).get("ORDERAMOUNT"));//交易金额
                  mTxtRefundNum.setText("￥" + mListMap.get(position).get("AMOUNT"));//退款金额
                  mTxtRefundPrice.setText("退款金额：");
                TypeReference typeListMap = new TypeReference<List<Map<String, Object>>>() {
                };
                List<Map<String, Object>> listMapGoods = (List<Map<String, Object>>) FastJSONHelper.deserializeAny(mListMap.get(position).get("GOODSLIST") + "", typeListMap);
                mAdapterRGoods = new AdapterRefundGoods(mContext,listMapGoods);
                mListView.setAdapter(mAdapterRGoods);
                setlistViewHeigh(mListView);
            } else {
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
         * 动态设置listview高度
         *
         * @param listView
         */
        private void setlistViewHeigh(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                return;
            }
            int totalHeight = 0;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
            listView.setLayoutParams(params);

        }

    }
}
