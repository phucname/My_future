package com.example.doan.Class;

public class class_giamgia {
    String id,thoi_gian,gia_tri,gia_new;



    public class_giamgia() {
    }

    public class_giamgia(String gia_tri, String gia_new) {
        this.gia_tri = gia_tri;
        this.gia_new = gia_new;
    }

    public class_giamgia(String id, String thoi_gian, String gia_tri) {
        this.id = id;
        this.thoi_gian = thoi_gian;
        this.gia_tri = gia_tri;
    }
    public String getGia_new() {
        return gia_new;
    }

    public void setGia_new(String gia_new) {
        this.gia_new = gia_new;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThoi_gian() {
        return thoi_gian;
    }

    public void setThoi_gian(String thoi_gian) {
        this.thoi_gian = thoi_gian;
    }

    public String getGia_tri() {
        return gia_tri;
    }

    public void setGia_tri(String gia_tri) {
        this.gia_tri = gia_tri;
    }
}
