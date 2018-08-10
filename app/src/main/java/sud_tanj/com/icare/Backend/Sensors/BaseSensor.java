package sud_tanj.com.icare.Backend.Sensors;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import sud_tanj.com.icare.Backend.BaseAbstractComponent;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 08/08/2018 - 16:25.
 * <p>
 * This class last modified by User
 */
public abstract class BaseSensor extends BaseAbstractComponent<SensorListener,Double> {
    @Getter
    protected static List<BaseSensor> baseSensors=new ArrayList<>();
    protected BaseSensor(){
        if(BaseSensor.baseSensors.indexOf(this)==-1) {
            BaseSensor.baseSensors.add(this);
        }
    }

    @Override
    protected void onEventListenerFired(SensorListener listener, Double... valuePassed) {
        listener.onCalculationDone(valuePassed[0]);
    }

    public abstract String getUnitMeasurement();

}
