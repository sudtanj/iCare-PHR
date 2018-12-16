package sud_tanj.com.icare.Backend.Microcontrollers.CustomMicrocontroller;

import com.dezlum.codelabs.getjson.GetJson;
import com.google.gson.JsonObject;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 08/08/2018 - 15:48.
 * <p>
 * This class last modified by User
 */
public class LolinESP8266 extends ESP8266{
    public static final String LOLIN_URL="http://192.168.4.1/getData";
    private static LolinESP8266 lolinESP8266=null;

    public static LolinESP8266 getInstance(){
        if(lolinESP8266==null){
            lolinESP8266=new LolinESP8266();
        }
        return lolinESP8266;
    }

    public LolinESP8266() {
        super();
    }

    @Override
    public void run() {
        System.out.println("Run LOLIN!");
        if(wifi.isWifiEnabled()) {
            System.out.println("WIFI ENABLE");
            try {
                //if(InetAddress.getByName(LOLIN_URL).isReachable(3000)) {
                    System.out.println("LOLIN WORKING!");
                    String response = new GetJson()
                            .AsString(LOLIN_URL);
                    JsonObject jsonObject = jsonParser.parse(response).getAsJsonObject();
                    fireEventListener(jsonObject);
               // }
               // else {
                  //  System.out.println("LOLIN FAILED!");
               // }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
