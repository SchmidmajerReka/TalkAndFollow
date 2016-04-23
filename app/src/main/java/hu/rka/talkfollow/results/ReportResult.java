package hu.rka.talkfollow.results;

import java.util.ArrayList;

import hu.rka.talkfollow.models.ForumMessage;

/**
 * Created by Réka on 2016.01.27..
 */
public class ReportResult {

    String msg = "";
    ArrayList<ForumMessage> forum_messages = new ArrayList<>();

    public String getMsg() {
        return msg;
    }

    public ArrayList<ForumMessage> getForum_messages() {
        return forum_messages;
    }
}
