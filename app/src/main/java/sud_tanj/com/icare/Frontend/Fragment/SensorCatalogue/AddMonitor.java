package sud_tanj.com.icare.Frontend.Fragment.SensorCatalogue;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder.IconValue;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import me.riddhimanadib.formmaster.model.BaseFormElement;
import me.riddhimanadib.formmaster.model.FormHeader;
import sud_tanj.com.icare.Backend.Database.HybridReference;
import sud_tanj.com.icare.Backend.Database.Monitoring.MonitoringInformation;
import sud_tanj.com.icare.Frontend.Icon.IconBuilder;
import sud_tanj.com.icare.Frontend.Notification.Notification;
import sud_tanj.com.icare.R;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 20/08/2018 - 10:05.
 * <p>
 * This class last modified by User
 */
@EActivity(R.layout.activity_add_modify_monitor)
public class AddMonitor extends AddModifyMonitor {
    @ViewById(R.id.addormodify_recycler_view)
    RecyclerView superRecyclerView;
    @ViewById(R.id.save_buton_floating)
    FloatingActionButton saveButton;
    @ViewById(R.id.delete_button_floating)
    protected FloatingActionButton deleteButton;

    @AfterViews
    protected void init(){
        this.initUI();
        saveButton.setImageDrawable(IconBuilder.get(IconValue.CONTENT_SAVE,R.color.temperature_text));
        deleteButton.setImageDrawable(IconBuilder.get(IconValue.DELETE,R.color.temperature_text));
    }
    @Override
    protected RecyclerView getRecyclerView() {
        return superRecyclerView;
    }

    @Override
    protected void formValueChanged(BaseFormElement baseFormElement) {

    }

    @Override
    protected FormHeader getFormHeader() {
        return FormHeader.createInstance(getString(R.string.new_monitor_header));
    }


    @Click(R.id.save_buton_floating)
    protected void saveButtonClicked(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReferenceFromUrl(MonitoringInformation.KEY).push();
        HybridReference hybridReference=new HybridReference(reference);
        MonitoringInformation monitoringInformation=new MonitoringInformation();
        monitoringInformation.setDescription(mFormBuilder.getFormElement(ELEMENT_DESCRIPTION).getValue());
        if(mFormBuilder.getFormElement(ELEMENT_STATUS).getValue().equals(STATUS_ON))
            monitoringInformation.setMonitoring(true);
        else
            monitoringInformation.setMonitoring(false);
        monitoringInformation.setImage(mFormBuilder.getFormElement(ELEMENT_IMAGE).getValue());
        monitoringInformation.setName(mFormBuilder.getFormElement(ELEMENT_NAME).getValue());
        String result=mFormBuilder.getFormElement(ELEMENT_GRAPHLEGEND).getValue();
        String[] spliter=result.split("\n");
        monitoringInformation.getGraphLegend().clear();
        for(String temp:spliter){
            monitoringInformation.getGraphLegend().add(temp);
        }
        hybridReference.setValue(monitoringInformation);
        Notification.notifySuccessful(getString(R.string.add_new_monitor_successful_notification,reference.getKey()));
        this.onStop();
    }

    @Click(R.id.delete_button_floating)
    protected void deleteButtonClicked(){
        this.onStop();
    }
}
