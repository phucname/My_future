package com.example.doan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.Class.NhaHang;
import com.example.doan.Class.NhanVien;
import com.example.doan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class DangNhapActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText Email, Pass;
    private Button Dangnhap;
    private TextView texdangky;
    private FrameLayout eye;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
       auth=FirebaseAuth.getInstance();
        Email = findViewById(R.id.email);
        Pass = findViewById(R.id.pass);
        eye=findViewById(R.id.img_eye);
        texdangky=findViewById(R.id.textdangky);
        Dangnhap = findViewById(R.id.dangnhap);
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int kieutyr=Pass.getInputType();
                Pass.setInputType(3);
                // Toast.makeText(MianActivityBanac.this,pass1.getInputType()+ "", Toast.LENGTH_SHORT).show();

                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Pass.setInputType(kieutyr);
                        // Toast.makeText(MianActivityBanac.this,pass1.getInputType()+ "", Toast.LENGTH_SHORT).show();
                    }


                },2000);

            }
        });

       Intent intent=getIntent();
         if(intent.getStringExtra("DN_NhaHang")!=null){
          texdangky.setVisibility(View.INVISIBLE);
         }

           Dangnhap.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   dangnhapclick();
               }
           });
           texdangky.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent=new Intent(DangNhapActivity.this, DangKyActivity.class);
                 intent.putExtra("ChucVu","1");
                   startActivity(intent);
               }
           });
       }



    private void dangnhapclick() {
        ProgressDialog progressDialog=new ProgressDialog(this);
        Intent intent=getIntent();
        String email = Email.getText().toString();
        String pass = Pass.getText().toString();
        progressDialog.show();
        if (!TextUtils.isEmpty(email)){
           if(email.contains("@gmail.com")){
               String email1=email.substring(email.length()-10);
               if(email1.equals("@gmail.com")){
                   if(!TextUtils.isEmpty(pass)){
                       if(pass.length()>5){
                       if(intent.getStringExtra("DNTaoNH")!=null)
                       {
                           auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                   if (task.isSuccessful()) {
                                       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                   DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference()
                                           .child("NhaHang");
                                   databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                                           for (DataSnapshot snapshot1:snapshot.getChildren()){
                                            if(snapshot1.getKey().equals(user.getUid())){
                                                if(snapshot1.child("TT_NhaHang").exists()){
                                                    Intent intent1 = new Intent(DangNhapActivity.this, TrangChuActivuty.class);
                                                    File myInternalFile;
                                                    ContextWrapper contextWrapper = new ContextWrapper(DangNhapActivity.this);
                                                    //Tạo (Hoặc là mở file nếu nó đã tồn tại) Trong bộ nhớ trong có thư mục là ThuMucCuaToi.
                                                    File directory = contextWrapper.getDir("TK", Context.MODE_APPEND);//tên Thư Mục
                                                    myInternalFile = new File(directory, "TinhTrang.txt");

                                                    try {
                                                        //Mở file
                                                        FileOutputStream fos = new FileOutputStream(myInternalFile);
                                                        //Ghi dữ liệu vào file
                                                        fos.write((snapshot1.child("DS_NhanVien").child(user.getUid()).child("chucVuNV").getValue().toString() + "," +snapshot1.getKey()).getBytes());
                                                        fos.close();
                                                        Intent intent_chuyen = new Intent(getApplication(), TrangChuActivuty.class);
                                                        startActivity(intent_chuyen);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }

                                                    progressDialog.dismiss();
                                                    //Toast.makeText(DangNhapActivity.this, snapshot.child("chucVuNV").getValue().toString()+"", Toast.LENGTH_SHORT).show();
                                                    startActivity(intent1);

                                                }else {
                                                    progressDialog.dismiss();
                                                    Intent intent1 = new Intent(DangNhapActivity.this, TaoNhaHangActivity.class);
                                                    startActivity(intent1);


                                                }
                                            }
                                           }
                                       }

                                       @Override
                                       public void onCancelled(@NonNull DatabaseError error) {

                                       }
                                   });




                                   }
                               }
                           });
                       }else {
                           auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                   if (task.isSuccessful()) {
                                       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                        UserProfileChangeRequest userProfileChangeRequest=new UserProfileChangeRequest.Builder()
//                                .setDisplayName("phuchong")
//                                .build();
                                       DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("NhaHang")
                                               .child(intent.getStringExtra("id_nhahang")).child("DS_NhanVien").child(user.getUid());
                                       databaseReference.addValueEventListener(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                                               if (snapshot.getValue() != null) {
                                                   Intent intent1 = new Intent(DangNhapActivity.this, TrangChuActivuty.class);
                                                   File myInternalFile;
                                                   ContextWrapper contextWrapper = new ContextWrapper(DangNhapActivity.this);
                                                   //Tạo (Hoặc là mở file nếu nó đã tồn tại) Trong bộ nhớ trong có thư mục là ThuMucCuaToi.
                                                   File directory = contextWrapper.getDir("TK", Context.MODE_APPEND);//tên Thư Mục
                                                   myInternalFile = new File(directory, "TinhTrang.txt");

                                                   try {
                                                       //Mở file
                                                       FileOutputStream fos = new FileOutputStream(myInternalFile);
                                                       //Ghi dữ liệu vào file
                                                       fos.write((snapshot.child("chucVuNV").getValue().toString() + ","
                                                               +intent.getStringExtra("id_nhahang")).getBytes());
                                                       fos.close();
                                                       Intent intent_chuyen = new Intent(getApplication(), TrangChuActivuty.class);
                                                       startActivity(intent_chuyen);
                                                   } catch (IOException e) {
                                                       e.printStackTrace();
                                                   }

                                                   progressDialog.dismiss();
                                                   Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                                   startActivity(intent1);
                                               } else {
                                                   progressDialog.dismiss();
                                                   auth.signOut();
                                                   Toast.makeText(DangNhapActivity.this, "Tài khoản không đúng", Toast.LENGTH_SHORT).show();
                                               }

                                           }

                                           @Override
                                           public void onCancelled(@NonNull DatabaseError error) {

                                           }
                                       });


                                   }else {
                                       progressDialog.dismiss();
                                       Toast.makeText(DangNhapActivity.this, "Tài khoản hoặc PassWord sai!", Toast.LENGTH_SHORT).show();
                                   }


                               }
                           });
                           //else {
