package com.dashihui.afford.thirdapi.autochangeLine;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dashihui.afford.util.screen.UtilScreen;
import com.lidroid.xutils.util.LogUtils;

public class CustomListView extends RelativeLayout {
    private String TAG = CustomListView.class.getSimpleName();
    private MainSexangleAdapter myCustomAdapter;
    private static boolean addChildType;
    private int dividerHeight = 0;
    private int dividerWidth = 0;


    private final Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if(msg.getData().containsKey("getRefreshThreadHandler")) {
                    CustomListView.addChildType = false;
                    CustomListView.this.myCustomAdapter.notifyCustomListView(CustomListView.this);
                }
            } catch (Exception var3) {
                Log.w(CustomListView.this.TAG, var3);
            }

        }
    };

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onLayout(boolean arg0, int argLeft, int argTop, int argRight, int argBottom) {
        Log.i(this.TAG, "L:" + argLeft + " T:" + argTop + " R:" + argRight + " B:" + argBottom);
        int count = this.getChildCount();
        int row =0;
        int lengthX = 0;
        int lengthY = 0;
        for(int lp = 0; lp < count; ++lp) {
            View child = this.getChildAt(lp);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            if(lengthX == 0) {
                lengthX += width;
            } else {
                lengthX += width + this.getDividerWidth();
            }

            if(lp == 0 && lengthX <= argRight) {
                lengthY += height;
            }

            if((lengthX+width) > argRight) {
                lengthX = width;
                lengthY += this.getDividerHeight() + height;
                ++row;
                child.layout(width - width, lengthY - height, width, lengthY);
            } else {
                child.layout(lengthX - width, lengthY - height, lengthX, lengthY);
            }
        }
//        ViewGroup.LayoutParams var14 = CustomListView.this.getLayoutParams();
//        var14.height = (int)UtilScreen.dpToPx(lengthX);
//        LogUtils.e("FragmentServerDetailPager=====onLayout======2222====var14.height=====>" + var14.height);
//        CustomListView.this.setLayoutParams(var14);
        if(isAddChildType()) {
            (new Thread(new RefreshCustomThread())).start();
        }

    }


    private void setlistViewHeigh(CustomListView gridView) {
        CustomAdapter listAdapter = gridView.getMyCustomAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        LogUtils.e("onLayout======setlistViewHeigh=====3333=====>" +listAdapter.getCount());
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        LogUtils.e("=====onLayout======setlistViewHeigh======44444====>" +listAdapter.getCount());
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + (gridView.getHeight() * (listAdapter.getCount()));
        gridView.setLayoutParams(params);

        LogUtils.e("=====onLayout======setlistViewHeigh==55555==>" + listAdapter.getCount());

    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(width, height);

        for(int i = 0; i < this.getChildCount(); ++i) {
            View child = this.getChildAt(i);
            child.measure(0, 0);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }



    static final boolean isAddChildType() {
        return addChildType;
    }

    public static void setAddChildType(boolean addChildType) {
        addChildType = addChildType;
    }

    final int getDividerHeight() {
        return this.dividerHeight;
    }

    public void setDividerHeight(int dividerHeight) {
        this.dividerHeight = dividerHeight;
    }

    final int getDividerWidth() {
        return this.dividerWidth;
    }

    public void setDividerWidth(int dividerWidth) {
        this.dividerWidth = dividerWidth;
    }

    public void setAdapter(MainSexangleAdapter adapter) {
        this.myCustomAdapter = adapter;
        setAddChildType(true);
        adapter.notifyCustomListView(this);
    }

    public CustomAdapter getMyCustomAdapter() {
        return myCustomAdapter;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.myCustomAdapter.setOnItemClickListener(listener);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.myCustomAdapter.setOnItemLongClickListener(listener);
    }

    private final void sendMsgHanlder(Handler handler, Bundle data) {
        Message msg = handler.obtainMessage();
        msg.setData(data);
        handler.sendMessage(msg);
    }

    private final class RefreshCustomThread implements Runnable {
        private RefreshCustomThread() {
        }

        public void run() {
            Bundle bundle = new Bundle();

            try {
                Thread.sleep(5L);
            } catch (Exception var6) {
                ;
            } finally {
                bundle.putBoolean("getRefreshThreadHandler", true);
                CustomListView.this.sendMsgHanlder(CustomListView.this.handler, bundle);
            }

        }
    }
}
