package com.example.doan.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.doan.Adapter.AdapterrecymenuMA;
import com.example.doan.Adapter.adapter_spienr;
import com.example.doan.Class.MonAn;
import com.example.doan.Class.class_loai;
import com.example.doan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Fragment_ds_mon extends Fragment {

    RecyclerView recycler_mon;
    public ArrayList<MonAn> arrayList_mon;
    AdapterrecymenuMA adapter_mon;
    DatabaseReference databaseReference;
    MianActivityBanac mianActivityBanac;
    SearchView searchView_monan;
    FloatingActionButton them_mon;
RadioGroup radioGroup;
Spinner timloai_mon;
    int dk=0;
int tes1t=0;
ArrayList<class_loai>arrayList_loai;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_ds_mon, container, false);
    recycler_mon=view.findViewById(R.id.recyclerview_ds_mon);
    radioGroup=view.findViewById(R.id.radio_group);
    searchView_monan=view.findViewById(R.id.search_ds_monan);
    them_mon=view.findViewById(R.id.floatbutton_them_mon);
    timloai_mon=view.findViewById(R.id.spinner_timkiem_loai);
        mianActivityBanac= (MianActivityBanac) getActivity();
    databaseReference= FirebaseDatabase.getInstance().getReference()
            .child("NhaHang").child(mianActivityBanac.getId_nhahang());
    if(mianActivityBanac.getChuvu_nv()!=1){
        them_mon.setVisibility(View.INVISIBLE);
    }
    getloai();

    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
              if(checkedId==R.id.radio_tim_gia){
                  searchView_monan.setVisibility(View.VISIBLE);
                  timloai_mon.setVisibility(View.INVISIBLE);
                  searchView_monan.setInputType(3);
                 // Toast.makeText(mianActivityBanac, "timg giá", Toast.LENGTH_SHORT).show();
              }if(checkedId==R.id.radio_tim_ten){
                searchView_monan.setVisibility(View.VISIBLE);
                timloai_mon.setVisibility(View.INVISIBLE);
                  searchView_monan.setInputType(1);
            }
              if(checkedId==R.id.radio_tim_loai){
                  searchView_monan.setVisibility(View.INVISIBLE);
                  timloai_mon.setVisibility(View.VISIBLE);
                //  Toast.makeText(mianActivityBanac, arrayList_loai.size()+"", Toast.LENGTH_SHORT).show();
              }
        }
    });

    timloai_mon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(!arrayList_loai.get(position).getTen().equals("All")){
                ArrayList<MonAn> newmonAnArrayList=new ArrayList<>();
                class_loai loai=arrayList_loai.get(position);
                for (MonAn monAn:arrayList_mon){
                    if(monAn.getLoai_mon().equals(loai.getId())){
                        newmonAnArrayList.add(monAn);
                    }
                }

                adapter_mon.set_arraynew(newmonAnArrayList);
            }else adapter_mon.set_arraynew(arrayList_mon);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });
    databaseReference.child("DS_MonAn").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            arrayList_mon=new ArrayList<>();
            for(DataSnapshot snapshot1:snapshot.getChildren()){

                        MonAn monAn=new MonAn(snapshot1.child("tenmon").getValue().toString(),
                        snapshot1.child("gia").getValue().toString(),
                        snapshot1.child("ghichu").getValue().toString(),
                        snapshot1.child("imgmon").getValue().toString(),
                        snapshot1.child("id").getValue().toString(),
                       "",Integer.parseInt(snapshot1.child("loai_giamgia").getValue().toString()));

                databaseReference.child("Loai_Menu").child(snapshot1.child("loai_mon").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        monAn.setLoai_mon(snapshot.getKey());
                    // Toast.makeText(getContext(), monAn.getLoai_mon()+"", Toast.LENGTH_SHORT).show();
                        arrayList_mon.add(monAn);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });

                // Toast.makeText(getActivity(),arrayListtest.get(0).getGia()+"",Toast.LENGTH_SHORT).show();
            } adapter_mon=new AdapterrecymenuMA(arrayList_mon,R.layout.carrecymenu,view.getContext(),1,mianActivityBanac.getId_nhahang());
             GridLayoutManager gridLayoutManager=new GridLayoutManager(view.getContext(), 2);
            //LinearLayoutManager gridLayoutManager=new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL,false);
            recycler_mon.setLayoutManager(gridLayoutManager);
            recycler_mon.setAdapter(adapter_mon);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    them_mon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intentc=new Intent(getActivity(),ThemMonAn.class);
            intentc.putExtra("id_nhahang",mianActivityBanac.getId_nhahang());
            startActivity(intentc);
        }
    });

