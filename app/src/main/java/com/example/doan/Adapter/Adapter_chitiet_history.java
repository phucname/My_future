package com.example.doan.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.ContextMenu;
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
import com.example.doan.Class.classHoaDon;
import com.example.doan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter_chitiet_history extends RecyclerView.Adapter<Adapter_chitiet_history.ViewHoder> {
    Context context;
    List<classHoaDon> monAnList;
    ArrayList<MonAn> monAnArrayList;
    int layout;
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
    public Adapter_chitiet_history(Context context, int layout, List<classHoaDon> monAnList,ArrayList<MonAn>monAnArrayList,String key) {
        this.context = context;
        this.monAnList = monAnList;
        this.layout = layout;
        this.monAnArrayList=monAnArrayList;
        this.key=key;

    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chitiet_history,parent,false);
        return new Adapter_chitiet_history.ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        GoiMonActivity goiMonActivity=new GoiMonActivity();
        classHoaDon hoaDon=monAnList.get(position);
        MonAn monAn=monAnArrayList.get(position);
        holder.ten.setText(monAn.getTenmon());
        Picasso.get().load(monAn.getImgmon()).into(holder.anh);
        holder.gia.setText("Giá:"+goiMonActivity.VDN(hoaDon.getGia()));
        holder.soluong.setText("Số Lượng:"+hoaDon.getSoluong());
    }

    @Override
    public int getItemCount() {
        return monAnArrayList.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder  {
        TextView ten;
        TextView gia;
        TextView soluong;

        ImageView anh;
        public ViewHoder(@NonNull View view) {
            super(view);
            ten=view.findViewById(R.id.hoadontenmon_chitiet_history);
            gia=view.findViewById(R.id.hoadongia_chitiet_history);
            soluong=view.findViewById(R.id.soluong_chitiet_history);

            anh=view.findViewById(R.id.hoadonimg_chitiet_history);

        }


    }
}
