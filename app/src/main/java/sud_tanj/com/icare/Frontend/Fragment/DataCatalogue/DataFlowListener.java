package sud_tanj.com.icare.Frontend.Fragment.DataCatalogue;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.badoo.mobile.util.WeakHandler;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import sud_tanj.com.icare.Backend.Database.PersonalData.HealthData;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 05/09/2018 - 9:13.
 * <p>
 * This class last modified by User
 */
@RequiredArgsConstructor(access= AccessLevel.PROTECTED)
public class DataFlowListener implements ChildEventListener,Runnable {
    private final Button dataStatusButton;
    private WeakHandler weakHandler=new WeakHandler();

    public static void listen(Button dataStatusButton,String identification){
        FirebaseDatabase.getInstance().getReferenceFromUrl(HealthData.KEY)
                .child(identification).addChildEventListener(new DataFlowListener(dataStatusButton));
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        weakHandler.removeCallbacksAndMessages(this);
        dataStatusButton.setText("Data Flow Status : On");
        weakHandler.postDelayed(this, TimeUnit.SECONDS.toMillis(5));
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    @Override
    public void run() {
        dataStatusButton.setText("Data Flow Status : Off");
    }
}
