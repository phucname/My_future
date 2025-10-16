package com.example.doan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.doan.Adapter.AdapterNhaHang;
import com.example.doan.Adapter.adapter_spienr;
import com.example.doan.Class.NhaHang;
import com.example.doan.Class.class_TTVN;
import com.example.doan.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TrangChuActivuty extends AppCompatActivity {

     ArrayList<NhaHang>ListNhaHang;
     RecyclerView listView;
     AdapterNhaHang adapterNhaHang;
     FloatingActionButton bt_taonhahang;
     FirebaseUser user;
     Intent intenttt;

    DatabaseReference databaseReference;
    Spinner spinner ,spinner_huyen,spinner_xa;
    String tinh;
    String huyen,xa;
    ArrayList<class_TTVN>list_tp,list_huyen,list_xa;
    ArrayList<String>tentinh,tenhuyen,tenxa;
    adapter_spienr adapter_spienr_tp,adapter_spienr_huyen,adapter_spienr_xa;
    DatabaseReference getData;
    Button timkiem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu_activuty);
        Anhxa();
        getData=FirebaseDatabase.getInstance().getReference().child("TT_VN");
        timkiem=findViewById(R.id.button_timnhahang);

        //kiem tra phone đã từng đăng nhập chưa rồi:chuyển đến giao diện nhà hàng
       databaseReference = FirebaseDatabase.getInstance().getReference().child("NhaHang");
       Kiemtra_Dangnhap();
       Load_danhsach_nhahang();
       //dọc file máy xem người dùng có đăng nhập là nhân viên,hay quản lý của nhà hàng nào chưa
        // kiểm tra tài khaonr nhân viên nếu đã đăng nhập
        bt_taonhahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert=new AlertDialog.Builder(TrangChuActivuty.this);
                alert.setTitle("Thông Báo!:");
                alert.setMessage("Bạn cần một tài khoản quản lý nhà hàng!");
                alert.setPositiveButton("Đăng Nhập", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(getApplicationContext(), DangNhapActivity.class);
                        intent.putExtra("DNTaoNH","tao");
                        startActivity(intent);
                    }
                });
                alert.setNegativeButton("Đăng Ký", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(getApplicationContext(), DangKyActivity.class);
                        intent.putExtra("ChucVu","1");
                        startActivity(intent);
                    }
                } );
                alert.show();

            }
        });
        spinner=findViewById(R.id.spinner);
        spinner_huyen=findViewById(R.id.spinner_huyen);
        spinner_xa=findViewById(R.id.spinner_xa);
        getData.child("TP").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_tp=new ArrayList<>();

                tentinh=new ArrayList<>();

                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    list_tp.add(new class_TTVN(snapshot1.child("id").getValue().toString()
                            ,snapshot1.child("ten").getValue().toString()));
                    tentinh.add(snapshot1.child("ten").getValue().toString());

                }
                adapter_spienr_tp=new adapter_spienr(TrangChuActivuty.this,R.layout.selct_spiner,tentinh,"TP,Tỉnh");
                spinner.setAdapter(adapter_spienr_tp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // tinh= tentinh.get(position);
                tinh=String.valueOf(position+1);
               // Toast.makeText(TrangChuActivuty.this, tinh, Toast.LENGTH_SHORT).show();
                getData.child("Huyen").child( list_tp.get(position).getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        list_huyen=new ArrayList<>();
                        list_huyen.add(new class_TTVN("All","All"));
                        tenhuyen=new ArrayList<>();
                        tenhuyen.add("All");
                        for (DataSnapshot snapshot1:snapshot.getChildren()){
                            list_huyen.add(new class_TTVN(snapshot1.child("id").getValue().toString()
                                    ,snapshot1.child("ten").getValue().toString()));
                            tenhuyen.add(snapshot1.child("ten").getValue().toString());
                        }
                        adapter_spienr_huyen=new adapter_spienr(TrangChuActivuty.this,R.layout.selct_spiner,tenhuyen,"Quận,Huyện");
                        spinner_huyen.setAdapter(adapter_spienr_huyen);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_huyen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // huyen= list_huyen.get(position).getTen();
               huyen=String.valueOf(position);
               // Toast.makeText(TrangChuActivuty.this, huyen+"", Toast.LENGTH_SHORT).show();
                getData.child("Xa").child( list_tp.get(position).getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        list_xa=new ArrayList<>();
                        list_xa.add(new class_TTVN("All","All"));
                        tenxa=new ArrayList<>();
                        tenxa.add("All");
                        for (DataSnapshot snapshot1:snapshot.getChildren()){
                            list_xa.add(new class_TTVN(snapshot1.child("id").getValue().toString()
                                    ,snapshot1.child("ten").getValue().toString()));
                            tenxa.add(snapshot1.child("ten").getValue().toString());
                        }
                        adapter_spienr_xa=new adapter_spienr(TrangChuActivuty.this,R.layout.selct_spiner,tenxa,"Phường,Xã");
                        spinner_xa.setAdapter(adapter_spienr_xa);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_xa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //xa=list_xa.get(position).getTen();
                xa=String.valueOf(position);
                //Toast.makeText(TrangChuActivuty.this, xa+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        timkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ArrayList<NhaHang>nhaHangArrayList_new=new ArrayList<>();

                for(NhaHang nhaHang:ListNhaHang){
                    String[]diachi=nhaHang.getDiachi_nhahang().split(",");
                   // Toast.makeText(TrangChuActivuty.this, nhaHang.getDiachi_nhahang()+"//"+tinh, Toast.LENGTH_SHORT).show();

                     // diachi[2]=diachi[2].replaceAll(" ","");

               //     if(nhaHang.getDiachi_nhahang().toLowerCase().contains(tinh.toLowerCase())){

                    if(nhaHang.getTinh().equals(tinh)){
                      //  Toast.makeText(TrangChuActivuty.this, "aaaaaaaaaaaa", Toast.LENGTH_SHORT).show();
                       if(huyen.equals("0")){
                          // Toast.makeText(TrangChuActivuty.this, "00", Toast.LENGTH_SHORT).show();
                               nhaHangArrayList_new.add(nhaHang);
                               adapterNhaHang = new AdapterNhaHang(TrangChuActivuty.this, nhaHangArrayList_new, R.layout.itemnhahang);
                               LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                               listView.setLayoutManager(linearLayoutManager);
                               listView.setAdapter(adapterNhaHang);

                       }
                       //else  if(("Huyen"+diachi[1]).contains(huyen)){
                        else  if(nhaHang.getHuyen().equals(huyen)){
                           if(xa.equals("0")) {
                               nhaHangArrayList_new.add(nhaHang);
                               adapterNhaHang = new AdapterNhaHang(TrangChuActivuty.this, nhaHangArrayList_new, R.layout.itemnhahang);
                               LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                               listView.setLayoutManager(linearLayoutManager);
                               listView.setAdapter(adapterNhaHang);
                           }
                            // else if (("xa" + diachi[0]).contains(xa)) {
                           else if(xa.equals(nhaHang.getXa())){
                                   nhaHangArrayList_new.add(nhaHang);
                                   adapterNhaHang = new AdapterNhaHang(TrangChuActivuty.this, nhaHangArrayList_new, R.layout.itemnhahang);
                                   LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                   listView.setLayoutManager(linearLayoutManager);
                                   listView.setAdapter(adapterNhaHang);
                               }
                           }

                       }


                }
                if(nhaHangArrayList_new.size()==0){
                    Toast.makeText(TrangChuActivuty.this, "No Data", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(TrangChuActivuty.this,tinh+"/"+huyen+"/"+xa, Toast.LENGTH_SHORT).show();
            }
        });
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//               listseach(newText);
//                return true;
//            }
//        });


}

    private void listseach(String newText) {
        ArrayList<NhaHang>nhaHangArrayList=new ArrayList<>();
        for (NhaHang nhaHang:ListNhaHang){
            if(nhaHang.getTen_nhahang().toLowerCase().contains(newText)){
                nhaHangArrayList.add(nhaHang);
            }
        }
        if(nhaHangArrayList.isEmpty()){
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }else adapterNhaHang.set_array(nhaHangArrayList);
    }

    private  void Load_danhsach_nhahang(){
    databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            ListNhaHang = new ArrayList<>();
            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                if(snapshot1.child("TT_NhaHang").getValue()!=null){
                    NhaHang nhaHang = new NhaHang(
                            snapshot1.child("TT_NhaHang").child("ten_nhahang").getValue().toString()
                            ,snapshot1.child("TT_NhaHang").child("diachi_nhahang").getValue().toString()
                            , snapshot1.getKey(),snapshot1.child("TT_NhaHang").child("mg_nhahang").getValue().toString()
                    ,snapshot1.child("TT_NhaHang").child("huyen").getValue().toString(),
                            snapshot1.child("TT_NhaHang").child("tinh").getValue().toString(),
                            snapshot1.child("TT_NhaHang").child("xa").getValue().toString());
                    ListNhaHang.add(nhaHang);
                }

                // Toast.makeText(TrangChuActivuty.this,snapshot1.child("TenNhaHang").getValue()+"",Toast.LENGTH_LONG).show();
            }
            adapterNhaHang = new AdapterNhaHang(TrangChuActivuty.this, ListNhaHang, R.layout.itemnhahang);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            listView.setLayoutManager(linearLayoutManager);
            listView.setAdapter(adapterNhaHang);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            return ;
        }

    });
}

