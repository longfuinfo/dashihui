 package com.dashihui.afford.thirdapi.autochangeLine;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;

public class CustomAdapter {
    private String TAG = CustomAdapter.class.getSimpleName();
    private View myView;
    private ViewGroup myViewGroup;
    private CustomListView myCustomListView;
    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;

    public CustomAdapter() {
    }

    public int getCount() {
        return 0;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0L;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    private final void getAllViewAddSexangle() {
        this.myCustomListView.removeAllViews();

        for(int i = 0; i < this.getCount(); ++i) {
            View viewItem = this.getView(i, this.myView, this.myViewGroup);
            this.myCustomListView.addView(viewItem, i);
        }

    }

    public void notifyDataSetChanged() {
        CustomListView.setAddChildType(true);
        this.notifyCustomListView(this.myCustomListView);
    }

    public void notifyCustomListView(CustomListView formateList) {
        this.myCustomListView = formateList;
        this.myCustomListView.removeAllViews();
        this.getAllViewAddSexangle();
        this.setOnItemClickListener(this.listener);
        this.setOnItemLongClickListener(this.longListener);
    }

    public void setOnItemClickListener(final OnItemClickListener listener) {
        this.listener = listener;

        for(int i = 0; i < this.myCustomListView.getChildCount(); i++) {
            View view = this.myCustomListView.getChildAt(i);
            final int position = i;
            view.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Log.i(CustomAdapter.this.TAG, "当前Item的值 : " + position);
                    listener.onItemClick((AdapterView)null, v, position, (long)CustomAdapter.this.getCount());
                }
            });
        }

    }

    public void setOnItemLongClickListener(final OnItemLongClickListener listener) {
        this.longListener = listener;

        for(int i = 0; i < this.myCustomListView.getChildCount(); i++) {
            View view = this.myCustomListView.getChildAt(i);
            final int position = i;
            view.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    listener.onItemLongClick((AdapterView)null, v, position, (long)CustomAdapter.this.getCount());
                    return true;
                }
            });
        }

    }
}
