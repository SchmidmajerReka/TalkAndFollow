package hu.rka.talkfollow;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
        aboutMe.setOnClickListener(textClick);
    }

    private View.OnClickListener textClick=new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Edit text", Toast.LENGTH_LONG).show();
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_empty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
        }

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
