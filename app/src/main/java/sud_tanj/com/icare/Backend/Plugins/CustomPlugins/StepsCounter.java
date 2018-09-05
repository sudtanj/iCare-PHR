package sud_tanj.com.icare.Backend.Plugins.CustomPlugins;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import lombok.NoArgsConstructor;
import sud_tanj.com.icare.Backend.Analysis.AnalysisListener;
import sud_tanj.com.icare.Backend.Analysis.CustomAnalysis.StepsAnalysis;
import sud_tanj.com.icare.Backend.Database.HybridReference;
import sud_tanj.com.icare.Backend.Database.PersonalData.DataAnalysis;
import sud_tanj.com.icare.Backend.Database.PersonalData.HealthData;
import sud_tanj.com.icare.Backend.Plugins.BasePlugin;
import sud_tanj.com.icare.Backend.Sensors.CustomSensor.Pedometer;
import sud_tanj.com.icare.Backend.Sensors.SensorListener;
import sud_tanj.com.icare.Backend.Utility.AnalysisDataSynchronizer;

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
public class StepsCounter extends BasePlugin implements SensorListener, AnalysisListener {
    public static final String IDENTIFICATION="-LJXvUiu95PkUihaMlmn";
    private static StepsCounter stepsCounter=null;
    private double valueResult=-1;
    private int personCondition=-1;
    private String message;
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
        StepsAnalysis stepsAnalysis=new StepsAnalysis();
        stepsAnalysis.setSteps(this.valueResult);
        stepsAnalysis.addListener(this);
    }

    @Override
    public void onDispose() {
        super.onDispose();
        StepsCounter.stepsCounter=null;
    }

    @Override
    public void onAnalysisDone(final int personCondition, final String message) {
        this.personCondition=personCondition;
        this.message=message;
        //Get Monitoring Information object
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance()
                .getReferenceFromUrl(HealthData.KEY)
                .child(IDENTIFICATION).push();
        final HybridReference hybridReference=new HybridReference(databaseReference);
        //create new HealthData object locally
        HealthData healthData=new HealthData(databaseReference);
        //Record value to the health data instance
        healthData.getDataList().add(this.valueResult);
        hybridReference.setValue(healthData);
        DataAnalysis dataAnalysis=new DataAnalysis();
        dataAnalysis.setCondition(personCondition);
        dataAnalysis.setAnalysisMessage(message);
        AnalysisDataSynchronizer.sync(IDENTIFICATION,dataAnalysis);
    }
}
