package sud_tanj.com.icare.Frontend.Fragment.DataCatalogue.DataDetail;

import android.support.annotation.NonNull;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import sud_tanj.com.icare.Backend.Database.Monitoring.MonitoringInformation;
import sud_tanj.com.icare.Backend.Database.PersonalData.HealthData;
import sud_tanj.com.icare.Frontend.Animation.LoadingScreen;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 01/09/2018 - 16:10.
 * <p>
 * This class last modified by User
 */
@AllArgsConstructor
public class GraphEventListener implements ValueEventListener {
    private MonitoringInformation monitoringInformation=null;
    private LineChart lineChartView=null;

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        LoadingScreen.showLoadingScreen("Fetching data from database");
        lineChartView.clear();
        if (dataSnapshot.getChildrenCount() > 0) {
            LineData lineData = new LineData();
            List< List<Entry> > entries=new ArrayList<>();
            //List<Entry> entries = new ArrayList<Entry>();
            if(monitoringInformation!=null) {
                if(monitoringInformation.getGraphLegend()!=null) {
                    if (!monitoringInformation.getGraphLegend().isEmpty()) {
                        for (String temp : monitoringInformation.getGraphLegend()) {
                            entries.add(new ArrayList<Entry>());
                        }
                        for (DataSnapshot temp : dataSnapshot.getChildren()) {
                            HealthData healthData = temp.getValue(HealthData.class);
                            for (int i = 0; i < healthData.getDataList().size(); i++) {
                                entries.get(i).add(new Entry(healthData.getTimeStamp(), healthData.getDataList().get(i).floatValue()));
                                //entries.add(new Entry(healthData.getTimeStamp(), healthData.getDataList().get(i).floatValue()));
                            }
                        }
                        int i = 0;
                        for (String temp : monitoringInformation.getGraphLegend()) {
                            if (!entries.get(i).isEmpty()) {
                                LineDataSet lineDataSet = new LineDataSet(entries.get(i), temp);
                                lineDataSet.setDrawValues(false);
                                lineData.addDataSet(lineDataSet);
                                i++;
                            }
                        }
                        //LineDataSet dataSet = new LineDataSet(entries, "Steps");
                        //dataSet.setDrawValues(false);
                        lineChartView.setData(lineData);
                        lineChartView.invalidate();
                    }
                }
            }
        }
        LoadingScreen.hideLoadingScreen();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
