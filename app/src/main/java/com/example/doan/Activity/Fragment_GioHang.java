package com.example.doan.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.Adapter.Adapter_Giohang;
import com.example.doan.Adapter.adapter_combo_giohang;
import com.example.doan.Class.MonAn;
import com.example.doan.Class.classBan;
import com.example.doan.Class.classHoaDon;
import com.example.doan.Class.class_Bep;
import com.example.doan.Class.class_combo;
import com.example.doan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;


public class Fragment_GioHang extends Fragment {

    ListView listView;
    Adapter_Giohang adapter_giohang;
    int soluongthem;
   TextView tong;
    int count;
    ArrayList<classBan> arrayList;
    ArrayList<String> masoban;
    ArrayList<classHoaDon> donArrayList;
    ArrayList<classHoaDon> comboArrayList;
    ArrayList<MonAn>monAnArrayList;
    Button goimon;
    File file;
    int i=0;
    File comboflie;

    MianActivityBanac mianActivityBanac;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View  view=  inflater.inflate(R.layout.fragment__gio_hang, container, false);
      listView=view.findViewById(R.id.listview_giohang);

      tong=view.findViewById(R.id.text_tong_giohang);
      goimon=view.findViewById(R.id.button_goimon_giohang);

       mianActivityBanac= (MianActivityBanac) getActivity();
       getlist_giohang();

                 goimon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                   if(user!=null){
                       Showdialog(mianActivityBanac.getId_nhahang());
                   }else Toast.makeText(mianActivityBanac, "Bạn Phải Là Nhân Viên Nhà " +
                           "Hàng Mới Được Gọi Món! Mời Bạn " +
                           "Đăng Nhập Tài Khảo.", Toast.LENGTH_SHORT).show();

                }
            });


        return view;
    }
    private void getcombo(){

        ContextWrapper contextWrapper=new ContextWrapper(getActivity());
        File directory = contextWrapper.getDir("TK", Context.MODE_APPEND);//tên Thư Mục
        comboflie = new File(directory,"combo.txt");

        comboArrayList=new ArrayList<>();
        try {
            FileInputStream inputStream=new FileInputStream(comboflie);
            DataInputStream in = new DataInputStream(inputStream);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(in));

            String strLine;
            while ((strLine= br.readLine())!=null){
                String []a=strLine.split(",");
                classHoaDon classcombo=new classHoaDon(a[0],a[1]);
               comboArrayList.add(classcombo);
               donArrayList.add(classcombo);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(comboArrayList.size()>0){

                for(classHoaDon hoaDon:comboArrayList){

                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference()
                            .child("NhaHang").child(mianActivityBanac.getId_nhahang())
                            .child("DS_ComBo").child(hoaDon.getId_mon());

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                MonAn combo = new MonAn(snapshot.child("ten_combo").getValue().toString()
                                        , snapshot.child("gia_combo").getValue().toString(),
                                        "",
                                        snapshot.child("img_combo").getValue().toString(),hoaDon.getId_mon(),hoaDon.getSoluong(),"1");

                                monAnArrayList.add(combo);

                            } catch (NullPointerException e){

                                comboflie.delete();

                                FragmentTransaction transaction=mianActivityBanac.getSupportFragmentManager()
                                        .beginTransaction();

                                transaction.replace(R.id.container,new Fragment_GioHang());

                                transaction.commit();
                               // BottomNavigationView navigationView=mianActivityBanac.findViewById(R.id.bottomvieww);
                                //navigationView.getMenu().findItem(R.id.giohang).setChecked(true);

                            }finally {
                                //  Toast.makeText(contextWrapper, monAnArrayList.size()+"//"+donArrayList.size(), Toast.LENGTH_SHORT).show();
                                if(donArrayList.size()==monAnArrayList.size()){

                                    adapter_giohang = new Adapter_Giohang(getActivity(), R.layout.item_giohang, tong, monAnArrayList);
                                    listView.setAdapter(adapter_giohang);
                                }
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });






                }
            }else if(donArrayList.size()>0){
                adapter_giohang = new Adapter_Giohang(getActivity(), R.layout.item_giohang, tong, monAnArrayList);
                listView.setAdapter(adapter_giohang);
            }


        }
    }
    private  void getlist_giohang(){
        ContextWrapper contextWrapper=new ContextWrapper(getContext());
        File directory = contextWrapper.getDir("TK", Context.MODE_APPEND);//tên Thư Mục
        file = new File(directory, "Gio_Hang.txt");
        donArrayList=new ArrayList<>();
        monAnArrayList=new ArrayList<>();
        try {
            FileInputStream inputStream=new FileInputStream(file);
            DataInputStream in = new DataInputStream(inputStream);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(in));

            String strLine;
            while ((strLine= br.readLine())!=null){
                String []a=strLine.split(",");
                classHoaDon classHoaDon=new classHoaDon(a[0],a[1]);
                donArrayList.add(classHoaDon);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(donArrayList.size()>0){

                for(classHoaDon hoaDon:donArrayList){


                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference()
                            .child("NhaHang").child(mianActivityBanac.getId_nhahang())
                            .child("DS_MonAn").child(hoaDon.getId_mon());

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                MonAn monAn = new MonAn(snapshot.child("tenmon").getValue().toString()
                                        , snapshot.child("gia").getValue().toString(),
                                        snapshot.child("ghichu").getValue().toString(),
                                        snapshot.child("imgmon").getValue().toString(),hoaDon.getId_mon(),hoaDon.getSoluong(),"0");

                                monAnArrayList.add(monAn);

                            } catch (NullPointerException e){

                                file.delete();
                                FragmentTransaction transaction=mianActivityBanac.getSupportFragmentManager()
                                        .beginTransaction();

                                transaction.replace(R.id.container,new Fragment_GioHang());
                                transaction.commit();
//                                BottomNavigationView navigationView=mianActivityBanac.findViewById(R.id.bottomvieww);
//                                navigationView.getMenu().findItem(R.id.giohang).setChecked(true);

                            }finally {
                                //  Toast.makeText(contextWrapper, monAnArrayList.size()+"//"+donArrayList.size(), Toast.LENGTH_SHORT).show();
                                if(monAnArrayList.size()==donArrayList.size()){
                                   getcombo();
                                    //Toast.makeText(contextWrapper, "adate", Toast.LENGTH_SHORT).show();
                                }
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });






                }

            }else getcombo();


        }
    }

