package com.example.doan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.Activity.GoiMonActivity;
import com.example.doan.Class.MonAn;
import com.example.doan.Class.classHoaDon;
import com.example.doan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class adapter_dsmon_themcombo extends BaseAdapter {
    Context context;
    List<classHoaDon> monAnList;
    int layout;
    ArrayList<MonAn> monAnArrayList;
    Adaptermenu adaptermenu;
    public List<classHoaDon> getMonAnList() {
        return monAnList;
    }

    public void setMonAnList(List<classHoaDon> monAnList) {
        this.monAnList = monAnList;
    }



    public ArrayList<MonAn> getMonAnArrayList() {
        return monAnArrayList;
    }

    public void setMonAnArrayList(ArrayList<MonAn> monAnArrayList) {
        this.monAnArrayList = monAnArrayList;
    }

    public adapter_dsmon_themcombo(Context context,
                                   List<classHoaDon> monAnList,
                                   int layout, ArrayList<MonAn> monAnArrayList
                                   ,Adaptermenu adaptermenu) {
        this.context = context;
        this.monAnList = monAnList;
        this.layout = layout;
        this.adaptermenu=adaptermenu;
        this.monAnArrayList = monAnArrayList;
    }

    @Override
    public int getCount() {
        return monAnArrayList.size();
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
    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=layoutInflater.inflate(layout,null);
        TextView ten=view.findViewById(R.id.hoadontenmon);
        TextView gia=view.findViewById(R.id.hoadongia);
        TextView soluong=view.findViewById(R.id.text_sl_giohang);
        ImageView giamsl=view.findViewById(R.id.img_giamsl_giohang);
        ImageView tangsl=view.findViewById(R.id.img_tangsl_giohang);
        ImageView delete=view.findViewById(R.id.img_delete_itemgiohang);
        // TextView tonggia=view.findViewById(R.id.tonggiamon);
        ImageView anh=view.findViewById(R.id.hoadonimg);
        GoiMonActivity goiMonActivity=new GoiMonActivity();
        classHoaDon hoaDon=monAnList.get(i);
        MonAn monAn=monAnArrayList.get(i);

        ten.setText(monAn.getTenmon());
        gia.setText("Giá:"+goiMonActivity.VDN(monAn.getGia()));
        soluong.setText(hoaDon.getSoluong());
        // tonggia.setText("Tổng Tiền:"+goiMonActivity.VDN(Integer.parseInt(hoaDon.getGia())*Integer.parseInt(hoaDon.getSoLuong())+""));
        Picasso.get().load(monAn.getImgmon()).into(anh);


        giamsl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(soluong.getText().toString()) > 1) {
                    soluong.setText((Integer.parseInt(soluong.getText().toString()) - 1) + "");
                    monAnList.get(i).setSoluong(soluong.getText().toString());

                    //  String gia1=gia.getText().toString();
                    ///  gia1=gia1.replaceAll("đ","");
                    //  String gia2=gia1.replaceAll(".","");
                    //Toast.makeText(getApplicationContext(),intent.getStringExtra("keyNH"),Toast.LENGTH_LONG).show();

                }

            }
        });
        tangsl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soluong.setText((Integer.parseInt(soluong.getText().toString()) + 1) + "");

                monAnList.get(i).setSoluong(soluong.getText().toString());
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int dem=0;
                 for (MonAn monAn1:adaptermenu.monAnList){
                     if(monAn1.getId().equals(monAnArrayList.get(i).getId())){
                         adaptermenu.monAnList.get(dem).setCheck(false);
                     }
                     dem++;
                 }
                monAnArrayList.remove(i);
                notifyDataSetChanged();
                adaptermenu.notifyDataSetChanged();

            }
        });
        return view;
    }
}
