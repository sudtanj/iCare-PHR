package sud_tanj.com.icare.Backend.Plugins.CustomPlugins;

import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;

import sud_tanj.com.icare.Backend.Analysis.AnalysisListener;
import sud_tanj.com.icare.Backend.Analysis.CustomAnalysis.DistanceAnalysis;
import sud_tanj.com.icare.Backend.Database.HybridReference;
import sud_tanj.com.icare.Backend.Database.PersonalData.DataAnalysis;
import sud_tanj.com.icare.Backend.Database.PersonalData.HealthData;
import sud_tanj.com.icare.Backend.Microcontrollers.BaseMicrocontroller;
import sud_tanj.com.icare.Backend.Microcontrollers.CustomMicrocontroller.LolinESP8266;
import sud_tanj.com.icare.Backend.Microcontrollers.MicrocontrollerListener;
import sud_tanj.com.icare.Backend.Plugins.BasePlugin;
import sud_tanj.com.icare.Backend.Utility.AnalysisDataSynchronizer;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 15/12/2018 - 16:40.
 * <p>
 * This class last modified by User
 */
public class HCSR04UltrasonicSensor extends BasePlugin implements MicrocontrollerListener, AnalysisListener {
    public static final String IDENTIFICATION="-LTlDTu2dt8i1UqLCwpo";
    private static HCSR04UltrasonicSensor hcsr04UltrasonicSensor=null;
    private Double value=-1.0;
    public static HCSR04UltrasonicSensor getInstance(){
        if(hcsr04UltrasonicSensor==null){
            hcsr04UltrasonicSensor=new HCSR04UltrasonicSensor();
        }
        return hcsr04UltrasonicSensor;
    }
    @Override
    public void run() {
        System.out.println("HCSR04 Plugin run!");
        LolinESP8266.getInstance().addListener(this);
    }

    @Override
    public void onDataReceived(JsonObject data) {
        System.out.println(data);
        if(data.get(BaseMicrocontroller.SENSOR_ID)
                .getAsString().equals(IDENTIFICATION)){
            value=data.get("value").getAsDouble();
            DistanceAnalysis distanceAnalysis=new DistanceAnalysis();
            distanceAnalysis.setDistance(value);
            distanceAnalysis.addListener(this);
            HybridReference hybridReference=new HybridReference(FirebaseDatabase.getInstance().getReferenceFromUrl(HealthData.KEY)
                    .child(IDENTIFICATION).push());
            HealthData healthData=new HealthData();
            healthData.getDataList().add(value);
            healthData.getTag().add("None");
            hybridReference.setValue(healthData);
            System.out.println(healthData);
        }
    }

    @Override
    public void onAnalysisDone(int personCondition, String message) {
        DataAnalysis dataAnalysis=new DataAnalysis();
        dataAnalysis.setCondition(personCondition);
        dataAnalysis.setAnalysisMessage(message);
        AnalysisDataSynchronizer.sync(IDENTIFICATION,dataAnalysis);
        System.out.println(dataAnalysis);
    }
}
