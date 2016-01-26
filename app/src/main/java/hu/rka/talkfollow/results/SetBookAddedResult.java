package hu.rka.talkfollow.results;

import java.util.ArrayList;

import hu.rka.talkfollow.models.Book;
import hu.rka.talkfollow.models.Critic;
import hu.rka.talkfollow.models.ForumMessage;
import hu.rka.talkfollow.models.Readers;

/**
 * Created by Réka on 2016.01.26..
 */
public class SetBookAddedResult {
    String msg;

    Book book_details;
    ArrayList<Critic> critics;
    ArrayList<Readers> readers;
    ArrayList<ForumMessage> forum_messages;


    public String getMsg() {
        return msg;
    }

    public Book getBook_details() {
        return book_details;
    }

    public ArrayList<Critic> getCritics() {
        return critics;
    }

    public ArrayList<Readers> getReaders() {
        return readers;
    }

    public ArrayList<ForumMessage> getForum_messages() {
        return forum_messages;
    }

}
