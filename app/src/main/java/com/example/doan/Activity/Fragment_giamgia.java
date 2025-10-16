package com.example.doan.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.doan.Adapter.Adapter_giamgia;
import com.example.doan.Adapter.AdapterrecymenuMA;
import com.example.doan.Adapter.adapter_spienr;
import com.example.doan.Class.MonAn;
import com.example.doan.Class.class_giamgia;
import com.example.doan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Fragment_giamgia extends Fragment {

RecyclerView recyc_ds_giamgia;
ArrayList<MonAn>manArrayList;
DatabaseReference databaseReference;
MianActivityBanac mianActivityBanac;
Adapter_giamgia adapter_mon;
int kieu_giamgia;
FloatingActionButton them;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_giamgia, container, false);
        recyc_ds_giamgia=view.findViewById(R.id.recy_giamgia);
        them=view.findViewById(R.id.floatbutton_themgiamgia);
        mianActivityBanac= (MianActivityBanac) getActivity();
        if(mianActivityBanac.getChuvu_nv()!=1){
            them.setVisibility(View.INVISIBLE);
        }
        databaseReference= FirebaseDatabase.getInstance().getReference()
                .child("NhaHang").child(mianActivityBanac.getId_nhahang());

          databaseReference.child("DS_MonAn").addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  manArrayList=new ArrayList<>();
                  for (DataSnapshot snapshot1:snapshot.getChildren()) {
                      if (Integer.parseInt(snapshot1.child("loai_giamgia").getValue().toString()) != 2) {
                          MonAn monAn = new MonAn(snapshot1.child("tenmon").getValue().toString(),
                                  snapshot1.child("gia").getValue().toString(),
                                  snapshot1.child("ghichu").getValue().toString(),
                                  snapshot1.child("imgmon").getValue().toString(),
                                  snapshot1.child("id").getValue().toString(),
                                  "0", Integer.parseInt(snapshot1.child("loai_giamgia").getValue().toString()));
                          manArrayList.add(monAn);
                          // Toast.makeText(mianActivityBanac, manArrayList.size()+"", Toast.LENGTH_SHORT).show();

                      }
                  }
                  adapter_mon=new Adapter_giamgia(manArrayList,R.layout.item_giamgia,view.getContext(),mianActivityBanac.getId_nhahang(),1);
                  // GridLayoutManager gridLayoutManager=new GridLayoutManager(view.getContext(), 2);
                  LinearLayoutManager gridLayoutManager=new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL,false);
                  recyc_ds_giamgia.setLayoutManager(gridLayoutManager);
                  recyc_ds_giamgia.setAdapter(adapter_mon);

              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });

          databaseReference.child("DS_ComBo").addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {

                  for (DataSnapshot snapshot1:snapshot.getChildren()) {
                      if (Integer.parseInt(snapshot1.child("loai_giamgia").getValue().toString()) != 2) {
                          MonAn monAn = new MonAn(snapshot1.child("ten_combo").getValue().toString(),
                                  snapshot1.child("gia_combo").getValue().toString(),
                                  "",
                                  snapshot1.child("img_combo").getValue().toString(),
                                  snapshot1.child("id_combo").getValue().toString(),
                                  "1", Integer.parseInt(snapshot1.child("loai_giamgia").getValue().toString()));
                          manArrayList.add(monAn);
                          // Toast.makeText(mianActivityBanac, manArrayList.size()+"", Toast.LENGTH_SHORT).show();

                      }
                  }
                  adapter_mon=new Adapter_giamgia(manArrayList,R.layout.item_giamgia,view.getContext(),mianActivityBanac.getId_nhahang(),1);
                  // GridLayoutManager gridLayoutManager=new GridLayoutManager(view.getContext(), 2);
                  LinearLayoutManager gridLayoutManager=new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL,false);
                  recyc_ds_giamgia.setLayoutManager(gridLayoutManager);
                  recyc_ds_giamgia.setAdapter(adapter_mon);
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });



        them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Them_giamgia.class);
                intent.putExtra("id_nhahang",mianActivityBanac.getId_nhahang());
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.Sua:
                Dialog dialog=new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_giamgia);
                Window window=dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                //window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams windowaatriss= window.getAttributes();
                windowaatriss.gravity= Gravity.CENTER;
                ArrayList<String>arrayList_loai=new ArrayList<>();
                arrayList_loai.add("Phần Trăm");
                arrayList_loai.add("Số Tiền");

                EditText giatri=dialog.findViewById(R.id.dialog_giatrigiam);
                Spinner spinner_loai=dialog.findViewById(R.id.dialog_Spiner_giamgia);
                adapter_spienr adapter_spienr = new adapter_spienr(getActivity()
                        , R.layout.itme_spiner, arrayList_loai,"Kiểu Giảm");
                spinner_loai.setAdapter(adapter_spienr);
                spinner_loai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        kieu_giamgia=position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                Button them=dialog.findViewById(R.id.dialog_button_them_mongiamgia);
                them.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     MonAn monAn=manArrayList.get(adapter_mon.getStt());
                        Toast.makeText(mianActivityBanac,monAn.getLoai_mon()+ "", Toast.LENGTH_SHORT).show();
                        if (kieu_giamgia == 0) {
                            if (Integer.parseInt(giatri.getText().toString()) < 100) {

                                    if (monAn.getLoai_mon().equals("0")) {
                                        them_giamgia(monAn.getId(), "DS_MonAn", giatri.getText().toString(), "PT");
                                    } else
                                        them_giamgia(monAn.getId(), "DS_ComBo", giatri.getText().toString(), "PT");

                                }
                            }
                        else {
                            if (Integer.parseInt(monAn.getGia()) > Integer.parseInt(giatri.getText().toString())) {

                                if(monAn.getLoai_mon().equals("0")){
                                    them_giamgia(monAn.getId(),"DS_MonAn",giatri.getText().toString(),"Tien");
                                }else  them_giamgia(monAn.getId(),"DS_ComBo",giatri.getText().toString(),"Tien");
//
                            }else
                                Toast.makeText(getActivity(), "Món "+monAn.getTenmon()+" có số tiền nhỏ hơn giá trị khuyến mãi không áp dụng được", Toast.LENGTH_SHORT).show();


                        dialog.dismiss();}

                        }

                });
             dialog.show();


             break;
            case R.id.Xoa:
                MonAn monAn=manArrayList.get(adapter_mon.getStt());
                String kieu_loai="";
                if(monAn.getLoai_mon().equals("0")){
                 kieu_loai="DS_MonAn";
                }else kieu_loai="DS_ComBo";
                databaseReference.child(kieu_loai).child(monAn.getId()).child("loai_giamgia")
                        .setValue("2").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                          if(monAn.getLoai_giamgia()==0){
                              databaseReference.child("Khuyen_mai").child("PT").child(monAn.getId()).removeValue();
                          }else databaseReference.child("Khuyen_mai").child("Tien").child(monAn.getId()).removeValue();
                           manArrayList.remove(adapter_mon.getStt());
                          adapter_mon.notifyDataSetChanged();

                        }
                    }
                });


                break;
        }

        return super.onContextItemSelected(item);

    }
    private void them_giamgia(String idmon,String ten_chu,String giatri,String kieu_gg){
       // Toast.makeText(mianActivityBanac,ten_chu+ "/"+idmon, Toast.LENGTH_SHORT).show();
        databaseReference.child(ten_chu).child(idmon).child("loai_giamgia").setValue(kieu_giamgia).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    class_giamgia giamgia=new class_giamgia(idmon,"11/5/2023,15/5/2023",giatri);
                    databaseReference.child("Khuyen_mai").child(kieu_gg).child(idmon).setValue(giamgia).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                if(kieu_gg.equals("PT")){
                                    databaseReference.child("Khuyen_mai").child("Tien").child(idmon).removeValue();
                                }else databaseReference.child("Khuyen_mai").child("PT").child(idmon).removeValue();
                            }else {
                                databaseReference.child(ten_chu).child(idmon).child("loai_giamgia").setValue(2);
                            }
                        }
                    });
                }
            }
        });
    }
}