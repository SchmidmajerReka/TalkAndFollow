package hu.rka.talkfollow;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import hu.rka.talkfollow.models.UploadProfile;
import hu.rka.talkfollow.network.ContentSpiceService;
import hu.rka.talkfollow.requests.GetMyLibraryRequest;
import hu.rka.talkfollow.requests.GetMyProfileRequest;
import hu.rka.talkfollow.requests.PostProfileRequest;
import hu.rka.talkfollow.results.EditProfileResult;
import hu.rka.talkfollow.results.MyLibraryResult;
import hu.rka.talkfollow.results.MyProfileResult;

/**
 * Created by Réka on 2016.01.09..
 */
public class MyProfileActivity extends AppCompatActivity {
    Context context;
    int bookNum;
    int finished;
    String description;
    private SpiceManager spiceManager = new SpiceManager(ContentSpiceService.class);
    Dialog aboutMeDialog;
    AccessToken accessToken;


    @Bind(R.id.profile_pic)
    ImageView profilePic;
    @Bind(R.id.book_number)
    TextView bookNumView;
    @Bind(R.id.book_finished) TextView bookFinished;
    @Bind(R.id.about_me) TextView aboutMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Profile");
        /*Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            bookNum = bundle.getInt("booknum");
            if(bookNum!=0){
                bookNumView.setText("Number of books: " + bookNum);
            }
            finished = bundle.getInt("finished");
            bookFinished.setText("Finished: " + finished);

        }
        aboutMe.setText("Short introduction about me...");
        */
        accessToken = AccessToken.getCurrentAccessToken();

        GetMyProfileRequest getDataRequest = new GetMyProfileRequest();
        spiceManager.execute(getDataRequest, new DataRequestListener());


    }



    public final class DataRequestListener implements
            RequestListener<MyProfileResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, "Hiba történt!!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(MyProfileResult result) {

            Toast.makeText(context, "Hello Adat!", Toast.LENGTH_LONG).show();
            Picasso.with(context).load(result.getPicture()).placeholder(R.drawable.profilepic).into(profilePic);
            bookNumView.setText("Number of books: " + result.getBooks_number());
            bookFinished.setText("Finished: " + result.getFinished());
            aboutMe.setText(result.getAbout_me());
            Picasso.with(context).load("https://graph.facebook.com/" + accessToken.getUserId()+ "/picture?type=large").into(profilePic);

        }
    }

    public final class ProfileRequestListener implements
            RequestListener<EditProfileResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, "Hiba történt!!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(EditProfileResult result) {

            final EditText editText = (EditText) aboutMeDialog.findViewById(R.id.edit_about_me_text);
            aboutMe.setText(editText.getText());
            if(result.getMsg() == ""){
                Toast.makeText(context, "Uploaded", Toast.LENGTH_LONG).show();
                aboutMe.setText(editText.getText());
            }else{
                Toast.makeText(context, "Error: " + result.getMsg(), Toast.LENGTH_LONG).show();
            }
        }
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case  android.R.id.home:
                this.finish();
                return true;
            case R.id.edit_aboutme:
                aboutMeDialog = new Dialog(context);
                aboutMeDialog.setContentView(R.layout.dialog_about_me);
                aboutMeDialog.setTitle("About Me: ");
                Button setAboutMe = (Button) aboutMeDialog.findViewById(R.id.edit_about_me_confirm);
                final EditText editText = (EditText) aboutMeDialog.findViewById(R.id.edit_about_me_text);
                editText.setText(aboutMe.getText());
                setAboutMe.setOnClickListener(editClick);
                aboutMeDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private View.OnClickListener editClick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            final EditText editText = (EditText) aboutMeDialog.findViewById(R.id.edit_about_me_text);
            UploadProfile uploadProfile = new UploadProfile();
            uploadProfile.setAbout_me(String.valueOf(editText.getText()));
            PostProfileRequest postProfileRequest = new PostProfileRequest(uploadProfile);
            spiceManager.execute(postProfileRequest, new ProfileRequestListener());
            aboutMeDialog.dismiss();
        }
    };
}
