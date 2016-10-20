package com.dashihui.afford.ui.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessShop;
import com.dashihui.afford.business.entity.EtyList;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.sqlite.SqliteServiceSearch;
import com.dashihui.afford.sqlite.SqliteShoppingCart;
import com.dashihui.afford.thirdapi.greedsqlite.Search;
import com.dashihui.afford.thirdapi.greedsqlite.ShoppingCart;
import com.dashihui.afford.ui.adapter.AdapterSearch;
import com.dashihui.afford.util.list.UtilList;
import com.dashihui.afford.util.string.UtilString;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 应用搜索界面
 *
 * @author 张博
 * @version V1.0
 * @ClassName: AtyAppsSearch
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2014-11-26 下午5:00:25
 * @维护人:
 */
public class AtySearch extends BaseActivity {

    /**
     * 搜索相关界面跳转时的intent的key
     */
    public static final String INTENT_KEY_APPNAME = "intent_key_appname";
    public static final String INTENT_KEY_KEYID = "intent_key_keyid";
    public static final String INTENT_KEY_KEY = "intent_key_key";
    public static final String INTENT_KEY_TRADE_NAME = "intent_key_trade_name";
    public static final String INTENT_KEY_TEXT = "intent_key_text";
    /** 搜索相关界面跳转时的intent的key */

    /**
     * 搜索相关界面跳转时的intent的value
     */
    public static final String INTENT_VALUE_AREA = "AREA";
    public static final String INTENT_VALUE_TRADE = "TRADE";
    /** 搜索相关界面跳转时的intent的value */

    /**
     * 分页查询条数
     */
    public final static String QUERY_COUNT = "10";// 查询条数
    /**
     * 搜索类型
     */
    public static final int SEARCH_TYPE_APPNAME = 0;
    public static final int SEARCH_TYPE_TRADENAME = 1;
    public static final int SEARCH_TYPE_AREAID = 2;
    public static final int SEARCH_TYPE_TRADEID = 3;


    /**
     * 返回按钮，编辑框，搜索按钮，取消按钮
     */
    @ViewInject(R.id.apps_search_back_ibt)
    private ImageButton mBackIbt;
    @ViewInject(R.id.txt_zonghe)
    private TextView mTxtZonghe;
    //    @ViewInject(R.id.txt_sell)
//    private TextView mTxtSell;
    @ViewInject(R.id.txt_price)
    private TextView mTxtprice;
    @ViewInject(R.id.txt_self)
    private TextView mTxtSelf;
    @ViewInject(R.id.apps_search_edt)
    private EditText mEditText;
    @ViewInject(R.id.apps_search_img)
    private ImageView mSearchImg;
    @ViewInject(R.id.apps_search_cancle_bt)
    private Button mCancleBt;
    @ViewInject(R.id.rlyt_nohistory)
    private RelativeLayout mRlytNoHistory;
    @ViewInject(R.id.lyt_search_title)
    private LinearLayout mLytSearchTitle;

    @ViewInject(R.id.iv_add_cart)
    private ImageView mShopCart;//底部购物车
    @ViewInject(R.id.nums)
    private TextView mTvNums;//购物车小数字
    private int mNum = 0;
    private int mPress = 1;//记录是否被点击 1:综合 3:价格低到高 4:价格高到低
    private int mSelect = 0;//记录是否被选中 大实惠直营 0:否  1:是
    private int searchType = 1;//排序类型

    private ViewGroup anim_mask_layout;//动画层

    private boolean isFirst = false;
    /**
     * 分类列表
     */
    private ListView mClassifyLv;//
    private ClassifyAdapter mClassifyAdapter;//

    /**
     * 历史列表
     */
    private ListView mHistoryLv;//
    private HistoryAdapter mHistoryAdapter;//
    private ArrayList<Search> mHistoryList = new ArrayList<Search>();
    private View mHistoryFootview;

    /**
     * 结果列表
     */
    private ListView mResultLv;
    private AdapterSearch mResultAdapter;
    private List<Map<String, Object>> mResultList;

//	private BLLInformation mBll;
    /**
     * 输入的搜索文字
     */
    private String mSearchAppName;

