package com.dashihui.afford.business.entity;

/**
 * Created by NiuFC on 2015/11/20.
 */
public class EtyLogin {

    /**
     * TOKEN : 5c81466482b745c0afbe9b1af280d0543471
     * USER : {"ID":1,"NICKNAME":"我是大实惠粉丝一号","SEX":1,"AVATOR":"/avator/xxx.jpg","COMMUNITYID":1,"COMMUNITYNAME":"xxx小区","BUILDID":2,"BUILDNAME":"1#楼","UNITID":3,"UNITNAME":"1单元","ROOMID":4,"ROOMNAME":"101室"}
     */

    private String TOKEN;
    /**
     * ID : 1
     * NICKNAME : 我是大实惠粉丝一号
     * SEX : 1
     * AVATOR : /avator/xxx.jpg
     * COMMUNITYID : 1
     * COMMUNITYNAME : xxx小区
     * BUILDID : 2
     * BUILDNAME : 1#楼
     * UNITID : 3
     * UNITNAME : 1单元
     * ROOMID : 4
     * ROOMNAME : 101室
     */

    private USEREntity USER;

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public void setUSER(USEREntity USER) {
        this.USER = USER;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public USEREntity getUSER() {
        return USER;
    }

    /**
     * 内部类实体
     */
    public static class USEREntity {

        /**
         * ID : 1
         * NICKNAME : 我是大实惠粉丝一号
         * SEX : 1
         * AVATOR : /avator/xxx.jpg
         * COMMUNITYID : 1
         * COMMUNITYNAME : xxx小区
         * BUILDID : 2
         * BUILDNAME : 1#楼
         * UNITID : 3
         * UNITNAME : 1单元
         * ROOMID : 4
         * ROOMNAME : 101室
         */

        private int ID;
        private String NICKNAME;
        private int SEX;
        private String AVATOR;
        private int COMMUNITYID;
        private String COMMUNITYNAME;
        private int BUILDID;
        private String BUILDNAME;
        private int UNITID;
        private String UNITNAME;
        private int ROOMID;
        private String ROOMNAME;
        private String MSISDN;
        private int LEVEL;//1.普通用户，2.白金会员

        public void setID(int ID) {
            this.ID = ID;
        }

        public void setNICKNAME(String NICKNAME) {
            this.NICKNAME = NICKNAME;
        }

        public void setSEX(int SEX) {
            this.SEX = SEX;
        }

        public void setAVATOR(String AVATOR) {
            this.AVATOR = AVATOR;
        }

        public void setCOMMUNITYID(int COMMUNITYID) {
            this.COMMUNITYID = COMMUNITYID;
        }

        public void setCOMMUNITYNAME(String COMMUNITYNAME) {
            this.COMMUNITYNAME = COMMUNITYNAME;
        }

        public void setBUILDID(int BUILDID) {
            this.BUILDID = BUILDID;
        }

        public void setBUILDNAME(String BUILDNAME) {
            this.BUILDNAME = BUILDNAME;
        }

        public void setUNITID(int UNITID) {
            this.UNITID = UNITID;
        }

        public void setUNITNAME(String UNITNAME) {
            this.UNITNAME = UNITNAME;
        }

        public void setROOMID(int ROOMID) {
            this.ROOMID = ROOMID;
        }

        public void setROOMNAME(String ROOMNAME) {
            this.ROOMNAME = ROOMNAME;
        }
        public void setMSISDN(String MSISDN) {
            this.MSISDN = MSISDN;
        }

        public int getID() {
            return ID;
        }
        public void setLEVEL(int LEVEL) {
            this.LEVEL = LEVEL;
        }
        public int getLEVEL() {
            return LEVEL;
        }

        public String getNICKNAME() {
            return NICKNAME;
        }

        public String getMSISDN() {
            return MSISDN;
        }

        public int getSEX() {
            return SEX;
        }

        public String getAVATOR() {
            return AVATOR;
        }

        public int getCOMMUNITYID() {
            return COMMUNITYID;
        }

        public String getCOMMUNITYNAME() {
            return COMMUNITYNAME;
        }

        public int getBUILDID() {
            return BUILDID;
        }

        public String getBUILDNAME() {
            return BUILDNAME;
        }

        public int getUNITID() {
            return UNITID;
        }

        public String getUNITNAME() {
            return UNITNAME;
        }

        public int getROOMID() {
            return ROOMID;
        }

        public String getROOMNAME() {
            return ROOMNAME;
        }
    }
}
