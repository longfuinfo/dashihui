package com.dashihui.afford.business;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.business.entity.EntityLocation;
import com.dashihui.afford.common.base.AffRequestCallBack;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.ui.model.ModelServerOrder;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.util.LogUtils;

/**
 * Created by NiuFC on 2015/10/23.
 */
public class BusinessServer extends BusinessBase {


    private AffRequestCallBack mCallBack;
    private EntityLocation mLocation;

    public BusinessServer(AffRequestCallBack callBack) {
        super();
        mCallBack = callBack;

    }

    /**
     * shop 根方法
     *
     * @param method
     * @param params
     * @param affCallBack
     * @param tag
     * @param sendType
     */
    private void sendServer(String method, RequestParams params, final AffRequestCallBack affCallBack, final int tag, final int sendType) {
        if (params == null) {
            params = new RequestParams();
        }
        mLocation = AffordApp.getInstance().getEntityLocation();
        //店铺ID
        if (mLocation != null) {
            LogUtils.e("STOREID====店铺ID===>" + mLocation.getSTORE().getID());
            params.addQueryStringParameter("STOREID", mLocation.getSTORE().getID() + "");
        }
        //店铺ID
        if (AffordApp.getInstance() != null && AffordApp.getInstance().getUserLogin() != null) {
            LogUtils.e("AffordApp.TOKEN===用户登录====>" + AffordApp.getInstance().getUserLogin().getTOKEN());
            params.addQueryStringParameter("TOKEN", AffordApp.getInstance().getUserLogin().getTOKEN() + "");
        }
        send(method, params, affCallBack, tag, sendType);
    }

    /**
     * 服务分类获取
     */
    public void getCategory() {
        RequestParams params = new RequestParams();
        sendServer(AffConstans.BUSINESS.SERVICE_CATEGORY, params, mCallBack, AffConstans.BUSINESS.TAG_SERVICE_CATEGORY, SEND_LISTMAP);
    }

