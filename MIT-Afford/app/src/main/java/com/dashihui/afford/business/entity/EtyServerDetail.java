package com.dashihui.afford.business.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by NiuFC on 2015/12/19.
 */
public class EtyServerDetail implements Parcelable {


    /**
     * ID : 1
     * NAME : XX明星家政公司
     * THUMB : /service/shop/xxxx.jpg
     * IMAGES : ["/goods/xx1.jpg","/goods/xx2.jpg","/goods/xx3.jpg","/goods/xx4.jpg"]
     * COMMENT : 按小时收费，服务项目包括地面踢脚线清理......
     * TOTAL : 1
     * ITEMS : [{"ID":1,"TITLE":"清洁家具1小时 ￥25.5","MARKETPRICE":30,"SELLPRICE":25.5}]
     * HASDESCRIBE：图文详情
     */

    private int ID;
    private String NAME;
    private String THUMB;
    private String COMMENT;
    private int TOTAL;
    private int HASDESCRIBE;
    private List<String> IMAGES;
    /**
     * ID : 1
     * TITLE : 清洁家具1小时 ￥25.5
     * MARKETPRICE : 30.0
     * SELLPRICE : 25.5
     */

    private List<Map<String,Object>> ITEMS;

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public void setTHUMB(String THUMB) {
        this.THUMB = THUMB;
    }

    public void setCOMMENT(String COMMENT) {
        this.COMMENT = COMMENT;
    }

    public void setTOTAL(int TOTAL) {
        this.TOTAL = TOTAL;
    }

    public void setIMAGES(List<String> IMAGES) {
        this.IMAGES = IMAGES;
    }

    public void setITEMS(List<Map<String,Object>> ITEMS) {
        this.ITEMS = ITEMS;
    }

    public int getHASDESCRIBE() {
        return HASDESCRIBE;
    }

    public void setHASDESCRIBE(int HASDESCRIBE) {
        this.HASDESCRIBE = HASDESCRIBE;
    }

    public int getID() {
        return ID;
    }

    public String getNAME() {
        return NAME;
    }

    public String getTHUMB() {
        return THUMB;
    }

    public String getCOMMENT() {
        return COMMENT;
    }

    public int getTOTAL() {
        return TOTAL;
    }


    public List<String> getIMAGES() {
        return IMAGES;
    }

    public List<Map<String,Object>> getITEMS() {
        return ITEMS;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ID);
        dest.writeString(this.NAME);
        dest.writeString(this.THUMB);
        dest.writeString(this.COMMENT);
        dest.writeInt(this.TOTAL);
        dest.writeList(this.IMAGES);
        dest.writeList(this.ITEMS);
        dest.writeInt(this.HASDESCRIBE);
    }

    public EtyServerDetail() {
    }

    private EtyServerDetail(Parcel in) {
        this.ID = in.readInt();
        this.NAME = in.readString();
        this.THUMB = in.readString();
        this.COMMENT = in.readString();
        this.TOTAL = in.readInt();
        this.IMAGES = new ArrayList<String>();
        in.readList(this.IMAGES, IMAGES.getClass().getClassLoader());
        this.ITEMS = new ArrayList<Map<String, Object>>();
        in.readList(this.ITEMS, ITEMS.getClass().getClassLoader());
        this.HASDESCRIBE = in.readInt();
    }

    public static final Parcelable.Creator<EtyServerDetail> CREATOR = new Parcelable.Creator<EtyServerDetail>() {
        public EtyServerDetail createFromParcel(Parcel source) {
            return new EtyServerDetail(source);
        }

        public EtyServerDetail[] newArray(int size) {
            return new EtyServerDetail[size];
        }
    };
}
