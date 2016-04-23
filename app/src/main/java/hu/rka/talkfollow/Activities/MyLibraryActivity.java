package hu.rka.talkfollow.Activities;

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
import android.widget.TextView;
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
import hu.rka.talkfollow.R;
import hu.rka.talkfollow.adapters.BookAdapter;
import hu.rka.talkfollow.models.Book;
import hu.rka.talkfollow.network.ContentSpiceService;
import hu.rka.talkfollow.requests.GetMyLibraryRequest;
import hu.rka.talkfollow.results.MyLibraryResult;

/**
 * Created by Réka on 2016.01.08..
 */
public class MyLibraryActivity extends AppCompatActivity {

    @Bind(R.id.my_book_list)
    ListView bookList;

    @Bind(R.id.my_library_empty_view)
    TextView emptyView;

    BookAdapter bookAdapter;
    private Context context;
    private SpiceManager spiceManager = new SpiceManager(ContentSpiceService.class);
    ArrayList<Book> items = new ArrayList<>();
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_library);
        ButterKnife.bind(this);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.my_library);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bookAdapter = new BookAdapter(context, 0);
        bundle = getIntent().getExtras();

        GetMyLibraryRequest getMyLibraryRequest = new GetMyLibraryRequest();
        spiceManager.execute(getMyLibraryRequest, new myLibraryListener());

        boolean showChat = true;
        bookAdapter.setBook(items, showChat);
        bookList.setEmptyView(emptyView);
        bookList.setAdapter(bookAdapter);
        bookList.setOnItemClickListener(listItemClick);
    }

    public final class myLibraryListener implements
            RequestListener<MyLibraryResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(MyLibraryResult result) {
            if (result.getMsg().equals("")) {
                bookAdapter.setBook(result.getBooks(), true);
            } else {
                Toast.makeText(context, getString(R.string.error) + result.getMsg(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetMyLibraryRequest getMyLibraryRequest = new GetMyLibraryRequest();
        spiceManager.execute(getMyLibraryRequest, new myLibraryListener());
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

    AdapterView.OnItemClickListener listItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Book item = bookAdapter.getBook(position);

            Intent detailIntent = new Intent(context, TabMenuActivity.class);
            detailIntent.putExtra("molyid", item.getMolyid());
            detailIntent.putExtra("id", item.getId());
            detailIntent.putExtra("added", true);
            detailIntent.putExtra("title", item.getTitle());
            detailIntent.putExtra("author", item.getAuthors());
            context.startActivity(detailIntent);
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
        switch (id) {
            case android.R.id.home:
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
    public void onBackPressed() {
        LoginManager.getInstance().logOut();
        this.finish();
    }
}