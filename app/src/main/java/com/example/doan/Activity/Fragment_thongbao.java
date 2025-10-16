package com.example.doan.Activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.doan.Adapter.adapter_thongbao;
import com.example.doan.Class.class_thongbao;
import com.example.doan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Fragment_thongbao extends Fragment {

ListView listView_thongbao;
ArrayList<class_thongbao>arraytring;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view= inflater.inflate(R.layout.fragment_thongbao, container, false);
      listView_thongbao=view.findViewById(R.id.listview_thongbao);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        MianActivityBanac mianActivityBanac= (MianActivityBanac) getActivity();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference()
                .child("NhaHang").child(mianActivityBanac.getId_nhahang())
                .child("DS_NhanVien").child(user.getUid()).child("Thong_Bao");

          databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  arraytring = new ArrayList<>();

                      for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                         class_thongbao classThongbao=new class_thongbao(snapshot1.getKey()
                         ,snapshot1.child("noidung").getValue().toString()
                         ,snapshot1.child("date").getValue().toString());

                          arraytring.add(classThongbao);
                      }
                     adapter_thongbao adapter = new adapter_thongbao(arraytring,view.getContext(),R.layout.item_thongbao );
                      listView_thongbao.setAdapter(adapter);

              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });


        return view;
    }
}