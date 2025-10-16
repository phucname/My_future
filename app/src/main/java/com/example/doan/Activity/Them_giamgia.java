package com.example.doan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.Adapter.Adapter_giamgia;
import com.example.doan.Adapter.adapter_spienr;
import com.example.doan.Class.MonAn;
import com.example.doan.Class.class_giamgia;
import com.example.doan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Them_giamgia extends AppCompatActivity {
    RecyclerView recyc_ds_giamgia;
    ArrayList<MonAn> manArrayList;
    DatabaseReference databaseReference;
    Intent intent;
    Button next;
    Adapter_giamgia adapter_mon;
    int kieu_giamgia;
    int ngay_tu, thang_tu, nam_tu, ngay_den, thang_den, nam_den;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_giamgia);
        recyc_ds_giamgia = findViewById(R.id.recy_them_mongg);
        next = findViewById(R.id.button_next_themgiamgia);
        intent = getIntent();
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("NhaHang").child(intent.getStringExtra("id_nhahang"));
        databaseReference.child("DS_MonAn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                manArrayList = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (Integer.parseInt(snapshot1.child("loai_giamgia").getValue().toString()) == 2) {
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
                adapter_mon = new Adapter_giamgia(manArrayList, R.layout.item_giamgia, Them_giamgia.this, intent.getStringExtra("id_nhahang"), 0);
                // GridLayoutManager gridLayoutManager=new GridLayoutManager(view.getContext(), 2);
                LinearLayoutManager gridLayoutManager = new LinearLayoutManager(Them_giamgia.this, RecyclerView.VERTICAL, false);
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
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (Integer.parseInt(snapshot1.child("loai_giamgia").getValue().toString()) == 2) {
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
                adapter_mon = new Adapter_giamgia(manArrayList, R.layout.item_giamgia, Them_giamgia.this, intent.getStringExtra("id_nhahang"), 0);
                // GridLayoutManager gridLayoutManager=new GridLayoutManager(view.getContext(), 2);
                LinearLayoutManager gridLayoutManager = new LinearLayoutManager(Them_giamgia.this, RecyclerView.VERTICAL, false);
                recyc_ds_giamgia.setLayoutManager(gridLayoutManager);
                recyc_ds_giamgia.setAdapter(adapter_mon);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter_mon.getMondatron().size() > 0) {
                    Dialog dialog = new Dialog(Them_giamgia.this);
                    dialog.setContentView(R.layout.dialog_giamgia);
                  //  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    Window window = dialog.getWindow();
                    if (window == null) {
                        return;
                    }
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    //window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    WindowManager.LayoutParams windowaatriss = window.getAttributes();
                    windowaatriss.gravity = Gravity.CENTER;
                    ArrayList<String> arrayList_loai = new ArrayList<>();
                    arrayList_loai.add("Phần Trăm");
                    arrayList_loai.add("Số Tiền");

                    EditText giatri = dialog.findViewById(R.id.dialog_giatrigiam);
                    ImageView img_tu = dialog.findViewById(R.id.dialog_img_lich_tu);
                    ImageView img_den = dialog.findViewById(R.id.dialog_img_lich_den);
                    TextView text_tu = dialog.findViewById(R.id.dialog_text_lich_tu);
                    TextView text_den = dialog.findViewById(R.id.dialog_text_lich_den);
                    Calendar c = Calendar.getInstance();
                    nam_tu = c.get(Calendar.YEAR);
                    thang_tu = c.get(Calendar.MONTH) + 1;
                    ngay_tu = c.get(Calendar.DAY_OF_MONTH);
                    nam_den = c.get(Calendar.YEAR);
                    thang_den = c.get(Calendar.MONTH) + 1;
                    ngay_den = c.get(Calendar.DAY_OF_MONTH);
                    text_tu.setText(ngay_tu + "/" + (thang_tu) + "/" + nam_tu);
                    text_den.setText(ngay_tu + "/" + (thang_tu) + "/" + nam_tu);
                    Spinner spinner_loai = dialog.findViewById(R.id.dialog_Spiner_giamgia);
                    adapter_spienr adapter_spienr = new adapter_spienr(Them_giamgia.this
                            , R.layout.itme_spiner, arrayList_loai, "Kiểu Giảm");
                    spinner_loai.setAdapter(adapter_spienr);
                    spinner_loai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            kieu_giamgia = position;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    Button them = dialog.findViewById(R.id.dialog_button_them_mongiamgia);
                    img_tu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            //Toast.makeText(ThongkeActivity.this, year+"/"+mouth+"/"+day+"", Toast.LENGTH_SHORT).show();
                            DatePickerDialog dialog = new DatePickerDialog(Them_giamgia.this, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    nam_tu = year;
                                    thang_tu = month;
                                    ngay_tu = dayOfMonth;
                                    text_tu.setText(ngay_tu + "/" + (thang_tu) + "/" + nam_tu);
                                }
                            }, nam_tu, thang_tu, ngay_tu);
                            dialog.show();
                        }
                    });
                    img_den.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            //Toast.makeText(ThongkeActivity.this, year+"/"+mouth+"/"+day+"", Toast.LENGTH_SHORT).show();
                            DatePickerDialog dialog = new DatePickerDialog(Them_giamgia.this, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    nam_den = year;
                                    thang_den = month;
                                    ngay_den = dayOfMonth;
                                    text_den.setText(ngay_den + "/" + (thang_den) + "/" + nam_den);
                                }
                            }, nam_den, thang_den, ngay_den);
                            dialog.show();
                        }
                    });
                    them.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           if(nam_den>=c.get(Calendar.YEAR)){
                               if(thang_den>=(c.get(Calendar.MONTH)+1)){
                                   if(ngay_den>=c.get(Calendar.DAY_OF_MONTH)){
                                       if (nam_den >= nam_tu) {
                                           if (thang_den >= thang_tu) {
                                               if (ngay_den >= ngay_tu) {
                                                   ArrayList<MonAn> newmonArrayList = adapter_mon.getMondatron();
                                                   if (kieu_giamgia == 0) {

                                                       if (Integer.parseInt(giatri.getText().toString()) < 100) {

                                                           for (MonAn monAn : newmonArrayList) {
                                                               if (monAn.getLoai_mon().equals("0")) {
                                                                   them_giamgia(monAn.getId(), "DS_MonAn", giatri.getText().toString(), "PT");
                                                               } else
                                                                   them_giamgia(monAn.getId(), "DS_ComBo", giatri.getText().toString(), "PT");


                                                           }
                                                           finish();
                                                       } else
                                                           Toast.makeText(Them_giamgia.this, "Giá trị khuyến mãi không được quá 100%", Toast.LENGTH_SHORT).show();
                                                       dialog.dismiss();
                                                   } else {

                                                       for (MonAn monAn : newmonArrayList) {

                                                           if (Integer.parseInt(monAn.getGia()) > Integer.parseInt(giatri.getText().toString())) {
                                                               if (monAn.getLoai_mon().equals("0")) {
                                                                   them_giamgia(monAn.getId(), "DS_MonAn", giatri.getText().toString(), "Tien");
                                                               } else
                                                                   them_giamgia(monAn.getId(), "DS_ComBo", giatri.getText().toString(), "Tien");

                                                           } else
                                                               Toast.makeText(Them_giamgia.this, "Món " + monAn.getTenmon() + " có số tiền nhỏ hơn giá trị khuyến mãi không áp dụng được", Toast.LENGTH_SHORT).show();
                                                       }
                                                       finish();
                                                       dialog.dismiss();
                                                   }
                                               } else
                                                   Toast.makeText(Them_giamgia.this, "Ngày kết thúc không thể nhỏ hơn ngày bắt đầu!", Toast.LENGTH_SHORT).show();
                                           } else
                                               Toast.makeText(Them_giamgia.this, "Tháng kết thúc không thể nhỏ hơn Tháng bắt đầu!", Toast.LENGTH_SHORT).show();
                                       }
                                       else
                                           Toast.makeText(Them_giamgia.this, "Năm kết thúc không thể nhỏ hơn năm bắt đầu!", Toast.LENGTH_SHORT).show();
                                   }else
                                       Toast.makeText(Them_giamgia.this, "Ngày kết thúc không thể nhỏ hơn thài gian thực", Toast.LENGTH_SHORT).show();
                               }else  Toast.makeText(Them_giamgia.this, "Tháng kết thúc không thể nhỏ hơn thài gian thực", Toast.LENGTH_SHORT).show();
                           }else  Toast.makeText(Them_giamgia.this, "Năm kết thúc không thể nhỏ hơn thài gian thực", Toast.LENGTH_SHORT).show();



                        }
                    });
                    dialog.show();
                } else
                    Toast.makeText(Them_giamgia.this, "Bạn chưa chọn món ăn dành cho khuyến mãi!", Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void them_giamgia(String idmon, String ten_chu, String giatri, String kieu_gg) {
        class_giamgia giamgia = new class_giamgia(idmon, ngay_tu + "/" + thang_tu + "/" + nam_tu
                + "," + ngay_den + "/" + thang_den + "/" + nam_den, giatri);
        databaseReference.child("Khuyen_mai").child(kieu_gg).child(idmon).setValue(giamgia)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            databaseReference.child(ten_chu).child(idmon).child("loai_giamgia").setValue(kieu_giamgia);
                        }

                    }
                });
    }

    @Override
    public void onBackPressed() {

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        Intent intent=new Intent(Them_giamgia.this,MianActivityBanac.class);
        intent.putExtra("id_nhahang",user.getUid());
        intent.putExtra("chucvu","1");
        intent.putExtra("them","giamgia");
        startActivity(intent);

    }

}