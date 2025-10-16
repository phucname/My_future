package com.example.doan.Class;

import java.io.Serializable;
import java.util.Date;

public class class_Bep implements Serializable {
    String img,tenmon,gia,TT,soluong,id;
    String date,daubep,soban;

    public String getDaubep() {
        return daubep;
    }

    public String getSoban() {
        return soban;
    }

    public void setSoban(String soban) {
        this.soban = soban;
    }

    public void setDaubep(String daubep) {
        this.daubep = daubep;
    }

    public class_Bep(String img, String tenmon, String gia, String TT, String soluong, String id, String date, String daubep,String soban) {
        this.img = img;
        this.tenmon = tenmon;
        this.gia = gia;
        this.TT = TT;
        this.soluong = soluong;
        this.id = id;
        this.date = date;
        this.daubep = daubep;
        this.soban=soban;
    }

    public class_Bep(String img, String tenmon, String gia, String TT, String soluong, String id) {
        this.img = img;
        this.tenmon = tenmon;
        this.gia = gia;
        this.TT = TT;
        this.soluong = soluong;
        this.id = id;
    }

    public class_Bep(String img, String tenmon, String gia, String TT, String soluong, String id, String date,String soban) {
        this.img = img;
        this.tenmon = tenmon;
        this.gia = gia;
        this.TT = TT;
        this.soluong = soluong;
        this.id = id;
        this.date=date;
        this.soban=soban;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public String getTT() {
        return TT;
    }

    public void setTT(String TT) {
        this.TT = TT;
    }

    public String getSoluong() {
        return soluong;
    }

    public void setSoluong(String soluong) {
        this.soluong = soluong;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
