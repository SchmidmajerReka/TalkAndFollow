package hu.rka.talkfollow.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import hu.rka.talkfollow.PreferencesHelper;
import hu.rka.talkfollow.R;
import io.fabric.sdk.android.Fabric;
import hu.rka.talkfollow.models.UploadUser;
import hu.rka.talkfollow.network.ContentSpiceService;
import hu.rka.talkfollow.requests.PostUserRequest;
import hu.rka.talkfollow.results.UserResult;

/**
 * Created by Réka on 2016.01.08..
 */
public class LoginActivity extends AppCompatActivity {

    private Context context;
    AccessToken accessToken;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    private SpiceManager spiceManager = new SpiceManager(ContentSpiceService.class);
    Profile profile;
    private ProfileTracker mProfileTracker;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        FacebookSdk.sdkInitialize(getApplicationContext());

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }
        };

        callbackManager = CallbackManager.Factory.create();
        context = this;
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile");


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();

                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            mProfileTracker.stopTracking();

                            progress = new ProgressDialog(context);
                            progress.setTitle(getString(R.string.loading_your_library));
                            progress.setMessage(getString(R.string.please_wait));
                            progress.setCancelable(false);
                            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progress.show();

                            UploadUser uploadUser = new UploadUser(AccessToken.getCurrentAccessToken().getUserId(), Profile.getCurrentProfile().getFirstName(), Profile.getCurrentProfile().getLastName());
                            PostUserRequest postUserRequest = new PostUserRequest(uploadUser);
                            spiceManager.execute(postUserRequest, new LoginRequestListener());
                        }
                    };
                    mProfileTracker.startTracking();
                } else {
                    Profile profile = Profile.getCurrentProfile();
                }
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(context, getString(R.string.login_failed) + String.valueOf(exception), Toast.LENGTH_LONG).show();
            }
        });

    }


    private void updateWithToken(AccessToken currentAccessToken) {

        if (currentAccessToken != null) {
            if (Profile.getCurrentProfile() != null) {
                progress = new ProgressDialog(context);
                progress.setTitle(getString(R.string.loading_your_library));
                progress.setMessage(getString(R.string.please_wait));
                progress.setCancelable(false);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.show();
                profile = Profile.getCurrentProfile();

                UploadUser uploadUser = new UploadUser(AccessToken.getCurrentAccessToken().getUserId(), Profile.getCurrentProfile().getFirstName(), Profile.getCurrentProfile().getLastName());
                PostUserRequest postUserRequest = new PostUserRequest(uploadUser);
                spiceManager.execute(postUserRequest, new LoginRequestListener());
            }

        }
    }


    public final class LoginRequestListener implements
            RequestListener<UserResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            progress.dismiss();
            LoginManager.getInstance().logOut();
            Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(UserResult result) {

            if (result.getMsg().equals("")) {
                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            mProfileTracker.stopTracking();
                        }
                    };
                    mProfileTracker.startTracking();
                } else {
                    Profile profile = Profile.getCurrentProfile();
                }

                progress.dismiss();

                Intent intent = new Intent(context, MyLibraryActivity.class);
                PreferencesHelper.setStringByKey(context, "Auth-Token", result.getToken());
                String token = PreferencesHelper.getStringByKey(context, "Auth-Token", "def");
                Log.e("token", token);
                startActivity(intent);

            } else {
                progress.dismiss();
                Toast.makeText(context, getString(R.string.error) + result.getMsg(), Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_empty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(context);

    }
}
