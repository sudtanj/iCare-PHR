package sud_tanj.com.icare.Backend.Database.PersonalData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import sud_tanj.com.icare.Backend.Database.HybridDatabase;
import sud_tanj.com.icare.Backend.Database.OnDataChanges;
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
public class HealthData extends SyncableObject implements OnDataChanges {
    public static final String KEY="https://icare-89c17.firebaseio.com/Data";
    @Getter
    private List<Double> dataList=new ArrayList<>();
    @Exclude
    private List<HealthDataListener> listener=new ArrayList<>();

    public void addListener(HealthDataListener listener){
        this.listener.add(listener);
    }

    protected HealthData(){}

    public HealthData(String healthDataId){
        this.identification=healthDataId;
        database=new HybridDatabase(FirebaseDatabase.getInstance()
                .getReferenceFromUrl(KEY).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(this.identification));
        database.onDataChanges(this);
    }

    public HealthData(ArrayList<Double> healthDatas){
        this.dataList=healthDatas;
        DatabaseReference databaseReference=FirebaseDatabase.getInstance()
                .getReferenceFromUrl(KEY).child(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid()).push();
        this.identification=databaseReference.getKey();
        database=new HybridDatabase(databaseReference);
        database.onDataChanges(this);
    }

    @Exclude
    public String getId(){
        return this.identification;
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void onDataChanges(DataSnapshot dataSnapshot) {
        HealthData newData=dataSnapshot.getValue(HealthData.class);
        this.identification=dataSnapshot.getKey();
        this.timeStamp=newData.timeStamp;
        this.dataList=newData.dataList;
        newData=null;
    }

    @Override
    public void postLoad() {
        for (Iterator<HealthDataListener> iterator = this.listener.iterator(); iterator.hasNext(); ){
            HealthDataListener temp=iterator.next();
            temp.onReady(this);
            if(temp.isRunOnlyOnce()){
                iterator.remove();
            }
        }
    }
}
