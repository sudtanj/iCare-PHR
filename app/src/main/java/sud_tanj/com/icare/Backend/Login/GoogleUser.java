package sud_tanj.com.icare.Backend.Login;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;

import org.parceler.Parcel;
import org.parceler.Parcel.Serialization;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 27/07/2018 - 16:25.
 * <p>
 * This class last modified by User
 */
@Parcel(Serialization.BEAN)
public class GoogleUser extends BasicUser {
    protected OnCompleteListener<AuthResult> onCompleteListener;

    public void setOnCompleteListener(OnCompleteListener<AuthResult> onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    public GoogleUser() {
        super();
    }

    @Override
    public void signIn(Context context,Task<GoogleSignInAccount> task) throws ApiException {
        super.signIn(context,task);
        Boolean googleSignInStatus=Boolean.FALSE;
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity)context, onCompleteListener);
    }

    public void signOut(GoogleSignInClient mGoogleSignInClient) {
        super.signOut(mGoogleSignInClient);
        // Google sign out
        mGoogleSignInClient.signOut();
        mGoogleSignInClient.revokeAccess();
    }
}

