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
    Context context;

    public  CriticAdapter(Context context, int resource){
        super(context,resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
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

        Critic critic = critics.get(position);
        holder.criticAuthor.setText(critic.getUser_name());
        holder.criticTitle.setText(critic.getTitle());
        holder.criticText.setText(critic.getCritic());
        holder.criticRating.setRating(critic.getRating());
        if(critic.getTime()!=null){
            holder.criticTime.setText(critic.getTime());
        }else{
            holder.criticTime.setText("");
        }
        Picasso.with(context).load(critic.getUser_picture()).placeholder(R.drawable.profilepic).into(holder.criticProfile);

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

