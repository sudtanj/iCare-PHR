package sud_tanj.com.icare.Backend.BackgroundJob;

import android.content.Context;
import android.util.Log;

import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;
import com.orhanobut.logger.Logger;

import java.util.List;

import sud_tanj.com.icare.Backend.Analysis.BaseAnalysis;
import sud_tanj.com.icare.Backend.Microcontrollers.BaseMicrocontroller;
import sud_tanj.com.icare.Backend.Plugins.BasePlugin;
import sud_tanj.com.icare.Backend.Sensors.BaseSensor;
import sud_tanj.com.icare.Backend.Utility.SystemStatus;
import sud_tanj.com.icare.Frontend.Notification.Notification;

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
        if(SystemStatus.getBackgroundJobCancel())
            return null;
        Logger.i(this.toString(),"Started Background Job");
        BasePlugin.init();
        basePlugins=BasePlugin.getBasePluginList();
        Log.i(this.toString(),basePlugins.toString());
        for(int i=0;i<basePlugins.size();i++){
            basePlugins.get(i).run();
            basePlugins.get(i).fireEventListener(null);
            if(SystemStatus.getBackgroundJobCancel())
                return null;
        }
        if(SystemStatus.getBackgroundJobCancel())
            return null;
        runnableMicrocontrollers = BaseMicrocontroller.getBaseMicrocontrollerList();
        Logger.i(this.toString(),runnableMicrocontrollers.toString());
        for(int i=0;i<runnableMicrocontrollers.size();i++){
            runnableMicrocontrollers.get(i).run();
            if(SystemStatus.getBackgroundJobCancel())
                return null;
        }
        if(SystemStatus.getBackgroundJobCancel())
            return null;
        baseSensors=BaseSensor.getBaseSensors();
        Logger.i(this.toString(),baseSensors.toString());
        for(int i=0;i<baseSensors.size();i++){
            baseSensors.get(i).run();
            baseSensors.get(i).onDispose();
            if(SystemStatus.getBackgroundJobCancel())
                return null;
        }
        if(SystemStatus.getBackgroundJobCancel())
            return null;
        baseAnalyses=BaseAnalysis.getBaseAnalysisList();
        Logger.i(this.toString(),baseAnalyses.toString());
        for(int i=0;i<baseAnalyses.size();i++){
            baseAnalyses.get(i).run();
            baseAnalyses.get(i).onDispose();
            if(SystemStatus.getBackgroundJobCancel())
                return null;
        }
        if(SystemStatus.getBackgroundJobCancel())
            return null;
        for(int i=0;i<basePlugins.size();i++){
            basePlugins.get(i).onDispose();
            if(SystemStatus.getBackgroundJobCancel())
                return null;
        }
        Logger.i(this.toString(),"End Background Job");
        return null;
    }

    @Override
    public void onSuccess(Context context, Object result) {
        if(!SystemStatus.getBackgroundJobCancel()) {
            Tasks.executeInBackground(context, this, this);
        }
    }

    @Override
    public void onError(Context context, Exception e) {
        if(!SystemStatus.getBackgroundJobCancel()) {
            Notification.notifyFailure("Something wrong in the background! App is trying to fix the problem...");
            Tasks.executeInBackground(context, this, this);
        }
    }
}
