package com.example.doan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.doan.Adapter.Adapter_Quangcao;
import com.example.doan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class QuangCao_Activity extends AppCompatActivity {
ViewPager2 viewPager2;
CircleIndicator3 circleIndicator3;
    List<String>list;
private Handler handler=new Handler(

);
private Runnable runnable=new Runnable() {
    @Override
    public void run() {
        if(viewPager2.getCurrentItem()==list.size()-1){
            viewPager2.setCurrentItem(0);
        }else  viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);

    }
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quang_cao);
        viewPager2=findViewById(R.id.viewpaper);
        circleIndicator3=findViewById(R.id.circle_center);
      getListphoto();
       // Toast.makeText(this, listphoto.size()+"", Toast.LENGTH_SHORT).show();


    }
    private void getListphoto(){
     list=new ArrayList<>();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("NhaHang")
                .child("K7W2iGHfE2O138Xa9UefCz9JEXM2").child("QuangCao");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    list.add(dataSnapshot.getValue().toString());
                    Toast.makeText(QuangCao_Activity.this, dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                }
                Adapter_Quangcao adapter_quangcao=new Adapter_Quangcao(list);
                viewPager2.setAdapter(adapter_quangcao);
                circleIndicator3.setViewPager(viewPager2);
                viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        handler.removeCallbacks(runnable);
                        handler.postDelayed(runnable,3000);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}