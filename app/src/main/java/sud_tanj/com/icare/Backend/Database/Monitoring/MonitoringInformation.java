package sud_tanj.com.icare.Backend.Database.Monitoring;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import sud_tanj.com.icare.Backend.Database.HealthData;
import sud_tanj.com.icare.Backend.Database.HybridDatabase;
import sud_tanj.com.icare.Backend.Database.OnDataChanges;

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
public class MonitoringInformation implements OnDataChanges {
    public static final String SENSOR_DATA_CHILD_NAME = "https://icare-89c17.firebaseio.com/Sensor";
    @Getter
    private List<String> healthDatas = new ArrayList<>(),
            individualComments =new ArrayList<>(),
            medicalComments =new ArrayList<>(),
            analysisDatas=new ArrayList<>(),
            developer=new ArrayList<>(),
            graphLegend=new ArrayList<>();
    @Exclude
    private transient String identification = "";
    @Getter
    private Boolean monitoring;
    @Getter
    private String name,image;
    @Exclude
    private transient HybridDatabase database=null;

    public MonitoringInformation(String identification){
        this.identification=identification;
        database=new HybridDatabase(FirebaseDatabase.getInstance()
                .getReferenceFromUrl(SENSOR_DATA_CHILD_NAME).child(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid()).child(identification));
        database.onDataChanges(this);
    }

    public void addHealthData(HealthData s) {
        this.analysisDatas.add(s.getId());
        sync();
    }

    public void addIndividualComments(String s){
        this.analysisDatas.add(s);
        sync();
    }

    public void addMedicalComments(String s){
        this.analysisDatas.add(s);
        sync();
    }

    public void addAnalysisData(String s){
        this.analysisDatas.add(s);
        sync();
    }

    public void addGraphLegend(String s){
        this.graphLegend.add(s);
        sync();
    }

    public void setName(String name) {
        this.name = name;
        sync();
    }

    public void setImage(String image) {
        this.image = image;
        sync();
    }

    private void sync(){
        if(database==null){
            database=new HybridDatabase(FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(SENSOR_DATA_CHILD_NAME).child(FirebaseAuth.getInstance()
                            .getCurrentUser().getUid())).addChild(this);
            database.onDataChanges(this);
        }
        database.setValue(this);
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

    }
}
