package com.dashihui.afford.ui.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.thirdapi.addressPicker.adapters.ArrayWheelAdapter;
import com.dashihui.afford.thirdapi.addressPicker.widget.OnWheelChangedListener;
import com.dashihui.afford.thirdapi.addressPicker.widget.WheelView;
import com.dashihui.afford.ui.widget.WgtAlertDialog;
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

public class AtyMyAddressUpdata extends BaseActivity implements OnWheelChangedListener, View.OnClickListener {

    @ViewInject(R.id.updata_edit_name)//修改姓名
    private EditText mEditName;

    @ViewInject(R.id.upada_radiogroup)
    private RadioGroup mRadioSex;

    @ViewInject(R.id.updata_edit_phone)//修改电话
    private EditText mEtPhone;

    @ViewInject(R.id.updata_tv_addr_info)//修改地址
    private TextView mEtAddress;

    @ViewInject(R.id.et_address)
    private TextView mEdUpdataAddress;

    @ViewInject(R.id.updata_man)//男
    private RadioButton mRbMan;

    @ViewInject(R.id.updata_women)//女
    private RadioButton mRbWomen;

    @ViewInject(R.id.updata_default_checkbox)
    private CheckBox mCheckBox;//默认地址

    @ViewInject(R.id.updata_text_community)
    private TextView mTxtCommunity;//定位小区

    @ViewInject(R.id.updata_tv_addr_info)
    private TextView mTvAddrInfo;//用户楼号、单元、房间号详情

    @ViewInject(R.id.lyt_address)
    private LinearLayout mLytAddress;

    @ViewInject(R.id.updata_bottom_btn_save)
    private TextView mBtnUpdata;

    @ViewInject(R.id.et_address)
    private EditText mEtUserAddress;//用户详细地址编辑

    //
    private String mIsDefault, mUpdataAddressInfo, mSex, mBuildName, mUnitName, mRoomName;

    //提交给服务器的：地址ID、用户姓名、性别、收货电话、地址（楼号、单元、房间号）、楼号ID、单元ID、房间ID
    private String mAddressId, mUpdataName, mUpdateSex, mUpdataPhone, mUserAddress, mUpdateIsDefault;

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
    private String mUpdataBuildId;
    /**
     * 当前单元的名称
     */
    protected String mCurrentCityName;
    private String mUpdataUnitId;
    /**
     * 当前门牌号的名称
     */
    protected String mCurrentDistrictName = "";
    private String mUpdataRoomId;

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_my_address_updata);
        ViewUtils.inject(this);
        mLytAddress.setVisibility(View.GONE);
        mBllUser = new BusinessUser(this);
        getUserInfo();//
        LogUtils.e("AtyMyAddressNew===========onCreate==>");
