package com.dashihui.afford.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.lidroid.xutils.util.LogUtils;

/**
 * Created by Administrator on 2015/12/19.
 */
public class WgtGridViewServer extends GridView {


    public WgtGridViewServer(Context context) {
        super(context);
    }

    public WgtGridViewServer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WgtGridViewServer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //控制gridview的item不上下滑动，一次全部显示
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);


            // 不需要调用父类的方法
            // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            //处理最后一个子View的情况

            super.onMeasure(widthMeasureSpec, expandSpec);
        }


    }







