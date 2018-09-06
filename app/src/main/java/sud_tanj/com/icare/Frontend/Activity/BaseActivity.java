package sud_tanj.com.icare.Frontend.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.evernote.android.state.StateSaver;
import com.heinrichreimersoftware.materialdrawer.DrawerActivity;

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
        StateSaver.restoreInstanceState(this, savedInstanceState);
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override // This is optional, only when we want to keep arguments changes in case of rotation etc.
    protected void onSaveInstanceState(Bundle outState) {
        StateSaver.saveInstanceState(this, outState);
        super.onSaveInstanceState(outState);
    }

}
