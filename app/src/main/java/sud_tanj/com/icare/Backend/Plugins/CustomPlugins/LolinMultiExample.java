package sud_tanj.com.icare.Backend.Plugins.CustomPlugins;

import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;

import sud_tanj.com.icare.Backend.Database.HybridReference;
import sud_tanj.com.icare.Backend.Database.PersonalData.HealthData;
import sud_tanj.com.icare.Backend.Microcontrollers.BaseMicrocontroller;
import sud_tanj.com.icare.Backend.Microcontrollers.CustomMicrocontroller.LolinESP8266Multi;
import sud_tanj.com.icare.Backend.Microcontrollers.MicrocontrollerListener;
import sud_tanj.com.icare.Backend.Plugins.BasePlugin;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 16/09/2018 - 9:49.
 * <p>
 * This class last modified by User
 */
public class LolinMultiExample extends BasePlugin implements MicrocontrollerListener {
    public static final String FIRST_MICROCONTROLLER_TAG="ONE";
    public static final String SECOND_MICROCONTROLLER_TAG="TWO";
    public static final String IDENTIFICATION="-LMVSOD5yK8uHUNd7X_8";
    private static LolinMultiExample lolinMultiExample=null;
    private String firstMicrocontrollerValue;
    private String secondMicrocontrollerValue;
    private HealthData healthData=null;

    public static LolinMultiExample getInstance(){
        if(lolinMultiExample==null){
            lolinMultiExample=new LolinMultiExample();
        }
        return lolinMultiExample;
    }

    @Override
    public void run() {
        LolinESP8266Multi.getInstance().setBruteForce(true);
        LolinESP8266Multi.getInstance().addListener(this);
    }

    @Override
    public void onDispose() {
        super.onDispose();
        lolinMultiExample=null;
    }

    @Override
    public void onDataReceived(JsonObject data) {
        if(data.get(BaseMicrocontroller.SENSOR_ID)
                .getAsString().equals(IDENTIFICATION)){
            if(data.get(BaseMicrocontroller.DATA_TAG).getAsString().equals(FIRST_MICROCONTROLLER_TAG)) {
                healthData=new HealthData();
                firstMicrocontrollerValue = data.get("value").getAsString();
                healthData.getDataList().add(Double.parseDouble(firstMicrocontrollerValue));
                healthData.getTag().add(FIRST_MICROCONTROLLER_TAG);
            }
            if(data.get(BaseMicrocontroller.DATA_TAG).getAsString().equals(SECOND_MICROCONTROLLER_TAG)) {
                secondMicrocontrollerValue = data.get("value").getAsString();
                if(healthData!=null){
                    healthData.getDataList().add(Double.parseDouble(secondMicrocontrollerValue));
                    healthData.getTag().add(SECOND_MICROCONTROLLER_TAG);
                    HybridReference hybridReference=new HybridReference(FirebaseDatabase.getInstance().getReferenceFromUrl(HealthData.KEY)
                            .child(IDENTIFICATION).push());
                    hybridReference.setValue(healthData);
                }
            }
        }
    }
}
