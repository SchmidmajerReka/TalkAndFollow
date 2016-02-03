package hu.rka.talkfollow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import hu.rka.talkfollow.adapters.BookAdapter;
import hu.rka.talkfollow.models.Book;
import hu.rka.talkfollow.network.ContentSpiceService;
import hu.rka.talkfollow.requests.GetMyLibraryRequest;
import hu.rka.talkfollow.results.MyLibraryResult;

/**
 * Created by Réka on 2016.01.08..
 */
public class MyLibraryActivity extends AppCompatActivity {
    BookAdapter bookAdapter;
    private Context context;
    @Bind(R.id.my_book_list)
    ListView bookList;
    private SpiceManager spiceManager = new SpiceManager(ContentSpiceService.class);
    Random rand = new Random();
    ArrayList<Book> items = new ArrayList<>();
    Profile profile;
    Bundle bundle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_library);
        ButterKnife.bind(this);
        context=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Library");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //bookList = (ListView) findViewById(R.id.my_book_list);
        bookAdapter = new BookAdapter(context, 0);
        bundle = getIntent().getExtras();
        GetMyLibraryRequest getMyLibraryRequest = new GetMyLibraryRequest();
        spiceManager.execute(getMyLibraryRequest, new myLibraryListener());
        boolean showChat=true;
        bookAdapter.setBook(items, showChat);
        bookList.setAdapter(bookAdapter);
        bookList.setOnItemClickListener(listItemClick);
        Toast.makeText(context, "Auth-Token: " + PreferencesHelper.getStringByKey(context, "Auth-Token", ""), Toast.LENGTH_LONG).show();
    }

    public final class myLibraryListener implements
            RequestListener<MyLibraryResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, "Hiba történt!!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(MyLibraryResult result) {
            if(result.getError().equals("")){
                Toast.makeText(context, "Hello Adat!", Toast.LENGTH_LONG).show();
                int size = result.getBooks().size();
                bookAdapter.setBook(result.getBooks(), true);
            }else{
                Toast.makeText(context, "Error: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(context);

    }

    AdapterView.OnItemClickListener listItemClick = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Book item = bookAdapter.getBook(position);
            Intent detailIntent = new Intent(context, TabMenuActivity.class);
            detailIntent.putExtra("molyid", item.getMolyid());
            detailIntent.putExtra("added" ,true);
            detailIntent.putExtra("title", item.getTitle());
            detailIntent.putExtra("author", item.getAuthors());
            context.startActivity(detailIntent);
            Toast.makeText(context, "Details", Toast.LENGTH_LONG).show();
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_library, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case  android.R.id.home:
                LoginManager.getInstance().logOut();
                this.finish();
                return true;
            case R.id.add_book:
                Intent detailIntent = new Intent(context, AddBookActivity.class);
                context.startActivity(detailIntent);
                return true;
            case R.id.my_profile:
                Intent profileIntent = new Intent(context, MyProfileActivity.class);
                context.startActivity(profileIntent);
                return true;
            case R.id.order_author:
                bookAdapter.orderAuthor();
                return true;
            case R.id.order_title:
                bookAdapter.orderTitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        GetMyLibraryRequest getMyLibraryRequest = new GetMyLibraryRequest();
        spiceManager.execute(getMyLibraryRequest, new myLibraryListener());
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        LoginManager.getInstance().logOut();
        this.finish();
    }
}