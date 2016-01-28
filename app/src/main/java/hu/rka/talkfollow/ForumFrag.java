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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;

import butterknife.ButterKnife;
import hu.rka.talkfollow.adapters.ForumAdapter;
import hu.rka.talkfollow.models.Book;
import hu.rka.talkfollow.models.ForumMessage;
import hu.rka.talkfollow.models.UploadForumMessage;
import hu.rka.talkfollow.models.UploadReport;
import hu.rka.talkfollow.models.UploadVote;
import hu.rka.talkfollow.network.ContentSpiceService;
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

    ListView listView;
    ForumAdapter forumAdapter;
    Context context;
    TabMenuActivity activity;
    ArrayList<ForumMessage> messages;
    boolean onCreate = false;
    private SpiceManager spiceManager = new SpiceManager(ContentSpiceService.class);
    int message_id;
    int addToCount;
    Book bookDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_forum, container, false);
            listView = (ListView) v.findViewById(R.id.forum_list);
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
        if(messages != null) {
            if(onCreate){
                forumAdapter.setForumMessages(messages);
                listView.setAdapter(forumAdapter);

            }
        }
    }

    public void upVote(int id){
        messages = activity.getMessages();
        message_id = id;
        addToCount = 1;
        UploadVote uploadVote = new UploadVote(messages.get(id).getId(), bookDetail.getMolyid() , "up");
        PostVoteRequest postVoteRequest = new PostVoteRequest(uploadVote);
        spiceManager.execute(postVoteRequest, new VoteRequestListener());
    }

    public final class VoteRequestListener implements
            RequestListener<VoteResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, "Hiba történt!!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(VoteResult result) {
            if(result.getMsg().equals("")){
                messages = activity.getMessages();
                int count = messages.get(message_id).getApproval_count();
                messages.get(message_id).setApproval_count(count + addToCount);
                setUI(messages);
            }else{
                Toast.makeText(context, "Error: " + result.getMsg(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void downVote(int id){
        messages = activity.getMessages();
        message_id = id;
        addToCount = -1;
        UploadVote uploadVote = new UploadVote(messages.get(id).getId(),bookDetail.getMolyid(), "down");
        PostVoteRequest postVoteRequest = new PostVoteRequest(uploadVote);
        spiceManager.execute(postVoteRequest, new VoteRequestListener());
        Toast.makeText(context, "DownVote", Toast.LENGTH_LONG).show();
    }

    public void report(final Context context1, final int id){
        message_id = id;
        final Dialog reportDialog = new Dialog(context1);
        reportDialog.setContentView(R.layout.dialog_report);
        reportDialog.setTitle("Report: ");

        Button sendReport = (Button) reportDialog.findViewById(R.id.send_report);
        sendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportDialog.dismiss();
                EditText reportText = (EditText) reportDialog.findViewById(R.id.write_report_text);
                UploadReport uploadReport = new UploadReport(id, bookDetail.getMolyid(), String.valueOf(reportText.getText()));
                PostReportRequest postReportRequest = new PostReportRequest(uploadReport);
                spiceManager.execute(postReportRequest, new ReportRequestListener());
            }
        });
        reportDialog.show();
    }

    public final class ReportRequestListener implements
            RequestListener<ReportResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, "Hiba történt!!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(ReportResult result) {
            if(result.getMsg().equals("")){
                int count = messages.get(message_id).getApproval_count();
                messages.get(message_id).setApproval_count(count - 5);
                Toast.makeText(context, "Report sent", Toast.LENGTH_LONG).show();
                setUI(messages);
            }else{
                Toast.makeText(context, "Error: " + result.getMsg(), Toast.LENGTH_LONG).show();
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
                        EditText messageText = (EditText) writeMessageDialog.findViewById(R.id.write_forum_message_text);
                        if(String.valueOf(messageText.getText()).isEmpty()){
                            Toast.makeText(context, "You can't post an empty message", Toast.LENGTH_LONG).show();
                        }else{
                            UploadForumMessage uploadForumMessage = new UploadForumMessage(bookDetail.getMolyid(), String.valueOf(messageText.getText()));
                            PostWriteMessageRequest postWriteMessageRequest = new PostWriteMessageRequest(uploadForumMessage);
                            spiceManager.execute(postWriteMessageRequest, new WriteMessageRequestListener());
                            writeMessageDialog.dismiss();
                        }
                    }
                });
                writeMessageDialog.show();
            return true;
    default:
            return super.onOptionsItemSelected(item);
            }
    }

    public final class WriteMessageRequestListener implements
            RequestListener<ForumMessageResult> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, "Hiba történt!!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(ForumMessageResult result) {
            if(result.getMsg().equals("")){
                int count = messages.get(message_id).getApproval_count();
                ForumMessage forumMessage = new ForumMessage();
                forumMessage.setId(3);
                forumMessage.setApproval_count(10);
                forumMessage.setUser_name("András");
                forumMessage.setMessage("Ez egy példa");
                messages.add(forumMessage);
                Toast.makeText(context, "Message sent", Toast.LENGTH_LONG).show();
                setUI(messages);
            }else{
                Toast.makeText(context, "Error: " + result.getMsg(), Toast.LENGTH_LONG).show();
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
