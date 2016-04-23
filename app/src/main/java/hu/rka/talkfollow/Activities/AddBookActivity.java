package hu.rka.talkfollow.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import hu.rka.talkfollow.R;
import hu.rka.talkfollow.adapters.BookAdapter;
import hu.rka.talkfollow.models.Book;
import hu.rka.talkfollow.network.ContentSpiceService;
import hu.rka.talkfollow.requests.GetRecommendationsRequest;
import hu.rka.talkfollow.requests.GetSearchResultRequest;
import hu.rka.talkfollow.results.RecommendationsResult;
import hu.rka.talkfollow.results.SearchResult;

/**
 * Created by Réka on 2016.01.09..
 */
public class AddBookActivity extends AppCompatActivity {

    @Bind(R.id.offer_book_list)
    ListView offerBookList;
    @Bind(R.id.search_help_text)
    TextView helpText;

    BookAdapter bookAdapter;
    private Context context;
    private SpiceManager spiceManager = new SpiceManager(ContentSpiceService.class);
    ArrayList<Book> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        ButterKnife.bind(this);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.add_to_library);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bookAdapter = new BookAdapter(context, 0);
        boolean showChat = false;

        GetRecommendationsRequest getRecommendationsRequest = new GetRecommendationsRequest();
        spiceManager.execute(getRecommendationsRequest, new RecommendationsListener());

        bookAdapter.setBook(items, showChat);
        offerBookList.setAdapter(bookAdapter);
        offerBookList.setOnItemClickListener(listItemClick);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add_book, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // Ha a keresés gombra kattintott a billentyuzeten akkor fut le.
            @Override
            public boolean onQueryTextSubmit(String query) {
                GetSearchResultRequest getSearchRequest = new GetSearchResultRequest(String.valueOf(query));
                spiceManager.execute(getSearchRequest, new SearchRequestListener());
                return false;
            }

            // Ha megváltozott a keresőben a szöveg akkor fut le.
            @Override
            public boolean onQueryTextChange(String query) {
                return true;
            }
        });

        return true;
    }

    AdapterView.OnItemClickListener listItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Book item = bookAdapter.getBook(position);

            Intent detailIntent = new Intent(context, TabMenuActivity.class);
            detailIntent.putExtra("molyid", item.getMolyid());
            detailIntent.putExtra("added", false);
            detailIntent.putExtra("title", item.getTitle());
            detailIntent.putExtra("author", item.getAuthors());
            context.startActivity(detailIntent);
        }
        /*    Book item = bookAdapter.getBook(position);
            Intent detailIntent = new Intent(context, TabMenuActivity.class);
            detailIntent.putExtra("added" ,false);
            detailIntent.putExtra("url", item.getVolumeInfo().getImageLinks());
            detailIntent.putExtra("title", item.getVolumeInfo().getTitle());
            detailIntent.putExtra("author", item.getVolumeInfo().getAuthors());
            detailIntent.putExtra("tags", item.getVolumeInfo().getCategories());
            detailIntent.putExtra("bookmark", item.getPageRead());
            detailIntent.putExtra("otherrating", item.getVolumeInfo().getAverageRating());
            detailIntent.putExtra("myrating", item.getMyRating());
            detailIntent.putExtra("description", item.getVolumeInfo().getDescription());
            context.startActivity(detailIntent);
        }*/
    };


    public final class SearchRequestListener implements RequestListener<SearchResult> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {

            helpText.setVisibility(View.VISIBLE);
            helpText.setText(R.string.network_error_no_book_found);
            ArrayList<Book> empty = new ArrayList<>();
            bookAdapter.setBook(empty, false);
        }

        @Override
        public void onRequestSuccess(SearchResult searchResult) {
            if (searchResult.getMsg() != null && searchResult.getMsg().equals("")) {
                helpText.setVisibility(View.GONE);
                ArrayList<Book> books = searchResult.getItems();
                if (books != null) {
                    bookAdapter.setBook(books, false);
                } else {
                    Toast.makeText(context, R.string.book_not_found, Toast.LENGTH_LONG).show();
                }
            } else {
                if (searchResult.getMsg() != null) {
                    Toast.makeText(context, getString(R.string.error) + searchResult.getMsg(), Toast.LENGTH_LONG).show();
                    helpText.setVisibility(View.VISIBLE);
                    helpText.setText(R.string.error_occured_no_book_found);
                    if (searchResult.getItems() != null) {
                        bookAdapter.setBook(searchResult.getItems(), false);
                    }
                }
            }
        }
    }


    public final class RecommendationsListener implements
            RequestListener<RecommendationsResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, R.string.network_error, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(RecommendationsResult result) {
            ArrayList<Book> itemstmp = result.getItems();
            if (itemstmp != null) {
                boolean showChat = false;
                for (int i = 0; i < itemstmp.size(); i++) {
                    items.add(itemstmp.get(i));
                }
                bookAdapter.setBook(items, showChat);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
            } else {
                Log.d("MainActivity", "Scanned");
                GetSearchResultRequest getSearchRequest = new GetSearchResultRequest(result.getContents());
                spiceManager.execute(getSearchRequest, new SearchRequestListener());
                helpText.setVisibility(View.VISIBLE);
                helpText.setText(R.string.ISBN_not_found_try_text_search);
                Toast.makeText(this, getString(R.string.scanned) + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                return true;
            case R.id.barcode:
                new IntentIntegrator(this).initiateScan();
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
}

