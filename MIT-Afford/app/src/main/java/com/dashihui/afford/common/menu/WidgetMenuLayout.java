package com.dashihui.afford.common.menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.common.menu.entry.EntyMenuSource;
import com.dashihui.afford.common.menu.entry.EntyMenuSourceBase;
import com.dashihui.afford.sqlite.SqliteShoppingCart;
import com.dashihui.afford.thirdapi.greedsqlite.ShoppingCart;
import com.dashihui.afford.ui.activity.AtyLocation;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.number.UtilNumber;
import com.dashihui.afford.util.screen.UtilScreen;
import com.lidroid.xutils.util.LogUtils;
import com.meizu.flyme.reflect.ActionBarProxy;

import java.util.List;

/**
 * @author NiuFC
 * @version V1.0
 * @ClassName: WigBotMenuLayout
 * @Description: 底部菜单
 * @date 2014年3月28日 下午4:37:04
 * @维护人:
 */
public class WidgetMenuLayout extends LinearLayout {

    private final static String TAG = "WidgetBottomMenuLayout";
    // 该值需要根据按钮背景图片来调整。
    private final static float bottom_layoutHeight = 76;
    // 实例化layout使用的类
    private LayoutInflater mInflater;
    private Context mContext;
    // 保存菜单按钮的集合，每一个集合元素代表一个按钮，包含了按钮所需要的信息：图片，文字，按键处理事件。
    private List<WidgetMenuBtn> bottomButtons;
    // 封装菜单按钮的布局。
    private View bottomMenuLayout;
    private LinearLayout menuLayout01,menuLayout02,menuLayout03,menuLayout04,menuLayout05;
    //图片背景
    private ImageView menuImg01,menuImg02,menuImg03,menuImg04,menuImg05;
    //底部名称
    private TextView menuTxtViewName01,menuTxtViewName02,menuTxtViewName03,menuTxtViewName04,menuTxtViewName05;
    //右上角数字
    private TextView menuView01,menuView02,menuView03,menuView04,menuView05;
    private WidgetMenuLayout.CountViewBoadcast mBoadcast;

    public WidgetMenuLayout(Context context) {
        super(context);
        this.mContext = context;
        if (mBoadcast ==null){
            mBoadcast = new WidgetMenuLayout.CountViewBoadcast();
        }
    }

    public WidgetMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public void processInitButton() {
        // 初始化布局，将底部菜单layout加入到视图中。
        initLayout(this.getContext());
        // 绑定每一个菜单按钮
        bindingButton();
        // 重新计算整个布局的大小，使用整个屏幕的高度减去底部菜单的高度，
        // 得出并设置中间页面部分的高度，就能够将菜单固定在底部。
        resizeLayout();
    }

    /**
     * 初始化布局 TODO(这里用一句话描述这个方法的作用)
     *
     * @param context 设定文件
     * @return void 返回类型
     * @Title: initLayout
     * @author 牛丰产
     * @date 2015年1月12日 下午2:20:09
     * @维护人:
     * @version V1.0
     */
    private void initLayout(Context context) {
        LogUtils.e("initLayout========WidgetMenuLayout=======初始化布局===>");
        this.mInflater = LayoutInflater.from(context);
        bottomMenuLayout = mInflater.inflate(R.layout.widget_menu, null);
        menuLayout01 = (LinearLayout)bottomMenuLayout.findViewById(R.id.menuLayout01);
        menuLayout02 = (LinearLayout)bottomMenuLayout.findViewById(R.id.menuLayout02);
        menuLayout03 = (LinearLayout)bottomMenuLayout.findViewById(R.id.menuLayout03);
        menuLayout04 = (LinearLayout)bottomMenuLayout.findViewById(R.id.menuLayout04);
        menuLayout05 = (LinearLayout)bottomMenuLayout.findViewById(R.id.menuLayout05);

        menuImg01 = (ImageView)bottomMenuLayout.findViewById(R.id.menuImg01);
        menuImg02 = (ImageView)bottomMenuLayout.findViewById(R.id.menuImg02);
        menuImg03 = (ImageView)bottomMenuLayout.findViewById(R.id.menuImg03);
        menuImg04 = (ImageView)bottomMenuLayout.findViewById(R.id.menuImg04);
        menuImg05 = (ImageView)bottomMenuLayout.findViewById(R.id.menuImg05);

        menuTxtViewName01 = (TextView)bottomMenuLayout.findViewById(R.id.menuTxtViewName01);
        menuTxtViewName02 = (TextView)bottomMenuLayout.findViewById(R.id.menuTxtViewName02);
        menuTxtViewName03 = (TextView)bottomMenuLayout.findViewById(R.id.menuTxtViewName03);
        menuTxtViewName04 = (TextView)bottomMenuLayout.findViewById(R.id.menuTxtViewName04);
        menuTxtViewName05 = (TextView)bottomMenuLayout.findViewById(R.id.menuTxtViewName05);


        menuView01 = (TextView)bottomMenuLayout.findViewById(R.id.menuView01);
        menuView02 = (TextView)bottomMenuLayout.findViewById(R.id.menuView02);
        menuView03 = (TextView)bottomMenuLayout.findViewById(R.id.menuView03);
        menuView04 = (TextView)bottomMenuLayout.findViewById(R.id.menuView04);
        menuView05 = (TextView)bottomMenuLayout.findViewById(R.id.menuView05);

        addView(bottomMenuLayout);
        mContext = context;
    }

