package com.example.doan.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.Adapter.Adapter_Hoadon;
import com.example.doan.Adapter.adapter_biul_hd;
import com.example.doan.Class.MonAn;
import com.example.doan.Class.classBan;
import com.example.doan.Class.classHoaDon;
import com.example.doan.Class.classThongKe;
import com.example.doan.Class.class_giamgia;
import com.example.doan.Class.classhistory;
import com.example.doan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.internal.Sleeper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class HoaDonFragment extends AppCompatActivity {
RecyclerView listviewhoadon;
TextView texttonghoadon,texttenban;
DatabaseReference menu_ref,hoaodn_ref;
ArrayList<classHoaDon> hoaDonArrayList;
ArrayList<MonAn>monAnArrayList;
Adapter_Hoadon adapterHoaDon;
Button thanhtoan,doi_ban;
int TongTien;
ArrayList<class_giamgia>arrayList_giamgia;
String chucvu,ten_nv;
DatabaseReference databaseReference;
    FirebaseUser user;
    ArrayList<classBan>arrayList;
    ArrayList<String>masoban;
ArrayList<String>key_order;
Intent intent;
    int i;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user=FirebaseAuth.getInstance().getCurrentUser();
        GoiMonActivity goiMonActivity=new GoiMonActivity();
    // Inflate the layout for this fragment
        setContentView(R.layout.fragment_hoa_don);
        listviewhoadon= findViewById(R.id.list_hoadon);
        texttonghoadon=findViewById(R.id.text_tonghoadon);
        thanhtoan=findViewById(R.id.hoadonbuttonthanhtoan);
        doi_ban=findViewById(R.id.hoadonbuttondoiban);
        texttenban=findViewById(R.id.text_tenban_hoadon);
    intent  =getIntent();

        menu_ref=FirebaseDatabase.getInstance().getReference()
                .child("NhaHang")
                .child(intent.getStringExtra("id_nhahang"))
                .child("Ban").child(intent.getStringExtra("nameban"));
        databaseReference=FirebaseDatabase.getInstance().getReference()
                .child("NhaHang").child(intent.getStringExtra("id_nhahang"));
        databaseReference.child("DS_NhanVien").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               ten_nv=snapshot.child("tenNV").getValue().toString();
                chucvu=snapshot.child("chucVuNV").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        menu_ref.child("DSMon").child("mon")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                  hoaDonArrayList=new ArrayList<>();
                  monAnArrayList=new ArrayList<>();
                  arrayList_giamgia=new ArrayList<>();
                  key_order=new ArrayList<>();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    hoaDonArrayList.add(new classHoaDon(dataSnapshot.getKey(),dataSnapshot.child("id_ban").getValue().toString()
                               ,dataSnapshot.child("tinhtrang").getValue().toString()
                            ,dataSnapshot.child("soluong").getValue().toString()
                            ,dataSnapshot.child("id_mon").getValue().toString(),dataSnapshot.child("date_goimon").getValue().toString()
                            ,0,dataSnapshot.child("chat").getValue().toString()));
                    key_order.add(dataSnapshot.getKey());
//
                      DatabaseReference databaseReferencez=FirebaseDatabase.getInstance().getReference()
                              .child("NhaHang").child(intent.getStringExtra("id_nhahang"))
                              .child("DS_MonAn").child(dataSnapshot.child("id_mon").getValue().toString());
                      databaseReferencez.addListenerForSingleValueEvent(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                              monAnArrayList.add(new MonAn(snapshot.child("tenmon").getValue().toString()
                              ,snapshot.child("gia").getValue().toString()
                              ,snapshot.child("ghichu").getValue().toString()
                                      , snapshot.child("imgmon").getValue().toString(),
                                      Integer.parseInt(snapshot.child("loai_giamgia").getValue().toString())
                              ));
                              if(Integer.parseInt(snapshot.child("loai_giamgia").getValue().toString())==0){
                                  databaseReference.child("Khuyen_mai").child("PT").child(snapshot.getKey())
                                          .addListenerForSingleValueEvent(new ValueEventListener() {
                                              @Override
                                              public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                                int giatri=Integer.parseInt(snapshot1.child("gia_tri").getValue().toString());
                                                  String gia_saogiam=String.valueOf(((Integer.parseInt(snapshot.child("gia").getValue().toString())/100)
                                                          *(100-giatri)));
                                                  char[] a =gia_saogiam.toCharArray();
                                                  for (int i=gia_saogiam.length()-3;i<gia_saogiam.length();i++){
                                                      a[i]='0';
                                                  }
                                                  gia_saogiam=new String(a);
                                                  arrayList_giamgia.add(new class_giamgia(giatri+"",gia_saogiam));
                                                  TongTien=TongTien+
                                                          (Integer.parseInt(gia_saogiam)
                                                                  *Integer.parseInt(dataSnapshot.child("soluong").getValue().toString())) ;
                                                  texttonghoadon.setText("Tổng Hóa Đơn:"+ goiMonActivity.VDN(TongTien+""));
                                                  adapterHoaDon=new Adapter_Hoadon(HoaDonFragment.this,R.layout.item_hoadon
                                                          ,hoaDonArrayList,monAnArrayList, intent.getStringExtra("id_nhahang")
                                                          ,arrayList_giamgia);
                                                  GridLayoutManager gridLayoutManager=new GridLayoutManager(HoaDonFragment.this, 1);
                                                  listviewhoadon.setLayoutManager(gridLayoutManager);
                                                  listviewhoadon.setAdapter(adapterHoaDon);
                                              }

                                              @Override
                                              public void onCancelled(@NonNull DatabaseError error) {

                                              }
                                          });
                              }
                              if(Integer.parseInt(snapshot.child("loai_giamgia").getValue().toString())==1){
                                  databaseReference.child("Khuyen_mai").child("Tien").child(snapshot.getKey())
                                          .addListenerForSingleValueEvent(new ValueEventListener() {
                                              @Override
                                              public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                                  int giatri=Integer.parseInt(snapshot1.child("gia_tri").getValue().toString());
                                                  String gia_saogiam=String.valueOf((
                                                          (Integer.parseInt(snapshot.child("gia").getValue().toString())-giatri)
                                                  ));
                                                  arrayList_giamgia.add(new class_giamgia(giatri+"",gia_saogiam));
                                                  TongTien=TongTien+
                                                          (Integer.parseInt(gia_saogiam)
                                                                  *Integer.parseInt(dataSnapshot.child("soluong").getValue().toString())) ;
                                                  texttonghoadon.setText("Tổng Hóa Đơn:"+ goiMonActivity.VDN(TongTien+""));
                                                  adapterHoaDon=new Adapter_Hoadon(HoaDonFragment.this,R.layout.item_hoadon
                                                          ,hoaDonArrayList,monAnArrayList, intent.getStringExtra("id_nhahang")
                                                          ,arrayList_giamgia);
                                                  GridLayoutManager gridLayoutManager=new GridLayoutManager(HoaDonFragment.this, 1);
                                                  listviewhoadon.setLayoutManager(gridLayoutManager);
                                                  listviewhoadon.setAdapter(adapterHoaDon);
                                              }

                                              @Override
                                              public void onCancelled(@NonNull DatabaseError error) {

                                              }
                                          });
                              }
                              if(Integer.parseInt(snapshot.child("loai_giamgia").getValue().toString())==2){
                                  arrayList_giamgia.add(new class_giamgia("",""));
                                  TongTien=TongTien+
                                          (Integer.parseInt(snapshot.child("gia").getValue().toString())
                                                  *Integer.parseInt(dataSnapshot.child("soluong").getValue().toString())) ;
                                  texttonghoadon.setText("Tổng Hóa Đơn:"+ goiMonActivity.VDN(TongTien+""));
                                  adapterHoaDon=new Adapter_Hoadon(HoaDonFragment.this,R.layout.item_hoadon
                                          ,hoaDonArrayList,monAnArrayList, intent.getStringExtra("id_nhahang")
                                          ,arrayList_giamgia);
                                  GridLayoutManager gridLayoutManager=new GridLayoutManager(HoaDonFragment.this, 1);
                                  listviewhoadon.setLayoutManager(gridLayoutManager);
                                  listviewhoadon.setAdapter(adapterHoaDon);
                              }
                             // Toast.makeText(HoaDonFragment.this, snapshot+"", Toast.LENGTH_SHORT).show();




                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError error) {

                          }
                      });

                }

           // Toast.makeText(getApplicationContext(), TongTien+"",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        menu_ref.child("DSMon").child("combo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    hoaDonArrayList.add(new classHoaDon(dataSnapshot.getKey(),dataSnapshot.child("id_ban").getValue().toString()
                            ,dataSnapshot.child("tinhtrang").getValue().toString()
                            ,dataSnapshot.child("soluong").getValue().toString()
                            ,dataSnapshot.child("id_mon").getValue().toString(),dataSnapshot.child("date_goimon").getValue().toString()
                            ,1,dataSnapshot.child("chat").getValue().toString()));
                             key_order.add(dataSnapshot.getKey());
                    DatabaseReference databaseReferenceq=FirebaseDatabase.getInstance().getReference()
                            .child("NhaHang").child(intent.getStringExtra("id_nhahang"))
                            .child("DS_ComBo").child(dataSnapshot.child("id_mon").getValue().toString());
                    databaseReferenceq.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            monAnArrayList.add(new MonAn(snapshot.child("ten_combo").getValue().toString()
                                    ,snapshot.child("gia_combo").getValue().toString()
                                    ,""
                                    , snapshot.child("img_combo").getValue().toString()
                            , Integer.parseInt(snapshot.child("loai_giamgia").getValue().toString())));
                            // Toast.makeText(HoaDonFragment.this, snapshot+"", Toast.LENGTH_SHORT).show();
                            if(Integer.parseInt(snapshot.child("loai_giamgia").getValue().toString())==0){
                                databaseReference.child("Khuyen_mai").child("PT").child(snapshot.getKey())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                                int giatri=Integer.parseInt(snapshot1.child("gia_tri").getValue().toString());
                                                String gia_saogiam=String.valueOf(((Integer.parseInt(
                                                        snapshot.child("gia_combo").getValue().toString())/100)
                                                        *(100-giatri)));
                                                char[] a =gia_saogiam.toCharArray();
                                                for (int i=gia_saogiam.length()-3;i<gia_saogiam.length();i++){
                                                    a[i]='0';
                                                }
                                                gia_saogiam=new String(a);
                                                arrayList_giamgia.add(new class_giamgia(giatri+"",gia_saogiam));
                                                TongTien=TongTien+
                                                        (Integer.parseInt(gia_saogiam)
                                                                *Integer.parseInt(dataSnapshot.child("soluong").getValue().toString())) ;
                                                texttonghoadon.setText("Tổng Hóa Đơn:"+ goiMonActivity.VDN(TongTien+""));
                                                adapterHoaDon=new Adapter_Hoadon(HoaDonFragment.this,R.layout.item_hoadon
                                                        ,hoaDonArrayList,monAnArrayList, intent.getStringExtra("id_nhahang")
                                                        ,arrayList_giamgia);
                                                GridLayoutManager gridLayoutManager=new GridLayoutManager(HoaDonFragment.this, 1);
                                                listviewhoadon.setLayoutManager(gridLayoutManager);
                                                listviewhoadon.setAdapter(adapterHoaDon);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }
                            if(Integer.parseInt(snapshot.child("loai_giamgia").getValue().toString())==1){
                                databaseReference.child("Khuyen_mai").child("Tien").child(snapshot.getKey())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                                int giatri=Integer.parseInt(snapshot1.child("gia_tri").getValue().toString());
                                                String gia_saogiam=String.valueOf((
                                                        (Integer.parseInt(snapshot.child("gia").getValue().toString())-giatri)
                                                ));
                                                arrayList_giamgia.add(new class_giamgia(giatri+"",gia_saogiam));
                                                TongTien=TongTien+
                                                        (Integer.parseInt(gia_saogiam)
                                                                *Integer.parseInt(dataSnapshot.child("soluong").getValue().toString())) ;
                                                texttonghoadon.setText("Tổng Hóa Đơn:"+ goiMonActivity.VDN(TongTien+""));
                                                adapterHoaDon=new Adapter_Hoadon(HoaDonFragment.this,R.layout.item_hoadon
                                                        ,hoaDonArrayList,monAnArrayList, intent.getStringExtra("id_nhahang")
                                                        ,arrayList_giamgia);
                                                GridLayoutManager gridLayoutManager=new GridLayoutManager(HoaDonFragment.this, 1);
                                                listviewhoadon.setLayoutManager(gridLayoutManager);
                                                listviewhoadon.setAdapter(adapterHoaDon);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }
                            if(Integer.parseInt(snapshot.child("loai_giamgia").getValue().toString())==2){
                                arrayList_giamgia.add(new class_giamgia("",""));
                                TongTien=TongTien+
                                        (Integer.parseInt(snapshot.child("gia_combo").getValue().toString())
                                                *Integer.parseInt(dataSnapshot.child("soluong").getValue().toString())) ;
                                texttonghoadon.setText("Tổng Hóa Đơn:"+ goiMonActivity.VDN(TongTien+""));
                                adapterHoaDon=new Adapter_Hoadon(HoaDonFragment.this,R.layout.item_hoadon
                                        ,hoaDonArrayList,monAnArrayList, intent.getStringExtra("id_nhahang")
                                        ,arrayList_giamgia);
                                GridLayoutManager gridLayoutManager=new GridLayoutManager(HoaDonFragment.this, 1);
                                listviewhoadon.setLayoutManager(gridLayoutManager);
                                listviewhoadon.setAdapter(adapterHoaDon);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

                // Toast.makeText(getApplicationContext(), TongTien+"",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        texttenban.setText("Bàn :"+intent.getStringExtra("maban"));
        doi_ban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(intent.getStringExtra("chucvu").equals("0")){
                     Dialog dialog=new Dialog(HoaDonFragment.this);
                     dialog.setContentView(R.layout.dialogban);
                     ListView listView=dialog.findViewById(R.id.list_item_dialog);

                     DatabaseReference  data= FirebaseDatabase.getInstance().getReference().child("NhaHang")
                             .child(intent.getStringExtra("id_nhahang")).child("Ban");
                     data.addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot snapshot) {
                             arrayList=new ArrayList<>();
                             masoban=new ArrayList<>();
                             for(DataSnapshot snapshot1:snapshot.getChildren()){
                                 if(Boolean.parseBoolean(snapshot1.child("tinhTrang").getValue().toString()) ==false){
                                     classBan ban=new classBan(snapshot1.child("masoban").getValue().toString(),
                                             snapshot1.child("soghe").getValue().toString(),
                                             snapshot1.child("idBan").getValue().toString(),
                                             Boolean.valueOf(snapshot1.child("tinhTrang").getValue().toString()),
                                             0);

                                     arrayList.add(ban);//chứa khóacủa bàn
                                     masoban.add(snapshot1.child("masoban").getValue().toString());
                                 }

                             }


                             ArrayAdapter adapter=new ArrayAdapter(HoaDonFragment.this, android.R.layout.simple_list_item_1,masoban);
                             listView.setAdapter(adapter);
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {

                         }
                     });
                     listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                         @Override
                         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                             DatabaseReference  menu_ref1=FirebaseDatabase.getInstance().getReference()
                                     .child("NhaHang")
                                     .child(intent.getStringExtra("id_nhahang"))
                                     .child("Ban").child(arrayList.get(position).getIdBan());
                             menu_ref1.child("tinhTrang").setValue(true);
                             String id_ban=arrayList.get(position).getIdBan();
                             int i=0;
                             for (classHoaDon hoaDon:hoaDonArrayList){
                                 classHoaDon hoaDon1=new classHoaDon("",hoaDon.getId_ban(),hoaDon.getDate_goimon()
                                         ,hoaDon.getSoluong(),hoaDon.getId_mon(),hoaDon.getTinhtrang(),hoaDon.getChat());
                                 hoaDon1.setId_ban(id_ban);
                                 //  Toast.makeText(HoaDonFragment.this, hoaDon.getDate_goimon()+"", Toast.LENGTH_SHORT).show();
                                 if(hoaDon.getLoai()==0){
                                     menu_ref1.child("DSMon").child("mon").child(key_order.get(i)).setValue(hoaDon1);

                                 }else    menu_ref1.child("DSMon").child("combo").child(key_order.get(i)).setValue(hoaDon1);
                                 i++;
                                 if(i==hoaDonArrayList.size()){
                                     menu_ref.child("DSMon").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                         @Override
                                         public void onComplete(@NonNull Task<Void> task) {
                                             if(task.isSuccessful()){
                                                 menu_ref.child("tinhTrang").setValue(false);
                                                 Intent intent1 =new Intent(HoaDonFragment.this, HoaDonFragment.class);
                                                 intent1.putExtra("id_nhahang",intent.getStringExtra("id_nhahang"));
                                                 intent1.putExtra("nameban",arrayList.get(position).getIdBan());
                                                 intent1.putExtra("maban",arrayList.get(position).getMasoban());
                                                 intent1.putExtra("pass",arrayList.get(position).getPass()+"");
                                                 intent1.putExtra("dk","1");
                                                 startActivity(intent1);
                                             }
                                         }
                                     });

                                 }
                             }

                         }
                     });
                     dialog.show();
                 }else Toast.makeText(HoaDonFragment.this, "Chỉ nhân viên phục vụ mới được đổi bàn !", Toast.LENGTH_SHORT).show();

            }
        });
        thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //menu_ref.removeValue();
            if(intent.getStringExtra("chucvu").equals("0")){
                int test=0;
                for (classHoaDon hoaDon:hoaDonArrayList){
                    if(!hoaDon.getTinhtrang().equals("Đã Xong")){
                        test=1;
                        //kiểm tra xem các món đã hoàn thành chưa
                        break;
                    }
                }
                if (test!=1){
                    if(user!=null){
                        Dialog dialog=new Dialog(HoaDonFragment.this);
                        dialog.setContentView(R.layout.dialog_hoadon);
                        RecyclerView listView=dialog.findViewById(R.id.list_buile_hd);
                        Button cancel=dialog.findViewById(R.id.dialog_hoadon_button_cancel);
                        TextView ten_nhahang=dialog.findViewById(R.id.text_tennhahang_dialog_hd);
                        TextView so_ban=dialog.findViewById(R.id.text_bannhahang_dialog_hd);
                        TextView diachi_nhahang=dialog.findViewById(R.id.text_diachinhahang_dialog_hd);
                        TextView ten_nv1=dialog.findViewById(R.id.text_tennv_dialog_hd);
                        TextView Tong_hd=dialog.findViewById(R.id.text_dialog_tonghd);
                        ten_nv1.setText("Nhân viên thu:"+ten_nv);
                        databaseReference.child("TT_NhaHang").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ten_nhahang.setText(snapshot.child("ten_nhahang").getValue().toString());
                                diachi_nhahang.setText(snapshot.child("diachi_nhahang").getValue().toString());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        so_ban.setText(texttenban.getText());
                        Tong_hd.setText(texttonghoadon.getText());

                        adapter_biul_hd  adapter_biul_hd=new adapter_biul_hd(hoaDonArrayList,monAnArrayList
                                ,R.layout.item_hoadon_thanhtoan,HoaDonFragment.this);
                        Window window=dialog.getWindow();
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                        //  Toast.makeText(HoaDonFragment.this, hoaDonArrayList.size()+"", Toast.LENGTH_SHORT).show();
                        // window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        WindowManager.LayoutParams windowaatriss= window.getAttributes();
                        windowaatriss.gravity= Gravity.CENTER;
                        dialog.setCancelable(false);

                        LinearLayoutManager gridLayoutManager=new LinearLayoutManager(HoaDonFragment.this,
                                RecyclerView.VERTICAL,false);
                        listView.setLayoutManager(gridLayoutManager);
                        listView.setAdapter(adapter_biul_hd);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        dialog.show();
//                      Dialog dialog=new Dialog(HoaDonFragment.this);
//                      dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                      dialog.setContentView(R.layout.dialog_input_passban);
//                      Window window=dialog.getWindow();
//                      if(window==null){
//                          return;
//                      }
//                      window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
//                      window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                      WindowManager.LayoutParams windowaatriss= window.getAttributes();
//                      windowaatriss.gravity= Gravity.CENTER;
//                      dialog.setCancelable(false);
//                      EditText input_pass=dialog.findViewById(R.id.dialog_editex_pass);
//                      Button button_ok=dialog.findViewById(R.id.dialog_button_ok);
//                      Button button_cancel=dialog.findViewById(R.id.dialog_button_candel);
//                      button_cancel.setOnClickListener(new View.OnClickListener() {
//                          @Override
//                          public void onClick(View v) {
//                              dialog.dismiss();
//                          }
//                      });
//                      button_ok.setOnClickListener(new View.OnClickListener() {
//                          @Override
//                          public void onClick(View v) {
//                              //Toast.makeText(HoaDonFragment.this, intent.getStringExtra("pass")+"", Toast.LENGTH_SHORT).show();
//                              if (input_pass.getText().toString().equals(intent.getStringExtra("pass"))) {
                        Calendar c = Calendar.getInstance();
                        int year = c.get(Calendar.YEAR);
                        int mouth = c.get(Calendar.MONTH);
                        int day=c.get(Calendar.DAY_OF_MONTH);
                        mouth = mouth + 1;
                        ArrayList<classHoaDon> thongKelisthoadon = new ArrayList<>();
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("NhaHang")
                                .child(intent.getStringExtra("id_nhahang")).child("Thongke")
                                .child(year + "").child(mouth + "").child(day+"");
                        DatabaseReference data_history = FirebaseDatabase.getInstance().getReference().child("NhaHang").child(intent.getStringExtra("id_nhahang")).child("LichSu");
                        DatabaseReference data_chitiet_history = FirebaseDatabase.getInstance().getReference().child("NhaHang")
                                .child(intent.getStringExtra("id_nhahang")).child("ChiTiet_History");
                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                i=0;
                                if(snapshot.exists()==true) {
                                    for (classHoaDon hoaDon : hoaDonArrayList) {
                                        String gia;
                                        if(monAnArrayList.get(i).getLoai_giamgia()!=2){
                                            gia= arrayList_giamgia.get(i).getGia_new();
                                        }else gia=monAnArrayList.get(i).getGia();
                                        if (hoaDon.getLoai() == 0) {
                                            data.child("mon").child(hoaDon.getId_mon()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists() == true) {
                                                        int sl = Integer.parseInt(snapshot.child("soluong").getValue().toString())
                                                                + Integer.parseInt(hoaDon.getSoluong());
                                                        classHoaDon hoaDon1 = new classHoaDon(hoaDon.getId_mon(), sl + "",
                                                                gia);
                                                        data.child("mon").child(hoaDon.getId_mon()).setValue(hoaDon1);
                                                    }else {
                                                        classHoaDon hoaDon1 = new classHoaDon(hoaDon.getId_mon(), hoaDon.getSoluong() + ""
                                                                ,gia);
                                                        data.child("mon").child(hoaDon.getId_mon()).setValue(hoaDon1);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                        }
                                        else {
                                            data.child("combo").child(hoaDon.getId_mon()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists() == true) {
                                                        int sl = Integer.parseInt(snapshot.child("soluong").getValue().toString())
                                                                + Integer.parseInt(hoaDon.getSoluong());
                                                        classHoaDon hoaDon1 = new classHoaDon(hoaDon.getId_mon(), sl + ""
                                                                ,gia);
                                                        data.child("combo").child(hoaDon.getId_mon()).setValue(hoaDon1);
                                                    }else {
                                                        classHoaDon hoaDon1 = new classHoaDon(hoaDon.getId_mon(), hoaDon.getSoluong() + ""
                                                                ,gia);
                                                        data.child("combo").child(hoaDon.getId_mon()).setValue(hoaDon1);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                        i++;
                                    }
                                }
                                else {
                                    for (classHoaDon hoaDon : hoaDonArrayList) {
                                        String gia;
                                        if(monAnArrayList.get(i).getLoai_giamgia()!=2){
                                            gia= arrayList_giamgia.get(i).getGia_new();
                                        }else gia=monAnArrayList.get(i).getGia();
                                        if (hoaDon.getLoai() == 0) {

                                            classHoaDon hoaDon1 = new classHoaDon(hoaDon.getId_mon(), hoaDon.getSoluong()
                                                    ,gia);

                                            data.child("mon").child(hoaDon.getId_mon()).setValue(hoaDon1);
                                        }
                                        else {

                                            classHoaDon hoaDon1 = new classHoaDon(hoaDon.getId_mon(), hoaDon.getSoluong()
                                                    ,gia);
                                            data.child("combo").child(hoaDon.getId_mon()).setValue(hoaDon1);
                                        }



                                        i++; }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

//                                  data.addListenerForSingleValueEvent(new ValueEventListener() {
//                                      @Override
//                                      public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                          if (snapshot.exists()==true)//tháng đã có món
//                                          {
//                                              for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                                                  classHoaDon hoaDontt = new classHoaDon(dataSnapshot.child("id_mon").getValue().toString()
//                                                          , dataSnapshot.child("soluong").getValue().toString()
//                                                  );
//                                                  thongKelisthoadon.add(hoaDontt);
//                                              }
//
//
//                                              for (classHoaDon hoadon : hoaDonArrayList) {
//
//                                                  int cout = thongKelisthoadon.size();
//                                                  for (classHoaDon hoaDontk : thongKelisthoadon
//                                                  ) {
//                                                      //Toast.makeText(HoaDonFragment.this, hoadon.getId_mon() + "   vaf   " + thong.getId(), Toast.LENGTH_SHORT).show();
//
//                                                      if (hoaDontk.getId_mon().equals(hoadon.getId_mon())) {
//                                                          int sl = Integer.parseInt(hoaDontk.getSoluong())+Integer.parseInt(hoadon.getSoluong()) ;
//
////                                    HashMap<String, Object> hashMap = new HashMap<>();
////                                    hashMap.put("soluong", sl);
//
//                                                          data.child(hoadon.getId_mon()).child("soluong").setValue(sl);
//
//                                                          // Toast.makeText(HoaDonFragment.this, "update", Toast.LENGTH_SHORT).show();
//                                                      } else cout--;
//
//
//                                                  }
//
//                                                  if (cout == 0) {
//
//                                                      data.child(hoadon.getId_mon()).setValue(hoadon);
//                                                      Toast.makeText(HoaDonFragment.this, "them", Toast.LENGTH_SHORT).show();
//                                                  }
//                                                  Toast.makeText(HoaDonFragment.this, cout + "", Toast.LENGTH_SHORT).show();
//
//
//                                              }
//                                              menu_ref.child("DSMon").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                  @Override
//                                                  public void onComplete(@NonNull Task<Void> task) {
//                                                      if(task.isSuccessful()){
//                                                          menu_ref.child("tinhTrang").setValue(false);
//                                                          menu_ref.child("pass").removeValue();
//                                                          finish();
//                                                      }
//                                                  }
//                                              });
//
//                                          }
//                                          else {
//                                              for (classHoaDon hoadon : hoaDonArrayList) {
//                                                  classHoaDon hoaDon1=new classHoaDon(hoadon.getId_mon(),hoadon.getSoluong());
//                                                  data.child(hoadon.getId_mon()).setValue(hoaDon1);
//
//
//
//                                              }
                        menu_ref.child("DSMon").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                menu_ref.child("tinhTrang").setValue(false);
                                menu_ref.child("pass").removeValue();

                            }
                        });
                        // }
//
//
//
//                                      }
//                                      @Override
//                                      public void onCancelled(@NonNull DatabaseError error) {
//
//                                      }
//
//                                  });
                        DatabaseReference data_chitiet_history_puss=data_chitiet_history.push();
                        DatabaseReference   data_history_puss=data_history.push();
                        Date date=new Date();
                        classhistory classhistory=new classhistory(data_chitiet_history_puss.getKey()
                                ,user.getUid()
                                , intent.getStringExtra("nameban")
                                ,TongTien+"" ,date.toString());
                        data_history_puss.setValue(classhistory);


                        int dem=0;
                        for (classHoaDon hoaDon:hoaDonArrayList){
                            String gia;
                            if(monAnArrayList.get(dem).getLoai_giamgia()!=2){
                                gia= arrayList_giamgia.get(dem).getGia_new();
                            }else gia=monAnArrayList.get(dem).getGia();
                            if (hoaDon.getLoai() == 0) {

                                classHoaDon hoaDon1 = new classHoaDon(hoaDon.getId_mon(), hoaDon.getSoluong()
                                        ,gia);

                                data_chitiet_history_puss.child("mon").child(dem+"").setValue(hoaDon1);

                            }
                            else {

                                classHoaDon hoaDon1 = new classHoaDon(hoaDon.getId_mon(), hoaDon.getSoluong()
                                        ,gia);
                                data_chitiet_history_puss.child("combo").child(dem+"").setValue(hoaDon1);
                                Toast.makeText(HoaDonFragment.this, dem+"", Toast.LENGTH_SHORT).show();
                            }


                            dem++;

                        }



                    }
                    else {Toast.makeText(HoaDonFragment.this, "Bạn Phải Là Nhân Viên Nhà " +
                            "Hàng Mới Được Gọi Món! Mời Bạn " +
                            "Đăng Nhập Tài Khảo.", Toast.LENGTH_SHORT).show();}


                }
                else Toast.makeText(HoaDonFragment.this, "Món Ăn Chưa Xong" +
                        " Không Thể Thanh Toán", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(HoaDonFragment.this, "Chỉ nhân viên hục vụ mới có quyền thanh toán!", Toast.LENGTH_SHORT).show();

              }
        });


    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
       if(item.getItemId()==R.id.Xoa){
           AlertDialog.Builder alert=new AlertDialog.Builder(HoaDonFragment.this);
           alert.setTitle("Thông Báo!:");
           alert.setMessage("Bạn mcó muốn xóa món khỏi danh sách order!");
           alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   classHoaDon hoaDon=hoaDonArrayList.get(adapterHoaDon.getPosi());

                   if(hoaDon.getTinhtrang().equals("Chưa Nhận")){
                       if(hoaDon.getLoai()==0){
                           menu_ref.child("DSMon").child("mon").child(hoaDon.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   Toast.makeText(HoaDonFragment.this, "Xóa món thành công!", Toast.LENGTH_SHORT).show();
                               }
                           });
                       }else  menu_ref.child("DSMon").child("combo").child(hoaDon.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               Toast.makeText(HoaDonFragment.this, "Xóa Combo thành công!", Toast.LENGTH_SHORT).show();
                           }
                       });
                   }else Toast.makeText(HoaDonFragment.this, "Món ăn đã được nhận không thể xóa!", Toast.LENGTH_SHORT).show();




               }
           });
           alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                       dialogInterface.dismiss();
               }
           } );
           alert.show();

       }
        return super.onContextItemSelected(item);
    }

    public void dialog_input_pass(int pos, String id_nhahang){
        Dialog dialog=new Dialog(HoaDonFragment.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_input_passban);
        Window window=dialog.getWindow();
        if(window==null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowaatriss= window.getAttributes();
        windowaatriss.gravity= Gravity.CENTER;
        dialog.setCancelable(false);
        EditText input_pass=dialog.findViewById(R.id.dialog_editex_pass);
        Button button_ok=dialog.findViewById(R.id.dialog_button_ok);
        Button button_cancel=dialog.findViewById(R.id.dialog_button_candel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
//        button_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if( arrayList.get(pos).getPass()==Integer.parseInt(input_pass.getText().toString())){
//                    Intent intent =new Intent(mcontext, HoaDonFragment.class);
//                    intent.putExtra("id_nhahang",id_nhahang);
//                    intent.putExtra("nameban",arrayList.get(pos).getIdBan());
//                    intent.putExtra("maban",arrayList.get(pos).getMasoban());
//                    mcontext.startActivity(intent);
//                }
//            }
//        });
        dialog.show();

    }

    @Override
    public void onBackPressed() {
          if(intent.getStringExtra("dk")!=null){
              Intent intent1=new Intent(HoaDonFragment.this,MianActivityBanac.class);
              intent1.putExtra("id_nhahang",intent.getStringExtra("id_nhahang"));
              intent1.putExtra("chucvu",chucvu);
              startActivity(intent1);
          }else super.onBackPressed();

    }

}

