package sud_tanj.com.icare.Backend.Plugins.CustomPlugins;

import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;

import lombok.NoArgsConstructor;
import sud_tanj.com.icare.Backend.Analysis.AnalysisListener;
import sud_tanj.com.icare.Backend.Analysis.CustomAnalysis.AirQualityAnalysis;
import sud_tanj.com.icare.Backend.Database.HybridReference;
import sud_tanj.com.icare.Backend.Database.PersonalData.DataAnalysis;
import sud_tanj.com.icare.Backend.Database.PersonalData.HealthData;
import sud_tanj.com.icare.Backend.Microcontrollers.BaseMicrocontroller;
import sud_tanj.com.icare.Backend.Microcontrollers.CustomMicrocontroller.LolinESP8266;
import sud_tanj.com.icare.Backend.Microcontrollers.MicrocontrollerListener;
import sud_tanj.com.icare.Backend.Plugins.BasePlugin;
import sud_tanj.com.icare.Backend.Utility.AnalysisDataSynchronizer;

@NoArgsConstructor
public class AirQualitySensor extends BasePlugin implements MicrocontrollerListener, AnalysisListener {
    public static final String IDENTIFICATION="-LTfkcBIFCRtgl0b134i";
    public static AirQualitySensor airQualitySensor=null;
    private double value=-1.0;
    public static AirQualitySensor getInstance(){
        if(airQualitySensor==null){
            airQualitySensor=new AirQualitySensor();
        }
        return airQualitySensor;
    }
    @Override
    public void run() {
        LolinESP8266.getInstance().addListener(this);
    }

    @Override
    public void onDataReceived(JsonObject data) {
        if(data.get(BaseMicrocontroller.SENSOR_ID)
                .getAsString().equals(IDENTIFICATION)){
            value=data.get("value").getAsDouble();
            HybridReference hybridReference=new HybridReference(FirebaseDatabase.getInstance().getReferenceFromUrl(HealthData.KEY)
                    .child(IDENTIFICATION).push());
            HealthData healthData=new HealthData();
            healthData.getDataList().add(value);
            healthData.getTag().add("None");
            hybridReference.setValue(healthData);
            AirQualityAnalysis airQualityAnalysis=new AirQualityAnalysis();
            airQualityAnalysis.setPpm(value);
            airQualityAnalysis.addListener(this);
        }
    }

    @Override
    public void onAnalysisDone(int personCondition, String message) {
        DataAnalysis dataAnalysis=new DataAnalysis();
        dataAnalysis.setCondition(personCondition);
        dataAnalysis.setAnalysisMessage(message);
        AnalysisDataSynchronizer.sync(IDENTIFICATION,dataAnalysis);
    }
}
