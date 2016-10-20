package com.dashihui.afford.business;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.business.entity.EntityLocation;
import com.dashihui.afford.common.base.AffRequestCallBack;
import com.dashihui.afford.common.constants.AffConstans;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.util.LogUtils;

/**
 * Created by NiuFC on 2015/11/18.
 */
public class BusinessUser extends BusinessBase {
    private AffRequestCallBack mCallBack;
    private EntityLocation mLocation;

    public BusinessUser(AffRequestCallBack callBack){
        super();
        mCallBack = callBack;
    }



    /**
     * 用户登录后
     * @param method
     * @param params
     * @param affCallBack
     * @param tag
     * @param sendType
     */
    private void userSend(String method, RequestParams params, final AffRequestCallBack affCallBack,final int tag, final int sendType){
        if (params == null){
            params = new RequestParams();
        }
        mLocation = AffordApp.getInstance().getEntityLocation();
        //店铺ID
        if (mLocation != null) {
            LogUtils.e("STOREID====店铺ID===>" + mLocation.getSTORE().getID());
            params.addQueryStringParameter("STOREID", mLocation.getSTORE().getID() + "");
        }
        if (AffordApp.getInstance()!=null && AffordApp.getInstance().getUserLogin()!=null ){
            LogUtils.e("AffordApp.TOKEN===用户登录====>"+AffordApp.getInstance().getUserLogin().getTOKEN());
            params.addQueryStringParameter("TOKEN", AffordApp.getInstance().getUserLogin().getTOKEN()+"");
            LogUtils.e("Login===============>"+AffordApp.getInstance().getUserLogin().getTOKEN()+"");
        }

        send(method, params, affCallBack, tag, sendType);
    }
    /**
     * 用户注册时获取短信验证码
     * @param phone 手机号码
     */
    public void sendregcode(String phone){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("PHONE", phone);
        userSend(AffConstans.BUSINESS.USER_SENDREGCODE, params, mCallBack, AffConstans.BUSINESS
                .TAG_USER_SENDREGCODE, SEND_MAP);
    }
    /**
     * //用户注册
     * @param phone 手机号码
     * @param password 手机密码
     * @param code 验证码
     * @param invite 邀请码
     */
    public void regist(String phone,String password, String code, String invite){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("PHONE",phone);
        params.addQueryStringParameter("PASSWORD", password);
        params.addQueryStringParameter("CODE", code);
        params.addQueryStringParameter("INVITECODE", invite);
        if (AffordApp.getInstance().getEntityLocation()!=null && AffordApp.getInstance().getEntityLocation().getCOMMUNITY()!=null){
            LogUtils.e("用户注册========大实惠总部社区===================>" + AffordApp.getInstance().getEntityLocation().getCOMMUNITY().getID());
            params.addQueryStringParameter("COMMUNITYID", AffordApp.getInstance().getEntityLocation().getCOMMUNITY().getID() + "");
        }
        userSend(AffConstans.BUSINESS.USER_REGIST, params, mCallBack, AffConstans.BUSINESS.TAG_USER_RIGIST, SEND_MAP);
    }
    /**
     * 用户登录
     * @param userName
     * @param pwd
     */
    public void login(String userName,String pwd){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("PHONE", userName);
        params.addQueryStringParameter("PASSWORD", pwd);
        userSend(AffConstans.BUSINESS.USER_LOGIN, params, mCallBack, AffConstans.BUSINESS.TAG_USER_LOGIN, SEND_ENTITYLOGIN);
    }
    /**
     * 获取登录用户个人信息
     */
    public void getInfo(){
        RequestParams params = new RequestParams();
        userSend(AffConstans.BUSINESS.USER_INFO, params, mCallBack, AffConstans.BUSINESS.TAG_USER_INFO, SEND_MAP);
    }
    /**
     * 获取登录用户的实惠币
     */
    public void getMoney(){
        RequestParams params = new RequestParams();
        userSend(AffConstans.BUSINESS.USER_MONEY,params,mCallBack,AffConstans.BUSINESS.TAG_USER_MONEY,SEND_MAP);
    }
    /**
     * 获取用户实惠币变动记录
     *
     * @param searchtype    1:我的购物收益明细;
                              2:好友购物收益明细;
                              3:我的消费记录;
                              4:好友购物收益合计
     * @param pagenum
     */

