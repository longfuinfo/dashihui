package com.dashihui.afford.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.NearbySearchInfo;
import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessCommon;
import com.dashihui.afford.business.entity.EntityLocation;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.ui.adapter.AdapterLocation;
import com.dashihui.afford.ui.widget.WgtAlertDialog;
import com.dashihui.afford.util.UtilCommon;
import com.dashihui.afford.util.toast.UtilToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtyLocation extends BaseActivity implements CloudListener, View.OnClickListener {

    // 定位相关
    private LocationClient mLocClient;

    @ViewInject(R.id.loccity)//定位的当前城市
    private TextView mLocCity;
    @ViewInject(R.id.community)//定位的最近小区
    private TextView mLocCommunity;
    @ViewInject(R.id.layout_err)//定位失败时隐藏的布局
    private LinearLayout mLinearLayout;
    @ViewInject(R.id.lyt_visibi_err)//定位失败时显示的布局
    private LinearLayout mLytVisibiErr;
    @ViewInject(R.id.listView)
    private ListView mListView;

    @ViewInject(R.id.ibtnBack)
    private ImageButton mIbtn;
    @ViewInject(R.id.title)
    private TextView mTitle;
    @ViewInject(R.id.txtLoction)
    private TextView mTxtLoction;
    @ViewInject(R.id.txtLocVisit)
    private TextView mTxtLocVisit;


    @ViewInject(R.id.change_community)
    private Button mBtnCommnity;

    @ViewInject(R.id.btnNoAuto)
    private Button mBtnNoAuto;//用户不允许定位时显示
    @ViewInject(R.id.btnContinue)
    private Button mBtnContinue;//用户所在地区范围内没有大实惠便利店时


    @ViewInject(R.id.community_address)
    private TextView mAddress;

    @ViewInject(R.id.loginTxt)
    private TextView mlogin;

    private String currentPosition;
    private AdapterLocation mAdapterLoction;
    private List<Map<String, Object>> mListMaps = new ArrayList<>();

    private BusinessCommon mBllCommon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_location);
        ViewUtils.inject(this);
        mBllCommon = new BusinessCommon(AtyLocation.this);
        mTitle.setText(R.string.location);
        LogUtils.e("AtyLocation===AtyLocation===333===>");

        mIbtn.setOnClickListener(this);
        mBtnCommnity.setOnClickListener(this);
        mlogin.setOnClickListener(this);

        if (AffordApp.isLogin()){
            mlogin.setVisibility(View.GONE);
        }else {
            mlogin.setVisibility(View.VISIBLE);
        }

        setLocationOption();

        initItemView();
    }

    public void initItemView(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListMaps != null && mListMaps.size() > position) {
                    //TODO 不可重复多次点击控制  成功后跳转
                    //显示加载框
                    showProDialog(AtyLocation.this);
                    mBllCommon.getlocation(mListMaps.get(position).get("uid") + "");
                } else {
                    LogUtils.e("onItemClick===Err======>");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        String titleName = getIntent().getStringExtra(AtyHome.INTENT_TAG_TITLENAME);
        if (titleName != null) {
            if (!"".equals(titleName)){
                mLocCommunity.setText(getIntent().getStringExtra(AtyHome.INTENT_TAG_TITLENAME));
                mAddress.setText(getIntent().getStringExtra(AtyHome.INTENT_TAG_ADDRESS));
                mLinearLayout.setVisibility(View.VISIBLE);
                mLytVisibiErr.setVisibility(View.GONE);//定位成功时隐藏定位失败页面
            }else {
                mLinearLayout.setVisibility(View.GONE);//定位失败隐藏定位小区的布局
                mLytVisibiErr.setVisibility(View.VISIBLE);//定位失败时显示定位失败页面
                mTxtLoction.setText(R.string.loction_noerr);
                mTxtLocVisit.setVisibility(View.VISIBLE);
                mBtnNoAuto.setVisibility(View.GONE);//用户不允许定位时显示
                mBtnContinue.setVisibility(View.VISIBLE);//用户所在地区范围内没有大实惠便利店时
            }
        } else {
            mLinearLayout.setVisibility(View.GONE);//定位失败隐藏定位小区的布局
            mLytVisibiErr.setVisibility(View.VISIBLE);//定位失败时显示定位失败页面
            mTxtLoction.setText(R.string.loction_noerr);
            mTxtLocVisit.setVisibility(View.VISIBLE);
            mBtnNoAuto.setVisibility(View.VISIBLE);//用户不允许定位时显示
            mBtnContinue.setVisibility(View.GONE);//用户所在地区范围内没有大实惠便利店时
            mLocCommunity.setText("");
            mAddress.setText("谢谢关注！你的关注是我们动力的基础！我们会尽快为您所在区域提供服务！");
        }
        super.onResume();
    }

    /**
     * 定位
     */
    public void setLocationOption() {
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(new MyLocationListener());
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//打开GPS
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式为高精模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认是gcj02
        option.setScanSpan(6000);//设置发起定位请求的间隔时间是6秒
        option.setIsNeedAddress(true);//返回定位结果包含地址信息
//        option.setNeedDeviceDirect(false);//返回定位信息包含手机机头的方向
        mLocClient.setLocOption(option);
        mLocClient.start();
        mLocClient.requestLocation();
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //map view 销毁后不再处理新接手的位置
            if (bdLocation == null) {
                return;
            }
            mLocCity.setText(bdLocation.getCity());
            currentPosition = bdLocation.getLongitude() + "," + bdLocation.getLatitude();
            LogUtils.e("云检索一定范围内的社区信息===currentPosition=================>" + currentPosition);
        }
    }

    /**
     * 云检索一定范围内的社区信息
     *
     * @param currentPosition
     */
    private void nearbySearch(String currentPosition) {
        LogUtils.e("nearbySearch=========0000=======>" + currentPosition);
        CloudManager.getInstance().init(AtyLocation.this);
        NearbySearchInfo _info = new NearbySearchInfo();
        _info.ak = AffConstans.PUBLIC.INFO_AK;
        _info.geoTableId = AffConstans.PUBLIC.INFO_geoTableId;
        _info.radius = AffConstans.PUBLIC.INFO_RADIUS;
        _info.location = currentPosition;//定位后传过来的坐标
//        _info.location = "113.778245,34.767289";
        //云检索一定范围内的社区信息
        CloudManager.getInstance().nearbySearch(_info);
    }

    @Override
    public void onGetSearchResult(CloudSearchResult cloudSearchResult, int i) {
        if (cloudSearchResult != null && cloudSearchResult.poiList != null
                && cloudSearchResult.poiList.size() > 0) {
            mListMaps.clear();
            for (CloudPoiInfo info : cloudSearchResult.poiList) {
                //搜索到范围内所有的便利店信息
                Map<String, Object> map = new HashMap<>();
                map.put("city", info.city);
                map.put("title", info.title);
                map.put("distance", info.distance);
                map.put("address", info.address);
                map.put("uid", info.uid);
//                map.put("shopName", info.extras.get("shopName"));

                mListMaps.add(map);
            }
            mAdapterLoction = new AdapterLocation(this, mListMaps);
            mListView.setAdapter(mAdapterLoction);
            mLinearLayout.setVisibility(View.VISIBLE);
            mLytVisibiErr.setVisibility(View.GONE);//定位成功时隐藏定位失败页面
        } else {
            mLinearLayout.setVisibility(View.GONE);//定位失败隐藏定位小区的布局
            mLytVisibiErr.setVisibility(View.VISIBLE);//定位失败时显示定位失败页面
            mTxtLoction.setText(R.string.loction_noerr);
            mTxtLocVisit.setVisibility(View.VISIBLE);
            mBtnNoAuto.setVisibility(View.VISIBLE);//用户不允许定位时显示
            mBtnContinue.setVisibility(View.GONE);//用户所在地区范围内没有大实惠便利店时
        }
        dissProDialog();
    }
    @OnClick(R.id.btnNoAuto)//用户不允许定位时显示
    public void onNoAutoClick(View view) {
        if (UtilCommon.isNetworkAvailable(this)) {
            Intent mIntent = new Intent(AtyLocation.this, AtyLocationList.class);
            startActivity(mIntent);
        } else {
            UtilToast.show(getApplicationContext(), R.string.home_examine, Toast.LENGTH_SHORT);
            mBaseUtilAty.startActivity(AtyNetWork.class);
        }
    }
    @OnClick(R.id.btnContinue)//用户所在地区范围内没有大实惠便利店时
    public void onContinueClick(View view) {
        if (UtilCommon.isNetworkAvailable(this)) {
            //显示加载框
            showProDialog(AtyLocation.this);
            //大实惠总部的UID
            mBllCommon.getlocation(1491112789 + "");
        } else {
            UtilToast.show(getApplicationContext(), R.string.home_examine, Toast.LENGTH_SHORT);
            mBaseUtilAty.startActivity(AtyNetWork.class);
        }
    }
    @Override
    protected void onDestroy() {
        //退出时销毁定位
        if (mLocClient != null) {
            mLocClient.stop();
            mLocClient = null;
        }
        CloudManager.getInstance().destroy();
        super.onDestroy();
    }

    @Override
    public void onGetDetailSearchResult(DetailSearchResult detailSearchResult, int i) {
        LogUtils.e("========onGetDetailSearchResult========>");
    }

    @Override
    public void onSuccess(EtySendToUI info) {
        //隐藏对话框
        dissProDialog();
        EntityLocation entityLocation = (EntityLocation) info.getInfo();
        LogUtils.e("onSuccess=========entityLocation=========>" + entityLocation);
        AffordApp.getInstance().setEntityLocation(entityLocation);
        //发送广播更新底部购物车显示
        sendShopChartBroadcast();
        //设置店铺ID
        Intent mIntent = new Intent(AtyLocation.this, AtyHome.class);
        // 重用堆栈里的已经启动activity
        mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(mIntent);
        finish();

    }

    @Override
    public void onFailure(EtySendToUI error) {
        LogUtils.e("onFailure====AtyAffordShop====>" + error);
        if (mDialog == null) {
            mDialog = new WgtAlertDialog();
        }
        mDialog.show(this,
                "确定",
                error.getInfo().toString(),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                        AtyLocation.this.finish();
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtnBack:
                onBackPressed();
                break;
            case R.id.loginTxt:
                Intent intent = new Intent(AtyLocation.this, AtyLogin.class);
                intent.putExtra(CommConstans.REGISTER.INTENT_NO_RIGEDIT, "0");//隐藏注册
                startActivity(intent);
                break;
            case R.id.change_community://定位页面点击控件:切换便利店
                if (UtilCommon.isNetworkAvailable(this)){
                    LogUtils.e("nearbySearch=======999========>" + UtilCommon.gPSIsOPen(AtyLocation.this));
                    if (currentPosition!=null){
                        LogUtils.e("nearbySearch=========dddd======>" + currentPosition);
                        //显示加载框
                        showProDialog(this);
                        nearbySearch(currentPosition);
                    }else {
                        LogUtils.e("nearbySearch=========pppp======>" + currentPosition);
                        mLinearLayout.setVisibility(View.GONE);//定位失败隐藏定位小区的布局
                        mLytVisibiErr.setVisibility(View.VISIBLE);//定位失败时显示定位失败页面
                        mTxtLoction.setText(R.string.loction_noerr);
                        mTxtLocVisit.setVisibility(View.VISIBLE);
                        mBtnNoAuto.setVisibility(View.VISIBLE);//用户不允许定位时显示
                        mBtnContinue.setVisibility(View.GONE);//用户所在地区范围内没有大实惠便利店时
                    }

                }else {
                    Toast.makeText(AtyLocation.this,R.string.home_examine,Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }
    }
}
