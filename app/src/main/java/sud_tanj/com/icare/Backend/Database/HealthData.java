package sud_tanj.com.icare.Backend.Database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public static final String KEY="https://icare-89c17.firebaseio.com/Data";
    @Exclude
    private transient String healthDataId = "";
    @Exclude
    private transient HybridDatabase database=null;
    @Getter @Setter
    private Long timeStamp = new Date().getTime();
    @Getter
    private List<Double> dataList=new ArrayList<>();

    public HealthData(String healthDataId){
        this.healthDataId=healthDataId;
        database=new HybridDatabase(FirebaseDatabase.getInstance()
                .getReferenceFromUrl(KEY).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(this.healthDataId));
        database.onDataChanges(this);
    }

    public void addData(Double value){
        this.dataList.add(value);
        sync();
    }

    private void sync(){
        if(database==null){
            database=new HybridDatabase(FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(KEY).child(FirebaseAuth.getInstance()
                            .getCurrentUser().getUid())).addChild(this);
            database.onDataChanges(this);
        }
        database.setValue(this);
    }
    @Exclude
    public String getId(){
        return this.healthDataId;
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void onDataChanges(DataSnapshot dataSnapshot) {
        HealthData newData=dataSnapshot.getValue(HealthData.class);
        this.healthDataId=dataSnapshot.getKey();
        this.timeStamp=newData.timeStamp;
        this.dataList=newData.dataList;
        newData=null;
    }

    @Override
    public void postLoad() {

    }
}
