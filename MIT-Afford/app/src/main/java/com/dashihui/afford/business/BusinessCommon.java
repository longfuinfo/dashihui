package com.dashihui.afford.business;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.util.LogUtils;

/**
 * Created by NiuFC on 2015/10/23.
 */
public class BusinessCommon extends BusinessBase {

    private final static String PAGESIZE = "12";
    private BaseActivity mContext;

    public BusinessCommon(BaseActivity context) {
        super();
        mContext = context;
    }

    /**
     * 手机唯一标识注册
     */
    public void mobileRegister() {
        //如果成功返回UI层Map类型
        RequestParams params = new RequestParams();
        // 初始化全局异常处理类
        TelephonyManager mTm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        // 手机品牌
        params.addQueryStringParameter("BRAND", android.os.Build.BRAND);
        // 手机型号
        params.addQueryStringParameter("MODEL", android.os.Build.MODEL);
        // 操作系统
        params.addQueryStringParameter("OS", "Android");
        // 系统版本
        params.addQueryStringParameter("OSVERSION", android.os.Build.VERSION.RELEASE);
        metric = new DisplayMetrics();

        mContext.getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕分辨率
        params.addQueryStringParameter("RESOLUTION", "" + metric.widthPixels + "x" + metric.heightPixels);
        // 屏幕密度
        params.addQueryStringParameter("MIDU", metric.density + "");
        // 设备识别号，手机IMEI号
        params.addQueryStringParameter("UQID", mTm.getDeviceId());
        // 系统ID号
        params.addQueryStringParameter("OSID", android.os.Build.ID);

        params.addQueryStringParameter("TVERSION", mContext.getResources().getString(R.string.versionName));
        params.addQueryStringParameter("TYPE", "A");
        params.addQueryStringParameter("VERSION", mContext.getResources().getString(R.string.versionName));
        send(AffConstans.BUSINESS.COMMON_REGISTER, params, mContext, AffConstans.BUSINESS.TAG_COMMOON_REGISTER, SEND_MAP);
    }

    /**
     * 版本更新检查
     */
    public void checkVersion() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("TYPE", "A");
        params.addQueryStringParameter("VERSION", mContext.getResources().getString(R.string.versionName));
        send(AffConstans.BUSINESS.COMMON_CHECKVERSION, params, mContext, AffConstans.BUSINESS.TAG_COMMOON_CHECKVERSION, SEND_MAP);
    }

    /**
     * 根据百度定位获取社区及店铺信息
     *
     * @param baidukey
     */
    public void getlocation(String baidukey) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("BAIDUKEY", baidukey);
        send(AffConstans.BUSINESS.COMMON_LOCATION, params, mContext, AffConstans.BUSINESS.TAG_COMMON_LOCATION, SEND_ENTITYLOCTION);
    }

    /**
     * 根据社区ID获取社区及店铺信息
     *
     * @param coumunityID
     */
    public void getcommunity(String coumunityID) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("COMMUNITYID", coumunityID);
        send(AffConstans.BUSINESS.COMMON_GETCOMMUNITY, params, mContext, AffConstans.BUSINESS.TAG_COMMON_GETCOMMUNITY, SEND_ENTITYLOCTION);
    }

    /**
     * 首页轮播图
     */
    public void getpublicAdList() {
        RequestParams params = new RequestParams();
        send(AffConstans.BUSINESS.AD_PUBLICADLIST, params, mContext, AffConstans.BUSINESS.TAG_AD_PUBLICADLIST, SEND_LISTMAP);
    }

    /**
     * 意见反馈
     *
     * @param context
     */
    public void feedback(String context) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("CONTEXT", context);
        if (AffordApp.getInstance().getUserLogin() != null) {
            params.addQueryStringParameter("TOKEN", AffordApp.getInstance().getUserLogin().getTOKEN());
        }
        send(AffConstans.BUSINESS.COMMON_FEEDBACK, params, mContext, AffConstans.BUSINESS.TAG_COMMOON_FEEDBACK, SEND_MAP);
    }

    /**
     * 查询社区信息
     *
     * @param coumunityID
     */
    public void communityDetail(String coumunityID) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("COMMUNITYID", coumunityID);
        send(AffConstans.BUSINESS.COMMON_COMMUNITYDETAIL, params, mContext, AffConstans.BUSINESS.TAG_COMMOON_COMMUNITYDETAIL, SEND_ETYBUILDING);
    }

    /**
     * 查询商铺详情
     *
     * @param storeID
     */
    public void storeDetail(String storeID) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("STOREID", storeID);
        send(AffConstans.BUSINESS.STORE_DETAIL, params, mContext, AffConstans.BUSINESS.TAG_STORE_DETAIL, SEND_MAP);
    }

    /**
     * 商品分类查询
     */
    public void commonCategory() {
        RequestParams params = new RequestParams();
        //店铺ID
        if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance().getEntityLocation().getSTORE() != null) {
            params.addQueryStringParameter("STOREID", AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
        }
        send(AffConstans.BUSINESS.COMMON_CATEGORY, params, mContext, AffConstans.BUSINESS.TAG_COMMON_CATEGORY, SEND_LISTMAP);
    }

    /**
     * 商品分类查询
     * @param isself 是否是直营列表
     */
    public void commonCategory(String isself) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ISSELF",isself);
        //店铺ID
        if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance().getEntityLocation().getSTORE() != null) {
            params.addQueryStringParameter("STOREID", AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
        }
        send(AffConstans.BUSINESS.COMMON_CATEGORY, params, mContext, AffConstans.BUSINESS.TAG_COMMON_CATEGORY, SEND_LISTMAP);
    }
    /**
     * 店铺通知查询
     *
     */
    public void storeTip() {
        RequestParams params = new RequestParams();

        //店铺ID
        if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance().getEntityLocation().getSTORE() != null) {
            params.addQueryStringParameter("STOREID", AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
        }
        send(AffConstans.BUSINESS.STORE_TIP, params, mContext, AffConstans.BUSINESS.TAG_STORE_TIP, SEND_MAP);
    }


    /**
     * 查询百度lbs数据（在没有定位情况下用）
     * @param keyword
     */
    public void getlbs(String keyword) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("KEYWORD", keyword);
        send(AffConstans.BUSINESS.COMMON_LBS, params, mContext, AffConstans.BUSINESS.TAG_COMMON_LBS, SEND_LISTMAP);
    }

}
