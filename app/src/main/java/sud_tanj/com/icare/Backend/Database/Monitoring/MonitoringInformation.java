package sud_tanj.com.icare.Backend.Database.Monitoring;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sud_tanj.com.icare.Backend.Database.HybridDatabase;
import sud_tanj.com.icare.Backend.Database.OnDataChanges;
import sud_tanj.com.icare.Backend.Database.SyncableObject;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 07/08/2018 - 15:17.
 * <p>
 * This class last modified by User
 */
@NoArgsConstructor
public class MonitoringInformation extends SyncableObject implements OnDataChanges {
    public static final String SENSOR_DATA_CHILD_NAME = "https://icare-89c17.firebaseio.com/Sensor";
    @Getter
    private List<String> healthDatas = new ArrayList<>(),
            individualComments =new ArrayList<>(),
            medicalComments =new ArrayList<>(),
            analysisDatas=new ArrayList<>(),
            developer=new ArrayList<>(),
            graphLegend=new ArrayList<>();
    @Getter @Setter
    private Boolean monitoring;
    @Getter @Setter
    private String name,image;
    @Exclude
    private List<MonitoringListener> monitoringListeners=new ArrayList<>();

    public void addListener(MonitoringListener listener){
        this.monitoringListeners.add(listener);
    }

    public MonitoringInformation(String identification){
        this.identification=identification;
        database=new HybridDatabase(FirebaseDatabase.getInstance()
                .getReferenceFromUrl(SENSOR_DATA_CHILD_NAME).child(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid()).child(identification));
        database.onDataChanges(this);
    }

    public void sync(){
        if(database==null){
            DatabaseReference databaseReference=FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(SENSOR_DATA_CHILD_NAME).push();
            this.identification=databaseReference.getKey();
            database=new HybridDatabase(databaseReference);
            database.onDataChanges(this);
        }
        super.sync();
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void onDataChanges(DataSnapshot dataSnapshot) {
        MonitoringInformation newData=dataSnapshot.getValue(MonitoringInformation.class);
        this.identification=dataSnapshot.getKey();
        this.name=newData.name;
        this.analysisDatas=newData.analysisDatas;
        this.developer=newData.developer;
        this.graphLegend=newData.graphLegend;
        this.healthDatas=newData.healthDatas;
        this.individualComments=newData.individualComments;
        this.medicalComments=newData.medicalComments;
        this.monitoring=newData.monitoring;
        newData=null;
    }

    @Override
    public void postLoad() {
        for (Iterator<MonitoringListener> iterator = this.monitoringListeners.iterator(); iterator.hasNext(); ){
            MonitoringListener temp=iterator.next();
            temp.onReady(this);
            if(temp.isRunOnlyOnce()){
                iterator.remove();
            }
        }
    }
}
