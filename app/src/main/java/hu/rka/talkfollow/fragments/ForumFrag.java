package hu.rka.talkfollow.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import hu.rka.talkfollow.Activities.TabMenuActivity;
import hu.rka.talkfollow.R;
import hu.rka.talkfollow.adapters.ForumAdapter;
import hu.rka.talkfollow.models.Book;
import hu.rka.talkfollow.models.ForumMessage;
import hu.rka.talkfollow.models.UploadForumMessage;
import hu.rka.talkfollow.models.UploadReport;
import hu.rka.talkfollow.models.UploadVote;
import hu.rka.talkfollow.network.ContentSpiceService;
import hu.rka.talkfollow.requests.GetForumMessagesRequest;
import hu.rka.talkfollow.requests.PostReportRequest;
import hu.rka.talkfollow.requests.PostVoteRequest;
import hu.rka.talkfollow.requests.PostWriteMessageRequest;
import hu.rka.talkfollow.results.ForumMessageResult;
import hu.rka.talkfollow.results.ReportResult;
import hu.rka.talkfollow.results.VoteResult;

/**
 * Created by Réka on 2016.01.10..
 */
public class ForumFrag extends android.support.v4.app.Fragment {

    @Bind(R.id.empty_view)
    TextView emptyView;

    @Bind(R.id.forum_list)
    ListView listView;

    ForumAdapter forumAdapter;
    Context context;
    TabMenuActivity activity;
    ArrayList<ForumMessage> messages;
    boolean onCreate = false;
    private SpiceManager spiceManager = new SpiceManager(ContentSpiceService.class);
    int message_id;
    Book bookDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_forum, container, false);
        ButterKnife.bind(this, v);
        activity = (TabMenuActivity) getActivity();
        context = getActivity();
        messages = activity.getMessages();
        forumAdapter = new ForumAdapter(getActivity(), 0, this);
        onCreate = true;
        bookDetail = activity.getBookDetails();
        setUI(messages);
        return v;
    }

    public void refreshUI(ArrayList<ForumMessage> messages) {
        setUI(messages);
    }

    private void setUI(final ArrayList<ForumMessage> messages) {
        if (messages != null) {
            if (onCreate) {
                forumAdapter.setForumMessages(messages);
                listView.setEmptyView(emptyView);
                listView.setAdapter(forumAdapter);
                listView.setSelection(forumAdapter.getCount() - 1);
            }
        }
    }

    public void upVote(int id) {
        messages = activity.getMessages();
        UploadVote uploadVote = new UploadVote(id, bookDetail.getMolyid(), "up");
        PostVoteRequest postVoteRequest = new PostVoteRequest(uploadVote);
        spiceManager.execute(postVoteRequest, new VoteRequestListener());
    }

    public final class VoteRequestListener implements
            RequestListener<VoteResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(VoteResult result) {
            if (result.getMsg().equals("")) {
                activity.setMessages(result.getForum_messages());
                setUI(result.getForum_messages());
            } else {
                Toast.makeText(context, getString(R.string.error) + result.getMsg(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void downVote(int id) {
        UploadVote uploadVote = new UploadVote(id, bookDetail.getMolyid(), "down");
        PostVoteRequest postVoteRequest = new PostVoteRequest(uploadVote);
        spiceManager.execute(postVoteRequest, new VoteRequestListener());
//        Toast.makeText(context, "DownVote", Toast.LENGTH_LONG).show();
    }

    public void report(final Context context1, final int id) {
        message_id = id;

        AlertDialog.Builder reportDialog = new AlertDialog.Builder(context);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_report, null);
        reportDialog.setView(dialogView);
        reportDialog.setTitle(R.string.report);

        final AlertDialog alertDialog = reportDialog.create();

        Button sendReport = (Button) dialogView.findViewById(R.id.send_report);
        sendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText reportText = (EditText) dialogView.findViewById(R.id.write_report_text);
                UploadReport uploadReport = new UploadReport(id, bookDetail.getMolyid(), String.valueOf(reportText.getText()));
                PostReportRequest postReportRequest = new PostReportRequest(uploadReport);
                spiceManager.execute(postReportRequest, new ReportRequestListener());
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = alertDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    public final class ReportRequestListener implements
            RequestListener<ReportResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(ReportResult result) {
            if (result.getMsg().equals("")) {
                Toast.makeText(context, R.string.report_sent, Toast.LENGTH_LONG).show();
                activity.setMessages(result.getForum_messages());
                setUI(result.getForum_messages());
            } else {
                Toast.makeText(context, getString(R.string.error) + result.getMsg(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_forum, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.write_message:


                AlertDialog.Builder writeMessageDialog = new AlertDialog.Builder(context);
// ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.dialog_write_message, null);
                writeMessageDialog.setView(dialogView);
                writeMessageDialog.setTitle(R.string.message);

                final AlertDialog alertDialog = writeMessageDialog.create();

                Button sendMessage = (Button) dialogView.findViewById(R.id.send_forum_message);
                sendMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText messageText = (EditText) dialogView.findViewById(R.id.write_forum_message_text);
                        if (String.valueOf(messageText.getText()).isEmpty()) {
                            Toast.makeText(context, R.string.you_can_not_post_empty_message, Toast.LENGTH_LONG).show();
                        } else {
                            UploadForumMessage uploadForumMessage = new UploadForumMessage(bookDetail.getMolyid(), String.valueOf(messageText.getText()));
                            PostWriteMessageRequest postWriteMessageRequest = new PostWriteMessageRequest(uploadForumMessage);
                            spiceManager.execute(postWriteMessageRequest, new WriteMessageRequestListener());
                            alertDialog.dismiss();
                        }
                    }
                });
                alertDialog.show();

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = alertDialog.getWindow();
                lp.copyFrom(window.getAttributes());
                //This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                return true;
            case R.id.refresh_messages:
                GetForumMessagesRequest getForumMessagesRequest = new GetForumMessagesRequest(bookDetail.getMolyid());
                spiceManager.execute(getForumMessagesRequest, new GetForumMessagesRequestListener());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public final class WriteMessageRequestListener implements
            RequestListener<ForumMessageResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(ForumMessageResult result) {
            if (result.getMsg().equals("")) {
                activity.setMessages(result.getForum_messages());
                setUI(result.getForum_messages());
            } else {
                Toast.makeText(context, getString(R.string.error) + result.getMsg(), Toast.LENGTH_LONG).show();
            }
        }
    }


    public final class GetForumMessagesRequestListener implements
            RequestListener<ForumMessageResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(ForumMessageResult result) {
            if (result.getMsg().equals("")) {
                activity.setMessages(result.getForum_messages());
                setUI(result.getForum_messages());
            } else {
                Toast.makeText(context, getString(R.string.error) + result.getMsg(), Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        spiceManager.start(context);
    }
}
