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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.doan.Adapter.Adapter_combo;
import com.example.doan.Adapter.AdapterrecymenuMA;
import com.example.doan.Class.MonAn;
import com.example.doan.Class.class_combo;
import com.example.doan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class Fragment_ds_combo extends Fragment {

    RecyclerView recycler_combo;
    public ArrayList<class_combo> arrayList_combo;
    Adapter_combo adapter_combo;
    DatabaseReference databaseReference;
    MianActivityBanac mianActivityBanac;
    FloatingActionButton them_combo;
    SearchView searchView_combo;
    int dk = 0,dk_tontai=0;
    RadioGroup radioGroup;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_ds_combo, container, false);
        recycler_combo=view.findViewById(R.id.recyclerview_ds_combo);
        mianActivityBanac= (MianActivityBanac) getActivity();
        radioGroup=view.findViewById(R.id.radio_group_combo);
        searchView_combo=view.findViewById(R.id.search_ds_combo);
        them_combo=view.findViewById(R.id.float_them_combo);
        if(mianActivityBanac.getChuvu_nv()!=1){
            them_combo.setVisibility(View.INVISIBLE);
        }
        databaseReference= FirebaseDatabase.getInstance().getReference()
                .child("NhaHang").child(mianActivityBanac.getId_nhahang());
        databaseReference.child("DS_ComBo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList_combo = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    class_combo combo = new class_combo(dataSnapshot.child("id_combo").getValue().toString()
                            , dataSnapshot.child("gia_combo").getValue().toString()
                            , dataSnapshot.child("ten_combo").getValue().toString(),
                            dataSnapshot.child("img_combo").getValue().toString(),
                            Integer.parseInt(dataSnapshot.child("loai_giamgia").getValue().toString()));
                    arrayList_combo.add(combo);

                }  adapter_combo = new Adapter_combo(arrayList_combo, R.layout.carrecymenu, getContext(),mianActivityBanac.getId_nhahang(),0);
                //LinearLayoutManager gridLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
                GridLayoutManager gridLayoutManager=new GridLayoutManager(view.getContext(),2);
                recycler_combo.setLayoutManager(gridLayoutManager);
                recycler_combo.setAdapter(adapter_combo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
them_combo.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getActivity(),Them_combo.class);
        intent.putExtra("id_nhahang",mianActivityBanac.getId_nhahang());
        startActivity(intent);
    }
});
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radio_tim_gia_combo){
                    searchView_combo.setInputType(3);//kiểu dữ liệu nhập
                    // Toast.makeText(mianActivityBanac, "timg giá", Toast.LENGTH_SHORT).show();
                }if(checkedId==R.id.radio_tim_ten_combo){

                    searchView_combo.setInputType(1);
                }

            }
        });
        searchView_combo.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(radioGroup.getCheckedRadioButtonId()==R.id.radio_tim_ten_combo){
                    arrayList_monan(newText);

                }if(radioGroup.getCheckedRadioButtonId()==R.id.radio_tim_gia_combo) {
                    arrayList_combo_gia(newText);
                }

                return true;
            }
        });



        return  view;
    }



    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.sua_combo:
                Intent intent=new Intent(getActivity(),Them_combo.class);
                 Bundle bundle=new Bundle();
                 bundle.putSerializable("class_combo",arrayList_combo.get(adapter_combo.getI()));
                 intent.putExtras(bundle);
                 intent.putExtra("id_nhahang",mianActivityBanac.getId_nhahang());
                 startActivity(intent);
               // Toast.makeText(mianActivityBanac, arrayList_combo.get(adapter_combo.getI()).getId_combo(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.xoa_combo:
                if(mianActivityBanac.getChuvu_nv()==1){
                    dk=0;
                    dk_tontai=0;// Toast.makeText(getActivity(),"xoa",Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder aler=new AlertDialog.Builder(getContext());
                    aler.setTitle("Thông Báo");
                    aler.setMessage("Bạn có muốn xóa combo này khỏi thực đơn!");
                    aler.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           databaseReference.child("Ban").addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                       dk= (int) snapshot.getChildrenCount();
                                   for (DataSnapshot snap:snapshot.getChildren()
                                        ) {
                                       Query query= databaseReference.child("Ban").child(snap.getKey())
                                               .child("DSMon").child("combo").orderByChild("id_mon")
                                               .equalTo(arrayList_combo.get(adapter_combo.getI()).getId_combo());
                                       query.addListenerForSingleValueEvent(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                                               if(snapshot.getValue()==null){
                                                   dk--;
                                                   if(dk==0){

//                                                          FirebaseStorage storage=FirebaseStorage.getInstance();
//                                                          StorageReference storageReference=storage.getReferenceFromUrl("gs://doan-f52ef.appspot.com/");
//                                                          StorageReference mytorage= storageReference.child(mianActivityBanac.getId_nhahang()+"/imgComBo/"
//                                                                  +arrayList_combo.get(adapter_combo.getI()).getId_combo());
//                                                          mytorage.delete();
                                                       Toast.makeText(getActivity(), "xóa", Toast.LENGTH_SHORT).show();

                                                       class_combo combo=arrayList_combo.get(adapter_combo.getI());
                                                       MonAn monAn=new MonAn(combo.getTen_combo(),combo.getGia_combo(),"",combo.getImg_combo()
                                                       ,combo.getId_combo(),"",combo.getLoai_giamgia());
                                                       databaseReference.child("Thuc_Don_Cu").child("combo")
                                                               .child(combo.getId_combo()).setValue(monAn).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                   @Override
                                                                   public void onComplete(@NonNull Task<Void> task) {
                                                                       if(task.isSuccessful()){
                                                                           databaseReference.child("DS_ComBo")
                                                                                   .child(combo.getId_combo()).removeValue();
                                                                           databaseReference.child("ChiTiet_ComBo")
                                                                                   .child(combo.getId_combo()).removeValue();
                                                                       }
                                                                   }
                                                               });

                                                   }
                                               }else {
                                                   dk_tontai=1;

                                               }
                                           }

                                           @Override
                                           public void onCancelled(@NonNull DatabaseError error) {

                                           }
                                       });
