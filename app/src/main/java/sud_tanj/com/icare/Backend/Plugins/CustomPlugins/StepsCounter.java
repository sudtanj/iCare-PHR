package sud_tanj.com.icare.Backend.Plugins.CustomPlugins;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import lombok.NoArgsConstructor;
import sud_tanj.com.icare.Backend.Database.HybridReference;
import sud_tanj.com.icare.Backend.Database.Monitoring.MonitoringInformation;
import sud_tanj.com.icare.Backend.Database.PersonalData.HealthData;
import sud_tanj.com.icare.Backend.Plugins.BasePlugin;
import sud_tanj.com.icare.Backend.Sensors.CustomSensor.Pedometer;
import sud_tanj.com.icare.Backend.Sensors.SensorListener;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 10/08/2018 - 14:59.
 * <p>
 * This class last modified by User
 */
@NoArgsConstructor
public class StepsCounter extends BasePlugin implements ValueEventListener,SensorListener{
    public static final String IDENTIFICATION="-LJXvUiu95PkUihaMlmn";
    private static StepsCounter stepsCounter=null;
    private double valueResult=-1;
    public static StepsCounter getInstance(){
        if(stepsCounter==null)
            stepsCounter=new StepsCounter();
        return stepsCounter;
    }
    @Override
    public void run() {
        //listen to sensor result
        Pedometer.getInstance().addListener(this);
    }

    @Override
    public void onCalculationDone(double value) {
        //store the value in the current class [StepsCounter] for firebase to be able to handle
        this.valueResult=value;
        //Get Monitoring Information object
        DatabaseReference databaseReference= FirebaseDatabase.getInstance()
                .getReferenceFromUrl(MonitoringInformation.KEY)
                .child(IDENTIFICATION);
        //Listen for query result from firebase
        databaseReference.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        //Firebase creating the monitoring object according data from cloud
        MonitoringInformation monitoringInformation=dataSnapshot.getValue(MonitoringInformation.class);
        //Create new empty places in cloud for new health data
        DatabaseReference databaseReference=FirebaseDatabase.getInstance()
                .getReferenceFromUrl(HealthData.KEY).push();
        //create new HealthData object locally
        HealthData healthData=new HealthData(databaseReference);
        //Record value to the health data instance
        healthData.getDataList().add(this.valueResult);
        //A special class that wrap firebase class to handle offline transaction limitation
        HybridReference hybridReference =new HybridReference(databaseReference);
        //Tell the firebase to upload newly created healthData to the cloud
        hybridReference.setValue(healthData);
        //tell the monitoringinformation to link the health data
        monitoringInformation.getHealthDatas().add(healthData.toString());
        //sync the monitoring information in the cloud with the local copy
        dataSnapshot.getRef().setValue(monitoringInformation);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
