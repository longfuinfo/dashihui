package com.dashihui.afford.common.constants;

/**
 * Created by hhz on 2015/5/12.
 */
public class AffConstans {

    /**
     * 公共的
     */
    public static class PUBLIC {

        public static final String RESULT_STATE_SUCCESS = "0";//成功返回标识
        public static final String RESULT_STATE_ERROR = "-1";//错误返回标识
        public static final String RESULT_STATE_RIGEDIT_ERROR = "102";//重复注册
        public static final String RESULT_STATE_MOBLERIGEDIT_ERROR = "901";//手机没有注册，需要退出重新登录

        public static final String ADDRESS = "http://api.91dashihui.com/";//服务地址
        public static final String ADDRESS_IMAGE = "http://static.91dashihui.com";//服务地址

        public static final int TIMEOUT = 1000 * 15;//网络连接超时时间
        public static final int CACHETIME = 1000 * 5;//网络缓存时间
        public static final String INFO_AK = "S2kGRESvR7jHYhPI8XEfZylv";//大实惠服务端数据
        public static final int INFO_geoTableId = 125217;//DSHData 表ID
        public static final int INFO_RADIUS= 900000;//范围

        public static final String APP_CACAHE_DIRNAME ="android/com.dashihui.afford";
    }

    public static class BUSINESS{
        private final static String COMMON = "common/";
        private final static String GOODS = "goods/";
        private final static String AD = "ad/";
        private final static String USER = "user/";
        private final static String ORDER = "order/";
        private final static String SEARCH = "search/";
        private final static String SERVICE = "service/";
        private final static String SER = "ser/";
        private final static String STORE = "store/";

        private final static int TAG_CODE = 1000;
        /** 服务API公共接口*/
        public final static String COMMON_REGISTER = COMMON+"regist";
        public final static String COMMON_LOCATION = COMMON+"location";
        public final static String COMMON_CHECKVERSION = COMMON+"checkVersion";//版本更新检查
        public final static String COMMON_TEST = COMMON+"test";//测试连接
        public final static String COMMON_FEEDBACK = COMMON+"feedback";//feedback
        public final static String COMMON_GETCOMMUNITY = COMMON+"getCommunity";//获取登录用户所在社区的社区及店铺信息
        public final static String COMMON_COMMUNITYDETAIL = COMMON+"communityDetail";//查询社区信息
        public final static String COMMON_CATEGORY = COMMON+"category";//商品分类查询

        public final static String COMMON_LBS = COMMON+"lbs";//查询百度lbs数据（在没有定位情况下用）


        /** 商品接口*****/
        public final static String GOODS_LIST = GOODS+"list";
        public final static String GOODS_DETAIL = GOODS+"detail";
        public final static String GOODS_TOPBYTYPE = GOODS+"topByType";//店铺首页查询三种优惠类商品
        public final static String GOODS_DETAILDESCRIBE = GOODS+"detailDescribe";////商品详情描述
        public final static String AD_PUBLICADLIST = AD+"publicAdList";
        public final static String AD_STOREADLIST = AD+"storeAdList";

        public final static String GOODS_TAGS = GOODS + "tags";//商品标签查询
        public final static String GOODS_LISTBYTAG = GOODS + "listByTag";//商品按标签列表
        public final static String GOODS_LISTBYRECOM = GOODS + "listByRecom";//精品推荐商品列表

        /*************商铺详情************/
        public final static String STORE_DETAIL = STORE+"detail";
        /*************店铺通知查询************/
        public final static String STORE_TIP = STORE+"tip";

        /***** 用户接口****/
        public final static String USER_SENDREGCODE = USER+"sendRegCode";//用户注册时获取短信验证码
        public final static String USER_REGIST = USER+"regist";//用户注册
        public final static String USER_LOGIN = USER+"login";//用户登录
        public final static String USER_INFO = USER+"info";//获取登录用户个人信息
        public final static String USER_RULE = USER+"rule";//会员奖励
        public final static String USER_SHARE = USER+"share";//会员分享
        public final static String USER_MONEY = USER+"money";//会员实惠币
        public final static String USER_EXPENSDRECORD= USER+"expenseRecord";//会员实惠币变动记录



        public final static String USER_UPDATE = USER+"update";//修改登录用户个人信息
        public final static String USER_DOCOLLECT = USER+"doCollect";//用户收藏商品
        public final static String USER_CANCELCOLLECT = USER+"cancelCollect";//用户取消收藏商品
        public final static String USER_COLLECTIONLIST = USER+"collectionList";//收藏商品列表
        public final static String USER_ADDRESSLIST = USER+"addressList";//收货地址列表


