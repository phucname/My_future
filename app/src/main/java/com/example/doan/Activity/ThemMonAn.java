package com.example.doan.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.Adapter.adapter_spienr;
import com.example.doan.Class.MonAn;
import com.example.doan.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kotlinx.coroutines.Delay;

public class ThemMonAn extends AppCompatActivity {

    private EditText tenmon,gia,ghichu;
    Button themmon;
    ImageView imageViewthem;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage storage ;
    MonAn monAn;
    TextView title;
    StorageReference mountainsRef;
    Spinner spinner;   FirebaseUser user;
    String loai="null";
    ArrayList<String>arrayListspienr;
    ArrayList<String>arrayList_keyloai;
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_mon_an);
        AnhXa();
        spinner=findViewById(R.id.spinner_loaimon);
        storage = FirebaseStorage.getInstance();//khởi tạo
        StorageReference storageReference=storage.getReferenceFromUrl("gs://doan-f52ef.appspot.com/");//url của trang storage
       //thư mục lưu
      user  = FirebaseAuth.getInstance().getCurrentUser();
        Bundle bundle=getIntent().getExtras();
        Intent intent=getIntent();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("NhaHang").child(intent.getStringExtra("id_nhahang")).child("DS_MonAn");
       title=findViewById(R.id.text_title_themnom);
           setSpinner();
        if(bundle.get("classsua")!=null) {
         monAn = (MonAn) bundle.get("classsua");
            tenmon.setText(monAn.getTenmon());
            gia.setText(monAn.getGia());
            ghichu.setText(monAn.getGhichu());
            Picasso.get().load(monAn.getImgmon()).into(imageViewthem);
        }else {
            title.setText("Thêm Món Ăn");
            themmon.setText("Thêm Món");
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loai=arrayList_keyloai.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

            themmon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!TextUtils.isEmpty(tenmon.getText().toString())){
                        if(!TextUtils.isEmpty(gia.getText().toString())){
                            DatabaseReference databaseReference=myRef.push();
                            if(bundle.get("classsua")!=null) {
                                mountainsRef  = storageReference
                                        .child(intent.getStringExtra("id_nhahang") + "/imgMonAn/" + monAn.getId());}
                            else {//taoj khoa
                                mountainsRef = storageReference.child(intent.getStringExtra("id_nhahang") +
                                        "/imgMonAn/" + databaseReference);
                            }
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
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        if (bundle.get("classsua") != null) {
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            hashMap.put("tenmon", tenmon.getText().toString());
                                            hashMap.put("gia", gia.getText().toString());
                                            hashMap.put("ghichu", ghichu.getText().toString());
                                            hashMap.put("imgmon", downloadUri.toString());
                                            hashMap.put("loai_mon",loai);
                                            myRef.child(monAn.getId()).updateChildren(hashMap)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(),
                                                                "Sửa Món Thành Công", Toast.LENGTH_SHORT).show();
                                                        SystemClock.sleep(1000);
                                                        finish();
                                                    }
                                                }
                                            });
                                        } else {

                                            MonAn monAn = new MonAn(tenmon.getText().toString(),
                                                    gia.getText().toString(),
                                                    ghichu.getText().toString(),
                                                    String.valueOf(downloadUri)
                                                    , "",loai,2
                                            );


                                            monAn.setId(databaseReference.getKey());
                                            databaseReference.setValue(monAn).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(ThemMonAn.this, "Thêm Món Thành Công",
                                                            Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            });

                                        }
                                    }
                                }



                            });

                        }else Toast.makeText(ThemMonAn.this, "Giá Món Ăn không được trống!", Toast.LENGTH_SHORT).show();
                    }else Toast.makeText(ThemMonAn.this, "Tên Món Ăn không được trống!", Toast.LENGTH_SHORT).show();





    }
        });




        // Get the data from an ImageView as bytes



  imageViewthem.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
         Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
          startActivityForResult(intent,100);

      }
  });
    }
private  void setSpinner(){
arrayListspienr=new ArrayList<>();
arrayList_keyloai=new ArrayList<>();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference()
                .child("NhaHang").child(user.getUid()).child("Loai_Menu");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot snapshot1:snapshot.getChildren()){
                   arrayListspienr.add(snapshot1.getValue().toString());
                   arrayList_keyloai.add(snapshot1.getKey());
                 //  Toast.makeText(ThemMonAn.this, snapshot1.getValue().toString(), Toast.LENGTH_SHORT).show();

               }

                if(arrayListspienr.size()>0){
                    adapter_spienr adapter_spienr=new adapter_spienr(ThemMonAn.this,R.layout.itme_spiner,arrayListspienr,"Loại ");
                    spinner.setAdapter(adapter_spienr);
                    loai=arrayList_keyloai.get(0);
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    private void AnhXa() {
       imageViewthem=findViewById(R.id.imgthemhinh);
        tenmon=findViewById(R.id.editthemtenmon);
        gia=findViewById(R.id.editthemgia);
        ghichu=findViewById(R.id.editthemghichu);
        themmon=findViewById(R.id.buttonthem);
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
        Intent intent=new Intent(ThemMonAn.this,MianActivityBanac.class);
        intent.putExtra("id_nhahang",user.getUid());
        intent.putExtra("chucvu","1");
        intent.putExtra("them","mon");
        startActivity(intent);
    }
}