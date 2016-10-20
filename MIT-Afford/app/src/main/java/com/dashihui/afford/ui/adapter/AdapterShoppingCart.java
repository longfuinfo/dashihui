package com.dashihui.afford.ui.adapter;

import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.sqlite.SqliteShoppingCart;
import com.dashihui.afford.thirdapi.greedsqlite.ShoppingCart;
import com.dashihui.afford.ui.activity.AtyShoppingCart;
import com.dashihui.afford.ui.activity.shop.AtyAffordShop;
import com.dashihui.afford.ui.activity.shop.AtyAffordShopDetail;
import com.dashihui.afford.ui.model.TreeElement;
import com.dashihui.afford.ui.widget.WgtAlertDialog;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.number.UtilNumber;
import com.dashihui.afford.util.preferences.UtilPreferences;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NiuFC on 2015/11/15.
 */
public class AdapterShoppingCart extends AdapterBase<TreeElement> {

    public final static String PRICE_XALL = "小计:￥";
    public final static String PRICE_HALL = "合计:￥";
    private boolean flag = false;


    private CheckBox mCheckAllBox, mDeleteAllAll;
    private TextView mTxtViewAllPrice;
    private TextView mTxtViewAllNum;


    @ViewInject(R.id.itemBoxAgree)
    private CheckBox mItemCheckBox;//是否选中
    private AtyShoppingCart mAtyShopCart;

    private ArrayList<TreeElement> mArrayList;

