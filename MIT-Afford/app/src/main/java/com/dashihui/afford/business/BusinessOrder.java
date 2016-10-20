package com.dashihui.afford.business;

import android.util.Log;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.common.base.AffRequestCallBack;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.ui.model.ModelDoAdd;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.util.LogUtils;

/**
 * Created by NiuFC on 2015/11/22.
 */
public class BusinessOrder extends BusinessBase {

    private AffRequestCallBack mCallBack;

    public BusinessOrder(AffRequestCallBack callBack) {
        super();
        mCallBack = callBack;
    }


    /**
     * 用户登录后
     *
     * @param method
     * @param params
     * @param affCallBack
     * @param tag
     * @param sendType
     */
    private void orderSend(String method, RequestParams params, final AffRequestCallBack affCallBack, final int tag, final int sendType) {
        if (params == null) {
            params = new RequestParams();
        }
        //店铺ID
        if (AffordApp.getInstance() != null && AffordApp.getInstance().getUserLogin() != null) {
            LogUtils.e("AffordApp.TOKEN===用户登录====>" + AffordApp.getInstance().getUserLogin().getTOKEN());
            params.addQueryStringParameter("TOKEN", AffordApp.getInstance().getUserLogin()
                    .getTOKEN() + "");
        }
        send(method, params, affCallBack, tag, sendType);
    }


    /**
     * 保存订单
     *
     * @param modelDoAdd 订单实体类
     */
    public void sava(ModelDoAdd modelDoAdd) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("STOREID", modelDoAdd.getStoreid());//店铺ID
        params.addQueryStringParameter("STOREGOODSIDS", modelDoAdd.getStoregoodsids());//门店商品ID
        params.addQueryStringParameter("SELFGOODSIDS", modelDoAdd.getSelfgoodsids());//直营商品ID
        params.addQueryStringParameter("STOREAMOUNT", modelDoAdd.getStoreamount());//店铺商品总金额
        params.addQueryStringParameter("SELFAMOUNT", modelDoAdd.getSelfamount());//自营商品总金额
        params.addQueryStringParameter("TEL", modelDoAdd.getTel()); //电话
        params.addQueryStringParameter("SELFCOUNTS", modelDoAdd.getSelfcounts());//直营商品数量
        params.addQueryStringParameter("STORECOUNTS", modelDoAdd.getStorecounts());//店铺商品数量
        params.addQueryStringParameter("SELFPAYTYPE", modelDoAdd.getPaytypeself());//直营支付方式 默认在线支付
        params.addQueryStringParameter("STOREPAYTYPE", modelDoAdd.getPaytypeshop()); //门店支付方式
        params.addQueryStringParameter("SELFTAKETYPE", modelDoAdd.getTaketypeself()); //直营送货方式
        params.addQueryStringParameter("STORETAKETYPE", modelDoAdd.getTaketypeshop());//门店支付方式
        params.addQueryStringParameter("LINKNAME", modelDoAdd.getLinkname()); //收货人姓名
        params.addQueryStringParameter("SEX", modelDoAdd.getSex()); //性别
        params.addQueryStringParameter("ADDRESS", modelDoAdd.getAddress()); //收货地址
        params.addQueryStringParameter("DESCRIBE", modelDoAdd.getDescribe());//订单备注
        params.addQueryStringParameter("ISREDEEM",modelDoAdd.getIsredeem());//是否抵用实惠币

