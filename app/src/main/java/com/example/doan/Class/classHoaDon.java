package com.example.doan.Class;

import java.io.Serializable;

public class classHoaDon implements Serializable {



    String daubep,id_ban,date_goimon;
    String soluong,id_mon,tinhtrang,id;
Integer loai;
String gia,chat;

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getDaubep() {
        return daubep;
    }

    public Integer getLoai() {
        return loai;
    }

    public void setLoai(Integer loai) {
        this.loai = loai;
    }

    public void setDaubep(String daubep) {
        this.daubep = daubep;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public classHoaDon(String id, String id_ban, String tinhtrang , String soluong, String id_mon, String date_goimon, Integer loaimon,String chat) {
        this.id_ban = id_ban;
        this.tinhtrang=tinhtrang;
        this.soluong = soluong;
        this.id_mon = id_mon;
        this.date_goimon = date_goimon;
        this.loai=loaimon;
        this.id=id;
        this.chat=chat;
    }

    public classHoaDon(String daubep, String id_ban, String date_goimon, String soluong, String id_mon, String tinhtrang,String chat) {
        this.daubep = daubep;
        this.id_ban = id_ban;
        this.date_goimon = date_goimon;
        this.soluong = soluong;
        this.id_mon = id_mon;
        this.tinhtrang = tinhtrang;    this.chat=chat;
    }
    public classHoaDon(String id_ban,String date_goimon,String soluong,String id_mon,String tinhtrang) {

        this.id_ban = id_ban;
        this.date_goimon = date_goimon;
        this.soluong = soluong;
        this.id_mon = id_mon;
        this.tinhtrang = tinhtrang;

    }


    public String getId_ban() {
        return id_ban;
    }

    public void setId_ban(String id) {
        this.id_ban = id;
    }

    public String getDate_goimon() {
        return date_goimon;
    }

    public void setDate_goimon(String date_goimon) {
        this.date_goimon = date_goimon;
    }

    public String getSoluong() {
        return soluong;
    }

    public void setSoluong(String soluong) {
        this.soluong = soluong;
    }

    public String getId_mon() {
        return id_mon;
    }

    public void setId_mon(String id_mon) {
        this.id_mon = id_mon;
    }

    public String getTinhtrang() {
        return tinhtrang;
    }

    public void setTinhtrang(String tinhtrang) {
        this.tinhtrang = tinhtrang;
    }

    public classHoaDon(String id, String soLuong ,String gia) {
        soluong = soLuong;
        this.id_mon = id;
        this.gia=gia;
    }
    public classHoaDon(String id, String soLuong ) {
        soluong = soLuong;
        this.id_mon = id;

    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public classHoaDon() {
    }
}
