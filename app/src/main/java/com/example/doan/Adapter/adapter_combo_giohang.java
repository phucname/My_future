package com.example.doan.Adapter;

import android.content.Context;
import android.content.ContextWrapper;
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
import com.example.doan.Class.class_combo;
import com.example.doan.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class adapter_combo_giohang extends BaseAdapter {
    Context context;
    List<classHoaDon> comboarray1;
    int layout;
    TextView tong;

    public ArrayList<class_combo> getClass_comboArrayList() {
        return class_comboArrayList;
    }

    public void setClass_comboArrayList(ArrayList<class_combo> class_comboArrayList) {
        this.class_comboArrayList = class_comboArrayList;
    }

    ArrayList<class_combo> class_comboArrayList;
    int position;
    public int getPosition() {
        return position;
    }

    public adapter_combo_giohang(Context context
            , List<classHoaDon> comboarray1
            , int layout, ArrayList<class_combo> class_comboArrayList,TextView tong) {
        this.context = context;
        this.comboarray1 = comboarray1;
        this.layout = layout;
this.tong=tong;
        this.class_comboArrayList = class_comboArrayList;
    }

    public void setPosition(int position) {
        this.position = position;
    }



    @Override
    public int getCount() {
        return class_comboArrayList.size();
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
        classHoaDon hoaDon=comboarray1.get(i);
        class_combo combo=class_comboArrayList.get(i);

        ten.setText(combo.getTen_combo());
        gia.setText("Giá:"+goiMonActivity.VDN(combo.getGia_combo()));
        soluong.setText(hoaDon.getSoluong());
        String tongs=tong.getText().toString().replaceAll(".","");
       // tongs=tongs.replaceAll("đ","");

       // int tien=Integer.parseInt(tongs)+(Integer.parseInt(combo.getGia_combo())*Integer.parseInt(hoaDon.getSoluong()));
        Toast.makeText(context, tongs+"...", Toast.LENGTH_SHORT).show();
        // tonggia.setText("Tổng Tiền:"+goiMonActivity.VDN(Integer.parseInt(hoaDon.getGia())*Integer.parseInt(hoaDon.getSoLuong())+""));
        Picasso.get().load(combo.getImg_combo()).into(anh);
             // tong.setText(goiMonActivity.VDN(tien+""));

        giamsl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(soluong.getText().toString()) > 1) {
                    soluong.setText((Integer.parseInt(soluong.getText().toString()) - 1) + "");
                    comboarray1.get(i).setSoluong(soluong.getText().toString());
                   // tien=tien-Integer.parseInt(monAn.getGia());
                    //  String gia1=gia.getText().toString();
                    ///  gia1=gia1.replaceAll("đ","");
                    //  String gia2=gia1.replaceAll(".","");
                    //Toast.makeText(getApplicationContext(),intent.getStringExtra("keyNH"),Toast.LENGTH_LONG).show();
                   // tong.setText(goiMonActivity.VDN(tien+""));
                }

            }
        });
        tangsl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soluong.setText((Integer.parseInt(soluong.getText().toString()) + 1) + "");
//                tien=tien+Integer.parseInt(monAn.getGia());
//                tong.setText(goiMonActivity.VDN(tien+""));
              comboarray1.get(i).setSoluong(soluong.getText().toString());
            }
        });

//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                setPosition(i);// Toast.makeText(context, position+"dđ", Toast.LENGTH_SHORT).show();
//                File file;
//                ContextWrapper contextWrapper=new ContextWrapper(context);
//                File directory = contextWrapper.getDir("TK", Context.MODE_APPEND);//tên Thư Mục
//                file = new File(directory, "Gio_Hang.txt");
//                file.delete();
//                tien=tien -(Integer.parseInt(monAnArrayList.get(i).getGia())
//                        *Integer.parseInt(soluong.getText().toString()));
//                tong.setText(goiMonActivity.VDN(tien+""));
//                monAnList.remove(i);
//                monAnArrayList.remove(i);
//                tien=0;
//                notifyDataSetChanged();
//
//                try {
//                    FileOutputStream outputStream=new FileOutputStream(file);
//                    for (classHoaDon hoaDon1:monAnList){
//                        outputStream.write((hoaDon1.getId_mon()+","+hoaDon1.getSoluong()).getBytes());
//                        outputStream.write(("\n").getBytes());
//                    } }catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//
//
//
//
//
//
//
//
//            }
//        });

        return view;
    }
}
