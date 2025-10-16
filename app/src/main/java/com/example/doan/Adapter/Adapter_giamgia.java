package com.example.doan.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Activity.GoiMonActivity;
import com.example.doan.Activity.MianActivityBanac;
import com.example.doan.Class.MonAn;
import com.example.doan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter_giamgia extends RecyclerView.Adapter<Adapter_giamgia.ViewHoler>  {
    private ArrayList<MonAn> arrayList;
    ArrayList<MonAn>mondatron=new ArrayList<>();
int kieu;
    public ArrayList<MonAn> getMondatron() {
        return mondatron;
    }

    public void setMondatron(ArrayList<MonAn> mondatron) {
        this.mondatron = mondatron;
    }

    private final int RESOURCE_ID;
    Context mcontext;
    String key;
    int stt;

    public ArrayList<MonAn> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<MonAn> arrayList) {
        this.arrayList = arrayList;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public Adapter_giamgia(ArrayList<MonAn> arrayList, int RESOURCE_ID, Context mcontext, String key,int kieu) {
        this.arrayList = arrayList;
        this.RESOURCE_ID = RESOURCE_ID;
        this.mcontext = mcontext;
        this.key = key;
        this.kieu=kieu;
    }

    public ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(RESOURCE_ID,parent,false);
      Adapter_giamgia.ViewHoler viewHoler=new Adapter_giamgia.ViewHoler(view);
        return viewHoler;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoler holder, int position) {
       // Toast.makeText(mcontext, "aaaaaaaaaaaa", Toast.LENGTH_SHORT).show();
        MonAn monAn=arrayList.get(position);
        GoiMonActivity goiMonActivity=new GoiMonActivity();

        holder.textten2.setText(monAn.getTenmon());
        holder.textgia2.setText("Giá:"+goiMonActivity.VDN(monAn.getGia()));
        Picasso.get().load(monAn.getImgmon()).into(holder.imgmonan2);
        if(kieu==1){
            holder.check.setVisibility(View.INVISIBLE);
        }
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference()
                .child("NhaHang").child(key);
        if(monAn.getLoai_giamgia()==2){
            holder.gia_tri_giam.setVisibility(View.INVISIBLE);
            holder.giagiam.setVisibility(View.INVISIBLE);holder.thoi_gian_giam.setVisibility(View.INVISIBLE);
        }
        if(monAn.getLoai_giamgia()==0){

            databaseReference.child("Khuyen_mai").child("PT").child( monAn.getId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int giatri=Integer.parseInt(snapshot.child("gia_tri").getValue().toString());
                            holder.gia_tri_giam.setText("Giảm:"+giatri+"%");
                            String gia_saogiam= String.valueOf(((Integer.parseInt(monAn.getGia())/100)*(100-giatri)));
                            //Toast.makeText(mcontext, gia_saogiam, Toast.LENGTH_SHORT).show();
                            MianActivityBanac mianActivityBanac=new MianActivityBanac();

                            holder.giagiam.setText(goiMonActivity.VDN(mianActivityBanac.lam_chan(gia_saogiam)));

                            holder.thoi_gian_giam.setText(tach_ngay(snapshot.child("thoi_gian").getValue().toString()));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }if(monAn.getLoai_giamgia()==1){
            databaseReference.child("Khuyen_mai").child("Tien").child( monAn.getId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int giatri=Integer.parseInt(snapshot.child("gia_tri").getValue().toString());
                            holder.gia_tri_giam.setText("Giảm:"+giatri+".đ");
                            int gia_saogiam=  ((Integer.parseInt(monAn.getGia())-giatri));
                            holder.giagiam.setText(goiMonActivity.VDN(gia_saogiam+""));

                            holder.thoi_gian_giam.setText(tach_ngay(snapshot.child("thoi_gian").getValue().toString()));
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }
        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStt(holder.getBindingAdapterPosition());
                if(holder.check.isChecked()==true){
                    mondatron.add( arrayList.get(stt));
                }else {
                    mondatron.remove(arrayList.get(stt));
                }

                //Toast.makeText(mcontext,  arrayList.get(stt).getCheck()+"/"+stt, Toast.LENGTH_SHORT).show();
            }
        });
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setStt(holder.getBindingAdapterPosition());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class  ViewHoler  extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        ImageView imgmonan2;
        TextView textgia2,textten2,gia_tri_giam,giagiam,thoi_gian_giam;
        LinearLayout linearLayout;
        CheckBox check;

        public ViewHoler(@NonNull View itemView) {
            super(itemView);
            imgmonan2=itemView.findViewById(R.id.item_img_giamgia);
            textgia2=itemView.findViewById(R.id.item_text_gia_giamgia);
            textten2=itemView.findViewById(R.id.item_text_ten_giamgia);
            giagiam=itemView.findViewById(R.id.item_text_giasogiam);
            check=itemView.findViewById(R.id.item_check_giamgia);
            gia_tri_giam=itemView.findViewById(R.id.item_text_gtkguyenmai);
            linearLayout=itemView.findViewById(R.id.item_linear_giamgia);
            thoi_gian_giam=itemView.findViewById(R.id.item_text_tg_khuyenmai);
            linearLayout.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
if(kieu==1){
    Activity activity= (Activity) v.getContext();
    activity.getMenuInflater().inflate(R.menu.menupopo,menu);
}



        }
    }
    private String tach_ngay(String date){
        String a[]=date.split(",");
        String a0[]=a[0].split("/");
        String a1[]=a[1].split("/");
        String newday="";
        if(a0[2].equals(a1[2])){
            if(a0[1].equals(a1[1])){
                newday=a0[0]+"-"+a1[0]+"/"+a0[1]+"/"+a0[2];
            }else  newday=a0[0]+"/"+a0[1]+"-"+a1[0]+"/"+a1[1]+",Năm "+a0[2];
        }else newday=a[0]+"-"+a[1];
        return newday;
    }
}
