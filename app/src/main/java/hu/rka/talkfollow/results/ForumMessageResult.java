package hu.rka.talkfollow.results;

import java.util.ArrayList;

import hu.rka.talkfollow.models.ForumMessage;

/**
 * Created by Réka on 2016.01.27..
 */
public class ForumMessageResult {

    String msg = "";

    ArrayList<ForumMessage> forum_messages = new ArrayList<>();

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<ForumMessage> getForum_messages() {
        return forum_messages;
    }

    public void setForum_messages(ArrayList<ForumMessage> forum_messages) {
        this.forum_messages = forum_messages;
    }
}
