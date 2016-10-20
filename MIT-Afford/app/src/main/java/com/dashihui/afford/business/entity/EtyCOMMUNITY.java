package com.dashihui.afford.business.entity;

/**
 * Created by NiuFC on 2015/11/5.
 */
public class EtyCOMMUNITY {


    /**
     * ID : 1
     * TITLE : xxx小区
     * PROVINCENAME : 河南省
     * CITYNAME : 郑州市
     * AREANAME : 金水区
     * ADDRESS : 东风路南阳路交叉口向西50米路北
     * THUMB : /community/xxxx.jpg
     */

    private int ID;
    private String TITLE;
    private String PROVINCENAME;
    private String CITYNAME;
    private String AREANAME;
    private String ADDRESS;
    private String THUMB;

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
}
