package com.example.doan.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.Adapter.Adapter_Giohang;
import com.example.doan.Adapter.Adaptermenu;
import com.example.doan.Adapter.adapter_combo_giohang;
import com.example.doan.Adapter.adapter_dsmon_themcombo;
import com.example.doan.Adapter.adapter_spienr;
import com.example.doan.Class.MonAn;
import com.example.doan.Class.classHoaDon;
import com.example.doan.Class.class_combo;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Them_combo extends AppCompatActivity {
    ListView listView;
    Adaptermenu adaptermenu;
    ArrayList<MonAn> monAnArrayList;
    ArrayList<MonAn> updatemonAnArrayList;
    ArrayList<classHoaDon> slupdate;
    Button updatecombo;
    TextView textView_gia;
    EditText gia,ten;
    FirebaseStorage storage ;
    DatabaseReference myRef;
    ImageView imageViewthem;
    Spinner spinner;
    DatabaseReference databaseReference;
    ArrayList<String>arrayList_loai;
    ImageView img_dsthemcombo;
    ImageView img;
    int tong=0;
TextView text_slmon_dathem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_combo);
       listView=findViewById(R.id.list_them_combo);
        Intent intent=getIntent();
        Bundle bundle=getIntent().getExtras();

        spinner=findViewById(R.id.spinner_them_combo);
        text_slmon_dathem=findViewById(R.id.text_sl_mon_dathem);
       img_dsthemcombo=findViewById(R.id.img_danhsach_them_combo);
        databaseReference= FirebaseDatabase.getInstance().getReference()
                .child("NhaHang").child(intent.getStringExtra("id_nhahang"));
        getloai();

            databaseReference.child("DS_MonAn").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    monAnArrayList = new ArrayList<>();
                    updatemonAnArrayList = new ArrayList<>();
                    slupdate = new ArrayList<>();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        MonAn monAn = new MonAn(snapshot1.child("tenmon").getValue().toString(),
                                snapshot1.child("gia").getValue().toString(),
                                snapshot1.child("ghichu").getValue().toString(),
                                snapshot1.child("imgmon").getValue().toString(),
                                snapshot1.child("id").getValue().toString()
                                , "",Integer.parseInt(snapshot1.child("loai_giamgia").getValue().toString()));
                     if(snapshot1.child("loai_mon").getValue().toString().equals("null")){
                         monAn.setLoai_mon("null");
                     }else {
                         databaseReference.child("Loai_Menu").child(snapshot1.child("loai_mon").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot snapshot) {
                                 monAn.setLoai_mon(snapshot.getValue().toString());
                                 //  Toast.makeText(Them_combo.this, monAn.getLoai_mon()+"", Toast.LENGTH_SHORT).show();
                             }

                             @Override
                             public void onCancelled(@NonNull DatabaseError error) {

                             }
                         });
                     }

                        if(bundle.getSerializable("class_combo")!=null) {
                            class_combo combo = (class_combo) bundle.getSerializable("class_combo");
                            DatabaseReference d = databaseReference.child("ChiTiet_ComBo")
                                    .child(combo.getId_combo()).child(snapshot1.child("id").getValue().toString());
                            d.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        monAn.setCheck(true);
                                        monAnArrayList.add(monAn);
                                        updatemonAnArrayList.add(monAn);
                                        slupdate.add(new classHoaDon(snapshot.getKey(), snapshot.child("soluong").getValue().toString()));


                                    } else {
                                        monAn.setCheck(false);
                                        monAnArrayList.add(monAn);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }else {
                            monAn.setCheck(false);
                            monAnArrayList.add(monAn);
                        }


                        // Toast.makeText(getActivity(),arrayListtest.get(0).getGia()+"",Toast.LENGTH_SHORT).show();
                    }
                    adaptermenu = new Adaptermenu(getApplicationContext(), R.layout.listmenu, monAnArrayList, text_slmon_dathem);
                   if(bundle.getSerializable("class_combo")!=null){
                       adaptermenu.setMonAnArrayList_dathem(updatemonAnArrayList);
                       adaptermenu.setArrayListsl(slupdate);
                   }
                    listView.setAdapter(adaptermenu);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        img_dsthemcombo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MonAn>ds_combo_them=adaptermenu.getMonAnArrayList_dathem();
                Dialog dialog=new Dialog(Them_combo.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_them_combo);
                Window window=dialog.getWindow();
                if(window==null){
                    return;
                }
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                //window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams windowaatriss= window.getAttributes();
                windowaatriss.gravity= Gravity.CENTER;

                ListView listView=dialog.findViewById(R.id.list_item_dialog);
                 TextView title=dialog.findViewById(R.id.text_tite);
                 Button them=dialog.findViewById(R.id.button_them_combo_dialog);
                 title.setText("Danh Sách Món ComBo");
               adapter_dsmon_themcombo adapter_dsmon_themcombog=new adapter_dsmon_themcombo(Them_combo.this,
                        adaptermenu.getArrayListsl(),R.layout.item_giohang
                ,ds_combo_them,adaptermenu);
                listView.setAdapter(adapter_dsmon_themcombog);
                them.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       tong=0;
                        ArrayList<MonAn>ds_combo_them=adapter_dsmon_themcombog.getMonAnArrayList();
                        if(ds_combo_them.size()>1){
                            dialog.dismiss();
                            ArrayList<classHoaDon>slmon= (ArrayList<classHoaDon>) adapter_dsmon_themcombog.getMonAnList();
                            Dialog dialog1=new Dialog(Them_combo.this);

                            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog1.setContentView(R.layout.dialog_themcombo);
                            Window window=dialog1.getWindow();
                            if(window==null){
                                return;
                            }
                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                            //window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            WindowManager.LayoutParams windowaatriss= window.getAttributes();
                            windowaatriss.gravity= Gravity.CENTER;
                            TextView textView_gia=dialog1.findViewById(R.id.text_tonggia_combo);
                            EditText ten=dialog1.findViewById(R.id.edittext_tencombo);
                            EditText gianew=dialog1.findViewById(R.id.edittext_giacombo);
                            Button them=dialog1.findViewById(R.id.button_themcombo);
                            for (int i=0;i<ds_combo_them.size();i++){
                                tong=tong+(Integer.parseInt(ds_combo_them.get(i).getGia())*
                                        Integer.parseInt(slmon.get(i).getSoluong()));
                            }
                            img=dialog1.findViewById(R.id.img_them_combo);
                            img.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(intent,100);

                                }
                            });
                            MianActivityBanac mianActivityBanac1=new MianActivityBanac();
                            textView_gia.setText(mianActivityBanac1.VND(tong));
                            them.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(!TextUtils.isEmpty(ten.getText().toString())){
                                        if(!TextUtils.isEmpty(gianew.getText().toString())){
                                            if(tong>Integer.parseInt(gianew.getText().toString())){



                                                FirebaseStorage storage = FirebaseStorage.getInstance();//khởi tạo
                                                Intent intent=getIntent();
                                                DatabaseReference myRef=databaseReference.child("DS_ComBo").push() ;
                                                StorageReference storageReference=storage.getReferenceFromUrl("gs://doan-f52ef.appspot.com/");//url của trang storage
                                                StorageReference mountainsRef;
                                                class_combo combo= (class_combo) bundle.getSerializable("class_combo");
                                                if(bundle.getSerializable("class_combo")!=null){
                                                    databaseReference.child("ChiTiet_ComBo").child(combo.getId_combo()).removeValue();
                                                    mountainsRef= storageReference.child(intent.getStringExtra("id_nhahang")+"/imgComBo/"+combo.getId_combo());
                                                }else{
                                                    mountainsRef = storageReference.child(intent.getStringExtra("id_nhahang")+"/imgComBo/"+myRef.getKey());
                                                }



                                                UploadTask uploadTask = mountainsRef.putBytes(Img(img));//thêm ảnh lên sever

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

                                                            if(bundle.getSerializable("class_combo")!=null){
                                                                HashMap<String, Object> hashMap = new HashMap<>();
                                                                hashMap.put("ten_combo", ten.getText().toString());
                                                                hashMap.put("gia_combo", gianew.getText().toString());
                                                                hashMap.put("img_combo", uri.toString());
                                                                databaseReference.child("DS_ComBo").child(combo.getId_combo()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful()){
                                                                            Toast.makeText(Them_combo.this, "Update ComBo Thành Công", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                            }else {
                                                                class_combo class_combo=new class_combo(myRef.getKey(),
                                                                        gianew.getText().toString(),ten.getText().toString()
                                                                        ,uri.toString(),2);
                                                                myRef.setValue(class_combo);
                                                            }



                                                            int i=0;
                                                            DatabaseReference chitietcombo=databaseReference.child("ChiTiet_ComBo");
                                                            for (MonAn monan:ds_combo_them
                                                            ) { classHoaDon classHoaDon=new classHoaDon(monan.getId(),slmon.get(i).getSoluong()+"");
                                                                if(bundle.getSerializable("class_combo")!=null){
                                                                    chitietcombo.child(combo.getId_combo()).child(classHoaDon.getId_mon()).setValue(classHoaDon);
                                                                }else {
                                                                    chitietcombo.child(myRef.getKey()).child(classHoaDon.getId_mon()).setValue(classHoaDon);
                                                                }
                                                                i++;
                                                            }

                                                            finish();
                                                        }
                                                    }
                                                });
                                            }else
                                                Toast.makeText(Them_combo.this, "Giá Mới không thể cao hơn giá củ!", Toast.LENGTH_SHORT).show();

                                        } else Toast.makeText(Them_combo.this,"Giá Combo không dược trống!", Toast.LENGTH_SHORT).show();
                                    }else Toast.makeText(Them_combo.this, "Tên Combo  không dược trống!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog1.show();
                        }else Toast.makeText(Them_combo.this, "Số lượng món " +
                                "trong một combo phải lớn hơn 1", Toast.LENGTH_SHORT).show();


                    }
                });
                dialog.show();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              if(!arrayList_loai.get(position).equals("All")){
                  ArrayList<MonAn> newmonAnArrayList=new ArrayList<>();
                  String loai=arrayList_loai.get(position);

                  for (MonAn monAn:monAnArrayList){
                      if(monAn.getLoai_mon().equals(loai)){
                        //  Toast.makeText(Them_combo.this, monAn.getCheck()+"", Toast.LENGTH_SHORT).show();
                          newmonAnArrayList.add(monAn);
                      }
                  }

                  adaptermenu.setnewlist(newmonAnArrayList);
              }else adaptermenu.setnewlist(monAnArrayList);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getloai() {
        databaseReference.child("Loai_Menu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList_loai = new ArrayList<>();
                arrayList_loai.add("All");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    arrayList_loai.add(dataSnapshot.getValue().toString());
                }
                adapter_spienr adapter_spienr = new adapter_spienr(Them_combo.this
                        , R.layout.itme_spiner, arrayList_loai,"Loại");
                spinner.setAdapter(adapter_spienr);
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
                img.setImageURI(uridata);

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

    @Override
    public void onBackPressed() {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        Intent intent=new Intent(Them_combo.this,MianActivityBanac.class);
        intent.putExtra("id_nhahang",user.getUid());
        intent.putExtra("chucvu","1");
        intent.putExtra("them","combo");
        startActivity(intent);
    }
}