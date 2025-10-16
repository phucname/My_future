package com.example.doan.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.Adapter.Adapter_combo;
import com.example.doan.Adapter.AdapterrecymenuMA;
import com.example.doan.Class.classHoaDon;
import com.example.doan.Class.MonAn;
import com.example.doan.Class.class_combo;
import com.example.doan.R;
import com.example.doan.Class.classBan;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

public class GoiMonActivity extends Fragment {
    TextView tenmon, gia, ghichu, soluong,loai,gianew,giatri_km;
    Button butttongoimon;
    ImageView buttongiamsl, anhmon;
    ProgressDialog progressDialog;
    TextView textView_dagoi;
 FrameLayout fragment_tangsl;
    MonAn monAn;
    RecyclerView dexuatmon,combo_dexuat;
    ArrayList<MonAn> monAnArrayList;
ArrayList<classHoaDon>hoaDonArrayList;
MianActivityBanac mianActivityBanac;
ArrayList<class_combo> class_comboArrayList;
Adapter_combo adapter_combo;
DatabaseReference databaseReference;
ArrayList<String>masoban;
ArrayList<classBan>arrayList;
    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_goi_mon, container, false);
        dexuatmon = view.findViewById(R.id.recyclerview_dexuatmon);
        combo_dexuat=view.findViewById(R.id.recyclerview_combo_goiy);
        Bundle bundle = getArguments();
        textView_dagoi=view.findViewById(R.id.tex_dathem);
        mianActivityBanac= (MianActivityBanac) getActivity();
        monAn = (MonAn) bundle.getSerializable("goimon");
        AnhXa(view);
        progressDialog = new ProgressDialog(view.getContext());
        tenmon.setText(monAn.getTenmon());
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("NhaHang").child(bundle.getString("id_nhahang"));
       if(monAn.getLoai_giamgia()!=2){
           giatri_km.setVisibility(View.VISIBLE);
           gianew.setVisibility(View.VISIBLE);
           gia.setPaintFlags(gia.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
           if(monAn.getLoai_giamgia()==0){

               databaseReference.child("Khuyen_mai").child("PT").child( monAn.getId())
                       .addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               int giatri=Integer.parseInt(snapshot.child("gia_tri").getValue().toString());
                              giatri_km.setText("Giảm:"+giatri+"%");
                               String gia_saogiam= String.valueOf(((Integer.parseInt(monAn.getGia())/100)*(100-giatri)));

                              gianew.setText(VDN(mianActivityBanac.lam_chan(gia_saogiam)));
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }
                       });
           }
           if(monAn.getLoai_giamgia()==1){
               databaseReference.child("Khuyen_mai").child("Tien").child( monAn.getId())
                       .addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               int giatri=Integer.parseInt(snapshot.child("gia_tri").getValue().toString());
                              giatri_km.setText("Giảm:"+giatri+".đ");
                               int gia_saogiam=  ((Integer.parseInt(monAn.getGia())-giatri));
                              gianew.setText(VDN(gia_saogiam+""));
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }
                       });

           }
       }
        gia.setText(VDN( monAn.getGia()));
        ghichu.setText("Chi Tiết :" + monAn.getGhichu() + "\n");
        hoaDonArrayList=new ArrayList<>();
        Picasso.get().load(monAn.getImgmon()).into(anhmon);
        if(monAn.getLoai_mon().equals("null")){
            loai.setText("Loại:Null");
        }else {
            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference()
                    .child("NhaHang").child(bundle.getString("id_nhahang")).child("Loai_Menu").child(monAn.getLoai_mon());
            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    loai.setText("Loại Món:"+snapshot.getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }



        databaseReference.child("ChiTiet_ComBo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<DataSnapshot> arrayList_id_monan = new ArrayList<>();
                class_comboArrayList=new ArrayList<>(4);
                for (DataSnapshot snapshot1:snapshot.getChildren()) {
                    for (DataSnapshot snapshot2:snapshot1.getChildren()) {
                        if (snapshot2.getKey().equals(monAn.getId())) {
                            arrayList_id_monan.add(snapshot1);
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                                    .child("NhaHang").child(bundle.getString("id_nhahang")).child("DS_ComBo")
                                    .child(snapshot1.getKey());
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    class_combo combo=new class_combo(snapshot.child("id_combo").getValue().toString()
                                            ,snapshot.child("gia_combo").getValue().toString()
                                            ,snapshot.child("ten_combo").getValue().toString()
                                    ,snapshot.child("img_combo").getValue().toString(),
                                            Integer.parseInt(snapshot.child("loai_giamgia").getValue().toString()));
                                  class_comboArrayList.add(combo);
                                    adapter_combo=new Adapter_combo(class_comboArrayList,R.layout.carrecymenu,getContext()
                                            ,mianActivityBanac.getId_nhahang(),1);
                                    //LinearLayoutManager gridLayoutManager=new LinearLayoutManager(view.getContext(),
                                           // RecyclerView.VERTICAL,false);
                                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                                    combo_dexuat.setLayoutManager(gridLayoutManager);
                                   combo_dexuat.setAdapter(adapter_combo);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                         break;
                        }
                    }
                }
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference()
                        .child("NhaHang").child(bundle.getString("id_nhahang")).child("DS_MonAn");
                monAnArrayList = new ArrayList<>();
                for (DataSnapshot snapshot1 : arrayList_id_monan) {
                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                     if(!snapshot2.child("id_mon").getValue().toString().equals(monAn.getId())){
                            Query query = databaseReference1.orderByKey().equalTo(snapshot2.getKey());
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        monAnArrayList.add(new MonAn(dataSnapshot.child("tenmon").getValue().toString(),
                                                dataSnapshot.child("gia").getValue().toString(),
                                                dataSnapshot.child("ghichu").getValue().toString(),
                                                dataSnapshot.child("imgmon").getValue().toString(),
                                                dataSnapshot.child("id").getValue().toString(),
                                              dataSnapshot.child("loai_mon").getValue().toString(),
                                                Integer.parseInt(dataSnapshot.child("loai_giamgia").getValue().toString())));
                                        //progressDialog.show();
                                    }

                                    //Toast.makeText(GoiMonActivity.this, monAnArrayList.size()+"", Toast.LENGTH_SHORT).show();
                                    AdapterrecymenuMA adapterrecymenuMA = new AdapterrecymenuMA(monAnArrayList, R.layout.carrecymenu, view.getContext(),0,mianActivityBanac.getId_nhahang());
                                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                                    dexuatmon.setLayoutManager(gridLayoutManager);
                                    dexuatmon.setAdapter(adapterrecymenuMA);
                                    //progressDialog.dismiss();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        File myInternalFile;
//        ContextWrapper contextWrapper = new ContextWrapper(getActivity());
//        //Tạo (Hoặc là mở file nếu nó đã tồn tại) Trong bộ nhớ trong có thư mục là ThuMucCuaToi.
//        File directory = contextWrapper.getDir("TK", Context.MODE_APPEND);//tên Thư Mục
//        myInternalFile = new File(directory, "Gio_Hang.txt");
//        int i=0;
//        if(myInternalFile.exists()==true) {
//            try {
//                FileInputStream inputStream = new FileInputStream(myInternalFile);
//                DataInputStream in = new DataInputStream(inputStream);
//                BufferedReader br = new BufferedReader(
//                        new InputStreamReader(in));
//
//                String strLine;
//                while ((strLine = br.readLine()) != null) {
//                    String[] a = strLine.split(",");
//                    if(a[0].equals(monAn.getId())){
//                       // Toast.makeText(contextWrapper, "Món Ăn Đã Có Ở Giỏ Hàng", Toast.LENGTH_SHORT).show();
//                        textView_dagoi.setVisibility(View.VISIBLE);
//                        i++;
//                        break;
//                    }
//
//                }
//
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }

        buttongiamsl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(soluong.getText().toString()) > 1) {
                    soluong.setText((Integer.parseInt(soluong.getText().toString()) - 1) + "");
                    //  String gia1=gia.getText().toString();
                    ///  gia1=gia1.replaceAll("đ","");
                    //  String gia2=gia1.replaceAll(".","");
                    //Toast.makeText(getApplicationContext(),intent.getStringExtra("keyNH"),Toast.LENGTH_LONG).show();

                }

            }
        });
       fragment_tangsl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soluong.setText((Integer.parseInt(soluong.getText().toString()) + 1) + "");
            }
        });
       butttongoimon.setOnClickListener(new View.OnClickListener() {
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
//        butttongoimon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//               textView_dagoi.setVisibility(View.VISIBLE);
//                    int dem = 0;
//                    ArrayList<classHoaDon> hoaDons = new ArrayList<>();
//                    // Showdialog(intent.getStringExtra("keyNH"));
//
//
//
//                    FileInputStream foss = null;//dùng dọc file
//
//
//                File myInternalFile;
//                ContextWrapper contextWrapper = new ContextWrapper(getContext());
//                //Tạo (Hoặc là mở file nếu nó đã tồn tại) Trong bộ nhớ trong có thư mục là ThuMucCuaToi.
//                File directory = contextWrapper.getDir("TK", Context.MODE_APPEND);//tên Thư Mục
//                myInternalFile = new File(directory, "Gio_Hang.txt");
//                Toast.makeText(getActivity(), myInternalFile.exists()+"", Toast.LENGTH_SHORT).show();
//               if(myInternalFile.exists()==true){
//                   try {
//                       FileInputStream inputStream=new FileInputStream(myInternalFile);
//                       DataInputStream in = new DataInputStream(inputStream);
//                       BufferedReader br = new BufferedReader(
//                               new InputStreamReader(in));
//
//                       String strLine;
//                       while ((strLine= br.readLine())!=null){
//                           String []a=strLine.split(",");
//                           classHoaDon classHoaDon=new classHoaDon(a[0],a[1]);
//                          hoaDonArrayList.add(classHoaDon);
//                   } }catch (FileNotFoundException e) {
//                       e.printStackTrace();
//                   } catch (IOException e) {
//                       e.printStackTrace();
//                   }finally {
//
//                       int i=0;
//                       int bien=0;
//                       for (classHoaDon hodon:hoaDonArrayList
//                            ) {
//                             if(hodon.getId_mon().equals(monAn.getId())){
//                                 hoaDonArrayList.get(i).setSoluong(Integer.parseInt(hodon.getSoluong())+Integer.parseInt(soluong.getText().toString())+"");
//                                bien++;
//                                 break;
//                             }
//                             i++;
//                       }
//                       if(bien==0){
//                           classHoaDon hoaDon=new classHoaDon(monAn.getId(),soluong.getText().toString());
//                           hoaDonArrayList.add(hoaDon);
//                       }
//
//                       try {
//                           //Mở file
//                           FileOutputStream fos = new FileOutputStream(myInternalFile);
//                           //Ghi dữ liệu vào file
//                          String a="";
//                           for (classHoaDon hodon:hoaDonArrayList
//                           ) {  fos.write((hodon.getId_mon()+","+hodon.getSoluong()).getBytes());
//                               fos.write(("\n").getBytes());
//                           }
//                           Toast.makeText(contextWrapper, a, Toast.LENGTH_SHORT).show();
//
//
//                           fos.close();
//
//                       } catch (IOException e) {
//                           e.printStackTrace();
//                       }
//                   }
//               }else {
//                   try {
//                       //Mở file
//                       FileOutputStream fos = new FileOutputStream(myInternalFile);
//                       //Ghi dữ liệu vào file
//                       fos.write((monAn.getId()+","+soluong.getText()).getBytes());
//                       fos.write(("\n").getBytes());
//                       fos.close();
//
//                   } catch (IOException e) {
//                       e.printStackTrace();
//                   }
//               }
//
//
//
//
////                    MianActivityBanac mianActivityBanac = (MianActivityBanac) getActivity();
////                    FragmentTransaction transaction=mianActivityBanac.getSupportFragmentManager()
////                            .beginTransaction();
////
////                    transaction.replace(R.id.container,new Fragment_GioHang());
////
////                    transaction.commit();
////                    BottomNavigationView navigationView=mianActivityBanac.findViewById(R.id.bottomvieww);
////                    navigationView.getMenu().findItem(R.id.giohang).setChecked(true);
//
//
//
//
//            }
//        });




        return  view;
    }



    private void Showdialog(String key) {

        Dialog dialog=new Dialog(getContext());
        dialog.setContentView(R.layout.dialogban);
        ListView listView=dialog.findViewById(R.id.list_item_dialog);
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
                        masoban.add("Bàn Số:"+snapshot1.child("masoban").getValue().toString());
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
            DatabaseReference data=  databaseReference.child("Ban").child(arrayList.get(i).getIdBan()).child("DSMon")
                    .child("mon");
                Query query=data.orderByChild("id_mon").equalTo(monAn.getId());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() == null) {
                            Calendar calendar = Calendar.getInstance();
                            classHoaDon classHoaDon = new classHoaDon("", arrayList.get(i).getIdBan() + ""
                                    , calendar.get(Calendar.MINUTE) + "/"
                                    + calendar.get(Calendar.HOUR) + "/" + calendar.get(Calendar.DATE)
                                    , soluong.getText().toString(), monAn.getId(), "Chưa Nhận",""
                            );

                            data.push().setValue(classHoaDon);

                        }
                        else {
                            int kt=0;
                            String key1=snapshot.getValue().toString(),keyphu="",sl="";

                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                if (snapshot1.child("tinhtrang").getValue().toString().equals("Chưa Nhận")) {
                                    kt = 1;
                                    sl = snapshot1.child("soluong").getValue().toString();
                                    // Toast.makeText(getActivity(), "gióng", Toast.LENGTH_SHORT).show();
                                    key1 = snapshot1.getKey();
                                }
                            }
                            // Toast.makeText(getActivity(), key1+"khóa"+kt, Toast.LENGTH_SHORT).show();
                            if (kt == 1) {
                                DatabaseReference databaseReference1 = data.child(key1).child("soluong");
                               int  soluongthem = Integer.parseInt(sl)
                                        + Integer.parseInt(soluong.getText().toString());
                                databaseReference1.setValue(soluongthem + "");
                                //Toast.makeText(getActivity(), arrayList.get(i).getIdBan(), Toast.LENGTH_LONG).show();
                            } else {
                                Calendar calendar = Calendar.getInstance();
                                classHoaDon classHoaDon = new classHoaDon("", arrayList.get(i).getIdBan() + ""
                                        , calendar.get(Calendar.MINUTE) + "/"
                                        + calendar.get(Calendar.HOUR) + "/" + calendar.get(Calendar.DATE)
                                        , soluong.getText().toString(), monAn.getId(), "Chưa Nhận",""
                                );

                                data.push().setValue(classHoaDon);

                            }
                    }
                       dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }




        });
        dialog.show();

    }


    private void AnhXa(View view) {
        tenmon=view.findViewById(R.id.goimontenmonan);
        gia=view.findViewById(R.id.goimongia);
        ghichu=view.findViewById(R.id.goimonchitiettt);
        soluong=view.findViewById(R.id.goimonsoluong);
        anhmon=view.findViewById(R.id.goimonimg);
        loai=view.findViewById(R.id.loaimon);
       buttongiamsl=view.findViewById(R.id.goimonslgiam);
          gianew=view.findViewById(R.id.goimon_giakhuyenmai);
          giatri_km=view.findViewById(R.id.goimon_giatrikm);
        butttongoimon=view.findViewById(R.id.goimonbutton);
        fragment_tangsl=view.findViewById(R.id.framelayout_cong_mon);
    }
    public   String VDN(String tien){

        StringBuilder builder=new StringBuilder(tien);
        int key=tien.length()%3;
        for(int i=0; key<tien.length();i++){
            if(key==0){
                key+=3;
            }
            if(key>0){
                builder.insert(key,".");
                key=key+4;
            }
        }



        // Toast.makeText(this,tien.length()+"",Toast.LENGTH_LONG).show();
        return builder+"đ";
    }

}