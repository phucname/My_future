package com.example.doan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.Class.MonAn;
import com.example.doan.Class.classHoaDon;
import com.example.doan.Class.class_Bep;
import com.example.doan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class xuly_monan_activity extends AppCompatActivity {
TextView textten,textsoluong,textykien;
ImageView imgmonxuly;
Button button;
    String []s;
    classHoaDon bep;
    ArrayList<MonAn>monAnArrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuly_monan);
        button=findViewById(R.id.button_hoanthanhmonan_bepxl);
        File file;
        ContextWrapper contextWrapper=new ContextWrapper(this);
        File fixx=contextWrapper.getDir("TK", Context.MODE_APPEND);
        file=new File(fixx,"Don.txt");
            try {
                FileInputStream fileInputStream=new FileInputStream(file);
                InputStreamReader streamReader=new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader=new BufferedReader(streamReader);
                String i="";
                while ((i=bufferedReader.readLine())!=null){
                   s=i.split(",");
                    DatabaseReference data= FirebaseDatabase.getInstance().getReference()
                            .child("NhaHang").child(s[0])
                            .child("Ban").child(s[1]).child("mon").child(s[2]);
                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            bep = new classHoaDon(snapshot.child("daubep").getValue().toString()
                                    , snapshot.child("id_ban").getValue().toString()
                                    , snapshot.child("date_goimon").getValue().toString()
                                    , snapshot.child("soluong").getValue().toString()
                                    , snapshot.child("id_mon").getValue().toString()
                                    , snapshot.child("tinhtrang").getValue().toString(),
                                    snapshot.child("chat").getValue().toString());
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                                    .child("NhaHang").child(s[0])
                                    .child("DS_MonAn").child(bep.getId_mon());
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    monAnArrayList.add(new MonAn(snapshot.child("tenmon").getValue().toString()
                                            , snapshot.child("gia").getValue().toString()
                                            , snapshot.child("ghichu").getValue().toString()
                                            , snapshot.child("imgmon").getValue().toString()
                                    , Integer.parseInt(snapshot.child("loai_giamgia").getValue().toString())));
                                    load();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            } );
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatabaseReference data= FirebaseDatabase.getInstance().getReference()
                        .child("NhaHang").child(s[0])
                        .child("Ban").child(s[1]);//DSM

                data.child(s[2]).child("tinhtrang").setValue("Đã Xong").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Query query=data.orderByChild("id_mon").equalTo(bep.getId_mon());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                   if(snapshot.getChildrenCount()==2){
                                       int i=0,sl1=0;
                                       String key="";
                                       for (DataSnapshot snapshot1:snapshot.getChildren()){
                                             if(snapshot1.child("tinhtrang").getValue().toString()
                                                     .equals("Đã Xong")){
                                                 i++;
                                                 if(snapshot1.getKey().equals(s[2])){

                                                 }else{
                                                     key=snapshot1.getKey();
                                                     sl1=Integer.parseInt(snapshot1.child("soluong").getValue().toString());
                                                 }

                                             }
                                       }
                                       if (i == 2) {
                                           data.child(s[2]).removeValue();
                                           int sl= sl1+Integer.parseInt(textsoluong.getText().toString());
                                           data.child(key).child("soluong")
                                                   .setValue(sl+"");
                                       }
                                   }
                                   else {

                                   }
                                file.delete();
                              finish();

                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
//
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "không thể " +
                "thoát khi món ăn chưa dx hoàn " +
                "thành", Toast.LENGTH_SHORT).show();

    }
    public  void load(){
        textten=findViewById(R.id.text_tenmon_bepxl);
        textsoluong=findViewById(R.id.text_slmon_bepxl);
        textykien=findViewById(R.id.text_yeucaukhachhang_bepxl);
        imgmonxuly=findViewById(R.id.img_monan_bepxl);
        textten.setText(monAnArrayList.get(0).getTenmon());
        textsoluong.setText(bep.getSoluong());
        Picasso.get().load(monAnArrayList.get(0).getImgmon()).into(imgmonxuly);
    }
}