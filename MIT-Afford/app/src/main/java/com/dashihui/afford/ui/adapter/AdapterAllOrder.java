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
import com.dashihui.afford.ui.activity.AtyEvaluate;
import com.dashihui.afford.ui.activity.AtyOrderState;
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
 * Created by NiuFC on 2015/11/15.
 */
public class AdapterAllOrder extends AdapterBase<Map<String, Object>> {
    private List<Map<String, Object>> mMapList;
    private FragmentOrder mFragmentOrder;

    public AdapterAllOrder(Activity context, BaseFragment fragment, List<Map<String, Object>>
            _mList) {
        super(context, _mList);
        this.mContext = context;
        this.mMapList = _mList;
        mFragmentOrder = (FragmentOrder) fragment;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.aty_all_order_item, null);
            viewHolder = new ViewHolder(mContext, mFragmentOrder, mMapList);
            //依赖注入初始化
            ViewUtils.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
            viewHolder.refresh(position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.update(position, mMapList);
        }


        viewHolder.mPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(mContext, AtySettlementOrder.class);
                mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, mMapList.get(position).get
                        ("AMOUNT") + "");
                mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, mMapList.get(position).get
                        ("ORDERNUM") + "");

                mapIntent.putExtra(AtySettlementOrder.ORDER_PAY_TYPE, CommConstans.ORDER.ORDER_PAY);
                mContext.startActivity(mapIntent);
            }
        });
        viewHolder.lytPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(mContext, AtyOrdertDetail.class);
                mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, mMapList.get(position).get
                        ("AMOUNT") + "");
                mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, mMapList.get(position).get
                        ("ORDERNUM") + "");
                mContext.startActivity(mapIntent);
            }
        });
        return convertView;
    }


    /**
     * @author p
     */
    static class ViewHolder {
        @ViewInject(R.id.all_order_goodsimg)//商品图片
        private ImageView mGoodsImg;
        @ViewInject(R.id.all_order_goodsname)//商品名称
        private TextView mTvName;
        @ViewInject(R.id.all_order_money)//实际应付款
        private TextView mTvPrice;

        @ViewInject(R.id.goods_lyt)
        private LinearLayout lytPrice;


        @ViewInject(R.id.orderState)
        private TextView mOrderState;//状态

        @ViewInject(R.id.storeName)
        private TextView mStoreName;//店的名称


        @ViewInject(R.id.all_order_delete)
        private Button mDeleteBtn;//删除订单
//        @ViewInject(R.id.btn_cancel)
//        private Button mCancelBtn;//取消订单

        @ViewInject(R.id.all_order_topay)
        private Button mPayBtn;//去支付

        @ViewInject(R.id.tv_btn_urged)
        private Button mUrged;//催单
        @ViewInject(R.id.tv_btn_notake)
        private Button mSubmitBtn;//确认收货
        @ViewInject(R.id.tv_btn_getgood)
        private Button mSubmitGoodBtn;//确认取货

        @ViewInject(R.id.all_order_track)
        private Button mOrderTrackBtn;//订单跟踪
        @ViewInject(R.id.btnOrderEvaluate)
        private Button mBtnEvaluate;//去评价

        @ViewInject(R.id.luoutImgArray)
        private LinearLayout luoutImgArray;
        @ViewInject(R.id.imgGoods01)//商品图片
        private ImageView mImgGoods01;
        @ViewInject(R.id.imgGoods02)//商品图片
        private ImageView mImgGoods02;
        @ViewInject(R.id.imgGoods03)//商品图片
        private ImageView mImgGoods03;
        @ViewInject(R.id.imgGoods04)//商品图片
        private ImageView mImgGoods04;

        //        private String mOrderNum;
        private BusinessOrder mBllOrder;

        private Activity mContent;
        private List<Map<String, Object>> mListMap;
        private int mPosition = -1;

        public ViewHolder(Activity context, BaseFragment fragment, List<Map<String, Object>>
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
         * 第一次刷新
         */
        public void refresh(final int position) {
            mPosition = position;
            if (!UtilList.isEmpty(mListMap)) {

                final List<Map<String, Object>> listMaps = (List<Map<String, Object>>) mListMap
                        .get(position).get("GOODSLIST");
                if (!UtilList.isEmpty(listMaps)) {
                    if (listMaps.size() == 1) {
                        luoutImgArray.setVisibility(View.GONE);
                        mTvName.setVisibility(View.VISIBLE);
                        mGoodsImg.setVisibility(View.VISIBLE);
                        mTvName.setText(listMaps.get(0).get("NAME") + "");
                        Glide.with(mContent)
                                .load(AffConstans.PUBLIC.ADDRESS_IMAGE + listMaps.get(0).get
                                        ("THUMB") + "")
                                .placeholder(R.drawable.default_list)
                                .error(R.drawable.default_list)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(mGoodsImg);
                    } else {
                        //清空
                        ImageView[] arrayImgView = {mImgGoods01, mImgGoods02, mImgGoods03,
                                mImgGoods04};
                        mTvName.setVisibility(View.GONE);
                        mGoodsImg.setVisibility(View.GONE);
                        luoutImgArray.setVisibility(View.VISIBLE);
                        //先全部隐藏图标
                        if (listMaps.size() < 3) {
                            for (int k = 0; k <= 3; k++) {
                                arrayImgView[k].setVisibility(View.GONE);
                            }
                        }
                        for (int i = 0; i < listMaps.size(); i++) {
                            if (i <= 3) {
                                arrayImgView[i].setVisibility(View.VISIBLE);
                                Glide.with(mContent)
                                        .load(AffConstans.PUBLIC.ADDRESS_IMAGE + listMaps.get(i)
                                                .get("THUMB") + "")
                                        .placeholder(R.drawable.default_list)
                                        .error(R.drawable.default_list)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(arrayImgView[i]);
                            } else {
                                LogUtils.e("refresh============>" + listMaps + "");
                            }

                        }
                    }

                } else {
                    LogUtils.e("refresh===Error=====null=========>" + listMaps + "");
                }
                LogUtils.e("ISSELF======================="+mListMap.get(position).get("ISSELF"));
                if(((mListMap.get(position).get("ISSELF")+"").equals("1"))){
                    mStoreName.setText("大实惠直营");
                }else{
                    mStoreName.setText(mListMap.get(position).get("STORENAME") + "");

                }
                mTvPrice.setText("￥" + mListMap.get(position).get("REALPAY") + "");
                //订单状态
                int orderState = UtilNumber.IntegerValueOf(mListMap.get(position).get
                        ("ORDERSTATE") + "");
                LogUtils.e("refresh=============订单状态==============>" + orderState);
                //订单状态，1:正常，2：已完成，3：取消，4：删除，5：过期
                if (orderState == CommConstans.SHOPORDERSTATE.NORMAL) {
                    LogUtils.e("refresh=============订单状态========111======>" + orderState);
                    /*************************正常订单状态
                     * 开始***************************************************************
                     * *
                     * ****************************************************************************************************/
                    //根据支付方式、支付状态判断要显示的按钮
                    //支付状态1：待支付，2：已支付
                    int payType = UtilNumber.IntegerValueOf(mListMap.get(position).get("PAYTYPE")
                            + "");
                    //收货方式1：送货，2：自取
                    int takeType = UtilNumber.IntegerValueOf(mListMap.get(position).get
                            ("TAKETYPE") + "");
                    /**************************************
                     * *********在线付款+送货上门************
                     * ***********************************/
                    if (payType == CommConstans.SHOPORDERPAYTYPE.ONLINE && takeType ==
                            CommConstans.SHOPORDERTAKETYPE.DELIVER) {
                        //支付状态
                        int payState = UtilNumber.IntegerValueOf(mListMap.get(position).get
                                ("PAYSTATE") + "");
                        if (payState == CommConstans.SHOPORDERPAYSTATE.NOPAY) {//支付状态为 未支付
                            mDeleteBtn.setVisibility(View.GONE);//删除订单
                            mPayBtn.setVisibility(View.VISIBLE);//去支付
                            mUrged.setVisibility(View.GONE);//催单
                            mSubmitBtn.setVisibility(View.GONE);//确认收货
                            mSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                            mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                            mBtnEvaluate.setVisibility(View.GONE);//去评价
                            mOrderState.setText("待付款");

                        } else if (payState == CommConstans.SHOPORDERPAYSTATE.HADPAY) {//订单正常
                            // 支付状态为 已支付

                            //打包状态1：未接单，2：已接单，3：打包中，4：打包完成
                            int packState = UtilNumber.IntegerValueOf(mListMap.get(position).get
                                    ("PACKSTATE") + "");
                            if (packState == CommConstans.ORDERPACKSTATE.NO_ACCEPT) {
                                mDeleteBtn.setVisibility(View.GONE);//删除订单
                                mPayBtn.setVisibility(View.GONE);//去支付
                                mUrged.setVisibility(View.VISIBLE);//催单
                                mSubmitBtn.setVisibility(View.GONE);//确认收货
                                mSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                                mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                                mBtnEvaluate.setVisibility(View.GONE);//去评价
                                mOrderState.setText("付款成功");
                            } else if (packState == CommConstans.ORDERPACKSTATE.HAD_ACCEPT) {
                                mDeleteBtn.setVisibility(View.GONE);//删除订单
                                mPayBtn.setVisibility(View.GONE);//去支付
                                mUrged.setVisibility(View.VISIBLE);//催单
                                mSubmitBtn.setVisibility(View.GONE);//确认收货
                                mSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                                mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                                mBtnEvaluate.setVisibility(View.GONE);//去评价
                                mOrderState.setText("打包中");
                            } else if (packState == CommConstans.ORDERPACKSTATE.PACKING) {
                                mDeleteBtn.setVisibility(View.GONE);//删除订单
                                mPayBtn.setVisibility(View.GONE);//去支付
                                mUrged.setVisibility(View.VISIBLE);//催单
                                mSubmitBtn.setVisibility(View.GONE);//确认收货
                                mSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                                mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                                mBtnEvaluate.setVisibility(View.GONE);//去评价
                                mOrderState.setText("打包中");
                            } else {
                                //收货状态 1：待发货，2：已发货
                                int deliverState = UtilNumber.IntegerValueOf(mListMap.get
                                        (position).get("DELIVERSTATE") + "");
                                if (deliverState == CommConstans.SHOPORDERTAKETYPE.DELIVER) {//待收货
                                    mDeleteBtn.setVisibility(View.GONE);//删除订单
                                    mPayBtn.setVisibility(View.GONE);//去支付
                                    mUrged.setVisibility(View.VISIBLE);//催单
                                    mSubmitBtn.setVisibility(View.GONE);//确认收货
                                    mSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                                    mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                                    mBtnEvaluate.setVisibility(View.GONE);//去评价
                                    mOrderState.setText("待配送");
                                } else if (deliverState == CommConstans.SHOPORDERTAKETYPE
                                        .TAKESELF) {
                                    mDeleteBtn.setVisibility(View.GONE);//删除订单
                                    mPayBtn.setVisibility(View.GONE);//去支付
                                    mUrged.setVisibility(View.GONE);//催单
                                    mSubmitBtn.setVisibility(View.VISIBLE);//确认收货
                                    mSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                                    mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                                    mBtnEvaluate.setVisibility(View.GONE);//去评价
                                    mOrderState.setText("配送中");
                                }
                            }


                        }
                        /**************************************
                         * *********在线付款+门店自取************
                         * ***********************************/
                    } else if (payType == CommConstans.SHOPORDERPAYTYPE.ONLINE && takeType ==
                            CommConstans.SHOPORDERTAKETYPE.TAKESELF) {
                        //支付状态
                        int payState = UtilNumber.IntegerValueOf(mListMap.get(position).get
                                ("PAYSTATE") + "");
                        if (payState == CommConstans.SHOPORDERPAYSTATE.NOPAY) {//支付状态为 未支付
                            mDeleteBtn.setVisibility(View.GONE);//删除订单
                            mPayBtn.setVisibility(View.VISIBLE);//去支付
                            mUrged.setVisibility(View.GONE);//催单
                            mSubmitBtn.setVisibility(View.GONE);//确认收货
                            mSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                            mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                            mBtnEvaluate.setVisibility(View.GONE);//去评价
                            mOrderState.setText("待付款");

                        } else if (payState == CommConstans.SHOPORDERPAYSTATE.HADPAY) {//订单正常
                            // 支付状态为 已支付
                            //打包状态1：未接单，2：已接单，3：打包中，4：打包完成
                            int packState = UtilNumber.IntegerValueOf(mListMap.get(position).get
                                    ("PACKSTATE") + "");
                            if (packState == CommConstans.ORDERPACKSTATE.NO_ACCEPT) {
                                mDeleteBtn.setVisibility(View.GONE);//删除订单
                                mPayBtn.setVisibility(View.GONE);//去支付
                                mUrged.setVisibility(View.GONE);//催单
                                mSubmitBtn.setVisibility(View.GONE);//确认收货
                                mSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                                mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                                mBtnEvaluate.setVisibility(View.GONE);//去评价
                                mOrderState.setText("付款成功");
                            } else if (packState == CommConstans.ORDERPACKSTATE.HAD_ACCEPT) {
                                mDeleteBtn.setVisibility(View.GONE);//删除订单
                                mPayBtn.setVisibility(View.GONE);//去支付
                                mUrged.setVisibility(View.GONE);//催单
                                mSubmitBtn.setVisibility(View.GONE);//确认收货
                                mSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                                mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                                mBtnEvaluate.setVisibility(View.GONE);//去评价
                                mOrderState.setText("打包中");
                            } else if (packState == CommConstans.ORDERPACKSTATE.PACKING) {
                                mDeleteBtn.setVisibility(View.GONE);//删除订单
                                mPayBtn.setVisibility(View.GONE);//去支付
                                mUrged.setVisibility(View.GONE);//催单
                                mSubmitBtn.setVisibility(View.GONE);//确认收货
                                mSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                                mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                                mBtnEvaluate.setVisibility(View.GONE);//去评价
                                mOrderState.setText("打包中");
                            } else {
                                mDeleteBtn.setVisibility(View.GONE);//删除订单
                                mPayBtn.setVisibility(View.GONE);//去支付
                                mUrged.setVisibility(View.GONE);//催单
                                mSubmitBtn.setVisibility(View.GONE);//确认收货
                                mSubmitGoodBtn.setVisibility(View.VISIBLE);//确认取货
                                mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                                mBtnEvaluate.setVisibility(View.GONE);//去评价
                                mOrderState.setText("待取货");
                            }
                        }
                        /**************************************
                         * *********货到付款+送货上门************
                         * ***********************************/
                    } else if (payType == CommConstans.SHOPORDERPAYTYPE.ONDELIVERY && takeType ==
                            CommConstans.SHOPORDERTAKETYPE.DELIVER) {
                        //货到付款+送货上门
                        //打包状态1：未接单，2：已接单，3：打包中，4：打包完成
                        int packState = UtilNumber.IntegerValueOf(mListMap.get(position).get
                                ("PACKSTATE") + "");
                        if (packState == CommConstans.ORDERPACKSTATE.NO_ACCEPT) {
                            mDeleteBtn.setVisibility(View.GONE);//删除订单
                            mPayBtn.setVisibility(View.GONE);//去支付
                            mUrged.setVisibility(View.VISIBLE);//催单
                            mSubmitBtn.setVisibility(View.GONE);//确认收货
                            mSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                            mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                            mBtnEvaluate.setVisibility(View.GONE);//去评价
                            mOrderState.setText("处理中");
                        } else if (packState == CommConstans.ORDERPACKSTATE.HAD_ACCEPT) {
                            mDeleteBtn.setVisibility(View.GONE);//删除订单
                            mPayBtn.setVisibility(View.GONE);//去支付
                            mUrged.setVisibility(View.VISIBLE);//催单
                            mSubmitBtn.setVisibility(View.GONE);//确认收货
                            mSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                            mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                            mBtnEvaluate.setVisibility(View.GONE);//去评价
                            mOrderState.setText("打包中");
                        } else if (packState == CommConstans.ORDERPACKSTATE.PACKING) {
                            mDeleteBtn.setVisibility(View.GONE);//删除订单
                            mPayBtn.setVisibility(View.GONE);//去支付
                            mUrged.setVisibility(View.VISIBLE);//催单
                            mSubmitBtn.setVisibility(View.GONE);//确认收货
                            mSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                            mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                            mBtnEvaluate.setVisibility(View.GONE);//去评价
                            mOrderState.setText("打包中");
                        } else {
                            //收货状态1：待发货，2：已发货
                            int deliverState = UtilNumber.IntegerValueOf(mListMap.get(position)
                                    .get("DELIVERSTATE") + "");

                            if (deliverState == CommConstans.SHOPORDERTAKETYPE.DELIVER) {//待收货
                                mDeleteBtn.setVisibility(View.GONE);//删除订单
                                mPayBtn.setVisibility(View.GONE);//去支付
                                mUrged.setVisibility(View.VISIBLE);//催单
                                mSubmitBtn.setVisibility(View.GONE);//确认收货
                                mSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                                mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                                mBtnEvaluate.setVisibility(View.GONE);//去评价
                                mOrderState.setText("待配送");
                            } else if (deliverState == CommConstans.SHOPORDERTAKETYPE.TAKESELF) {


                                mDeleteBtn.setVisibility(View.GONE);//删除订单
                                mPayBtn.setVisibility(View.GONE);//去支付
                                mUrged.setVisibility(View.GONE);//催单
                                mSubmitBtn.setVisibility(View.VISIBLE);//确认收货
                                mSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                                mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                                mBtnEvaluate.setVisibility(View.GONE);//去评价
                                mOrderState.setText("配送中");


                            }
                        }
                        /**************************************
                         * *********货到付款+门店自取 ***********
                         * ***********************************/
                    } else if (payType == CommConstans.SHOPORDERPAYTYPE.ONDELIVERY && takeType ==
                            CommConstans.SHOPORDERTAKETYPE.TAKESELF) {
                        //货到付款+门店自取
                        //打包状态1：未接单，2：已接单，3：打包中，4：打包完成
                        int packState = UtilNumber.IntegerValueOf(mListMap.get(position).get
                                ("PACKSTATE") + "");
                        if (packState == CommConstans.ORDERPACKSTATE.NO_ACCEPT) {
                            mDeleteBtn.setVisibility(View.GONE);//删除订单
                            mPayBtn.setVisibility(View.GONE);//去支付
                            mUrged.setVisibility(View.GONE);//催单
                            mSubmitBtn.setVisibility(View.GONE);//确认收货
                            mSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                            mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                            mBtnEvaluate.setVisibility(View.GONE);//去评价
                            mOrderState.setText("处理中");
                        } else if (packState == CommConstans.ORDERPACKSTATE.HAD_ACCEPT) {
                            mDeleteBtn.setVisibility(View.GONE);//删除订单
                            mPayBtn.setVisibility(View.GONE);//去支付
                            mUrged.setVisibility(View.GONE);//催单
                            mSubmitBtn.setVisibility(View.GONE);//确认收货
                            mSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                            mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                            mBtnEvaluate.setVisibility(View.GONE);//去评价
                            mOrderState.setText("打包中");
                        } else if (packState == CommConstans.ORDERPACKSTATE.PACKING) {
                            mDeleteBtn.setVisibility(View.GONE);//删除订单
                            mPayBtn.setVisibility(View.GONE);//去支付
                            mUrged.setVisibility(View.GONE);//催单
                            mSubmitBtn.setVisibility(View.GONE);//确认收货
                            mSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                            mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                            mBtnEvaluate.setVisibility(View.GONE);//去评价
                            mOrderState.setText("打包中");
                        } else {

                            mDeleteBtn.setVisibility(View.GONE);//删除订单
                            mPayBtn.setVisibility(View.GONE);//去支付
                            mUrged.setVisibility(View.GONE);//催单
                            mSubmitBtn.setVisibility(View.GONE);//确认收货
                            mSubmitGoodBtn.setVisibility(View.VISIBLE);//确认取货
                            mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                            mBtnEvaluate.setVisibility(View.GONE);//去评价
                            mOrderState.setText("待取货");
                        }
                    }
                    /*****************************订单状态 正常
                     * 结束********************************************************
                     * ****************************************************************************************************/
                } else if (orderState == CommConstans.SHOPORDERSTATE.FINISH) {//已完成
                    //订单是否评价1：是，0：否
                    int evalstate = UtilNumber.IntegerValueOf(mListMap.get(position).get
                            ("EVALSTATE") + "");
                    //已经评价
                    if (evalstate == CommConstans.SHOPORDERDELEVALSTATE.NOEVALSTATE) {
                        mDeleteBtn.setVisibility(View.GONE);//删除订单
                        mPayBtn.setVisibility(View.GONE);//去支付
                        mUrged.setVisibility(View.GONE);//催单
                        mSubmitBtn.setVisibility(View.GONE);//确认收货
                        mSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                        mOrderTrackBtn.setVisibility(View.VISIBLE);//订单跟踪
                        mBtnEvaluate.setVisibility(View.VISIBLE);//去评价
                        mOrderState.setText("交易完成");
                    } else {
                        mDeleteBtn.setVisibility(View.VISIBLE);//删除订单
                        mPayBtn.setVisibility(View.GONE);//去支付
                        mUrged.setVisibility(View.GONE);//催单
                        mSubmitBtn.setVisibility(View.GONE);//确认收货
                        mSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                        mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                        mBtnEvaluate.setVisibility(View.GONE);//去评价
                        mOrderState.setText("交易完成");
                    }

                } else if (orderState == CommConstans.SHOPORDERSTATE.CANCEL) {//已取消
                    mDeleteBtn.setVisibility(View.VISIBLE);//删除订单
                    mPayBtn.setVisibility(View.GONE);//去支付
                    mUrged.setVisibility(View.GONE);//催单
                    mSubmitBtn.setVisibility(View.GONE);//确认收货
                    mSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                    mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                    mBtnEvaluate.setVisibility(View.GONE);//去评价
                    mOrderState.setText("已取消");
                } else if (orderState == CommConstans.SHOPORDERSTATE.EXPIRE) {
                    mDeleteBtn.setVisibility(View.VISIBLE);//删除订单
                    mPayBtn.setVisibility(View.GONE);//去支付
                    mUrged.setVisibility(View.GONE);//催单
                    mSubmitBtn.setVisibility(View.GONE);//确认收货
                    mSubmitGoodBtn.setVisibility(View.GONE);//确认取货
                    mOrderTrackBtn.setVisibility(View.GONE);//订单跟踪
                    mBtnEvaluate.setVisibility(View.GONE);//去评价
                    mOrderState.setText("已过期");
                }


//                mOrderNum = mListMap.get(position).get("ORDERNUM") + "";
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
         * 催单
         */
        @OnClick(R.id.tv_btn_urged)
        public void onUrgedClick(View view) {
            final WgtAlertDialog mAtDialog = new WgtAlertDialog();
            mAtDialog.show(mContent,
                    "取消", "确定",
                    "确认催单？",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mAtDialog.dismiss();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mListMap.get(mPosition).get("ORDERNUM") != null) {
                                mBllOrder.urge(mListMap.get(mPosition).get("ORDERNUM") + "");
                                //提交服务器请求
                            } else {
                                UtilToast.show(mContent, "催单失败！", Toast.LENGTH_SHORT);
                            }
                            mAtDialog.dismiss();
                        }
                    });
        }

        /**
         * 删除订单
         */
        @OnClick(R.id.all_order_delete)
        public void onDeleteClick(View view) {
            final WgtAlertDialog mAtDialog = new WgtAlertDialog();
            mAtDialog.show(mContent,
                    "取消", "确定",
                    "确认删除订单？",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mAtDialog.dismiss();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mListMap.get(mPosition).get("ORDERNUM") != null) {
                                mBllOrder.doDeleteOrder(mListMap.get(mPosition).get("ORDERNUM") +
                                        "");//提交服务器请求
                            } else {
                                UtilToast.show(mContent, "删除订单失败！", Toast.LENGTH_SHORT);
                            }
                            mAtDialog.dismiss();
                        }
                    });
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
                    if (mListMap.get(mPosition).get("ORDERNUM") != null && !"".equals(mListMap
                            .get(mPosition).get("ORDERNUM"))) {
                        mBllOrder.doCancelOrder(mListMap.get(mPosition).get("ORDERNUM") + "");
                        //点击确定，提交服务器请求取消订单
                    } else {
                        UtilToast.show(mContent, "取消订单失败", Toast.LENGTH_SHORT);
                    }
                    mAtDialog.dismiss();
                }
            });
        }


        @OnClick(R.id.tv_btn_notake)//确认收货
        public void onNotakeClick(View v) {
            cofirmOrder();
        }

        @OnClick(R.id.tv_btn_getgood)//确认取货
        public void onGetGoodClick(View v) {
            cofirmOrder();
        }

        @OnClick(R.id.btnOrderEvaluate)//去评价
        public void onEvaluateClick(View v) {
            if (!UtilList.isEmpty(mListMap)) {
                Intent mapIntent = new Intent(mContent, AtyEvaluate.class);

                mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, mListMap.get(mPosition).get
                        ("AMOUNT") + "");
                mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, mListMap.get(mPosition).get
                        ("ORDERNUM") + "");
                mContent.startActivity(mapIntent);
            } else {
                LogUtils.e("OrderTrack=======else====订单跟踪=============>");
            }

        }

        @OnClick(R.id.all_order_track)//订单跟踪
        public void onOrderTrackClick(View v) {
            //订单跟踪
            if (!UtilList.isEmpty(mListMap)) {
                Intent mapIntent = new Intent(mContent, AtyOrderState.class);
                //支付方式 1：在线支付，2：货到付款
                mapIntent.putExtra(AtyOrderState.PAYTYPE, mListMap.get(mPosition).get("PAYTYPE")
                        + "");
                //收货方式  1：送货，2：自取
                mapIntent.putExtra(AtyOrderState.TAKETYPE, mListMap.get(mPosition).get
                        ("TAKETYPE") + "");

                mapIntent.putExtra(AtySettlementOrder.ORDER_PRICE, mListMap.get(mPosition).get
                        ("AMOUNT") + "");
                mapIntent.putExtra(AtySettlementOrder.ORDER_CODE, mListMap.get(mPosition).get
                        ("ORDERNUM") + "");
                mContent.startActivity(mapIntent);
            } else {
                LogUtils.e("OrderTrack=======else====订单跟踪=============>");
            }


        }

        /**
         * 确认收货
         */
        public void cofirmOrder() {
            final WgtAlertDialog mAtDialog = new WgtAlertDialog();
            mAtDialog.show(mContent,
                    "取消", "确定",
                    "确认收货？",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mAtDialog.dismiss();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!UtilList.isEmpty(mListMap)) {
                                if (mListMap.get(mPosition).get("ORDERNUM") != null && !"".equals
                                        (mListMap.get(mPosition).get("ORDERNUM"))) {
                                    FragmentOrder.orderCode = mListMap.get(mPosition).get
                                            ("ORDERNUM") + "";
                                    FragmentOrder.orderPrice = mListMap.get(mPosition).get
                                            ("AMOUNT") + "";
                                    mBllOrder.doReceiveOrder(mListMap.get(mPosition).get
                                            ("ORDERNUM") + "");//提交服务器请求

                                } else {
                                    UtilToast.show(mContent, "确认收货失败，请从新尝试！", Toast.LENGTH_SHORT);
                                }
                                mAtDialog.dismiss();
                            }
                        }
                    });
        }


    }
}
