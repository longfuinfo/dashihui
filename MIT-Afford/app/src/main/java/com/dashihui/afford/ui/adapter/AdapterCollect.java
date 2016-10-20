package com.dashihui.afford.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.sqlite.SqliteShoppingCart;
import com.dashihui.afford.thirdapi.greedsqlite.ShoppingCart;
import com.dashihui.afford.ui.activity.my.AtyMyCollect;
import com.dashihui.afford.ui.activity.shop.AtyAffordShop;
import com.dashihui.afford.ui.activity.shop.AtyAffordShopDetail;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.number.UtilNumber;
import com.dashihui.afford.util.string.UtilString;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;
import java.util.Map;

/**
 * Created by NiuFC on 2015/11/15.
 */
public class AdapterCollect extends AdapterBase<Map<String, Object>> {
    private boolean flag = false;
    private static int mSelectId = -1;
    private boolean[] isVisible;//CheckBoxs本身是否显示
    private boolean[] mIsChecked;//CheckBoxs状态isChecked的值；

    public AdapterCollect(Activity context, List<Map<String, Object>> _mList) {
        super(context, _mList);
        this.mContext = context;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            viewHolder = new ViewHolder(mContext, mList);
            convertView = inflater.inflate(R.layout.aty_my_collected_item, null);
            //依赖注入初始化
            ViewUtils.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
            viewHolder.refresh(position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.update(position, mList);

        }

        if (isVisible != null && isVisible.length > 0 && isVisible[position]) {
            viewHolder.mLeftCheckBox.setChecked(mIsChecked[position]);
            viewHolder.mLeftCheckBox.setVisibility(View.VISIBLE);
//            viewHolder.mShopCartImg.setVisibility(View.GONE);
        } else {
            viewHolder.mLeftCheckBox.setVisibility(View.GONE);
//            viewHolder.mShopCartImg.setVisibility(View.VISIBLE);
        }

        //mLeftCheckBox点击监听事件
        viewHolder.mLeftCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectId = position;
                mIsChecked[position] = !mIsChecked[position];
            }
        });

        return convertView;
    }


    /**
     * 刷新数据时同时刷新单选框的个数
     */
    public void initCheckData() {
        LogUtils.e("initCheckData=======mIsChecked=========>" + mIsChecked);
        isVisible = new boolean[getCount()];
        mIsChecked = new boolean[getCount()];
        for (int i = 0; i < mIsChecked.length; i++) {
            mIsChecked[i] = false;
        }
        setItemVisible(isFlag);
    }

    /**
     * 返回当前所有的Checkbox的状态
     *
     * @return
     */
    public boolean[] getmIsChecked() {
        LogUtils.e("getmIsChecked=======mIsChecked=========>" + mIsChecked);
        return mIsChecked;
    }

    /**
     * 返回当前点击的Checkbox的下标
     *
     * @return
     */
    public int getPositon() {
        return mSelectId;
    }

    /**
     * 定义显示或隐藏 Checkbox并刷新数据
     */
    private boolean isFlag;
    public void setItemVisible(Boolean isVisble) {
        LogUtils.e("setItemVisible=======mIsChecked=========>" + mIsChecked);
        for (int i = 0; i < isVisible.length; i++) {
            isFlag = isVisble;
            isVisible[i] = isFlag;

        }
        this.notifyDataSetChanged();
    }


    /**
     * @author p
     */
    static class ViewHolder {
        @ViewInject(R.id.left_img)//商品图片
        private ImageView mLeftGoodsImg;
        @ViewInject(R.id.tv_name)//商品名称
        private TextView mTextName;
        @ViewInject(R.id.tv_price)//商品优惠后价格
        private TextView mTextPrice;
        @ViewInject(R.id.tv_oldprice)//商品原价
        private TextView mTextOldPrice;
        @ViewInject(R.id.left_cBoxAgree)//选择按钮
        private CheckBox mLeftCheckBox;
        @ViewInject(R.id.shopcart_image)
        private ImageButton mShopCartImg;

        @ViewInject(R.id.itemLyt)//item点击
        private LinearLayout mItemLyt;

        private Activity mContent;
        private List<Map<String, Object>> mListMap;
        private int mPosition;

        public ViewHolder(Activity context, List<Map<String, Object>> _listMap) {
            mContent = context;
            mListMap = _listMap;
        }

        /**
         * 更新最新数据
         *
         * @param _listMap
         */
        public void refreshList(List<Map<String, Object>> _listMap) {
            mListMap = _listMap;
        }

        /**
         * 第一次刷新
         */
        public void refresh(final int position) {
            mPosition = position;
            if (!UtilList.isEmpty(mListMap)){
                Glide.with(mContent)
                        .load(AffConstans.PUBLIC.ADDRESS_IMAGE + mListMap.get(position).get("THUMB") + "")
                        .placeholder(R.drawable.default_list)
                        .error(R.drawable.default_list)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mLeftGoodsImg);
                mTextPrice.setText("￥" + mListMap.get(position).get("SELLPRICE"));
                mTextOldPrice.setText("￥" + mListMap.get(position).get("MARKETPRICE"));
                mTextOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//删除线并加清
                if((""+mListMap.get(position).get("ISSELF")).equals("1")){
                    //自营
                    Bitmap b = BitmapFactory.decodeResource(mContent.getResources(), R.drawable.title_myself);
                    ImageSpan imgSpan = new ImageSpan(mContent, b);
                    SpannableString spanString = new SpannableString("icon");
                    spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mTextName.setText(spanString);
                    mTextName.append(mListMap.get(position).get("NAME") + "");
                }else{
                    mTextName.setText(mListMap.get(position).get("NAME") + "");
                }
            }else{
                LogUtils.e("refresh===Error=====null=========>" + mListMap + "");
            }

        }

        /**
         * 更新
         */
        public void update(final int position, List<Map<String, Object>> _listMap) {
            refreshList(_listMap);
            refresh(position);
        }

        @OnClick(R.id.itemLyt)
        public void OnCheckClick(View v) {
            Intent intent = new Intent(mContent, AtyAffordShopDetail.class);
            intent.putExtra(AtyAffordShop.INTENT_SHOP_ID, mListMap.get(mPosition).get("ID") + "");
            intent.putExtra(AtyAffordShop.INTENT_SHOP_IMG, mListMap.get(mPosition).get("THUMB") + "");
            mContent.startActivity(intent);
        }


        @OnClick(R.id.shopcart_image)//购物车图片
        public void onImgBtnClick(View view) {
            if (!UtilList.isEmpty(mListMap)) {
                ShoppingCart _ShopCart = SqliteShoppingCart.getInstance(mContent).getShoppingCartByUid(mListMap.get(mPosition).get("ID") + "");
                if (_ShopCart != null) {
                    int _ShopNums = UtilNumber.IntegerValueOf(_ShopCart.getBuynum());
//                    LogUtils.e("购物车已有数据===============>" + _ShopNums);
                    addShopCart(mListMap.get(mPosition), ++_ShopNums);
                } else {
//                    LogUtils.e("购物车第一次添加数据===============>" + _ShopCart);
                    addShopCart(mListMap.get(mPosition), 1);
                }
                int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                view.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                ImageView ball = new ImageView(mContent);// buyImg是动画的图片
                //设置动画图片大小
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(40, 40);
                ball.setLayoutParams(layoutParams);
                if (!UtilString.isEmpty(mListMap.get(mPosition).get("THUMB") + "")) {
                    String uri = AffConstans.PUBLIC.ADDRESS_IMAGE + mListMap.get(mPosition).get("THUMB") + "";
                    Glide.with(mContent)
                            .load(uri)
                            .placeholder(R.drawable.cvs_btn_addshopcar)
                            .error(R.drawable.cvs_btn_addshopcar)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(ball);
                } else {
//                    LogUtils.e("uri===图片地址====error=======>" + mListMap.get(mPosition).get("THUMB")+"");
                }
                ((AtyMyCollect) mContent).setAnim(ball, startLocation);// 开始执行动画
            } else {
                Toast.makeText(mContent, "服务器繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
            }
        }

        private void addShopCart(Map<String, Object> objectMap, int num) {
            if (objectMap == null) {
//                LogUtils.e("null===============>" + objectMap);
                return;
            }
            ShoppingCart _shoppingCart = new ShoppingCart();

            String isSelf = objectMap.get("ISSELF") + "";
            //点击加入购物车将数据保存至本地数据库
            _shoppingCart.setID(objectMap.get("ID")  + "");
            //如果是自营
            if ("1".equals(isSelf)){
                _shoppingCart.setShopID(CommConstans.SELF_TYPE);
            }else {
                _shoppingCart.setShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
            }

            _shoppingCart.setName(objectMap.get("NAME") + "");
            _shoppingCart.setSpec(objectMap.get("SPEC") + "");
            _shoppingCart.setShortinfo(objectMap.get("SHORTINFO") + "");
            _shoppingCart.setMarketprice(objectMap.get("MARKETPRICE") + "");
            String img = objectMap.get("THUMB") + "";
            if (!UtilString.isEmpty(img)) {
                _shoppingCart.setImgsrc(img + "");
            } else {
                _shoppingCart.setImgsrc("");
            }
            //是否是自营商品：0：不是，1：是。
            _shoppingCart.setShoptype(objectMap.get("ISSELF") + "");
            _shoppingCart.setUrv(objectMap.get("URV") + "");
            _shoppingCart.setType(objectMap.get("TYPE") + "");
            _shoppingCart.setBuynum(num + "");
            _shoppingCart.setSellprice(objectMap.get("SELLPRICE") + "");
            _shoppingCart.setIschoose(true);
            //保存到本地数据库
            SqliteShoppingCart.getInstance(mContent).update(objectMap.get("ID") + "", _shoppingCart);
        }
    }
}
