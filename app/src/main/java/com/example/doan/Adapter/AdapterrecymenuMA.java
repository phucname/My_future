package com.example.doan.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
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

public class AdapterrecymenuMA extends RecyclerView.Adapter<AdapterrecymenuMA.ViewHoler> implements Filterable {
    private   ArrayList<MonAn>arrayList;
    private   ArrayList<MonAn>arrayListold;
    private final  int RESOURCE_ID;
    Context mcontext;
    int positin;
    int test;
    String key;

    public void setTest(int test) {
        this.test = test;
    }

    public int getTest() {
        return test;
    }

    public int getPositin() {
        return positin;
    }

    public ArrayList<MonAn> getArrayList() {
        return arrayList;
    }

    public void setPositin(int positin) {
        this.positin = positin;
    }
public  void set_arraynew(ArrayList<MonAn>monAnArrayList){
        arrayList=monAnArrayList;
        notifyDataSetChanged();
}
    public AdapterrecymenuMA(ArrayList<MonAn> arrayList, int RESOURCE_ID, Context context,int test,String key) {
        this.arrayList = arrayList;
        this.arrayListold=arrayList;
        this.RESOURCE_ID = RESOURCE_ID;
        mcontext=context;
        this.test=test;
        this.key=key;
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
     final MonAn monAn=arrayList.get(holder.getBindingAdapterPosition());
      GoiMonActivity goiMonActivity=new GoiMonActivity();
if(monAn.getTenmon().length()>14){
    holder.textten2.setText(monAn.getTenmon().substring(0,12)+"...");
}else {
    holder.textten2.setText(monAn.getTenmon());
}

      holder.textgia2.setText(goiMonActivity.VDN(monAn.getGia()));
      Picasso.get().load(monAn.getImgmon()).into(holder.imgmonan2);
      //holder.textten2.setPaintFlags(holder.textten2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
     holder.linearLayout.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
//             File myInternalFile;
//             ContextWrapper contextWrapper = new ContextWrapper(mcontext);
//             //Tạo (Hoặc là mở file nếu nó đã tồn tại) Trong bộ nhớ trong có thư mục là ThuMucCuaToi.
//             File directory = contextWrapper.getDir("TK", Context.MODE_APPEND);//tên Thư Mục
//             myInternalFile = new File(directory, "Gio_Hang.txt");
//           int i=0;
//             if(myInternalFile.exists()==true) {
//                 try {
//                     FileInputStream inputStream = new FileInputStream(myInternalFile);
//                     DataInputStream in = new DataInputStream(inputStream);
//                     BufferedReader br = new BufferedReader(
//                             new InputStreamReader(in));
//
//                     String strLine;
//                     while ((strLine = br.readLine()) != null) {
//                         String[] a = strLine.split(",");
//                       if(a[0].equals(monAn.getId())){
//                           Toast.makeText(contextWrapper, "Món Ăn Đã Có Ở Giỏ Hàng", Toast.LENGTH_SHORT).show();
//                        i++;
//                       }
//
//                     }
//                     if(i==0){
//                         MianActivityBanac mianActivityBanac = (MianActivityBanac) mcontext;
//                         FragmentTransaction transaction=mianActivityBanac.getSupportFragmentManager()
//                                 .beginTransaction();
//                         Bundle bundle=new Bundle();
//                         bundle.putSerializable("goimon",monAn);
//                         bundle.putString("id_nhahang",mianActivityBanac.getId_nhahang());
//                         GoiMonActivity goiMonActivity1=new GoiMonActivity();
//                         goiMonActivity1.setArguments(bundle);
//                         transaction.replace(R.id.container,goiMonActivity1);
//                         transaction.commit();
//                     }
//                 } catch (FileNotFoundException e) {
//                     e.printStackTrace();
//                 } catch (IOException e) {
//                     e.printStackTrace();
//                 }
//
//             }else {
                 MianActivityBanac mianActivityBanac = (MianActivityBanac) mcontext;
                 FragmentTransaction transaction=mianActivityBanac.getSupportFragmentManager()
                         .beginTransaction();
                 Bundle bundle=new Bundle();
                 bundle.putSerializable("goimon",monAn);
                 bundle.putString("id_nhahang",mianActivityBanac.getId_nhahang());
                 GoiMonActivity goiMonActivity1=new GoiMonActivity();
                 goiMonActivity1.setArguments(bundle);
                 transaction.replace(R.id.container,goiMonActivity1);

                 transaction.commit();
           //  }






         }
     });
     holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
         @Override
         public boolean onLongClick(View v) {

             setPositin(holder.getBindingAdapterPosition());

             return false;
         }
     });DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference()
                .child("NhaHang").child(key);
    if(monAn.getLoai_giamgia()!=2){
        holder.giagiam.setVisibility(View.VISIBLE);
        holder.giam_gia.setVisibility(View.VISIBLE);
        holder.textgia2.setPaintFlags(holder.textgia2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

    }
     if(monAn.getLoai_giamgia()==0){

         databaseReference.child("Khuyen_mai").child("PT").child( monAn.getId())
                 .addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                         int giatri=Integer.parseInt(snapshot.child("gia_tri").getValue().toString());
                         holder.giam_gia.setText("Giảm:"+giatri+"%");
                         String gia_saogiam= String.valueOf(((Integer.parseInt(monAn.getGia())/100)*(100-giatri)));
                           char[] a =gia_saogiam.toCharArray();
                           for (int i=gia_saogiam.length()-3;i<gia_saogiam.length();i++){
                               a[i]='0';
                           }
                           gia_saogiam=new String(a);
                             holder.giagiam.setText(goiMonActivity.VDN(gia_saogiam));
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });
     }
     if(monAn.getLoai_giamgia()==1){
            databaseReference.child("Khuyen_mai").child("Tien").child( monAn.getId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int giatri=Integer.parseInt(snapshot.child("gia_tri").getValue().toString());
                            holder.giam_gia.setText("Giảm:"+giatri+".đ");
                           int gia_saogiam=  ((Integer.parseInt(monAn.getGia())-giatri));
                            holder.giagiam.setText(goiMonActivity.VDN(gia_saogiam+""));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
               String stseach =constraint.toString();
               if(stseach.isEmpty()){
                   arrayList=arrayListold;
               }else {
                   ArrayList<MonAn>list=new ArrayList<>();
                   for (MonAn monAn:arrayListold){
                     if( monAn.getTenmon().toLowerCase().contains(stseach.toLowerCase())){
                         list.add(monAn);
                        // Toast.makeText(mcontext.getApplicationContext(), ten+"", Toast.LENGTH_SHORT).show();
                     }
                   }
                   arrayList=list;
               }
                FilterResults filterResults=new FilterResults();
                filterResults.values=arrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayList= (ArrayList<MonAn>) results.values;
            notifyDataSetChanged();
            }
        };
    }

    public class  ViewHoler  extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
 ImageView imgmonan2;
 TextView textgia2,textten2,giam_gia,giagiam;
 LinearLayout linearLayout;

    public ViewHoler(@NonNull View itemView) {
        super(itemView);
        imgmonan2=itemView.findViewById(R.id.imgmenubanan);
        textgia2=itemView.findViewById(R.id.textgia2);
        textten2=itemView.findViewById(R.id.texttenmonan2);
        giagiam=itemView.findViewById(R.id.textgia_giam);
       giam_gia=itemView.findViewById(R.id.giam_gia);
        linearLayout=itemView.findViewById(R.id.linear_item_combo);
        linearLayout.setOnCreateContextMenuListener(this);
    }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
           if(getTest()==0){

           }else {
               Activity activity= (Activity) v.getContext();
               activity.getMenuInflater().inflate(R.menu.menupopo,menu);
           }

        }
    }
}
