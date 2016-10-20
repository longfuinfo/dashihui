package com.dashihui.afford.common.menu.entry;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;

public class EntyMenuSource extends EntyMenuSourceBase {
    private final static String TAG = "EntyMenuSource";

    private AffordApp mAffordApp;

    public EntyMenuSource() {
        super();
        mAffordApp = AffordApp.getInstance();
    }

    @Override
    protected String[] returnMenuName() {
        String[] menuName = {mAffordApp.getResources().getString(R.string.home),
                mAffordApp.getResources().getString(R.string.affordShop),
                mAffordApp.getResources().getString(R.string.service),
                mAffordApp.getResources().getString(R.string.shoppingCart),
                mAffordApp.getResources().getString(R.string.my),};
        return menuName;
    }

    @Override
    protected int[] returnMenuid() {
        int[] menuid = {R.drawable.menu_bot01, R.drawable.menu_bot02, R.drawable.menu_bot03, R.drawable.menu_bot04, R.drawable.menu_bot05};
        return menuid;
    }

    @Override
    protected int[] returnBgRes() {
        int[] bgRes = {R.color.transparent,
                R.color.transparent,
                R.color.transparent,
                R.color.transparent,
                R.color.transparent};
        return bgRes;
    }

    @Override
    public int getSelectedBgdrawable() {
        // TODO Auto-generated method stub
        //选中菜单item背景图标
        return R.color.transparent;
    }

    @Override
    public int getSelectedBgdrawable(int index) {
        // TODO Auto-generated method stub
        //选中菜单图标
        return returnMenuActiveid()[index];
    }

    protected int[] returnMenuActiveid() {
        int[] menuid = {R.drawable.menu_bot11, R.drawable.menu_bot12,
                R.drawable.menu_bot13, R.drawable.menu_bot14, R.drawable.menu_bot15};
        return menuid;
    }

    @Override
    public int getSelectedFontdrawable() {
        // TODO Auto-generated method stub
        //选中是字体菜单背景颜色
        return mAffordApp.getResources().getColor(R.color.menu_selected);
    }

    @Override
    protected int[] returnFontColor() {
        int[] fontColor = {mAffordApp.getResources().getColor(R.color.menu_pressed),
                mAffordApp.getResources().getColor(R.color.menu_pressed),
                mAffordApp.getResources().getColor(R.color.menu_pressed),
                mAffordApp.getResources().getColor(R.color.menu_pressed),
                mAffordApp.getResources().getColor(R.color.menu_pressed)};
        return fontColor;
    }


}