        public final static String USER_ADDADDRESS = USER+"addAddress";//收货地址添加
        public final static String USER_DELADDRESS = USER+"delAddress";//收货地址删除
        public final static String USER_UPDATEADDRESS = USER+"updateAddress";//收货地址修改
        public final static String USER_ADDRESSDETAIL = USER+"addressDetail";//收货地址详情
        public final static String USER_DEFAULTADDRESS = USER+"defaultAddress";//收货地址详情

        public final static String USER_SENDRESETPWDCODE = USER+"sendResetPwdCode";//发送验证码
        //public final static String USER_LOOKFORPWD = USER+"lookforPwd";//找回密码 校验验证码
        public final static String USER_RESETPWD = USER+"resetPwd";//修改密码

        public final static String USER_STATE = USER+"state";//用户订单状态查询
        public final static String USER_SERVICEORDERLIST = USER+"serviceOrderList";//用户服务订单列表（0：全部，1：待付款，2：待服务，3：已完成）


        /***** 订单接口 ****/
        public final static String ORDER_SAVE = ORDER+"save";//保存订单
        public final static String ORDER_QUERY= ORDER+"query";//查询支付结果
        public final static String ORDER_LIST= ORDER+"list";//(全部、待付款 待发货 待收货)订单列表
        public final static String ORDER_DOCANCELORDER = ORDER+"cancel";//取消订单
        public final static String ORDER_DELETE = ORDER+"delete";//删除订单

        public final static String ORDER_DORECEIVEORDER = ORDER+"receive";//签收订单
        public final static String ORDER_DETAIL = ORDER+"detail";//订单详情
        public final static String ORDER_PAY= ORDER+"pay";//订单支付
        public final static String ORDER_URGE= ORDER+"urge";//催单

        public final static String ORDER_TRACK= ORDER+"track";//订单跟踪

        public final static String ORDER_EVAL= ORDER+"eval";//订单评价

        public final static String ORDER_REFUND= ORDER+"refund";//退款单列表


        /***** 搜索接口 ****/
        public final static String SEARCH_KEYWORDS = SEARCH+"keywords";//获取关键字列表
        public final static String SEARCH_DOSEARCH = SEARCH+"doSearch";//搜索商品列表


        /***** 服务接口 ****/
        public final static String SERVICE_CATEGORY = SERVICE+"category";//服务分类获取
        public final static String SERVICE_SHOPS = SERVICE+"shops";//服务商家列表获取
        public final static String SERVICE_DETAIL = SERVICE+"detail";//商家详情
        public final static String SERVICE_DESCRIBE = SERVICE+"describe";//商家详情描述
        public final static String SERVICE_ORDER_LIST= SERVICE+"order/list";//(全部、待付款 待发货 待收货)服务订单列表
        public final static String SERVICE_ORDER_TIMES = SERVICE+"order/times";//获取服务时间

        public final static String SERVICE_ORDER_SAVE = SERVICE+"order/save";//服务保存订单
        public final static String SERVICE_ORDER_QUERY = SERVICE+"order/query";//查询支付结果
        public final static String SERVICE_ORDER_CANCEL = SERVICE+"order/cancel";//取消订单
        public final static String SERVICE_ORDER_DELETE = SERVICE+"order/delete";// 删除订单
        public final static String SERVICE_ORDER_SIGN = SERVICE+"order/sign";//签收
        public final static String SERVICE_ORDER_DETAIL = SERVICE+"order/detail";//订单详情


        public final static String SERVICE_ORDER_PAY = SERVICE+"order/pay";//订单支付
        public final static String SERVICE_ORDER_URGE = SERVICE+"order/urge";//催单

        /***** 服务家政接口 ****/
        public final static String SER_ORDER_DAILY = SER+"order/daily";//服务家政日常保洁接口
        public final static String SER_ORDER_DEPTH = SER+"order/depth";//服务家政深度保洁接口
        public final static String SER_ORDER_SAVE = SER+"order/save";//服务家政保存订单
        public final static String SER_ORDER_PAY = SER+"order/pay";//服务家政支付订单
        public final static String SER_ORDER_QUERY = SER+"order/query";//服务家政查询订单
        public final static String SER_ORDER_CANCEL = SER+"order/cancel";//服务家政取消订单
        public final static String SER_ORDER_DELETE = SER+"order/delete";//服务家政删除订单
        public final static String SER_ORDER_EVALUATE = SER+"order/evaluate";//服务家政评价订单
        public final static String SER_ORDER_LIST = SER+"order/list";//服务家政订单列表
        public final static String SER_ORDER_COUNT = SER+"order/count";//服务家政统计订单数量
        public final static String SER_ORDER_DETAIL = SER+"order/detail";//服务家政订单详情
        public final static String SER_ORDER_SIGN = SER+"order/sign";//服务家政订单签收
        public final static String SER_ORDER_URGE = SER+"order/urge";//家政催单

