package com.dashihui.afford.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.dashihui.afford.R;

/**
 * Created by apple on 15/5/20.
 */
public class WgtAlertDialog {
    private Dialog dialog = null;
    private View view = null;

    /**
     * 不带标题的弹出框显示
     * @param context
     * @param leftBtnText 左边按钮名称 为null时不显示
     * @param rightBtnText 右边按钮名称 为null时不显示
     * @param msg  中间内容
     * @param left 左边按钮事件
     * @param right 右边按钮事件
     */
    public void show (Context context, String leftBtnText, String rightBtnText,
                      String msg, View.OnClickListener left, View.OnClickListener right) {
        this.show(context, leftBtnText, rightBtnText, msg, left, right, false, false,0,null);
    }

    /**
     * 带标题的弹出框
     * @param context
     * @param leftBtnText
     * @param rightBtnText
     * @param msg
     * @param left
     * @param right
     * @param titleDrawable
     * @param titleText
     */
    public void show (Context context, String leftBtnText, String rightBtnText,
                      String msg, View.OnClickListener left, View.OnClickListener right,int titleDrawable,String titleText ) {
        this.show(context, leftBtnText, rightBtnText, msg, left, right, true, true,titleDrawable,titleText);
    }

    /**
     *
     * @param context
     * @param rightBtnText
     * @param msg
     * @param right
     */
    public void show(Context context, String rightBtnText,
                     String msg, View.OnClickListener right) {
        show(context, null, rightBtnText, msg, null, right, false, false, 0, null);
    }

    /**
     *
     * @param context
     * @param rightBtnText
     * @param msg
     * @param right
     * @param cancelOnTouchOutside
     * @param cancelable
     */
    public void show(Context context, String rightBtnText,
                     String msg, View.OnClickListener right,
                     boolean cancelOnTouchOutside, boolean cancelable) {
        show(context, null, rightBtnText, msg, null, right, cancelOnTouchOutside, cancelable, 0, null);
    }
    /**
     *
     * @param context
     * @param leftBtnText 左边按钮名称 为null时不显示
     * @param rightBtnText 右边按钮名称 为null时不显示
     * @param msg  中间内容
     * @param left 左边按钮事件
     * @param right 右边按钮事件
     * @param cancelOnTouchOutside
     * @param cancelable 为false 按返回键无反应
     * @param titleDrawable
     * @param titleText
     */
    public void show(Context context, String leftBtnText, String rightBtnText,
                     String msg, View.OnClickListener left, View.OnClickListener right,
                     boolean cancelOnTouchOutside, boolean cancelable,int titleDrawable,String titleText) {
        dialog = new Dialog(context, R.style.dialog);
        view = LayoutInflater.from(context).inflate(
                R.layout.dialog_alert, null);
        Button leftBtn = (Button) view.findViewById(R.id.dialog_left_btn);
        TextView titleTxt = (TextView) view.findViewById(R.id.dialog_title);
        View lineView1 = view.findViewById(R.id.dialog_line1);
        View lineView2 = view.findViewById(R.id.dialog_line2);

        if (titleDrawable == 0){
            titleTxt.setVisibility(View.GONE);
            lineView1.setVisibility(View.GONE);
            lineView2.setVisibility(View.VISIBLE);
        }else {
            titleTxt.setVisibility(View.VISIBLE);
            lineView1.setVisibility(View.VISIBLE);
            lineView2.setVisibility(View.GONE);
            titleTxt.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(titleDrawable),null,null,null);
            titleTxt.setText(titleText);
        }

        if (!TextUtils.isEmpty(leftBtnText)) {
            leftBtn.setText(leftBtnText);
            leftBtn.setVisibility(View.VISIBLE);
        }else{
            leftBtn.setVisibility(View.GONE);
        }
        Button rightBtn = (Button) view.findViewById(R.id.dialog_right_btn);

        if (!TextUtils.isEmpty(rightBtnText)) {
            rightBtn.setText(rightBtnText);
            rightBtn.setVisibility(View.VISIBLE);
        }else{
            rightBtn.setVisibility(View.GONE);
        }
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(cancelOnTouchOutside);
        dialog.setCancelable(cancelable);
        dialog.setOwnerActivity((Activity) context);
        dialog.setOnCancelListener(null);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.5f;
        lp.width = context.getResources().getDimensionPixelSize(R.dimen.alert_dialog_width);
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        TextView message = (TextView) view.findViewById(R.id.dialog_message);
        if (left != null) {
            leftBtn.setOnClickListener(left);
        }
        if (right != null) {
            rightBtn.setOnClickListener(right);
        }
        if (msg != null) {
            message.setText(msg);
            message.setVisibility(View.VISIBLE);
        } else {
            message.setVisibility(View.GONE);
        }
        if (!((Activity) context).isFinishing()) {
            dialog.show();
        }
    }



    public boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        }
        return false;
    }


    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
