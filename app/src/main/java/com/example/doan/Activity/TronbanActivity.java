package com.example.doan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.doan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TronbanActivity extends AppCompatActivity {
ListView listViewBan;
DatabaseReference data;
ArrayList<String>pass;
    ArrayList<String>arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tronban);
        Intent intent=getIntent();
        listViewBan=findViewById(R.id.listviewBan);
        data= FirebaseDatabase.getInstance().getReference().child(intent.getStringExtra("key")).child("Ban");
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               arrayList=new ArrayList<>();
                pass=new ArrayList<>();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    arrayList.add(snapshot1.getKey());//chứa kháo của bàn
                    pass.add(snapshot1.child("Pass").getValue().toString());
                }
                ArrayAdapter adapter=new ArrayAdapter(TronbanActivity.this, android.R.layout.simple_list_item_1,arrayList);
                listViewBan.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listViewBan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(TronbanActivity.this,i+"",Toast.LENGTH_LONG).show();
                //Showdialog(i);
            }

//            private void Showdialog(int i) {
//                Dialog dialog=new Dialog(TronbanActivity.this);
//                  dialog.setContentView(R.layout.dialogban);
//               // EditText passs=dialog.findViewById(R.id.dialogeditpass);
//                Button dangnhap=dialog.findViewById(R.id.btdangnhapban);
//                dangnhap.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if ((passs.getText().toString()).equals(pass.get(i).toString())){
//                            Intent intent1=new Intent(TronbanActivity.this,MianActivityBanac.class);
//                            intent1.putExtra("key",intent.getStringExtra("key"));
//                            intent1.putExtra("ban",arrayList.get(i));
//                            intent1.putExtra("name",intent.getStringExtra("name"));
//                            startActivity(intent1);
//                        }
//                    }
//                });
//                dialog.show();
//            }
        });
    }
}