//                                       databaseReference.child("Ban").child(snap.getKey())
//                                               .child("DSMon").child("combo")
//                                               .addListenerForSingleValueEvent(new ValueEventListener() {
//                                           @Override
//                                           public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                               if(snapshot.getValue()!=null){
//                                                   for (DataSnapshot snapshot1:snapshot.getChildren()){
//                                                       if(snapshot1.child("id_mon").getValue().toString()
//                                                               .equals(arrayList_combo.get(adapter_combo.getI()).getId_combo())){
//                                                           dk_tontai=1;
//                                                           Toast.makeText(getActivity(), "ComBo Đang được phục vụ không thể xóa!", Toast.LENGTH_SHORT).show();
//
//
//                                                       }else {
//                                                           dk--;
//                                                           if(dk==0){
//
////                                                          FirebaseStorage storage=FirebaseStorage.getInstance();
////                                                          StorageReference storageReference=storage.getReferenceFromUrl("gs://doan-f52ef.appspot.com/");
////                                                          StorageReference mytorage= storageReference.child(mianActivityBanac.getId_nhahang()+"/imgComBo/"
////                                                                  +arrayList_combo.get(adapter_combo.getI()).getId_combo());
////                                                          mytorage.delete();
//                                                               Toast.makeText(getActivity(), "xóa", Toast.LENGTH_SHORT).show();
//                                                               class_combo combo=arrayList_combo.get(adapter_combo.getI());
//                                                               databaseReference.child("Thuc_Don_Cu").child("combo")
//                                                                       .child(combo.getId_combo()).setValue(combo).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                           @Override
//                                                                           public void onComplete(@NonNull Task<Void> task) {
//                                                                               if(task.isSuccessful()){
//                                                                                   databaseReference.child("DS_ComBo")
//                                                                                           .child(combo.getId_combo()).removeValue();
//                                                                                   databaseReference.child("ChiTiet_ComBo")
//                                                                                           .child(combo.getId_combo()).removeValue();
//                                                                               }
//                                                                           }
//                                                                       });
//
//                                                           }
//                                                       }
//                                                       if(dk_tontai==1){
//                                                           break;
//                                                       }
//                                                   }
//                                               }else{
//                                                   dk--;
//                                                   if(dk==0){
//
////                                                          FirebaseStorage storage=FirebaseStorage.getInstance();
////                                                          StorageReference storageReference=storage.getReferenceFromUrl("gs://doan-f52ef.appspot.com/");
////                                                          StorageReference mytorage= storageReference.child(mianActivityBanac.getId_nhahang()+"/imgComBo/"
////                                                                  +arrayList_combo.get(adapter_combo.getI()).getId_combo());
////                                                          mytorage.delete();
//                                                       Toast.makeText(getActivity(), "xóa", Toast.LENGTH_SHORT).show();
//                                                       class_combo combo=arrayList_combo.get(adapter_combo.getI());
//                                                       databaseReference.child("Thuc_Don_Cu").child("combo")
//                                                               .child(combo.getId_combo()).setValue(combo).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                   @Override
//                                                                   public void onComplete(@NonNull Task<Void> task) {
//                                                                       if(task.isSuccessful()){
//                                                                           databaseReference.child("DS_ComBo")
//                                                                                   .child(combo.getId_combo()).removeValue();
//                                                                           databaseReference.child("ChiTiet_ComBo")
//                                                                                   .child(combo.getId_combo()).removeValue();
//                                                                       }
//                                                                   }
//                                                               });
//
//                                                   }
//                                               }
//
//
//                                           }
//
//                                           @Override
//                                           public void onCancelled(@NonNull DatabaseError error) {
//
//                                           }
//                                       });                                      i
                                       if(dk_tontai==1){
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
                }else
                    Toast.makeText(getActivity(),"Bạn Không Phải Là Adim Nên Không Thể Thực Hiện Thao Tác!",Toast.LENGTH_LONG).show();
        }
        return super.onContextItemSelected(item);
    }
    private void arrayList_combo_gia(String newText) {
        try {
            int gia=Integer.parseInt(newText);
            ArrayList<class_combo>comboArrayListnew=new ArrayList<>();
            for (class_combo combo:arrayList_combo){
                if(Integer.parseInt(combo.getGia_combo())<gia){
                   comboArrayListnew.add(combo);
                }
            }
            if (comboArrayListnew.isEmpty()){
                Toast.makeText(mianActivityBanac, "No data", Toast.LENGTH_SHORT).show();
            }else adapter_combo.set_arraynew(comboArrayListnew);
        }catch (NumberFormatException e){

        }

    }

    private void arrayList_monan(String newText) {
        ArrayList<class_combo>comboArrayListnew=new ArrayList<>();
        for (class_combo combo:arrayList_combo){
            if(combo.getTen_combo().toLowerCase().contains(newText)){
                comboArrayListnew.add(combo);
            }
        }
        if (comboArrayListnew.isEmpty()){
            Toast.makeText(mianActivityBanac, "No data", Toast.LENGTH_SHORT).show();
        }else adapter_combo.set_arraynew(comboArrayListnew);
    }

}