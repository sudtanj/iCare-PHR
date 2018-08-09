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
 * Created by Sudono Tanjung on 08/08/2018 - 11:03.
 * <p>
 * This class last modified by User
 */
@NoArgsConstructor
public class DataComment extends SyncableObject {
    public static final String KEY="https://icare-89c17.firebaseio.com/DataComment/"+ FirebaseAuth.getInstance().getCurrentUser().getUid();
    public static final int COMMENT_BY_DOCTOR=0;
    public static final int COMMENT_BY_INDIVIDUAL=1;
    @Getter @Setter
    private String message="";
    @Getter @Setter
    private Integer commentType;

    public DataComment(DatabaseReference databaseReference) {
        super(databaseReference);
    }
}
