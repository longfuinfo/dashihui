package com.dashihui.afford.ui.activity.my;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.R;
import com.dashihui.afford.business.BusinessCommon;
import com.dashihui.afford.business.BusinessUser;
import com.dashihui.afford.business.entity.EtyBuilding;
import com.dashihui.afford.business.entity.EtySendToUI;
import com.dashihui.afford.common.base.BaseActivity;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.thirdapi.addressPicker.adapters.ArrayWheelAdapter;
import com.dashihui.afford.thirdapi.addressPicker.widget.OnWheelChangedListener;
import com.dashihui.afford.thirdapi.addressPicker.widget.WheelView;
import com.dashihui.afford.util.UtilCommon;
import com.dashihui.afford.util.string.UtilString;
import com.dashihui.afford.util.toast.UtilToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtyMyAddressNew extends BaseActivity implements OnWheelChangedListener, View.OnClickListener {

    @ViewInject(R.id.edit_name)//新增姓名
    private EditText mEtName;

    @ViewInject(R.id.radiogroup)
    private RadioGroup mRadioSex;

    @ViewInject(R.id.edit_phone)//新增电话
    private EditText mEtPhone;

    @ViewInject(R.id.text_community)
    private TextView mTxtCommunity;//定位小区

    @ViewInject(R.id.text_address)//新增地址
    private TextView mEtAddress;
    @ViewInject(R.id.tv_addr_info)
    private TextView mTvAddrInfo;//用户楼号、单元、房间号详情
    @ViewInject(R.id.et_address)
    private EditText mEtUserAddress;//用户详细地址编辑


    @ViewInject(R.id.default_checkbox)
    private CheckBox mCheckBox;//默认地址
    private String mIsDefault = "1";//是否为默认地址

    @ViewInject(R.id.lyt_address)
    private LinearLayout mLytAddress;

    @ViewInject(R.id.bottom_btn_save)
    private TextView mBtnSave;

    //用来提交给服务器的用户姓名、性别、收货电话、地址
    private String mNewName, mSex = "1", mNewPhone,mUserAddress;

    private BusinessCommon mBllCommon;
    private BusinessUser mBllUser;

    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private Button mBtnConfirm;//提交按钮
    private String mAddressInfo;//地址

    /**
     * 所有楼号
     */
    protected String[] mProvinceDatas;
    protected String[] mArrayBuildId;
    /**
     * key - 楼号 value - 单元
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<>();
    protected Map<String, String[]> mUnitIdMap = new HashMap<>();
    /**
     * key - 单元 values - 门牌号
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
    protected Map<String, String[]> mRoomIdMap = new HashMap<String, String[]>();

    /**
     * 当前楼的名称
     */
    protected String mCurrentProviceName;
    private String mBuildId;
    /**
     * 当前单元的名称
     */
    protected String mCurrentCityName;
    private String mUnitId;
    /**
     * 当前门牌号的名称
     */
    protected String mCurrentDistrictName = "";
    private String mRoomId;

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_my_address_new);
        ViewUtils.inject(this);
        mLytAddress.setVisibility(View.GONE);

        mBllCommon = new BusinessCommon(this);
        mBllUser = new BusinessUser(this);
        if (AffordApp.getInstance().getEntityLocation() != null) {
            //根据小区Id获取楼号信息
            mBllCommon.communityDetail(AffordApp.getInstance().getEntityLocation().getCOMMUNITY().getID() + "");
        } else {
            finish();
        }
        if (AffordApp.getInstance().getUserLogin() != null){
            //获取小区名称
            mTxtCommunity.setText(AffordApp.getInstance().getUserLogin().getUSER().getCOMMUNITYNAME());
        }

        if (AffordApp.getInstance().getUserLogin() != null) {
            //获取手机号
            LogUtils.e("onCreate===========phone=========>" + AffordApp.getInstance().getUserLogin().getUSER().getMSISDN() + "");
            mEtPhone.setText(AffordApp.getInstance().getUserLogin().getUSER().getMSISDN() + "");
        } else {
            finish();
        }
        mCheckBox.setChecked(true);

        //性别选择
        initSexView();
    }

    //性别选择
    public void initSexView(){
        mRadioSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.man:
                        mSex = "1";
                        break;
                    case R.id.women:
                        mSex = "2";
                        break;
                    default:
                        break;
                }
            }
        });
    }


    @Override
    protected void onResume() {
//        if (AffordApp.getInstance().getEntityLocation() != null) {
//            mTxtView.setText(AffordApp.getInstance().getEntityLocation().getCOMMUNITY().getTITLE());
//        } else {
//            LogUtils.e("社区信息出错！=======>");
//        }
        super.onResume();
    }


    private void setUpViews() {
        mViewProvince = (WheelView) findViewById(R.id.id_province);
        mViewCity = (WheelView) findViewById(R.id.id_city);
        mViewDistrict = (WheelView) findViewById(R.id.id_district);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
    }

    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加onclick事件
        mBtnConfirm.setOnClickListener(this);
    }

    private void setUpData() {
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(AtyMyAddressNew.this, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(5);
        mViewCity.setVisibleItems(5);
        mViewDistrict.setVisibleItems(5);
        updateCities();
        updateAreas();
        updateDistrict();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            updateDistrict();
        }
    }

    private void updateDistrict() {
        int pCurrent = mViewDistrict.getCurrentItem();

        mCurrentDistrictName = mDistrictDatasMap.get(mCurrentProviceName + mCurrentCityName)[pCurrent];
        mRoomId = mRoomIdMap.get(mCurrentProviceName + mCurrentCityName)[pCurrent];
    }

    /**
     * 根据当前的单元号，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mBuildId + mCurrentProviceName)[pCurrent];
        mUnitId = mUnitIdMap.get(mBuildId + mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentProviceName + mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        mViewDistrict.setCurrentItem(0);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        mBuildId = mArrayBuildId[pCurrent];
        String[] cities = mCitisDatasMap.get(mBuildId + mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm://地址选择完成
                showSelectedResult();
                if (mAddressInfo != null && mAddressInfo.length() > 0) {
                    mTvAddrInfo.setText(mAddressInfo);
                    mLytAddress.setVisibility(View.GONE);
                    mBtnSave.setVisibility(View.VISIBLE);
                } else {
                    mTvAddrInfo.setText("");
                    mTvAddrInfo.setHint("楼号-单元-门牌号（请选择）");
                    UtilToast.show(this, "请选择详细地址", Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;
        }
    }

    private void showSelectedResult() {
        Toast.makeText(this, "当前选中:" + mCurrentProviceName + "," + mCurrentCityName + ","
                + mCurrentDistrictName, Toast.LENGTH_SHORT).show();
        //地址
        mAddressInfo = mCurrentProviceName + "" + mCurrentCityName + " " + mCurrentDistrictName + "";
    }

    /**
     * 楼号-单元-门牌号的数据
     */

    protected void initProvinceDatas(List<EtyBuilding> listBuilding) {
        List<EtyBuilding> provinceList = listBuilding;
        try {
            //*/ 初始化默认选中的楼、单元、门牌号
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getNAME();
                mBuildId = provinceList.get(0).getID() + "";//楼号id

//                LogUtils.e("initProvinceDatas============mBuildIds==========>" + mBuildId);
//                LogUtils.e("initProvinceDatas========mCurrentProviceName=========>" + mCurrentProviceName);

                List<EtyBuilding.ChildrenEntity> cityList = provinceList.get(0).getCHILDREN();

                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getNAME();
                    mUnitId = cityList.get(0).getID() + "";//单元id

//                    LogUtils.e("initProvinceDatas============mUnitIds==========>" + mUnitId);
//                    LogUtils.e("initProvinceDatas========mCurrentCityName=========>" + mCurrentCityName);


                    List<EtyBuilding.ChildrenEntity.CHILDRENEnty> districtList = cityList.get(0).getCHILDREN();
                    mCurrentDistrictName = districtList.get(0).getNAME();
                    mRoomId = districtList.get(0).getID() + "";//房间id

//                    LogUtils.e("initProvinceDatas============mRoomIds==========>" + mRoomId);
//                    LogUtils.e("initProvinceDatas========mCurrentDistrictName=========>" + mCurrentDistrictName);

                    mCurrentZipCode = districtList.get(0).getID() + "";
                }
            }
            mProvinceDatas = new String[provinceList.size()];
            mArrayBuildId = new String[provinceList.size()];

            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有栋楼的数据
                mProvinceDatas[i] = provinceList.get(i).getNAME();
                mArrayBuildId[i] = provinceList.get(i).getID() + "";

                List<EtyBuilding.ChildrenEntity> cityList = provinceList.get(i).getCHILDREN();
                String[] cityNames = new String[cityList.size()];
                String[] unitIds = new String[cityList.size()];

                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历单元下面的所有门牌号的数据
                    cityNames[j] = cityList.get(j).getNAME();
                    unitIds[j] = cityList.get(j).getID() + "";

                    List<EtyBuilding.ChildrenEntity.CHILDRENEnty> districtList = cityList.get(j).getCHILDREN();
                    String[] distrinctNameArray = new String[districtList.size()];
                    String[] roomIds = new String[districtList.size()];

                    EtyBuilding.ChildrenEntity.CHILDRENEnty[] distrinctArray = new EtyBuilding.ChildrenEntity.CHILDRENEnty[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历单元下面所有门牌号的数据
                        EtyBuilding.ChildrenEntity.CHILDRENEnty districtModel = new EtyBuilding.ChildrenEntity.CHILDRENEnty(districtList.get(k).getNAME(), districtList.get(k).getID() + "");
//                        // 区/县对于的邮编，保存到mZipcodeDatasMap
//                        mZipcodeDatasMap.put(districtList.get(k).getNAME(), districtList.get(k).getID() + "");

                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getNAME();
                        roomIds[k] = districtModel.getID() + "";
                    }

                    // 门牌号的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(provinceList.get(i).getNAME() + cityNames[j], distrinctNameArray);
                    mRoomIdMap.put(provinceList.get(i).getNAME() + cityNames[j], roomIds);
                }
                // 栋楼-单元的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getID() + provinceList.get(i).getNAME(), cityNames);
                mUnitIdMap.put(provinceList.get(i).getID() + provinceList.get(i).getNAME() + "", unitIds);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
