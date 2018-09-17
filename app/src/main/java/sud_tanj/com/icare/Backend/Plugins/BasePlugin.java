package sud_tanj.com.icare.Backend.Plugins;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import sud_tanj.com.icare.Backend.BaseAbstractComponent;
import sud_tanj.com.icare.Backend.Plugins.CustomPlugins.ArduinoExample;
import sud_tanj.com.icare.Backend.Plugins.CustomPlugins.LolinExample;
import sud_tanj.com.icare.Backend.Plugins.CustomPlugins.LolinMultiExample;
import sud_tanj.com.icare.Backend.Plugins.CustomPlugins.StepsCounter;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 09/08/2018 - 17:12.
 * <p>
 * This class last modified by User
 */
public abstract class BasePlugin extends BaseAbstractComponent<PluginListener,DataSnapshot> implements Runnable{
    @Getter
    private static List<BasePlugin> basePluginList=new ArrayList<>();

    public BasePlugin(){
        if(basePluginList.indexOf(this)==-1) {
            basePluginList.add(this);
        }
    }

    public static void init(){
        //Put your [plugins_class_name].getInstance(); here
        StepsCounter.getInstance();
        ArduinoExample.getInstance();
        LolinExample.getInstance();
        LolinMultiExample.getInstance();
    }

    @Override
    protected void onEventListenerFired(PluginListener listener, DataSnapshot... valuePassed) {
        listener.processInBackground(listener.dataSnapshot);
    }

    @Override
    public void fireEventListener(DataSnapshot... valuePassed) {
        super.fireEventListener(valuePassed);
    }

    public void onDispose(){
        if(basePluginList.indexOf(this)>-1){
            basePluginList.remove(this);
        }
    }
}
