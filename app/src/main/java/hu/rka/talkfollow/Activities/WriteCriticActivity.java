package hu.rka.talkfollow.Activities;

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
import hu.rka.talkfollow.R;
import hu.rka.talkfollow.models.Critic;
import hu.rka.talkfollow.models.UploadEditCritic;
import hu.rka.talkfollow.models.UploadNewCritic;
import hu.rka.talkfollow.network.ContentSpiceService;
import hu.rka.talkfollow.requests.PostEditCriticRequest;
import hu.rka.talkfollow.requests.PostNewCriticRequest;
import hu.rka.talkfollow.results.EditCriticResult;
import hu.rka.talkfollow.results.NewCriticResult;

/**
 * Created by Réka on 2016.01.10..
 */
public class WriteCriticActivity extends AppCompatActivity {

    @Bind(R.id.write_critic_title)
    EditText criticTitle;
    @Bind(R.id.write_critic_text)
    EditText criticText;
    @Bind(R.id.write_critic_rate)
    RatingBar criticRate;

    Context context;
    Critic critic = new Critic();
    int bookId;
    Bundle bundle;
    private SpiceManager spiceManager = new SpiceManager(ContentSpiceService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_write_critic);
        ButterKnife.bind(this);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.write_critic);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            bookId = bundle.getInt("bookId");
            getSupportActionBar().setSubtitle(bundle.getString("booktitle"));
            String author = bundle.getString("author");
            critic.setUser_name(author);
            if (bundle.getString("title") != null) {
                critic.setId(bundle.getInt("Id", bundle.getInt("Id")));
                critic.setTitle(bundle.getString("title"));
                critic.setCritic(bundle.getString("critictext"));
                critic.setRating(bundle.getFloat("rate"));
                critic.setCreated_at(bundle.getLong("createdtime"));
                critic.setUpdated_at(bundle.getLong("updatedtime"));
                critic.setMine(bundle.getBoolean("mine"));
                critic.setUser_picture(bundle.getString("profile_pic"));

                criticTitle.setText(critic.getTitle());
                criticText.setText(critic.getCritic());
                criticRate.setRating(critic.getRating());
            }
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
        switch (id) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                this.finish();
                return true;
            case R.id.critic_send:
                if (bundle.getInt("WhatToDo") == 0) {
                    //új kritika
                    if (String.valueOf(criticText.getText()).isEmpty()) {
                        Toast.makeText(context, R.string.can_not_post_empty_critic, Toast.LENGTH_LONG).show();
                    } else {

                        UploadNewCritic uploadNewCritic = new UploadNewCritic(bookId, String.valueOf(criticTitle.getText()), String.valueOf(criticText.getText()), criticRate.getRating());
                        PostNewCriticRequest postNewCriticRequest = new PostNewCriticRequest(uploadNewCritic);
                        spiceManager.execute(postNewCriticRequest, new NewCriticListener());
                    }

                } else if (bundle.getInt("WhatToDo") == 1) {
                    //kritika szerkesztése
                    if (String.valueOf(criticText.getText()).isEmpty()) {
                        Toast.makeText(context, R.string.can_not_post_empty_critic, Toast.LENGTH_LONG).show();
                    } else {

                        UploadEditCritic uploadEditCritic = new UploadEditCritic(critic.getId(), String.valueOf(criticTitle.getText()), String.valueOf(criticText.getText()), criticRate.getRating());
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
            Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(NewCriticResult result) {
            //itt is figyelni kell, hogy az msg üres-e
            if (result.getMsg() != null && result.getMsg().equals("")) {
                Intent result2 = new Intent();
                result2.putExtra("Mine", true);
                result2.putExtra("Id", result.getCritic().getId());
                result2.putExtra("Rating", result.getCritic().getRating());
                result2.putExtra("Title", result.getCritic().getTitle());
                result2.putExtra("Critic", result.getCritic().getCritic());
                result2.putExtra("Updated_at", result.getCritic().getUpdated_at());
                result2.putExtra("Created_at", result.getCritic().getCreated_at());
                result2.putExtra("User", result.getCritic().getUser_name());
                result2.putExtra("Profile_pic", result.getCritic().getUser_picture());
                setResult(2, result2);
//                Toast.makeText(context, "Message sent", Toast.LENGTH_LONG).show();
                finish();
            }
            if (result.getMsg() != null && !result.getMsg().equals("")) {
                Toast.makeText(context, getString(R.string.error) + result.getMsg(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public final class EditCriticListener implements
            RequestListener<EditCriticResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(EditCriticResult result) {

            if (result.getMsg() != null && result.getMsg().equals("")) {
                Intent result2 = new Intent();
                result2.putExtra("Mine", true);
                result2.putExtra("Id", result.getCritic().getId());
                result2.putExtra("Rating", result.getCritic().getRating());
                result2.putExtra("Title", result.getCritic().getTitle());
                result2.putExtra("Critic", result.getCritic().getCritic());
                result2.putExtra("Updated_at", result.getCritic().getUpdated_at());
                result2.putExtra("Created_at", result.getCritic().getCreated_at());
                result2.putExtra("User", result.getCritic().getUser_name());
                result2.putExtra("Profile_pic", result.getCritic().getUser_picture());
                result2.putExtra("Edited", true);
                setResult(3, result2);
                finish();
            } else {
                if (result.getMsg() != null) {
                    Toast.makeText(context, getString(R.string.error) + result.getMsg(), Toast.LENGTH_LONG).show();
                }
            }
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

