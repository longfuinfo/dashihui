package com.dashihui.afford.ui.activity.my;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dashihui.afford.R;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.util.toast.UtilToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;


public class AtySetAbout extends Activity {

    private int flag = 0;
    private long[] mHints = new long[10];//初始全部为0
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_set_about);
        ViewUtils.inject(this);
    }

    @OnClick(R.id.logo)
    public void onImgLogoClick(View v) {
        flag++;
        if (flag == 10) {
            UtilToast.show(this, AffConstans.PUBLIC.ADDRESS, Toast.LENGTH_LONG);
            flag = 0;
//            //将mHints数组内的所有元素左移一个位置
//            System.arraycopy(mHints, 1, mHints, 0, mHints.length - 1);
//            //获得当前系统已经启动的时间
//            mHints[mHints.length - 1] = SystemClock.uptimeMillis();
//            if (SystemClock.uptimeMillis() - mHints[0] == 3000) {
//                UtilToast.show(this, AffConstans.PUBLIC.ADDRESS, Toast.LENGTH_LONG);
//            }
        }
    }

    //返回按钮
    @OnClick(R.id.my_address_back)
    public void onTvBtnSetClick(View v) {
        switch (v.getId()) {
            case R.id.my_address_back:
                onBackPressed();
                break;

            default:
                break;
        }
    }
}
