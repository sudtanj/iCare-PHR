package sud_tanj.com.icare.Frontend.Fragment.SensorCatalogue;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder.IconValue;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import me.riddhimanadib.formmaster.model.BaseFormElement;
import me.riddhimanadib.formmaster.model.FormElementTextMultiLine;
import me.riddhimanadib.formmaster.model.FormHeader;
import sud_tanj.com.icare.Backend.Database.HybridReference;
import sud_tanj.com.icare.Backend.Database.Monitoring.MonitoringInformation;
import sud_tanj.com.icare.Frontend.Animation.LoadingScreen;
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
public class ModifyMonitor extends AddModifyMonitor implements ValueEventListener {
    @ViewById(R.id.addormodify_recycler_view)
    protected RecyclerView superRecyclerView;
    @ViewById(R.id.save_buton_floating)
    protected FloatingActionButton saveButton;
    @ViewById(R.id.delete_button_floating)
    protected FloatingActionButton deleteButton;
    @Extra("MonitorID")
    protected String id;
    private MonitoringInformation monitoringInformation;

    @AfterViews
    protected void init() {
        this.initUI();
        FormElementTextMultiLine developerElement= FormElementTextMultiLine
                .createInstance()
                .setTag(ELEMENT_DEVELOPER)
                .setTitle(getString(R.string.developer_monitor_title))
                .setHint(getString(R.string.developer_monitor_hint));
        formItems.add(developerElement);
        mFormBuilder.addFormElements(formItems);
        LoadingScreen.showLoadingScreen(getString(R.string.modify_monitor_loading_messages));
        FirebaseDatabase.getInstance().getReferenceFromUrl(MonitoringInformation.KEY)
                .child(id).addListenerForSingleValueEvent(this);
        saveButton.setImageDrawable(IconBuilder.get(IconValue.CONTENT_SAVE, R.color.temperature_text));
        deleteButton.setImageDrawable(IconBuilder.get(IconValue.DELETE,R.color.temperature_text));
    }

    @Override
    protected RecyclerView getRecyclerView() {
        return superRecyclerView;
    }

    @Override
    protected void formValueChanged(BaseFormElement baseFormElement) {
        if (monitoringInformation != null) {
            HybridReference hybridReference = new HybridReference(
                    FirebaseDatabase.getInstance().getReferenceFromUrl(MonitoringInformation.KEY)
                            .child(id)
            );
            String result=null;
            String[] spliter=null;
            switch (baseFormElement.getTag()) {
                case ELEMENT_DESCRIPTION:
                    monitoringInformation.setDescription(baseFormElement.getValue());
                    break;
                case ELEMENT_IMAGE:
                    monitoringInformation.setImage(baseFormElement.getValue());
                    break;
                case ELEMENT_NAME:
                    monitoringInformation.setName(baseFormElement.getValue());
                    break;
                case ELEMENT_GRAPHLEGEND:
                    result=baseFormElement.getValue();
                    spliter=result.split("\n");
                    monitoringInformation.getGraphLegend().clear();
                    for(String temp:spliter){
                        monitoringInformation.getGraphLegend().add(temp);
                    }
                    break;
                case ELEMENT_DEVELOPER:
                    result=baseFormElement.getValue();
                    spliter=result.split("\n");
                    if(spliter.length>0){
                        if(TextUtils.isEmpty(spliter[0])) {
                            monitoringInformation.getDeveloper().clear();
                            monitoringInformation.getDeveloper().add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        } else {
                            monitoringInformation.getDeveloper().clear();
                            for (String temp : spliter) {
                                monitoringInformation.getDeveloper().add(temp);
                            }
                        }
                    }
                    break;
                case ELEMENT_STATUS:
                    if (baseFormElement.getValue().equals(STATUS_ON)) {
                        monitoringInformation.setMonitoring(true);
                    } else {
                        monitoringInformation.setMonitoring(false);
                    }
                    break;
                default:
                    break;
            }
            hybridReference.setValue(monitoringInformation);
        }
    }

    @Override
    protected FormHeader getFormHeader() {
        FormHeader formHeader = FormHeader.createInstance(getString(R.string.header_modify_monitor, ""));
        formHeader.setTag(ELEMENT_HEADER);
        return formHeader;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        monitoringInformation = dataSnapshot.getValue(MonitoringInformation.class);
        if(monitoringInformation.getDeveloper().indexOf(FirebaseAuth.getInstance().getCurrentUser().getUid())==-1){
            this.onStop();
            Notification.notifyFailure("Restricted Area! Developer's only!");
        }
        this.mFormBuilder.getFormElement(ELEMENT_HEADER).setTitle(
                getString(R.string.header_modify_monitor, dataSnapshot.getKey()));
        this.mFormBuilder.getFormElement(ELEMENT_NAME).setValue(monitoringInformation.getName());
        this.mFormBuilder.getFormElement(ELEMENT_DESCRIPTION).setValue(monitoringInformation.getDescription());
        this.mFormBuilder.getFormElement(ELEMENT_IMAGE).setValue(monitoringInformation.getImage());
        if (monitoringInformation.getMonitoring())
            this.mFormBuilder.getFormElement(ELEMENT_STATUS).setValue(STATUS_ON);
        else
            this.mFormBuilder.getFormElement(ELEMENT_STATUS).setValue(STATUS_OFF);
        String graphLegend=TextUtils.join("\n",monitoringInformation.getGraphLegend());
        this.mFormBuilder.getFormElement(ELEMENT_GRAPHLEGEND).setValue(graphLegend);
        String developer=TextUtils.join("\n",monitoringInformation.getDeveloper());
        this.mFormBuilder.getFormElement(ELEMENT_DEVELOPER).setValue(developer);
        superRecyclerView.getAdapter().notifyDataSetChanged();
        LoadingScreen.hideLoadingScreen();
    }

    @Click(R.id.save_buton_floating)
    protected void saveButtonClicked(){
        Notification.notifySuccessful(getString(R.string.modify_successful_notification));
        this.onStop();
    }

    @Click(R.id.delete_button_floating)
    protected void deleteButtonClicked(){
        FirebaseDatabase.getInstance().getReferenceFromUrl(MonitoringInformation.KEY)
                .child(id).removeValue();
        this.onStop();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        LoadingScreen.hideLoadingScreen();
        this.onStop();
    }
}