//        mCheckBox.setChecked(true);
        mBllCommon = new BusinessCommon(this);
        //根据小区Id获取楼号信息
        mBllCommon.communityDetail(AffordApp.getInstance().getEntityLocation().getCOMMUNITY().getID() + "");

        //性别选择
        initSexView();
    }


    //性别选择
    public void initSexView(){
        mRadioSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.updata_man:
                        mUpdateSex = "1";
                        LogUtils.e("男士=======>" + mUpdateSex);
                        break;
                    case R.id.updata_women:
                        mUpdateSex = "2";
                        LogUtils.e("女士=======>" + mUpdateSex);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    //从AdapterMyAddress传来的用户信息
    private void getUserInfo() {
        Intent intent = getIntent();
        mAddressId = intent.getStringExtra(CommConstans.ADDRESS.ADDRESSID);
        mUserAddress = intent.getStringExtra(CommConstans.ADDRESS.USERADDRESS);
        mEditName.setText(intent.getStringExtra(CommConstans.ADDRESS.USERNAME));
        mSex = intent.getStringExtra(CommConstans.ADDRESS.USERSEX);
        mEtPhone.setText(intent.getStringExtra(CommConstans.ADDRESS.USERTEL));
        mIsDefault = intent.getStringExtra(CommConstans.ADDRESS.ISDEFAULT);
        mEdUpdataAddress.setText(mUserAddress);
//        mUpdataBuildId = intent.getStringExtra(AffConstans.ADDRESS.BUILDID);
        if (mIsDefault.equals("1")) {
            mCheckBox.setChecked(true);
        } else {
            mCheckBox.setChecked(false);
        }

        if (mSex.equals("1")) {
            mRbMan.setChecked(true);
            mUpdateSex = "1";
        } else {
            mRbWomen.setChecked(true);
            mUpdateSex = "2";
        }
    }

    @Override
    protected void onResume() {
        if (AffordApp.getInstance().getEntityLocation() != null) {
            mTxtCommunity.setText(AffordApp.getInstance().getEntityLocation().getCOMMUNITY().getTITLE());
        } else {
            LogUtils.e("社区信息出错！=======>");
        }
        super.onResume();
    }



    //是否选中默认
    @OnClick(R.id.updata_default_checkbox)
    public void onIsDefaultCBoxClick(View v) {
        if (mCheckBox.isChecked()) {
            mUpdateIsDefault = "1";
            LogUtils.e("选中默认============》" + mUpdateIsDefault);
        } else {
            mUpdateIsDefault = "0";
            LogUtils.e("未选默认============》" + mUpdateIsDefault);
        }
    }

    //点击修改
    @OnClick(R.id.updata_bottom_btn_save)
    public void onTvUpdataClick(View v) {
        mUpdataName = mEditName.getText().toString().trim();
        mUpdataPhone = mEtPhone.getText().toString().trim();
        mUserAddress = mEtUserAddress.getText().toString().trim();
        //判断用户名、手机号是否为空，以及手机号格式是否正确
        if (mUpdataName.length() <= 0 && "".equals(mUpdataName)) {
            UtilToast.show(this, "请输入联系人姓名", Toast.LENGTH_SHORT);
        } else if (mUpdataPhone.length() <= 0 && "".equals(mUpdataPhone)) {
            UtilToast.show(this, "请输入收货人电话号码", Toast.LENGTH_SHORT);
        } else if (mUpdataPhone != null && mUpdataPhone.length() != 11 && !UtilCommon.isMobileNO(mUpdataPhone)) {
            UtilToast.show(this, "请输入正确的手机号！", Toast.LENGTH_SHORT);
//        } else {
//            mBllUser.updateaddress(mAddressId, mUpdataName, mUpdateSex, mUpdataPhone, mUpdataBuildId, mUpdataUnitId, mUpdataRoomId, mUpdateIsDefault);
        } else if (!UtilString.isEmpty(mUserAddress)) {
            mBllUser.updateaddress(mAddressId,mUpdataName, mUpdateSex, mUpdataPhone, mUserAddress, mUpdateIsDefault);
        } else {
            UtilToast.show(this, "请输入收货地址", Toast.LENGTH_SHORT);
        }
    }

    //删除当前地址
    @OnClick(R.id.delete_bottom_btn_save)
    public void onDeleteAddrClick(View v) {
        final WgtAlertDialog mAtDialog = new WgtAlertDialog();
        mAtDialog.show(AtyMyAddressUpdata.this, "取消", "确定", "确认删除当前地址？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAtDialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //根据AddressID提交给服务器删除当前地址
                mBllUser.deladdress(mAddressId);
            }
        }, false, false, 0, null);
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
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(AtyMyAddressUpdata.this, mProvinceDatas));
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
        mUpdataRoomId = mRoomIdMap.get(mCurrentProviceName + mCurrentCityName)[pCurrent];
    }

    /**
     * 根据当前的单元号，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mUpdataBuildId + mCurrentProviceName)[pCurrent];
        mUpdataUnitId = mUnitIdMap.get(mUpdataBuildId + mCurrentProviceName)[pCurrent];
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
        LogUtils.e("updateCities=========pCurrent=========>" + pCurrent);
        LogUtils.e("updateCities=========mProvinceDatas=========>" + mProvinceDatas);
        LogUtils.e("updateCities=========mCurrentProviceName=========>" + mCurrentProviceName);
        mCurrentProviceName = mProvinceDatas[pCurrent];
        mUpdataBuildId = mArrayBuildId[pCurrent];
        String[] cities = mCitisDatasMap.get(mUpdataBuildId + mCurrentProviceName);
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
                    LogUtils.e("AtyMyAddressNew==========mAddressInfo========>" + mAddressInfo);
                    mTvAddrInfo.setText(mAddressInfo);
                    mLytAddress.setVisibility(View.GONE);
                    mBtnUpdata.setVisibility(View.VISIBLE);
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
                mUpdataBuildId = provinceList.get(0).getID() + "";//楼号id

//                LogUtils.e("initProvinceDatas============mBuildIds==========>" + mBuildId);
//                LogUtils.e("initProvinceDatas========mCurrentProviceName=========>" + mCurrentProviceName);

                List<EtyBuilding.ChildrenEntity> cityList = provinceList.get(0).getCHILDREN();

                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getNAME();
                    mUpdataUnitId = cityList.get(0).getID() + "";//单元id

//                    LogUtils.e("initProvinceDatas============mUnitIds==========>" + mUnitId);
//                    LogUtils.e("initProvinceDatas========mCurrentCityName=========>" + mCurrentCityName);


                    List<EtyBuilding.ChildrenEntity.CHILDRENEnty> districtList = cityList.get(0).getCHILDREN();
                    mCurrentDistrictName = districtList.get(0).getNAME();
                    mUpdataRoomId = districtList.get(0).getID() + "";//房间id

//                    LogUtils.e("initProvinceDatas============mRoomIds==========>" + mRoomId);
//                    LogUtils.e("initProvinceDatas========mCurrentDistrictName=========>" + mCurrentDistrictName);

                    mCurrentZipCode = districtList.get(0).getID() + "";
                }
            }
            LogUtils.e("try========initProvinceDatas=======provinceList.size()=======>" + provinceList.size());
            mProvinceDatas = new String[provinceList.size()];
            mArrayBuildId = new String[provinceList.size()];

            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有栋楼的数据
                mProvinceDatas[i] = provinceList.get(i).getNAME();
                mArrayBuildId[i] = provinceList.get(i).getID() + "";
//                LogUtils.e("mArrayBuildId[i]=========楼号id========>" + mArrayBuildId[i]);
//                LogUtils.e("mCurrentProviceName===========楼号==========>" + mProvinceDatas[i]);

                List<EtyBuilding.ChildrenEntity> cityList = provinceList.get(i).getCHILDREN();
                String[] cityNames = new String[cityList.size()];
                String[] unitIds = new String[cityList.size()];

                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历单元下面的所有门牌号的数据
                    cityNames[j] = cityList.get(j).getNAME();
                    unitIds[j] = cityList.get(j).getID() + "";

//                    LogUtils.e("try======== unitIds[j]=======单元id=======>" + unitIds[j]);
//                    LogUtils.e("try======== cityNames=======单元=======>" + cityNames[j]);

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
//                        LogUtils.e(" districtList===============房间号id===============>" + roomIds[k]);
//                        LogUtils.e(" districtList===============房间===============>" + distrinctNameArray[k]);
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

    @OnClick(R.id.updata_address_back)//返回
    public void onBackClick(View v) {
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
        mBtnUpdata.setVisibility(View.GONE);
        setUpViews();
        initProvinceDatas(AffordApp.getInstance().getmListEtyBuilding());
        setUpData();
        setUpListener();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        LogUtils.e("data.getExtras.size=========>" + data.getExtras().size());
//        mUpdataAddressInfo = data.getStringExtra(AffConstans.ADDRESS.ADDRREMARK);
//        mUpdataBuildId = data.getStringExtra(AffConstans.ADDRESS.BUILDID);
//        mUpdataUnitId = data.getStringExtra(AffConstans.ADDRESS.UNITID);
//        mUpdataRoomId = data.getStringExtra(AffConstans.ADDRESS.ROOMID);
//        if (mUpdataAddressInfo.length() > 0) {
//            LogUtils.e("mAddressInfo==========>" + mUpdataAddressInfo);
//            mTvAddrInfo.setText(mUpdataAddressInfo);
//        } else {
//            mTvAddrInfo.setText("");
//            mTvAddrInfo.setHint("楼号-单元-门牌号（请选择）");
//        }
//    }

    @Override
    public void onSuccess(EtySendToUI beanSendUI) {

        if (beanSendUI != null) {
            switch (beanSendUI.getTag()) {
                case AffConstans.BUSINESS.TAG_COMMOON_COMMUNITYDETAIL://小区楼号信息
                    List<EtyBuilding> ddrListMap = (List<EtyBuilding>) beanSendUI.getInfo();
                    AffordApp.getInstance().setmListEtyBuilding(ddrListMap);
                    LogUtils.e("AtyMyAddressNew========小区楼号=======beanSendUI=======>" + beanSendUI);
                    LogUtils.e("AtyMyAddressNew========小区楼号=======beanSendUI.getInfo()=======>" + beanSendUI.getInfo());
                    break;

                case AffConstans.BUSINESS.TAG_USER_UPDATEADDRESS://收货地址修改
                    LogUtils.e("====修改地址成功后结束Activity=====>");
                    finish();
                    UtilToast.show(this, "地址修改成功", Toast.LENGTH_SHORT);
                    break;
                case AffConstans.BUSINESS.TAG_USER_DELADDRESS://收货地址删除
                    LogUtils.e("====当前地址删除成功后结束Activity=====>");
                    finish();
                    UtilToast.show(this, "地址删除成功", Toast.LENGTH_SHORT);
                    break;

                default:
                    LogUtils.e("onSuccess===default===========>" + beanSendUI);
                    break;
            }
        } else {
            LogUtils.e("onSuccess======AtyHome=========>" + beanSendUI);
        }
    }

    @Override
    public void onFailure(EtySendToUI beanSendUI) {
        LogUtils.e("AtyMyAddressNew========onFailure=======beanSendUI=======>" + beanSendUI);
        //请求服务失败时提示
        UtilToast.show(this, "网络无法链接，请检查您的网络！", Toast.LENGTH_SHORT);
    }
}
