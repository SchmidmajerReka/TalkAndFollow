package hu.rka.talkfollow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Réka on 2016.01.10..
 */
public class CriticDetailsActivity extends AppCompatActivity {
    Context context;
    @Bind(R.id.critic_detail_title) TextView detailTitle;
    @Bind(R.id.critc_detail_profile_pic)
    ImageView detailPicture;
    @Bind(R.id.critc_detail_created_at_time) TextView detailCreatedTime;
    @Bind(R.id.critc_detail_updtaed_at_time) TextView detailUpdatedTime;
    @Bind(R.id.critic_detail_rate)
    RatingBar detailRate;
    @Bind(R.id.critic_detail_text) TextView detailText;
    @Bind(R.id.critic_detal_delete) ImageView criticDelete;
    @Bind(R.id.critc_detail_edit) ImageView criticEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_critic_details);
        ButterKnife.bind(this);
        context=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if ( bundle != null) {
            getSupportActionBar().setTitle(bundle.getString("author"));
            detailTitle.setText(bundle.getString("title"));
            detailCreatedTime.setText(bundle.getString("createdtime"));
            detailUpdatedTime.setText(bundle.getString("updatedtime"));
            detailRate.setRating(bundle.getFloat("rate"));
            detailText.setText(bundle.getString("critictext"));
            if(bundle.getBoolean("mine")){
                criticDelete.setVisibility(View.VISIBLE);
                criticDelete.setOnClickListener(deleteClick);
                criticEdit.setVisibility(View.VISIBLE);
                criticEdit.setOnClickListener(editClick);
            }
        }

    }


    private View.OnClickListener deleteClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Toast.makeText(context, "Critic deleted", Toast.LENGTH_LONG).show();
            finish();
        }
    };

    private View.OnClickListener editClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent editIntent = new Intent(context, WriteCriticActivity.class);
            editIntent.putExtra("title", detailTitle.getText());
            editIntent.putExtra("text", detailText.getText());
            editIntent.putExtra("rate", detailRate.getRating());
            context.startActivity(editIntent);
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


