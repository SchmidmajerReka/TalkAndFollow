package hu.rka.talkfollow.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import hu.rka.talkfollow.R;
import hu.rka.talkfollow.models.Critic;
import hu.rka.talkfollow.network.ContentSpiceService;
import hu.rka.talkfollow.requests.PostDeleteCriticRequest;
import hu.rka.talkfollow.results.DeleteCriticResult;

/**
 * Created by Réka on 2016.01.10..
 */
public class CriticDetailsActivity extends AppCompatActivity {

    @Bind(R.id.critic_detail_title)
    TextView detailTitle;
    @Bind(R.id.critc_detail_profile_pic)
    ImageView detailPicture;
    @Bind(R.id.critc_detail_created_at_time)
    TextView detailCreatedTime;
    @Bind(R.id.critc_detail_updtaed_at_time)
    TextView detailUpdatedTime;
    @Bind(R.id.critic_detail_rate)
    RatingBar detailRate;
    @Bind(R.id.critic_detail_text)
    TextView detailText;

    Context context;
    Bundle bundle;
    Critic critic = new Critic();
    Boolean edited = false;
    private SpiceManager spiceManager = new SpiceManager(ContentSpiceService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_critic_details);
        ButterKnife.bind(this);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        bundle = getIntent().getExtras();
        if (bundle != null) {
            critic.setId(bundle.getInt("Id"));
            critic.setUser_name(bundle.getString("author"));
            critic.setTitle(bundle.getString("title"));
            critic.setCreated_at(bundle.getLong("createdtime"));
            critic.setUpdated_at(bundle.getLong("updatedtime"));
            critic.setRating(bundle.getFloat("rate"));
            critic.setCritic(bundle.getString("critictext"));
            critic.setUser_picture(bundle.getString("profile_pic"));
            setUI(critic);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == 3) {
            Bundle bundletmp = data.getExtras();
            if (bundletmp != null) {
                edited = bundletmp.getBoolean("Edited");

                critic.setMine(bundletmp.getBoolean("Mine"));
                critic.setId(bundletmp.getInt("Id"));
                critic.setRating(bundletmp.getFloat("Rating"));
                critic.setTitle(bundletmp.getString("Title"));
                critic.setCritic(bundletmp.getString("Critic"));
                critic.setUpdated_at(bundletmp.getLong("Updated_at"));
                critic.setCreated_at(bundletmp.getLong("Created_at"));
                critic.setUser_name(bundletmp.getString("User"));
                critic.setUser_picture(bundletmp.getString("Profile_pic"));
                setUI(critic);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setUI(Critic critic) {
        getSupportActionBar().setTitle(critic.getUser_name());
        detailTitle.setText(critic.getTitle());
        String dateCreated = new SimpleDateFormat("dd/MM/yyyy").format(new Date((critic.getCreated_at())));
        detailCreatedTime.setText(dateCreated);
        String dateUpdated = new SimpleDateFormat("dd/MM/yyyy").format(new Date((critic.getUpdated_at())));
        detailUpdatedTime.setText(dateUpdated);
        detailRate.setRating(critic.getRating());
        detailText.setText(critic.getCritic());
        Picasso.with(context).load(critic.getUser_picture()).placeholder(R.drawable.profilepic).into(detailPicture);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (bundle != null) {
            if (bundle.getBoolean("mine")) {
                getMenuInflater().inflate(R.menu.menu_critic_details, menu);
                return true;
            } else {
                getMenuInflater().inflate(R.menu.menu_empty, menu);
                return true;
            }
        } else {
            getMenuInflater().inflate(R.menu.menu_empty, menu);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if (edited) {
                    //ha a kritikát módosították
                    Bundle bundle = getIntent().getExtras();
                    Intent result = new Intent();
                    result.putExtra("Mine", true);
                    result.putExtra("Rating", critic.getRating());
                    result.putExtra("Title", critic.getTitle());
                    result.putExtra("Critic", critic.getCritic());
                    result.putExtra("Created_at", critic.getCreated_at());
                    result.putExtra("Updated_at", critic.getUpdated_at());
                    result.putExtra("Id", critic.getId());
                    result.putExtra("User", critic.getUser_name());
                    result.putExtra("Profile_pic", critic.getUser_picture());
                    setResult(3, result);
                } else {
                    //ha nem módosult
                    setResult(1);
                }
                this.finish();
                return true;
            case R.id.critic_delete:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                // set title
                alertDialogBuilder.setTitle(getString(R.string.delete));
                // set dialog message
                alertDialogBuilder
                        .setMessage(R.string.click_yes_to_delete_critic)
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                PostDeleteCriticRequest postDeleteCriticRequest = new PostDeleteCriticRequest(bundle.getInt("Id"));
                                spiceManager.execute(postDeleteCriticRequest, new DeleteCriticListener());
                            }
                        })
                        .setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = alertDialog.getWindow();
                lp.copyFrom(window.getAttributes());
                //This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                return true;
            case R.id.critic_edit:
                Intent editIntent = new Intent(context, WriteCriticActivity.class);
                editIntent.putExtra("Id", critic.getId());
                editIntent.putExtra("bookId", bundle.getInt("bookId"));
                editIntent.putExtra("title", bundle.getString("title"));
                editIntent.putExtra("author", bundle.getString("author"));
                editIntent.putExtra("critictext", bundle.getString("critictext"));
                editIntent.putExtra("rate", bundle.getFloat("rate"));
                editIntent.putExtra("createdtime", bundle.getFloat("createdtime"));
                editIntent.putExtra("updatedtime", bundle.getFloat("updatedtime"));
                editIntent.putExtra("booktitle", bundle.getString("booktitle"));
                editIntent.putExtra("mine", bundle.getBoolean("mine"));
                editIntent.putExtra("profile_pic", bundle.getString("profile_pic"));

                editIntent.putExtra("WhatToDo", 1);
                startActivityForResult(editIntent, 3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public final class DeleteCriticListener implements
            RequestListener<DeleteCriticResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(DeleteCriticResult result) {
            //itt is figyelni kell, hogy az msg üres-e
            if (result.getMsg() != null && result.getMsg().equals("")) {
                Intent result2 = new Intent();
                result2.putExtra("Id", bundle.getInt("Id"));
                setResult(4, result2);
//                Toast.makeText(context, "Critic deleted", Toast.LENGTH_LONG).show();
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


