package com.example.doan.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.Adapter.Adapte_lits_NV;
import com.example.doan.Class.NhanVien;
import com.example.doan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


public class Quan_Ly_Tai_Khoan extends Fragment {


    ArrayList<NhanVien>listnhavien;
    FirebaseUser user;
    String key;
    FloatingActionButton them_nhanvien;
    ListView listViewnv;
    Adapte_lits_NV adapte_lits_nv;
    MianActivityBanac mianActivityBanac;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view=  inflater.inflate(R.layout.fragment_quan__ly__tai__khoan, container, false);
        listViewnv=view.findViewById(R.id.listview_nhanvien);
            them_nhanvien=view.findViewById(R.id.float_them_nhanvien);
     mianActivityBanac= (MianActivityBanac) getActivity();
       user= FirebaseAuth.getInstance().getCurrentUser();
      if(user==null){
          them_nhanvien.setVisibility(View.INVISIBLE);
      }
      if(mianActivityBanac.getChuvu_nv()!=1){
          them_nhanvien.setVisibility(View.INVISIBLE);
      }
       loaddanhsachnv();

them_nhanvien.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        androidx.appcompat.app.AlertDialog.Builder alert=new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        alert.setTitle("Thông Báo!:");
        alert.setMessage("Bạn muốn thêm loại nhân viên nào!");
        alert.setPositiveButton("Phục Vụ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent  intent=new Intent(getActivity(),ThemNhanVienActivity.class);
                intent.putExtra("id_nhahang",mianActivityBanac.getId_nhahang());
                intent.putExtra("chuc_vu","0");
                startActivity(intent);
            }
        });
        alert.setNegativeButton("Đầu Bếp", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(getActivity(),ThemNhanVienActivity.class);
                intent.putExtra("id_nhahang",mianActivityBanac.getId_nhahang());
                intent.putExtra("chuc_vu","2");
                startActivity(intent);
            }
        } );
        alert.show();
    }
});
       return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }







            private void loaddanhsachnv(){
                MianActivityBanac mianActivityBanac;
                mianActivityBanac= (MianActivityBanac) getActivity();
                DatabaseReference data=FirebaseDatabase.getInstance().getReference().child("NhaHang"
                ).child(mianActivityBanac.getId_nhahang()).child("DS_NhanVien");
                data.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listnhavien=new ArrayList<>();

                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                              NhanVien nhanVien=new NhanVien(snapshot1.child("tenNV").getValue().toString(),
                                      snapshot1.child("sdtnv").getValue().toString(),
                                      snapshot1.child("id").getValue().toString(),
                                      snapshot1.child("anhnv").getValue().toString(),
                                     Integer.parseInt(snapshot1.child("chucVuNV").getValue().toString()) ,
                                      snapshot1.child("mail").getValue().toString()
                                      );
                              listnhavien.add(nhanVien);
                        }
                        adapte_lits_nv=new Adapte_lits_NV(getContext(),listnhavien,R.layout.itemnhanvien);
                        listViewnv.setAdapter(adapte_lits_nv);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        MianActivityBanac mianActivityBanac= (MianActivityBanac) getActivity();

        switch (item.getItemId()){
            case R.id.Sua:
                if(mianActivityBanac.getChuvu_nv()==1){
                    Intent intent=new Intent(getActivity(),ThemNhanVienActivity.class);
                    Bundle bundle=new Bundle();
                    NhanVien nhanVien=listnhavien.get(adapte_lits_nv.getPosition());
                    bundle.putSerializable("classsuanhanvien",nhanVien );
                    intent.putExtras(bundle);
                    intent.putExtra("id_nhahang",mianActivityBanac.getId_nhahang());

                    startActivity(intent);
                }else Toast.makeText(getActivity(),"Bạn Không Phải Là Adim Nên Không Thể Thực Hiện Thao Tác!",Toast.LENGTH_LONG).show();


                break;
            case R.id.Xoa:
                if(mianActivityBanac.getChuvu_nv()==1) {
                   // Toast.makeText(getActivity(), "xoa", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder aler = new AlertDialog.Builder(getContext());
                    aler.setTitle("Thông Báo");
                    aler.setMessage("Bạn có muốn xóa tài khoản nhân viên này khỏi nhà hàng!");
                    aler.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("NhaHang").child(mianActivityBanac.getId_nhahang()).child("DanhSachNV");
                            if (listnhavien.get(adapte_lits_nv.getPosition()).getChucVuNV() != 1) {
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                StorageReference storageReference=storage.getReferenceFromUrl("gs://doan-f52ef.appspot.com/");

                                StorageReference mytorage=storageReference.child(mianActivityBanac.getId_nhahang()+
                                        "imgNhanVien/"+user.getUid());
                                mytorage.delete();
                                data.child(listnhavien.get(adapte_lits_nv.getPosition()).getId()).removeValue();
                            } else
                                Toast.makeText(getActivity(), "Quản lý không được xóa chỉ được sửa", Toast.LENGTH_SHORT).show();

                        }
                    });
                    aler.show();
                }else Toast.makeText(getActivity(),"Bạn Không Phải Là Adim Nên Không Thể Thực Hiện Thao Tác!",Toast.LENGTH_LONG).show();

        }
        return super.onContextItemSelected(item);
    }
}