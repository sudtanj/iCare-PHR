package sud_tanj.com.icare.Backend.Database;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class HealthData implements OnDataChanges {
    public static final String KEY="Data";
    @Getter @Setter
    private String healthDataId = null;
    @Getter @Setter
    private Long timeStamp = null;

    public HealthData(String healthDataId, Date timeStamp) {
        this.healthDataId = healthDataId;
        this.timeStamp = timeStamp.getTime();
        HybridDatabase database=HybridDatabase.getInstance()
                .child(KEY)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addChild(this);
        database.setClassCasting(HealthData.class);
        database.onDataChanges(this).sync();
    }

    public void unSync(){
        HybridDatabase.getInstance().child(KEY).unSync();
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void onDataChanges(Object updatedObject) {
        HealthData newData=(HealthData)updatedObject;
        this.healthDataId=newData.healthDataId;
        this.timeStamp=newData.timeStamp;
    }

    @Override
    public void postLoad() {

    }
}
