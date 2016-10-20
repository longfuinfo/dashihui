package com.dashihui.afford.business.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by NiuFC on 2015/12/24.
 */
public class EtyServerTime {


    /**
     * TITLE : 今日
     * DATE : 12月23日
     * HOURS : [{"TITLE":"11:00","DATETIME":"2015-12-22 11:00:00"},{"TITLE":"12:00","DATETIME":"2015-12-22 12:00:00"}]
     */

    private String TITLE;
    private String DATE;
    /**
     * TITLE : 11:00
     * DATETIME : 2015-12-22 11:00:00
     */

    private List<Map<String,Object>> HOURS;

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public void setHOURS(List<Map<String,Object>> HOURS) {
        this.HOURS = HOURS;
    }

    public String getTITLE() {
        return TITLE;
    }

    public String getDATE() {
        return DATE;
    }

    public List<Map<String,Object>> getHOURS() {
        return HOURS;
    }


}
