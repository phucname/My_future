package com.example.doan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.doan.Activity.MianActivityBanac;
//import com.example.doan.Activity.chitiet_history;
import com.example.doan.Activity.chitiet_history;
import com.example.doan.Class.NhanVien;
import com.example.doan.Class.classBan;
import com.example.doan.Class.classHoaDon;
import com.example.doan.Class.classhistory;
import com.example.doan.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_history extends BaseAdapter {
    Context context;
    List<classhistory> danhsach_history;
    ArrayList<classBan>banArrayList;
    ArrayList<NhanVien>nhanVienArrayList;
    int layout;

    public Adapter_history(Context context, List<classhistory> danhsach_history, int layout, ArrayList<classBan>banArrayList,
            ArrayList<NhanVien>nhanVienArrayList) {
        this.context = context;
        this.danhsach_history = danhsach_history;
        this.layout = layout;
        this.banArrayList=banArrayList;
        this.nhanVienArrayList=nhanVienArrayList;
    }

    @Override
    public int getCount() {
        return danhsach_history.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=layoutInflater.inflate(layout,null);
        TextView tennhanvien=convertView.findViewById(R.id.text_nvthu_history);
        TextView tongbill=convertView.findViewById(R.id.text_tongbill_history);
        TextView maban=convertView.findViewById(R.id.text_maban_history);
        TextView date=convertView.findViewById(R.id.text_date_history);
        Button button=convertView.findViewById(R.id.button_chitiet_history);
        classhistory classhistory=danhsach_history.get(danhsach_history.size()-1-position);
        classBan classBan=banArrayList.get(banArrayList.size()-1-position);
        NhanVien classnhanvien=nhanVienArrayList.get(nhanVienArrayList.size()-1-position);
        tennhanvien.setText("Tên nhân viên thu :"+classnhanvien.getTenNV());
        tongbill.setText("Tổng bill:"+classhistory.getTong_bill());
        maban.setText("Mã bàn:"+classBan.getMasoban());
        date.setText("Thời Gian:"+classhistory.getDate());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i=new Intent(context, chitiet_history.class);
                MianActivityBanac mianActivityBanac = (MianActivityBanac) context;
                i.putExtra("id_nhahang",mianActivityBanac.getId_nhahang());
                i.putExtra("id_chitiet",classhistory.getId());
                context.startActivity(i);

            }
        });
        return convertView;
    }
}
