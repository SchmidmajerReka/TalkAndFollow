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

import java.util.ArrayList;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import hu.rka.talkfollow.adapters.BookAdapter;
import hu.rka.talkfollow.models.Book;

/**
 * Created by Réka on 2016.01.09..
 */
public class AddBookActivity extends AppCompatActivity {
    BookAdapter bookAdapter;
    private Context context;
    @Bind(R.id.offer_book_list)
    ListView offerBookList;
    Random rand = new Random();

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

        ArrayList<Book> items = new ArrayList<>();
        for (int i = 25; i < 40; i++) {
            Book item = new Book();
            item.setTitle("Title: " + (100 - i));
            item.setAuthor("Author: " + (100 - i));
            item.setUrl("something");
            item.setIsbn("123456789");
            item.setGenre("Romantic, Sci-fi, Crimi");
            item.setPageNum(100 + i);
            int randomnumber = rand.nextInt(item.getPageNum())%+1;
            item.setPageRead(randomnumber);
            item.setOtherRating(4);
            item.setMyRating(5);
            item.setDescription("If you can't explain it simply you don't understand it well enough");
            items.add(item);
        }

        boolean showChat = false;
        bookAdapter.setBook(items, showChat);
        offerBookList.setAdapter(bookAdapter);
        offerBookList.setOnItemClickListener(listItemClick);
        //kell majd egy on item click listener, hogy a detailviewval össze legyen kötve

    }


    AdapterView.OnItemClickListener listItemClick = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Book item = bookAdapter.getBook(position);
            Intent detailIntent = new Intent(context, TabMenuActivity.class);
            detailIntent.putExtra("added" ,false);
            detailIntent.putExtra("title", item.getTitle());
            detailIntent.putExtra("author", item.getAuthor());
            detailIntent.putExtra("isbn", item.getIsbn());
            detailIntent.putExtra("genre", item.getGenre());
            detailIntent.putExtra("pagenum", item.getPageNum());
            detailIntent.putExtra("pageread", item.getPageRead());
            detailIntent.putExtra("otherrating", item.getOtherRating());
            detailIntent.putExtra("myrating", item.getMyRating());
            detailIntent.putExtra("description", item.getDescription());
            context.startActivity(detailIntent);
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.search:
                Intent detailIntent = new Intent(context, SearchActivity.class);
                context.startActivity(detailIntent);
                //Toast.makeText(context, "Search book", Toast.LENGTH_LONG).show();
                return true;
            case R.id.barcode:
                //detailIntent = new Intent(context, MyProfileActivity.class);
                //detailIntent.putExtra("booknum", bookAdapter.getCount() );
                //detailIntent.putExtra("finished", bookAdapter.getfinished());
                //context.startActivity(detailIntent);
                Toast.makeText(context, "Scan barcode", Toast.LENGTH_LONG).show();
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

