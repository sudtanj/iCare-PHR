package sud_tanj.com.icare.Backend.Plugins.CustomPlugins;

import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;

import sud_tanj.com.icare.Backend.Database.HybridReference;
import sud_tanj.com.icare.Backend.Database.PersonalData.HealthData;
import sud_tanj.com.icare.Backend.Microcontrollers.BaseMicrocontroller;
import sud_tanj.com.icare.Backend.Microcontrollers.CustomMicrocontroller.LolinESP8266;
import sud_tanj.com.icare.Backend.Microcontrollers.MicrocontrollerListener;
import sud_tanj.com.icare.Backend.Plugins.BasePlugin;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 06/09/2018 - 11:48.
 * <p>
 * This class last modified by User
 */
public class LolinExample extends BasePlugin implements MicrocontrollerListener {
    public static final String IDENTIFICATION="-LKPKW2mpUT3VBCT5XWk";

    @Override
    public void onDispose() {
        super.onDispose();
        arduinoExample=null;
    }

    private String value="";
    private static LolinExample arduinoExample=null;
    public static LolinExample getInstance(){
        if(arduinoExample==null)
            arduinoExample=new LolinExample();
        return arduinoExample;
    }
    @Override
    public void onDataReceived(JsonObject data) {
        if(data.get(BaseMicrocontroller.SENSOR_ID)
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
    public void run() {
        LolinESP8266.getInstance().addListener(this);
    }
}
