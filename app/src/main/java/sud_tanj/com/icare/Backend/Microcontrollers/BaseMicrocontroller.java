package sud_tanj.com.icare.Backend.Microcontrollers;

import android.content.Context;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import sud_tanj.com.icare.Backend.BaseAbstractComponent;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 08/08/2018 - 17:02.
 * <p>
 * This class last modified by User
 */
public abstract class BaseMicrocontroller extends BaseAbstractComponent<MicrocontrollerListener,JsonObject> {
    public static final String SENSOR_ID="sensorid";
    public static final String DATA_TAG="tag";
    @Getter(AccessLevel.PROTECTED)
    private static Context context;
    @Getter
    private static List<BaseMicrocontroller> baseMicrocontrollerList=new ArrayList<>();

    public BaseMicrocontroller() {
        if(BaseMicrocontroller.getBaseMicrocontrollerList().indexOf(this)==-1)
            BaseMicrocontroller.getBaseMicrocontrollerList().add(this);
    }

    public static void init(Context context){
        BaseMicrocontroller.context=context;
    }

    @Override
    protected void onEventListenerFired(MicrocontrollerListener listener, JsonObject... valuePassed) {
        listener.onDataReceived(valuePassed[0]);
    }

    @Override
    public void onDispose(){
        if(BaseMicrocontroller.baseMicrocontrollerList.indexOf(this)>-1)
            BaseMicrocontroller.baseMicrocontrollerList.remove(this);
        this.listeners.clear();
    }
}
