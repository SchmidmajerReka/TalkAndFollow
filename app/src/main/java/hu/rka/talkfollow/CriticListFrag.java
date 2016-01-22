package hu.rka.talkfollow;

import android.app.Activity;
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
import android.widget.Toast;

import java.util.ArrayList;

import hu.rka.talkfollow.adapters.CriticAdapter;
import hu.rka.talkfollow.models.Critic;

/**
 * Created by Réka on 2016.01.09..
 */
public class CriticListFrag extends android.support.v4.app.Fragment {

    ListView listView;
    CriticAdapter criticAdapter;
    boolean bookAdded;
    Context context;
    Bundle bundle;
    TabMenuActivity activity;
    ArrayList<Critic> critics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_critic, container, false);
        listView = (ListView) v.findViewById(R.id.critic_list);

        context = getActivity();
        TabMenuActivity activity = (TabMenuActivity) getActivity();
        bundle = activity.getBundle();
        criticAdapter = new CriticAdapter(getActivity(), 0);
        bookAdded=activity.isBookadded();
        critics = activity.getCritics();

        /*
        Critic item = new Critic();
        item.setTitle("Saját Kritikám");
        item.setAuthor("Én");
        item.setCriticText("Ügyes Okos Szép Kritika");
        item.setRate(4);
        item.setCreatedTime("2016.01.10");
        item.setUpdatedTime("2016.01.10");
        item.setMine(true);
        items.add(item);

        for (int i = 0; i < 30; i++) {
            item = new Critic();
            item.setTitle("Title " + i);
            item.setAuthor("Author " + i);
            item.setCriticText("BlablaBlaasdfsalkuhuslaldhfhfhalikawefjcguisadhasjdeuizBLAblaBlabLA");
            item.setRate(3);
            item.setCreatedTime("2015.10.10");
            item.setUpdatedTime("2016.01.10");
            item.setMine(false);
            items.add(item);
        }
        */
        criticAdapter.setCritic(critics);
        listView.setAdapter(criticAdapter);
        listView.setOnItemClickListener(listItemClick);
        return v;
    }

    AdapterView.OnItemClickListener listItemClick = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Critic item = criticAdapter.getCritic(position);
            Intent detailIntent = new Intent(context, CriticDetailsActivity.class);
            detailIntent.putExtra("title", item.getTitle());
            detailIntent.putExtra("author", item.getUser_name());
            detailIntent.putExtra("critictext", item.getCritic());
            detailIntent.putExtra("rate", item.getRating());
            detailIntent.putExtra("createdtime", item.getCreated_at());
            detailIntent.putExtra("updatedtime", item.getUpdated_at());
            detailIntent.putExtra("booktitle", bundle.getString("title"));
            detailIntent.putExtra("mine", item.isMine());
            context.startActivity(detailIntent);
            Toast.makeText(context, "Details", Toast.LENGTH_LONG).show();
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
        if(bookAdded) {
            inflater.inflate(R.menu.menu_critics, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.write_critic:
                Intent writeIntent = new Intent(context,WriteCriticActivity.class);
                writeIntent.putExtra("booktitle", bundle.getString("title"));
                context.startActivity(writeIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
