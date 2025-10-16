package com.example.doan.Class;

public class classhistory {
    String id,ten_nv,ma_ban,tong_bill,date;

    public classhistory(String id,String tong_bill,String date) {
        this.id = id;
    this.date=date;
        this.tong_bill = tong_bill;
    }

    public classhistory() {
    }

    public classhistory(String id, String ten_nv, String ma_ban, String tong_bill, String date) {
        this.id = id;
        this.ten_nv = ten_nv;
        this.ma_ban = ma_ban;
        this.tong_bill = tong_bill;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTen_nv() {
        return ten_nv;
    }

    public void setTen_nv(String ten_nv) {
        this.ten_nv = ten_nv;
    }

    public String getMa_ban() {
        return ma_ban;
    }

    public void setMa_ban(String ma_ban) {
        this.ma_ban = ma_ban;
    }

    public String getTong_bill() {
        return tong_bill;
    }

    public void setTong_bill(String tong_bill) {
        this.tong_bill = tong_bill;
    }
}
