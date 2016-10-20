package com.dashihui.afford.common.base;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.dashihui.afford.ui.widget.WdtProDialog;

/**
 * Created by NiuFC on 2016/3/28.
 */
public abstract class BaseAtyFragment extends FragmentActivity implements AffRequestCallBack {
    protected WdtProDialog mProDialog =null;
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
}
