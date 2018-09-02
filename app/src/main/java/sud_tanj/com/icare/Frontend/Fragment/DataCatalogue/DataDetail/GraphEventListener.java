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
import java.util.Calendar;
import java.util.List;

import lombok.AllArgsConstructor;
import sud_tanj.com.icare.Backend.Database.Monitoring.MonitoringInformation;
import sud_tanj.com.icare.Backend.Database.PersonalData.HealthData;

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
    private MonitoringInformation monitoringInformation;
    private LineChart lineChartView;

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        lineChartView.clear();
        if (dataSnapshot.getChildrenCount() > 0) {
            List<Entry> entries = new ArrayList<Entry>();
            int j = 0;
            for (DataSnapshot temp : dataSnapshot.getChildren()) {
                if (monitoringInformation.getHealthDatas().indexOf(temp.getKey()) > -1) {
                    HealthData healthData = temp.getValue(HealthData.class);
                    Calendar calendar= Calendar.getInstance();
                    calendar.setTimeInMillis(healthData.getTimeStamp());
                    for (int i = 0; i < healthData.getDataList().size(); i++) {
                        entries.add(new Entry(calendar.getTimeInMillis(), healthData.getDataList().get(i).floatValue()));
                    }
                    j++;
                }
            }
            LineDataSet dataSet = new LineDataSet(entries, "Steps");
            LineData lineData = new LineData(dataSet);
            lineChartView.setData(lineData);
            lineChartView.invalidate();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