        //日常保洁服务器请求地址
        public final static String DAILY_URL = AffConstans.PUBLIC.ADDRESS + AffConstans.BUSINESS.SER_ORDER_DAILY;
        //深度保洁服务器请求地址
        public final static String DEPTH_URL = AffConstans.PUBLIC.ADDRESS + AffConstans.BUSINESS.SER_ORDER_DEPTH;

        /******************************** UI协议唯一标识*********************************************/
        public final static int TAG_COMMOON_REGISTER = TAG_CODE +1;// 手机注册
        public final static int TAG_COMMON_LOCATION = TAG_CODE +2; //根据定位获取社区信息、店铺信息
        public final static int TAG_COMMOON_CHECKVERSION = TAG_CODE +25;//版本更新检查
        public final static int TAG_COMMOON_TEST = TAG_CODE +26;//测试连接
        public final static int TAG_COMMOON_FEEDBACK = TAG_CODE +27;// 意见反馈
        public final static int TAG_COMMOON_COMMUNITYDETAIL = TAG_CODE +28;//查询社区信息
        public final static int TAG_COMMON_GETCOMMUNITY= TAG_CODE +11;//获取登录用户所在社区的社区及店铺信息

        public final static int TAG_COMMON_CATEGORY = TAG_CODE +73;//商品分类查询
        public final static int TAG_COMMON_LBS = TAG_CODE+83;//查询百度lbs数据（在没有定位情况下用）

        /*****************商品**********************/
        public final static int TAG_GOODS_LIST = TAG_CODE +3;//便利店商品列表
        public final static int TAG_GOODS_TOPBYTYPE= TAG_CODE+40;//店铺首页查询三种优惠类商品
        public final static int TAG_GOODS_DETAIL= TAG_CODE+42;//商品详情
        public final static int TAG_GOODS_DETAILDESCRIBE= TAG_CODE+41;//商品详情描述
        public final static int TAG_AD_PUBLICADLIST = TAG_CODE +4;//首页轮播图
        public final static int TAG_AD_STOREADLIST = TAG_CODE +5;//便利店轮播图
        public final static int TAG_AD_GOODSDETAIL = TAG_CODE +6;//商品详情


        public final static int TAG_USER_SENDREGCODE = TAG_CODE +7;//用户注册时获取短信验证码
        public final static int TAG_USER_RIGIST = TAG_CODE +8;//用户注册
        public final static int TAG_USER_LOGIN = TAG_CODE +9;//用户登录
        public final static int TAG_USER_INFO = TAG_CODE +10;//获取登录用户个人信息
        public final static int TAG_USER_MONEY = TAG_CODE + 91;//获取用户的实惠币
        public final static int TAG_USER_EXPENSDRECORD = TAG_CODE + 92;//获取用户的实惠币变动记录


        public final static int TAG_USER_UPDATE= TAG_CODE +12;//修改登录用户个人信息
        public final static int TAG_USER_DOCOLLECT = TAG_CODE +13;//用户收藏商品
        public final static int TAG_USER_CANCELCOLLECT= TAG_CODE +14;//用户取消收藏商品
        public final static int TAG_USER_COLLECTIONLIST= TAG_CODE +15;//收藏商品列表
        public final static int TAG_USER_ADDRESSLIST = TAG_CODE +16;//收货地址列表

        public final static int TAG_USER_ADDADDRESS = TAG_CODE +17;//收货地址添加
        public final static int TAG_USER_DELADDRESS = TAG_CODE +18;//收货地址删除
        public final static int TAG_USER_UPDATEADDRESS = TAG_CODE +19;//收货地址修改
        public final static int TAG_USER_ADDRESSDETAIL = TAG_CODE +20;//收货地址详情
        public final static int TAG_USER_DEFAULTADDRESS = TAG_CODE +21;//收货地址详情


        public final static int TAG_USER_SENDRESETPWDCODE = TAG_CODE +22;//
        public final static int TAG_USER_LOOKFORPWD = TAG_CODE +23;//收货地址详情
        public final static int TAG_USER_RESETPWD = TAG_CODE +24;//收货地址详情
        public final static int TAG_USER_STATE= TAG_CODE+38;//用户订单状态查询

        //***************************************************29
        /***** 订单接口 ****/

