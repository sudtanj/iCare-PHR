package sud_tanj.com.icare.Frontend.Notification;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 15/07/2018 - 21:03.
 * <p>
 * This class last modified by BasicUser
 */
public class Notification {
    private static Context context=null;
    private static RelativeLayout frameLayout=null;
    public static void init(Context context){
        Notification.context=context;
    }
    public static void notifyUser(String message){
        Toasty.info(context,message, Toast.LENGTH_SHORT, true).show();
    }

    public static void notifySuccessful(String message){
        Toasty.success(context,message, Toast.LENGTH_SHORT, true).show();
    }

    public static void notifyFailure(String message){
        Toasty.error(context,message, Toast.LENGTH_SHORT, true).show();
    }
}
