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

import java.util.ArrayList;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import hu.rka.talkfollow.adapters.BookAdapter;
import hu.rka.talkfollow.models.Book;
import hu.rka.talkfollow.models.IndustryIdentifiers;
import hu.rka.talkfollow.models.VolumeInfo;

/**
 * Created by Réka on 2016.01.08..
 */
public class MyLibraryActivity extends AppCompatActivity {
    BookAdapter bookAdapter;
    private Context context;
    @Bind(R.id.my_book_list)
    ListView bookList;
    Random rand = new Random();

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

        ArrayList<Book> items = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
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
        boolean showChat=true;
        bookAdapter.setBook(items, showChat);
        bookList.setAdapter(bookAdapter);
        bookList.setOnItemClickListener(listItemClick);

    }

    AdapterView.OnItemClickListener listItemClick = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Book item = bookAdapter.getBook(position);
            Intent detailIntent = new Intent(context, TabMenuActivity.class);
            detailIntent.putExtra("added" ,true);
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
                detailIntent.putExtra("booknum", bookAdapter.getCount() );
                detailIntent.putExtra("finished", bookAdapter.getfinished());
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