searchView_monan.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
      if(radioGroup.getCheckedRadioButtonId()==R.id.radio_tim_ten){
          arrayList_monan(newText);

      }if(radioGroup.getCheckedRadioButtonId()==R.id.radio_tim_gia) {
          arrayList_mon_gia(newText);
      }

        return true;
    }
});


        return  view;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Sua:
                if (mianActivityBanac.getChuvu_nv() == 1) {
                    Intent intent = new Intent(getActivity(), ThemMonAn.class);
                    Bundle bundle = new Bundle();
                  ArrayList<MonAn>arrayList=adapter_mon.getArrayList();

                    MonAn monAn = arrayList.get(adapter_mon.getPositin());
                    bundle.putSerializable("classsua", monAn);
                    intent.putExtras(bundle);
                    intent.putExtra("id_nhahang", mianActivityBanac.getId_nhahang());

                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), "Bạn Không Phải Là Adim Nên Không Thể Thực Hiện Thao Tác!", Toast.LENGTH_LONG).show();


                break;

            case R.id.Xoa:
                if (mianActivityBanac.getChuvu_nv() == 1) // nếu là quản lý mới dx xóa
                {
                    tes1t=0;
                    dk=0;

                   // Toast.makeText(getActivity(), "xoa", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder aler = new AlertDialog.Builder(getContext());
                    aler.setTitle("Thông Báo");
                    aler.setMessage("Bạn có muốn xóa món ăn này khỏi thực đơn");
                    aler.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           String id=arrayList_mon.get(adapter_mon.getPositin()).getId();
MonAn monAn=arrayList_mon.get(adapter_mon.getPositin());
                          databaseReference.child("Ban").addListenerForSingleValueEvent(new ValueEventListener() {
                              @Override
                              public void onDataChange(@NonNull DataSnapshot snapshot) {
                                  tes1t= (int) snapshot.getChildrenCount();

                                  for (DataSnapshot snapshot1:snapshot.getChildren()){

                                          databaseReference.child("Ban").child(snapshot1.getKey()).child("DSMon").child("mon").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                              @Override
                                              public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                  if(snapshot.getValue()!=null){
                                                      Toast.makeText(mianActivityBanac, "Món Đang được gọi không thể xóa!", Toast.LENGTH_SHORT).show();
                                                   dk=1;
                                                  }else {
                                                      tes1t--;
                                                      if(tes1t==0){
                                                          dk=0;
                                          databaseReference.child("ChiTiet_ComBo").addListenerForSingleValueEvent(new ValueEventListener() {
                                              @Override
                                              public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                  tes1t= (int) snapshot.getChildrenCount();
                                                  for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                                      databaseReference.child("ChiTiet_ComBo").child(snapshot1.getKey()).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                                          @Override
                                                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                              if (snapshot.getValue() != null) {
                                                                  Toast.makeText(mianActivityBanac, "Món Ăn Tồn Tại Ở Một Combo Bạn Không Thẻ Xóa!", Toast.LENGTH_SHORT).show();
                                                                   dk=1;

                                                              }else{
                                                                  tes1t--;
                                                                  if(tes1t==0){
                                                                      FirebaseStorage storage = FirebaseStorage.getInstance();
//                                                                      StorageReference storageReference=storage.getReferenceFromUrl("gs://doan-f52ef.appspot.com/");
//
//                                                                      StorageReference mytorage=storageReference.child(mianActivityBanac.getId_nhahang()
//                                                                              +"/imgMonAn/"+arrayList_mon.get(adapter_mon.getPositin()).getId());
//                                                                      mytorage.delete();
                                                                      databaseReference.child("DS_MonAn").child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                          @Override
                                                                          public void onComplete(@NonNull Task<Void> task) {
                                                                              if(task.isSuccessful()){
                                                                                 databaseReference.child("Thuc_Don_Cu").child("mon").child(id).setValue(monAn).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                     @Override
                                                                                     public void onComplete(@NonNull Task<Void> task) {
                                                                                         if(task.isSuccessful()){
                                                                                             adapter_mon.notifyDataSetChanged();
                                                                                         }
                                                                                     }
                                                                                 });

                                                                              }
                                                                          }
                                                                      });
                                                                  }
                                                              }

                                                          }

                                                          @Override
                                                          public void onCancelled(@NonNull DatabaseError error) {

                                                          }
                                                      });

                                                      if(dk==1){
                                                          break;
                                                      }

                                                  }




                                              }
                                              @Override
                                              public void onCancelled(@NonNull DatabaseError error) {

                                              }
                                          });
                                                         // Toast.makeText(mianActivityBanac, "aaaaaaaaaaaaa", Toast.LENGTH_SHORT).show();
                                                      }
                                                  }

                                              }

                                              @Override
                                              public void onCancelled(@NonNull DatabaseError error) {

                                              }
                                          });

                                      if(dk==1){
                                          break;
                                      }

                                  }




                              }

                              @Override
                              public void onCancelled(@NonNull DatabaseError error) {

                              }
                          });

