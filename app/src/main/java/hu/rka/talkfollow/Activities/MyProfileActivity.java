package hu.rka.talkfollow.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import hu.rka.talkfollow.R;
import hu.rka.talkfollow.models.UploadProfile;
import hu.rka.talkfollow.network.ContentSpiceService;
import hu.rka.talkfollow.requests.GetMyProfileRequest;
import hu.rka.talkfollow.requests.PostProfileRequest;
import hu.rka.talkfollow.results.EditProfileResult;
import hu.rka.talkfollow.results.MyProfileResult;

/**
 * Created by Réka on 2016.01.09..
 */
public class MyProfileActivity extends AppCompatActivity {

    @Bind(R.id.profile_pic)
    ImageView profilePic;
    @Bind(R.id.book_number)
    TextView bookNumView;
    @Nullable
    @Bind(R.id.book_finished)
    TextView bookFinished;
    @Bind(R.id.about_me)
    TextView aboutMe;

    Context context;
    private SpiceManager spiceManager = new SpiceManager(ContentSpiceService.class);
//    Dialog aboutMeDialog;
    View dialogView;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.my_profile);

        GetMyProfileRequest getDataRequest = new GetMyProfileRequest();
        spiceManager.execute(getDataRequest, new DataRequestListener());
    }

    public final class DataRequestListener implements
            RequestListener<MyProfileResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(MyProfileResult result) {

            if (result != null && result.getMsg().equals("")) {
                if (result.getPicture() != null) {
                    Picasso.with(context).load(result.getPicture()).placeholder(R.drawable.profilepic).into(profilePic);
                }
                bookNumView.setText(getString(R.string.my_profile_number_of_books) + result.getBooks_number());
                bookFinished.setText(getString(R.string.my_profile_finished) + result.getFinished());
                aboutMe.setText(result.getAbout_me());
            } else {
                if (result != null) {
                    Toast.makeText(context, getString(R.string.error) + result.getMsg(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public final class ProfileRequestListener implements
            RequestListener<EditProfileResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(EditProfileResult result) {

            final EditText editText = (EditText) dialogView.findViewById(R.id.edit_about_me_text);
            aboutMe.setText(editText.getText());
            if (result.getMsg() == "") {
                Toast.makeText(context, R.string.uploaded, Toast.LENGTH_LONG).show();
                aboutMe.setText(editText.getText());
            } else {
                Toast.makeText(context, getString(R.string.error) + result.getMsg(), Toast.LENGTH_LONG).show();
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

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.edit_aboutme:

                AlertDialog.Builder aboutMeDialog = new AlertDialog.Builder(context);
// ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                dialogView = inflater.inflate(R.layout.dialog_about_me, null);
                aboutMeDialog.setView(dialogView);
                aboutMeDialog.setTitle(R.string.about_me);

                alertDialog = aboutMeDialog.create();

                Button setAboutMe = (Button) dialogView.findViewById(R.id.edit_about_me_confirm);
                final EditText editText = (EditText) dialogView.findViewById(R.id.edit_about_me_text);
                editText.setText(aboutMe.getText());
                setAboutMe.setOnClickListener(editClick);
                alertDialog.show();

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = alertDialog.getWindow();
                lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private View.OnClickListener editClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            final EditText editText = (EditText) dialogView.findViewById(R.id.edit_about_me_text);
            UploadProfile uploadProfile = new UploadProfile();
            uploadProfile.setAbout_me(String.valueOf(editText.getText()));
            PostProfileRequest postProfileRequest = new PostProfileRequest(uploadProfile);
            spiceManager.execute(postProfileRequest, new ProfileRequestListener());
            alertDialog.dismiss();
        }
    };
}
