package com.example.doan.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.doan.Class.MonAn;
import com.example.doan.Class.NhanVien;
import com.example.doan.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class DangKyActivity extends AppCompatActivity {
    EditText Email,Phone,tennguoidung,matkhau,nhaplaimatkhau;
    Button dangky;
    FirebaseAuth auth;
    ImageView hienpass,hienpass1,img_dangky;
    FirebaseStorage storage;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        auth = FirebaseAuth.getInstance();
        Email = findViewById(R.id.editemail);
        Phone = findViewById(R.id.edit_dk_sdt);
        tennguoidung = findViewById(R.id.edittennguoidung);
        matkhau = findViewById(R.id.editmatkhau1);
        hienpass=findViewById(R.id.img_dangky_pass);
        storage = FirebaseStorage.getInstance();//khởi tạo
        StorageReference storageReference=storage.getReferenceFromUrl("gs://doan-f52ef.appspot.com/");//url của trang storage
        hienpass1=findViewById(R.id.img_dangky_pass1);
        img_dangky=findViewById(R.id.img_dangky);
        nhaplaimatkhau = findViewById(R.id.editmatkhau2);
        dangky = findViewById(R.id.buttondangky);
        Intent intent = getIntent();
        img_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);
            }
        });

          hienpass1.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  int kieutyr=nhaplaimatkhau.getInputType();
                nhaplaimatkhau.setInputType(3);
                  // Toast.makeText(MianActivityBanac.this,pass1.getInputType()+ "", Toast.LENGTH_SHORT).show();

                  Handler handler=new Handler();
                  handler.postDelayed(new Runnable() {
                      @Override
                      public void run() {
                          nhaplaimatkhau.setInputType(kieutyr);
                          // Toast.makeText(MianActivityBanac.this,pass1.getInputType()+ "", Toast.LENGTH_SHORT).show();
                      }


                  },2000);
              }
          });
        hienpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int kieutyr=matkhau.getInputType();
                matkhau.setInputType(3);
                // Toast.makeText(MianActivityBanac.this,pass1.getInputType()+ "", Toast.LENGTH_SHORT).show();

                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       matkhau.setInputType(kieutyr);
                        // Toast.makeText(MianActivityBanac.this,pass1.getInputType()+ "", Toast.LENGTH_SHORT).show();
                    }


                },2000);
            }
        });
        dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = Email.getText().toString();
                String pass = matkhau.getText().toString();
                String pass1 = nhaplaimatkhau.getText().toString();
                    if (!TextUtils.isEmpty(email)) {
                        if (email.contains("@gmail.com")) {
                            String email1 = email.substring(email.length() - 10);
                            if (email1.equals("@gmail.com")) {
                                if (!TextUtils.isEmpty(pass)) {
                                    if (!TextUtils.isEmpty(pass1)) {
                                        if(pass.equals(pass1)) {
                                            auth.createUserWithEmailAndPassword(Email.getText().toString(),matkhau.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                        StorageReference mountainsRef = storageReference.child( user.getUid()+ "/imgNhanVien/" +user.getUid());

                                                        UploadTask uploadTask = mountainsRef.putBytes(Img(img_dangky));//thêm ảnh lên sever

                                                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                                            @Override
                                                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                                if (!task.isSuccessful()) {
                                                                    throw task.getException();
                                                                }

                                                                // Continue with the task to get the download URL
                                                                return mountainsRef.getDownloadUrl();
                                                            }
                                                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Uri> task) {
                                                                if (task.isSuccessful()) {
                                                                    Uri downloadUri = task.getResult();
                                                                    if (intent.getStringExtra("ChucVu").equals("1")) {

                                                                        NhanVien nhanVien = new NhanVien(tennguoidung.getText().toString()
                                                                                , Phone.getText().toString(), user.getUid()
                                                                                , downloadUri.toString(), 1, Email.getText().toString());
                                                                        DatabaseReference data = FirebaseDatabase.getInstance().getReference()
                                                                                .child("NhaHang").child(user.getUid()).child("DS_NhanVien").child(user.getUid());
                                                                        data.setValue(nhanVien).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    Intent intent = new Intent(DangKyActivity.this, TaoNhaHangActivity.class);
                                                                                    Bundle bundle = new Bundle();
                                                                                    bundle.putSerializable("TTNhanVien", nhanVien);
                                                                                    intent.putExtras(bundle);
                                                                                    startActivity(intent);
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            }
                                                        });

                                                    } else
                                                        Toast.makeText(DangKyActivity.this, "Đăng Ký Không Thành Công ", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }else   Toast.makeText(DangKyActivity.this, " PassWord không giống", Toast.LENGTH_SHORT).show();
                                    }else   Toast.makeText(DangKyActivity.this, "Vui lòng nhập PassWord 2", Toast.LENGTH_SHORT).show();

                                } else
                                    Toast.makeText(DangKyActivity.this, "Vui lòng nhập PassWord", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(DangKyActivity.this, "Email phải có đuôi là @gmail.com", Toast.LENGTH_SHORT).show();
                        } else Toast.makeText(DangKyActivity.this, "không phải email ", Toast.LENGTH_SHORT).show();


                    } else Toast.makeText(DangKyActivity.this, "Vui lòng nhập Email", Toast.LENGTH_SHORT).show();
//

                }
            });

//                dangky.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        auth.createUserWithEmailAndPassword(email, matkhau.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    Intent intent = new Intent(DangKyActivity.this,chitiet_history.class);
//                                    startActivity(intent);
//                                } else
//                                    Toast.makeText(DangKyActivity.this, "that bai", Toast.LENGTH_LONG).show();
//                            }
//                        });
//                    }
//                });


        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==RESULT_OK&&null!=data.getData()){
            Uri uridata=data.getData();
          /* String[] mang={MediaStore.Images.Media.DATA};
           Cursor cursor=getContentResolver().query(uridata,mang,null,null,null);
          cursor.moveToFirst();
            int colum=cursor.getColumnIndex(mang[0]);

            String stirngss=cursor.getString(colum);
           cursor.close();
            imageViewthem.setImageBitmap(BitmapFactory.decodeFile(stirngss));*/
            img_dangky.setImageURI(uridata);

        }



    }


    public byte[] Img(ImageView img){
        img.setDrawingCacheEnabled(true);
        img.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        return data;
    }
}