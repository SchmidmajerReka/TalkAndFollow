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
import hu.rka.talkfollow.models.Critic;

/**
 * Created by Réka on 2016.01.09..
 */
public class CriticAdapter extends ArrayAdapter<Critic> {
    ArrayList<Critic> critics = new ArrayList<>();
    LayoutInflater inflater;

    public  CriticAdapter(Context context, int resource){
        super(context,resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return critics.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.item_critic, null);
            ViewHolder viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) rowView.getTag();

        Critic item = critics.get(position);
        holder.criticAuthor.setText(item.getAuthor());
        holder.criticTitle.setText(item.getTitle());
        holder.criticText.setText(item.getCriticText());
        holder.criticRating.setRating(item.getRate());
        holder.criticTime.setText(item.getUpdatedTime());


        return rowView;
    }

    public void setCritic(ArrayList<Critic> itemsArg) {
        critics = itemsArg;
        notifyDataSetChanged();
    }

    static class ViewHolder{
        @Bind(R.id.critic_author) TextView criticAuthor;
        @Bind(R.id.critic_profile_pic)
        ImageView criticProfile;
        @Bind(R.id.critic_text) TextView criticText;
        @Bind(R.id.critic_title) TextView criticTitle;
        @Bind(R.id.critic_ratingBar)
        RatingBar criticRating;
        @Bind(R.id.critic_time) TextView criticTime;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public Critic getCritic(int position) {
        return critics.get(position);
    }
}

