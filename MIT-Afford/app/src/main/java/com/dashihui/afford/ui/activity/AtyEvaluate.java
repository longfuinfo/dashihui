package com.dashihui.afford.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessOrder;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.util.number.UtilNumber;
import com.dashihui.afford.util.string.UtilString;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class AtyEvaluate extends BaseActivity {

    @ViewInject(R.id.ratBarShop)
    private RatingBar mRBarShop;//商品满意度
    @ViewInject(R.id.ratBarSpeed)
    private RatingBar mRBarSpeed;//配送速度满意度
    @ViewInject(R.id.ratBarServer)
    private RatingBar mRBarServer;//服务质量满意度
    @ViewInject(R.id.et_feedback)
    private EditText mEdtxtFeedBack;
    @ViewInject(R.id.title)
    private TextView mTxtView;

    private BusinessOrder mBllOrder;
    private  String mOrderCode="";
    private  String mOrderPrice="";
    private int mShopLastX=5,mSpeedLastX=5,mServerLastX=5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_evaluate);
        ViewUtils.inject(this);//依赖注入
        mTxtView.setText(R.string.order_comment);
        mBllOrder= new BusinessOrder(this);
        mOrderCode = getIntent().getStringExtra(AtySettlementOrder.ORDER_CODE);
        mOrderPrice = getIntent().getStringExtra(AtySettlementOrder.ORDER_PRICE_STORE);
        mRBarShop.setRating(5);
        mRBarSpeed.setRating(5);
        mRBarServer.setRating(5);

        /**
         *  商品满意度
         *  评分的ratingbar评分范围为1-5星
         *  根据首饰判断ratingbar的操作，如果是在向左滑动即减星的时候，当为1星时setIsIndicator值为true
         *  这时ratingbar就只起显示作用MotionEvent
         */
        mRBarShop.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mShopLastX = UtilNumber.IntegerValueOf(rating+"");
                LogUtils.e("mRBarShop============商品满意度==*****************************====rating======>"+ rating);
            }
        });
        //配送速度满意度
        mRBarSpeed.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mSpeedLastX = UtilNumber.IntegerValueOf(rating+"");
            }
        });
        //服务质量满意度
        mRBarServer.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mServerLastX = UtilNumber.IntegerValueOf(rating+"");
            }
        });

    }


    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_ORDER_EVAL://评价成功
                    dissProDialog();
                    Toast.makeText(this,"评价成功！",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                default:
                    break;
            }
        } else {
            LogUtils.e("onSuccess=======error====>" + beanSendUI);
        }
    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_ORDER_EVAL://评价失败
                    dissProDialog();
                    Toast.makeText(this,"评分服务器忙，请稍后再试！",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        } else {
            LogUtils.e("onSuccess=======error====>" + beanSendUI);
        }
    }
    @OnClick(R.id.tvbtn_submit)//提交评论
    public void onSumbitClick(View view){
        if (mShopLastX ==0 ||mSpeedLastX==0 ||mServerLastX==0){
            Toast.makeText(this,"请对我们的服务打分！",Toast.LENGTH_SHORT).show();
            return;
        }
        String feedback = mEdtxtFeedBack.getText() +"";
        if (!UtilString.isEmpty(feedback)){
            if (feedback.length()<500){
                showProDialog(this);
                mBllOrder.orderEval(mOrderCode,mShopLastX+"",mShopLastX+"",mShopLastX+"",feedback);
            }else {
                Toast.makeText(this,"评价不能大于500字",Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(this,"您还未对本次购物进行评价",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 返回
     *
     * @param v
     */
    @OnClick(R.id.ibtnBack)
    public void onBtnBackClick(View v) {
        onBackPressed();
    }
}