    private Activity mContext;
    private BusinessShop mBllShop;
    private SqliteServiceSearch mDbServiceSearch;

    private boolean mLoadMore = false;// 是否为加载更多，如果是上拉操作，则为true
    private List<ShoppingCart> mShopCartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_search);
        ViewUtils.inject(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mShopCart.setVisibility(View.GONE);
        mTvNums.setVisibility(View.GONE);
        mLytSearchTitle.setVisibility(View.GONE);


        //进入页面展示搜索历史
        if (mHistoryLv.isShown()) {
            return;
        }
        if (mResultList.size() > 0) {
            showResultLv();
        } else {
            showHistoryLv();
        }
        showHistory(mEditText.getText().toString());

        //获取搜索框的焦点及弹出软键盘
        mEditText.setFocusable(true);
        mEditText.setFocusableInTouchMode(true);
        mEditText.requestFocus();


        //延时300毫秒待页面加载完毕后显示软键盘
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager = (InputMethodManager) mEditText
                                       .getContext().getSystemService(AtySearch.this
                                               .INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(mEditText, 0);
                           }
                       },
                300);
    }

    /**
     * 初始化视图
     */
    protected void initView() {
        mShopCart.setVisibility(View.GONE);

        if (mNum > 0) {
            mTvNums.setVisibility(View.VISIBLE);

        } else {
            mTvNums.setVisibility(View.GONE);
        }
        mContext = AtySearch.this;
        mBllShop = new BusinessShop(this);
        mDbServiceSearch = SqliteServiceSearch.getInstance(mContext);

        mEditText.addTextChangedListener(mTextWatcher);
        mEditText.setOnEditorActionListener(onEditorActionListener);
        //默认选中综合
        mTxtZonghe.setTextColor(getResources().getColor(R.color.search_press));

        initListView();// 初始化listview
        showClassifyLv();// 展示分类列表
    }

    /**
     * 展示分类列表
     */
    private void showClassifyLv() {
        mClassifyLv.setVisibility(View.GONE);
        mHistoryLv.setVisibility(View.GONE);
        mResultLv.setVisibility(View.GONE);
        mCancleBt.setVisibility(View.GONE);
        mRlytNoHistory.setVisibility(View.GONE);
        mLytSearchTitle.setVisibility(View.GONE);
    }

    /**
     * 展示没有搜索结果
     */
    private void showNoHistory() {
        mClassifyLv.setVisibility(View.GONE);
        mHistoryLv.setVisibility(View.GONE);
        mResultLv.setVisibility(View.GONE);
        mCancleBt.setVisibility(View.GONE);
        mRlytNoHistory.setVisibility(View.VISIBLE);
        mLytSearchTitle.setVisibility(mLytSearchTitle.getVisibility());

    }

    /**
     * 展示历史列表
     */
    private void showHistoryLv() {
        mClassifyLv.setVisibility(View.GONE);
        mHistoryLv.setVisibility(View.VISIBLE);
        mResultLv.setVisibility(View.GONE);
        mCancleBt.setVisibility(View.VISIBLE);
        mRlytNoHistory.setVisibility(View.GONE);
        mLytSearchTitle.setVisibility(View.GONE);
    }

    /**
     * 展示历史和结果
     */
    private void showHistoryAndResult() {
        mClassifyLv.setVisibility(View.GONE);
        mHistoryLv.setVisibility(View.VISIBLE);
        mResultLv.setVisibility(View.VISIBLE);
        mCancleBt.setVisibility(View.VISIBLE);
        mRlytNoHistory.setVisibility(View.GONE);
        mLytSearchTitle.setVisibility(View.GONE);
    }

    /**
     * 展示结果列表
     */
    private void showResultLv() {
        mClassifyLv.setVisibility(View.GONE);
        mHistoryLv.setVisibility(View.GONE);
        mResultLv.setVisibility(View.VISIBLE);
        mCancleBt.setVisibility(View.VISIBLE);
        mRlytNoHistory.setVisibility(View.GONE);
        mShopCart.setVisibility(View.VISIBLE);
        mLytSearchTitle.setVisibility(View.VISIBLE);
        showShopCartNum();
    }

    /**
     * 查询购物车商品数量
     */
    public void showShopCartNum() {
        mShopCartList = SqliteShoppingCart.getInstance(AtySearch.this)
                .getListByShopID(AffordApp.getInstance().getEntityLocation().getSTORE().getID() + "");
        if (mShopCartList.size() > 0) {
            mTvNums.setVisibility(View.VISIBLE);
            mTvNums.setText(mShopCartList.size() + "");
        } else {
            mTvNums.setVisibility(View.GONE);
        }
    }

    /**
     * 列表初始化
     */
    private void initListView() {

        mClassifyLv = (ListView) findViewById(R.id.apps_search_classify_lv);
        mClassifyAdapter = new ClassifyAdapter();
        mClassifyLv.setAdapter(mClassifyAdapter);
//		mClassifyLv.setOnItemClickListener(classifyItemClickListener);

        mHistoryLv = (ListView) findViewById(R.id.apps_search_history_lv);
        mHistoryAdapter = new HistoryAdapter();
        mHistoryLv.setAdapter(mHistoryAdapter);
        mHistoryLv.setOnItemClickListener(historyItemClickListener);
        mHistoryFootview = mContext.getLayoutInflater().inflate(R.layout
                .aty_search_history_lv_item, null);

        mResultLv = (ListView) findViewById(R.id.apps_search_result_lv);
        mResultList = new ArrayList<Map<String, Object>>();
        mResultAdapter = new AdapterSearch(mContext, mResultList);
        setEmptyView(mResultLv);
        mResultLv.setAdapter(mResultAdapter);
        // 设置上下刷新
//		mResultLv.setMode(Mode.BOTH);
//		mResultLv.setOnRefreshListener(new OnRefreshListener2<ListView>() {
//			@Override
//			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//				mLoadMore = false;
//				requestSearchData();
//			}
//
//			@Override
//			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//				mLoadMore = true;
//				requestSearchData();
//			}
//		});

    }

    private void setEmptyView(ListView resultLv) {
        TextView tv = new TextView(mContext);
        tv.setGravity(Gravity.CENTER);
        tv.setText("没有找到相关内容");
        mResultLv.setEmptyView(tv);
    }

    /**
     * 按钮点击事件监听器
     */
    @OnClick({R.id.apps_search_back_ibt, R.id.apps_search_edt, R.id.apps_search_img, R.id
            .apps_search_cancle_bt, R.id.txt_self, R.id.txt_zonghe, R.id.txt_price})
    public void onListenerClick(View v) {
        switch (v.getId()) {
            case R.id.apps_search_back_ibt:// 返回
                onBackPressed();
                break;
            case R.id.apps_search_edt://
                if (mHistoryLv.isShown()) {
                    return;
                }
                if (!isFirst) {
                    isFirst = true;
                    showHistoryLv();
                } else {
                    showHistoryAndResult();
                }
                showHistory(mEditText.getText().toString());
                break;
            case R.id.apps_search_img:
                doSearch();
                break;
            case R.id.apps_search_cancle_bt:// 取消
                // 如果当前显示的是历史记录
                if (mHistoryLv.isShown()) {
                    // 先判断结果列表是否为空
                    if (mResultList != null && mResultList.size() > 0) {
                        // 如果不为空，判断输入框是否有文字
                        if (mEditText.getText().toString().trim().equals("")) {
                            // 如果文字被删掉了，返回默认分类页，并清楚结果数据
                            mResultList.clear();
                            showClassifyLv();
                        } else {
                            // 如果文字没被删光，仍返回到刚才的结果列表页
                            showResultLv();
                            mBaseUtilAty.hideWindowSoftInput();
                            return;
                        }
                    } else {
                        // 如果为空，就是说没有搜索到记录，返回默认分类页
                        showClassifyLv();
                    }
                } else if (mResultLv.isShown()) {
                    // 如果当前是结果页，直接返回到默认分类页
                    showClassifyLv();
                }
                v.setVisibility(View.GONE);
                mBaseUtilAty.hideWindowSoftInput();
                break;
            case R.id.txt_zonghe:
                mPress = 1;
                mSelect = 2;

                Drawable drawable = getResources().getDrawable(R.drawable.store_arrow_dow);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable
                        .getIntrinsicHeight());
                mTxtprice.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                mTxtZonghe.setTextColor(getResources().getColor(R.color.search_press));
                searchType = 1;
                doSearch();
                //mTxtSell.setTextColor(getResources().getColor(R.color.search_black));
                mTxtprice.setTextColor(getResources().getColor(R.color.search_black));

                break;
//            case R.id.txt_sell:
//                mPress = 2;
//                mTxtZonghe.setTextColor(getResources().getColor(R.color.search_black));
//                mTxtSell.setTextColor(getResources().getColor(R.color.search_press));
//                mTxtprice.setTextColor(getResources().getColor(R.color.search_black));
//
//                break;
            case R.id.txt_self:
                if (mSelect == 0) {
                    mSelect = 1;
                    setDrawable(mSelect, mTxtSelf);
                    mTxtSelf.setTextColor(getResources().getColor(R.color.search_press));
                    doSearch();
                } else {
                    mSelect = 0;
                    setDrawable(mSelect, mTxtSelf);
                    doSearch();
                    mTxtSelf.setTextColor(getResources().getColor(R.color.search_black));
                }
                break;
            case R.id.txt_price:
                if (mPress != 3) {
                    mPress = 3;
                    setDrawable(mPress, mTxtprice);
                    mTxtprice.setTextColor(getResources().getColor(R.color.search_press));
                    searchType = 2;
                    doSearch();
                } else {
                    mPress = 4;
                    setDrawable(mPress, mTxtprice);
                    searchType = 3;
                    doSearch();
                }
                mTxtZonghe.setTextColor(getResources().getColor(R.color.search_black));

                // mTxtSell.setTextColor(getResources().getColor(R.color.search_black));
                break;
            default:
                break;
        }
    }

    //设置图片
    private void setDrawable(int i, TextView t) {
        Drawable drawable = null;
        switch (i) {
            case 0://直营 未选
                drawable = getResources().getDrawable(R.drawable.store_nav_btn_self_nor);
                LogUtils.e("onSuccess===中和===========>" + 0);
                break;
            case 1:
                drawable = getResources().getDrawable(R.drawable.store_nav_btn_self_clik);
                LogUtils.e("onSuccess===中和===========>" + 1);
                break;
            case 4:
                drawable = getResources().getDrawable(R.drawable.store_arrow_down_price);
                LogUtils.e("onSuccess===中和===========>" + 4);
                break;
            case 3:
                drawable = getResources().getDrawable(R.drawable.store_arrow_up);
                LogUtils.e("onSuccess===中和===========>" + 3);
                break;
//            case 2:
//                drawable = getResources().getDrawable(R.drawable.store_arrow_up_price);
//                LogUtils.e("onSuccess===中和===========>" + 2);

        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable
                .getIntrinsicHeight());
        t.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
    }


    /**
     * 分类点击监听器
     */
