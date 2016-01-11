package hu.rka.talkfollow;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Réka on 2016.01.09..
 */
public class BookDetailsFrag extends android.support.v4.app.Fragment {

    @Bind(R.id.detail_isbn)
    TextView isbn;
    @Bind(R.id.detail_genre) TextView genre;
    @Bind(R.id.detail_pagenum) TextView pagenum;
    @Bind(R.id.others_ratingBar)
    RatingBar othersRating;
    @Bind(R.id.my_ratingBar) RatingBar myRating;
    @Bind(R.id.detail_description) TextView description;
    Context context;
    @Bind(R.id.add_button)
    Button add;
    boolean bookAdded;
    Bundle bundle;
    TabMenuActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_book_details, container, false);
        ButterKnife.bind(this,v);
        context = getActivity();
        activity = (TabMenuActivity) getActivity();
        //activity.getMenuInflater().inflate(R.menu.menu_bookdetail, menu);
        bundle = activity.getBundle();
        bookAdded = activity.isBookadded();
        isbn.setText("ISBN: " + bundle.getString("isbn"));
        genre.setText("Genre: " + bundle.getString("genre"));
        if(bookAdded) {
            pagenum.setText("Page number: " + bundle.getInt("pageread") + "/" + bundle.getInt("pagenum"));
            myRating.setVisibility(View.VISIBLE);
            myRating.setRating(bundle.getFloat("myrating"));
            pagenum.setOnClickListener(textClick);
        }else{
            pagenum.setText("Page number: " + bundle.getInt("pagenum"));
            myRating.setVisibility(View.INVISIBLE);
            add.setVisibility(View.VISIBLE);
            add.setOnClickListener(addClick);
        }
        othersRating.setRating(bundle.getFloat("otherrating"));
        description.setText(bundle.getString("description"));

        return v;

    }

    private View.OnClickListener addClick=new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            activity.setBookadded(true);
            bookAdded=true;
            pagenum.setText("Page number: " + bundle.getInt("pageread") + "/" + bundle.getInt("pagenum"));
            myRating.setVisibility(View.VISIBLE);
            myRating.setRating(bundle.getFloat("myrating"));
            pagenum.setOnClickListener(textClick);

            Toast.makeText(context, "Added to library", Toast.LENGTH_LONG).show();
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
            inflater.inflate(R.menu.menu_bookdetail, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.visibility:
                Toast.makeText(context,"Visibility set",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private View.OnClickListener textClick=new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Edit number", Toast.LENGTH_LONG).show();
        }
    };

}
