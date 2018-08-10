package sud_tanj.com.icare.Backend;

import java.util.ArrayList;
import java.util.List;

import lombok.NoArgsConstructor;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 10/08/2018 - 16:34.
 * <p>
 * This class last modified by User
 */
@NoArgsConstructor
public abstract class BaseAbstractComponent<T,W> implements Runnable {
    protected List<T> listeners=new ArrayList<>();

    public void addListener(T listener){
        if(listeners.indexOf(listener)==-1)
            this.listeners.add(listener);
    }
    public void removeSensorListener(T listener){
        if(listeners.indexOf(listener)>-1){
            this.listeners.remove(listener);
        }
    }

    protected abstract void onEventListenerFired(T listener, W... valuePassed);

    protected void fireEventListener(W... valuePassed){
        for(T listener:listeners){
            onEventListenerFired(listener,valuePassed);
        }
    }

    public abstract void onDispose();

}
