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
import com.dashihui.afford.ui.activity.fragment.FragmentWallet;
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
public class AdapterShihuibi  extends AdapterBase<Map<String, Object>> {
    private List<Map<String, Object>> mMapList;
    private FragmentWallet mFragmentOrder;
    private int i;

    public AdapterShihuibi(Activity context, List<Map<String, Object>> _List) {
        super(context, _List);
        this.mMapList = _List;
       // mFragmentOrder = (FragmentWallet) fragment;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.aty_my_shihuibi_item, null);
            viewHolder = new ViewHolder(mContext, mFragmentOrder, mMapList);
            //依赖注入初始化
            ViewUtils.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
            viewHolder.refresh(position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.update(position, mMapList);
        }



        return convertView;
    }


    /**
     * @author p
     */
    static class ViewHolder  {
        @ViewInject(R.id.order_time)
        private TextView mOrderTime;//订单时间
        @ViewInject(R.id.income)
        private TextView mIncome;//订单消费

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
            mOrderTime.setText(mListMap.get(position).get("CREATEDATE") + "");
            if (String.valueOf(mListMap.get(position).get("TYPE")).equals("1")) {
                mIncome.setText("+" + mListMap.get(position).get("AMOUNT"));
            }else {
                mIncome.setText("-" + mListMap.get(position).get("AMOUNT"));
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





        /**
         * 取消订单提示
         */



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