    public void getExpenseRecord(String searchtype,String pagenum){
        LogUtils.e("onSuccess=========mMapList=successEty===>" + searchtype+"和"+pagenum);
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("SIGNATURE", AffordApp.getInstance().getmAffordBean().getSIGNATURE());
            params.addQueryStringParameter("TOKEN", AffordApp.getInstance().getUserLogin().getTOKEN()+"");
            params.addQueryStringParameter("SEARCHTYPE", searchtype);
            params.addQueryStringParameter("PAGENUM", pagenum);
            params.addQueryStringParameter("PAGESIZE", SERHOUSEPAGESIZE);
            userSend(AffConstans.BUSINESS.USER_EXPENSDRECORD, params, mCallBack, AffConstans.BUSINESS.TAG_USER_EXPENSDRECORD, SEND_ETYLIST);
    }

    /**
     * 修改登录用户个人信息
     * @param nickName 昵称
     * @param sex 性别
             1：男，2：女
     * @param avator 头像
     * @param communityID 社区ID
     * @param buildID 楼号ID
     * @param unitID 单元号ID
     * @param roomID 房间号ID
     */
    public void update(String nickName,String sex,String avator,String communityID,String buildID,String unitID,String roomID){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("NICKNAME",nickName);
        params.addQueryStringParameter("SEX",sex);
        params.addQueryStringParameter("AVATOR",avator);
        params.addQueryStringParameter("COMMUNITYID",communityID);
        params.addQueryStringParameter("BUILDID",buildID);
        params.addQueryStringParameter("UNITID", unitID);
        params.addQueryStringParameter("ROOMID", roomID);
        userSend(AffConstans.BUSINESS.USER_UPDATE, params, mCallBack, AffConstans.BUSINESS.TAG_USER_UPDATE, SEND_MAP);
    }
    /**
     * 用户收藏商品
     * @param goodsID
     */
    public void docollect(String goodsID){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("GOODSID", goodsID);
        userSend(AffConstans.BUSINESS.USER_DOCOLLECT, params, mCallBack, AffConstans.BUSINESS.TAG_USER_DOCOLLECT, SEND_MAP);
    }

    /**
     * 用户收藏商品
     * @param goodsID 商品ID
     * @param isself 是否自营 0：非自营 1自营
     */
    public void docollect(String goodsID,String isself){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("GOODSID", goodsID);
        params.addQueryStringParameter("ISSELF", isself);
        userSend(AffConstans.BUSINESS.USER_DOCOLLECT, params, mCallBack, AffConstans.BUSINESS
                .TAG_USER_DOCOLLECT, SEND_MAP);
    }
    /**
     * 用户取消收藏商品
     * @param goodsID
     */
    public void cancelcollect(String goodsID){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("GOODSID", goodsID);
        userSend(AffConstans.BUSINESS.USER_CANCELCOLLECT, params, mCallBack, AffConstans.BUSINESS.TAG_USER_CANCELCOLLECT, SEND_MAP);
    }

    /**
     * 收藏商品列表
     * @param pageNum
     */
    public void collectionlist(String pageNum){
        RequestParams params = new RequestParams();

        params.addQueryStringParameter("PAGENUM", pageNum);
        params.addQueryStringParameter("PAGESIZE", PAGESIZE);
        userSend(AffConstans.BUSINESS.USER_COLLECTIONLIST, params, mCallBack, AffConstans.BUSINESS.TAG_USER_COLLECTIONLIST, SEND_ETYLIST);
    }

    /**
     * 收货地址列表
     */
//    public void addresslist(){
//        RequestParams params = new RequestParams();
//        userSend(AffConstans.BUSINESS.USER_ADDRESSLIST, params, mCallBack, AffConstans.BUSINESS.TAG_USER_ADDRESSLIST, SEND_LISTMAP);
//    }

