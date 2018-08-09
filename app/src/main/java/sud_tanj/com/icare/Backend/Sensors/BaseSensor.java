package sud_tanj.com.icare.Backend.Sensors;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 08/08/2018 - 16:25.
 * <p>
 * This class last modified by User
 */
public abstract class BaseSensor implements Runnable{
    @Getter
    protected static List<BaseSensor> baseSensors=new ArrayList<>();
    protected List<SensorListener> sensorListeners;
    public BaseSensor(){
        if(baseSensors.indexOf(this)==-1)
            baseSensors.add(this);
    }
    public void addSensorListener(SensorListener sensorListener){
        if(sensorListeners.indexOf(sensorListener)==-1)
            this.sensorListeners.add(sensorListener);
    }
    public void removeSensorListener(SensorListener sensorListener){
        if(sensorListeners.indexOf(sensorListener)>-1){
            this.sensorListeners.remove(sensorListener);
        }
    }
    public abstract String getUnitMeasurement();

    public void onDispose(){
        if(BaseSensor.baseSensors.indexOf(this)>-1)
            BaseSensor.baseSensors.remove(this);
        this.sensorListeners.clear();
    }
}
