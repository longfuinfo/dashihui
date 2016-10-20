package com.dashihui.afford.business;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.business.entity.EntityLocation;
import com.dashihui.afford.common.base.AffRequestCallBack;
import com.dashihui.afford.common.constants.AffConstans;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.util.LogUtils;

/**
 * Created by NiuFC on 2015/10/23.
 */
public class BusinessShop extends BusinessBase {


    private AffRequestCallBack mCallBack;
    private EntityLocation mLocation;

    public BusinessShop(AffRequestCallBack callBack) {
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
    private void sendShopCart(String method, RequestParams params, final AffRequestCallBack affCallBack, final int tag, final int sendType) {
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
            //是否收藏
            params.addQueryStringParameter("TOKEN", AffordApp.getInstance().getUserLogin().getTOKEN() + "");
        }
        send(method, params, affCallBack, tag, sendType);
    }


    /**
     * @param categoryoncode 类别代码	1：蔬菜水果，2：酒水饮料，3：生活百货，4：粮油调料，5：营养早餐
     * @param categorytwcode 商品二级类别代码
     * @param type           优惠类型	1：普通，2：推荐，3：限量，4：一元购
     * @param orderby        排序 1：默认排序，2：销量从高到低，3：价格从低到高，4：价格从高到低
     * @param pageNum        页码
     */
    public void getGoodsList(String categoryoncode, String categorytwcode, String type, String orderby, String pageNum) {
        RequestParams params = new RequestParams();

        params.addQueryStringParameter("CATEGORYONCODE", categoryoncode);
        params.addQueryStringParameter("CATEGORYTWCODE", categorytwcode);
        params.addQueryStringParameter("TYPE", type);
        params.addQueryStringParameter("ORDERBY", orderby);
        params.addQueryStringParameter("PAGENUM", pageNum);
        params.addQueryStringParameter("PAGESIZE", PAGESIZE);
        sendShopCart(AffConstans.BUSINESS.GOODS_LIST, params, mCallBack, AffConstans.BUSINESS.TAG_GOODS_LIST, SEND_ETYLIST);
    }

    /**
     * @param categoryoncode 类别代码	1：蔬菜水果，2：酒水饮料，3：生活百货，4：粮油调料，5：营养早餐
     * @param categorytwcode 商品二级类别代码
     * @param type           优惠类型	1：普通，2：推荐，3：限量，4：一元购
     * @param orderby        排序 1：默认排序，2：销量从高到低，3：价格从低到高，4：价格从高到低
     * @param isSelf         是否是自营 0:不是  1:是
     * @param isRebate       是否返利 0:不  1:是
     * @param pageNum        页码
     */
    public void getGoodsList(String categoryoncode, String categorytwcode, String type, String orderby, String pageNum,String isSelf,String isRebate) {
        RequestParams params = new RequestParams();

        params.addQueryStringParameter("CATEGORYONCODE", categoryoncode);
        params.addQueryStringParameter("CATEGORYTWCODE", categorytwcode);
        params.addQueryStringParameter("TYPE", type);
        params.addQueryStringParameter("ISSELF", isSelf);
        params.addQueryStringParameter("ISREBATE", isRebate);
        params.addQueryStringParameter("ORDERBY", orderby);
        params.addQueryStringParameter("PAGENUM", pageNum);
        params.addQueryStringParameter("PAGESIZE", PAGESIZE);
        sendShopCart(AffConstans.BUSINESS.GOODS_LIST, params, mCallBack, AffConstans.BUSINESS.TAG_GOODS_LIST, SEND_ETYLIST);
    }

//    public void getGoodsList(String categorycode,String type,String pageNum){
//        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("CATEGORYCODE", categorycode);
//        params.addQueryStringParameter("TYPE", type);
//        params.addQueryStringParameter("PAGENUM", pageNum);
//        params.addQueryStringParameter("PAGESIZE", PAGESIZE);
//        sendShopCart(AffConstans.BUSINESS.GOODS_LIST, params, mCallBack, AffConstans.BUSINESS.TAG_GOODS_LIST, SEND_ETYLIST);
//    }

