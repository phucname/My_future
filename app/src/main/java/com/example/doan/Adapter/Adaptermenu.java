package com.example.doan.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.doan.Activity.MianActivityBanac;
import com.example.doan.Class.MonAn;
import com.example.doan.Class.classHoaDon;
import com.example.doan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adaptermenu extends BaseAdapter {
    Context context;
   ArrayList<MonAn> monAnList;
    ArrayList<Integer>sl=new ArrayList<>();
    int layout;
    TextView tonggia;
    int tong;

    ArrayList<MonAn>monAnArrayList_dathem=new ArrayList<>();
    ArrayList<classHoaDon>arrayListsl=new ArrayList<>();


    public ArrayList<classHoaDon> getArrayListsl() {
        return arrayListsl;
    }

    public void setArrayListsl(ArrayList<classHoaDon> arrayListsl) {
        this.arrayListsl = arrayListsl;
    }

    public ArrayList<MonAn> getMonAnArrayList_dathem() {
        return monAnArrayList_dathem;
    }

    public void setMonAnArrayList_dathem(ArrayList<MonAn> monAnArrayList_dathem) {
        this.monAnArrayList_dathem = monAnArrayList_dathem;
    }

    public void setnewlist(ArrayList<MonAn> newarraylist){
    monAnList=newarraylist;
    notifyDataSetChanged();
}
    public int getTong() {
        return tong;
    }

    public ArrayList<Integer> getSl() {
        return sl;
    }

    public void setSl(ArrayList<Integer> sl) {
        this.sl = sl;
    }

    public void setTong(int tong) {
        this.tong = tong;
    }

    public Adaptermenu(Context context, int  layout, ArrayList<MonAn> monAnList, TextView tonggia) {
        this.context = context;
        this.monAnList = monAnList;
        this.layout = layout;
        this.tonggia=tonggia;
    }

    public ArrayList<MonAn> getMonAnList() {
        return monAnList;
    }

    public void setMonAnList(ArrayList<MonAn> monAnList) {
        this.monAnList = monAnList;
    }

    @Override
    public int getCount() {
        return monAnList.size();
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
        TextView ten=view.findViewById(R.id.text_tenmon_combo);
        TextView gia=view.findViewById(R.id.text_giamon_combo);
       // CheckBox checkBox=view.findViewById(R.id.check_mon_combo);
        ImageView ivBasicImage = view.findViewById(R.id.img_mon_combo);
        FrameLayout fragment_congsl=view.findViewById(R.id.framelayout_item_cong_slcombo);
        FrameLayout fragment_trusl=view.findViewById(R.id.frame_item_tru_slcombo);
        TextView sluong=view.findViewById(R.id.text_item_sl_combo);
        Button themmon=view.findViewById(R.id.button_themmon_combo);
        MonAn monAn=monAnList.get(i);
        MianActivityBanac mianActivityBanac=new MianActivityBanac();
        Button dathemmon=view.findViewById(R.id.button_dathem_combo);
        ten.setText(monAn.getTenmon());
        gia.setText(mianActivityBanac.VND(Integer.parseInt(monAn.getGia())));
        sl.add(1);
         if(monAn.getCheck()==true){
             dathemmon.setVisibility(View.VISIBLE);
             themmon.setVisibility(View.INVISIBLE);

         }
        tonggia.setText(monAnArrayList_dathem.size()+"");

        themmon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monAnArrayList_dathem.add(monAn);
                arrayListsl.add(new classHoaDon("",sl.get(i)+"",""));
                //Toast.makeText(context, monAn.getTenmon()+"", Toast.LENGTH_SHORT).show();
                dathemmon.setVisibility(View.VISIBLE);
                themmon.setVisibility(View.INVISIBLE);
                monAnList.get(i).setCheck(true);
                 tonggia.setText(monAnArrayList_dathem.size()+"");

            }
        });

       fragment_congsl.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              sl.set(i,sl.get(i)+1) ;
               sluong.setText(sl.get(i)+"");
//              if(monAn.getCheck()){
//                  tong=tong+Integer.parseInt(monAn.getGia());
//                  tonggia.setText(tong+"");
//              }

           }
       });
       fragment_trusl.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(sl.get(i)>1){

                   sl.set(i,sl.get(i)-1) ;
                   sluong.setText(sl.get(i)+"");
//                   if(monAn.getCheck()){
//                       tong=tong-Integer.parseInt(monAn.getGia());
//                       tonggia.setText(tong+"");
//                   }
               }

           }
       });
       sluong.setText("1");
//        checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(checkBox.isChecked()==true){
//                    monAnList.get(i).setCheck(true);
//                    tong=tong+(Integer.parseInt(monAn.getGia())*sl.get(i));
//                    tonggia.setText(tong+"");
//                }else{
//                    monAnList.get(i).setCheck(false);
//                    tong=tong-(Integer.parseInt(monAn.getGia())*sl.get(i));
//                    tonggia.setText(tong+"");
//                }
//                Toast.makeText(context,  monAnList.get(i).getCheck()+"/"+i, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        if(checkBox.isChecked()==true){
//            tong=tong+Integer.parseInt(monAn.getGia());
//            tonggia.setText(tong+"");
//        }
        Picasso.get().load(monAn.getImgmon()).into(ivBasicImage);
        return view;
    }
}
