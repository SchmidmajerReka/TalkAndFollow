package hu.rka.talkfollow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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



        updateWithToken(AccessToken.getCurrentAccessToken());


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();

                /*GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                try {
                                    String name = object.getString("name");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.v("LoginActivity", response.toString());
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,about_me");
                request.setParameters(parameters);
                request.executeAsync();*/


                Intent detailIntent = new Intent(context, MyLibraryActivity.class);

                profile = Profile.getCurrentProfile();
                UploadUser uploadUser = new UploadUser(AccessToken.getCurrentAccessToken().getUserId(),Profile.getCurrentProfile().getName());
                PostUserRequest postUserRequest = new PostUserRequest(uploadUser);
                spiceManager.execute(postUserRequest, new LoginRequestListener() );


            }

            @Override
            public void onCancel() {
                Toast.makeText(context, "Login Canceld", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(context, "Login failed: " + String.valueOf(exception), Toast.LENGTH_LONG).show();
            }
        });

    }


    private void updateWithToken(AccessToken currentAccessToken) {

        if (currentAccessToken != null) {
            if(Profile.getCurrentProfile() != null) {
                profile = Profile.getCurrentProfile();
                UploadUser uploadUser = new UploadUser(AccessToken.getCurrentAccessToken().getUserId(), Profile.getCurrentProfile().getName());
                PostUserRequest postUserRequest = new PostUserRequest(uploadUser);
                spiceManager.execute(postUserRequest, new LoginRequestListener());
            }

        }
    }


    public final class LoginRequestListener implements
            RequestListener<UserResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, "Hiba történt!!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(UserResult result) {

            if(result.getMsg().equals("")){
                Intent intent = new Intent(context, MyLibraryActivity.class);
                //intent.putExtra("Token", result.getToken());

                startActivity(intent);
            }else{
               Toast.makeText(context, "Error: " + result.getMsg(), Toast.LENGTH_LONG).show();
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