//	AdapterView.OnItemClickListener classifyItemClickListener = new AdapterView
// .OnItemClickListener() {
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//			if (mIntent == null) {
//				mIntent = new Intent(AtySearch.this, AtyAffordShopDetail.class);
//			}
//			mIntent.putExtra(AtyAffordShop.INTENT_SHOP_ID, mResultList.get(position).get("ID") +
// "");
//			startActivity(mIntent);
//		}
//	};

    /**
     * 历史记录点击监听器
     */
    AdapterView.OnItemClickListener historyItemClickListener = new AdapterView
            .OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView tv = (TextView) view.findViewById(R.id.apps_search_history_item_tv);
            if (mHistoryLv.getFooterViewsCount() > 0) {// 如果有footview，则需要额外判断点击的位置是不是footview
                if (position == mHistoryLv.getCount() - 1) {
                    // 判断点击的是不是footview
                    if (tv.getText().equals("清除历史记录")) {
                        // 根据footview中的文字判断要执行的操作
                        mDbServiceSearch.deleteAll();
                        showHistory("");
                        return;
                    } else if (tv.getText().equals("没有历史记录")) {
                        return;
                    }
                }
            }
            mEditText.setText(tv.getText().toString());
            Editable etext = mEditText.getText();
            int pos = etext.length();
            Selection.setSelection(etext, pos);
            doSearch();

        }
    };


    /**
     * 查询历史记录并刷新列表
     */
    private void showHistory(String query) {
        if (mHistoryLv.getFooterViewsCount() > 0) {
            mHistoryLv.removeFooterView(mHistoryFootview);
        }
        TextView tv = (TextView) mHistoryFootview.findViewById(R.id.apps_search_history_item_tv);
        mHistoryFootview.findViewById(R.id.apps_search_history_item_delete_bt).setVisibility(View
                .GONE);
        tv.setGravity(Gravity.CENTER);
        mHistoryList = (ArrayList<Search>) mDbServiceSearch.getSearchList(query);
        if (mHistoryList != null && mHistoryList.size() > 0) {
            // 如果有历史记录
            tv.setText("清除历史记录");
            // tv.setTextColor(getResources().getColor(R.color.graphite_grey));
            // mFootview.setBackgroundResource(R.drawable.bg_search_listview_selector);
        } else {
            // 如果没有历史记录
            tv.setText("没有历史记录");
            // tv.setTextColor(getResources().getColor(R.color.squirrel_grey));
            // mFootview.setBackgroundResource(R.drawable.white_item_bg);
        }
        mHistoryLv.addFooterView(mHistoryFootview);
        mHistoryLv.setAdapter(mHistoryAdapter);
        mHistoryAdapter.notifyDataSetChanged();
    }

    /**
     * 搜索
     */
    private void doSearch() {
        String searchKey = mEditText.getText().toString();
        if (!UtilString.isEmpty(searchKey)) {// 如果搜索字符串为""或者去空后为""
            // 如果输入的是一堆空格，不让搜索
            if (searchKey.length() < 1) {
                Toast.makeText(mContext, "关键字不能为空", Toast.LENGTH_SHORT).show();
            } else {
                mDbServiceSearch.addOne(searchKey);
                mSearchAppName = searchKey;
                requestSearchData(searchKey, searchType + "", mSelect + "");
            }
        } else {
            Toast.makeText(mContext, "请输入要搜索的关键字", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 发起搜索请求
     */
    private void requestSearchData(String searchKey, String orderby, String isself) {
        if (mBaseUtilAty.isNetworkAvailable(this)) {
            showProDialog(this);
            mBllShop.doSearch("", searchKey, 1 + "", orderby, isself);
        } else {
            mBaseUtilAty.ShowMsg(R.string.error_network);
        }
    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_SEARCH_DOSEARCH://
                    dissProDialog();
                    mResultList.clear();
                    LogUtils.e("onSuccess===关键字搜索状态===========>" + beanSendUI.getInfo());
                    LogUtils.e("onSuccess===服务请求===========>" + "成功" + searchType + "    " + mSelect);
                    EtyList listMaps = (EtyList) beanSendUI.getInfo();

//                    LogUtils.e("onSuccess===listMaps=size==========>" + (List<Map<String,
// Object>>) listMaps.getLIST());
                    if (!UtilList.isEmpty(listMaps.getLIST())) {
                        mResultList.addAll(listMaps.getLIST());
                    }
                    LogUtils.e("onSuccess===服务请求===========>" + mResultList);

                    if (mResultList.size() > 0) {
                        mResultAdapter.setList(mResultList);
                        showResultLv();
                        mResultAdapter.notifyDataSetChanged();
                    } else {
                        showNoHistory();
                        mShopCart.setVisibility(View.GONE);
                        mTvNums.setVisibility(View.GONE);
                        LogUtils.e("onSuccess===没有搜索到内容===========>" + mResultList);
                    }

                    break;
                default:
                    LogUtils.e("onSuccess===default===========>" + beanSendUI);
                    LogUtils.e("onSuccess===服务请求===========>" + "失败");

                    break;
            }
        } else {
            LogUtils.e("onSuccess===============>" + beanSendUI);
        }
    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_SEARCH_DOSEARCH://
                    dissProDialog();
                    mResultList.clear();
                    break;
                default:
                    LogUtils.e("onFailure===default===========>" + beanSendUI);

                    break;
            }
        } else {
            LogUtils.e("onFailure===============>" + beanSendUI);
        }
    }


    /**
     * 编辑框的TextWatcher
     */
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String mCurrentKey = s.toString();

            mCancleBt.setVisibility(View.VISIBLE);

            Editable etext = mEditText.getText();
            int position = etext.length();
            Selection.setSelection(etext, position);
            showHistory(mCurrentKey);
        }
    };

    /**
     * 监听软键盘搜索按键
     */
    OnEditorActionListener onEditorActionListener = new OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            // EditorInfo.IME_ACTION_SEARCH 值为3
            // actionId event.getKeyCode()
            // 搜狗 3，null
            // 触宝 3，null
            // 百度 3，null
            // QQ 0，66


            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch();
                return true;
            } else if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    doSearch();
                    return true;
                }
            }
            return false;
        }
    };

    /**
     * 分类列表Adapter
     */
    public class ClassifyAdapter extends BaseAdapter {

        private String[] classify = new String[]{"按关注排名搜索", "按销售数量搜索"};

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = mContext.getLayoutInflater().inflate(R.layout
                    .aty_search_classifying_item, null);
            TextView classify = (TextView) convertView.findViewById(R.id.apps_classify_item_name);
            classify.setText(this.classify[position]);
            return convertView;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int position) {
            return classify[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    /**
     * 历史列表Adapter
     */
    private class HistoryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mHistoryList.size();
        }

        @Override
        public Object getItem(int position) {
            return mHistoryList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mContext.getLayoutInflater().inflate(R.layout
                        .aty_search_history_lv_item, null);
                holder.suggestItemTv = (TextView) convertView.findViewById(R.id
                        .apps_search_history_item_tv);
                holder.deleteImg = (ImageView) convertView.findViewById(R.id
                        .apps_search_history_item_delete_bt);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.suggestItemTv.setText(mHistoryList.get(position).getQuery());
            holder.deleteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDbServiceSearch.deleteOne(mHistoryList.get(position));
                    mHistoryList.remove(position);
                    mHistoryAdapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView suggestItemTv;
            ImageView deleteImg;
        }
    }

    /**
     * 底部购物车监听事件
     *
     * @param v
     */
    @OnClick(R.id.rlyt_shopcart)
    public void onRlytShopCartClick(View v) {
        mBaseUtilAty.startActivity(AtyShoppingCart.class);
    }

    /**
     * @param
     * @return void
     * @throws
     * @Description: 创建动画层
     */
    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(Integer.MAX_VALUE - 1);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    /**
     * @param parent
     * @param view
     * @param location
     * @return
     */
    private View addViewToAnimLayout(final ViewGroup parent, final View view, int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }


    /**
     * 设置动画
     *
     * @param view1
     * @param startLocation
     */
    public void setAnim(final View view1, int[] startLocation) {
        anim_mask_layout = null;
        anim_mask_layout = createAnimLayout();
        anim_mask_layout.addView(view1);//把动画小球添加到动画层
        final View viewAnim = addViewToAnimLayout(anim_mask_layout, view1, startLocation);
        int[] endLocation = new int[2];// 存储动画结束位置的X、Y坐标

        mTvNums.getLocationInWindow(endLocation);// mImgShopCart是购物车图标/数字小图标

        // 计算位移
        int endX = endLocation[0] - startLocation[0] + 50;// 动画位移的X坐标
        int endY = endLocation[1] - startLocation[1];// 动画位移的y坐标

//        TranslateAnimation translateAnimationY = new TranslateAnimation(100, endX, -130, endY);
        TranslateAnimation translateAnimationY = new TranslateAnimation(0, endX, 0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationY.setFillAfter(true);

        AnimationSet set1 = new AnimationSet(false);
        set1.setFillAfter(false);
        set1.addAnimation(translateAnimationY);
        set1.setDuration(800);// 动画的执行时间
        viewAnim.startAnimation(set1);
        // 动画监听事件
        set1.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                view1.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                mTvNums.setVisibility(View.VISIBLE);
                mNum++;
                mTvNums.setText(mShopCartList.size() + mNum + "");
                view1.setVisibility(View.INVISIBLE);
                //发送广播更新底部购物车显示
                sendShopChartBroadcast();
            }
        });
    }
}
