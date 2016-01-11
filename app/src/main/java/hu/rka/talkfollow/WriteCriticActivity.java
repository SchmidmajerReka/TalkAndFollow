package hu.rka.talkfollow;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Réka on 2016.01.10..
 */
public class WriteCriticActivity extends AppCompatActivity{

    Context context;
    @Bind(R.id.write_critic_send)
    Button criticSend;
    @Bind(R.id.write_critic_title)
    EditText criticTitle;
    @Bind(R.id.write_critic_text) EditText criticText;
    @Bind(R.id.write_critic_rate)
    RatingBar criticRate;
    /*@Bind(R.id.critic_detail_title)
    TextView detailTitle;
    @Bind(R.id.critc_detail_profile_pic)
    ImageView detailPicture;
    @Bind(R.id.critc_detail_created_at_time) TextView detailCreatedTime;
    @Bind(R.id.critc_detail_updtaed_at_time) TextView detailUpdatedTime;
    @Bind(R.id.critic_detail_rate)
    RatingBar detailRate;
    @Bind(R.id.critic_detail_text) TextView detailText;
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_critic);
        ButterKnife.bind(this);
        context=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Critic");
        criticSend.setOnClickListener(buttonClick);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            criticTitle.setText(bundle.getString("title"));
            criticText.setText(bundle.getString("text"));
            criticRate.setRating(bundle.getFloat("rate"));
        }

    }

    private View.OnClickListener buttonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Toast.makeText(context, "Message sent", Toast.LENGTH_LONG ).show();
            finish();
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_empty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

