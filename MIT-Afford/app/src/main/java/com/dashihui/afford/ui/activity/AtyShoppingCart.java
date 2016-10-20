package com.dashihui.afford.ui.activity;

import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessUser;
import com.dashihui.afford.business.entity.EtyMoney;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseMenuActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.sqlite.SqliteShoppingCart;
import com.dashihui.afford.thirdapi.greedsqlite.ShoppingCart;
import com.dashihui.afford.thirdapi.swipemenulistview.SwipeMenu;
import com.dashihui.afford.thirdapi.swipemenulistview.SwipeMenuCreator;
import com.dashihui.afford.thirdapi.swipemenulistview.SwipeMenuItem;
import com.dashihui.afford.thirdapi.swipemenulistview.SwipeMenuListView;
import com.dashihui.afford.ui.activity.shop.AtyAffordShop;
import com.dashihui.afford.ui.adapter.AdapterShoppingCart;
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
import java.util.Map;

public class AtyShoppingCart extends BaseMenuActivity {
    @ViewInject(R.id.listView)
    private ListView mListView;//正常时显示
    private List<ApplicationInfo> mAppList;

    @ViewInject(R.id.cBoxAgree)
    private CheckBox mCheckBoxAll;//全选
    @ViewInject(R.id.deleteAllBoxAgree)
    private CheckBox mDeleteAllAll;//全选

    @ViewInject(R.id.txtViewPrice)
    private TextView mTxtViewPrice;//合计
    @ViewInject(R.id.txtViewSettlement)
    private TextView mTxtViewSettlement;//结算
    @ViewInject(R.id.aty_shopcart_noshopping_layout)
    private LinearLayout mNoShoppingLyout;//购物车没有物品时显示
    @ViewInject(R.id.aty_affordcart_detail_bottom)
    private LinearLayout mHaveShoppingLayout;

    @ViewInject(R.id.aty_edit_bottom)
    private LinearLayout mEditbottom;//编辑删除

    @ViewInject(R.id.title)
    private TextView mTitle;
    @ViewInject(R.id.ibtnBack)
    private ImageButton mIBtnBack;
    @ViewInject(R.id.editTxt)
    private TextView mEditTxt;//编辑
    @ViewInject(R.id.completeTxt)
    private TextView mCompleteTxt;//完成

    private AdapterShoppingCart mAdapterShoppingCart;
    private List<ShoppingCart> mShopCartList;
    private ArrayList<TreeElement> mListTree ;

    public final static String SHOPCART_NUM = "ShopCartNum";
    public final static String SHOPCART_PRICE = "ShopCartPrice";
    public final static String SHOPCART_CHOOSE = "shopcartChoose";
    private boolean isChoose;
    private double mShoppingNum, mShoppingPrice, mUpdateNum, mUpdatePrice;

    private Map<String, Object> mTotalMap;
    private BusinessUser mBllUser;

    private EtyMoney userMoney;

    @Override
    public int getContentViewLayoutResId() {
        return R.layout.aty_shoppingcart;
    }

    @Override
    protected void onCreatOverride(Bundle savedInstanceState) {
        ViewUtils.inject(this);//依赖注入
        //标题
        mTitle.setText(R.string.shoppingCart);
        mListTree = new ArrayList<>();

         //滑动删除
//        itemDelete();
    }


    @Override
    protected void onResume() {
        //更新购物车数据
        mBllUser = new BusinessUser(this);
        mBllUser.getMoney();
        userMoney = new EtyMoney();
        if (AffordApp.getInstance().getEntityLocation()!=null){
            mShopCartList = SqliteShoppingCart.getInstance(this).getListByShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
        }

        if (UtilList.isEmpty(mShopCartList)){
            visiableNoShopping();
        }else {
            initData();
        }
        super.onResume();
    }

