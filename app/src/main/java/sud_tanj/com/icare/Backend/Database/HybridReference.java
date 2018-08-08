package sud_tanj.com.icare.Backend.Database;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.paperdb.Paper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sud_tanj.com.icare.Frontend.Animation.LoadingScreen;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 08/08/2018 - 14:38.
 * <p>
 * This class last modified by User
 */

@RequiredArgsConstructor
public class HybridReference implements CompletionListener {
    protected static final String PAPER_OFFLINE_KEY_COLLECTION = "keycollection";
    @Getter @NonNull
    private DatabaseReference databaseReference;
    @Nullable
    private CompletionListener completionListener;

    @NonNull
    public void setValue(@Nullable Object o) {
        databaseReference.setValue(o,this);
        cacheToFirebase();
        cacheToPaper(databaseReference.toString(), o);
    }

    public static void init() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(Boolean.TRUE);
        DatabaseReference syncReference;
        HybridReference syncDatabase;
        LoadingScreen.showLoadingScreen("Synchronize offline data with central database");
        List<String> syncObjects = Paper.book().read(PAPER_OFFLINE_KEY_COLLECTION
                , new ArrayList<String>());
        for (Iterator<String> iterator = syncObjects.iterator(); iterator.hasNext(); ) {
            String target=iterator.next();
            Object value=Paper.book().read(target,null);
            if(value!=null) {
                syncReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Uri.decode(target));
                syncDatabase = new HybridReference(syncReference);
                syncDatabase.setValue(value);
            }
            else {
                iterator.remove();
            }
        }
        Paper.book().write(PAPER_OFFLINE_KEY_COLLECTION,syncObjects);
        LoadingScreen.hideLoadingScreen();
    }

    public void setValue(@Nullable Object o, @Nullable CompletionListener completionListener) {
        databaseReference.setValue(o, this);
        cacheToFirebase();
        cacheToPaper(databaseReference.toString(), o);
    }

    private void cacheToFirebase() {
        databaseReference.keepSynced(Boolean.TRUE);
    }

    private void cacheToPaper(String childPath, Object value) {
        List<String> syncObject = Paper.book().read(PAPER_OFFLINE_KEY_COLLECTION,new ArrayList<String>());
        String link=Uri.encode(childPath);
        syncObject.add(link);
        Paper.book().write(link, value);
        Paper.book().write(PAPER_OFFLINE_KEY_COLLECTION,syncObject);
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
        if(completionListener!=null)
            completionListener.onComplete(databaseError,databaseReference);
    }
}
