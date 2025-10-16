package com.example.doan.Adapter;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.Activity.Fragment_GioHang;
import com.example.doan.Activity.GoiMonActivity;
import com.example.doan.Activity.MianActivityBanac;
import com.example.doan.Class.MonAn;
import com.example.doan.Class.classHoaDon;
import com.example.doan.Class.class_combo;
import com.example.doan.R;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Adapter_Giohang extends BaseAdapter {
    Context context;

    int layout;
    TextView tong;
    ArrayList<MonAn>monAnArrayList;

    public ArrayList<MonAn> getMonAnArrayList() {
        return monAnArrayList;
    }

    public void setMonAnArrayList(ArrayList<MonAn> monAnArrayList) {
        this.monAnArrayList = monAnArrayList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    int position;
    int tong_tien=0;






    public Adapter_Giohang(Context context, int layout, TextView tong,ArrayList<MonAn>monAnArrayList) {
        this.context = context;

        this.layout = layout;
        this.tong=tong;
        this.monAnArrayList=monAnArrayList;
    }


    @Override
    public int getCount() {
        return monAnArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

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

        MonAn monAn=monAnArrayList.get(i);

            ten.setText(monAn.getTenmon());
            gia.setText("Giá:"+goiMonActivity.VDN(monAn.getGia()));
            soluong.setText(monAn.getSl());


               tong_tien=tong_tien+(Integer.parseInt(monAn.getGia())
                       *Integer.parseInt(monAn.getSl()));



//        Toast.makeText(context, tien+"///", Toast.LENGTH_SHORT).show();
//
            // tonggia.setText("Tổng Tiền:"+goiMonActivity.VDN(Integer.parseInt(hoaDon.getGia())*Integer.parseInt(hoaDon.getSoLuong())+""));
            Picasso.get().load(monAn.getImgmon()).into(anh);
          tong.setText(goiMonActivity.VDN(tong_tien+""));

            giamsl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ( Integer.parseInt(soluong.getText().toString())> 1) {
                       monAnArrayList.get(i).setSl((Integer.parseInt(monAnArrayList.get(i).getSl())-1)+"");
                       tong_tien=tong_tien-Integer.parseInt(monAn.getGia());
                        //  String gia1=gia.getText().toString();
                        ///  gia1=gia1.replaceAll("đ","");
                        //  String gia2=gia1.replaceAll(".","");
                        //Toast.makeText(getApplicationContext(),intent.getStringExtra("keyNH"),Toast.LENGTH_LONG).show();
                        tong.setText(goiMonActivity.VDN(tong_tien+""));
                        soluong.setText(monAnArrayList.get(i).getSl());

                    }

                }
            });
            tangsl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    monAnArrayList.get(i).setSl((Integer.parseInt(monAnArrayList.get(i).getSl())+1)+"");
                   tong_tien=tong_tien+Integer.parseInt(monAn.getGia());
                    tong.setText(goiMonActivity.VDN(tong_tien+""));
                    soluong.setText(monAnArrayList.get(i).getSl());
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setPosition(i);// Toast.makeText(context, position+"dđ", Toast.LENGTH_SHORT).show();
                    if(monAnArrayList.get(getPosition()).getLoai_mon().equals(0)){
                        File file;
                        ContextWrapper contextWrapper=new ContextWrapper(context);
                        File directory = contextWrapper.getDir("TK", Context.MODE_APPEND);//tên Thư Mục
                        file = new File(directory, "Gio_Hang.txt");
                        file.delete();
                        tong_tien=tong_tien -(Integer.parseInt(monAnArrayList.get(i).getGia())
                                *Integer.parseInt(monAnArrayList.get(i).getSl()));
                        tong.setText(goiMonActivity.VDN(tong_tien+""));

                        monAnArrayList.remove(i);
                        tong_tien=0;
                        notifyDataSetChanged();

                        try {
                            FileOutputStream outputStream=new FileOutputStream(file);
                            for (MonAn monAn1:monAnArrayList){
                              if(monAn1.getLoai_mon().equals("0")){
                                  outputStream.write((monAn1.getId()+","+monAn1.getSl()).getBytes());
                                  outputStream.write(("\n").getBytes());
                              }

                            } }catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {

                setPosition(i);// Toast.makeText(context, position+"dđ", Toast.LENGTH_SHORT).show();
                File file;
                ContextWrapper contextWrapper=new ContextWrapper(context);
                File directory = contextWrapper.getDir("TK", Context.MODE_APPEND);//tên Thư Mục
                file = new File(directory, "combo.txt");
                file.delete();
               tong_tien=tong_tien -(Integer.parseInt(monAnArrayList.get(i).getGia())
                        *Integer.parseInt(soluong.getText().toString()));
                tong.setText(goiMonActivity.VDN(tong_tien+""));

                monAnArrayList.remove(i);
               tong_tien=0;
                notifyDataSetChanged();

                        try {
                            FileOutputStream outputStream=new FileOutputStream(file);
                            for (MonAn monAn1:monAnArrayList){
                                if(monAn1.getLoai_mon().equals("1")){
                                    outputStream.write((monAn1.getId()+","+monAn1.getSl()).getBytes());
                                    outputStream.write(("\n").getBytes());
                                }

                            } }catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }catch (IOException e) {
                            e.printStackTrace();
                        }

                    }











                }
            });




//        monAnList.get(i).setCheckhd(false);
//        checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                     if (checkBox.isChecked()==true){
//                         monAnList.get(i).setCheckhd(true);
//                     }else  monAnList.get(i).setCheckhd(false);
//            }
//        });



        return view;
    }
}
