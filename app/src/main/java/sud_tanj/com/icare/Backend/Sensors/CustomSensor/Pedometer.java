package sud_tanj.com.icare.Backend.Sensors.CustomSensor;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import sud_tanj.com.icare.Backend.Sensors.BuiltInSensor;

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
    public String getUnitMeasurement() {
        return "Steps";
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        fireEventListener(sensorEvent.values[0]);
    }

    @Override
    public void onDispose() {
        super.onDispose();
        Pedometer.pedometer=null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
