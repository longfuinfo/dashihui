package com.dashihui.afford.ui.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessOrder;
import com.dashihui.afford.business.entity.EtyList;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.ui.adapter.AdapterRefund;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.number.UtilNumber;
import com.dashihui.afford.util.toast.UtilToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AtyRefund extends BaseActivity {
    @ViewInject(R.id.title)
    private TextView mTitle;//名称
    @ViewInject(R.id.listview)
    private ListView mListview;
    @ViewInject(R.id.noOrder)
    private LinearLayout mLytOrder;

    private AdapterRefund mRefundAdapter;
    private List<Map<String, Object>> mListmap;//获取退款商品列表
    private BusinessOrder mBussOrder;
    //分页码
    private int pageNum = 1;
    private int mTotalPage = 0;//总页数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_refund);
        ViewUtils.inject(this);
        //标题命名
        mTitle.setText(R.string.order_refund);
        mBussOrder = new BusinessOrder(this);
        mListmap = new ArrayList<>();

        mRefundAdapter = new AdapterRefund(this, mListmap);

        mListview.setAdapter(mRefundAdapter);
        //请求退款单列表
        mBussOrder.getOrderRefund(pageNum+"");

    }



    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_ORDER_REFUND://退款单列表
                    EtyList etyList = (EtyList) beanSendUI.getInfo();
                    if (pageNum ==1){
                        mListmap.clear();
                    }
                    if (etyList != null && !UtilList.isEmpty(etyList.getLIST())) {
                        mTotalPage = UtilNumber.IntegerValueOf(etyList.getTOTALPAGE() + "");
                        mListmap.addAll(etyList.getLIST());
                        mRefundAdapter.setList(mListmap);
                        mRefundAdapter.notifyDataSetChanged();
                    }else {
                        mLytOrder.setVisibility(View.VISIBLE);
                        mListview.setVisibility(View.GONE);
                        UtilToast.show(this, "暂无退款单", Toast.LENGTH_SHORT);
                    }
                    //隐藏加载框
                    dissProDialog();
                    break;

                default:
                    LogUtils.e("onSuccess===default===========>" + beanSendUI);
                    break;
            }
        } else {
            LogUtils.e("onSuccess======AtyHome====null=====>" + beanSendUI);
        }

    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_ORDER_REFUND://退款单列表
                    //隐藏加载框
                    dissProDialog();
                    break;

                default:
                    LogUtils.e("onSuccess===default===========>" + beanSendUI);
                    break;
            }
        } else {
            LogUtils.e("onSuccess======AtyHome====null=====>" + beanSendUI);
        }
    }

    @OnClick(R.id.ibtnBack)
    public void baceOnclick(View view){
        onBackPressed();
    }

}
