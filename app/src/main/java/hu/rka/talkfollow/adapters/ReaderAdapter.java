package hu.rka.talkfollow.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import java.util.ArrayList;

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

    public ReaderAdapter(Context context, int resource){
        super(context,resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        Readers item = readers.get(position);
        holder.name.setText(item.getName());
        holder.readerTime.setText(item.getDate());
        holder.criticRating.setRating(item.getRate());
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
        ImageView criticProfile;
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
