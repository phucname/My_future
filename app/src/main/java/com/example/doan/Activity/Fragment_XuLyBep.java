package com.example.doan.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.Adapter.Adapter_bep;
import com.example.doan.Class.classHoaDon;
import com.example.doan.Class.class_Bep;
import com.example.doan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;

public class Fragment_XuLyBep extends Fragment {



    ArrayList<classHoaDon> hoaDonArrayList;
    ArrayList<classHoaDon> comboArrayList;
    Adapter_bep adapter_bep;
    ListView listView;
    String keyban;
    DatabaseReference databaseReference;
    MianActivityBanac mianActivityBanac;
    ArrayList<String> keymon;
    ProgressDialog progressDialog;
    TextView danhan, chuanhan;
    public String getKeyban() {
        return keyban;
    }
    public void setKeyban(String keyban) {
        this.keyban = keyban;
    }
    TextView t;

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__xu_ly_bep, container, false);
        mianActivityBanac = (MianActivityBanac) getActivity();
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("NhaHang").child(mianActivityBanac.getId_nhahang()).child("Ban");
        danhan = view.findViewById(R.id.button_ds_mon_oder2);
        chuanhan = view.findViewById(R.id.button_ds_mon_oder1);
        progressDialog=new ProgressDialog(view.getContext());
        listView = view.findViewById(R.id.listview_mon_bep);
        getds("Chưa Nhận");
        chuanhan.setBackgroundResource(R.color.red);
        chuanhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                danhan.setBackgroundResource(R.color.xam);
                chuanhan.setBackgroundResource(R.color.red);
                getds("Chưa Nhận");
            }
        });

        danhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                danhan.setBackgroundResource(R.color.red);
                chuanhan.setBackgroundResource(R.color.xam);
                getds("Đã Nhận");
            }
        });


        return view;
    }

    private void getds(String dk) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hoaDonArrayList = new ArrayList<>();
                keymon = new ArrayList<>();
                comboArrayList = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.child("DSMon").child("mon").exists()) {
                        for(DataSnapshot snapshot2:snapshot1.child("DSMon").child("mon").getChildren())
                        {
                                            if (snapshot2.child("tinhtrang").getValue().toString().equals(dk)) {
                                                keymon.add(snapshot2.getKey());
                                                hoaDonArrayList.add(new classHoaDon(snapshot2.getKey(),
                                                        snapshot2.child("id_ban").getValue().toString()
                                                        , snapshot2.child("tinhtrang").getValue().toString()
                                                        , snapshot2.child("soluong").getValue().toString()
                                                        , snapshot2.child("id_mon").getValue().toString()
                                                        , snapshot2.child("date_goimon").getValue().toString()
                                                        , 0,snapshot2.child("chat").getValue().toString()));
//                                  class_Bep bep=  new class_Bep( snapshot2.child("img").getValue().toString()
//                                          ,snapshot2.child("tenmon").getValue().toString(),
//                                          snapshot2.child("gia").getValue().toString()
//                                          ,snapshot2.child("tt").getValue().toString()
//                                          ,snapshot2.child("soluong").getValue().toString()
//                                          ,snapshot2.getKey(),
//                                          snapshot2.child("date").getValue().toString(),
//                                          snapshot2.child("soban").getValue().toString());
//                                  if(snapshot2.child("daubep").exists()){
//                                      bep.setDaubep(snapshot2.child("daubep").getValue().toString());
                                                // }
                                                //  hoaDonArrayList.add(bep);
                                                adapter_bep = new Adapter_bep(getActivity(), hoaDonArrayList, R.layout.itembep, t, mianActivityBanac.getId_nhahang());
                                                listView.setAdapter(adapter_bep);
                                            }
                                        }
                                    }


