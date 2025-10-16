package com.example.doan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.doan.Class.class_TTVN;
import com.example.doan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
             activiti();
            }


        },500);



    }
    private void activiti() {
     DatabaseReference data=FirebaseDatabase.getInstance().getReference().child("TT_VN");
//data.removeValue();
     data.addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {
             if(snapshot.getValue()==null){
                 int i=0,ii=0,iii=0;    String tp="";
                 String huyen="";
                 String xa="";
                 try {

                     InputStream inputStream=getResources().openRawResource(R.raw.ds_vn);

                     BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                     String line;



                     while((line=bufferedReader.readLine())!=null){
                         String[]a=line.split(",");
                         if(tp.equals(a[1])){
                             if(huyen.equals(a[3])){

                                    // Toast.makeText(this, ii+"huyen", Toast.LENGTH_SHORT).show();
                                     iii++;
                                     class_TTVN classxa=new class_TTVN(""+iii,a[4]);
                                     data.child("Xa").child(ii+"").child(iii+"").setValue(classxa);


                             }else {
                               //  Toast.makeText(MainActivity.this, i+"tp", Toast.LENGTH_SHORT).show();
                                 ii++;
                                 huyen=a[3];
                                 class_TTVN classhuyen=new class_TTVN(ii+"",a[2]);
                                 data.child("Huyen").child(i+"").child(ii+"").setValue(classhuyen);
                             }

                         }else {

                             i++;
                             tp=a[1];
                             class_TTVN class_tp=new class_TTVN(i+"",a[0]);
                             data.child("TP").child(i+"").setValue(class_tp);
                           //  Toast.makeText(this,a[0]+"/"+i, Toast.LENGTH_SHORT).show();
//
                         }




                     }




                 }catch (Exception exception){
                 }finally {
                     Intent intent=new Intent(MainActivity.this,TrangChuActivuty.class);
                    startActivity(intent);
                 }


             }else {
                             Intent intent=new Intent(MainActivity.this,TrangChuActivuty.class);
            startActivity(intent);
             }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {

         }
     });




    }


}