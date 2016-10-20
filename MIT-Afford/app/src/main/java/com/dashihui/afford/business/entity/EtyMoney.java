package com.dashihui.afford.business.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/6.
 */
public class EtyMoney {
    public Double getMONEY() {
        return MONEY;
    }

    public void setMONEY(Double MONEY) {
        this.MONEY = MONEY;
    }

    public int getINVITECOUNT() {
        return INVITECOUNT;
    }

    public void setINVITECOUNT(int INVITECOUNT) {
        this.INVITECOUNT = INVITECOUNT;
    }

    private Double MONEY;//用户金额
    private int INVITECOUNT;//用户邀请的好友数量

//    public int getPAGESIZE() {
//        return PAGESIZE;
//    }
//
//    public void setPAGESIZE(int PAGESIZE) {
//        this.PAGESIZE = PAGESIZE;
//    }
//
//    private int PAGENUMBER;//当前加载页码
//
//    public int getPAGENUMBER() {
//        return PAGENUMBER;
//    }
//
//    public void setPAGENUMBER(int PAGENUMBER) {
//        this.PAGENUMBER = PAGENUMBER;
//    }
//
//    private int PAGESIZE;//每页显示记录数量
//
//    public int getTOTALPAGE() {
//        return TOTALPAGE;
//    }
//
//    public void setTOTALPAGE(int TOTALPAGE) {
//        this.TOTALPAGE = TOTALPAGE;
//    }
//
//    private int TOTALPAGE;//总页数
//
//    public int getTOTALROW() {
//        return TOTALROW;
//    }
//
//    public void setTOTALROW(int TOTALROW) {
//        this.TOTALROW = TOTALROW;
//    }
//
//    private int TOTALROW;//总记录数
//
//    public List<Map<String, Object>> getLIST() {
//        return LIST;
//    }
//
//    public void setLIST(List<Map<String, Object>> LIST) {
//        this.LIST = LIST;
//    }
//
//    private List<Map<String,Object>> LIST;
}
