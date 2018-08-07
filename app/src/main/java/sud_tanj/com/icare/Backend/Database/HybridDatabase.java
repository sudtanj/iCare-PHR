package sud_tanj.com.icare.Backend.Database;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.paperdb.Paper;
import sud_tanj.com.icare.Frontend.Animation.LoadingScreen;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 19/07/2018 - 8:35.
 * <p>
 * This class last modified by User
 */

public class HybridDatabase implements ValueEventListener, DatabaseReference.CompletionListener {
    //public static final String PAPER_OFFLINE_DATA_COLLECTION = "paperarray";
    public static final String PAPER_OFFLINE_KEY_COLLECTION = "keycollection";
    private DatabaseReference databaseReference;
    private ArrayList<OnDataChanges> onDataChangesArrayList;

    public HybridDatabase(DatabaseReference databaseReference) {
        this.onDataChangesArrayList = new ArrayList<>();
        this.databaseReference = databaseReference;
    }

    public static void init() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(Boolean.TRUE);
        DatabaseReference syncReference;
        HybridDatabase syncDatabase;
        LoadingScreen.showLoadingScreen("Synchronize offline data with central database");
        List<String> syncObjects = Paper.book().read(PAPER_OFFLINE_KEY_COLLECTION
                , new ArrayList<String>());
        for (Iterator<String> iterator = syncObjects.iterator(); iterator.hasNext(); ) {
            String target=iterator.next();
            Object value=Paper.book().read(target,null);
            if(value!=null) {
                syncReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Uri.decode(target));
                syncDatabase = new HybridDatabase(syncReference);
                syncDatabase.setValue(value);
            }
            else {
                iterator.remove();
            }
        }
        Paper.book().write(PAPER_OFFLINE_KEY_COLLECTION,syncObjects);
        LoadingScreen.hideLoadingScreen();
    }

    public HybridDatabase setValue(Object value) {
        cacheToFirebase(databaseReference, value);
        cacheToPaper(databaseReference.toString(), value);
        return this;
    }

    public String getKey() {
        return databaseReference.getKey();
    }

    public HybridDatabase addChild(Object value) {
        DatabaseReference temp = this.databaseReference.push();
        cacheToFirebase(temp, value);
        cacheToPaper(temp.toString(), value);
        return new HybridDatabase(temp);
    }

    private void cacheToFirebase(DatabaseReference databaseReference, Object value) {
        databaseReference.setValue(value, this);
        databaseReference.keepSynced(Boolean.TRUE);
    }

    private void cacheToPaper(String childPath, Object value) {
        List<String> syncObject = Paper.book().read(PAPER_OFFLINE_KEY_COLLECTION,new ArrayList<String>());
        String link=Uri.encode(childPath);
        syncObject.add(link);
        Paper.book().write(link, value);
        Paper.book().write(PAPER_OFFLINE_KEY_COLLECTION,syncObject);
    }

    public HybridDatabase onDataChanges(OnDataChanges onDataChanges) {
        this.onDataChangesArrayList.add(onDataChanges);
        this.databaseReference.addValueEventListener(this);
        return this;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (OnDataChanges temp : this.onDataChangesArrayList) {
            temp.preLoad();
            temp.onDataChanges(dataSnapshot);
            temp.postLoad();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
        if (databaseError == null) {
            List<String> syncObject = Paper.book().read(PAPER_OFFLINE_KEY_COLLECTION
                    , new ArrayList<String>());
            for (String temp : syncObject) {
                if (temp.equals(Uri.encode(databaseReference.toString()))) {
                    Paper.book().delete(temp);
                    syncObject.remove(temp);
                    Paper.book().write(PAPER_OFFLINE_KEY_COLLECTION,syncObject);
                    break;
                }
            }
        }
    }
}
