package sud_tanj.com.icare.Backend.Preferences;

import android.content.Context;

import sharefirebasepreferences.crysxd.de.lib.SharedFirebasePreferences;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 29/07/2018 - 17:06.
 * <p>
 * This class last modified by User
 */
public class HybridPreferences{
    private static SharedFirebasePreferences sharedFirebasePreferences;

    public static void init(Context context){
        sharedFirebasePreferences=SharedFirebasePreferences.getDefaultInstance(context);
        sharedFirebasePreferences.pull();
        sharedFirebasePreferences.push();
        sharedFirebasePreferences.keepSynced(Boolean.TRUE);
    }
    public static SharedFirebasePreferences getFirebaseInstance(){return sharedFirebasePreferences;}
}