//                        Toast.makeText(getActivity().getApplicationContext(), arrayList.get(adapterrecymenuMA.getPositin()).getId(),Toast.LENGTH_LONG).show();
                        }
                    });
                    aler.show();
                } else
                    Toast.makeText(getActivity(), "Bạn Không Phải Là Adim Nên Không Thể Thực Hiện Thao Tác!", Toast.LENGTH_LONG).show();
                break;

        }
        return super.onContextItemSelected(item);
    }

    private void arrayList_mon_gia(String newText) {
       try {
           int gia=Integer.parseInt(newText);
           ArrayList<MonAn>monAnArrayListnew=new ArrayList<>();
           for (MonAn monAn:arrayList_mon){
               if(Integer.parseInt(monAn.getGia())<gia){
                   monAnArrayListnew.add(monAn);
               }
           }
           if (monAnArrayListnew.isEmpty()){
               Toast.makeText(mianActivityBanac, "No data", Toast.LENGTH_SHORT).show();
           }else adapter_mon.set_arraynew(monAnArrayListnew);
       }catch (NumberFormatException e){

       }

    }

    private void arrayList_monan(String newText) {
        ArrayList<MonAn>monAnArrayListnew=new ArrayList<>();
        for (MonAn monAn:arrayList_mon){
            if(monAn.getTenmon().toLowerCase().contains(newText)){
                monAnArrayListnew.add(monAn);
            }
        }
        if (monAnArrayListnew.isEmpty()){
            Toast.makeText(mianActivityBanac, "No data", Toast.LENGTH_SHORT).show();
        }else adapter_mon.set_arraynew(monAnArrayListnew);
    }
    private void getloai() {
        databaseReference.child("Loai_Menu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList_loai = new ArrayList<>();
                ArrayList<String>arrayList_l=new ArrayList<>();
             class_loai loai=new class_loai("0","All");
             arrayList_l.add("All");
                arrayList_loai.add(loai);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                   class_loai loai1=new class_loai(dataSnapshot.getKey(),dataSnapshot.getValue().toString());
                    arrayList_loai.add(loai1);
                    arrayList_l.add(dataSnapshot.getValue().toString());
                }
                adapter_spienr adapter_spienr = new adapter_spienr(getActivity()
                        , R.layout.itme_spiner, arrayList_l,"Loại");
                timloai_mon.setAdapter(adapter_spienr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}