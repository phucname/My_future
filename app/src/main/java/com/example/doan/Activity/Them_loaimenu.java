package com.example.doan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.doan.Class.class_loai;
import com.example.doan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Them_loaimenu extends AppCompatActivity {
Button them;
EditText editTexttloai;
class_loai loai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_loaimenu);
        them=findViewById(R.id.button_themloai);
        editTexttloai=findViewById(R.id.edit_themloai);
        Intent intent=getIntent();
        Bundle bundle=getIntent().getExtras();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference()
                .child("NhaHang").child(intent.getStringExtra("id_nhahang"))
                .child("Loai_Menu");
        if(bundle.getSerializable("class_loai")!=null){
          loai= (class_loai) bundle.getSerializable("class_loai");
            editTexttloai.setText(loai.getTen());
        }
        them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bundle.getSerializable("class_loai")!=null){
                    databaseReference.child(loai.getId()).setValue(editTexttloai.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                finish();
                            }
                        }
                    });
               }else {
                    databaseReference.push().setValue(editTexttloai.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        finish();
                                    }
                                }
                            });;
                }


            }
        });
    }
}