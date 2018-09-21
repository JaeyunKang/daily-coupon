package com.daypon.ccswwf.dailycoupon;

import java.io.Serializable;
import java.util.ArrayList;

public class Shop implements Serializable {

    private int id;
    private String name;
    private String imgUrl;
    private int visitNum;
    private String location;
    private String description;
    private String notice;
    private String naverUrl;
    private String address;
    private ArrayList<Coupon> coupons;
    private ArrayList<String> openings;
    private String openingNotice;
    private int subImgNum;

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getVisitNum() {
        return visitNum;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getNotice() {
        return notice;
    }

    public String getNaverUrl() {
        return naverUrl;
    }

    public String getAddress() {
        return address;
    }

    public ArrayList<Coupon> getCoupons() {
        return coupons;
    }

    public ArrayList<String> getOpenings() {
        return openings;
    }

    public String getOpeningNotice() {
        return openingNotice;
    }

    public int getSubImgNum() {
        return subImgNum;
    }

    public int getId() { return id; }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setVisitNum(int visitNum) {
        this.visitNum = visitNum;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public void setNaverUrl(String naverUrl) {
        this.naverUrl = naverUrl;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCoupons(ArrayList<Coupon> coupons) {
        this.coupons = coupons;
    }

    public void setOpenings(ArrayList<String> openings) {
        this.openings = openings;
    }

    public void setOpeningNotice(String openingNotice) {
        this.openingNotice = openingNotice;
    }

    public void setSubImgNum(int subImgNum) {
        this.subImgNum = subImgNum;
    }

    public void setId(int id) { this.id = id; }
}
