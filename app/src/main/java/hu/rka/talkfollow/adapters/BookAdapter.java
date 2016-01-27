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

        Book book = books.get(position);

        Picasso.with(context).load(book.getPicture()).placeholder(R.drawable.profilepic).into(holder.cover);

        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthors());
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
            Book book = books.get(position);

            Intent detailIntent = new Intent(context, TabMenuActivity.class);
            detailIntent.putExtra("molyid", book.getMolyid());
            detailIntent.putExtra("added" ,true);
            detailIntent.putExtra("title", book.getTitle());
            detailIntent.putExtra("author", book.getAuthors());
            detailIntent.putExtra("starter", 3);
            context.startActivity(detailIntent);
        }
    };

    public void setBook(ArrayList<Book> itemsArg, boolean flag) {
        books = itemsArg;
        this.flag=flag;
        notifyDataSetChanged();
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
                int res = String.CASE_INSENSITIVE_ORDER.compare(lhs.getAuthors(), rhs.getAuthors());
                return res;
            }
        });
        notifyDataSetChanged();
    }

    public void orderTitle(){
        Collections.sort(books, new Comparator<Book>() {
            @Override
            public int compare(Book lhs, Book rhs) {
                int res = String.CASE_INSENSITIVE_ORDER.compare(lhs.getTitle(), rhs.getTitle());
                return res;
            }
        });
        notifyDataSetChanged();
    }

    public Book getBook(int position) {
        return
                books.get(position);
    }
}
