
package com.dashihui.afford.thirdapi.choosemenu;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dashihui.afford.R;
import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;

/**
 * 菜单控件头部，封装了下拉动画，动态生成头部按钮个数
 *
 * @author yueyueniao
 */

public class ExpandTabView extends LinearLayout implements OnDismissListener {

    private ToggleButton selectedButton;
    private ArrayList<String> mTextArray = new ArrayList<String>();
    private ArrayList<RelativeLayout> mViewArray = new ArrayList<RelativeLayout>();
    private ArrayList<ToggleButton> mToggleButton = new ArrayList<ToggleButton>();
    private Context mContext;
    private final int SMALL = 0;
    private int displayWidth;
    private int displayHeight;
    private PopupWindow popupWindow;
    private int selectPosition;
    private OnButtonClickListener mOnButtonClickListener;

    public ExpandTabView(Context context) {
        super(context);
        init(context);
    }

    public ExpandTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    /**
     * 根据选择的位置设置tabitem显示的颜色
     */
    public void setTitleColor(int color, int position) {
        if (position < mToggleButton.size()) {
            mToggleButton.get(position).setTextColor(color);
        }
    }

    /**
     * 根据选择的位置获取tabitem显示的颜色
     */
    public String getTitleColor(int position) {
        if (position < mToggleButton.size() && mToggleButton.get(position).getTextColors() != null) {
            return mToggleButton.get(position).getTextColors() + "";
        }
        return "";
    }

    /**
     * 根据选择的位置设置tabitem显示的值
     */
    public void setTitle(String valueText, int position) {
        if (position < mToggleButton.size()) {
            mToggleButton.get(position).setText(valueText);
        }
    }

    /**
     * 根据选择的位置获取tabitem显示的值
     */
    public String getTitle(int position) {
        if (position < mToggleButton.size() && mToggleButton.get(position).getText() != null) {
            return mToggleButton.get(position).getText().toString();
        }
        return "";
    }

    /**
     * 切换大实惠自营右边显示的图片
     *
     * @param i        0:未背选中 1:被选中
     * @param position
     */
    public void toggleDrawable(int i, int position) {
        if (position < mToggleButton.size()) {
            ToggleButton toggleButton = mToggleButton.get(position);
            if (i == 0) {
                Drawable drawable = mContext.getResources().getDrawable(R.drawable.store_nav_btn_self_nor);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                toggleButton.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            } else {
                Drawable drawable = mContext.getResources().getDrawable(R.drawable.store_nav_btn_self_clik);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                toggleButton.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            }
        }
    }

    /**
     * 切换标题价格箭头
     *
     * @param i        决定箭头方向
     * @param position 决定是哪个控件
     */
    public void togglePriceDrawable(int i, int position) {
        if (position < mToggleButton.size()) {
            ToggleButton toggleButton = mToggleButton.get(position);
            if (i == 3) {
                Drawable drawable = mContext.getResources().getDrawable(R.drawable.store_arrow_up);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                toggleButton.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            } else if (i == 4) {
                Drawable drawable = mContext.getResources().getDrawable(R.drawable.store_arrow_down_price);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                toggleButton.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            } else {
                Drawable drawable = mContext.getResources().getDrawable(R.drawable.store_arrow_dow);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                toggleButton.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            }
        }
    }

