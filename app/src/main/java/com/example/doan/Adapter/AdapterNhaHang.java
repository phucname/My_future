package com.example.doan.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Activity.MianActivityBanac;
import com.example.doan.Activity.TrangChuActivuty;
import com.example.doan.Class.NhaHang;
import com.example.doan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterNhaHang extends RecyclerView.Adapter<AdapterNhaHang.ViewHoler> {
    private ArrayList<NhaHang>arrayListNH;
    private final  int RESOURCE_ID;
    int position;
    Context mcontext;
public  void set_array(ArrayList<NhaHang>nhaHangArrayList){
    arrayListNH=nhaHangArrayList;
    notifyDataSetChanged();
}

    public AdapterNhaHang(Context context,ArrayList<NhaHang> arrayListNH, int RESOURCE_ID) {
        this.arrayListNH = arrayListNH;
        this.RESOURCE_ID = RESOURCE_ID;
        mcontext=context;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @NonNull
    @Override
    public ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View view= LayoutInflater.from(parent.getContext()).inflate(RESOURCE_ID,parent,false);
         ViewHoler holer=new ViewHoler(view);

        return holer;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoler holder, int position) {
        NhaHang nhaHang=arrayListNH.get(holder.getBindingAdapterPosition());
            holder.tennhahang.setText(nhaHang.getTen_nhahang());
      Picasso.get().load(nhaHang.getMg_nhahang()).into(holder.imgnhahang);
        holder.diachi.setText(nhaHang.getDiachi_nhahang());
         holder.linearLayoutNH.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent=new Intent(mcontext, MianActivityBanac.class);
                 intent.putExtra("key",nhaHang.get_sdt_nhahangi());
                 //Toast.makeText(mcontext, nhaHang.getId()+"", Toast.LENGTH_SHORT).show();
                 intent.putExtra("name",nhaHang.getTen_nhahang());
                mcontext.startActivity(intent);

             }
         });






        }



    @Override
    public int getItemCount() {
        return arrayListNH.size();
    }

    public class ViewHoler extends RecyclerView.ViewHolder  {
      private TextView  tennhahang,diachi;
      private LinearLayout linearLayoutNH;
      ImageView imgnhahang;
        public ViewHoler(@NonNull View itemView) {
            super(itemView);
            diachi=itemView.findViewById(R.id.text_diachinhahang);
            imgnhahang=itemView.findViewById(R.id.img_nhahang);
            tennhahang = itemView.findViewById(R.id.texttennhahang);
            linearLayoutNH = itemView.findViewById(R.id.linernhahang);
        }



    }
}
