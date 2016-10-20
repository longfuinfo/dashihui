package com.dashihui.afford.business;

import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.TypeReference;
import com.dashihui.afford.AffordApp;
import com.dashihui.afford.business.entity.EntityLocation;
import com.dashihui.afford.business.entity.EtyBuilding;
import com.dashihui.afford.business.entity.EtyList;
import com.dashihui.afford.business.entity.EtyLogin;
import com.dashihui.afford.business.entity.EtyRespone;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.business.entity.EtyServerDetail;
import com.dashihui.afford.business.entity.EtyServerTime;
import com.dashihui.afford.business.entity.EtyShopDetail;
import com.dashihui.afford.common.base.AffRequestCallBack;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.exception.AffException;
import com.dashihui.afford.thirdapi.FastJSONHelper;
import com.dashihui.afford.ui.model.ModelOrder;
import com.dashihui.afford.util.json.UtilJSON;
import com.dashihui.afford.util.string.UtilString;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by NiuFC on 2015/11/1.
 */
public class BusinessBase {
    protected final static int SEND_MAP = 0;
    protected final static int SEND_LISTMAP = 1;
    protected final static int SEND_ENTITYLOCTION = 2;
    protected final static int SEND_ENTITYLOGIN = 3;
    protected final static int SEND_ETYSHOPDETAIL = 4;
    protected final static int SEND_ETYBUILDING = 5;
    protected final static int SEND_ORDER = 6;
    protected final static int SEND_ETYLIST = 7;
    protected final static int SEND_ETYSERVERDETAIL = 8;
    protected final static int SEND_ETYSERVERTIME = 9;


    protected final static String PAGESIZE = "12";
    protected final static String SERHOUSEPAGESIZE = "100";
    private HttpUtils httpUtils;
    private EtyRespone mBean;
    //协议版本号 兼容低版本协议
    private final static String APIVERSION = "1.3.3";


    public BusinessBase() {
        if (httpUtils == null) {
            httpUtils = new HttpUtils(AffConstans.PUBLIC.TIMEOUT);
        }
        //设置网络请求缓存时长
        httpUtils.configCurrentHttpCacheExpiry(AffConstans.PUBLIC.CACHETIME);
    }