private void Kiemtra_Dangnhap(){
    user=FirebaseAuth.getInstance().getCurrentUser();

    intenttt=new Intent(TrangChuActivuty.this,MianActivityBanac.class);
    //kiểu tra xem là có đăng nhập trên firebaseauth chưa;
    if(user!=null){
        ArrayList<String>arrayList=new ArrayList<>();
        //nếu có mở file lưu thông tin nhà hàng ở trong máy
        try {
            ContextWrapper contextWrapper = new ContextWrapper(
                    getApplicationContext());
            //Tạo (Hoặc là mở file nếu nó đã tồn tại) Trong bộ nhớ trong có thư mục là ThuMucCuaToi.
            File directory = contextWrapper.getDir("TK", Context.MODE_PRIVATE);
            File myInternalFile = new File(directory, "TinhTrang.txt");//Đọc file
            if(myInternalFile.exists()==true) {
                FileInputStream fis = new FileInputStream(myInternalFile);
                DataInputStream in = new DataInputStream(fis);
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(in));

                String strLine;
                //Đọc từng dòng
                while ((strLine = br.readLine()) != null) {

                    String[] a = strLine.split(",");
                    arrayList.add(a[0]);
                    arrayList.add(a[1]);
                    databaseReference.child(a[1])
                            .child("TT_NhaHang").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.getValue()!=null){

                                    }else {
                                        FirebaseAuth auth=FirebaseAuth.getInstance();
                                        auth.signOut();

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }
                in.close();
            }else {
                FirebaseAuth auth=FirebaseAuth.getInstance();
                auth.signOut();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        // nếu có chueyer dến nhà hàng đó và cấp quyền nhân viên

        if(arrayList.size()>0) {

            if (arrayList.get(0).equals("1")) {
                intenttt.putExtra("chucvu", "1");
                intenttt.putExtra("id_nhahang", arrayList.get(1));
                startActivity(intenttt);
            } if (arrayList.get(0).equals("0"))   {
                intenttt.putExtra("id_nhahang", arrayList.get(1));
                intenttt.putExtra("chucvu", "0");
                startActivity(intenttt);
            }
            if (arrayList.get(0).equals("2")) {
                intenttt.putExtra("id_nhahang", arrayList.get(1));
                intenttt.putExtra("chucvu", "2");
                startActivity(intenttt);
            }
        }


    }
}
private void Anhxa(){
    listView=findViewById(R.id.listtrangchu);

    bt_taonhahang=findViewById(R.id.button_taonhahang);
}


}