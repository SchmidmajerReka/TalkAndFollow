package hu.rka.talkfollow.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import hu.rka.talkfollow.R;
import hu.rka.talkfollow.models.Readers;


/**
 * Created by Réka on 2016.01.09..
 */
public class ReaderAdapter extends ArrayAdapter<Readers> {
    ArrayList<Readers> readers = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public ReaderAdapter(Context context, int resource){
        super(context,resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getCount() {
        return readers.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.item_reader, null);
            ViewHolder viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) rowView.getTag();

        Readers reader = readers.get(position);
        holder.name.setText(reader.getName());
        long bookAdded = reader.getBook_added();
        String dateBookAdded = new SimpleDateFormat("dd/MM/yyyy").format(new Date(bookAdded));
        holder.readerTime.setText(dateBookAdded);
        holder.criticRating.setRating(reader.getRating());
        Picasso.with(context).load(reader.getUser_picture()).placeholder(R.drawable.profilepic).into(holder.readerProfile);

        return rowView;
    }

    public void setReaders(ArrayList<Readers> itemsArg) {
        readers = itemsArg;
        notifyDataSetChanged();
    }

    static class ViewHolder{
        @Bind(R.id.reader_name)
        TextView name;
        @Bind(R.id.reader_profile_pic)
        ImageView readerProfile;
        @Bind(R.id.reader_time) TextView readerTime;
        @Bind(R.id.reader_ratingBar)
        RatingBar criticRating;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public Readers getReaders(int position) {
        return readers.get(position);
    }
}
