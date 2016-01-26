package hu.rka.talkfollow;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.squareup.picasso.Picasso;
import butterknife.Bind;
import butterknife.ButterKnife;
import hu.rka.talkfollow.models.Book;
import hu.rka.talkfollow.models.UploadBookAdd;
import hu.rka.talkfollow.models.UploadBookRead;
import hu.rka.talkfollow.models.UploadBookmark;
import hu.rka.talkfollow.models.UploadRating;
import hu.rka.talkfollow.models.UploadVisibility;
import hu.rka.talkfollow.network.ContentSpiceService;
import hu.rka.talkfollow.requests.PostBookAddRequest;
import hu.rka.talkfollow.requests.PostBookReadRequest;
import hu.rka.talkfollow.requests.PostBookmarkRequest;
import hu.rka.talkfollow.requests.PostRatingRequest;
import hu.rka.talkfollow.requests.PostVisibilityRequest;
import hu.rka.talkfollow.results.EditBookmarkResult;
import hu.rka.talkfollow.results.EditRatingResult;
import hu.rka.talkfollow.results.SetBookAddedResult;
import hu.rka.talkfollow.results.SetBookReadResult;
import hu.rka.talkfollow.results.SetVisibilityResult;


/**
 * Created by Réka on 2016.01.09..
 */
public class BookDetailsFrag extends android.support.v4.app.Fragment {

