package sud_tanj.com.icare.Backend.Database.PersonalData;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 08/08/2018 - 10:30.
 * <p>
 * This class last modified by User
 */
public interface HealthDataListener {
    void onReady(HealthData healthData);
    Boolean isRunOnlyOnce();
}
