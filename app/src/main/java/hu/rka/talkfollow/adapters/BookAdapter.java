package hu.rka.talkfollow.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import butterknife.Bind;
import butterknife.ButterKnife;
import hu.rka.talkfollow.R;
import hu.rka.talkfollow.TabMenuActivity;
import hu.rka.talkfollow.models.Book;

/**
 * Created by Réka on 2016.01.08..
 */
public class BookAdapter extends ArrayAdapter<Book> {


    ArrayList<Book> books = new ArrayList<>();
    boolean flag;
    LayoutInflater inflater;
    private Context context;

    public BookAdapter(Context c, int resource) {
        super(c, resource);
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        context=c;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.item_book, null);
            ViewHolder viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();

        Book item = books.get(position);

        //Picasso.with(context).load(item.getUrl()).into(holder.icon);
        holder.title.setText(item.getTitle());
        holder.author.setText(item.getAuthor());
        if(flag == true){
            holder.forumIcon.setVisibility(View.VISIBLE);
            holder.forumIcon.setTag(position);
            holder.forumIcon.setOnClickListener(iconClick);
        }else{
            holder.forumIcon.setVisibility(View.INVISIBLE);
        }
        return rowView;
    }

    private View.OnClickListener iconClick=new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            int position=(Integer)v.getTag();
            Book item = books.get(position);
            Intent detailIntent = new Intent(context, TabMenuActivity.class);
            detailIntent.putExtra("added" ,true);
            detailIntent.putExtra("starter", 3);
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

            //Toast.makeText(context, "Icon position: " + position, Toast.LENGTH_LONG).show();
        }
    };

    public void setBook(ArrayList<Book> itemsArg, boolean flag) {
        books = itemsArg;
        this.flag=flag;
        notifyDataSetChanged();
    }

    public int getfinished(){
        int finished=0;
        for(int i=0; i<books.size(); i++){
            if(books.get(i).getPageRead() == books.get(i).getPageNum()){
                finished++;
            }
        }
        return finished;
    }
    static class ViewHolder {
        @Bind(R.id.book_author)
        TextView author;
        @Bind(R.id.book_cover)
        ImageView cover;
        @Bind(R.id.book_title) TextView title;
        @Bind(R.id.forum_icon) ImageView forumIcon;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void orderAuthor(){
        Collections.sort(books, new Comparator<Book>() {
            @Override
            public int compare(Book lhs, Book rhs) {
                int res = String.CASE_INSENSITIVE_ORDER.compare(lhs.getAuthor(), rhs.getAuthor());
                return res;
                // /return lhs.getAuthor().compareToIgnoreCase(rhs.getAuthor());
            }
        });
        notifyDataSetChanged();
    }

    public void orderTitle(){
        Collections.sort(books, new Comparator<Book>() {
            @Override
            public int compare(Book lhs, Book rhs) {
                int res = String.CASE_INSENSITIVE_ORDER.compare(lhs.getAuthor(), rhs.getAuthor());
                return res;
                // /return lhs.getAuthor().compareToIgnoreCase(rhs.getAuthor());
            }
        });
        notifyDataSetChanged();
    }

    public Book getBook(int position) {
        // Hibakezelés, milyen hiba lehet?
        return
                books.get(position);
    }
}
