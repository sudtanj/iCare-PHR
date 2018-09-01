package sud_tanj.com.icare.Frontend.Fragment.DataCatalogue.DataDetail;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ArrayAdapter;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.mikephil.charting.charts.LineChart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.isapanah.awesomespinner.AwesomeSpinner;
import com.isapanah.awesomespinner.AwesomeSpinner.onSpinnerItemClickListener;
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
public class DataDetails extends AppCompatActivity implements ValueEventListener,onSpinnerItemClickListener<String> {

    public static final String REALTIME="Realtime";
    public static final String DAY="Day";
    public static final String MONTH="Month";
    public static final String YEAR="Year";
    @Extra("MonitorId")
    protected String id;
    protected CurrentDataAdapter currentDataAdapter;
    @ViewById(R.id.current_data_recycler_view)
    protected SuperRecyclerView firstRecyclerView;
    @ViewById(R.id.data_line_chart)
    protected LineChart lineChartView;
    @ViewById(R.id.show_by_spinner)
    protected AwesomeSpinner spinner;
    private MonitoringInformation monitoringInformation;
    private GraphEventListener graphEventListener=null;
    private Query firebaseQuery=null;

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

    @AfterViews
    protected void initSpinner(){
        List<String> spinnerList=new ArrayList<>();
        spinnerList.add(REALTIME);
        spinnerList.add(DAY);
        spinnerList.add(MONTH);
        spinnerList.add(YEAR);

        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(this
                , android.R.layout.simple_spinner_item, spinnerList);

        spinner.setAdapter(categoriesAdapter);
        spinner.setOnSpinnerItemClickListener(this);
        spinner.setSelection(REALTIME);
    }

    private void initCurrentData(String healthId,List<String> units){
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

    private void setGraphViewByRealtime(){
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.set(Calendar.HOUR_OF_DAY,0);
        long result = c.getTimeInMillis();
        firebaseQuery=FirebaseDatabase.getInstance().getReferenceFromUrl(HealthData.KEY)
                .orderByChild("timeStamp").startAt(result)
                .endAt(Calendar.getInstance().getTimeInMillis()).limitToLast(100);
        firebaseQuery.addListenerForSingleValueEvent(graphEventListener);
    }

    private void setGraphViewByMonth(){
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.add(Calendar.MONTH, -1);
        long result = c.getTimeInMillis();
        firebaseQuery=FirebaseDatabase.getInstance().getReferenceFromUrl(HealthData.KEY)
                .orderByChild("timeStamp").startAt(result)
                .endAt(Calendar.getInstance().getTimeInMillis());
        firebaseQuery.addListenerForSingleValueEvent(graphEventListener);
    }

    private void initGraph(){
        lineChartView.getAxisLeft().setDrawGridLines(false);
        lineChartView.getXAxis().setDrawGridLines(false);
        lineChartView.getAxisRight().setDrawGridLines(false);
        if(graphEventListener==null){
            graphEventListener=new GraphEventListener(monitoringInformation,lineChartView);
        }
    }

    private void updateView(){
        if(firebaseQuery==null){
            setGraphViewByRealtime();
        } else {
            firebaseQuery.addListenerForSingleValueEvent(graphEventListener);
        }
    }

    @OptionsItem(android.R.id.home)
    protected void backButtonClicked(){
        this.onStop();
        finish();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        monitoringInformation=dataSnapshot.getValue(MonitoringInformation.class);
        setTitle(getString(R.string.details_information_title,monitoringInformation.getName()));
        if(!monitoringInformation.getHealthDatas().isEmpty()) {
            initCurrentData(monitoringInformation.getHealthDatas().get(
                    monitoringInformation.getHealthDatas().size()-1)
                    , monitoringInformation.getGraphLegend());
            initGraph();
            updateView();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    @Override
    public void onItemSelected(int i, String s) {
        if(s.equals(REALTIME)){
            setGraphViewByRealtime();
        }
        if(s.equals(DAY)){

        }
        if(s.equals(MONTH)){
            setGraphViewByMonth();
        }
        if(s.equals(YEAR)){

        }
    }
}
