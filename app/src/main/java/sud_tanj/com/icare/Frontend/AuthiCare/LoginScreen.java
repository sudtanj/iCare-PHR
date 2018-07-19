package sud_tanj.com.icare.Frontend.AuthiCare;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import org.altmail.displaytextview.DisplayTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import spencerstudios.com.bungeelib.Bungee;
import sud_tanj.com.icare.Frontend.Animation.LoadingScreen;
import sud_tanj.com.icare.Frontend.Notification.Notification;
import sud_tanj.com.icare.MainActivityStarter;
import sud_tanj.com.icare.R;

public class LoginScreen extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    @BindView(R.id.logo_description)
    DisplayTextView displayTextView;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    @OnClick(R.id.sign_in_button)
    public void submit(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        }
        /**
         else if (i == R.id.sign_out_button) {
         signOut();
         } else if (i == R.id.disconnect_button) {
         revokeAccess();
         }
         */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        initBackgroundService();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

        initUiComponent();
    }

    private void initUiComponent() {
        displayTextView.setText(R.string.logo_description);
        displayTextView.startAnimation();
    }

    private void initBackgroundService() {
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
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                LoadingScreen.showLoadingScreen(getString(R.string.Loading_google_account));
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Notification.notifyFailure(getString(R.string.failed_login_message_api) + e.getStatusCode());
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        LoadingScreen.hideLoadingScreen();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Notification.notifySuccessful(getString(R.string.login_successful_google));
                            MainActivityStarter.start(LoginScreen.this, mAuth.getCurrentUser());
                            Bungee.swipeRight(LoginScreen.this);
                        } else {
                            // If sign in fails, display a message to the user.
                            Notification.notifyFailure(getString(R.string.login_failed_google));
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }
}
