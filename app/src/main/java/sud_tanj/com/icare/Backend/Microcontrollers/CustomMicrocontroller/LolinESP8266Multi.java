package sud_tanj.com.icare.Backend.Microcontrollers.CustomMicrocontroller;

import com.knexis.hotspot.ConnectedDevice;
import com.knexis.hotspot.ConnectionResult;
import com.knexis.hotspot.Hotspot;
import com.knexis.hotspot.HotspotListener;

import java.util.ArrayList;

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

public class LolinESP8266Multi extends BaseMicrocontroller implements HotspotListener {
    private Hotspot hotspot=null;

    private static LolinESP8266Multi lolinESP8266Multi=null;

    public static LolinESP8266Multi getInstance(){
        if(lolinESP8266Multi==null)
            lolinESP8266Multi=new LolinESP8266Multi();
        return lolinESP8266Multi;
    }

    public LolinESP8266Multi() {
        hotspot = new Hotspot(BaseMicrocontroller.getContext());

        hotspot.startListener(this);
        hotspot.start("Hotspot-Android", "12345678");
        System.out.println("Hostpost started");
    }

    @Override
    public void OnDevicesConnectedRetrieved(ArrayList<ConnectedDevice> arrayList) {
        System.out.println("device list");
        for(ConnectedDevice temp:arrayList){
            System.out.println(temp.getDevice());
        }
    }

    @Override
    public void OnHotspotStartResult(ConnectionResult connectionResult) {
        System.out.println(connectionResult.getMessage());
    }

    @Override
    public void run() {

    }
}
