package com.example.doan.Activity;



import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.provider.ContactsContract;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.Activity.MianActivityBanac;
import com.example.doan.Adapter.Adapter_Quangcao;
import com.example.doan.Adapter.Adapter_combo;
import com.example.doan.Adapter.AdapterrecymenuMA;
import com.example.doan.Class.MonAn;
import com.example.doan.Activity.ThemMonAn;
import com.example.doan.Class.NhaHang;
import com.example.doan.Class.class_combo;
import com.example.doan.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator3;

/**
 * A simple {@link Fragment} subclass.

 */
public class MenuBananFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
RecyclerView recyclerView,recyclerView1;
public ArrayList<MonAn>arrayList;
AdapterrecymenuMA adapterrecymenuMA;
    DatabaseReference databaseReference;

private MianActivityBanac mianActivityBanac;

CircleImageView img_backgound;
ArrayList<class_combo>class_comboArrayList;
    String a;
    String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
TextView tennh,dcnh,sdtnh;
    public String getA() {
        return a;
    }
    public void setA(String a) {
        this.a = a;
    }
    ViewPager2 viewPager2;
    CircleIndicator3 circleIndicator3;
    List<String> list;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view=inflater.inflate(R.layout.fragment_menu_banan, container, false);
         recyclerView=view.findViewById(R.id.recyview);
         tennh=view.findViewById(R.id.textview_tennh);
       dcnh=view.findViewById(R.id.textview_diachinhahang);
        sdtnh=view.findViewById(R.id.textview_sdtnh);
        // Toast.makeText(getActivity(),"ok",Toast.LENGTH_SHORT).show();
        viewPager2=view.findViewById(R.id.viewpaper);
        circleIndicator3=view.findViewById(R.id.circle_center);
        img_backgound=view.findViewById(R.id.img_bacgound);
        recyclerView1=view.findViewById(R.id.recyclerview_combo);
       // searchView_monan=view.findViewById(R.id.search_monan);
        // Toast.makeText(this, listphoto.size()+"", Toast.LENGTH_SHORT).show()
          mianActivityBanac= (MianActivityBanac) getActivity();
          setA(mianActivityBanac.getId_nhahang());

        getListphoto();
         // backgound=view.findViewById(R.id.img_backgound);

        // getArguments().getString("key");
       //FirebaseAuth.getInstance().signOut();hàm đăng xuất
      //  user=FirebaseAuth.getInstance().getCurrentUser();

        //nếu là quản lý mới có quyền thêm món ăn ==null chính ta đây k pải tk quản lý
     //Toast.makeText(view.getContext(),mianActivityBanac.getA()+"",Toast.LENGTH_LONG).show();
//      if(user!=null){
//          databaseReference=FirebaseDatabase.getInstance().getReference().child("NhaHang").child(user.getUid());
//         // Toast.makeText(view.getContext(),user.getUid(),Toast.LENGTH_LONG).show();
//      }else{
//          float_themmon.setVisibility(View.INVISIBLE);
         databaseReference=FirebaseDatabase.getInstance()
                 .getReference().child("NhaHang").child(getA());
//      }

      databaseReference.child("TT_NhaHang").addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {



              NhaHang nhaHang  =new NhaHang(  snapshot.child("ten_nhahang").getValue().toString()
                      ,snapshot.child("diachi_nhahang").getValue().toString(),
                      snapshot.child("_sdt_nhahangi").getValue().toString(),
                      snapshot.child("mg_nhahang").getValue().toString(),

                      snapshot.child("huyen").getValue().toString()
                      , snapshot.child("tinh").getValue().toString()
                      , snapshot.child("xa").getValue().toString());
             if(snapshot.child("mg_nhahang").getValue().toString()!=null){
                 Picasso.get().load(snapshot.child("mg_nhahang").getValue().toString()).into(img_backgound);
             }

                   tennh.setText(nhaHang.getTen_nhahang());
                   dcnh.setText(nhaHang.getDiachi_nhahang());
                   sdtnh.setText("Phone:"+nhaHang.get_sdt_nhahangi());

          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });
        Query query=databaseReference.child("DS_MonAn").limitToFirst(6);
      query.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                    arrayList=new ArrayList<>(6);
                 for(DataSnapshot snapshot1:snapshot.getChildren()){
                    MonAn monAn=new  MonAn(snapshot1.child("tenmon").getValue().toString(),
                             snapshot1.child("gia").getValue().toString(),
                             snapshot1.child("ghichu").getValue().toString(),
                             snapshot1.child("imgmon").getValue().toString(),
                             snapshot1.child("id").getValue().toString(),
                             snapshot1.child("loai_mon").getValue().toString(),
                            Integer.parseInt(snapshot1.child("loai_giamgia").getValue().toString()));

                       arrayList.add(monAn);

                    // Toast.makeText(getActivity(),arrayListtest.get(0).getGia()+"",Toast.LENGTH_SHORT).show();
                 } adapterrecymenuMA=new AdapterrecymenuMA(arrayList,R.layout.carrecymenu,view.getContext(),0,mianActivityBanac.getId_nhahang());
                 GridLayoutManager gridLayoutManager=new GridLayoutManager(view.getContext(), 2);
                //  LinearLayoutManager gridLayoutManager=new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL,false);
                  recyclerView.setLayoutManager(gridLayoutManager);
                  recyclerView.setAdapter(adapterrecymenuMA);
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });

//searchView_monan.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//       arrayList_monan(newText);
//        return true;
//    }
//});
            databaseReference.child("DS_ComBo").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    class_comboArrayList=new ArrayList<>(6);
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        class_combo combo=new class_combo(dataSnapshot.child("id_combo").getValue().toString()
                        ,dataSnapshot.child("gia_combo").getValue().toString()
                                ,dataSnapshot.child("ten_combo").getValue().toString(),
                                dataSnapshot.child("img_combo").getValue().toString()
                        ,Integer.parseInt(dataSnapshot.child("loai_giamgia").getValue().toString()));
                        class_comboArrayList.add(combo);
                        Adapter_combo adapter_combo=new Adapter_combo(class_comboArrayList,R.layout.carrecymenu,getContext(),mianActivityBanac.getId_nhahang(),1);
                        GridLayoutManager gridLayoutManager=new GridLayoutManager(view.getContext(), 2);
                        recyclerView1.setLayoutManager(gridLayoutManager);
                        recyclerView1.setAdapter(adapter_combo);
                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


       return view;
    }




//    @Override
//    public boolean onContextItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.Sua:
//             if(mianActivityBanac.getChuvu_nv()==1){
//                 Intent intent=new Intent(getActivity(),ThemMonAn.class);
//                Bundle bundle=new Bundle();
//                MonAn monAn=arrayList.get(adapterrecymenuMA.getPositin());
//                bundle.putSerializable("classsua",monAn);
//                intent.putExtras(bundle);
//                 intent.putExtra("id_nhahang",mianActivityBanac.getId_nhahang());
//
//                 startActivity(intent);
//             }else Toast.makeText(getActivity(),"Bạn Không Phải Là Adim Nên Không Thể Thực Hiện Thao Tác!",Toast.LENGTH_LONG).show();
//
//
//                break;
//
//            case R.id.Xoa:
//                if(mianActivityBanac.getChuvu_nv()==1){
//                Toast.makeText(getActivity(),"xoa",Toast.LENGTH_SHORT).show();
//                AlertDialog.Builder aler=new AlertDialog.Builder(getContext());
//                aler.setTitle("Thông Báo");
//                aler.setMessage("Bạn có muốn xóa món ăn này khỏi thực đơn");
//                aler.setPositiveButton("Có", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        FirebaseStorage storage=FirebaseStorage.getInstance();
////                        StorageReference storageReference=storage.getReferenceFromUrl("gs://doan-f52ef.appspot.com/");
////
////                        StorageReference mytorage=storageReference.child("DanhSachAnh/"+snapshot.child("tenAnh"));
////                        mytorage.delete();
//                        databaseReference.child("DS_MonAn").child(arrayList.get(adapterrecymenuMA.getPositin()).getId()+"").removeValue();
////                        Toast.makeText(getActivity().getApplicationContext(), arrayList.get(adapterrecymenuMA.getPositin()).getId(),Toast.LENGTH_LONG).show();
//                    }
//                });
//                aler.show();
//                }else Toast.makeText(getActivity(),"Bạn Không Phải Là Adim Nên Không Thể Thực Hiện Thao Tác!",Toast.LENGTH_LONG).show();
//            case R.id.sua_combo:
//
//                break;
//            case R.id.xoa_combo:
//
//                break;
//
//        }
//
//        return super.onContextItemSelected(item);
//    }
                                    //nút tìm kiếm
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//      setHasOptionsMenu(true);
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.seach,menu);
//        MenuItem menuItem=menu.findItem(R.id.menu_search);
//        SearchView searchView= (SearchView) menuItem.getActionView();
//
//
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//           @Override
//           public boolean onQueryTextSubmit(String query) {
//               adapterrecymenuMA.getFilter().filter(query);
//
//               return false;
//           }
//
//           @Override
//           public boolean onQueryTextChange(String newText) {
//            // adapterrecymenuMA.getFilter().filter(newText);
//               return false;
//           }
//       });
//        super.onCreateOptionsMenu(menu, inflater);
//    }
    private void getListphoto(){
        list=new ArrayList<>();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("NhaHang")
                .child(mianActivityBanac.getId_nhahang()).child("DS_ImgQuangCao");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             if(snapshot.getValue()==null){
                         list.add("https://firebasestorage.googleapis.com/v0/b/doan-f5" +
                                 "2ef.appspot.com/o/ig.png?alt=media&token=6afaf343-25a1-40a1-" +
                                 "b83a-8a65c601e855&_gl=1*y7g7y6*_ga*NTE4MTkwNjgxLjE2Njk3ODA2" +
                                 "MDY.*_ga_CW55HF8NVT*MTY4NTQzNTM4OS4yMC4xLjE2ODU0MzcyNTQuMC4wLjA.");
             }else {
                 for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                     list.add(dataSnapshot.getValue().toString());

                 }

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