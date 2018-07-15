package sud_tanj.com.icare.Frontend.Activity;

import android.os.Bundle;

import com.heinrichreimersoftware.materialdrawer.DrawerActivity;

import activitystarter.ActivityStarter;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 15/07/2018 - 21:51.
 * <p>
 * This class last modified by User
 */
public class BaseActivity extends DrawerActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStarter.fill(this);
    }

    @Override // This is optional, only when we want to keep arguments changes in case of rotation etc.
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ActivityStarter.save(this, outState);
    }
}
