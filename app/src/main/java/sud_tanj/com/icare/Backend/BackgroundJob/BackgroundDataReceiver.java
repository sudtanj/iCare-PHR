package sud_tanj.com.icare.Backend.BackgroundJob;

import android.content.Context;

import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;

import java.util.List;

import sud_tanj.com.icare.Backend.Analysis.BaseAnalysis;
import sud_tanj.com.icare.Backend.Microcontrollers.BaseMicrocontroller;
import sud_tanj.com.icare.Backend.Plugins.BasePlugin;
import sud_tanj.com.icare.Backend.Sensors.BaseSensor;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 09/08/2018 - 10:22.
 * <p>
 * This class last modified by User
 */
public class BackgroundDataReceiver implements BackgroundWork, Completion {
    List<BaseMicrocontroller> runnableMicrocontrollers;
    List<BaseAnalysis> baseAnalyses;
    List<BaseSensor> baseSensors;
    List<BasePlugin> basePlugins;

    @Override
    public Object doInBackground() throws Exception {
        runnableMicrocontrollers = BaseMicrocontroller.getBaseMicrocontrollerList();
        baseAnalyses=BaseAnalysis.getBaseAnalysisList();
        baseSensors=BaseSensor.getBaseSensors();
        basePlugins=BasePlugin.getBasePluginList();
        BasePlugin.onStart();
        for (BaseMicrocontroller runnableMicrocontroller : runnableMicrocontrollers) {
            runnableMicrocontroller.run();
            runnableMicrocontroller.onDispose();
        }
        for(BaseSensor baseSensor:baseSensors){
            baseSensor.run();
            baseSensor.onDispose();
        }
        for(BaseAnalysis baseAnalysis:baseAnalyses){
            baseAnalysis.run();
            baseAnalysis.onDispose();
        }
        for(BasePlugin basePlugin:basePlugins){
            basePlugin.onDispose();
        }
        return null;
    }

    @Override
    public void onSuccess(Context context, Object result) {

    }

    @Override
    public void onError(Context context, Exception e) {

    }
}