////                                        Intent intent1 = new Intent(DangNhapActivity.this, MianActivityBanac.class);
////                                        startActivity(intent1);
//                                    }
//
//
//                                }
                           //khi nhân viên muốn đăng nhập vào tài khoản
//                                if(intent.getStringExtra("DN_NhaHang")!=null){
//                                    //Toast.makeText(this, "Đăng nhập nahf hàng", Toast.LENGTH_SHORT).show();
//                                    Bundle bundle = getIntent().getExtras();
//                                    Intent intent1 = getIntent();
//                                    String key = intent1.getStringExtra("key");
//                                    ArrayList<NhanVien> listnhavien = (ArrayList<NhanVien>) bundle.get("listNV");
//                                    for (NhanVien vienList : listnhavien) {
//                                        if (email.equals(vienList.getMail())) {
//                                            if(pass.equals(vienList.getPass())){
//                                                Toast.makeText(DangNhapActivity.this, "Đăng Nhập Thành Công", Toast.LENGTH_SHORT).show();
//                                                File myInternalFile;
//                                                ContextWrapper contextWrapper = new ContextWrapper(DangNhapActivity.this);
//                                                //Tạo (Hoặc là mở file nếu nó đã tồn tại) Trong bộ nhớ trong có thư mục là ThuMucCuaToi.
//                                                File directory = contextWrapper.getDir("TK", Context.MODE_PRIVATE);//tên Thư Mục
//                                                myInternalFile = new File(directory, "TinhTrang.txt");
//                                                try {
//                                                    //Mở file
//                                                    FileOutputStream fos = new FileOutputStream(myInternalFile);
//                                                    //Ghi dữ liệu vào file
//                                                    fos.write((vienList.getChucVuNV()+"," + key).getBytes());
//                                                    fos.close();
//                                                    Intent intent_chuyen= new Intent(getApplication(), TrangChuActivuty.class);
//                                                    startActivity(intent_chuyen);
//                                                } catch (IOException e) {
//                                                    e.printStackTrace();
//                                                }
//                                                break;
//                                            }else
//                                                Toast.makeText(DangNhapActivity.this, "Tài Khoản Hoặc PassWrod sai", Toast.LENGTH_SHORT).show();
//                                        }else Toast.makeText(DangNhapActivity.this, "Tài Khoản Hoặc PassWrod sai", Toast.LENGTH_SHORT).show();

                           //                  else {
                           //                        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                           //                        DatabaseReference data= FirebaseDatabase.getInstance().getReference();
                           //                        data.addValueEventListener(new ValueEventListener() {
                           //                            @Override
                           //                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                           //                                for(DataSnapshot snapshot1:snapshot.getChildren()){
                           //                                    if(snapshot1.getKey().equals(user.getUid())){
                           //                                        Intent intent = new Intent(DangNhapActivity.this, MianActivityBanac.class);
                           //                                        intent.putExtra("key",snapshot1.getKey());
                           //                                        intent.putExtra("name",snapshot1.child("TenNhaHang").getValue().toString());
                           //                                        startActivity(intent);
                           //                                    }
                           //
                           //                                }
                           //
                           //
                           //                            }
                           //
                           //                            @Override
                           //                            public void onCancelled(@NonNull DatabaseError error) {
                           //
                           //                            }
                           //                        });

                           // }


//                            }
//
//                        });

                       }
                       }else Toast.makeText(this, "PassWord phải trên 6 kí tự", Toast.LENGTH_SHORT).show();
                   }else Toast.makeText(this, "Vui lòng nhập PassWord", Toast.LENGTH_SHORT).show();
               }else Toast.makeText(this, "Email phải có đuôi là @gmail.com", Toast.LENGTH_SHORT).show();
           }else Toast.makeText(this, "không phải email ", Toast.LENGTH_SHORT).show();


        }else Toast.makeText(this, "Vui lòng nhập Email", Toast.LENGTH_SHORT).show();
//
        }

}