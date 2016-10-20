package com.dashihui.afford.business.entity;

import java.util.List;

/**
 * Created by NiuFC on 2015/11/23.
 */
public class EtyShopDetail {


    /**
     * ID : 1
     * NAME : 大白菜
     * SPEC : 个
     * BRANDNAME : 老菜农牌
     * SHORTINFO : 东北大白菜，产自东北....
     * MARKETPRICE : 0.8
     * SELLPRICE : 0.8
     * IMAGES : ["/goods/xx1.jpg","/goods/xx2.jpg","/goods/xx3.jpg","/goods/xx4.jpg"]
     * TYPE : 3
     * URV : 2
     * ISCOLLECTED : 0
     * HASDESCRIBE
     */

    private int ID;
    private String NAME;
    private String SPEC;
    private String BRANDNAME;
    private String SHORTINFO;
    private String THUMB;
    private String SALECOUNT;
    private String COLLECTEDCOUNT;
    private double MARKETPRICE;
    private double SELLPRICE;
    private int TYPE;
    private int URV;
    private int ISCOLLECTED;
    private int HASDESCRIBE;
    private int ISSELF;//是否是自营商品：0：不是，1：是。
    private int ISREBATE;//是否返利，0：不是，1：是
    private double PERCENT1;//	推荐人返利比例
    private double PERCENT2;//DSH返利比例
    private double PERCENT3;//社区负责人返利比例
    private double PERCENT4;//	一级分销返利比例
    private double PERCENT5;//	二级分销返利比例


    private List<String> IMAGES;
    public void setID(int ID) {
        this.ID = ID;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public void setSPEC(String SPEC) {
        this.SPEC = SPEC;
    }
    public void setBRANDNAME(String BRANDNAME) {
        this.BRANDNAME = BRANDNAME;
    }
    public void setSHORTINFO(String SHORTINFO) {
        this.SHORTINFO = SHORTINFO;
    }

    public String getTHUMB() {
        return THUMB;
    }

    public void setTHUMB(String THUMB) {
        this.THUMB = THUMB;
    }


    public String getSALECOUNT() {
        return SALECOUNT;
    }

    public void setSALECOUNT(String SALECOUNT) {
        this.SALECOUNT = SALECOUNT;
    }

    public String getCOLLECTEDCOUNT() {
        return COLLECTEDCOUNT;
    }

    public void setCOLLECTEDCOUNT(String COLLECTEDCOUNT) {
        this.COLLECTEDCOUNT = COLLECTEDCOUNT;
    }

    public void setMARKETPRICE(double MARKETPRICE) {
        this.MARKETPRICE = MARKETPRICE;
    }

    public void setSELLPRICE(double SELLPRICE) {
        this.SELLPRICE = SELLPRICE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    public void setURV(int URV) {
        this.URV = URV;
    }

    public void setISCOLLECTED(int ISCOLLECTED) {
        this.ISCOLLECTED = ISCOLLECTED;
    }

    public void setHASDESCRIBE(int HASDESCRIBE) {
        this.HASDESCRIBE = HASDESCRIBE;
    }

    public void setIMAGES(List<String> IMAGES) {
        this.IMAGES = IMAGES;
    }

    public int getID() {
        return ID;
    }

    public String getNAME() {
        return NAME;
    }

    public String getSPEC() {
        return SPEC;
    }

    public String getBRANDNAME() {
        return BRANDNAME;
    }

    public String getSHORTINFO() {
        return SHORTINFO;
    }

    public double getMARKETPRICE() {
        return MARKETPRICE;
    }

    public double getSELLPRICE() {
        return SELLPRICE;
    }

    public int getTYPE() {
        return TYPE;
    }

    public int getURV() {
        return URV;
    }

    public int getISCOLLECTED() {
        return ISCOLLECTED;
    }

    public int getHASDESCRIBE() {
        return HASDESCRIBE;
    }

    public List<String> getIMAGES() {
        return IMAGES;
    }

    public int getISSELF() {
        return ISSELF;
    }

    public void setISSELF(int ISSELF) {
        this.ISSELF = ISSELF;
    }

    public int getISREBATE() {
        return ISREBATE;
    }

    public void setISREBATE(int ISREBATE) {
        this.ISREBATE = ISREBATE;
    }

    public double getPERCENT1() {
        return PERCENT1;
    }

    public void setPERCENT1(double PERCENT1) {
        this.PERCENT1 = PERCENT1;
    }

    public double getPERCENT2() {
        return PERCENT2;
    }

    public void setPERCENT2(double PERCENT2) {
        this.PERCENT2 = PERCENT2;
    }

    public double getPERCENT3() {
        return PERCENT3;
    }

    public void setPERCENT3(double PERCENT3) {
        this.PERCENT3 = PERCENT3;
    }

    public double getPERCENT4() {
        return PERCENT4;
    }

    public void setPERCENT4(double PERCENT4) {
        this.PERCENT4 = PERCENT4;
    }

    public double getPERCENT5() {
        return PERCENT5;
    }

    public void setPERCENT5(double PERCENT5) {
        this.PERCENT5 = PERCENT5;
    }
}
