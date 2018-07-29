package sud_tanj.com.icare.Backend.Preferences;

import android.content.Context;

import com.iamhabib.easy_preference.EasyPreference;
import com.iamhabib.easy_preference.EasyPreference.Builder;

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
public class HybridPreferences {
    private static Builder builder;
    private static SharedFirebasePreferences sharedFirebasePreferences;

    public static void init(Context context){
        sharedFirebasePreferences=SharedFirebasePreferences.getDefaultInstance(context);
        sharedFirebasePreferences.pull();
        builder=EasyPreference.with(context);
    }

    public static Builder getInstance(){
        return builder;
    }
}
