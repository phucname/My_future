package com.example.doan.Class;

import java.io.Serializable;

public class class_loai  implements Serializable {
    String id,ten;

    public class_loai(String id, String ten) {
        this.id = id;
        this.ten = ten;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }
}
