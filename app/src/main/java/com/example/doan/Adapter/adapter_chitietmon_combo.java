package com.example.doan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Activity.GoiMonActivity;
import com.example.doan.Class.MonAn;
import com.example.doan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapter_chitietmon_combo extends RecyclerView.Adapter<adapter_chitietmon_combo.ViewHodel> {
ArrayList<MonAn>monAnArrayList;
    private final  int RESOURCE_ID;
    Context mcontext;

    public adapter_chitietmon_combo(ArrayList<MonAn> monAnArrayList, int RESOURCE_ID, Context mcontext) {
        this.monAnArrayList = monAnArrayList;
        this.RESOURCE_ID = RESOURCE_ID;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(RESOURCE_ID,parent,false);
       adapter_chitietmon_combo.ViewHodel viewHoler=new adapter_chitietmon_combo.ViewHodel(view);
        return viewHoler;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodel holder, int position) {
        MonAn monAn=monAnArrayList.get(position);
        GoiMonActivity goiMonActivity=new GoiMonActivity();
        Picasso.get().load(monAn.getImgmon()).into(holder.imgmon);
        holder.textsl.setText("Số Lượng:"+monAn.getLoai_mon());
        holder.textgia.setText("Giá:"+goiMonActivity.VDN(monAn.getGia()));
        holder.textten.setText(monAn.getTenmon());

    }

    @Override
    public int getItemCount() {
        return monAnArrayList.size();
    }

    public  class ViewHodel extends RecyclerView.ViewHolder {
        ImageView imgmon;
        TextView textgia,textten,textsl;
        LinearLayout linearLayout;

        public ViewHodel(@NonNull View itemView) {
            super(itemView);
            imgmon=itemView.findViewById(R.id.item_img_mon_combo);
            textgia=itemView.findViewById(R.id.item_text_giamon_combo);
            textten=itemView.findViewById(R.id.item_text_tenmon_combo);
            textsl=itemView.findViewById(R.id.item_text_slmon_combo);
            linearLayout=itemView.findViewById(R.id.item_linear_mon_combo);
        }
    }
}
