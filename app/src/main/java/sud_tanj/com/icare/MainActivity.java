package sud_tanj.com.icare;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.ncapdevi.fragnav.FragNavController;

import activitystarter.Arg;
import butterknife.ButterKnife;
import io.paperdb.Paper;
import sud_tanj.com.icare.Frontend.Activity.BaseActivity;
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
                        .setName(getString(R.string.default_profile_name))
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
    }

    private void initActivityBeforeLoad(Bundle savedInstanceState){
        //Init Offline Storage
        Paper.init(getApplicationContext());
        //Init ButterKnife
        ButterKnife.bind(this);
    }

    private void initActivityAfterLoad(Bundle savedInstanceState){
        //init fragment manager
        builder = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.content_container);
        builder.rootFragment(HealthDataList.newInstance("A","b")).build();
        //Init Notification
        Notification.init(getApplicationContext());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