        orderSend(AffConstans.BUSINESS.ORDER_SAVE, params, mCallBack, AffConstans.BUSINESS.TAG_ORDER_SAVA, SEND_MAP);
    }

    /**
     * 签收订单
     *
     * @param ordernum 订单号
     */
    public void doReceiveOrder(String ordernum) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", ordernum);
        orderSend(AffConstans.BUSINESS.ORDER_DORECEIVEORDER, params, mCallBack, AffConstans.BUSINESS.TAG_ORDER_DORECEIVEORDER, SEND_MAP);
    }

    /**
     * 取消订单
     *
     * @param ordernum
     */
    public void doCancelOrder(String ordernum) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", ordernum);
        orderSend(AffConstans.BUSINESS.ORDER_DOCANCELORDER, params, mCallBack, AffConstans.BUSINESS.TAG_ORDER_DOCANCELORDER, SEND_MAP);
    }

    /**
     * 删除订单
     *
     * @param ordernum
     */
    public void doDeleteOrder(String ordernum) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", ordernum);
        orderSend(AffConstans.BUSINESS.ORDER_DELETE, params, mCallBack, AffConstans.BUSINESS.TAG_ORDER_DELETE, SEND_MAP);
    }

    /**
     * 根据订单号查询出订单以及订单清单
     *
     * @param ordernum
     */
    public void getOrderDetail(String ordernum) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", ordernum);
        orderSend(AffConstans.BUSINESS.ORDER_DETAIL, params, mCallBack, AffConstans.BUSINESS.TAG_ORDER_DETAIL, SEND_MAP);
    }

    /**
     * (待付款 待发货 待收货)订单
     *
     * @param flag    0：全部 1：待付款 2：待发货 3：待收货，4：已完成
     * @param pagenum
     */
    public void ordeList(String flag, String pagenum) {
        RequestParams params = new RequestParams();
        if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance().getEntityLocation().getSTORE() != null) {
            params.addQueryStringParameter("STOREID", AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
        }
        params.addQueryStringParameter("FLAG", flag);
        params.addQueryStringParameter("PAGENUM", pagenum);
        params.addQueryStringParameter("PAGESIZE", SERHOUSEPAGESIZE);
        orderSend(AffConstans.BUSINESS.ORDER_LIST, params, mCallBack, AffConstans.BUSINESS.TAG_ORDER_LIST, SEND_ETYLIST);
    }


    /**
     * 订单支付
     *
     * @param ordernum
     * @param flag
     */
    public void pay(String ordernum, String flag) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", ordernum);
        params.addQueryStringParameter("FLAG", flag);
        orderSend(AffConstans.BUSINESS.ORDER_PAY, params, mCallBack, AffConstans.BUSINESS.TAG_ORDER_PAY, SEND_MAP);
    }

    /**
     * 查询支付结果
     *
     * @param ordernum
     */
    public void queryPay(String ordernum) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", ordernum);
        orderSend(AffConstans.BUSINESS.ORDER_QUERY, params, mCallBack, AffConstans.BUSINESS.TAG_ORDER_QUERY, SEND_MAP);
    }

    /**
     * 催单
     *
     * @param ordernum
     */
    public void urge(String ordernum) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", ordernum);
        orderSend(AffConstans.BUSINESS.ORDER_URGE, params, mCallBack, AffConstans.BUSINESS.TAG_ORDER_URGE, SEND_MAP);
    }

    /**
     * 订单跟踪
     *
     * @param ordernum
     */
    public void getOrderTrack(String ordernum) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", ordernum);
        orderSend(AffConstans.BUSINESS.ORDER_TRACK, params, mCallBack, AffConstans.BUSINESS.TAG_ORDER_TRACK, SEND_LISTMAP);
    }

    /**
     * 订单跟踪
     *
     * @param ordernum 订单号
     * @param EVAL1    商品满意度
     * @param EVAL2    配送速度满意度
     * @param EVAL3    服务质量满意度
     * @param content  评价内容
     */
    public void orderEval(String ordernum, String EVAL1, String EVAL2, String EVAL3, String content) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", ordernum);
        params.addQueryStringParameter("EVAL1", EVAL1);
        params.addQueryStringParameter("EVAL2", EVAL2);
        params.addQueryStringParameter("EVAL3", EVAL3);
        params.addQueryStringParameter("CONTENT", content);
        orderSend(AffConstans.BUSINESS.ORDER_EVAL, params, mCallBack, AffConstans.BUSINESS.TAG_ORDER_EVAL, SEND_MAP);
    }

    /**
     * 退款单列表
     *
     * @param pagenum
     */
    public void getOrderRefund(String pagenum) {
        RequestParams params = new RequestParams();
        if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance().getEntityLocation().getSTORE() != null) {
            params.addQueryStringParameter("STOREID", AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
        }
        params.addQueryStringParameter("PAGENUM", pagenum);
        params.addQueryStringParameter("PAGESIZE", SERHOUSEPAGESIZE);
        orderSend(AffConstans.BUSINESS.ORDER_REFUND, params, mCallBack, AffConstans.BUSINESS.TAG_ORDER_REFUND, SEND_ETYLIST);
    }


}
