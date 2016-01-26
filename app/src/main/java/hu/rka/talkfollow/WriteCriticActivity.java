package hu.rka.talkfollow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import hu.rka.talkfollow.models.UploadEditCritic;
import hu.rka.talkfollow.models.UploadNewCritic;
import hu.rka.talkfollow.network.ContentSpiceService;
import hu.rka.talkfollow.requests.PostEditCriticRequest;
import hu.rka.talkfollow.requests.PostNewCriticRequest;
import hu.rka.talkfollow.results.EditCriticResult;
import hu.rka.talkfollow.results.NewCriticResult;

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
    Bundle bundle;
    private SpiceManager spiceManager = new SpiceManager(ContentSpiceService.class);

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

        bundle = getIntent().getExtras();
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
                setResult(RESULT_CANCELED);
                this.finish();
                return true;
            case R.id.critic_send:
                if(bundle.getInt("WhatToDo") == 0) {
                    if(String.valueOf(criticText.getText()).isEmpty()){
                        Toast.makeText(context, "Can not post empty critic", Toast.LENGTH_LONG).show();
                    }else {
                        UploadNewCritic uploadNewCritic = new UploadNewCritic(bundle.getInt("bookId"), String.valueOf(criticTitle.getText()), String.valueOf(criticText.getText()), criticRate.getRating());
                        PostNewCriticRequest postNewCriticRequest = new PostNewCriticRequest(uploadNewCritic);
                        spiceManager.execute(postNewCriticRequest, new NewCriticListener());
                    }

                }else if(bundle.getInt("WhatToDo") == 1){
                    if(String.valueOf(criticText.getText()).isEmpty()){
                        Toast.makeText(context, "Can not post empty critic", Toast.LENGTH_LONG).show();
                    }else{
                        UploadEditCritic uploadEditCritic = new UploadEditCritic(bundle.getInt("Id"), String.valueOf(criticTitle.getText()), String.valueOf(criticText.getText()), criticRate.getRating());
                        PostEditCriticRequest postEdigCriticRequest = new PostEditCriticRequest(uploadEditCritic);
                        spiceManager.execute(postEdigCriticRequest, new EditCriticListener());
                    }

                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public final class NewCriticListener implements
            RequestListener<NewCriticResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, "Hiba történt!!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(NewCriticResult result) {
            //itt is figyelni kell, hogy az msg üres-e

            Intent result2 = new Intent();
            result2.putExtra("Mine", true);
            result2.putExtra("Id", result.getCritic_id());
            result2.putExtra("Rating", criticRate.getRating());
            result2.putExtra("Title", String.valueOf(criticTitle.getText()));
            result2.putExtra("Critic", String.valueOf(criticText.getText()));
            result2.putExtra("Time", "2 órája");
            result2.putExtra("User", bundle.getString("User"));
            setResult(2, result2);
            Toast.makeText(context, "Message sent", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public final class EditCriticListener implements
            RequestListener<EditCriticResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, "Hiba történt!!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(EditCriticResult result) {
            //itt is figyelni kell, hogy az msg üres-e
            Intent result2 = new Intent();
            result2.putExtra("Edited", true);
            result2.putExtra("Mine", true);
            result2.putExtra("Id", bundle.getInt("Id"));

            result2.putExtra("Rating", criticRate.getRating());
            result2.putExtra("Title", String.valueOf(criticTitle.getText()));
            result2.putExtra("Critic", String.valueOf(criticText.getText()));
            result2.putExtra("Time", "1 minute ago");
            setResult(3, result2);
            finish();
        }
    }
    @Override
    public void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        spiceManager.start(context);

    }

}

