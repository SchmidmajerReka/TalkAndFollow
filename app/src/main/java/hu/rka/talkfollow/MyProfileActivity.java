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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Réka on 2016.01.09..
 */
public class MyProfileActivity extends AppCompatActivity {
    Context context;
    int bookNum;
    int finished;
    String description;


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
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            bookNum = bundle.getInt("booknum");
            if(bookNum!=0){
                bookNumView.setText("Number of books: " + bookNum);
            }
            finished = bundle.getInt("finished");
            bookFinished.setText("Finished: " + finished);

        }
        aboutMe.setText("Short introduction about me...");
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

                final Dialog aboutMeDialog = new Dialog(context);
                aboutMeDialog.setContentView(R.layout.dialog_about_me);
                aboutMeDialog.setTitle("About Me: ");
                Button setAboutMe = (Button) aboutMeDialog.findViewById(R.id.edit_about_me_confirm);
                final EditText editText = (EditText) aboutMeDialog.findViewById(R.id.edit_about_me_text);
                editText.setText(aboutMe.getText());

                setAboutMe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final EditText editText = (EditText) aboutMeDialog.findViewById(R.id.edit_about_me_text);
                        aboutMe.setText(editText.getText());
                        Toast.makeText(context, "AboutMe changed", Toast.LENGTH_LONG).show();
                        aboutMeDialog.dismiss();
                    }
                });
                aboutMeDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
