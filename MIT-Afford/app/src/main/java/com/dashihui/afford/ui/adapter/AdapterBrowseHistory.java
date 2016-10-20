package com.dashihui.afford.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dashihui.afford.R;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.thirdapi.greedsqlite.BrowseHistory;
import com.dashihui.afford.ui.activity.shop.AtyAffordShop;
import com.dashihui.afford.ui.activity.shop.AtyAffordShopDetail;
import com.dashihui.afford.util.list.UtilList;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;
import java.util.Map;

/**
 * Created by NiuFC on 2015/11/11.
 */
public class AdapterBrowseHistory extends AdapterBase<BrowseHistory> {

//    private SqliteBrowseHistory mSqlitBrowseHistory;
    private List<BrowseHistory> listObject;
    public AdapterBrowseHistory(Activity context, List<BrowseHistory> _mList) {
        super(context,_mList);
        listObject = _mList;
//        mSqlitBrowseHistory = new SqliteBrowseHistory();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.aty_my_browsehistory_item, null);
            viewHolder = new ViewHolder(mContext,mList);
            //依赖注入初始化
            ViewUtils.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
            viewHolder.refresh(position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.update(position,mList);
        }

        return convertView;
    }


    /**
     * @author p
     */
    static class ViewHolder {

        @ViewInject(R.id.apps_item_img)
        private ImageView mImageView;//图片
        @ViewInject(R.id.apps_item_app_name_tv)
        private TextView mTextViewName;//商品名
        @ViewInject(R.id.apps_item_download_count_tv)
        private TextView mTextViewPrice;//商品现价
        @ViewInject(R.id.apps_item_size_tv)
        private TextView mTextViewOldPrice;//商品原价
        @ViewInject(R.id.apps_item_desc_tv)
        private TextView mTextViewInputNums;//输入的数字

        private Activity mContent;
        private List<BrowseHistory> mListMap;
        private List<Map<String, Object>> mBrowseListMap;
        private int mPosition;

        public ViewHolder(Activity context,List<BrowseHistory> _listMap) {
            mContent = context;
            mListMap = _listMap;
        }

        /**
         *  更新最新数据
         * @param _listMap
         */
        public void refreshList(List<BrowseHistory> _listMap){
            mListMap = _listMap;
        }
        /**
         * 第一次刷新
         */
        public void refresh(final int position) {
            mPosition = position;
            if (!UtilList.isEmpty(mListMap)){
                Glide.with(mContent)
                        .load(AffConstans.PUBLIC.ADDRESS_IMAGE + mListMap.get(position).getImgsrc() + "")
                        .placeholder(R.drawable.default_list)
                        .error(R.drawable.default_list)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mImageView);
                mTextViewName.setText(mListMap.get(position).getName() + "");
                mTextViewInputNums.setText("浏览"+mListMap.get(position).getHistorynum()+"次");
                mTextViewPrice.setText("￥" + mListMap.get(position).getSellprice());
                mTextViewOldPrice.setText("￥" + mListMap.get(position).getMarketprice());
                mTextViewOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
                if((mListMap.get(position).getShoptype()).equals("1")){
                    Bitmap b = BitmapFactory.decodeResource(mContent.getResources(), R.drawable.title_myself);
                    ImageSpan imgSpan = new ImageSpan(mContent, b);
                    SpannableString spanString = new SpannableString("icon");
                    spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mTextViewName.setText(spanString);
                    mTextViewName.append(mListMap.get(position).getName() + "");
                }else{
                    mTextViewName.setText(mListMap.get(position).getName() + "");                }
            }else{
                LogUtils.e("refresh===Error=====null=========>" + mListMap + "");
            }

        }

        /**
         * 更新
         */
        public void update(final int position,List<BrowseHistory> _listMap) {
            refreshList(_listMap);
            refresh(position);
        }

