package com.example.doan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.Class.MonAn;
import com.example.doan.Class.classBan;
import com.example.doan.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ThembanActivity extends AppCompatActivity {
    EditText maban, soghe, pas;
    Button themban;
    classBan ban;
    TextView tieude;
    DatabaseReference data;
    ArrayList<classBan>banArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themban);
        maban = findViewById(R.id.editthemmsoban);
        soghe = findViewById(R.id.editthemsoghe);
        themban = findViewById(R.id.btthemban);
        tieude=findViewById(R.id.text_themban);
        //pas=findViewById(R.id.editthempass);
        Bundle bundle = getIntent().getExtras();
        Intent intent = getIntent();
        data = FirebaseDatabase.getInstance().getReference().child("NhaHang")
                .child(intent.getStringExtra("id_nhahang")).child("Ban");
if(bundle.getSerializable("class_ban")!=null){
    banArrayList= (ArrayList<classBan>) bundle.get("class_ban");
}
        if (bundle.get("classsuaban") != null) {
            ban = (classBan) bundle.get("classsuaban");
            data.child(ban.getIdBan());
            maban.setText(ban.getMasoban());
            soghe.setText(ban.getSoghe());
            data=data.child(ban.getIdBan());

        }else  {
            themban.setText("Thêm Bàn");
            tieude.setText("Thêm Bàn Ăn");
        }


        themban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(maban.getText().toString())) {
                    if (!TextUtils.isEmpty(soghe.getText().toString())) {
                        if (bundle.get("classsuaban") != null) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("masoban", maban.getText().toString());
                            hashMap.put("soghe", soghe.getText().toString());

                            data.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ThembanActivity.this, "Sửa Bàn Thành Công", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                }
                            });
                        } else {
                            int i=0;
                            for (classBan ban:banArrayList){
                                if(ban.getMasoban().equals(maban.getText().toString())){
                                    i++;
                                }
                            }
                            if(i==0){
                                DatabaseReference databaseReference = data.push();
                                classBan ban = new classBan(maban.getText().toString(), soghe.getText().toString(), databaseReference.getKey(), false);
                                databaseReference.setValue(ban).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ThembanActivity.this, "Thêm Bàn Thành Công", Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    }
                                });
                            }else
                                Toast.makeText(ThembanActivity.this, "Mã số bàn đã tông tại!", Toast.LENGTH_SHORT).show();

                        }
                    } else Toast.makeText(ThembanActivity.this, "Số Ghế Của Bàn Không Được Trống", Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(ThembanActivity.this, "Mã Bàn Không Được Trống", Toast.LENGTH_SHORT).show();

            }
        });


    }
}