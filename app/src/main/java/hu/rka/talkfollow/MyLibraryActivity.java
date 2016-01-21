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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_library);
        ButterKnife.bind(this);
        context=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Library");
        //bookList = (ListView) findViewById(R.id.my_book_list);
        bookAdapter = new BookAdapter(context, 0);


        /*for (int i = 0; i < 25; i++) {
            IndustryIdentifiers industryIdentifiers = new IndustryIdentifiers();
            industryIdentifiers.setType("ISBN_13");
            industryIdentifiers.setIdentifier("123456789");
            ArrayList<IndustryIdentifiers> industryIdentifiersArray = new ArrayList<>();
            industryIdentifiersArray.add(industryIdentifiers);
            VolumeInfo volumeInfo = new VolumeInfo();
            volumeInfo.setTitle("Title: " + (100 - i));
            ArrayList<String> authors = new ArrayList<>();
            authors.add("Author: " + (100 - i));
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
        }*/
        GetMyLibraryRequest getDataRequest = new GetMyLibraryRequest();
        spiceManager.execute(getDataRequest, new DataRequestListener());
        boolean showChat=true;
        bookAdapter.setBook(items, showChat);
        bookList.setAdapter(bookAdapter);
        bookList.setOnItemClickListener(listItemClick);

    }


    public final class DataRequestListener implements
            RequestListener<MyLibraryResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, "Hiba történt!!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(MyLibraryResult result) {
            ArrayList<Book> itemstmp = result.getItems();
            if (itemstmp != null){
                Toast.makeText(context, "Hello Adat!", Toast.LENGTH_LONG).show();
                boolean showChat = true;
                for(int i=0; i<itemstmp.size(); i++){
                    items.add(itemstmp.get(i));
                }
                bookAdapter.setBook(items, showChat);
            }else {
                Toast.makeText(context, "Book not found", Toast.LENGTH_LONG).show();
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
            /*detailIntent.putExtra("tags", item.getVolumeInfo().getCategories());
            detailIntent.putExtra("bookmark", item.getPageRead());
            detailIntent.putExtra("otherrating", item.getVolumeInfo().getAverageRating());
            detailIntent.putExtra("myrating", item.getMyRating());
            detailIntent.putExtra("description", item.getVolumeInfo().getDescription());
            */context.startActivity(detailIntent);
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
            case R.id.add_book:
                Intent detailIntent = new Intent(context, AddBookActivity.class);
                context.startActivity(detailIntent);
                return true;
            case R.id.my_profile:
                detailIntent = new Intent(context, MyProfileActivity.class);
                //Toast.makeText(context, "MyProfile", Toast.LENGTH_LONG).show();
                context.startActivity(detailIntent);
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
}