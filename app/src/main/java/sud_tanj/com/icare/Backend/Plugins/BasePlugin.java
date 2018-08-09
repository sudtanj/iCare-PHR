package sud_tanj.com.icare.Backend.Plugins;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 09/08/2018 - 17:12.
 * <p>
 * This class last modified by User
 */
public class BasePlugin {
    @Getter
    private static List<BasePlugin> basePluginList=new ArrayList<>();

    public BasePlugin(){
        if(basePluginList.indexOf(this)==-1) {
            basePluginList.add(this);
        }
    }

    public static void onStart(){
        //Put your [plugins_class_name].getInstance(); here
    }

    public void onDispose(){
        if(basePluginList.indexOf(this)>-1){
            basePluginList.remove(this);
        }
    }
}
