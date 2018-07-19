package sud_tanj.com.icare;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.mikepenz.aboutlibraries.Libs.ActivityStyle;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.ncapdevi.fragnav.FragNavController;

import activitystarter.Arg;
import butterknife.ButterKnife;
import io.paperdb.Paper;
import sud_tanj.com.icare.Frontend.Activity.BaseActivity;
import sud_tanj.com.icare.Frontend.Animation.LoadingScreen;
import sud_tanj.com.icare.Frontend.Fragment.FragmentBuilder;
import sud_tanj.com.icare.Frontend.Notification.Notification;

public class MainActivity extends BaseActivity implements OnFragmentInteractionListener {

    private FragNavController.Builder builder;

    @Arg FirebaseUser firebaseUser;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initActivityBeforeLoad(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initActivityAfterLoad(savedInstanceState);

        initNavigationDrawer();
        Notification.notifyUser("Hello "+firebaseUser.getDisplayName());
    }

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
                        .setImage(getResources().getDrawable(R.drawable.ic_healthdata))
                        .setTextPrimary(getString(R.string.Health_Data_Menu_Title))
                        .setTextSecondary(getString(R.string.Health_Data_Menu_Description))
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long id, int position) {
                                Toast.makeText(MainActivity.this, "Clicked first item #" + id, Toast.LENGTH_SHORT).show();
                            }
                        })
        );
        addDivider();
        addItem(
                new DrawerItem()
                        .setTextPrimary(getString(R.string.about_menu_title))
                        .setTextSecondary(getString(R.string.about_menu_descriptioni))
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long id, int position) {
                                new LibsBuilder().withActivityStyle(ActivityStyle.LIGHT)
                                        //start the activity
                                        .start(MainActivity.this);
                            }
                        })
        );
    }

    private void initActivityBeforeLoad(Bundle savedInstanceState){
        //Init Offline Storage
        Paper.init(getApplicationContext());
        //Init ButterKnife
        ButterKnife.bind(this);
        //Init Loading Screen
        LoadingScreen.init(this);
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
}