        @OnClick(R.id.itemLyt)
        public void OnCheckClick(View v){
            Intent intent = new Intent(mContent, AtyAffordShopDetail.class);
            intent.putExtra(AtyAffordShop.INTENT_SHOP_ID, mListMap.get(mPosition).getID() + "");
            intent.putExtra(AtyAffordShop.INTENT_SHOP_IMG, mListMap.get(mPosition).getImgsrc() + "");
            mContent.startActivity(intent);
        }

//        @OnClick(R.id.shopcart_image)//购物车图片
//        public void onImgBtnClick(View view) {
//            if (!UtilList.isEmpty(mListMap)) {
//                ShoppingCart _ShopCart = SqliteShoppingCart.getInstance(mContent).getShoppingCartByUid(mListMap.get(mPosition).getID() + "");
//                if (_ShopCart != null) {
//                    int _ShopNums = UtilNumber.IntegerValueOf(_ShopCart.getBuynum());
////                    LogUtils.e("购物车已有数据===============>" + _ShopNums);
////                    addShopCart(mListMap.get(mPosition), ++_ShopNums);
//                } else {
////                    LogUtils.e("购物车第一次添加数据===============>" + _ShopCart);
////                    addShopCart(mListMap.get(mPosition), 1);
//                }
//                int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
//                view.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
//                ImageView ball = new ImageView(mContent);// buyImg是动画的图片
//                //设置动画图片大小
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(40, 40);
//                ball.setLayoutParams(layoutParams);
//                if (!UtilString.isEmpty(mListMap.get(mPosition).getImgsrc())) {
//                    String uri = AffConstans.PUBLIC.ADDRESS_IMAGE + mListMap.get(mPosition).getImgsrc() + "";
//                    Glide.with(mContent)
//        .load(uri)
//        .placeholder(R.drawable.cvs_btn_addshopcar)
//        .error(R.drawable.cvs_btn_addshopcar)
//        .diskCacheStrategy(DiskCacheStrategy.ALL)
//        .into(ball);
//                } else {
////                    LogUtils.e("uri===图片地址====error=======>" + mListMap.get(mPosition).get("THUMB")+"");
//                }
//                ((AtyMyBrowseHistory) mContent).setAnim(ball, startLocation);// 开始执行动画
//            } else {
//                Toast.makeText(mContent, "服务器繁忙，请稍后再试...", Toast.LENGTH_SHORT).show();
//            }
//        }

//        private void addShopCart(Map<String, Object> objectMap, int num) {
//            if (objectMap == null) {
////                LogUtils.e("null===============>" + objectMap);
//                return;
//            }
//            ShoppingCart _shoppingCart = new ShoppingCart();
//            //点击加入购物车将数据保存至本地数据库
//            _shoppingCart.setID(objectMap.get("ID") + "");
//            _shoppingCart.setShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
//            _shoppingCart.setName(objectMap.get("NAME") + "");
//            _shoppingCart.setSpec(objectMap.get("SPEC") + "");
//            _shoppingCart.setShortinfo(objectMap.get("SHORTINFO") + "");
//            _shoppingCart.setMarketprice(objectMap.get("MARKETPRICE") + "");
//            String img = objectMap.get("THUMB") + "";
//            if (!UtilString.isEmpty(img)) {
//                _shoppingCart.setImgsrc(img + "");
//            } else {
//                _shoppingCart.setImgsrc("");
//            }
//              是否是自营商品：0：不是，1：是
//            _shoppingCart.setShoptype(objectMap.get("ISSELF") + "");
//            _shoppingCart.setUrv(objectMap.get("URV") + "");
//            _shoppingCart.setType(objectMap.get("TYPE") + "");
//            _shoppingCart.setBuynum(num + "");
//            _shoppingCart.setSellprice(objectMap.get("SELLPRICE") + "");
//            _shoppingCart.setIschoose(true);
//            //保存到本地数据库
//            SqliteShoppingCart.getInstance(mContent).update(objectMap.get("ID") + "", _shoppingCart);
//        }
    }
}
