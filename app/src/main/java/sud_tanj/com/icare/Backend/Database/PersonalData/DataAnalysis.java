package sud_tanj.com.icare.Backend.Database.PersonalData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sud_tanj.com.icare.Backend.Database.SyncableObject;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 08/08/2018 - 15:33.
 * <p>
 * This class last modified by User
 */
@NoArgsConstructor
public class DataAnalysis extends SyncableObject {
    public static final String KEY="https://icare-89c17.firebaseio.com/DataAnalysis/"+ FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Getter @Setter
    private int condition;
    @Getter @Setter
    private String analysisMessage;
    public DataAnalysis(DatabaseReference databaseReference) {
        super(databaseReference);
    }
}
