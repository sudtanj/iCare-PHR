package sud_tanj.com.icare.Backend.Database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.paperdb.Paper;
import lombok.Getter;
import lombok.Setter;

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
    @Getter
    private String path;
    private static HybridDatabase hybridDatabase=null;
    private ArrayList<OnDataChanges> onDataChangesArrayList;
    @Setter
    private Class classCasting;

    public static HybridDatabase getInstance(){
        if(hybridDatabase==null)
            hybridDatabase=new HybridDatabase("",FirebaseDatabase.getInstance().getReference());
        return hybridDatabase;
    }

    public HybridDatabase child(String child) {
        return new HybridDatabase(this.path+"-"+child,this.databaseReference.child(child));
    }

    private HybridDatabase(String path,DatabaseReference databaseReference){
        this.path=path;
        this.onDataChangesArrayList=new ArrayList<>();
        this.databaseReference=databaseReference;
    }

    public HybridDatabase sync(){
        this.databaseReference.addValueEventListener(this);
        return this;
    }

    public HybridDatabase unSync(){
        this.databaseReference.removeEventListener(this);
        return this;
    }

    public HybridDatabase setValue(Object value){
        this.databaseReference.setValue(value);
        Paper.book().write(this.path,value);
        fireEventListener();
        return this;
    }

    public String getKey(){
        return databaseReference.getKey();
    }

    public HybridDatabase addChild(Object value){
        DatabaseReference temp=this.databaseReference.push();
        temp.setValue(value);
        Paper.book().write(this.path+"-"+temp.getKey(),value);
        return new HybridDatabase(this.path+"-"+temp.getKey(),temp);
    }

    public HybridDatabase onDataChanges(OnDataChanges onDataChanges){
        this.onDataChangesArrayList.add(onDataChanges);
        fireEventListener();
        return this;
    }

    private void fireEventListener(){
        if(Paper.book().read(this.path)!=null) {
            for (OnDataChanges temp : this.onDataChangesArrayList) {
                temp.preLoad();
                temp.onDataChanges(Paper.book().read(this.path));
                temp.postLoad();
            }
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Paper.book().write(this.path, dataSnapshot.getValue(classCasting));
        fireEventListener();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
