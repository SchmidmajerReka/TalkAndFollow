package hu.rka.talkfollow.models;

/**
 * Created by Réka on 2016.01.25..
 */
public class UploadBookRead {

    int book_id;
    boolean finished;

    public UploadBookRead(int book_id, boolean read) {
        this.book_id = book_id;
        this.finished = read;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public void setRead(boolean read) {
        this.finished = read;
    }
}
