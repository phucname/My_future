package com.example.doan.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.doan.Activity.DangKyActivity;
import com.example.doan.Activity.DangNhapActivity;
import com.example.doan.Activity.GoiMonActivity;
import com.example.doan.Activity.TrangChuActivuty;
import com.example.doan.Class.classHoaDon;
import com.example.doan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_bep  extends BaseAdapter implements View.OnCreateContextMenuListener {
    Context context;
    List<classHoaDon> bepList;

    int layout;
    TextView Tongtien;
    int positionnew;
    String id_nhahang;
 GoiMonActivity goiMonActivity=new GoiMonActivity();
    public int getPosition() {
        return positionnew;
    }

    public void setPosition(int position) {
        this.positionnew = position;
    }

    public Adapter_bep(Context context, List<classHoaDon> bepList, int layout, TextView tong,String id_nhahang) {
        this.context = context;
        this.bepList = bepList;
        this.layout = layout;
        Tongtien=tong;

        this.id_nhahang=id_nhahang;
    }

    @Override
    public int getCount() {

            return bepList.size();


    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=layoutInflater.inflate(layout,null);
        TextView ten=convertView.findViewById(R.id.text_tenmon_bep);
       // TextView gia=convertView.findViewById(R.id.text_giamon_bep);
        TextView soluong=convertView.findViewById(R.id.text_slmon_bep);
        TextView tt=convertView.findViewById(R.id.text_tinhtrangmon_bep);
        TextView date=convertView.findViewById(R.id.text_thoigian_bep);
        TextView ban=convertView.findViewById(R.id.text_tenban_bep);
        ImageView chat=convertView.findViewById(R.id.img_chat_itembep);

        // TextView tonggia=view.findViewById(R.id.tonggiamon);
        ImageView anh=convertView.findViewById(R.id.img_mon_bep);
        LinearLayout linearLayout=convertView.findViewById(R.id.linearlayout_bep);

          classHoaDon hoaDon = bepList.get(i);
          if(hoaDon.getChat().equals("")){
              chat.setVisibility(View.INVISIBLE);
          }
          chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert=new AlertDialog.Builder(context);
                alert.setTitle("Ghi chú món ăn!:");
                alert.setMessage(hoaDon.getChat());

                alert.show();
            }
        });
           if(hoaDon.getLoai()==0){
          DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                  .child("NhaHang").child(id_nhahang);
          databaseReference.child("DS_MonAn").child(hoaDon.getId_mon()).addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  ten.setText(snapshot.child("tenmon").getValue().toString());
                  Picasso.get().load(snapshot.child("imgmon").getValue().toString()).into(anh);
                 // gia.setText("Giá:"+goiMonActivity.VDN(snapshot.child("gia").getValue().toString()));
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });
          databaseReference.child("Ban").child(hoaDon.getId_ban()).addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                   ban.setText("Bàn:"+snapshot.child("masoban").getValue().toString());
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });
          //  gia.setText("Bàn số:"+hoaDon.getSoban());
          soluong.setText("Số Lượng:" + hoaDon.getSoluong());
          tt.setText("Tình Trạng:" + hoaDon.getTinhtrang());

          String[] hoe = hoaDon.getDate_goimon().split("/");
          date.setText(hoe[1] + "h" + hoe[0] + "p");
          // tonggia.setText("Tổng Tiền:"+goiMonActivity.VDN(Integer.parseInt(hoaDon.getGia())*Integer.parseInt(hoaDon.getSoLuong())+""));

      }else {
          DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                  .child("NhaHang").child(id_nhahang);
          databaseReference.child("DS_ComBo").child(hoaDon.getId_mon()).addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  ten.setText(snapshot.child("ten_combo").getValue().toString());
                  Picasso.get().load(snapshot.child("img_combo").getValue().toString()).into(anh);
                 // gia.setText("Giá:"+goiMonActivity.VDN(snapshot.child("gia_combo").getValue().toString()));
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });
               databaseReference.child("Ban").child(hoaDon.getId_ban()).addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       ban.setText("Bàn Số:"+snapshot.child("masoban").getValue().toString());

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
          soluong.setText("Số Lượng:" + hoaDon.getSoluong());
          tt.setText("Tình Trạng:" + hoaDon.getTinhtrang());

               String[] hoe =hoaDon.getDate_goimon().split("/");
               date.setText(hoe[1] + "h" + hoe[0] + "p");
          // tonggia.setText("Tổng Tiền:"+goiMonActivity.VDN(Integer.parseInt(hoaDon.getGia())*Integer.parseInt(hoaDon.getSoLuong())+""));




      }

        linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(i);
              //  Toast.makeText(context, getPosition() + "", Toast.LENGTH_SHORT).show();
                return false;
            }

        });

         linearLayout.setOnCreateContextMenuListener(this);
            return convertView;
        }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Activity activity= (Activity) v.getContext();
        activity.getMenuInflater().inflate(R.menu.menu_bep,menu);
     if(bepList.get(0).getTinhtrang().equals("Đã Nhận")){
       menu.getItem(0).setVisible(false);

     }else menu.getItem(1).setVisible(false);



    }
}
