package com.example.doan.Class;

public class class_thongbao {
    String id,noidung,date;

    public class_thongbao(String id, String noidung, String date) {
        this.id = id;
        this.noidung = noidung;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
