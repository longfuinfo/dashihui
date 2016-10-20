package com.dashihui.afford.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessOrder;
import com.dashihui.afford.common.base.BaseFragment;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.ui.activity.AtyOrdertDetail;
import com.dashihui.afford.ui.activity.AtySettlementOrder;
import com.dashihui.afford.ui.activity.fragment.FragmentOrder;
import com.dashihui.afford.ui.activity.fragment.FragmentWallet;
import com.dashihui.afford.util.list.UtilList;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/5.
 */
public class AdapterFriend extends AdapterBase<Map<String, Object>> {

    private List<Map<String, Object>> mMapList;
    private FragmentWallet mFragmentOrder;
    private Activity mContent;

    public AdapterFriend(Activity context, List<Map<String, Object>> _List) {
        super(context, _List);
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        {
            FriendHolder viewHolder = null;
            LayoutInflater inflater = mContent.getLayoutInflater();
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.aty_my_wallet_item, null);
                viewHolder = new FriendHolder(mContent, mFragmentOrder, mMapList);
                //依赖注入初始化
                ViewUtils.inject(viewHolder, convertView);
                convertView.setTag(viewHolder);
                viewHolder.refresh(position);
            } else {
                viewHolder = (FriendHolder) convertView.getTag();
                viewHolder.update(position, mMapList);
            }

        }

        return convertView;
    }


   final class FriendHolder {
     @ViewInject(R.id.order_number)
     private TextView mOrderNumber;//手机号
     @ViewInject(R.id.order_time)
     private TextView mOrderTime;//订单时间
     @ViewInject(R.id.income)
     private TextView mIncome;//订单收益

     private BusinessOrder mBllOrder;

     private Activity mContent;
     private List<Map<String, Object>> mListMap;
     private int mPosition = -1;

     public FriendHolder(Activity context, BaseFragment fragment, List<Map<String, Object>>
            _listMap) {
        mContent = context;
        mListMap = _listMap;
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
     * 更新
     */
     public void update(final int position, List<Map<String, Object>> _listMap) {
        refreshList(_listMap);
        refresh(position);

     }

     public void refresh(final int position) {
        mPosition = position;
        mOrderNumber.setText("" + mListMap.get(position).get("USERID"));
        mOrderTime.setText(mListMap.get(position).get("CREATEDATE") + "");
        if (String.valueOf(mListMap.get(position).get("TYPE")).equals("1")) {
            mIncome.setText("+" + mListMap.get(position).get("AMOUNT"));
        } else {
            mIncome.setText("-" + mListMap.get(position).get("AMOUNT"));
        }
     }
}

}

