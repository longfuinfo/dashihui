package com.dashihui.afford.common.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.dashihui.afford.ui.widget.WdtProDialog;
import com.dashihui.afford.ui.widget.WgtAlertDialog;
import com.dashihui.afford.util.UtilCommon;

/**
 * Created by hhz on 2015/5/13.
 */
public abstract class BaseFragment extends Fragment implements AffRequestCallBack{


    protected WdtProDialog mProDialog =null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 显示过渡加载框
     * @param context
     */
    protected void showProDialog(Context context){
        if (mProDialog != null && mProDialog.isShowing()) {
            return;
        }
        if (mProDialog == null){
            mProDialog = WdtProDialog.createDialog(context);
        }
        mProDialog.show();
    }

    protected void dissProDialog(){
        if (mProDialog!=null && mProDialog.isShowing()){
            mProDialog.dismiss();
        }
    }
    /**
     *
     * TODO(这里用一句话描述这个方法的作用)
     *
     * @Title: isNetworkAvailable
     * @param context
     * @return 设定文件
     * @return boolean 返回类型
     * @author 牛丰产
     * @date 2015年1月9日 下午2:23:38
     * @维护人:
     * @version V1.0
     */
    protected static boolean isNetworkAvailable(Context context) {
        return UtilCommon.isNetworkAvailable(context);
    }



}
