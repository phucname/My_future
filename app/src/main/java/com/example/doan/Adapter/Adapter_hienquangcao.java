package com.example.doan.Adapter;

import android.app.Activity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_hienquangcao extends RecyclerView.Adapter<Adapter_hienquangcao.photoViewHodel> {
    List<String> listphoto;
    int position_item;
    String dieukien;

    public String getDieukien() {
        return dieukien;
    }

    public void setDieukien(String dieukien) {
        this.dieukien = dieukien;
    }

    public int getPosition_item() {
        return position_item;
    }

    public void setPosition_item(int position_item) {
        this.position_item = position_item;
    }

    public Adapter_hienquangcao(List<String> listphoto) {
        this.listphoto = listphoto;
    }

    @NonNull
    @Override
    public photoViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout._itemhien_quangcao,parent,false);
        return new photoViewHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull photoViewHodel holder, int position) {
        String link=listphoto.get(position);
        if(link==null){
            return;
        }
        Picasso.get().load(link).into(holder.imageView);
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
               setPosition_item(holder.getBindingAdapterPosition());
               setDieukien("ok");
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listphoto!=null){
            return listphoto.size();
        }
        return 0;
    }


    public class photoViewHodel extends RecyclerView.ViewHolder implements  View.OnCreateContextMenuListener {
        ImageView imageView;
       LinearLayout linearLayout;
        public photoViewHodel(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.hien_quangcao);
            linearLayout=itemView.findViewById(R.id.linear_itemQuangcao);
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
