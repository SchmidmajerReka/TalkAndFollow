package hu.rka.talkfollow.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import hu.rka.talkfollow.Activities.TabMenuActivity;
import hu.rka.talkfollow.Activities.WriteCriticActivity;
import hu.rka.talkfollow.Activities.CriticDetailsActivity;
import hu.rka.talkfollow.R;
import hu.rka.talkfollow.adapters.CriticAdapter;
import hu.rka.talkfollow.models.Book;
import hu.rka.talkfollow.models.Critic;

/**
 * Created by Réka on 2016.01.09..
 */
public class CriticListFrag extends android.support.v4.app.Fragment {


    @Bind(R.id.empty_view)
    TextView emptyView;

    @Bind(R.id.critic_list)
    ListView listView;

    CriticAdapter criticAdapter;
    boolean bookAdded;
    Context context;
    Bundle bundle;
    ArrayList<Critic> critics;
    Book bookDetail;
    int bookId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_critic, container, false);
        ButterKnife.bind(this, v);
        context = getActivity();
        TabMenuActivity activity = (TabMenuActivity) getActivity();
        bundle = activity.getBundle();

        bookDetail = activity.getBookDetails();
        critics = activity.getCritics();

        criticAdapter = new CriticAdapter(getActivity(), 0);
        setUI(bookDetail, critics);

        return v;
    }

    public void refreshUI(Book bookDetail, ArrayList<Critic> critics) {
        setUI(bookDetail, critics);
    }

    private void setUI(final Book bookDetail, final ArrayList<Critic> critics) {
        if(bookDetail != null) {
            if(listView != null) {
                bookAdded = bookDetail.isMine();
                bookId = bookDetail.getId();
                listView.setEmptyView(emptyView);
                criticAdapter.setCritic(critics);
                listView.setAdapter(criticAdapter);
                listView.setSelection(criticAdapter.getCount()-1);
                listView.setOnItemClickListener(listItemClick);
            }
        }
    }


    AdapterView.OnItemClickListener listItemClick = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Critic item = criticAdapter.getCritic(position);
            Intent detailIntent = new Intent(context, CriticDetailsActivity.class);
            detailIntent.putExtra("Id", item.getId());
            detailIntent.putExtra("bookId", bookId);
            detailIntent.putExtra("title", item.getTitle());
            detailIntent.putExtra("author", item.getUser_name());
            detailIntent.putExtra("critictext", item.getCritic());
            detailIntent.putExtra("rate", item.getRating());
            detailIntent.putExtra("createdtime", item.getCreated_at());
            detailIntent.putExtra("updatedtime", item.getUpdated_at());
            detailIntent.putExtra("booktitle", bundle.getString("title"));
            detailIntent.putExtra("mine", item.isMine());
            detailIntent.putExtra("profile_pic", item.getUser_picture());
            startActivityForResult(detailIntent, 1);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        menu.clear();
        if(bookAdded) {
            inflater.inflate(R.menu.menu_critics, menu);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //új kritika
        if(resultCode == 2) {
            Bundle bundletmp = data.getExtras();
            Critic critic = new Critic();
            critic.setMine(bundletmp.getBoolean("Mine"));
            critic.setId(bundletmp.getInt("Id"));
            critic.setRating(bundletmp.getFloat("Rating"));
            critic.setTitle(bundletmp.getString("Title"));
            critic.setCritic(bundletmp.getString("Critic"));
            critic.setCreated_at(bundletmp.getLong("Created_at"));
            critic.setUpdated_at(bundletmp.getLong("Updated_at"));
            critic.setUser_name(bundletmp.getString("User"));
            critic.setUser_picture(bundletmp.getString("Profile_pic"));
            critics.add(critic);
            criticAdapter.notifyDataSetChanged();
        }else if(resultCode == 3) {
            //kritika szerkesztése
            Bundle bundletmp = data.getExtras();
            boolean found = false;
            for (int i = 0; i < critics.size(); i++) {
                if (critics.get(i).getId() == bundletmp.getInt("Id")) {
                    found = true;
                    critics.get(i).setMine(bundletmp.getBoolean("Mine"));
                    critics.get(i).setId(bundletmp.getInt("Id"));
                    critics.get(i).setRating(bundletmp.getFloat("Rating"));
                    critics.get(i).setTitle(bundletmp.getString("Title"));
                    critics.get(i).setCritic(bundletmp.getString("Critic"));
                    critics.get(i).setCreated_at(bundletmp.getLong("Created_at"));
                    critics.get(i).setUpdated_at(bundletmp.getLong("Updated_at"));
                    critics.get(i).setUser_name(bundletmp.getString("User"));
                    critics.get(i).setUser_picture(bundletmp.getString("Profile_pic"));
                    criticAdapter.notifyDataSetChanged();
                }
            }
            if (found) {
                
            } else {
                Toast.makeText(context, "Critic not found!!", Toast.LENGTH_LONG).show();
            }
        //kritika törlése
        }else if(resultCode == 4){
            Bundle bundletmp = data.getExtras();
            boolean del = false;
            for(int i = 0; i < critics.size(); i++ ){
                if(critics.get(i).getId() == bundletmp.getInt("Id")){
                    critics.remove(i);
                    criticAdapter.notifyDataSetChanged();
                    del = true;
                }
            }
            if(!del){
                Toast.makeText(context, "Critic not found!!", Toast.LENGTH_LONG).show();
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.write_critic:
                Intent writeIntent = new Intent(context,WriteCriticActivity.class);
                writeIntent.putExtra("booktitle", bundle.getString("title"));
                writeIntent.putExtra("bookId", bookDetail.getId());
                writeIntent.putExtra("WhatToDO", 0);
                Profile profile = Profile.getCurrentProfile();
                writeIntent.putExtra("author", profile.getName());
                startActivityForResult(writeIntent, 2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
