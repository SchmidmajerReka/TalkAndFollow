package hu.rka.talkfollow.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import hu.rka.talkfollow.fragments.ForumFrag;
import hu.rka.talkfollow.R;
import hu.rka.talkfollow.models.ForumMessage;

/**
 * Created by Réka on 2016.01.10..
 */
public class ForumAdapter extends ArrayAdapter<ForumMessage> {

    ArrayList<ForumMessage> forumMessages = new ArrayList<ForumMessage>();
    LayoutInflater inflater;
    Context context;
    ForumFrag forumFrag;
    ForumMessage message;

    public ForumAdapter(Context context, int resource, ForumFrag forumFrag) {
        super(context, resource);
        this.forumFrag = forumFrag;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return forumMessages.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.item_forum, null);
            ViewHolder viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) rowView.getTag();

        message = forumMessages.get(position);

        if (position % 2 == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.messageLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_light));
                holder.forumUserName.setTextColor(context.getResources().getColor(R.color.comment_background_dark));
                holder.messageText.setTextColor(context.getResources().getColor(R.color.comment_background_dark));
                holder.voteCount.setTextColor(context.getResources().getColor(R.color.comment_background_dark));
                holder.report.setTextColor(context.getResources().getColor(R.color.comment_background_dark));
                holder.downVote.setImageResource(R.drawable.ic_thumb_down_blue_36dp);
                holder.upVote.setImageResource(R.drawable.ic_thumb_up_blue_36dp);
            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.messageLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_dark));
                holder.forumUserName.setTextColor(context.getResources().getColor(R.color.white_text_color));
                holder.messageText.setTextColor(context.getResources().getColor(R.color.white_text_color));
                holder.voteCount.setTextColor(context.getResources().getColor(R.color.white_text_color));
                holder.report.setTextColor(context.getResources().getColor(R.color.white_text_color));
                holder.upVote.setImageResource(R.drawable.ic_thumb_up_white_36dp);
                holder.downVote.setImageResource(R.drawable.ic_thumb_down_white_36dp);
            }
        }

        holder.forumUserName.setText(message.getUser_name());
        holder.messageText.setText(message.getMessage());
        holder.voteCount.setText(String.valueOf(message.getApproval_count()));
        holder.report.setTag(message.getId());
        holder.report.setOnClickListener(reportClick);
        holder.upVote.setTag(message.getId());
        holder.upVote.setOnClickListener(upClick);
        holder.downVote.setTag(message.getId());
        holder.downVote.setOnClickListener(downClick);
        Picasso.with(context).load(message.getUser_picture()).placeholder(R.drawable.profilepic).into(holder.forumPicture);

        return rowView;
    }

    public void setForumMessages(ArrayList<ForumMessage> itemsArg) {
        forumMessages = itemsArg;
        notifyDataSetChanged();
    }

    private View.OnClickListener upClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int selected_id = (Integer) v.getTag();
            forumFrag.upVote(selected_id);
        }
    };

    private View.OnClickListener downClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int selected_id = (Integer) v.getTag();
            forumFrag.downVote(selected_id);
        }
    };

    private View.OnClickListener reportClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int selected_id = (Integer) v.getTag();
            forumFrag.report(context, selected_id);
        }
    };


    static class ViewHolder {

        @Bind(R.id.forum_user_name)
        TextView forumUserName;
        @Bind(R.id.forum_message)
        TextView messageText;
        @Bind(R.id.forum_count)
        TextView voteCount;
        @Bind(R.id.forum_report)
        TextView report;
        @Bind(R.id.forum_up)
        ImageView upVote;
        @Bind(R.id.forum_down)
        ImageView downVote;
        @Bind(R.id.forum_profile_pic)
        ImageView forumPicture;
        @Bind(R.id.forum_message_layout)
        LinearLayout messageLayout;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    public ForumMessage getForumMessages(int position) {
        return forumMessages.get(position);
    }
}

