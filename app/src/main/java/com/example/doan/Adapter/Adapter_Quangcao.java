package com.example.doan.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.doan.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_Quangcao extends RecyclerView.Adapter<Adapter_Quangcao.photoViewHodel>{
    List<String>listphoto;

    public Adapter_Quangcao(List<String> listphoto) {
        this.listphoto = listphoto;
    }

    @NonNull
    @Override
    public photoViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quangcao,parent,false);
        return new  photoViewHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull photoViewHodel holder, int position) {
          String link=listphoto.get(position);
          if(link==null){
              return;
          }
        Picasso.get().load(link).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
       if(listphoto!=null){
           return listphoto.size();
       }
return 0;
    }

    public  class  photoViewHodel extends RecyclerView.ViewHolder
  {  ImageView imageView;
       public photoViewHodel(@NonNull View itemView) {
           super(itemView);
           imageView=itemView.findViewById(R.id.item_img_quangcao);
       }
   }



}