    /**
     * 便利店页轮播图
     */
    public void getStoreAdList() {
        RequestParams params = new RequestParams();

        sendShopCart(AffConstans.BUSINESS.AD_STOREADLIST, params, mCallBack, AffConstans.BUSINESS.TAG_AD_STOREADLIST, SEND_LISTMAP);
    }

    /**
     * 商品详情信息
     *
     * @param goodsID
     */
    public void getGoodsDetail(String goodsID) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("GOODSID", goodsID);
        sendShopCart(AffConstans.BUSINESS.GOODS_DETAIL, params, mCallBack, AffConstans.BUSINESS.TAG_GOODS_DETAIL, SEND_ETYSHOPDETAIL);
    }

    /**
     * 店铺首页查询三种优惠类商品
     */
    public void topByType() {
        RequestParams params = new RequestParams();
        sendShopCart(AffConstans.BUSINESS.GOODS_TOPBYTYPE, params, mCallBack, AffConstans.BUSINESS.TAG_GOODS_TOPBYTYPE, SEND_MAP);
    }

    /**
     * 商品图文详情描述
     *
     * @param goodsID
     */
    public void detailDescribe(String goodsID) {
        RequestParams params = new RequestParams();

        params.addQueryStringParameter("GOODSID", goodsID);
        sendShopCart(AffConstans.BUSINESS.GOODS_DETAILDESCRIBE, params, mCallBack, AffConstans.BUSINESS.TAG_GOODS_DETAILDESCRIBE, SEND_MAP);
    }


    /**
     * 要获取的关键字数量
     *
     * @param size
     */
    public void keywords(String size) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("SIZE", size);
        sendShopCart(AffConstans.BUSINESS.SEARCH_KEYWORDS, params, mCallBack, AffConstans.BUSINESS.TAG_SEARCH_KEYWORDS, SEND_ETYLIST);
    }

    /**
     * 搜索商品列表
     *
     * @param keywordid 关键字ID 用户点击系统关键字搜索时不为空；用户自己填写的内容搜索时则为空；
     * @param keyword   关键字
     * @param pagenum   页码
     * @param orderby   排序类型
     * @param isself    是否自营
     */
    public void doSearch(String keywordid, String keyword, String pagenum,String orderby,String isself) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("KEYWORDID", keywordid);
        params.addQueryStringParameter("KEYWORD", keyword);
        params.addQueryStringParameter("PAGENUM", pagenum);
        params.addQueryStringParameter("ORDERBY", orderby);
        params.addQueryStringParameter("PAGESIZE", PAGESIZE);
        params.addQueryStringParameter("ISSELF", isself);
        sendShopCart(AffConstans.BUSINESS.SEARCH_DOSEARCH, params, mCallBack, AffConstans.BUSINESS.TAG_SEARCH_DOSEARCH, SEND_ETYLIST);
    }


    /**
     * 商品标签查询
     */
    public void goodsTags() {
        RequestParams params = new RequestParams();
        sendShopCart(AffConstans.BUSINESS.GOODS_TAGS, params, mCallBack, AffConstans.BUSINESS.TAG_GOODS_TAGS, SEND_LISTMAP);
    }

    /**
     * 商品按标签列表
     *
     * @param tagcode
     * @param pagenum
     */
    public void goodsListByTag(String tagcode, String pagenum) {
        RequestParams params = new RequestParams();
        //店铺ID
        params.addQueryStringParameter("TAGCODE", tagcode);
        params.addQueryStringParameter("PAGENUM", pagenum);
        params.addQueryStringParameter("PAGESIZE", PAGESIZE);
        sendShopCart(AffConstans.BUSINESS.GOODS_LISTBYTAG, params, mCallBack, AffConstans.BUSINESS.TAG_GOODS_LISTBYTAG, SEND_ETYLIST);
    }

    /**
     * 精品推荐商品列表
     *
     * @param pagenum
     */
    public void goodsListByRecom(String pagenum) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("PAGENUM", pagenum);
        params.addQueryStringParameter("PAGESIZE", PAGESIZE);
        sendShopCart(AffConstans.BUSINESS.GOODS_LISTBYRECOM, params, mCallBack, AffConstans.BUSINESS.TAG_GOODS_LISTBYRECOM, SEND_ETYLIST);
    }
}
