package com.example.doan.Activity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.doan.Adapter.adapter_spienr;
import com.example.doan.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class thongke_cot_Fragment extends Fragment {

Spinner spinner_nam;
ArrayList<String>arrayListnam;
adapter_spienr adapter_spienr_nam;
int nam;
Button button;
    BarChart chart;
int tong;
    int tong_thang = 0;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_thongke_cot_, container, false);
       spinner_nam=view.findViewById(R.id.tk_nam);
       getnam();
       button=view.findViewById(R.id.ok);
        adapter_spienr_nam =new adapter_spienr(getContext(),R.layout.selct_spiner,arrayListnam,"Năm");
        spinner_nam.setAdapter(adapter_spienr_nam);
        spinner_nam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nam=Integer.parseInt(arrayListnam.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        drawBarChart(view);
    }
});

       return  view;
    }
    private void getnam() {
        arrayListnam=new ArrayList<>();
        arrayListnam.add("2019");
        arrayListnam.add("2020");
        arrayListnam.add("2021");
        Calendar calendar=Calendar.getInstance();
        if(2021<calendar.get(Calendar.YEAR)){
            for(int i= 2022;i<=calendar.get(Calendar.YEAR);i++){
                arrayListnam.add(i+"");
            }
        }

    }



        private void drawBarChart(View view) {
           chart = view.findViewById(R.id.piechart_cot); // Replace with your chart view

            // Customize chart appearance
            chart.getDescription().setEnabled(false); // Hide chart description

            // Customize X-axis
            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Position X-axis at the bottom

            // Customize Y-axis
            YAxis yAxisLeft = chart.getAxisLeft();
            YAxis yAxisRight = chart.getAxisRight();
            yAxisRight.setEnabled(false); // Disable right Y-axis

            // Customize chart data
            List<BarEntry> entries = new ArrayList<>();

            DatabaseReference data= FirebaseDatabase.getInstance().getReference()
                    .child("NhaHang").child(user.getUid()).child("Thongke").child(nam+"");
            tong=0;

            data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue()!=null){
                        for(DataSnapshot snapshot_thang:snapshot.getChildren()){
                            tong_thang=0;
                            for(DataSnapshot snapshot_ngay:snapshot_thang.getChildren()){

                                data.child(snapshot_thang.getKey())
                                        .child(snapshot_ngay.getKey()).child("mon").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        for (DataSnapshot snapshot1:snapshot.getChildren()){

                                            tong_thang=tong_thang+
                                                    (Integer.parseInt(snapshot1.child("gia").getValue().toString())
                                                            * Integer.parseInt(snapshot1.child("soluong").getValue().toString()));
                                            Toast.makeText(getActivity(), tong_thang+"mon/"+snapshot_ngay.getKey(), Toast.LENGTH_SHORT).show();
                                            if(entries.size()==0) {
                                                entries.add(new BarEntry(Integer.parseInt(snapshot_thang.getKey()),
                                                        tong_thang));
                                                BarDataSet dataSet = new BarDataSet(entries, "Data"); // Create a dataset with your data
                                                dataSet.setColor(Color.BLUE); // Set bar color

                                                BarData barData = new BarData(dataSet);
                                                chart.setData(barData);
                                                chart.setFitBars(true); // Adjust bar width to fit the chart

                                                // Customize chart animation (optional)
                                                chart.animateY(1000); // Animate bars vertically

                                                chart.invalidate(); // Refresh the chart to display the data
                                            }else {
                                                int i=0;
                                                int kd=0;
                                                for (BarEntry barEntry:entries){
                                                    if(barEntry.getX()==Integer.parseInt(snapshot_thang.getKey())){
                                                        entries.get(i).setY(tong_thang);
                                                        BarDataSet dataSet = new BarDataSet(entries, "Data"); // Create a dataset with your data
                                                        dataSet.setColor(Color.BLUE); // Set bar color

                                                        BarData barData = new BarData(dataSet);
                                                        chart.setData(barData);
                                                        chart.setFitBars(true); // Adjust bar width to fit the chart

                                                        // Customize chart animation (optional)
                                                        chart.animateY(1000); // Animate bars vertically
                                                        kd=1;
                                                        chart.invalidate(); // Refresh the chart to display the data
                                                        break;
                                                    }
                                                    i++;

                                                }
                                                if(kd==0){
                                                    entries.add(new BarEntry(Integer.parseInt(snapshot_thang.getKey()),
                                                            tong_thang));
                                                    BarDataSet dataSet = new BarDataSet(entries, "Data"); // Create a dataset with your data
                                                    dataSet.setColor(Color.BLUE); // Set bar color

                                                    BarData barData = new BarData(dataSet);
                                                    chart.setData(barData);
                                                    chart.setFitBars(true); // Adjust bar width to fit the chart

                                                    // Customize chart animation (optional)
                                                    chart.animateY(1000); // Animate bars vertically

                                                    chart.invalidate(); // Refresh the chart to display the data
                                                }
                                               }


                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                data.child(snapshot_thang.getKey())
                                        .child(snapshot_ngay.getKey()).child("combo").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                for (DataSnapshot snapshot1:snapshot.getChildren()){
                                                    tong_thang=tong_thang+
                                                            (Integer.parseInt(snapshot1.child("gia").getValue().toString())
                                                                    * Integer.parseInt(snapshot1.child("soluong").getValue().toString()));
                                                    Toast.makeText(getActivity(),tong_thang+ "combo/"+snapshot_ngay.getKey(), Toast.LENGTH_SHORT).show();
                                                    if(entries.size()==0) {
                                                        entries.add(new BarEntry(Integer.parseInt(snapshot_thang.getKey()),
                                                                tong_thang));
                                                        BarDataSet dataSet = new BarDataSet(entries, "Data"); // Create a dataset with your data
                                                        dataSet.setColor(Color.BLUE); // Set bar color

                                                        BarData barData = new BarData(dataSet);
                                                        chart.setData(barData);
                                                        chart.setFitBars(true); // Adjust bar width to fit the chart

                                                        // Customize chart animation (optional)
                                                        chart.animateY(1000); // Animate bars vertically

                                                        chart.invalidate(); // Refresh the chart to display the data
                                                    }else {
                                                        int i=0;
                                                        int kd=0;
                                                        for (BarEntry barEntry:entries){
                                                            if(barEntry.getX()==Integer.parseInt(snapshot_thang.getKey())){
                                                                entries.get(i).setY(tong_thang);
                                                                BarDataSet dataSet = new BarDataSet(entries, "Data"); // Create a dataset with your data
                                                                dataSet.setColor(Color.BLUE); // Set bar color

                                                                BarData barData = new BarData(dataSet);
                                                                chart.setData(barData);
                                                                chart.setFitBars(true); // Adjust bar width to fit the chart

                                                                // Customize chart animation (optional)
                                                                chart.animateY(1000); // Animate bars vertically
                                                                kd=1;
                                                                chart.invalidate(); // Refresh the chart to display the data
                                                                break;
                                                            }
                                                            i++;

                                                        }
                                                        if(kd==0){
                                                            entries.add(new BarEntry(Integer.parseInt(snapshot_thang.getKey()),
                                                                    tong_thang));
                                                            BarDataSet dataSet = new BarDataSet(entries, "Data"); // Create a dataset with your data
                                                            dataSet.setColor(Color.BLUE); // Set bar color

                                                            BarData barData = new BarData(dataSet);
                                                            chart.setData(barData);
                                                            chart.setFitBars(true); // Adjust bar width to fit the chart

                                                            // Customize chart animation (optional)
                                                            chart.animateY(1000); // Animate bars vertically

                                                            chart.invalidate(); // Refresh the chart to display the data
                                                        }
                                                    }


                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                Toast.makeText(getActivity(), "ngay", Toast.LENGTH_SHORT).show();

                            }
                            Toast.makeText(getActivity(), "thang", Toast.LENGTH_SHORT).show();


                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });

            // Add data entry with value



        }


        private void drawPieChart(View view) {
            PieChart chart = view.findViewById(R.id.piechart_cot); // Replace with your chart view

            // Customize chart appearance
            chart.getDescription().setEnabled(false); // Hide chart description
            chart.setUsePercentValues(true); // Display values as percentages
            chart.setDrawEntryLabels(false); // Disable labels on chart slices
            chart.setHoleRadius(40f); // Set the radius of the center hole
            chart.setTransparentCircleRadius(45f); // Set the radius of the transparent circle around the hole

            // Customize chart data
            List<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(30f, "Label 1")); // Add data entry with value and label
            entries.add(new PieEntry(20f, "Label 2"));
            entries.add(new PieEntry(50f, "Label 3"));

            PieDataSet dataSet = new PieDataSet(entries, "Data"); // Create a dataset with your data
            dataSet.setColors(Color.BLUE, Color.GREEN, Color.RED); // Set colors for chart slices

            PieData pieData = new PieData(dataSet);
            chart.setData(pieData);
            chart.invalidate(); // Refresh the chart to display the data
        }

        private void drawLineChart(View view) {
            LineChart chart =view.findViewById(R.id.piechart_cot); // Replace with your chart view

            // Customize chart appearance
            chart.getDescription().setEnabled(false); // Hide chart description
            chart.setDrawGridBackground(false); // Disable grid background
            chart.setTouchEnabled(true); // Enable touch gestures
            chart.setDragEnabled(true); // Enable drag gestures
            chart.setScaleEnabled(true); // Enable scaling gestures

            // Customize X-axis
            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Position X-axis at the bottom
            xAxis.setGranularity(1f); // Set the minimum interval between axis labels

            // Customize Y-axis
            YAxis yAxisLeft = chart.getAxisLeft();
            YAxis yAxisRight = chart.getAxisRight();
            yAxisRight.setEnabled(false); // Disable right Y-axis

            // Customize chart data
            List<Entry> entries = new ArrayList<>();
            entries.add(new Entry(0, 3));
            entries.add(new Entry(1, 5));
            entries.add(new Entry(2, 2));
            entries.add(new Entry(3, 7));

            LineDataSet dataSet = new LineDataSet(entries, "Data"); // Create a dataset with your data
            dataSet.setColor(Color.BLUE); // Set line color
            dataSet.setLineWidth(2f); // Set line width

            LineData lineData = new LineData(dataSet);
            chart.setData(lineData);
            chart.invalidate(); // Refresh the chart to display the data
        }

    }