    /**
     * 收货地址列表
     */
    public void addresslist(){
        RequestParams params = new RequestParams();
        userSend(AffConstans.BUSINESS.USER_ADDRESSLIST, params, mCallBack, AffConstans.BUSINESS.TAG_USER_ADDRESSLIST, SEND_LISTMAP);
    }

    /**
     * 收货地址添加
     * @param nickName 昵称
     * @param sex 性别
                1：男，2：女
     * @param tel 联系电话，座机或手机
     * @param buildID 楼号ID
     * @param unitID 单元号ID
     * @param roomID 房间门牌号ID
     * @param isdefault 是否默认
                     1：是，0：否
     */
//    public void addaddress(String nickName,String sex,String tel,String buildID,String unitID,String roomID,String isdefault){
//        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("LINKNAME",nickName);
//        params.addQueryStringParameter("SEX",sex);
//        params.addQueryStringParameter("TEL",tel);
//        if (AffordApp.getInstance().getEntityLocation()!=null && AffordApp.getInstance().getEntityLocation().getCOMMUNITY()!=null){
//            params.addQueryStringParameter("COMMUNITYID",AffordApp.getInstance().getEntityLocation().getCOMMUNITY().getID()+"");
//        }
//        params.addQueryStringParameter("BUILDID",buildID);
//        params.addQueryStringParameter("UNITID",unitID);
//        params.addQueryStringParameter("ROOMID", roomID);
//        params.addQueryStringParameter("ISDEFAULT",isdefault);
//        userSend(AffConstans.BUSINESS.USER_ADDADDRESS, params, mCallBack, AffConstans.BUSINESS.TAG_USER_ADDADDRESS, SEND_MAP);
//    }

    /**
     * 收货地址添加
     * @param nickName 昵称
     * @param sex 性别
     *            1：男，2：女
     * @param tel 电话
     * @param address 详细地址
     * @param isdefault 是否默认
     *                  1：是，0：否
     */
    public void addaddress(String nickName,String sex,String tel,String address,String isdefault){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("LINKNAME",nickName);
        params.addQueryStringParameter("SEX",sex);
        params.addQueryStringParameter("TEL",tel);
        params.addQueryStringParameter("ADDRESS",address);
        params.addQueryStringParameter("ISDEFAULT",isdefault);
        userSend(AffConstans.BUSINESS.USER_ADDADDRESS, params, mCallBack, AffConstans.BUSINESS.TAG_USER_ADDADDRESS, SEND_MAP);
    }

    /**
     * 收货地址删除
     * @param addressID
     */
    public void deladdress(String addressID){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ADDRESSID",addressID);
        userSend(AffConstans.BUSINESS.USER_DELADDRESS, params, mCallBack, AffConstans.BUSINESS.TAG_USER_DELADDRESS, SEND_MAP);
    }

    /**
     * 收货地址修改
     * @param addressID
     * @param linkname
     * @param sex
     * @param tel
     * @param buildID
     * @param unitID
     * @param roomID
     * @param isdefault
     */
//    public void updateaddress(String addressID,String linkname,String sex,String tel,String buildID,String unitID,String roomID,String isdefault){
//        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("ADDRESSID",addressID);
//        params.addQueryStringParameter("LINKNAME",linkname);
//        params.addQueryStringParameter("SEX",sex);
//        params.addQueryStringParameter("TEL",tel);
//        if (AffordApp.getInstance().getEntityLocation()!=null && AffordApp.getInstance().getEntityLocation().getCOMMUNITY()!=null){
//            params.addQueryStringParameter("COMMUNITYID",AffordApp.getInstance().getEntityLocation().getCOMMUNITY().getID()+"");
//        }
//        params.addQueryStringParameter("BUILDID",buildID);
//        params.addQueryStringParameter("UNITID",unitID);
//        params.addQueryStringParameter("ROOMID", roomID);
//        params.addQueryStringParameter("ISDEFAULT",isdefault);
//        userSend(AffConstans.BUSINESS.USER_UPDATEADDRESS, params, mCallBack, AffConstans.BUSINESS.TAG_USER_UPDATEADDRESS, SEND_MAP);
//    }