//    @Override
//    public void onPause() {
//        Toast.makeText(mianActivityBanac, "aaaa", Toast.LENGTH_SHORT).show();
//        super.onPause();
//    }khi chuyển framlll


    private void Showdialog(String key) {

        Dialog dialog=new Dialog(getContext());
        dialog.setContentView(R.layout.dialogban);
        ListView listView=dialog.findViewById(R.id.list_item_dialog);
        Button button=dialog.findViewById(R.id.button_them_combo_dialog);
        button.setVisibility(View.INVISIBLE);
      DatabaseReference  data= FirebaseDatabase.getInstance().getReference().child("NhaHang").child(key).child("Ban");
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              arrayList=new ArrayList<>();
             masoban=new ArrayList<>();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                  if(Boolean.parseBoolean(snapshot1.child("tinhTrang").getValue().toString()) ==true){
                      classBan ban=new classBan(snapshot1.child("masoban").getValue().toString(),
                              snapshot1.child("soghe").getValue().toString(),
                              snapshot1.child("idBan").getValue().toString(),
                              Boolean.valueOf(snapshot1.child("tinhTrang").getValue().toString()),
                              0);

                      arrayList.add(ban);//chứa khóacủa bàn
                      masoban.add(snapshot1.child("masoban").getValue().toString());
                  }

                }


                ArrayAdapter adapter=new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,masoban);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//             if(adapter_combo_giohang.isEmpty()){
