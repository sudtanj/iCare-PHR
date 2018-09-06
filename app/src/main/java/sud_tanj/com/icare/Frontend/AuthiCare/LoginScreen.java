package sud_tanj.com.icare.Frontend.AuthiCare;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import org.altmail.displaytextview.DisplayTextView;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import io.paperdb.Paper;
import spencerstudios.com.bungeelib.Bungee;
import sud_tanj.com.icare.Backend.Database.HybridReference;
import sud_tanj.com.icare.Backend.Database.UserInformation;
import sud_tanj.com.icare.Frontend.Animation.LoadingScreen;
import sud_tanj.com.icare.Frontend.Notification.Notification;
import sud_tanj.com.icare.MainActivity_;
import sud_tanj.com.icare.R;

@Fullscreen
@EActivity(R.layout.activity_login_screen)
@WindowFeature(Window.FEATURE_NO_TITLE)
public class LoginScreen extends Activity implements OnCompleteListener<AuthResult> {
    private static final int RC_SIGN_IN = 9001;
    @ViewById(R.id.logo_description)
    DisplayTextView displayTextView;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private GoogleSignInOptions gso;

    @Click(R.id.sign_in_button)
    public void submit(View v) {
        signIn();
    }

    @AfterViews
    protected void initFirebaseAuth() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("713034949688-85becbtnb3inr12mo6uo1k5jcdsccc3a.apps.googleusercontent.com")
                .requestEmail()
                .requestProfile()
                .requestId()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
    }

    @AfterViews
    protected void initUiComponent() {
        displayTextView.setText(R.string.logo_description);
        displayTextView.startAnimation();
    }

    @AfterViews
    protected void initBackgroundService() {
        //Init Notification
        Notification.init(this);
        //Init Loading Screen
        LoadingScreen.init(this);
        //Init Paper
        Paper.init(this);
        //Init hybridreferences
        HybridReference.init(this);
    }

    @OnActivityResult(RC_SIGN_IN)
    protected void onResult(Integer resultCode, Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            LoadingScreen.showLoadingScreen(getString(R.string.Loading_google_account));
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            Notification.notifyFailure(getString(R.string.failed_login_message_api) + e.getStatusCode());
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, this);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //if the user is already signed in
        //we will close this activity
        //and take the user to profile activity
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if (currentUser != null) {
            finish();
            currentUser.reload();
            syncUserInformation(currentUser);
            Notification.notifySuccessful(getString(R.string.welcome_back_notification)+currentUser.getDisplayName());
            startMainActivity();
        }
    }

    public void syncUserInformation(FirebaseUser currentUser){
        UserInformation userInformation = new UserInformation();
        userInformation.setName(currentUser.getDisplayName());
        userInformation.setEmail(currentUser.getEmail());
        HybridReference hybridReference=new HybridReference(FirebaseDatabase
                .getInstance().getReferenceFromUrl(UserInformation.KEY)
                .child(currentUser.getUid()));
        hybridReference.setValue(userInformation);
    }

    //if google auth return result
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        LoadingScreen.hideLoadingScreen();
        if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            FirebaseUser currentUser=mAuth.getCurrentUser();
            syncUserInformation(currentUser);
            finish();
            Notification.notifySuccessful(getString(R.string.login_successful_google));
            startMainActivity();
            //Message the user
            Notification.notifyUser("Hello "+currentUser.getDisplayName());
        } else {
            // If sign in fails, display a message to the user.
            Notification.notifyFailure(getString(R.string.login_failed_google));
        }
    }
    private void startMainActivity(){
        MainActivity_.intent(LoginScreen.this).gso(gso).start();
        Bungee.swipeRight(LoginScreen.this);
    }
}
