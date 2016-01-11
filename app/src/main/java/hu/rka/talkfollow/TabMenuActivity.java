package hu.rka.talkfollow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
        bookadded=bundle.getBoolean("added");
        starter=bundle.getInt("starter");
        context=this;

        viewPager = (ViewPager) findViewById(R.id.pager);
        if(bookadded){
            tabNumber=4;
        }else{
            tabNumber=3;
        }


        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),tabNumber);

        viewPager.setAdapter(pagerAdapter);
        if(starter==3){
            viewPager.setCurrentItem(3);
        }

    }

    public void setBookadded(boolean bookadded) {
        this.bookadded = bookadded;
        tabNumber=4;
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){


            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/
    }


    public Bundle getBundle() {
        return bundle;
    }
}
