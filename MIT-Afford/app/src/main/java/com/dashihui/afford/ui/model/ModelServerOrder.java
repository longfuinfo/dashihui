package com.dashihui.afford.ui.model;

/**
 * Created by NiuFC on 2015/12/23.
 */
public class ModelServerOrder {

    private String storeid;            //商铺id
    private String storename;//商铺名称
    private String type;//服务类型
    private String linkname;            //收货人姓名
    private String sex;            //性别
    private String tel;            //电话
    private String address;            //收货地址
    private String sertime;//服务预订时间
    private String totaltime;//服务时长
    private String title;//服务标题
    private String unitprice;//服务单价
    private String count;//服务数量
    private String amount;            //总价格
    private String paytype;            //支付方式 1：在线支付，2：服务后付款
    private String describe;            //订单备注

    private String servicesid;            //服务项ID

    public String getStoreid() {
        return storeid;
    }

    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getSertime() {
        return sertime;
    }

    public void setSertime(String sertime) {
        this.sertime = sertime;
    }

    public String getTotaltime() {
        return totaltime;
    }

    public void setTotaltime(String totaltime) {
        this.totaltime = totaltime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(String unitprice) {
        this.unitprice = unitprice;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getServicesid() {
        return servicesid;
    }

    public void setServicesid(String servicesid) {
        this.servicesid = servicesid;
    }
}
