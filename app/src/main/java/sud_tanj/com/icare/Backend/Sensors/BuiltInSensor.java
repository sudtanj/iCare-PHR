package sud_tanj.com.icare.Backend.Sensors;

import android.content.Context;

import lombok.Getter;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 08/08/2018 - 16:26.
 * <p>
 * This class last modified by User
 */
public abstract class BuiltInSensor extends BaseSensor {
    @Getter
    private static Context context;

    public static void init(Context context){
        BuiltInSensor.context=context;
    }

    public void run(){}
}
