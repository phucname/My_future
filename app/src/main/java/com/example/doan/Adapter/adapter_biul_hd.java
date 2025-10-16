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

import com.example.doan.Class.MonAn;
import com.example.doan.Class.classHoaDon;
import com.example.doan.R;

import java.util.ArrayList;

public class adapter_biul_hd extends RecyclerView.Adapter<adapter_biul_hd.ViewHoler> {
 private ArrayList<classHoaDon>arrayList_hoadon;
 private  ArrayList<MonAn>monAnArrayList;
    private final  int RESOURCE_ID;
    Context mcontext;

    public adapter_biul_hd(ArrayList<classHoaDon> arrayList_hoadon, ArrayList<MonAn> monAnArrayList, int RESOURCE_ID, Context mcontext) {
        this.arrayList_hoadon = arrayList_hoadon;
        this.monAnArrayList = monAnArrayList;
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
    public void onBindViewHolder(@NonNull ViewHoler holder, int position) {
             classHoaDon hoaDon=arrayList_hoadon.get(position);
             MonAn monAn=monAnArrayList.get(position);
             holder.textten.setText(monAn.getTenmon());
             holder.text_sl.setText(hoaDon.getSoluong());
             holder.textgia.setText(monAn.getGia());
             holder.text_tonggia.setText(""+(Integer.parseInt(monAn.getGia())*
                     Integer.parseInt(hoaDon.getSoluong())));
    }

    @Override
    public int getItemCount() {
        return arrayList_hoadon.size();
    }

    class  ViewHoler  extends RecyclerView.ViewHolder {

        TextView textgia, textten, text_tonggia, text_sl, text_stt;

        public ViewHoler(@NonNull View itemView) {
            super(itemView);

            textgia = itemView.findViewById(R.id.item_hd_gia);
            textten = itemView.findViewById(R.id.item_hd_tenmon);
            text_sl=itemView.findViewById(R.id.item_hd_slmon);
            text_tonggia=itemView.findViewById(R.id.item_hd_tongtien);


        }
    }
}