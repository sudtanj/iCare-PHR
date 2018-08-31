package sud_tanj.com.icare.Frontend.Fragment.DataCatalogue.DataDetail;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
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

import java.util.List;

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

    @OptionsItem(android.R.id.home)
    protected void backButtonClicked(){
        this.onStop();
        finish();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        MonitoringInformation monitoringInformation=dataSnapshot.getValue(MonitoringInformation.class);
        if(!monitoringInformation.getHealthDatas().isEmpty()) {
            initCurrentData(monitoringInformation.getHealthDatas().get(
                    monitoringInformation.getHealthDatas().size()-1)
                    , monitoringInformation.getGraphLegend());
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