//            LogUtils.e("finally========initProvinceDatas=======beanSendUI=======>");
        }
    }

//    //获取性别的选项值
//    @OnRadioGroupCheckedChange(R.id.radiogroup)
//    public void onGropCheckedChanged(RadioGroup group, int checkedId) {
//        switch (checkedId) {
//            case R.id.man:
//                mSex = "1";
//                break;
//            case R.id.women:
//                mSex = "2";
//                break;
//            default:
//                break;
//        }
//    }

    //是否选中默认
    @OnClick(R.id.default_checkbox)
    public void onIsDefaultCBoxClick(View v) {
        if (mCheckBox.isChecked()) {
            mIsDefault = "1";
        } else {
            mIsDefault = "0";
        }
    }

    //点击保存
    @OnClick(R.id.bottom_btn_save)
    public void onTvBtnSave(View v) {
        mNewName = mEtName.getText().toString().trim();
        mNewPhone = mEtPhone.getText().toString().trim();
        mUserAddress = mEtUserAddress.getText().toString().trim();
        LogUtils.e("onTvBtnSave=========mUserAddress==========>" + mUserAddress);
        if (mNewName.length() <= 0 && "".equals(mNewName)) {
            UtilToast.show(this, "请输入联系人姓名", Toast.LENGTH_SHORT);
        } else if (mNewPhone.length() <= 0 && "".equals(mNewPhone)) {
            UtilToast.show(this, "请输入收货人电话号码", Toast.LENGTH_SHORT);
        } else if (mNewPhone != null && mNewPhone.length() != 11 && !UtilCommon.isMobileNO(mNewPhone)) {
            UtilToast.show(this, "请输入正确的手机号！", Toast.LENGTH_SHORT);
        } else if(!UtilString.isEmpty(mUserAddress)) {
            mBllUser.addaddress(mNewName, mSex, mNewPhone, mUserAddress, mIsDefault);
        }else {
            UtilToast.show(this, "请输入收货地址", Toast.LENGTH_SHORT);
//        }else if (mAddressInfo != null && mAddressInfo.length() > 0) {
//            String nickName,String sex,String tel,String address,String isdefault
//        } else {
//            mTvAddrInfo.setText("");
//            mTvAddrInfo.setHint("楼号-单元-门牌号（请选择）");
//            UtilToast.show(this, "请选择详细地址", Toast.LENGTH_SHORT);
        }
    }

    @OnClick(R.id.my_address_back)//返回
    public void onLeftBackClick(View v) {
        onBackPressed();
    }

    /**
     * 选择楼层信息点击事件
     *
     * @param v
     */
    @OnClick(R.id.Lyt_chooseaddr)
    public void onAddrPickerClick(View v) {
        //隐藏软键盘
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputManger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        mLytAddress.setVisibility(View.VISIBLE);
        mBtnSave.setVisibility(View.GONE);
        setUpViews();
        initProvinceDatas(AffordApp.getInstance().getmListEtyBuilding());
        setUpData();
        setUpListener();

    }


    @Override
    public void onSuccess(EtySendToUI beanSendUI) {
        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_COMMOON_COMMUNITYDETAIL://小区楼号信息
                    List<EtyBuilding> ddrListMap = (List<EtyBuilding>) beanSendUI.getInfo();
                    AffordApp.getInstance().setmListEtyBuilding(ddrListMap);
                    break;
                case AffConstans.BUSINESS.TAG_USER_ADDADDRESS://收货地址添加
                    Map<String,Object> mapObject = (Map<String, Object>) beanSendUI.getInfo();
                    LogUtils.e("onSuccess==========mapObject=========>" + mapObject);
                    finish();
                    UtilToast.show(this, "地址添加成功", Toast.LENGTH_SHORT);
                    break;

                default:
                    break;
            }
        } else {
//            LogUtils.e("onSuccess======AtyHome=========>" + beanSendUI);
        }
    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        LogUtils.e("AtyMyAddressNew========onFailure=======beanSendUI=======>" + beanSendUI.getInfo());
//        UtilToast.show(this, "网络无法链接，请检查您的网络！", Toast.LENGTH_SHORT);
    }
}
