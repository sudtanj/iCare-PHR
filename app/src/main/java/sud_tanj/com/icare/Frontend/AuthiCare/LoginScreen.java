package sud_tanj.com.icare.Frontend.AuthiCare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import org.altmail.displaytextview.DisplayTextView;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

import butterknife.ButterKnife;
import butterknife.OnClick;
import spencerstudios.com.bungeelib.Bungee;
import sud_tanj.com.icare.Backend.Login.BasicUser;
import sud_tanj.com.icare.Backend.Login.GoogleUser;
import sud_tanj.com.icare.Frontend.Animation.LoadingScreen;
import sud_tanj.com.icare.Frontend.Notification.Notification;
import sud_tanj.com.icare.R;

@Fullscreen
@EActivity(R.layout.activity_login_screen)
public class LoginScreen extends AppCompatActivity implements OnCompleteListener<AuthResult> {
    private static final int RC_SIGN_IN = 9001;
    @ViewById(R.id.logo_description) DisplayTextView displayTextView;
    private GoogleSignInClient mGoogleSignInClient;
    private BasicUser basicUser=null;

    @OnClick(R.id.sign_in_button)
    public void submit(View v) {
        signIn();
    }

    @AfterViews
    protected void initFirebaseAuth(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
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
        //Init ButterKnife
        ButterKnife.bind(this);
        //Init Loading Screen
        LoadingScreen.init(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            LoadingScreen.showLoadingScreen(getString(R.string.Loading_google_account));
            basicUser=new GoogleUser();
            try {
                ((GoogleUser)(basicUser)).signIn(getApplicationContext(),task);
            } catch (ApiException e) {
                Notification.notifyFailure(getString(R.string.failed_login_message_api) + e.getStatusCode());
                LoadingScreen.hideLoadingScreen();
            }
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //if google auth return result
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        LoadingScreen.hideLoadingScreen();
        if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Notification.notifySuccessful(getString(R.string.login_successful_google));
            Bungee.swipeRight(LoginScreen.this);
        } else {
            // If sign in fails, display a message to the user.
            Notification.notifyFailure(getString(R.string.login_failed_google));
        }
    }
}