    //购物车总数为0时显示的提示页面
    public void visiableNoShopping() {
        //查询当前店铺购物车商品
        if (AffordApp.getInstance().getEntityLocation()!=null){
            mShopCartList = SqliteShoppingCart.getInstance(this).getListByShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
        }
        //购物车为空时显示为空的界面
        if (mShopCartList.size() <= 0) {
            mNoShoppingLyout.setVisibility(View.VISIBLE);
            mEditbottom.setVisibility(View.GONE);
            mListView.setVisibility(View.GONE);
            mHaveShoppingLayout.setVisibility(View.GONE);
            mCompleteTxt.setVisibility(View.GONE);//完成按钮
            mEditTxt.setVisibility(View.GONE);//编辑按钮
//            UtilDialog.dissProgressDialog();
        } else {
            mNoShoppingLyout.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mHaveShoppingLayout.setVisibility(View.VISIBLE);
            mEditTxt.setVisibility(View.GONE);//编辑按钮
        }
    }


    @OnClick({(R.id.cBoxAgree), (R.id.deleteAllBoxAgree)})
    public void onCheckedClick(View view) {
        boolean isChecked;
        switch (view.getId()) {
            case R.id.cBoxAgree://
                isChecked = mCheckBoxAll.isChecked();
                break;
            case R.id.deleteAllBoxAgree://删除全选
                isChecked = mDeleteAllAll.isChecked();
                break;
            default:
                isChecked = mCheckBoxAll.isChecked();
                break;
        }

        double num = 0;
        double price = 0;
        for (int i = 0; i < mShopCartList.size(); i++) {
            mShopCartList.get(i).setIschoose(isChecked);
            num += UtilNumber.DoubleValueOf(mShopCartList.get(i).getBuynum());
            price += UtilNumber.DoubleValueOf(mShopCartList.get(i).getBuynum()) * UtilNumber.DoubleValueOf(mShopCartList.get(i).getSellprice());
            //修改数据库中所有选择状态
            SqliteShoppingCart.getInstance(this).update(mShopCartList.get(i).getID(), mShopCartList.get(i));
        }

        if (isChecked) {
            mTxtViewPrice.setText(AdapterShoppingCart.PRICE_HALL + UtilNumber.DoubleValueOf(price + "") + "");
            mTxtViewSettlement.setText("去结算(" + UtilNumber.IntegerValueOf(num + "") + ")");
            //TODO 存数据到本地
            //存储本地全选状态
            UtilPreferences.putString(this, SHOPCART_CHOOSE, isChecked + "");
            UtilPreferences.putInt(this, SHOPCART_NUM, UtilNumber.IntegerValueOf(num + ""));
            UtilPreferences.putString(this, SHOPCART_PRICE, UtilNumber.DoubleValueOf(price + "") + "");
        } else {
            mTxtViewPrice.setText(AdapterShoppingCart.PRICE_HALL + "0.0");
            mTxtViewSettlement.setText("去结算");
            //TODO 存数据到本地
            //存储本地全选状态
            UtilPreferences.putString(this, SHOPCART_CHOOSE, isChecked + "");
            UtilPreferences.putInt(this, SHOPCART_NUM, 0);
            UtilPreferences.putString(this, SHOPCART_PRICE, 0 + "");
        }
        mAdapterShoppingCart.setList(mListTree);
        mAdapterShoppingCart.notifyDataSetChanged();


    }

