package com.dashihui.afford.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dashihui.afford.R;
import com.dashihui.afford.common.constants.AffConstans;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.ui.activity.my.AtyMyAddressUpdata;
import com.dashihui.afford.util.list.UtilList;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.Map;

/**
 * Created by NiuFC on 2015/11/11.
 */
public class AdapterMyAddress extends AdapterBase<Map<String, Object>> {

    private List<Map<String, Object>> listObject;
    public AdapterMyAddress(Activity context, List<Map<String, Object>> _mList) {
        super(context, _mList);
        mContext = context;
        listObject = _mList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.aty_my_address_item, null);
            viewHolder = new ViewHolder(mContext,mList);
            //依赖注入初始化
            ViewUtils.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
            viewHolder.refresh(position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.update(position,mList);
        }
        //修改收货地址按钮
        viewHolder.mImgBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AtyMyAddressUpdata.class);
                intent.putExtra(CommConstans.ADDRESS.ADDRESSID, mList.get(position).get("ID") + "");
                intent.putExtra(CommConstans.ADDRESS.USERNAME, mList.get(position).get("LINKNAME") + "");
                intent.putExtra(CommConstans.ADDRESS.USERSEX, mList.get(position).get("SEX") + "");
                intent.putExtra(CommConstans.ADDRESS.USERTEL, mList.get(position).get("TEL") + "");
                intent.putExtra(CommConstans.ADDRESS.USERADDRESS, mList.get(position).get("ADDRESS") + "");
                intent.putExtra(CommConstans.ADDRESS.ISDEFAULT, mList.get(position).get("ISDEFAULT") + "");
                LogUtils.e("getView===========USERADDRESS==========>" + mList.get(position).get("ADDRESS") + "");
//                intent.putExtra(AffConstans.ADDRESS.BUILDID, mList.get(position).get("BUILDID") + "");
//                intent.putExtra(AffConstans.ADDRESS.UNITID, mList.get(position).get("UNITID") + "");
//                intent.putExtra(AffConstans.ADDRESS.ROOMID, mList.get(position).get("ROOMID") + "");
//                intent.putExtra(AffConstans.ADDRESS.BUILDNAME, mList.get(position).get("BUILDNAME") + "");
//                intent.putExtra(AffConstans.ADDRESS.UNITNAME, mList.get(position).get("UNITNAME") + "");
//                intent.putExtra(AffConstans.ADDRESS.ROOMNAME, mList.get(position).get("ROOMNAME") + "");
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    /**
     * @author p
     */
    static class ViewHolder {
        @ViewInject(R.id.username_addr)
        private TextView mTvUserName;//用户名

        @ViewInject(R.id.phone_addr)
        private TextView mTvUserPhone;//用户电话

        @ViewInject(R.id.my_address)
        private TextView mTvAddressInfo;//用户地址

        @ViewInject(R.id.right_imgbtn)//编辑按钮
        private ImageView mImgBtnEdit;

        @ViewInject(R.id.text_default)//默认view
        private TextView mTvDefault;

        private Activity mContent;
        private List<Map<String, Object>> mListMap;
        public ViewHolder(Activity context,List<Map<String, Object>> _listMap) {
            mContent = context;
            mListMap = _listMap;
        }

        /**
         *  更新最新数据
         * @param _listMap
         */
        public void refreshList(List<Map<String, Object>> _listMap){
            mListMap = _listMap;
        }

        /**
         * 第一次刷新
         */
        public void refresh(final int position) {
            if (!UtilList.isEmpty(mListMap)){
                //地址列表信息
                mTvUserName.setText(mListMap.get(position).get("LINKNAME") + "");
                mTvUserPhone.setText(mListMap.get(position).get("TEL") + "");
                mTvAddressInfo.setText(mListMap.get(position).get("ADDRESS") + "");

                //是否为默认地址，是则显示默认，不是则隐藏，1为默认，0为非默认，
                if (("1").equals(mListMap.get(position).get("ISDEFAULT") + "")) {
                    mTvDefault.setVisibility(View.VISIBLE);
                } else if (("0").equals(mListMap.get(position).get("ISDEFAULT") + "")) {
                    mTvDefault.setVisibility(View.GONE);
                }
            }else{
                LogUtils.e("refresh===Error=====null=========>" + mListMap + "");
            }


//            mCommunity.setText(mListMap.get(position).get("COMMUNITYTITLE") + "");
//            mAddressId.setText(mListMap.get(position).get("ID") + "");
//            mBuildId.setText(mListMap.get(position).get("BUILDID") + "");
//            mUnitId.setText(mListMap.get(position).get("UNITID") + "");
//            mRoomId.setText(mListMap.get(position).get("ROOMID") + "");


//            if ("1".equals(mListMap.get(position).get("SEX") + "")) {
//                mTvSex.setText("1");
//            } else if ("2".equals(mListMap.get(position).get("SEX") + "")) {
//                mTvSex.setText("2");
//            }
//
//            mTvAddressInfo.setText((mListMap.get(position).get("BUILDNAME") + "") + (mListMap.get(position).get("UNITNAME") + "") + (mListMap.get(position).get("ROOMNAME") + ""));
//            LogUtils.e("refresh======mTvAddressInfo=======》" + mListMap.get(position).get("ADDRESS") + "");
        }

        /**
         * 更新
         */
        public void update(final int position,List<Map<String, Object>> _listMap) {
            refreshList(_listMap);
            refresh(position);
        }
    }
}
