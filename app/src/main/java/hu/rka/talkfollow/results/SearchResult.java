package hu.rka.talkfollow.results;

import java.util.ArrayList;

import hu.rka.talkfollow.models.Book;

/**
 * Created by Réka on 2016.01.25..
 */
public class SearchResult {

    String msg;
    ArrayList<Book> books;

    public ArrayList<Book> getItems() {
        return books;
    }

    public String getMsg() {
        return msg;
    }
}