    @OnClick(R.id.txtViewSettlement)//去结算
    public void OnSettlementClick(View view) {
        if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance().getEntityLocation().getSTORE() != null) {
            int communtyId = UtilNumber.IntegerValueOf(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
            if (communtyId == CommConstans.LOCCOMMUNITY.COMMUNITYID) {
                if (mDialog == null) {
                    mDialog = new WgtAlertDialog();
                }
                mDialog.show(this,
                        "确定",
                        "您所在的小区暂未开通服务，敬请期待！",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.dismiss();
                            }
                        }, false, false);
            } else {
                if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance().getEntityLocation().getSTORE() != null) {
                    List<ShoppingCart> shopCartList = SqliteShoppingCart.getInstance(this).getListByShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "", true);
                    if (!UtilList.isEmpty(shopCartList)) {
                        mBaseUtilAty.startActivity(AtySettlement.class);
                        //finish();
                    } else {
//                UtilToast.show(this,"您还没有选择商品哦！", Toast.LENGTH_SHORT);
                        if (mDialog == null) {
                            mDialog = new WgtAlertDialog();
                        }
                        mDialog.show(this,
                                "确定",
                                "您还没有选择商品哦！",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mDialog.dismiss();
                                    }
                                });
                    }

                } else {
                    if (mDialog == null) {
                        mDialog = new WgtAlertDialog();
                    }
                    mDialog.show(this,
                            "确定",
                            "请重新登录！",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialog.dismiss();
                                   // AtyShoppingCart.this.finish();
                                }
                            }, false, false);
                }
            }
        }
    }

    @OnClick(R.id.txtViewDetele)//删除
    public void OnDeteleClick(View view) {
        if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance().getEntityLocation().getSTORE() != null) {
            List<ShoppingCart> shopCartList = SqliteShoppingCart.getInstance(this).getListByShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "", true);
            if (!UtilList.isEmpty(shopCartList)) {
                //删除
                if (mDialog == null) {
                    mDialog = new WgtAlertDialog();
                }
                mDialog.show(this, "取消",
                        "确定",
                        "确定要删除选中的商品？",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.dismiss();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                List<ShoppingCart> _shopCartList = SqliteShoppingCart.getInstance(AtyShoppingCart.this).getListByShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "", true);
                                for (int i = 0; i < _shopCartList.size(); i++) {
                                    SqliteShoppingCart.getInstance(AtyShoppingCart.this).deleteShoppingCart(_shopCartList.get(i));
                                }
                                if (AffordApp.getInstance().getEntityLocation()!=null){
                                    mShopCartList = SqliteShoppingCart.getInstance(AtyShoppingCart.this).getListByShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
                                }
                                //去掉删除数据残留
                                mTxtViewPrice.setText(AdapterShoppingCart.PRICE_HALL + "0.0");
                                mTxtViewSettlement.setText("去结算");
                                //TODO 存数据到本地
                                //存储本地全选状态
                                UtilPreferences.putString(AtyShoppingCart.this, SHOPCART_CHOOSE, false + "");
                                UtilPreferences.putInt(AtyShoppingCart.this, SHOPCART_NUM, 0);
                                UtilPreferences.putString(AtyShoppingCart.this, SHOPCART_PRICE, 0 + "");
                                mAdapterShoppingCart.setList(mListTree);
                                mAdapterShoppingCart.notifyDataSetChanged();
                                mDialog.dismiss();
                                initData();
                                visiableNoShopping(); //购物车总数为0时显示的提示页面
                                //发送广播更新底部购物车显示
                                sendShopChartBroadcast();

                            }
                        });
            } else {
//                UtilToast.show(this,"您还没有选择商品哦！", Toast.LENGTH_SHORT);
                if (mDialog == null) {
                    mDialog = new WgtAlertDialog();
                }
                mDialog.show(this,
                        "确定",
                        "您还没有选择商品哦！",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.dismiss();
                            }
                        });
            }

        } else {
            if (mDialog == null) {
                mDialog = new WgtAlertDialog();
            }
            mDialog.show(this,
                    "确定",
                    "请重新登录！",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                            //AtyShoppingCart.this.finish();
                        }
                    }, false, false);
        }
    }


    @OnClick(R.id.go_shopping)//购物车没有商品时“立即逛逛”按钮
    public void onTvBtnGoShoppingClick(View v) {
        mBaseUtilAty.startActivity(AtyHome.class);
    }

    @OnClick({(R.id.ibtnBack), (R.id.editTxt), (R.id.completeTxt)})
    public void onIBtnBackClick(View v) {
        switch (v.getId()) {
            case R.id.ibtnBack://顶部返回按钮
                onBackPressed();
                break;
            case R.id.editTxt://编辑
                mAdapterShoppingCart.refreshChanged();//是否改变全选状态
                mEditbottom.setVisibility(View.VISIBLE);//编辑购物车底部栏
                mHaveShoppingLayout.setVisibility(View.GONE);//有商品时底部栏
                mNoShoppingLyout.setVisibility(View.GONE);
                mCompleteTxt.setVisibility(View.VISIBLE);//完成按钮
                mEditTxt.setVisibility(View.GONE);//编辑按钮
                LogUtils.e("===========编辑===========>" + mShopCartList.size());
                break;
            case R.id.completeTxt://完成
     mAdapterShoppingCart.refreshChanged();//是否改变全选状态
     mEditbottom.setVisibility(View.GONE);//编辑购物车底部栏
     mHaveShoppingLayout.setVisibility(View.VISIBLE);//有商品时底部栏
     mCompleteTxt.setVisibility(View.GONE);//完成按钮
     mEditTxt.setVisibility(View.VISIBLE);//编辑按钮

     LogUtils.e("===========完成===========>" + mShopCartList.size());
     break;
     default:
     break;
     }
     }

     @Override
     public void onSuccess(EtySendToUI info) {
         if (info != null) {
             switch (info.getTag()) {
                 case AffConstans.BUSINESS.TAG_USER_MONEY://实惠币
                     LogUtils.e("onSuccess===购物车===========>" + "");
                     // LogUtils.e("onSuccess=====钱包页面=========>" + mTotalMap.get("MONEY")+"  "+mTotalMap.get("INVITECOUNT"));
                     mTotalMap = (Map<String, Object>) info.getInfo();
                     LogUtils.e("onSuccess===购物车===========>" + mTotalMap.toString());
                     AffordApp.getInstance().setUserMoney(mTotalMap.get("MONEY") + "");
                     UtilPreferences.putString(this, CommConstans.Login.MONEY, userMoney.getMONEY() + "");
                     break;

                 default:
                     LogUtils.e("onSuccess===default===========>" + "");
                     break;

             }
         }
     LogUtils.e("onSuccess======AtyAffordShop=========>" + info);
     }

     @Override
     public void onFailure(EtySendToUI error) {
     LogUtils.e("onFailure====AtyAffordShop===========>" + error);
     }

     /**
      * 更新购物车数据
     */
    public void initData() {
        LogUtils.e("mShopCartList===更新购物车数据====>" + "更新购物车数据");
        if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance().getEntityLocation().getSTORE() != null) {
            mShopCartList = SqliteShoppingCart.getInstance(this).getListByShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
            mListTree.clear();
            if (!UtilList.isEmpty(mShopCartList)){

               for (int i =0;i<mShopCartList.size();i++){
                   TreeElement treeElement = new TreeElement();
                   treeElement.setmShopingCart(mShopCartList.get(i));
                   mListTree.add(treeElement);
               }

               mAdapterShoppingCart = new AdapterShoppingCart(this, mListTree, mCheckBoxAll, mDeleteAllAll, mTxtViewPrice, mTxtViewSettlement);
               mListView.setAdapter(mAdapterShoppingCart);

               LogUtils.e("mShopCartList=======>" + mShopCartList.size());
                LogUtils.e("mShopCartList=======>" + mCompleteTxt.getVisibility());
               //判断查询出来的ShopCartList的size是否为0，为0时显示购物车为空的页面,隐藏底部栏和listview
               if (mShopCartList.size() <= 0) {
                   mNoShoppingLyout.setVisibility(View.VISIBLE);
                   mListView.setVisibility(View.GONE);
                   mHaveShoppingLayout.setVisibility(View.GONE);
                   mEditTxt.setVisibility(View.GONE);
                   return;
               } else {
                   mNoShoppingLyout.setVisibility(View.GONE);
                   mListView.setVisibility(View.VISIBLE);
                   mHaveShoppingLayout.setVisibility(View.VISIBLE);
                  if (mCompleteTxt.getVisibility() == View.VISIBLE){
                      mEditTxt.setVisibility(View.GONE);
                      LogUtils.e("mShopCartList==更新购物车数据=====>" + mCompleteTxt.getVisibility() + "he" + View.VISIBLE);
                  }else {
                      // LogUtils.e("mShopCartList==更新购物车数据=====>" + mCompleteTxt.getVisibility()+"he"+View.VISIBLE);
                      mEditTxt.setVisibility(View.VISIBLE);//编辑按钮
                  }
               }
               //初始化本地数量
               if (UtilPreferences.getInt(this, SHOPCART_NUM) == -1) {
                   UtilPreferences.putInt(this, SHOPCART_NUM, 0);
               }
               LogUtils.e("默认没有时取值======int=======>" + UtilPreferences.getInt(this, SHOPCART_NUM));
               //初始化已经选择的商品  合计价格、去结算数量
               double _allNum = 0;
               double _allPrice = 0;
               for (int i = 0; i < mShopCartList.size(); i++) {
                   if (mShopCartList.get(i).getIschoose()) {
                       double num = UtilNumber.DoubleValueOf(mShopCartList.get(i).getBuynum() + "");
                       LogUtils.e("优惠后的价格=====AtyShoppingCart=======>" + mShopCartList.get(i).getSellprice());
                       double price = UtilNumber.DoubleValueOf(mShopCartList.get(i).getSellprice() + "");
                       double numPrice = UtilNumber.DoubleValueOf(num * price + "");
                       _allNum += num;
                       _allPrice += numPrice;
                   }
               }
               if (_allNum > 0) {
                   mTxtViewPrice.setText(AdapterShoppingCart.PRICE_HALL + UtilNumber.DoubleValueOf(_allPrice + "") + "");
                   mTxtViewSettlement.setText("去结算(" + UtilNumber.IntegerValueOf(_allNum + "") + ")");
                   UtilPreferences.putInt(this, SHOPCART_NUM, UtilNumber.IntegerValueOf(_allNum + ""));
                   UtilPreferences.putString(this, SHOPCART_PRICE, UtilNumber.DoubleValueOf(_allPrice + "") + "");
               }else {
                   mTxtViewPrice.setText(AdapterShoppingCart.PRICE_HALL +  "0.0");
                   mTxtViewSettlement.setText("去结算");
                   UtilPreferences.putInt(this, SHOPCART_NUM, UtilNumber.IntegerValueOf(0 + ""));
                   UtilPreferences.putString(this, SHOPCART_PRICE, "0");
               }

               //获取本地全选状态
               String isChoose = UtilPreferences.getString(this, SHOPCART_CHOOSE);
               LogUtils.e("isChoose============333================>" + isChoose);
               if (isChoose != null) {
                   if ("true".equals(isChoose)) {
                       mCheckBoxAll.setChecked(true);
                   } else {
                       mCheckBoxAll.setChecked(false);
                   }
               } else {
                   LogUtils.e("isChoose============4444===============>" + isChoose);
               }
           } else {

               //finish();
           }
//            if (mShopCartList.size() <= 0){
//            }else {

                mAdapterShoppingCart.refreshChanged();//是否改变全选状态
            //}
        }else {
            LogUtils.e("===========数据库为null==============>" + isChoose);
        }
    }
    @Override
    public int getButtonType() {
        return 3;
    }

    /**
     * 滑动删除
     */
