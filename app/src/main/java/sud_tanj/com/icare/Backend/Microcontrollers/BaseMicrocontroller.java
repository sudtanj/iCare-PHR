package sud_tanj.com.icare.Backend.Microcontrollers;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 08/08/2018 - 17:02.
 * <p>
 * This class last modified by User
 */
public abstract class BaseMicrocontroller implements Runnable{
    public static final String BACKGROUND_JOB_TAG="microcontroller_background_job";
    @Getter(AccessLevel.PROTECTED)
    private static Context context;
    @Getter
    private static List<BaseMicrocontroller> baseMicrocontrollerList=new ArrayList<>();

    public BaseMicrocontroller() {
        BaseMicrocontroller.getBaseMicrocontrollerList().add(this);
    }

    public static void init(Context context){
        BaseMicrocontroller.context=context;
    }
    public abstract void onDispose();
}
