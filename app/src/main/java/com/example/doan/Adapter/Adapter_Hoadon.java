package com.example.doan.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Activity.GoiMonActivity;
import com.example.doan.Activity.MianActivityBanac;
import com.example.doan.Class.MonAn;
import com.example.doan.Class.classHoaDon;
import com.example.doan.Class.class_giamgia;
import com.example.doan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Adapter_Hoadon extends RecyclerView.Adapter<Adapter_Hoadon.ViewHoder> {
    Context context;
    List<classHoaDon> monAnList;
    ArrayList<MonAn>monAnArrayList;
    ArrayList<class_giamgia>arrayList_giamgia;
    int layout;
    class_giamgia  giamgia;
    classHoaDon hoaDon;
    MonAn monAn;
String key;
    int tien;
    int posi;

    public int getPosi() {
        return posi;
    }

    public void setPosi(int posi) {
        this.posi = posi;
    }

    public List<classHoaDon> getMonAnList() {

        return monAnList;
    }



    public Adapter_Hoadon(Context context, int layout, List<classHoaDon> monAnList,
                          ArrayList<MonAn>monAnArrayList,String key,ArrayList<class_giamgia>arrayList_giamgia) {
        this.context = context;
        this.monAnList = monAnList;
        this.layout = layout;
        this.monAnArrayList=monAnArrayList;
        this.arrayList_giamgia=arrayList_giamgia;
     this.key=key;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hoadon,parent,false);
        return new Adapter_Hoadon.ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, @SuppressLint("RecyclerView") int position) {
        GoiMonActivity goiMonActivity=new GoiMonActivity();

        try{
            hoaDon=monAnList.get(position);
           monAn=monAnArrayList.get(position);

        }catch (IndexOutOfBoundsException ex){

        }


        if(arrayList_giamgia!=null){
            if(arrayList_giamgia.isEmpty()==false){

                try{
                    giamgia =arrayList_giamgia.get(position);
                    if(monAn.getLoai_giamgia()==0) {


                        holder.giatri_km.setText("Giảm:" + giamgia.getGia_tri() + "%");

                        holder.gianew.setText(goiMonActivity.VDN(giamgia.getGia_new() ));
                    }
                    if(monAn.getLoai_giamgia()==1){

                        holder.giatri_km.setText("Giảm:"+goiMonActivity.VDN(giamgia.getGia_tri()));

                        holder.gianew.setText(goiMonActivity.VDN(giamgia.getGia_new()));


                    }
                }catch (IndexOutOfBoundsException ex){

                }


            }
        }


        if(monAn.getLoai_giamgia()!=2){
            holder.gianew.setVisibility(View.VISIBLE);
            holder.gia.setPaintFlags(holder.gia.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }


         holder.chat.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Dialog dialog=new Dialog(context);
                 dialog.setContentView(R.layout.dialog_chat);
                 EditText eitchat=dialog.findViewById(R.id.dialog_editex_chat);

                 Button ok=dialog.findViewById(R.id.dialog_button_chat);
                 Button caneld=dialog.findViewById(R.id.dialog_button_candel);

                 eitchat.setText(monAnList.get(position).getChat());

                 Window window=dialog.getWindow();
                 window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                 // window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                 WindowManager.LayoutParams windowaatriss= window.getAttributes();
                 windowaatriss.gravity= Gravity.CENTER;
                 ok.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference()
                                 .child("NhaHang").child(key).child("Ban").child(hoaDon.getId_ban()).child("DSMon");
                              if(hoaDon.getLoai()==0){
                                  databaseReference.child("mon").child(hoaDon.getId()).child("chat").setValue(eitchat.getText().toString());
                                  monAnList.get(holder.getBindingAdapterPosition()).setChat(eitchat.getText().toString());
                                  notifyDataSetChanged();
                                  dialog.dismiss();
                              }
                         if(hoaDon.getLoai()==1){
                             databaseReference.child("combo").child(hoaDon.getId())
                                     .child("chat").setValue(eitchat.getText().toString());
                             monAnList.get(holder.getBindingAdapterPosition()).setChat(eitchat.getText().toString());
                             notifyDataSetChanged();
                             dialog.dismiss();
                         }

                     }
                 });
                 dialog.show();
             }
         });
       holder.ten.setText(monAn.getTenmon());
       if(!hoaDon.getTinhtrang().equals("Chưa Nhận")){
           holder.delete.setVisibility(View.INVISIBLE);
           holder.chat.setVisibility(View.INVISIBLE);
       }

        holder.gia.setText(goiMonActivity.VDN(monAn.getGia()));
        holder.soluong.setText("Số Lượng:"+hoaDon.getSoluong());
        holder.tt.setText("Tình Trạng:"+hoaDon.getTinhtrang());
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosi(holder.getBindingAdapterPosition());
                return false;
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classHoaDon hoaDon1=monAnList.get(holder.getBindingAdapterPosition());
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference()
                        .child("NhaHang").child(key).child("Ban").child(hoaDon1.getId_ban()).child("DSMon");

                if(hoaDon1.getLoai()==0){
                    databaseReference.child("mon").child(hoaDon1.getId()).removeValue();
                   monAnList.remove(holder.getBindingAdapterPosition());
                    monAnArrayList.remove(holder.getBindingAdapterPosition());
                    arrayList_giamgia.remove(holder.getBindingAdapterPosition());
                    notifyDataSetChanged();
                }else  {
                    databaseReference.child("combo").child(hoaDon1.getId()).removeValue();
                    monAnList.remove(holder.getBindingAdapterPosition());
                    monAnArrayList.remove(holder.getBindingAdapterPosition());
                    arrayList_giamgia.remove(holder.getBindingAdapterPosition());
                    notifyDataSetChanged();
                }

            }
        });
        tien=tien+(Integer.parseInt(monAn.getGia())*Integer.parseInt(hoaDon.getSoluong()));
        // Toast.makeText(context, hoaDon.getDaubep()+"", Toast.LENGTH_SHORT).show();
        // tonggia.setText("Tổng Tiền:"+goiMonActivity.VDN(Integer.parseInt(hoaDon.getGia())*Integer.parseInt(hoaDon.getSoLuong())+""));
        Picasso.get().load(monAn.getImgmon()).into(holder.anh);
    }

    @Override
    public int getItemCount() {
        return monAnList.size();
    }


    public class ViewHoder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView ten;
        TextView gia,gianew,giatri_km;
        TextView soluong;
        TextView tt;
        LinearLayout linearLayout;
        ImageView delete,chat;
        // TextView tonggia=view.findViewById(R.id.tonggiamon);
        ImageView anh;
        public ViewHoder(@NonNull View view) {
            super(view);
           ten=view.findViewById(R.id.hoadontenmon2);
            gia=view.findViewById(R.id.hoadongia2);
            gianew=view.findViewById(R.id.new_hoadongia2);
            giatri_km=view.findViewById(R.id.giatri_khuyenmai_hd);
           soluong=view.findViewById(R.id.soluong_hoadon2);
           tt=view.findViewById(R.id.text_tinhtrangmon_hoadon2);
          linearLayout=view.findViewById(R.id.linear_hoadon);
         delete=view.findViewById(R.id.img_delete_itemgiohang2);
         chat=view.findViewById(R.id.img_chat_itemhd);
            // TextView tonggia=view.findViewById(R.id.tonggiamon);
         anh=view.findViewById(R.id.hoadonimg2);
            linearLayout.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            Activity activity= (Activity) v.getContext();
            activity.getMenuInflater().inflate(R.menu.menupopo,menu);
            menu.getItem(0).setVisible(false);
        }
    }


}
