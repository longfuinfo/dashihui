package com.dashihui.afford.util.array;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.dashihui.afford.util.UtilCommon;
import com.dashihui.afford.util.toast.UtilToast;

/**
 * Created by NiuFC on 2015/11/14.
 */
public class UtilActivity {
    private Activity mActivity;
    public UtilActivity(Activity context){
        this.mActivity = context;
    }


    /**
     * 弹出消息
     *
     * @param p_ResID
     */
    public void ShowMsg(int p_ResID) {
        Toast.makeText(mActivity, p_ResID, Toast.LENGTH_SHORT).show();
    }
    /**
     * @Description: 弹出消息
     * @param @param pResStr
     * @return void
     * @author NiuFC
     * @date 2014年3月28日 下午4:17:44
     * @维护人:
     * @version V1.0
     */
    public void ShowMsg(String pResStr) {
        Toast.makeText(mActivity, pResStr, Toast.LENGTH_SHORT).show();
    }

    /**
     * 查询WiFi与Mobile的移动网络是否可用
     *
     * @param context
     * @return 可用返回true,否则返回false
     */
    public static boolean isNetworkAvailable(Context context) {
        return UtilCommon.isNetworkAvailable(context);
    }

    /**
     * 弹出消息
     * @param  intMsg
     */
    public void showToast(int intMsg) {
        UtilToast.show(mActivity, intMsg, 0);
    }

    /**
     * @Description: 弹出消息
     * @param @param pResStr
     * @return void
     * @author NiuFC
     * @date 2014年3月28日 下午4:17:44
     * @维护人:
     * @version V1.0
     */
    public void showToast(String strMsg) {
        UtilToast.show(mActivity, strMsg, 0);
    }


    /**
     * 默认不关闭 TODO(这里用一句话描述这个方法的作用)
     *
     * @Title: forward
     * @param target
     * @return 设定文件
     * @return boolean 返回类型
     * @author 牛丰产
     * @date 2015年1月9日 下午2:25:19
     * @维护人:
     * @version V1.0
     */
    public boolean startActivity(Class<?> target) {
        return startActivity(target, null, false);
    }

    /**
     * 不含参数的跳转
     *
     * @Title: forward
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param @param target
     * @param @param isFinish
     * @param @return 设定文件
     * @return boolean 返回类型
     * @author LvJW
     * @date 2014年11月14日 下午4:03:03
     * @维护人:
     * @version V1.0
     */
    public boolean startActivity(Class<?> target, boolean isFinish) {
        return startActivity(target, null, isFinish);
    }

    /**
     * 含参数的跳转
     * @param target 目标activity
     * @param paramter
     *            传递参数
     * @param isFinish 是否释放当前Activity
     *            true 关闭 false 不释放
     * @return
     */
    public boolean startActivity(Class<?> target, Bundle paramter, boolean isFinish) {
        boolean flag = true;
        try {
            Intent itent = new Intent(mActivity, target);
            if (paramter != null) {
                itent.putExtras(paramter);
            }
            // 重用堆栈里的已经启动activity
            itent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            mActivity.startActivity(itent);
            if (isFinish) {
                mActivity.finish();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            flag = false;
        }
        return flag;
    }
    /**
     * 弹出软键盘
     *
     * @param view
     */
    public void showWindowSoftInput(final View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                        InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }, 100);
    }

    /**
     * 关闭输入键盘
     * @Title: hideWindowSoftInput
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @return void    返回类型
     * @author 吕军伟
     * @date 2014年11月18日 上午9:50:33
     * @维护人:
     * @version V1.0
     */
    public void hideWindowSoftInput(){
        if (mActivity.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),
                    0); // 强制隐藏键盘
        }
    }
}
