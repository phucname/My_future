package com.example.doan.Class;

import java.io.Serializable;

public class classThongKe implements Serializable {
    String gia,soluong,id;

    public classThongKe(String gia, String soluong,String id) {
        this.gia=gia;
        this.soluong = soluong;
        this.id=id;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public void setSoluong(String soluong) {
        this.soluong = soluong;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getGia() {
        return gia;
    }

    public String getSoluong() {
        return soluong;
    }
}
