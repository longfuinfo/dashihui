package com.dashihui.afford.business.entity;

/**
 * 定位返回商铺和小区信息
 * Created by NiuFC on 2015/11/5.
 */
public class EntityLocation {

        /**
         * CITYNAME : 市辖区
         * STOREID : 2
         * CODE : 110101001
         * AREA : 378
         * CITY : 33
         * PROVINCENAME : 北京市
         * ADDRESS : 北京市东城区xx路xx号东城小区1号院
         * THUMB : 26b9de05020248718883f3019888200e.jpg
         * ORDERNO : 2
         * TITLE : 东城小区1号
         * PROVINCE : 2
         * AREANAME : 东城区
         * ID : 1
         * BAIDUKEY : 1
         */

        private EtyCOMMUNITY COMMUNITY;
        /**
         * CITYNAME : 市辖区
         * CODE : 11010102
         * AREA : 378
         * CITY : 33
         * PROVINCENAME : 北京市
         * ADDRESS : 大实惠东城区1号
         * THUMB : null
         * ORDERNO : 2
         * TITLE : 大实惠东城区2号
         * PROVINCE : 2
         * AREANAME : 东城区
         * ID : 2
         */

        private EtySTORE STORE;

        public void setCOMMUNITY(EtyCOMMUNITY COMMUNITY) {
            this.COMMUNITY = COMMUNITY;
        }

        public void setSTORE(EtySTORE STORE) {
            this.STORE = STORE;
        }

        public EtyCOMMUNITY getCOMMUNITY() {
            return COMMUNITY;
        }

        public EtySTORE getSTORE() {
            return STORE;
        }

}
