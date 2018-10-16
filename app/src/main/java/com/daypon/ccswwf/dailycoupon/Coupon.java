package com.daypon.ccswwf.dailycoupon;

import java.io.Serializable;

public class Coupon implements Serializable{

    private int id;
    private String imgUrl;
    private String name;
    private String shopName;
    private String useDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShopName() { return this.shopName; }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getUseDate() {
        return useDate;
    }

    public void setUseDate(String useDate) {
        this.useDate = useDate;
    }
}
