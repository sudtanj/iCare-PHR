package sud_tanj.com.icare.Backend.Login;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.marcinmoskala.activitystarterparcelerargconverter.IsParcel;

import org.parceler.Parcel;
import org.parceler.Parcel.Serialization;
import org.parceler.ParcelConstructor;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 27/07/2018 - 16:20.
 * <p>
 * This class last modified by BasicUser
 */
@Parcel(Serialization.BEAN)
public class BasicUser implements IsParcel {
    protected FirebaseAuth mAuth;
    protected FirebaseUser mUser;
    protected GoogleSignInAccount account;

    @ParcelConstructor
    protected BasicUser(){
        mAuth = FirebaseAuth.getInstance();
    }

    public String getDisplayName(){
        if(mUser!=null)
            return mUser.getDisplayName();
        return new String();
    }

    public void signIn(Context context, Task<GoogleSignInAccount> task) throws ApiException{
        GoogleSignInAccount account = task.getResult(ApiException.class);
    }

    public void signOut(GoogleSignInClient mGoogleSignInClient){
        mAuth.signOut();
    }
}
