package com.dashihui.afford.ui;

import com.dashihui.afford.business.entity.EntityLocation;

/**
 * Created by NiuFC on 2015/11/7.
 */
public class AffordBean {
    /**手机注册唯一标识*/
    private  String SIGNATURE = "";




    private static AffordBean instance;
    private AffordBean()
    {
    }

    /**
     * 单一实例
     */
    public static AffordBean getAppBean()
    {
        if (instance == null)
        {
            instance = new AffordBean();
        }
        return instance;
    }

    public String getSIGNATURE() {
        return SIGNATURE;
    }

    public void setSIGNATURE(String SIGNATURE) {
        this.SIGNATURE = SIGNATURE;
    }



    public static AffordBean getInstance() {
        return instance;
    }

    public static void setInstance(AffordBean instance) {
        AffordBean.instance = instance;
    }




}
