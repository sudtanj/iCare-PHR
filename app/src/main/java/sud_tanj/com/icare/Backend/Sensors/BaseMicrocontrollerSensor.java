package sud_tanj.com.icare.Backend.Sensors;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 08/08/2018 - 15:54.
 * <p>
 * This class last modified by User
 */
public abstract class BaseMicrocontrollerSensor extends BaseSensor{

    abstract Double calculateResult();

    public void onDispose(){
        if(BaseSensor.baseSensors.indexOf(this)>-1)
            BaseSensor.baseSensors.remove(this);
        this.listeners.clear();
    }

}
