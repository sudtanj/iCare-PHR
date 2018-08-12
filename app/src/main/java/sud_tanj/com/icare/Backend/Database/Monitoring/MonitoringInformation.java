package sud_tanj.com.icare.Backend.Database.Monitoring;

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
 * Created by Sudono Tanjung on 07/08/2018 - 15:17.
 * <p>
 * This class last modified by User
 */
@NoArgsConstructor
public class MonitoringInformation extends SyncableObject {
    public static final String KEY = "https://icare-89c17.firebaseio.com/Sensor";
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
    private String name,image,description;

    public MonitoringInformation(DatabaseReference databaseReference) {
        super(databaseReference);
    }
}
