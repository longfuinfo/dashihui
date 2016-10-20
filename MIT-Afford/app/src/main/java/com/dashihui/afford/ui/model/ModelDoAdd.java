package com.dashihui.afford.ui.model;

/**
 * Created by NiuFC on 2015/11/25.
 */
public class ModelDoAdd {
    /**
     * @param storeid 商铺id
     * @param goodsid 商品id
     * @param count 数量
     * @param paytype 支付方式
     * 1：在线支付，2：货到付款
     * @param taketype 收货方式
     * 1：送货，2：自取
     * @param linkname 收货人姓名
     * @param sex 性别
     * @param tel 电话
     * @param address  地址
     * @param describe 订单备注
     */
    private String storeid;// 商铺id
    private String selfcounts;// 直营商品数量
    private String storecounts;// 店铺商品数量
    private String storeamount;// 店铺商品总金额
    private String selfamount;// 自营商品总金额
    private String paytypeself;//  直营支付方式1：在线支付，2：货到付款 默认为1
    private String paytypeshop;//  门店支付方式1：在线支付，2：货到付款
    private String taketypeself;//  直营收货方式 1：送货，2：自取
    private String taketypeshop;//  门店收货方式 1：送货，2：自取
    private String linkname;//  收货人姓名
    private String sex;// 性别
    private String tel;//  电话
    private String address;//  社区
    private String describe;// 订单备注
    private String selfgoodsids;// 直营商品ID
    private String storegoodsids;// 店铺商品ID

    public String getIsredeem() {
        return isredeem;
    }

    public void setIsredeem(String isredeem) {
        this.isredeem = isredeem;
    }

    private String isredeem;//是否用实惠币抵用  0：否  1：是
    private String payType;
    private String takeType;

    private String amount;//总价格

    /***
     * 商品数量“，”号分隔
     ***/

    public String getStorecounts() {
        return storecounts;
    }

    public void setStorecounts(String storecounts) {
        this.storecounts = storecounts;
    }

    public String getSelfcounts() {
        return selfcounts;
    }

    public void setSelfcounts(String selfcounts) {
        this.selfcounts = selfcounts;
    }


    public String getStoreamount() {
        return storeamount;
    }

    public void setStoreamount(String storeamount) {
        this.storeamount = storeamount;
    }

    public String getSelfamount() {
        return selfamount;
    }

    public void setSelfamount(String selfamount) {
        this.selfamount = selfamount;
    }



    public String getStoreid() {
        return storeid;
    }

    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }

    public void setTaketype(String takeType) {
        this.takeType = takeType;
    }

    public String getTaketype() {
        return takeType;
    }

    public String getSelfgoodsids() {
        return selfgoodsids;
    }

    public void setSelfgoodsids(String selfgoodsids) {
        this.selfgoodsids = selfgoodsids;
    }

    public String getPaytype() {
        return payType;
    }

    public void setPaytype(String payType) {
        this.payType = payType;
    }

    public String getStoregoodsids() {
        return storegoodsids;
    }

    public void setStoregoodsids(String storegoodsids) {
        this.storegoodsids = storegoodsids;
    }

    public String getPaytypeself() {
        return paytypeself;
    }

    public String getTaketypeself() {
        return taketypeself;
    }

    public void setTaketypeself(String taketypeself) {
        this.taketypeself = taketypeself;
    }

    public String getTaketypeshop() {
        return taketypeshop;
    }

    public void setTaketypeshop(String taketypeshop) {
        this.taketypeshop = taketypeshop;
    }


    public void setPaytypeself(String paytypeself) {
        this.paytypeself = paytypeself;
    }

    public String getPaytypeshop() {
        return paytypeshop;
    }

    public void setPaytypeshop(String paytypeshop) {
        this.paytypeshop = paytypeshop;
    }


    public String getLinkname() {
        return linkname;
    }

    public void setLinkname(String linkname) {
        this.linkname = linkname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
