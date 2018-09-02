package sud_tanj.com.icare.Backend.Plugins.CustomPlugins;

import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;

import sud_tanj.com.icare.Backend.Database.HybridReference;
import sud_tanj.com.icare.Backend.Database.PersonalData.HealthData;
import sud_tanj.com.icare.Backend.Microcontrollers.CustomMicrocontroller.ArduinoUnoCH340;
import sud_tanj.com.icare.Backend.Microcontrollers.MicrocontrollerListener;
import sud_tanj.com.icare.Backend.Plugins.BasePlugin;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 23/08/2018 - 9:04.
 * <p>
 * This class last modified by User
 */
public class ArduinoExample extends BasePlugin implements MicrocontrollerListener {
    public static final String IDENTIFICATION="-LKKcHt2-AGwGlHik9v_";
    private static ArduinoExample arduinoExample=null;
    private String value="";
    public static ArduinoExample getInstance(){
        if(arduinoExample==null)
            arduinoExample=new ArduinoExample();
        return arduinoExample;
    }
    @Override
    public void run() {
        ArduinoUnoCH340 arduinoUnoCH340=ArduinoUnoCH340.getInstance();
        arduinoUnoCH340.addListener(this);
    }

    @Override
    public void onDataReceived(JsonObject data) {
        if(data.get("sensorid")
                .getAsString().equals(IDENTIFICATION)){
            value=data.get("value").getAsString();
            HybridReference hybridReference=new HybridReference(FirebaseDatabase.getInstance().getReferenceFromUrl(HealthData.KEY)
                    .child(IDENTIFICATION).push());
            HealthData healthData=new HealthData();
            healthData.getDataList().add(Double.parseDouble(value));
            hybridReference.setValue(healthData);
        }
    }

    @Override
    public void onDispose() {
        super.onDispose();
        ArduinoExample.arduinoExample=null;
    }
}
