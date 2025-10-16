package com.example.doan.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Activity.HoaDonFragment;
import com.example.doan.Activity.MianActivityBanac;
import com.example.doan.Class.class_thongbao;
import com.example.doan.R;
import com.example.doan.Class.classBan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class AdapterBan extends RecyclerView.Adapter<AdapterBan.ViewHoler> {
    private ArrayList<classBan> arrayList;
    private final  int RESOURCE_ID;
    Context mcontext;
    int position;
    int puss ;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public AdapterBan(ArrayList<classBan> arrayList, int RESOURCE_ID, Context mcontext) {
        this.arrayList = arrayList;
        this.RESOURCE_ID = RESOURCE_ID;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(RESOURCE_ID,parent,false);
        ViewHoler viewHoler=new ViewHoler(view);
       return viewHoler;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoler holder, int positionq) {
           holder.ten_ban.setText("Bàn Số :"+arrayList.get(holder.getBindingAdapterPosition()).getMasoban());
        holder.soghe.setText("Chỗ Ngồi:"+arrayList.get(holder.getBindingAdapterPosition()).getSoghe());

       // Toast.makeText(mcontext, arrayList.get(holder.getBindingAdapterPosition()).isTinhTrang()+"ad", Toast.LENGTH_SHORT).show();
       if(arrayList.get(holder.getBindingAdapterPosition()).isTinhTrang()==false){
           holder.tinhtrang.setText("Bàn Trống");
       }else holder.tinhtrang.setText("Bàn Có Người");
           MianActivityBanac mianActivityBanac = (MianActivityBanac) mcontext;
           String a = mianActivityBanac.getId_nhahang();


        holder.Car_item.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                  FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

                   if(user!=null){
                       if(arrayList.get(holder.getBindingAdapterPosition()).isTinhTrang()==false){


                           dialog_datban(mianActivityBanac.getId_nhahang(),
                                   arrayList.get(holder.getBindingAdapterPosition()).getIdBan());

                       }
                       else {
                           //  dialog_input_pass(holder.getBindingAdapterPosition(),a);
                           Intent intent =new Intent(mcontext, HoaDonFragment.class);
                           intent.putExtra("id_nhahang",mianActivityBanac.getId_nhahang());
                           intent.putExtra("nameban",arrayList.get(holder.getBindingAdapterPosition()).getIdBan());
                           intent.putExtra("maban",arrayList.get(holder.getBindingAdapterPosition()).getMasoban());
                           intent.putExtra("pass",arrayList.get(holder.getBindingAdapterPosition()).getPass()+"");
                           intent.putExtra("chucvu",mianActivityBanac.getChuvu_nv()+"");
                           mcontext.startActivity(intent);
                       }
                   }else Toast.makeText(mianActivityBanac, "Chỉ có nhân viên nhà hàng mới thực hiện được thao tác này!", Toast.LENGTH_SHORT).show();

//
               }
           });
        holder.Car_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
               setPosition(holder.getBindingAdapterPosition());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    //buocw 1 tao class viewholer
    class ViewHoler extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
     TextView ten_ban,tinhtrang,soghe;
     CardView Car_item;
        public ViewHoler(@NonNull View itemView) {
            super(itemView);
            ten_ban=itemView.findViewById(R.id.text_tenban);
            tinhtrang=itemView.findViewById(R.id.tinhtrang_ban);
            Car_item=itemView.findViewById(R.id.linear_banitem);
            soghe=itemView.findViewById(R.id.text_ghe);
            Car_item.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            Activity activity= (Activity) v.getContext();
            activity.getMenuInflater().inflate(R.menu.menupopo,menu);
        }
    }
    public void dialog_input_pass(int pos,String id_nhahang){
        Dialog dialog=new Dialog(mcontext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_input_passban);
        Window window=dialog.getWindow();
        if(window==null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowaatriss= window.getAttributes();
        windowaatriss.gravity= Gravity.CENTER;
        dialog.setCancelable(false);
        EditText input_pass=dialog.findViewById(R.id.dialog_editex_pass);
        Button button_ok=dialog.findViewById(R.id.dialog_button_ok);
        Button button_cancel=dialog.findViewById(R.id.dialog_button_candel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if( arrayList.get(pos).getPass()==Integer.parseInt(input_pass.getText().toString())){
                   Intent intent =new Intent(mcontext, HoaDonFragment.class);
                   intent.putExtra("id_nhahang",id_nhahang);
                   intent.putExtra("nameban",arrayList.get(pos).getIdBan());
                   intent.putExtra("maban",arrayList.get(pos).getMasoban());
                   intent.putExtra("pass",arrayList.get(pos).getPass());
                  mcontext.startActivity(intent);
               }
            }
        });
        dialog.show();

    }
    private  void dialog_datban(String id_nhahang,String id_ban){

        AlertDialog.Builder aler=new AlertDialog.Builder(mcontext);

        aler.setTitle("Thông Báo");
        aler.setMessage("Bàn trống bạn có muốn trọn bàn này!");
        aler.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null) {
                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference()
                            .child("NhaHang").child(id_nhahang).child("Ban").child(id_ban)
                            ;
                    databaseReference.child("tinhTrang").setValue(true);
//                    Random random=new Random();
//                    puss= random.nextInt(1000);
//                    databaseReference.child("pass").setValue(puss);
//                    notifyDataSetChanged();
//                    DatabaseReference data=FirebaseDatabase.getInstance().getReference()
//                            .child("NhaHang").child(id_nhahang).child("DS_NhanVien")
//                            .child(user.getUid()).child("Thong_Bao").push();
//                    Calendar calendar=Calendar.getInstance();
//
//                    class_thongbao thongbaoclass=new class_thongbao(data.getKey(),"Bạn Vừa Đặt  Bàn:"+arrayList.get(getPosition()).getMasoban()
//                            +",Pass là:"+puss,
//                            calendar.get(Calendar.HOUR)+"h"
//                                    +calendar.get(Calendar.MINUTE)
//                                    +","+calendar.get(Calendar.DATE)+"/"
//                                    +calendar.get(Calendar.MONTH)+"/" +
//                                    "/"+calendar.get(Calendar.YEAR));
//
//                    AlertDialog.Builder aler=new AlertDialog.Builder(mcontext);
//
//                   data.setValue(thongbaoclass
                // );
//                    aler.setTitle("Thông Báo");
//                    aler.setMessage("PassWord của bàn là:"+puss);
//                    aler.show();
                }else Toast.makeText(mcontext, "Bạn Phải Là Nhân Viên Mới Được Đặt Bàn", Toast.LENGTH_SHORT).show();

            }
        });

        aler.show();

    }
}
