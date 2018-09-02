package sud_tanj.com.icare.Frontend.Fragment.DataCatalogue.DataDetail;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ArrayAdapter;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.borax12.materialdaterangepicker.date.DatePickerDialog.OnDateSetListener;
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
import sud_tanj.com.icare.Frontend.Notification.Notification;
import sud_tanj.com.icare.R;

@EActivity(R.layout.activity_data_details)
public class DataDetails extends AppCompatActivity implements ValueEventListener,onSpinnerItemClickListener<String>, OnDateSetListener {

    public static final String REALTIME="Realtime";
    public static final String FROM_TO="Show data from -> to";
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
    private DatePickerDialog dpd=null;

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
        spinnerList.add(FROM_TO);

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

    private void setGraphViewBy(Calendar from,Calendar to){
        firebaseQuery=FirebaseDatabase.getInstance().getReferenceFromUrl(HealthData.KEY)
                .orderByChild("timeStamp").startAt(from.getTimeInMillis())
                .endAt(to.getTimeInMillis());
        firebaseQuery.addListenerForSingleValueEvent(graphEventListener);
    }

    private void initGraph(){
        lineChartView.getAxisLeft().setDrawGridLines(false);
        lineChartView.getXAxis().setDrawGridLines(false);
        lineChartView.getAxisRight().setDrawGridLines(false);
        lineChartView.getXAxis().setValueFormatter(new DayAxisValueFormatter(lineChartView));
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
        if(s.equals(FROM_TO)){
            if(dpd==null) {
                Calendar fromToChoice = Calendar.getInstance();
                dpd = DatePickerDialog.newInstance(
                        this,
                        fromToChoice.get(Calendar.YEAR),
                        fromToChoice.get(Calendar.MONTH),
                        fromToChoice.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMaxDate(Calendar.getInstance());
            }
            dpd.show(getFragmentManager(), "Datepickerdialog");
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        if(year==yearEnd) {
            Calendar from = Calendar.getInstance(), to = Calendar.getInstance();
            from.set(Calendar.YEAR, year);
            from.set(Calendar.MONTH, monthOfYear);
            from.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            to.set(Calendar.YEAR, yearEnd);
            to.set(Calendar.MONTH, monthOfYearEnd);
            to.set(Calendar.DAY_OF_MONTH, dayOfMonthEnd);
            setGraphViewBy(from, to);
        } else {
            Notification.notifyFailure(getString(R.string.date_range_selection_fail));
        }
    }
}
