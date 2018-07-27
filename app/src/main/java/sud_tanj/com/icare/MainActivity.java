package sud_tanj.com.icare;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.marcinmoskala.activitystarterparcelerargconverter.ParcelarArgConverter;
import com.mikepenz.aboutlibraries.Libs.ActivityStyle;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.ncapdevi.fragnav.FragNavController;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder.IconValue;

import activitystarter.ActivityStarterConfig;
import activitystarter.Arg;
import butterknife.ButterKnife;
import io.paperdb.Paper;
import spencerstudios.com.bungeelib.Bungee;
import sud_tanj.com.icare.Backend.Login.BasicUser;
import sud_tanj.com.icare.Frontend.Activity.BaseActivity;
import sud_tanj.com.icare.Frontend.Animation.LoadingScreen;
import sud_tanj.com.icare.Frontend.Fragment.FragmentBuilder;
import sud_tanj.com.icare.Frontend.Icon.IconBuilder;
import sud_tanj.com.icare.Frontend.Notification.Notification;

@ActivityStarterConfig(converters = { ParcelarArgConverter.class })
public class MainActivity extends BaseActivity implements OnFragmentInteractionListener,DrawerItem.OnItemClickListener {

    private FragNavController.Builder builder;

    @Arg BasicUser basicUser;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initActivityBeforeLoad(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initActivityAfterLoad(savedInstanceState);

        initNavigationDrawer();
        Notification.notifyUser("Hello "+basicUser.getDisplayName());
    }

    private void initNavigationDrawer(){
        addProfile(
                new DrawerProfile()
                        .setRoundedAvatar((BitmapDrawable)getResources().getDrawable(R.drawable.ic_arduino))
                        .setBackground((BitmapDrawable)getResources().getDrawable(R.drawable.nav_bar_background))
                        .setName(basicUser.getDisplayName())
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
                        .setImage(getResources().getDrawable(R.drawable.ic_healthdata))
                        .setTextPrimary(getString(R.string.Health_Data_Menu_Title))
                        .setTextSecondary(getString(R.string.Health_Data_Menu_Description))
                        .setOnItemClickListener(this)
        );
        addDivider();
        addItem(
                new DrawerItem()
                        .setTextPrimary(getString(R.string.about_menu_title))
                        .setTextSecondary(getString(R.string.about_menu_descriptioni))
                        .setOnItemClickListener(this)
        );
        addItem(new DrawerItem().setImage(IconBuilder.get(IconValue.LOGOUT_VARIANT)).setTextPrimary(getString(R.string.logout_menu_title)).setOnItemClickListener(this));
    }

    private void initActivityBeforeLoad(Bundle savedInstanceState){
        //Init Offline Storage
        Paper.init(getApplicationContext());
        //Init ButterKnife
        ButterKnife.bind(this);
        //Init Loading Screen
        LoadingScreen.init(this);
        //Init Icon Builder
        IconBuilder.init(this);
    }

    private void initActivityAfterLoad(Bundle savedInstanceState){
        //init fragment manager
        FragmentBuilder.init(savedInstanceState,getSupportFragmentManager());
        FragmentBuilder.changeFragment(HealthDataList.newInstance("A","B"));
        //Init Notification
        Notification.init(getApplicationContext());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(DrawerItem drawerItem, long l, int position) {
        if(drawerItem.getTextPrimary().equals(getString(R.string.Health_Data_Menu_Title))){

        }
        if (drawerItem.getTextPrimary().equals(getString(R.string.about_menu_title))){
            new LibsBuilder().withActivityStyle(ActivityStyle.LIGHT)
                    //start the activity
                    .start(MainActivity.this);
            Bungee.card(MainActivity.this);
        }
        if (drawerItem.getTextPrimary().equals(getString(R.string.logout_menu_title))){

        }
    }
}
