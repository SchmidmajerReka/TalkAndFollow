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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_forum, container, false);
            ButterKnife.bind(this, v);
            listView = (ListView) v.findViewById(R.id.forum_list);
            context = getActivity();
            TabMenuActivity activity = (TabMenuActivity) getActivity();
            forumAdapter = new ForumAdapter(getActivity(), 0);
            ArrayList<ForumMessage> items = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
            ForumMessage item = new ForumMessage();
            item.setAuthor("Author " + i);
            item.setMessageText("If you can't explain it quickly you don't understand it well enough. If you can't explain it quickly you don't understand it well enough. If you can't explain it quickly you don't understand it well enough. If you can't explain it quickly you don't understand it well enough.");
            item.setVoteCount(15);
            items.add(item);
            }

            forumAdapter.setForumMessages(items);
            listView.setAdapter(forumAdapter);
            return v;
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
