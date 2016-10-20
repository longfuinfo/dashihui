package com.dashihui.afford.common.constants;

/**
 * Created by hhz on 2015/5/12.
 */
public class CommConstans {



    /***自营标识符号**/
    public final static String SELF_TYPE ="SELF";

    public static class SHOPDETAIL {
        //便利店列表进入详情页标识
        public final static String RETURN_TAG0 ="0";
        //购物车列表进入详情页标识
        public final static String RETURN_TAG1 ="1";
        //轮播图进入详情页标识
        public final static String RETURN_TAG2 ="2";
    }


    /**
     * 公共类业务方法
     */
    public static class COMMONARRAY {
        public final static String WALLET_ARRAY[] = new String[]{"我的购物收益","好友购物收益"};
        public final static String TABS_ARRAY[] = new String[]{"全部","待付款","待收货","待评价"};
        public final static String TABS_DISCOUNT[] = new String[]{"推荐","限量","一元购"};
        public final static String TABS_SERVICE[] = new String[]{"待付款","待服务","已完成"};
        public final static String TABS_ORDERDETAIL[] = new String[]{"订单状态","订单详情"};
    }
    public static class Login {
        /**** 本地存储账号文件中存储账号列表的键名***/
        public static final String SHPREFER_USER_LIST = "UserList";
        /****自动登录标志****/
        public static final String SHPREFER_AUTO_LOGIN = "AutoLogin";
        /****设置自动登录时间****/
        public static final String AUTO_LOGIN_SET_TIME = "AutoLoginSetTime";
        /***** 用户名*** */
        public static final String SHPREFER_USER_NAME = "UserName";
        /*****密码******/
        public static final String SHPREFER_USER_PSWD = "UserPswd";
        /***** 用户签名****/
        public static final String TOKEN = "TOKEN";
        public static final String INTENT_KEY = "loginType";
        /******用户实惠币******/
        public static final String MONEY = "money";
        /*****用户邀请好友**/
        public static final String FRIEND = "friend";
        /***** 结算界面过来****/
        public static final String INTENT_VALUE_SETTLEMENT = "settlement";
        public static final String INTENT_VALUE_LOGIN = "login";
    }
    public static class REGISTER {
        public static final String INTENT_KEY_TYPE = "titleType";//新用户注册
        //        public static final String INTENT_KEY_CODE = "code";//验证码
        public static final String INTENT_KEY_PHONE = "phone";//手机号码
        public static final String INTENT_NO_RIGEDIT = "GONERIGEDIT";//是否隐藏注册标识


        public final static String INTENT_VALUE_REGISTER = "1";//新用户注册
        public final static String INTENT_VALUE_PWD = "2";//找回密码
        public final static String INTENT_VALUE_PWDRESET = "3";//密码重置
        public final static String INTENT_VALUE_PHONERESET = "4";//变更手机号码


        public final static String BROADCAST_INTENT_ACTION = "com.dashihui.afford.ACTION_COUNT";

    }

    /**
     * 用户新添加地址dialog传给新增地址时所用常量key
     */
    public static class ADDRESS {
        /********地址Id，楼号Id，单元Id，门牌号ID *******/
        public final static String ADDRESSID = "addressID";
        /************ 收货人姓名，收货人电话，收货人性别 ,地址***********/
        public final static String USERNAME = "userName";
        public final static String USERTEL = "userTel";
        public final static String USERSEX = "userSex";
        public final static String USERADDRESS = "ADDRESS";
        public final static String ISDEFAULT = "isdefault";
        /********* 修改地址intent传值的唯一标示 ********/
        public final static int CODE = 101;
        public final static int MARS =  CODE+2;
        public final static int SERVERDATAS = CODE+3;
    }

    public static class SERVER {
        public static final String SERVER_CODE = "CODE";
        public static final String SERVER_NAME = "NAME";
        public static final String SERVER_IMAGE = "IMAGE";
        public final static String INTENT_TAG_TYPE ="tagType";

    }

    /***************商品订单状态*******************/
    public static class SHOPORDER {
        public final static String  PAYTYPE = "PAYTYPE"; //支付方式
        public final static String  TAKETYPE = "TAKETYPE";//收货方式
        public final static String  PAYMETHOD = "PAYMETHOD";// 支付渠道
        public final static String  PAYSTATE = "PAYSTATE";//支付状态
        public final static String  DELIVERSTATE = "DELIVERSTATE";//收货状态
        public final static String  ORDERSTATE = "ORDERSTATE";//订单状态
        public final static String  PACKSTATE = "PACKSTATE";//打包状态
    }

    /**
     * 订单状态
     * 订单状态，1:正常，2：已完成，3：取消，4：删除，5：过期
     */
    public static class SHOPORDERSTATE {
        public final static int  NORMAL = 1;
        public final static int  FINISH = 2;
        public final static int  CANCEL = 3;
        public final static int  DELETE = 4;
        public final static int  EXPIRE = 5;
        public final static int  CLOSE = 6;
    }

    /**
     * 订单支付类型
     * 支付方式，1：在线支付，2：货到付款
     */
    public static class SHOPORDERPAYTYPE {
        public final static int  ONLINE   = 1;
        public final static int  ONDELIVERY = 2;
    }