    /**
     * 发送类型
     *
     * @param method      链接方法
     * @param params      参数
     * @param affCallBack
     * @param tag         协议唯一标识号
     * @param sendType    0：返回的类型设置，1：发</>
     * @throws AffException
     */
    public void send(String method, RequestParams params, final AffRequestCallBack affCallBack,
                     final int tag, final int sendType) throws AffException {
        try {
            if (params == null) {
                params = new RequestParams();
            }
            //商品列表
            params.addQueryStringParameter("APIVERSION", APIVERSION);
            send(method, params, new RequestCallBack<Object>() {
                @Override
                public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                    //返回结果字符串
                    String jsonResult = (String) objectResponseInfo.result;
                    EtySendToUI sendUI = new EtySendToUI();
                    sendUI.setTag(tag);
                    LogUtils.e("返回JSON字符串结果=========>" + jsonResult);
                    if (!UtilString.isEmpty(jsonResult)) {
                        mBean = FastJSONHelper.deserialize(jsonResult, EtyRespone.class);
                        LogUtils.e("返回状态=========>" + mBean.getSTATE());
                    } else {
                        sendUI.setInfo("服务器返回数据为null");
                        affCallBack.onFailure(sendUI);
                        return;
                    }
                    if (mBean == null) {
                        sendUI.setInfo("JSON解析异常");
                        affCallBack.onFailure(sendUI);
                        return;
                    }
                    //TODO 此处对返回的不同结果进行分流处理 0成功

                    switch (mBean.getSTATE()) {
                        case AffConstans.PUBLIC.RESULT_STATE_SUCCESS://成功
                            if (sendType == SEND_MAP) {//一般Map<String,Objects>类型
                                LogUtils.e("返回===Map======>" + mBean.getSTATE());
                                sendUI.setInfo(UtilJSON.parseKeyAndValueToMap(mBean.getOBJECT() +
                                        ""));
                            } else if (sendType == SEND_LISTMAP) {//列表类型List<HashMap<String,
                            // Objects>>
                                LogUtils.e("返回=====List====>" + mBean.getOBJECT().toString());
                                TypeReference typeListMap = new TypeReference<List<Map<String,
                                        Object>>>() {
                                };
                                sendUI.setInfo(FastJSONHelper.deserializeAny(mBean.getOBJECT() +
                                        "", typeListMap));
                            } else if (sendType == SEND_ENTITYLOCTION) {//特殊类型EntityLocation，
                                TypeReference responseType = new TypeReference<EntityLocation>() {
                                };
                                sendUI.setInfo(FastJSONHelper.deserializeAny(mBean.getOBJECT() +
                                        "", responseType));
                            } else if (sendType == SEND_ENTITYLOGIN) {//特殊类型EtyLogin，
                                TypeReference responseType = new TypeReference<EtyLogin>() {
                                };
                                sendUI.setInfo(FastJSONHelper.deserializeAny(mBean.getOBJECT() +
                                        "", responseType));
                            } else if (sendType == SEND_ETYSHOPDETAIL) {//特殊类型EtyShopDetail，
                                TypeReference responseType = new TypeReference<EtyShopDetail>() {
                                };
                                sendUI.setInfo(FastJSONHelper.deserializeAny(mBean.getOBJECT() +
                                        "", responseType));
                            } else if (sendType == SEND_ETYBUILDING) {//特殊类型EtyBuilding，
                                TypeReference responseType = new TypeReference<List<EtyBuilding>>
                                        () {
                                };
                                sendUI.setInfo(FastJSONHelper.deserializeAny(mBean.getOBJECT() +
                                        "", responseType));
                            } else if (sendType == SEND_ORDER) {//订单
//                                TypeReference responseType = new TypeReference<Map<String,Object>>() {};
                                TypeReference responseType = new TypeReference<ModelOrder>() {};//此处已修改
                                sendUI.setInfo(FastJSONHelper.deserializeAny(mBean.getOBJECT() +
                                        "", responseType));
                            } else if (sendType == SEND_ETYLIST) {//列表代分页
                                TypeReference responseType = new TypeReference<EtyList>() {
                                };
                                sendUI.setInfo(FastJSONHelper.deserializeAny(mBean.getOBJECT() +
                                        "", responseType));
                            } else if (sendType == SEND_ETYSERVERDETAIL) {//服务详情
                                TypeReference responseType = new TypeReference<EtyServerDetail>() {
                                };
                                sendUI.setInfo(FastJSONHelper.deserializeAny(mBean.getOBJECT() +
                                        "", responseType));
                            } else if (sendType == SEND_ETYSERVERTIME) {//服务时间
                                TypeReference responseType = new
                                        TypeReference<List<EtyServerTime>>() {
                                };
                                sendUI.setInfo(FastJSONHelper.deserializeAny(mBean.getOBJECT() +
                                        "", responseType));
                            }
                            affCallBack.onSuccess(sendUI);
                            break;
                        case AffConstans.PUBLIC.RESULT_STATE_ERROR://失败
                            sendUI.setInfo(mBean.getMSG());
                            affCallBack.onFailure(sendUI);
                            break;
                        case AffConstans.PUBLIC.RESULT_STATE_RIGEDIT_ERROR://手机账号已经存在，重复注册
                            sendUI.setInfo(mBean.getSTATE());
                            affCallBack.onFailure(sendUI);
                            break;
                        case AffConstans.PUBLIC.RESULT_STATE_MOBLERIGEDIT_ERROR://手机没有注册，需要退出重新登录
                            sendUI.setInfo("数据异常，请重新启动APP！");
                            affCallBack.onFailure(sendUI);
                            break;
                        default://其他特殊情况
                            sendUI.setInfo("特殊情况，程序猿要面壁了！");
                            affCallBack.onFailure(sendUI);
                            break;
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    LogUtils.e(s + "onFailure====BusinessBase=======>" + e.getMessage());
                    EtySendToUI sendUI = new EtySendToUI();
                    sendUI.setTag(tag);
                    sendUI.setInfo("服务协议异常，程序猿要面壁了！");
                    affCallBack.onFailure(sendUI);
                }
            });
        } catch (Exception e) {
            throw new AffException("网络请求数据有误");
        }
    }

    /**
     * xUtils工具类
     *
     * @param method
     * @param params
     * @param callBack
     * @param <T>
     * @return
     * @throws AffException
     */
    public <T> HttpHandler<T> send(String method, RequestParams params, RequestCallBack<T> callBack) throws AffException {

        if (params == null) {
            params = new RequestParams();
        }
        params.addQueryStringParameter("SIGNATURE", AffordApp.getInstance().getmAffordBean().getSIGNATURE());
        return httpUtils.send(HttpRequest.HttpMethod.POST, AffConstans.PUBLIC.ADDRESS + method, params, callBack);
    }


}
