package sud_tanj.com.icare.Backend.BackgroundJob;

import android.content.Context;
import android.util.Log;

import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.orhanobut.logger.Logger;

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
        Logger.i(this.toString(),"Started Background Job");
        basePlugins=BasePlugin.getBasePluginList();
        Log.i(this.toString(),basePlugins.toString());
        for(BasePlugin basePlugin:basePlugins){
            basePlugin.run();
        }
        runnableMicrocontrollers = BaseMicrocontroller.getBaseMicrocontrollerList();
        Logger.i(this.toString(),runnableMicrocontrollers.toString());
        for (BaseMicrocontroller runnableMicrocontroller : runnableMicrocontrollers) {
            runnableMicrocontroller.run();
        }
        baseSensors=BaseSensor.getBaseSensors();
        Logger.i(this.toString(),baseSensors.toString());
        for(BaseSensor baseSensor:baseSensors){
            baseSensor.run();
            baseSensor.onDispose();
        }
        baseAnalyses=BaseAnalysis.getBaseAnalysisList();
        Logger.i(this.toString(),baseAnalyses.toString());
        for(BaseAnalysis baseAnalysis:baseAnalyses){
            baseAnalysis.run();
            baseAnalysis.onDispose();
        }
        Logger.i(this.toString(),"End Background Job");
        return null;
    }

    @Override
    public void onSuccess(Context context, Object result) {

    }

    @Override
    public void onError(Context context, Exception e) {

    }
}
