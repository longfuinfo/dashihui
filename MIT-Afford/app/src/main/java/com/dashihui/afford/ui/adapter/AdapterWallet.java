package com.dashihui.afford.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessOrder;
import com.dashihui.afford.business.BusinessUser;
import com.dashihui.afford.common.base.BaseFragment;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.ui.activity.AtyOrdertDetail;
import com.dashihui.afford.ui.activity.AtySettlementOrder;
import com.dashihui.afford.ui.activity.fragment.FragmentOrder;
import com.dashihui.afford.ui.activity.fragment.FragmentWallet;
import com.dashihui.afford.util.list.UtilList;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/5.
 */
public class AdapterWallet extends AdapterBase<Map<String, Object>> {

    private List<Map<String, Object>> mMapList;
    private FragmentWallet mFragmentOrder;

    public AdapterWallet(Activity context, List<Map<String, Object>> _List,BaseFragment fragment) {
        super(context, _List);
        this.mContext = context;
        this.mMapList = _List;
        mFragmentOrder = (FragmentWallet) fragment;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        {
            Holder viewHolder = null;
            LayoutInflater inflater = mContext.getLayoutInflater();
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.aty_my_wallet_item, null);
                viewHolder = new Holder(mContext, mFragmentOrder, mMapList);
                //依赖注入初始化
                ViewUtils.inject(viewHolder, convertView);
                convertView.setTag(viewHolder);
                try {
                    viewHolder.refresh(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                viewHolder = (Holder) convertView.getTag();
                try {
                    viewHolder.update(position, mMapList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

                }

            return convertView;
        }


    final class Holder {
        @ViewInject(R.id.order_number)
        private TextView mOrderNumber;//订单号
        @ViewInject(R.id.order_time)
        private TextView mOrderTime;//订单时间
        @ViewInject(R.id.income)
        private TextView mIncome;//订单收益


        private Activity mContent;
        private List<Map<String, Object>> mListMap;
        private Map<String,Object> mMap;
        private int mPosition = -1;


        public Holder(Activity context, BaseFragment fragment, List<Map<String, Object>>
                _listMap) {
            mContent = context;
            mListMap = _listMap;

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
         * 更新
         */
        public void update(final int position, List<Map<String, Object>> _listMap) throws JSONException {
            refreshList(_listMap);
            refresh(position);
        }

        public void refresh(final int position) throws JSONException {
            int i = 0;

            mPosition = position;
//            String jsonStr = mListMap.get(position).get("DATA").toString();
//            JSONObject object = new JSONObject(jsonStr);
           // object.get("orderNum");
            LogUtils.e("onSuccess===adapter===========>" );
            mOrderNumber.setText("订单号：" + mListMap.get(position).get("FROMORDERNUM"));
            //mMap = mListMap.get(position).get("DATA");

            mOrderTime.setText(mListMap.get(position).get("CREATEDATE") + "");
            //i = Integer.parseInt(String.valueOf(mListMap.get(position).get("AMOUNT")));
            if (String.valueOf(mListMap.get(position).get("TYPE")).equals("1")) {
                mIncome.setText("+" + mListMap.get(position).get("AMOUNT"));
            } else {
                mIncome.setText("-" + mListMap.get(position).get("AMOUNT"));
            }
        }
    }

    }