//                        adapter_bep = new Adapter_bep(getActivity(), hoaDonArrayList, R.layout.itembep, t, mianActivityBanac.getId_nhahang());
//                        listView.setAdapter(adapter_bep);
                    if (snapshot1.child("DSMon").child("combo").exists()) {

                                        for (DataSnapshot snapshot2 : snapshot1.child("DSMon").child("combo").getChildren()) {
                                            if (snapshot2.child("tinhtrang").getValue().toString().equals(dk)) {

                                                keymon.add(snapshot2.getKey());
                                                hoaDonArrayList.add(new classHoaDon(snapshot2.getKey(),snapshot2.child("id_ban").getValue().toString()
                                                        , snapshot2.child("tinhtrang").getValue().toString()
                                                        , snapshot2.child("soluong").getValue().toString()
                                                        , snapshot2.child("id_mon").getValue().toString(), snapshot2.child("date_goimon").getValue().toString()
                                                        , 1,snapshot2.child("chat").getValue().toString()));
                                                adapter_bep = new Adapter_bep(getActivity(), hoaDonArrayList, R.layout.itembep, t, mianActivityBanac.getId_nhahang());
                                                listView.setAdapter(adapter_bep);
                                            }
                                        }
                                        adapter_bep = new Adapter_bep(getActivity(), hoaDonArrayList, R.layout.itembep, t, mianActivityBanac.getId_nhahang());
                                        listView.setAdapter(adapter_bep);
                                    }


                    adapter_bep = new Adapter_bep(getActivity(), hoaDonArrayList, R.layout.itembep, t, mianActivityBanac.getId_nhahang());
                    listView.setAdapter(adapter_bep);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nhanmon) {
           if(mianActivityBanac.getChuvu_nv()==2){
               progressDialog.show();
               classHoaDon hoaDon = hoaDonArrayList.get(adapter_bep.getPosition());
              // Toast.makeText(mianActivityBanac, adapter_bep.getPosition()+"//so", Toast.LENGTH_SHORT).show();
              // Toast.makeText(mianActivityBanac, keymon.get(adapter_bep.getPosition())+"", Toast.LENGTH_SHORT).show();
               MianActivityBanac mianActivityBanac = (MianActivityBanac) getActivity();
               if (hoaDon.getLoai() == 0) {

                   DatabaseReference data = FirebaseDatabase.getInstance().getReference()
                           .child("NhaHang").child(mianActivityBanac.getId_nhahang())
                           .child("Ban")
                           .child(hoaDon.getId_ban())
                           .child("DSMon").child("mon").child(keymon.get(adapter_bep.getPosition()));
                   data.child("tinhtrang").setValue("Đã Nhận").addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               TextView textView = mianActivityBanac.getTen();
                               data.child("daubep").setValue(textView.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if(task.isSuccessful()){
                                           progressDialog.dismiss();
                                           hoaDonArrayList.remove(adapter_bep.getPosition());
                                           keymon.remove(adapter_bep.getPosition());
                                           adapter_bep.notifyDataSetChanged();
                                       }
                                       // getds();
                                       //  Toast.makeText(mianActivityBanac, keymon.get(adapter_bep.getPosition()) + "", Toast.LENGTH_SHORT).show();
                                   }
                               });
                           }
                       }
                   });




                   //Toast.makeText(mianActivityBanac, snapshot1.getKey(), Toast.LENGTH_SHORT).show();
//                       File file;
//                       ContextWrapper contextWrapper=new ContextWrapper(getActivity());
//                      File dix=contextWrapper.getDir("TK", Context.MODE_APPEND);
//                     file=new File(dix,"Don.txt");
//                       FileOutputStream or = null;
//                       try {
//                          or = new FileOutputStream(file);
//                           or.write((mianActivityBanac.getId_nhahang()+ "," +
//                                   hoaDon.getId_ban()+"/DSMon/"+
//                                   ","+ keyban).getBytes());
//                           or.write(("\n").getBytes());
//                           or.flush();
//                       } catch (FileNotFoundException e) {
//                           e.printStackTrace();
//                       } catch (IOException e) {
//                           e.printStackTrace();
//                       }
//
//
//                       // Toast.makeText(GoiMonActivity.this, "okkkk", Toast.LENGTH_SHORT).show();
//
//
//                       Intent intent=new Intent(getContext(),xuly_monan_activity.class);
//
//                       startActivity(intent);


                   // Toast.makeText(mianActivityBanac, snapshot.child("")+"", Toast.LENGTH_SHORT).show();



               } else {
                   DatabaseReference data = FirebaseDatabase.getInstance().getReference()
                           .child("NhaHang").child(mianActivityBanac.getId_nhahang())
                           .child("Ban")
                           .child(hoaDon.getId_ban())
                           .child("DSMon").child("combo").child(keymon.get(adapter_bep.getPosition()));
                   data.child("tinhtrang").setValue("Đã Nhận").addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               TextView textView = mianActivityBanac.getTen();
                               data.child("daubep").setValue(textView.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if(task.isSuccessful()){
                                           progressDialog.dismiss();
                                           hoaDonArrayList.remove(adapter_bep.getPosition());
                                           keymon.remove(adapter_bep.getPosition());                                           adapter_bep.notifyDataSetChanged();
                                       }
                                   }
                               });
                           }
                       }
                   });



               }

