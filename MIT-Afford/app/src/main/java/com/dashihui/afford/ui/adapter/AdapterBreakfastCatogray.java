package com.dashihui.afford.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dashihui.afford.R;
import com.dashihui.afford.ui.model.ModelCatogray;

import java.util.List;

/**
 * Created by bobge on 15/7/31.
 */
public class AdapterBreakfastCatogray extends BaseAdapter {

    private Context context;
    private List<ModelCatogray> list;

    private int selectPosition;
    public AdapterBreakfastCatogray(Context context, List<ModelCatogray> list) {
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        Viewholder viewholder;
        if (view==null){
            view= LayoutInflater.from(context).inflate(R.layout.aty_breakfast_left_listview,null);
            viewholder=new Viewholder();
            viewholder.tv_catogray= (TextView) view.findViewById(R.id.tv_catogray);
            viewholder.tv_count= (TextView) view.findViewById(R.id.tv_count);
            viewholder.itemRLyt= (RelativeLayout) view.findViewById(R.id.itemRLyt);
            view.setTag(viewholder);
        }else{
            viewholder= (Viewholder) view.getTag();
        }

        if (position==getSelectPosition()){
            viewholder.itemRLyt.setBackgroundResource(R.color.white);
        }else {
            viewholder.itemRLyt.setBackgroundResource(R.color.transparent);
        }
        viewholder.tv_catogray.setText(list.get(position).getKind());
        int count=0;
        for (int i = 0; i < list.get(position).getList().size(); i++) {
            count+=list.get(position).getList().get(i).getCount();
        }
        list.get(position).setCount(count);
        if (count<=0){
            viewholder.tv_count.setVisibility(View.INVISIBLE);
        }else{
            viewholder.tv_count.setVisibility(View.VISIBLE);
            viewholder.tv_count.getPaint().setAntiAlias(true);
            viewholder.tv_count.setText(count+"");
        }
        return view;
    }
    class Viewholder{
         TextView tv_catogray;
        TextView tv_count;
        RelativeLayout itemRLyt;
    }
}
