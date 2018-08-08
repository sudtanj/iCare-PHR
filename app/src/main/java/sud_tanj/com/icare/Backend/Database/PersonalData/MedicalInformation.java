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
 * Created by Sudono Tanjung on 08/08/2018 - 11:03.
 * <p>
 * This class last modified by User
 */
@NoArgsConstructor
public class MedicalInformation extends SyncableObject implements OnDataChanges {
    public static final String KEY="https://icare-89c17.firebaseio.com/MedicalComment";
    @Getter @Setter
    private String message="";
    @Exclude
    private List<MedicalInformationListener> listener=new ArrayList<>();

    public MedicalInformation (String identification){
        this.identification=identification;
        database=new HybridDatabase(FirebaseDatabase.getInstance()
                .getReferenceFromUrl(KEY).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(this.identification));
        database.onDataChanges(this);
    }

    public void modifyMessage(String message){
        this.message=message;
        DatabaseReference databaseReference=FirebaseDatabase.getInstance()
                .getReferenceFromUrl(KEY).child(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid()).push();
        this.identification=databaseReference.getKey();
        database=new HybridDatabase(databaseReference);
        database.onDataChanges(this);
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void onDataChanges(DataSnapshot dataSnapshot) {
        MedicalInformation newData = dataSnapshot.getValue(MedicalInformation.class);
        this.identification=dataSnapshot.getKey();
        this.message=newData.message;
        this.timeStamp=newData.timeStamp;
    }

    @Override
    public void postLoad() {
        for (Iterator<MedicalInformationListener> iterator = this.listener.iterator(); iterator.hasNext(); ){
            MedicalInformationListener temp=iterator.next();
            temp.onReady(this);
            if(temp.isRunOnlyOnce()){
                iterator.remove();
            }
        }
    }
}
