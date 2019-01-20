package sud_tanj.com.icare.Backend.Microcontrollers.CustomMicrocontroller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dezlum.codelabs.getjson.GetJson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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

    public boolean isServerReachable() {
        ConnectivityManager connMan = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            try {
                URL urlServer = new URL(LOLIN_URL);
                HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
                urlConn.setConnectTimeout(3000); //<- 3Seconds Timeout
                urlConn.connect();
                if (urlConn.getResponseCode() == 200) {
                    return true;
                } else {
                    return false;
                }
            } catch (MalformedURLException e1) {
                return false;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public void run() {
        System.out.println("Run LOLIN!");
        if(wifi.isWifiEnabled()) {
            System.out.println("WIFI ENABLE");
            try {
                //if(InetAddress.getByName(LOLIN_URL).isReachable(3000)) {
                if(isServerReachable()) {
                    System.out.println("LOLIN WORKING!");
                    String response = new GetJson()
                            .AsString(LOLIN_URL);
                    System.out.println("Parsing JSON");
                    JsonObject jsonObject = jsonParser.parse(response).getAsJsonObject();
                    System.out.println("Fire");
                    fireEventListener(jsonObject);
                }
               // }
               // else {
                  //  System.out.println("LOLIN FAILED!");
               // }
            } catch (Exception e) {
               e.printStackTrace();
            }
            System.out.println("Lolin end");
        }
    }
}