    /**
     * 设置tabitem的个数和初始值
     */
    public void setValue(ArrayList<String> textArray, ArrayList<View> viewArray) {
        if (mContext == null) {
            return;
        }

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewArray.clear();
        mTextArray.clear();
        mToggleButton.clear();
        mTextArray.addAll(textArray);
        for (int i = 0; i < viewArray.size(); i++) {
            final RelativeLayout rLayout = new RelativeLayout(mContext);
            int maxHeight = (int) (displayHeight * 0.7);
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, maxHeight);
            rl.leftMargin = 10;
            rl.rightMargin = 10;

            rLayout.addView(viewArray.get(i), rl);
            mViewArray.add(rLayout);
            rLayout.setTag(SMALL);
            ToggleButton tButton = (ToggleButton) inflater.inflate(R.layout.third_choose_toggle_button, this, false);
            addView(tButton);
            View line = new TextView(mContext);
            line.setBackgroundResource(R.drawable.line_gray);
            if (i < viewArray.size() - 1) {
                LayoutParams lp = new LayoutParams(2, 50);
                addView(line, lp);
            }
            //去掉销量的右边箭头
            if (i == 1) {
                tButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                tButton.setWidth((int) (displayWidth * 0.15));
            }
            //添加自营的右边图标
            if (i == 2) {
                Drawable drawable = getResources().getDrawable(R.drawable.store_nav_btn_self_nor);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tButton.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                tButton.setWidth((int) (displayWidth * 0.33));
            } else if (i == 3) {
                tButton.setWidth((int) (displayWidth * 0.22));
                Drawable drawable = getResources().getDrawable(R.drawable.store_arrow_dow);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tButton.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            } else if (i == 0) {
                tButton.setWidth((int) (displayWidth * 0.3));
            }
            mToggleButton.add(tButton);
            tButton.setTag(i);

            tButton.setText(mTextArray.get(i));

            LogUtils.e("onRefresh==========mTextArray======kk=======>" + mTextArray.get(i) + i);

            rLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPressBack();
                }
            });

            rLayout.setBackgroundColor(mContext.getResources().getColor(R.color.popup_main_background));
            tButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    // initPopupWindow();
                    ToggleButton tButton = (ToggleButton) view;

                    if (selectedButton != null && selectedButton != tButton) {
                        selectedButton.setChecked(false);
                    }
                    selectedButton = tButton;
                    selectPosition = (Integer) selectedButton.getTag();
                    if (selectPosition == 0) {
                        startAnimation();
                        if (mOnButtonClickListener != null && tButton.isChecked()) {
                            mOnButtonClickListener.onClick(selectPosition);
                        }
                    } else {
                        if (mOnButtonClickListener != null) {
                            mOnButtonClickListener.onClick(selectPosition);
                        }
                        LogUtils.e("onRefresh==========selectPosition======kk=======>" + selectPosition);
                    }

                }
            });
        }
    }

    /**
     * 隐藏popupWindow
     */
    public void dismisPopup() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            hideView();
        }
    }

    private void startAnimation() {

        if (popupWindow == null) {
            popupWindow = new PopupWindow(mViewArray.get(selectPosition), displayWidth, displayHeight);
            popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
            popupWindow.setFocusable(false);
            popupWindow.setOutsideTouchable(true);
        }

        if (selectedButton.isChecked()) {
            if (!popupWindow.isShowing()) {
                showPopup(selectPosition);
            } else {
                popupWindow.setOnDismissListener(this);
                popupWindow.dismiss();
                hideView();
            }
        } else {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
                hideView();
            }
        }
    }

    private void showPopup(int position) {
        View tView = mViewArray.get(selectPosition).getChildAt(0);
        if (tView instanceof ViewBaseAction) {
            ViewBaseAction f = (ViewBaseAction) tView;
            f.show();
        }
        if (popupWindow.getContentView() != mViewArray.get(position)) {
            popupWindow.setContentView(mViewArray.get(position));
        }
        popupWindow.showAsDropDown(this, 0, 0);
    }

    /**
     * 如果菜单成展开状态，则让菜单收回去
     */
    public boolean onPressBack() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            hideView();
            if (selectedButton != null) {
                selectedButton.setChecked(false);
            }
            return true;
        } else {
            return false;
        }

    }

    private void hideView() {
        View tView = mViewArray.get(selectPosition).getChildAt(0);
        if (tView instanceof ViewBaseAction) {
            ViewBaseAction f = (ViewBaseAction) tView;
            f.hide();
        }
    }

    private void init(Context context) {
        mContext = context;
        displayWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
        displayHeight = ((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight();
        setOrientation(LinearLayout.HORIZONTAL);
    }

    @Override
    public void onDismiss() {
        showPopup(selectPosition);
        popupWindow.setOnDismissListener(null);
    }


    /**
     * 设置tabitem的点击监听事件
     */
    public void setOnButtonClickListener(OnButtonClickListener l) {
        mOnButtonClickListener = l;
    }


    /**
     * 自定义tabitem点击回调接口
     */
    public interface OnButtonClickListener {
        public void onClick(int selectPosition);
    }

}
