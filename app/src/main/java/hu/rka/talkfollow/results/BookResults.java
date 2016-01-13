package hu.rka.talkfollow.results;

import java.util.ArrayList;

import hu.rka.talkfollow.models.Book;

/**
 * Created by Réka on 2016.01.13..
 */
public class BookResults {

    ArrayList<Book> books;

    public ArrayList<Book> getBooks(){
        return books;
    }

    public void setBooks(ArrayList<Book> books){
        this.books = books;
    }
}
