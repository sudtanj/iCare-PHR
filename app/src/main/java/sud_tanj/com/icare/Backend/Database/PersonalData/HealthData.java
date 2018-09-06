package sud_tanj.com.icare.Backend.Database.PersonalData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sud_tanj.com.icare.Backend.Database.SyncableObject;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 15/07/2018 - 14:20.
 * <p>
 * This class last modified by User
 */
@NoArgsConstructor
public class HealthData extends SyncableObject {
    public static final String KEY="https://icare-89c17.firebaseio.com/Data/"+ FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Getter
    private List<Double> dataList=new ArrayList<>();
    @Getter @Setter
    private String tag="";

    public HealthData(DatabaseReference databaseReference) {
        super(databaseReference);
    }
}
