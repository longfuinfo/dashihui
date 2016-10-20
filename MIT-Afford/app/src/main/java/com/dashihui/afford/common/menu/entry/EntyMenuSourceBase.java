package com.dashihui.afford.common.menu.entry;


import com.dashihui.afford.ui.activity.AtyHome;
import com.dashihui.afford.ui.activity.AtyShoppingCart;
import com.dashihui.afford.ui.activity.my.AtyMy;
import com.dashihui.afford.ui.activity.server.AtyServer;
import com.dashihui.afford.ui.activity.shop.AtyAffordShop;

public abstract class EntyMenuSourceBase {


    public EntyMenuSourceBase() {
        super();
    }

    /**
     * 返回底部菜单名称数组
     *
     * @return
     */
    protected abstract String[] returnMenuName();

    /**
     * 返回底部菜单图片资源数组
     *
     * @return
     */
    protected abstract int[] returnMenuid();

    /**
     * 返回底部背景颜色数组
     *
     * @return
     */
    protected abstract int[] returnBgRes();

    /**
     * 点击后底部菜单按钮的背景颜色
     *
     * @return
     */
    public abstract int getSelectedBgdrawable();

    /**
     * 点击后底部菜单按钮的背景颜色
     *
     * @return
     */
    public abstract int getSelectedBgdrawable(int index);

    /**
     * 返回底部文字颜色颜色数组
     *
     * @return
     */
    protected abstract int[] returnFontColor();

    /**
     * 点击后底部按钮字体的颜色
     *
     * @return
     */
    public abstract int getSelectedFontdrawable();


    public String[] getMenuName() {
        return returnMenuName();
    }

    /**
     * 返回底部菜单默认图片的集合
     *
     * @return
     */
    public int[] getMenuid() {
        return returnMenuid();
    }

    /**
     * 返回底部菜单背景的集合
     *
     * @return
     */
    public int[] getBgRes() {
        return returnBgRes();
    }

    /**
     * 返回底部菜单文字的集合
     *
     * @return
     */
    public int[] getFontColor() {
        return returnFontColor();
    }

    public Class<?>[] getActivities() {
        Class<?>[] class1 = {AtyHome.class, AtyAffordShop.class, AtyServer.class, AtyShoppingCart.class, AtyMy.class};
        return class1;

    }


}
