package com.example.doan.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.Class.NhanVien;
import com.example.doan.Class.classBan;
import com.example.doan.Class.classHoaDon;
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
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import kotlinx.coroutines.Delay;

public class ThemNhanVienActivity extends AppCompatActivity {


EditText tennv,sdtnv,gmailnv,pass;
TextView title;
Button themnv;
ImageView imgnhanvien,img_share_pass;
FirebaseStorage storage;
NhanVien nhanVien;
FrameLayout frameLayout_pass;
    ArrayList<String>a=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nhan_vien);
        tennv = findViewById(R.id.edittext_tennv);
        title=findViewById(R.id.text_title_themnv);
        sdtnv = findViewById(R.id.edittext_sdtnv);
        gmailnv = findViewById(R.id.edittext_mailnv);
        img_share_pass=findViewById(R.id.img_vise_pass_themnv);
        pass=findViewById(R.id.edittext_passnv);
        themnv = findViewById(R.id.button_themnv);
        frameLayout_pass=findViewById(R.id.fragmet_pass_nhanvien);
        imgnhanvien=findViewById(R.id.img_themnhanvien);
        storage = FirebaseStorage.getInstance();//khởi tạo
        StorageReference storageReference=storage.getReferenceFromUrl("gs://doan-f52ef.appspot.com/");//url của trang storage
        Bundle bundle = getIntent().getExtras();
        Intent intent = getIntent();
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        img_share_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int kieutyr=pass.getInputType();
                pass.setInputType(3);
                // Toast.makeText(MianActivityBanac.this,pass1.getInputType()+ "", Toast.LENGTH_SHORT).show();

                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pass.setInputType(kieutyr);
                        // Toast.makeText(MianActivityBanac.this,pass1.getInputType()+ "", Toast.LENGTH_SHORT).show();
                    }


                },2000);
            }
        });
        if (bundle.get("classsuanhanvien")!=null)
              //  bundle.get("classsuanhanvien") != null)
        {
            title.setText("Update");
            nhanVien = (NhanVien) bundle.get("classsuanhanvien");
            DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("NhaHang")
                    .child(intent.getStringExtra("id_nhahang")).child("DS_NhanVien")
                    .child(nhanVien.getId());
            StorageReference mountainsRef = storageReference.child(intent.getStringExtra("id_nhahang") + "/imgNhanVien/" + data.getKey());
            tennv.setText(nhanVien.getTenNV());
            sdtnv.setText(nhanVien.getSdtnv());
            gmailnv.setText(nhanVien.getMail());
           frameLayout_pass.setVisibility(View.INVISIBLE);

           if(nhanVien.getAnhnv().equals("")){
           }else      Picasso.get().load(nhanVien.getAnhnv()).into(imgnhanvien);


            themnv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = gmailnv.getText().toString();
                    String pass1 = pass.getText().toString();
                    if (!TextUtils.isEmpty(email)) {
                        if (email.contains("@gmail.com")) {
                            String email1 = email.substring(email.length() - 10);
                            if (email1.equals("@gmail.com")) {
                              //  if (!TextUtils.isEmpty(pass1)) {
                                    if(sdtnv.getText().toString().length()==10) {

                                                UploadTask uploadTask = mountainsRef.putBytes(Img(imgnhanvien));//thêm ảnh lên sever

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

                                                                user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            HashMap<String, Object> hashMap = new HashMap<>();
                                                                            hashMap.put("tenNV", tennv.getText().toString());
                                                                            hashMap.put("mail", gmailnv.getText().toString());
                                                                            hashMap.put("sdtnv", sdtnv.getText().toString());
                                                                            hashMap.put("anhnv", downloadUri.toString());

                                                                            data.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        Toast.makeText(ThemNhanVienActivity.this, "Sửa Nhân Viên Thành Công", Toast.LENGTH_SHORT).show();
                                                                                        finish();

                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                });
                                                            }



                                                    }
                                                });


                                    }else
                                        Toast.makeText(ThemNhanVienActivity.this, "số điện thoại phải đủ 10 số", Toast.LENGTH_SHORT).show();
                               // } else
                                    //Toast.makeText(ThemNhanVienActivity.this, "Vui lòng nhập PassWord", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(ThemNhanVienActivity.this, "Email phải có đuôi là @gmail.com", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(ThemNhanVienActivity.this, "không phải email ", Toast.LENGTH_SHORT).show();


                    } else
                        Toast.makeText(ThemNhanVienActivity.this, "Vui lòng nhập Email", Toast.LENGTH_SHORT).show();
                }
            });
        }else {


                            themnv.setText("Thêm Nhân Viên");
                            themnv.setPadding(30,20,30,30);

                            themnv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String email = gmailnv.getText().toString();
                                    String pass1 = pass.getText().toString();
                                    if (!TextUtils.isEmpty(email)) {
                                    if (email.contains("@gmail.com")) {
                                        String email1 = email.substring(email.length() - 10);
                                        if (email1.equals("@gmail.com")) {
                                            if (!TextUtils.isEmpty(pass1)) {
                                                if(pass1.length()>5){
                                                if(sdtnv.getText().toString().length()==10) {
                                                    File file=new File("/data/data/com.example.doan/shared_prefs/com.google.firebase.auth.api.Store." +
                                                            "W0RFRkFVTFRd+MToxMDU5MDc0NDA1NzExOmFuZHJvaWQ" +
                                                            "6ZWRiMWIxOTNiYTJiOGZlZGU3YzEzYQ.xml");



                                                        try {
                                                            FileInputStream inputStream=new FileInputStream(file);
                                                            DataInputStream in = new DataInputStream(inputStream);
                                                            BufferedReader br = new BufferedReader(
                                                                    new InputStreamReader(in));


                                                         String line;
                                                             while ((line=br.readLine())!=null){
                                                                 a.add(line);
                                                             }


                                                          //  Toast.makeText(getApplicationContext(), a+"", Toast.LENGTH_SHORT).show();
                                                        }catch (FileNotFoundException e) {
                                                            e.printStackTrace();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }

                                           FirebaseAuth auth=FirebaseAuth.getInstance();
                                           auth.createUserWithEmailAndPassword(email,pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                               @Override
                                               public void onComplete(@NonNull Task<AuthResult> task) {
                                                   if(task.isSuccessful()){
                                                       FirebaseUser user1=auth.getCurrentUser();
                                                       DatabaseReference data = FirebaseDatabase.getInstance()
                                                               .getReference().child("NhaHang")
                                                               .child(intent.getStringExtra("id_nhahang"))
                                                               .child("DS_NhanVien").child(user1.getUid());

                                                       StorageReference mountainsRef = storageReference.child(intent.getStringExtra("id_nhahang") + "/imgNhanVien/" +user1.getUid());

                                                       UploadTask uploadTask = mountainsRef.putBytes(Img(imgnhanvien));//thêm ảnh lên sever

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

                                                                   FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                                   NhanVien nhanVien = new NhanVien(tennv.getText().toString()
                                                                           , sdtnv.getText().toString(), user1.getUid()
                                                                           , downloadUri.toString(), Integer.parseInt(intent.getStringExtra("chuc_vu")), gmailnv.getText().toString());
                                                                data.setValue(nhanVien).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                       @Override
                                                                       public void onComplete(@NonNull Task<Void> task) {
                                                                           if (task.isSuccessful()) {
                                                                               Toast.makeText(ThemNhanVienActivity.this, "Thêm Nhân Viên Thành Công", Toast.LENGTH_SHORT).show();
                                                                                auth.signOut();
                                                                               try {
                                                                                   FileOutputStream outputStream=new FileOutputStream(file);
                                                                                   for (String a:a
                                                                                        ) {outputStream.write(a.getBytes());
                                                                                       outputStream.write("\n".getBytes());

                                                                                   }
                                                                               } catch (FileNotFoundException e) {
                                                                                   e.printStackTrace();
                                                                               } catch (IOException e) {
                                                                                   e.printStackTrace();
                                                                               }
                                                                               finish();
                                                                           }
                                                                       }
                                                                   });

                                                               }

                                                           }

                                                       });
                                                   }
                                               }
                                           });



                                                }else
                                                    Toast.makeText(ThemNhanVienActivity.this, "số điện thoại phải đủ 10 số", Toast.LENGTH_SHORT).show();
                                                }else  Toast.makeText(ThemNhanVienActivity.this, "PassWord phải từ 6 kí tự trở lên", Toast.LENGTH_SHORT).show();
                                            } else
                                                Toast.makeText(ThemNhanVienActivity.this, "Vui lòng nhập PassWord", Toast.LENGTH_SHORT).show();
                                        } else
                                            Toast.makeText(ThemNhanVienActivity.this, "Email phải có đuôi là @gmail.com", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(ThemNhanVienActivity.this, "không phải email ", Toast.LENGTH_SHORT).show();


                                } else
                                    Toast.makeText(ThemNhanVienActivity.this, "Vui lòng nhập Email", Toast.LENGTH_SHORT).show();
                                }
                            });



        }
            imgnhanvien.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 100);

                        }
                    });
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
           imgnhanvien.setImageURI(uridata);

        }


        //Toast.makeText(ThemNhanVienActivity.this,imgnhanvien.getDrawable().toString(),Toast.LENGTH_LONG).show();
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
