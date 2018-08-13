package sud_tanj.com.icare;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.view.Window;

import com.badoo.mobile.util.WeakHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile.OnProfileClickListener;
import com.mikepenz.aboutlibraries.Libs.ActivityStyle;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsSupportFragment;
import com.nanotasks.Tasks;
import com.ncapdevi.fragnav.FragNavController;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder.IconValue;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.WindowFeature;

import java.util.concurrent.TimeUnit;

import sharefirebasepreferences.crysxd.de.lib.SharedFirebasePreferencesContextWrapper;
import sud_tanj.com.icare.Backend.BackgroundJob.BackgroundDataReceiver;
import sud_tanj.com.icare.Backend.Database.HybridReference;
import sud_tanj.com.icare.Backend.Microcontrollers.BaseMicrocontroller;
import sud_tanj.com.icare.Backend.Plugins.BasePlugin;
import sud_tanj.com.icare.Backend.Preferences.HybridPreferences;
import sud_tanj.com.icare.Backend.Sensors.BuiltInSensor;
import sud_tanj.com.icare.Frontend.Activity.BaseActivity;
import sud_tanj.com.icare.Frontend.Animation.LoadingScreen;
import sud_tanj.com.icare.Frontend.Fragment.DataCatalogue.DataUi_;
import sud_tanj.com.icare.Frontend.Fragment.FragmentBuilder;
import sud_tanj.com.icare.Frontend.Fragment.SensorCatalogue.SensorUi_;
import sud_tanj.com.icare.Frontend.Icon.IconBuilder;
import sud_tanj.com.icare.Frontend.Listener.FirebaseProfilePictureListener;
import sud_tanj.com.icare.Frontend.Notification.Notification;
import sud_tanj.com.icare.Frontend.Settings.SettingsFragment;
import sud_tanj.com.icare.Frontend.Settings.SettingsFragment_;

@EActivity(R.layout.activity_main)
@WindowFeature(Window.FEATURE_ACTION_BAR)
public class MainActivity extends BaseActivity implements Runnable,OnProfileClickListener,OnFragmentInteractionListener,DrawerItem.OnItemClickListener,OnSharedPreferenceChangeListener {

