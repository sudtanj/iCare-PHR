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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.isapanah.awesomespinner.AwesomeSpinner;
import com.isapanah.awesomespinner.AwesomeSpinner.onSpinnerItemClickListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.nightonke.boommenu.Animation.BoomEnum;
import com.nightonke.boommenu.Animation.OrderEnum;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton.Builder;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.ChatView.OnSentMessageListener;
import co.intentservice.chatui.models.ChatMessage;
import co.intentservice.chatui.models.ChatMessage.Type;
import sud_tanj.com.icare.Backend.Database.HybridReference;
import sud_tanj.com.icare.Backend.Database.Monitoring.MonitoringInformation;
import sud_tanj.com.icare.Backend.Database.PersonalData.DataAnalysis;
import sud_tanj.com.icare.Backend.Database.PersonalData.DataComment;
import sud_tanj.com.icare.Backend.Database.PersonalData.HealthData;
import sud_tanj.com.icare.Frontend.Notification.Notification;
import sud_tanj.com.icare.R;

@EActivity(R.layout.activity_data_details)
public class DataDetails extends AppCompatActivity implements OnBMClickListener,OnSentMessageListener,ValueEventListener,onSpinnerItemClickListener<String>, OnDateSetListener {

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
    @ViewById(R.id.comment_chat_view)
    protected ChatView commentView;
    @ViewById(R.id.bmb)
    protected BoomMenuButton boomButton;
    @ViewById(R.id.analysis_recycler)
    protected SuperRecyclerView analysisRecycler;
    private GraphEventListener graphEventListener=null;
    private Query firebaseQuery=null;
    private CommentEventListener commentEventListener=null;
    private Query firebaseCommentQuery=null;
    private DatePickerDialog dpd=null;
    private ChatMessage chatMessage=null;
    private MonitoringInformation monitoringInformation=null;

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
    protected void initAnalysis(){
        LinearLayoutManager firstManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        analysisRecycler.setLayoutManager(firstManager);
        Query query = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(DataAnalysis.KEY)
                .child(id)
                .orderByKey().limitToLast(1);

        FirebaseRecyclerOptions<DataAnalysis> options =
                new FirebaseRecyclerOptions.Builder<DataAnalysis>()
                        .setQuery(query, DataAnalysis.class)
                        .setLifecycleOwner(this)
                        .build();
        AnalysisAdapter analysisAdapter=new AnalysisAdapter(options);
        analysisRecycler.setAdapter(analysisAdapter);
    }

    private void initGraph(){
        lineChartView.getAxisLeft().setDrawGridLines(false);
        lineChartView.getXAxis().setDrawGridLines(false);
        lineChartView.getAxisRight().setDrawGridLines(false);
        lineChartView.getXAxis().setValueFormatter(new DayAxisValueFormatter(lineChartView));
        if(graphEventListener==null){
            graphEventListener=new GraphEventListener(monitoringInformation,lineChartView);
        }
        if(firebaseQuery==null){
            setGraphViewBy(null,null);
        }
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
    }
    @AfterViews
    protected void initCommentView(){
        commentView.setOnSentMessageListener(this);
        boomButton.setBoomEnum(BoomEnum.RANDOM);
        boomButton.setButtonEnum(ButtonEnum.Ham);
        boomButton.setPiecePlaceEnum(PiecePlaceEnum.HAM_2);
        boomButton.setButtonPlaceEnum(ButtonPlaceEnum.HAM_2);
        boomButton.setOrderEnum(OrderEnum.DEFAULT);
        Builder doctorButtonBuilder = new Builder()
                .normalText("Post as Doctor")
                .subNormalText("We will tag this comment as from doctor!")
                .listener(this);
        doctorButtonBuilder.index(DataComment.COMMENT_BY_DOCTOR);
        Builder userButtonBuilder = new Builder()
                .normalText("Post as User")
                .subNormalText("We will tag this comment as from you!")
                .listener(this);
        userButtonBuilder.index(DataComment.COMMENT_BY_INDIVIDUAL);
        boomButton.addBuilder(doctorButtonBuilder);
        boomButton.addBuilder(userButtonBuilder);
        commentEventListener=new CommentEventListener(commentView);
        setCommentBy(null,null);
    }

    private void setCommentBy(Calendar from,Calendar to){
        if(firebaseCommentQuery!=null){
            firebaseCommentQuery.removeEventListener(commentEventListener);
        }
        firebaseCommentQuery=FirebaseDatabase.getInstance().getReferenceFromUrl(DataComment.KEY)
                .child(id);
        if(from == null && to ==null){
            firebaseCommentQuery.orderByKey().limitToLast(50).addValueEventListener(commentEventListener);
        } else {
            firebaseCommentQuery.orderByChild("timeStamp").startAt(from.getTimeInMillis())
                    .endAt(to.getTimeInMillis()).addValueEventListener(commentEventListener);
        }
    }

    private void initLatestData(){
        LinearLayoutManager firstManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        firstRecyclerView.setLayoutManager(firstManager);
        Query query = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(HealthData.KEY)
                .child(id)
                .orderByKey().limitToLast(1);

        FirebaseRecyclerOptions<HealthData> options =
                new FirebaseRecyclerOptions.Builder<HealthData>()
                        .setQuery(query, HealthData.class)
                        .setLifecycleOwner(this)
                        .build();
        currentDataAdapter=new CurrentDataAdapter(options,monitoringInformation.getGraphLegend());
        firstRecyclerView.setAdapter(currentDataAdapter);
    }

    private void setGraphViewBy(Calendar from,Calendar to){
        if(firebaseQuery!=null){
            firebaseQuery.removeEventListener(graphEventListener);
        }
        firebaseQuery=FirebaseDatabase.getInstance().getReferenceFromUrl(HealthData.KEY)
                .child(id);
        if(from==null && to==null){
            firebaseQuery.orderByKey().limitToLast(80).addValueEventListener(graphEventListener);
        }
        else {
            firebaseQuery.orderByChild("timeStamp")
                    .startAt(from.getTimeInMillis())
                    .endAt(to.getTimeInMillis()).addValueEventListener(graphEventListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseQuery!=null)
            firebaseQuery.removeEventListener(graphEventListener);
        if(firebaseCommentQuery!=null){
            firebaseCommentQuery.removeEventListener(commentEventListener);
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
        System.out.println(monitoringInformation.getGraphLegend());
        setTitle(getString(R.string.details_information_title,monitoringInformation.getName()));
        initLatestData();
        initGraph();
        spinner.setSelection(REALTIME);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    @Override
    public void onItemSelected(int i, String s) {
        if(s.equals(REALTIME)){
            setGraphViewBy(null,null);
            setCommentBy(null,null);
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
            setCommentBy(from,to);
        } else {
            Notification.notifyFailure(getString(R.string.date_range_selection_fail));
        }
    }

    @Override
    public boolean sendMessage(ChatMessage chatMessage) {
        boomButton.boom();
        this.chatMessage=chatMessage;
        return true;
    }

    @Override
    public void onBoomButtonClick(int index) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReferenceFromUrl(DataComment.KEY)
                .child(id).push();
        HybridReference hybridReference=new HybridReference(databaseReference);
        DataComment dataComment=new DataComment();
        dataComment.setCommentType(index);
        dataComment.setMessage(chatMessage.getMessage());
        hybridReference.setValue(dataComment);
        if(index==DataComment.COMMENT_BY_DOCTOR){
            chatMessage.setSender("Doctor");
            chatMessage.setType(Type.RECEIVED);
        }
        else {
            chatMessage.setSender("Individual");
            chatMessage.setType(Type.SENT);
        }
    }
}
