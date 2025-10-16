package com.example.doan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.doan.R;

import java.util.List;

public class adapter_spienr extends ArrayAdapter<String> {
    String title;
    public adapter_spienr(@NonNull Context context, int resource, @NonNull List<String> objects,String title) {
      super(context,resource,objects);
      this.title=title;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.selct_spiner,parent,false);
        TextView text=convertView.findViewById(R.id.text_selct_spinner);
        TextView titlee=convertView.findViewById(R.id.text_selct_spinner_title);
        titlee.setText(title);
        String nam=getItem(position);
        if(nam!=null){
            text.setText(nam);
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
      convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.itme_spiner,parent,false);
        TextView text=convertView.findViewById(R.id.item_spinner);
        String nam=getItem(position);
        if(nam!=null){
            text.setText(nam);
        }
        return convertView;
    }
}