    /**
     * 订单支付状态
     * 支付状态，1：待支付，2：已支付
     */
    public static class SHOPORDERPAYSTATE {
        public final static int  NOPAY  = 1;
        public final static int  HADPAY = 2;
    }

    /**
     * 收货方式
     * 收货方式，1：送货，2：自取
     */
    public static class SHOPORDERTAKETYPE {
        public final static int DELIVER = 1;
        public final static int TAKESELF = 2;
    }

    /**
     * 收货状态
     * 收货状态，1：待发货，2：已经发货
     */
    public static class SHOPORDERDELIVERSTATE {
        public final static int NODELIVER = 1;
        public final static int HADDELIVER= 2;
    }


    /**
     * 订单是否评价
     * 订单是否评价 1：是，0：否
     */
    public static class SHOPORDERDELEVALSTATE {
        public final static int NOEVALSTATE = 0;
        public final static int HADEVALSTATE =1;
    }
    /**
     * 打包状态
     * 打包状态，1：未接单，2：已接单，3：打包中，4：打包完成
     */
    public static class ORDERPACKSTATE{
        public final static int NO_ACCEPT = 1;
        public final static int HAD_ACCEPT = 2;
        public final static int PACKING = 3;
        public final static int PACK_FINISH = 4;
    }

    /*************** 服务订单状态*******************/

    public static class FRAGSERSTATEORDERCODE {
        public final static String  TYPE = "TYPE";
        public final static String  PAYTYPE = "PAYTYPE";
        public final static String  PAYSTATE = "PAYSTATE";
        public final static String  DELIVERSTATE = "DELIVERSTATE";
        public final static String  ORDERSTATE = "ORDERSTATE";
        public final static String  STARTDATE = "STARTDATE";
        public final static String  PAYMETHOD = "PAYMETHOD";
        public final static String  ORDERNUM = "ORDERNUM";
        public final static String  SERVICETITLE = "SERVICETITLE";
        public final static String  AMOUNT = "AMOUNT";
    }

    /**
     * 订单状态
     * 订单类型为家政服务时：1：正常，2：完成，3：取消,4：拒单，5：删除，6：过期,7：无效
     */
    public static class FRAGSERSTATEORDERSTATE {
        public final static int  NORMAL = 1;
        public final static int  FINISH = 2;
        public final static int  CANCEL = 3;
        public final static int  REJECT = 4;
        public final static int  DELETE = 5;
        public final static int  EXPIRE = 6;
        public final static int  INVALID = 7;
    }


    /**
     * 订单状态
     * 订单状态，1:正常，2：已完成，3：取消，4：删除，5：过期
     */
    public static class FRAGSERSTATESERVERORDERSTATE{
        public final static int  NORMAL = 1;
        public final static int  FINISH = 2;
        public final static int  CANCEL = 3;
        public final static int  DELETE = 4;
        public final static int  EXPIRE = 5;
    }

    /**
     * 订单类型，1：家政服务，2：其他服务
     */
    public static class FRAGSERSTATEORDERTYPE {
        public final static int  HOUSESER   = 1;
        public final static int  SERVER = 2;
    }

    /**
     * 服务类型
     * 服务类型 1:日常保洁 2:深度保洁
     */
    public static class FRAGSERSTATECLEANTYPE{
        public final static int DAILY = 1;
        public final static int DEPTH = 2;
    }

    /**
     * 订单支付类型
     * 支付方式，1：在线支付，2：服务后付款
     */
    public static class FRAGSERSTATEORDERPAYTYPE {
        public final static int  ON_LINE   = 1;
        public final static int  AFTER_SERVICE = 2;
    }

    /**
     * 订单支付状态
     * 支付状态，1：待支付，2：已支付
     */
    public static class FRAGSERSTATEORDERPAYSTATE {
        public final static int  NO_PAY  = 1;
        public final static int  HAD_PAY = 2;
    }

    /**
     * 派单状态
     * 派单状态,1:店铺待接单,2:店铺已接单，3：店铺已派单 ,4:商家确认接单
     */
    public static class FRAGSERSTATEORDERDELIVERSTATE {
        public final static int STORE_NO_ACCEPT = 1;
        public final static int STORE_HAD_ACCEPT = 2;
        public final static int STORE_HAD_DISPATCH = 3;
        public final static int PROSER_HAD_ACCEPT = 4;
    }

    /**
     * 派单状态
     * 派单状态，1：待派单，2：已派单
     */
    public static class FRAGSERSTATEORDERDISPATCHSTATE {
        public final static int NO_DISPATCH = 1;
        public final static int HAD_DISPATCH= 2;
    }

    /***************用户定位到大实惠总部时 提示用户不可购买商品***************/

    public static class LOCCOMMUNITY{
        public final static int COMMUNITYID = 1;//大实惠总部ID
    }


    /**
     * 在线支付的类型 商品、服务、家政
     */
    public static class ORDER{
        /*******
         * 订单传输 值
         *******/
        public final static String ORDER_PAY = "1";
        public final static String SERVER_ORDER_PAY = "2";//服务
        public final static String HOUSE_ORDER_PAY = "3";//家政
    }


}
