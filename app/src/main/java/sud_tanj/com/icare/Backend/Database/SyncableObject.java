package sud_tanj.com.icare.Backend.Database;

import com.google.firebase.database.Exclude;

import java.util.Date;

import lombok.Getter;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 08/08/2018 - 11:15.
 * <p>
 * This class last modified by User
 */
public abstract class SyncableObject {
    @Exclude
    protected transient String identification = "";
    @Getter
    protected Long timeStamp = new Date().getTime();
    @Exclude
    protected transient HybridDatabase database=null;
    public void sync(){
        if(database!=null)
            database.setValue(this);
    }
    public void unSync() {
        if(database!=null)
            this.database.unSync();
    }
}
