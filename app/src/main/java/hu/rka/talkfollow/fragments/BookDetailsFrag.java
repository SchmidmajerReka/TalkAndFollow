package hu.rka.talkfollow.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import hu.rka.talkfollow.Activities.TabMenuActivity;
import hu.rka.talkfollow.R;
import hu.rka.talkfollow.models.Book;
import hu.rka.talkfollow.models.UploadBookAdd;
import hu.rka.talkfollow.models.UploadBookRead;
import hu.rka.talkfollow.models.UploadBookmark;
import hu.rka.talkfollow.models.UploadRating;
import hu.rka.talkfollow.models.UploadVisibility;
import hu.rka.talkfollow.network.ContentSpiceService;
import hu.rka.talkfollow.requests.DeleteBookRequest;
import hu.rka.talkfollow.requests.PostBookAddRequest;
import hu.rka.talkfollow.requests.PostBookReadRequest;
import hu.rka.talkfollow.requests.PostBookmarkRequest;
import hu.rka.talkfollow.requests.PostRatingRequest;
import hu.rka.talkfollow.requests.PostVisibilityRequest;
import hu.rka.talkfollow.results.DeleteBookResult;
import hu.rka.talkfollow.results.EditBookmarkResult;
import hu.rka.talkfollow.results.EditRatingResult;
import hu.rka.talkfollow.results.SetBookAddedResult;
import hu.rka.talkfollow.results.SetBookReadResult;
import hu.rka.talkfollow.results.SetVisibilityResult;


/**
 * Created by Réka on 2016.01.09..
 */
public class BookDetailsFrag extends android.support.v4.app.Fragment {

    @Bind(R.id.detail_tags)
    TextView tags;

    @Bind(R.id.detail_bookmark)
    TextView bookmark;

    @Bind(R.id.others_ratingBar)
    RatingBar othersRating;

    @Bind(R.id.my_ratingBar)
    RatingBar myRating;

    @Bind(R.id.detail_description)
    TextView description;

    @Bind(R.id.add_button)
    Button add;

    @Bind(R.id.detail_cover)
    ImageView detailCover;

    @Bind(R.id.detail_pagenum)
    LinearLayout pagenumDetails;

    @Bind(R.id.edit_detail_bookmark)
    ImageView editBookmark;

    @Bind(R.id.detail_finished_button)
    Button bookFinished;

    @Bind(R.id.detail_visibility_text)
    TextView visibilityText;

    @Bind(R.id.detail_visibility)
    CheckBox visibility;

    Context context;
    boolean bookAdded;
    Bundle bundle;
    TabMenuActivity activity;
    Book bookDetail;
    private SpiceManager spiceManager = new SpiceManager(ContentSpiceService.class);
    int bookmarktmp;
    boolean onCreate = false;

