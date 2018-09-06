package sud_tanj.com.icare.Backend.BackgroundJob;

import android.content.Context;

import com.nanotasks.Tasks;

import lombok.AllArgsConstructor;

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
    @Override
    public void run() {
        BackgroundDataReceiver backgroundDataReceiver=new BackgroundDataReceiver();
        Tasks.executeInBackground(context,backgroundDataReceiver,backgroundDataReceiver);
    }
}
