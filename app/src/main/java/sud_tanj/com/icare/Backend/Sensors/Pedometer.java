package sud_tanj.com.icare.Backend.Sensors;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 08/08/2018 - 16:15.
 * <p>
 * This class last modified by User
 */
public class Pedometer extends BuiltInSensor implements SensorEventListener {
    @Getter
    protected List<SensorEventListener> sensorEventListenerList=new ArrayList<>();
    protected static Pedometer pedometer=null;

    public static Pedometer getInstance(){
        if(pedometer==null){
            pedometer=new Pedometer();
        }
        return pedometer;
    }

    protected Pedometer(){
        if (getContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)){
            SensorManager sensorManager = (SensorManager) getContext()
                    .getSystemService(Context.SENSOR_SERVICE);
            Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if (countSensor != null) {
                sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }

    @Override
    String getUnitMeasurement() {
        return "Steps";
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        for(SensorEventListener temp:sensorEventListenerList){
            temp.onSensorChanged(sensorEvent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        for(SensorEventListener temp:sensorEventListenerList){
            temp.onAccuracyChanged(sensor,i);
        }
    }
}
