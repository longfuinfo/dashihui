package com.dashihui.afford.business.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by NiuFC on 2015/12/1.
 */
public class EtyList {


    /**
     * PAGENUMBER : 1
     * PAGESIZE : 15
     * TOTALPAGE : 3
     * TOTALROW : 40
     * LIST : [{"ID":1,"NAME":"大白菜","SPEC":"个","SHORTINFO":"东北大白菜，产自东北....","PRICE":0.8,"THUMB":"/goods/xxx.jpg","PURCHASEAMOUNT":0,"TYPE":2,"STICKTOP":1,"FAVOPRICE":0}]
     */

    private int LISTMAPTYPE;//数据分类
    private int PAGENUMBER;//当前加载页码
    private int PAGESIZE;//每页显示记录数量
    private int TOTALPAGE;//总页数
    private int TOTALROW;//总记录数
    /**
     * ID : 1
     * NAME : 大白菜
     * SPEC : 个
     * SHORTINFO : 东北大白菜，产自东北....
     * PRICE : 0.8
     * THUMB : /goods/xxx.jpg
     * PURCHASEAMOUNT : 0
     * TYPE : 2
     * STICKTOP : 1
     * FAVOPRICE : 0
     */

    private List<Map<String,Object>> LIST;

    public void setPAGENUMBER(int PAGENUMBER) {
        this.PAGENUMBER = PAGENUMBER;
    }

    public void setPAGESIZE(int PAGESIZE) {
        this.PAGESIZE = PAGESIZE;
    }

    public void setTOTALPAGE(int TOTALPAGE) {
        this.TOTALPAGE = TOTALPAGE;
    }

    public void setTOTALROW(int TOTALROW) {
        this.TOTALROW = TOTALROW;
    }

    public void setLIST(List<Map<String,Object>> LIST) {
        this.LIST = LIST;
    }

    public int getPAGENUMBER() {
        return PAGENUMBER;
    }

    public int getPAGESIZE() {
        return PAGESIZE;
    }

    public int getTOTALPAGE() {
        return TOTALPAGE;
    }

    public int getTOTALROW() {
        return TOTALROW;
    }

    public List<Map<String,Object>> getLIST() {
        return LIST;
    }


}
