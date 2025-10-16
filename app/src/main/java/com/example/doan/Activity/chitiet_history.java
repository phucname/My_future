package com.example.doan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.Adapter.Adapter_Giohang;
import com.example.doan.Adapter.Adapter_Hoadon;
import com.example.doan.Adapter.Adapter_chitiet_history;
import com.example.doan.Class.MonAn;
import com.example.doan.Class.classHoaDon;
import com.example.doan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class chitiet_history extends AppCompatActivity {
ArrayList<classHoaDon>arrayList;
ArrayList<MonAn>monAnArrayList;
RecyclerView listView;
Adapter_chitiet_history adapter_chitiet_history;
TextView textView;
    Intent intent;
int tongbill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_history);
        listView=findViewById(R.id.listview_chitiet_history);
        textView=findViewById(R.id.text_tong_chitiethistory);
        intent=getIntent();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("NhaHang")
                .child(intent.getStringExtra("id_nhahang"));
        databaseReference.child("ChiTiet_History")
                .child(intent.getStringExtra("id_chitiet")).child("mon").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList=new ArrayList<>();
                        monAnArrayList=new ArrayList<>();
                        if(snapshot.exists()==true){
                            for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                                arrayList.add(new classHoaDon(dataSnapshot.child("id_mon").getValue().toString(),
                                        dataSnapshot.child("soluong").getValue().toString()
                                        ,dataSnapshot.child("gia").getValue().toString()
                                ) );
                                databaseReference.child("DS_MonAn").child(dataSnapshot.child("id_mon").getValue().toString())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                               if(snapshot.getValue()==null){
                                                   databaseReference.child("Thuc_Don_Cu").child("mon")
                                                           .child(dataSnapshot.child("id_mon").getValue().toString())
                                                           .addListenerForSingleValueEvent(new ValueEventListener() {
                                                               @Override
                                                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                   monAnArrayList.add(new MonAn(snapshot.child("tenmon").getValue().toString()
                                                                           , snapshot.child("gia").getValue().toString()
                                                                           , snapshot.child("ghichu").getValue().toString()
                                                                           , snapshot.child("imgmon").getValue().toString(),
                                                                           Integer.parseInt(snapshot.child("loai_giamgia").getValue().toString())));
                                                                   tongbill = tongbill + (Integer.parseInt(dataSnapshot.child("gia").getValue().toString())
                                                                           * Integer.parseInt(dataSnapshot.child("soluong").getValue().toString()));
                                                                   adapter_chitiet_history=new Adapter_chitiet_history(getApplicationContext(),R.layout.item_chitiet_history
                                                                           ,arrayList,monAnArrayList,intent.getStringExtra("id_nhahang"));
                                                                   GridLayoutManager gridLayoutManager=new GridLayoutManager(chitiet_history.this, 1);
                                                                   listView.setLayoutManager(gridLayoutManager);
                                                                   listView.setAdapter(adapter_chitiet_history);
                                                                   MianActivityBanac mianActivityBanac=new MianActivityBanac();
                                                                   textView.setText("Tổng:"+mianActivityBanac.VND(tongbill));
                                                               }

                                                               @Override
                                                               public void onCancelled(@NonNull DatabaseError error) {

                                                               }
                                                           });
                                               }else {
                                                   monAnArrayList.add(new MonAn(snapshot.child("tenmon").getValue().toString()
                                                           , snapshot.child("gia").getValue().toString()
                                                           , snapshot.child("ghichu").getValue().toString()
                                                           , snapshot.child("imgmon").getValue().toString(),
                                                           Integer.parseInt(snapshot.child("loai_giamgia").getValue().toString())));
                                                   tongbill = tongbill + (Integer.parseInt(dataSnapshot.child("gia").getValue().toString())
                                                           * Integer.parseInt(dataSnapshot.child("soluong").getValue().toString()));

                                                   adapter_chitiet_history=new Adapter_chitiet_history(getApplicationContext(),R.layout.item_chitiet_history
                                                           ,arrayList,monAnArrayList,intent.getStringExtra("id_nhahang"));
                                                   GridLayoutManager gridLayoutManager=new GridLayoutManager(chitiet_history.this, 1);
                                                   listView.setLayoutManager(gridLayoutManager);
                                                   listView.setAdapter(adapter_chitiet_history);
                                                   MianActivityBanac mianActivityBanac=new MianActivityBanac();
                                                   textView.setText("Tổng:"+mianActivityBanac.VND(tongbill));
                                               }


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                            }
                        }

                        // Toast.makeText(chitiet_history.this, arrayList.size()+"/"+intent.getStringExtra("id_nhahang")+"/"+intent.getStringExtra("id_chitiet"), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        databaseReference.child("ChiTiet_History")
                .child(intent.getStringExtra("id_chitiet")).child("combo").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()==true){
                            for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                arrayList.add(new classHoaDon(dataSnapshot.child("id_mon").getValue().toString(),
                                        dataSnapshot.child("soluong").getValue().toString()
                                        ,dataSnapshot.child("gia").getValue().toString()
                                ) );
                                databaseReference.child("DS_ComBo").child(dataSnapshot.child("id_mon").getValue().toString())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.getValue() == null) {
                                                    databaseReference.child("Thuc_Don_Cu").child("combo")
                                                            .child(dataSnapshot.child("id_mon").getValue().toString())
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    monAnArrayList.add(new MonAn(snapshot.child("tenmon").getValue().toString()
                                                                            , snapshot.child("gia").getValue().toString()
                                                                            , snapshot.child("ghichu").getValue().toString()
                                                                            , snapshot.child("imgmon").getValue().toString(),
                                                                            Integer.parseInt(snapshot.child("loai_giamgia").getValue().toString())));
                                                                    tongbill = tongbill + (Integer.parseInt(dataSnapshot.child("gia").getValue().toString())
                                                                            * Integer.parseInt(dataSnapshot.child("soluong").getValue().toString()));
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                } else {


                                                    monAnArrayList.add(new MonAn(snapshot.child("ten_combo").getValue().toString()
                                                            , snapshot.child("gia_combo").getValue().toString()
                                                            , ""
                                                            , snapshot.child("img_combo").getValue().toString(),
                                                            Integer.parseInt(snapshot.child("loai_giamgia").getValue().toString())));
                                                    tongbill = tongbill + (Integer.parseInt(dataSnapshot.child("gia").getValue().toString())
                                                            * Integer.parseInt(dataSnapshot.child("soluong").getValue().toString()));

                                                } adapter_chitiet_history = new Adapter_chitiet_history(getApplicationContext(), R.layout.item_hoadon
                                                        , arrayList, monAnArrayList, intent.getStringExtra("id_nhahang"));
                                                GridLayoutManager gridLayoutManager = new GridLayoutManager(chitiet_history.this, 1);
                                                listView.setLayoutManager(gridLayoutManager);
                                                listView.setAdapter(adapter_chitiet_history);
                                                MianActivityBanac mianActivityBanac = new MianActivityBanac();
                                                textView.setText("Tổng:" + mianActivityBanac.VND(tongbill));
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                            }
                        }


                        // Toast.makeText(chitiet_history.this, arrayList.size()+"/"+intent.getStringExtra("id_nhahang")+"/"+intent.getStringExtra("id_chitiet"), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
  }

    @Override
    public void onBackPressed() {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        Intent intent=new Intent(chitiet_history.this,MianActivityBanac.class);
        intent.putExtra("id_nhahang",user.getUid());
        intent.putExtra("chucvu","1");
        intent.putExtra("them","history");
        startActivity(intent);
    }
}