    View dialogView;
    AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_book_details, container, false);
        ButterKnife.bind(this, v);
        context = getActivity();
        activity = (TabMenuActivity) getActivity();
        bundle = activity.getBundle();
        bookDetail = activity.getBookDetails();
        onCreate = true;
        setUI(bookDetail);
        return v;
    }

    public void refreshUI(Book bookDetail) {
        setUI(bookDetail);
    }

    private void setUI(final Book bookDetail) {
        if (bookDetail != null) {
            if (onCreate) {
                bookAdded = bookDetail.isMine();
                tags.setText(bookDetail.getTags());
                Picasso.with(context).load(bookDetail.getPicture()).placeholder(R.drawable.bookcover).into(detailCover);
                if (bookAdded) {
                    pagenumDetails.setVisibility(View.VISIBLE);
                    add.setVisibility(View.GONE);
                    bookmark.setText(String.valueOf(bookDetail.getBookmark()));
                    myRating.setMax(5);
                    myRating.setStepSize(0.5f);
                    myRating.setRating(bookDetail.getMy_rating());
                    myRating.setVisibility(View.VISIBLE);
                    myRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                            if (fromUser) {
                                UploadRating uploadRating = new UploadRating(bookDetail.getId(), myRating.getRating());
                                PostRatingRequest postRatingRequest = new PostRatingRequest(uploadRating);
                                spiceManager.execute(postRatingRequest, new PostRatingListener());
                            }
                        }
                    });

                    editBookmark.setOnClickListener(editBookmarkClick);
                    if (bookDetail.isFinished()) {
                        bookFinished.setBackgroundColor(context.getResources().getColor(R.color.comment_background_dark));
                        bookFinished.setTextColor(context.getResources().getColor(R.color.white_text_color));
                    }
                    bookFinished.setVisibility(View.VISIBLE);
                    bookFinished.setOnClickListener(bookFinishedClick);
                    visibilityText.setVisibility(View.VISIBLE);
                    visibility.setVisibility(View.VISIBLE);
                    visibility.setChecked(bookDetail.isVisibility());
                    visibility.setOnClickListener(checkBoxClick);
                } else {
                    pagenumDetails.setVisibility(View.GONE);
                    myRating.setVisibility(View.GONE);
                    add.setVisibility(View.VISIBLE);
                    add.setOnClickListener(addClick);
                }
                othersRating.setMax(5);
                othersRating.setStepSize(0.5f);
                othersRating.setRating(bookDetail.getAverage_rating());
                description.setText(bookDetail.getDescription());
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        menu.clear();
        if (bookAdded) {
            Log.e("BookDetailsmenu", "1");
            inflater.inflate(R.menu.menu_bookdetail, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.delete_book:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                // set title
                alertDialogBuilder.setTitle(getString(R.string.delete));
                // set dialog message
                alertDialogBuilder
                        .setMessage(getString(R.string.click_yes_to_delete_book))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DeleteBookRequest deleteBookRequest = new DeleteBookRequest(bookDetail.getId());
                                spiceManager.execute(deleteBookRequest, new DeleteBookRequestListener());
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public final class DeleteBookRequestListener implements
            RequestListener<DeleteBookResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(DeleteBookResult deleteCriticResult) {
            if (deleteCriticResult.getMsg() != null && deleteCriticResult.getMsg().equals("")) {
//                Toast.makeText(context, "Book deleted", Toast.LENGTH_LONG).show();
                activity.finish();
            } else if (deleteCriticResult.getMsg() != null) {
                Toast.makeText(context, getString(R.string.error) + deleteCriticResult.getMsg(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public final class PostRatingListener implements
            RequestListener<EditRatingResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(EditRatingResult result) {

            if (result.getMsg().equals("")) {
                bookDetail.setMy_rating(myRating.getRating());
//                Toast.makeText(context, "Rating changed: " + String.valueOf(myRating.getRating()), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, getString(R.string.error) + result.getMsg(), Toast.LENGTH_LONG).show();
                myRating.setRating(bookDetail.getMy_rating());
            }
        }
    }


    private View.OnClickListener addClick = new View.OnClickListener() {

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
            Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(SetBookAddedResult result) {


            if (result.getMsg().equals("")) {
                bookDetail.setMine(true);
                bookDetail = result.getBook_details();
                activity.setBookDetails(result.getBook_details());
                activity.setCritics(result.getCritics());
                activity.setReaders(result.getReaders());
                activity.setMessages(result.getForum_messages());
                activity.refreshTabLayout();
//                Toast.makeText(context, "Added to library", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(context, getString(R.string.error) + result.getMsg(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private View.OnClickListener editBookmarkClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {



            AlertDialog.Builder bookmarkDialog = new AlertDialog.Builder(context);
// ...Irrelevant code for customizing the buttons and title
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            dialogView = inflater.inflate(R.layout.dialog_edit_bookmark, null);
            bookmarkDialog.setView(dialogView);
            bookmarkDialog.setTitle(R.string.set_bookmark_page);

            alertDialog = bookmarkDialog.create();

            Button setBookmark = (Button) dialogView.findViewById(R.id.edit_bookmark_confirm);
//            final EditText editText = (EditText) dialogView.findViewById(R.id.edit_about_me_text);
//            editText.setText(aboutMe.getText());
            setBookmark.setOnClickListener(setBookmarkClick);
            alertDialog.show();

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = alertDialog.getWindow();
            lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);

        }
    };

    private View.OnClickListener setBookmarkClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
//            Toast.makeText(context, "Edit number", Toast.LENGTH_LONG).show();
            UploadBookmark uploadBookmark = new UploadBookmark();
            final EditText editText = (EditText) dialogView.findViewById(R.id.edit_bookmark);
            if (!String.valueOf(editText.getText()).equals("")) {
                if (Integer.valueOf(String.valueOf(editText.getText())) != null) {
                    bookmarktmp = Integer.valueOf(String.valueOf(editText.getText()));
                    uploadBookmark.setPage(bookmarktmp);
                    uploadBookmark.setBook_id(bookDetail.getId());
                    PostBookmarkRequest postBookmarkRequest = new PostBookmarkRequest(uploadBookmark);
                    spiceManager.execute(postBookmarkRequest, new BookmarkRequestListener());
                    alertDialog.dismiss();
                }
            } else {
                Toast.makeText(context, R.string.add_a_valid_page_number_or_press_back, Toast.LENGTH_LONG).show();
            }
        }
    };

    public final class BookmarkRequestListener implements
            RequestListener<EditBookmarkResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(EditBookmarkResult result) {

            if (result.getMsg().equals("")) {
//                Toast.makeText(context, "Uploaded", Toast.LENGTH_LONG).show();
                bookmark.setText(String.valueOf(bookmarktmp));

            } else {
                Toast.makeText(context, getString(R.string.error) + result.getMsg(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public final class BookReadRequestListener implements
            RequestListener<SetBookReadResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(SetBookReadResult result) {

            if (result.getMsg().equals("")) {
                bookFinished.setBackgroundColor(context.getResources().getColor(R.color.comment_background_dark));
                bookFinished.setTextColor(context.getResources().getColor(R.color.white_text_color));
//                Toast.makeText(context, "Book Finished", Toast.LENGTH_LONG).show();
                bookmark.setText("0");
            } else {
                Toast.makeText(context, getString(R.string.error) + result.getMsg(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public final class PostVisibilityListener implements
            RequestListener<SetVisibilityResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(SetVisibilityResult result) {

            if (result.getMsg().equals("")) {
//                Toast.makeText(context, "Visibility Changed", Toast.LENGTH_LONG).show();
                activity.setBookDetails(bookDetail);
            } else {
                Toast.makeText(context, getString(R.string.error) + result.getMsg(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private View.OnClickListener bookFinishedClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            UploadBookRead uploadBookRead = new UploadBookRead(bookDetail.getId(), true);
            PostBookReadRequest postBookReadRequest = new PostBookReadRequest(uploadBookRead);
            spiceManager.execute(postBookReadRequest, new BookReadRequestListener());
        }
    };

    private View.OnClickListener checkBoxClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (visibility.isChecked()) {
                bookDetail.setVisibility(true);
                UploadVisibility uploadVisibility = new UploadVisibility(bookDetail.getId(), "true");
                PostVisibilityRequest postVisibilityRequest = new PostVisibilityRequest(uploadVisibility);
                spiceManager.execute(postVisibilityRequest, new PostVisibilityListener());

            } else {
                bookDetail.setVisibility(false);
                UploadVisibility uploadVisibility = new UploadVisibility(bookDetail.getId(), "false");
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