package sud_tanj.com.icare.Frontend.Listener;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;

import sud_tanj.com.icare.Frontend.Activity.BaseActivity;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 30/07/2018 - 16:44.
 * <p>
 * This class last modified by User
 */
public class FirebaseProfilePictureListener extends SimpleTarget<Bitmap> {
    private DrawerProfile drawerProfile;
    private BaseActivity baseActivity;

    public FirebaseProfilePictureListener(DrawerProfile drawerProfile, BaseActivity baseActivity) {
        this.drawerProfile = drawerProfile;
        this.baseActivity = baseActivity;
    }

    @Override
    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
        drawerProfile.setAvatar(baseActivity.getApplicationContext(),resource);
        baseActivity.removeProfile(drawerProfile);
        baseActivity.addProfile(drawerProfile);
    }
}
