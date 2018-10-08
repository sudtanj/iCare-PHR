package sud_tanj.com.icare.Backend.Microcontrollers.CustomMicrocontroller;

import com.dezlum.codelabs.getjson.GetJson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 09/09/2018 - 9:24.
 * <p>
 * This class last modified by User
 */

public class LolinESP8266Multi extends ESP8266 {
    public static final String LOLIN_URL = "http://192.168.4.1/getData";
    public static final String NEW_IP = "newIp";
    private static LolinESP8266Multi lolinESP8266Multi = null;
    private List<String> ipAddress;

    public static LolinESP8266Multi getInstance() {
        if (lolinESP8266Multi == null)
            lolinESP8266Multi = new LolinESP8266Multi();
        return lolinESP8266Multi;
    }

    public LolinESP8266Multi() {
        super();
        this.ipAddress = new ArrayList<>();
    }

    @Override
    public void run() {
        if (wifi.isWifiEnabled()) {
            try {
                if(InetAddress.getByName(LOLIN_URL).isReachable(3000)) {
                    String response = new GetJson()
                            .AsString(LOLIN_URL);
                    System.out.println(response);
                    JsonObject jsonObject = jsonParser.parse(response).getAsJsonObject();
                    JsonArray jsonArray = jsonObject.get(NEW_IP).getAsJsonArray();
                    for (JsonElement temp : jsonArray) {
                        String result = temp.getAsString();
                        if (this.ipAddress.indexOf(result) == -1) {
                            this.ipAddress.add(result);
                        }
                    }
                    fireEventListener(jsonObject);
                }
            } catch (Exception e) {

            }

            for (int i=0;i<ipAddress.size();i++) {
                try {
                    String response = new GetJson()
                            .AsString("http://"+ipAddress.get(i)+"/getData");
                    System.out.println(response);
                    JsonObject jsonObject = jsonParser.parse(response).getAsJsonObject();
                    fireEventListener(jsonObject);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    ipAddress.remove(i);
                }
            }
        }
    }
}
