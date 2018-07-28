package sud_tanj.com.icare.Backend.Database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.paperdb.Paper;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 19/07/2018 - 8:35.
 * <p>
 * This class last modified by User
 */
public class HybridDatabase implements ValueEventListener{
    private DatabaseReference databaseReference;
    private String path;
    private ArrayList<OnDataChanges> onDataChangesArrayList;
    public HybridDatabase(String path){
        this.path=path;
        this.onDataChangesArrayList=new ArrayList<>();
        this.databaseReference=FirebaseDatabase.getInstance().getReference(path);
        this.databaseReference.addValueEventListener(this);
    }

    public void addChild(String key,Object value){
        this.databaseReference.child(key).setValue(value);
        Paper.book().write(this.path+"/"+key,value);
        fireEventListener();
    }

    public void setValue(Object value){
        this.databaseReference.setValue(value);
        Paper.book().write(this.path,value);
        fireEventListener();
    }

    public void onDataChanges(OnDataChanges onDataChanges){
        this.onDataChangesArrayList.add(onDataChanges);
    }

    public String getPath() {
        return path;
    }

    private void fireEventListener(){
        for(OnDataChanges temp:this.onDataChangesArrayList){
            temp.onDataChanges();
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(this.path.contains(dataSnapshot.getKey())){
            Paper.book().write(this.path,dataSnapshot.getValue());
            fireEventListener();
        }
        String childPath=this.path+"/"+dataSnapshot.getKey();
        if(Paper.book().contains(childPath)){
            Paper.book().write(childPath,dataSnapshot.getValue());
            fireEventListener();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
