package sud_tanj.com.icare.Backend.Microcontrollers.CustomMicrocontroller;

import com.dezlum.codelabs.getjson.GetJson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

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
    @Setter
    private boolean bruteForce=false;
    private static LolinESP8266Multi lolinESP8266Multi = null;
    private List<String> ipAddress;
    private boolean bruteForceAdded=false;

    public static LolinESP8266Multi getInstance() {
        if (lolinESP8266Multi == null)
            lolinESP8266Multi = new LolinESP8266Multi();
        return lolinESP8266Multi;
    }

    public LolinESP8266Multi() {
        super();
        this.ipAddress = new ArrayList<>();
        ESP8266.addURL(LOLIN_URL);
        ESP8266.getEsp8266List().add(this);
    }

    @Override
    void onDataDownloaded(String url, String data) {
        if(url.equals(LOLIN_URL)){
            JsonObject jsonObject = jsonParser.parse(data).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get(NEW_IP).getAsJsonArray();
            if(bruteForce==false) {
                for (JsonElement temp : jsonArray) {
                    String result = temp.getAsString();
                    if (this.ipAddress.indexOf(result) == -1) {
                        String ipAddressUrl = "http://" + result + "/getData";
                        this.ipAddress.add(ipAddressUrl);
                        ESP8266.getUrlList().add(ipAddressUrl);
                    }
                }
            } else {
                if(!bruteForceAdded) {
                    ipAddress.clear();
                    ipAddress.add("192.168.4.2");
                    ipAddress.add("192.168.4.3");
                    ipAddress.add("192.168.4.4");
                    ipAddress.add("192.168.4.5");
                    ipAddress.add("192.168.4.6");
                    ipAddress.add("192.168.4.7");
                    ipAddress.add("192.168.4.8");
                    ipAddress.add("192.168.4.9");
                    bruteForceAdded = true;
                }
            }
            fireEventListener(jsonObject);
            for (int i=0;i<ipAddress.size();i++) {
                System.out.println("-LMVSOD5yK8uHUNd7X_8 "+ipAddress.get(i));
                String ipAddressUrl="http://"+ipAddress.get(i)+"/getData";
                try {
                        String response = new GetJson()
                                .AsString(ipAddressUrl);
                        System.out.println(response);
                        jsonObject = jsonParser.parse(response).getAsJsonObject();
                        fireEventListener(jsonObject);
                } catch (Exception e) {
                    System.out.println("-LMVSOD5yK8uHUNd7X_8 Remove");
                    System.out.println(e.getMessage());
                    //ipAddress.remove(i);
                }
            }
        }
    }

}