    public AdapterShoppingCart(AtyShoppingCart atyShopCart, ArrayList<TreeElement> _List, CheckBox _checkBox, CheckBox _deletCheckbox, TextView _price, TextView _num) {
        super(atyShopCart, _List);
        mAtyShopCart=atyShopCart;
        this.mCheckAllBox = _checkBox;
        this.mDeleteAllAll = _deletCheckbox;
        this.mTxtViewAllPrice = _price;
        this.mTxtViewAllNum = _num;
        this.mArrayList = _List;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            LogUtils.e("onReduceClick=========初始化===refresh==>");
            convertView = inflater.inflate(R.layout.aty_shoppingcart_item, null);
            viewHolder = new ViewHolder(mAtyShopCart, mArrayList, mCheckAllBox, mDeleteAllAll, mTxtViewAllPrice, mTxtViewAllNum, position);
            //依赖注入初始化
            ViewUtils.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
            viewHolder.refresh(position);
        } else {
            LogUtils.e("onReduceClick=========初始化===update==>");
            viewHolder = (ViewHolder) convertView.getTag();
            LogUtils.e("onReduceClick=========初始化==666666=update==>"+mArrayList.size());
            viewHolder.update(position, mArrayList);
        }
        return convertView;
    }


    /**
     * 是否改变全选状态
     */
    public void refreshChanged() {
        List<ShoppingCart> shopCartList = SqliteShoppingCart.getInstance(mContext).getListByShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
        if (!UtilList.isEmpty(shopCartList)) {
            boolean isAllCheck = true;
            for (int k = 0; k < shopCartList.size(); k++) {
                if (!shopCartList.get(k).getIschoose()) {
                    isAllCheck = false;
                }
            }
            mCheckAllBox.setChecked(isAllCheck);
            mDeleteAllAll.setChecked(isAllCheck);
        }
    }

    /**
     * @author p
     */
    class ViewHolder {
        @ViewInject(R.id.itemBoxAgree)
        private CheckBox mItemCheckBox;//是否选中
        @ViewInject(R.id.apps_item_img)
        private ImageView mImageView;//图片
        @ViewInject(R.id.apps_item_app_name_tv)
        private TextView mTextViewName;//商品名
        @ViewInject(R.id.apps_item_download_count_tv)
        private TextView mTextViewPrice;//商品现价
        @ViewInject(R.id.apps_item_size_tv)
        private TextView mTextViewOldPrice;//商品原价
        @ViewInject(R.id.trade_edit_num_input)
        private TextView mTextViewInputNums;//输入的数字

        @ViewInject(R.id.apps_item_desc_tv)
        private TextView mTextViewTotalPrice;//小计

        @ViewInject(R.id.trade_btn_num_reduce)
        private ImageButton mButReduce;//减少按钮
        @ViewInject(R.id.trade_btn_num_raise)
        private ImageButton mButRaise;//增加按钮
        @ViewInject(R.id.lyt_edit)
        private LinearLayout mLytEdit;
        @ViewInject(R.id.deleteAllBoxAgree)
        private CheckBox mDeleteAllAll;//全选


        @ViewInject(R.id.lyoutSelf)
        private LinearLayout mlyoutSelf;


        @ViewInject(R.id.checkboxSelf)
        private CheckBox mCheckboxSelf;//自营或非自营

        private ArrayList<TreeElement> mListTreeElement;

        private ArrayList<TreeElement> mListTree;

        private ArrayList<TreeElement> mListselfShop;//自营列表
        private ArrayList<TreeElement> mListShop;//非自营列表

        private int mSelfShopNum=-1;//自营列表个数
        private int mShopNum=-1;//非自营列表个数
        private boolean isSelfTop = false;//是否自营


        private CheckBox m_CheckAllBox;//全选
        private TextView m_TxtViewAllPrice;//选择的价格
        private TextView m_TxtViewAllNum;//选择的数量
        private int mPosition;
        private AtyShoppingCart mAtyShopCart;

        public ViewHolder(AtyShoppingCart atyShopCart, ArrayList<TreeElement> listObject, CheckBox _checkBox, CheckBox _deleteCheckBox, TextView _price, TextView _num, int position) {
            this.mListTree = listObject;
            mAtyShopCart = atyShopCart;
            this.m_CheckAllBox = _checkBox;
            this.mDeleteAllAll = _deleteCheckBox;
            this.m_TxtViewAllPrice = _price;
            this.m_TxtViewAllNum = _num;
            mListselfShop= new ArrayList<>();
            mListShop= new ArrayList<>();
            mListTreeElement = new ArrayList<>();
            mPosition = position;
            //初始化数据
            initData();
        }

        /**
         * 更新最新数据
         *
         * @param _listMap
         */
        public void refreshList(ArrayList<TreeElement> _listMap) {
            mListTree = _listMap;
            //初始化数据
            initData();
        }

        /**
         * 第一次刷新
         */
        public void refresh(final int position) {
            mPosition = position;
            if (!UtilList.isEmpty(mListTreeElement) && position < mListTreeElement.size() && mListTreeElement.get(position)!=null) {
              ShoppingCart shoppingCart = mListTreeElement.get(position).getmShopingCart();
                mCheckboxSelf.setVisibility(View.GONE);
                mlyoutSelf.setVisibility(View.GONE);
                //是否是自营商品：0：不是，1：是。
                if ("0".equals(shoppingCart.getShoptype())){
                    //非大实惠自营，便利店
                    if (mListTreeElement.get(position).isVisible()){
                        mlyoutSelf.setVisibility(View.VISIBLE);
                        mCheckboxSelf.setVisibility(View.VISIBLE);
                        mCheckboxSelf.setText(AffordApp.getInstance().getEntityLocation().getSTORE().getTITLE());
                        LogUtils.e("uri======mCheckboxSelf========>" + shoppingCart);

                    }else {
                        mCheckboxSelf.setVisibility(View.GONE);
                        mlyoutSelf.setVisibility(View.GONE);
                    }
                }else if("1".equals(shoppingCart.getShoptype())) {
                    //大实惠直营
                    if (mListTreeElement.get(position).isVisible()){
                        mlyoutSelf.setVisibility(View.VISIBLE);
                        mCheckboxSelf.setVisibility(View.VISIBLE);
                        mCheckboxSelf.setText("大实惠直营");
                    }else {
                        mCheckboxSelf.setVisibility(View.GONE);
                        mlyoutSelf.setVisibility(View.GONE);
                    }

                }
                mItemCheckBox.setChecked(shoppingCart.getIschoose());

                //自营或非自营全选状态
                mCheckboxSelf.setChecked(mListTreeElement.get(position).isChoosed());
                String uri = AffConstans.PUBLIC.ADDRESS_IMAGE + shoppingCart.getImgsrc() + "";
                LogUtils.e("uri===图片地址===========>" + uri);
                Glide.with(mAtyShopCart)
                        .load(uri)
                        .placeholder(R.drawable.default_list)
                        .error(R.drawable.default_list)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mImageView);

                mTextViewName.setText(shoppingCart.getName() + "");
                LogUtils.e("优惠后价格======mTextViewName========>" + shoppingCart.getName() + "");
                LogUtils.e("优惠后价格==============>" + shoppingCart.getSellprice());
                mTextViewPrice.setText(shoppingCart.getSellprice() + "");
                mTextViewOldPrice.setText("￥" + shoppingCart.getMarketprice() + "");
                mTextViewOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
                mTextViewInputNums.setText(UtilNumber.IntegerValueOf(shoppingCart.getBuynum() + "") + "");

                double num = UtilNumber.DoubleValueOf(shoppingCart.getBuynum() + "");
                double price = UtilNumber.DoubleValueOf(shoppingCart.getSellprice() + "");
                double allPrice = UtilNumber.DoubleValueOf(num * price + "");
                mTextViewTotalPrice.setText(PRICE_XALL + allPrice + "");

            }else{
                LogUtils.e("refresh===Error=====null=========>" + mListTreeElement + "");
            }


        }


        @OnClick(R.id.checkboxSelf)//自营或非自营全选按钮
        public void OnCheckSelfClick(View v) {
            boolean isChecked = mCheckboxSelf.isChecked();
            LogUtils.e("uri===自营或非自营全选状态===========>" + isChecked);
            //被点击的是是否是自营商品：0：不是，1：是。
            ShoppingCart shoppingCart1= mListTreeElement.get(mPosition).getmShopingCart();
            double num = 0;
            double price = 0;


            //获取本地存储总数量和价格
            double preFerNumAll = UtilPreferences.getInt(mAtyShopCart, AtyShoppingCart.SHOPCART_NUM);
            double preFerPriceAll = UtilNumber.DoubleValueOf(UtilPreferences.getString(mAtyShopCart, AtyShoppingCart.SHOPCART_PRICE));
            double finalPrice = 0;
            double finalNum = 0;
            if (isChecked) {
                /******************start***************************/
                //刷新数据
                for (int i=0;i<mListTreeElement.size();i++){
                    ShoppingCart shoppingCart = mListTreeElement.get(i).getmShopingCart();
                    //是否是自营商品：0：不是，1：是。
                    if ("0".equals(shoppingCart1.getShoptype()) && "0".equals(shoppingCart.getShoptype()) ){
                        //如果非自营商品本身没有被选中
                        if (!shoppingCart.getIschoose()){
                            num += UtilNumber.DoubleValueOf(mListTreeElement.get(i).getmShopingCart().getBuynum());
                            price += UtilNumber.DoubleValueOf(mListTreeElement.get(i).getmShopingCart().getBuynum()) * UtilNumber.DoubleValueOf(mListTreeElement.get(i).getmShopingCart().getSellprice());
                        }
                        //改变选择按钮的属性
                        mListTreeElement.get(i).getmShopingCart().setIschoose(isChecked);
                    }else if("1".equals(shoppingCart1.getShoptype()) && "1".equals(shoppingCart.getShoptype()) ){
                        //如果自营商品本身没有被选中
                        if (!shoppingCart.getIschoose()){
                            num += UtilNumber.DoubleValueOf(mListTreeElement.get(i).getmShopingCart().getBuynum());
                            price += UtilNumber.DoubleValueOf(mListTreeElement.get(i).getmShopingCart().getBuynum()) * UtilNumber.DoubleValueOf(mListTreeElement.get(i).getmShopingCart().getSellprice());
                        }
                        //改变选择按钮的属性
                        mListTreeElement.get(i).getmShopingCart().setIschoose(isChecked);
                    }
                    //修改数据库中所有选择状态
                    SqliteShoppingCart.getInstance(mAtyShopCart).update(mListTreeElement.get(i).getmShopingCart().getID(), mListTreeElement.get(i).getmShopingCart());
                }
                /******************end***************************/
                //底部总的
                finalNum = UtilNumber.DoubleValueOf((preFerNumAll + num) + "");
                finalPrice = UtilNumber.DoubleValueOf((preFerPriceAll + price) + "");
                mTextViewTotalPrice.setText(PRICE_XALL + price + "");
                m_TxtViewAllNum.setText("去结算(" + UtilNumber.IntegerValueOf(finalNum + "") + ")");//改变底部结算数量
                m_TxtViewAllPrice.setText(PRICE_HALL + finalPrice + "");//改变底部合计

                UtilPreferences.putString(mAtyShopCart, AtyShoppingCart.SHOPCART_CHOOSE, isChecked + "");

                //判断是否改变全选的状态
                refreshChanged();
            }else {
                if (preFerNumAll != 0 || preFerPriceAll != 0) {
                    /******************start***************************/
                    //刷新数据
                    for (int i=0;i<mListTreeElement.size();i++){
                        ShoppingCart _cart = mListTreeElement.get(i).getmShopingCart();
                        //是否是自营商品：0：不是，1：是。
                        if ("0".equals(shoppingCart1.getShoptype()) && "0".equals(_cart.getShoptype()) ){
                            num += UtilNumber.DoubleValueOf(mListTreeElement.get(i).getmShopingCart().getBuynum());
                            price += UtilNumber.DoubleValueOf(mListTreeElement.get(i).getmShopingCart().getBuynum()) * UtilNumber.DoubleValueOf(mListTreeElement.get(i).getmShopingCart().getSellprice());
                            //改变选择按钮的属性
                            mListTreeElement.get(i).getmShopingCart().setIschoose(isChecked);
                        }else if("1".equals(shoppingCart1.getShoptype())&& "1".equals(_cart.getShoptype()) ){
                            num += UtilNumber.DoubleValueOf(mListTreeElement.get(i).getmShopingCart().getBuynum());
                            price += UtilNumber.DoubleValueOf(mListTreeElement.get(i).getmShopingCart().getBuynum()) * UtilNumber.DoubleValueOf(mListTreeElement.get(i).getmShopingCart().getSellprice());

                            //改变选择按钮的属性
                            mListTreeElement.get(i).getmShopingCart().setIschoose(isChecked);
                        }
                        //修改数据库中所有选择状态
                        SqliteShoppingCart.getInstance(mAtyShopCart).update(mListTreeElement.get(i).getmShopingCart().getID(), mListTreeElement.get(i).getmShopingCart());
                    }
                    /******************end***************************/
                    finalNum = UtilNumber.IntegerValueOf((preFerNumAll - num) + "");
                    finalPrice = UtilNumber.DoubleValueOf((preFerPriceAll - price) + "");
                    m_TxtViewAllNum.setText("去结算(" + UtilNumber.IntegerValueOf(finalNum + "") + ")");//改变底部结算数量
                    m_TxtViewAllPrice.setText(PRICE_HALL + finalPrice + "");//改变底部合计
                }
                //全选的状态保存
                UtilPreferences.putString(mAtyShopCart, AtyShoppingCart.SHOPCART_CHOOSE, isChecked + "");
                m_CheckAllBox.setChecked(false);//改变底部全选状态
                mDeleteAllAll.setChecked(false);//改变底部全选状态
            }
            UtilPreferences.putString(mAtyShopCart, AtyShoppingCart.SHOPCART_PRICE, finalPrice + "");
            UtilPreferences.putInt(mAtyShopCart, AtyShoppingCart.SHOPCART_NUM, UtilNumber.IntegerValueOf(finalNum + ""));
            notifyDataSetChanged();
        }

        @OnClick(R.id.itemBoxAgree)
        public void OnCheckClick(View v) {//单选按钮
            boolean isChecked = mItemCheckBox.isChecked();
            LogUtils.e("OnCheckClick====点击后触发==========>" + isChecked);
            if (!UtilList.isEmpty(mListTreeElement) && mListTreeElement.get(mPosition).getmShopingCart()!=null) {

                //被点击的是是否是自营商品：0：不是，1：是。
                ShoppingCart shoppingCart2= mListTreeElement.get(mPosition).getmShopingCart();
                //改变选择按钮的属性
                mListTreeElement.get(mPosition).getmShopingCart().setIschoose(isChecked);

                LogUtils.e("OnCheckClick====点击后触发==修改数据库========>" + isChecked);
                double num;
                double price;
                double allPrice;
                //获取本地存储总数量和价格
                double preFerNumAll = UtilPreferences.getInt(mAtyShopCart, AtyShoppingCart.SHOPCART_NUM);
                double preFerPriceAll = UtilNumber.DoubleValueOf(UtilPreferences.getString(mAtyShopCart, AtyShoppingCart.SHOPCART_PRICE));

                double finalPrice = 0;
                double finalNum = 0;
                if (isChecked) {
                    if ("0".equals(mTextViewInputNums.getText().toString())) {
                        mListTreeElement.get(mPosition).getmShopingCart().setBuynum("1");
                        mTextViewInputNums.setText("1");
                    }
                    num = UtilNumber.DoubleValueOf(mListTreeElement.get(mPosition).getmShopingCart().getBuynum() + "");
                    price = UtilNumber.DoubleValueOf(mListTreeElement.get(mPosition).getmShopingCart().getSellprice() + "");
                    allPrice = UtilNumber.DoubleValueOf(num * price + "");
                    //底部总的
                    finalNum = UtilNumber.DoubleValueOf((preFerNumAll + num) + "");
                    finalPrice = UtilNumber.DoubleValueOf((preFerPriceAll + allPrice) + "");
                    mTextViewTotalPrice.setText(PRICE_XALL + allPrice + "");
                    m_TxtViewAllNum.setText("去结算(" + UtilNumber.IntegerValueOf(finalNum + "") + ")");//改变底部结算数量
                    m_TxtViewAllPrice.setText(PRICE_HALL + finalPrice + "");//改变底部合计

                    UtilPreferences.putString(mAtyShopCart, AtyShoppingCart.SHOPCART_CHOOSE, isChecked + "");

                    //判断是否改变全选的状态
                    refreshChanged();
                    //刷新当前自营或非自营的全选状态
                    refreshSelfStateChanged(shoppingCart2);
                } else {
                    if (preFerNumAll != 0 || preFerPriceAll != 0) {
                        num = UtilNumber.DoubleValueOf(mListTreeElement.get(mPosition).getmShopingCart().getBuynum() + "");
                        price = UtilNumber.DoubleValueOf(mListTreeElement.get(mPosition).getmShopingCart().getSellprice() + "");
                        allPrice = UtilNumber.DoubleValueOf(num * price + "");

                        finalNum = UtilNumber.IntegerValueOf((preFerNumAll - num) + "");
                        finalPrice = UtilNumber.DoubleValueOf((preFerPriceAll - allPrice) + "");
                        m_TxtViewAllNum.setText("去结算(" + UtilNumber.IntegerValueOf(finalNum + "") + ")");//改变底部结算数量
                        m_TxtViewAllPrice.setText(PRICE_HALL + finalPrice + "");//改变底部合计
                    }
                    //全选的状态保存
                    UtilPreferences.putString(mAtyShopCart, AtyShoppingCart.SHOPCART_CHOOSE, isChecked + "");
                    mCheckboxSelf.setChecked(false);
                    m_CheckAllBox.setChecked(false);//改变底部全选状态
                    mDeleteAllAll.setChecked(false);//改变底部全选状态
                }

                //保存到数据库
                SqliteShoppingCart.getInstance(mAtyShopCart).update(mListTreeElement.get(mPosition).getmShopingCart().getID(), mListTreeElement.get(mPosition).getmShopingCart());
                UtilPreferences.putString(mAtyShopCart, AtyShoppingCart.SHOPCART_PRICE, finalPrice + "");
                UtilPreferences.putInt(mAtyShopCart, AtyShoppingCart.SHOPCART_NUM, UtilNumber.IntegerValueOf(finalNum + ""));
            }else {

            }
            //刷新本地
            notifyDataSetChanged();

        }

        /**
         * 是否改变全选的状态、自营的全选的状态，非自营全选的状态
         */
        private void refreshChanged() {
            List<ShoppingCart> shopCartList = SqliteShoppingCart.getInstance(mAtyShopCart).getListByShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");

            if (!UtilList.isEmpty(shopCartList)) {
                boolean isAllCheck = true;
                for (int k = 0; k < shopCartList.size(); k++) {
                    if (!shopCartList.get(k).getIschoose()) {
                        isAllCheck = false;
                    }
                }
                m_CheckAllBox.setChecked(isAllCheck);
                mDeleteAllAll.setChecked(isAllCheck);
            }
        }


        @OnClick(R.id.trade_btn_num_raise)
        public void onRaiseClick(View v) {//添加
            boolean isChecked = mItemCheckBox.isChecked();
            //数量
            String numEdit = mTextViewInputNums.getText().toString().trim();
            double price = UtilNumber.DoubleValueOf(mListTreeElement.get(mPosition).getmShopingCart().getSellprice() + "");
            if (UtilNumber.isNumeric(numEdit)) {
                double result = UtilNumber.DoubleValueOf(numEdit);
                if (result <= 0) {
                    mTextViewInputNums.setText(UtilNumber.IntegerValueOf(++result + "") + "");
                    mTextViewTotalPrice.setText(PRICE_XALL + price + "");
                } else {
                    LogUtils.e("结果为零==============>");
                    mTextViewInputNums.setText(UtilNumber.IntegerValueOf(++result + "") + "");
                    double allPrice = UtilNumber.DoubleValueOf(result * price + "");
                    mTextViewTotalPrice.setText(PRICE_XALL + allPrice + "");
                    //小计
                    if (isChecked) {//改变总合计和总数量
                        LogUtils.e("++改变总合计和总数量==========改变====>");
                        //获取本地存储总数量和价格
                        double preFerNumAll = UtilPreferences.getInt(mAtyShopCart, AtyShoppingCart.SHOPCART_NUM);
                        double preFerPriceAll = UtilNumber.DoubleValueOf(UtilPreferences.getString(mAtyShopCart, AtyShoppingCart.SHOPCART_PRICE));
                        double allNum = ++preFerNumAll;
                        double priceAll = UtilNumber.DoubleValueOf(preFerPriceAll + price + "");
                        m_TxtViewAllNum.setText("去结算(" + UtilNumber.IntegerValueOf(allNum + "") + ")");//改变底部结算数量
                        m_TxtViewAllPrice.setText(PRICE_HALL + priceAll + "");//改变底部合计
                        //存储到本地
                        UtilPreferences.putString(mAtyShopCart, AtyShoppingCart.SHOPCART_PRICE, priceAll + "");
                        UtilPreferences.putInt(mAtyShopCart, AtyShoppingCart.SHOPCART_NUM, UtilNumber.IntegerValueOf(allNum + ""));
                    } else {
                        LogUtils.e("++不改变总合计和总数量==============>");
                    }
                }
                //改变数据库中此商品的数量
                mListTreeElement.get(mPosition).getmShopingCart().setBuynum(result + "");
                SqliteShoppingCart.getInstance(mAtyShopCart).update(mListTreeElement.get(mPosition).getmShopingCart().getID(), mListTreeElement.get(mPosition).getmShopingCart());
            } else {
                mTextViewInputNums.setText("1");
                mTextViewTotalPrice.setText(PRICE_XALL + price + "");
            }

            //发送广播更新底部购物车显示
            sendShopChartBroadcast();
        }


        @OnClick(R.id.trade_btn_num_reduce)
        public void onReduceClick(View v) {//减少
            String numEdit = mTextViewInputNums.getText().toString().trim();

            if (UtilNumber.isNumeric(numEdit) && !UtilList.isEmpty(mListTreeElement)&& mListTreeElement.get(mPosition).getmShopingCart()!=null) {
                double result = UtilNumber.DoubleValueOf(numEdit);
                double price = UtilNumber.DoubleValueOf(mListTreeElement.get(mPosition).getmShopingCart().getSellprice() + "");
                if (result <= 1) {
                    final WgtAlertDialog mAtDialog = new WgtAlertDialog();
                    mAtDialog.show(mAtyShopCart,
                            "取消", "确定",
                            "确认删除当前物品？",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mAtDialog.dismiss();
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //更新界面数据
                                    boolean isChoose = mListTreeElement.get(mPosition).getmShopingCart().getIschoose();
                                    LogUtils.e("OnCheckClick====点击后触发==========>" + isChoose + mPosition);
                                    double price1 = UtilNumber.DoubleValueOf(mListTreeElement.get(mPosition).getmShopingCart().getSellprice() + "");
                                    if (isChoose == true) {
                                        double preFerNumAll = UtilPreferences.getInt(mAtyShopCart, AtyShoppingCart.SHOPCART_NUM);
                                        double preFerPriceAll = UtilNumber.DoubleValueOf(UtilPreferences.getString(mAtyShopCart, AtyShoppingCart.SHOPCART_PRICE));
                                        double allNum = --preFerNumAll;
                                        double priceAll = UtilNumber.DoubleValueOf((preFerPriceAll - price1) + "");
                                        m_TxtViewAllNum.setText("去结算(" + UtilNumber.IntegerValueOf(allNum + "") + ")");//改变底部结算数量
                                        m_TxtViewAllPrice.setText(PRICE_HALL + priceAll + "");//改变底部合计
                                        //存储到本地
                                        UtilPreferences.putString(mAtyShopCart, AtyShoppingCart.SHOPCART_PRICE, priceAll + "");
                                        UtilPreferences.putInt(mAtyShopCart, AtyShoppingCart.SHOPCART_NUM, UtilNumber.IntegerValueOf(allNum + ""));
                                    }
                                    LogUtils.e("onReduceClick====点击后触发======0000====>" +mListTreeElement.size());
                                    //删除数据库中数据
                                    SqliteShoppingCart.getInstance(mAtyShopCart).deleteShoppingCart(mListTreeElement.get(mPosition).getmShopingCart());
                                    mListTreeElement.remove(mPosition);
                                    LogUtils.e("onReduceClick====点击后触发======1111====>" +mListTreeElement.size());
                                    //TODO 改变
                                    //刷新当前数据
                                    mAtyShopCart.initData();

                                    //购物车为空时显示为空的界面
                                    if (mListTreeElement.size() <= 0) {
//                                        mAtyShopCart.onBackPressed();
                                        mAtyShopCart.visiableNoShopping();
                                    }
                                    //发送广播更新底部购物车显示
                                    sendShopChartBroadcast();

                                    mAtDialog.dismiss();
                                }
                            });

                } else {
                    mTextViewInputNums.setText(UtilNumber.IntegerValueOf(--result + "") + "");
                    boolean isChecked = mItemCheckBox.isChecked();
                    /*****************/
                    if (result == 0) {

//                        mItemCheckBox.setChecked(false);
                        m_CheckAllBox.setChecked(false);
                        mDeleteAllAll.setChecked(false);
                        //改变数据库的数据
                        mListTreeElement.get(mPosition).getmShopingCart().setIschoose(false);
                    } else {
                        LogUtils.e("数量不为为零===00===改变小计========>");
                    }
                    double allPrice = UtilNumber.DoubleValueOf(result * price + "");
                    mTextViewTotalPrice.setText(PRICE_XALL + allPrice + "");
                    //小计
                    if (isChecked) {
                        //获取本地存储总数量和价格
                        double preFerNumAll = UtilPreferences.getInt(mAtyShopCart, AtyShoppingCart.SHOPCART_NUM);
                        double preFerPriceAll = UtilNumber.DoubleValueOf(UtilPreferences.getString(mAtyShopCart, AtyShoppingCart.SHOPCART_PRICE));
                        double allNum = --preFerNumAll;
                        double priceAll = UtilNumber.DoubleValueOf((preFerPriceAll - price) + "");
                        m_TxtViewAllNum.setText("去结算(" + UtilNumber.IntegerValueOf(allNum + "") + ")");//改变底部结算数量
                        m_TxtViewAllPrice.setText(PRICE_HALL + priceAll + "");//改变底部合计
                        //存储到本地
                        UtilPreferences.putString(mAtyShopCart, AtyShoppingCart.SHOPCART_PRICE, priceAll + "");
                        UtilPreferences.putInt(mAtyShopCart, AtyShoppingCart.SHOPCART_NUM, UtilNumber.IntegerValueOf(allNum + ""));
                    } else {
                        LogUtils.e("--不改变总合计和总数量==============>");
                    }
                    //改变数据库中此商品的数量
                    mListTreeElement.get(mPosition).getmShopingCart().setBuynum(result + "");
                    SqliteShoppingCart.getInstance(mAtyShopCart).update(mListTreeElement.get(mPosition).getmShopingCart().getID(), mListTreeElement.get(mPosition).getmShopingCart());
                }

            } else {
//                mTextViewInputNums.setText("0");
//                mItemCheckBox.setChecked(false);
//                mTextViewTotalPrice.setText(PRICE_XALL + "0.0");
            }

            //发送广播更新底部购物车显示
            sendShopChartBroadcast();
        }


        @OnClick(R.id.itemRLyt)
        public void mItemRLytClick(View view) {
            Intent mIntent = new Intent(mAtyShopCart, AtyAffordShopDetail.class);
            //便利店列表
            mIntent.putExtra(AtyAffordShop.INTENT_SHOP_ID, mListTreeElement.get(mPosition).getmShopingCart().getID() + "");
            mAtyShopCart.startActivity(mIntent);
        }

        /**
         * 底部购物车显示
         */
        public void sendShopChartBroadcast() {
            int shopCartNum = 0;
            if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance().getEntityLocation().getSTORE() != null) {
                List<ShoppingCart> shopCartList = SqliteShoppingCart.getInstance(mAtyShopCart).getListByShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
                if (!UtilList.isEmpty(shopCartList)) {
                    for (int k = 0; k < shopCartList.size(); k++) {
                        shopCartNum += UtilNumber.IntegerValueOf(shopCartList.get(k).getBuynum());
                    }
                }
            }

            Intent intent = new Intent();  //Itent就是我们要发送的内容
            intent.putExtra("data", shopCartNum);
            intent.setAction(CommConstans.REGISTER.BROADCAST_INTENT_ACTION);   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
            mAtyShopCart.sendBroadcast(intent);   //发送广播
        }

        /**
         * 更新
         */
        public void update(final int position, ArrayList<TreeElement> _listMap) {
            refreshList(_listMap);
            refresh(position);
        }


        /**
         * 初始化数据
         */
        private void initData(){
            if (!UtilList.isEmpty(mListTree)){
                mShopNum =0;
                mSelfShopNum = 0;
                mListShop.clear();
                mListselfShop.clear();
                boolean isSelfAllBox = true;
                boolean isNoSelfAllBox = true;
                for (int i=0;i<mListTree.size();i++){
                    ShoppingCart shoppingCart = mListTree.get(i).getmShopingCart();
                    //是否是自营商品：0：不是，1：是。
                    if ("0".equals(shoppingCart.getShoptype())){
                        mListTree.get(i).setIsSelf(0);
                        //非自营列表最后一个的索引
                        mShopNum++;
                        if (i==0){
                            isSelfTop = false;//非自营
                        }
                        mListShop.add(mListTree.get(i));
                        if (!shoppingCart.getIschoose() ){
                            isSelfAllBox = false;
                        }
                    }else if("1".equals(shoppingCart.getShoptype())){
                        mListTree.get(i).setIsSelf(1);
                        //非自营列表最后一个的索引
                        mSelfShopNum++;
                        if (i==0){
                            isSelfTop = true;//自营
                        }
                        mListselfShop.add(mListTree.get(i));
                        if (!shoppingCart.getIschoose() ){
                            isNoSelfAllBox = false;
                        }
                    }
                }
                LogUtils.e("onReduceClick=========初始化==77777=update==>"+mListTree.size());
                mListTreeElement.clear();
//                mListTreeElement = null;
//                mListTreeElement = new ArrayList<>();
                //如非自营在顶部
                if (!isSelfTop){
                    mListTreeElement.addAll(mListShop);
                    mListTreeElement.addAll(mListselfShop);
                    //自营和非自营不为空时
                    if (!UtilList.isEmpty(mListTreeElement)){
                        mListTreeElement.get(0).setIsVisible(true);//非自营列表个数
                        mListTreeElement.get(0).setIsChoosed(isSelfAllBox);//非自营列表个数
                        if (!UtilList.isEmpty(mListselfShop)){
                            mListTreeElement.get(mShopNum).setIsVisible(true);//自营列表个数
                            mListTreeElement.get(mShopNum).setIsChoosed(isNoSelfAllBox);//自营列表个数
                        }
                    }else {
                        LogUtils.e("自营和非自营为空============null====>");
                    }

                }else {
                    mListTreeElement.addAll(mListselfShop);
                    mListTreeElement.addAll(mListShop);
                    //自营和非自营不为空时
                    if (!UtilList.isEmpty(mListTreeElement)){
                        mListTreeElement.get(0).setIsVisible(true);//自营列表个数
                        mListTreeElement.get(0).setIsChoosed(isNoSelfAllBox);//自营列表个数
                        if (!UtilList.isEmpty(mListShop)){
                            mListTreeElement.get(mSelfShopNum).setIsVisible(true);//非自营列表个数
                            mListTreeElement.get(mSelfShopNum).setIsChoosed(isSelfAllBox);//非自营列表个数
                        }
                    }else {
                        LogUtils.e("自营和非自营为空============null====>");
                    }


                }
                LogUtils.e("onReduceClick=========初始化==888888=update==>"+mListTreeElement.size());
            }else {
                //数据为空
            }

        }

        /**
         * 刷新当前自营或非自营的全选状态
         * @param shoppingCart2
         */
       private void refreshSelfStateChanged(ShoppingCart shoppingCart2){
           //判断是否改变自营或非自营全选状态
           boolean isSelfAllBox = true;
           String selfState = shoppingCart2.getShoptype();
           for (int i=0;i<mListTreeElement.size();i++){
               ShoppingCart _cart3 = mListTreeElement.get(i).getmShopingCart();
               //是否是自营商品：0：不是，1：是。
               if (selfState.equals(_cart3.getShoptype()) && !_cart3.getIschoose() ){
                   isSelfAllBox = false;
               }

           }
           //如果是非自营
           if ("0".equals(selfState)){
               //如非自营在顶部
               if (!isSelfTop){
                   //是否是自营商品：0：不是，1：是。
                   mListTreeElement.get(0).setIsChoosed(isSelfAllBox);//非自营列表个数
               }else {
                   if (!UtilList.isEmpty(mListShop)){
                       mListTreeElement.get(mSelfShopNum).setIsChoosed(isSelfAllBox);//非自营列表个数
                   }
               }
           }else if("1".equals(selfState)){
               //如非自营在顶部
               if (!isSelfTop){
                   if (!UtilList.isEmpty(mListselfShop)){
                       mListTreeElement.get(mShopNum).setIsChoosed(isSelfAllBox);//自营列表个数
                   }
               }else {
                    mListTreeElement.get(0).setIsChoosed(isSelfAllBox);//非自营列表个数
               }
           }

       }



    }

}
