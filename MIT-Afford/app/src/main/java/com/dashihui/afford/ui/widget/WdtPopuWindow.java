package com.dashihui.afford.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dashihui.afford.R;
import com.dashihui.afford.ui.activity.AtyHome;
import com.dashihui.afford.ui.activity.shop.AtyAffordShop;


/**
 * Created by Codo on 2015/11/15.
 */
public class WdtPopuWindow extends PopupWindow {
    private View mMenuView;

    //生活百货， 粮油调料， 酒水饮料， 蔬菜水果
    private TextView mTextViewLife, mTextViewOils, mTextViewDrink, mTextViewVegetable, mTextViewCancelBtn;
    private Activity mActivity;

    public WdtPopuWindow(final Activity context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.aty_affordshop_popuwindow, null);
        mActivity = context;
        initPopuView();//初始化popuwindow界面

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);

        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);

        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);

        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimBottom);

        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(88000000);

        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.topdialog_linlayout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    //初始化popuWindow
    private void initPopuView() {
        mTextViewLife = (TextView) mMenuView.findViewById(R.id.topdialog_lifestyle);
        mTextViewOils = (TextView) mMenuView.findViewById(R.id.topdialog_oils);
        mTextViewDrink = (TextView) mMenuView.findViewById(R.id.topdialog_drinks);
        mTextViewVegetable = (TextView) mMenuView.findViewById(R.id.topdialog_vegetable);
        mTextViewCancelBtn = (TextView) mMenuView.findViewById(R.id.btn_popu_cancel);

        //添加菜单子项的监听事件
        mTextViewLife.setOnClickListener(onItemClick);
        mTextViewOils.setOnClickListener(onItemClick);
        mTextViewDrink.setOnClickListener(onItemClick);
        mTextViewVegetable.setOnClickListener(onItemClick);
        mTextViewCancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    //popuWindow子项的监听事件
    private OnClickListener onItemClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra(AtyHome.INTENT_TAG_TYPE, v.getTag() + "");//tag是xml布局文件的tag
            intent.setClass(mActivity, AtyAffordShop.class);
            mActivity.startActivity(intent);
            dismiss();
        }
    };
}
