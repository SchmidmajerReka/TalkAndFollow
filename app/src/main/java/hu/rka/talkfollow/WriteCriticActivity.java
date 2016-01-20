package hu.rka.talkfollow;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.app.PendingIntent.getActivity;

/**
 * Created by Réka on 2016.01.10..
 */
public class WriteCriticActivity extends AppCompatActivity{

    Context context;
    @Bind(R.id.write_critic_title)
    EditText criticTitle;
    @Bind(R.id.write_critic_text) EditText criticText;
    @Bind(R.id.write_critic_rate)
    RatingBar criticRate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_critic);
        ButterKnife.bind(this);
        context=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Write critic");

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            getSupportActionBar().setSubtitle(bundle.getString("booktitle"));
            criticTitle.setText(bundle.getString("title"));
            criticText.setText(bundle.getString("text"));
            criticRate.setRating(bundle.getFloat("rate"));
        }

        criticTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard((Activity) context);
                } else {
                    showKeyboard((Activity) context);
                }
            }
        });

    }


    public static void showKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_write_critic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.critic_send:
                Toast.makeText(context, "Message sent", Toast.LENGTH_LONG ).show();
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