    /**
     * 根据横竖屏调整高度 TODO(这里用一句话描述这个方法的作用)
     *
     * @return void 返回类型
     * @Title: resizeLayout 设定文件
     * @author 牛丰产
     * @date 2015年1月12日 下午2:20:50
     * @维护人:
     * @version V1.0
     */
    private void resizeLayout() {
        View customView = getChildAt(0);
        android.view.ViewGroup.LayoutParams params = customView.getLayoutParams();
        int screenHeight;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { // 横屏
            screenHeight = (int) UtilScreen.getScreenHeight();
            // 横屏不用考虑底部
            params.height = screenHeight;
            params.width = (int) UtilScreen.getScreenWidth();
            customView.setLayoutParams(params);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) { // 竖屏
            screenHeight = (int) UtilScreen.getScreenHeight();
            // 菜单上面内容高度
            float contentHeight = getContentHeight(screenHeight);
            params.height = (int) contentHeight;
            params.width = (int) UtilScreen.getScreenWidth();
            customView.setLayoutParams(params);
        }
    }

    private float getContentHeight(int screenHeight) {
        //需要对魅族手机做出适配,要判断是否是魅族的手机并取得有没有smartbar
        float contentHeight = 0L;
        boolean hasSmartBar = ActionBarProxy.hasSmartBar();
        if (hasSmartBar) {
            //魅族的smartbar高度为48dip
            contentHeight = screenHeight - UtilScreen.dpToPx(bottom_layoutHeight + 48);
        } else {
            contentHeight = screenHeight - UtilScreen.dpToPx(bottom_layoutHeight);
        }
        return contentHeight;
    }

    /**
     * @return void    返回类型
     * @Title: bindingButton
     * @Description: 绑定菜单按钮的单击事件
     * @author 吕军伟
     * @date 2015年4月7日 下午1:36:07
     * @评审人:
     * @维护人:
     * @version V1.0
     */
    private void bindingButton() {
        LogUtils.e("bindingButton========WidgetMenuLayout=======初始化布局中的按钮===>");
        int shopCartNum = 0;
        if(AffordApp.getInstance().getEntityLocation()!=null && AffordApp.getInstance().getEntityLocation().getSTORE()!=null){
            List<ShoppingCart> shopCartList = SqliteShoppingCart.getInstance(mContext).getListByShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
            if (!UtilList.isEmpty(shopCartList)){
                for (int k =0;k<shopCartList.size();k++){
                    shopCartNum += UtilNumber.IntegerValueOf(shopCartList.get(k).getBuynum());
                }

            }
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.dashihui.afford.ACTION_COUNT");

        mContext.registerReceiver(mBoadcast,intentFilter);
        LogUtils.e("购物车总数量===============>" + shopCartNum);
        if (this.bottomButtons != null && this.bottomButtons.size() > 0) {
            initViewClick(menuLayout01,menuImg01,menuTxtViewName01,menuView01,bottomButtons.get(0),0,shopCartNum);

            initViewClick(menuLayout02,menuImg02,menuTxtViewName02,menuView02,bottomButtons.get(1),1,shopCartNum);

            initViewClick(menuLayout03,menuImg03,menuTxtViewName03,menuView03,bottomButtons.get(2),2,shopCartNum);

            initViewClick(menuLayout04,menuImg04,menuTxtViewName04,menuView04,bottomButtons.get(3),3,shopCartNum);

            initViewClick(menuLayout05,menuImg05,menuTxtViewName05,menuView05,bottomButtons.get(4),4,shopCartNum);

        }
    }


    public void unregisterReceiver(){
        if (mContext != null && mBoadcast != null){
            mContext.unregisterReceiver(mBoadcast);
        }
    }

    /**
     * 底部菜单初始化
     * @param layout
     * @param img
     * @param txtViewName
     * @param txtViewCount
     * @param oneButton
     * @param position
     * @param shopCartNum
     */
    private void initViewClick(LinearLayout layout,ImageView img,TextView txtViewName,
                               TextView txtViewCount,WidgetMenuBtn oneButton,final int position,int shopCartNum){
        txtViewName.setTextColor(oneButton.getFontColor());
        // 每个按钮图标图像
        img.setImageResource(oneButton.getImageResource());

        // 底部一个整体按钮单元 的背景
        if (position!=3){//
            txtViewCount.setVisibility(View.GONE);
        }else {
            if (shopCartNum ==0){
                txtViewCount.setVisibility(View.GONE);
            }else {
                txtViewCount.setVisibility(View.VISIBLE);
                txtViewCount.setText(shopCartNum + "");
            }
        }
        layout.setBackgroundResource(oneButton.getBackgroundResource());
        // 设置监听事件。
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 首页、便利店、服务、购物车、我的--
                startActity(position);
            }
        });
    }

    /**
     *
     */
    public class CountViewBoadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int shopNum = intent.getIntExtra("data",0);
            if (shopNum ==0){
                menuView04.setVisibility(View.GONE);
            }else {
                menuView04.setVisibility(View.VISIBLE);
                menuView04.setText(shopNum + "");
            }
        }
    }
    /**
     * 根据不同的交易模式执行不同的展示 TODO(这里用一句话描述这个方法的作用)
     *
     * @return void 返回类型
     * @Title: startActity
     * @author 牛丰产
     * @date 2015年1月12日 下午1:51:27
     * @维护人:
     * @version V1.0
     */
    private void startActity(int position) {

        if (AffordApp.getInstance().getEntityLocation() != null && AffordApp.getInstance().getEntityLocation().getCOMMUNITY() != null) {
            Intent _intent = new Intent();
            EntyMenuSourceBase source = new EntyMenuSource();
            _intent.setClass(mContext, source.getActivities()[position]);
            //_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // 重用堆栈里的已经启动activity
           _intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            mContext.startActivity(_intent);
        } else {
            Intent _intent1 = new Intent(mContext,AtyLocation.class);
            mContext.startActivity(_intent1);
        }

    }

    public void setButtonList(List<WidgetMenuBtn> bottomButtons) {
        this.bottomButtons = bottomButtons;
    }
}
