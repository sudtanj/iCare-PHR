package sud_tanj.com.icare.Frontend.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.ncapdevi.fragnav.FragNavController;

import sud_tanj.com.icare.R;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 19/07/2018 - 7:06.
 * <p>
 * This class last modified by BasicUser
 */
public class FragmentBuilder {
    private static FragNavController.Builder builder;
    public static void init(Bundle savedInstanceState,FragmentManager fragmentManager){
        builder = FragNavController.newBuilder(savedInstanceState,fragmentManager, R.id.content_container);
    }
    public static void changeFragment(Fragment fragment){
        builder.rootFragment(fragment).build();
    }
}
