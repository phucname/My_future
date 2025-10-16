package com.example.doan.Class;

import java.io.Serializable;

public class classBan implements Serializable {
    String masoban,soghe,idban;
    int pass;
    boolean TinhTrang;

    public String getMasoban() {
        return masoban;
    }

    public String getSoghe() {
        return soghe;
    }

    public String getIdBan() {
        return idban;
    }

    public boolean isTinhTrang() {
        return TinhTrang;
    }

    public int getPass() {
        return pass;
    }

    public classBan(String masoban, String soghe, String idban,boolean TinhTrang, int pass) {
        this.masoban = masoban;
        this.soghe = soghe;
        this.idban = idban;
        this.pass = pass;
        this.TinhTrang=TinhTrang;
    }

    public classBan(String masoban, String soghe, String idban,boolean TinhTrang) {
        this.masoban = masoban;
        this.soghe = soghe;
        this.idban = idban;
        this.TinhTrang=TinhTrang;

    }


}
