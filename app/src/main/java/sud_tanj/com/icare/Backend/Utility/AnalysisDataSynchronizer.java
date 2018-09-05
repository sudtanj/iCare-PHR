package sud_tanj.com.icare.Backend.Utility;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import sud_tanj.com.icare.Backend.Database.HybridReference;
import sud_tanj.com.icare.Backend.Database.PersonalData.DataAnalysis;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 05/09/2018 - 8:41.
 * <p>
 * This class last modified by User
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnalysisDataSynchronizer implements ValueEventListener {
    private String identification;
    private DataAnalysis dataAnalysis;
    public static void sync(String identification, DataAnalysis dataAnalysis){
        FirebaseDatabase.getInstance().getReferenceFromUrl(DataAnalysis.KEY)
                .child(identification).orderByKey().limitToLast(1)
                .addListenerForSingleValueEvent(new AnalysisDataSynchronizer(identification,dataAnalysis));
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if(dataSnapshot.getChildrenCount()==0){
            HybridReference hybridReference=new HybridReference(
                    FirebaseDatabase.getInstance().getReferenceFromUrl(DataAnalysis.KEY)
                            .child(identification).push()
            );
            hybridReference.setValue(dataAnalysis);
        }
        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
            DataAnalysis dataAnalysis=dataSnapshot1.getValue(DataAnalysis.class);
            Calendar c= Calendar.getInstance();
            c.setTimeInMillis(dataAnalysis.getTimeStamp());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
            if(!simpleDateFormat.format(c.getTime()).equals(simpleDateFormat.format(Calendar.getInstance().getTime()))){
                HybridReference hybridReference=new HybridReference(
                        FirebaseDatabase.getInstance().getReferenceFromUrl(DataAnalysis.KEY)
                                .child(identification).push()
                );
                hybridReference.setValue(this.dataAnalysis);
            } else {
                HybridReference hybridReference=new HybridReference(
                        dataSnapshot1.getRef());
                dataAnalysis.setAnalysisMessage(this.dataAnalysis.getAnalysisMessage());
                dataAnalysis.setCondition(this.dataAnalysis.getCondition());
                hybridReference.setValue(dataAnalysis);
            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
