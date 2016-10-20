package com.dashihui.afford.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dashihui.afford.R;
import com.dashihui.afford.ui.activity.AtyBreakfast;
import com.dashihui.afford.ui.model.ModelGoods;
import java.util.List;

/**
 * Created by bobge on 15/7/31.
 */
public class AdapterBreakfastGoods extends BaseAdapter{

    private List<ModelGoods> list;
    private Context context;
    private AdapterBreakfastCatogray catograyAdapter;
    public AdapterBreakfastGoods(Context context, List<ModelGoods> list1) {
        this.context=context;
        this.list=list1;
    }

    public AdapterBreakfastGoods(Context context, List<ModelGoods> list2, AdapterBreakfastCatogray catograyAdapter) {
        this.context=context;
        this.list=list2;
        this.catograyAdapter=catograyAdapter;
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view=convertView;
        final Viewholder viewholder;
        if (view==null){
            view= LayoutInflater.from(context).inflate(R.layout.aty_breakfast_right_listview,null);
            viewholder=new Viewholder();
            viewholder.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewholder.tv_desc= (TextView) view.findViewById(R.id.tv_desc);
            viewholder.tv_price= (TextView) view.findViewById(R.id.tv_price);
            viewholder.iv_add= (ImageView) view.findViewById(R.id.iv_add);
            viewholder.iv_remove= (ImageView) view.findViewById(R.id.iv_remove);
            viewholder.et_acount= (EditText) view.findViewById(R.id.et_count);
            view.setTag(viewholder);
        }else
            viewholder= (Viewholder) view.getTag();
        viewholder.tv_name.setText(list.get(position).getGood_name());
        viewholder.tv_desc.setText(list.get(position).getDescrible());
        viewholder.tv_price.setText(list.get(position).getPrice());
        viewholder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = list.get(position).getCount();
                count++;
                list.get(position).setCount(count);
                viewholder.et_acount.setVisibility(View.VISIBLE);
                viewholder.iv_remove.setVisibility(View.VISIBLE);
                viewholder.et_acount.setText(list.get(position).getCount() + "");
                catograyAdapter.notifyDataSetChanged();

                int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                ImageView ball = new ImageView(context);// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
                ball.setImageResource(R.mipmap.ic_launcher);// 设置buyImg的图片
                ((AtyBreakfast)context).setAnim(ball, startLocation);// 开始执行动画
            }
        });
        Log.i("test",list.get(position).getCount()+"==");
        if (list.get(position).getCount()<=0){
            viewholder.et_acount.setVisibility(View.INVISIBLE);
            viewholder.iv_remove.setVisibility(View.INVISIBLE);
        }else{
            viewholder.et_acount.setVisibility(View.VISIBLE);
            viewholder.iv_remove.setVisibility(View.VISIBLE);
        }
        return view;
    }
    class Viewholder{
        TextView tv_name;
        TextView tv_desc;
        TextView tv_price;
        ImageView iv_add,iv_remove;
        EditText et_acount;
    }

}
