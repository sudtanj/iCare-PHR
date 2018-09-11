package sud_tanj.com.icare.Backend.BackgroundJob;

import android.content.Context;

import com.badoo.mobile.util.WeakHandler;
import com.nanotasks.Tasks;

import lombok.AllArgsConstructor;
import sud_tanj.com.icare.Backend.Preferences.HybridPreferences;
import sud_tanj.com.icare.Backend.Utility.SystemStatus;
import sud_tanj.com.icare.Frontend.Settings.SettingsFragment;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 06/09/2018 - 17:53.
 * <p>
 * This class last modified by User
 */
@AllArgsConstructor
public class BackgroundRunnable implements Runnable {
    public static final int BACKGROUND_EXECUTION_TIME=1;
    private Context context=null;
    private static BackgroundDataReceiver backgroundDataReceive;
    protected static WeakHandler weakHandler;
    protected static BackgroundRunnable backgroundRunnable;

    public static void init(Context context){
        BackgroundRunnable.backgroundDataReceive=new BackgroundDataReceiver();
        weakHandler=new WeakHandler();
        backgroundRunnable=new BackgroundRunnable(context);
        if(HybridPreferences.getFirebaseInstance()
                .getString(SettingsFragment.BACKGROUND_JOB_SETTINGS,"On")
                .equals("On")) {
            backgroundRunnable.run();
        }
    }

    public static void reRunBackgroundService(){
        backgroundRunnable.run();
    }

    @Override
    public void run() {
        if(!SystemStatus.getBackgroundJobCancel())
            Tasks.executeInBackground(context,backgroundDataReceive,backgroundDataReceive);
    }
}
