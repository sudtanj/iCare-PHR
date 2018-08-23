package sud_tanj.com.icare.Backend.Plugins;

import com.google.firebase.database.DataSnapshot;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 23/08/2018 - 8:46.
 * <p>
 * This class last modified by User
 */
public interface PluginListener {
    DataSnapshot dataSnapshot=null;

    void processInBackground(DataSnapshot dataSnapshot);
}
