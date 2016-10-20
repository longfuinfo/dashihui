package com.dashihui.afford.business.entity;

/**
 * Created by Administrator on 2016/2/19.
 */
public class EtyHomeSpecail {


    /**
     * MARKETPRICE : 4.9
     * SELLPRICE : 4.9
     * NAME : 北京二锅头白酒 鑫帝优致小酒版
     * THUMB : /goods/2015/12/10/E2927650E21844D9A4E49544E4081670.jpg
     * ID : 1
     */
    private double MARKETPRICE;
    private double SELLPRICE;
    private String NAME;
    private String THUMB;
    private int ID;

    public void setMARKETPRICE(double MARKETPRICE) {
        this.MARKETPRICE = MARKETPRICE;
    }

    public void setSELLPRICE(double SELLPRICE) {
        this.SELLPRICE = SELLPRICE;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public void setTHUMB(String THUMB) {
        this.THUMB = THUMB;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getMARKETPRICE() {
        return MARKETPRICE;
    }

    public double getSELLPRICE() {
        return SELLPRICE;
    }

    public String getNAME() {
        return NAME;
    }

    public String getTHUMB() {
        return THUMB;
    }

    public int getID() {
        return ID;
    }
}
