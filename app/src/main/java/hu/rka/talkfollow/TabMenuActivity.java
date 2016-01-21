package hu.rka.talkfollow;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import hu.rka.talkfollow.adapters.TabPagerAdapter;
import hu.rka.talkfollow.models.Book;
import hu.rka.talkfollow.models.Critic;
import hu.rka.talkfollow.models.ForumMessage;
import hu.rka.talkfollow.models.Readers;
import hu.rka.talkfollow.network.ContentSpiceService;
import hu.rka.talkfollow.requests.GetDetailsRequest;
import hu.rka.talkfollow.requests.GetMyProfileRequest;
import hu.rka.talkfollow.results.DetailsResult;
import hu.rka.talkfollow.results.MyProfileResult;

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
    boolean bookadded;
    Context context;
    int tabNumber;
    TabPagerAdapter pagerAdapter;
    Menu menu;
    int starter;

    TabLayout tabLayout;

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
        bookadded = bundle.getBoolean("added");
        starter = bundle.getInt("starter");
        context = this;
        GetDetailsRequest getDataRequest = new GetDetailsRequest(bundle.getInt("molyid"));
        spiceManager.execute(getDataRequest, new DataRequestListener());

    }

    public final class DataRequestListener implements
            RequestListener<DetailsResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, "Hiba történt!!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(DetailsResult result) {
            bookDetail = result.getBook_details();
            critics = result.getCritics();
            readers = result.getReaders();
            messages = result.getForum_messages();

            viewPager = (ViewPager) findViewById(R.id.pager);
            if(bookadded){
                tabNumber = 4;
            }else{
                tabNumber = 3;
            }

            //TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),tabNumber);


            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

            int size = tabNumber;
            for (int i = 0; i < size; i++) {
                tabLayout.addTab(tabLayout.newTab().setText(TabPagerAdapter.titles[i]));
            }
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


            TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabNumber);
            viewPager.setAdapter(pagerAdapter);

            if(starter==3){
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

    public void setBookadded(boolean bookadded) {
        this.bookadded = bookadded;
        tabNumber = 4;
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText(TabPagerAdapter.titles[3]));

        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabNumber);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public boolean isBookadded() {
        return bookadded;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){


            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
