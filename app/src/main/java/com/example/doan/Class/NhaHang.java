package com.example.doan.Class;

public class NhaHang {
    String ten_nhahang, diachi_nhahang, _sdt_nhahangi, mg_nhahang, id;
    String Huyen,Tinh,Xa;

    public NhaHang(String ten_nhahang, String diachi_nhahang, String _sdt_nhahangi,
                   String mg_nhahang, String huyen, String tinh, String xa) {
        this.ten_nhahang = ten_nhahang;
        this.diachi_nhahang = diachi_nhahang;
        this._sdt_nhahangi = _sdt_nhahangi;
        this.mg_nhahang = mg_nhahang;

        this.Huyen = huyen;
        this.Tinh = tinh;
        this.Xa = xa;
    }

    public String getHuyen() {
        return Huyen;
    }

    public void setHuyen(String huyen) {
        Huyen = huyen;
    }

    public String getTinh() {
        return Tinh;
    }

    public void setTinh(String tinh) {
        Tinh = tinh;
    }

    public String getXa() {
        return Xa;
    }

    public void setXa(String xa) {
        Xa = xa;
    }

    public String getTen_nhahang() {
        return ten_nhahang;
    }

    public void setTen_nhahang(String ten_nhahang) {
        this.ten_nhahang = ten_nhahang;
    }

    public String getDiachi_nhahang() {
        return diachi_nhahang;
    }

    public void setDiachi_nhahang(String diachi_nhahang) {
        this.diachi_nhahang = diachi_nhahang;
    }

    public String get_sdt_nhahangi() {
        return _sdt_nhahangi;
    }

    public void set_sdt_nhahangi(String _sdt_nhahangi) {
        this._sdt_nhahangi = _sdt_nhahangi;
    }

    public String getMg_nhahang() {
        return mg_nhahang;
    }

    public void setMg_nhahang(String mg_nhahang) {
        this.mg_nhahang = mg_nhahang;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