//
//             }
             if(adapter_giohang!=null){
                if(adapter_giohang.getMonAnArrayList().size()>0){
                    themmon(i,dialog);
                }

             }







            }




        });
        dialog.show();

    }

    private void themmon(int i,Dialog dialog) {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("NhaHang")
                .child(mianActivityBanac.getId_nhahang())
                .child("Ban")
                .child(arrayList.get(i).getIdBan())
                .child("DSMon");
        monAnArrayList=adapter_giohang.getMonAnArrayList();
        for (MonAn monAn:monAnArrayList){
            //kiểm tra xem bàn đã gọi món này chưa
            if(monAn.getLoai_mon().equals("0")){
                Query query=databaseReference.child("mon").orderByChild("id_mon").equalTo(monAn.getId());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            int kt=0;
                            String key1=snapshot.getValue().toString(),keyphu="",sl="";
                            Array a[]=new Array[3];
                            for (DataSnapshot snapshot1:snapshot.getChildren()){
                                if(snapshot1.child("tinhtrang").getValue().toString().equals("Chưa Nhận")){
                                    kt=1;
                                    sl=snapshot1.child("soluong").getValue().toString();
                                    // Toast.makeText(getActivity(), "gióng", Toast.LENGTH_SHORT).show();
                                    key1=snapshot1.getKey();
                                }
                            }
                            // Toast.makeText(getActivity(), key1+"khóa"+kt, Toast.LENGTH_SHORT).show();
                            if(kt==1){
                                DatabaseReference databaseReference1= databaseReference.child("mon").child(key1).child("soluong");
                                soluongthem=Integer.parseInt(sl)
                                        +Integer.parseInt(monAn.getSl());
                                databaseReference1.setValue(soluongthem+"");
                                //Toast.makeText(getActivity(), arrayList.get(i).getIdBan(), Toast.LENGTH_LONG).show();
                            }
                            else {
                                Calendar calendar=Calendar.getInstance();
                                classHoaDon classHoaDon=new classHoaDon("",arrayList.get(i).getIdBan()+""
                                        ,calendar.get(Calendar.MINUTE)+"/"
                                        +calendar.get(Calendar.HOUR)+"/"+calendar.get(Calendar.DATE)
                                        ,monAn.getSl(),monAn.getId(),"Chưa Nhận",""
                                );
                                DatabaseReference data=databaseReference.child("mon").push();
                                data.setValue(classHoaDon);

                            }

                        }catch (NullPointerException ex){
                            Calendar calendar=Calendar.getInstance();
                            classHoaDon classHoaDoncatch=new classHoaDon("",arrayList.get(i).getIdBan()+""
                                    ,calendar.get(Calendar.MINUTE)+"/"
                                    +calendar.get(Calendar.HOUR)+"/"+calendar.get(Calendar.DATE)
                                    ,monAn.getSl(),monAn.getId(),"Chưa Nhận",""
                            );

                            DatabaseReference data=databaseReference.child("mon").push();
                            data.setValue(classHoaDoncatch);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }else {
                Query query=databaseReference.child("combo").orderByChild("id_mon").equalTo(monAn.getId());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            int kt=0;
                            String key1=snapshot.getValue().toString(),keyphu="",sl="";
                            Array a[]=new Array[3];
                            for (DataSnapshot snapshot1:snapshot.getChildren()){
                                if(snapshot1.child("tinhtrang").getValue().toString().equals("Chưa Nhận")){
                                    kt=1;
                                    sl=snapshot1.child("soluong").getValue().toString();
                                    // Toast.makeText(getActivity(), "gióng", Toast.LENGTH_SHORT).show();
                                    key1=snapshot1.getKey();
                                }else {
                                    keyphu=snapshot1.getKey();
                                    //Toast.makeText(getActivity(), "k giống", Toast.LENGTH_SHORT).show();
                                }
                            }
                            // Toast.makeText(getActivity(), key1+"khóa"+kt, Toast.LENGTH_SHORT).show();
                            if(kt==1){
                                DatabaseReference databaseReference1= databaseReference.child("combo").child(key1).child("soluong");
                                soluongthem=Integer.parseInt(sl)
                                        +Integer.parseInt("1");
                                databaseReference1.setValue(soluongthem+"");
                                Toast.makeText(getActivity(), arrayList.get(i).getIdBan(), Toast.LENGTH_LONG).show();
                            }
                            else {
                                Calendar calendar=Calendar.getInstance();
                                classHoaDon classHoaDon=new classHoaDon("",arrayList.get(i).getIdBan()+""
                                        ,calendar.get(Calendar.MINUTE)+"/"
                                        +calendar.get(Calendar.HOUR)+"/"+calendar.get(Calendar.DATE)
                                        ,monAn.getSl(),monAn.getId(),"Chưa Nhận",""
                                );
                                DatabaseReference data=databaseReference.child("combo").push();
                                data.setValue(classHoaDon);

                            }

                        }catch (NullPointerException ex){
                            Calendar calendar=Calendar.getInstance();
                            classHoaDon classHoaDoncatch=new classHoaDon("",arrayList.get(i).getIdBan()+""
                                    ,calendar.get(Calendar.MINUTE)+"/"
                                    +calendar.get(Calendar.HOUR)+"/"+calendar.get(Calendar.DATE)
                                    ,monAn.getSl(), monAn.getId(),"Chưa Nhận",""
                            );

                            DatabaseReference data=databaseReference.child("combo").push();
                            data.setValue(classHoaDoncatch);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        }

        file.delete();
        comboflie.delete();
        dialog.dismiss();
        FragmentTransaction transaction=mianActivityBanac.getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.container,new Fragment_GioHang());
        transaction.commit();

        Intent intent=new Intent(getContext(),HoaDonFragment.class);
        MianActivityBanac mianActivityBanac= (MianActivityBanac) getActivity();
        intent.putExtra("id_nhahang",mianActivityBanac.getId_nhahang());
        intent.putExtra("nameban",arrayList.get(i).getIdBan());
        intent.putExtra("maban",arrayList.get(i).getMasoban());
        intent.putExtra("pass",arrayList.get(i).getPass()+"");
        startActivity(intent);
    }
}