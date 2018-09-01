package sud_tanj.com.icare.Frontend.Fragment.DataCatalogue.DataDetail;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import sud_tanj.com.icare.Backend.Database.Monitoring.MonitoringInformation;
import sud_tanj.com.icare.Backend.Database.PersonalData.HealthData;
import sud_tanj.com.icare.R;

@EActivity(R.layout.activity_data_details)
public class DataDetails extends AppCompatActivity implements ValueEventListener {

    @Extra("MonitorId")
    protected String id;
    protected CurrentDataAdapter currentDataAdapter;
    @ViewById(R.id.current_data_recycler_view)
    protected SuperRecyclerView firstRecyclerView;
    @ViewById(R.id.data_line_chart)
    protected LineChart lineChartView;

    @AfterExtras
    protected void initActionBar(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));
    }

    @AfterViews
    protected void initUI(){
        FirebaseDatabase.getInstance().getReferenceFromUrl(MonitoringInformation.KEY).child(id)
                .addValueEventListener(this);
    }

    protected void initCurrentData(String healthId,List<String> units){
        LinearLayoutManager firstManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        firstRecyclerView.setLayoutManager(firstManager);
        Query query = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(HealthData.KEY)
                .child(healthId)
                .child("dataList");

        FirebaseRecyclerOptions<Double> options =
                new FirebaseRecyclerOptions.Builder<Double>()
                        .setQuery(query, Double.class)
                        .setLifecycleOwner(this)
                        .build();
        currentDataAdapter=new CurrentDataAdapter(options,units);
        firstRecyclerView.setAdapter(currentDataAdapter);
    }

    private void initGraph(final MonitoringInformation monitoringInformation){
        lineChartView.getAxisLeft().setDrawGridLines(false);
        lineChartView.getXAxis().setDrawGridLines(false);
        lineChartView.getAxisRight().setDrawGridLines(false);
        //one month ago milliseconds
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.add(Calendar.MONTH, -1);
        long result = c.getTimeInMillis();
        FirebaseDatabase.getInstance().getReferenceFromUrl(HealthData.KEY)
                .orderByChild("timeStamp").startAt(result)
                .endAt(Calendar.getInstance().getTimeInMillis()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Entry> entries = new ArrayList<Entry>();
                        int j=0;
                        for(DataSnapshot temp:dataSnapshot.getChildren()){
                            if(monitoringInformation.getHealthDatas().indexOf(temp.getKey())>-1){
                                HealthData healthData=temp.getValue(HealthData.class);
                                for(int i=0;i<healthData.getDataList().size();i++) {
                                    entries.add(new Entry(j,healthData.getDataList().get(i).floatValue()));
                                }
                                j++;
                            }
                        }
                        LineDataSet dataSet = new LineDataSet(entries, "Steps");
                        LineData lineData = new LineData(dataSet);
                        lineChartView.setData(lineData);
                        lineChartView.invalidate();
                     }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }

    @OptionsItem(android.R.id.home)
    protected void backButtonClicked(){
        this.onStop();
        finish();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        MonitoringInformation monitoringInformation=dataSnapshot.getValue(MonitoringInformation.class);
        setTitle(getString(R.string.details_information_title,monitoringInformation.getName()));
        if(!monitoringInformation.getHealthDatas().isEmpty()) {
            initCurrentData(monitoringInformation.getHealthDatas().get(
                    monitoringInformation.getHealthDatas().size()-1)
                    , monitoringInformation.getGraphLegend());
            initGraph(monitoringInformation);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
