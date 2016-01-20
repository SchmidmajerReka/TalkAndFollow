package hu.rka.talkfollow;

import android.app.SearchManager;
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

import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import hu.rka.talkfollow.adapters.BookAdapter;
import hu.rka.talkfollow.models.Book;
import hu.rka.talkfollow.models.IndustryIdentifiers;
import hu.rka.talkfollow.models.VolumeInfo;
import hu.rka.talkfollow.network.ContentSpiceService;
import hu.rka.talkfollow.requests.GetBookRequest;
import hu.rka.talkfollow.results.BookResults;

/**
 * Created by Réka on 2016.01.09..
 */
public class AddBookActivity extends AppCompatActivity {
    BookAdapter bookAdapter;
    private Context context;
    @Bind(R.id.offer_book_list)
    ListView offerBookList;
    @Bind(R.id.search_help_text)
    TextView helpText;
    Random rand = new Random();
    private SpiceManager spiceManager = new SpiceManager(ContentSpiceService.class);
    ArrayList<Book> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        ButterKnife.bind(this);
        context=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add to Library");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bookAdapter = new BookAdapter(context, 0);

        for (int i = 90; i < 98; i++) {
            IndustryIdentifiers industryIdentifiers = new IndustryIdentifiers();
            industryIdentifiers.setType("ISBN_13");
            industryIdentifiers.setIdentifier("123456789");
            ArrayList<IndustryIdentifiers> industryIdentifiersArray = new ArrayList<>();
            industryIdentifiersArray.add(industryIdentifiers);
            VolumeInfo volumeInfo = new VolumeInfo();
            volumeInfo.setTitle("Title: " + (100 - i));
            ArrayList<String> authors = new ArrayList<>();
            authors.add("Title: " + (100 - i));
            volumeInfo.setAuthors(authors);
            volumeInfo.setDescription("If you can't explain it simply you don't understand it well enough");
            volumeInfo.setIndustryIdentifierses(industryIdentifiersArray);
            volumeInfo.setPageCount(165);
            ArrayList<String> categories = new ArrayList<>();
            categories.add("Romantic");
            categories.add("Sci-fi");
            volumeInfo.setCategories(categories);
            volumeInfo.setAverageRating(4);
            Book item = new Book();
            item.setVolumeInfo(volumeInfo);
            int randomnumber = rand.nextInt(item.getVolumeInfo().getPageCount())%+1;
            item.setPageRead(randomnumber);
            item.setMyRating(5);
            items.add(item);
        }

        boolean showChat = false;
        GetBookRequest getDataRequest = new GetBookRequest("9781780891286");
        spiceManager.execute(getDataRequest, new DataRequestListener());
        bookAdapter.setBook(items, showChat);
        offerBookList.setAdapter(bookAdapter);

        offerBookList.setOnItemClickListener(listItemClick);
    }

    AdapterView.OnItemClickListener listItemClick = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Book item = bookAdapter.getBook(position);
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
        }
    };


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
                Toast.makeText(context, "Search phrase: " + query, Toast.LENGTH_LONG).show();
                return false;
            }

            // Ha megváltozott a keresőben a szöveg akkor fut le.
            @Override
            public boolean onQueryTextChange(String query) {
                Toast.makeText(context, "Text changed: " + query, Toast.LENGTH_LONG).show();
                return true;
            }
        });

        return true;
    };



    public final class DataRequestListener implements
            RequestListener<BookResults> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, "Hiba történt!!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(BookResults result) {
            ArrayList<Book> itemstmp = result.getItems();
            if (itemstmp != null){
                Toast.makeText(context, "Hello Adat!", Toast.LENGTH_LONG).show();
                boolean showChat = false;
                for(int i=0; i<itemstmp.size(); i++){
                    items.add(itemstmp.get(i));
                }
                bookAdapter.setBook(items, showChat);
            }else{
                Toast.makeText(context, "Book not found", Toast.LENGTH_LONG).show();
                helpText.setText("Since ISBN numbers are country specific, there is a chance that the book you are looking for is not available on that language according to google.books. you can try to find the book by author or title");
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                GetBookRequest getDataRequest = new GetBookRequest(result.getContents());
                spiceManager.execute(getDataRequest, new DataRequestListener());
                helpText.setText("");
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_search:
                //Intent detailIntent = new Intent(context, LoginActivity.class);
                //context.startActivity(detailIntent);
                Toast.makeText(context, "Search book", Toast.LENGTH_LONG).show();
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

