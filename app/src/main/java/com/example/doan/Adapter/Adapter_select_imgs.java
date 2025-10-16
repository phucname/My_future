package com.example.doan.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class Adapter_select_imgs extends RecyclerView.Adapter<Adapter_select_imgs.selctimgViewHodel> {

ArrayList<Uri>uri;
int positon_select_quangcao;

    public int getPositon_select_quangcao() {
        return positon_select_quangcao;
    }

    public void setPositon_select_quangcao(int positon_select_quangcao) {
        this.positon_select_quangcao = positon_select_quangcao;
    }

    public Adapter_select_imgs(ArrayList<Uri> uri) {
        this.uri = uri;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public selctimgViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout._itemhien_quangcao,parent,false);
        return new selctimgViewHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull selctimgViewHodel holder,  int position) {
                if(uri!=null){
                    Uri uri1=uri.get(position);
                    holder.img.setImageURI(uri1);

                }
                holder.linearLayout_select_img.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                       setPositon_select_quangcao(holder.getBindingAdapterPosition());
                        Toast.makeText(holder.itemView.getContext(), uri.size()+"", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });


                return;





        //Toast.makeText(holder.itemView.getContext(), uri1+"", Toast.LENGTH_SHORT).show();

    }

    @Override
    public int getItemCount() {
      if(uri!=null){
          return uri.size();
      }

        return 0;
    }

    public  class selctimgViewHodel extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
 ImageView img;
 LinearLayout linearLayout_select_img;
        public selctimgViewHodel(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.hien_quangcao);
            linearLayout_select_img=itemView.findViewById(R.id.linear_itemQuangcao);
            linearLayout_select_img.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            Activity activity= (Activity) v.getContext();
            activity.getMenuInflater().inflate(R.menu.menupopo,menu);
            menu.getItem(0).setVisible(false);
        }
    }
}
