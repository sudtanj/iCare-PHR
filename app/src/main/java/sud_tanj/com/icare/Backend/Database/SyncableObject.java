package sud_tanj.com.icare.Backend.Database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 08/08/2018 - 11:15.
 * <p>
 * This class last modified by User
 */
@NoArgsConstructor
public abstract class SyncableObject {
    @Exclude
    protected transient String identification = "";
    @Getter
    protected Long timeStamp = new Date().getTime();

    public SyncableObject(DatabaseReference databaseReference){
        this.identification=databaseReference.getKey();
    }

    @Override
    public String toString() {
        super.toString();
        return this.identification;
    }
}
