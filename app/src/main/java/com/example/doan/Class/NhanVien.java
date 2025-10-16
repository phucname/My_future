package com.example.doan.Class;

import java.io.Serializable;

public class NhanVien implements Serializable {

    String TenNV, sdtnv, id, anhnv,mail,pass;
    int ChucVuNV;

    public NhanVien(String tenNV, String sdtnv, String id, String anhnv, int chucVuNV,String mail) {
        TenNV = tenNV;
        this.sdtnv = sdtnv;
        this.id = id;
        this.anhnv = anhnv;
        ChucVuNV = chucVuNV;
        this.mail=mail;
       // this.pass=pass;
    }

    public String getPass() {
        return pass;
    }

    public String getTenNV() {
        return TenNV;
    }

    public String getSdtnv() {
        return sdtnv;
    }

    public String getId() {
        return id;
    }

    public String getAnhnv() {
        return anhnv;
    }

    public int getChucVuNV() {
        return ChucVuNV;
    }

    public String getMail() {
        return mail;
    }
}
