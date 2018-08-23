package sud_tanj.com.icare.Frontend.Fragment.DataCatalogue;

import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;

import sud_tanj.com.icare.R;

@EActivity(R.layout.activity_data_details)
public class DataDetails extends AppCompatActivity {

    @Extra("MonitorId")
    protected String id;

    @AfterExtras
    protected void init(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));
    }

    @OptionsItem(android.R.id.home)
    protected void backButtonClicked(){
        this.onStop();
        finish();
    }

}
