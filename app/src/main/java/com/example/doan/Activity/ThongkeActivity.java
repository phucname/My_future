package com.example.doan.Activity;

import static android.media.CamcorderProfile.get;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.LayoutTransition;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.Adapter.adapter_spienr;
import com.example.doan.Class.MonAn;
import com.example.doan.Class.classThongKe;
import com.example.doan.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ThongkeActivity extends AppCompatActivity {

PieChart pieChart;
Spinner spinnerthang,spinnernam;
int thang,nam,ngay,ngay1,thang1,nam1;
Button thongke;
ArrayList<MonAn>arrayList_thongke;
ImageView lich,img_khoan1,img_khoan2;
TextView textView_date,text_khoan1,text_khoan2;
adapter_spienr adapter_spienr_nam,adapter_spienr_thang;
    int tong=0;
    Intent intent;
    ArrayList<PieEntry>pieEntries;
    ArrayList<String> arrayListnam,arrayListthang,arrayListngay;
    MianActivityBanac mianActivityBanac=new MianActivityBanac();
RadioGroup radioGroup;
LinearLayout linear_ngay,linear_khoan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongke);
        thongke=findViewById(R.id.button_thongke);
        lich=findViewById(R.id.img_thongke_ngay);
        textView_date=findViewById(R.id.text_date_thongke);
        radioGroup=findViewById(R.id.radio_group_thongke);
        linear_khoan=findViewById(R.id.liner_thongke_khoan);
        linear_ngay=findViewById(R.id.liner_thongke_ngay);
        text_khoan1=findViewById(R.id.text_thongke_khoan1);
        text_khoan2=findViewById(R.id.text_thongke_khoan2);
        img_khoan1=findViewById(R.id.img_thongke_khoan1);
        img_khoan2=findViewById(R.id.img_thongke_khoan2);
        spinnernam=findViewById(R.id.spinner_thongke_nam);
        spinnerthang=findViewById(R.id.spinner_thongke_thang);
     intent  =getIntent();
        textngay();


radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.radio_ngay:
                    textngay();
                    linear_ngay.setVisibility(View.VISIBLE);
                    linear_khoan.setVisibility(View.INVISIBLE);
                  spinnernam.setVisibility(View.INVISIBLE);
                  spinnerthang.setVisibility(View.INVISIBLE);

                  break;
                case R.id.radio_khoan:
                    textngay();
                    linear_ngay.setVisibility(View.INVISIBLE);
                    linear_khoan.setVisibility(View.VISIBLE);
                    spinnernam.setVisibility(View.INVISIBLE);
                    spinnerthang.setVisibility(View.INVISIBLE);

                    break;
                case R.id.radio_nam:
                    linear_ngay.setVisibility(View.INVISIBLE);
                    linear_khoan.setVisibility(View.INVISIBLE);
                    spinnernam.setVisibility(View.VISIBLE);
                    spinnerthang.setVisibility(View.INVISIBLE);


                    getnam();
                    adapter_spienr_nam =new adapter_spienr(ThongkeActivity.this,R.layout.selct_spiner,arrayListnam,"Năm");
                    spinnernam.setAdapter(adapter_spienr_nam);
                    spinnernam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        nam=Integer.parseInt(arrayListnam.get(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                    break;
                case R.id.radio_thang:
                    getnam();
                    adapter_spienr_nam =new adapter_spienr(ThongkeActivity.this,R.layout.selct_spiner,arrayListnam,"Năm");
                    spinnernam.setAdapter(adapter_spienr_nam);
                    spinnernam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            nam=Integer.parseInt(arrayListnam.get(position));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    getthang();
                    adapter_spienr_thang =new adapter_spienr(ThongkeActivity.this,R.layout.selct_spiner,arrayListthang,"Tháng");
                    spinnerthang.setAdapter(adapter_spienr_thang);
                    spinnerthang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        thang=Integer.parseInt(arrayListthang.get(position));

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                    linear_ngay.setVisibility(View.INVISIBLE);
                    linear_khoan.setVisibility(View.INVISIBLE);
                    spinnernam.setVisibility(View.VISIBLE);
                    spinnerthang.setVisibility(View.VISIBLE);
            }
    }
});


        thongke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieChart=findViewById(R.id.piechart);
                arrayList_thongke=new ArrayList<>();
               // Toast.makeText(ThongkeActivity.this, thang+"", Toast.LENGTH_SHORT).show();

                ArrayList<classThongKe>thongKes=new ArrayList<>();
                 if(radioGroup.getCheckedRadioButtonId()==R.id.radio_ngay){
                     thongke_ngay();
                 }
                if(radioGroup.getCheckedRadioButtonId()==R.id.radio_khoan){
                    thongke_khoan();
                }
                if(radioGroup.getCheckedRadioButtonId()==R.id.radio_thang){
                    thongke_thang();

                }
                if(radioGroup.getCheckedRadioButtonId()==R.id.radio_nam){
                    thongke_nam();
                }





    }
});


        lich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast.makeText(ThongkeActivity.this, year+"/"+mouth+"/"+day+"", Toast.LENGTH_SHORT).show();
                DatePickerDialog dialog=new DatePickerDialog(ThongkeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                       nam=year;
                       thang=month;
                       ngay=dayOfMonth;
                        textView_date.setText(ngay+"/"+thang+"/"+nam);
                    }
                }, nam,thang,ngay);
                dialog.show();
            }
        });
        img_khoan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast.makeText(ThongkeActivity.this, year+"/"+mouth+"/"+day+"", Toast.LENGTH_SHORT).show();
                DatePickerDialog dialog=new DatePickerDialog(ThongkeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        nam=year;
                        thang=month;
                        ngay=dayOfMonth;
                        text_khoan1.setText(ngay+"/"+thang+"/"+nam);
                    }
                }, nam,thang,ngay);
                dialog.show();
            }
        });
        img_khoan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast.makeText(ThongkeActivity.this, year+"/"+mouth+"/"+day+"", Toast.LENGTH_SHORT).show();
                DatePickerDialog dialog=new DatePickerDialog(ThongkeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        nam1=year;
                        thang1=month;
                        ngay1=dayOfMonth;
                        text_khoan2.setText(ngay1+"/"+thang1+"/"+nam1);
                    }
                }, nam1,thang1,ngay1);
                dialog.show();
            }
        });

        }

    private void thongke_nam() {


            DatabaseReference data = FirebaseDatabase.getInstance().getReference()
                    .child("NhaHang").child(intent.getStringExtra("id_nhahang"))
                    .child("Thongke").child(nam + "");
            tong=0;
            pieEntries=new ArrayList<>();
            data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue()!=null){
                        for(DataSnapshot snapshot_thang:snapshot.getChildren()){

                        for(DataSnapshot snapshot_ngay:snapshot_thang.getChildren()){

                            get_dl_tk("DS_MonAn","mon",snapshot_ngay.getKey(),snapshot_thang.getKey()
                                    ,nam+"");
                            get_dl_tk("DS_ComBo","combo",snapshot_ngay.getKey(),snapshot_thang.getKey()
                            ,nam+"");

                        }
                        }

                    }
                    else{
                       pieEntries=new ArrayList<>();
                        run_thongke();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });



        }


    private void thongke_thang() {

            DatabaseReference data = FirebaseDatabase.getInstance().getReference()
                    .child("NhaHang").child(intent.getStringExtra("id_nhahang"))
                    .child("Thongke").child(nam + "").child(thang+ "");
            tong=0;
        pieEntries=new ArrayList<>();
            data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue()!=null){
                        for(DataSnapshot snapshot_ngay:snapshot.getChildren()){
                            get_dl_tk("DS_MonAn","mon",snapshot_ngay.getKey(),thang+"",nam+"");
                            get_dl_tk("DS_ComBo","combo",snapshot_ngay.getKey(),thang+"",nam+"");

                        }

                    }
                    else{
                       run_thongke();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });



    }

    private void thongke_khoan() {
        tong=0;
        if(nam1==nam){
            if(thang1>thang){
                for (int i=thang;i<=thang1;i++){

                    DatabaseReference data = FirebaseDatabase.getInstance().getReference()
                            .child("NhaHang").child(intent.getStringExtra("id_nhahang"))
                            .child("Thongke").child(nam + "").child(i+ "");
                    tong=0;
                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.getValue()!=null){
                                for(DataSnapshot snapshot_ngay:snapshot.getChildren()){

                                    data.child(snapshot_ngay.getKey()).child("mon").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot snapshot_mon:snapshot.getChildren()){

                                                DatabaseReference datamon = FirebaseDatabase.getInstance().getReference()
                                                        .child("NhaHang").child(intent.getStringExtra("id_nhahang")).child("DS_MonAn");
                                                datamon.child(snapshot_mon.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        run_thongke1(snapshot_mon,snapshot,0);
                                                        run_thongke();
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
                                    data.child(snapshot_ngay.getKey()).child("combo").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.getValue()!=null){
                                                for (DataSnapshot snapshot_mon:snapshot.getChildren()){
                                                    DatabaseReference datamon = FirebaseDatabase.getInstance().getReference()
                                                            .child("NhaHang").child(intent.getStringExtra("id_nhahang")).child("DS_ComBo");
                                                    datamon.child(snapshot_mon.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                            run_thongke1(snapshot_mon,snapshot,1);
                                                            run_thongke();
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            }
                                            else {

                                                run_thongke();
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                            }
                            else{
                                run_thongke();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                }
            }
           if(thang1==thang) {
               if(ngay1>=ngay){
                   for (int i = ngay; i <= ngay1; i++) {
                       DatabaseReference data = FirebaseDatabase.getInstance().getReference()
                               .child("NhaHang").child(intent.getStringExtra("id_nhahang"))
                               .child("Thongke").child(nam + "").child(thang + "").child(i + "");
                       tong = 0;
                       data.addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               if (snapshot.getValue() != null) {


                                   data.child("mon").addListenerForSingleValueEvent(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                                           for (DataSnapshot snapshot_mon : snapshot.getChildren()) {

                                               DatabaseReference datamon = FirebaseDatabase.getInstance().getReference()
                                                       .child("NhaHang").child(intent.getStringExtra("id_nhahang")).child("DS_MonAn");
                                               datamon.child(snapshot_mon.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                   @Override
                                                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                       run_thongke1(snapshot_mon, snapshot, 0);
                                                       run_thongke();
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
                                   data.child("combo").addListenerForSingleValueEvent(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                                           if (snapshot.getValue() != null) {
                                               for (DataSnapshot snapshot_mon : snapshot.child("combo").getChildren()) {
                                                   DatabaseReference datamon = FirebaseDatabase.getInstance().getReference()
                                                           .child("NhaHang").child(intent.getStringExtra("id_nhahang")).child("DS_ComBo");
                                                   datamon.child(snapshot_mon.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                       @Override
                                                       public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                           run_thongke1(snapshot_mon, snapshot, 1);
                                                           run_thongke();
                                                       }

                                                       @Override
                                                       public void onCancelled(@NonNull DatabaseError error) {

                                                       }
                                                   });
                                               }
                                           } else {

                                               run_thongke();
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
                   }
               }else Toast.makeText(ThongkeActivity.this, "Ngày kết thức không thể nhỏ hơn ngày bắt đầu!", Toast.LENGTH_SHORT).show();

            }
           if(thang1<thang){
               Toast.makeText(ThongkeActivity.this, "Tháng kết thúc không thể nhỏ hơn tháng bắt đầu", Toast.LENGTH_SHORT).show();
           }
        }if(nam1>nam) {
            if (thang1 == thang) {
                if (ngay1 >= ngay) {
                    if (nam1 > nam) {
                        for (int i = nam; i <= nam1; i++) {
                            for (int j = 1; j <= 12; j++) {
                                if (i == nam1) {
                                    if (j <= thang1) {
                                        DatabaseReference data = FirebaseDatabase.getInstance().getReference()
                                                .child("NhaHang").child(intent.getStringExtra("id_nhahang"))
                                                .child("Thongke").child(i + "").child(j + "");
                                        tong = 0;
                                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.getValue() != null) {
                                                    for (DataSnapshot snapshot_ngay : snapshot.getChildren()) {

                                                        data.child(snapshot_ngay.getKey()).child("mon").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                for (DataSnapshot snapshot_mon : snapshot.getChildren()) {

                                                                    DatabaseReference datamon = FirebaseDatabase.getInstance().getReference()
                                                                            .child("NhaHang").child(intent.getStringExtra("id_nhahang")).child("DS_MonAn");
                                                                    datamon.child(snapshot_mon.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            run_thongke1(snapshot_mon, snapshot, 0);
                                                                            run_thongke();
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
                                                        data.child(snapshot_ngay.getKey()).child("combo").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if (snapshot.getValue() != null) {
                                                                    for (DataSnapshot snapshot_mon : snapshot.child("combo").getChildren()) {
                                                                        DatabaseReference datamon = FirebaseDatabase.getInstance().getReference()
                                                                                .child("NhaHang").child(intent.getStringExtra("id_nhahang")).child("DS_ComBo");
                                                                        datamon.child(snapshot_mon.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                                run_thongke1(snapshot_mon, snapshot, 1);
                                                                                run_thongke();
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                                            }
                                                                        });
                                                                    }
                                                                } else {

                                                                    run_thongke();
                                                                }

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                    }

                                                } else {
                                                    run_thongke();

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }

                                        });
                                    }
                                } else {
                                    DatabaseReference data = FirebaseDatabase.getInstance().getReference()
                                            .child("NhaHang").child(intent.getStringExtra("id_nhahang"))
                                            .child("Thongke").child(i + "").child(j + "");
                                    tong = 0;
                                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.getValue() != null) {
                                                for (DataSnapshot snapshot_ngay : snapshot.getChildren()) {

                                                    data.child(snapshot_ngay.getKey()).child("mon").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for (DataSnapshot snapshot_mon : snapshot.getChildren()) {

                                                                DatabaseReference datamon = FirebaseDatabase.getInstance().getReference()
                                                                        .child("NhaHang").child(intent.getStringExtra("id_nhahang")).child("DS_MonAn");
                                                                datamon.child(snapshot_mon.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        run_thongke1(snapshot_mon, snapshot, 0);
                                                                        run_thongke();
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
                                                    data.child(snapshot_ngay.getKey()).child("combo").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.getValue() != null) {
                                                                for (DataSnapshot snapshot_mon : snapshot.child("combo").getChildren()) {
                                                                    DatabaseReference datamon = FirebaseDatabase.getInstance().getReference()
                                                                            .child("NhaHang").child(intent.getStringExtra("id_nhahang")).child("DS_ComBo");
                                                                    datamon.child(snapshot_mon.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                            run_thongke1(snapshot_mon, snapshot, 1);
                                                                            run_thongke();
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });
                                                                }
                                                            } else {

                                                                run_thongke();
                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }

                                            } else {
                                                run_thongke();

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }

                                    });
                                }
                            }
                        }
                    }

                }
            }
        }
        if(nam1<nam){
            Toast.makeText(ThongkeActivity.this, "Năm kết thúc không thể nhỏ hơn năm bắt đầu!", Toast.LENGTH_SHORT).show();
        }


    }

    private void thongke_ngay(){
        pieEntries=new ArrayList<>();
        DatabaseReference data = FirebaseDatabase.getInstance().getReference()
                .child("NhaHang").child(intent.getStringExtra("id_nhahang"))
                .child("Thongke").child(nam + "").child(thang+ "").child(ngay+"");
            tong=0;
//            data.child("mon").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if(snapshot.getValue()!=null){
//                        for (DataSnapshot snapshot_mon:snapshot.getChildren()){
//                            DatabaseReference datamon = FirebaseDatabase.getInstance().getReference()
//                                    .child("NhaHang").child(intent.getStringExtra("id_nhahang"));
//                            datamon.child("DS_MonAn").child(snapshot_mon.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    if(snapshot.getValue()!=null){
//                                        pieEntries.add(new PieEntry(Integer.parseInt(snapshot_mon.child("soluong").getValue().toString())
//                                                ,snapshot.child("tenmon").getValue().toString()));
//                                        tong=tong+(Integer.parseInt(snapshot_mon.child("soluong").getValue().toString())
//                                                *Integer.parseInt(snapshot.child("gia").getValue().toString()));
//                                        // Toast.makeText(ThongkeActivity.this, snapshot_mon+"", Toast.LENGTH_SHORT).show();
//                                        run_thongke();
//                                    }else {
//                                        datamon.child("Thuc_Don_Cu").child("mon").child(snapshot_mon.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                pieEntries.add(new PieEntry(Integer.parseInt(snapshot_mon.child("soluong").getValue().toString())
//                                                        ,snapshot.child("tenmon").getValue().toString()));
//                                                tong=tong+(Integer.parseInt(snapshot_mon.child("soluong").getValue().toString())
//                                                        *Integer.parseInt(snapshot.child("gia").getValue().toString()));
//                                                // Toast.makeText(ThongkeActivity.this, snapshot_mon+"", Toast.LENGTH_SHORT).show();
//                                                run_thongke();
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError error) {
//
//                                            }
//                                        });
//                                    }
//
//                                }
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//                                }
//                            });
//                        }
//                    }else{
//                       run_thongke();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//            data.child("combo").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if(snapshot.getValue()!=null){
//                        for (DataSnapshot snapshot_mon:snapshot.child("combo").getChildren()){
//                            DatabaseReference datamon = FirebaseDatabase.getInstance().getReference()
//                                    .child("NhaHang").child(intent.getStringExtra("id_nhahang"));
//                            datamon.child("DS_ComBo").child(snapshot_mon.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                   if(snapshot.getValue()!=null){
//                                       pieEntries.add(new PieEntry(Integer.parseInt(snapshot_mon.child("soluong").getValue().toString())
//                                               ,snapshot.child("ten_combo").getValue().toString()));
//                                       tong=tong+(Integer.parseInt(snapshot_mon.child("soluong").getValue().toString())
//                                               *Integer.parseInt(snapshot.child("gia_combo").getValue().toString()));
//                                       // Toast.makeText(ThongkeActivity.this, snapshot_mon+"", Toast.LENGTH_SHORT).show();
//                                       run_thongke();
//                                   }else {
//                                       datamon.child("Thuc_Don_Cu").child("combo").child(snapshot_mon.getKey())
//                                               .addListenerForSingleValueEvent(new ValueEventListener() {
//                                           @Override
//                                           public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                               pieEntries.add(new PieEntry(Integer.parseInt(snapshot_mon.child("soluong").getValue().toString())
//                                                       ,snapshot.child("ten_combo").getValue().toString()));
//                                               tong=tong+(Integer.parseInt(snapshot_mon.child("soluong").getValue().toString())
//                                                       *Integer.parseInt(snapshot.child("gia_combo").getValue().toString()));
//                                               // Toast.makeText(ThongkeActivity.this, snapshot_mon+"", Toast.LENGTH_SHORT).show();
//                                               run_thongke();
//                                           }
//
//                                           @Override
//                                           public void onCancelled(@NonNull DatabaseError error) {
//
//                                           }
//                                       });
//
//                                       }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                }
//                            });
//                        }
//                    }
//                    else {
//
//                      run_thongke();
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
        get_dl_tk("DS_MonAn","mon",ngay+"",thang+"",nam+"");
        get_dl_tk("DS_ComBo","combo",ngay+"",thang+"",nam+"");
        }
        private void textngay(){
            Calendar c = Calendar.getInstance();
            nam = c.get(Calendar.YEAR);
            thang = c.get(Calendar.MONTH)+1;
            ngay=c.get(Calendar.DAY_OF_MONTH);
            nam1 = c.get(Calendar.YEAR);
            thang1 = c.get(Calendar.MONTH)+1;
            ngay1=c.get(Calendar.DAY_OF_MONTH);
            textView_date.setText(ngay+"/"+(thang)+"/"+nam);
            text_khoan1.setText(ngay+"/"+(thang)+"/"+nam);
            text_khoan2.setText(ngay+"/"+(thang)+"/"+nam);
        }
private  void get_dl_tk(String title_max,String title_min,String day,String muoth,String yer){
    DatabaseReference data = FirebaseDatabase.getInstance().getReference()
            .child("NhaHang").child(intent.getStringExtra("id_nhahang"))
            .child("Thongke").child(yer + "").child(muoth+ "").child(day+"");
    data.child(title_min).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.getValue()!=null){
                for (DataSnapshot snapshot_mon:snapshot.getChildren()){
                    DatabaseReference datamon = FirebaseDatabase.getInstance().getReference()
                            .child("NhaHang").child(intent.getStringExtra("id_nhahang"));
                    datamon.child(title_max).child(snapshot_mon.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.getValue()!=null){
                                if(title_min.equals("mon")){
                                   run_thongke1(snapshot_mon,snapshot,0);
                                   run_thongke();
                               }else {

                                    run_thongke1(snapshot_mon,snapshot,1);
                                    run_thongke();
                                }



                            }else {
                                datamon.child("Thuc_Don_Cu").child(title_min).child(snapshot_mon.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        pieEntries.add(new PieEntry(Integer.parseInt(snapshot_mon.child("soluong").getValue().toString())
                                                ,snapshot.child("tenmon").getValue().toString()));
                                        tong=tong+(Integer.parseInt(snapshot_mon.child("soluong").getValue().toString())
                                                *Integer.parseInt(snapshot.child("gia").getValue().toString()));
                                        // Toast.makeText(ThongkeActivity.this, snapshot_mon+"", Toast.LENGTH_SHORT).show();
                                        run_thongke1(snapshot_mon,snapshot,0);
                                        run_thongke();
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
                }
            }else{
                run_thongke();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
}
    private void getnam() {
        arrayListnam=new ArrayList<>();
      arrayListnam.add("2019");
        arrayListnam.add("2020");
        arrayListnam.add("2021");
        Calendar calendar=Calendar.getInstance();
        if(2021<calendar.get(Calendar.YEAR)){
            for(int i= 2022;i<=calendar.get(Calendar.YEAR);i++){
                arrayListnam.add(i+"");
            }
        }

    }
    private void getthang() {
        arrayListthang=new ArrayList<>();
      for(int i=1;i<13;i++){
          arrayListthang.add(i+"");
      }


    }
    private void run_thongke1(DataSnapshot snapshot_mon,DataSnapshot snapshot,int title){
     String ten="",gia="";
      if(title==0){
          ten="tenmon";
      }else {
          ten="ten_combo";
          gia="_combo";
      }
        int dk=0;
        for (int i=0;i<arrayList_thongke.size();i++){
            MonAn monAn=arrayList_thongke.get(i);
            if(monAn.getId().equals(snapshot_mon.getKey())){
                arrayList_thongke.get(i).setSl((Integer.parseInt(monAn.getSl())
                        +Integer.parseInt(snapshot_mon.child("soluong").getValue().toString()))+"");
                dk=1;
                break;
            }
            //  Toast.makeText(ThongkeActivity.this, "fff", Toast.LENGTH_SHORT).show();
        }
        if(dk==0) {
            arrayList_thongke.add(new MonAn(snapshot.child(ten).getValue().toString(),
                    snapshot_mon.child("soluong").getValue().toString()
                    ,snapshot_mon.getKey() ));

        }
        tong=tong+(Integer.parseInt(snapshot_mon.child("soluong").getValue().toString())
                *Integer.parseInt(snapshot.child("gia"+gia).getValue().toString()));
        // Toast.makeText(ThongkeActivity.this, snapshot_mon+"", Toast.LENGTH_SHORT).show();
        pieEntries =new ArrayList<>();
        for (MonAn m :arrayList_thongke) {
            pieEntries.add(new PieEntry(Integer.parseInt(m.getSl()),m.getTenmon()));
        }

    }
    private void run_thongke(){
        PieDataSet pieDataSet=new PieDataSet(pieEntries,"Tháng  "+thang);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setFormSize(30f);
        pieDataSet.setValueTextSize(30f);
        PieData pieData=new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setCenterText(mianActivityBanac.VND(tong));
        pieChart.setCenterTextSize(30f);
        pieChart.getDescription().setEnabled(false);
        pieChart.animate();
    }
    private void geyngay(int month){
        switch (month) {
            // các tháng 1, 3, 5, 7, 8, 10 và 12 có 31 ngày.
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                ngay=31;
                break;

            // các tháng 4, 6, 9 và 11 có 30 ngày
            case 4:
            case 6:
            case 9:
            case 11:
                ngay=30;
                break;

            // Riêng tháng 2 nếu là năm nhuận thì có 29 ngày, còn không thì có 28 ngày.
            case 2:

                if ((nam % 4 == 0 && nam % 100 != 0) || (nam % 400 == 0)) {
                   ngay=29;
                } else {
                    ngay=28;
                }
                break;


        }
        arrayListngay=new ArrayList<>();
        for(int i=1;i<=ngay;i++){
            arrayListngay.add(i+"");
        }
    }
}