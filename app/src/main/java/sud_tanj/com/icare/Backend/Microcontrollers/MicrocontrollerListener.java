package sud_tanj.com.icare.Backend.Microcontrollers;

import com.google.gson.JsonObject;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 08/08/2018 - 18:34.
 * <p>
 * This class last modified by User
 */
public interface MicrocontrollerListener {
    void onDataReceived(JsonObject data);
}
