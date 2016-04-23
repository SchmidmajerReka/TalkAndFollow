package hu.rka.talkfollow.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;

import hu.rka.talkfollow.R;
import hu.rka.talkfollow.adapters.TabPagerAdapter;
import hu.rka.talkfollow.models.Book;
import hu.rka.talkfollow.models.Critic;
import hu.rka.talkfollow.models.ForumMessage;
import hu.rka.talkfollow.models.Readers;
import hu.rka.talkfollow.network.ContentSpiceService;
import hu.rka.talkfollow.requests.DeleteBookRequest;
import hu.rka.talkfollow.requests.GetDetailsRequest;
import hu.rka.talkfollow.results.DeleteBookResult;
import hu.rka.talkfollow.results.DetailsResult;

/**
 * Created by Réka on 2016.01.09..
 */
public class TabMenuActivity extends AppCompatActivity {

    Bundle bundle;
    Book bookDetail;
    ArrayList<Critic> critics;
    ArrayList<Readers> readers;
    ArrayList<ForumMessage> messages;
    private SpiceManager spiceManager = new SpiceManager(ContentSpiceService.class);
    ViewPager viewPager;
    Toolbar toolbar;
    boolean bookadded = false;
    Context context;
    int tabNumber;
    TabPagerAdapter pagerAdapter;
    int starter;
    private ProgressDialog progress;

    TabLayout tabLayout;

    public TabMenuActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_menu);
        bundle = getIntent().getExtras();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(bundle.getString("title"));
        getSupportActionBar().setSubtitle(bundle.getString("author"));
        bookDetail = new Book();
        critics = new ArrayList<>();
        readers = new ArrayList<>();
        messages = new ArrayList<>();
        starter = bundle.getInt("starter");
        context = this;

        progress = new ProgressDialog(this);
        progress.setTitle(getString(R.string.please_wait));
        progress.setMessage(getString(R.string.loading_data));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

        viewPager = (ViewPager) findViewById(R.id.pager);
        bookadded = bookDetail.isMine();
        if (bookadded) {
            tabNumber = 4;
        } else {
            tabNumber = 3;
        }

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabNumber, context);
        tabLayout.setTabsFromPagerAdapter(pagerAdapter);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        viewPager.setAdapter(pagerAdapter);

        if (starter == 3) {
            viewPager.setCurrentItem(3);
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        GetDetailsRequest getDataRequest = new GetDetailsRequest(bundle.getInt("molyid"));
        spiceManager.execute(getDataRequest, new DataRequestListener());

    }

    public void refreshTabLayout() {
        if (!bookDetail.isMine()) {
            pagerAdapter.setSize(3);
        } else {
            pagerAdapter.setSize(4);
        }

        tabLayout.setTabsFromPagerAdapter(pagerAdapter);

        progress.dismiss();
        if (starter == 3) {
            tabLayout.setScrollPosition(3, 0f, true);
            viewPager.setCurrentItem(3);
        }
    }

    public final class DataRequestListener implements
            RequestListener<DetailsResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            //handler.sendEmptyMessage(0);
            finish();
            Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(DetailsResult result) {

            if (result.getMsg() != null && result.getMsg().equals("")) {
                bookDetail = result.getBook_details();
                critics = result.getCritics();
                readers = result.getReaders();
                messages = result.getForum_messages();
                if (!bookDetail.isMine()) {
                    pagerAdapter.setSize(3);
                } else {
                    pagerAdapter.setSize(4);
                }
                tabLayout.setTabsFromPagerAdapter(pagerAdapter);

                progress.dismiss();
                pagerAdapter.refreshChilds(result);
                if (starter == 3) {
                    tabLayout.setScrollPosition(3, 0f, true);
                    viewPager.setCurrentItem(3);
                }
            } else {
                if (result != null) {
                    Toast.makeText(context, getString(R.string.error) + result.getMsg(), Toast.LENGTH_LONG).show();
                    finish();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (bookDetail != null && bookDetail.isMine()) {
            getMenuInflater().inflate(R.menu.menu_bookdetail, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.delete_book:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                // set title
                alertDialogBuilder.setTitle(R.string.delete);
                // set dialog message
                alertDialogBuilder
                        .setMessage(R.string.click_yes_to_delete_book)
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
            case android.R.id.home:
                this.finish();
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
                finish();
            } else if (deleteCriticResult.getMsg() != null) {
                Toast.makeText(context, getString(R.string.error) + deleteCriticResult.getMsg(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public Book getBookDetails() {
        return bookDetail;
    }

    public ArrayList<Critic> getCritics() {
        return critics;
    }

    public ArrayList<Readers> getReaders() {
        return readers;
    }

    public ArrayList<ForumMessage> getMessages() {
        return messages;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBookDetails(Book bookDetails) {
        this.bookDetail = bookDetails;
    }

    public void setCritics(ArrayList<Critic> critics) {
        this.critics = critics;
    }

    public void setReaders(ArrayList<Readers> readers) {
        this.readers = readers;
    }

    public void setMessages(ArrayList<ForumMessage> messages) {
        this.messages = messages;
    }
}
