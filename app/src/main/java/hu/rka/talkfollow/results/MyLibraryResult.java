package hu.rka.talkfollow.results;

import java.util.ArrayList;

import hu.rka.talkfollow.models.Book;

/**
 * Created by Réka on 2016.01.13..
 */
public class MyLibraryResult {

    String error;
    ArrayList<Book> books;

    public String getError() {
        return error;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

}
