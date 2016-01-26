package hu.rka.talkfollow.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import hu.rka.talkfollow.CriticDetailsActivity;
import hu.rka.talkfollow.R;
import hu.rka.talkfollow.models.Critic;
import hu.rka.talkfollow.models.ForumMessage;

/**
 * Created by Réka on 2016.01.10..
 */
public class ForumAdapter extends ArrayAdapter<ForumMessage> {
    ArrayList<ForumMessage> forumMessages = new ArrayList<ForumMessage>();
    LayoutInflater inflater;
    Context context;

    public ForumAdapter (Context context, int resource){
        super(context,resource);
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

        ForumMessage message = forumMessages.get(position);
        holder.forumUserName.setText(message.getUser_name());
        holder.messageText.setText(message.getMessage());
        holder.voteCount.setText(String.valueOf(message.getApproval_count()));
        holder.report.setOnClickListener(reportClick);
        holder.upVote.setOnClickListener(upClick);
        holder.downVote.setOnClickListener(downClick);
        Picasso.with(context).load(message.getUser_picture()).placeholder(R.drawable.profilepic).into(holder.forumPicture);
        return rowView;
    }

    public void setForumMessages(ArrayList<ForumMessage> itemsArg){
        forumMessages = itemsArg;
        notifyDataSetChanged();
    }

    private View.OnClickListener upClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Toast.makeText(context, "UpVote", Toast.LENGTH_LONG).show();
        }
    };

    private View.OnClickListener downClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Toast.makeText(context, "DownVote", Toast.LENGTH_LONG).show();
        }
    };

    private View.OnClickListener reportClick = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            final Dialog reportDialog = new Dialog(context);
            reportDialog.setContentView(R.layout.dialog_report);
            reportDialog.setTitle("Report: ");

            Button sendReport = (Button) reportDialog.findViewById(R.id.send_report);
            sendReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reportDialog.dismiss();
                    Toast.makeText(context, "Report sent", Toast.LENGTH_LONG).show();
                }
            });
            reportDialog.show();
        }
    };

    static class ViewHolder {
        @Bind(R.id.forum_user_name)
        TextView forumUserName;
        @Bind(R.id.forum_message) TextView messageText;
        @Bind(R.id.forum_count) TextView voteCount;
        @Bind(R.id.forum_report) TextView report;
        @Bind(R.id.forum_up) ImageView upVote;
        @Bind(R.id.forum_down) ImageView downVote;
        @Bind(R.id.forum_profile_pic) ImageView forumPicture;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }


    public ForumMessage getForumMessages(int position){
        return forumMessages.get(position);
    }
}

