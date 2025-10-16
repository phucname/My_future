package com.example.doan.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.doan.Adapter.Adapter_Quangcao;
import com.example.doan.Adapter.Adapter_hienquangcao;
import com.example.doan.Adapter.Adapter_select_imgs;
import com.example.doan.Class.MonAn;
import com.example.doan.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class select_nhieuhinh extends AppCompatActivity {

Button danglem;
FloatingActionButton button;
RecyclerView recyclerView,recyclerView_quangcao_cu;
Adapter_select_imgs adapter_select_imgs;
private  static  final int Read_Permi=101;
ArrayList<Uri> uris=new ArrayList<>();
    ArrayList<String>arrayList_photo;
    Adapter_hienquangcao adapter_quangcao;
    int gtthaydoianh;//kiểm tra mảng quảng cáo có bị xóa k
    ArrayList<String>id_img,id_img_xoa;//aray chứa id củ các img quảng cáo gồm củ vad sao khi xóa
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_nhieuhinh);
        button=findViewById(R.id.button_selecthinh);
        recyclerView=findViewById(R.id.recyclerview_seclehinh);
        danglem=findViewById(R.id.danglen);

        Intent intent=getIntent();//gọi lấy id nhà hàng

        id_img_xoa=new ArrayList<>();
         databaseReference=FirebaseDatabase.getInstance().getReference()
                .child("NhaHang").child(intent.getStringExtra("id_nhahang")).
                child("DS_ImgQuangCao");//khai báo tới nút quangcao trên firebase
            if(ContextCompat.checkSelfPermission(select_nhieuhinh.this,Manifest.permission.READ_EXTERNAL_STORAGE
            )!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(select_nhieuhinh.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Read_Permi);
            };
            adhinh();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //requestperision();
//                uris.clear();
//                adapter_select_imgs.notifyDataSetChanged();
                Intent intent=new Intent();
                intent.setType("image/*");
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR2){
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                }
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Seclet"),1);

            }


        });
        danglem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              FirebaseStorage  storage = FirebaseStorage.getInstance();//khởi tạo
                StorageReference storageReference=storage.getReferenceFromUrl("gs://doan-f52ef.appspot.com/");//url của trang storage

                if(gtthaydoianh!=arrayList_photo.size()){
                    for (String id: id_img_xoa){
                        databaseReference.child(id).removeValue();
                        StorageReference mountainsRef = storageReference
                                .child(intent.getStringExtra("id_nhahang")+"/QuangCao/"+id);
                        mountainsRef.delete();
                    }
                }

              if(uris.size()>0){
                  for (Uri uri:uris){
                      DatabaseReference databaseReferencenew=databaseReference.push();
                      StorageReference mountainsRef = storageReference
                              .child(intent.getStringExtra("id_nhahang")+"/QuangCao/"
                              +databaseReferencenew.getKey());

                      button.setImageURI(uri);
                      UploadTask uploadTask = mountainsRef.putBytes(Img(button));//thêm ảnh lên sever

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
                              Uri dow=task.getResult();
                              databaseReferencenew.setValue(dow.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                  @Override
                                  public void onComplete(@NonNull Task<Void> task) {
                                      if(task.isSuccessful()){
                                          Intent intent1=new Intent(select_nhieuhinh.this,TrangChuActivuty.class);
                                          startActivity(intent1);
                                      }
                                  }
                              });
                          }
                      });

                  }
              }else {
                  Intent intent1=new Intent(select_nhieuhinh.this,TrangChuActivuty.class);
                  startActivity(intent1);
              }
    }
    });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==Activity.RESULT_OK){

            adapter_select_imgs=new Adapter_select_imgs(uris);
            GridLayoutManager gridLayoutManager=new GridLayoutManager(this,1);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(adapter_select_imgs);
            if(data.getClipData()!=null){
                int x=data.getClipData().getItemCount();
                for(int i=0;i<x;i++){
                    uris.add(data.getClipData().getItemAt(i).getUri());
                   // Toast.makeText(this, data.getClipData().getItemAt(i).getUri()+"th1", Toast.LENGTH_SHORT).show();
                }
                adapter_select_imgs.notifyDataSetChanged();

            }else if(data.getData()!=null){
                Uri uri=data.getData();
                uris.add(Uri.parse(uri.toString()));
              //  Toast.makeText(this, Uri.parse(img)+"th2", Toast.LENGTH_SHORT).show();
                adapter_select_imgs.notifyDataSetChanged();
            }
        }
    }
    private  void adhinh(){


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList_photo=new ArrayList<>();
                id_img=new ArrayList<>();

                recyclerView_quangcao_cu=findViewById(R.id.recyclerview_quangcao_cu);
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    arrayList_photo.add(dataSnapshot.getValue().toString());
                    id_img.add(dataSnapshot.getKey());
                }
                gtthaydoianh=arrayList_photo.size();
                adapter_quangcao=new Adapter_hienquangcao(arrayList_photo);
                GridLayoutManager gridLayoutManager=new GridLayoutManager(select_nhieuhinh.this,1);
                recyclerView_quangcao_cu.setLayoutManager(gridLayoutManager);
                recyclerView_quangcao_cu.setAdapter(adapter_quangcao);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.Xoa:
      if(adapter_quangcao.getDieukien()=="ok"){
          arrayList_photo.remove(adapter_quangcao.getPosition_item());
          adapter_quangcao.notifyDataSetChanged();
          id_img_xoa.add(id_img.get(adapter_quangcao.getPosition_item()));
          Toast.makeText(this, gtthaydoianh+"", Toast.LENGTH_SHORT).show();
      }else {
          uris.remove(adapter_select_imgs.getPositon_select_quangcao());
          adapter_select_imgs.notifyDataSetChanged();
      }

                break;

        }

        return super.onContextItemSelected(item);
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