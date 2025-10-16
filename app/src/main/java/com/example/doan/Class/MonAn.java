package com.example.doan.Class;

import java.io.Serializable;

public class MonAn implements  Serializable {
    String tenmon;
    String gia;
    String ghichu;
    String imgmon;
    String id;
    String loai_mon;
    Boolean Check;
    String sl;
    int loai_giamgia;

    public int getLoai_giamgia() {
        return loai_giamgia;
    }

    public void setLoai_giamgia(int loai_giamgia) {
        this.loai_giamgia = loai_giamgia;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public MonAn(String tenmon, String gia, String ghichu, String imgmon, String id,String loai_mon,int loai_giamgia) {
        this.tenmon = tenmon;
        this.gia = gia;
        this.ghichu = ghichu;
        this.imgmon = imgmon;
        this.id = id;
        this.loai_mon=loai_mon;
        this.loai_giamgia=loai_giamgia;

    }
    public MonAn(String tenmon, String gia, String ghichu, String imgmon, String id,String sl,String loai_mon) {
        this.tenmon = tenmon;
        this.gia = gia;
        this.ghichu = ghichu;
        this.imgmon = imgmon;
        this.id = id;
        this.sl=sl;
        this.loai_mon=loai_mon;
    }

    public MonAn() {
    }

    public MonAn(String tenmon, String gia, String ghichu, String imgmon, int loai_giamgia) {
        this.tenmon = tenmon;
        this.gia = gia;
        this.ghichu = ghichu;
        this.imgmon = imgmon;
this.loai_giamgia=loai_giamgia;
    }

    public MonAn(String tenmon, String gia, String imgmon, String id, Boolean check) {
        this.tenmon = tenmon;
        this.gia = gia;
        this.imgmon = imgmon;
        this.id = id;
        Check = check;
    }
    public MonAn(String tenmon, String sl, String id) {
        this.tenmon = tenmon;
       this.sl=sl;

        this.id = id;

    }

    public String getLoai_mon() {
        return loai_mon;
    }

    public void setLoai_mon(String loai_mon) {
        this.loai_mon = loai_mon;
    }

    public Boolean getCheck() {
        return Check;
    }

    public void setCheck(Boolean check) {
        Check = check;
    }

    public String getTenmon() {
        return tenmon;
    }

    public void setTenmon(String tenmon) {
        this.tenmon = tenmon;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public String getImgmon() {
        return imgmon;
    }

    public void setImgmon(String imgmon) {
        this.imgmon = imgmon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}



