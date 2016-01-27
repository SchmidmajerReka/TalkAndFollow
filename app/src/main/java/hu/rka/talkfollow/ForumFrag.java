package hu.rka.talkfollow;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;
import hu.rka.talkfollow.adapters.ForumAdapter;
import hu.rka.talkfollow.models.ForumMessage;

/**
 * Created by Réka on 2016.01.10..
 */
public class ForumFrag extends android.support.v4.app.Fragment {

        ListView listView;
        ForumAdapter forumAdapter;
        Context context;
        TabMenuActivity activity;
        ArrayList<ForumMessage> messages;
        boolean onCreate = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_forum, container, false);
            listView = (ListView) v.findViewById(R.id.forum_list);
            activity = (TabMenuActivity) getActivity();
            context = getActivity();
            messages = activity.getMessages();
            forumAdapter = new ForumAdapter(getActivity(), 0);
            onCreate = true;
            setUI(messages);
            return v;
    }

    public void refreshUI(ArrayList<ForumMessage> messages) {
        setUI(messages);
    }

    private void setUI(final ArrayList<ForumMessage> messages) {
        if(messages != null) {
            if(onCreate){
                forumAdapter.setForumMessages(messages);
                listView.setAdapter(forumAdapter);

            }
        }
    }

    public void upVote(Context context, int id){
        messages = activity.getMessages();
        int count = messages.get(id).getApproval_count();
        messages.get(id).setApproval_count(count + 1);
        this.setUI(messages);
        Toast.makeText(context, "UpVote", Toast.LENGTH_LONG).show();
    }

    public void downVote(Context context){
        Toast.makeText(context, "DownVote", Toast.LENGTH_LONG).show();
    }

    public void report(final Context context1){
        final Dialog reportDialog = new Dialog(context1);
        reportDialog.setContentView(R.layout.dialog_report);
        reportDialog.setTitle("Report: ");

        Button sendReport = (Button) reportDialog.findViewById(R.id.send_report);
        sendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportDialog.dismiss();
                Toast.makeText(context1, "Report sent", Toast.LENGTH_LONG).show();
            }
        });
        reportDialog.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_forum, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
            case R.id.write_message:
                final Dialog writeMessageDialog = new Dialog(context);
                writeMessageDialog.setContentView(R.layout.dialog_write_message);
                writeMessageDialog.setTitle("Message: ");
                Button sendMessage = (Button) writeMessageDialog.findViewById(R.id.send_forum_message);
                sendMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Message sent", Toast.LENGTH_LONG).show();
                        writeMessageDialog.dismiss();
                    }
                });
                writeMessageDialog.show();
            return true;
    default:
            return super.onOptionsItemSelected(item);
            }
    }
}