//        if(item.getItemId()==R.id.nhan_combo){
//            classHoaDon hoaDon=comboArrayList.get(adapter_combobep.getPosition());
//            MianActivityBanac mianActivityBanac= (MianActivityBanac) getActivity();
//
//
               //Toast.makeText(mianActivityBanac, snapshot1.getKey(), Toast.LENGTH_SHORT).show();
//            File file;
//            ContextWrapper contextWrapper=new ContextWrapper(getActivity());
//            File dix=contextWrapper.getDir("TK", Context.MODE_APPEND);
//            file=new File(dix,"Don.txt");
//            FileOutputStream or = null;
//            try {
//                or = new FileOutputStream(file);
//                or.write((mianActivityBanac.getId_nhahang()+ "," +
//                        hoaDon.getId_ban()+"/DSMon/"+
//                        ","+ keyban).getBytes());
//                or.write(("\n").getBytes());
//                or.flush();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//            // Toast.makeText(GoiMonActivity.this, "okkkk", Toast.LENGTH_SHORT).show();
//
//
//            Intent intent=new Intent(getContext(),xuly_monan_activity.class);
//
//            startActivity(intent);


               // Toast.makeText(mianActivityBanac, snapshot.child("")+"", Toast.LENGTH_SHORT).show();


           }else {
               Toast.makeText(mianActivityBanac, "Chỉ có đầu bếp mới được sử dụng chức năng này!", Toast.LENGTH_SHORT).show();
           }

        }
        if (item.getItemId() == R.id.hoanthanh) {
          if(mianActivityBanac.getChuvu_nv()==2){
              progressDialog.show();
              classHoaDon hoaDon = hoaDonArrayList.get(adapter_bep.getPosition());
              DatabaseReference data = FirebaseDatabase.getInstance().getReference()
                      .child("NhaHang").child(mianActivityBanac.getId_nhahang())
                      .child("Ban").child(hoaDon.getId_ban()).child("DSMon");//DSM
              if (hoaDon.getLoai() == 0) {
                  data.child("mon").child(keymon.get(adapter_bep.getPosition()))
                          .child("tinhtrang").setValue("Đã Xong").addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                  if (task.isSuccessful()) {
                                      Query query = data.child("mon").orderByChild("id_mon").equalTo(hoaDon.getId_mon());
                                      query.addListenerForSingleValueEvent(new ValueEventListener() {
                                          @Override
                                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                                              if (snapshot.getChildrenCount() >1) {
                                                  int i = 0, sl1 = 0;
                                                  ArrayList<String>key=new ArrayList<>() ;
                                                  String key2="";
                                                  for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                                      if (snapshot1.child("tinhtrang").getValue().toString()
                                                              .equals("Đã Xong")) {
                                                          i++;
                                                          if (snapshot1.getKey().equals(keymon.get(adapter_bep.getPosition()))) {
                                                              key2=snapshot1.getKey();
                                                              Toast.makeText(mianActivityBanac, key2+"//khoasss", Toast.LENGTH_SHORT).show();

                                                          } else {
                                                              key.add(snapshot1.getKey());
                                                              sl1 =sl1+ Integer.parseInt(snapshot1.child("soluong").getValue().toString());

                                                          }

                                                      }
                                                  }
                                                  if (i >1) {
                                                      //   Toast.makeText(mianActivityBanac, keymon.get(adapter_bep.getPosition())+"", Toast.LENGTH_SHORT).show();
                                                      for (String key_oder:key
                                                           ) {
                                                          data.child("mon").child(key_oder).removeValue();
                                                      }

                                                      int sl = sl1 + Integer.parseInt(hoaDon.getSoluong());
                                                      data.child("mon").child(key2).child("soluong")
                                                              .setValue(sl + "").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                  @Override
                                                                  public void onComplete(@NonNull Task<Void> task) {
                                                                      if(task.isSuccessful()){
                                                                          adapter_bep.notifyDataSetChanged();
                                                                          keymon.remove(adapter_bep.getPosition());
                                                                          progressDialog.dismiss();
                                                                      }
                                                                  }
                                                              });
                                                  }
                                              }else {
                                                  adapter_bep.notifyDataSetChanged();
                                                  keymon.remove(adapter_bep.getPosition());
                                                  progressDialog.dismiss();
                                              }
                                          }

                                          @Override
                                          public void onCancelled(@NonNull DatabaseError error) {

                                          }
                                      });
                                  }
                              }
                          });
              }else {

                  data.child("combo").child(keymon.get(adapter_bep.getPosition()))
                          .child("tinhtrang").setValue("Đã Xong").addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                  if (task.isSuccessful()) {
                                      Query query = data.child("combo").orderByChild("id_mon").equalTo(hoaDon.getId_mon());
                                      query.addListenerForSingleValueEvent(new ValueEventListener() {
                                          @Override
                                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                                              if (snapshot.getChildrenCount() >1) {
                                                  int i = 0, sl1 = 0;
                                                  ArrayList<String>key=new ArrayList<>() ;
                                                  String key2="";
                                                  for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                                      if (snapshot1.child("tinhtrang").getValue().toString()
                                                              .equals("Đã Xong")) {
                                                          i++;
                                                          if (snapshot1.getKey().equals(keymon.get(adapter_bep.getPosition()))) {
                                                              key2=snapshot1.getKey();
                                                          } else {
                                                              key.add(snapshot1.getKey());
                                                              sl1 =sl1+ Integer.parseInt(snapshot1.child("soluong").getValue().toString());
                                                          }

                                                      }
                                                  }
                                                  if (i >1) {
                                                   // Toast.makeText(mianActivityBanac, keymon.get(adapter_bep.getPosition())+"", Toast.LENGTH_SHORT).show();
                                                      for (String key_oder:key
                                                      ) {
                                                          data.child("combo").child(key_oder).removeValue();
                                                      }

                                                      int sl = sl1 + Integer.parseInt(hoaDon.getSoluong());
                                                      data.child("combo").child(key2).child("soluong")
                                                              .setValue(sl + "").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                  @Override
                                                                  public void onComplete(@NonNull Task<Void> task) {
                                                                      if(task.isSuccessful()){
                                                                          keymon.remove(adapter_bep.getPosition());
                                                                          adapter_bep.notifyDataSetChanged();
                                                                          progressDialog.dismiss();
                                                                      }
                                                                  }
                                                              });
                                                  }
                                              }else {
                                                  adapter_bep.notifyDataSetChanged();
                                                  keymon.remove(adapter_bep.getPosition());
                                                  progressDialog.dismiss();
                                              }
                                          }

                                          @Override
                                          public void onCancelled(@NonNull DatabaseError error) {

                                          }
                                      });
                                  }
                              }
                          });
              }
              hoaDonArrayList.remove(adapter_bep.getPosition());
              adapter_bep.notifyDataSetChanged();
          }else                Toast.makeText(mianActivityBanac, "Chỉ có đầu bếp mới được sử dụng chức năng này!", Toast.LENGTH_SHORT).show();


        }
        return super.onContextItemSelected(item);

    }
}