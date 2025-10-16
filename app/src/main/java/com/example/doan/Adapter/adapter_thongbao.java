package com.example.doan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.doan.Class.class_thongbao;
import com.example.doan.R;

import java.util.ArrayList;

public class adapter_thongbao extends BaseAdapter {
   ArrayList<class_thongbao>arrayListthongbao;
   Context mcontext;
    private final  int RESOURCE_ID;

    public adapter_thongbao(ArrayList<class_thongbao> arrayListthongbao, Context mcontext, int RESOURCE_ID) {
        this.arrayListthongbao = arrayListthongbao;
        this.mcontext = mcontext;
        this.RESOURCE_ID = RESOURCE_ID;
    }

    @Override
    public int getCount() {
        return arrayListthongbao.size();
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
      View view= LayoutInflater.from(mcontext).inflate(RESOURCE_ID,parent,false);
        TextView date=view.findViewById(R.id.text_item_thongbao_date);
        TextView noidung=view.findViewById(R.id.text_item_thongbao_noidung);
        class_thongbao classThongbao=arrayListthongbao.get(position);
        String[] a =classThongbao.getDate().split(",");
        date.setText(a[0]+", Ngày:"+a[1]);
        noidung.setText("Nội dung:"+classThongbao.getNoidung());
        return view;
    }
}