/*    public void itemDelete() {
        // 创建一个 MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //创建删除按钮
                SwipeMenuItem deleteItem = new SwipeMenuItem(AtyShoppingCart.this);
                //设置背景颜色
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                //设置宽度
                deleteItem.setWidth(dp2px(120));
                //设置顶部距离
                deleteItem.setMarginTop(dp2px(10));
                //设置背景图片
                deleteItem.setIcon(R.drawable.ic_delete);
                //所有设置添加到按钮
                menu.addMenuItem(deleteItem);*/
       /* mListView.setMenuCreator(creator);
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                //查询当前店铺购物车商品

                if (!UtilList.isEmpty(mListTree)){
                    //从购物车删除当前点击的商品
                    SqliteShoppingCart.getInstance(AtyShoppingCart.this).deleteShoppingCart(mListTree.get(position).getmShopingCart());

                    //移除当前点击的item
                    mListTree.remove(position);

                    //更新购物车列表，刷新数据
                    mAdapterShoppingCart.setList(mListTree);
                    mAdapterShoppingCart.notifyDataSetChanged();

                    //发送广播更新底部购物车显示
                    sendShopChartBroadcast();

                    //更新底部合计金额及结算数量
                    updateBottomPriceAndNum();
                }

            }
        });*/
        // set SwipeListener
      /*  mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                if (!UtilList.isEmpty(mShopCartList)) {
                    isChoose = mShopCartList.get(position).getIschoose();

                    mShoppingNum = UtilPreferences.getInt(AtyShoppingCart.this, SHOPCART_NUM);
                    mShoppingPrice = UtilNumber.DoubleValueOf(UtilPreferences.getString(AtyShoppingCart.this, SHOPCART_PRICE));

                    double num = UtilNumber.DoubleValueOf(mShopCartList.get(position).getBuynum() + "");
                    double price = UtilNumber.DoubleValueOf(mShopCartList.get(position).getSellprice() + "");
                    double numPrice = UtilNumber.DoubleValueOf(num * price + "");

                    //删除item后“去结算”变化后的数字写入控件
                    mUpdateNum = mShoppingNum - num;//去结算数字 减去 删除的item的购买数量
                    mUpdatePrice = mShoppingPrice - numPrice;//去结算金额 减去 删除item的购买数量总价
                    //删除当前item时判断当前item 的选择框有没有被选中，如果被选中则从底部结算数字和合计金额减去当前item的总金额和数字
                } else {
                    LogUtils.e("onSwipeStart=======================>");
                }

            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });
    }*/


    //    //更新底部合计金额及结算数量
    public void updateBottomPriceAndNum() {
        if (isChoose == true) {
            if (mUpdateNum > 0) {
                mTxtViewPrice.setText(AdapterShoppingCart.PRICE_HALL + UtilNumber.DoubleValueOf(mUpdatePrice + "") + "");
                mTxtViewSettlement.setText("去结算(" + UtilNumber.IntegerValueOf(mUpdateNum + "") + ")");
                UtilPreferences.putInt(this, SHOPCART_NUM, UtilNumber.IntegerValueOf(mUpdateNum + ""));
                UtilPreferences.putString(this, SHOPCART_PRICE, UtilNumber.DoubleValueOf(mUpdatePrice + "") + "");
            } else {
                mTxtViewPrice.setText(AdapterShoppingCart.PRICE_HALL + "0.0");
                mTxtViewSettlement.setText("去结算");
                //TODO 存数据到本地
                //存储本地全选状态
                UtilPreferences.putInt(this, SHOPCART_NUM, 0);
                UtilPreferences.putString(this, SHOPCART_PRICE, 0 + "");
            }
        } else {
            mTxtViewPrice.setText(AdapterShoppingCart.PRICE_HALL + UtilNumber.DoubleValueOf(mShoppingPrice + "") + "");
            mTxtViewSettlement.setText("去结算(" + UtilNumber.IntegerValueOf(mShoppingNum + "") + ")");
            UtilPreferences.putInt(this, SHOPCART_NUM, UtilNumber.IntegerValueOf(mShoppingNum + ""));
            UtilPreferences.putString(this, SHOPCART_PRICE, UtilNumber.DoubleValueOf(mShoppingPrice + "") + "");
        }
        mAdapterShoppingCart.refreshChanged();//是否改变全选状态
        visiableNoShopping(); //购物车总数为0时显示的提示页面
    }

}
