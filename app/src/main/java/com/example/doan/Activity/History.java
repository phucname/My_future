package com.example.doan.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.example.doan.Adapter.Adapter_history;
import com.example.doan.Class.NhanVien;
import com.example.doan.Class.classBan;
import com.example.doan.Class.classhistory;
import com.example.doan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;


public class History extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

ListView listView_history;
ArrayList<classhistory> list_histoy;
ArrayList<classBan>banArrayList;
ArrayList<NhanVien>nhanVienArrayList;
Adapter_history adapter_history;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_history, container,false);
              listView_history=view.findViewById(R.id.listview_history);
        MianActivityBanac mianActivityBanac= (MianActivityBanac) getActivity();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference()
                .child("NhaHang").child(mianActivityBanac.getId_nhahang());;
       DatabaseReference databaseReference1= databaseReference.child("LichSu");
        Query query=databaseReference1.orderByChild("date");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_histoy=new ArrayList<>();
                banArrayList=new ArrayList<>();
                nhanVienArrayList=new ArrayList<>();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                   databaseReference.child("DS_NhanVien")
                           .child(dataSnapshot.child("ten_nv").getValue().toString())
                           .addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           NhanVien nhanVien=new NhanVien(snapshot.child("tenNV").getValue().toString(),
                                   snapshot.child("sdtnv").getValue().toString(),
                                   snapshot.child("id").getValue().toString(),
                                   snapshot.child("anhnv").getValue().toString(),
                                   Integer.parseInt(snapshot.child("chucVuNV").getValue().toString()) ,
                                   snapshot.child("mail").getValue().toString()
                           );
                        nhanVienArrayList.add(nhanVien);
                          // Toast.makeText(getActivity(),snapshot+ "nhanvien", Toast.LENGTH_SHORT).show();
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });
                   databaseReference.child("Ban").child(dataSnapshot.child("ma_ban")
                           .getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if(snapshot.exists()){
                           classBan ban=new classBan(snapshot.child("masoban").getValue().toString(),
                                   snapshot.child("soghe").getValue().toString(),
                                   snapshot.child("idBan").getValue().toString(),
                                   Boolean.valueOf(snapshot.child("tinhTrang").getValue().toString())
                           );
                           banArrayList.add(ban); list_histoy.add(new classhistory(dataSnapshot.child("id").getValue().toString()
                                   ,dataSnapshot.child("tong_bill").getValue().toString()
                                   ,dataSnapshot.child("date").getValue().toString()));
                           //  Toast.makeText(getActivity(), snapshot+"ban", Toast.LENGTH_SHORT). show();
                           adapter_history=new Adapter_history(view.getContext(),list_histoy,R.layout.item_history,banArrayList,nhanVienArrayList);
                           listView_history.setAdapter(adapter_history);
                       }

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
               return view;
    }
}