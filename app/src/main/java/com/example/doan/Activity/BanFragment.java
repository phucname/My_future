package com.example.doan.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.security.keystore.WrappedKeyEntry;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.doan.Adapter.AdapterBan;
import com.example.doan.R;
import com.example.doan.Class.classBan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.Serializable;
import java.util.ArrayList;


public class BanFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
RecyclerView recyclerView;
AdapterBan adapterBan;
ArrayList<classBan>arrayList;
FloatingActionButton them_ban;
    FirebaseUser user;
    DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_ban, container, false);
        MianActivityBanac mianActivityBanac= (MianActivityBanac) getActivity();
        recyclerView=view.findViewById(R.id.recyclerview_ban);
them_ban=view.findViewById(R.id.float_them_ban);
      user= FirebaseAuth.getInstance().getCurrentUser();
     if(mianActivityBanac.getChuvu_nv()!=1){
         them_ban.setVisibility(View.INVISIBLE);
     }
        databaseReference= FirebaseDatabase.getInstance().getReference().child("NhaHang").child(mianActivityBanac.getId_nhahang()).child("Ban");
      databaseReference.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
             arrayList=new ArrayList<>();
              for(DataSnapshot  snapshot1:snapshot.getChildren()){
                try {
                    classBan ban=new classBan(snapshot1.child("masoban").getValue().toString(),
                            snapshot1.child("soghe").getValue().toString(),
                            snapshot1.child("idBan").getValue().toString(),
                            Boolean.valueOf(snapshot1.child("tinhTrang").getValue().toString())
                            ,Integer.parseInt(snapshot1.child("pass").getValue().toString()));
                    arrayList.add(ban);
                } catch (NullPointerException e) {
                    classBan ban=new classBan(snapshot1.child("masoban").getValue().toString(),
                            snapshot1.child("soghe").getValue().toString(),
                            snapshot1.child("idBan").getValue().toString(),
                            Boolean.valueOf(snapshot1.child("tinhTrang").getValue().toString())
                            ,0);
                    arrayList.add(ban);

                }

                  //Toast.makeText(mianActivityBanac,Boolean.valueOf(snapshot1.child("tinhTrang").getValue().toString())+"", Toast.LENGTH_SHORT).show();
              } adapterBan=new AdapterBan(arrayList,R.layout.item_ban,view.getContext());
              GridLayoutManager gridLayoutManager=new GridLayoutManager(view.getContext(), 1);
              recyclerView.setLayoutManager(gridLayoutManager);
              recyclerView.setAdapter(adapterBan);


          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });

them_ban.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       Intent intent=new Intent(getActivity(),ThembanActivity.class);
        intent.putExtra("id_nhahang",mianActivityBanac.getId_nhahang());
        Bundle bundle=new Bundle();
        bundle.putSerializable("class_ban",arrayList);
        intent.putExtras(bundle);
        startActivity(intent);
    }
});
       return  view;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
       MianActivityBanac mianActivityBanac= (MianActivityBanac) getActivity();

        switch (item.getItemId()){
            case R.id.Sua:
                if(mianActivityBanac.getChuvu_nv()==1){
                    if(arrayList.get(adapterBan.getPosition()).isTinhTrang()==true){
                        Toast.makeText(mianActivityBanac, "Bàn Đang trong quá trinhg phục vụ không thể sửa", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent(getActivity(), ThembanActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("classsuaban", arrayList.get(adapterBan.getPosition()));
                        intent.putExtras(bundle);
                        intent.putExtra("id_nhahang", mianActivityBanac.getId_nhahang());

                        startActivity(intent);
                    }
                }else Toast.makeText(getActivity(),"Bạn Không Phải Là Adim Nên Không Thể Thực Hiện Thao Tác!",Toast.LENGTH_LONG).show();


                break;
            case R.id.Xoa:
                if(mianActivityBanac.getChuvu_nv()==1) {
                   // Toast.makeText(getActivity(), "xoa", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder aler = new AlertDialog.Builder(getContext());
                    aler.setTitle("Thông Báo");
                    aler.setMessage("Bạn có muốn xóa Bàn này khỏi Danh Sách Bàn");
                    aler.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseStorage storage = FirebaseStorage.getInstance();
//                        StorageReference storageReference=storage.getReferenceFromUrl("gs://doan-f52ef.appspot.com/");
//
//                        StorageReference mytorage=storageReference.child("DanhSachAnh/"+snapshot.child("tenAnh"));
//                        mytorage.delete();
                            if(arrayList.get(adapterBan.getPosition()).isTinhTrang()==true){
                                Toast.makeText(mianActivityBanac, "Bàn Đang trong quá trinhg phục vụ không thể xóa", Toast.LENGTH_SHORT).show();
                            }else {
                                databaseReference.child(arrayList.get(adapterBan.getPosition()).getIdBan() + "").removeValue();
                            }

                        }
                    });
                    aler.show();
                }else Toast.makeText(getActivity(),"Bạn Không Phải Là Adim Nên Không Thể Thực Hiện Thao Tác!",Toast.LENGTH_LONG).show();

        }
        return super.onContextItemSelected(item);
    }
}