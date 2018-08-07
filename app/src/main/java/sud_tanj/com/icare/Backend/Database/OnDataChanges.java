package sud_tanj.com.icare.Backend.Database;

import com.google.firebase.database.DataSnapshot;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 28/07/2018 - 10:56.
 * <p>
 * This class last modified by User
 */
public interface OnDataChanges {
    void preLoad();
    void onDataChanges(DataSnapshot dataSnapshot);
    void postLoad();
}
