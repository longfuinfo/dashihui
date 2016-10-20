package com.dashihui.afford.business.entity;

/**
 * 商铺信息
 * Created by NiuFC on 2015/11/5.
 */
public class EtySTORE {


    /**
     * ID : 1
     * TITLE : 大实惠xxx小区店
     * PROVINCENAME : 河南省
     * CITYNAME : 郑州市
     * AREANAME : 金水区
     * ADDRESS : 东风路南阳路交叉口向西50米路北
     * THUMB : /community/xxxx.jpg
     * TEL : 15165984123
     * MAIL : 5026874126@qq.com
     * BEGINTIME : 00:00
     * ENDTIME : 00:00
     * MANAGER : 赵默笙
     * DELIVERYDES : 购物必须满20元才开始配送
     */

    private int ID;
    private String TITLE;
    private String PROVINCENAME;
    private String CITYNAME;
    private String AREANAME;
    private String ADDRESS;
    private String THUMB;
    private String TEL;
    private String MAIL;
    private String BEGINTIME;
    private String ENDTIME;
    private String MANAGER;
    private String DELIVERYDES;

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public void setPROVINCENAME(String PROVINCENAME) {
        this.PROVINCENAME = PROVINCENAME;
    }

    public void setCITYNAME(String CITYNAME) {
        this.CITYNAME = CITYNAME;
    }

    public void setAREANAME(String AREANAME) {
        this.AREANAME = AREANAME;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public void setTHUMB(String THUMB) {
        this.THUMB = THUMB;
    }

    public void setTEL(String TEL) {
        this.TEL = TEL;
    }

    public void setMAIL(String MAIL) {
        this.MAIL = MAIL;
    }

    public void setBEGINTIME(String BEGINTIME) {
        this.BEGINTIME = BEGINTIME;
    }

    public void setENDTIME(String ENDTIME) {
        this.ENDTIME = ENDTIME;
    }

    public void setMANAGER(String MANAGER) {
        this.MANAGER = MANAGER;
    }

    public void setDELIVERYDES(String DELIVERYDES) {
        this.DELIVERYDES = DELIVERYDES;
    }

    public int getID() {
        return ID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public String getPROVINCENAME() {
        return PROVINCENAME;
    }

    public String getCITYNAME() {
        return CITYNAME;
    }

    public String getAREANAME() {
        return AREANAME;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public String getTHUMB() {
        return THUMB;
    }

    public String getTEL() {
        return TEL;
    }

    public String getMAIL() {
        return MAIL;
    }

    public String getBEGINTIME() {
        return BEGINTIME;
    }

    public String getENDTIME() {
        return ENDTIME;
    }

    public String getMANAGER() {
        return MANAGER;
    }

    public String getDELIVERYDES() {
        return DELIVERYDES;
    }
}
