package sud_tanj.com.icare.Backend.Plugins;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
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
public abstract class BasePlugin implements Runnable{
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
    }

    public void onDispose(){
        if(basePluginList.indexOf(this)>-1){
            basePluginList.remove(this);
        }
    }
}
