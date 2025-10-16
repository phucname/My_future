package com.example.doan.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.doan.Adapter.adapter_spienr;
import com.example.doan.Class.NhaHang;
import com.example.doan.Class.NhanVien;
import com.example.doan.Class.class_TTVN;
import com.example.doan.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TaoNhaHangActivity extends AppCompatActivity {
EditText tennh,diachinh,sdt;
Button bttaonh;
ImageView imgnhahang;
DatabaseReference data;
FirebaseStorage storage;
Spinner spinner ,spinner_huyen,spinner_xa;
String tinh;
String huyen,xa;
ArrayList<class_TTVN>list_tp,list_huyen,list_xa;
ArrayList<String>tentinh,tenhuyen,tenxa;
    Intent intent;
adapter_spienr adapter_spienr_tp,adapter_spienr_huyen,adapter_spienr_xa;
DatabaseReference getData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao_nha_hang);
        tennh=findViewById(R.id.edittext_tennhahang);
        diachinh=findViewById(R.id.edittext_diachi);
        sdt=findViewById(R.id.edittext_sdt);
        imgnhahang=findViewById(R.id.img_nhahang);
        getData=FirebaseDatabase.getInstance().getReference().child("TT_VN");
        bttaonh=findViewById(R.id.button_taonhahang);
        spinner=findViewById(R.id.spinner);
        spinner_huyen=findViewById(R.id.spinner_huyen);
        spinner_xa=findViewById(R.id.spinner_xa);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            bttaonh.setVisibility(View.INVISIBLE);
        }
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
               adapter_spienr_tp=new adapter_spienr(TaoNhaHangActivity.this,R.layout.selct_spiner,tentinh,"TP,Tỉnh");
               spinner.setAdapter(adapter_spienr_tp);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  tinh= list_tp.get(position).getId();
               // Toast.makeText(TaoNhaHangActivity.this, list_tp.get(position).getId(), Toast.LENGTH_SHORT).show();
              getData.child("Huyen").child( list_tp.get(position).getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {

                           list_huyen=new ArrayList<>();
                           tenhuyen=new ArrayList<>();
                           for (DataSnapshot snapshot1:snapshot.getChildren()){
                               list_huyen.add(new class_TTVN(snapshot1.child("id").getValue().toString()
                                       ,snapshot1.child("ten").getValue().toString()));
                               tenhuyen.add(snapshot1.child("ten").getValue().toString());
                           }
                           adapter_spienr_huyen=new adapter_spienr(TaoNhaHangActivity.this,R.layout.selct_spiner,tenhuyen,"Quận,Huyện");
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
             huyen= list_huyen.get(position).getId();
             getData.child("Xa").child( list_tp.get(position).getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {

                     list_xa=new ArrayList<>();
                     tenxa=new ArrayList<>();
                     for (DataSnapshot snapshot1:snapshot.getChildren()){
                         list_xa.add(new class_TTVN(snapshot1.child("id").getValue().toString()
                                 ,snapshot1.child("ten").getValue().toString()));
                         tenxa.add(snapshot1.child("ten").getValue().toString());
                     }
                     adapter_spienr_xa=new adapter_spienr(TaoNhaHangActivity.this,R.layout.selct_spiner,tenxa,"Phường,Xã");
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
             xa=list_xa.get(position).getId();
         }

         @Override
         public void onNothingSelected(AdapterView<?> parent) {

         }
     });
        storage = FirebaseStorage.getInstance();//khởi tạo
        StorageReference storageReference=storage.getReferenceFromUrl("gs://doan-f52ef.appspot.com/");//url của trang storage
        imgnhahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,100);
            }
        });


     intent=getIntent();
        if(intent.getStringExtra("edit")!=null){

                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference()
                            .child("NhaHang").child(intent.getStringExtra("id_nhahang")).child("TT_NhaHang");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            tennh.setText(snapshot.child("ten_nhahang").getValue().toString());
                            sdt.setText(snapshot.child("_sdt_nhahangi").getValue().toString());
                            diachinh.setText(snapshot.child("diachi_nhahang").getValue().toString());

                            Picasso.get().load(snapshot.child("mg_nhahang").getValue().toString()).into(imgnhahang);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    bttaonh.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if(intent.getStringExtra("id_nhahang").equals(user.getUid())){


                                if(!TextUtils.isEmpty(tennh.getText().toString())){
                                    if(!TextUtils.isEmpty(diachinh.getText().toString())){

                                        if(sdt.length()==10) {
                                            StorageReference mountainsRef = storageReference.child(intent.getStringExtra("id_nhahang") + "/" + "imgNH");

                                            UploadTask uploadTask = mountainsRef.putBytes(Img(imgnhahang));//thêm ảnh lên sever
                                            Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                                                        Uri uri = task.getResult();
                                                        HashMap<String, Object> hashMap = new HashMap<>();
                                                        hashMap.put("ten_nhahang", tennh.getText().toString());
                                                        hashMap.put("_sdt_nhahangi", sdt.getText().toString());
                                                        hashMap.put("diachi_nhahang",diachinh.getText().toString());
                                                        hashMap.put("mg_nhahang", uri.toString());
                                                        hashMap.put("Tinh",tinh);
                                                        hashMap.put("Huyen",huyen);
                                                        hashMap.put("Xa",xa);

                                                        databaseReference.updateChildren(hashMap);
                                                    }
                                                }
                                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(TaoNhaHangActivity.this, "Sửa Thành Công ", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                }

                                            });

                                        }else
                                            Toast.makeText(TaoNhaHangActivity.this, "số điện thoại phải đủ 10 số", Toast.LENGTH_SHORT).show();

                                    }else Toast.makeText(TaoNhaHangActivity.this, "Địa chỉ nhà hàng không dược trống!", Toast.LENGTH_SHORT).show();
                                }
                                else Toast.makeText(TaoNhaHangActivity.this, "Tên nhà hàng không dược trống!", Toast.LENGTH_SHORT).show();
                            }
                                else Toast.makeText(TaoNhaHangActivity.this, "Chỉ có quản lý mới được sửa thông tin nhà hàng!", Toast.LENGTH_SHORT).show();
                            }





                    });
        }

      else {
            bttaonh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(sdt.length()==10) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        StorageReference mountainsRef = storageReference.child(user.getUid() + "/" + "imgNH");

                        UploadTask uploadTask = mountainsRef.putBytes(Img(imgnhahang));//thêm ảnh lên sever
                        Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                                    Uri uri = task.getResult();
                                    data = FirebaseDatabase.getInstance().getReference().child("NhaHang").child(user.getUid());
                                    NhaHang nhaHang = new NhaHang(tennh.getText().toString(), diachinh.getText().toString(),
                                            sdt.getText().toString(), uri.toString(),
                                            huyen,tinh,xa);
                                    data.child("TT_NhaHang").setValue(nhaHang);


                                    Toast.makeText(TaoNhaHangActivity.this, "Bạn Đã Có Một Nhà Hàng Cho Mình Rồi Hãy Xây Dững nó đi nào", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(TaoNhaHangActivity.this, TrangChuActivuty.class);

                                    File myInternalFile;
                                    ContextWrapper contextWrapper = new ContextWrapper(TaoNhaHangActivity.this);
                                    //Tạo (Hoặc là mở file nếu nó đã tồn tại) Trong bộ nhớ trong có thư mục là ThuMucCuaToi.
                                    File directory = contextWrapper.getDir("TK", Context.MODE_PRIVATE);//tên Thư Mục
                                    myInternalFile = new File(directory, "TinhTrang.txt");
                                    try {
                                        //Mở file
                                        FileOutputStream fos = new FileOutputStream(myInternalFile);
                                        //Ghi dữ liệu vào file
                                        fos.write(("1" + "," + user.getUid()).getBytes());
                                        fos.close();

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    //  progressDialog.dismiss();
                                    //Toast.makeText(DangNhapActivity.this, snapshot.child("chucVuNV").getValue().toString()+"", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);

                                }


                            }
                        });
                    }else Toast.makeText(TaoNhaHangActivity.this, "số điện thaoij phải đủ 10 số", Toast.LENGTH_SHORT).show();
                }
            });
        }
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
            imgnhahang.setImageURI(uridata);

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

    @Override
    public void onBackPressed() {
        if(intent.getStringExtra("edit")==null){
            FirebaseAuth auth=FirebaseAuth.getInstance();
            auth.signOut();
        }
        super.onBackPressed();
    }
}