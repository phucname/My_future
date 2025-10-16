package com.example.doan.Adapter;



import android.app.Activity;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.doan.Class.NhanVien;
import com.example.doan.Class.classHoaDon;
import com.example.doan.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapte_lits_NV extends BaseAdapter implements View.OnCreateContextMenuListener {
    Context context;
    List<NhanVien> Listnhavien;
    int layout;
    int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Adapte_lits_NV(Context context, List<NhanVien> listnhavien, int layout) {
        this.context = context;
        Listnhavien = listnhavien;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return Listnhavien.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=layoutInflater.inflate(layout,null);
        TextView ten=convertView.findViewById(R.id.textview_tennv);
        TextView sdt=convertView.findViewById(R.id.textview_sdtnv1);
        TextView chucvu=convertView.findViewById(R.id.textview_chucvunv);
        CircleImageView imgnhanvien=convertView.findViewById(R.id.img_nhanvien);
        LinearLayout linearLayout_itemnv=convertView.findViewById(R.id.liner_itemnv);
        NhanVien nhanVien=Listnhavien.get(position);
        ten.setText("Tên:"+nhanVien.getTenNV());
        sdt.setText( "SDT:"+nhanVien.getSdtnv());

  if(nhanVien.getAnhnv().equals("")){

  }else {
      Picasso.get().load(nhanVien.getAnhnv()).into(imgnhanvien);
  }


       if(nhanVien.getChucVuNV()==1){
           chucvu.setText("Chức Vụ:Quản Lý");
       }else if(nhanVien.getChucVuNV()==0) chucvu.setText("Chức Vụ:Nhân Viên");
       if(nhanVien.getChucVuNV()==2)chucvu.setText("Chức Vụ:Đầu Bếp" );
linearLayout_itemnv.setOnLongClickListener(new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View v) {
        setPosition(position);
        return false;
    }
});
   linearLayout_itemnv.setOnCreateContextMenuListener(this);

        return convertView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Activity activity= (Activity) v.getContext();
        activity.getMenuInflater().inflate(R.menu.menupopo,menu);
        menu.getItem(0).setVisible(false);
    }
}