    @Nullable@Bind(R.id.detail_tags) TextView tags;
    @Nullable@Bind(R.id.detail_bookmark) TextView bookmark;
    @Nullable@Bind(R.id.others_ratingBar) RatingBar othersRating;
    @Nullable@Bind(R.id.my_ratingBar) RatingBar myRating;
    @Nullable@Bind(R.id.detail_description) TextView description;
    @Nullable@Bind(R.id.add_button) Button add;
    @Nullable@Bind(R.id.detail_cover) ImageView detailCover;
    @Nullable@Bind(R.id.detail_pagenum)
    LinearLayout pagenumDetails;
    @Nullable@Bind(R.id.edit_detail_bookmark) ImageView editBookmark;
    @Nullable@Bind(R.id.detail_finished_button) Button bookFinished;
    @Nullable@Bind(R.id.detail_visibility_text)
    TextView visibilityText;
    @Nullable@Bind(R.id.detail_visibility)
    CheckBox visibility;
    Context context;
    boolean bookAdded;
    Bundle bundle;
    TabMenuActivity activity;
    Book bookDetail;
    Dialog bookmarkDialog;
    private SpiceManager spiceManager = new SpiceManager(ContentSpiceService.class);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_book_details, container, false);
        ButterKnife.bind(this,v);
        context = getActivity();
        activity = (TabMenuActivity) getActivity();
        bundle = activity.getBundle();
        bookDetail = activity.getBookDetails();

        if(bookDetail!=null) {
            bookAdded = bookDetail.isMine();
            tags.setText("Tags: " + bookDetail.getTags());
            Picasso.with(context).load(bookDetail.getPicture()).placeholder(R.drawable.bookcover).into(detailCover);
            if (bookAdded) {
                pagenumDetails.setVisibility(View.VISIBLE);
                bookmark.setText(String.valueOf(bookDetail.getBookmark()));
                myRating.setMax(5);
                myRating.setStepSize(0.5f);
                myRating.setRating(bookDetail.getMy_rating());
                myRating.setVisibility(View.VISIBLE);
                myRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        UploadRating uploadRating = new UploadRating(bookDetail.getId(), myRating.getRating());
                        PostRatingRequest postRatingRequest = new PostRatingRequest(uploadRating);
                        spiceManager.execute(postRatingRequest, new PostRatingListener());
                    }
                });

                editBookmark.setOnClickListener(editBookmarkClick);
                bookFinished.setVisibility(View.VISIBLE);
                bookFinished.setOnClickListener(bookFinishedClick);
                visibilityText.setVisibility(View.VISIBLE);
                visibility.setVisibility(View.VISIBLE);
                visibility.setOnClickListener(checkBoxClick);
            } else {
                myRating.setVisibility(View.INVISIBLE);
                add.setVisibility(View.VISIBLE);
                add.setOnClickListener(addClick);
            }
            othersRating.setMax(5);
            othersRating.setStepSize(0.5f);
            othersRating.setRating(bookDetail.getAverage_rating());
            Toast.makeText(context, "AverageRating: " + othersRating.getRating(), Toast.LENGTH_LONG).show();
            description.setText(bookDetail.getDescription());
        }
        return v;
    }

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

    public final class PostRatingListener implements
            RequestListener<EditRatingResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, "Hiba történt!!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(EditRatingResult result) {

            if(result.getMsg() == ""){
                bookDetail.setMy_rating(myRating.getRating());
                Toast.makeText(context, "Rating changed: " + String.valueOf(myRating.getRating()), Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context, "Error: " + result.getMsg(), Toast.LENGTH_LONG).show();
                myRating.setRating(bookDetail.getMy_rating());
            }
        }
    }


    private View.OnClickListener addClick=new View.OnClickListener(){

        @Override
        public void onClick(View v) {

            UploadBookAdd uploadBookAdd = new UploadBookAdd(bookDetail.getMolyid());
            PostBookAddRequest postBookAddRequest = new PostBookAddRequest(uploadBookAdd);
            spiceManager.execute(postBookAddRequest, new AddRequestListener());


        }
    };

    public final class AddRequestListener implements
            RequestListener<SetBookAddedResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, "Hiba történt!!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(SetBookAddedResult result) {


            if(result.getMsg() == ""){
                bookDetail.setMine(true);
                bookDetail = result.getBook_details();
                activity.setBookDetails(result.getBook_details());
                activity.setCritics(result.getCritics());
                activity.setReaders(result.getReaders());
                activity.setMessages(result.getForum_messages());

                add.setVisibility(View.GONE);
                pagenumDetails.setVisibility(View.VISIBLE);
                bookmark.setText("0");
                myRating.setVisibility(View.VISIBLE);
                editBookmark.setOnClickListener(editBookmarkClick);
                bookFinished.setVisibility(View.VISIBLE);
                visibilityText.setVisibility(View.VISIBLE);
                visibility.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Added to library", Toast.LENGTH_LONG).show();



            }else{
                Toast.makeText(context, "Error: " + result.getMsg(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private View.OnClickListener editBookmarkClick=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            bookmarkDialog = new Dialog(context);
            bookmarkDialog.setContentView(R.layout.dialog_edit_bookmark);
            bookmarkDialog.setTitle("Set bookmark page: ");
            Button setBookmark = (Button) bookmarkDialog.findViewById(R.id.edit_bookmark_confirm);
            final EditText editText = (EditText) bookmarkDialog.findViewById(R.id.edit_bookmark);
            editText.setText(bookmark.getText());
            setBookmark.setOnClickListener(setBookmarkClick);
            bookmarkDialog.show();
            bookFinished.setBackgroundResource(android.R.drawable.btn_default);
        }
    };

    private View.OnClickListener setBookmarkClick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Edit number", Toast.LENGTH_LONG).show();
            UploadBookmark uploadBookmark = new UploadBookmark();
            final EditText editText = (EditText) bookmarkDialog.findViewById(R.id.edit_bookmark);
            uploadBookmark.setBookmark(Integer.valueOf(String.valueOf(editText.getText())));
            PostBookmarkRequest postBookmarkRequest = new PostBookmarkRequest(uploadBookmark);
            spiceManager.execute(postBookmarkRequest, new BookmarkRequestListener());
            bookmarkDialog.dismiss();
        }
    };

    public final class BookmarkRequestListener implements
            RequestListener<EditBookmarkResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, "Hiba történt!!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(EditBookmarkResult result) {

            if(result.getMsg() == ""){
                Toast.makeText(context, "Uploaded", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context, "Error: " + result.getMsg(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public final class BookReadRequestListener implements
            RequestListener<SetBookReadResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, "Hiba történt!!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(SetBookReadResult result) {

            if(result.getMsg() == ""){
                bookFinished.setBackgroundColor(Color.YELLOW);
                Toast.makeText(context, "Book Finished", Toast.LENGTH_LONG).show();
                bookmark.setText("0");
            }else {
                Toast.makeText(context, "Error: " + result.getMsg(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public final class PostVisibilityListener implements
            RequestListener<SetVisibilityResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, "Hiba történt!!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(SetVisibilityResult result) {

            if(result.getMsg() == ""){
                Toast.makeText(context, "Visibility Changed", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(context, "Error: " + result.getMsg(), Toast.LENGTH_LONG).show();
            }
        }
    }


    private View.OnClickListener bookFinishedClick=new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            UploadBookRead uploadBookRead = new UploadBookRead(bookDetail.getId(), true);
            PostBookReadRequest postBookReadRequest = new PostBookReadRequest(uploadBookRead);
            spiceManager.execute(postBookReadRequest, new BookReadRequestListener());
        }
    };

    private View.OnClickListener checkBoxClick=new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if(visibility.isChecked()){
                UploadVisibility uploadVisibility =new UploadVisibility(bookDetail.getId(), true);
                PostVisibilityRequest postVisibilityRequest = new PostVisibilityRequest(uploadVisibility);
                spiceManager.execute(postVisibilityRequest, new PostVisibilityListener());

            }else{
                UploadVisibility uploadVisibility =new UploadVisibility(bookDetail.getId(), false);
                PostVisibilityRequest postVisibilityRequest = new PostVisibilityRequest(uploadVisibility);
                spiceManager.execute(postVisibilityRequest, new PostVisibilityListener());
            }
        }
    };

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
