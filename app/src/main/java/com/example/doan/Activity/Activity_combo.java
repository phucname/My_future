package com.example.doan.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.Adapter.Adaptermenu;
import com.example.doan.Adapter.AdapterrecymenuMA;
import com.example.doan.Class.MonAn;
import com.example.doan.Class.classHoaDon;
import com.example.doan.Class.class_combo;
import com.example.doan.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Activity_combo extends AppCompatActivity {
ListView listView;
Adaptermenu adaptermenu;
ArrayList<MonAn>monAnArrayList;
Button updatecombo;
TextView textView_gia;
EditText gia,ten;
    FirebaseStorage storage ;
    DatabaseReference myRef;
    ImageView imageViewthem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combo);
        listView=findViewById(R.id.list_combo);
        updatecombo=findViewById(R.id.button_update_combo);
        textView_gia=findViewById(R.id.text_tonggia_combo);
        gia=findViewById(R.id.edittext_giacombo);
        ten=findViewById(R.id.edittext_tencombo);
        imageViewthem=findViewById(R.id.img_them_combo);
        storage = FirebaseStorage.getInstance();//khởi tạo
         Intent intent=getIntent();
        myRef = FirebaseDatabase.getInstance().getReference().child("NhaHang").child(intent.getStringExtra("id_nhahang")).child("DS_MonAn");
        StorageReference storageReference=storage.getReferenceFromUrl("gs://doan-f52ef.appspot.com/");//url của trang storage

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference()
                .child("NhaHang").child(intent.getStringExtra("id_nhahang"));
        databaseReference.child("DS_MonAn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               monAnArrayList=new ArrayList<>();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    monAnArrayList.add(new MonAn(snapshot1.child("tenmon").getValue().toString(),
                            snapshot1.child("gia").getValue().toString(),
                            snapshot1.child("ghichu").getValue().toString(),
                            snapshot1.child("imgmon").getValue().toString(),
                            snapshot1.child("id").getValue().toString()
                    ,   snapshot1.child("loai_mon").getValue().toString(),0));


                    // Toast.makeText(getActivity(),arrayListtest.get(0).getGia()+"",Toast.LENGTH_SHORT).show();
                } adaptermenu=new Adaptermenu(getApplicationContext(),R.layout.listmenu,monAnArrayList,textView_gia);
                listView.setAdapter(adaptermenu);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        updatecombo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference=myRef.push();//taoj khoa
                StorageReference mountainsRef = storageReference.child(intent.getStringExtra("id_nhahang")+"/imgMonAn/"+databaseReference.getKey());

                UploadTask uploadTask = mountainsRef.putBytes(Img(imageViewthem));//thêm ảnh lên sever

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
                        if(task.isSuccessful()){
                            Uri uri=task.getResult();
                            monAnArrayList= (ArrayList<MonAn>) adaptermenu.getMonAnList();
                            DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference()
                                    .child("NhaHang").child(getIntent().getStringExtra("id_nhahang"));
                            DatabaseReference data=databaseReference1.child("DS_ComBo").push();
                            class_combo  class_combo=new class_combo(data.getKey(),
                                    gia.getText().toString(),ten.getText().toString()
                            ,uri.toString(),2);
                            data.setValue(class_combo);
                            int i=0;
                            for (MonAn monan:monAnArrayList
                            ) {
                                if(monan.getCheck()){
                                    classHoaDon classHoaDon=new classHoaDon(monan.getId(),adaptermenu.getSl().get(i)+"",null);
                                    databaseReference1.child("ChiTiet_DSComBo").child(data.getKey()).child(classHoaDon.getId_mon()).setValue(classHoaDon);
                                    i++;
                                }


                            }
                            finish();
                        }
                    }
                });

//                FirebaseAuth auth=FirebaseAuth.getInstance();
//                auth.sendPasswordResetEmail("hongphuc4701@gmail.com").addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(Activity_combo.this, "Thành Công", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });


            }
        });


        imageViewthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,100);

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
            imageViewthem.setImageURI(uridata);

        }


        // Toast.makeText(ThemMonAn.this,imageViewthem.getDrawable().toString(),Toast.LENGTH_LONG).show();
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