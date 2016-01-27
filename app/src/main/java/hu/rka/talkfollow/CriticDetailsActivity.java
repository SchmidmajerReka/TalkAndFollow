package hu.rka.talkfollow;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import hu.rka.talkfollow.models.Critic;
import hu.rka.talkfollow.models.UploadDeleteCritic;
import hu.rka.talkfollow.network.ContentSpiceService;
import hu.rka.talkfollow.requests.PostDeleteCriticRequest;
import hu.rka.talkfollow.results.DeleteCriticResult;
import hu.rka.talkfollow.results.NewCriticResult;

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
    //@Bind(R.id.critic_detal_delete) ImageView criticDelete;
    //@Bind(R.id.critc_detail_edit) ImageView criticEdit;
    Bundle bundle;
    Boolean edited = false;
    private SpiceManager spiceManager = new SpiceManager(ContentSpiceService.class);
    Dialog confirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_critic_details);
        ButterKnife.bind(this);
        context=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        bundle = getIntent().getExtras();
        if ( bundle != null) {
            getSupportActionBar().setTitle(bundle.getString("author"));
            detailTitle.setText(bundle.getString("title"));
            long created = bundle.getLong("createdtime");
            String dateCreated = new SimpleDateFormat("dd/MM/yyyy").format(new Date(created));
            detailCreatedTime.setText(dateCreated);
            long updated = bundle.getLong("updatedtime");
            String dateUpdated = new SimpleDateFormat("dd/MM/yyyy").format(new Date(updated));
            detailUpdatedTime.setText(dateUpdated);
            detailRate.setRating(bundle.getFloat("rate"));
            detailText.setText(bundle.getString("critictext"));
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == 3) {
            Bundle bundletmp = data.getExtras();
            if ( bundletmp != null) {
                detailTitle.setText(bundletmp.getString("Title"));
                long created = bundle.getLong("createdtime");
                String dateCreated = new SimpleDateFormat("dd/MM/yyyy").format(new Date(created));
                detailCreatedTime.setText(dateCreated);
                long updated = bundle.getLong("updatedtime");
                String dateUpdated = new SimpleDateFormat("dd/MM/yyyy").format(new Date(updated));
                detailUpdatedTime.setText(dateUpdated);
                detailRate.setRating(bundletmp.getFloat("Rating"));
                detailText.setText(bundletmp.getString("Critic"));
                edited = bundletmp.getBoolean("Edited");
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (bundle != null){
            if(bundle.getBoolean("mine")){
                getMenuInflater().inflate(R.menu.menu_critic_details, menu);
                return true;
            }else{
                getMenuInflater().inflate(R.menu.menu_empty, menu);
                return true;
            }
        }else{
        getMenuInflater().inflate(R.menu.menu_empty, menu);
        return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                if(edited){
                    Intent result = new Intent();
                    result.putExtra("Mine", true);
                    result.putExtra("Rating", detailRate.getRating());
                    result.putExtra("Title", detailTitle.getText());
                    result.putExtra("Critic", detailText.getText());
                    result.putExtra("Time", "Just now");
                    result.putExtra("Id", bundle.getInt("Id"));
                    setResult(3, result);
                }else{
                    setResult(1);
                }
                this.finish();
                return true;
            case R.id.critic_delete:
                confirmDialog = new Dialog(context);
                confirmDialog.setContentView(R.layout.dialog_confirm_delete);
                confirmDialog.setTitle("Delete this critic?");
                Button yesButton = (Button) confirmDialog.findViewById(R.id.confirm_yes);
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        UploadDeleteCritic uploadDeleteCritic = new UploadDeleteCritic(bundle.getInt("Id"));
                        PostDeleteCriticRequest postDeleteCriticRequest = new PostDeleteCriticRequest(uploadDeleteCritic);
                        spiceManager.execute(postDeleteCriticRequest, new DeleteCriticListener());


                    }
                });
                Button noButton = (Button) confirmDialog.findViewById(R.id.confirm_no);
                noButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        confirmDialog.dismiss();
                    }
                });
                confirmDialog.show();
                return true;
            case R.id.critic_edit:
                Intent editIntent = new Intent(context, WriteCriticActivity.class);
                editIntent.putExtra("Id", bundle.getInt("Id"));
                editIntent.putExtra("User", bundle.getString("author"));
                editIntent.putExtra("title", detailTitle.getText());
                editIntent.putExtra("text", detailText.getText());
                editIntent.putExtra("rate", detailRate.getRating());
                editIntent.putExtra("booktitle", bundle.getString("booktitle"));
                editIntent.putExtra("WhatToDo", 1);
                startActivityForResult(editIntent, 3);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public final class DeleteCriticListener implements
            RequestListener<DeleteCriticResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, "Hiba történt!!", Toast.LENGTH_LONG).show();
            confirmDialog.dismiss();
        }

        @Override
        public void onRequestSuccess(DeleteCriticResult result) {
            //itt is figyelni kell, hogy az msg üres-e

            Intent result2 = new Intent();
            result2.putExtra("Id", bundle.getInt("Id"));
            setResult(4, result2);
            Toast.makeText(context, "Critic deleted", Toast.LENGTH_LONG).show();
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