    private FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
    @Extra("googleSigninObject")
    GoogleSignInOptions gso;
    private FragNavController.Builder builder;
    private DrawerProfile drawerProfile;
    private BackgroundDataReceiver backgroundDataReceiver=new BackgroundDataReceiver();
    private WeakHandler backgroundHandler;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new SharedFirebasePreferencesContextWrapper(newBase));
    }

    @Override
    protected void onStop() {
        super.onStop();
        for(BaseMicrocontroller baseMicrocontroller:BaseMicrocontroller.getBaseMicrocontrollerList()){
            baseMicrocontroller.onDispose();
        }
        for(BasePlugin basePlugin:BasePlugin.getBasePluginList()){
            basePlugin.onDispose();
        }
    }

    @AfterViews
    protected void initActivity(){
        //Init Hybrid Database
        HybridReference.init(this);
        //Init logger
        Logger.addLogAdapter(new AndroidLogAdapter());
        //Init Loading Screen
        LoadingScreen.init(this);
        //Init builtin Sensor
        BuiltInSensor.init(this);
        //Init Microcontroller
        BaseMicrocontroller.init(this);
        //Init Icon Builder
        IconBuilder.init(this);
        //init fragment manager
        FragmentBuilder.init(getIntent().getExtras(),getSupportFragmentManager());
        if(FragmentBuilder.getLastFragment()==null){
           FragmentBuilder.changeFragment(DataUi_.builder().build());
        } else {
            FragmentBuilder.launchLastFragment();
        }
        //Init Notification
        Notification.init(getApplicationContext());
        //Init Hybrid Preferences
        HybridPreferences.init(this);
        HybridPreferences.getFirebaseInstance().registerOnSharedPreferenceChangeListener(this);
        //Init all plugins
        BasePlugin.init();
        //Init background job
        backgroundHandler=new WeakHandler();
        backgroundHandler.postDelayed(this, TimeUnit.SECONDS.toMillis(5));
    }

    @AfterViews
    protected void initNavigationDrawer(){
        drawerProfile=new DrawerProfile()
                .setAvatar(IconBuilder.get(IconValue.CLOUD_OFF_OUTLINE))
                .setBackground(IconBuilder.get(IconValue.HEART_PULSE))
                //.setBackground((BitmapDrawable)getResources().getDrawable(R.drawable.nav_bar_background))
                .setName(firebaseUser.getDisplayName())
                .setDescription(HybridPreferences.getFirebaseInstance().getString(SettingsFragment.AGE_SETTINGS,"")+getString(R.string.profile_age_drawer))
                .setOnProfileClickListener(this);
        addProfile(drawerProfile);
        addItem(
                new DrawerItem()
                        .setImage(IconBuilder.get(IconValue.CLIPBOARD_PULSE))
                        .setTextPrimary(getString(R.string.Health_Data_Menu_Title))
                        .setTextSecondary(getString(R.string.Health_Data_Menu_Description))
                        .setOnItemClickListener(this)
        );
        addItem(
                new DrawerItem()
                        .setImage(IconBuilder.get(IconValue.DEVELOPER_BOARD))
                        .setTextPrimary(getString(R.string.sensor_catalogue_menu_title))
                        .setTextSecondary(getString(R.string.sensor_catalogue_menu_description))
                        .setOnItemClickListener(this)
        );
        addDivider();
        addItem(
                new DrawerItem()
                        .setTextPrimary(getString(R.string.settings_menu_title))
                        .setTextSecondary(getString(R.string.settings_menu_description))
                        .setImage(IconBuilder.get(IconValue.SETTINGS))
                        .setOnItemClickListener(this)
        );
        addItem(
                new DrawerItem()
                        .setTextPrimary(getString(R.string.about_menu_title))
                        .setTextSecondary(getString(R.string.about_menu_descriptioni))
                        .setImage(IconBuilder.get(IconValue.ASSISTANT))
                        .setOnItemClickListener(this)
        );
        addItem(
                new DrawerItem()
                        .setImage(IconBuilder.get(IconValue.LOGOUT_VARIANT))
                        .setTextPrimary(getString(R.string.logout_menu_title))
                        .setOnItemClickListener(this));

        //Init profile picture component
        Glide.with(this)
                .asBitmap()
                .load(firebaseUser.getPhotoUrl())
                .apply(new RequestOptions()
                        .signature(new ObjectKey(System.currentTimeMillis()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(new FirebaseProfilePictureListener(drawerProfile,this));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(DrawerItem drawerItem, long l, int position) {
        if(drawerItem.getTextPrimary().equals(getString(R.string.Health_Data_Menu_Title))){
            FragmentBuilder.changeFragment(DataUi_.builder().build());
        }
        if(drawerItem.getTextPrimary().equals(getString(R.string.sensor_catalogue_menu_title))){
            FragmentBuilder.changeFragment(SensorUi_.builder().build());
            getSupportActionBar().setTitle(R.string.sensor_catalogue_menu_title);
        }
        if(drawerItem.getTextPrimary().equals(getString(R.string.settings_menu_title))){
            //SettingsFragment settingsFragment=new SettingsFragment_();
            FragmentBuilder.changeFragment(SettingsFragment_.builder().build());
            getSupportActionBar().setTitle(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()+getString(R.string.settings_personal_information_headings));
        }
        if (drawerItem.getTextPrimary().equals(getString(R.string.about_menu_title))){
            LibsSupportFragment aboutFragment=new LibsBuilder()
                    .withActivityStyle(ActivityStyle.LIGHT)
                    .withAboutAppName(getString(R.string.app_name))
                    .withAboutIconShown(Boolean.TRUE)
                    .withActivityTitle(getString(R.string.about_us_title))
                    .withAboutDescription(getString(R.string.about_us_description))
                    .withAboutSpecial1(getString(R.string.about_us_special1))
                    .withAboutSpecial1Description(getString(R.string.about_us_special1_description))
                    .withShowLoadingProgress(Boolean.TRUE)
                    .supportFragment();
            FragmentBuilder.changeFragment(aboutFragment);
            getSupportActionBar().setTitle(R.string.about_us_title);
        }
        if (drawerItem.getTextPrimary().equals(getString(R.string.logout_menu_title))){
            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
            FirebaseAuth.getInstance().signOut();
            googleSignInClient.signOut();
            googleSignInClient.revokeAccess();
            finish();
        }
        closeDrawer();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.equals(SettingsFragment.AGE_SETTINGS)){
            drawerProfile.setDescription(sharedPreferences.getString(s,"")+getString(R.string.profile_age_drawer));
            removeProfile(drawerProfile);
            addProfile(drawerProfile);
        }
    }

    @Override
    public void onClick(DrawerProfile drawerProfile, long l) {
        Notification.notifyUser(getString(R.string.profile_changing_guide));
    }

    @Override
    public void run() {
        Tasks.executeInBackground(getApplicationContext(),backgroundDataReceiver,backgroundDataReceiver);
        backgroundHandler.postDelayed(this, TimeUnit.SECONDS.toMillis(5));
    }
}
