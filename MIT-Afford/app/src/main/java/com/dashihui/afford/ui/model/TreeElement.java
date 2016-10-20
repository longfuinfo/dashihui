package com.dashihui.afford.ui.model;

import com.dashihui.afford.thirdapi.greedsqlite.ShoppingCart;


/**
 * Created by lwj on 2015/5/28.
 */
public class TreeElement {



    /**
     * 购物车商品信息
     */
    private ShoppingCart mShopingCart;


    /**
     * 本身是否已经选中
     */
    private boolean isChoosed = false;

    /**
     * 是否是自营的
     */
    private int isSelf;//是否是自营商品：0：不是，1：是。

    /**
     * 是否显示
     */
    private boolean isVisible= false;

    /**
     *
     * @return
     */





    public ShoppingCart getmShopingCart() {
        return mShopingCart;
    }

    public void setmShopingCart(ShoppingCart mShopingCart) {
        this.mShopingCart = mShopingCart;
    }


    public int getIsSelf() {
        return isSelf;
    }

    public void setIsSelf(int isSelf) {
        this.isSelf = isSelf;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setIsChoosed(boolean isChoosed) {
        this.isChoosed = isChoosed;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }
}
