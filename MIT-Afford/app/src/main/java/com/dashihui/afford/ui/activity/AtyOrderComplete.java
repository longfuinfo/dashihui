package com.dashihui.afford.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessOrder;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.thirdapi.FastJSONHelper;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.string.UtilString;
import com.dashihui.afford.util.toast.UtilToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;
import java.util.Map;

public class AtyOrderComplete extends BaseActivity {

    @ViewInject(R.id.storeName)
    private TextView mOrderNum;//订单号
    @ViewInject(R.id.all_order_goodsimg)
    private ImageView mOrdergoodsimg;//图片
    @ViewInject(R.id.all_order_goodsname)
    private TextView mOrdergoodname;//商品名字
    @ViewInject(R.id.all_order_money)
    private TextView mOrderMoney;//总价
    @ViewInject(R.id.goods_lyt)
    private LinearLayout mOrderLyt;



    private List<Map<String, Object>> listMapGoods;
    private  String mOrderCode="";
    private  String mOrderPrice="";
    private BusinessOrder mBllOrder;
//    public final static int ORDEREVALUATE = 1001;//去评价 = "tabCode";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_ordercomplete);
        ViewUtils.inject(this);
        mBllOrder = new BusinessOrder(this);

        mOrderCode = getIntent().getStringExtra(AtySettlementOrder.ORDER_CODE);
        mOrderPrice = getIntent().getStringExtra(AtySettlementOrder.ORDER_PRICE);
        if (!"".equals(mOrderCode)) {
            mBllOrder.getOrderDetail(mOrderCode);
        }
    }



    @Override
    public void onSuccess(EtySendToUI successEty) {
        if (successEty != null) {
            switch (successEty.getTag()) {
                case AffConstans.BUSINESS.TAG_ORDER_DETAIL://订单详情
                    Map<String, Object>  mapObject = (Map<String, Object>) successEty.getInfo();
                    setData(mapObject);
                    break;
                default:
                    break;
            }
        } else {
            LogUtils.e("onSuccess=======error====>" + successEty);
        }


    }

    @Override
    public void onFailure(EtySendToUI failureEty) {
        if (failureEty != null) {
            switch (failureEty.getTag()) {
                case AffConstans.BUSINESS.TAG_ORDER_DETAIL:
                    UtilToast.show(this, failureEty.getInfo() + "", Toast.LENGTH_SHORT);
                    LogUtils.e("onFailure==========mOrderNum==cancelOrder==========>" );
                    break;
                default:
                    break;
            }
        }
    }
    @OnClick(R.id.btnOrderEvaluate)//去评价
    public void onPriceClick(View v){
        if (!UtilString.isEmpty(mOrderCode)){
            Intent mapIntent = new Intent(this, AtyEvaluate.class);
            mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, mOrderPrice + "");
            mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, mOrderCode + "");
            // 重用堆栈里的已经启动activity
            mapIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(mapIntent);
            finish();
        }else {
            LogUtils.e("OrderTrack=======else====订单跟踪=============>");
        }
    }
    @OnClick(R.id.ibtnBack)
    public void baceOnclick(View view){
      onBackPressed();
    }
    @OnClick(R.id.ord_back_but)//返回首页
    public void back(View v){
        Intent intent = new Intent(this, AtyHome.class);
        // 重用堆栈里的已经启动activity
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.all_order_but)//全部订单
    public void allOrder(View view){
        Intent intent = new Intent(this,AtyFragmentOrdert.class);
        // 重用堆栈里的已经启动activity
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

    /**
     *
     * @param objectMap
     */
    public void setData(Map<String,Object> objectMap){
        TypeReference typeListMap = new TypeReference<List<Map<String, Object>>>() {
        };
        listMapGoods = (List<Map<String, Object>>) FastJSONHelper.deserializeAny(objectMap.get("GOODSLIST") + "", typeListMap);
        if (!UtilList.isEmpty(listMapGoods)) {
            if (listMapGoods.size()==1){
                mOrdergoodname.setVisibility(View.VISIBLE);
                mOrdergoodsimg.setVisibility(View.VISIBLE);
                mOrdergoodname.setText(listMapGoods.get(0).get("NAME") + "");
                Glide.with(this)
                        .load(AffConstans.PUBLIC.ADDRESS_IMAGE + listMapGoods.get(0).get("THUMB") + "")
                        .placeholder(R.drawable.default_list)
                        .error(R.drawable.default_list)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mOrdergoodsimg);
            }else {
                //清空
                mOrderLyt.removeAllViews();
                mOrdergoodname.setVisibility(View.GONE);
                mOrdergoodsimg.setVisibility(View.GONE);
                for (int i=0;i<listMapGoods.size();i++){
                    if (i<=3){
                        ImageView ball = new ImageView(this);// buyImg是动画的图片
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(195, 195);
                        //设置左边距
                        layoutParams.setMargins(16,0,0,0);
                        ball.setLayoutParams(layoutParams);
                        Glide.with(this)
                                .load(AffConstans.PUBLIC.ADDRESS_IMAGE + listMapGoods.get(i).get("THUMB") + "")
                                .placeholder(R.drawable.default_list)
                                .error(R.drawable.default_list)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(ball);
                        mOrderLyt.addView(ball);
                    }else {

                    }

                }
            }
            mOrderNum.setText("订单号：" + mOrderCode);
            mOrderMoney.setText("￥" + mOrderPrice);

        } else {
            LogUtils.e("refresh===Error=====null=========>" + listMapGoods + "");
        }
    }
}
