package com.example.doan.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.Adapter.adapter_loaimon;
import com.example.doan.Class.class_loai;
import com.example.doan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Fragment_loai_mon extends Fragment {
ListView listView;
adapter_loaimon adapter_loaimon;
ArrayList<class_loai>arrayList_loai;
DatabaseReference databaseReference;
MianActivityBanac mianActivityBanac;
FloatingActionButton them_loai;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view=inflater.inflate(R.layout.fragment_loai_mon, container, false);
         them_loai=view.findViewById(R.id.float_them_loai);
         mianActivityBanac= (MianActivityBanac) getActivity();
         if(mianActivityBanac.getChuvu_nv()!=1){
             them_loai.setVisibility(View.INVISIBLE);
         }
         listView=view.findViewById(R.id.list_ds_loaimon);

         databaseReference= FirebaseDatabase.getInstance().getReference()
                 .child("NhaHang").child(mianActivityBanac.getId_nhahang());
         databaseReference.child("Loai_Menu").addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList_loai=new ArrayList<>();
                 for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                     class_loai loai=new class_loai(dataSnapshot.getKey(),dataSnapshot.getValue().toString());
                     arrayList_loai.add(loai);
                 }
                 adapter_loaimon=new adapter_loaimon(arrayList_loai,getContext(),R.layout.itme_loai);
                 listView.setAdapter(adapter_loaimon);
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
         them_loai.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Dialog dialog=new Dialog(getActivity());
                 dialog.setContentView(R.layout.activity_them_loaimenu);
                Button them=dialog.findViewById(R.id.button_themloai);
                 EditText editTexttloai=dialog.findViewById(R.id.edit_themloai);
             them.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     databaseReference.child("Loai_Menu").push().setValue(editTexttloai.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                             dialog.dismiss();
                             Toast.makeText(mianActivityBanac, "Thêm Loại Món Ăn Thành Công", Toast.LENGTH_SHORT).show();
                         }
                         }
                     });
                 }
             });

//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setContentView(R.layout.dialog_input_passban);
                 Window window=dialog.getWindow();
                 window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                 // window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                 WindowManager.LayoutParams windowaatriss= window.getAttributes();
                 windowaatriss.gravity= Gravity.CENTER;
//                 Intent intent=new Intent(getActivity(),Them_loaimenu.class);
//                 intent.putExtra("id_nhahang",mianActivityBanac.getId_nhahang());
//                 startActivity(intent);
                 dialog.show();
             }
         });
        return view;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.Sua:
                    Dialog dialog=new Dialog(getActivity());
                    dialog.setContentView(R.layout.activity_them_loaimenu);
                    Button them=dialog.findViewById(R.id.button_themloai);
                    EditText editTexttloai=dialog.findViewById(R.id.edit_themloai);
                    TextView title=dialog.findViewById(R.id.text_title_themloai);
                    title.setText("Update");
                    them.setText("Update");
                    editTexttloai.setText(arrayList_loai.get(adapter_loaimon.getI()).getTen());
                    them.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            databaseReference.child("Loai_Menu").child(arrayList_loai.get(adapter_loaimon.getI()).getId()).setValue(editTexttloai.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful()){
                                       dialog.dismiss();
                                       Toast.makeText(mianActivityBanac, "Sửa Loại Món Ăn Thành Công", Toast.LENGTH_SHORT).show();
                                   }

                                }
                            });
                        }
                    });
                    Window window=dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                    // window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    WindowManager.LayoutParams windowaatriss= window.getAttributes();
                    windowaatriss.gravity= Gravity.CENTER;
                    dialog.show();
//                    Intent intent=new Intent(getActivity(),Them_loaimenu.class);
//                    intent.putExtra("id_nhahang",mianActivityBanac.getId_nhahang());
//                    Bundle bundle=new Bundle();
//                    bundle.putSerializable("class_loai",arrayList_loai.get(adapter_loaimon.getI()));
//                    intent.putExtras(bundle);
//                    startActivity(intent);

                    break;
                case R.id.Xoa:
                    AlertDialog.Builder alertDialog=new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Thông báo!");
                    alertDialog.setMessage("Bạn có muốn xóa kiểu loại này khỏi danh sách:");
                    alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Query query= databaseReference.child("DS_MonAn").orderByChild("loai_mon")
                                    .equalTo(arrayList_loai.get(adapter_loaimon.getI()).getId());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if(snapshot.getValue()==null){
                                        databaseReference.child("Loai_Menu").child(arrayList_loai.get(adapter_loaimon.getI()).getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(mianActivityBanac, "Xóa Loại Thành Công", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }else Toast.makeText(mianActivityBanac, "Có Một Số Món Đang Có Loai Này Bạn Không Thể Xóa!", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                    alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.setCancelable(true);
                        }
                    });

                       alertDialog.show();
                    break;
            }
        return super.onContextItemSelected(item);
    }
}