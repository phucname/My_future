package com.example.doan.Class;

import java.io.Serializable;

public class class_combo implements Serializable {
    String id_combo,gia_combo,ten_combo,img_combo;
    int loai_giamgia;

    public int getLoai_giamgia() {
        return loai_giamgia;
    }

    public void setLoai_giamgia(int loai_giamgia) {
        this.loai_giamgia = loai_giamgia;
    }

    public class_combo(String id_combo, String gia_combo, String ten_combo, String img_combo, int loai_giamgia) {
        this.id_combo = id_combo;
        this.gia_combo = gia_combo;
        this.ten_combo = ten_combo;
        this.img_combo=img_combo;
        this.loai_giamgia=loai_giamgia;
    }

    public String getImg_combo() {
        return img_combo;
    }

    public void setImg_combo(String img_combo) {
        this.img_combo = img_combo;
    }

    public String getId_combo() {
        return id_combo;
    }

    public void setId_combo(String id_combo) {
        this.id_combo = id_combo;
    }

    public String getGia_combo() {
        return gia_combo;
    }

    public void setGia_combo(String gia_combo) {
        this.gia_combo = gia_combo;
    }

    public String getTen_combo() {
        return ten_combo;
    }

    public void setTen_combo(String ten_combo) {
        this.ten_combo = ten_combo;
    }
}
