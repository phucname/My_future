package com.example.doan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.Adapter.Adapter_Quangcao;
import com.example.doan.Adapter.adapter_chitietmon_combo;
import com.example.doan.Class.MonAn;
import com.example.doan.Class.classBan;
import com.example.doan.Class.classHoaDon;
import com.example.doan.Class.class_combo;
import com.example.doan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

import me.relex.circleindicator.CircleIndicator3;

public class Activity_chitiet_combo extends AppCompatActivity {
    ViewPager2 viewPager2;
    CircleIndicator3 circleIndicator3;
    ArrayList<MonAn>monAnArrayList;
    ArrayList<String>list;
    RecyclerView recyclerView_chitietmon;
    TextView giacombo,tencombo,sl;
    Button themcombo,dathem;
    FrameLayout frame_tangsl,frame_trusl;
    class_combo combo;
    ArrayList<String>masoban;
    DatabaseReference databaseReference;
    ArrayList<classBan>arrayList;
    Intent intent;
    private Handler handler=new Handler(

    );
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            if(viewPager2.getCurrentItem()==list.size()-1){
                viewPager2.setCurrentItem(0);
            }else  viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_combo);
        viewPager2=findViewById(R.id.viewpaper_combo);
        circleIndicator3=findViewById(R.id.circle_center_combo);
        recyclerView_chitietmon=findViewById(R.id.recyclerview_chitiet_combo);
        giacombo=findViewById(R.id.text_giacombo);
        tencombo=findViewById(R.id.text_tencombo);
        themcombo=findViewById(R.id.button_themcombo);
        dathem=findViewById(R.id.button_dathem_combo);
        frame_tangsl=findViewById(R.id.framelayout_cong_combo);
      sl=findViewById(R.id.goicombosoluong);
        frame_trusl=findViewById(R.id.framelayout_tru_combo);
       intent=getIntent();
        Bundle bundle=getIntent().getExtras();
      combo = (class_combo) bundle.getSerializable("class_combo");
        tencombo.setText(combo.getTen_combo());
        GoiMonActivity goiMonActivity=new GoiMonActivity();
        giacombo.setText(goiMonActivity.VDN(combo.getGia_combo()));
        kiemtra();
       databaseReference= FirebaseDatabase.getInstance().getReference()
                .child("NhaHang").child(intent.getStringExtra("id_nhahang"));
        databaseReference.child("ChiTiet_ComBo")
                .child(combo.getId_combo())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                monAnArrayList=new ArrayList<>();
                list=new ArrayList<>();
                list.add(combo.getImg_combo());
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        databaseReference.child("DS_MonAn").child(snapshot1.child("id_mon").getValue().toString())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                monAnArrayList.add(new MonAn(snapshot.child("tenmon").getValue().toString(),
                                        snapshot.child("gia").getValue().toString(),
                                        snapshot.child("ghichu").getValue().toString(),
                                        snapshot.child("imgmon").getValue().toString(),
                                        snapshot.child("id").getValue().toString()
                                ,snapshot1.child("soluong").getValue().toString(),
                                0));
                                list.add( snapshot.child("imgmon").getValue().toString());
                                Adapter_Quangcao adapter_quangcao=new Adapter_Quangcao(list);
                                viewPager2.setAdapter(adapter_quangcao);
                                circleIndicator3.setViewPager(viewPager2);
                                viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                                    @Override
                                    public void onPageSelected(int position) {
                                        super.onPageSelected(position);
                                        handler.removeCallbacks(runnable);
                                        handler.postDelayed(runnable,3000);
                                        adapter_chitietmon_combo adapter_chitietmon_combo=new adapter_chitietmon_combo(monAnArrayList,R.layout.item_chitiet_combo,Activity_chitiet_combo.this);
                                        GridLayoutManager gridLayoutManager=new GridLayoutManager(Activity_chitiet_combo.this,2);
                                        recyclerView_chitietmon.setLayoutManager(gridLayoutManager);
                                        recyclerView_chitietmon.setAdapter(adapter_chitietmon_combo);    }
                                });
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
        themcombo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Showdialog(intent.getStringExtra("id_nhahang"));
                }else Toast.makeText(Activity_chitiet_combo.this, "Bạn Phải Là Nhân Viên Nhà " +
                        "Hàng Mới Được Gọi Món! Mời Bạn " +
                        "Đăng Nhập Tài Khảo.", Toast.LENGTH_SHORT).show();


            }
        });
        frame_trusl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(sl.getText().toString()) > 1) {
                    sl.setText((Integer.parseInt(sl.getText().toString()) - 1) + "");
                    //  String gia1=gia.getText().toString();
                    ///  gia1=gia1.replaceAll("đ","");
                    //  String gia2=gia1.replaceAll(".","");
                    //Toast.makeText(getApplicationContext(),intent.getStringExtra("keyNH"),Toast.LENGTH_LONG).show();

                }

            }
        });
        frame_tangsl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sl.setText((Integer.parseInt(sl.getText().toString()) + 1) + "");
            }
        });
    }
    private  void kiemtra(){
        File myInternalFile;
        ContextWrapper contextWrapper = new ContextWrapper(Activity_chitiet_combo.this);
        //Tạo (Hoặc là mở file nếu nó đã tồn tại) Trong bộ nhớ trong có thư mục là ThuMucCuaToi.
        File directory = contextWrapper.getDir("TK", Context.MODE_APPEND);//tên Thư Mục
        myInternalFile = new File(directory, "combo.txt");
        int i=0;
        if(myInternalFile.exists()==true) {
            try {
                FileInputStream inputStream = new FileInputStream(myInternalFile);
                DataInputStream in = new DataInputStream(inputStream);
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(in));

                String strLine;
                while ((strLine = br.readLine()) != null) {
                    String[] a = strLine.split(",");
                    if(a[0].equals(combo.getId_combo())){
                      //Toast.makeText(contextWrapper, "Món Ăn Đã Có Ở Giỏ Hàng", Toast.LENGTH_SHORT).show();
                        dathem.setVisibility(View.VISIBLE);
                        break;
                    }

                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    private void Showdialog(String key) {

        Dialog dialog=new Dialog(Activity_chitiet_combo.this);
        dialog.setContentView(R.layout.dialogban);
        ListView listView=dialog.findViewById(R.id.list_item_dialog);
        DatabaseReference  data= FirebaseDatabase.getInstance().getReference().child("NhaHang").child(key).child("Ban");
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList=new ArrayList<>();
                masoban=new ArrayList<>();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    if(Boolean.parseBoolean(snapshot1.child("tinhTrang").getValue().toString()) ==true){
                        classBan ban=new classBan(snapshot1.child("masoban").getValue().toString(),
                                snapshot1.child("soghe").getValue().toString(),
                                snapshot1.child("idBan").getValue().toString(),
                                Boolean.valueOf(snapshot1.child("tinhTrang").getValue().toString()),
                                0);

                        arrayList.add(ban);//chứa khóacủa bàn
                        masoban.add("Bàn Số:"+snapshot1.child("masoban").getValue().toString());
                    }

                }


                ArrayAdapter adapter=new ArrayAdapter(Activity_chitiet_combo.this, android.R.layout.simple_list_item_1,masoban);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseReference data=  databaseReference.child("Ban").child(arrayList.get(i).getIdBan()).child("DSMon")
                        .child("combo");
                Query query=data.orderByChild("id_mon").equalTo(combo.getId_combo());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() == null) {
                            Calendar calendar = Calendar.getInstance();
                            classHoaDon classHoaDon = new classHoaDon("", arrayList.get(i).getIdBan() + ""
                                    , calendar.get(Calendar.MINUTE) + "/"
                                    + calendar.get(Calendar.HOUR) + "/" + calendar.get(Calendar.DATE)
                                    ,sl.getText().toString() , combo.getId_combo(), "Chưa Nhận",""
                            );

                            data.push().setValue(classHoaDon);

                        }
                        else {
                            int kt=0;
                            String key1=snapshot.getValue().toString(),keyphu="",sl1="";

                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                if (snapshot1.child("tinhtrang").getValue().toString().equals("Chưa Nhận")) {
                                    kt = 1;
                                    sl1 = snapshot1.child("soluong").getValue().toString();
                                    // Toast.makeText(getActivity(), "gióng", Toast.LENGTH_SHORT).show();
                                    key1 = snapshot1.getKey();
                                }
                            }
                            // Toast.makeText(getActivity(), key1+"khóa"+kt, Toast.LENGTH_SHORT).show();
                            if (kt == 1) {
                                DatabaseReference databaseReference1 = data.child(key1).child("soluong");
                                int  soluongthem = Integer.parseInt(sl1)
                                        + Integer.parseInt(sl.getText().toString());
                                databaseReference1.setValue(soluongthem + "");
                                //Toast.makeText(getActivity(), arrayList.get(i).getIdBan(), Toast.LENGTH_LONG).show();
                            } else {
                                Calendar calendar = Calendar.getInstance();
                                classHoaDon classHoaDon = new classHoaDon("", arrayList.get(i).getIdBan() + ""
                                        , calendar.get(Calendar.MINUTE) + "/"
                                        + calendar.get(Calendar.HOUR) + "/" + calendar.get(Calendar.DATE)
                                        , sl.getText().toString(), combo.getId_combo(), "Chưa Nhận",""
                                );

                                data.push().setValue(classHoaDon);

                            }
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }




        });
        dialog.show();

    }
}