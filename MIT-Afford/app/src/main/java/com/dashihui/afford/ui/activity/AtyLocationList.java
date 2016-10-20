package com.dashihui.afford.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessCommon;
import com.dashihui.afford.business.entity.EntityLocation;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.ui.adapter.AdapterLocation;
import com.dashihui.afford.ui.adapter.AdapterLocationList;
import com.dashihui.afford.ui.adapter.AdapterOrderState;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.toast.UtilToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AtyLocationList extends BaseActivity {

    @ViewInject(R.id.title)
    private TextView mTitle;
    @ViewInject(R.id.lyt_visibi_err)//定位失败时显示的布局
    private LinearLayout mLytVisibiErr;
    @ViewInject(R.id.listView)
    private ListView mListView;


    private BusinessCommon mBllCommon;
    private AdapterLocationList mAdapterLoction;
    private List<Map<String, Object>> mListMaps = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_locationlist);
        ViewUtils.inject(this);
        initView();
    }

    /**
     * 初始化数据
     */
    private void initView(){
        mAdapterLoction = new AdapterLocationList(this, mListMaps);
        mListView.setAdapter(mAdapterLoction);
        mBllCommon = new BusinessCommon(this);
        mTitle.setText(R.string.choiceConvenienceStore);

        showProDialog(this);
        mBllCommon.getlbs("");
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListMaps != null && mListMaps.size() > position) {
                    //TODO 不可重复多次点击控制  成功后跳转
                    //显示加载框
                    showProDialog(AtyLocationList.this);
                    mBllCommon.getlocation(mListMaps.get(position).get("GEOTABLE_ID") + "");
                } else {
                    LogUtils.e("onItemClick===Err======>");
                }
            }
        });
    }
    @Override
    public void onSuccess(EtySendToUI successEty) {
        if (successEty != null) {
            switch (successEty.getTag()) {
                case AffConstans.BUSINESS.TAG_COMMON_LBS://查询百度lbs数据（在没有定位情况下用）
                    dissProDialog();
                    mListMaps.clear();
                    List<Map<String,Object>> listmap = (List<Map<String,Object>>) successEty.getInfo();
                    if (!UtilList.isEmpty(listmap)) {
                        LogUtils.e("onSuccess====成功=======>" + listmap);
                        mListMaps.addAll(listmap);
                        mAdapterLoction.setList(mListMaps);
                        mLytVisibiErr.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                        mAdapterLoction.notifyDataSetChanged();

                    }else {
                        mLytVisibiErr.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                        LogUtils.e("onSuccess=======null====>" + listmap);
                    }
                    break;
                case AffConstans.BUSINESS.TAG_COMMON_LOCATION://
                    //隐藏对话框
                    dissProDialog();
                    EntityLocation entityLocation = (EntityLocation) successEty.getInfo();
                    LogUtils.e("onSuccess==================>" + entityLocation);
                    AffordApp.getInstance().setEntityLocation(entityLocation);
                    //发送广播更新底部购物车显示
                    sendShopChartBroadcast();
                    //设置店铺ID
                    Intent mIntent = new Intent(AtyLocationList.this, AtyHome.class);
                    // 重用堆栈里的已经启动activity
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(mIntent);
                    finish();
                    break;

                default:
                    break;
            }
        } else {
            LogUtils.e("onSuccess=======error====>" + successEty);
        }

    }

    @Override
    public void onFailure(EtySendToUI failureEty) {
        if (failureEty != null) {
            switch (failureEty.getTag()) {
                case AffConstans.BUSINESS.TAG_COMMON_LBS://查询百度lbs数据（在没有定位情况下用）
                    dissProDialog();
                    mLytVisibiErr.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                    break;
                case AffConstans.BUSINESS.TAG_COMMON_LOCATION://
                    //隐藏对话框
                    dissProDialog();
                    UtilToast.show(this,failureEty.getInfo()+"");
                    break;
                default:
                    break;
            }
        } else {
            LogUtils.e("onFailure=======error====>" + failureEty);
        }
    }

    @OnClick(R.id.ibtnBack)
    public void baceOnclick(View view){
        onBackPressed();
    }

}