    /**
     * 服务商家列表获取
     *
     * @param categorycode 分类代码
     * @param pageNum      页码
     */
    public void getServersShops(String categorycode, String pageNum) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("CATEGORYCODE", categorycode);
        params.addQueryStringParameter("PAGENUM", pageNum);
        params.addQueryStringParameter("PAGESIZE", PAGESIZE);
        sendServer(AffConstans.BUSINESS.SERVICE_SHOPS, params, mCallBack, AffConstans.BUSINESS.TAG_SERVICE_SHOPS, SEND_ETYLIST);
    }

    /**
     * 商家详情
     *
     * @param shopid 服务商家ID
     */
    public void getDetail(String shopid) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("SHOPID", shopid);
        sendServer(AffConstans.BUSINESS.SERVICE_DETAIL, params, mCallBack, AffConstans.BUSINESS.TAG_SERVICE_DETAIL, SEND_ETYSERVERDETAIL);
    }

    /**
     * 商家详情描述
     *
     * @param shopid 服务商家ID
     */
    public void getDescribe(String shopid) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("SHOPID", shopid);
        sendServer(AffConstans.BUSINESS.SERVICE_DESCRIBE, params, mCallBack, AffConstans.BUSINESS.TAG_SERVICE_DESCRIBE, SEND_MAP);
    }


    /**
     * 获取服务时间
     */
    public void getOrderTimes() {
        RequestParams params = new RequestParams();
        sendServer(AffConstans.BUSINESS.SERVICE_ORDER_TIMES, params, mCallBack, AffConstans.BUSINESS.TAG_SERVICE_ORDER_TIMES, SEND_ETYSERVERTIME);
    }


    /**
     * 服务保存订单
     *
     * @param serverOrder
     */
    public void getOrderSave(ModelServerOrder serverOrder) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("TEL", serverOrder.getTel());
        params.addQueryStringParameter("PAYTYPE", serverOrder.getPaytype());
        params.addQueryStringParameter("LINKNAME", serverOrder.getLinkname());
        params.addQueryStringParameter("SEX", serverOrder.getSex());
        params.addQueryStringParameter("ADDRESS", serverOrder.getAddress());
        params.addQueryStringParameter("DESCRIBE", serverOrder.getDescribe());
        params.addQueryStringParameter("AMOUNT", serverOrder.getAmount());

        params.addQueryStringParameter("SERTIME", serverOrder.getSertime());
        params.addQueryStringParameter("SERVICESID", serverOrder.getServicesid());
        sendServer(AffConstans.BUSINESS.SERVICE_ORDER_SAVE, params, mCallBack, AffConstans.BUSINESS.TAG_SERVICE_ORDER_SAVE, SEND_MAP);
    }


    /**
     * 查询支付结果
     *
     * @param ordernum
     */
    public void getOrderQuery(String ordernum) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", ordernum);
        sendServer(AffConstans.BUSINESS.SERVICE_ORDER_QUERY, params, mCallBack, AffConstans.BUSINESS.TAG_SERVICE_ORDER_QUERY, SEND_MAP);
    }


    /**
     * 服务
     * 订单列表（0：全部，1:待付款，2：待服务，3：已完成）
     *
     * @param pagenum
     * @param flag
     */
    public void getOrderList(String pagenum, String flag) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("PAGENUM", pagenum);
        params.addQueryStringParameter("PAGESIZE", SERHOUSEPAGESIZE);//一次加载完毕，100条数据
        params.addQueryStringParameter("FLAG", flag);
        sendServer(AffConstans.BUSINESS.ORDER_LIST, params, mCallBack, AffConstans.BUSINESS.TAG_ORDER_LIST, SEND_ETYLIST);
    }

    /**
     * //取消订单
     *
     * @param ordernum
     */
    public void getOrderCancel(String ordernum) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", ordernum);
        sendServer(AffConstans.BUSINESS.SERVICE_ORDER_CANCEL, params, mCallBack, AffConstans.BUSINESS.TAG_SERVICE_ORDER_CANCEL, SEND_MAP);
    }

    /**
     * 删除订单
     *
     * @param ordernum
     */
    public void getOrderDelete(String ordernum) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", ordernum);
        sendServer(AffConstans.BUSINESS.SERVICE_ORDER_DELETE, params, mCallBack, AffConstans.BUSINESS.TAG_SERVICE_ORDER_DELETE, SEND_MAP);
    }

    /**
     * 签收
     *
     * @param ordernum
     */
    public void getOrderSign(String ordernum) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", ordernum);
        sendServer(AffConstans.BUSINESS.SERVICE_ORDER_SIGN, params, mCallBack, AffConstans.BUSINESS.TAG_SERVICE_ORDER_SIGN, SEND_MAP);
    }

    /**
     * 订单详情
     *
     * @param ordernum
     */
    public void getOrderDetail(String ordernum) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", ordernum);
        sendServer(AffConstans.BUSINESS.SERVICE_ORDER_DETAIL, params, mCallBack, AffConstans.BUSINESS.TAG_SERVICE_ORDER_DETAIL, SEND_MAP);
    }

    /**
     * 订单支付
     *
     * @param ordernum
     * @param flag
     */
    public void getOrderPay(String ordernum,String flag) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", ordernum);
        params.addQueryStringParameter("FLAG", flag);
        sendServer(AffConstans.BUSINESS.SERVICE_ORDER_PAY, params, mCallBack, AffConstans.BUSINESS.TAG_SERVICE_ORDER_PAY, SEND_MAP);
    }

    /**
     * 催单
     *
     * @param ordernum
     */
    public void getOrderUrge(String ordernum) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", ordernum);
        sendServer(AffConstans.BUSINESS.SERVICE_ORDER_URGE, params, mCallBack, AffConstans.BUSINESS.TAG_SERVICE_ORDER_URGE, SEND_MAP);
    }


    /**********************************以下是服务：家政***********************************/

    /**
     * 服务家政保存订单
     *
     * @param serOrder
     */
    public void getSerOrderSave(ModelServerOrder serOrder) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("STOREID", serOrder.getStoreid());
        params.addQueryStringParameter("STORENAME", serOrder.getStorename());
        params.addQueryStringParameter("TYPE", serOrder.getStoreid());
        params.addQueryStringParameter("LINKNAME", serOrder.getLinkname());
        params.addQueryStringParameter("SEX", serOrder.getSex());
        params.addQueryStringParameter("TEL", serOrder.getTel());
        params.addQueryStringParameter("ADDRESS", serOrder.getAddress());
        params.addQueryStringParameter("SERTIME", serOrder.getSertime());
        params.addQueryStringParameter("TOTALTIME", serOrder.getTotaltime());
        params.addQueryStringParameter("TITLE", serOrder.getTitle());
        params.addQueryStringParameter("UNITPRICE", serOrder.getUnitprice());
        params.addQueryStringParameter("COUNT", serOrder.getCount());
        params.addQueryStringParameter("AMOUNT", serOrder.getAmount());
        params.addQueryStringParameter("PAYTYPE", serOrder.getPaytype());
        params.addQueryStringParameter("DESCRIBE", serOrder.getDescribe());

        sendServer(AffConstans.BUSINESS.SER_ORDER_SAVE, params, mCallBack, AffConstans.BUSINESS.TAG_SER_ORDER_SAVE, SEND_MAP);
    }

    /**
     * 服务家政 支付订单
     *
     * @param orderNum
     * @param flag
     */
    public void getSerOrderPay(String orderNum, String flag) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", orderNum);
        params.addQueryStringParameter("FLAG", flag);
        sendServer(AffConstans.BUSINESS.SER_ORDER_PAY, params, mCallBack, AffConstans.BUSINESS.TAG_SER_ORDER_PAY, SEND_MAP);
    }

    /**
     * 服务家政
     * 查询支付结果
     *
     * @param ordernum
     */
    public void getSerOrderQuery(String ordernum) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", ordernum);
        sendServer(AffConstans.BUSINESS.SER_ORDER_QUERY, params, mCallBack, AffConstans.BUSINESS.TAG_SER_ORDER_QUERY, SEND_MAP);
    }

    /**
     * 服务
     * 取消订单
     *
     * @param ordernum
     * @param content
     */
    public void getSerOrderCancel(String ordernum, String content) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", ordernum);
        params.addQueryStringParameter("CONTENT", content);
        sendServer(AffConstans.BUSINESS.SER_ORDER_CANCEL, params, mCallBack, AffConstans.BUSINESS.TAG_SER_ORDER_CANCEL, SEND_MAP);
    }

    /**
     * 服务家政
     * 删除订单
     *
     * @param ordernum
     */
    public void getSerOrderDelete(String ordernum) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", ordernum);
        sendServer(AffConstans.BUSINESS.SER_ORDER_DELETE, params, mCallBack, AffConstans.BUSINESS.TAG_SER_ORDER_DELETE, SEND_MAP);
    }

    /**
     * 服务家政
     * 评价订单
     *
     * @param ordernum
     * @param content
     * @param star
     */
    public void getSerOrderEvaluate(String ordernum, String content, String star) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", ordernum);
        params.addQueryStringParameter("CONTENT", content);
        params.addQueryStringParameter("STAR", star);
        sendServer(AffConstans.BUSINESS.SER_ORDER_EVALUATE, params, mCallBack, AffConstans.BUSINESS.TAG_SER_ORDER_EVALUATE, SEND_MAP);
    }


    /**
     * 服务家政
     * 订单列表（0：全部，1:待付款，2：待服务，3：已完成）
     *
     * @param pagenum
     * @param flag
     */
    public void getSerOrderList(String pagenum, String flag) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("PAGENUM", pagenum);
        params.addQueryStringParameter("PAGESIZE", SERHOUSEPAGESIZE);//一次加载完毕，100条数据
        params.addQueryStringParameter("FLAG", flag);
        sendServer(AffConstans.BUSINESS.SER_ORDER_LIST, params, mCallBack, AffConstans.BUSINESS.TAG_SER_ORDER_LIST, SEND_ETYLIST);
    }


    /**
     * 服务家政
     * 订单数量
     * <p/>
     * 待支付  NOPAY:12
     * 待服务  NOSERVICE:12
     * 已完成  FINISH:12
     */
    public void getSerOrderCount() {
        RequestParams params = new RequestParams();
        sendServer(AffConstans.BUSINESS.SER_ORDER_COUNT, params, mCallBack, AffConstans.BUSINESS.TAG_SER_ORDER_COUNT, SEND_MAP);
    }

    /**
     * 签收
     *
     * @param ordernum
     */
    public void getSerOrderSign(String ordernum) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", ordernum);
        sendServer(AffConstans.BUSINESS.SER_ORDER_SIGN, params, mCallBack, AffConstans.BUSINESS.TAG_SER_ORDER_SIGN, SEND_MAP);
    }


    /**
     * 服务家政
     * 订单详情
     *
     * @param ordernum
     */
    public void getSerOrderDetail(String ordernum) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", ordernum);
        sendServer(AffConstans.BUSINESS.SER_ORDER_DETAIL, params, mCallBack, AffConstans.BUSINESS.TAG_SER_ORDER_DETAIL, SEND_MAP);
    }

    /**
     * 家政催单
     *
     * @param ordernum
     */
    public void getSerOrderUrge(String ordernum) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ORDERNUM", ordernum);
        sendServer(AffConstans.BUSINESS.SER_ORDER_URGE, params, mCallBack, AffConstans.BUSINESS.TAG_SER_ORDER_URGE, SEND_MAP);
    }

}

