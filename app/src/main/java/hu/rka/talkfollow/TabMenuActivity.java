package hu.rka.talkfollow;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import hu.rka.talkfollow.adapters.TabPagerAdapter;

/**
 * Created by Réka on 2016.01.09..
 */
public class TabMenuActivity extends AppCompatActivity {

    Bundle bundle;
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


    public Bundle getBundle() {
        return bundle;
    }
}
