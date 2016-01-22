package hu.rka.talkfollow;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import hu.rka.talkfollow.models.Book;
import hu.rka.talkfollow.network.ContentSpiceService;
import hu.rka.talkfollow.requests.GetDetailsRequest;
import hu.rka.talkfollow.results.DetailsResult;

/**
 * Created by Réka on 2016.01.09..
 */
public class BookDetailsFrag extends android.support.v4.app.Fragment {

    @Bind(R.id.detail_tags) TextView tags;
    @Bind(R.id.detail_bookmark) TextView bookmark;
    @Bind(R.id.others_ratingBar) RatingBar othersRating;
    @Bind(R.id.my_ratingBar) RatingBar myRating;
    @Bind(R.id.detail_description) TextView description;
    @Bind(R.id.add_button) Button add;
    @Bind(R.id.detail_cover) ImageView detailCover;
    @Bind(R.id.detail_pagenum)
    LinearLayout pagenumDetails;
    @Bind(R.id.edit_detail_bookmark) ImageView editBookmark;
    @Bind(R.id.detail_finished_button) Button bookFinished;
    @Bind(R.id.detail_visibility_text)
    TextView visibilityText;
    @Bind(R.id.detail_visibility)
    CheckBox visibility;
    Context context;
    boolean bookAdded;
    Bundle bundle;
    TabMenuActivity activity;
    Book bookDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_book_details, container, false);
        ButterKnife.bind(this,v);
        context = getActivity();
        activity = (TabMenuActivity) getActivity();
        bundle = activity.getBundle();
        bookAdded = activity.isBookadded();
        bookDetail = activity.getBookDetails();

        tags.setText("Tags: " + bookDetail.getTags());
        Picasso.with(context).load(bookDetail.getPicture()).placeholder(R.drawable.bookcover).into(detailCover);
        if(bookAdded) {
            pagenumDetails.setVisibility(View.VISIBLE);
            bookmark.setText(String.valueOf(bookDetail.getBookmark()));
            myRating.setVisibility(View.VISIBLE);
            myRating.setRating(bookDetail.getMy_rating());
            editBookmark.setOnClickListener(editBookmarkClick);
            bookFinished.setVisibility(View.VISIBLE);
            bookFinished.setOnClickListener(bookFinishedClick);
            visibilityText.setVisibility(View.VISIBLE);
            visibility.setVisibility(View.VISIBLE);
            visibility.setOnClickListener(checkBoxClick);
        }else{
            myRating.setVisibility(View.INVISIBLE);
            add.setVisibility(View.VISIBLE);
            add.setOnClickListener(addClick);
        }


        othersRating.setRating( bookDetail.getAverage_rating());
        othersRating.setRating((float)2);

        //Toast.makeText(context, "AverageRating: " + othersRating.getRating(), Toast.LENGTH_LONG).show();

        description.setText(bookDetail.getDescription());

        return v;
    }

    private View.OnClickListener addClick=new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            activity.setBookadded(true);
            bookAdded = true;
            pagenumDetails.setVisibility(View.VISIBLE);
            bookmark.setText("0");
            myRating.setVisibility(View.VISIBLE);
            editBookmark.setOnClickListener(editBookmarkClick);
            bookFinished.setVisibility(View.VISIBLE);
            visibilityText.setVisibility(View.VISIBLE);
            visibility.setVisibility(View.VISIBLE);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private View.OnClickListener editBookmarkClick=new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            final Dialog bookmarkDialog = new Dialog(context);
            bookmarkDialog.setContentView(R.layout.dialog_edit_bookmark);
            bookmarkDialog.setTitle("Set bookmark page: ");
            Button setBookmark = (Button) bookmarkDialog.findViewById(R.id.edit_bookmark_confirm);

            setBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final EditText editText = (EditText) bookmarkDialog.findViewById(R.id.edit_bookmark);
                    bookmark.setText(editText.getText());
                    bookmarkDialog.dismiss();
                }
            });
            bookmarkDialog.show();
            bookFinished.setBackgroundResource(android.R.drawable.btn_default);

            Toast.makeText(context, "Edit number", Toast.LENGTH_LONG).show();
        }
    };

    private View.OnClickListener bookFinishedClick=new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            bookFinished.setBackgroundColor(Color.YELLOW);
            Toast.makeText(context, "Book Finished", Toast.LENGTH_LONG).show();
            bookmark.setText("0");
        }
    };

    private View.OnClickListener checkBoxClick=new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if(visibility.isChecked()){
                Toast.makeText(context, "You can appear in the readers' list", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context, "You can not appear in the readers' list", Toast.LENGTH_LONG).show();
            }
        }
    };

}
