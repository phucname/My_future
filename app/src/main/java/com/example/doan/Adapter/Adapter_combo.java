package com.example.doan.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Activity.Activity_chitiet_combo;
import com.example.doan.Activity.GoiMonActivity;
import com.example.doan.Activity.MianActivityBanac;
import com.example.doan.Class.MonAn;
import com.example.doan.Class.class_combo;
import com.example.doan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class Adapter_combo extends RecyclerView.Adapter<Adapter_combo.ViewHodel> {
    ArrayList<class_combo> class_comboArrayList;
    private final  int RESOURCE_ID;
    Context mcontext;
    int i,dk;
    String key;

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
    public  void set_arraynew(ArrayList<class_combo>comboArrayList){
       class_comboArrayList=comboArrayList;
        notifyDataSetChanged();
    }
    public Adapter_combo(ArrayList<class_combo> class_comboArrayList, int RESOURCE_ID, Context mcontext,String key,int dk) {
        this.class_comboArrayList = class_comboArrayList;
        this.RESOURCE_ID = RESOURCE_ID;
        this.mcontext = mcontext;
        this.key=key;
        this.dk=dk;
    }

    @NonNull
    @Override
    public ViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(RESOURCE_ID,parent,false);
        return new ViewHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodel holder, @SuppressLint("RecyclerView") int position) {
        class_combo combo = class_comboArrayList.get(position);
        holder.ten_combo.setText(combo.getTen_combo());
        GoiMonActivity mianActivityBanac = new GoiMonActivity();
        holder.gia_combo.setText(mianActivityBanac.VDN(combo.getGia_combo()));
        Picasso.get().load(combo.getImg_combo()).into(holder.img_combo);
        holder.linearLayout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MianActivityBanac mianActivityBanac = (MianActivityBanac) mcontext;
                Intent intent = new Intent(mcontext, Activity_chitiet_combo.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("class_combo", (Serializable) class_comboArrayList.get(position));
                // intent.putExtra("id_combo",combo.getId_combo());
                intent.putExtras(bundle);
                intent.putExtra("id_nhahang", mianActivityBanac.getId_nhahang());
                mcontext.startActivity(intent);
            }
        });
        holder.linearLayout_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setI(position);
                return false;
            }
        });
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("NhaHang").child(key);
        if (combo.getLoai_giamgia() != 2) {
            holder.giagiam.setVisibility(View.VISIBLE);
            holder.giam_gia.setVisibility(View.VISIBLE);
            holder.gia_combo.setPaintFlags(holder.gia_combo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }
        if (combo.getLoai_giamgia() == 0) {

            databaseReference.child("Khuyen_mai").child("PT").child(combo.getId_combo())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int giatri = Integer.parseInt(snapshot.child("gia_tri").getValue().toString());
                            holder.giam_gia.setText(giatri + "%");
                            String gia_saogiam = String.valueOf(((Integer.parseInt(combo.getGia_combo()) / 100) * (100 - giatri)));
                            char[] a =gia_saogiam.toCharArray();
                            for (int i=gia_saogiam.length()-3;i<gia_saogiam.length();i++){
                                a[i]='0';
                            }
                            gia_saogiam=new String(a);
                            holder.giagiam.setText(mianActivityBanac.VDN(gia_saogiam ));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
        if (combo.getLoai_giamgia() == 1) {
            databaseReference.child("Khuyen_mai").child("Tien").child(combo.getId_combo())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int giatri = Integer.parseInt(snapshot.child("gia_tri").getValue().toString());
                            holder.giam_gia.setText(giatri + ".đ");
                            int gia_saogiam = ((Integer.parseInt(combo.getGia_combo()) - giatri));
                            holder.giagiam.setText(mianActivityBanac.VDN(gia_saogiam + ""));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }

    }
    @Override
    public int getItemCount() {
        return class_comboArrayList.size();
    }

    public  class ViewHodel extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            TextView ten_combo,gia_combo;
            ImageView img_combo;
        TextView giam_gia,giagiam;
            LinearLayout linearLayout_item;
        public ViewHodel(@NonNull View itemView) {
            super(itemView);
            linearLayout_item=itemView.findViewById(R.id.linear_item_combo);
            ten_combo=itemView.findViewById(R.id.texttenmonan2);
            gia_combo=itemView.findViewById(R.id.textgia2);
            giagiam=itemView.findViewById(R.id.textgia_giam);
            giam_gia=itemView.findViewById(R.id.giam_gia);
            img_combo=itemView.findViewById(R.id.imgmenubanan);
           linearLayout_item.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
           if(dk==0){
               Activity activity= (Activity) v.getContext();
               activity.getMenuInflater().inflate(R.menu.stingcombo,menu);
           }
        }

    }
}