    /**
     * 收货地址修改
     * @param addressID 地址ID
     * @param linkname 用户名
     * @param sex 性别
     * @param tel 电话
     * @param address 地址
     * @param isdefault 是否默认
     */
    public void updateaddress(String addressID,String linkname,String sex,String tel,String address,String isdefault){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ADDRESSID",addressID);
        params.addQueryStringParameter("LINKNAME",linkname);
        params.addQueryStringParameter("SEX",sex);
        params.addQueryStringParameter("TEL",tel);
        params.addQueryStringParameter("ADDRESS",address);
        params.addQueryStringParameter("ISDEFAULT",isdefault);
        userSend(AffConstans.BUSINESS.USER_UPDATEADDRESS, params, mCallBack, AffConstans.BUSINESS.TAG_USER_UPDATEADDRESS, SEND_MAP);
    }

    /**
     * 收货地址详情
     * @param addressID
     */
    public void addressdetail(String addressID){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ADDRESSID",addressID);
        userSend(AffConstans.BUSINESS.USER_ADDRESSDETAIL, params, mCallBack, AffConstans.BUSINESS.TAG_USER_ADDRESSDETAIL, SEND_MAP);
    }

    /**
     * 查询默认收货地址详情
     */
//    public void defaultAddress(){
//        RequestParams params = new RequestParams();
//        userSend(AffConstans.BUSINESS.USER_DEFAULTADDRESS, params, mCallBack, AffConstans.BUSINESS.TAG_USER_DEFAULTADDRESS, SEND_MAP);
//    }
//
    /**
     * 查询默认收货地址详情
     */
    public void defaultAddress(){
        RequestParams params = new RequestParams();
        userSend(AffConstans.BUSINESS.USER_DEFAULTADDRESS, params, mCallBack, AffConstans.BUSINESS.TAG_USER_DEFAULTADDRESS, SEND_MAP);
    }
    /**
     * 找回密码 发送验证码
     * @param phone
     */
    public void sendResetPwdCode(String phone){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("PHONE",phone);
        userSend(AffConstans.BUSINESS.USER_SENDRESETPWDCODE, params, mCallBack, AffConstans.BUSINESS.TAG_USER_SENDRESETPWDCODE, SEND_MAP);
    }

    /**
     * //重置密码
     * @param phone
     * @param code
     * @param pwd
     * @param verifyPwd
     */
    public void resetPwd(String phone,String pwd,String verifyPwd,String code){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("PHONE",phone);
        params.addQueryStringParameter("CODE",code);
        params.addQueryStringParameter("PWD",pwd);
        params.addQueryStringParameter("VERIFYPWD",verifyPwd);

        userSend(AffConstans.BUSINESS.USER_RESETPWD, params, mCallBack, AffConstans.BUSINESS.TAG_USER_RESETPWD, SEND_MAP);
    }

    /**
     * 用户订单状态查询
     */
    public void getOrderState(){
        RequestParams params = new RequestParams();
        userSend(AffConstans.BUSINESS.USER_STATE, params, mCallBack, AffConstans.BUSINESS.TAG_USER_STATE, SEND_MAP);
    }

    /**
     * 服务订单列表（0：全部 1：待付款 2：待发货 3：待收货，4：已完成）
     *
     * @param flag
     * @param pagenum
     */
    public void userServerOrderList(String flag, String pagenum) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("FLAG", flag);
        params.addQueryStringParameter("PAGENUM", pagenum);
        params.addQueryStringParameter("PAGESIZE", PAGESIZE);
        userSend(AffConstans.BUSINESS.USER_SERVICEORDERLIST, params, mCallBack, AffConstans.BUSINESS.TAG_USER_SERVICEORDERLIST, SEND_ETYLIST);
    }
}
