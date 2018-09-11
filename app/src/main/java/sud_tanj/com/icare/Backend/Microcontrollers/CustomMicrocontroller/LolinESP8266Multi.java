package sud_tanj.com.icare.Backend.Microcontrollers.CustomMicrocontroller;

import sud_tanj.com.icare.Backend.Microcontrollers.BaseMicrocontroller;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 09/09/2018 - 9:24.
 * <p>
 * This class last modified by User
 */

public class LolinESP8266Multi extends BaseMicrocontroller {

    private static LolinESP8266Multi lolinESP8266Multi=null;

    public static LolinESP8266Multi getInstance(){
        if(lolinESP8266Multi==null)
            lolinESP8266Multi=new LolinESP8266Multi();
        return lolinESP8266Multi;
    }

    public LolinESP8266Multi() {

    }

    @Override
    public void run() {

    }
}
