package com.dashihui.afford.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by NiuFC on 2015/11/26.
 */
public class ModelOrder implements Parcelable {


    /**
     * PACKAGE : Sign=WXPay
     * APPID : wxb595a449fa9ba651
     * SIGN : 1F2890B443EDB085075CA688FEECF60E
     * PARTNERID : 1288445201
     * PREPAYID : wx2015112617273408573ad44b0282658283
     * NONCESTR : iswhusjhchx31lqqk4rl37uvh05fpicq
     * TIMESTAMP : 1448530055
     */


    private String PACKAGE;

    private String APPID;
   private String SIGN;
    private String PARTNERID;
    private String PREPAYID;
    private String NONCESTR;
    private String TIMESTAMP;



    public String getPACKAGE() {
        return PACKAGE;
    }

    public void setPACKAGE(String PACKAGE) {
        this.PACKAGE = PACKAGE;
    }


    public String getAPPID() {
        return APPID;
    }

    public void setAPPID(String APPID) {
        this.APPID = APPID;
    }

    public String getSIGN() {
        return SIGN;
    }

    public void setSIGN(String SIGN) {
        this.SIGN = SIGN;
    }

    public String getPARTNERID() {
        return PARTNERID;
    }

    public void setPARTNERID(String PARTNERID) {
        this.PARTNERID = PARTNERID;
    }

    public String getPREPAYID() {
        return PREPAYID;
    }

    public void setPREPAYID(String PREPAYID) {
        this.PREPAYID = PREPAYID;
    }

    public String getNONCESTR() {
        return NONCESTR;
    }

    public void setNONCESTR(String NONCESTR) {
        this.NONCESTR = NONCESTR;
    }

    public String getTIMESTAMP() {
        return TIMESTAMP;
    }

    public void setTIMESTAMP(String TIMESTAMP) {
        this.TIMESTAMP = TIMESTAMP;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.PACKAGE);
        dest.writeString(this.APPID);
        dest.writeString(this.SIGN);
        dest.writeString(this.PARTNERID);
        dest.writeString(this.PREPAYID);
        dest.writeString(this.NONCESTR);
        dest.writeString(this.TIMESTAMP);
    }

    public ModelOrder() {
    }

    private ModelOrder(Parcel in) {
        this.PACKAGE = in.readString();
        this.APPID = in.readString();
        this.SIGN = in.readString();
        this.PARTNERID = in.readString();
        this.PREPAYID = in.readString();
        this.NONCESTR = in.readString();
        this.TIMESTAMP = in.readString();
    }

    public static final Creator<ModelOrder> CREATOR = new Creator<ModelOrder>() {
        public ModelOrder createFromParcel(Parcel source) {
            return new ModelOrder(source);
        }

        public ModelOrder[] newArray(int size) {
            return new ModelOrder[size];
        }
    };
}
