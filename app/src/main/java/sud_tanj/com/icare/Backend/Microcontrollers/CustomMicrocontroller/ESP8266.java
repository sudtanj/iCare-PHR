package sud_tanj.com.icare.Backend.Microcontrollers.CustomMicrocontroller;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.dezlum.codelabs.getjson.GetJson;
import com.google.gson.JsonParser;

import sud_tanj.com.icare.Backend.Microcontrollers.BaseMicrocontroller;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 16/09/2018 - 9:29.
 * <p>
 * This class last modified by User
 */
public abstract class ESP8266 extends BaseMicrocontroller {
    protected JsonParser jsonParser=null;
    protected GetJson getJson;
    protected WifiManager wifi;
    public ESP8266() {
        super();
        this.jsonParser=new JsonParser();
        this.getJson=new GetJson();
        wifi = (WifiManager)BaseMicrocontroller.getContext()
                .getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }
}