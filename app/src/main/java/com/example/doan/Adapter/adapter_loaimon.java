package com.example.doan.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.doan.Class.class_loai;
import com.example.doan.R;

import java.util.ArrayList;

public class adapter_loaimon extends BaseAdapter implements View.OnCreateContextMenuListener {
   ArrayList<class_loai>arrayList_loai;
   Context mcontex;
    int layout;
    int i;

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public adapter_loaimon(ArrayList<class_loai> arrayList_loai, Context mcontex, int layout) {
        this.arrayList_loai = arrayList_loai;
        this.mcontex = mcontex;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return arrayList_loai.size();
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
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) mcontex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=layoutInflater.inflate(layout,null);
        TextView ten=view.findViewById(R.id.item_text_loai);
        LinearLayout linearLayout=view.findViewById(R.id.item_linear_loai);
        class_loai a=arrayList_loai.get(position);
        ten.setText(a.getTen());
        linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
               setI(position);
                return false;
            }
        });
        linearLayout.setOnCreateContextMenuListener(this);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Activity activity= (Activity) v.getContext();
        activity.getMenuInflater().inflate(R.menu.menupopo,menu);
    }
}