        public final static int TAG_ORDER_SAVA = TAG_CODE+31;//保存订单
        public final static int TAG_ORDER_DORECEIVEORDER = TAG_CODE+32;//签收订单
        public final static int TAG_ORDER_DOCANCELORDER = TAG_CODE+33;//取消订单
        public final static int TAG_ORDER_DELETE = TAG_CODE+34;//删除订单
        public final static int TAG_ORDER_DETAIL = TAG_CODE+35;//订单详情
        public final static int TAG_ORDER_PAY = TAG_CODE + 45;//订单支付
        public final static int TAG_ORDER_QUERY= TAG_CODE + 46;//查询支付结果
        public final static int TAG_ORDER_URGE= TAG_CODE + 47;//催单
        public final static int TAG_ORDER_TRACK= TAG_CODE+80;//订单跟踪

        public final static int TAG_ORDER_EVAL= TAG_CODE+81;//订单评价

        public final static int TAG_ORDER_REFUND= TAG_CODE+82;//退款单列表

        public final static int TAG_ORDER_LIST= TAG_CODE+36;//(待付款 待发货 待收货)订单
        public final static int TAG_ORDER_GETTOTALORDERBYSTATE= TAG_CODE+37;//统计(待付款 待发货 待收货)订单


        //***************************************************4
        /***** 搜索接口 ****/
        public final static int TAG_SEARCH_KEYWORDS = TAG_CODE + 43;//获取关键字列表
        public final static int TAG_SEARCH_DOSEARCH = TAG_CODE + 44;//搜索商品列表



        /***** 服务接口 ****/
        public final static int TAG_SERVICE_CATEGORY = TAG_CODE+48;//服务分类获取
        public final static int TAG_SERVICE_SHOPS = TAG_CODE+49;//服务商家列表获取
        public final static int TAG_SERVICE_DETAIL = TAG_CODE+50;//商家详情
        public final static int TAG_SERVICE_DESCRIBE = TAG_CODE+51;//商家详情描述
        public final static int TAG_SERVICE_ORDER_LIST= TAG_CODE+52;//(全部、待付款 待发货 待收货)订单列表
        public final static int TAG_SERVICE_ORDER_TIMES = TAG_CODE+53;//获取服务时间


        public final static int TAG_SERVICE_ORDER_SAVE = TAG_CODE+54;//服务保存订单
        public final static int TAG_SERVICE_ORDER_QUERY = TAG_CODE+55;//查询支付结果
        public final static int TAG_SERVICE_ORDER_CANCEL = TAG_CODE+56;//取消订单
        public final static int TAG_SERVICE_ORDER_DELETE = TAG_CODE+57;// 删除订单
        public final static int TAG_SERVICE_ORDER_SIGN = TAG_CODE+58;//签收
        public final static int TAG_SERVICE_ORDER_DETAIL = TAG_CODE+59;//订单详情

        public final static int TAG_SERVICE_ORDER_PAY = TAG_CODE+60;//订单支付
        public final static int TAG_SERVICE_ORDER_URGE = TAG_CODE+61;//催单

        /***** 服务家政 ****/
        public final static int TAG_SER_ORDER_SAVE = TAG_CODE+62;//服务家政保存订单
        public final static int TAG_SER_ORDER_PAY = TAG_CODE+63;//服务家政支付订单
        public final static int TAG_SER_ORDER_QUERY = TAG_CODE+64;//服务家政查询支付状态
        public final static int TAG_SER_ORDER_CANCEL = TAG_CODE+65;//服务家政取消订单
        public final static int TAG_SER_ORDER_DELETE = TAG_CODE+66;//服务家政删除订单
        public final static int TAG_SER_ORDER_EVALUATE = TAG_CODE+67;//服务家政评论订单
        public final static int TAG_SER_ORDER_LIST = TAG_CODE+68;//服务家政订单列表
        public final static int TAG_SER_ORDER_COUNT = TAG_CODE+69;//服务家政订单统计数量
        public final static int TAG_SER_ORDER_DETAIL = TAG_CODE+70;//服务家政订单详情
        public final static int TAG_SER_ORDER_SIGN = TAG_CODE+71;//服务家政订单签收
        public final static int TAG_STORE_DETAIL = TAG_CODE+72;//服务家政订单签收
        //***********************73已用在商品分类列表****************************73
        public final static int TAG_GOODS_TAGS = TAG_CODE+74;//商品标签查询
        public final static int TAG_GOODS_LISTBYTAG = TAG_CODE+75;//商品标签查询
        public final static int TAG_GOODS_LISTBYRECOM = TAG_CODE+76;//商品标签查询


        /***************************************************/
        public final static int TAG_USER_SERVICEORDERLIST = TAG_CODE+77;//用户服务订单列表（0：全部，1：待付款，2：待服务，3：已完成）
        public final static int TAG_STORE_TIP = TAG_CODE + 78;//店铺通知查询
        public final static int TAG_SER_ORDER_URGE = TAG_CODE + 79;//家政催单

        /**************************************查询百度lbs数据（在没有定位情况下用）*******83*/

    }




}
