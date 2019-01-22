package sud_tanj.com.icare.Backend.Microcontrollers.CustomMicrocontroller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.dezlum.codelabs.getjson.GetJson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
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
    private boolean executed=false;
    private static GetJson getJson=new GetJson();
    protected static WifiManager wifi=(WifiManager)BaseMicrocontroller.getContext()
            .getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    @Getter
    private static List<ESP8266> esp8266List=new ArrayList<>();

    @Override
    public void run() {
        for (String url : urlList) {
            if (wifi.isWifiEnabled()) {
                System.out.println("WIFI ENABLE");
                try {
                    //if(InetAddress.getByName(LOLIN_URL).isReachable(3000)) {
                    if (isServerReachable(url)) {
                        System.out.println("LOLIN WORKING!");
                        String response = getJson
                                .AsString(url);
                        System.out.println("Parsing JSON");
                        for (ESP8266 esp8266 : esp8266List) {
                            esp8266.onDataDownloaded(url, response);
                            esp8266.executed=true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    urlList.clear();
                    esp8266List.clear();
                }
            }
        }
        urlList.clear();
        esp8266List.clear();
    }

    @Getter
    private static List<String> urlList=new ArrayList<>();


    public static void addURL(String url){
        if(!urlList.contains(url)){
            urlList.add(url);
        }
    }

    public ESP8266() {
        super();
        this.executed=false;
        this.jsonParser=new JsonParser();
    }

    abstract void onDataDownloaded(String url, String data);

    public static boolean isServerReachable(String url) {
        ConnectivityManager connMan = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            try {
                URL urlServer = new URL(url);
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
}