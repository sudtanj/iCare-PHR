package sud_tanj.com.icare;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.mikepenz.aboutlibraries.Libs.ActivityStyle;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.ncapdevi.fragnav.FragNavController;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder.IconValue;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.WindowFeature;

import java.util.Locale;

import io.paperdb.Paper;
import sharefirebasepreferences.crysxd.de.lib.SharedFirebasePreferences;
import spencerstudios.com.bungeelib.Bungee;
import sud_tanj.com.icare.Frontend.Activity.BaseActivity;
import sud_tanj.com.icare.Frontend.Animation.LoadingScreen;
import sud_tanj.com.icare.Frontend.Fragment.FragmentBuilder;
import sud_tanj.com.icare.Frontend.Icon.IconBuilder;
import sud_tanj.com.icare.Frontend.Notification.Notification;
import sud_tanj.com.icare.Frontend.Settings.SettingsFragment_;

@EActivity(R.layout.activity_main)
@WindowFeature(Window.FEATURE_ACTION_BAR)
public class MainActivity extends BaseActivity implements OnFragmentInteractionListener,DrawerItem.OnItemClickListener {

    private FragNavController.Builder builder;
    @Extra("firebaseUser")
    FirebaseUser firebaseUser;
    @Extra("googleSigninObject")
    GoogleSignInOptions gso;

    private void initNavigationDrawer(){
        addProfile(
                new DrawerProfile()
                        .setRoundedAvatar((BitmapDrawable)getResources().getDrawable(R.drawable.ic_arduino))
                        .setBackground((BitmapDrawable)getResources().getDrawable(R.drawable.nav_bar_background))
                        .setName(firebaseUser.getDisplayName())
                        .setDescription(getString(R.string.default_email))
                        .setOnProfileClickListener(new DrawerProfile.OnProfileClickListener() {
                            @Override
                            public void onClick(DrawerProfile drawerProfile, long id) {
                                Toast.makeText(MainActivity.this, "Clicked profile #" + id, Toast.LENGTH_SHORT).show();
                            }
                        })
        );
        addItem(
                new DrawerItem()
                        .setImage(IconBuilder.get(IconValue.HEART_PULSE))
                        .setTextPrimary(getString(R.string.Health_Data_Menu_Title))
                        .setTextSecondary(getString(R.string.Health_Data_Menu_Description))
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
    }
    @AfterViews
    protected void initActivity(){
        //Init Offline Storage
        Paper.init(getApplicationContext());
        //Init Loading Screen
        LoadingScreen.init(this);
        //Init Icon Builder
        IconBuilder.init(this);
        //init fragment manager
        FragmentBuilder.init(getIntent().getExtras(),getSupportFragmentManager());
        FragmentBuilder.changeFragment(HealthDataList_.newInstance("A","B"));
        //Init Notification
        Notification.init(getApplicationContext());
        //init Drawer
        initNavigationDrawer();
        //Init Firebase Shared Preferences
        SharedFirebasePreferences.setPathPattern(String.format(Locale.ENGLISH, "users/shared_prefs/%s", firebaseUser.getUid()));
        SharedFirebasePreferences.getDefaultInstance(getApplicationContext()).keepSynced(Boolean.TRUE);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(DrawerItem drawerItem, long l, int position) {
        if(drawerItem.getTextPrimary().equals(getString(R.string.Health_Data_Menu_Title))){

        }
        if(drawerItem.getTextPrimary().equals(getString(R.string.settings_menu_title))){
            //SettingsFragment settingsFragment=new SettingsFragment_();
            FragmentBuilder.changeFragment(SettingsFragment_.builder().build());
        }
        if (drawerItem.getTextPrimary().equals(getString(R.string.about_menu_title))){
            new LibsBuilder().withActivityStyle(ActivityStyle.LIGHT).withAboutAppName(getString(R.string.app_name))
                    //start the activity
                    .start(MainActivity.this);
            Bungee.card(MainActivity.this);
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
}
